package com.hengye.share.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.hengye.share.ui.base.BaseApplication;

public class NotificationUtil extends ApplicationUtil{

    public static void show(Notification notification, int id) {
        NotificationManager notificationManager = (NotificationManager) getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    public static void cancel(int id) {
        NotificationManager notificationManager = (NotificationManager) getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}
