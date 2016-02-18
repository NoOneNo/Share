package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hengye.share.R;
import com.hengye.share.model.TopicFavorites;

import java.util.List;

public class TopicFavoritesAdapter extends CommonAdapter<TopicFavorites.TopicFavorite, TopicAdapter.TopicViewHolder> {


    public TopicFavoritesAdapter(Context context, List<TopicFavorites.TopicFavorite> data) {
        super(context, data);
    }

    @Override
    public TopicAdapter.TopicViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new TopicAdapter.TopicViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_topic_notify, parent, false));
    }

    @Override
    public void onBindBasicItemView(TopicAdapter.TopicViewHolder holder, int position) {
        holder.setOnItemClickListener(getOnItemClickListener());
        holder.setOnChildViewItemClickListener(getOnChildViewItemClickListener());
        holder.bindData(getContext(), getItem(position).getTopic(), position);
    }

}
