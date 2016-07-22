package com.hengye.share.ui.widget.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.StringRes;

import com.hengye.share.R;
import com.hengye.share.util.ResUtil;

public class SimpleTwoBtnDialog{

    private String title, content, negativeButtonText, positiveButtonText;
    private DialogInterface.OnClickListener negativeButtonClickListener, positiveButtonClickListener;

    public SimpleTwoBtnDialog(){}

    public Dialog create(Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(getTitle(context))
                .setMessage(getContent())
                .setNegativeButton(getNegativeButtonText(context), getNegativeButtonClickListener())
                .setPositiveButton(getPositiveButtonText(context), getPositiveButtonClickListener());
        return builder.create();
    }

    public DialogInterface.OnClickListener getDefaultOnClickListener(){
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        };
    }

    public DialogInterface.OnClickListener getNegativeButtonClickListener() {
        if(negativeButtonClickListener == null){
            return getDefaultOnClickListener();
        }
        return negativeButtonClickListener;
    }

    public void setNegativeButtonClickListener(DialogInterface.OnClickListener negativeButtonClickListener) {
        this.negativeButtonClickListener = negativeButtonClickListener;
    }

    public DialogInterface.OnClickListener getPositiveButtonClickListener() {
        if(positiveButtonClickListener == null){
            return getDefaultOnClickListener();
        }
        return positiveButtonClickListener;
    }

    public void setPositiveButtonClickListener(DialogInterface.OnClickListener positiveButtonClickListener) {
        this.positiveButtonClickListener = positiveButtonClickListener;
    }

    public String getTitle(Context context) {
        if(title == null){
            return context.getString(R.string.dialog_text_tip);
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTitle(@StringRes int resId){
        setTitle(ResUtil.getString(resId));
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setContent(@StringRes int resId) {
        setContent(ResUtil.getString(resId));
    }

    public CharSequence getNegativeButtonText(Context context) {
        if(negativeButtonText == null){
            return context.getString(R.string.dialog_text_cancel);
        }
        return negativeButtonText;
    }

    public void setNegativeButtonText(String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
    }

    public String getPositiveButtonText(Context context) {
        if(positiveButtonText == null){
            return context.getString(R.string.dialog_text_confirm);
        }
        return positiveButtonText;
    }

    public void setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
    }
}
