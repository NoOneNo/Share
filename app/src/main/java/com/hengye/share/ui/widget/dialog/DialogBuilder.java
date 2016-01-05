package com.hengye.share.ui.widget.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogBuilder {


    public static Dialog getItemDialog(Context context, CharSequence[] items, DialogInterface.OnClickListener onClickListener){

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setItems(items, onClickListener);
        return builder.create();
    }

}
