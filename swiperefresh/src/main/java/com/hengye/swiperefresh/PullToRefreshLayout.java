package com.hengye.swiperefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.LinearLayout;

import com.hengye.swiperefresh.listener.SwipeListener.OnLoadListener;
import com.hengye.swiperefresh.listener.SwipeListener.OnRefreshListener;


public class PullToRefreshLayout extends LinearLayout {

    private static final String LOG_TAG = PullToRefreshLayout.class.getSimpleName();
    private int mTouchSlop;
    private DecelerateInterpolator mDecelerateInterpolator;

    private MarginLayoutParams mPullToRefreshHeaderLayoutParams;
    //	private MarginLayoutParams mPullToRefreshFooterLayoutParams;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2f;
    private static final int INVALID_POINTER = -1;
    private static final float DRAG_RATE = .5f;
    private float mTotalDragTopDistance = -1;
    //    private float mTotalDragBottomDistance = -1;

    private static final int ANIMATE_TO_TRIGGER_DURATION = 200;
    private static final int ANIMATE_TO_START_DURATION = 200;

    private static final int[] LAYOUT_ATTRS = new int[]{
            android.R.attr.enabled
    };

    // Whether or not the starting offset has been determined.
    private boolean mOriginalOffsetCalculated = false;

    protected int mFrom;
    protected int mOriginalOffsetTop, mOriginalOffsetBottom;
    protected int mActionDownOffsetTop;
    private int mCurrentTargetOffsetTop;
    private PullToRefreshHeader mPullToRefreshHeaderView;
    private View mTarget; // the target of the gesture

    private boolean mIsAutoLoading = false;
    private boolean mIsSetAutoLoading = false;
    private AutoLoadingListener mAutoLoadingListener;
    private OnLoadMoreCallBack mOnLoadMoreCallBack;
    // Target is returning to its start offset because it was cancelled or a
    // refresh was triggered.
    private boolean mRefreshing = false;
    private boolean mNotify;
    private OnRefreshListener mRefreshListener;

    private boolean mRefreshEnable = true, mLoadEnable = false;

    //    private boolean mCanLoadMore = false;
//    private boolean mReturningToBottom;
    private boolean mLoading = false;
    private boolean mLoadFail = false;

    private boolean mResetting = false;
    private boolean mCanTouchEvent = false;
    private OnLoadListener mLoadListener;

    private float mInitialMotionY;
    private float mInitialDownY;
    private boolean mIsBeingDragged;
    private int mActivePointerId = INVALID_POINTER;
    private long mRefreshCompleteDelay = 1500;


    /**
     * Simple constructor to use when creating a SwipeRefreshLayout from code.
     *
     * @param context
     */
    public PullToRefreshLayout(Context context) {
        this(context, null);
    }

    /**
     * Constructor that is called when inflating SwipeRefreshLayout from XML.
     *
     * @param context
     * @param attrs
     */
    @SuppressLint("InflateParams")
    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        setWillNotDraw(false);
        mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);

        final TypedArray a = context.obtainStyledAttributes(attrs, LAYOUT_ATTRS);
        setEnabled(a.getBoolean(0, true));
        a.recycle();
        final TypedArray b = context.obtainStyledAttributes(attrs, R.styleable.PullToRefreshLayout);
        mPullToRefreshHeaderView = new PullToRefreshHeader(context, b);

        b.recycle();

        setOrientation(VERTICAL);

        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
        addView(mPullToRefreshHeaderView, 0);
        initHeaderViewLayoutParams();

//        mPullToRefreshLoadingView.setVisibility(View.GONE);
//        addView(mPullToRefreshLoadingView);
    }

    private void initHeaderViewLayoutParams() {
        int headLayoutHeight = getContext().getResources().getDimensionPixelSize(R.dimen.margin_1_dp) * 50;
        mPullToRefreshHeaderLayoutParams = (MarginLayoutParams) mPullToRefreshHeaderView.getLayoutParams();
        mCurrentTargetOffsetTop = mOriginalOffsetTop = mPullToRefreshHeaderLayoutParams.topMargin = -headLayoutHeight;
        mTotalDragTopDistance = headLayoutHeight;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        super.onLayout(changed, left, top, right, bottom);

        if (getChildCount() == 0) {
            return;
        }
        if (mTarget == null) {
            ensureTarget();
        }
        if (mTarget == null) {
            return;
        }

        if (!mOriginalOffsetCalculated) {
            mOriginalOffsetCalculated = true;
            mPullToRefreshHeaderLayoutParams = (MarginLayoutParams) mPullToRefreshHeaderView.getLayoutParams();
            mCurrentTargetOffsetTop = mOriginalOffsetTop = mPullToRefreshHeaderLayoutParams.topMargin = -mPullToRefreshHeaderView.getHeight();
            mTotalDragTopDistance = mPullToRefreshHeaderView.getHeight();
        }

    }

    public View getTarget() {
        return mTarget;
    }

    public void ensureTarget() {
        // Don't bother getting the parent height if the parent hasn't been laid
        // out yet.
        if (mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (!child.equals(mPullToRefreshHeaderView)) {

                    if (child.getTag() instanceof Boolean) {
                        boolean isHeader = (Boolean) child.getTag();
                        if (isHeader) {
                            continue;
                        }
                    }
                    mTarget = child;

                    if (mAutoLoadingListener != null && !mIsSetAutoLoading) {
                        mIsAutoLoading = mAutoLoadingListener.setAutoLoading(mTarget);
                        mIsSetAutoLoading = true;
                    }
                    break;
                }
            }
        } else if (mAutoLoadingListener != null && !mIsSetAutoLoading) {
            mIsAutoLoading = mAutoLoadingListener.setAutoLoading(mTarget);
            mIsSetAutoLoading = true;
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTarget, -1) || mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, -1);
        }
    }

    /**
     * @return Whether it is possible for the child view of this layout to
     * scroll up. Override this if the child view is a custom view.
     */
    public boolean canChildScrollDown() {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (mTarget instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTarget;
                return absListView.getLastVisiblePosition() != (absListView.getCount() - 1);
            } else {
                return ViewCompat.canScrollVertically(mTarget, 1) || mTarget.getHeight() - mTarget.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTarget, 1);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        boolean isScrollUp = false;
        ensureTarget();

        final int action = MotionEventCompat.getActionMasked(ev);

        if (!isEnabled() || mLoading || mResetting) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!mRefreshing) {
                    setTargetOffsetTopAndBottom(mOriginalOffsetTop, true);
                    mPullToRefreshHeaderView.onPressDown();
                }
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                final float initialDownY = getMotionEventY(ev, mActivePointerId);
                if (initialDownY == -1) {
                    return false;
                }
                mInitialDownY = initialDownY;

                mActionDownOffsetTop = mCurrentTargetOffsetTop;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                    return false;
                }

                final float y = getMotionEventY(ev, mActivePointerId);
                if (y == -1) {
                    return false;
                }
                final float yDiff = y - mInitialDownY;
                if (yDiff < 0) {
                    isScrollUp = true;
                }
                if (yDiff > mTouchSlop && !mIsBeingDragged) {
                    mInitialMotionY = mInitialDownY + mTouchSlop;
                    mIsBeingDragged = true;
                }
                break;

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;
        }

        if (!mIsAutoLoading && canLoadMore() && isScrollUp && !canChildScrollDown()) {
            startLoading();
            return false;
        }

        if (canChildScrollUp() || !mRefreshEnable) {
            return false;
        } else {
            if (mIsBeingDragged) {
                mCanTouchEvent = true;
            }
            return mIsBeingDragged;
//            return mIsBeingDragged || (isScrollUp && mRefreshing);
        }
    }

    public boolean canLoadMore(){
        if(!isLoadFail() && isLoadEnable() && !isRefreshing() && !isLoading()){
            return true;
        }
        return false;
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);

        if (!isEnabled() || !mCanTouchEvent || canChildScrollUp()) {
            // Fail fast if we're not in a state where a swipe is possible
            return false;
        }

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = MotionEventCompat.getPointerId(ev, 0);
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                if (pointerIndex < 0) {
                    Log.e(LOG_TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }

                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
                if (mIsBeingDragged) {

                    float originalDragPercent = overscrollTop / mTotalDragTopDistance;
                    if (originalDragPercent < 0) {
                        return false;
                    }

                    if (!mRefreshing) {
                        mPullToRefreshHeaderView.onPullToRefresh(overscrollTop >= mTotalDragTopDistance);
//                        if (overscrollTop < mTotalDragTopDistance) {
//                            mPullToRefreshHeaderView.onPullToRefresh(false);
//                        } else {
//                            mPullToRefreshHeaderView.onPullToRefresh(true);
//                        }
                    }
//                    setTargetOffsetTopAndBottom((int) (mOriginalOffsetTop + overscrollTop),
//                            true /* requires update */);
                    setTargetOffsetTopAndBottom((int) (mActionDownOffsetTop + overscrollTop),
                            true /* requires update */);
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP:
                onSecondaryPointerUp(ev);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                if (mActivePointerId == INVALID_POINTER) {
                    if (action == MotionEvent.ACTION_UP) {
                        Log.e(LOG_TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    }
                    return false;
                }
                final int pointerIndex = MotionEventCompat.findPointerIndex(ev, mActivePointerId);
                final float y = MotionEventCompat.getY(ev, pointerIndex);
                final float overscrollTop = (y - mInitialMotionY) * DRAG_RATE;
                mIsBeingDragged = false;
                if (overscrollTop > mTotalDragTopDistance) {
                    if (mRefreshing) {
                        if (mActionDownOffsetTop >= 0) {
                            animateOffsetToStartPosition(mCurrentTargetOffsetTop, null);
                        } else {
                            animateOffsetToCorrectPosition(mCurrentTargetOffsetTop, null);
                        }
                    } else {
                        setRefreshing(true, true /* notify */);
                    }
                } else {

//                    if (mRefreshing) {
//                        animateOffsetToStartPosition(mCurrentTargetOffsetTop, null);
//                    }else{
//                        animateOffsetToStartPosition(mCurrentTargetOffsetTop, mRefreshAnimationListener);
//                    }
                    mRefreshing = false;
                    animateOffsetToStartPosition(mCurrentTargetOffsetTop, mRefreshAnimationListener);
                }
                mActivePointerId = INVALID_POINTER;
                return false;
            }
        }

        return true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        final int pointerIndex = MotionEventCompat.getActionIndex(ev);
        final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
        if (pointerId == mActivePointerId) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            mActivePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
        }
    }

    private void setTargetOffsetTopAndBottom(int offset, boolean requiresUpdate) {
        mPullToRefreshHeaderLayoutParams.topMargin = offset;
        mPullToRefreshHeaderView.setLayoutParams(mPullToRefreshHeaderLayoutParams);
        mCurrentTargetOffsetTop = mPullToRefreshHeaderView.getTop();
        if (requiresUpdate && android.os.Build.VERSION.SDK_INT < 11) {
            invalidate();
        }
    }

    private void animateOffsetToStartPosition(int from, AnimationListener listener) {
        mFrom = from;
        mAnimateToStartPosition.reset();
        mAnimateToStartPosition.setDuration(ANIMATE_TO_START_DURATION);
        mAnimateToStartPosition.setInterpolator(mDecelerateInterpolator);
        mAnimateToStartPosition.setAnimationListener(listener);

        mPullToRefreshHeaderView.clearAnimation();
        mPullToRefreshHeaderView.startAnimation(mAnimateToStartPosition);
    }

    private void animateOffsetToCorrectPosition(int from, AnimationListener listener) {
        mFrom = from;
        mAnimateToCorrectPosition.reset();
        mAnimateToCorrectPosition.setDuration(ANIMATE_TO_TRIGGER_DURATION);
        mAnimateToCorrectPosition.setInterpolator(mDecelerateInterpolator);
        mAnimateToCorrectPosition.setAnimationListener(listener);

        mPullToRefreshHeaderView.clearAnimation();
        mPullToRefreshHeaderView.startAnimation(mAnimateToCorrectPosition);
    }

    private final Animation mAnimateToCorrectPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            int offset = (mFrom - (int) (mFrom * interpolatedTime));
            setTargetOffsetTopAndBottom(offset, false /* requires update */);
        }
    };

    private void moveToStart(float interpolatedTime) {
        int offset = (mFrom + (int) ((mOriginalOffsetTop - mFrom) * interpolatedTime));
        setTargetOffsetTopAndBottom(offset, false /* requires update */);
    }

    private final Animation mAnimateToStartPosition = new Animation() {
        @Override
        public void applyTransformation(float interpolatedTime, Transformation t) {
            moveToStart(interpolatedTime);
        }
    };

    private AnimationListener mRefreshAnimationListener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if (mRefreshing) {
                // Make sure the progress view is fully visible
                if (mNotify) {
                    if (mRefreshListener != null) {
                        mRefreshListener.onRefresh();
                    }
                }
            } else {
                // Return the circle to its start position
                setTargetOffsetTopAndBottom(mOriginalOffsetTop,
                        true /* requires update */);
            }
            mCurrentTargetOffsetTop = mPullToRefreshHeaderView.getTop();
        }
    };

    private AnimationListener mResetAnimationListener = new AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {
            mResetting = true;
            mCanTouchEvent = false;
        }

        @Override
        public void onAnimationRepeat(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mResetting = false;
        }
    };

    private void setRefreshing(boolean refreshing, final boolean notify) {
        if (mRefreshing != refreshing) {
            mNotify = notify;
            ensureTarget();
            mRefreshing = refreshing;
            if (mRefreshing) {
                mPullToRefreshHeaderView.onRefreshStart();
                animateOffsetToCorrectPosition(mCurrentTargetOffsetTop, mRefreshAnimationListener);
            } else {

                postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshComplete();
                    }
                }, mRefreshCompleteDelay);
            }
        }
    }

    private void refreshComplete(){
        mPullToRefreshHeaderView.onRefreshComplete();
        if (mIsAttachedToWindow) {
            animateOffsetToStartPosition(mCurrentTargetOffsetTop, mResetAnimationListener);
        }
    }

    /**
     * Notify the widget that refresh state has changed. Do not call this when
     * refresh is triggered by a swipe gesture.
     *
     * @param refreshing Whether or not the view should show refresh progress.
     */
    public void setRefreshing(boolean refreshing) {
        if (refreshing && mRefreshing != refreshing && !mLoading) {
            mRefreshing = refreshing;
            mNotify = true;
            mPullToRefreshHeaderView.onRefreshStart();
            animateOffsetToCorrectPosition(mCurrentTargetOffsetTop, mRefreshAnimationListener);
        } else {
            setRefreshing(refreshing, false /* notify */);
        }
    }

    public void update() {
        if (mRefreshing) {
            mPullToRefreshHeaderView.onRefreshStart();
            setTargetOffsetTopAndBottom(0, true);
//            animateOffsetToCorrectPosition(mCurrentTargetOffsetTop, null);
        } else {
            setTargetOffsetTopAndBottom(mOriginalOffsetTop, true);
            mResetting = false;
//            animateOffsetToStartPosition(mCurrentTargetOffsetTop, null);
        }
    }

    boolean mIsAttachedToWindow;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mIsAttachedToWindow = true;
        update();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mIsAttachedToWindow = false;
    }

    public void startLoading() {
        setLoading(true, false);
    }

    public void stopLoading(boolean isSuccess) {
        setLoading(false, isSuccess);
    }

    private void setLoading(boolean loading, boolean isSuccess) {
        if (mLoading == loading || (loading && !mLoadEnable)) {
            return;
        }
        mLoading = loading;
        if (loading && !mRefreshing) {
            mLoadFail = false;
            onLoad();
        } else {
            mLoadFail = !isSuccess;
            if (mLoadFail) {
                onShowLoadFail();
            }
        }
    }

    public void setLoadEnable(boolean loadEnable) {
        mLoadEnable = loadEnable;
        if (loadEnable) {
            if(mLoadFail){
                onShowLoadFail();
            }else {
                onShowLoading();
            }
        } else {
            onShowEnd();
        }
    }


    public void onShowLoading() {
        if(mOnLoadMoreCallBack != null){
            mOnLoadMoreCallBack.showLoading();
        }
    }

    public void onShowLoadFail() {
        if(mOnLoadMoreCallBack != null){
            mOnLoadMoreCallBack.showLoadFail();
        }
    }

    public void onShowEnd(){
        if(mOnLoadMoreCallBack != null){
            mOnLoadMoreCallBack.showEnd();
        }
    }

    public void setLoadFail() {
        mLoadFail = true;
    }

    private void onLoad() {
        if (mLoadListener != null) {
            mLoadListener.onLoad();
        }
    }

    public void onTaskComplete(boolean isSuccess) {
        if (isRefreshing()) {
            setRefreshing(false);
        } else if (isLoading()) {
            setLoading(false, isSuccess);
        }
    }

    public boolean isRefreshing() {
        return mRefreshing;
    }

    public boolean isLoading() {
        return mLoading;
    }

    public boolean isLoadFail() {
        return mLoadFail;
    }

    public boolean isLoadEnable() {
        return mLoadEnable;
    }

    public boolean isRefreshEnable() {
        return mRefreshEnable;
    }

    public void setRefreshEnable(boolean refreshEnable) {
        this.mRefreshEnable = refreshEnable;
    }

    /**
     * Set the listener to be notified when a refresh is triggered via the swipe
     * gesture.
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mRefreshListener = listener;
    }

    /**
     * Set the listener to be notified when a load more is triggered via the swipe
     * gesture.
     */
    public void setOnLoadListener(OnLoadListener listener) {
        mLoadListener = listener;
    }

    public OnLoadListener getOnLoadListener() {
        return mLoadListener;
    }

    public OnRefreshListener getOnRefreshListener() {
        return mRefreshListener;
    }

    public PullToRefreshHeader getPullToRefreshHeaderView() {
        return mPullToRefreshHeaderView;
    }

    public void setAutoLoadingListener(AutoLoadingListener autoLoadingListener) {
        this.mAutoLoadingListener = autoLoadingListener;
    }

    public OnLoadMoreCallBack getOnLoadMoreCallBack() {
        return mOnLoadMoreCallBack;
    }

    public void setOnLoadMoreCallBack(OnLoadMoreCallBack onLoadMoreCallBack) {
        this.mOnLoadMoreCallBack = onLoadMoreCallBack;
    }

    public long getRefreshCompleteDelay() {
        return mRefreshCompleteDelay;
    }

    public void setRefreshCompleteDelay(long refreshCompleteDelay) {
        this.mRefreshCompleteDelay = refreshCompleteDelay;
    }

    public interface AutoLoadingListener {

        boolean setAutoLoading(View target);
    }

    public interface OnLoadMoreCallBack{

        void showLoading();

        void showLoadFail();

        void showEnd();
    }
}
