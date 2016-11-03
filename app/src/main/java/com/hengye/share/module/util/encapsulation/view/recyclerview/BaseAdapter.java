package com.hengye.share.module.util.encapsulation.view.recyclerview;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by yuhy on 2016/10/18.
 */

public abstract class BaseAdapter extends RecyclerView.Adapter {


    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
        mLayoutManager = mRecyclerView.getLayoutManager();
    }

    public void scrollToPosition(int position) {
        scrollToPosition(position, false);
    }

    public void scrollToPosition(int position, boolean isSmooth) {
        if (mRecyclerView == null) {
            return;
        }
        if (isSmooth) {
            mRecyclerView.smoothScrollToPosition(position);
        } else {
            if (mLayoutManager != null && mLayoutManager instanceof LinearLayoutManager) {
                ((LinearLayoutManager) mLayoutManager).scrollToPositionWithOffset(position, 0);
            } else {
                mRecyclerView.scrollToPosition(position);
            }
        }

    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
