package com.hengye.share.module.topicdetail;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.model.TopicComment;
import com.hengye.share.model.TopicComments;
import com.hengye.share.model.greenrobot.TopicDraftHelper;
import com.hengye.share.module.publish.TopicPublishActivity;
import com.hengye.share.module.topic.StatusFragment;
import com.hengye.share.module.topic.TopicTitleViewHolder;
import com.hengye.share.module.util.FragmentActivity;
import com.hengye.share.module.util.encapsulation.base.DataHandler;
import com.hengye.share.module.util.encapsulation.base.DefaultDataHandler;
import com.hengye.share.module.util.encapsulation.base.Pager;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.handler.TopicNumberPager;

import java.util.ArrayList;

public class TopicHotCommentFragment extends StatusFragment<TopicComment> implements TopicCommentMvpView, DialogInterface.OnClickListener {

    public static Bundle getStartBundle(Topic topic) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("topic", topic);
        return bundle;
    }

    public static TopicHotCommentFragment newInstance(Topic topic) {
        TopicHotCommentFragment fragment = new TopicHotCommentFragment();
        fragment.setArguments(getStartBundle(topic));
        return fragment;
    }

    public static void start(Context context, Topic topic){
        context.startActivity(FragmentActivity.getStartIntent(context, TopicHotCommentFragment.class, TopicHotCommentFragment.getStartBundle(topic)));
    }

    TopicCommentAdapter mAdapter;
    TopicCommentPresenter mPresenter;
    Topic mTopic;
    Dialog mTopicCommentDialog;
    int mCurrentPosition;

    @Override
    public String getTitle() {
        return ResUtil.getString(R.string.title_activity_hot_comment);
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        mTopic = (Topic) bundle.getSerializable("topic");
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        setAdapter(mAdapter = new TopicCommentAdapter(getContext(), new ArrayList<TopicComment>(), true));
        mTopicPager = new TopicNumberPager();
        mHandler = new DefaultDataHandler<>(mAdapter);

        addPresenter(mPresenter = new TopicCommentPresenter(this, true));

        onRefresh();

        initView();
        mTopicCommentDialog = DialogBuilder.getTopicCommentDialog(getContext(), this);
    }

    private void initView() {
        setRefreshEnable(false);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(final View view, final int position) {
                final int id = view.getId();

                if (TopicTitleViewHolder.isClickTopicTitle(id)) {
                    TopicTitleViewHolder.onClickTopicTitle(getActivity(), mAdapter, view, position, mAdapter.getItem(position).getUserInfo());
                } else if (id == R.id.layout_like) {
                    //点赞
                    TopicComment topicComment = mAdapter.getItem(position);
                    mPresenter.likeComment(topicComment);
                    topicComment.updateLiked(!topicComment.isLiked());
                    mAdapter.updateItem(position);
                } else if (id != R.id.item_topic_total) {
                    // 为了点击头像有item的波纹效果，点击头像等区域的时候会触发item的触摸事件
                    // 为了防止同时显示对话框和点击头像，判断id不等于item的id时才显示对话框
                    mCurrentPosition = position;
                    mTopicCommentDialog.show();
                }
//                为了显示波纹效果再启动
//                getHandler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (TopicTitleViewHolder.isClickTopicTitle(id)) {
//                            TopicTitleViewHolder.onClickTopicTitle(getActivity(), mAdapter, view, position, mAdapter.getItem(position).getUserInfo());
//                        } else if (id == R.id.layout_like) {
//                            L.debug("like btn click");
//                        } else if (id != R.id.item_topic_total) {
//                            // 为了点击头像有item的波纹效果，点击头像等区域的时候会触发item的触摸事件
//                            // 为了防止同时显示对话框和点击头像，判断id不等于item的id时才显示对话框
//                            mCurrentPosition = position;
//                            mTopicCommentDialog.show();
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
        mPresenter.loadWBHotComment(true, mTopic.getId(), mTopicPager.getFirstPage(), mTopicPager.getPageSize());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        mPresenter.loadWBHotComment(false, mTopic.getId(), mTopicPager.getNextPage(), mTopicPager.getPageSize());
    }

    @Override
    public void onLoadTopicComments(TopicComments topicComments) {
    }

    @Override
    public void onTopicCommentLike(TopicComment topicComment, int taskState) {
        if(!TaskState.isSuccess(taskState)) {
            topicComment.updateLiked(!topicComment.isLiked());
            mAdapter.notifyDataSetChanged();
            ToastUtil.showToast(TaskState.toString(taskState));
        }
    }

    TopicNumberPager mTopicPager;
    DefaultDataHandler<TopicComment> mHandler;

    @Override
    public DataHandler<TopicComment> getDataHandler() {
        return mHandler;
    }

    @Override
    public Pager getPager() {
        return mTopicPager;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        TopicComment topicComment = mAdapter.getItem(mCurrentPosition);
        if (topicComment == null) {
            return;
        }
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
    }
}
