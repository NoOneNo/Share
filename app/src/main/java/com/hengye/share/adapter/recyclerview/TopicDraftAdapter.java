package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.module.Topic;
import com.hengye.share.ui.view.GridGalleryView;

import java.util.List;

public class TopicDraftAdapter extends CommonAdapter<Topic, TopicDraftAdapter.TopicDraftViewHolder> {

    public TopicDraftAdapter(Context context, List<Topic> data) {
        super(context, data);
    }

    @Override
    public TopicDraftViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new TopicDraftViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_topic_draft, parent, false));
    }

    public static class TopicDraftViewHolder extends CommonAdapter.ItemViewHolder<Topic> {

        TopicAdapter.TopicTitleViewHolder mTopicTitle;
        TopicAdapter.TopicContentViewHolder mTopic;

        public TopicDraftViewHolder(View v) {
            super(v, true);

            if (mTopicTitle == null) {
                mTopicTitle = new TopicAdapter.TopicTitleViewHolder(v);
            }
            if (mTopic == null) {
                mTopic = new TopicAdapter.TopicContentViewHolder();
            }

            mTopic.mContent = (TextView) v.findViewById(R.id.tv_topic_content);
            mTopic.mGallery = (GridGalleryView) v.findViewById(R.id.gl_topic_gallery);
        }

        @Override
        public void bindData(Context context, Topic topic) {
//            registerChildViewItemClick(mTopicTitle);

            mTopicTitle.initTopicTitle(context, topic);
            mTopic.initTopicContent(context, topic, false);
        }
    }
}
