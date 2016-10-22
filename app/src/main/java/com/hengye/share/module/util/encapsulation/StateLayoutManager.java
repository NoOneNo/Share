package com.hengye.share.module.util.encapsulation;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

/**
 * Created by yuhy on 2016/10/19.
 */

public class StateLayoutManager {

    public static final int STATE_CONTENT = 1;
    public static final int STATE_LOADING = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_NO_NETWORK = 4;
    public static final int STATE_SERVICE_ERROR = 5;

    public StateLayoutManager(Context context, ViewGroup viewGroup) {
        this(context, viewGroup, true);
    }

    public StateLayoutManager(Context context, ViewGroup viewGroup, boolean isLayoutResource) {
        mContext = context;
        mViewGroup = viewGroup;
        mIsLayoutResource = isLayoutResource;
    }

    private Context mContext;
    private ViewGroup mViewGroup;
    private boolean mIsLayoutResource;

    private SparseIntArray mViewResourceIds = new SparseIntArray();
    private SparseArray<View> mViews = new SparseArray<>();
    private OnFindStateViewListener mOnFindStateViewListener;
    private int mCurrentState;
    private boolean mAnimateViewChange = true;
    private OnAnimateViewChangeListener mOnAnimateViewChangeListener;

    public void setViewResourceId(int state, @LayoutRes int layoutId) {
        mViewResourceIds.append(state, layoutId);
    }

    public int getViewResourceId(int state) {
        return mViewResourceIds.get(state);
    }

    public View getStateView(int state) {

        View view = mViews.get(state);
        if (view != null) {
            return view;
        }

        view = findStateView(state);
        mViews.append(state, view);
        return view;
    }

    private View findStateView(int state) {
        View view = null;
        int resourceId = mViewResourceIds.get(state);
        if (mIsLayoutResource) {
            if (resourceId != 0) {
                view = View.inflate(mContext, resourceId, null);
                hideView(view);
                mViewGroup.addView(view);
            }
        } else {
            view = mViewGroup.findViewById(resourceId);
        }
        if (view != null) {
            onFindStateView(view, state);
        }
        return view;
    }

    private void onFindStateView(View stateView, int state) {
        if (mOnFindStateViewListener != null) {
            mOnFindStateViewListener.onFindStateView(stateView, state);
        }
    }

    private void showView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    private void hideView(View view) {
        view.setVisibility(View.GONE);
    }

    public void showState(int state) {
        if (state != mCurrentState) {
            int previousState = mCurrentState;
            mCurrentState = state;
            setView(previousState);
        }
    }

    private void setView(int previousState) {

        View currentStateView = getStateView(mCurrentState);
        if (currentStateView == null) {
            return;
        }

        int len = mViewResourceIds.size();
        for (int i = 0; i < len; i++) {
            int viewState = mViewResourceIds.keyAt(i);

            if (viewState == mCurrentState) {
                continue;
            }

            View v = mViews.get(viewState);

            if (v == null) {
                continue;
            }

            hideView(v);

        }

        boolean canAnimateViewChange = isAnimateViewChange() && getStateView(previousState) != null;
        if (canAnimateViewChange) {
            animateViewChange(getStateView(previousState), currentStateView);
        } else {
            showView(currentStateView);
        }

    }

    private void animateViewChange(final View previousView, final View currentStateView) {
        getOnAnimateViewChangeListener().onAnimateViewChange(previousView, currentStateView);
    }

    public OnFindStateViewListener getOnFindStateViewListener() {
        return mOnFindStateViewListener;
    }

    public void setOnFindStateViewListener(OnFindStateViewListener onFindStateViewListener) {
        this.mOnFindStateViewListener = onFindStateViewListener;
    }

    public boolean isAnimateViewChange() {
        return mAnimateViewChange;
    }

    public void setAnimateViewChanges(boolean animateViewChange) {
        this.mAnimateViewChange = animateViewChange;
    }

    public OnAnimateViewChangeListener getOnAnimateViewChangeListener() {
        if(mOnAnimateViewChangeListener == null){
            mOnAnimateViewChangeListener = new DefaultAnimateViewChangeListener();
        }
        return mOnAnimateViewChangeListener;
    }

    public void setOnAnimateViewChangeListener(OnAnimateViewChangeListener onAnimateViewChangeListener) {
        this.mOnAnimateViewChangeListener = onAnimateViewChangeListener;
    }

    public interface OnFindStateViewListener {

        void onFindStateView(View stateView, int state);
    }

    public interface OnAnimateViewChangeListener{

        void onAnimateViewChange(View previousView, View currentStateView);
    }

    public class DefaultAnimateViewChangeListener implements OnAnimateViewChangeListener{
        @Override
        public void onAnimateViewChange(final View previousView, final View currentStateView) {
            showView(previousView);
            AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(350);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    hideView(previousView);
                    showView(currentStateView);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            previousView.setAnimation(animation);
            animation.start();
        }
    }
}
















