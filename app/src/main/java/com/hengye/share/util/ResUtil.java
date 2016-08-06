package com.hengye.share.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.TypedValue;

/**
 * Created by yuhy on 16/6/14.
 */
public class ResUtil extends ApplicationUtil {

    @NonNull
    public static String getString(@StringRes int id){
        return getResources().getString(id);
    }

    @NonNull
    public static String getString(@StringRes int id, Object... formatArgs){
        final String raw = getString(id);
        return String.format(raw, formatArgs);
    }

    public static int getColor(@ColorRes int id){
        return getResources().getColor(id);
    }

    public static Drawable getDrawable(@DrawableRes int id){
        return getResources().getDrawable(id);
    }

    public static TypedValue mTmpValue;

    public static int getDimenIntValue(@DimenRes int id){
        return (int) getDimenFloatValue(id);
    }

    /**
     * 得到dp单位的值, 如果是16dp则结果为16
     * @param id
     * @return
     */
    public static float getDimenFloatValue(@DimenRes int id){
        TypedValue value = mTmpValue;
        if (value == null) {
            mTmpValue = value = new TypedValue();
        }
        getResources().getValue(id, value, true);
        if (value.type == TypedValue.TYPE_DIMENSION) {
            return ((int)TypedValue.complexToFloat(value.data));
        }
        throw new Resources.NotFoundException(
                "Resource ID #0x" + Integer.toHexString(id) + " type #0x"
                        + Integer.toHexString(value.type) + " is not valid");
    }

    public static int getDimensionPixelSize(@DimenRes int id) throws Resources.NotFoundException {
        return getResources().getDimensionPixelSize(id);
    }
}
