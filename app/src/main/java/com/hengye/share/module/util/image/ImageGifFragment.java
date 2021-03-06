package com.hengye.share.module.util.image;


import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageKey;
import com.hengye.share.R;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.ui.widget.image.ClipImageView;
import com.hengye.share.util.RequestManager;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifIOException;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageGifFragment extends ImageBaseFragment {

    public static ImageGifFragment newInstance(String path, AnimationRect rect,
                                                  boolean animationIn) {
        ImageGifFragment fragment = new ImageGifFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putParcelable("rect", rect);
        bundle.putBoolean("animationIn", animationIn);
        fragment.setArguments(bundle);
        return fragment;
    }

    String mPath;
    boolean mAnimateIn;
    AnimationRect mRect;

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        mPath = bundle.getString("path");
        mAnimateIn = bundle.getBoolean("animationIn");
        mRect = bundle.getParcelable("rect");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private PhotoView mGifView;
    private ClipImageView mPhotoView;
    private int mScreenWidth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_gif, container, false);

        mGifView = (PhotoView) view.findViewById(R.id.iv_normal);
        mPhotoView = (ClipImageView) view.findViewById(R.id.iv_assist);
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;

        mGifView.setOnLongClickListener(this);

        File gifFile = new File(mPath);
        try {
            GifDrawable gifFromFile = new GifDrawable(gifFile);
            mGifView.setImageDrawable(gifFromFile);
        }catch (GifIOException gIOE) {
//            gIOE.printStackTrace();
            //不是gif
            setBitmapToImageView(mGifView);
        }catch (IOException e) {
            e.printStackTrace();
        }

        setBitmapToImageView(mPhotoView);
        if (mAnimateIn) {
            mAnimateIn = false;
            runEnterAnimation();
        }

        if(SettingHelper.isClickToCloseGallery()) {
            mGifView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    ImageGifFragment.this.onClick(view);
                }
            });
        }
        return view;
    }

    private void setBitmapToImageView(ImageView iv){
        ImageKey imageKey = new ImageKey(mPath, mScreenWidth, 0, ImageView.ScaleType.FIT_XY);
        Bitmap bitmap = RequestManager.getBitmapByLocalPath(imageKey);
        iv.setImageBitmap(bitmap);
    }

    private void runEnterAnimation() {

        mPhotoView.setVisibility(View.VISIBLE);
        mGifView.setVisibility(View.GONE);
        AnimationRect.runEnterAnimation(mPhotoView, mRect, mGifView);
    }

    public void runExitAnimation(ObjectAnimator backgroundAnimator) {

        mGifView.setVisibility(View.GONE);
        mPhotoView.setVisibility(View.VISIBLE);
//        setBitmapToPhotoView();
//        AnimationRect rect = getArguments().getParcelable("rect");

        AnimationRect.runExitAnimation(mPhotoView, mRect, backgroundAnimator);
    }

}
