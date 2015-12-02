package com.hengye.share.module.sina;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WBUtil {

    public final static int MAX_COUNT_REQUEST = 30;
    public final static int START_PAGE = 1;

    //从微博里得到匹配的@名字，正则表达式@[^:： ]+(:|：| )
    public static Map<Integer, String> getMatchAtWBName(String str){

        if(TextUtils.isEmpty(str)){
            return null;
        }

        Map<Integer, String> result = new HashMap<>();
        String regex = "@[^:： ]+(:|：| )";
        Matcher m = Pattern.compile(regex).matcher(str);
        while(m.find()){
            result.put(m.start(), m.group());
        }

        return result;
    }

    public static Pattern getMatchWBContentPattern(){
        return Pattern.compile("@[^: ]+(:| )");
    }
}
