package com.hengye.share.ui.fragment.setting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;

import com.hengye.share.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.ui.fragment.BaseFragment;
import com.hengye.share.ui.fragment.BasePreferenceFragment;
import com.hengye.share.util.L;

public class SettingSoundControlFragment extends BasePreferenceFragment {

    @Override
    public String getTitle() {
        return BaseApplication.getInstance().getString(R.string.title_fragment_sound_control);
    }

    @Override
    protected boolean isRegisterOnSharedPreferenceChangeListener() {
        return true;
    }

    Preference mRefreshRinging, mSendSuccessShock;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting_sound_control);

        mRefreshRinging = findPreference(getString(R.string.title_setting_pull_to_refresh));
        mSendSuccessShock = findPreference(getString(R.string.title_setting_shock_after_send_success));

        boolean isMute = getPreferenceManager().getSharedPreferences().getBoolean(getString(R.string.title_setting_mute), false);
        setRingingEnable(!isMute);

        boolean isShock = getPreferenceManager().getSharedPreferences().getBoolean(getString(R.string.title_setting_shock_open), true);
        setShockEnable(isShock);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (key.equals(getString(R.string.title_setting_mute))) {
            //静音模式
            boolean isMute = sharedPreferences.getBoolean(key, false);
            setRingingEnable(!isMute);
        } else if (key.equals(getString(R.string.title_setting_shock_open))) {
            //震动模式
            boolean isShock = sharedPreferences.getBoolean(key, true);
            setShockEnable(isShock);
        }
    }

    private void setRingingEnable(boolean enabled) {
        mRefreshRinging.setEnabled(enabled);
    }

    private void setShockEnable(boolean enabled) {
        mSendSuccessShock.setEnabled(enabled);
    }
}
