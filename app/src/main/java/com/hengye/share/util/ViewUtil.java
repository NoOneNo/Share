package com.hengye.share.util;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.support.annotation.DimenRes;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.view.listener.OnScrollChangeCompatListener;
import com.hengye.share.ui.widget.scrollview.ObservableScrollView;

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
        return "#W" + maxWidth +
                "#H" + maxHeight + "#S" + scaleType.ordinal() + url;
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

    public static void hideKeyBoardOnTouch(View view, final View paramEditText) {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getActionMasked() == MotionEvent.ACTION_DOWN){
                    hideKeyBoard(paramEditText);
//                }
                return false;
            }
        });

    }
//    public static void hideKeyBoardOnScroll(ObservableScrollView scrollView, final View paramEditText) {
//        scrollView.addOnScrollChangeListener(new OnScrollChangeCompatListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                if(Math.abs(scrollY - oldScrollY) > getViewConfiguration().getScaledTouchSlop()){
//                    //onScroll Y
//                    hideKeyBoard(paramEditText);
//                }
//            }
//        });
//    }

    public static void hideKeyBoardOnScroll(RecyclerView recyclerView, final View paramEditText) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (RecyclerView.SCROLL_STATE_DRAGGING == newState) {
                    hideKeyBoard(paramEditText);
                }
            }
        });
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
        return getScreenWidth(context, false);
    }

    public static int getScreenWidth(Context context, boolean refresh) {
        if (screenWidth == 0 || refresh) {
            setScreenInfo(context);
        }
        return screenWidth;
    }

    public static int getScreenHeight(Context context) {
        return getScreenHeight(context, false);
    }

    public static int getScreenHeight(Context context, boolean refresh) {
        if (screenHeight == 0 || refresh) {
            setScreenInfo(context);
        }
        return screenHeight;
    }
}
