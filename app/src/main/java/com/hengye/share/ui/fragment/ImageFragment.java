package com.hengye.share.ui.fragment;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.cache.BitmapCache;
import com.android.volley.cache.ImageDiskLruCache;
import com.android.volley.error.VolleyError;
import com.android.volley.request.ImageRequest;
import com.android.volley.toolbox.Util;
import com.hengye.share.R;
import com.hengye.share.ui.base.BaseFragment;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.util.FileUtil;
import com.hengye.share.util.ImageUtil;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.ToastUtil;

public class ImageFragment extends BaseFragment {

    public static ImageFragment newInstance(String url, AnimationRect rect,
                                            boolean animationIn, boolean firstEnter) {
        ImageFragment fragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putParcelable("rect", rect);
        bundle.putBoolean("animationIn", animationIn);
        bundle.putBoolean("firstEnter", firstEnter);
        fragment.setArguments(bundle);
        return fragment;
    }

    String mUrl, mPath;
    boolean mAnimateIn, mFirstEnter;
    AnimationRect mRect;
    View mProgress;

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        mUrl = bundle.getString("url");
        mAnimateIn = bundle.getBoolean("animationIn");
        mRect = bundle.getParcelable("rect");
        mFirstEnter = bundle.getBoolean("firstEnter");
    }

    @Override
    protected String getRequestTag() {
        return "ImageFragment";
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_image;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        mProgress = findViewById(R.id.progress_bar);
        if (mUrl != null) {
            String path;
            if(!Util.isHttpUrl(mUrl)){
                path = mUrl;
            }else {
                path = ImageDiskLruCache.getInstance().getDiskCachePath(mUrl);
            }
            if (path != null) {
                displayImage(mUrl, path, mAnimateIn);
                mAnimateIn = false;
                hideProgress();
            }else {
                int screenWidth = getResources().getDisplayMetrics().widthPixels;
                RequestManager.addToRequestQueue(makeImageRequest(mUrl, screenWidth, 0, ImageView.ScaleType.FIT_XY), getRequestTag());
            }
        }

    }

    public void saveImage(){
        if(mPath != null){
            String path;
            if((path = FileUtil.saveImage(mPath)) != null){
                ToastUtil.showToast(getString(R.string.topic_gallery_image_save_success, path));
            }
        }
    }

    private void hideProgress(){
        mProgress.setVisibility(View.GONE);
    }

    private void displayImage(String url, String path, boolean animateIn) {

        mPath = path;
        if (mFirstEnter && animateIn) {
            mFirstEnter = true;
            GalleryFragment fragment = (GalleryFragment)getFragmentManager().findFragmentById(R.id.content);
            if(fragment != null) {
                fragment.getShowBackgroundAnimation().start();
            }
        }

        Fragment fragment;
        if (!ImageUtil.isThisBitmapTooLargeToRead(path)) {
            if (ImageUtil.isThisPictureGif(url)) {
                fragment = ImageGifFragment.newInstance(path, mRect, animateIn);
            } else {
                fragment = ImageNormalFragment.newInstance(path, mRect, animateIn);
            }
        } else {
            fragment = ImageWebViewFragment.newInstance(path, animateIn);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.content, fragment)
                .commitAllowingStateLoss();
    }

    public void runExitAnimation(ObjectAnimator backgroundAnimator) {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.content);
        if (fragment instanceof ImageNormalFragment) {
            ImageNormalFragment child = (ImageNormalFragment) fragment;
            child.runExitAnimation(backgroundAnimator);
        } else if (fragment instanceof ImageGifFragment) {
            ImageGifFragment child = (ImageGifFragment) fragment;
            child.runExitAnimation(backgroundAnimator);
        }
    }

    public boolean canFinishWithAnimation() {
        if(mRect == null){
            return false;
        }
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.content);
        if (fragment instanceof ImageNormalFragment) {
            return true;
        } else if (fragment instanceof ImageGifFragment) {
            return true;
        } else {
            return false;
        }
    }

    protected Request<Bitmap> makeImageRequest(String requestUrl, int maxWidth, int maxHeight,
                                               ImageView.ScaleType scaleType) {
        return new ImageRequest(requestUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                String path = ImageDiskLruCache.getInstance().getDiskCachePath(mUrl);
                if (path != null) {
                    displayImage(mUrl, path, false);
                    BitmapCache.getInstance().putBitmap(path, response);
                }
                hideProgress();
            }
        }, maxWidth, maxHeight, scaleType, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgress();
                ToastUtil.showToast(getString(R.string.tip_load_image_fail));
            }
        });
    }
}
