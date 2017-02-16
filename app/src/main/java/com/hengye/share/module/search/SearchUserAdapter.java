package com.hengye.share.module.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.ui.widget.image.AvatarImageView;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.CommonUtil;

import java.util.ArrayList;

public class SearchUserAdapter extends CommonAdapter<UserInfo> {

    public SearchUserAdapter(Context context, ArrayList<UserInfo> userInfos) {
        super(context, userInfos);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_search_user, parent, false));
    }

    public static class MainViewHolder extends ItemViewHolder<UserInfo> {

        TextView mUsername, mSign;
        AvatarImageView mAvatar;

        public MainViewHolder(View v) {
            super(v);

            mUsername = (TextView) findViewById(R.id.tv_username);
            mSign = (TextView) findViewById(R.id.tv_sign);
            mAvatar = (AvatarImageView) findViewById(R.id.iv_avatar);

            mAvatar.setAutoClipBitmap(false);
            SelectorLoader
                    .getInstance()
                    .setTransparentRippleBackground(v);
        }

        @Override
        public void bindData(Context context, UserInfo userInfo, int position) {
            super.bindData(context, userInfo, position);
            mAvatar.setImageUrl(userInfo.getAvatar());
            mUsername.setText(userInfo.getName());
            if(CommonUtil.isEmpty(userInfo.getSign())){
                mSign.setText(null);
                mSign.setVisibility(View.GONE);
            }else{
                mSign.setText(userInfo.getSign());
                mSign.setVisibility(View.VISIBLE);
            }
        }
    }
}
