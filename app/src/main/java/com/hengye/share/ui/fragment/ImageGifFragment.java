package com.hengye.share.ui.fragment;


import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.cache.BitmapCache;
import com.android.volley.toolbox.BitmapUtil;
import com.hengye.share.R;
import com.hengye.share.helper.SettingHelper;
import com.hengye.share.ui.base.BaseFragment;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.ui.widget.image.ClipImageView;

import java.io.File;
import java.io.IOException;

import pl.droidsonroids.gif.GifDrawable;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_gif, null);

        mGifView = (PhotoView) view.findViewById(R.id.iv_normal);
        mPhotoView = (ClipImageView) view.findViewById(R.id.cover);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        File gifFile = new File(mPath);
        try {
            GifDrawable gifFromFile = new GifDrawable(gifFile);
            mGifView.setImageDrawable(gifFromFile);
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

            runEnterAnimation();
        }

        if(SettingHelper.isClickToCloseGallery()) {
            mGifView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    getActivity().onBackPressed();
                }
            });
        }
        return view;
    }

    private void runEnterAnimation() {

        mPhotoView.setVisibility(View.VISIBLE);
        mGifView.setVisibility(View.GONE);
        AnimationRect.runEnterAnimation(mPhotoView, mRect, mGifView);
    }

    public void runExitAnimation(ObjectAnimator backgroundAnimator) {

        mGifView.setVisibility(View.GONE);
        mPhotoView.setVisibility(View.VISIBLE);
        AnimationRect rect = getArguments().getParcelable("rect");

        AnimationRect.runExitAnimation(mPhotoView, rect, backgroundAnimator);
    }

}
