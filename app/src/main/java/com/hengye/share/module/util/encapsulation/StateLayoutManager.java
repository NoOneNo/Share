package com.hengye.share.module.util.encapsulation;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;

/**
 * Created by yuhy on 2016/10/19.
 */

public class StateLayoutManager {

    public static final int STATE_LOADING = 1;
    public static final int STATE_EMPTY = 2;
    public static final int STATE_NO_NETWORK = 3;
    public static final int STATE_CONTENT = 4;

    public StateLayoutManager(Context context, ViewGroup viewGroup){
        this(context, viewGroup, true);
    }

    public StateLayoutManager(Context context, ViewGroup viewGroup, boolean isLayoutResource){
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

    public void setViewResourceId(int state, @LayoutRes int layoutId){
        mViewResourceIds.append(state, layoutId);
    }

    public int getViewResourceId(int state){
        return mViewResourceIds.get(state);
    }

    public View getStateView(int state){

        View view = mViews.get(state);
        if(view != null){
            return view;
        }

        view = findStateView(state);
        mViews.append(state, view);
        return view;
    }

    private View findStateView(int state){
        View view = null;
        int resourceId = mViewResourceIds.get(state);
        if(mIsLayoutResource){
            if(resourceId != 0) {
                view = View.inflate(mContext, resourceId, null);
                hideView(view);
                mViewGroup.addView(view);
            }
        }else{
            view = mViewGroup.findViewById(resourceId);
        }
        if(view != null){
            onFindStateView(view, state);
        }
        return view;
    }

    private void onFindStateView(View stateView, int state){
        if(mOnFindStateViewListener != null){
            mOnFindStateViewListener.onFindStateView(stateView, state);
        }
    }

    private void showView(View view){
        view.setVisibility(View.VISIBLE);
    }

    private void hideView(View view){
        view.setVisibility(View.GONE);
    }

    public void showState(int state) {
        View view = getStateView(state);

        if(view == null){
            return;
        }


//        showViewAnimation(view);
        showView(view);

        int len = mViewResourceIds.size();
        for(int i = 0; i < len; i++){
            int viewState = mViewResourceIds.keyAt(i);

            if (viewState == state) {
                continue;
            }

            View v = mViews.get(viewState);

            if (v == null) {
                continue;
            }

            hideView(v);
        }
    }

    private void showViewAnimation(View view){
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(500);
        view.setAnimation(animation);
    }

    public OnFindStateViewListener getOnFindStateViewListener() {
        return mOnFindStateViewListener;
    }

    public void setOnFindStateViewListener(OnFindStateViewListener onFindStateViewListener) {
        this.mOnFindStateViewListener = onFindStateViewListener;
    }

    public interface OnFindStateViewListener{

        void onFindStateView(View stateView, int state);
    }
}
















