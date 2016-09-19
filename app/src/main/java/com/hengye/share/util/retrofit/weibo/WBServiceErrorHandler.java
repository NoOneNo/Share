package com.hengye.share.util.retrofit.weibo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import com.hengye.share.R;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.util.GsonUtil;
import com.hengye.share.util.HandlerUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ResUtil;
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
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            checkErrorFromResponse(httpException.response());
        }
    }

    public void checkErrorFromResponse(Response response) {
        if (response == null || response.errorBody() == null) {
            return;
        }

        WBServiceError wbServiceError = null;
        try {
            String error = response.errorBody().string();

            wbServiceError = GsonUtil.fromJson(error, WBServiceError.class);
            L.debug("checkErrorFromResponse : {}", error);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(wbServiceError == null){
            if(response.code() == 503){
                wbServiceError = WBServiceError.get503ServiceError();
            }
        }

        if(wbServiceError == null){
            return;
        }

        if(!handleError(wbServiceError)){
            showErrorDialog(wbServiceError);
        }
    }

    public boolean handleError(WBServiceError wbServiceError){
        if(wbServiceError.error_code.equals("10014")){
            HandlerUtil.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    if (BaseActivity.getCurrentActivity() != null) {
                        new AdTokenInterceptor(BaseActivity.getCurrentActivity()).start();
                    }else{
                        ToastUtil.showToast("权限不足");
                    }
                }
            });
            return true;
        }
        return false;
    }

    private boolean mIsDialogShowing = false;
    public void showErrorDialog(WBServiceError wbServiceError){

        Activity activity = BaseActivity.getCurrentActivity();

        if(activity == null || mIsDialogShowing){
            return;
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setCancelable(true)
                .setTitle(activity.getString(R.string.dialog_text_error_tip))
                .setMessage("(" +  wbServiceError.error_code + ")" + getWBServiceErrorMsg(wbServiceError))
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

    public String getWBServiceErrorMsg(WBServiceError wbServiceError){
        String msg = WBServiceErrorMsgUtil.getErrorMsg(wbServiceError.error_code);
        return msg == null ? wbServiceError.error : msg;
    }

    private static class WBServiceError{

        protected static WBServiceError get503ServiceError(){
            WBServiceError wbServiceError = new WBServiceError();
            wbServiceError.setError(ResUtil.getString(R.string.tip_error_service_unavailable));
            wbServiceError.setError_code("503");
            wbServiceError.setRequest("");
            return wbServiceError;
        }


        /**
         * request : /statuses/home_timeline.json
         * error_code : 20502
         * error : Need you follow uid.
         */

        private String request;
        private String error_code;
        private String error;

        public String getRequest() {
            return request;
        }

        public void setRequest(String request) {
            this.request = request;
        }

        public String getError_code() {
            return error_code;
        }

        public void setError_code(String error_code) {
            this.error_code = error_code;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}

















