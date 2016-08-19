package com.hengye.share.util;

import java.util.regex.Pattern;

/**
 * Created by yuhy on 16/8/19.
 */
public class EncodeUtil {

    private final static Pattern CHINESE_PATTERN = Pattern.compile("[\u4e00-\u9fa5]");

    /**
     * 一个汉字在utf-8编码下,字节的长度是3,字母则是1;
     * @param s
     * @return
     */
    public static int getChineseLength(String s){
        float valueLength = 0;
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < s.length(); i++) {
            // 获取一个字符
            String temp = s.substring(i, i + 1);
            // 判断是否为中文字符
            if (CHINESE_PATTERN.matcher(temp).matches()) {
                // 中文字符长度为1
                valueLength += 1;
            } else {
                // 其他字符长度为0.5
                valueLength += 0.5;
            }
        }
        //进位取整
        return  Math.round(valueLength);
    }
}
