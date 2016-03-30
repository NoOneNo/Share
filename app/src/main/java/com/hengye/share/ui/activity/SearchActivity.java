package com.hengye.share.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.hengye.share.adapter.recyclerview.TopicAdapter;
import com.hengye.share.model.Topic;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.TopicDraftHelper;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.SearchUserAdapter;
import com.hengye.share.ui.mvpview.SearchMvpView;
import com.hengye.share.ui.presenter.SearchPresenter;
import com.hengye.share.ui.widget.dialog.LoadingDialog;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements View.OnClickListener, SearchMvpView{

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
        return false;
    }

    @Override
    public void finish() {
        setHideAnimationOnFinish();
        super.finish();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_search;
    }

    public static Intent getStartIntent(Context context, String keywords) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("keywords", keywords);
        return intent;
    }

    private String mKeywords;

    @Override
    protected void handleBundleExtra() {
        super.handleBundleExtra();
        mKeywords = getIntent().getStringExtra("keywords");
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setupPresenter(mPresenter = new SearchPresenter(this));
        initView();

        if(!TextUtils.isEmpty(mKeywords)){
            mContent.setText(mKeywords);
            search();
            ViewUtil.hideKeyBoard(mContent);
        }
    }

    private EditText mContent;
    private RecyclerView mUserRV, mTopicRV;
    private LoadingDialog mLoadingDialog;

    private SearchUserAdapter mUserAdapter;
    private TopicAdapter mTopicAdapter;

    private SearchPresenter mPresenter;

    private void initView(){
        findViewById(R.id.card_search).setVisibility(View.VISIBLE);
        findViewById(R.id.iv_search_back).setOnClickListener(this);
        mContent = (EditText) findViewById(R.id.et_search_content);
        mUserRV = (RecyclerView) findViewById(R.id.recycler_view_user);
        mUserRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        mUserRV.setAdapter(mUserAdapter = new SearchUserAdapter(this, new ArrayList<UserInfo>()));
        mUserRV.setItemAnimator(new DefaultItemAnimator());
        mTopicRV = (RecyclerView) findViewById(R.id.recycler_view_topic);
        mTopicRV.setAdapter(mTopicAdapter = new TopicAdapter(this, new ArrayList<Topic>(), mTopicRV));
        mTopicRV.setLayoutManager(new LinearLayoutManager(this));
        mTopicRV.setItemAnimator(new DefaultItemAnimator());

        mUserAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(PersonalHomepageActivity.getStartIntent(SearchActivity.this, mUserAdapter.getItem(position)));
            }
        });

        mLoadingDialog = new LoadingDialog(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.iv_search_back){
            finish();
        }
    }

    private void search() {
        if(!canSearchContent()){
            ToastUtil.showToast("搜索内容不能为空");
            return;
        }

        ViewUtil.hideKeyBoard(mContent);
        mLoadingDialog.show();
        mPresenter.loadWBSearchContent(mContent.getText().toString().trim());
    }

    private boolean canSearchContent(){
        return !TextUtils.isEmpty(mContent.getText().toString());
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
