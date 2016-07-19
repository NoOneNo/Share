package com.hengye.share.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
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
        return (int)((double)(dpValue * reSize) + 0.5D);
    }

    public static int px2dp(int pxValue) {
        float reSize = getContext().getResources().getDisplayMetrics().density;
        return (int)((double)((float)pxValue / reSize) + 0.5D);
    }

    public static int sp2px(@DimenRes int spResId) {
        return sp2px(getDimenFloatValue(spResId));
    }

    public static int sp2px(float spValue) {
        float reSize = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int)((double)(spValue * reSize) + 0.5D);
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

    public static int getStatusBarHeight() {
        return getStatusBarHeight(getContext());
    }

    public static int getStatusBarHeight(Context context) {
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            return context.getResources().getDimensionPixelSize(x);
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
        return getDimensionPixelSize(getContext(), android.R.attr.actionBarSize,
                getContext().getResources().getDimensionPixelSize(R.dimen.action_bar_default_size));
    }

    public static int getDimensionPixelSize(Context context, int attr, int defaultValue) {
        int[] attrs = new int[]{attr};
        TypedArray ta = context.obtainStyledAttributes(attrs);
        int value = ta.getDimensionPixelSize(0, defaultValue);
        ta.recycle();
        return value;
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * RecyclerView has been clicked.
     */
    public interface OnItemClickListener {

        /**
         * Callback method to be invoked when an item in this RecyclerView has
         * been clicked.
         * <p>
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param view     The view within the AdapterView that was clicked (this
         *                 will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         */
        void onItemClick(View view, int position);
    }

    /**
     * Interface definition for a callback to be invoked when an item in this
     * RecyclerView has been clicked.
     */
    public interface OnItemLongClickListener {

        /**
         * Callback method to be invoked when an item in this RecyclerView has
         * been clicked.
         * <p>
         * Implementers can call getItemAtPosition(position) if they need
         * to access the data associated with the selected item.
         *
         * @param view     The view within the AdapterView that was clicked (this
         *                 will be a view provided by the adapter)
         * @param position The position of the view in the adapter.
         */
        boolean onItemLongClick(View view, int position);
    }
}
