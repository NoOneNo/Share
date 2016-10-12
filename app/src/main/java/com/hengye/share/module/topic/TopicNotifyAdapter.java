package com.hengye.share.module.topic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.CommonAdapter;
import com.hengye.share.model.Topic;
import com.hengye.share.module.topic.TopicAdapter;

import java.util.List;

public class TopicNotifyAdapter extends CommonAdapter<Topic> {

    public TopicNotifyAdapter(Context context, List<Topic> data) {
        super(context, data);
    }

    @Override
    public TopicAdapter.TopicDefaultViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new TopicAdapter.TopicDefaultViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_topic_total, parent, false));
    }


}
