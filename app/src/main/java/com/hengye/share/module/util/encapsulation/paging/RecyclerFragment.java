package com.hengye.share.module.util.encapsulation.paging;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.CommonAdapter;
import com.hengye.share.ui.widget.listener.OnItemClickListener;
import com.hengye.share.ui.widget.listener.OnItemLongClickListener;

/**
 * Created by yuhy on 16/7/27.
 */
public abstract class RecyclerFragment<T> extends PagingFragment<T> implements OnItemClickListener, OnItemLongClickListener{

    @Override
    public int getContentResId() {
        return R.layout.fragment_recycler;
    }

    public @IdRes int getRecyclerViewId(){
        return R.id.recycler_view;
    }

    public RecyclerView getRecyclerView(){
        return mRecyclerView;
    }

    @Override
    protected void initContent(@Nullable Bundle savedInstanceState) {
        super.initContent(savedInstanceState);

        mRecyclerView = (RecyclerView) findViewById(getRecyclerViewId());

        setUpRecycler(mRecyclerView);
    }

    RecyclerView mRecyclerView;
    CommonAdapter mCommonAdapter;

    protected void setUpRecycler(RecyclerView recyclerView){
        recyclerView.setLayoutManager(generateLayoutManager());
        recyclerView.setItemAnimator(getItemAnimator());
    }

    public void setAdapter(CommonAdapter adapter){
        setAdapter(adapter, false);
    }

    public void setAdapter(CommonAdapter adapter, boolean isBindClick){
        mRecyclerView.setAdapter(mCommonAdapter = adapter);

        if(isBindClick) {
            adapter.setOnItemClickListener(this);
            adapter.setOnItemLongClickListener(this);
        }
    }

    public CommonAdapter getAdapter(){
        return mCommonAdapter;
    }

    public boolean isEmpty(){
        if(getAdapter() != null){
            return getAdapter().getBasicItemCount() == 0;
        }
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        return false;
    }

    /**
     * 默认是LinearLayoutManager
     *
     * @return
     */
    protected RecyclerView.LayoutManager generateLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    protected RecyclerView.ItemAnimator getItemAnimator(){
        return new DefaultItemAnimator();
    }

}
















