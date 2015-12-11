package com.hengye.share.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.R;

import java.util.List;

public class RecyclerViewCommentAdapter extends RecyclerViewSimpleAdapter<String, RecyclerViewCommentAdapter.MainViewHolder>{

    public RecyclerViewCommentAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false));
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
