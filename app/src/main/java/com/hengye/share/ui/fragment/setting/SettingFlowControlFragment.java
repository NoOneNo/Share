package com.hengye.share.ui.fragment.setting;

import android.os.Bundle;

import com.hengye.share.ui.base.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.ui.fragment.BasePreferenceFragment;

public class SettingFlowControlFragment extends BasePreferenceFragment{

    @Override
    public String getTitle(){
        return BaseApplication.getInstance().getString(R.string.title_fragment_flow_control);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting_flow_control);
    }

}
