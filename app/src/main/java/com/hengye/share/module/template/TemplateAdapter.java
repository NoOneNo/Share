package com.hengye.share.module.template;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.adapter.recyclerview.CommonAdapter;

public class TemplateAdapter extends CommonAdapter<String, TemplateAdapter.MainViewHolder> {

    public TemplateAdapter(Context context) {
        super(context);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(null, parent, false));
    }

    public static class MainViewHolder extends CommonAdapter.ItemViewHolder<String> {
        public MainViewHolder(View v) {
            super(v);
        }

        @Override
        public void bindData(Context context, String string, int position) {

        }
    }
}
