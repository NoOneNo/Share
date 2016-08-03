package com.hengye.share.util;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by yuhy on 16/8/3.
 */
public class HandlerUtil {

    /** Lazy initialization via inner-class holder. */
    private static class HandlerHolder {
        /** A singleton instance. */
        private final static Handler INSTANCE = new Handler(Looper.getMainLooper());
    }

    /**
     * @return a singleton instance of this stateless operator.
     */
    public static Handler getInstance() {
        return HandlerHolder.INSTANCE;
    }


}
