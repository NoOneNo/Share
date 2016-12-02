package com.hengye.share.module.test;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.view.animation.AlphaAnimation;

import com.android.volley.cache.BitmapCache;
import com.hengye.share.R;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.publish.TopicPublishActivity;
import com.hengye.share.module.sso.ThirdPartyLoginActivity;
import com.hengye.share.module.util.WebViewActivity;
import com.hengye.share.ui.widget.dialog.ListDialog;
import com.hengye.share.ui.widget.dialog.LoadingDialog;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.ui.widget.loading.FramesLoadingView;
import com.hengye.share.util.L;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.intercept.Action;
import com.hengye.share.util.intercept.AdTokenInterceptor;
import com.tencent.smtt.sdk.CookieManager;

import java.io.InputStream;
import java.util.ArrayList;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

public class TestActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected String getRequestTag() {
        return "TestActivity";
    }

    private static final String TAG = "RxAndroidSamples";

    private Handler backgroundHandler;
    private FramesLoadingView mLoading;
    private LoadingDialog mLoadingDialog;
    private ListDialog mListDialog;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_test;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        findViewById(R.id.btn_test).setOnClickListener(this);
        findViewById(R.id.btn_test2).setOnClickListener(this);
        findViewById(R.id.btn_test3).setOnClickListener(this);
        findViewById(R.id.btn_test4).setOnClickListener(this);
        findViewById(R.id.btn_test5).setOnClickListener(this);
        findViewById(R.id.btn_test6).setOnClickListener(this);
        findViewById(R.id.btn_test7).setOnClickListener(this);
        mLoading = (FramesLoadingView) findViewById(R.id.loading);
        mLoading.stop();
        mListDialog = new ListDialog(this, new ArrayList<ListDialog.KeyValue>());

        BackgroundThread backgroundThread = new BackgroundThread();
        backgroundThread.start();
        backgroundHandler = new Handler(backgroundThread.getLooper());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_test) {
            BitmapCache.getInstance().clearCacheFromMemory();
//            onRunSchedulerExampleButtonClicked();
        } else if (v.getId() == R.id.btn_test2) {
            User user = UserUtil.getCurrentUser();
            user.setCookie(null);
            UserUtil.updateUser(user);
        } else if (v.getId() == R.id.btn_test3) {
            CookieManager.getInstance().removeAllCookie();
        } else if (v.getId() == R.id.btn_test4) {
            if (mLoading.isRunning()) {
                mLoading.stop();
            } else {
                mLoading.start();
            }
        } else if (v.getId() == R.id.btn_test5) {
//            mListDialog.show();

        } else if (v.getId() == R.id.btn_test6) {
//            testInterceptor();

//            testAnimation();
//            startActivity(FragmentActivity.getStartIntent(this, TestWindowManagerFragment.class));

//            startActivity(WebViewActivity.getStartIntent(this, "https://passport.weibo.cn/signin/login"));
            startActivity(WebViewActivity.getStartIntent(this, "https://m.weibo.cn"));
//            startActivity(WebViewActivity.getStartIntent(this, "http://www.baidu.com"));
        } else if (v.getId() == R.id.btn_test7) {
            startActivity(SetTokenActivity.class);
        }

    }

    static class BackgroundThread extends HandlerThread {
        BackgroundThread() {
            super("SchedulerSample-BackgroundThread", THREAD_PRIORITY_BACKGROUND);
        }
    }

    private Dialog mLoginDialog;
//    private Interceptor mInterceptor;

    public Dialog getLoginDialog() {
        if (mLoginDialog == null) {
            SimpleTwoBtnDialog build = new SimpleTwoBtnDialog();
            build.setContent("需要高级授权, 是否继续?");
            build.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivityForResult(ThirdPartyLoginActivity.getAdTokenStartIntent(TestActivity.this), 3);
                }
            });
            mLoginDialog = build.create(this);
        }
        return mLoginDialog;
    }

    AdTokenInterceptor mAdTokenInterceptor;

    public void testInterceptor() {
        if (mAdTokenInterceptor == null) {
            mAdTokenInterceptor = new AdTokenInterceptor(this, new Action() {
                @Override
                public void run() {
                    startActivity(TopicPublishActivity.getStartIntent(TestActivity.this, "test"));
                }
            });
        }
        mAdTokenInterceptor.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void testAnimation() {
        if (mLoading.getVisibility() == View.VISIBLE) {
            AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(350);
            mLoading.setAnimation(animation);
            mLoading.setVisibility(View.GONE);
        } else {
            AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
            animation.setDuration(350);
            mLoading.setAnimation(animation);
            mLoading.setVisibility(View.VISIBLE);
        }
    }

    private void testResources() {
        try {
            InputStream is = TestActivity.class.getResourceAsStream("testF2.xml");
            if (is == null) {
                L.debug("get resource is null");
            } else {
                L.debug("get resource is not null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


























