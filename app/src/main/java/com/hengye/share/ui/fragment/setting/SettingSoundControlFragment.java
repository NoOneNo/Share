package com.hengye.share.ui.fragment.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;

import com.hengye.share.ui.base.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.ui.fragment.BasePreferenceFragment;
import com.hengye.share.util.SettingHelper;

public class SettingSoundControlFragment extends BasePreferenceFragment {

    @Override
    public String getTitle() {
        return BaseApplication.getInstance().getString(R.string.title_fragment_sound_control);
    }

    @Override
    protected boolean isRegisterOnSharedPreferenceChangeListener() {
        return true;
    }

    Preference mRefreshRingtone, mVibrationFeedback, mVibrationRemind;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting_sound_control);

        mRefreshRingtone = findPreference(SettingHelper.KEY_SOUND_REFRESH_RINGTONE);
        mVibrationFeedback = findPreference(SettingHelper.KEY_SOUND_VIBRATION_FEEDBACK);
        mVibrationRemind = findPreference(SettingHelper.KEY_NOTIFY_VIBRATION);

        setRingingEnable(!SettingHelper.isSoundOff());

        setShockEnable(SettingHelper.isVibrationOn());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (key.equals(SettingHelper.KEY_SOUND_OFF)) {
            //静音模式
            boolean isMute = sharedPreferences.getBoolean(key, false);
            setRingingEnable(!isMute);
        } else if (key.equals(SettingHelper.KEY_SOUND_VIBRATION_ON)) {
            //震动模式
            boolean isShock = sharedPreferences.getBoolean(key, true);
            setShockEnable(isShock);
        }
    }

    private void setRingingEnable(boolean enabled) {
        mRefreshRingtone.setEnabled(enabled);
    }

    private void setShockEnable(boolean enabled) {
        mVibrationFeedback.setEnabled(enabled);
        mVibrationRemind.setEnabled(enabled);
    }
}
