package com.hengye.share.util;



import android.os.Bundle;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

/**
 * 微博认证授权回调类。
 * 1. SSO 授权时，需要在 {@link android.app.Activity#onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
 * 该回调才会被执行。
 * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
 * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
 */
public class CustomWeiboAuthListener implements WeiboAuthListener {

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    protected Oauth2AccessToken mAccessToken;

    @Override
    public void onComplete(Bundle values) {
        L.debug("sso : login by weibo success");
        // 从 Bundle 中解析 Token
        mAccessToken = Oauth2AccessToken.parseAccessToken(values);
        if (mAccessToken.isSessionValid()) {
            SPUtil.getInstance().setSinaAccessToken(mAccessToken);
            //请求服务器登录或注册
//                loginByThirdParty(mAccessToken.getUid(), mAccessToken.getToken(), ThirdPartyConstants.TYPE_FOR_WEIBO);
        } else {
            // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
//                String code = values.getString("code");
        }
    }

    @Override
    public void onCancel() {
        L.debug("sso : login by weibo cancel");
    }

    @Override
    public void onWeiboException(WeiboException e) {
        L.debug("sso : login by weibo occur exception : {}", e.toString());
    }
}
