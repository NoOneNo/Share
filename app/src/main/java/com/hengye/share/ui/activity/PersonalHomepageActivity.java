package com.hengye.share.ui.activity;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.adapter.RecyclerViewTopicAdapter;
import com.hengye.share.module.Topic;
import com.hengye.share.module.UserInfo;
import com.hengye.share.module.sina.WBTopics;
import com.hengye.share.module.sina.WBUserInfo;
import com.hengye.share.module.sina.WBUtil;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.hengye.volleyplus.toolbox.RequestManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.ArrayList;
import java.util.List;

public class PersonalHomepageActivity extends BaseActivity{

    @Override
    protected String getRequestTag() {
        return super.getRequestTag();
    }

    @Override
    protected boolean setCustomTheme() {
        return true;
    }

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    protected void handleBundleExtra() {
        mUserInfo = (UserInfo) getIntent().getSerializableExtra(UserInfo.class.getSimpleName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_homepage);

        initView();
        initData();

        loadBackdrop();
    }

    private UserInfo mUserInfo;
    private PullToRefreshLayout mPullToRefreshLayout;
    private RecyclerViewTopicAdapter mAdapter;
    private Oauth2AccessToken mWBAccessToken;

    private void initView() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle("Homepage");

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new RecyclerViewTopicAdapter(this, new ArrayList<Topic>()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);

        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(mUserInfo == null || mWBAccessToken == null || TextUtils.isEmpty(mWBAccessToken.getToken())){
                    mPullToRefreshLayout.setRefreshing(false);
                    return;
                }
                if(mUserInfo.getParent().isWeiBo()){
                    L.debug(mUserInfo.getParent().getTarget().toString());
                    WBUserInfo wbUserInfo = (WBUserInfo) mUserInfo.getParent().getTarget();
                    RequestManager.addToRequestQueue(getWBTopicRequest(mWBAccessToken.getToken(), wbUserInfo.getIdstr(), 0 + "", true), getRequestTag());
                }
            }
        });
        mPullToRefreshLayout.setRefreshing(true);
    }

    private void initData(){
        mWBAccessToken = SPUtil.getSinaAccessToken();
    }

    private void loadBackdrop() {
        final ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        imageView.setImageResource(R.drawable.cheese_1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }


    private GsonRequest<WBTopics> getWBTopicRequest(String token, String uid, String id, final boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBUserTopicUrl());
        ub.addParameter("access_token", token);
        ub.addParameter("uid", uid);
        if (isRefresh) {
            ub.addParameter("since_id", id);
        } else {
            ub.addParameter("max_id", id);
        }
        ub.addParameter("count", WBUtil.MAX_COUNT_REQUEST);
        return new GsonRequest<>(
                ub.getRequestUrl()
                , WBTopics.class
                , new Response.Listener<WBTopics>() {
            @Override
            public void onResponse(WBTopics response) {
                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
                List<Topic> data = Topic.getTopics(response);
                if (isRefresh) {
                    //下拉刷新
                    mPullToRefreshLayout.setRefreshing(false);
                    if (!CommonUtil.isEmptyCollection(mAdapter.getData())) {
                        //微博属于刷新
                        if (CommonUtil.isEmptyCollection(data)) {
                            //没有内容更新
                            Snackbar.make(mPullToRefreshLayout, "没有新的微博", Snackbar.LENGTH_SHORT).show();
                            return;
                        } else if (data.size() < WBUtil.MAX_COUNT_REQUEST) {
                            //结果小于请求条数
                            mAdapter.addAll(0, data);
                            Snackbar.make(mPullToRefreshLayout, data.size() + "条新微博", Snackbar.LENGTH_SHORT).show();
                        } else {
                            //结果大于或等于请求条数
                            mPullToRefreshLayout.setLoadEnable(true);
                            mAdapter.refresh(data);
                            Snackbar.make(mPullToRefreshLayout, "超过" + WBUtil.MAX_COUNT_REQUEST + "条新微博", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        //属于第一次加载
                        if (CommonUtil.isEmptyCollection(data)) {
                            //内容为空
                            mPullToRefreshLayout.setLoadEnable(false);
                        }
                        mAdapter.refresh(data);
                    }
                    //存储数据
                    SPUtil.setModule(mAdapter.getData(), Topic.class.getSimpleName());
                } else {
                    //上拉加载
                    mPullToRefreshLayout.setLoading(false);
                    if (CommonUtil.isEmptyCollection(data)) {
                        //没有数据可供加载
                        mPullToRefreshLayout.setLoadEnable(false);
                        Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
                    } else {
                        //成功加载更多
//                        mPageNo++;
                        if (data.size() < WBUtil.MAX_COUNT_REQUEST) {
                            //没有更多的数据可供加载
                            mPullToRefreshLayout.setLoadEnable(false);
                            Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
                        }
                        //因为请求的数据是小于或等于max_id，需要做是否重复判断处理
                        if (data.get(0).getId() != null && data.get(0).getId().
                                equals(CommonUtil.getLastItem(mAdapter.getData()).getId())) {
                            data.remove(0);
                        }
                        mAdapter.addAll(data);
                    }
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (isRefresh) {
                    mPullToRefreshLayout.setRefreshing(false);
                } else {
                    mPullToRefreshLayout.setLoading(false);
                }
                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), error);
            }

        });
    }
}
