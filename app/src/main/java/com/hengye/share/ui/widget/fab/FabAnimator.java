package com.hengye.share.ui.widget.fab;

import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;

import com.hengye.share.R;
import com.hengye.share.util.ResUtil;

/**
 * Created by wangdan on 16/2/2.
 */
public class FabAnimator {

    public static FabAnimator create(View fabBtn) {
        return create(fabBtn, ResUtil.getDimensionPixelSize(R.dimen.fab_scroll_threshold));
    }

    public static FabAnimator create(View fabBtn, int scrollThreshold) {
        return new FabAnimator(fabBtn, scrollThreshold);
    }

    private static final int TRANSLATE_DURATION_MILLIS = 300;

    private int duration = TRANSLATE_DURATION_MILLIS;

    private int scrollThreshold;

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    private FabAnimator(View fabBtn, int scrollThreshold) {
        this.fabBtn = fabBtn;
        this.scrollThreshold = scrollThreshold;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    private View fabBtn;
    private boolean mVisible = true;
    private CustomAnimator mCustomAnimator;

    public void attachToListView(AbsListView listView) {
        attachToListView(listView, null);
    }

    public FabAnimator attachToListView(AbsListView listView, AbsListView.OnScrollListener onScrollListener) {
        AbsListViewScrollDetectorImpl scrollDetector = new AbsListViewScrollDetectorImpl();
        scrollDetector.setListView(listView);
        scrollDetector.setScrollThreshold(scrollThreshold);
        if (onScrollListener != null) {
            scrollDetector.setOnScrollListener(onScrollListener);
        }
        listView.setOnScrollListener(scrollDetector);

        return this;
    }

    public FabAnimator attachToRecyclerView(RecyclerView recyclerView) {
        RecyclerViewScrollDetectorImpl scrollDetector = new RecyclerViewScrollDetectorImpl();
        scrollDetector.setScrollThreshold(scrollThreshold);
        recyclerView.addOnScrollListener(scrollDetector);

        return this;
    }

    private abstract class AbsListViewScrollDetector implements AbsListView.OnScrollListener {
        private int mLastScrollY;
        private int mPreviousFirstVisibleItem;
        private AbsListView mListView;
        private int mScrollThreshold;

        abstract void onScrollUp();

        abstract void onScrollDown();

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            if (totalItemCount == 0) return;
            if (isSameRow(firstVisibleItem)) {
                int newScrollY = getTopItemScrollY();
                boolean isSignificantDelta = Math.abs(mLastScrollY - newScrollY) > mScrollThreshold;
                if (isSignificantDelta) {
                    if (mLastScrollY > newScrollY) {
                        onScrollUp();
                    } else {
                        onScrollDown();
                    }
                }
                mLastScrollY = newScrollY;
            } else {
                if (firstVisibleItem > mPreviousFirstVisibleItem) {
                    onScrollUp();
                } else {
                    onScrollDown();
                }

                mLastScrollY = getTopItemScrollY();
                mPreviousFirstVisibleItem = firstVisibleItem;
            }
        }

        public void setScrollThreshold(int scrollThreshold) {
            mScrollThreshold = scrollThreshold;
        }

        public void setListView(AbsListView listView) {
            mListView = listView;
        }

        private boolean isSameRow(int firstVisibleItem) {
            return firstVisibleItem == mPreviousFirstVisibleItem;
        }

        private int getTopItemScrollY() {
            if (mListView == null || mListView.getChildAt(0) == null) return 0;
            View topChild = mListView.getChildAt(0);
            return topChild.getTop();
        }
    }

    private class AbsListViewScrollDetectorImpl extends AbsListViewScrollDetector {

        public AbsListView.OnScrollListener onScrollListener;

        public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
            this.onScrollListener = onScrollListener;
        }

        @Override
        public void onScrollDown() {
            show();
        }

        @Override
        public void onScrollUp() {
            hide();
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            super.onScrollStateChanged(view, scrollState);

            if (onScrollListener != null) {
                onScrollListener.onScrollStateChanged(view, scrollState);
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);

            if (onScrollListener != null) {
                onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }
        }
    }

    public void show() {
        show(true);
    }

    public void hide() {
        hide(true);
    }

    public void show(boolean animate) {
        toggle(true, animate, false);
    }

    public void hide(boolean animate) {
        toggle(false, animate, false);
    }

    private void toggle(final boolean visible, final boolean animate, boolean force) {
        if (mVisible != visible || force) {
            mVisible = visible;
            int height = mCustomAnimator != null ? mCustomAnimator.getViewHeight() : fabBtn.getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = fabBtn.getViewTreeObserver();
                if (vto.isAlive()) {
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = fabBtn.getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height + getMarginBottom();
            if (animate) {
                fabBtn.animate().setInterpolator(mInterpolator)
                        .setDuration(duration)
                        .translationY(translationY);
//                ViewPropertyAnimator.animate(fabBtn).setInterpolator(mInterpolator)
//                        .setDuration(TRANSLATE_DURATION_MILLIS)
//                        .translationY(translationY);
            } else {
                fabBtn.setTranslationY(translationY);
//                ViewHelper.setTranslationY(fabBtn, translationY);
            }

            // On pre-Honeycomb a translated view is still clickable, so we need to disable clicks manually
            if (!hasHoneycombApi()) {
                fabBtn.setClickable(visible);
            }
        }
    }

    private int getMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = fabBtn.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginBottom = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }

    private boolean hasHoneycombApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public CustomAnimator getCustomAnimator() {
        return mCustomAnimator;
    }

    public FabAnimator setCustomAnimator(CustomAnimator customAnimator) {
        this.mCustomAnimator = customAnimator;
        return this;
    }

    abstract class RecyclerViewScrollDetector extends RecyclerView.OnScrollListener {

        private int mScrollThreshold;

        abstract void onScrollUp();

        abstract void onScrollDown();

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            boolean isSignificantDelta = Math.abs(dy) > mScrollThreshold;
            if (isSignificantDelta) {
                if (dy > 0) {
                    onScrollUp();
                } else {
                    onScrollDown();
                }
            }
        }

        public void setScrollThreshold(int scrollThreshold) {
            mScrollThreshold = scrollThreshold;
        }
    }

    private class RecyclerViewScrollDetectorImpl extends RecyclerViewScrollDetector {
        @Override
        public void onScrollDown() {
            show();
        }

        @Override
        public void onScrollUp() {
            hide();
        }
    }

    public interface CustomAnimator{
        int getViewHeight();
    }
}
