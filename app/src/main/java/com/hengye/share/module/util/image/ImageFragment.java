package com.hengye.share.module.util.image;

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
import com.hengye.share.module.util.encapsulation.fragment.BaseFragment;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.util.FileUtil;
import com.hengye.share.util.ImageUtil;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.thirdparty.WBUtil;

public class ImageFragment extends BaseFragment implements View.OnLongClickListener{

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
            boolean isLocalPath = !Util.isHttpUrl(mUrl);
            if(isLocalPath){
                //本地图片地址，如果是草稿的话
                path = mUrl;
            }else {
                path = ImageDiskLruCache.getInstance().getDiskCachePath(mUrl);
            }

            if(isLocalPath || !mUrl.endsWith("gif")) {
                //如果是本地路径或者不是gif图片
                String largeUrlPath = ImageDiskLruCache.getInstance().getDiskCachePath(WBUtil.getWBLargeImgUrl(mUrl));
                if(largeUrlPath != null){
                    path = largeUrlPath;
                }

                if (path != null) {
                    displayImage(mUrl, path, mAnimateIn);
                    mAnimateIn = false;
                    hideProgress();
                } else {
                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
                    RequestManager.addToRequestQueue(makeImageRequest(mUrl, screenWidth, 0, ImageView.ScaleType.FIT_XY), getRequestTag());
                }
            }else{
                String gifUrl = WBUtil.getWBGifUrl(mUrl);
                String gifPath = ImageDiskLruCache.getInstance().getDiskCachePath(gifUrl);
                if(gifPath != null){
                    //本地有gif缓存，直接显示
                    displayImage(gifUrl, gifPath, mAnimateIn);
                    mAnimateIn = false;
                    hideProgress();
                }else{
                    //本地没有gif缓存
                    if(path != null){
                        //先显示gif的第一帧图片
                        displayImage(mUrl, path, mAnimateIn);
                        mAnimateIn = false;
                    }
                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
                    RequestManager.addToRequestQueue(makeImageRequest(gifUrl, screenWidth, 0, ImageView.ScaleType.FIT_XY), getRequestTag());
                }

            }
        }

    }

    @Override
    public boolean onLongClick(View v) {
        if(getActivity() != null){
            Fragment fragment = getActivity().getSupportFragmentManager()
                    .findFragmentById(R.id.content);
            if(fragment != null && fragment instanceof GalleryFragment){
                return ((GalleryFragment)fragment).onLongClick(v);
            }
        }
        return false;
    }

    public void saveImage(){
        if(mPath != null && mUrl != null){
            String path;
            if((path = FileUtil.saveImage(mPath, mUrl.endsWith("gif"))) != null){
                FileUtil.sendMediaScanIntent(getContext(), path);
                ToastUtil.showToast(getString(R.string.tip_gallery_image_save_success, path));
            }
        }
    }

    public void loadLargeImage(){
        if(mUrl != null){
            if(!WBUtil.isWBLargeUrl(mUrl)){
                String largeUrl = WBUtil.getWBLargeImgUrl(mUrl);
                String path = ImageDiskLruCache.getInstance().getDiskCachePath(largeUrl);
                if(path != null){
                    //本地有原图缓存，直接显示
                    displayImage(largeUrl, path, false);
                    mAnimateIn = false;
                    hideProgress();
                }else{
                    //本地没有原图缓存
                    showProgress();
                    int screenWidth = getResources().getDisplayMetrics().widthPixels;
                    RequestManager.addToRequestQueue(makeImageRequest(largeUrl, screenWidth, 0, ImageView.ScaleType.FIT_XY), getRequestTag());
                }
            }
        }
    }

    private void showProgress(){
        mProgress.setVisibility(View.VISIBLE);
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
            if (WBUtil.isWBGifUrl(url)) {
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
            return ((ImageNormalFragment)fragment).canAnimatedOut();
        } else if (fragment instanceof ImageGifFragment) {
            return true;
        } else {
            return false;
        }
    }

    protected Request<Bitmap> makeImageRequest(final String requestUrl, int maxWidth, int maxHeight,
                                               ImageView.ScaleType scaleType) {
        return new ImageRequest(requestUrl, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                String path = ImageDiskLruCache.getInstance().getDiskCachePath(requestUrl);
                if (path != null) {
                    displayImage(requestUrl, path, false);
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
