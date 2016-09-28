package com.hengye.photopicker.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hengye.photopicker.R;
import com.hengye.photopicker.activity.PhotoPickerActivity;
import com.hengye.photopicker.activity.ThemeActivity;
import com.hengye.photopicker.model.Photo;
import com.hengye.photopicker.util.BitmapUtil;
import com.hengye.photopicker.view.TouchImageView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends ThemeFragment implements View.OnClickListener{

    public final static String IMG_TOTAL_PHOTO = "totalPhotos";
//    public final static String IMG_IS_PRIMITIVE = "isPrimitive";
//    public final static String IMG_SELECT_PHOTO = "selectPhotos";
    public final static String IMG_INDEX = "imgIndex";
    public final static String IMG_LOCATION = "imgLocation";
    public final static String IMG_WIDTH = "imgWidth";
    public final static String IMG_TAKE_PHOTO = "imgTakePhoto";
    private ViewPager mViewPager;
    private TextView mPages, mPrimitiveText;
    private View mCancel, mSelectBtn, mUnSelectBtn, mSelectScope, mPrimitiveBtn, mPrimitiveLayout, mTitleBar;
    private Button mNextBtn;

    //默认从0开始
    private int mIndexNow, mIndexStart;
    private boolean isPrimitive;
//    private BitmapUtil mBitmapUtil;
    private ArrayList<Photo> mTotalPhotos;
    private int mMaxWidth, mMaxHeight;
    private int mTouchSlop;
    private float mDownX, mDownY;
    private int[] mImgLocationOnScreen;
    private int mImgWidth;
    private boolean mIsTakePhoto;

    public static GalleryFragment newInstance(ArrayList<Photo> photos, int index, int[] locationOnScreen, int width, boolean isTakePhoto, boolean isPrimitive){
        GalleryFragment fragment = new GalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(GalleryFragment.IMG_TOTAL_PHOTO, photos);
        bundle.putBoolean(PhotoPickerActivity.PICK_PHOTO_PRIMITIVE, isPrimitive);
        bundle.putBoolean(GalleryFragment.IMG_TAKE_PHOTO, isTakePhoto);
        bundle.putInt(GalleryFragment.IMG_INDEX, index);
        bundle.putInt(GalleryFragment.IMG_WIDTH, width);
        bundle.putIntArray(GalleryFragment.IMG_LOCATION, locationOnScreen);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            mTotalPhotos = (ArrayList<Photo>) getArguments().getSerializable(IMG_TOTAL_PHOTO);
            isPrimitive = getArguments().getBoolean(PhotoPickerActivity.PICK_PHOTO_PRIMITIVE);
            mIndexStart = mIndexNow = getArguments().getInt(IMG_INDEX, 0);
            mImgWidth = getArguments().getInt(IMG_WIDTH, 0);
            mImgLocationOnScreen = getArguments().getIntArray(IMG_LOCATION);
            mIsTakePhoto = getArguments().getBoolean(IMG_TAKE_PHOTO);
//            mTotalPhotos = (ArrayList<Photo>) getActivity().getIntent().getSerializableExtra(IMG_TOTAL_PHOTO);
//            mIndexStart = mIndexNow =  getActivity().getIntent().getIntExtra(IMG_INDEX, 0);
//            mImgWidth =  getActivity().getIntent().getIntExtra(IMG_WIDTH, 0);
//            mImgLocationOnScreen = getActivity().getIntent().getIntArrayExtra(IMG_LOCATION);
            Log.i("gallery", "image width : " + mImgWidth);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果传进来的图片路径为空
        if (mTotalPhotos == null || mTotalPhotos.isEmpty()) {
            this.getActivity().getSupportFragmentManager().popBackStack();
        }
//        if(mSelectPhotos == null){
            //初始化以选择的图片;
//            mSelectPhotos = new ArrayList<Photo>();
//            if(mTotalPhotos != null && !mTotalPhotos.isEmpty()){
//                for(Photo photo : mTotalPhotos){
//                    if(photo.isSelected()){
//                        mSelectPhotos.add(photo);
//                    }
//                }
//            }
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_gallery, container, false);

        mViewPager = (ViewPager) rootView.findViewById(R.id.select_photo_gallery_view_pager);
        mPages = (TextView) rootView.findViewById(R.id.select_photo_gallery_pages);
        mTitleBar = rootView.findViewById(R.id.titlebar_gallery);
        mCancel = rootView.findViewById(R.id.title_left_bt);
        mUnSelectBtn = rootView.findViewById(R.id.adapter_gridview_photo_item_btn_unselect);
        mSelectBtn = rootView.findViewById(R.id.adapter_gridview_photo_item_btn_select);
        mSelectScope = rootView.findViewById(R.id.adapter_gridview_photo_item_btn_scope);
        mPrimitiveLayout = rootView.findViewById(R.id.activity_select_photo_layout_primitive_layout);
        mPrimitiveBtn = rootView.findViewById(R.id.activity_select_photo_layout_primitive);
        mPrimitiveText = (TextView) rootView.findViewById(R.id.activity_select_photo_layout_primitive_tv);
        mNextBtn = (Button) rootView.findViewById(R.id.activity_select_photo_layout_next_btn);
//        mVideoPlayBtn = rootView.findViewById(R.id.btn_video_play);
//        mVideoSize = (TextView) rootView.findViewById(R.id.tv_video_size);
        initData();
        initViewPager();
        initClick();
        initSelectPhotoAnim();
        initCustomThemeStyle();

        if(savedInstanceState == null){
            initAnimation();
        }
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().getIntent().putExtra(PhotoPickerActivity.PICK_PHOTO_PRIMITIVE, mPrimitiveLayout.isSelected());
    }

    private void initData() {
        mNextBtn.setEnabled(false);
        mPrimitiveLayout.setSelected(isPrimitive);
        updatePrimitive(isPrimitive);
        if(mIsTakePhoto) {
            mSelectScope.setVisibility(View.GONE);
            canClickNext(true);
        }else{
            canClickNext(!getSelectPhotos().isEmpty());
        }
    }

    public void initViewPager() {

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mMaxWidth = dm.widthPixels / 2;
        mMaxHeight = dm.heightPixels / 2;
        mTouchSlop = ViewConfiguration.get(getActivity()).getScaledTouchSlop();
//        mBitmapUtil = BitmapUtil.getInstance();

        mViewPager.setAdapter(new PhotoPagerAdapter(getActivity(), mTotalPhotos));
        mViewPager.setCurrentItem(mIndexStart);// 设置当前显示标签页为显示页
        updateCurrentItem(mIndexStart);
//        Photo photo = mTotalPhotos.get(mIndexStart);
//        setSelectBtn(photo.isSelected());
//        setVideoPlayBtn(photo.isVideo());
//        if(mTotalPhotos != null) {
//            mPages.setText(mIndexStart + 1 + "/" + mTotalPhotos.size());
//        }
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);// 页面变化时的监听器
    }

    private void updateCurrentItem(int position){
        mIndexNow = position;
        Photo photo = mTotalPhotos.get(mIndexNow);
        setSelectBtn(photo.isSelected());
//        setVideoPlayBtn(photo.isVideo());
        if(mTotalPhotos != null) {
            mPages.setText(mIndexNow + 1 + "/" + mTotalPhotos.size());
        }
    }

    private void initClick(){
        mCancel.setOnClickListener(this);
        mNextBtn.setOnClickListener(this);
        mSelectScope.setOnClickListener(this);
        mPrimitiveLayout.setOnClickListener(this);
//        mVideoPlayBtn.setOnClickListener(this);
    }

    private void initCustomThemeStyle(){
        mTitleBar.setBackgroundColor(getResources().getColor(getCustomTheme().getTitleBackground()));
        mCancel.setBackgroundResource(getCustomTheme().getTitleBackIcon());
        mPages.setTextColor(getResources().getColor(getCustomTheme().getTitleTextFontColor()));
        mSelectBtn.setBackgroundResource(getCustomTheme().getPhotoSelected());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.title_left_bt){
            //后退按钮
            onClickCancel();
        }else if(id == R.id.activity_select_photo_layout_next_btn){
            //下一步按钮
            finishAtyWithData();
        }else if(id == R.id.activity_select_photo_layout_primitive_layout){
            //原图按钮
            onClickPrimitiveBtn(v);
        }else if(id == R.id.btn_video_play){
            //视频播放按钮
            Photo photo = mTotalPhotos.get(mIndexNow);
            File file = new File(photo.getDataPath());
            if(!photo.isVideo() || !file.exists()){
                return;
            }
            Log.i("photopicker", "video mimetype : " + photo.getMimeType());
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, photo.getMimeType());
            if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                getActivity().startActivity(intent);
            }else{
                Toast.makeText(getActivity(),"找不到可以播放此视频的应用",Toast.LENGTH_SHORT).show();
            }
        }else if(id == R.id.adapter_gridview_photo_item_btn_scope){
            Photo photo = mTotalPhotos.get(mIndexNow);
            if (mSelectBtn.getVisibility() == View.VISIBLE) {
                // 取消选择
                photo.setSelected(false);
                getSelectPhotos().remove(photo);
//                mSelectPhotos.remove(photo);
            } else {
                // 选择图片
                if(photo.isVideo()){
                    if (isOutSideMaxSelectVideoCount()) {
                        return;
                    }
                }else {
                    if (isOutSideMaxSelectImageCount()) {
                        return;
                    }
                }
                photo.setSelected(true);
                getSelectPhotos().add(photo);
//                mSelectPhotos.add(photo);
                mSelectBtn.startAnimation(mSelectPhotoAnim);
            }
            setSelectBtn(photo.isSelected());

            canClickNext(getSelectPhotos().size() != 0);
        }
    }

    private void onClickCancel() {
        getActivity().onBackPressed();
    }

    private void finishAtyWithData() {
        if(mIsTakePhoto) {
            getSelectPhotos().addAll(mTotalPhotos);
            ((ThemeActivity) getActivity()).finishAtyWithData(getSelectPhotos(), mPrimitiveLayout.isSelected());
        }else{
            ((ThemeActivity) getActivity()).finishAtyWithData(getSelectPhotos(), mPrimitiveLayout.isSelected());
        }
    }

    private void onClickPrimitiveBtn(View v) {
        if (v.isSelected()){
            v.setSelected(false);
            updatePrimitive(false);
        }else{
            v.setSelected(true);
            updatePrimitive(true);
        }
    }

    //切换显示原图的按钮样式
    private void updatePrimitive(boolean flag){
        if(flag){
//            mPrimitiveBtn.setBackgroundResource(R.drawable.compose_photo_preview_right);
            mPrimitiveBtn.setBackgroundResource(getCustomTheme().getAlbumPointSelected());
            mPrimitiveText.setText( String.format(getString(R.string.select_photo_primitive), getSelectPhotoSize()));
        }else{
            mPrimitiveBtn.setBackgroundResource(R.drawable.compose_photo_preview_default);
            mPrimitiveText.setText(getString(R.string.select_photo_primitive_null));
        }
    }

    private String getSelectPhotoSize(){
        return Photo.formatTotalImageSize(getActivity().getApplicationContext(), mIsTakePhoto ? mTotalPhotos : getSelectPhotos());
    }

    private boolean isOutSideMaxSelectImageCount() {
        if (Photo.getExistImageCount(getSelectPhotos()) >= getCustomTheme().getMaxSelectImageSize()) {
            String str = String.format(
                    getResources().getString(R.string.select_photo_image_count_limit),
                    getCustomTheme().getMaxSelectImageSize());
            Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    private boolean isOutSideMaxSelectVideoCount() {
        if (Photo.getExistVideoCount(getSelectPhotos()) >= getCustomTheme().getMaxSelectVideoSize()) {
            String str = String.format(
                    getResources().getString(R.string.select_photo_video_count_limit),
                    getCustomTheme().getMaxSelectVideoSize());
            Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return false;
        }
    }

    //切换预览和下一步的按钮样式
    private void canClickNext(boolean flag){
        if(flag){
            if(mPrimitiveLayout.isSelected()){
                updatePrimitive(true);
            }
            if(mIsTakePhoto){
                mNextBtn.setText(getString(R.string.select_photo_next_null));
            }else {
                mNextBtn.setText(String.format(getString(R.string.select_photo_next), getSelectPhotos().size()));
            }
            //如果已经可以点击下一步的时候，以下样式无需重复再改直接返回;
            if(mNextBtn.isEnabled()){
                return;
            }
//            mPreviewBtn.setEnabled(true);
//            mPreviewBtn.setTextColor(getResources().getColor(R.color.select_photo_text_able_bg_white));
            mNextBtn.setEnabled(true);
//            mNextBtn.setBackgroundResource(R.drawable.btn_bg_orange);
            mNextBtn.setBackgroundResource(getCustomTheme().getNextBtn());
            mNextBtn.setTextColor(getResources().getColor(R.color.select_photo_text_able_bg_orange));
            mPrimitiveLayout.setEnabled(true);
            mPrimitiveText.setTextColor(getResources().getColor(R.color.select_photo_text_able_bg_white));

        }else{
//            mPreviewBtn.setEnabled(false);
//            mPreviewBtn.setTextColor(getResources().getColor(R.color.select_photo_text_unable_bg_white));
            mNextBtn.setEnabled(false);
            mNextBtn.setBackgroundResource(R.drawable.btn_bg_white);
            mNextBtn.setText(getString(R.string.select_photo_next_null));
            mNextBtn.setTextColor(getResources().getColor(R.color.select_photo_text_unable_bg_white));

            mPrimitiveLayout.setEnabled(false);
            mPrimitiveText.setTextColor(getResources().getColor(R.color.select_photo_text_unable_bg_white));
            updatePrimitive(false);
        }
    }

//    private void setVideoPlayBtn(boolean isVideo){
//        mVideoPlayBtn.setVisibility(isVideo ? View.VISIBLE : View.GONE);
//    }

    private void setSelectBtn(boolean isSelected) {
        if (isSelected) {
            mUnSelectBtn.setVisibility(View.GONE);
            mSelectBtn.setVisibility(View.VISIBLE);
        } else {
            mSelectBtn.setVisibility(View.GONE);
            mUnSelectBtn.setVisibility(View.VISIBLE);
        }
    }

    private Animation mSelectPhotoAnim;
    private void initSelectPhotoAnim() {
        mSelectPhotoAnim = new ScaleAnimation(1.0f, 1.15f, 1.0f, 1.15f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mSelectPhotoAnim.setDuration(250);
        mSelectPhotoAnim.setInterpolator(new DecelerateInterpolator());
        mSelectPhotoAnim.setFillAfter(false);
    }

    private void initAnimation(){
        if (mImgLocationOnScreen != null && mImgWidth != 0) {
            ViewTreeObserver observer = mViewPager.getViewTreeObserver();
            observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {

                    mViewPager.getViewTreeObserver().removeOnPreDrawListener(this);

                    int[] locationOnScreen = new int[2];
                    mViewPager.getLocationOnScreen(locationOnScreen);

                    mDifferX = mImgLocationOnScreen[0] - locationOnScreen[0];
                    mDifferY = mImgLocationOnScreen[1] - locationOnScreen[1];
                    runEnterAnimation();

                    return true;
                }
            });
        }else{
            ViewHelper.setScaleX(mViewPager, 1.0f);
            ViewHelper.setScaleY(mViewPager, 1.0f);
        }
    }

    private int mDifferX, mDifferY;
    private final long DEFAULT_ANIMATION_DURATION = 300L;

    private void runEnterAnimation() {

        // Set starting values for properties we're going to animate. These
        // values scale and position the full size version down to the thumbnail
        // size/location, from which we'll animate it back up
        ViewHelper.setPivotX(mViewPager, 0);
        ViewHelper.setPivotY(mViewPager, 0);
        ViewHelper.setScaleX(mViewPager, (float) mImgWidth / mViewPager.getWidth());
        ViewHelper.setScaleY(mViewPager, (float) mImgWidth / mViewPager.getHeight());
        ViewHelper.setTranslationX(mViewPager, mDifferX);
        ViewHelper.setTranslationY(mViewPager, mDifferY);

        // Animate scale and translation to go from thumbnail to full size
        ViewPropertyAnimator.animate(mViewPager)
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .scaleX(1)
                .scaleY(1)
                .translationX(0)
                .translationY(0)
                .setInterpolator(new DecelerateInterpolator());

        // Fade in the black background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mViewPager.getBackground(), "alpha", 0, 255);
        bgAnim.setDuration(DEFAULT_ANIMATION_DURATION);
        bgAnim.start();

        // Animate a color filter to take the image from grayscale to full color.
        // This happens in parallel with the image scaling and moving into place.
        ObjectAnimator colorizer = ObjectAnimator.ofFloat(GalleryFragment.this,
                "saturation", 0, 1);
        colorizer.setDuration(DEFAULT_ANIMATION_DURATION);
        colorizer.start();

    }

    public void runExitAnimation(final Runnable endAction) {

        if (mImgLocationOnScreen != null && mImgWidth != 0 && mIndexStart != mIndexNow) {
            endAction.run();
            return;
        }

        // Animate image back to thumbnail size/location
        ViewPropertyAnimator.animate(mViewPager)
                .setDuration(DEFAULT_ANIMATION_DURATION)
                .setInterpolator(new AccelerateInterpolator())
                .scaleX((float) mImgWidth / mViewPager.getWidth())
                .scaleY((float) mImgWidth / mViewPager.getHeight())
                .translationX(mDifferX)
                .translationY(mDifferY)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        endAction.run();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });

        // Fade out background
        ObjectAnimator bgAnim = ObjectAnimator.ofInt(mViewPager.getBackground(), "alpha", 0);
        bgAnim.setDuration(DEFAULT_ANIMATION_DURATION);
//        bgAnim.start();

        // Animate a color filter to take the image back to grayscale,
        // in parallel with the image scaling and moving into place.
        ObjectAnimator colorizer = ObjectAnimator.ofFloat(GalleryFragment.this, "saturation", 1, 0);
        colorizer.setDuration(DEFAULT_ANIMATION_DURATION);
//        colorizer.start();
    }

    private boolean mIsFirstBack = true;

    private View.OnClickListener mOnPhotoClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(mIsFirstBack){
                mIsFirstBack = false;
                getActivity().onBackPressed();
            }
        }
    };

    private OnTouchListener mOnPhotoTouchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                mDownX = event.getX();  // 取得按下时的坐标
                mDownY = event.getY();
                return true;
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                if (Math.abs(event.getX() - mDownX) > mTouchSlop ||
                        Math.abs(event.getY() - mDownY) > mTouchSlop) {
                    return false;
                } else {
                    v.performClick();
                    return true;
                }
            }
            return true;
        }
    };

    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            updateCurrentItem(position);
//            mIndexNow = position;
//            Photo photo = mTotalPhotos.get(position);
//            mPages.setText(mIndexNow + 1 + "/" + mTotalPhotos.size());
//            setSelectBtn(photo.isSelected());
//            setVideoPlayBtn(photo.isVideo());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    class PhotoPagerAdapter extends PagerAdapter {

        private ArrayList<Photo> photos = new ArrayList<Photo>();
        private Context mContext;
        private LayoutInflater mLayoutInflater;


        public PhotoPagerAdapter(Context mContext, ArrayList<Photo> photos) {
            this.mContext = mContext;
            this.photos = photos;
            mLayoutInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            if(photos == null){
                return 0;
            }
            return photos.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = mLayoutInflater.inflate(R.layout.activity_gallery_viewpager_item, null);
            final TouchImageView touchIv = (TouchImageView) view.findViewById(R.id.select_photo_gallery_fragment_iv);
            TextView videoSize = (TextView) view.findViewById(R.id.tv_video_size);
            View videoPlayBtn = view.findViewById(R.id.btn_video_play);
            final Photo photo = photos.get(position);

            if(photo.isVideo()){
                videoPlayBtn.setVisibility(View.VISIBLE);
                videoSize.setVisibility(View.VISIBLE);
                videoSize.setText(String.format(getResources().getString(R.string.select_photo_video_size), photo.formatSize(getActivity())));
                videoPlayBtn.setOnClickListener(GalleryFragment.this);
            }else{
                videoPlayBtn.setVisibility(View.GONE);
                videoSize.setVisibility(View.GONE);
            }

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Bitmap bitmap = loadImage(photo);
                    if (bitmap == null) {
//				touchIv.setImageDrawable(mDefaultDrawable);
                    } else {
                        touchIv.setImageBitmap(bitmap);
                    }
                }
            });

//            view.setOnClickListener(mOnPhotoClickListener);
            touchIv.setOnClickListener(mOnPhotoClickListener);
//            touchIv.setOnTouchListener(mOnPhotoTouchListener);
            container.addView(view);
            return view;
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

    public static Handler mHandler = new Handler();

    private Bitmap loadImage(Photo photo) {
        String path = photo.isVideo() ? photo.getThumbnail() : photo.getDataPath();
        if (path != null) {
            Bitmap bitmap = BitmapUtil.getInstance().getBitmapFromMemoryCache(getCacheKey(path));
            if (bitmap == null) {
                bitmap = BitmapUtil.decodeSampledBitmapFromPath(path, mMaxWidth, mMaxHeight, photo.getOrientation());
                if(bitmap != null){
                    BitmapUtil.getInstance().addBitmapToMemoryCache(getCacheKey(path), bitmap);
                }
            }
            return bitmap;
        }
        return null;
    }

    private String getCacheKey(String path){
        return path + "/gallery";
    }

}
