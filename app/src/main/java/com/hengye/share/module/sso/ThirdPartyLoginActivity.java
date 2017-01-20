package com.hengye.share.module.sso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.hengye.share.R;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;
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
        Intent intent = new Intent(context, ThirdPartyLoginActivity.class);
        intent.putExtra("isCurrentUser", isCurrentUser);
        return intent;
    }

    public static Intent getAdTokenStartIntent(Context context){
        Intent intent = new Intent(context, ThirdPartyLoginActivity.class);
        intent.putExtra("appKey", ThirdPartyUtils.WeiboApp.WEICO.name());
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
        boolean isCurrentUser = getIntent().getBooleanExtra("isCurrentUser", true);
        if(appName != null && appName.equals(ThirdPartyUtils.WeiboApp.WEICO.name())){
            startActivityForResult(WeiboWebAuthorizeActivity.getAdTokenStartIntent(this), WEIBO_WEB_LOGIN);
        }else {
            startActivityForResult(WeiboWebAuthorizeActivity.getStartIntent(this, ThirdPartyUtils.WeiboApp.SHARE, isCurrentUser), WEIBO_WEB_LOGIN);
        }
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
    public void loadSuccess(User user) {
        mLoadingDialog.dismiss();
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void loadFail() {
        mLoadingDialog.dismiss();
        ToastUtil.showToast(R.string.label_get_user_info_fail);
        finish();
    }

    @Override
    public void handleUserInfo(WBUserInfo wbUserInfo) {

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
                }else if(mAccessToken.getUid() != null && mAccessToken.getUid().equals(UserUtil.getUid())){
                    User user = UserUtil.getCurrentUser();
                    user.setAccount(account);
                    user.setPassword(password);
                    user.setAdToken(mAccessToken.getToken());
                    UserUtil.updateUser(user);
                    ToastUtil.showToast(R.string.tip_authorize_success);
                    setResult(Activity.RESULT_OK);
                    finish();
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
