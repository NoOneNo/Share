package com.hengye.share.module.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.EditModeAdapter;
import com.hengye.share.model.Select;
import com.hengye.share.model.UserInfo;
import com.hengye.share.ui.widget.image.AvatarImageView;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.RequestManager;

public class UserListAdapter extends EditModeAdapter<Select<UserInfo>, UserListAdapter.MainViewHolder> {

    public UserListAdapter(Context context) {
        super(context);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_user_list, parent, false));
    }

    @Override
    public void onBindBasicItemView(MainViewHolder holder, int position) {
        super.onBindBasicItemView(holder, position);
    }

    public static class MainViewHolder extends EditModeAdapter.EditModeViewHolder<Select<UserInfo>> {

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
        public void updateEditMode(boolean isEditMode) {
            mCheckBox.setVisibility(isEditMode ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void bindData(Context context, Select<UserInfo> select, int position) {
            mCheckBox.setImageResource(select.isSelected() ? R.drawable.ic_check_select : 0);

            UserInfo userInfo = select.getTarget();
            if (userInfo != null) {
                mUsername.setText(userInfo.getName());
                mAvatar.setImageUrl(userInfo.getAvatar(), RequestManager.getImageLoader());
            } else {
                mAvatar.setImageResource(0);
            }
        }
    }
}
