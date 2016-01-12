package com.hengye.share.ui.emoticon;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hengye.share.ui.base.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.util.SettingHelper;

public class EmoticonPickerUtil {

    public static void addContentToEditTextEnd(EditText editText, CharSequence charSequence){
        Editable editAble = editText.getEditableText();
        editAble.insert(getEditTextEndSelection(editText), charSequence);
    }

    public static int getEditTextEndSelection(EditText editText){
        int start = editText.getSelectionStart();
        if(start == -1){
            String str = editText.getText().toString();
            if(TextUtils.isEmpty(str)){
                start = 0;
            }else {
                start = str.length();
            }
        }
        return start;
    }

    public static void hideKeyBoard(View paramEditText) {
        ((InputMethodManager) BaseApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(paramEditText.getWindowToken(), 0);
    }

    public static void showKeyBoard(final View paramEditText) {
        paramEditText.requestFocus();
        paramEditText.post(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) BaseApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .showSoftInput(paramEditText, 0);
            }
        });
    }

    public static int getScreenHeight(Activity paramActivity) {
        Display display = paramActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

    public static int getStatusBarHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.top;
    }

    public static int getActionBarHeight(Activity paramActivity) {
        //test on samsung 9300 android 4.1.2, this value is 96px
        //but on galaxy nexus android 4.2, this value is 146px
        //statusbar height is 50px
        //I guess 4.1 Window.ID_ANDROID_CONTENT contain statusbar
//        int contentViewTop =
//                paramActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();

//        return contentViewTop - getStatusBarHeight(paramActivity);

        return getDimensionPixelSize(paramActivity, android.R.attr.actionBarSize,
                paramActivity.getResources().getDimensionPixelSize(R.dimen.action_bar_default_size));
    }

    //below status bar,include actionbar, above softkeyboard
    public static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }

    //below actionbar, above softkeyboard
    public static int getAppContentHeight(Activity paramActivity) {
        return EmoticonPickerUtil.getScreenHeight(paramActivity)
                - EmoticonPickerUtil.getStatusBarHeight(paramActivity)
                - EmoticonPickerUtil.getActionBarHeight(paramActivity)
                - EmoticonPickerUtil.getKeyboardHeight(paramActivity);
    }

    public static int getKeyboardHeight(Activity paramActivity) {

        int height = EmoticonPickerUtil.getScreenHeight(paramActivity)
                - EmoticonPickerUtil.getStatusBarHeight(paramActivity)
                - EmoticonPickerUtil.getAppHeight(paramActivity);
        if (height == 0) {
            height = getDefaultSoftKeyBoardHeight();
        }else{
            setDefaultSoftKeyBoardHeight(height);
        }

        return height;
    }

    public static boolean isKeyBoardShow(Activity paramActivity) {
        int height = EmoticonPickerUtil.getScreenHeight(paramActivity)
                - EmoticonPickerUtil.getStatusBarHeight(paramActivity)
                - EmoticonPickerUtil.getAppHeight(paramActivity);
        return height != 0;
    }

    public static int getDimensionPixelSize(Activity activity, int attr, int defaultValue) {
        int[] attrs = new int[]{attr};
        TypedArray ta = activity.obtainStyledAttributes(attrs);
        int value = ta.getDimensionPixelSize(0, defaultValue);
        ta.recycle();
        return value;
    }

    public final static String KEY_DEFAULT_KEYBOARD_HEIGHT = "keyboard_height";
    public static void setDefaultSoftKeyBoardHeight(int height) {
        SharedPreferences.Editor editor = SettingHelper.getPreferences().edit();
        editor.putInt(KEY_DEFAULT_KEYBOARD_HEIGHT, height);
        editor.apply();
    }

    public static int getDefaultSoftKeyBoardHeight() {
        return SettingHelper.getPreferences().getInt(KEY_DEFAULT_KEYBOARD_HEIGHT, 400);
    }
}
