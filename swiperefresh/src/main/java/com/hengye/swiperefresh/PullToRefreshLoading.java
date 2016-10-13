package com.hengye.swiperefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class PullToRefreshLoading extends FrameLayout {

	private View mFooterView;
	private ImageView mArrow;
	private ProgressBar mProgressBar;
	private View mLoading;
	private boolean mIsAnimatedRotate;
	private View mProgress;
	private TextView mRefreshDescription, mRefreshTime;
	
	public PullToRefreshLoading(Context context){
		this(context, null);
	}
	
	public PullToRefreshLoading(Context context, TypedArray attrs){
		super(context);
		
		LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_loading, this);
		
		mFooterView = findViewById(R.id.pull_to_refresh_foot);
		mProgressBar = (ProgressBar) findViewById(R.id.pull_to_refresh_foot_progress_bar);
		mLoading = findViewById(R.id.pull_to_refresh_foot_loading);
		mProgress = mProgressBar;
		
		if(attrs == null){
			return;
		}
		// dispose loading
		mIsAnimatedRotate = attrs.getBoolean(R.styleable.PullToRefreshLayout_isAnimatedRotate, false);
		if (mIsAnimatedRotate) {
			mProgress = mProgressBar;
		}else{
			mProgress = mLoading;
		}
		
		if (attrs.hasValue(R.styleable.PullToRefreshLayout_loadingHeight)) {
			int size = attrs.getDimensionPixelSize(R.styleable.PullToRefreshLayout_loadingHeight, 0);
			ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mProgress.getLayoutParams();
			lp.height = size;
			requestLayout();
		}
		if (attrs.hasValue(R.styleable.PullToRefreshLayout_layoutHeight)) {
			int size = attrs.getDimensionPixelSize(R.styleable.PullToRefreshLayout_layoutHeight, 0);
			ViewGroup.LayoutParams lp = (ViewGroup.LayoutParams) mFooterView.getLayoutParams();
			lp.height = size;
			requestLayout();
		}
		
//		attrs.recycle();
	}
	
	public View getLoadingView(){
		return mProgress;
	}
	
	public boolean isAnimatedRotate(){
		return mIsAnimatedRotate;
	}
	
    /**
     * Pre API 11, alpha is used to make the arrow appear instead of rotation.
     */
    private boolean isAvailableForRotation() {
        return android.os.Build.VERSION.SDK_INT > 11;
    }	
	
//    public void startRotationAnimation(final float startingAngle, final float endingAngle) {
//        // Pre API 11, rotation is unavailable;
//        if (!isAvailableForRotation()) {
//            return;
//        }
//        Animation rotation = new Animation() {
//            @Override
//            public void applyTransformation(float interpolatedTime, Transformation t) {
//            	mLoading.setRotation(startingAngle + (endingAngle - startingAngle)
//            			* interpolatedTime);
//            	mLoading.invalidate();
//            }
//        };
//        rotation.setDuration(1000);
//        rotation.setRepeatCount(Animation.INFINITE);
//        // Clear out the previous animation listeners.
//        mLoading.clearAnimation();
//        mLoading.startAnimation(rotation);
//    }
}
