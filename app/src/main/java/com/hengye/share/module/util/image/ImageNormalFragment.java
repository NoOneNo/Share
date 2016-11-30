package com.hengye.share.module.util.image;


import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.cache.BitmapCache;
import com.android.volley.toolbox.BitmapUtil;
import com.hengye.share.R;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.util.ImageUtil;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import uk.co.senab.photoview.ScaleType;

public class ImageNormalFragment extends ImageBaseFragment {

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
    boolean mCanAnimatedIn;
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
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        Bitmap bitmap = BitmapCache.getInstance().getBitmap(mPath);
        if (bitmap == null) {
            bitmap = BitmapUtil.getSuitableBitmap(mPath, screenWidth, 0, BitmapUtil.DEFAULT_CONFIG, ImageView.ScaleType.FIT_XY);
            if (bitmap != null) {
                BitmapCache.getInstance().putBitmap(mPath, bitmap);
            }
        }

        mCanAnimatedIn = true;
        if(bitmap != null){
            //如果宽度比高度长，并且宽度至少有屏幕宽度的1/3，则扩充宽度
            if(bitmap.getHeight() > bitmap.getWidth() && bitmap.getWidth() > screenWidth / 3){
                float scale = (float)screenWidth / bitmap.getWidth();
                int actualHeight = (int)(bitmap.getHeight() * scale);
                if(actualHeight > screenHeight * 1.2){
                    mPhotoView.setCustomScaleType(ScaleType.FIT_TOP);
                }

                if(bitmap.getHeight() > screenHeight){
                    //如果原图高度已经高过屏幕高度，不执行动画
                    mCanAnimatedIn = false;
                }
            }
        }
        setImageBitmap(bitmap);

        mPhotoView.setOnLongClickListener(this);

//        mPhotoView.setOnMatrixChangeListener(new PhotoViewAttacher.OnMatrixChangedListener() {
//            @Override
//            public void onMatrixChanged(RectF rect) {
//                L.debug("rect width : {} , height : {}", rect.right - rect.left, rect.bottom - rect.top);
//            }
//        });
//        mPhotoView.getDisplayRect()

        if(SettingHelper.isClickToCloseGallery()) {
            mPhotoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    getActivity().onBackPressed();
                }
            });
        }

        if (mAnimateIn && mCanAnimatedIn) {
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

    private void setImageBitmap(Bitmap bitmap){
        int MAX_HEIGHT_WIDTH = ImageUtil.getBitmapMaxSize();
        if (bitmap.getWidth() < bitmap.getHeight() && bitmap.getHeight() > MAX_HEIGHT_WIDTH) {
            mPhotoView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * MAX_HEIGHT_WIDTH / bitmap.getHeight(), MAX_HEIGHT_WIDTH, true));
        } else if (bitmap.getWidth() > bitmap.getHeight() && bitmap.getWidth() > MAX_HEIGHT_WIDTH) {
            mPhotoView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, MAX_HEIGHT_WIDTH, bitmap.getHeight() * MAX_HEIGHT_WIDTH / bitmap.getWidth(), true));
        } else {
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    public boolean canAnimatedOut(){
        return mCanAnimatedIn;
    }
}
