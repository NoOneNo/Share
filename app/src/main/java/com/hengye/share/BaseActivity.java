package com.hengye.share;

import android.support.v7.app.AppCompatActivity;

import com.hengye.volleyplus.toolbox.RequestManager;

import org.slf4j.Logger;

public abstract class BaseActivity extends AppCompatActivity {

//    public final static Logger L = com.hengye.share.util.L.L;

    protected abstract String getRequestTag();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestManager.cancelPendingRequests(getRequestTag());
    }
}
