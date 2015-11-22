package com.hengye.share.ui.fragment.setting;

import android.os.Bundle;

import com.hengye.share.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.ui.fragment.BaseFragment;
import com.hengye.share.ui.fragment.BasePreferenceFragment;

public class SettingBasicFragment extends BasePreferenceFragment{

    @Override
    public String getTitle(){
        return BaseApplication.getInstance().getString(R.string.title_fragment_basic_setting);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting_basic);
    }

}
