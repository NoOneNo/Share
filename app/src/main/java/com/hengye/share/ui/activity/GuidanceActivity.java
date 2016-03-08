package com.hengye.share.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.hengye.share.R;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.util.L;
import com.hengye.share.util.SPUtil;

public class GuidanceActivity extends BaseActivity {

    @Override
    protected void afterCreate(Bundle savedInstanceState) {

        createShortcutIfNeed();
        startActivity(TopicActivity.class);
        finish();
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

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(this, GuidanceActivity.class));
        //快捷方式的图标
        Intent.ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.mipmap.ic_launcher);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
        sendBroadcast(shortcut);

        SPUtil.putBoolean("isShortcutCreate", true);
    }
}
