package com.hengye.share.module.draft;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.model.Topic;
import com.hengye.share.model.greenrobot.TopicDraft;
import com.hengye.share.module.topic.TopicAdapter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ResUtil;

import java.util.List;

public class TopicDraftAdapter extends CommonAdapter<TopicDraft> {

    public TopicDraftAdapter(Context context, List<TopicDraft> data) {
        super(context, data);
    }

    @Override
    public TopicDraftViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new TopicDraftViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_topic_total, parent, false));
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
            registerOnClickListener(mSendAgain);
            Topic topic = topicDraft.getTopic();
            if (topic != null) {
                mTopicTitle.initTopicTitle(context, topic);
                String time = mTopicTitle.mDescription.getText().toString();
                StringBuilder sb = new StringBuilder();
                if (!CommonUtil.isEmpty(time)) {
                    sb.append(ResUtil.getString(R.string.label_edit_when, time));
                }
                if(topicDraft.isPublishTiming()){
                    String prefix = mTopicTitle.mDescription.getText().toString();
                    if(sb.length() != 0){
                        //增加间隙
                        sb.append("  ");
                    }

                    String dateFormat = DateUtil.getLaterDateFormat(topicDraft.getPublishTiming(), false);
                    if(DateUtil.TIME_UNKOWN.equals(dateFormat)){
                        sb.append(ResUtil.getString(R.string.label_timing_expire));
                    }else {
                        sb.append(ResUtil.getString(R.string.label_publish_when, dateFormat));
                    }
                }
                mTopicTitle.mDescription.setText(sb.toString());
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
//            registerOnClickListener(mSendAgain);
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
