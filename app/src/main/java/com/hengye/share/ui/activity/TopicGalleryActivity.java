package com.hengye.share.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.ui.fragment.TopicGalleryFragment;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.util.CommonUtil;

import java.util.ArrayList;
import java.util.HashMap;

public class TopicGalleryActivity extends BaseActivity {

    public final static String IMG_URLS = "img_paths";
    public final static String IMG_INDEX = "img_index";
    public final static String IMG_RECTS = "img_rects";
    private ViewPager mViewPager;
    private TextView mPages;
    private int mIndexNow, mIndexStart;
    private ArrayList<String> mUrls;
    private ArrayList<AnimationRect> mRects;
    private boolean hasAnimatedIn = false;
    private View background;
    private ColorDrawable backgroundColor;

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

    public static void startWithIntent(Context context, ArrayList<String> urls, ArrayList<AnimationRect> rects,
                                   int index) {
        Intent intent = new Intent(context, TopicGalleryActivity.class);
        intent.putExtra(IMG_URLS, urls);
        intent.putExtra(IMG_RECTS, rects);
        intent.putExtra(IMG_INDEX, index);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_gallery);

        mViewPager = (ViewPager) findViewById(R.id.select_photo_gallery_view_pager);
        mPages = (TextView) findViewById(R.id.select_photo_gallery_pages);
        background = findViewById(android.R.id.content);

        initViewPager();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void getBundleExtra(){
        mUrls = (ArrayList<String>) getIntent().getSerializableExtra(IMG_URLS);
        mIndexNow = mIndexStart = getIntent().getIntExtra(IMG_INDEX, 0);
        mRects = (ArrayList<AnimationRect>) getIntent().getSerializableExtra(IMG_RECTS);

        if(CommonUtil.isEmptyCollection(mUrls, mRects)){
            this.finish();
        }
    }

    public void initViewPager() {

        DisplayMetrics dm = getResources().getDisplayMetrics();

        // 给ViewPager设置适配器
        mViewPager.setAdapter(new PhotoPagerAdapter(getSupportFragmentManager()));
//        mViewPager.setAdapter(new PhotoPagerAdapter(this, mUrls));
        mViewPager.setCurrentItem(mIndexNow);// 设置当前显示标签页为显示页
        mPages.setText(mIndexNow + 1 + "/" + mUrls.size());
        mViewPager.addOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
    }

    private View.OnClickListener mOnPhotoClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            finish();
        }
    };

    public class MyOnPageChangeListener implements OnPageChangeListener {
        /**
         * arg0 :当前页面，及你点击滑动的页面 arg1:当前页面偏移的百分比 arg2:当前页面偏移的像素位置
         */
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageSelected(int arg0) {
            mIndexNow = arg0;
            mPages.setText(mIndexNow + 1 + "/" + mUrls.size());
        }
    }

    private HashMap<Integer, TopicGalleryFragment> fragmentMap = new HashMap<>();

    private boolean alreadyAnimateIn = false;

    class PhotoPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<String> paths = new ArrayList<>();
        private Context mContext;

        public PhotoPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            TopicGalleryFragment fragment = fragmentMap.get(position);
            if (fragment == null) {

                boolean animateIn = (mIndexStart == position) && !alreadyAnimateIn;
//                fragment = new TopicGalleryFragment();
                fragment = TopicGalleryFragment
                        .newInstance(mUrls.get(position), mRects.get(position), animateIn,
                                mIndexStart == position);
                alreadyAnimateIn = true;
                fragmentMap.put(position, fragment);
            }

            return fragment;
        }

        @Override
        public int getCount() {
            return mUrls.size();
        }

        //when activity is recycled, ViewPager will reuse fragment by theirs name, so
        //getItem wont be called, but we need fragmentMap to animate close operation
        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (object instanceof TopicGalleryFragment) {
                fragmentMap.put(position, (TopicGalleryFragment) object);
            }
        }

    }

    public void showBackgroundImmediately() {
        if (background.getBackground() == null) {
            backgroundColor = new ColorDrawable(Color.BLACK);
            background.setBackground(backgroundColor);
        }
    }

    public ObjectAnimator showBackgroundAnimate() {
        backgroundColor = new ColorDrawable(Color.BLACK);
        background.setBackground(backgroundColor);
        ObjectAnimator bgAnim = ObjectAnimator
                .ofInt(backgroundColor, "alpha", 0, 255);
        bgAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                background.setBackground(backgroundColor);
            }
        });
        return bgAnim;
    }

    @Override
    public void onBackPressed() {

        TopicGalleryFragment fragment = fragmentMap.get(mViewPager.getCurrentItem());
        if (fragment != null && fragment.canAnimateCloseActivity()) {
            backgroundColor = new ColorDrawable(Color.BLACK);
            ObjectAnimator bgAnim = ObjectAnimator.ofInt(backgroundColor, "alpha", 0);
            bgAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    background.setBackground(backgroundColor);
                }
            });
            bgAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    TopicGalleryActivity.super.finish();
                    overridePendingTransition(-1, -1);
                }
            });
            fragment.animationExit(bgAnim);
        } else {
            super.onBackPressed();
        }
    }

}
