package com.hengye.share.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.hengye.share.adapter.recyclerview.TopicAdapter;
import com.hengye.share.model.Topic;
import com.hengye.share.model.UserInfo;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.SearchUserAdapter;
import com.hengye.share.ui.mvpview.SearchMvpView;
import com.hengye.share.ui.presenter.SearchPresenter;
import com.hengye.share.ui.view.SearchView;
import com.hengye.share.ui.view.listener.OnItemClickListener;
import com.hengye.share.ui.widget.dialog.LoadingDialog;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements SearchMvpView{

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
        return false;
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

    @Override
    protected void handleBundleExtra(Intent intent) {
        super.handleBundleExtra(intent);
        mKeywords = intent.getStringExtra("keywords");
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        addPresenter(mPresenter = new SearchPresenter(this));
        initView();

        if(!TextUtils.isEmpty(mKeywords)){
            mSearchView.setSearchContent(mKeywords);
            search(mKeywords);
        }
    }

    private SearchView mSearchView;
    private RecyclerView mUserRV, mTopicRV;
    private LoadingDialog mLoadingDialog;

    private SearchUserAdapter mUserAdapter;
    private TopicAdapter mTopicAdapter;

    private SearchPresenter mPresenter;

    private void initView(){
        mUserRV = (RecyclerView) findViewById(R.id.recycler_view_user);
        mUserRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mUserRV.setAdapter(mUserAdapter = new SearchUserAdapter(this, new ArrayList<UserInfo>()));
        mUserRV.setItemAnimator(new DefaultItemAnimator());
        mTopicRV = (RecyclerView) findViewById(R.id.recycler_view_topic);
        mTopicRV.setAdapter(mTopicAdapter = new TopicAdapter(this, new ArrayList<Topic>(), mTopicRV));
        mTopicRV.setLayoutManager(new LinearLayoutManager(this));
        mTopicRV.setItemAnimator(new DefaultItemAnimator());

        mUserAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                View avatar = view.findViewById(R.id.iv_avatar);
                PersonalHomepageActivity.start(SearchActivity.this, avatar, mUserAdapter.getItem(position));
            }
        });

        mLoadingDialog = new LoadingDialog(this);

        mSearchView = (SearchView) findViewById(R.id.search_view);

        mSearchView.setMode(SearchView.MODE_FINISH, this);

        mSearchView.setSearchListener(new SearchView.onSearchListener() {
            @Override
            public void onSearch(String content) {
                search(content);
            }
        });
    }

    private void search(String content) {
        if(!canSearchContent(content)){
            ToastUtil.showToast(R.string.label_search_content_is_null);
            return;
        }

        ViewUtil.hideKeyBoard(SearchActivity.this);
        mLoadingDialog.show();
        mPresenter.loadWBSearchContent(content.trim());
    }

    private boolean canSearchContent(String content){
        return !TextUtils.isEmpty(content);
    }

    @Override
    public void loadSuccess() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void loadFail() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void handleSearchUserData(List<UserInfo> userInfos) {
        mUserAdapter.refresh(userInfos);
    }

    @Override
    public void handleSearchPublicData(List<Topic> topics) {
        mTopicAdapter.refresh(topics);
    }


}
