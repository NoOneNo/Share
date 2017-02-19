package com.hengye.share.util;

import android.support.annotation.ColorRes;

import com.hengye.share.R;

/**
 * Created by yuhy on 16/8/16.
 */
public class ThemeUtil extends ResUtil{

    public static int getColor(){
//        return ResUtil.getAttrColor(R.attr.colorPrimary, 0);
        return ResUtil.getAttrColor(R.attr.theme_color, 0);
    }

    public static int getDarkColor(){
//        return ResUtil.getAttrColor(R.attr.colorPrimaryDark, 0);
        return ResUtil.getAttrColor(R.attr.theme_dark_color, 0);
    }

    public static int getAccentColor(){
//        return ResUtil.getAttrColor(R.attr.colorPrimaryDark, 0);
        return ResUtil.getAttrColor(R.attr.theme_accent_color, 0);
    }

    public static int getBackgroundColor(){
        return getColor(R.color.background_default);
    }

    public static int getTextColor(){
        return getUntingedColor();
    }

    public static int getTintColor(boolean isTinted){
        return isTinted ? getColor() : getUntingedColor();
    }

    public static int getUntingedColor(){
        return getColor(getUntingedColorResId());
    }

    public static int getIconTintColor(){
        return getColor(R.color.icon_grey);
    }

    public static int getTextTintLightBgColor(){
        return getColor(getTextTintLightBgResId());
    }

    public static int getTextTintDarkBgColor(){
        return getColor(getTextTintDarkBgResId());
    }

    public static int getDividerDrawable(){
        return ResUtil.getAttrColor(R.attr.theme_divider, 0);
    }

    private static @ColorRes int getUntingedColorResId(){
        return R.color.theme_untinged;
    }

    private static @ColorRes int getTextTintLightBgResId(){
        return R.color.theme_tint_light_bg;
    }

    private static @ColorRes int getTextTintDarkBgResId(){
        return R.color.theme_tint_dark_bg;
    }
}
