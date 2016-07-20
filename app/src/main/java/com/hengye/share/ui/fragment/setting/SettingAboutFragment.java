package com.hengye.share.ui.fragment.setting;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import com.hengye.share.BuildConfig;
import com.hengye.share.R;
import com.hengye.share.ui.activity.web.WebViewActivity;
import com.hengye.share.ui.fragment.BasePreferenceFragment;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.helper.SettingHelper;

public class SettingAboutFragment extends BasePreferenceFragment{

    @Override
    public String getTitle(){
        return getString(R.string.title_fragment_about);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_setting_about);

        Preference versionCode = findPreference(SettingHelper.KEY_ABOUT_VERSION_CODE);
        versionCode.setSummary("Android " + BuildConfig.VERSION_NAME);
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

        String title = preference.getTitle().toString();
        if(title.equals(getString(R.string.title_setting_check_update))) {

        }else if(title.equals(getString(R.string.title_setting_version_code))) {

        }else if(title.equals(getString(R.string.title_setting_donate_to_developer))) {
            ClipboardUtil.copy("1240001796@qq.com");
            showDialog();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private Dialog mDialog;
    public void showDialog(){
        if(mDialog == null){
            SimpleTwoBtnDialog stbd = new SimpleTwoBtnDialog();
            stbd.setContent("支付宝账号1240001796@qq.com已复制到粘贴板, 进入支付宝可直接粘贴完成捐赠, 感谢您的支持!");
            stbd.setPositiveButtonText("打开支付宝");
            stbd.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    startActivity(WebViewActivity.getStartIntent(getActivity(), "http://auth.alipay.com"));
                }
            });
            mDialog = stbd.create(getActivity());
        }

        mDialog.show();
    }
}
