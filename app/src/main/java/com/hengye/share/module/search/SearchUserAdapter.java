package com.hengye.share.module.search;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.ui.widget.image.AvatarImageView;
import com.hengye.share.ui.widget.util.DrawableLoader;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.ThemeUtil;

import java.util.ArrayList;

public class SearchUserAdapter extends CommonAdapter<UserInfo> {

    public SearchUserAdapter(Context context, ArrayList<UserInfo> userInfos) {
        super(context, userInfos);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater
                .from(getContext())
                .inflate(R.layout.item_search_user, parent, false));
    }

    public static class MainViewHolder extends ItemViewHolder<UserInfo> {

        TextView mUsername, mSign, mAttention;
        ImageView mAttentionIcon;
        View mAttentionLayout;
        AvatarImageView mAvatar;

        public MainViewHolder(View v) {
            super(v);

            SelectorLoader.getInstance().setTransparentRippleBackground(v);
            mUsername = (TextView) findViewById(R.id.tv_username);
            mSign = (TextView) findViewById(R.id.tv_sign);
            mAttention = (TextView) findViewById(R.id.tv_attention);
            mAttentionIcon = (ImageView) findViewById(R.id.iv_attention);
            mAttentionLayout = findViewById(R.id.layout_attention);

            mAttention.setTextColor(ThemeUtil.getUntingedColor());
            SelectorLoader
                    .getInstance()
                    .setDefaultRippleBackground(mAttentionLayout, ThemeUtil.getColor());
            mAvatar = (AvatarImageView) findViewById(R.id.iv_avatar);
            mAvatar.setAutoClipBitmap(false);

            registerOnClickListener(mAttentionLayout);
        }

        @Override
        public void bindData(Context context, UserInfo userInfo, int position) {
            super.bindData(context, userInfo, position);
            mAvatar.setImageUrl(userInfo.getAvatar());
            mUsername.setText(userInfo.getName());
            mAttention.setText(userInfo.getFollowRelation());

            mAttentionIcon.setImageDrawable(DrawableLoader
                    .setTintDrawable(getAttentionResId(userInfo),
                            ThemeUtil.getUntingedColor()));

            if (CommonUtil.isEmpty(userInfo.getSign())) {
                mSign.setText(null);
                mSign.setVisibility(View.GONE);
            } else {
                mSign.setText(userInfo.getSign());
                mSign.setVisibility(View.VISIBLE);
            }
        }

        public static
        @DrawableRes
        int getAttentionResId(UserInfo userInfo) {
            if (userInfo.isFollowing()) {
                return userInfo.isFollowMe() ?
                        R.drawable.ic_swap_horiz_black_36dp :
                        R.drawable.ic_done_black_36dp;
            } else {
                return R.drawable.ic_add_black_36dp;
            }
        }
    }
}
