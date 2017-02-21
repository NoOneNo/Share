package com.hengye.share.module.sso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hengye.share.R;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.ui.widget.dialog.LoadingDialog;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.thirdparty.ParseTokenWeiboAuthListener;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

public class ThirdPartyLoginActivity extends BaseActivity implements UserContract.View {

    public static Intent getStartIntent(Context context, boolean isCurrentUser){
        return getStartIntent(context, isCurrentUser ? UserUtil.getUid() : null);
    }

    public static Intent getStartIntent(Context context, String uid){
        return getStartIntent(context, ThirdPartyUtils.WeiboApp.SHARE.name(), uid);
    }

    public static Intent getAdTokenStartIntent(Context context, boolean isCurrentUser){
        return getAdTokenStartIntent(context, isCurrentUser ? UserUtil.getUid() : null);
    }

    public static Intent getAdTokenStartIntent(Context context, String uid){
        return getStartIntent(context, ThirdPartyUtils.WeiboApp.WEICO.name(), uid);
    }

    public static Intent getStartIntent(Context context, String appKey, String uid){
        Intent intent = new Intent(context, ThirdPartyLoginActivity.class);
        intent.putExtra("appKey", appKey);
        intent.putExtra("uid", uid);
        return intent;
    }

    @Override
    protected boolean setCustomTheme() {
        return false;
    }

    @Override
    protected boolean setFinishPendingTransition() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new UserPresenter(this);

        initData();
        String appName = getIntent().getStringExtra("appKey");
        String uid = getIntent().getStringExtra("uid");

        startActivityForResult(WeiboWebAuthorizeActivity.getStartIntent(this, appName, uid), WEIBO_WEB_LOGIN);
//        try {
//            mSsoHandler.authorize(ThirdPartyUtils.REQUEST_CODE_FOR_WEIBO, mWeiboAuthListener, null);
//        } catch (Exception e) {
//            startActivityForResult(WeiboWebLoginActivity.class, WEIBO_WEB_LOGIN);
////            mWeiboAuth.anthorize(new WBAuthListener());
//            e.printStackTrace();
//        }
    }

    private final static int WEIBO_WEB_LOGIN = 3;
    private LoadingDialog mLoadingDialog;
    private UserContract.Presenter mPresenter;
    private WBAuthListener mWeiboAuthListener;

    private void initData() {
        mLoadingDialog = new LoadingDialog(this, getString(R.string.label_get_user_info));
        mWeiboAuth = ThirdPartyUtils.getWeiboData(this);
        mSsoHandler = new SsoHandler(this, mWeiboAuth);
        mWeiboAuthListener = new WBAuthListener();
    }

    @Override
    public void loadUserInfoFail() {
        mLoadingDialog.dismiss();
        ToastUtil.showToastError(R.string.label_get_user_info_fail);
        finish();
    }

    @Override
    public void loadUserInfoSuccess(UserInfo wbUserInfo) {
        mLoadingDialog.dismiss();
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ThirdPartyUtils.REQUEST_CODE_FOR_WEIBO && mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }else if(requestCode == WEIBO_WEB_LOGIN){
            if(resultCode != RESULT_OK || data == null){
                finish();
            }else{
                mWeiboAuthListener.onComplete(data.getExtras());
            }
        }
    }


    /**
     * 微博 Web 授权类，提供登陆等功能
     */
    private WeiboAuth mWeiboAuth;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    class WBAuthListener extends ParseTokenWeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            super.onComplete(values);
            ThirdPartyUtils.WeiboApp app = ThirdPartyUtils.WeiboApp.valueOf(values.getString("appKey", ThirdPartyUtils.WeiboApp.SHARE.name()));

            if (mAccessToken != null && mAccessToken.isSessionValid()) {
                String account = values.getString("account");
                String password = values.getString("password");
                if(app == ThirdPartyUtils.WeiboApp.SHARE){
                    mLoadingDialog.show();
                    UserUtil.updateUser(mAccessToken, account, password);
                    mPresenter.loadWBUserInfo(mAccessToken.getUid(), null);
                }else if(mAccessToken.getUid() != null){
                    User user = UserUtil.getUser(mAccessToken.getUid());
                    if(user != null) {
                        user.setAccount(account);
                        user.setPassword(password);
                        user.setAdToken(mAccessToken.getToken());
                        UserUtil.updateUser(user);
                    }
                    ToastUtil.showToastSuccess(R.string.tip_authorize_success);

                    mPresenter.loadWBUserInfo();
//                    setResult(Activity.RESULT_OK);
//                    finish();
                }

            } else {
                finish();
            }

        }

        @Override
        public void onCancel() {
            super.onCancel();
            finish();
        }
    }
}
