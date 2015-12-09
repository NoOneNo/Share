package com.hengye.share.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.hengye.share.util.L;

public class ShareService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
        L.debug("ShareService onCreate invoke");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.debug("ShareService onStartCommand invoke");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        L.debug("ShareService onDestroy invoke");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ShareBinder(this);
    }

    static class ShareBinder extends Binder {
        private ShareService mShareService;
        public ShareBinder(ShareService shareService){
            this.mShareService = shareService;
        }

        public ShareService getShareService() {
            return mShareService;
        }

        public void setShareService(ShareService shareService) {
            this.mShareService = shareService;
        }
    }
}
