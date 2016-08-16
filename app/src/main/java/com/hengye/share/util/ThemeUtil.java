package com.hengye.share.util;

import android.support.annotation.ColorRes;

import com.hengye.share.R;

/**
 * Created by yuhy on 16/8/16.
 */
public class ThemeUtil extends ResUtil{

    public static int getColor(){
        return ResUtil.getAttrColor(R.attr.colorPrimary, 0);
    }

    public static int getDarkColor(){
        return ResUtil.getAttrColor(R.attr.colorPrimaryDark, 0);
    }

    public static int getAccentColor(){
        return ResUtil.getAttrColor(R.attr.colorPrimaryDark, 0);
    }

    public static int getTextColor(){
        return getTintColor();
    }

    public static int getTintColor(){
        return getColor(getTintColorResId());
    }

    public static int getTintLightColor(){
        return getColor(getTintLightColorResId());
    }

    public static int getTintDarkColor(){
        return getColor(getTintDarkColorResId());
    }

    public static @ColorRes int getTintColorResId(){
        return R.color.theme_tint_color;
    }

    public static @ColorRes int getTintLightColorResId(){
        return R.color.theme_tint_light_color;
    }

    public static @ColorRes int getTintDarkColorResId(){
        return R.color.theme_tint_dark_color;
    }
}
