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
import com.hengye.share.util.AnimationUtil;

import uk.co.senab.photoview.PhotoView;

public class ImageNormalFragment extends BaseFragment {

    public static ImageNormalFragment newInstance(String path, AnimationRect rect,
                                                  boolean animationIn) {
        ImageNormalFragment fragment = new ImageNormalFragment();
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

    private PhotoView photoView;
//    private TouchImageView photoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_normal, null);

        photoView = (PhotoView) view.findViewById(R.id.iv_normal);
//        photoView = (TouchImageView) view.findViewById(R.id.iv_normal);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        Bitmap bitmap = BitmapCache.getInstance().getBitmap(mPath);
        if(bitmap == null){
            bitmap = BitmapUtil.getSuitableBitmap(mPath, screenWidth, 0, BitmapUtil.DEFAULT_CONFIG, ImageView.ScaleType.FIT_XY);
            if(bitmap != null){
                BitmapCache.getInstance().putBitmap(mPath, bitmap);
            }
        }
        photoView.setImageBitmap(bitmap);

        if (mAnimateIn) {
            mAnimateIn = false;
            runEnterAnimation();
        }
        return view;
    }

    private void runEnterAnimation() {
        photoView.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        photoView.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (mRect == null) {
                            return true;
                        }

                        final Rect startBounds = new Rect(mRect.scaledBitmapRect);
                        final Rect finalBounds = AnimationRect
                                .getBitmapRectFromImageView(photoView);

                        if (finalBounds == null) {
                            return true;
                        }

                        float startScale = (float) finalBounds.width() / startBounds.width();

                        if (startScale * startBounds.height() > finalBounds.height()) {
                            startScale = (float) finalBounds.height() / startBounds.height();
                        }

                        int deltaTop = startBounds.top - finalBounds.top;
                        int deltaLeft = startBounds.left - finalBounds.left;

                        photoView.setPivotY(
                                (photoView.getHeight() - finalBounds.height()) / 2);
                        photoView.setPivotX((photoView.getWidth() - finalBounds.width()) / 2);

                        photoView.setScaleX(1 / startScale);
                        photoView.setScaleY(1 / startScale);

                        photoView.setTranslationX(deltaLeft);
                        photoView.setTranslationY(deltaTop);

                        photoView.animate().translationY(0).translationX(0)
                                .scaleY(1)
                                .scaleX(1).setDuration(AnimationUtil.GALLERY_ANIMATION_DURATION)
                                .setInterpolator(
                                        new AccelerateDecelerateInterpolator());

                        AnimatorSet animationSet = new AnimatorSet();
                        animationSet.setDuration(AnimationUtil.GALLERY_ANIMATION_DURATION);
                        animationSet
                                .setInterpolator(new AccelerateDecelerateInterpolator());

                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipBottom",
                                AnimationRect.getClipBottom(mRect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipRight",
                                AnimationRect.getClipRight(mRect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipTop", AnimationRect.getClipTop(mRect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipLeft", AnimationRect.getClipLeft(mRect, finalBounds), 0));

                        animationSet.start();

                        return true;
                    }
                });
    }

    public void runExitAnimation(ObjectAnimator backgroundAnimator) {

//        if (Math.abs(photoView.getScale() - 1.0f) > 0.1f) {
//            photoView.setScale(1, true);
//            return;
//        }

        AnimationRect rect = getArguments().getParcelable("rect");

        if (rect == null) {
            photoView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }

        final Rect startBounds = rect.scaledBitmapRect;
        final Rect finalBounds = AnimationRect.getBitmapRectFromImageView(photoView);

        if (finalBounds == null) {
            photoView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }

        if (isDevicePort() != rect.isScreenPortrait) {
            photoView.animate().alpha(0);
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

        photoView.setPivotY((photoView.getHeight() - finalBounds.height()) / 2);
        photoView.setPivotX((photoView.getWidth() - finalBounds.width()) / 2);

        photoView.animate().translationX(deltaLeft).translationY(deltaTop)
                .scaleY(startScaleFinal)
                .scaleX(startScaleFinal).setDuration(AnimationUtil.GALLERY_ANIMATION_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        photoView.animate().alpha(0.0f).setDuration(200);
                    }
                });

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setDuration(AnimationUtil.GALLERY_ANIMATION_DURATION);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());

        animationSet.playTogether(backgroundAnimator);

        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipBottom", 0,
                AnimationRect.getClipBottom(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipRight", 0,
                AnimationRect.getClipRight(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipTop", 0, AnimationRect.getClipTop(rect, finalBounds)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipLeft", 0, AnimationRect.getClipLeft(rect, finalBounds)));

        animationSet.start();
    }

    public static boolean isDevicePort() {
        return BaseApplication.getInstance().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }
}
