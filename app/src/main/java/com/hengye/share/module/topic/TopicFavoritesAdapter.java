package com.hengye.share.module.topic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.CommonAdapter;
import com.hengye.share.model.TopicFavorites;
import com.hengye.share.module.topic.TopicAdapter;

import java.util.List;

public class TopicFavoritesAdapter extends CommonAdapter<TopicFavorites.TopicFavorite, TopicAdapter.TopicDefaultViewHolder> {


    public TopicFavoritesAdapter(Context context, List<TopicFavorites.TopicFavorite> data) {
        super(context, data);
    }

    @Override
    public TopicAdapter.TopicDefaultViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new TopicAdapter.TopicDefaultViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_topic_total, parent, false));
    }

    @Override
    public void onBindBasicItemView(TopicAdapter.TopicDefaultViewHolder holder, int position) {
        holder.setOnItemClickListener(getOnItemClickListener());
        holder.setOnChildViewItemClickListener(getOnChildViewItemClickListener());
        holder.bindData(getContext(), getItem(position).getTopic(), position);
    }

}
