package com.hengye.share.ui.fragment.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;

import com.hengye.share.helper.SettingHelper;
import com.hengye.share.ui.base.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.ui.fragment.BasePreferenceFragment;
import com.hengye.share.util.ApplicationUtil;
import com.hengye.skinloader.support.SkinManager;

import java.io.File;

public class SettingThemeFragment extends BasePreferenceFragment{

    @Override
    public String getTitle(){
        return BaseApplication.getInstance().getString(R.string.title_fragment_theme);
    }

    @Override
    protected boolean isRegisterOnSharedPreferenceChangeListener() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting_flow_control);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if(key.equals(SettingHelper.KEY_THEME_APP)){
            SettingHelper.removeCache(SettingHelper.KEY_THEME_APP);

            SkinManager.getInstance().load(getSkinPath(), null);
        }
    }

    private static final String SKIN_NAME = "night_skin.zip";
    private static final String SKIN_DIR = Environment
            .getExternalStorageDirectory() + File.separator + SKIN_NAME;

    public String getSkinPath(){
        if(SettingHelper.THEME_COLOR_NIGHT.equals(SettingHelper.getAppThemeColor())){
            return SKIN_DIR;
        }
        return "";
    }
}
