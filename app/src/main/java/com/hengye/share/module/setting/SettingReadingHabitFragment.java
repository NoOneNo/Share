package com.hengye.share.module.setting;

import android.os.Bundle;
import android.preference.Preference;

import com.hengye.share.module.base.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.module.util.BasePreferenceFragment;
import com.hengye.share.util.ToastUtil;

public class SettingReadingHabitFragment extends BasePreferenceFragment{

    @Override
    public String getTitle(){
        return BaseApplication.getInstance().getString(R.string.title_fragment_reading_habit);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting_read_habit);


        Preference.OnPreferenceClickListener onClickListener = new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                ToastUtil.showToBeAchievedToast();
                return true;
            }
        };


        findPreference(SettingHelper.KEY_HABIT_AUTO_NIGHT).setOnPreferenceClickListener(onClickListener);
        findPreference(SettingHelper.KEY_HABIT_FONT_ZOOM).setOnPreferenceClickListener(onClickListener);
    }

}
