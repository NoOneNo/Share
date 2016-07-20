package com.hengye.share.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.ui.fragment.TopicGalleryFragment;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.AnimationUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class TopicGalleryActivity extends BaseActivity {

    public final static String IMG_URLS = "img_paths";
    public final static String IMG_INDEX = "img_index";
    public final static String IMG_RECT_LIST = "img_rect_list";
    private ViewPager mViewPager;
    private PhotoPagerAdapter mAdapter;
    private TextView mPages;
    private int mIndexStart;
    private ArrayList<String> mUrls;
    private ArrayList<AnimationRect> mRectList;
    private View mBackground, mSaveBtn;
    private ColorDrawable mBackgroundColor;

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    protected boolean setCustomTheme() {
        return false;
    }

    @Override
    protected String getRequestTag() {
        return "TopicGalleryActivity";
    }

    public static void startWithIntent(Context context, ArrayList<String> urls, ArrayList<AnimationRect> rectList,
                                   int index) {
        Intent intent = new Intent(context, TopicGalleryActivity.class);
        intent.putExtra(IMG_URLS, urls);
        intent.putExtra(IMG_RECT_LIST, rectList);
        intent.putExtra(IMG_INDEX, index);
        context.startActivity(intent);
        if(context instanceof Activity){
            ((Activity)context).overridePendingTransition(0, 0);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void handleBundleExtra(Intent intent){
        mUrls = (ArrayList<String>) intent.getSerializableExtra(IMG_URLS);
        mIndexStart = intent.getIntExtra(IMG_INDEX, 0);
        mRectList = (ArrayList<AnimationRect>) intent.getSerializableExtra(IMG_RECT_LIST);

        if(CommonUtil.hasEmpty(mUrls, mRectList)){
            this.finish();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_topic_gallery;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mViewPager = (ViewPager) findViewById(R.id.select_photo_gallery_view_pager);
        mPages = (TextView) findViewById(R.id.select_photo_gallery_pages);
        mBackground = findViewById(android.R.id.content);
        mBackgroundColor = new ColorDrawable(Color.BLACK);
        mBackground.setBackground(mBackgroundColor);
        initViewPager();
        mSaveBtn = findViewById(R.id.btn_image_save);
        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapter.getItem(mViewPager.getCurrentItem()).saveImage();
            }
        });
    }

    public void initViewPager() {
        mViewPager.setAdapter(mAdapter = new PhotoPagerAdapter(getSupportFragmentManager()));
        mViewPager.setCurrentItem(mIndexStart);// 设置当前显示标签页为显示页
        mPages.setText(mIndexStart + 1 + "/" + mUrls.size());
        mViewPager.addOnPageChangeListener(new GalleryPageChangeListener());// 页面变化时的监听器
    }

    class GalleryPageChangeListener implements OnPageChangeListener{
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mPages.setText(position + 1 + "/" + mUrls.size());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private HashMap<Integer, TopicGalleryFragment> mFragmentMap = new HashMap<>();

    private boolean mHasAnimateIn = false;

    class PhotoPagerAdapter extends FragmentPagerAdapter {

        public PhotoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public TopicGalleryFragment getItem(int position) {

            TopicGalleryFragment fragment = mFragmentMap.get(position);
            if (fragment == null) {

                boolean animateIn = (mIndexStart == position) && !mHasAnimateIn;
                fragment = TopicGalleryFragment
                        .newInstance(mUrls.get(position), mRectList.get(position), animateIn,
                                mIndexStart == position);
                mHasAnimateIn = true;
                mFragmentMap.put(position, fragment);
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return mUrls.size();
        }

        //when activity is recycled, ViewPager will reuse fragment by theirs name, so
        //getItem wont be called, but we need mFragmentMap to animate close operation
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (object instanceof TopicGalleryFragment) {
                mFragmentMap.put(position, (TopicGalleryFragment) object);
            }
        }

    }

    ObjectAnimator mShowBackgroundAnimation;
    public ObjectAnimator getShowBackgroundAnimation() {
        if(mShowBackgroundAnimation == null) {
            mShowBackgroundAnimation = ObjectAnimator.ofInt(mBackgroundColor, "alpha", 0, 255);
            mShowBackgroundAnimation.setDuration(AnimationUtil.GALLERY_SHOW_BACKGROUND_ANIMATION_DURATION);
        }
        return mShowBackgroundAnimation;
    }

    ObjectAnimator mHideBackgroundAnimation;
    public ObjectAnimator getHideBackgroundAnimation() {
        if(mHideBackgroundAnimation == null) {
            mHideBackgroundAnimation = ObjectAnimator.ofInt(mBackgroundColor, "alpha", 255, 0);
            mHideBackgroundAnimation.setDuration(AnimationUtil.GALLERY_HIDE_BACKGROUND_ANIMATION_DURATION);
            mHideBackgroundAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    finish();
                    overridePendingTransition(0, 0);
                }
            });
        }
        return mHideBackgroundAnimation;
    }

    @Override
    public void onBackPressed() {
        TopicGalleryFragment fragment = mFragmentMap.get(mViewPager.getCurrentItem());
        if (fragment != null && fragment.canFinishWithAnimation()) {
            fragment.runExitAnimation(getHideBackgroundAnimation());
        } else {
            super.onBackPressed();
        }
    }

}
