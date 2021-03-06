package com.hengye.share.module.setting;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceScreen;

import com.hengye.share.BuildConfig;
import com.hengye.share.R;
import com.hengye.share.module.userguide.UserGuideFragment;
import com.hengye.share.module.util.FragmentActivity;
import com.hengye.share.module.util.WebViewActivity;
import com.hengye.share.module.util.BasePreferenceFragment;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.ToastUtil;

public class SettingAboutFragment extends BasePreferenceFragment {

    @Override
    public String getTitle() {
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
        if (title.equals(getString(R.string.title_setting_check_update))) {
            startMarket();
        } else if (title.equals(getString(R.string.title_setting_version_code))) {

        } else if (title.equals(getString(R.string.title_setting_user_guide))) {

            getActivity().startActivity(FragmentActivity.getStartIntent(getActivity(), UserGuideFragment.class));

        }else if (title.equals(getString(R.string.title_setting_donate_to_developer))) {
            ClipboardUtil.copy("1240001796@qq.com");
            showDialog();
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private Dialog mDialog;

    public void showDialog() {
        if (mDialog == null) {
            SimpleTwoBtnDialog stbd = new SimpleTwoBtnDialog();
            stbd.setContent("支付宝账号1240001796@qq.com已复制到粘贴板, 进入支付宝可直接粘贴完成捐赠, 感谢您的支持!");
            stbd.setPositiveButtonText("打开支付宝");
            stbd.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().startActivity(WebViewActivity.getStartIntent(getActivity(), "http://auth.alipay.com"));
                }
            });
            mDialog = stbd.create(getActivity());
        }

        mDialog.show();
    }

    private void startMarket() {
        Uri uri = Uri.parse(String.format("market://details?id=%s", BuildConfig.APPLICATION_ID));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (IntentUtil.resolveActivity(intent)) {
            startActivity(intent);
        } else {
            ToastUtil.showToast(R.string.tip_no_app_store);
        }
    }
}
