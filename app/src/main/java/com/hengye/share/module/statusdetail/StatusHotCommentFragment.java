package com.hengye.share.module.statusdetail;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.Status;
import com.hengye.share.model.StatusComment;
import com.hengye.share.model.StatusComments;
import com.hengye.share.model.greenrobot.StatusDraftHelper;
import com.hengye.share.module.publish.StatusPublishActivity;
import com.hengye.share.module.status.ShareLoadDataCallbackFragment;
import com.hengye.share.module.status.StatusTitleViewHolder;
import com.hengye.share.module.util.FragmentActivity;
import com.hengye.share.module.util.encapsulation.base.DefaultDataHandler;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.handler.StatusNumberPager;

import java.util.ArrayList;

public class StatusHotCommentFragment extends ShareLoadDataCallbackFragment<StatusComment> implements StatusCommentContract.View, DialogInterface.OnClickListener {

    public static Bundle getStartBundle(Status status) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("status", status);
        return bundle;
    }

    public static StatusHotCommentFragment newInstance(Status status) {
        StatusHotCommentFragment fragment = new StatusHotCommentFragment();
        fragment.setArguments(getStartBundle(status));
        return fragment;
    }

    public static void start(Context context, Status topic){
        context.startActivity(FragmentActivity.getStartIntent(context, StatusHotCommentFragment.class, StatusHotCommentFragment.getStartBundle(topic)));
    }

    StatusCommentAdapter mAdapter;
    StatusCommentContract.Presenter mPresenter;
    Status mStatus;
    Dialog mStatusCommentDialog;
    int mCurrentPosition;

    StatusNumberPager mStatusNumberPager;

    @Override
    public String getTitle() {
        return ResUtil.getString(R.string.title_activity_hot_comment);
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        mStatus = (Status) bundle.getSerializable("status");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        setAdapter(mAdapter = new StatusCommentAdapter(getContext(), new ArrayList<StatusComment>(), true));
        setPager(mStatusNumberPager = new StatusNumberPager());
        setDataHandler(new DefaultDataHandler<>(mAdapter));

        mPresenter = new StatusCommentPresenter(this, true);

        onRefresh();

        initView();
        mStatusCommentDialog = DialogBuilder.getStatusCommentDialog(getContext(), this);
    }

    private void initView() {
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                final int id = view.getId();

                if (StatusTitleViewHolder.isClickStatusTitle(id)) {
                    StatusTitleViewHolder.onClickStatusTitle(getActivity(), mAdapter, view, position, mAdapter.getItem(position).getUserInfo());
                } else if (id == R.id.layout_like) {
                    //点赞
                    StatusComment topicComment = mAdapter.getItem(position);
                    mPresenter.likeComment(topicComment);
                    topicComment.updateLiked(!topicComment.isLiked());
                    mAdapter.updateItem(position);
                } else if (id != R.id.item_status_total) {
                    // 为了点击头像有item的波纹效果，点击头像等区域的时候会触发item的触摸事件
                    // 为了防止同时显示对话框和点击头像，判断id不等于item的id时才显示对话框
                    mCurrentPosition = position;
                    mStatusCommentDialog.show();
                }
//                为了显示波纹效果再启动
//                getHandler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (TopicTitleViewHolder.isClickStatusTitle(id)) {
//                            TopicTitleViewHolder.onClickStatusTitle(getActivity(), mAdapter, view, position, mAdapter.getItem(position).getUserInfo());
//                        } else if (id == R.id.layout_like) {
//                            L.debug("like btn click");
//                        } else if (id != R.id.item_status_total) {
//                            // 为了点击头像有item的波纹效果，点击头像等区域的时候会触发item的触摸事件
//                            // 为了防止同时显示对话框和点击头像，判断id不等于item的id时才显示对话框
//                            mCurrentPosition = position;
//                            mStatusCommentDialog.show();
//                        }
//
//                    }
//                }, 100);
            }
        });

    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.loadWBHotComment(true, mStatus.getId(), mStatusNumberPager.getFirstPage(), mStatusNumberPager.getPageSize());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        mPresenter.loadWBHotComment(false, mStatus.getId(), mStatusNumberPager.getNextPage(), mStatusNumberPager.getPageSize());
    }

    @Override
    public void onLoadStatusComments(StatusComments statusComments) {
    }

    @Override
    public void onStatusCommentLike(StatusComment statusComment, int taskState) {
        if(!TaskState.isSuccess(taskState)) {
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
    }
}
