package com.hengye.share.ui.widget.pulltorefresh;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.HeaderAdapter;
import com.hengye.share.ui.widget.listview.CommonListView;

/**
 * Created by yuhy on 2016/10/12.
 */

public class PullToRefreshLayout extends com.hengye.swiperefresh.PullToRefreshLayout {


    public PullToRefreshLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private HeaderAdapter mHeaderAdapter;
    private RecyclerView mRecyclerView;
    private CommonListView mListView;
    private View mLoading, mEnding, mLoadFail;
    private int mLastScrollPosition;

    @Override
    public void ensureTarget() {
        boolean isEmpty = getTarget() == null;
        super.ensureTarget();
        if (isEmpty && getTarget() != null) {
            if (getTarget() instanceof RecyclerView) {
                mRecyclerView = (RecyclerView) getTarget();
                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                    boolean hasLoading = false;

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        boolean canScrollDown = ViewCompat.canScrollVertically(mRecyclerView, 1);

                        if (!hasLoading && !isLoadFail() && !isLoading() && !canScrollDown && isLoadEnable() && dy > 0) {
                            hasLoading = true;
                            startLoading();
                        } else {
                            hasLoading = false;
                        }
                    }
                });
            } else if (getTarget() instanceof CommonListView) {
                mListView = (CommonListView) getTarget();
                mListView.addOnScrollListener(new AbsListView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                        // 当不滚动时
                        if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                            // 判断是否滚动到底部
                            if (view.getLastVisiblePosition() == view.getCount() - 1) {

                                if (!isLoadFail() && !isLoading() && isLoadEnable()) {
                                    startLoading();
                                }

                            }
                        }
                    }

                    @Override
                    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    }
                });
            }
        }
    }

    private boolean canCustomHeader() {
        if (mRecyclerView != null) {
            if (mHeaderAdapter != null) {
                return true;
            } else if (mRecyclerView.getAdapter() != null && mRecyclerView.getAdapter() instanceof HeaderAdapter) {
                mHeaderAdapter = (HeaderAdapter) mRecyclerView.getAdapter();
                return true;
            } else {
                return false;
            }
        } else if (mListView != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void showLoadingView() {
//        if (!canCustomHeader()) {
            super.showLoadingView();
//        } else {
//            if (mRecyclerView != null) {
//                mHeaderAdapter.setFooter(getLoading());
//            } else if (mListView != null) {
//                mListView.addFooterView(getLoading());
//
//                if (mEnding != null) {
//                    mListView.removeFooterView(getEnding());
//                }
//            }
//        }
    }

    @Override
    public void hideLoadingView() {
//        if (!canCustomHeader()) {
            super.hideLoadingView();
//        } else {
//            if (mRecyclerView != null) {
//                mHeaderAdapter.removeFooter();
//            } else if (mListView != null) {
//
//                if (mLoading != null) {
//                    mListView.removeFooterView(getLoading());
//                }
//
//                if (mEnding != null) {
//                    mListView.removeFooterView(getEnding());
//                }
//            }
//        }
    }

    boolean mLoadEnable = true;

    @Override
    public void setLoadEnable(boolean loadEnable) {
        super.setLoadEnable(loadEnable);

        if (mLoadEnable == loadEnable) {
            return;
        }

        mLoadEnable = loadEnable;
        if (canCustomHeader() && !loadEnable) {
            if (mRecyclerView != null) {
                if (mLoading != null) {
                    mHeaderAdapter.setFooter(getEnding());
                } else {
                    mHeaderAdapter.removeFooter();
                }
            } else if (mListView != null) {

                if (mLoading != null) {
                    mListView.addFooterView(getEnding());
                    mListView.removeFooterView(getLoading());
                }
            }
        }
    }

    private View getLoading() {
        if (mLoading == null) {
            mLoading = View.inflate(getContext(), R.layout.footer_load_more, null);
            mLoading.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return mLoading;
    }

    private View getEnding() {
        if (mEnding == null) {
            mEnding = View.inflate(getContext(), R.layout.footer_ending, null);
            mEnding.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
        return mEnding;
    }

//    private View getLoadFail() {
//        if (mLoadFail == null) {
//            mLoadFail = View.inflate(getContext(), R.layout.footer_load_fail, null);
//            mLoadFail.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            mLoadFail.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startLoading();
//                }
//            });
//        }
//        return mLoadFail;
//    }

}






