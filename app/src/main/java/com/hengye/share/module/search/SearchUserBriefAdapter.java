package com.hengye.share.module.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.ui.widget.image.AvatarImageView;

import java.util.List;

public class SearchUserBriefAdapter extends CommonAdapter<UserInfo> {

    public SearchUserBriefAdapter(Context context, List<UserInfo> data) {
        super(context, data);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_search_user_brief, parent, false));
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
            avatar.setImageUrl(userInfo.getAvatar());
        }
    }
}
