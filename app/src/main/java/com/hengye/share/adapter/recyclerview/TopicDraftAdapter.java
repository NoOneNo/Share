package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.module.Topic;
import com.hengye.share.service.TopicPublishService;
import com.hengye.share.ui.activity.TopicDraftActivity;
import com.hengye.share.ui.view.GridGalleryView;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.ViewUtil;

import java.util.List;

public class TopicDraftAdapter extends CommonAdapter<Topic, TopicDraftAdapter.TopicDraftViewHolder>
        implements ViewUtil.OnItemClickListener{

    public TopicDraftAdapter(Context context, List<Topic> data) {
        super(context, data);
        setOnChildViewItemClickListener(this);
    }

    @Override
    public TopicDraftViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new TopicDraftViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_topic_draft, parent, false));
    }

    @Override
    public void onBindBasicItemView(TopicDraftViewHolder holder, int position) {
        super.onBindBasicItemView(holder, position);
    }

    @Override
    public void onItemClick(View view, int position) {
        int id = view.getId();
        if(id == R.id.btn_topic_send_again){
            Topic topic = getItem(position);
            TopicDraftActivity.removeTopicFromDraft(topic);
            remove(position);
            TopicPublishService.publish(getContext(), topic, SPUtil.getSinaToken());
        }
    }

    public static class TopicDraftViewHolder extends CommonAdapter.ItemViewHolder<Topic> {

        TopicAdapter.TopicTitleViewHolder mTopicTitle;
        TopicAdapter.TopicContentViewHolder mTopic;
        Button mSendAgain;

        public TopicDraftViewHolder(View v) {
            super(v);

            if (mTopicTitle == null) {
                mTopicTitle = new TopicAdapter.TopicTitleViewHolder(v);
            }
            if (mTopic == null) {
                mTopic = new TopicAdapter.TopicContentViewHolder();
            }

            mTopic.mContent = (TextView) v.findViewById(R.id.tv_topic_content);
            mTopic.mGallery = (GridGalleryView) v.findViewById(R.id.gl_topic_gallery);
            mSendAgain = (Button) v.findViewById(R.id.btn_topic_send_again);
            mSendAgain.setVisibility(View.VISIBLE);
        }

        @Override
        public void bindData(Context context, Topic topic) {
            registerChildViewItemClick(mSendAgain);
            mTopicTitle.initTopicTitle(context, topic);
            mTopic.initTopicContent(context, topic, false);
        }
    }
}
