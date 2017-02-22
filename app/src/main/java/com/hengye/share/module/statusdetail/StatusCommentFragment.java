package com.hengye.share.module.statusdetail;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.hengye.floatingactionbutton.FloatingActionsMenu;
import com.hengye.share.R;
import com.hengye.share.model.Status;
import com.hengye.share.model.StatusComment;
import com.hengye.share.model.StatusComments;
import com.hengye.share.model.greenrobot.StatusDraftHelper;
import com.hengye.share.module.publish.StatusPublishActivity;
import com.hengye.share.module.status.ShareLoadDataCallbackFragment;
import com.hengye.share.module.status.StatusTitleViewHolder;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.ui.widget.fab.FabAnimator;
import com.hengye.share.ui.widget.recyclerview.DividerItemDecoration;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.handler.StatusAdapterIdPager;
import com.hengye.share.util.handler.StatusRefreshIdHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class StatusCommentFragment extends ShareLoadDataCallbackFragment<StatusComment> implements StatusCommentContract.View, DialogInterface.OnClickListener {

    public static Bundle getStartBundle(Status topic, boolean isComment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("status", topic);
        bundle.putBoolean("isComment", isComment);
        return bundle;
    }

    public static StatusCommentFragment newInstance(Status topic, boolean isComment) {
        StatusCommentFragment fragment = new StatusCommentFragment();
        fragment.setArguments(getStartBundle(topic, isComment));
        return fragment;
    }

    StatusCommentAdapter mAdapter;
    StatusCommentContract.Presenter mPresenter;
    Status mStatus;
    boolean mIsComment;
    Dialog mStatusCommentDialog;
    int mCurrentPosition;
    TabLayout.Tab mTab;

    StatusAdapterIdPager mStatusPager;

    @Override
    public int getLoadingResId() {
        return R.layout.state_loading_top;
    }

    @Override
    public int getEmptyResId() {
        return R.layout.state_empty_top;
    }

    @Override
    public int getNoNetworkResId() {
        return R.layout.state_no_network_top;
    }

    @Override
    public int getServiceErrorResId() {
        return R.layout.state_service_error_top;
    }


    @Override
    protected void handleBundleExtra(Bundle bundle) {
        mStatus = (Status) bundle.getSerializable("status");
        mIsComment = bundle.getBoolean("isComment");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getRecyclerView().addItemDecoration(new DividerItemDecoration(getContext()));
        boolean isLikeMode = mIsComment && !UserUtil.isAdTokenEmpty();
        setAdapter(mAdapter = new StatusCommentAdapter(getContext(), new ArrayList<StatusComment>(), isLikeMode), true);
        setPager(mStatusPager = new StatusAdapterIdPager(mAdapter));
        setDataHandler(new StatusRefreshIdHandler<>(mAdapter));
        mStatusPager.setForceRefresh(true);
        mPresenter = new StatusCommentPresenter(this, isLikeMode);

        initView();
        showLoading();

        markLazyLoadPreparedAndLazyLoadIfCan();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(StatusComment.StatusCommentEvent event) {
        boolean isComment = event.isComment();
        String targetStatusCommentId = event.getTargetStatusId();
        if(mStatus.getId() != null && mStatus.getId().equals(targetStatusCommentId) && isComment == mIsComment){
            StatusComment topicComment = event.getStatusComment();
            if(!isShowLoading()){
                //加载完的情况下才添加刷新
                mAdapter.addItem(0, topicComment);
                showContent();
            }
        }
    }

    @Override
    protected void onLazyLoad() {
        if(isShowLoading()) {
            //如果调用了下拉刷新就没必要再懒加载了
            onRefresh();
        }
    }

    private void initView() {
        mStatusCommentDialog = mIsComment ? DialogBuilder.getStatusCommentDialog(getContext(), this) : DialogBuilder.getStatusRepostDialog(getContext(), this);
        setRefreshEnable(false);
        final TabLayout tabLayout = (TabLayout) getActivity().findViewById(R.id.tab);
        if (tabLayout != null) {
            mTab = tabLayout.getTabAt(mIsComment ? 0 : 1);
        }

        final FloatingActionsMenu actionsMenu = (FloatingActionsMenu) getActivity().findViewById(R.id.fab);
        if (actionsMenu != null) {
            FabAnimator
                    .create(actionsMenu)
                    .attachToRecyclerView(getRecyclerView())
                    .setCustomAnimator(new FabAnimator.CustomAnimator() {
                        @Override
                        public int getViewHeight() {
                            return actionsMenu.getAddButtion().getHeight();
                        }
                    });
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);
        performItemClick(view, position, false);
    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        super.onItemLongClick(view, position);
        int viewType = mAdapter.getBasicItemType(position);
        if(viewType == R.layout.item_status_comment) {
            performItemClick(view, position, true);
            return true;
        }
        return false;
    }

    private void performItemClick(final View view, final int position, boolean isLongClick){
        int viewType = mAdapter.getBasicItemType(position);

        final int id = view.getId();
        if (viewType == R.layout.item_status_comment_hot_label) {
            //热门评论
            StatusHotCommentFragment.start(getContext(), mStatus);
        } else {
            if (StatusTitleViewHolder.isClickStatusTitle(id)) {
                //点击头像标题
                StatusTitleViewHolder.onClickStatusTitle(getActivity(), mAdapter, view, position, mAdapter.getItem(position).getUserInfo());
            } else if (id == R.id.layout_attitude) {
                //点赞
                StatusComment topicComment = mAdapter.getItem(position);
                mPresenter.likeComment(topicComment);
                topicComment.updateLiked(!topicComment.isLiked());
                mAdapter.updateItem(position);
            } else if (isLongClick || id != R.id.item_status_total) {
                //其他部位
                // 为了点击头像有item的波纹效果，点击头像等区域的时候会触发item的触摸事件(触发两次click)
                // 为了防止同时显示对话框和点击头像，判断id不等于item的id时才显示对话框，如果是长按就不用判断
                mCurrentPosition = position;
                mStatusCommentDialog.show();
            }
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.loadWBCommentOrRepost(mStatus.getId(), mStatusPager.getFirstPage(), true, mIsComment);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        mPresenter.loadWBCommentOrRepost(mStatus.getId(), mStatusPager.getNextPage(), false, mIsComment);
    }

    @Override
    public void onLoadStatusComments(StatusComments statusComments) {
        if (mTab != null) {
            String str = ResUtil.getString(mIsComment ?
                    R.string.label_status_comment_number : R.string.label_status_repost_number,
                    DataUtil.getCounter(statusComments.getTotalNumber()));
            mTab.setText(str);
        }
    }

    @Override
    public void onStatusCommentLike(StatusComment statusComment, int taskState) {
        if (!TaskState.isSuccess(taskState)) {
            statusComment.updateLiked(!statusComment.isLiked());
            mAdapter.notifyDataSetChanged();
            ToastUtil.showToast(TaskState.toString(taskState));
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        StatusComment topicComment = mAdapter.getItem(mCurrentPosition);
        if (topicComment == null) {
            return;
        }
        if (mIsComment) {
            switch (which) {
                case DialogBuilder.COMMENT_COPY:
                    ClipboardUtil.copyWBContent(topicComment.getContent());
                    break;
                case DialogBuilder.COMMENT_REPLY:
                    startActivity(StatusPublishActivity.getStartIntent(getContext(), StatusDraftHelper.getWBStatusDraftByCommentReply(mStatus, topicComment)));
                    break;
                case DialogBuilder.COMMENT_REPOST:
                    startActivity(StatusPublishActivity.getStartIntent(getContext(), StatusDraftHelper.getWBStatusDraftByCommentRepost(mStatus, topicComment)));
                    break;
            }

        } else {
            switch (which) {
                case DialogBuilder.REPOST_COPY:
                    ClipboardUtil.copyWBContent(topicComment.getContent());
                    break;
                case DialogBuilder.REPOST_DETAIL:
                    startActivity(StatusDetail2Activity.getStartIntent(getContext(), topicComment.toStatus(), true));
                    break;
                case DialogBuilder.REPOST_REPOST:
                    startActivity(StatusPublishActivity.getStartIntent(getContext(), StatusDraftHelper.getWBStatusDraftByRepostRepost(topicComment)));
                    break;
                case DialogBuilder.REPOST_COMMENT:
                    startActivity(StatusPublishActivity.getStartIntent(getContext(), StatusDraftHelper.getWBStatusDraftByRepostComment(topicComment)));
                    break;
            }

        }

    }
}
