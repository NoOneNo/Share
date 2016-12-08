package com.greenDao;

import java.util.zip.CRC32;

/**
 * Created by yuhy on 16/7/11.
 */
public class Test {

    public static void main(String[] args){

//        long time = System.currentTimeMillis();

//        crc32.reset();
//
//        crc32.update((str1 + str2 + str3).getBytes());
//        System.out.println("test2 : " + crc32.getValue());
        System.out.println("url : " + rot47R("https://api.weibo.com/oauth2/default.html"));
        System.out.println("url : " + rot47("9EEADi^^2A:]H6:3@]4@>^@2FE9a^5672F=E]9E>="));
        System.out.println("key : " + rot47("`_edd``d`b"));
//        System.out.println("key : " + rot47("JAAHABYZEA=="));
    }

    public static long crc32(String value){
        CRC32 crc32 = new CRC32();
        crc32.update(value.getBytes());
        return crc32.getValue();
    }

    public static String rot47R(String value) {
        int length = value.length();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);

            // Process letters, numbers, and symbols -- ignore spaces.
            if (c != ' ') {
                // Add 47 (it is ROT-47, after all).
                c -= 47;

                // If character is now above printable range, make it printable.
                // Range of printable characters is ! (33) to ~ (126).  A value
                // of 127 (just above ~) would therefore get rotated down to a
                // 33 (the !).  The value 94 comes from 127 - 33 = 94, which is
                // therefore the value that needs to be subtracted from the
                // non-printable character to put it into the correct printable
                // range.
                if (c > '~' || c < '!') {
                    c += 94;
                }
            }

            result.append(c);
        }

        return result.toString();
    }

    public static String rot47(String value) {
        int length = value.length();
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < length; i++) {
            char c = value.charAt(i);

            // Process letters, numbers, and symbols -- ignore spaces.
            if (c != ' ') {
                // Add 47 (it is ROT-47, after all).
                c += 47;

                // If character is now above printable range, make it printable.
                // Range of printable characters is ! (33) to ~ (126).  A value
                // of 127 (just above ~) would therefore get rotated down to a
                // 33 (the !).  The value 94 comes from 127 - 33 = 94, which is
                // therefore the value that needs to be subtracted from the
                // non-printable character to put it into the correct printable
                // range.
                if (c > '~') {
                    c -= 94;
                }
            }

            result.append(c);
        }

        return result.toString();
    }
}
