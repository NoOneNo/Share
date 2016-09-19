package com.hengye.share.module.setting;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;

import com.hengye.share.module.base.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.module.util.BasePreferenceFragment;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.ToastUtil;

public class SettingBasicFragment extends BasePreferenceFragment{

    @Override
    public String getTitle(){
        return BaseApplication.getInstance().getString(R.string.title_fragment_basic_setting);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting_basic);

        Preference clearPhotoCache = findPreference(SettingHelper.KEY_BASIC_CLEAR_PHOTO_CACHE);

        clearPhotoCache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showDialog();
                return false;
            }
        });


        Preference.OnPreferenceClickListener onClickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ToastUtil.showToBeAchievedToast();
                return true;
            }
        };
        findPreference(SettingHelper.KEY_BASIC_PHOTO_SAVE_PATH).setOnPreferenceClickListener(onClickListener);
    }

    private Dialog mDialog;
    public void showDialog(){
        if(mDialog == null){
            SimpleTwoBtnDialog stbd = new SimpleTwoBtnDialog();
            stbd.setContent("是否清除图片缓存?");
            stbd.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RequestManager.clearImageCache();
                    ToastUtil.showToast("清楚图片缓存成功");
                }
            });
            mDialog = stbd.create(getActivity());
        }

        mDialog.show();
    }
}
