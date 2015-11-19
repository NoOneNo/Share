package com.hengye.share.util;

import android.os.Bundle;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.hengye.share.module.Topic;
import com.hengye.share.module.UserInfo;
import com.hengye.share.module.sina.WBUserInfo;
import com.hengye.volleyplus.toolbox.RequestManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

public class SaveUserInfoWeiboAuthListener extends ParseTokenWeiboAuthListener {

    @Override
    public void onComplete(Bundle values) {
        // 从 Bundle 中解析 Token
        super.onComplete(values);
        if (mAccessToken != null && mAccessToken.isSessionValid()) {
            SPUtil.setSinaAccessToken(mAccessToken);
            RequestManager.addToRequestQueue(RequestFactory.getInstance().
                    getWBUserInfoRequest(mAccessToken.getToken(), mAccessToken.getUid()));
        } else {
            // 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
//                String code = values.getString("code");
        }
    }

    @Override
    public void onCancel() {
        super.onCancel();
    }

    @Override
    public void onWeiboException(WeiboException e) {
        super.onWeiboException(e);
    }

}
