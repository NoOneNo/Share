package com.hengye.share.module.sina;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WBUtil {

    public final static int MAX_COUNT_REQUEST = 30;
    public final static int START_PAGE = 1;

    public final static String WB_USERNAME_REGEX = "@[^:： ]+(:|：| )";
    //从微博里得到匹配的@名字，正则表达式@[^:： ]+(:|：| )
    public static Map<Integer, String> getMatchAtWBName(String str){

        if(TextUtils.isEmpty(str)){
            return null;
        }

        Map<Integer, String> result = new HashMap<>();
        String regex = WB_USERNAME_REGEX;
        Matcher m = Pattern.compile(regex).matcher(str);
        while(m.find()){
            result.put(m.start(), m.group());
        }

        return result;
    }

    public static String IMAGE_TYPE_THUMBNAIL = "thumbnail";//缩略图
    public static String IMAGE_TYPE_BMIDDLE = "bmiddle";//高清
    public static String IMAGE_TYPE_LARGE = "large";//原图
    //默认返回缩略图
    //要得到高清图或者原图，把地址"http://ww1.sinaimg.cn/thumbnail/6dab804cjw1exv392snomj21kw23ukjl.jpg"
    //中的thumbnail换成对应的bmiddle(高清)或者large(原图)
    public static String getWBTopicImgUrl(String url, String toType){
        return getWBTopicImgUrl(url, IMAGE_TYPE_THUMBNAIL, toType);
    }

    public static String getWBTopicImgUrl(String url, String fromType, String toType){
        if(TextUtils.isEmpty(url)){
            return null;
        }
        return url.replaceFirst(fromType, toType);
    }
//    public static Pattern getMatchWBContentPattern(){
//        return Pattern.compile("@[^: ]+(:| )");
//    }
}
