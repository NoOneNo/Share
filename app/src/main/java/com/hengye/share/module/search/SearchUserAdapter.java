package com.hengye.share.module.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.CommonAdapter;
import com.hengye.share.adapter.recyclerview.ItemViewHolder;
import com.hengye.share.model.UserInfo;
import com.hengye.share.ui.widget.image.AvatarImageView;
import com.hengye.share.util.RequestManager;

import java.util.List;

public class SearchUserAdapter extends CommonAdapter<UserInfo> {

    public SearchUserAdapter(Context context, List<UserInfo> data) {
        super(context, data);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_search_user, parent, false));
    }

    public static class MainViewHolder extends ItemViewHolder<UserInfo> {

        AvatarImageView avatar;
        TextView username;

        public MainViewHolder(View v) {
            super(v);

            avatar = (AvatarImageView) findViewById(R.id.iv_avatar);
            username = (TextView) findViewById(R.id.tv_username);
        }

        @Override
        public void bindData(Context context, UserInfo userInfo, int position) {
            username.setText(userInfo.getName());
            avatar.setImageUrl(userInfo.getAvatar(), RequestManager.getImageLoader());
        }
    }
}
