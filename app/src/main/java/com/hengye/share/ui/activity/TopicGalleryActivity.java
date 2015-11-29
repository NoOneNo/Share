package com.hengye.share.ui.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.android.volley.Cache;
import com.android.volley.error.VolleyError;
import com.android.volley.toolbox.BitmapUtil;
import com.android.volley.toolbox.ImageLoader;
import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.module.Topic;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.ui.view.TouchImageView;
import com.hengye.volleyplus.toolbox.RequestManager;

import java.util.ArrayList;

public class TopicGalleryActivity extends BaseActivity {

    public final static String IMG_URLS = "img_paths";
    public final static String IMG_INDEX = "img_index";
    public final static String IMG_RECTS = "img_rects";
    private ViewPager mPager;
    private TextView mPages;
    private int mIndexNow, mIndexInit;
    private ArrayList<String> mUrls;
    private ArrayList<AnimationRect> mRects;
    private boolean hasAnimatedIn = false;

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

        mPager = (ViewPager) findViewById(R.id.select_photo_gallery_view_pager);
        mPages = (TextView) findViewById(R.id.select_photo_gallery_pages);

        initViewPager();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void getBundleExtra(){
        mUrls = (ArrayList<String>) getIntent().getSerializableExtra(IMG_URLS);
        mIndexNow = mIndexInit = getIntent().getIntExtra(IMG_INDEX, 0);
        mRects = (ArrayList<AnimationRect>) getIntent().getSerializableExtra(IMG_RECTS);

        if(CommonUtil.isEmptyCollection(mUrls, mRects)){
            this.finish();
        }
    }

    public void initViewPager() {

        DisplayMetrics dm = getResources().getDisplayMetrics();

        // 给ViewPager设置适配器
        mPager.setAdapter(new PhotoPagerAdapter(this, mUrls));
        mPager.setCurrentItem(mIndexNow);// 设置当前显示标签页为显示页
        mPages.setText(mIndexNow + 1 + "/" + mUrls.size());
        mPager.addOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
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

    class PhotoPagerAdapter extends PagerAdapter {

        private ArrayList<String> paths = new ArrayList<>();
        private Context mContext;


        public PhotoPagerAdapter(Context mContext, ArrayList<String> paths) {
            this.mContext = mContext;
            this.paths = paths;
        }

        @Override
        public int getCount() {
            return paths.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view =  LayoutInflater.from(mContext).inflate(R.layout.item_viewpager_topic_gallery_, null);

            final String url = paths.get(position);
            final TouchImageView touchIv = (TouchImageView) view.findViewById(R.id.select_photo_gallery_fragment_iv);
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
                RequestManager.getImageLoader().get(url, new ImageLoader.ImageListener() {
                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate, boolean isFromCache) {
                        touchIv.setImageBitmap(response.getBitmap());
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        L.debug("request image fail, url : {}", url);
                    }
                }, screenWidth, 0, touchIv.getScaleType());
//            }







//            imageLarge.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    pb.setVisibility(View.VISIBLE);
//                    final String imageLargeUrl = Topic.getWBTopicImgUrl(url, Topic.IMAGE_TYPE_BMIDDLE, Topic.IMAGE_TYPE_LARGE);
//                    RequestManager.getImageLoader().get(imageLargeUrl, new ImageLoader.ImageListener() {
//                        @Override
//                        public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate, boolean isFromCache) {
//                            if(response.getBitmap() != null){
//                                touchIv.setImageBitmap(response.getBitmap());
//                                imageLarge.setVisibility(View.GONE);
//                                pb.setVisibility(View.GONE);
//                            }
//                        }
//
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            pb.setVisibility(View.GONE);
//                            L.debug("request image fail, url : {}", imageLargeUrl);
//                        }
//                    });
//                }
//            });

            touchIv.setOnClickListener(mOnPhotoClickListener);
            container.addView(view);
            return view;
        }

        public static final int ANIMATION_DURATION = 300;

        private void doAnimation(final ImageView imageview, final AnimationRect rect){
            imageview.getViewTreeObserver()
                    .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {

                            if (rect == null) {
                                imageview.getViewTreeObserver().removeOnPreDrawListener(this);
//                                endAction.run();
                                return true;
                            }

                            final Rect startBounds = new Rect(rect.scaledBitmapRect);
                            final Rect finalBounds = AnimationRect
                                    .getBitmapRectFromImageView(imageview);

                            if (finalBounds == null) {
                                imageview.getViewTreeObserver().removeOnPreDrawListener(this);
//                                endAction.run();
                                return true;
                            }

                            float startScale = (float) finalBounds.width() / startBounds.width();

                            if (startScale * startBounds.height() > finalBounds.height()) {
                                startScale = (float) finalBounds.height() / startBounds.height();
                            }

                            int deltaTop = startBounds.top - finalBounds.top;
                            int deltaLeft = startBounds.left - finalBounds.left;

                            imageview.setPivotY(
                                    (imageview.getHeight() - finalBounds.height()) / 2);
                            imageview.setPivotX((imageview.getWidth() - finalBounds.width()) / 2);

                            imageview.setScaleX(1 / startScale);
                            imageview.setScaleY(1 / startScale);

                            imageview.setTranslationX(deltaLeft);
                            imageview.setTranslationY(deltaTop);

                            imageview.animate().translationY(0).translationX(0)
                                    .scaleY(1)
                                    .scaleX(1).setDuration(ANIMATION_DURATION)
                                    .setInterpolator(
                                            new AccelerateDecelerateInterpolator());
//                                    .withEndAction(endAction);

                            AnimatorSet animationSet = new AnimatorSet();
                            animationSet.setDuration(ANIMATION_DURATION);
                            animationSet
                                    .setInterpolator(new AccelerateDecelerateInterpolator());

//                            animationSet.playTogether(ObjectAnimator.ofFloat(imageview,
//                                    "clipBottom",
//                                    AnimationRect.getClipBottom(rect, finalBounds), 0));
//                            animationSet.playTogether(ObjectAnimator.ofFloat(imageview,
//                                    "clipRight",
//                                    AnimationRect.getClipRight(rect, finalBounds), 0));
//                            animationSet.playTogether(ObjectAnimator.ofFloat(imageview,
//                                    "clipTop", AnimationRect.getClipTop(rect, finalBounds), 0));
//                            animationSet.playTogether(ObjectAnimator.ofFloat(imageview,
//                                    "clipLeft", AnimationRect.getClipLeft(rect, finalBounds), 0));

                            animationSet.start();

                            imageview.getViewTreeObserver().removeOnPreDrawListener(this);
                            return true;
                        }
                    });
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

}
