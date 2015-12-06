package com.hengye.share.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.android.volley.view.NetworkImageView;
import com.google.gson.reflect.TypeToken;
import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.adapter.RecyclerViewTopicAdapter;
import com.hengye.share.module.Topic;
import com.hengye.share.module.UserInfo;
import com.hengye.share.module.sina.WBTopic;
import com.hengye.share.module.sina.WBTopicIds;
import com.hengye.share.module.sina.WBTopics;
import com.hengye.share.module.sina.WBUserInfo;
import com.hengye.share.module.sina.WBUtil;
import com.hengye.share.support.ActionBarDrawerToggleCustom;
import com.hengye.share.ui.activity.setting.SettingActivity;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestFactory;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.ViewUtil;
import com.hengye.share.util.thirdparty.SaveUserInfoWeiboAuthListener;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.hengye.volleyplus.toolbox.RequestManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected String getRequestTag() {
        return "MainActivity";
    }

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initView();

        initData();

    }

    private PullToRefreshLayout mPullToRefreshLayout;
    private RecyclerViewTopicAdapter mAdapter;

    private Oauth2AccessToken mWBAccessToken;

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TabLayout tablayout = (TabLayout) findViewById(R.id.tablayout);
        tablayout.addTab((tablayout.newTab().setText("tab one")));
        tablayout.addTab((tablayout.newTab().setText("tab two")));
        tablayout.addTab((tablayout.newTab().setText("tab three")));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggleCustom toggle = new ActionBarDrawerToggleCustom(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setGravityCompat(GravityCompat.END);
        drawer.setDrawerListener(toggle);
        toggle.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                updateNavigationView();
            }
        });

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        updateNavigationView();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new RecyclerViewTopicAdapter(this, getDatas()));
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

                if (!CommonUtil.isEmptyCollection(mAdapter.getData())) {
                    String id = mAdapter.getData().get(0).getId();
                    RequestManager.addToRequestQueue(getWBTopicIdsRequest(mWBAccessToken.getToken(), id), getRequestTag());
                }else{
                    RequestManager.addToRequestQueue(getWBTopicRequest(mWBAccessToken.getToken(), 0 + "", true), getRequestTag());
                }
            }
        });
        mPullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                if (!CommonUtil.isEmptyCollection(mAdapter.getData())) {
                    String id = CommonUtil.getLastItem(mAdapter.getData()).getId();
                    RequestManager.addToRequestQueue(getWBTopicRequest(mWBAccessToken.getToken(), id, false), getRequestTag());
                } else {
                    mPullToRefreshLayout.setLoading(false);
                    mPullToRefreshLayout.setLoadEnable(false);
                }
            }
        });

        mAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(MainActivity.this, "click item : " + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_login_by_third) {
//            if (isExistWeibo){
            try {
                mSsoHandler.authorize(ThirdPartyUtils.REQUEST_CODE_FOR_WEIBO, new WBAuthListener(), (String) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            }else{
//                mWeiboAuth.anthorize(new WBAuthListener());
//            }
        } else if (id == R.id.action_test) {
//            Intent intent = new Intent(this, TestActivity.class);
            Intent intent = new Intent(this, PersonalHomepageActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camara) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            IntentUtil.startActivity(this, SettingActivity.class);
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ThirdPartyUtils.REQUEST_CODE_FOR_WEIBO && mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private ArrayList<Topic> getDatas() {

        ArrayList<Topic> datas = SPUtil.getModule(new TypeToken<ArrayList<Topic>>() {
        }.getType(), Topic.class.getSimpleName());
//        ArrayList<Topic> datas = null;
//        if (wbTopic != null) {
//            datas = Topic.getTopics(wbTopic);
//        }
        if (datas == null) {
            datas = new ArrayList<>();
        }
        return datas;
    }

    private void initData() {
        if (mWeiboAuth == null) {
            mWeiboAuth = ThirdPartyUtils.getWeiboData(MainActivity.this);
        }
        if (mSsoHandler == null) {
            mSsoHandler = new SsoHandler(MainActivity.this, mWeiboAuth);
        }
    }

    private void updateNavigationView() {
        WBUserInfo wbUserInfo = SPUtil.getModule(WBUserInfo.class, WBUserInfo.class.getSimpleName());
        if (wbUserInfo == null) {
            //用户数据为空
            L.debug("updateNavigationView invoke, UserInfo is null");

            if(mWBAccessToken != null){
                RequestManager.addToRequestQueue(RequestFactory.getInstance().
                        getWBUserInfoRequest(mWBAccessToken.getToken(), mWBAccessToken.getUid()), getRequestTag());
            }
            return;
        }

        UserInfo userInfo = UserInfo.getUserInfo(wbUserInfo);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        String uid = (String) navigationView.getTag();
        if (!TextUtils.isEmpty(uid)){
            if(uid.equals(userInfo.getUid())){
                //此ID数据已经更新过
                L.debug("updateNavigationView invoke, UserInfo has updated");
                return;
            }else{
                if(mWBAccessToken != null){
                    RequestManager.addToRequestQueue(RequestFactory.getInstance().
                            getWBUserInfoRequest(mWBAccessToken.getToken(), mWBAccessToken.getUid()));
                }
            }
        }

        L.debug("updateNavigationView invoke, UserInfo has not updated");
        navigationView.setTag(userInfo.getUid());
        NetworkImageView avatar = (NetworkImageView) navigationView.findViewById(R.id.iv_avatar);
        TextView name = (TextView) navigationView.findViewById(R.id.tv_username);
        TextView sign = (TextView) navigationView.findViewById(R.id.tv_sign);

        avatar.setImageUrl(userInfo.getAvatar(), RequestManager.getImageLoader());
        name.setText(userInfo.getName());
        sign.setText(userInfo.getSign());
    }


    private GsonRequest<WBTopics> getWBTopicRequest(String token, String id, final boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBFriendTopicUrl());
        ub.addParameter("access_token", token);
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
                List<Topic> datas = Topic.getTopics(response);
                if (isRefresh) {
                    //下拉刷新
                    mPullToRefreshLayout.setRefreshing(false);
                    if (!CommonUtil.isEmptyCollection(mAdapter.getData())) {
                        //微博属于刷新
                        if (CommonUtil.isEmptyCollection(datas)) {
                            //没有内容更新
                            Snackbar.make(mPullToRefreshLayout, "没有新的微博", Snackbar.LENGTH_SHORT).show();
                            return;
                        } else if (datas.size() < WBUtil.MAX_COUNT_REQUEST) {
                            //结果小于请求条数
                            mAdapter.addAll(0, datas);
                            Snackbar.make(mPullToRefreshLayout, datas.size() + "条新微博", Snackbar.LENGTH_SHORT).show();
                        } else {
                            //结果大于或等于请求条数
                            mPullToRefreshLayout.setLoadEnable(true);
                            mAdapter.refresh(datas);
                            Snackbar.make(mPullToRefreshLayout, "超过" + WBUtil.MAX_COUNT_REQUEST + "条新微博", Snackbar.LENGTH_SHORT).show();
                        }
                    } else {
                        //属于第一次加载
                        if (CommonUtil.isEmptyCollection(datas)) {
                            //内容为空
                            mPullToRefreshLayout.setLoadEnable(false);
                        }
                        mAdapter.refresh(datas);
                    }
                    //存储数据
                    SPUtil.setModule(mAdapter.getData(), Topic.class.getSimpleName());
                } else {
                    //上拉加载
                    mPullToRefreshLayout.setLoading(false);
                    if (CommonUtil.isEmptyCollection(datas)) {
                        //没有数据可供加载
                        mPullToRefreshLayout.setLoadEnable(false);
                        Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
                    } else {
                        //成功加载更多
//                        mPageNo++;
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

    private GsonRequest<WBTopicIds> getWBTopicIdsRequest(final String token, final String since_id) {
        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBFriendTopicIdsUrl());
        ub.addParameter("access_token", token);
        ub.addParameter("since_id", since_id);
        ub.addParameter("count", WBUtil.MAX_COUNT_REQUEST);
        return new GsonRequest<>(
                ub.getRequestUrl()
                , WBTopicIds.class,
                new Response.Listener<WBTopicIds>() {
                    @Override
                    public void onResponse(WBTopicIds response) {
                        L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
                        if (response == null || CommonUtil.isEmptyCollection(response.getStatuses())) {
                            //没有新的微博
                            mPullToRefreshLayout.setRefreshing(false);
                            Snackbar.make(mPullToRefreshLayout, "没有新的微博", Snackbar.LENGTH_SHORT).show();
                            L.debug("no topic update");
                        } else {
                            if (response.getStatuses().size() >= WBUtil.MAX_COUNT_REQUEST) {
                                //还有更新的微博，重新请求刷新
                                RequestManager.addToRequestQueue(getWBTopicRequest(token, 0 + "", true), getRequestTag());
                                L.debug("exist newer topic, request refresh again");
                            } else {
                                //新的微博条数没有超过请求条数，显示更新多少条微博，根据请求的since_id获取微博
                                RequestManager.addToRequestQueue(getWBTopicRequest(token, since_id, true), getRequestTag());
                                L.debug("new topic is less than MAX_COUNT_REQUEST, request refresh by since_id");
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mPullToRefreshLayout.setRefreshing(false);
                        L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), error);
                    }

                });
    }

    /**
     * 微博 Web 授权类，提供登陆等功能
     */
    private WeiboAuth mWeiboAuth;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    class WBAuthListener extends SaveUserInfoWeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            super.onComplete(values);
            if (mAccessToken != null && mAccessToken.isSessionValid()) {
                MainActivity.this.mWBAccessToken = mAccessToken;
                mPullToRefreshLayout.setRefreshing(true);
//                RequestManager.addToRequestQueue(getWBTopicRequest(mAccessToken.getToken(), 0 + "", true));
            }
        }
    }
}
