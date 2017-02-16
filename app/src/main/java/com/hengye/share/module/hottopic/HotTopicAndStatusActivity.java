package com.hengye.share.module.hottopic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.HotSearch;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.base.BaseFragmentActivity;
import com.hengye.share.module.topic.TopicThemeActivity;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.ui.widget.SearchView;

import java.util.List;

public class HotTopicAndStatusActivity extends BaseFragmentActivity implements HotSearchContract.View{

    SearchView mSearchView;
    HotSearchContract.Presenter mPresenter;
    HotSearchAdapter mAdapter;
    List<HotSearch> mHotSearches;

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_hot_topic;
    }

    @Override
    protected Fragment createFragment() {
        return new HotTopicAndStatusFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new HotSearchPresenter(this);
        initToolbar();
        initSearch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            mSearchView.handleSearch();
            loadHotSearchIfNeed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSearch() {
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.getSearchEditText().setHint(R.string.label_hot_topic_search);
        mSearchView.setMode(SearchView.MODE_ANIMATION, this);
        mSearchView.setSearchListener(new SearchView.onSearchListener() {
            @Override
            public void onSearch(String content) {
                if (!TextUtils.isEmpty(content.trim())) {
                    startActivity(TopicThemeActivity.getStartIntent(HotTopicAndStatusActivity.this, content));
                }
            }
        });
        mSearchView.getSearchResult().setLayoutManager(new LinearLayoutManager(this));
        mSearchView.getSearchResult().setAdapter(mAdapter = new HotSearchAdapter(this));
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                HotSearch hotSearch = mAdapter.getItem(position);
                mSearchView.setSearchContent(hotSearch.getContent());
                mSearchView.getSearchListener().onSearch(hotSearch.getContent());
            }
        });
    }

    private void loadHotSearchIfNeed(){
        if(mHotSearches != null){
            return;
        }
        mPresenter.loadHotSearch();
    }

    @Override
    public void onLoadHotSearchSuccess(List<HotSearch> hotSearches) {
        mHotSearches = hotSearches;
        mAdapter.refresh(mHotSearches);
    }

    @Override
    public void onTaskStart() {

    }

    @Override
    public void onTaskComplete(int taskState) {

    }
}
