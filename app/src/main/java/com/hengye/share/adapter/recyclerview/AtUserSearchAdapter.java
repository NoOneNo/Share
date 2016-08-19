package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.R;
import com.hengye.share.model.AtUser;
import com.hengye.share.model.UserInfo;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.RequestManager;

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

    public void showSearchResult(String str, List<AtUser> totalData) {
        if (TextUtils.isEmpty(str)) {
            refresh(totalData);
        } else {
            refresh(AtUser.search(totalData, str));
        }
    }

    public static class MainViewHolder extends CommonAdapter.ItemViewHolder<AtUser> {

        ImageButton mCheckBox;
        TextView mUsername;
        NetworkImageViewPlus mAvatar;

        public MainViewHolder(View v) {
            super(v);

            mCheckBox = (ImageButton) findViewById(R.id.btn_check);
            mUsername = (TextView) findViewById(R.id.tv_username);
            mAvatar = (NetworkImageViewPlus) findViewById(R.id.iv_avatar);

            SelectorLoader
                    .getInstance()
                    .setDefaultRippleWhiteBackground(v);
        }

        @Override
        public void bindData(Context context, AtUser atUser, int position) {
            mCheckBox.setTag(position);
            mCheckBox.setImageResource(atUser.isSelected() ? R.drawable.ic_check_select : 0);

            UserInfo userInfo = atUser.getUserInfo();
            if (userInfo != null) {
                mUsername.setText(userInfo.getName());
                mAvatar.setImageUrl(userInfo.getAvatar(), RequestManager.getImageLoader());
            } else {
                mAvatar.setImageResource(0);
            }
        }
    }
}
