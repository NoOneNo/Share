package com.hengye.share.module.other;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.module.base.BaseApplication;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.module.topic.TopicActivity;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.update.CheckUpdateMvpImpl;
import com.hengye.share.module.update.CheckUpdatePresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;

public class GuidanceActivity extends BaseActivity {

    @Override
    protected boolean setToolBar() {
        return false;
    }

//    @Override
//    protected boolean setFinishPendingTransition() {
//        return false;
//    }


    @Override
    protected boolean setCustomTheme() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int themeColor = SettingHelper.getAppThemeColorPrimary();
//        int themeColor = ResUtil.getColor(R.color.text_red_warn);
        getWindow().setStatusBarColor(themeColor);
        getWindow().setNavigationBarColor(themeColor);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guidance);

        showUsername();
    }

    boolean isInit = false;

    @Override
    protected void onResume() {
        super.onResume();

        if(!isInit) {
            isInit = true;
            start = System.currentTimeMillis();
            init();
            end = System.currentTimeMillis();
            long consume = end - start;
            long waitTime = DEFAULT_WAIT_DURATION - consume;
            if (waitTime < 0 || waitTime > DEFAULT_WAIT_DURATION) {
                waitTime = 0;
            }
            L.debug("GuidanceActivity init consume : {}, waitTime : {}", consume, waitTime);
            getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
//                    setHideAnimationOnStart();
                    startActivity(TopicActivity.class);
                    finish();
                }
            }, waitTime);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private final long DEFAULT_WAIT_DURATION = 1000;
    private long start, end;

    private void init(){

        createShortcutIfNeed();
        checkUpdateIfNeed();


        //初始化腾讯bugly
        CrashReport.initCrashReport(BaseApplication.getInstance(), ThirdPartyUtils.getAppKeyForBugly(), false);
        //初始化腾讯x5
        QbSdk.initX5Environment(BaseApplication.getInstance(), null);
    }

    private void showUsername(){
        TextView welcomeTV = (TextView) findViewById(R.id.tv_welcome);
        String name = UserUtil.getName();
        if(CommonUtil.isEmpty(name)){
            name = "world";
        }
        welcomeTV.setText(getString(R.string.tip_welcome, name));
    }

    public static final String ACTION_ADD_SHORTCUT = "com.android.launcher.action.INSTALL_SHORTCUT";
    public static final String ACTION_REMOVE_SHORTCUT = "com.android.launcher.action.UNINSTALL_SHORTCUT";
    private void createShortcutIfNeed() {

        if(SPUtil.getBoolean("isShortcutCreate2.0", false)){
            L.debug("shortcut is exist");
            return;
        }

        L.debug("shortcut is not exist, create it");
//        Intent shortcut = new Intent(Intent.ACTION_CREATE_SHORTCUT);//这个action不管用
        Intent shortcut = new Intent(ACTION_ADD_SHORTCUT);
        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));

        Intent launcherIntent = new Intent();
        launcherIntent.setClassName(this, getString(R.string.app_launcher));
        launcherIntent.setAction(Intent.ACTION_MAIN);
        launcherIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        //快捷方式的指向action
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, launcherIntent);
        //快捷方式的图标
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher));
        //不允许重复创建
        shortcut.putExtra("duplicate", false);
        sendBroadcast(shortcut);

        SPUtil.putBoolean("isShortcutCreate2.0", true);
    }

    private void checkUpdateIfNeed(){
        if(SettingHelper.isLaunchCheckUpdate()) {
            //启动时检查更新
            new CheckUpdatePresenter(new CheckUpdateMvpImpl(), false).checkUpdate();
        }
    }
}
