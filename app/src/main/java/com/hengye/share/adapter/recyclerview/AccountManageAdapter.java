package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.R;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.RequestManager;

import java.util.List;

public class AccountManageAdapter extends CommonAdapter<User, AccountManageAdapter.MainViewHolder> {

    public AccountManageAdapter(Context context, List<User> data) {
        super(context, data);
    }

    @Override
    public boolean isAddFooterView() {
        return true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return new FooterViewHolder(getContext(), LayoutInflater.from(getContext()).inflate(R.layout.item_account_manage, parent, false));
    }

    @Override
    public void onBindFooterView(RecyclerView.ViewHolder holder, int position) {

    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(Context context, View v) {
            super(v);

            NetworkImageViewPlus avatar = (NetworkImageViewPlus) v.findViewById(R.id.iv_avatar);
            TextView username = (TextView) v.findViewById(R.id.tv_username);

            SelectorLoader.getInstance().setImageSelector(context, avatar, R.drawable.compose_more_account_add, R.drawable.compose_more_account_add_highlighted);
            username.setText(R.string.label_add_account);
        }
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_account_manage, parent, false));
    }

    public static class MainViewHolder extends CommonAdapter.ItemViewHolder<User> {

        NetworkImageViewPlus mAvatar;
        TextView mUsername;

        public MainViewHolder(View v) {
            super(v);

            mAvatar = (NetworkImageViewPlus) findViewById(R.id.iv_avatar);
            mUsername = (TextView) findViewById(R.id.tv_username);
        }

        @Override
        public void bindData(Context context, User user) {

            mAvatar.setImageUrl(user.getAvatar(), RequestManager.getImageLoader());

            mUsername.setText(user.getName());
        }
    }
}
