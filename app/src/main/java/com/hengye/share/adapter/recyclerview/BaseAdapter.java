package com.hengye.share.adapter.recyclerview;

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

    public void scrollToPosition(int position){
        if(mLayoutManager != null){
            if(mLayoutManager instanceof LinearLayoutManager){
                ((LinearLayoutManager)mLayoutManager).scrollToPositionWithOffset(position, 0);
            }else{
                mLayoutManager.scrollToPosition(position);
            }
        }
    }

    public RecyclerView.LayoutManager getLayoutManager(){
        return mLayoutManager;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }
}
