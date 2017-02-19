package com.hengye.share.module.setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;

import com.hengye.share.module.base.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.module.util.BasePreferenceFragment;
import com.hengye.share.util.ToastUtil;

public class SettingReadingHabitFragment extends BasePreferenceFragment{

    @Override
    protected boolean isRegisterOnSharedPreferenceChangeListener() {
        return true;
    }

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

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(SettingHelper.KEY_HABIT_SHOW_DRAWER_FROM_LEFT) ||
                key.equals(SettingHelper.KEY_HABIT_SHOW_STATUS_OPTIONS)) {
            new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.dialog_text_tip)
                    .setMessage(R.string.tip_show_drawer_from_left_summary)
                    .setNegativeButton(R.string.dialog_text_no, null)
                    .setPositiveButton(R.string.dialog_text_yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getActivity().recreate();
                        }
                    })
                    .show();
        }
    }
}
