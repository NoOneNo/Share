package com.hengye.swiperefresh;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class PullToRefreshHeader extends FrameLayout implements PullToRefreshCallBack{

	public static final long ONE_MINUTE = 60 * 1000;
	public static final long ONE_HOUR = 60 * ONE_MINUTE;
	public static final long ONE_DAY = 24 * ONE_HOUR;
	public static final long ONE_MONTH = 30 * ONE_DAY;
	public static final long ONE_YEAR = 12 * ONE_MONTH;

	private static final float STARTING_ARROW_ANGLE = 0;
	private static final float MAX_ARROW_ANGLE = 180;
	private static final float STARTING_PROGRESS_ANGLE = 0;
	private static final float MAX_PROGRESS_ANGLE = 360;

	private static final int ROTATION_ANIMATION_DURATION = 150;

	private View mHeaderView;
	private ImageView mArrow;
	private ProgressBar mProgressBar;
	private View mLoading;
	private boolean mIsAnimatedRotate;
	private View mProgress;
	private TextView mRefreshDescription, mRefreshTime;

	private String mPullToRefreshTip, mReleaseToRefreshTip, mOnRefreshingTip;
	private String mMinutes, mHours, mDays, mMonths, mYears;

	private Animation mRotationStartAnimation;
	private Animation mRotationMaxAnimation;

	private long mLastUpdateTime = -1;

	public PullToRefreshHeader(Context context){
		this(context, null);
	}
	
	public PullToRefreshHeader(Context context, TypedArray attrs){
		super(context);
		
		LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header, this);
		mHeaderView = findViewById(R.id.pull_to_refresh_head);
		mArrow = (ImageView) findViewById(R.id.pull_to_refresh_arrow);
		mRefreshDescription = (TextView) findViewById(R.id.pull_to_refresh_tip);
		mRefreshTime = (TextView) findViewById(R.id.pull_to_refresh_last_updated_time);
		mProgressBar = (ProgressBar) findViewById(R.id.pull_to_refresh_progress_bar);
		mLoading = findViewById(R.id.pull_to_refresh_loading);

		// dispose loading
		mIsAnimatedRotate = attrs.getBoolean(R.styleable.PullToRefreshLayout_isAnimatedRotate, true);
		if (mIsAnimatedRotate) {
			mProgress = mProgressBar;
		}else{
			mProgress = mLoading;
		}

		initContentDescription();
		hideProgress();


		// dispose arrow
		if (attrs.hasValue(R.styleable.PullToRefreshLayout_loadingArrowSrc)) {
			Drawable drawable = attrs.getDrawable(R.styleable.PullToRefreshLayout_loadingArrowSrc);
			mArrow.setImageDrawable(drawable);
		}
		


		if (attrs.hasValue(R.styleable.PullToRefreshLayout_loadingProgressSrc)) {
			Drawable drawable = attrs.getDrawable(R.styleable.PullToRefreshLayout_loadingProgressSrc);
			if(mIsAnimatedRotate){
				mProgressBar.setIndeterminateDrawable(drawable);
			}else{
				mLoading.setBackground(drawable);
			}

		}	
		if (attrs.hasValue(R.styleable.PullToRefreshLayout_layoutBackground)) {
			Drawable background = attrs.getDrawable(R.styleable.PullToRefreshLayout_layoutBackground);
			this.setBackground(background);
		}
		if (attrs.hasValue(R.styleable.PullToRefreshLayout_refreshDescriptionTextColor)) {
			ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefreshLayout_refreshDescriptionTextColor);
			if(colors != null){
				mRefreshDescription.setTextColor(colors);
			}
		}
		if (attrs.hasValue(R.styleable.PullToRefreshLayout_refreshTimeTextColor)) {
			ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefreshLayout_refreshTimeTextColor);
			if(colors != null){
				mRefreshTime.setTextColor(colors);
			}
		}
		if (attrs.hasValue(R.styleable.PullToRefreshLayout_refreshDescriptionTextAppearance)) {
			TypedValue styleID = new TypedValue();
			attrs.getValue(R.styleable.PullToRefreshLayout_refreshDescriptionTextAppearance, styleID);
			mRefreshDescription.setTextAppearance(getContext(), styleID.data);
		}
		if (attrs.hasValue(R.styleable.PullToRefreshLayout_refreshTimeTextAppearance)) {
			TypedValue styleID = new TypedValue();
			attrs.getValue(R.styleable.PullToRefreshLayout_refreshTimeTextAppearance, styleID);
			mRefreshTime.setTextAppearance(getContext(), styleID.data);
		}
		if (attrs.hasValue(R.styleable.PullToRefreshLayout_arrowWidth)) {
			int size = attrs.getDimensionPixelSize(R.styleable.PullToRefreshLayout_arrowWidth, 0);
			ViewGroup.LayoutParams lp = mArrow.getLayoutParams();
			lp.width = size;
			requestLayout();
		}
		if (attrs.hasValue(R.styleable.PullToRefreshLayout_arrowHeight)) {
			int size = attrs.getDimensionPixelSize(R.styleable.PullToRefreshLayout_arrowHeight, 0);
			ViewGroup.LayoutParams lp = mArrow.getLayoutParams();
			lp.height = size;
			requestLayout();
		}
		if (attrs.hasValue(R.styleable.PullToRefreshLayout_loadingWidth)) {
			int size = attrs.getDimensionPixelSize(R.styleable.PullToRefreshLayout_loadingWidth, 0);
			ViewGroup.LayoutParams lp = mProgress.getLayoutParams();
			lp.width = size;
			requestLayout();
		}
		if (attrs.hasValue(R.styleable.PullToRefreshLayout_loadingHeight)) {
			int size = attrs.getDimensionPixelSize(R.styleable.PullToRefreshLayout_loadingHeight, 0);
			ViewGroup.LayoutParams lp = mProgress.getLayoutParams();
			lp.height = size;
			requestLayout();
		}
		if (attrs.hasValue(R.styleable.PullToRefreshLayout_layoutHeight)) {
			int size = attrs.getDimensionPixelSize(R.styleable.PullToRefreshLayout_layoutHeight, 0);
			ViewGroup.LayoutParams lp = mHeaderView.getLayoutParams();
			lp.height = size;
			requestLayout();
		}
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
	
    public void startLoadingRotationAnimation(final float startingAngle, final float endingAngle) {
        // Pre API 11, rotation is unavailable;
        if (!isAvailableForRotation()) {
            return;
        }
        Animation rotation = new Animation() {
            @Override
            public void applyTransformation(float interpolatedTime, Transformation t) {
            	mLoading.setRotation(startingAngle + (endingAngle - startingAngle)
            			* interpolatedTime);
            	mLoading.invalidate();
            }
        };
        rotation.setDuration(1000);
        rotation.setRepeatCount(Animation.INFINITE);
        // Clear out the previous animation listeners.
        mLoading.clearAnimation();
        mLoading.startAnimation(rotation);
    }

	private void startArrowRotationStartAnimation() {
		mRotationStartAnimation = startArrowRotationAnimation(mArrow.getRotation(), STARTING_ARROW_ANGLE);
	}

	private void startArrowRotationMaxAnimation() {
		mRotationMaxAnimation = startArrowRotationAnimation(mArrow.getRotation(), MAX_ARROW_ANGLE);
	}

	public Animation startArrowRotationAnimation(final float startingAngle, final float endingAngle) {
		// Pre API 11, rotation is unavailable;
		if (!isAvailableForRotation()) {
			return null;
		}
		Animation rotation = new Animation() {
			@Override
			public void applyTransformation(float interpolatedTime, Transformation t) {
				mArrow.setRotation(startingAngle + (endingAngle - startingAngle)
						* interpolatedTime);
				mArrow.invalidate();
			}
		};
		rotation.setDuration(ROTATION_ANIMATION_DURATION);
		// Clear out the previous animation listeners.
		mArrow.clearAnimation();
		mArrow.startAnimation(rotation);
		return rotation;
	}

	private void initContentDescription() {
		mPullToRefreshTip = getResources().getString(R.string.pull_to_refresh);
		mReleaseToRefreshTip = getResources().getString(R.string.release_to_refresh);
		mOnRefreshingTip = getResources().getString(R.string.on_refreshing);

		mMinutes = getResources().getString(R.string.refresh_time_minute);
		mHours = getResources().getString(R.string.refresh_time_hour);
		mDays = getResources().getString(R.string.refresh_time_day);
		mMonths = getResources().getString(R.string.refresh_time_month);
		mYears = getResources().getString(R.string.refresh_time_year);
	}

	private void updateRefreshTime() {
		long currentTime = System.currentTimeMillis();
		long timePassed = currentTime - mLastUpdateTime;
		String str;
		if (mLastUpdateTime == -1) {
			str = getResources().getString(R.string.not_updated_yet);
		} else if (timePassed < 0) {
			str = getResources().getString(R.string.time_error);
		} else if (timePassed < ONE_MINUTE) {
			str = getResources().getString(R.string.updated_just_now);
		} else if (timePassed < ONE_HOUR) {
			String value = timePassed / ONE_MINUTE + mMinutes;
			str = String.format(getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_DAY) {
			String value = timePassed / ONE_HOUR + mHours;
			str = String.format(getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_MONTH) {
			String value = timePassed / ONE_DAY + mDays;
			str = String.format(getResources().getString(R.string.updated_at), value);
		} else if (timePassed < ONE_YEAR) {
			String value = timePassed / ONE_MONTH + mMonths;
			str = String.format(getResources().getString(R.string.updated_at), value);
		} else {
			String value = timePassed / ONE_YEAR + mYears;
			str = String.format(getResources().getString(R.string.updated_at), value);
		}
		mRefreshTime.setText(str);
	}

	private void showProgress() {
		mArrow.clearAnimation();
		mArrow.setVisibility(View.GONE);
		if (!isAnimatedRotate()) {
			startLoadingRotationAnimation(STARTING_PROGRESS_ANGLE, MAX_PROGRESS_ANGLE);
		}
		mProgress.setVisibility(View.VISIBLE);
		mRefreshDescription.setText(mOnRefreshingTip);
	}

	private void hideProgress() {
		mProgress.clearAnimation();
		mProgress.setVisibility(View.GONE);
		mArrow.setVisibility(View.VISIBLE);
		mRefreshDescription.setText(mPullToRefreshTip);
	}

	private boolean isAnimationRunning(Animation animation) {
		return animation != null && animation.hasStarted() && !animation.hasEnded();
	}

	@Override
	public void onPressDown() {
		hideProgress();
		updateRefreshTime();
		if (isAvailableForRotation()) {
			mArrow.setRotation(STARTING_ARROW_ANGLE);
		}
	}

	@Override
	public void onPullToRefresh(boolean canRefresh) {
		if(canRefresh){
			if (mArrow.getRotation() < MAX_ARROW_ANGLE
					&& !isAnimationRunning(mRotationMaxAnimation)) {
				mRefreshDescription.setText(mReleaseToRefreshTip);
				startArrowRotationMaxAnimation();
			}
		}else{
			if (mArrow.getRotation() > STARTING_ARROW_ANGLE
					&& !isAnimationRunning(mRotationStartAnimation)) {
				mRefreshDescription.setText(mPullToRefreshTip);
				startArrowRotationStartAnimation();
			}
		}
	}

	@Override
	public void onRefreshStart() {
		updateRefreshTime();
		showProgress();
	}

	@Override
	public void onRefreshComplete() {
		mLastUpdateTime = System.currentTimeMillis();
	}
}
