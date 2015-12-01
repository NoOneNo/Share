package com.hengye.share.ui.fragment;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.toolbox.BitmapUtil;
import com.hengye.share.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.ui.support.AnimationRect;

import uk.co.senab.photoview.PhotoView;

public class ImageGifFragment extends Fragment {

    private static final int IMAGEVIEW_SOFT_LAYER_MAX_WIDTH = 2000;
    private static final int IMAGEVIEW_SOFT_LAYER_MAX_HEIGHT = 3000;

    public static final int ANIMATION_DURATION = 300;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private PhotoView photoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_viewpager_topic_gallery_, null);

//        final String url = paths.get(position);
        photoView = (PhotoView) view.findViewById(R.id.select_photo_gallery_fragment_iv);
//        photoView = (TouchImageView) view.findViewById(R.id.select_photo_gallery_fragment_iv);
        final ProgressBar pb = (ProgressBar) view.findViewById(R.id.pb_image_large);
        final Button imageLarge = (Button) view.findViewById(R.id.btn_image_large);

//            if(position == mIndexInit && !hasAnimatedIn){
//                hasAnimatedIn = true;
//                Cache.Entry entry = RequestManager.getCache(url);
//                if(entry != null) {
//                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
//                    int screenHeight = getResources().getDisplayMetrics().heightPixels;
//                    Bitmap bitmap = BitmapUtil.getSuitableBitmap(entry.data, screenWidth, screenHeight, BitmapUtil.DEFAULT_CONFIG, touchIv.getScaleType());
//                    if(bitmap != null){
//                        touchIv.setImageBitmap(bitmap);
//                        doAnimation(touchIv, mRects.get(position));
//                    }
//                }
//            }else{

        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        final String path = getArguments().getString("path");
        boolean animateIn = getArguments().getBoolean("animationIn");
        final AnimationRect rect = getArguments().getParcelable("rect");

        if (!animateIn) {
//            RequestManager.getImageLoader().get(url, new ImageLoader.ImageListener() {
//                @Override
//                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate, boolean isFromCache) {
//                    touchIv.setImageBitmap(response.getBitmap());
//                }
//
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    L.debug("request image fail, url : {}", url);
//                }
//            }, screenWidth, 0, touchIv.getScaleType());

            return view;
        }

//        final Bitmap bitmap = ImageUtility
//                .decodeBitmapFromSDCard(path, IMAGEVIEW_SOFT_LAYER_MAX_WIDTH,
//                        IMAGEVIEW_SOFT_LAYER_MAX_HEIGHT);
        final Bitmap bitmap = BitmapUtil.getSuitableBitmap(path, screenWidth, 0, BitmapUtil.DEFAULT_CONFIG, ImageView.ScaleType.FIT_XY);

        photoView.setImageBitmap(bitmap);

        final Runnable endAction = new Runnable() {
            @Override
            public void run() {
                Bundle bundle = getArguments();
                bundle.putBoolean("animationIn", false);
            }
        };

        photoView.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {

                        if (rect == null) {
                            photoView.getViewTreeObserver().removeOnPreDrawListener(this);
                            endAction.run();
                            return true;
                        }

                        final Rect startBounds = new Rect(rect.scaledBitmapRect);
                        final Rect finalBounds = AnimationRect
                                .getBitmapRectFromImageView(photoView);

                        if (finalBounds == null) {
                            photoView.getViewTreeObserver().removeOnPreDrawListener(this);
                            endAction.run();
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
                                .scaleX(1).setDuration(ANIMATION_DURATION)
                                .setInterpolator(
                                        new AccelerateDecelerateInterpolator())
                                .withEndAction(endAction);

                        AnimatorSet animationSet = new AnimatorSet();
                        animationSet.setDuration(ANIMATION_DURATION);
                        animationSet
                                .setInterpolator(new AccelerateDecelerateInterpolator());

                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipBottom",
                                AnimationRect.getClipBottom(rect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipRight",
                                AnimationRect.getClipRight(rect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipTop", AnimationRect.getClipTop(rect, finalBounds), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipLeft", AnimationRect.getClipLeft(rect, finalBounds), 0));

                        animationSet.start();

                        photoView.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                });

        return view;
    }

    public void animationExit(ObjectAnimator backgroundAnimator) {

//        if (Math.abs(photoView.getScale() - 1.0f) > 0.1f) {
//            photoView.setScale(1, true);
//            return;
//        }

        getActivity().overridePendingTransition(0, 0);
        animateClose(backgroundAnimator);
    }

    private void animateClose(ObjectAnimator backgroundAnimator) {

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
                .scaleX(startScaleFinal).setDuration(ANIMATION_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {

                        photoView.animate().alpha(0.0f).setDuration(200).withEndAction(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                    }
                });

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setDuration(ANIMATION_DURATION);
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
