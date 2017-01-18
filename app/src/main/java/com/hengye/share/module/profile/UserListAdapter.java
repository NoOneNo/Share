package com.hengye.share.module.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.Select;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.EditModeAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.ui.widget.image.AvatarImageView;
import com.hengye.share.ui.widget.util.SelectorLoader;

public class UserListAdapter extends CommonAdapter<UserInfo> {

    public UserListAdapter(Context context) {
        super(context);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_user_list, parent, false));
    }

    public static class MainViewHolder extends ItemViewHolder<UserInfo> {

        ImageButton mCheckBox;
        TextView mUsername;
        AvatarImageView mAvatar;

        public MainViewHolder(View v) {
            super(v);

            mCheckBox = (ImageButton) findViewById(R.id.btn_check);
            mUsername = (TextView) findViewById(R.id.tv_username);
            mAvatar = (AvatarImageView) findViewById(R.id.iv_avatar);

            mAvatar.setAutoClipBitmap(false);
            SelectorLoader
                    .getInstance()
                    .setDefaultRippleWhiteBackground(v);
        }

        @Override
        public void bindData(Context context, UserInfo userInfo, int position) {
            super.bindData(context, userInfo, position);
            if (userInfo != null) {
                mUsername.setText(userInfo.getName());
                mAvatar.setImageUrl(userInfo.getAvatar());
            } else {
                mAvatar.setImageResource(0);
            }
        }
    }
}
