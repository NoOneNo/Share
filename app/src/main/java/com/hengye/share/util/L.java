package com.hengye.share.util;

import com.hengye.share.BuildConfig;

import timber.log.Timber;

public class L {

    /**
     * 手机Debug开关命令 adb shell setprop log.tag.com.hengye.share.util.L VERBOSE
     */

//    public final static Logger L = LoggerFactory.getLogger(L.class);

    public static void init(){
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }
    }

    public static void debug(String message, Object... args) {
//        L.debug(message, args);
        Timber.d(message, args);
    }

    public static void info(String message, Object... args) {
        Timber.i(message, args);
    }

    public static void warn(String message, Object... args) {
        Timber.w(message, args);
    }

    public static void error(String message, Object... args) {
        Timber.e(message, args);
    }


    /** A tree which logs important information for crash reporting. */
    private static class CrashReportingTree extends Timber.Tree {
        @Override protected void log(int priority, String tag, String message, Throwable t) {
            //nothing
        }
    }
}
