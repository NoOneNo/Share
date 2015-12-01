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
import com.hengye.share.util.L;
import com.hengye.volleyplus.toolbox.RequestManager;

public class TopicGalleryFragment extends Fragment {

    public static TopicGalleryFragment newInstance(String url, AnimationRect rect,
                                                boolean animationIn, boolean firstOpenPage) {
        TopicGalleryFragment fragment = new TopicGalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putParcelable("rect", rect);
        bundle.putBoolean("animationIn", animationIn);
        bundle.putBoolean("firstOpenPage", firstOpenPage);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_gallery, container, false);

        Bundle bundle = getArguments();
        final String url = bundle.getString("url");
        boolean animateIn = bundle.getBoolean("animationIn");
        bundle.putBoolean("animationIn", false);

        String path = ImageDiskLruCache.getInstance().getDiskCachePath(url);
//        String path = FileManager.getFilePathFromUrl(url, FileLocationMethod.picture_large);

        if (path != null) {
            displayPicture(path, animateIn);
        } else {
            int screenWidth = getResources().getDisplayMetrics().widthPixels;
            RequestManager.getImageLoader().get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate, boolean isFromCache) {
                    String path = ImageDiskLruCache.getInstance().getDiskCachePath(url);
                    if(path != null){
                        displayPicture(path, false);
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    L.debug("request image fail, url : {}", url);
                }
            }, screenWidth, 0, ImageView.ScaleType.MATRIX);
//            GalleryAnimationActivity activity = (GalleryAnimationActivity) getActivity();
//            activity.showBackgroundImmediately();
//            progressView.setVisibility(View.VISIBLE);
//            wait.setVisibility(View.VISIBLE);
//            TimeLineBitmapDownloader.getInstance()
//                    .download(this, url, FileLocationMethod.picture_large, downloadCallback);
        }

        return view;
    }

    private void displayPicture(String path, boolean animateIn) {
        TopicGalleryActivity activity = (TopicGalleryActivity) getActivity();

        AnimationRect rect = getArguments().getParcelable("rect");
        boolean firstOpenPage = getArguments().getBoolean("firstOpenPage");

        if (firstOpenPage) {
            if (animateIn) {
                ObjectAnimator animator = activity.showBackgroundAnimate();
                animator.start();
            } else {
                activity.showBackgroundImmediately();
            }
            getArguments().putBoolean("firstOpenPage", false);
        }

        Fragment fragment = null;
        if (!ImageUtil.isThisBitmapTooLargeToRead(path)) {
            if (ImageUtil.isThisPictureGif(path)) {
                fragment = ImageGifFragment.newInstance(path, rect, animateIn);
            } else {
                fragment = ImageNormalFragment.newInstance(path, rect, animateIn);
            }
        } else {
            fragment = ImageLargeFragment.newInstance(path, animateIn);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.content, fragment)
                .commitAllowingStateLoss();
    }

    public void animationExit(ObjectAnimator backgroundAnimator) {
        Fragment fragment = getChildFragmentManager().findFragmentById(R.id.content);
        if (fragment instanceof ImageNormalFragment) {
            ImageNormalFragment child = (ImageNormalFragment) fragment;
            child.animationExit(backgroundAnimator);
        } else if (fragment instanceof ImageGifFragment) {
            ImageGifFragment child = (ImageGifFragment) fragment;
            child.animationExit(backgroundAnimator);
        }
    }

    public boolean canAnimateCloseActivity() {
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
