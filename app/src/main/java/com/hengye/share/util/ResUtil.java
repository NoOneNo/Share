package com.hengye.share.util;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

/**
 * Created by yuhy on 16/6/14.
 */
public class ResUtil extends ApplicationUtil {

    protected static Resources getResources(){
        return getContext().getResources();
    }

    @NonNull
    public String getString(@StringRes int id){
        return getResources().getString(id);
    }

    @NonNull
    public String getString(@StringRes int id, Object... formatArgs){
        final String raw = getString(id);
        return String.format(raw, formatArgs);
    }

}
