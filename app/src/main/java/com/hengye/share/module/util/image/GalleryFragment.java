package com.hengye.share.module.util.image;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.module.base.ActivityHelper;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.util.encapsulation.fragment.ViewPagerFragment;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.util.AnimationUtil;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.ViewUtil;

import java.util.ArrayList;

public class GalleryFragment extends ViewPagerFragment implements View.OnLongClickListener{

    public final static String IMG_URLS = "img_paths";
    public final static String IMG_INDEX = "img_index";
    public final static String IMG_RECT_LIST = "img_rect_list";
    private PhotoPagerAdapter mAdapter;
    private TextView mPages;
    private int mIndexStart;
    private ArrayList<String> mUrls;
    private ArrayList<AnimationRect> mRectList;
    private View mBackground;
    private ColorDrawable mBackgroundColor;
    private Dialog mLongClickDialog;

    @Override
    protected String getRequestTag() {
        return "GalleryFragment";
    }

    public static Bundle getStartArguments(ArrayList<String> urls,
                                           int index) {
        return getStartArguments(urls, index, null);
    }

    public static Bundle getStartArguments(ArrayList<String> urls,
                                           int index, ArrayList<AnimationRect> rectList) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(IMG_URLS, urls);
        bundle.putSerializable(IMG_RECT_LIST, rectList);
        bundle.putInt(IMG_INDEX, index);
        return bundle;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void handleBundleExtra(Bundle bundle) {
        mUrls = (ArrayList<String>) getArguments().getSerializable(IMG_URLS);
        mIndexStart = bundle.getInt(IMG_INDEX, 0);
        mRectList = (ArrayList<AnimationRect>) bundle.getSerializable(IMG_RECT_LIST);

        if (getImageSize() == 0) {
            getActivity().finish();
        }
    }

    protected int getImageSize(){
        return mUrls.size();
    }

    protected String getImageUrl(int position) {
        return mUrls.get(position);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_gallery;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(mAdapter = new PhotoPagerAdapter(getFragmentManager()));
        mBackground = getActivity().findViewById(android.R.id.content);
        mBackgroundColor = new ColorDrawable(Color.BLACK);
        mBackground.setBackground(mBackgroundColor);
        mPages = (TextView) findViewById(R.id.select_photo_gallery_pages);
        updatePage(mIndexStart);
        getViewPager().setCurrentItem(mIndexStart);
        if (getActivity() instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) getActivity();
            baseActivity.getActivityHelper().registerActivityActionInterceptListener(new ActivityHelper.DefaultActivityActionInterceptListener() {
                @Override
                public boolean onBackPressed() {
                    ImageFragment fragment = mFragmentMap.get(getViewPager().getCurrentItem());
                    if (fragment != null && fragment.canFinishWithAnimation()) {
                        fragment.runExitAnimation(getHideBackgroundAnimation());
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }
    }

    private Dialog getLongClickDialog(){
        if(mLongClickDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setItems(new CharSequence[]{ResUtil.getString(R.string.label_gallery_save_to_local)}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mAdapter.getItem(getCurrentPosition()).saveImage();
                        }
                    });
            mLongClickDialog = builder.create();
        }
        return mLongClickDialog;
    }

    @Override
    public boolean onLongClick(View v) {
        ViewUtil.vibrate(v);
        getLongClickDialog().show();
        return true;
    }

    public void updatePage(int pageNo) {
        mPages.setText(pageNo + 1 + "/" + getImageSize());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        updatePage(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        super.onPageScrollStateChanged(state);
    }

    private SparseArray<ImageFragment> mFragmentMap = new SparseArray<>();

    private boolean mHasAnimateIn = false;

    class PhotoPagerAdapter extends FragmentPagerAdapter {

        public PhotoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public ImageFragment getItem(int position) {

            ImageFragment fragment = mFragmentMap.get(position);
            if (fragment == null) {

                boolean animateIn = (mIndexStart == position) && !mHasAnimateIn;
                fragment = ImageFragment
                        .newInstance(getImageUrl(position), mRectList == null ? null : mRectList.get(position), animateIn,
                                mIndexStart == position);
                mHasAnimateIn = true;
                mFragmentMap.put(position, fragment);
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return getImageSize();
        }

        //when activity is recycled, ViewPager will reuse fragment by theirs name, so
        //getItem wont be called, but we need mFragmentMap to animate close operation
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (object instanceof ImageFragment) {
                mFragmentMap.put(position, (ImageFragment) object);
            }
        }

    }

    ObjectAnimator mShowBackgroundAnimation;

    public ObjectAnimator getShowBackgroundAnimation() {
        if (mShowBackgroundAnimation == null) {
            mShowBackgroundAnimation = ObjectAnimator.ofInt(mBackgroundColor, "alpha", 0, 255);
            mShowBackgroundAnimation.setDuration(AnimationUtil.GALLERY_SHOW_BACKGROUND_ANIMATION_DURATION);
        }
        return mShowBackgroundAnimation;
    }

    ObjectAnimator mHideBackgroundAnimation;

    public ObjectAnimator getHideBackgroundAnimation() {
        if (mHideBackgroundAnimation == null) {
            mHideBackgroundAnimation = ObjectAnimator.ofInt(mBackgroundColor, "alpha", 255, 0);
            mHideBackgroundAnimation.setDuration(AnimationUtil.GALLERY_HIDE_BACKGROUND_ANIMATION_DURATION);
            mHideBackgroundAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    getActivity().finish();
                    getActivity().overridePendingTransition(0, 0);
                }
            });
        }
        return mHideBackgroundAnimation;
    }


    public interface ImageUrl {
        String getUrl();
    }

    public static class Image implements ImageUrl {

        public String url;

        public Image(String url) {
            this.url = url;
        }

        public static ArrayList<Image> convert(ArrayList<String> urls) {
            ArrayList<Image> images = new ArrayList<>();
            for (String url : urls) {
                images.add(new Image(url));
            }
            return images;
        }

        @Override
        public String getUrl() {
            return url;
        }
    }
}
