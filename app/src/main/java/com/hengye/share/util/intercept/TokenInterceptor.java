package com.hengye.share.util.intercept;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.hengye.share.R;
import com.hengye.share.module.sso.ThirdPartyLoginActivity;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.base.BaseActivityHelper;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.util.UserUtil;

/**
 * Created by yuhy on 16/7/22.
 */
public class TokenInterceptor extends Interceptor{

    Dialog mLoginDialog;
    BaseActivity mActivity;

    public TokenInterceptor(BaseActivity activity) {
        this(activity, null);
    }

    public TokenInterceptor(BaseActivity activity, Action action) {
        this.mActivity = activity;
        Interception interception = new Interception() {
            @Override
            public boolean isIntercept() {
                if (UserUtil.isTokenEmpty()) {
                    getDialog().show();
                    return true;
                }
                return false;
            }
        };
        add(interception);
        setAction(action);
    }

    public Dialog getDialog() {
        if (mLoginDialog == null) {
            SimpleTwoBtnDialog build = new SimpleTwoBtnDialog();
            build.setContent(R.string.label_to_login);
            build.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mActivity.getActivityHelper().registerActivityLifecycleListener(new BaseActivityHelper(){
                        @Override
                        public void onActivityResulted(Activity activity, int requestCode, int resultCode, Intent data) {
                            if (requestCode == 33 && resultCode == Activity.RESULT_OK) {
                                next();
                            }
                            mActivity.getActivityHelper().unregisterActivityLifecycleListener(this);
                        }
                    });
                    mActivity.startActivityForResult(ThirdPartyLoginActivity.getStartIntent(mActivity, false), 33);
                }
            });
            mLoginDialog = build.create(mActivity);
        }
        return mLoginDialog;
    }

    @Override
    protected void runAction() {
        if(isIntercept()) {
            mActivity.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    TokenInterceptor.super.runAction();
                }
            });
        }else{
            super.runAction();
        }
    }
}
