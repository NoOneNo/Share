package com.hengye.share.ui.fragment.encapsulation.paging;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.CommonAdapter;

/**
 * Created by yuhy on 16/7/27.
 */
public abstract class RecyclerFragment<T> extends PagingFragment<T> {

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

    protected void setUpRecycler(RecyclerView recyclerView){
        recyclerView.setLayoutManager(getLayoutManager());
        recyclerView.setItemAnimator(getItemAnimator());

        RecyclerView.Adapter adapter = createAdapter();
        if(adapter != null) {
            recyclerView.setAdapter(createAdapter());
        }
    }

    public RecyclerView.Adapter createAdapter(){
        return null;
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        mRecyclerView.setAdapter(adapter);
    }

    /**
     * 默认是LinearLayoutManager
     *
     * @return
     */
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    protected RecyclerView.ItemAnimator getItemAnimator(){
        return new DefaultItemAnimator();
    }

}
















