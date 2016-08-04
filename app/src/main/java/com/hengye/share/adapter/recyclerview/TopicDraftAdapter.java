package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.model.greenrobot.TopicDraft;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ResUtil;

import java.util.List;

public class TopicDraftAdapter extends CommonAdapter<TopicDraft, TopicDraftAdapter.TopicDraftViewHolder> {

    public TopicDraftAdapter(Context context, List<TopicDraft> data) {
        super(context, data);
    }

    @Override
    public TopicDraftViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new TopicDraftViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_topic_total, parent, false));
    }

    @Override
    public void onBindBasicItemView(TopicDraftViewHolder holder, int position) {
        super.onBindBasicItemView(holder, position);
    }

    public static class TopicDraftViewHolder extends TopicAdapter.TopicViewHolder<TopicDraft> {

        Button mSendAgain;

        public TopicDraftViewHolder(View v){
            super(v);

            mSendAgain = (Button) findViewById(R.id.btn_topic_send_again);
            mSendAgain.setVisibility(View.VISIBLE);
        }

        @Override
        public void bindData(Context context, TopicDraft topicDraft, int position) {
            L.debug("topicDraft id : {}", topicDraft.getId());
            registerChildViewItemClick(mSendAgain);
            Topic topic = topicDraft.getTopic();
            if (topic != null) {
                mTopicTitle.initTopicTitle(context, topic);
                String time = mTopicTitle.mDescription.getText().toString();
                if (!CommonUtil.isEmpty(time)) {
                    mTopicTitle.mDescription.setText(ResUtil.getString(R.string.label_edit_when, time));
                }
                mTopic.initTopicContent(context, topic, false);
            }

            if(topicDraft.getTargetTopic() == null){
                mRetweetTopic.mTopicLayout.setVisibility(View.GONE);
            }else{
                mRetweetTopic.mTopicLayout.setVisibility(View.VISIBLE);
                mRetweetTopic.initTopicContent(context, topicDraft.getTargetTopic(), true);
            }
        }

    }

//    public static class TopicDraftViewHolder extends CommonAdapter.ItemViewHolder<TopicDraft> {
//
//        TopicAdapter.TopicTitleViewHolder mTopicTitle;
//        TopicAdapter.TopicContentViewHolder mTopic, mTargetTopic;
//        Button mSendAgain;
//
//        public TopicDraftViewHolder(View v) {
//            super(v);
//
//            if (mTopicTitle == null) {
//                mTopicTitle = new TopicAdapter.TopicTitleViewHolder(v);
//            }
//            if (mTopic == null) {
//                mTopic = new TopicAdapter.TopicContentViewHolder(findViewById(R.id.ll_topic_content));
//            }
//
//            if(mTargetTopic == null){
//                mTargetTopic = new TopicAdapter.TopicContentViewHolder(findViewById(R.id.ll_topic_retweeted_content), true);
//            }
//
////            mTopic.mContent = (TextView) findViewById(R.id.tv_topic_content);
////            mTopic.mGallery = (GridGalleryView) findViewById(R.id.gl_topic_gallery);
//            mSendAgain = (Button) findViewById(R.id.btn_topic_send_again);
//            mSendAgain.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        public void bindData(Context context, TopicDraft topicDraft, int position) {
//            L.debug("topicDraft id : {}", topicDraft.getId());
//            registerChildViewItemClick(mSendAgain);
//            Topic topic = topicDraft.getTopic();
//            if (topic != null) {
//                mTopicTitle.initTopicTitle(context, topic);
//                String time = mTopicTitle.mDescription.getText().toString();
//                if (!CommonUtil.isEmpty(time)) {
//                    mTopicTitle.mDescription.setText(ResUtil.getString(R.string.label_edit_when, time));
//                }
//                mTopic.initTopicContent(context, topic, false);
//            }
//
//            if(topicDraft.getTargetTopic() == null){
//                mTargetTopic.mTopicLayout.setVisibility(View.GONE);
//            }else{
//                mTargetTopic.mTopicLayout.setVisibility(View.VISIBLE);
//                mTargetTopic.initTopicContent(context, topicDraft.getTargetTopic(), true);
//            }
//        }
//    }
}
