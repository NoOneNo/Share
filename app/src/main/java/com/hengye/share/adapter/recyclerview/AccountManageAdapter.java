package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.R;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.ui.activity.AccountManageActivity;
import com.hengye.share.ui.activity.ThirdPartyLoginActivity;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;

import java.util.List;

public class AccountManageAdapter extends CommonAdapter<User, AccountManageAdapter.MainViewHolder> {

    private AccountManageActivity.AccountManageCallBack mCallBack;

    public AccountManageAdapter(Context context, List<User> data, AccountManageActivity.AccountManageCallBack callBack) {
        super(context, data);
        mCallBack = callBack;
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
        public FooterViewHolder(final Context context, View v) {
            super(v);

            NetworkImageViewPlus avatar = (NetworkImageViewPlus) v.findViewById(R.id.iv_avatar);
            TextView username = (TextView) v.findViewById(R.id.tv_username);

            SelectorLoader.getInstance().setImageSelector(context, avatar, R.drawable.compose_more_account_add, R.drawable.compose_more_account_add_highlighted);
            username.setText(R.string.label_add_account);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((BaseActivity)context).startActivityForResult(ThirdPartyLoginActivity.getStartIntent(context, false), ThirdPartyUtils.REQUEST_CODE_FOR_WEIBO);
                }
            });

            SelectorLoader
                    .getInstance()
                    .setDefaultRippleWhiteBackground(v);
        }
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(mCallBack, LayoutInflater.from(getContext()).inflate(R.layout.item_account_manage, parent, false));
    }

    public static class MainViewHolder extends CommonAdapter.ItemViewHolder<User> {

        AccountManageActivity.AccountManageCallBack mCallBack;

        NetworkImageViewPlus mAvatar;
        TextView mUsername;
        ImageButton mSelectBtn;

        public MainViewHolder(AccountManageActivity.AccountManageCallBack callBack, View v) {
            super(v);
            mCallBack = callBack;
            mAvatar = (NetworkImageViewPlus) findViewById(R.id.iv_avatar);
            mUsername = (TextView) findViewById(R.id.tv_username);
            mSelectBtn = (ImageButton) findViewById(R.id.btn_check);

            SelectorLoader
                    .getInstance()
                    .setDefaultRippleWhiteBackground(v);
        }

        @Override
        public void bindData(Context context, User user, int position) {

            mAvatar.setImageUrl(user.getAvatar(), RequestManager.getImageLoader());

            mUsername.setText(user.getName());

            if(mCallBack.getAccountSelectIndex() == position){
                mSelectBtn.setVisibility(View.VISIBLE);
                mCallBack.setAccountSelectBtn(mSelectBtn);
            }else{
                mSelectBtn.setVisibility(View.GONE);
            }
            mSelectBtn.setVisibility(mCallBack.getAccountSelectIndex() == position ? View.VISIBLE : View.GONE);

        }
    }
}
