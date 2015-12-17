package com.hengye.share.util;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import com.hengye.share.BaseApplication;

public class ToastUtil {

    public static void showToast(CharSequence text){
        Toast.makeText(BaseApplication.getInstance(), text, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackBar(CharSequence text, View view){
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }
}
