package com.hengye.share.ui.widget.dialog;

import android.app.Dialog;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.hengye.share.R;
import com.hengye.share.module.base.BaseApplication;

public class DialogStyleHelper {

    public static void setMenuDialogUpperRight(Dialog dialog){
        Window window = dialog.getWindow();

        window.setWindowAnimations(R.style.DialogAnimationScaleDiagonal);

        //        this.getWindow().getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.END | Gravity.TOP;
        lp.x = 50;

        TypedValue tv = new TypedValue();
        if (BaseApplication.getInstance().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            lp.y = TypedValue.complexToDimensionPixelSize(tv.data, dialog.getContext().getResources().getDisplayMetrics());
        }
        window.setAttributes(lp);
    }

    public static void setWebViewListDialogStyle(ListDialog listDialog){
        setMenuDialogUpperRight(listDialog);

        listDialog.setBackgroundColor(R.color.grey_800);
//        listDialog.setBackgroundColor(R.color.theme_green_primary);
//        TypedValue tv = new TypedValue();
//        if (BaseApplication.getInstance().getTheme().resolveAttribute(android.R.attr.colorPrimary, tv, true)) {
//            listDialog.setBackgroundColor(tv.resourceId);
//        }
    }
}
