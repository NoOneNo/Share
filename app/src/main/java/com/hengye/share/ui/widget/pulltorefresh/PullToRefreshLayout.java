package com.hengye.share.ui.widget.pulltorefresh;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import com.hengye.share.R;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.module.util.encapsulation.view.recyclerview.HeaderAdapter;
import com.hengye.share.ui.widget.FooterLoadStateView;
import com.hengye.share.ui.widget.common.CommonListView;
import com.hengye.share.util.L;

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
    private FooterLoadStateView mFooterLoadStateView;

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
        } else if (mListView != null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean needLoading(RecyclerView recyclerView) {
        if (SettingHelper.isPreRead() && recyclerView.getLayoutManager() instanceof LinearLayoutManager) {

            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

            int totalItemCount = layoutManager.getItemCount();

            if (totalItemCount > 6) {
                //预加载，提前5个位置就加载更多
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                int childCount = layoutManager.getChildCount();
//                int state = recyclerView.getScrollState();
                return childCount > 0 &&
                        lastVisibleItemPosition >= totalItemCount - 6;
//                &&
//                        state == RecyclerView.SCROLL_STATE_IDLE;
            } else {
                return !ViewCompat.canScrollVertically(recyclerView, 1);
            }

        } else {
            return !ViewCompat.canScrollVertically(recyclerView, 1);
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
                    if (canLoadMore() && needLoading(recyclerView) && dy > 0) {
//                        L.debug("auto loading start");
                        startLoading();
                    }
                }
            });
            return true;
        } else if (target instanceof CommonListView) {
            mListView = (CommonListView) target;
            mListView.addOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    // 当不滚动时
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                        // 判断是否滚动到底部
                        //增加预加载
                        boolean needLoading;
                        if (SettingHelper.isPreRead()) {
                            needLoading = view.getLastVisiblePosition() >= view.getCount() - 6;
                        } else {
                            needLoading = view.getLastVisiblePosition() == view.getCount() - 1;
                        }

                        if (needLoading && canLoadMore()) {
//                            L.debug("auto loading start");
                            startLoading();
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
        if (canCustomFooter()) {
            if (mHeaderAdapter != null) {
                getFooterLoadStateView().showLoading();
                mHeaderAdapter.setFooter(getFooterLoadStateView());
            } else if (mListView != null) {
                removeListViewFooters();
                getFooterLoadStateView().showLoading();
                addListViewFooters();
            }
        }
    }

    @Override
    public void showLoadFail() {
        if (canCustomFooter()) {
            if (mHeaderAdapter != null) {
                getFooterLoadStateView().showLoadFail();
                mHeaderAdapter.setFooter(getFooterLoadStateView());
            } else if (mListView != null) {
                removeListViewFooters();
                getFooterLoadStateView().showLoadFail();
                addListViewFooters();
            }
        }
    }

    @Override
    public void showEnd() {
        if (canCustomFooter()) {
            if (mHeaderAdapter != null) {
                if (mHeaderAdapter.getBasicItemCount() != 0) {
                    getFooterLoadStateView().showEnding();
                    mHeaderAdapter.setFooter(getFooterLoadStateView());
                } else {
                    mHeaderAdapter.removeFooter();
                }
            } else if (mListView != null) {
                removeListViewFooters();
                int actualChildCount = mListView.getCount() - mListView.getHeaderViewsCount() - mListView.getFooterViewsCount();
                if (actualChildCount > 0) {
                    getFooterLoadStateView().showEnding();
                    addListViewFooters();
                }
            }
        }
    }

    private void removeListViewFooters() {
        mListView.removeFooterView(getFooterLoadStateView());
    }

    private void addListViewFooters() {
        mListView.addFooterView(getFooterLoadStateView(), null, false);
    }

    private FooterLoadStateView getFooterLoadStateView() {
        if (mFooterLoadStateView == null) {
            mFooterLoadStateView = new FooterLoadStateView(getContext());
            mFooterLoadStateView.setOnLoadStateClickListener(new FooterLoadStateView.onLoadStateClickListener() {
                @Override
                public void onLoadStateClick(View v, int state) {
                    if (state == FooterLoadStateView.STATE_LOAD_FAIL ||
                            state == FooterLoadStateView.STATE_LOAD_MORE) {
                        onShowLoading();
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startLoading();
                            }
                        }, 1000);
                    }
                }
            });
        }
        return mFooterLoadStateView;
    }

}






