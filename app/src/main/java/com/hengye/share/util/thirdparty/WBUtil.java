package com.hengye.share.util.thirdparty;

import android.text.TextUtils;

import com.hengye.share.helper.SettingHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WBUtil {

    public final static int MAX_COUNT_REQUEST = 30;
    public final static int START_PAGE = 1;

    public static String IMAGE_TYPE_THUMBNAIL = "thumbnail";//缩略图
    public static String IMAGE_TYPE_BMIDDLE = "bmiddle";//高清
    public static String IMAGE_TYPE_LARGE = "large";//原图

    public static int getWBTopicRequestCount() {
        return SettingHelper.getLoadCount();
    }

    //默认返回缩略图
    //要得到高清图或者原图，把地址"http://ww1.sinaimg.cn/thumbnail/6dab804cjw1exv392snomj21kw23ukjl.jpg"
    //中的thumbnail换成对应的bmiddle(高清)或者large(原图)
    public static String getWBTopicImgUrl(String url){
        String value = SettingHelper.getPhotoDownloadQuality();
        if("1".equals(value)){
            //无图
            return null;
        }
        String toType = IMAGE_TYPE_BMIDDLE;
        if("2".equals(value)){
            toType = IMAGE_TYPE_THUMBNAIL;
        }else if("3".equals(value)){
            toType = IMAGE_TYPE_BMIDDLE;
        }else if("4".equals(value)){
            toType = IMAGE_TYPE_LARGE;
        }else if("5".equals(value)){
            toType = IMAGE_TYPE_BMIDDLE;
        }
        return getWBTopicImgUrl(url, toType);
    }

    public static String getWBTopicImgUrl(String url, String toType) {
        return getWBTopicImgUrl(url, IMAGE_TYPE_THUMBNAIL, toType);
    }

    public static String getWBTopicLargeImgUrl(String url) {
        return getWBTopicImgUrl(url, IMAGE_TYPE_THUMBNAIL, IMAGE_TYPE_LARGE);
    }

    public static String getWBTopicImgUrl(String url, String fromType, String toType) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        return url.replaceFirst(fromType, toType);
    }

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
    public static Map<Integer, String> getMatchAtWBName(String str) {

        if (TextUtils.isEmpty(str)) {
            return null;
        }

        Map<Integer, String> result = new HashMap<>();
        String regex = WB_USERNAME_REGEX;
        Matcher m = Pattern.compile(regex).matcher(str);
        while (m.find()) {
            result.put(m.start(), m.group());
        }

        return result;
    }

}
