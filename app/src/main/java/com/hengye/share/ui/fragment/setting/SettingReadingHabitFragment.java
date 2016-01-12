package com.hengye.share.ui.fragment.setting;

import android.os.Bundle;

import com.hengye.share.ui.base.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.ui.fragment.BasePreferenceFragment;

public class SettingReadingHabitFragment extends BasePreferenceFragment{

    @Override
    public String getTitle(){
        return BaseApplication.getInstance().getString(R.string.title_fragment_reading_habit);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting_read_habit);
    }

}
