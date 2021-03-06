package com.hengye.share.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Browser;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.google.gson.JsonObject;
import com.hengye.share.R;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.ui.widget.media.MediaController;
import com.hengye.share.ui.widget.media.VideoView;
import com.hengye.share.util.ApplicationUtil;
import com.hengye.share.util.GsonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ViewUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

/**
 * Created by yuhy on 2016/11/8.
 */

public class VideoPlayService extends Service implements View.OnClickListener {

    public static void start(Context context, String topicId, String url) {
        Intent intent = new Intent(context, VideoPlayService.class);
        intent.putExtra("statusId", topicId);
        intent.putExtra("url", url);
        context.startService(intent);
    }

    public static void stop(){
        if(RUNNING){
            ApplicationUtil.getContext().stopService(new Intent(ApplicationUtil.getContext(), VideoPlayService.class));
        }
    }

    public static boolean RUNNING = false;

    WindowManager mWindowManager;
    LayoutInflater mLayoutInflater;
    Handler mHandler = new Handler();
    String mStatusUrl;

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        RUNNING = true;

        L.debug("VideoPlayService onCreate()");

        initView();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String statusId = intent.getStringExtra("statusId");
        mStatusUrl = intent.getStringExtra("url");
        L.debug("onStartCommand, topicId : %s, url : %s", statusId, mStatusUrl);
        if (statusId == null) {
            handleFail();
        } else {
            mLoading.setVisibility(View.VISIBLE);
            requestMediaPlay(statusId);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RUNNING = false;
        mWindowManager.removeView(mParent);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (mVideoView == null || !mVideoView.isInPlaybackState()) {
            return;
        }

        adjustParentParams(newConfig);
        mWindowManager.updateViewLayout(mParent, mParentParams);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            L.debug("orientation is ORIENTATION_LANDSCAPE");

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            L.debug("orientation is ORIENTATION_PORTRAIT");
        }
    }

//    protected void adjustStatusBarAndNavigationBar(boolean isPortrait){
//        get
//        if(isPortrait){
//
//        }else{
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setNavigationBarColor(Color.TRANSPARENT);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//    }

    protected void adjustParentParams(Configuration config) {
        if (mParentParams == null) {
            mParentParams = new WindowManager.LayoutParams();
            //api大于等于19时用toast，小于19用于TYPE_SYSTEM_ALERT
//        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//            mParentParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            mParentParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            mParentParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            mParentParams.height = getVideoHeight(config);
            mParentParams.x = 0;
            mParentParams.y = 0;
            mParentParams.gravity = Gravity.TOP;
//        params.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
            mParentParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

//                    WindowManager.LayoutParams.FLAG_FULLSCREEN |
//                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;

//            mParentParams.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;

        } else {
            mParentParams.height = getVideoHeight(config);
        }
    }

    protected int getVideoHeight(Configuration configuration) {
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            int width = ViewUtil.getScreenWidth(this, true);
            return width * 9 / 16;  //16:9
        }
    }

    View mParent;
    View mLoading;
    WindowManager.LayoutParams mParentParams;

    @SuppressLint("InflateParams")
    private void initView() {

        mWindowManager = ((WindowManager) getSystemService(Context.WINDOW_SERVICE));
        mLayoutInflater = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        mParent = mLayoutInflater.inflate(R.layout.window_video_play, null, false);
        mLoading = mParent.findViewById(R.id.tv_loading);
        adjustParentParams(getResources().getConfiguration());
        initVideoView();
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mParentParams.width, mParentParams.height);
//        mVideoView.setLayoutParams(lp);
        mWindowManager.addView(mParent, mParentParams);
        mParent.setOnClickListener(this);

    }

    VideoView mVideoView;
    MediaController mMediaController;

    private void initVideoView() {
        mVideoView = (VideoView) mParent.findViewById(R.id.video_view);
        mMediaController = (MediaController) mParent.findViewById(R.id.media_controller);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                handleFail();
                return false;
            }
        });
        mVideoView.setOnFullscreenListener(new VideoView.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean isFullscreen) {
                if(mParentParams != null){
                    mParentParams.screenOrientation = isFullscreen ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT;
                    mWindowManager.updateViewLayout(mParent, mParentParams);

                    if(!isFullscreen){
                        //恢复横竖屏切换感应
                        getHandler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(mParentParams != null){
                                    mParentParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
                                    mWindowManager.updateViewLayout(mParent, mParentParams);
                                }
                            }
                        }, 4000);
                    }
                }
            }
        });
        mVideoView.setOnCloseListener(new VideoView.OnCloseListener() {
            @Override
            public void onClose(MediaPlayer mp) {
                stopSelf();
            }
        });
        // VideoView的宽度是包裹内容，如果宽度太小就无法触碰显示MediaController了，所以由父布局接管触摸事件
        mParent.setOnTouchListener(mMediaController.getTouchHelperListener());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_close) {
            stopSelf();
        }
    }

    private void handleFail() {
        L.debug("video play service handle fail");
        showErrorDialog();
        stopSelf();
    }

    private void requestMediaPlay(String topicId) {

        RetrofitManager
                .getWBService()
                .getMediaPlayUrl(topicId)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new SingleObserver<Object>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handleFail();
                    }

                    @Override
                    public void onSuccess(Object object) {
                        String url = null;
                        try {
                            JsonObject jsonObject = GsonUtil.toJsonObject(object);
                            int code = jsonObject.get("retcode").getAsInt();
                            if (code == 0) {
                                url = jsonObject.get("data").getAsString();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (url == null) {
                            handleFail();
                        } else {
                            playMedia(url);
                        }
                    }
                });
    }

    private void playMedia(String url) {
        mLoading.setVisibility(View.GONE);
        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.start();
    }

    private void showErrorDialog(){

        final Activity activity = BaseActivity.getCurrentActivity();
        if(activity != null) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                    .setCancelable(true)
                    .setTitle(R.string.dialog_text_tip)
                    .setMessage(R.string.tip_load_video_fail)
                    .setNegativeButton(R.string.dialog_text_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton(R.string.dialog_text_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(mStatusUrl));
                            intent.putExtra(Browser.EXTRA_APPLICATION_ID, activity.getPackageName());
                            activity.startActivity(intent);
                        }
                    });

            builder.create().show();
        }
    }
}











