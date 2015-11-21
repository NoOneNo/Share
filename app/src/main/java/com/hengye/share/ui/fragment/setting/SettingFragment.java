package com.hengye.share.ui.fragment.setting;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.ui.fragment.BasePreferenceFragment;
import com.hengye.share.util.L;

public class SettingFragment extends BasePreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    public String getTitle() {
        return BaseApplication.getInstance().getString(R.string.title_fragment_setting);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        String title = preference.getTitle().toString();
        Class<? extends Fragment> clazz = null;
        if(title.equals(getString(R.string.title_setting_common_basic))){
            //基本设置
            clazz = SettingBasicFragment.class;
        }else if(title.equals(getString(R.string.title_setting_common_flow_control))){
            //流量控制
            clazz = SettingFlowControlFragment.class;
        }else if(title.equals(getString(R.string.title_setting_common_sound_control))){
            //音效管理
            clazz = SettingSoundControlFragment.class;
        }else if(title.equals(getString(R.string.title_setting_common_notify))){
            //通知设置
            clazz = SettingNotifyFragment.class;
        }else if(title.equals(getString(R.string.title_setting_show_theme))){
            //主题
            clazz = SettingThemeFragment.class;
        }else if(title.equals(getString(R.string.title_setting_show_reading_habit))){
            //阅读习惯
            clazz = SettingReadingHabitFragment.class;
        }else if(title.equals(getString(R.string.title_setting_other_feedback))){
            //意见反馈
            clazz = SettingFeedbackFragment.class;
        }else if(title.equals(getString(R.string.title_setting_other_about))){
            //关于
            clazz = SettingAboutFragment.class;
        }else{
            L.debug("preference title not found");
        }

        try {
            if(clazz != null) {
                getFragmentManager().
                        beginTransaction().
                        setCustomAnimations(
                                R.anim.fragment_left_enter,
                                R.anim.fragment_right_exit,
                                R.anim.fragment_pop_right_enter,
                                R.anim.fragment_pop_left_exit).
                        replace(R.id.content, clazz.newInstance()).
                        addToBackStack(null).
                        commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
//        preference.setIntent(FragmentActivity.getStartIntent(getActivity(), clazz));


        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//        if (key.equals(SettingActivity.LIST_AVATAR_MODE)) {
//            String value = sharedPreferences.getString(key, "1");
//            if (value.equals("1")) {
//                SettingUtility.setEnableBigAvatar(false);
//            }
//            if (value.equals("2")) {
//                SettingUtility.setEnableBigAvatar(true);
//            }
//            if (value.equals("3")) {
//                SettingUtility.setEnableBigAvatar(Utility.isWifi(getActivity()));
//            }
//        }
//
//        if (key.equals(SettingActivity.LIST_PIC_MODE)) {
//            String value = sharedPreferences.getString(key, "1");
//            if (value.equals("1")) {
//                SettingUtility.setEnableBigPic(false);
//            }
//            if (value.equals("2")) {
//                SettingUtility.setEnableBigPic(true);
//            }
//            if (value.equals("3")) {
//                SettingUtility.setEnableBigPic(Utility.isWifi(getActivity()));
//            }
//        }
//        if (key.equals(SettingActivity.LIST_HIGH_PIC_MODE)) {
//            GlobalContext.getInstance().getBitmapCache().evictAll();
//        }
    }
}
