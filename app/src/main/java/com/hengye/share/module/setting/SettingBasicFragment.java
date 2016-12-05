package com.hengye.share.module.setting;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.text.format.Formatter;

import com.hengye.share.module.base.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.module.util.BasePreferenceFragment;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.ToastUtil;

public class SettingBasicFragment extends BasePreferenceFragment{

    Preference mClearPhotoCache;

    @Override
    public String getTitle(){
        return BaseApplication.getInstance().getString(R.string.title_fragment_basic_setting);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting_basic);

        mClearPhotoCache = findPreference(SettingHelper.KEY_BASIC_CLEAR_PHOTO_CACHE);
        updatePhotoCacheSize();
        mClearPhotoCache.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showDialog();
                return false;
            }
        });



        //功能暂未实现
        Preference.OnPreferenceClickListener onClickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ToastUtil.showToBeAchievedToast();
                return true;
            }
        };
        findPreference(SettingHelper.KEY_BASIC_PHOTO_SAVE_PATH).setOnPreferenceClickListener(onClickListener);
    }

    private void updatePhotoCacheSize(){
        mClearPhotoCache.setSummary(Formatter.formatShortFileSize(getActivity(), RequestManager.getImageCacheSize()));
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
                    getView().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            updatePhotoCacheSize();
                            ToastUtil.showToast("清楚图片缓存成功");
                        }
                    }, 500);
                }
            });
            mDialog = stbd.create(getActivity());
        }

        mDialog.show();
    }
}
