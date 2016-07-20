package com.hengye.share.ui.fragment.setting;

import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.text.TextUtils;

import com.hengye.share.ui.base.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.ui.fragment.BasePreferenceFragment;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.helper.SettingHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SettingNotifyFragment extends BasePreferenceFragment {

    @Override
    public String getTitle() {
        return BaseApplication.getInstance().getString(R.string.title_fragment_notify);
    }

    @Override
    protected boolean isRegisterOnSharedPreferenceChangeListener() {
        return true;
    }

    private Preference mNotifyType, mRingtone;
    private List<Preference> mNotifyItems = new ArrayList<>();
    private final static int REQUEST_RINGTONE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting_notify);

        mNotifyItems.add(findPreference(SettingHelper.KEY_NOTIFY_NO_REMIND_AT_NIGHT));
        mNotifyItems.add(findPreference(SettingHelper.KEY_NOTIFY_FREQUENCY));
        mNotifyItems.add(findPreference(SettingHelper.KEY_NOTIFY_TYPE));
        mNotifyItems.add(findPreference(SettingHelper.KEY_NOTIFY_RINGTONE));
        mNotifyItems.add(findPreference(SettingHelper.KEY_NOTIFY_VIBRATION));
        mNotifyItems.add(findPreference(SettingHelper.KEY_NOTIFY_LIGHTS));

        setNotifyItemsEnable(SettingHelper.isNotifyOpen());


        mNotifyType = findPreference(SettingHelper.KEY_NOTIFY_TYPE);
        updateNotifyTypeSummary();

        mRingtone = findPreference(SettingHelper.KEY_NOTIFY_RINGTONE);
        updateRingtoneSummary();
        mRingtone.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            //监听改变，RingtonePreference的值改变不会发生调用onSharedPreferenceChanged.
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                SettingHelper.setRingtone(newValue.toString());
                updateRingtoneSummary();
                return false;
            }
        });
    }

    private void updateRingtoneSummary() {
        String path = SettingHelper.getRingtone();
        if(TextUtils.isEmpty(path)){
            mRingtone.setSummary(getString(R.string.title_setting_remind_by_ringtone_summary_null));
        }else{
            mRingtone.setSummary(RingtoneManager.getRingtone(getActivity(), Uri.parse(path)).getTitle(getActivity()));
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (key.equals(SettingHelper.KEY_NOTIFY_OPEN)) {
            setNotifyItemsEnable(SettingHelper.isNotifyOpen());
        } else if (key.equals(SettingHelper.KEY_NOTIFY_TYPE)) {
            updateNotifyTypeSummary();
        } else if(key.equals(SettingHelper.KEY_NOTIFY_RINGTONE)){
            updateRingtoneSummary();
        }
    }

    private void setNotifyItemsEnable(boolean enabled) {
        for (Preference p : mNotifyItems) {
            p.setEnabled(enabled);
        }
    }

    private void updateNotifyTypeSummary() {
        Set<String> options = SettingHelper.getNotifyType();
        if (!CommonUtil.isEmpty(options)) {
            String str = "";
            String[] summary = getResources().getStringArray(R.array.notify_type_entries);
            String[] values = getResources().getStringArray(R.array.notify_type_values);
            //因为set没有排列顺序，为了顺序排列，一个，一个，找。
            for (String value : values) {
                if (options.contains(value)) {
                    str = str + summary[Integer.parseInt(value) - 1] + ", ";
                }
            }
            //清除最后的", "分隔符
            if (!TextUtils.isEmpty(str) && str.length() > 2) {
                mNotifyType.setSummary(str.substring(0, str.length() - 2));
            }
        } else {
            mNotifyType.setSummary(getString(R.string.title_setting_remind_type_summary));
        }

    }
}
