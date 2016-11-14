package com.hengye.share.module.test;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.fragment.BaseFragment;
import com.hengye.share.service.VideoPlayService;
import com.hengye.share.ui.widget.media.MediaController;
import com.hengye.share.ui.widget.media.VideoView;
import com.hengye.share.util.L;

/**
 * Created by yuhy on 16/7/18.
 */
public class TestWindowManagerFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public String getTitle() {
        return "test";
    }


    @Override
    public int getLayoutResId() {
        return R.layout.fragment_test_window_manager;
    }

    MediaPlayer mMediaPlayer;
    SurfaceView mSurfaceView;
    SurfaceHolder mSurfaceHolder;
    SeekBar mVideoSeekBar;
    Button mVideoPlay, mVideoStop;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        View start = findViewById(R.id.btn_start);
        View stop = findViewById(R.id.btn_stop);

        start.setOnClickListener(this);
        stop.setOnClickListener(this);

//        mMediaPlayer = new MediaPlayer();
        mSurfaceView = (SurfaceView) findViewById(R.id.surface_video);
        mVideoSeekBar = (SeekBar) findViewById(R.id.seek_bar_video);
        mVideoPlay = (Button) findViewById(R.id.video_play);
        mVideoStop = (Button) findViewById(R.id.video_stop);

        mVideoPlay.setOnClickListener(this);
        mVideoStop.setOnClickListener(this);

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setFixedSize(100, 100);

        VideoView videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse("http://220.170.49.101/6/u/z/n/i/uznieiofgthpmyvbfvujnrezonnstk/he.yinyuetai.com/6644014F4059639839454D0994238FBE.flv?sc\\u003da317971751670318\\u0026br\\u003d3142\\u0026vid\\u003d2353215\\u0026aid\\u003d28047\\u0026area\\u003dML\\u0026vst\\u003d0"));
        MediaController mc = (MediaController) findViewById(R.id.media_controller);
        videoView.setMediaController(mc);
        videoView.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mMediaPlayer != null) {
            mMediaPlayer.release();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_start) {
            Intent intent = new Intent(getActivity(), VideoPlayService.class);
//            getActivity().stopService(new Intent(getActivity(), VideoPlayService.class));
            getActivity().startService(intent);
        } else if (id == R.id.btn_stop) {
            getActivity().stopService(new Intent(getActivity(), VideoPlayService.class));
        }


        if (id == R.id.video_stop) {
            mMediaPlayer.stop();
        } else if (id == R.id.video_play) {
            try {

                mSurfaceView.setBackground(null);
                if (mMediaPlayer != null) {
                    mMediaPlayer.reset();//恢复到未初始化的状态
                    mMediaPlayer.release();
                }
//            mMediaPlayer = MediaPlayer.create(getActivity(), R.raw.video_test);//读取视频
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setDataSource("http://220.170.49.101/6/u/z/n/i/uznieiofgthpmyvbfvujnrezonnstk/he.yinyuetai.com/6644014F4059639839454D0994238FBE.flv?sc\\u003da317971751670318\\u0026br\\u003d3142\\u0026vid\\u003d2353215\\u0026aid\\u003d28047\\u0026area\\u003dML\\u0026vst\\u003d0");
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
//                mMediaPlayer.start();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}











