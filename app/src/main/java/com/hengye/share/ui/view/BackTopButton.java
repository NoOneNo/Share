package com.hengye.share.ui.view;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

public class BackTopButton extends ImageView{

    public BackTopButton(Context context) {
        this(context, null, -1);
    }

    public BackTopButton(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BackTopButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        init();
    }

    private RecyclerView mRecyclerView;
    private ListView mListView;

    private Animation mBackTopAnimation, mAppearAnimation;

    private OnClickListener mOnBackTopListener;

    private Handler mHandler = new Handler();

    private void init(){
        setVisibility(View.GONE);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getOnBackTopListener() != null){
                    getOnBackTopListener().onClick(v);
                }
                smoothScrollToTop();
                startAnimation(mBackTopAnimation);
            }
        });

        AlphaAnimation alphaAnim1 = new AlphaAnimation(1.0f, 0.3f);

        TranslateAnimation translateAnim1 = new TranslateAnimation
                (Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, -1.0f);

        AnimationSet as1 = new AnimationSet(true);
        as1.addAnimation(alphaAnim1);
        as1.addAnimation(translateAnim1);
        as1.setDuration(1000);
        as1.setInterpolator(new AccelerateInterpolator());

        AlphaAnimation alphaAnim2 = new AlphaAnimation(0.0f, 1.0f);

        TranslateAnimation translateAnim2 = new TranslateAnimation
                (Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 0.3f,
                        Animation.RELATIVE_TO_PARENT, 0f);

        AnimationSet as2 = new AnimationSet(true);
        as2.addAnimation(alphaAnim2);
        as2.addAnimation(translateAnim2);
        as2.setDuration(1000);
        as2.setInterpolator(new AccelerateInterpolator());

        mBackTopAnimation = as1;
        mAppearAnimation = as2;

        mBackTopAnimation.setAnimationListener(mBackTopAnimationListener);
    }

    public void setup(RecyclerView recyclerView){
        mRecyclerView = recyclerView;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 10){
                    appear();
                }else if(dy < -20){
                    disappear();
                }
            }
        });
    }

    public void setup(ListView listView){
        mListView = listView;
        mListView.setOnScrollListener(mListViewOnScrollListener);
    }

    public void smoothScrollToTop(){
        if(mRecyclerView != null){
            mRecyclerView.smoothScrollToPosition(0);
        }else if(mListView != null){
            mListView.smoothScrollToPosition(0);
        }
    }

    public void scrollToTop(){
        if(mRecyclerView != null){
            mRecyclerView.scrollToPosition(0);
            mRecyclerView.smoothScrollBy(0,1);
        }else if(mListView != null){
            mListView.setSelection(0);
        }
    }

    private void disappear(){
        setVisibility(View.GONE);
    }

    private void appear(){
        if(getVisibility() == View.GONE){
            startAnimation(mAppearAnimation);
            setVisibility(View.VISIBLE);
        }
    }

    private AbsListView.OnScrollListener mOnScrollListener;
    /**
     * Set the listener that will receive notifications every time the list scrolls.
     * This is used to ListView.
     * @param l the scroll listener
     */
    public void setOnScrollListener(AbsListView.OnScrollListener l){
        mOnScrollListener = l;
    }

    private Animation.AnimationListener mBackTopAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {}

        @Override
        public void onAnimationEnd(Animation animation) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    scrollToTop();
                }
            });
        }

        @Override
        public void onAnimationRepeat(Animation animation) {}
    };


    private int mLastVisibleItem;
    private AbsListView.OnScrollListener mListViewOnScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if(mOnScrollListener != null){
                mOnScrollListener.onScrollStateChanged(view, scrollState);
            }

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if(mOnScrollListener != null){
                mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }

            if(firstVisibleItem != 0){

                //向下滑
                if(firstVisibleItem - mLastVisibleItem > 0){
                    appear();
                }else if(firstVisibleItem - mLastVisibleItem < 0){
                    disappear();
                }
            }else{
                disappear();
            }

            mLastVisibleItem = firstVisibleItem;

        }
    };

    public OnClickListener getOnBackTopListener() {
        return mOnBackTopListener;
    }

    public void setOnBackTopListener(OnClickListener backTopListener) {
        this.mOnBackTopListener = backTopListener;
    }
}
