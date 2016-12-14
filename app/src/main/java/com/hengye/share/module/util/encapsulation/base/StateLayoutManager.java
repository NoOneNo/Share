package com.hengye.share.module.util.encapsulation.base;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;

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
    private OnAnimateViewChangeHelper mOnAnimateViewChangeHelper;

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
                view = LayoutInflater.from(mContext).inflate(resourceId, mViewGroup, false);
                hideView(view);
                mViewGroup.addView(view);
            }
        } else {
            view = mViewGroup.findViewById(resourceId);
            hideView(view);
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
        if(view == null){
            return;
        }
        view.setVisibility(View.VISIBLE);
    }

    private void hideView(View view) {
        if(view == null){
            return;
        }
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

        if(currentStateView.getVisibility() == View.VISIBLE){
            //isVisible already
//            L.debug("isVisible already");
            return;
        }

        int len = mViewResourceIds.size();
        for (int i = 0; i < len; i++) {
            int viewState = mViewResourceIds.keyAt(i);

            if (viewState == mCurrentState || viewState == previousState) {
                continue;
            }

            View v = mViews.get(viewState);

            if (v == null) {
                continue;
            }
            hideView(v);
        }

        View previousView = getStateView(previousState);
        boolean canAnimateViewChange = isAnimateViewChange() && previousView != null;
        if (canAnimateViewChange) {
            animateViewChange(previousView, currentStateView);
        } else {
            hideView(previousView);
            showView(currentStateView);
        }

    }

    public void updateState(){
//        L.debug("updateState invoke()");
        setView(mCurrentState);
    }

    public int getCurrentState(){
        return mCurrentState;
    }

    private void animateViewChange(final View previousView, final View currentStateView) {

        previousView.setAnimation(getOnAnimateViewChangeHelper().getDisappearAnimation());
        currentStateView.setAnimation(getOnAnimateViewChangeHelper().getAppearAnimation());
        hideView(previousView);
        showView(currentStateView);
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

    public OnAnimateViewChangeHelper getOnAnimateViewChangeHelper() {
        if(mOnAnimateViewChangeHelper == null){
            mOnAnimateViewChangeHelper = new DefaultAnimateViewChangeHelper();
        }
        return mOnAnimateViewChangeHelper;
    }

    public void setOnAnimateViewChangeListener(OnAnimateViewChangeHelper onAnimateViewChangeHelper) {
        this.mOnAnimateViewChangeHelper = onAnimateViewChangeHelper;
    }

    public interface OnFindStateViewListener {

        void onFindStateView(View stateView, int state);
    }

    public interface OnAnimateViewChangeHelper {

        Animation getDisappearAnimation();

        Animation getAppearAnimation();
    }

    public class DefaultAnimateViewChangeHelper implements OnAnimateViewChangeHelper {

        @Override
        public Animation getDisappearAnimation() {
//            Animation animation = new Animation() {
//                @Override
//                public void applyTransformation(float interpolatedTime, Transformation t) {
//                    if(interpolatedTime == 1.0f){
//                        t.setAlpha(0.0f);
//                    }else{
//                        t.setAlpha(1.0f);
//                    }
//                }
//            };
//            animation.setDuration(350);
//            return animation;


            AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
            animation.setDuration(350);
            return animation;
        }

        @Override
        public Animation getAppearAnimation() {
            Animation animation = new Animation() {
                @Override
                public void applyTransformation(float interpolatedTime, Transformation t) {
                    if(interpolatedTime == 1.0f){
                        t.setAlpha(1.0f);
                    }else{
                        t.setAlpha(0.0f);
                    }
                }
            };
            animation.setDuration(350);
            return animation;
        }
    }
}

