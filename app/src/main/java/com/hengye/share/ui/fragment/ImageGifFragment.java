package com.hengye.share.ui.fragment;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.android.volley.cache.BitmapCache;
import com.android.volley.toolbox.BitmapUtil;
import com.hengye.share.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.ui.view.ClipImageView;
import com.hengye.share.util.AnimationUtil;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import uk.co.senab.photoview.PhotoView;

public class ImageGifFragment extends BaseFragment {

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
    protected void handleBundleExtra() {
        mPath = getArguments().getString("path");
        mAnimateIn = getArguments().getBoolean("animationIn");
        mRect = getArguments().getParcelable("rect");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private PhotoView mGifView;
    private ClipImageView mPhotoView;
//    private TouchImageView mPhotoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_normal, null);

        mGifView = (PhotoView) view.findViewById(R.id.iv_normal);
        mPhotoView = (ClipImageView) view.findViewById(R.id.cover);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        File gifFile = new File(mPath);
        try {
            GifDrawable gifFromFile = new GifDrawable(gifFile);
            mPhotoView.setImageDrawable(gifFromFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mAnimateIn) {
            mAnimateIn = false;

            Bitmap bitmap = BitmapCache.getInstance().getBitmap(mPath);
            if(bitmap == null){
                bitmap = BitmapUtil.getSuitableBitmap(mPath, screenWidth, 0, BitmapUtil.DEFAULT_CONFIG, ImageView.ScaleType.FIT_XY);
                if(bitmap != null){
                    BitmapCache.getInstance().putBitmap(mPath, bitmap);
                }
            }
            mPhotoView.setImageBitmap(bitmap);
            mPhotoView.setVisibility(View.VISIBLE);
            mGifView.setVisibility(View.GONE);

            runEnterAnimation();
        }
        return view;
    }

    private void runEnterAnimation() {

        mPhotoView.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mPhotoView.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (mRect == null) {
                            return true;
                        }

                        final Rect startBounds = new Rect(mRect.scaledBitmapRect);
                        final Rect finalBounds = AnimationRect
                                .getBitmapRectFromImageView(mPhotoView);

                        if (finalBounds == null) {
                            return true;
                        }

                        float startScale = (float) finalBounds.width() / startBounds.width();

                        if (startScale * startBounds.height() > finalBounds.height()) {
                            startScale = (float) finalBounds.height() / startBounds.height();
                        }

                        int deltaTop = startBounds.top - finalBounds.top;
                        int deltaLeft = startBounds.left - finalBounds.left;

                        mPhotoView.setPivotY(
                                (mPhotoView.getHeight() - finalBounds.height()) / 2);
                        mPhotoView.setPivotX((mPhotoView.getWidth() - finalBounds.width()) / 2);

                        mPhotoView.setScaleX(1 / startScale);
                        mPhotoView.setScaleY(1 / startScale);

                        mPhotoView.setTranslationX(deltaLeft);
                        mPhotoView.setTranslationY(deltaTop);

                        mPhotoView.animate().translationY(0).translationX(0)
                                .scaleY(1)
                                .scaleX(1).setDuration(AnimationUtil.GALLERY_ANIMATION_DURATION)
                                .setInterpolator(
                                        new AccelerateDecelerateInterpolator())
                                .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                mGifView.setVisibility(View.VISIBLE);
                                mPhotoView.setVisibility(View.GONE);
                            }
                        });

                        AnimatorSet animationSet = new AnimatorSet();
                        animationSet.setDuration(AnimationUtil.GALLERY_ANIMATION_DURATION);
                        animationSet
                                .setInterpolator(new AccelerateDecelerateInterpolator());

                        animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                                "clipBottom",
                                AnimationRect.getClipBottom(mRect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                                "clipRight",
                                AnimationRect.getClipRight(mRect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                                "clipTop", AnimationRect.getClipTop(mRect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                                "clipLeft", AnimationRect.getClipLeft(mRect, finalBounds), 0));

                        animationSet.start();

                        return true;
                    }
                });
    }

    public void runExitAnimation(ObjectAnimator backgroundAnimator) {

//        if (Math.abs(mPhotoView.getScale() - 1.0f) > 0.1f) {
//            mPhotoView.setScale(1, true);
//            return;
//        }
        mGifView.setVisibility(View.GONE);
        mPhotoView.setVisibility(View.VISIBLE);
        AnimationRect rect = getArguments().getParcelable("rect");

        if (rect == null) {
            mPhotoView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }

        final Rect startBounds = rect.scaledBitmapRect;
        final Rect finalBounds = AnimationRect.getBitmapRectFromImageView(mPhotoView);

        if (finalBounds == null) {
            mPhotoView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }

        if (isDevicePort() != rect.isScreenPortrait) {
            mPhotoView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
        }

        final float startScaleFinal = startScale;

        int deltaTop = startBounds.top - finalBounds.top;
        int deltaLeft = startBounds.left - finalBounds.left;

        mPhotoView.setPivotY((mPhotoView.getHeight() - finalBounds.height()) / 2);
        mPhotoView.setPivotX((mPhotoView.getWidth() - finalBounds.width()) / 2);

        mPhotoView.animate().translationX(deltaLeft).translationY(deltaTop)
                .scaleY(startScaleFinal)
                .scaleX(startScaleFinal).setDuration(AnimationUtil.GALLERY_ANIMATION_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        mPhotoView.animate().alpha(0.0f).setDuration(200);
                    }
                });

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setDuration(AnimationUtil.GALLERY_ANIMATION_DURATION);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());

        animationSet.playTogether(backgroundAnimator);

        animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                "clipBottom", 0,
                AnimationRect.getClipBottom(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                "clipRight", 0,
                AnimationRect.getClipRight(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                "clipTop", 0, AnimationRect.getClipTop(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(mPhotoView,
                "clipLeft", 0, AnimationRect.getClipLeft(rect, finalBounds)));

        animationSet.start();
    }

    public static boolean isDevicePort() {
        return BaseApplication.getInstance().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }
}
