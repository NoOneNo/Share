package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.module.AtUser;
import com.hengye.share.module.Select;

import java.util.List;

public class AtUserSelectAdapter extends CommonAdapter<AtUser, AtUserSelectAdapter.MainViewHolder> {

    public AtUserSelectAdapter(Context context, List<AtUser> data) {
        super(context, data);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_at_user_select_result, parent, false));
    }

    @Override
    public void onBindBasicItemView(MainViewHolder holder, int position) {
        super.onBindBasicItemView(holder, position);

    }

    public static class MainViewHolder extends CommonAdapter.ItemViewHolder<AtUser> {

        ImageView mAvatar;

        public MainViewHolder(View v) {
            super(v);

            mAvatar = (ImageView) v.findViewById(R.id.iv_avatar);
        }

        @Override
        public void bindData(Context context, AtUser select, int position) {

        }
    }
}
