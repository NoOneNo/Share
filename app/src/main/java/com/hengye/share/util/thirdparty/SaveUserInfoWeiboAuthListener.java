package com.hengye.share.util.thirdparty;

import android.os.Bundle;

import com.hengye.share.util.RequestFactory;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.UserUtil;
import com.sina.weibo.sdk.exception.WeiboException;

public class SaveUserInfoWeiboAuthListener extends ParseTokenWeiboAuthListener {

    @Override
    public void onComplete(Bundle values) {
        // 从 Bundle 中解析 Token
        super.onComplete(values);
        if (mAccessToken != null && mAccessToken.isSessionValid()) {
            SPUtil.setSinaAccessToken(mAccessToken);
            UserUtil.updateUser(mAccessToken);

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
