package com.hengye.share.module.util.image;


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
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.module.base.BaseFragment;
import com.hengye.share.ui.support.AnimationRect;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

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
    protected void handleBundleExtra(Bundle bundle) {
        mPath = bundle.getString("path");
        mAnimateIn = bundle.getBoolean("animationIn");
        mRect = bundle.getParcelable("rect");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private PhotoView mPhotoView;
//    private TouchImageView mPhotoView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_normal, container, false);

        mPhotoView = (PhotoView) view.findViewById(R.id.iv_normal);
//        mPhotoView = (TouchImageView) view.findViewById(R.id.iv_normal);
        int screenWidth = getResources().getDisplayMetrics().widthPixels;

        Bitmap bitmap = BitmapCache.getInstance().getBitmap(mPath);
        if (bitmap == null) {
            bitmap = BitmapUtil.getSuitableBitmap(mPath, screenWidth, 0, BitmapUtil.DEFAULT_CONFIG, ImageView.ScaleType.FIT_XY);
            if (bitmap != null) {
                BitmapCache.getInstance().putBitmap(mPath, bitmap);
            }
        }
        mPhotoView.setImageBitmap(bitmap);

        if(SettingHelper.isClickToCloseGallery()) {
            mPhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    getActivity().onBackPressed();
                }
            });
        }

        if (mAnimateIn) {
            mAnimateIn = false;
            runEnterAnimation();
        }
        return view;
    }

    public void runEnterAnimation() {
        AnimationRect.runEnterAnimation(mPhotoView, mRect);
    }

    public void runExitAnimation(ObjectAnimator backgroundAnimator) {
        mPhotoView.resetMatrix();
        AnimationRect rect = getArguments().getParcelable("rect");
        AnimationRect.runExitAnimation(mPhotoView, rect, backgroundAnimator);
    }

}
