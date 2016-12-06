package com.hengye.share.module.template;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;

public class TemplateAdapter extends CommonAdapter<String> {

    public TemplateAdapter(Context context) {
        super(context);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(inflate(R.layout.item_user_list, parent));
    }

    public static class MainViewHolder extends ItemViewHolder<String> {
        public MainViewHolder(View v) {
            super(v);
        }

        @Override
        public void bindData(Context context, String string, int position) {

        }
    }
}
