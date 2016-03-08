package com.hengye.share.util;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.hengye.share.ui.base.BaseApplication;

public class ToastUtil extends ApplicationUtil{

    public static void showToast(CharSequence text){
        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(@StringRes int resId){
        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackBar(CharSequence text, View view){
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackBar(@StringRes int resId, View view){
        Snackbar.make(view, resId, Snackbar.LENGTH_SHORT).show();
    }
}
