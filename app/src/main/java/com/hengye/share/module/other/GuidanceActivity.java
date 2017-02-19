package com.hengye.share.module.other;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.module.status.StatusActivity;
import com.hengye.share.module.update.CheckUpdateMvpImpl;
import com.hengye.share.module.update.CheckUpdatePresenter;
import com.hengye.share.util.L;
import com.hengye.share.util.SPUtil;

public class GuidanceActivity extends BaseActivity {

//    private ShimmerFrameLayout mShimmerViewContainer;

    private boolean isInit = false;

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    protected boolean canSwipeBack() {
        return false;
    }

    @Override
    protected boolean setCustomTheme() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        int themeColor = SettingHelper.getAppThemeColorPrimary();
//        int themeColor = ResUtil.getColor(R.color.text_red_warn);
//        getWindow().setStatusBarColor(themeColor);
//        getWindow().setNavigationBarColor(themeColor);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_guidance);
//        showUsername();
//        mShimmerViewContainer = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
//        mShimmerViewContainer.useDefaults();
//        mShimmerViewContainer.setRepeatCount(0);
//        mShimmerViewContainer.setDuration(1500);
//        mShimmerViewContainer.startShimmerAnimation();
        createShortcutIfNeed();
        checkUpdateIfNeed();
        startActivity(StatusActivity.class);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        init();
    }

    private void init(){
        if(!isInit) {
            isInit = true;

            long start = System.currentTimeMillis();
            createShortcutIfNeed();
            long end = System.currentTimeMillis();
            long consume = end - start;
            long waitTime = DEFAULT_WAIT_DURATION - consume;
            if (waitTime < 0 || waitTime > DEFAULT_WAIT_DURATION) {
                waitTime = 0;
            }
            L.debug("GuidanceActivity init consume : %s", consume);
            startActivity(StatusActivity.class);
            finish();
//            getHandler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    startActivity(TopicActivity.class);
//                    finish();
//                }
//            }, waitTime);
        }
    }

    private static final long DEFAULT_WAIT_DURATION = 2000;

    private void showUsername(){
        TextView welcomeTV = (TextView) findViewById(R.id.tv_welcome);
//        String name = UserUtil.getName();
//        if(CommonUtil.isEmpty(name)){
//            name = "world";
//        }
        String name = "world";
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
