package com.hengye.share.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class RecyclerViewTemplateAdapter extends RecyclerViewBaseAdapter<String, RecyclerViewTemplateAdapter.MainViewHolder>{

    public RecyclerViewTemplateAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(null, parent, false));
    }

    public static class MainViewHolder extends RecyclerViewBaseAdapter.ItemViewHolder<String> {
        public MainViewHolder(View v) {
            super(v);
        }

        @Override
        public void bindData(Context context, String string) {

        }
    }
}
