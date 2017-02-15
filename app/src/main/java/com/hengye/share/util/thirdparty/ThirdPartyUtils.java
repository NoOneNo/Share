package com.hengye.share.util.thirdparty;


import android.content.Context;

import com.hengye.share.util.EncryptUtils;
import com.sina.weibo.sdk.auth.WeiboAuth;

/**
 * 微博测试
 * data : uid: 2207519004,
 * access_token: 2.00OXW56CiGSdcC7b3b00d3940PueHq,
 * refresh_token: 2.00OXW56CiGSdcC4996785380bN3LwC,
 * expires_in: 1603552695140
 */
public class ThirdPartyUtils {

    public enum WeiboApp {
        SHARE,
        WEICO
    }

    public static final int TYPE_FOR_WEIBO = 1;

    public static final int TYPE_FOR_QQ = 2;

    public static final int TYPE_FOR_WECHAT = 3;

    public static final int REQUEST_CODE_FOR_WEIBO = 2;
    /**
     * 当前 DEMO 应用的 APP_KEY_FOR_WEIBO，第三方应用应该使用自己的 APP_KEY_FOR_WEIBO 替换该 APP_KEY_FOR_WEIBO
     */
    private static final String APP_KEY_FOR_WEIBO = EncryptUtils.decode("ac_b`b_gba");
    private static final String APP_SECRET_FOR_WEIBO = EncryptUtils.decode("a67`46c5`bch264377a4372fbebf4bg4");
//    public static final String APP_KEY_FOR_WEIBO = "2403130832";
//    public static final String APP_SECRET_FOR_WEIBO = "2ef1ce4d1349aecbff2cbfa73637c38c";

    private static final String APP_KEY_FOR_WEICO_WEIBO = EncryptUtils.decode("a```e_efh");
    private static final String APP_SECRET_FOR_WEICO_WEIBO = EncryptUtils.decode("eb3ec5db`3hg4a5377accbg`e7afc55b");
//    public sttic final String APP_KEY_FOR_WEICO_WEIBO = "211160679";
//    public static final String APP_SECRET_FOR_WEICO_WEIBO = "63b64d531b98c2dbff2443816f274dd3";

    /**
     * 腾讯bugly安卓SDK的key
     */
    private static final String APP_KEY_FOR_BUGLY = EncryptUtils.decode("h___`hcba");
//    private static final String APP_KEY_FOR_BUGLY = EncryptUtils.decode("900019432");

    /**
     * 高德地图安卓SDK的key
     */
    private static final String APP_KEY_FOR_AMAP_ANDROID = EncryptUtils.decode("24hh52`2b4e34ha4f36`6766355fec62");

    /**
     * 高德地图WebApi的key
     */
    private static final String APP_KEY_FOR_AMAP_WEB = EncryptUtils.decode("7e4f3a23hfhh27bgfe76`d44ee47`3eb");
//    public static final String APP_KEY_AMAP_FOR_ANDROID = "ac99da1a3c6bc92c7be1efeebdd764ea";
//    public static final String APP_KEY_AMAP_FOR_WEB = "f6c7b2ab9799af3876fe15cc66cf1b63";


    public static final String DIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String DIRECT_URL_FOR_WEICO = "http://oauth.weico.cc";

    public static boolean isDebug = false;

    public static String getAppKeyForWeibo() {
        return APP_KEY_FOR_WEIBO;
    }

    public static String getAppKeyForWeico() {
        return APP_KEY_FOR_WEICO_WEIBO;
    }

    public static String getAppSecretForWeibo() {
        return APP_SECRET_FOR_WEIBO;
    }

    public static String getAppKeyForBugly() {
        return APP_KEY_FOR_BUGLY;
    }

    public static String getAppKeyForAMapAndroid() {
        return APP_KEY_FOR_AMAP_ANDROID;
    }

    public static String getAppKeyForAMapWeb() {
        return APP_KEY_FOR_AMAP_WEB;
    }

    public static String getWeiboRedirectUrl() {
        return DIRECT_URL;
    }

    public static String getAppKeyForWeibo(WeiboApp appKey) {
        if (appKey == WeiboApp.SHARE) {
            return APP_KEY_FOR_WEIBO;
        } else {
            return APP_KEY_FOR_WEICO_WEIBO;
        }
    }

    public static String getAppSecretForWeibo(WeiboApp appKey) {
        if (appKey == WeiboApp.SHARE) {
            return APP_SECRET_FOR_WEIBO;
        } else {
            return APP_SECRET_FOR_WEICO_WEIBO;
        }
    }

    public static String getWeiboRedirectUrl(WeiboApp appKey) {
        if (appKey == WeiboApp.SHARE) {
            return DIRECT_URL;
        } else {
            return DIRECT_URL_FOR_WEICO;
        }
    }

    public static WeiboAuth getWeiboData(Context context) {
        // 创建微博实例
        return new WeiboAuth(context, getAppKeyForWeibo(), REDIRECT_URL, SCOPE_FOR_WEIBO);
    }

//    public static String getKuxiaoAppKeyForQq(){
//        if(!isDebug){
//            return Config.getKuxiaoAppIdForQQ();
//        }else{
//            return KUXIAO_APP_KEY_FOR_QQ_TEST;
//        }
//    }
//
//    public static String getKuxiaoAppKeyForWechat(){
//        if(!isDebug){
//            return Config.getKuxiaoAppIdForWechat();
//        }else{
//            return KUXIAO_APP_KEY_FOR_WECHAT_TEST;
//        }
//    }
//
//    public static String getKuxiaoAppSecretForWechat(){
//        if(!isDebug){
//            return Config.getKuxiaoAppSecretForWechat();
//        }else{
//            return KUXIAO_APP_SECRET_FOR_WECHAT_TEST;
//        }
//    }

    public static void setDebug(boolean isDebug) {
        ThirdPartyUtils.isDebug = isDebug;
    }

    private static ThirdPartyLoginData tpld;

    public static ThirdPartyLoginData getTpld() {
        return tpld;
    }

    public static void setTpld(ThirdPartyLoginData tpld) {
        ThirdPartyUtils.tpld = tpld;
    }

    public class ThirdPartyLoginData {
        private String access_token;
        private String expires_in;
        private String refresh_token;
        private String openid;
        private String scope;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public void setRefresh_token(String refresh_token) {
            this.refresh_token = refresh_token;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }
    }

    /**
     * 当前 DEMO 应用的回调页，第三方应用可以使用自己的回调页。
     * <p>
     * <p>
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "http://www.kuiao.cn";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     * <p/>
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     * <p/>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p/>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE_FOR_WEIBO =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";
}
