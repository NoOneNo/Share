package com.hengye.share.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;

import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.AtUserAdapter;
import com.hengye.swiperefresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class AtUserActivity extends BaseActivity{

    @Override
    protected String getRequestTag() {
        return super.getRequestTag();
    }

    @Override
    protected boolean setCustomTheme() {
        return super.setCustomTheme();
    }

    @Override
    protected boolean setToolBar() {
        return super.setToolBar();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_at_user);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private PullToRefreshLayout mPullToRefreshLayout;
    private RecyclerView mRVSelectResult, mRVSearchResult;
    private EditText mSearchContent;

    private AtUserAdapter mAtUserAdapter;
    private List<String> mSearchResultData;


    private void initView(){

        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);

        mRVSelectResult = (RecyclerView) findViewById(R.id.recycler_view_select_result);

        mRVSearchResult = (RecyclerView) findViewById(R.id.recycler_view_search_result);

        mRVSearchResult.setLayoutManager(new LinearLayoutManager(this));
        mRVSearchResult.setAdapter(mAtUserAdapter = new AtUserAdapter(this, getSearchResultData()));
//        mRVSearchResult.setItemAnimator(new DefaultItemAnimator());

        mSearchContent = (EditText) findViewById(R.id.et_username);


    }

    private List<String> getSearchResultData(){
        mSearchResultData = new ArrayList<>();
        mSearchResultData.add("test");
        mSearchResultData.add("test");
        mSearchResultData.add("test");
        mSearchResultData.add("test");
        mSearchResultData.add("test");
        mSearchResultData.add("test");
        mSearchResultData.add("test");
        mSearchResultData.add("test");
        mSearchResultData.add("test");
        mSearchResultData.add("test");
        mSearchResultData.add("test");

        return mSearchResultData;
    }
}
