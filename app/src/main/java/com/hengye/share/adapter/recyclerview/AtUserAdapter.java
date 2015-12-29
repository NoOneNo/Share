package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.R;

import java.util.List;

public class AtUserAdapter extends CommonAdapter<String, AtUserAdapter.MainViewHolder> {

    public AtUserAdapter(Context context, List<String> data) {
        super(context, data);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_at_user_search_result, parent, false));
    }

    public static class MainViewHolder extends CommonAdapter.ItemViewHolder<String> {
        public MainViewHolder(View v) {
            super(v);
        }

        @Override
        public void bindData(Context context, String string) {

        }
    }
}
