package com.hengye.share.ui.activity.setting;

import android.os.Bundle;

import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.ui.fragment.setting.SettingFragment;


public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.content, new SettingFragment())
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        }else {
            super.onBackPressed();
        }
    }
}
