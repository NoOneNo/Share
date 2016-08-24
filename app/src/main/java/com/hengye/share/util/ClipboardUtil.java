package com.hengye.share.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.hengye.share.R;
import com.hengye.share.ui.base.BaseApplication;

public class ClipboardUtil extends ApplicationUtil{

    /**
     * 实现文本复制功能
     * add by wangqianzhou
     * @param content
     */
    public static void copy(String content)
    {
        // 得到剪贴板管理器
        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
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
        ClipboardManager cm = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = cm.getPrimaryClip();
        if (clip != null && clip.getItemCount() > 0) {
            return clip.getItemAt(0).coerceToText(getContext()).toString();
        }
        return null;
    }

    public static void copyAndToast(String content){
        copy(content);
        ToastUtil.showToast(R.string.label_topic_copy_to_clipboard_success);
    }
}
