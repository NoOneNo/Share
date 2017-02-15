package com.hengye.share.module.topicdetail;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.hengye.floatingactionbutton.FloatingActionsMenu;
import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.model.TopicComment;
import com.hengye.share.model.TopicComments;
import com.hengye.share.model.greenrobot.TopicDraftHelper;
import com.hengye.share.module.publish.TopicPublishActivity;
import com.hengye.share.module.topic.ShareLoadDataCallbackFragment;
import com.hengye.share.module.topic.TopicTitleViewHolder;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.ui.widget.fab.FabAnimator;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.handler.TopicAdapterIdPager;
import com.hengye.share.util.handler.TopicRefreshIdHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

public class TopicCommentFragment extends ShareLoadDataCallbackFragment<TopicComment> implements TopicCommentContract.View, DialogInterface.OnClickListener {

    public static Bundle getStartBundle(Topic topic, boolean isComment) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("topic", topic);
        bundle.putBoolean("isComment", isComment);
        return bundle;
    }

    public static TopicCommentFragment newInstance(Topic topic, boolean isComment) {
        TopicCommentFragment fragment = new TopicCommentFragment();
        fragment.setArguments(getStartBundle(topic, isComment));
        return fragment;
    }

    TopicCommentAdapter mAdapter;
    TopicCommentContract.Presenter mPresenter;
    Topic mTopic;
    boolean mIsComment;
    Dialog mTopicCommentDialog;
    int mCurrentPosition;
    TabLayout.Tab mTab;

    TopicAdapterIdPager mTopicPager;

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
        mTopic = (Topic) bundle.getSerializable("topic");
        mIsComment = bundle.getBoolean("isComment");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        boolean isLikeMode = mIsComment && !UserUtil.isAdTokenEmpty();
        setAdapter(mAdapter = new TopicCommentAdapter(getContext(), new ArrayList<TopicComment>(), isLikeMode), true);
        setPager(mTopicPager = new TopicAdapterIdPager(mAdapter));
        setDataHandler(new TopicRefreshIdHandler<>(mAdapter));
        mTopicPager.setForceRefresh(true);
        mPresenter = new TopicCommentPresenter(this, isLikeMode);

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
    public void onMessageEvent(TopicComment.TopicCommentEvent event) {
        boolean isComment = event.isComment();
        String targetTopicCommentId = event.getTargetTopicId();
        if(mTopic.getId() != null && mTopic.getId().equals(targetTopicCommentId) && isComment == mIsComment){
            TopicComment topicComment = event.getTopicComment();
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
        mTopicCommentDialog = mIsComment ? DialogBuilder.getTopicCommentDialog(getContext(), this) : DialogBuilder.getTopicRepostDialog(getContext(), this);
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
        if(viewType == R.layout.item_topic_comment) {
            performItemClick(view, position, true);
            return true;
        }
        return false;
    }

    private void performItemClick(final View view, final int position, boolean isLongClick){
        int viewType = mAdapter.getBasicItemType(position);

        final int id = view.getId();
        if (viewType == R.layout.item_topic_comment_hot_label) {
            //热门评论
            TopicHotCommentFragment.start(getContext(), mTopic);
        } else {
            if (TopicTitleViewHolder.isClickTopicTitle(id)) {
                //点击头像标题
                TopicTitleViewHolder.onClickTopicTitle(getActivity(), mAdapter, view, position, mAdapter.getItem(position).getUserInfo());
            } else if (id == R.id.layout_like) {
                //点赞
                TopicComment topicComment = mAdapter.getItem(position);
                mPresenter.likeComment(topicComment);
                topicComment.updateLiked(!topicComment.isLiked());
                mAdapter.updateItem(position);
            } else if (isLongClick || id != R.id.item_topic_total) {
                //其他部位
                // 为了点击头像有item的波纹效果，点击头像等区域的时候会触发item的触摸事件(触发两次click)
                // 为了防止同时显示对话框和点击头像，判断id不等于item的id时才显示对话框，如果是长按就不用判断
                mCurrentPosition = position;
                mTopicCommentDialog.show();
            }
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.loadWBCommentOrRepost(mTopic.getId(), mTopicPager.getFirstPage(), true, mIsComment);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        mPresenter.loadWBCommentOrRepost(mTopic.getId(), mTopicPager.getNextPage(), false, mIsComment);
    }

    @Override
    public void onLoadTopicComments(TopicComments topicComments) {
        if (mTab != null) {
            String str = ResUtil.getString(mIsComment ?
                    R.string.label_topic_comment_number : R.string.label_topic_repost_number,
                    DataUtil.getCounter(topicComments.getTotalNumber()));
            mTab.setText(str);
        }
    }

    @Override
    public void onTopicCommentLike(TopicComment topicComment, int taskState) {
        if (!TaskState.isSuccess(taskState)) {
            topicComment.updateLiked(!topicComment.isLiked());
            mAdapter.notifyDataSetChanged();
            ToastUtil.showToast(TaskState.toString(taskState));
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        TopicComment topicComment = mAdapter.getItem(mCurrentPosition);
        if (topicComment == null) {
            return;
        }
        if (mIsComment) {
            switch (which) {
                case DialogBuilder.COMMENT_COPY:
                    ClipboardUtil.copyWBContent(topicComment.getContent());
                    break;
                case DialogBuilder.COMMENT_REPLY:
                    startActivity(TopicPublishActivity.getStartIntent(getContext(), TopicDraftHelper.getWBTopicDraftByCommentReply(mTopic, topicComment)));
                    break;
                case DialogBuilder.COMMENT_REPOST:
                    startActivity(TopicPublishActivity.getStartIntent(getContext(), TopicDraftHelper.getWBTopicDraftByCommentRepost(mTopic, topicComment)));
                    break;
            }

        } else {
            switch (which) {
                case DialogBuilder.REPOST_COPY:
                    ClipboardUtil.copyWBContent(topicComment.getContent());
                    break;
                case DialogBuilder.REPOST_DETAIL:
                    startActivity(TopicDetail2Activity.getStartIntent(getContext(), topicComment.toTopic(), true));
                    break;
                case DialogBuilder.REPOST_REPOST:
                    startActivity(TopicPublishActivity.getStartIntent(getContext(), TopicDraftHelper.getWBTopicDraftByRepostRepost(topicComment)));
                    break;
                case DialogBuilder.REPOST_COMMENT:
                    startActivity(TopicPublishActivity.getStartIntent(getContext(), TopicDraftHelper.getWBTopicDraftByRepostComment(topicComment)));
                    break;
            }

        }

    }
}
