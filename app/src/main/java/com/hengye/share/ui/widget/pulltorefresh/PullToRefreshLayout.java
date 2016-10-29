package com.hengye.share.ui.widget.pulltorefresh;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.HeaderAdapter;
import com.hengye.share.ui.widget.listview.CommonListView;

/**
 * Created by yuhy on 2016/10/12.
 */

public class PullToRefreshLayout extends com.hengye.swiperefresh.PullToRefreshLayout
        implements com.hengye.swiperefresh.PullToRefreshLayout.AutoLoadingListener, com.hengye.swiperefresh.PullToRefreshLayout.OnLoadMoreCallBack {


    public PullToRefreshLayout(Context context) {
        this(context, null);
    }

    public PullToRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOnLoadMoreCallBack(this);
        setAutoLoadingListener(this);
        ensureTarget();
    }

    private HeaderAdapter mHeaderAdapter;
    private RecyclerView mRecyclerView;
    private CommonListView mListView;
    private View mLoading, mLoadEnd, mLoadFail;

    private boolean canCustomFooter() {
        if (mRecyclerView != null) {
            if (mHeaderAdapter != null) {
                return true;
            } else if (mRecyclerView.getAdapter() != null && mRecyclerView.getAdapter() instanceof HeaderAdapter) {
                mHeaderAdapter = (HeaderAdapter) mRecyclerView.getAdapter();
                mHeaderAdapter.setFooterFullSpan(true);
                return true;
            } else {
                return false;
            }
        }else if(mListView != null){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setAutoLoading(View target) {
        if (target instanceof RecyclerView) {
            mRecyclerView = (RecyclerView) target;
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    boolean canScrollDown = ViewCompat.canScrollVertically(recyclerView, 1);

                    if (canLoadMore() && !canScrollDown && dy > 0) {
                        startLoading();
                    }
                }
            });
            return true;
        } else if (getTarget() instanceof CommonListView) {
            mListView = (CommonListView) target;
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
            return true;
        }
        return false;
    }

    @Override
    public void showLoading() {
        if(canCustomFooter()) {
            if (mHeaderAdapter != null) {
                mHeaderAdapter.setFooter(getLoading());
            }else if(mListView != null){
                removeListViewFooters();
                mListView.addFooterView(getLoading(), null, false);
            }
        }
    }

    @Override
    public void showLoadFail() {
        if(canCustomFooter()) {
            if (mHeaderAdapter != null) {
                mHeaderAdapter.setFooter(getLoadFail());
            }else if(mListView != null){
                removeListViewFooters();
                mListView.addFooterView(getLoadFail(), null, false);
            }
        }
    }

    @Override
    public void showEnd() {
        if(canCustomFooter()) {
            if (mHeaderAdapter != null) {
                if(mHeaderAdapter.getBasicItemCount() != 0) {
                    mHeaderAdapter.setFooter(getLoadEnd());
                }else{
                    mHeaderAdapter.removeFooter();
                }
            }else if(mListView != null){
                removeListViewFooters();
                int actualChildCount = mListView.getCount() - mListView.getHeaderViewsCount() - mListView.getFooterViewsCount();
                if(actualChildCount > 0){
                    mListView.addFooterView(getLoadEnd(), null, false);
                }
            }
        }
    }

    private void removeListViewFooters(){
        mListView.removeFooterView(getLoading());
        mListView.removeFooterView(getLoadFail());
        mListView.removeFooterView(getLoadEnd());
    }

    private View getLoading() {
        if (mLoading == null) {
            mLoading = View.inflate(getContext(), R.layout.footer_load_more, null);
        }
        return mLoading;
    }

    private View getLoadEnd() {
        if (mLoadEnd == null) {
            mLoadEnd = View.inflate(getContext(), R.layout.footer_ending, null);
        }
        return mLoadEnd;
    }

    private View getLoadFail() {
        if (mLoadFail == null) {
            mLoadFail = View.inflate(getContext(), R.layout.footer_load_fail, null);
            mLoadFail.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onShowLoading();
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startLoading();
                        }
                    }, 1000);

                }
            });
        }
        return mLoadFail;
    }

}






