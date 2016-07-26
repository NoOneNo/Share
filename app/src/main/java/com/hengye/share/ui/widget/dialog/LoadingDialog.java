package com.hengye.share.ui.widget.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.ui.widget.util.ShapeLoader;
import com.hengye.share.util.ViewUtil;

public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        this(context, "加载中...", false);
    }

    public LoadingDialog(Context context, CharSequence text) {
        this(context, text, false);
    }

    public LoadingDialog(Context context, CharSequence text, boolean isNeedToShow) {
        this(context, text, isNeedToShow, true);
    }

    public LoadingDialog(Context context, CharSequence text, boolean isNeedToShow, boolean isCancelable) {
        super(context, R.style.DefaultDialog);
        setContentView(R.layout.dialog_loading);

        setCanceledOnTouchOutside(false);
        setCancelable(isCancelable);

        ShapeLoader shapeLoader = ShapeLoader.getInstance();
        mContent = (TextView) findViewById(R.id.tv_content);
        mContent.setText(text);
        View parent = findViewById(R.id.rl_dialog);
        shapeLoader.setRectConnerBackground(parent, getContext().getResources().getColor(R.color.white), ViewUtil.dp2px(10f));

        if (isNeedToShow) {
            show();
        }
    }

    private TextView mContent;

    @Override
    public void dismiss() {
        if(getContext() instanceof Activity){
            Activity activity = (Activity) getContext();
            if(activity.isDestroyed() || activity.isFinishing()){
                return;
            }
        }
        super.dismiss();
    }

    @Override
    public void show() {
        if(getContext() instanceof Activity){
            Activity activity = (Activity) getContext();
            if(activity.isDestroyed() || activity.isFinishing()){
                return;
            }
        }
        super.show();
    }
}
