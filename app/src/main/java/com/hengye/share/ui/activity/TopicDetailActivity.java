package com.hengye.share.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.adapter.RecyclerViewCommentAdapter;
import com.hengye.share.adapter.RecyclerViewTopicAdapter;
import com.hengye.share.module.Topic;
import com.hengye.share.module.UserInfo;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.ViewUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.hengye.volleyplus.toolbox.RequestManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.ArrayList;

public class TopicDetailActivity extends BaseActivity{

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

    public static Intent getIntentToStart(Context context, Topic topic){
        Intent intent = new Intent(context, TopicDetailActivity.class);
        intent.putExtra(Topic.class.getSimpleName(), topic);
        return intent;
    }

    Topic mTopic;
    @Override
    protected void handleBundleExtra() {
        mTopic = (Topic) getIntent().getSerializableExtra(Topic.class.getSimpleName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_topic_detail);

        initTopicView();

        initView();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initTopicView(){
        if(mTopic == null){
            return;
        }
        RecyclerViewTopicAdapter.MainViewHolder vh = new RecyclerViewTopicAdapter.MainViewHolder(findViewById(R.id.item_topic));
        vh.bindData(this, mTopic);
    }

    private PullToRefreshLayout mPullToRefreshLayout;
    private RecyclerViewCommentAdapter mAdapter;

    private Oauth2AccessToken mWBAccessToken;

    private void initView(){
        TabLayout tablayout = (TabLayout) findViewById(R.id.tablayout);
        tablayout.addTab((tablayout.newTab().setText("comment")));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new RecyclerViewCommentAdapter(this, new ArrayList<String>()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mWBAccessToken = SPUtil.getSinaAccessToken();
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mWBAccessToken == null || TextUtils.isEmpty(mWBAccessToken.getToken())){
                    mPullToRefreshLayout.setRefreshing(false);
                    return;
                }

//                if (!CommonUtil.isEmptyCollection(mAdapter.getData())) {
//                    String id = mAdapter.getData().get(0).getId();
//                    RequestManager.addToRequestQueue(getWBTopicIdsRequest(mWBAccessToken.getToken(), id), getRequestTag());
//                }else{
//                    RequestManager.addToRequestQueue(getWBTopicRequest(mWBAccessToken.getToken(), 0 + "", true), getRequestTag());
//                }
            }
        });
        mPullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                if (!CommonUtil.isEmptyCollection(mAdapter.getData())) {
//                    String id = CommonUtil.getLastItem(mAdapter.getData()).getId();
//                    RequestManager.addToRequestQueue(getWBTopicRequest(mWBAccessToken.getToken(), id, false), getRequestTag());
                } else {
                    mPullToRefreshLayout.setLoading(false);
                    mPullToRefreshLayout.setLoadEnable(false);
                }
            }
        });

        mAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(MainActivity.this, "click item : " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
