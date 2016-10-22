package com.hengye.share.module.other;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.module.topic.TopicActivity;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.UserUtil;

public class GuidanceActivity extends BaseActivity {

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

        setContentView(R.layout.activity_guidance);

        TextView welcomeTV = (TextView) findViewById(R.id.tv_welcome);
        String name = UserUtil.getName();
        if(CommonUtil.isEmpty(name)){
            name = "world";
        }
        welcomeTV.setText(getString(R.string.tip_welcome, name));
        createShortcutIfNeed();
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(TopicActivity.class);
                finish();
            }
        }, 1000);

    }

    private void createShortcutIfNeed() {

        if(SPUtil.getBoolean("isShortcutCreate", false)){
            L.debug("shortcut is exist");
            return;
        }

        L.debug("shortcut is not exist, create it");
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        //快捷方式的名称
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
        shortcut.putExtra("duplicate", false); //不允许重复创建

//        com.hengye.share.Launcher
        Intent intent = new Intent();
//        new Intent(this, GuidanceActivity.class)
        intent.setClassName(this, "com.hengye.share.Launcher");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
        //快捷方式的图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        sendBroadcast(shortcut);

        SPUtil.putBoolean("isShortcutCreate", true);
    }
}
