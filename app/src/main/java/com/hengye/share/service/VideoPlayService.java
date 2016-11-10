package com.hengye.share.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hengye.share.R;
import com.hengye.share.util.GsonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ViewUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Response;
import rx.Subscriber;

/**
 * Created by yuhy on 2016/11/8.
 */

public class VideoPlayService extends Service implements View.OnClickListener{

    public static void start(Context context, String topicId, String url){
        Intent intent = new Intent(context, VideoPlayService.class);
        intent.putExtra("topicId", topicId);
        intent.putExtra("url", url);
        context.startService(intent);
    }

    WindowManager mWindowManager;
    LayoutInflater mLayoutInflater;

    @Override
    public void onCreate() {
        super.onCreate();
        L.debug("VideoPlayService onCreate()");

        mWindowManager = ((WindowManager) getSystemService(Context.WINDOW_SERVICE));
        mLayoutInflater = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE));

        initView();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String topicId = intent.getStringExtra("topicId");
        String url = intent.getStringExtra("url");
        if(topicId == null){
            handleFail();
        }else{
            requestMediaPlay(topicId);
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWindowManager.removeView(mParent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    View mParent;
//    View mVideo;

    private void initView() {

        mParent = mLayoutInflater.inflate(R.layout.window_video_play, null, false);

//        mVideo = mParent.findViewById(R.id.window_video_play);


        WindowManager.LayoutParams params = new WindowManager.LayoutParams();

//        layoutParams.width = ViewUtil.dp2px(100.0f);
//        layoutParams.height = ViewUtil.dp2px(200.0f);
        //api大于等于19时用toast，小于19用于TYPE_SYSTEM_ALERT
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.x = 0;
        params.y = ViewUtil.getActionBarHeight();
        params.gravity = Gravity.TOP;
//        layoutParams.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
//        L.debug("gravity : {}", Gravity.BOTTOM | Gravity.START);
        mWindowManager.addView(mParent, params);


        mParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSelf();
            }
        });

        initVideoView();
    }

    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    View mClose;
    MediaPlayer mMediaPlayer;
    private void initVideoView(){
        mSurfaceView = (SurfaceView)mParent.findViewById(R.id.surface_video);
        mClose = mParent.findViewById(R.id.btn_close);
        mSurfaceHolder = mSurfaceView.getHolder();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btn_close){
            handleFail();
        }
    }

    private void handleFail(){
        L.debug("video play service handle fail");
        stopSelf();
    }

    private void requestMediaPlay(String topicId){

        RetrofitManager
                .getWBService()
                .getMediaPlayUrl(topicId)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        handleFail();
                    }

                    @Override
                    public void onNext(Object object) {
                        String url = null;
                        try {
                            JsonObject jsonObject = GsonUtil.toJsonObject(object);
                            int code = jsonObject.get("retcode").getAsInt();
                            if(code == 0){
                                url = jsonObject.get("data").getAsString();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        if(url == null){
                            handleFail();
                        }else{
                            playMedia(url);
                        }
                    }
                });
    }

    private void playMedia(String url){
        try{

            mSurfaceView.setBackground(null);
            if (mMediaPlayer != null) {
                mMediaPlayer.reset();//恢复到未初始化的状态
                mMediaPlayer.release();
            }
//            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.video_test);//读取视频
            mMediaPlayer = new MediaPlayer();
//            mMediaPlayer.setDataSource("http://220.170.49.101/6/u/z/n/i/uznieiofgthpmyvbfvujnrezonnstk/he.yinyuetai.com/6644014F4059639839454D0994238FBE.flv?sc\\u003da317971751670318\\u0026br\\u003d3142\\u0026vid\\u003d2353215\\u0026aid\\u003d28047\\u0026area\\u003dML\\u0026vst\\u003d0");
            mMediaPlayer.setDataSource(url);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

//                mVideoSeekBar.setMax(mMediaPlayer.getDuration());//设置SeekBar的长度
            mMediaPlayer.setDisplay(mSurfaceHolder);//设置屏幕
            mMediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
                @Override
                public void onSeekComplete(MediaPlayer mp) {
                    L.debug("onSeekComplete invoke");
                    mSurfaceView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mSurfaceView.setBackground(null);
                        }
                    }, 500);
                }
            });

            mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    L.debug("onError()");
                    return false;
                }
            });
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                @Override
                public void onPrepared(MediaPlayer mp) {
                    //准备完成后才给视频播放设置holder
                    L.debug("onPrepared()");
                    mMediaPlayer.start();
//                        mMediaPlayer.seekTo(0);
                }
            });

            mMediaPlayer.prepareAsync();

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}











