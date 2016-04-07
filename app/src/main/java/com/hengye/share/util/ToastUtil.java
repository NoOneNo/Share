package com.hengye.share.util;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.hengye.share.ui.base.BaseApplication;

public class ToastUtil extends ApplicationUtil{

    public static void showToast(CharSequence text){
        Toast toast = getToast();
        toast.setText(text);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
//        Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(@StringRes int resId){
        Toast toast = getToast();
        toast.setText(resId);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
//        Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackBar(CharSequence text, View view){
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    public static void showSnackBar(@StringRes int resId, View view){
        Snackbar.make(view, resId, Snackbar.LENGTH_SHORT).show();
    }

    private static Toast mToast;

    public static Toast getToast(){
        if(mToast == null){
            mToast = Toast.makeText(getContext(), 0, Toast.LENGTH_SHORT);
        }
        return mToast;
    }
}
