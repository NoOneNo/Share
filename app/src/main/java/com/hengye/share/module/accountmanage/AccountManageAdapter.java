package com.hengye.share.module.accountmanage;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.module.sso.ThirdPartyLoginActivity;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.HeaderAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.ui.widget.image.AvatarImageView;
import com.hengye.share.ui.widget.image.SuperImageView;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;

import java.util.List;

public class AccountManageAdapter extends CommonAdapter<User> {

    private AccountManageActivity.AccountManageCallBack mCallBack;

    public AccountManageAdapter(Context context, List<User> data, AccountManageActivity.AccountManageCallBack callBack) {
        super(context, data);
        mCallBack = callBack;

        setFooter(inflate(R.layout.item_account_manage));
    }

//    @Override
//    public boolean isAddFooterView() {
//        return true;
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
//        return new FooterViewHolder(getContext(), LayoutInflater.from(getContext()).inflate(R.layout.item_account_manage, parent, false));
//    }


    @Override
    public void onBindFooterView(HeaderAdapter.ContainerViewHolder holder, int position) {
        super.onBindFooterView(holder, position);

        View v = holder.container;
        SuperImageView avatar = (SuperImageView) v.findViewById(R.id.iv_avatar);
        TextView username = (TextView) v.findViewById(R.id.tv_username);

        SelectorLoader.getInstance().setImageSelector(getContext(), avatar, R.drawable.compose_more_account_add, R.drawable.compose_more_account_add_highlighted);
        username.setText(R.string.label_add_account);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).startActivityForResult(ThirdPartyLoginActivity.getStartIntent(getContext(), false), ThirdPartyUtils.REQUEST_CODE_FOR_WEIBO);
            }
        });

        SelectorLoader
                .getInstance()
                .setDefaultRippleWhiteBackground(v);
    }

    @Override
    public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(mCallBack, LayoutInflater.from(getContext()).inflate(R.layout.item_account_manage, parent, false));
    }

    public static class MainViewHolder extends ItemViewHolder<User> {

        AccountManageActivity.AccountManageCallBack mCallBack;

        AvatarImageView mAvatar;
        TextView mUsername;
        ImageButton mSelectBtn;

        public MainViewHolder(AccountManageActivity.AccountManageCallBack callBack, View v) {
            super(v);
            mCallBack = callBack;
            mAvatar = (AvatarImageView) findViewById(R.id.iv_avatar);
            mUsername = (TextView) findViewById(R.id.tv_username);
            mSelectBtn = (ImageButton) findViewById(R.id.btn_check);

            SelectorLoader
                    .getInstance()
                    .setDefaultRippleWhiteBackground(v);
        }

        @Override
        public void bindData(Context context, User user, int position) {

            mAvatar.setAutoClipBitmap(false);
            mAvatar.setImageUrl(user.getAvatar());

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
