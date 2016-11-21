package com.hengye.share.util.retrofit.weibo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;

import com.hengye.share.R;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.sso.ThirdPartyLoginActivity;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.util.GsonUtil;
import com.hengye.share.util.HandlerUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.intercept.AdTokenInterceptor;

import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by yuhy on 16/9/5.
 */
public class WBServiceErrorHandler {
    private static WBServiceErrorHandler ourInstance = new WBServiceErrorHandler();

    public static WBServiceErrorHandler getInstance() {
        return ourInstance;
    }

    private WBServiceErrorHandler() {
    }


    public void checkError(Throwable throwable) {
        WBApiException apiException = null;
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            apiException = checkErrorFromResponse(httpException.response());
        }else if(throwable instanceof WBApiException){
            apiException = (WBApiException) throwable;
        }

        handleApiException(apiException);
    }

    private WBApiException checkErrorFromResponse(Response response) {
        if (response == null || response.errorBody() == null) {
            return null;
        }

        WBApiException wbApiException = null;
        try {
            String error = response.errorBody().string();

            wbApiException = GsonUtil.fromJson(error, WBApiException.class);
            L.debug("checkErrorFromResponse : {}", error);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            response.errorBody().close();
        }

        if(wbApiException == null){
            if(response.code() == 503){
                wbApiException = WBApiException.get503ServiceError();
            }
        }
        return wbApiException;
    }

    private void handleApiException(WBApiException wbApiException){
        if(wbApiException == null){
            return;
        }

        if(!handleCustomApiException(wbApiException)){
            showErrorDialog(wbApiException);
        }
    }

    private boolean handleCustomApiException(WBApiException wbApiException){
        if(wbApiException.isNeedAdPermission()){
            HandlerUtil.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    if (BaseActivity.getCurrentActivity() != null) {
                        new AdTokenInterceptor(BaseActivity.getCurrentActivity()).start();
                    }else{
                        ToastUtil.showToast(R.string.tip_permission_denied);
                    }
                }
            });
            return true;
        }else if(wbApiException.isTokenExpired()){
            HandlerUtil.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    if (BaseActivity.getCurrentActivity() != null) {
                        getTokenExpireDialog(BaseActivity.getCurrentActivity()).show();
                    }else{
                        ToastUtil.showToast(R.string.tip_token_expire);
                    }
                }
            });
        }
        return false;
    }

    private boolean mIsDialogShowing = false;
    private void showErrorDialog(WBApiException wbApiException){

        Activity activity = BaseActivity.getCurrentActivity();

        if(activity == null || mIsDialogShowing){
            mIsDialogShowing = false;
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .setTitle(activity.getString(R.string.dialog_text_error_tip))
                .setMessage("(" +  wbApiException.errorCode + ")" + getWBServiceErrorMsg(wbApiException))
                .setPositiveButton(activity.getString(R.string.dialog_text_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        mIsDialogShowing = false;
                    }
                });

        mIsDialogShowing = true;
        HandlerUtil.getInstance().post(new Runnable() {
            @Override
            public void run() {
                builder.create().show();
            }
        });
    }

    private  Dialog getTokenExpireDialog(final Activity activity){
        SimpleTwoBtnDialog build = new SimpleTwoBtnDialog();
        build.setContent(R.string.tip_token_expire);
        build.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivity(ThirdPartyLoginActivity.getStartIntent(activity, true));
            }
        });
        return build.create(activity);
    }

    private String getWBServiceErrorMsg(WBApiException wbApiException){
        String msg = WBServiceErrorMsgUtil.getErrorMsg(wbApiException.getErrorcodeStr());
        return msg == null ? wbApiException.error : msg;
    }
}

















