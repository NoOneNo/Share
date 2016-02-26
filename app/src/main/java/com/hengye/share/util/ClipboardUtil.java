package com.hengye.share.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.hengye.share.ui.base.BaseApplication;

public class ClipboardUtil {

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     * @param content
     */
    public static void copy(String content)
    {
        // 得到剪贴板管理器
        ClipboardManager cm = (ClipboardManager) BaseApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(null, content));
    }
    /**
     * 实现粘贴功能
     * add by wangqianzhou
     * @return
     */
    public static String paste()
    {
        // 得到剪贴板管理器
        ClipboardManager cm = (ClipboardManager)BaseApplication.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = cm.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(BaseApplication.getInstance()).toString();
        }
        return null;
    }

}
