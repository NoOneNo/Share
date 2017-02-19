package com.hengye.share.util.thirdparty;

import android.text.TextUtils;
import android.util.SparseArray;

import com.android.volley.toolbox.Util;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.util.ImageUtil;
import com.hengye.share.util.NetworkUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WBUtil {

    public static final String SLASH = "/";

    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";

    public static final String URL_IMG_1 = "http://ww1.sinaimg.cn/";

    //私信入口
    public static final String URL_MOBILE_HOST = "m.weibo.cn";
    public static final String URL_LOGIN_HOST = "passport.weibo.cn/signin/login";
    public static final String URL_PHOTO = "photo.weibo.com";

    public static final String URL_HTTP_MOBILE = HTTP + URL_MOBILE_HOST;
    public static final String URL_HTTP_LOGIN = HTTP + URL_LOGIN_HOST;
    public static final String URL_HTTP_PHOTO = HTTP + URL_PHOTO;

    public static final String URL_HTTPS_MOBILE = HTTPS + URL_MOBILE_HOST;
    public static final String URL_HTTPS_LOGIN = HTTPS + URL_LOGIN_HOST;
    public static final String URL_HTTPS_PHOTO = HTTPS + URL_PHOTO;

    public final static int MAX_COUNT_REQUEST = 30;
    public final static int START_PAGE = 1;

    public static String IMAGE_TYPE_THUMBNAIL = "thumbnail";//缩略图，模糊，不建议使用，新浪微博默认返回这个
    public static String IMAGE_TYPE_BMIDDLE = "bmiddle";//高清，部分gif也不支持显示，缩略图建议用or480
    public static String IMAGE_TYPE_OR_480 = "or480";//高清不支持gif动图，只有一帧如果是gif的话
    public static String IMAGE_TYPE_WAP_720 = "wap720";//高清不支持gif动图，只有一帧如果是gif的话
    public static String IMAGE_TYPE_LARGE = "large";//原图
    public static String IMAGE_TYPE_ORIGINAL = "woriginal";//原图, 新浪微博gif用这个，

    public static int getWBStatusRequestCount() {
//        return 2;
        return SettingHelper.getLoadCount();
    }

    //默认返回缩略图
    //要得到高清图或者原图，把地址"http://ww1.sinaimg.cn/thumbnail/6dab804cjw1exv392snomj21kw23ukjl.jpg"
    //中的thumbnail换成对应的bmiddle(高清)或者large(原图)

    public static String getWBImgType() {
        String value = SettingHelper.getPhotoDownloadQuality();
        String toType;
        if ("1".equals(value)) {
            //无图
            toType = null;
        } else if ("2".equals(value)) {
            //缩略图
            toType = IMAGE_TYPE_OR_480;
        } else if ("3".equals(value)) {
            //高清图
            toType = IMAGE_TYPE_BMIDDLE;
        } else if ("4".equals(value)) {
            //原图
            toType = IMAGE_TYPE_LARGE;
        } else if ("5".equals(value)) {
            //自动
            toType = NetworkUtil.isWifiType() ?
                    IMAGE_TYPE_BMIDDLE :
                    IMAGE_TYPE_OR_480;
        } else {
            toType = IMAGE_TYPE_BMIDDLE;
        }
        return toType;
    }

    public static String getWBGifUrl(String url) {
        if (url != null && url.endsWith("gif")) {
            if (url.contains(IMAGE_TYPE_BMIDDLE)) {
                return url.replaceFirst(IMAGE_TYPE_BMIDDLE, IMAGE_TYPE_ORIGINAL);
            } else if (url.contains(IMAGE_TYPE_OR_480)) {
                return url.replaceFirst(IMAGE_TYPE_OR_480, IMAGE_TYPE_ORIGINAL);
            } else if (url.contains(IMAGE_TYPE_THUMBNAIL)) {
                return url.replaceFirst(IMAGE_TYPE_THUMBNAIL, IMAGE_TYPE_ORIGINAL);
            }
        }
        return url;
    }

    public static boolean isWBGifUrl(String url) {
        if (ImageUtil.isThisPictureGif(url)) {
            if (Util.isHttpUrl(url)) {
                return url.contains(IMAGE_TYPE_ORIGINAL);
            } else {
                return true;
            }
        }
        return false;
    }

    public static boolean isWBLargeUrl(String url) {
        return url != null && url.contains(IMAGE_TYPE_ORIGINAL);
    }

    public static boolean isWBThumbnailUrl(String url) {
        if (url != null) {
            if (url.contains(IMAGE_TYPE_BMIDDLE)) {
                return false;
            } else if (url.contains(IMAGE_TYPE_ORIGINAL)) {
                return false;
            }
        }
        return true;
    }

    public static String getWBImgUrlById(String picId) {
        return URL_IMG_1 + IMAGE_TYPE_ORIGINAL + SLASH + picId;
    }

    public static String getWBMiddleImgUrl(String url) {
        return getWBImgUrl(url, IMAGE_TYPE_BMIDDLE);
    }

    public static String getWBLargeImgUrl(String url) {
        return getWBImgUrl(url, IMAGE_TYPE_ORIGINAL);
    }

    public static String getWBImgUrl(String url) {
        return getWBImgUrl(url, getWBImgType());
    }

    public static String getWBImgUrl(String url, String toType) {
        if (url != null) {
            if (url.contains(IMAGE_TYPE_THUMBNAIL)) {
                return getWBImgUrl(url, IMAGE_TYPE_THUMBNAIL, toType);
            } else if (url.contains(IMAGE_TYPE_OR_480)) {
                return getWBImgUrl(url, IMAGE_TYPE_OR_480, toType);
            } else if (url.contains(IMAGE_TYPE_BMIDDLE)) {
                return getWBImgUrl(url, IMAGE_TYPE_BMIDDLE, toType);
            } else {
                return url;
            }
        }
        return null;
    }

    public static String getWBImgUrl(String url, String fromType, String toType) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        return url.replaceFirst(fromType, toType);
    }

    //这样判断不太好，这种链接少。
    @Deprecated
    public static boolean isWBAccountIdLink(String url) {
        url = convertWBCnToWBCom(url);
        return !TextUtils.isEmpty(url) && url.startsWith("http://weibo.com/u/");
    }

    private static String convertWBCnToWBCom(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith("http://weibo.cn")) {
                url = url.replace("http://weibo.cn", "http://weibo.com");
            } else if (url.startsWith("http://www.weibo.com")) {
                url = url.replace("http://www.weibo.com", "http://weibo.com");
            } else if (url.startsWith("http://www.weibo.cn")) {
                url = url.replace("http://www.weibo.cn", "http://weibo.com");
            }
        }
        return url;
    }

    //todo need refactor
    @Deprecated
    public static String getIdFromWBAccountLink(String url) {
        url = convertWBCnToWBCom(url);
        String id = url.substring("http://weibo.com/u/".length());
        id = id.replace("/", "");
        return id;
    }

    public final static String WB_USERNAME_REGEX = "@[^:： ]+(:|：| |\\s)";

    //旧：@[^:： ]+(:|：| |\s)
    //从微博里得到匹配的@名字，正则表达式@[^:： ]+(:|：| |\s)
    @Deprecated
    public static SparseArray<String> getMatchAtWBName(String str) {

        if (TextUtils.isEmpty(str)) {
            return null;
        }

        SparseArray<String> result = new SparseArray<>();
        String regex = WB_USERNAME_REGEX;
        Matcher m = Pattern.compile(regex).matcher(str);
        while (m.find()) {
            result.put(m.start(), m.group());
        }

        return result;
    }

    //http://weibo.com/login.php?url=http://photo.weibo.com/h5/repost/reppic_id/1022:23079681893d3893bca4a76d240398067f10c4
    public static boolean isRequestWBResourceUrl(String url) {
        if (url == null) {
            return false;
        }

        if (url.contains(URL_HTTP_PHOTO)
                || url.contains(URL_HTTPS_PHOTO)
                || url.contains(URL_HTTP_MOBILE)
                || url.contains(URL_HTTPS_MOBILE)) {
            return true;
        }
        return false;
    }

}
