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

public class AtUserSearchAdapter extends CommonAdapter<AtUser, AtUserSearchAdapter.MainViewHolder> {

    public AtUserSearchAdapter(Context context, List<AtUser> data) {
        super(context, data);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_at_user_search_result, parent, false));
    }

    @Override
    public void onBindBasicItemView(MainViewHolder holder, int position) {
        super.onBindBasicItemView(holder, position);

    }

    public static class MainViewHolder extends CommonAdapter.ItemViewHolder<AtUser> {

        ImageButton mCheckBox;
        TextView mUsername;
        ImageView mAvatar;

        public MainViewHolder(View v) {
            super(v);

            mCheckBox = (ImageButton) v.findViewById(R.id.btn_check);
            mUsername = (TextView) v.findViewById(R.id.tv_username);
            mAvatar = (ImageView) v.findViewById(R.id.iv_avatar);
        }

        @Override
        public void bindData(Context context, AtUser select, int position) {
            mCheckBox.setTag(position);
            mCheckBox.setImageResource(select.isSelected() ? R.drawable.ic_check_select : 0);
            mUsername.setText(select.getUsername());
        }
    }
}
