package com.hengye.share.module.update;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.hengye.share.BuildConfig;
import com.hengye.share.R;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.util.ApplicationUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.ToastUtil;

/**
 * Created by yuhy on 2016/11/21.
 */

public class CheckUpdateMvpImpl implements CheckUpdateContract.View {

    @Override
    public void setPresenter(MvpPresenter presenter) {

    }

    @Override
    public void onCheckUpdateStart() {

    }

    @Override
    public void onCheckUpdateComplete(@NonNull final UpdateBean updateBean) {
        L.debug("onCheckUpdateComplete invoke");
        Activity activity = BaseActivity.getCurrentActivity();
        if(activity == null){
            return;
        }

        String message = updateBean.getUpdateInfo() != null ? updateBean.getUpdateInfo() : ResUtil.getString(R.string.tip_new_version, BuildConfig.VERSION_NAME, updateBean.getVersionName());
        //防止转义不能换行
        message = message.replace("\\n", "\n");
        new AlertDialog.Builder(activity)
                .setCancelable(true)
                .setTitle(R.string.title_find_update)
                .setMessage(message)
                .setPositiveButton(activity.getString(R.string.dialog_text_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri uri = Uri.parse(updateBean.getAppUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        if (IntentUtil.resolveActivity(intent)) {
                            ApplicationUtil.getContext().startActivity(intent);
                        } else {
                            ToastUtil.showToast(R.string.label_resolve_url_activity_fail);
                        }
                        dialog.dismiss();
                    }
                })
                .setNeutralButton(R.string.tip_not_appear_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SPUtil.putInt(UpdateBean.IGNORE_VERSION, updateBean.getVersionCode());
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.tip_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    public void onCheckUpdateFail(int taskState) {
        ToastUtil.showToast(TaskState.toString(taskState));
    }
}
