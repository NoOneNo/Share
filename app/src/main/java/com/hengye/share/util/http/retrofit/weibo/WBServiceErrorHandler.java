package com.hengye.share.util.http.retrofit.weibo;

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
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.intercept.AdTokenInterceptor;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import retrofit2.Response;

/**
 * Created by yuhy on 16/9/5.
 */
public class WBServiceErrorHandler {
    private static WBServiceErrorHandler ourInstance = new WBServiceErrorHandler();

    public static WBServiceErrorHandler getInstance() {
        return ourInstance;
    }

    private WBServiceErrorHandler() {}

    public WBApiException checkError(Throwable throwable) {
        WBApiException apiException = null;
        if (throwable instanceof HttpException) {
            HttpException httpException = (HttpException) throwable;
            apiException = checkErrorFromResponse(httpException.response());
        } else if (throwable instanceof WBApiException) {
            apiException = (WBApiException) throwable;
        }

        handleApiException(apiException);
        return apiException;
    }

    private WBApiException checkErrorFromResponse(Response response) {
        if (response == null || response.errorBody() == null) {
            return null;
        }

        WBApiException wbApiException = null;
        try {
            String error = response.errorBody().string();

            wbApiException = GsonUtil.fromJson(error, WBApiException.class);
            L.debug("checkErrorFromResponse : %s", error);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            response.errorBody().close();
        }

        if (wbApiException == null) {
            if (response.code() == 503) {
                wbApiException = WBApiException.get503ServiceError();
            }
        }
        return wbApiException;
    }

    private void handleApiException(WBApiException wbApiException) {
        if (wbApiException == null) {
            return;
        }

        if (!handleCustomApiException(wbApiException)) {
            showErrorDialog(wbApiException);
        }
    }

    private boolean handleCustomApiException(WBApiException wbApiException) {
        if (wbApiException.isNeedAdPermission()) {
            HandlerUtil.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    if (BaseActivity.getCurrentActivity() != null) {
                        new AdTokenInterceptor(BaseActivity.getCurrentActivity()).start();
                    } else {
                        ToastUtil.showToast(R.string.tip_permission_denied);
                    }
                }
            });
            return true;
        } else if (wbApiException.isTokenExpired()) {
            HandlerUtil.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    UserUtil.resetToken();
                    if (BaseActivity.getCurrentActivity() != null) {
                        getTokenExpireDialog(BaseActivity.getCurrentActivity()).show();
                    } else {
                        ToastUtil.showToast(R.string.tip_token_expire);
                    }
                }
            });
            return true;
        } else if (wbApiException.isServicePause()) {
            HandlerUtil.getInstance().post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast(R.string.tip_service_pause);
                }
            });
            return true;
        }
        return false;
    }

    private boolean mIsDialogShowing = false;
    /**
     * 如果部分dialog没有执行dismiss就关闭了，那么下次需要显示对话框就以为已有对话框在显示了
     * 这个时候设置一个计数器，如果每次需要显示新的对话框，旧的对话框还在显示就给计数器+1
     * 到最大值后重置，让新的对话框显示；
     */
    private int mMaxErrorCount = 10;
    private int mCurrentErrorCount = 0;

    private void showErrorDialog(final WBApiException wbApiException) {
        HandlerUtil.getInstance().post(new Runnable() {
            @Override
            public void run() {
                Activity activity = BaseActivity.getCurrentActivity();

                if (activity == null) {
                    return;
                } else if (mIsDialogShowing) {
                    if (mCurrentErrorCount < mMaxErrorCount) {
                        mCurrentErrorCount++;
                        return;
                    } else {
                        mCurrentErrorCount = 0;
                        mIsDialogShowing = false;
                    }
                }

                final AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                        .setCancelable(true)
                        .setTitle(R.string.dialog_text_error_tip)
                        .setMessage("(" + wbApiException.errorCode + ")" + getWBServiceErrorMsg(wbApiException))
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
                builder.create().show();
            }
        });
    }

    private Dialog getTokenExpireDialog(final Activity activity) {
        SimpleTwoBtnDialog build = new SimpleTwoBtnDialog();
        build.setContent(R.string.tip_token_expire);
        build.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activity.startActivity(ThirdPartyLoginActivity.getStartIntent(activity, true));
            }
        });
        return build.create(activity);
    }

    private String getWBServiceErrorMsg(WBApiException wbApiException) {
        String msg = WBServiceErrorMsgUtil.getErrorMsg(wbApiException.getErrorCodeStr());
        return msg == null ? wbApiException.error : msg;
    }
}

















