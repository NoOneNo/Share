package com.hengye.share.ui.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.cache.ImageDiskLruCache;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.hengye.share.R;
import com.hengye.share.ui.activity.TopicGalleryActivity;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.util.ImageUtil;
import com.hengye.volleyplus.toolbox.RequestManager;

public class TopicGalleryFragment extends BaseFragment {

    public static TopicGalleryFragment newInstance(String url, AnimationRect rect,
                                                   boolean animationIn, boolean firstEnter) {
        TopicGalleryFragment fragment = new TopicGalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putParcelable("rect", rect);
        bundle.putBoolean("animationIn", animationIn);
        bundle.putBoolean("firstEnter", firstEnter);
        fragment.setArguments(bundle);
        return fragment;
    }

    String mUrl;
    boolean mAnimateIn, mFirstEnter;
    AnimationRect mRect;

    @Override
    protected void handleBundleExtra() {
        mUrl = getArguments().getString("url");
        mAnimateIn = getArguments().getBoolean("animationIn");
        mRect = getArguments().getParcelable("rect");
        mFirstEnter = getArguments().getBoolean("firstEnter");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_gallery, container, false);

        String path = ImageDiskLruCache.getInstance().getDiskCachePath(mUrl);

        if (path != null) {
            displayImage(path, mAnimateIn);
            mAnimateIn = false;
        } else {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            RequestManager.getImageLoader().get(mUrl, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate, boolean isFromCache) {
                    String path = ImageDiskLruCache.getInstance().getDiskCachePath(mUrl);
                    if (path != null) {
                        displayImage(path, false);
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                }
            }, screenWidth, 0, ImageView.ScaleType.FIT_XY);
        }

        return view;
    }

    private void displayImage(String path, boolean animateIn) {

        if (mFirstEnter && animateIn) {
            mFirstEnter = true;
            TopicGalleryActivity activity = (TopicGalleryActivity) getActivity();
            activity.getShowBackgroundAnimation().start();
        }

        Fragment fragment;
        if (!ImageUtil.isThisBitmapTooLargeToRead(path)) {
            if (ImageUtil.isThisPictureGif(path)) {
                fragment = ImageGifFragment.newInstance(path, mRect, animateIn);
            } else {
                fragment = ImageNormalFragment.newInstance(path, mRect, animateIn);
            }
        } else {
            fragment = ImageLargeFragment.newInstance(path, animateIn);
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
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.content);
        if (fragment instanceof ImageNormalFragment) {
            return true;
        } else if (fragment instanceof ImageGifFragment) {
            return true;
        } else {
            return false;
        }
    }
}
