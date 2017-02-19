package com.hengye.share.module.setting;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import com.hengye.share.R;
import com.hengye.share.module.publish.StatusPublishActivity;
import com.hengye.share.module.base.BaseApplication;
import com.hengye.share.module.util.BasePreferenceFragment;
import com.hengye.share.util.L;
import com.hengye.share.util.ToastUtil;

public class SettingFragment extends BasePreferenceFragment {

    @Override
    public String getTitle() {
        return BaseApplication.getInstance().getString(R.string.title_fragment_setting);
    }

    @Override
    protected boolean isRegisterOnSharedPreferenceChangeListener() {
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting);
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
            ToastUtil.showToBeAchievedToast();
            return false;
        }else if(title.equals(getString(R.string.title_setting_show_theme))){
            //主题
        }else if(title.equals(getString(R.string.title_setting_show_reading_habit))){
            //阅读习惯
            clazz = SettingReadingHabitFragment.class;
        }else if(title.equals(getString(R.string.title_setting_other_about))){
            //关于
            clazz = SettingAboutFragment.class;
        }else if(title.equals(getString(R.string.title_setting_other_feedback))){
            //意见反馈
            startActivity(StatusPublishActivity.getStartIntent(getActivity(), getFeedBackContent()));
        }else if(title.equals(getString(R.string.title_setting_other_share))){
            //意见反馈
            startActivity(StatusPublishActivity.getStartIntent(getActivity(), getShareContent()));
        }else{
            L.debug("preference title not found");
        }

        try {
            if(clazz != null) {
                getFragmentManager().
                        beginTransaction().
                        setCustomAnimations(
                                R.animator.fragment_enter,
                                R.animator.fragment_exit,
                                R.animator.fragment_pop_enter,
                                R.animator.fragment_pop_exit).
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
        super.onSharedPreferenceChanged(sharedPreferences, key);

        if (key.equals(SettingHelper.KEY_THEME_APP)) {
//            getActivity().onBackPressed();
            getActivity().recreate();
//            SettingHelper.removeCache(SettingHelper.KEY_THEME_APP);
//            SkinManager.getInstance().load(getSkinPath(), null);
        }
    }

    private String getFeedBackContent(){
        String prefix = "#Share意见反馈# @我是一只小小小鸡仔 ";
        String suffix = "设备型号：" + Build.MODEL + "，Android版本号：" + Build.VERSION.RELEASE + "；";
        return prefix + suffix;
    }

    private String getShareContent(){
        return "#Share#Share微博客户端, 非常好用[赞]";
    }


//    private static final String SKIN_NAME = "night_skin.zip";
//    private static final String SKIN_DIR = Environment
//            .getExternalStorageDirectory() + File.separator + SKIN_NAME;
//
//    public String getSkinPath(){
//        if(SettingHelper.THEME_COLOR_NIGHT.equals(SettingHelper.getAppThemeColor())){
//            return SKIN_DIR;
//        }
//        return "";
//    }
}
