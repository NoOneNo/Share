package com.hengye.share.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.adapter.RecyclerViewCommentAdapter;
import com.hengye.share.adapter.RecyclerViewTopicAdapter;
import com.hengye.share.module.Topic;
import com.hengye.share.module.TopicComment;
import com.hengye.share.module.sina.WBTopicComment;
import com.hengye.share.module.sina.WBTopicComments;
import com.hengye.share.module.sina.WBTopics;
import com.hengye.share.module.sina.WBUtil;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.ViewUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.hengye.volleyplus.toolbox.RequestManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.ArrayList;
import java.util.List;

public class TopicDetailActivity extends BaseActivity {

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

    public static Intent getIntentToStart(Context context, Topic topic) {
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

        initView();


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private PullToRefreshLayout mPullToRefreshLayout;
    private RecyclerViewCommentAdapter mAdapter;

    private Oauth2AccessToken mWBAccessToken;

    private void initView() {
        if (mTopic == null) {
            return;
        }


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new RecyclerViewCommentAdapter(this, new ArrayList<TopicComment>(), mTopic));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mWBAccessToken = SPUtil.getSinaAccessToken();
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mWBAccessToken == null || TextUtils.isEmpty(mWBAccessToken.getToken())) {
                    mPullToRefreshLayout.setRefreshing(false);
                    return;
                }

                RequestManager.addToRequestQueue(getWBCommentRequest(mWBAccessToken.getToken(), mTopic.getId(), "0", true), getRequestTag());
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
                    String id = CommonUtil.getLastItem(mAdapter.getData()).getId();
                    RequestManager.addToRequestQueue(getWBCommentRequest(mWBAccessToken.getToken(), mTopic.getId(), id, false), getRequestTag());
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

    private GsonRequest getWBCommentRequest(String token, String topicId, String id, final boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBCommentUrl());
        ub.addParameter("access_token", token);
        ub.addParameter("id", topicId);
        if (isRefresh) {
            ub.addParameter("since_id", id);
        } else {
            ub.addParameter("max_id", id);
        }
        ub.addParameter("count", WBUtil.MAX_COUNT_REQUEST);
        return new GsonRequest<>(
                ub.getRequestUrl()
                , WBTopicComments.class
                , new Response.Listener<WBTopicComments>() {
            @Override
            public void onResponse(WBTopicComments response) {
                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
                List<TopicComment> datas = TopicComment.getComments(response);
                if (isRefresh) {
                    //下拉刷新
                    mPullToRefreshLayout.setRefreshing(false);
                    if (!CommonUtil.isEmptyCollection(mAdapter.getData())) {
                        //微博属于刷新
                        if (CommonUtil.isEmptyCollection(datas)) {
                            //没有内容更新
                            Snackbar.make(mPullToRefreshLayout, "暂时没有内容", Snackbar.LENGTH_SHORT).show();
                            return;
                        } else if (datas.size() < WBUtil.MAX_COUNT_REQUEST) {
                            //结果小于请求条数
                            mAdapter.addAll(0, datas);
//                            Snackbar.make(mPullToRefreshLayout, datas.size() + "条新微博", Snackbar.LENGTH_SHORT).show();
                        } else {
                            //结果大于或等于请求条数
                            mPullToRefreshLayout.setLoadEnable(true);
                            mAdapter.refresh(datas);
//                            Snackbar.make(mPullToRefreshLayout, "超过" + WBUtil.MAX_COUNT_REQUEST + "条新微博", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        //属于第一次加载
                        if (CommonUtil.isEmptyCollection(datas)) {
                            //内容为空
                            mPullToRefreshLayout.setLoadEnable(false);
                        }else if (datas.size() < WBUtil.MAX_COUNT_REQUEST) {
                            //结果小于请求条数
                            mPullToRefreshLayout.setLoadEnable(false);
                        }else{
                            mPullToRefreshLayout.setLoadEnable(true);
                        }
                        mAdapter.refresh(datas);
                    }
                    //存储数据
//                    SPUtil.setModule(mAdapter.getData(), TopicComment.class.getSimpleName());
                } else {
                    //上拉加载
                    mPullToRefreshLayout.setLoading(false);
                    if (CommonUtil.isEmptyCollection(datas)) {
                        //没有数据可供加载
                        mPullToRefreshLayout.setLoadEnable(false);
                        Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
                    } else {
                        //成功加载更多
                        if (datas.size() < WBUtil.MAX_COUNT_REQUEST) {
                            //没有更多的数据可供加载
                            mPullToRefreshLayout.setLoadEnable(false);
                            Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
                        }
                        //因为请求的数据是小于或等于max_id，需要做是否重复判断处理
                        if (datas.get(0).getId() != null && datas.get(0).getId().
                                equals(CommonUtil.getLastItem(mAdapter.getData()).getId())) {
                            datas.remove(0);
                        }
                        mAdapter.addAll(datas);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
    }
}
