package com.hengye.share.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.util.TypedValue;
import android.view.ViewConfiguration;

import com.hengye.share.module.base.BaseActivity;

/**
 * Created by yuhy on 16/6/14.
 */
public class ResUtil extends ApplicationUtil {

    private static ViewConfiguration mViewConfiguration;

    public static ViewConfiguration getViewConfiguration(){
        if(mViewConfiguration == null){
            mViewConfiguration = ViewConfiguration.get(getContext());
        }
        return mViewConfiguration;
    }

    public static Resources getResources(){
        return getContext().getResources();
    }

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

    public static int getAttrColor(@AttrRes int attr, int defaultValue){
        Context context = BaseActivity.getCurrentActivity();
        if(context == null){
            context = getContext();
        }
        return getAttrColor(context, attr, defaultValue);
    }

    public static int getAttrColor(Context context, @AttrRes int attr, int defaultValue){
        int[] attrs = new int[]{attr};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int value = ta.getColor(0, defaultValue);
        ta.recycle();
        return value;
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

    public static int getDimensionPixelSize(@AttrRes int attr, int defaultValue) {
        int[] attrs = new int[]{attr};
        TypedArray ta = getContext().obtainStyledAttributes(attrs);
        int value = ta.getDimensionPixelSize(0, defaultValue);
        ta.recycle();
        return value;
    }
}
