package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.R;
import com.hengye.share.model.Topic;

import java.util.List;

public class TopicNotifyAdapter extends CommonAdapter<Topic, TopicAdapter.TopicDefaultViewHolder> {


    public TopicNotifyAdapter(Context context, List<Topic> data) {
        super(context, data);
    }

    @Override
    public boolean isAddHeaderView() {
        return true;
    }

    @Override
    public boolean isAddFooterView() {
        return true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return new HeaderViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.header_topic_notify, parent, false));
    }

    @Override
    public void onBindHeaderView(RecyclerView.ViewHolder holder, int position) {
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View v) {
            super(v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return new FooterViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.footer_null, parent, false));
    }

    @Override
    public void onBindFooterView(RecyclerView.ViewHolder holder, int position) {
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder{
        public FooterViewHolder(View v){
            super(v);
        }
    }

    @Override
    public TopicAdapter.TopicDefaultViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new TopicAdapter.TopicDefaultViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_topic_total, parent, false));
    }

    @Override
    public void onBindBasicItemView(TopicAdapter.TopicDefaultViewHolder holder, int position) {
        super.onBindBasicItemView(holder, position);
    }

}
