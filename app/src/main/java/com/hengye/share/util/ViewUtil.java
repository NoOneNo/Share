package com.hengye.share.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Vibrator;
import android.support.annotation.DimenRes;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.HapticFeedbackConstants;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.hengye.share.R;
import com.hengye.share.ui.base.BaseApplication;

import java.lang.reflect.Field;

public class ViewUtil extends ResUtil {

//    public static Bitmap mDrawingCacheBitmap;
//    public static Bitmap getDrawingCacheBitmap(){
//        return mDrawingCacheBitmap;
//    }
//    public static void setDrawingCacheBitmap(Bitmap bitmap){
//        mDrawingCacheBitmap = bitmap;
//    }

    public static int dp2px(@DimenRes int dpResId) {
        return dp2px(getDimenFloatValue(dpResId));
    }

    public static int dp2px(float dpValue) {
        float reSize = getContext().getResources().getDisplayMetrics().density;
        return (int) ((double) (dpValue * reSize) + 0.5D);
    }

    public static int px2dp(int pxValue) {
        float reSize = getContext().getResources().getDisplayMetrics().density;
        return (int) ((double) ((float) pxValue / reSize) + 0.5D);
    }

    public static int sp2px(@DimenRes int spResId) {
        return sp2px(getDimenFloatValue(spResId));
    }

    public static int sp2px(float spValue) {
        float reSize = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) ((double) (spValue * reSize) + 0.5D);
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, getContext().getResources().getDisplayMetrics());
    }

    public static String getCacheKey(String url, int maxWidth, int maxHeight, ImageView.ScaleType scaleType) {
        if (maxWidth == ViewGroup.LayoutParams.WRAP_CONTENT) {
            maxWidth = 0;
        }
        if (maxHeight == ViewGroup.LayoutParams.WRAP_CONTENT) {
            maxHeight = 0;
        }
        return new StringBuilder(url.length() + 12).append("#W").append(maxWidth)
                .append("#H").append(maxHeight).append("#S").append(scaleType.ordinal()).append(url)
                .toString();
    }

    public static void hideKeyBoard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void hideKeyBoard(View paramEditText) {
        ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }

    public static void showKeyBoard(final View paramEditText) {
        paramEditText.requestFocus();
        paramEditText.post(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .showSoftInput(paramEditText, 0);
            }
        });
    }

    public static int statusBarHeight = 0;

    public static int getStatusBarHeight() {
        if (statusBarHeight != 0) {
            return statusBarHeight;
        }

        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = getDimensionPixelSize(x);
            return statusBarHeight;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //    public static int getStatusBarHeight(Activity paramActivity) {
//        Rect localRect = new Rect();
//        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
//        return localRect.top;
//    }
    public static int getActionBarHeight() {
        return getDimensionPixelSize(android.R.attr.actionBarSize,
                getDimensionPixelSize(R.dimen.action_bar_default_size));
    }

    public static void vibrate(){
        Vibrator vibrator = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(300);
    }

    public static void vibrate(View view) {
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
    }

    public static void playClickSound(View view) {
        view.playSoundEffect(SoundEffectConstants.CLICK);
    }


    private static int screenWidth;

    private static int screenHeight;

    private static float density;

    private static void setScreenInfo(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        density = dm.density;
    }

    public static int getScreenWidth(Context context) {
        if (screenWidth == 0) {
            setScreenInfo(context);
        }
        return screenWidth;
    }

    public static int getScreenHeight(Context context) {
        if (screenHeight == 0) {
            setScreenInfo(context);
        }
        return screenHeight;
    }
}
