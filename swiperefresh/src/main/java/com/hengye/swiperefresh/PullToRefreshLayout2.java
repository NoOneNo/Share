package com.hengye.swiperefresh;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParentHelper;
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


public class PullToRefreshLayout2 extends LinearLayout {

    private static final String LOG_TAG = PullToRefreshLayout2.class.getSimpleName();
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
    public PullToRefreshLayout2(Context context) {
        this(context, null);
    }

    /**
     * Constructor that is called when inflating SwipeRefreshLayout from XML.
     *
     * @param context
     * @param attrs
     */
    @SuppressLint("InflateParams")
    public PullToRefreshLayout2(Context context, AttributeSet attrs) {
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

        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);

        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
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

    @Override
    public void requestDisallowInterceptTouchEvent(boolean b) {
        // if this is a List < L or another view that doesn't support nested
        // scrolling, ignore this request so that the vertical scroll event
        // isn't stolen
        if ((android.os.Build.VERSION.SDK_INT < 21 && mTarget instanceof AbsListView)
                || (mTarget != null && !ViewCompat.isNestedScrollingEnabled(mTarget))) {
            // Nope.
        } else {
            super.requestDisallowInterceptTouchEvent(b);
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

                    setAutoLoadingIfNeed();
                    break;
                }
            }
        }else{
            setAutoLoadingIfNeed();
        }
    }

    private void setAutoLoadingIfNeed(){
        if (mAutoLoadingListener != null && !mIsSetAutoLoading) {
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

//        Log.d(LOG_TAG, "onInterceptTouchEvent invoke");
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
//                mIsBeingDragged = false;
                final float initialDownY = getMotionEventY(ev, mActivePointerId);
                if (initialDownY == -1) {
                    return false;
                }
                mInitialDownY = initialDownY;

//                if(!mIsBeingDragged) {
                    mActionDownOffsetTop = mCurrentTargetOffsetTop;
//                }
                mIsBeingDragged = false;
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
        return !isLoadFail() && isLoadEnable() && !isRefreshing() && !isLoading();
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = MotionEventCompat.findPointerIndex(ev, activePointerId);
        if (index < 0) {
            return -1;
        }
        return MotionEventCompat.getY(ev, index);
    }

    int mTotalOverscrollTop, mOverscrollTop;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.d(LOG_TAG, "onTouchEvent invoke");
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
                final float overscrollTop = (y - mInitialMotionY) * DRAG_RATE + mTotalOverscrollTop;
                mOverscrollTop = (int)overscrollTop;
                if (mIsBeingDragged) {

                    //计算拖拉的比例返回给HeaderView，进行动态变换
//                    float originalDragPercent = overscrollTop / mTotalDragTopDistance;
//                    if (originalDragPercent < 0) {
//                        return false;
//                    }
                    if(overscrollTop > 0){

                        if (!mRefreshing) {
                            mPullToRefreshHeaderView.onPullToRefresh(overscrollTop >= mTotalDragTopDistance);
                        }
//                    setTargetOffsetTopAndBottom((int) (mOriginalOffsetTop + overscrollTop),
//                            true /* requires update */);
                        setTargetOffsetTopAndBottom((int) (mActionDownOffsetTop + overscrollTop),
                                true /* requires update */);
                    }else{
                        return false;
                    }
                }
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                final int index = MotionEventCompat.getActionIndex(ev);
                mActivePointerId = MotionEventCompat.getPointerId(ev, index);
                mTotalOverscrollTop = mOverscrollTop;

                final float initialDownY = getMotionEventY(ev, mActivePointerId);
                if (initialDownY == -1) {
                    return false;
                }
                mInitialMotionY = initialDownY;
                break;
            }

            case MotionEventCompat.ACTION_POINTER_UP:
                Log.d(LOG_TAG, "ACTION_POINTER_UP");
                onSecondaryPointerUp(ev);

                mTotalOverscrollTop = mOverscrollTop;
                final float initialDownY = getMotionEventY(ev, mActivePointerId);
                if (initialDownY == -1) {
                    return false;
                }
                mInitialMotionY = initialDownY;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {

                Log.d(LOG_TAG, "ACTION_UP");
                mTotalOverscrollTop = 0;
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
//                finishPullDown(overscrollTop);
                finishPullDown(mCurrentTargetOffsetTop);
                mActivePointerId = INVALID_POINTER;
                return false;
            }
        }

        return true;
    }

    private void finishPullDown(float overscrollTop){
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
            animateOffsetToStartPosition(mCurrentTargetOffsetTop, null);
        }
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

    protected void update() {
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
        if(mLoadEnable == loadEnable){
            return;
        }

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

    public void setTaskComplete(boolean isSuccess) {
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




    // If nested scrolling is enabled, the total amount that needed to be
    // consumed by this as the nested scrolling parent is used in place of the
    // overscroll determined by MOVE events in the onTouch handler
    private float mTotalUnconsumed;
    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];
    private boolean mNestedScrollInProgress;

    // NestedScrollingParent

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
//        return isEnabled() && !mReturningToStart && !mRefreshing
//                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;

        return isEnabled()
                && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, axes);
        // Dispatch up to the nested parent
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mTotalUnconsumed = 0;
        mNestedScrollInProgress = true;

        mActionDownOffsetTop = mCurrentTargetOffsetTop;

        Log.e(LOG_TAG, "onNestedScrollAccepted");
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        // If we are in the middle of consuming, a scroll, then we want to move the spinner back up
        // before allowing the list to scroll
        if (dy > 0 && mTotalUnconsumed > 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = dy - (int) mTotalUnconsumed;
                mTotalUnconsumed = 0;
            } else {
                mTotalUnconsumed -= dy;
                consumed[1] = dy;
            }
//            moveSpinner(mTotalUnconsumed);
            final float overscrollTop = mTotalUnconsumed * DRAG_RATE;
            setTargetOffsetTopAndBottom(mActionDownOffsetTop + (int)overscrollTop, true /* requires update */);
        }

        // If a client layout is using a custom start position for the circle
        // view, they mean to hide it again before scrolling the child view
        // If we get back to mTotalUnconsumed == 0 and there is more to go, hide
        // the circle so it isn't exposed if its blocking content is moved
//        if (mUsingCustomStart && dy > 0 && mTotalUnconsumed == 0
//                && Math.abs(dy - consumed[1]) > 0) {
//            mCircleView.setVisibility(View.GONE);
//        }


        // Now let our nested parent consume the leftovers
        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        mNestedScrollInProgress = false;
        // Finish the spinner for nested scrolling if we ever consumed any
        // unconsumed nested scroll
        if (mTotalUnconsumed > 0) {
//            finishSpinner(mTotalUnconsumed);
            finishPullDown(mTotalUnconsumed * DRAG_RATE);
            mTotalUnconsumed = 0;
        }
        // Dispatch up our nested parent
        stopNestedScroll();
    }

    @Override
    public void onNestedScroll(final View target, final int dxConsumed, final int dyConsumed,
                               final int dxUnconsumed, final int dyUnconsumed) {
        // Dispatch up to the nested parent first
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow);

        // This is a bit of a hack. Nested scrolling works from the bottom up, and as we are
        // sometimes between two nested scrolling views, we need a way to be able to know when any
        // nested scrolling parent has stopped handling events. We do that by using the
        // 'offset in window 'functionality to see if we have been moved from the event.
        // This is a decent indication of whether we should take over the event stream or not.
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy < 0 && !canChildScrollUp()) {
            mTotalUnconsumed += Math.abs(dy);
//            moveSpinner(mTotalUnconsumed);
            final float overscrollTop = mTotalUnconsumed * DRAG_RATE;
            setTargetOffsetTopAndBottom(mActionDownOffsetTop + (int)overscrollTop, true /* requires update */);
        }
        Log.d("debug", "dy : " + dy + ", mTotalUnconsumed : " + mTotalUnconsumed);
    }

    // NestedScrollingChild

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX,
                                    float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY,
                                 boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
