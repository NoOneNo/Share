package com.hengye.share.module.hottopic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hengye.share.R;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.base.BaseFragmentActivity;
import com.hengye.share.module.topic.TopicThemeActivity;
import com.hengye.share.ui.widget.SearchView;

public class HotTopicAndStatusActivity extends BaseFragmentActivity{

    SearchView mSearchView;

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
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSearch() {
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setMode(SearchView.MODE_ANIMATION, this);
        mSearchView.setSearchListener(new SearchView.onSearchListener() {
            @Override
            public void onSearch(String content) {
                if (!TextUtils.isEmpty(content.trim())) {
                    startActivity(TopicThemeActivity.getStartIntent(HotTopicAndStatusActivity.this, content));
                }
            }
        });
    }
}
