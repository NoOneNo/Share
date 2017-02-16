package com.hengye.share.module.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.hengye.share.module.setting.SettingFragment;
import com.hengye.share.module.topic.TopicAdapter;
import com.hengye.share.model.Topic;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.profile.PersonalHomepageActivity;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.ui.widget.SearchView;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.ui.widget.dialog.LoadingDialog;
import com.hengye.share.util.ActivityUtils;
import com.hengye.share.util.L;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity {

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
        return false;
    }

    @Override
    protected boolean canSwipeBack() {
        return true;
    }

    @Override
    protected boolean setFinishPendingTransition() {
        return true;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_search;
    }

    public static Intent getStartIntent(Context context, String keywords) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("keywords", keywords);
        return intent;
    }

    private String mKeywords;
    private SearchView mSearchView;
    private SearchFragment mSearchFragment;

    @Override
    protected void handleBundleExtra(Intent intent) {
        super.handleBundleExtra(intent);
        mKeywords = intent.getStringExtra("keywords");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        if(!TextUtils.isEmpty(mKeywords)){
            mSearchView.setSearchContent(mKeywords);
            search(mKeywords);
        }
    }

    private void initView(){
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.getSearchEditText().setHint(R.string.label_search_user_and_status_hint);
        mSearchView.setMode(SearchView.MODE_FINISH, this);
        mSearchView.setSearchListener(new SearchView.onSearchListener() {
            @Override
            public void onSearch(String content) {
                search(content);
            }
        });

        mSearchFragment = (SearchFragment) getSupportFragmentManager().findFragmentById(R.id.search_content);
    }

    private void search(String content) {
        mSearchFragment.search(content);
    }

}
