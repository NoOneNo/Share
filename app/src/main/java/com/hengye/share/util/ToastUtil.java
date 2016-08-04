package com.hengye.share.util;

import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.hengye.share.R;
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
        showToast(ResUtil.getString(resId));
    }

    public static void showSnackBar(CharSequence text, View view){
        getSnackBar(text, view).show();
    }

    public static void showSnackBar(@StringRes int resId, View view){
        getSnackBar(resId, view).show();
    }

    public static Snackbar getSnackBar(@StringRes int resId, View view) {
        return getSnackBar(ResUtil.getString(resId), view);
    }

    public static Snackbar getSnackBar(CharSequence text, View view){
        return Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
    }

    private static Toast mToast;

    public static Toast getToast(){
        if(mToast == null){
            mToast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
        }
        return mToast;
    }

    public static void showNetWorkErrorToast(){
        showToast(R.string.tip_no_network);
    }
}
