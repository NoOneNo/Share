package com.hengye.share.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.R;
import com.hengye.share.module.Topic;
import com.hengye.share.ui.view.GridGalleryView;

import java.util.List;

public class RecyclerViewTemplateAdapter extends RecyclerViewSimpleAdapter<String, RecyclerViewTemplateAdapter.MainViewHolder>{

    public RecyclerViewTemplateAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(null, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {

    }

    public static class MainViewHolder extends RecyclerViewSimpleAdapter.ViewHolder<String> {
        public MainViewHolder(View v) {
            super(v);
        }

        @Override
        public void bindData(Context context, String string) {

        }
    }
}
