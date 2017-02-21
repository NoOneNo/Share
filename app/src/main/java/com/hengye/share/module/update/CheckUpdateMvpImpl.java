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
import com.hengye.share.module.userguide.UserGuideFragment;
import com.hengye.share.module.util.FragmentActivity;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.util.encapsulation.mvp.MvpPresenter;
import com.hengye.share.util.ApplicationUtil;
import com.hengye.share.util.CommonUtil;
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

        if(updateBean.getAppUrl() != null){
            //更新
            showUpdateDialog(activity, updateBean);
        }else{
            //打开用户手册
            showUserGuide(activity);
        }
    }

    @Override
    public void onCheckUpdateFail(int taskState) {
        ToastUtil.showToast(TaskState.toString(taskState));
    }

    private void showUpdateDialog(Activity activity, final UpdateBean updateBean){

        String message = updateBean.getUpdateInfo() != null ? updateBean.getUpdateInfo() : ResUtil.getString(R.string.tip_new_version, BuildConfig.VERSION_NAME, updateBean.getVersionName());
        //防止转义不能换行
        new AlertDialog.Builder(activity)
                .setCancelable(true)
                .setTitle(activity.getString(R.string.title_find_update, updateBean.getVersionName()))
                .setMessage(message)
                .setPositiveButton(R.string.tip_update_now, new DialogInterface.OnClickListener() {
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
                .setNegativeButton(R.string.tip_cancel, null)
                .show();
    }

    private void showUserGuide(final Activity activity){
        boolean showUserGuide = SPUtil.getBoolean("showUserGuide", true);

        if(showUserGuide){
            SPUtil.putBoolean("showUserGuide", false);
            new AlertDialog.Builder(activity)
                    .setCancelable(true)
                    .setTitle(R.string.dialog_text_tip)
                    .setMessage(R.string.tip_start_user_guide)
                    .setPositiveButton(activity.getString(R.string.dialog_text_yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivity(FragmentActivity.getStartIntent(activity, UserGuideFragment.class));
                        }
                    })
                    .setNegativeButton(R.string.dialog_text_no, null)
                    .show();
        }
    }
}
