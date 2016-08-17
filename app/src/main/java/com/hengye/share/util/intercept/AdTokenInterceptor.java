package com.hengye.share.util.intercept;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.hengye.share.R;
import com.hengye.share.ui.activity.ThirdPartyLoginActivity;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.base.BaseActivityHelper;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.util.UserUtil;

/**
 * Created by yuhy on 16/7/22.
 */
public class AdTokenInterceptor extends Interceptor{

    Dialog mLoginDialog;
    BaseActivity mActivity;

    public AdTokenInterceptor(BaseActivity activity) {
        this(activity, null);
    }

    public AdTokenInterceptor(BaseActivity activity, Action action) {
        this.mActivity = activity;
        Interception interception = new Interception() {
            @Override
            public boolean isIntercept() {
                if (UserUtil.isAdTokenEmpty()) {
                    getDialog().show();
                    return true;
                }
                return false;
            }
        };
        setAction(action);
        add(interception);
    }

    public Dialog getDialog() {
        if (mLoginDialog == null) {
            SimpleTwoBtnDialog build = new SimpleTwoBtnDialog();
            build.setContent(R.string.tip_need_advanced_authorize);
            build.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mActivity.startActivityForResult(ThirdPartyLoginActivity.getAdTokenStartIntent(mActivity), 33);
                }
            });
            mLoginDialog = build.create(mActivity);
            mActivity.getActivityHelper().registerActivityLifecycleListener(new BaseActivityHelper(){
                @Override
                public void onActivityResulted(Activity activity, int requestCode, int resultCode, Intent data) {
                    super.onActivityResulted(activity, requestCode, resultCode, data);
                    if (requestCode == 33 && resultCode == Activity.RESULT_OK) {
                        next();
                    }
                    mActivity.getActivityHelper().unregisterActivityLifecycleListener(this);
                }
            });
        }
        return mLoginDialog;
    }

    @Override
    protected void runAction() {
        if(isIntercept()) {
            mActivity.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    AdTokenInterceptor.super.runAction();
                }
            });
        }else{
            super.runAction();
        }
    }
}
