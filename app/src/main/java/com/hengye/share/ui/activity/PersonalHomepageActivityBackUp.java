package com.hengye.share.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.TopicAdapter;
import com.hengye.share.model.Parent;
import com.hengye.share.model.Topic;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.ArrayList;
import java.util.List;

public class PersonalHomepageActivityBackUp extends BaseActivity implements View.OnClickListener{

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

        if(mUserInfo == null) {
            Uri data = getIntent().getData();
            if (data != null) {
                String value = data.toString();
                int index = value.lastIndexOf("@");
                String newValue = value.substring(index + 1);
                mUserInfo = new UserInfo();
                mUserInfo.setName(newValue);
                mUserInfo.setParent(new Parent(null, Parent.TYPE_WEIBO));
            }
        }
    }

    public static Intent getIntentToStart(Context context, UserInfo userInfo){
        Intent intent = new Intent(context, PersonalHomepageActivityBackUp.class);
        intent.putExtra(UserInfo.class.getSimpleName(), userInfo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_homepage);

        if(mUserInfo == null){
            PersonalHomepageActivityBackUp.this.finish();
        }else {
            initData();
            initView();
        }
    }

    private UserInfo mUserInfo;
//    private PullToRefreshLayout mPullToRefreshLayout;
    private RecyclerView mRecyclerView;
    private TopicAdapter mAdapter;

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mUserInfo.getName());

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new TopicAdapter(this, new ArrayList<Topic>()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        NetworkImageViewPlus cover = (NetworkImageViewPlus) findViewById(R.id.iv_cover);
        cover.setImageUrl(mUserInfo.getCover(), RequestManager.getImageLoader());
        NetworkImageViewPlus avatar = (NetworkImageViewPlus) findViewById(R.id.iv_avatar);
        avatar.setImageUrl(mUserInfo.getAvatar(), RequestManager.getImageLoader());

        if(mUserInfo.getParent().isWeiBo()) {
            WBUserInfo wbUserInfo = mUserInfo.getWBUserInfoFromParent();
            if (wbUserInfo != null) {
                TextView division = (TextView) findViewById(R.id.tv_division);
                division.setVisibility(View.VISIBLE);
                TextView attention = (TextView) findViewById(R.id.tv_attention);
                attention.setText(String.format(getString(R.string.label_attention), wbUserInfo.getFriends_count()));
                TextView fans = (TextView) findViewById(R.id.tv_fans);
                fans.setText(String.format(getString(R.string.label_fans), wbUserInfo.getFollowers_count()));
                TextView sign = (TextView) findViewById(R.id.tv_sign);
                sign.setText(wbUserInfo.getDescription());
            }
        }

        loadData();
    }

    private void initData(){
    }

    private boolean loadData(){
        if(mUserInfo == null || UserUtil.isUserEmpty()){
            return false;
        }
        if(mUserInfo.getParent().isWeiBo()){
            L.debug("userInfo : {}", mUserInfo.getParent().getJson());

            if(!TextUtils.isEmpty(mUserInfo.getUid())){
                RequestManager.addToRequestQueue(getWBTopicRequest(UserUtil.getToken(), mUserInfo.getUid(), null, 0 + "", true), getRequestTag());
                return true;
            }else if(!TextUtils.isEmpty(mUserInfo.getName())){
                RequestManager.addToRequestQueue(getWBTopicRequest(UserUtil.getToken(), null, mUserInfo.getName(), 0 + "", true), getRequestTag());
                return true;
            }
//            if(mUserInfo.getParent().getJson() != null) {
//                WBUserInfo wbUserInfo = mUserInfo.getWBUserInfoFromParent();
//                if (wbUserInfo != null) {
//                    RequestManager.addToRequestQueue(getWBTopicRequest(UserUtil.getToken(), wbUserInfo.getIdstr(), 0 + "", true), getRequestTag());
//                    return true;
//                }
//            }else{
//                L.debug("only find user name");
//            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
    }


    private GsonRequest<WBTopics> getWBTopicRequest(String token, String uid, String name, String id, final boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBUserTopicUrl());
        ub.addParameter("access_token", token);
        if(!TextUtils.isEmpty(uid)){
            ub.addParameter("uid", uid);
        }else if(!TextUtils.isEmpty(name)){
            ub.addParameter("screen_name", name);
        }
        if (isRefresh) {
            ub.addParameter("since_id", id);
        } else {
            ub.addParameter("max_id", id);
        }
        ub.addParameter("count", WBUtil.MAX_COUNT_REQUEST);
        return new GsonRequest<>(
                WBTopics.class,
                ub.getRequestUrl(),
                new Response.Listener<WBTopics>() {
            @Override
            public void onResponse(WBTopics response) {
                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
                List<Topic> data = Topic.getTopics(response);
                if (isRefresh) {
                    //下拉刷新
//                    mPullToRefreshLayout.setRefreshing(false);
                    if (!CommonUtil.isEmptyCollection(mAdapter.getData())) {
                        //微博属于刷新
                        if (CommonUtil.isEmptyCollection(data)) {
                            //没有内容更新
                            Snackbar.make(mRecyclerView, "没有新的微博", Snackbar.LENGTH_SHORT).show();
                            return;
                        } else if (data.size() < WBUtil.MAX_COUNT_REQUEST) {
                            //结果小于请求条数
                            mAdapter.addAll(0, data);
                            Snackbar.make(mRecyclerView, data.size() + "条新微博", Snackbar.LENGTH_SHORT).show();
                        } else {
                            //结果大于或等于请求条数
//                            mPullToRefreshLayout.setLoadEnable(true);
                            mAdapter.refresh(data);
                            Snackbar.make(mRecyclerView, "超过" + WBUtil.MAX_COUNT_REQUEST + "条新微博", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        //属于第一次加载
                        if (CommonUtil.isEmptyCollection(data)) {
                            //内容为空
//                            mPullToRefreshLayout.setLoadEnable(false);
                        }
                        mAdapter.refresh(data);
                    }
                    //存储数据
//                    SPUtil.setModule(mAdapter.getData(), Topic.class.getSimpleName());
                } else {
                    //上拉加载
//                    mPullToRefreshLayout.setLoading(false);
                    if (CommonUtil.isEmptyCollection(data)) {
                        //没有数据可供加载
//                        mPullToRefreshLayout.setLoadEnable(false);
                        Snackbar.make(mRecyclerView, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
                    } else {
                        //成功加载更多
//                        mPageNo++;
                        if (data.size() < WBUtil.MAX_COUNT_REQUEST) {
                            //没有更多的数据可供加载
//                            mPullToRefreshLayout.setLoadEnable(false);
                            Snackbar.make(mRecyclerView, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
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
//                    mPullToRefreshLayout.setRefreshing(false);
                } else {
//                    mPullToRefreshLayout.setLoading(false);
                }
                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), error);
            }

        });
    }
}
