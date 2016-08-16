package com.hengye.share.ui.fragment.setting;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import com.hengye.share.R;
import com.hengye.share.helper.SettingHelper;
import com.hengye.share.ui.activity.TopicPublishActivity;
import com.hengye.share.ui.base.BaseApplication;
import com.hengye.share.ui.fragment.BasePreferenceFragment;
import com.hengye.share.util.L;

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
        }else if(title.equals(getString(R.string.title_setting_show_theme))){
            //主题
        }else if(title.equals(getString(R.string.title_setting_show_reading_habit))){
            //阅读习惯
            clazz = SettingReadingHabitFragment.class;
        }else if(title.equals(getString(R.string.title_setting_other_feedback))){
            //意见反馈
            startActivity(TopicPublishActivity.getStartIntent(getActivity(), getFeedBackContent()));
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
                                R.anim.fragment_enter,
                                R.anim.fragment_exit,
                                R.anim.fragment_pop_enter,
                                R.anim.fragment_pop_exit).
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
        return "#Share意见反馈# @我是一只小小小鸡仔 ";
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
