package com.hengye.share.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class L {

    /**
     * 手机Debug开关命令 adb shell setprop log.tag.com.hengye.share.util.L VERBOSE
     */

    public final static Logger L = LoggerFactory.getLogger(L.class);

    public static void debug(String msg) {
        L.info(msg);
    }

    public static void debug(String format, Object arg) {
        L.info(format, arg);
    }

    public static void debug(String format, Object arg1, Object arg2) {
        L.info(format, arg1, arg2);
    }

    public static void debug(String format, Object... argArray) {
        L.info(format, argArray);
    }

    public static void info(String msg) {
        L.info(msg);
    }

    public static void info(String format, Object arg) {
        L.info(format, arg);
    }


    public static void info(String format, Object arg1, Object arg2) {
        L.info(format, arg1, arg2);
    }

    public static void info(String format, Object... argArray) {
        L.info(format, argArray);
    }
}
