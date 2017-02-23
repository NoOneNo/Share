package com.hengye.share.module.draft;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.model.Status;
import com.hengye.share.model.greenrobot.StatusDraft;
import com.hengye.share.module.status.StatusAdapter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ResUtil;

import java.util.List;

public class StatusDraftAdapter extends CommonAdapter<StatusDraft> {

    public StatusDraftAdapter(Context context, List<StatusDraft> data) {
        super(context, data);
    }

    @Override
    public StatusDraftViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new StatusDraftViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_status_draft_total, parent, false));
    }

    public static class StatusDraftViewHolder extends StatusAdapter.StatusViewHolder<StatusDraft> {

        Button mSendAgain;

        public StatusDraftViewHolder(View v){
            super(v);

            mSendAgain = (Button) findViewById(R.id.btn_topic_send_again);
            mSendAgain.setVisibility(View.VISIBLE);
        }

        @Override
        public void bindData(Context context, StatusDraft statusDraft, int position) {
            L.debug("topicDraft id : %s", statusDraft.getId());
            registerOnClickListener(mSendAgain);
            Status status = statusDraft.getOriginalStatus();
            if (status != null) {
                mStatusTitle.initStatusTitle(context, status);
                String time = mStatusTitle.mDescription.getText().toString();
                StringBuilder sb = new StringBuilder();
                if (!CommonUtil.isEmpty(time)) {
                    sb.append(ResUtil.getString(R.string.label_edit_when, time));
                }
                if(statusDraft.isPublishTiming()){
                    String prefix = mStatusTitle.mDescription.getText().toString();
                    if(sb.length() != 0){
                        //增加间隙
                        sb.append("  ");
                    }

                    String dateFormat = DateUtil.getLaterDateFormat(statusDraft.getPublishTiming(), false);
                    if(DateUtil.TIME_UNKOWN.equals(dateFormat)){
                        sb.append(ResUtil.getString(R.string.label_timing_expire));
                    }else {
                        sb.append(ResUtil.getString(R.string.label_publish_when, dateFormat));
                    }
                }
                mStatusTitle.mDescription.setText(sb.toString());
                mStatus.initStatusContent(context, status, false);
            }

            if(statusDraft.getTargetStatus() == null){
                mRetweetStatus.mStatusLayout.setVisibility(View.GONE);
            }else{
                mRetweetStatus.mStatusLayout.setVisibility(View.VISIBLE);
                mRetweetStatus.initStatusContent(context, statusDraft.getTargetStatus(), true);
            }
        }

    }

//    public static class TopicDraftViewHolder extends CommonAdapter.ItemViewHolder<TopicDraft> {
//
//        TopicAdapter.TopicTitleViewHolder mCommentTitle;
//        TopicAdapter.TopicContentViewHolder mComment, mTargetTopic;
//        Button mSendAgain;
//
//        public TopicDraftViewHolder(View v) {
//            super(v);
//
//            if (mCommentTitle == null) {
//                mCommentTitle = new TopicAdapter.TopicTitleViewHolder(v);
//            }
//            if (mComment == null) {
//                mComment = new TopicAdapter.TopicContentViewHolder(findViewById(R.id.ll_topic_content));
//            }
//
//            if(mTargetTopic == null){
//                mTargetTopic = new TopicAdapter.TopicContentViewHolder(findViewById(R.id.ll_topic_retweeted_content), true);
//            }
//
////            mComment.mContent = (TextView) findViewById(R.id.tv_topic_content);
////            mComment.mGallery = (GridGalleryView) findViewById(R.id.gl_topic_gallery);
//            mSendAgain = (Button) findViewById(R.id.btn_topic_send_again);
//            mSendAgain.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        public void bindData(Context context, TopicDraft topicDraft, int position) {
//            L.debug("topicDraft id : {}", topicDraft.getId());
//            registerOnClickListener(mSendAgain);
//            Topic topic = topicDraft.getStatus();
//            if (topic != null) {
//                mCommentTitle.initStatusTitle(context, topic);
//                String time = mCommentTitle.mDescription.getText().toString();
//                if (!CommonUtil.isEmpty(time)) {
//                    mCommentTitle.mDescription.setText(ResUtil.getString(R.string.label_edit_when, time));
//                }
//                mComment.initTopicContent(context, topic, false);
//            }
//
//            if(topicDraft.getTargetStatus() == null){
//                mTargetTopic.mTopicLayout.setVisibility(View.GONE);
//            }else{
//                mTargetTopic.mTopicLayout.setVisibility(View.VISIBLE);
//                mTargetTopic.initTopicContent(context, topicDraft.getTargetStatus(), true);
//            }
//        }
//    }
}
