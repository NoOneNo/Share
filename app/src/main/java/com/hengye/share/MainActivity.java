package com.hengye.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.android.volley.request.StringRequest;
import com.hengye.share.adapter.RecyclerViewMainAdapter;
import com.hengye.share.module.sina.Topic;
import com.hengye.share.support.ActionBarDrawerToggleCustom;
import com.hengye.share.util.CustomWeiboAuthListener;
import com.hengye.share.util.L;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.ThirdPartyUtils;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.volleyplus.toolbox.RequestManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import java.util.ArrayList;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected String getRequestTag() {
        return "MainActivity";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        initData();

    }

    private RecyclerViewMainAdapter mAdapter;

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
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_main);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setAdapter(mAdapter = new RecyclerViewMainAdapter(this, getDatas()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private ArrayList<String> getDatas() {
        ArrayList<String> datas = new ArrayList<String>();
        for (int i = 'A'; i <= 'z'; i++) {
            datas.add((char) i + "");
        }
        return datas;
    }

    private void initData(){
        if(mWeiboAuth == null){
            mWeiboAuth = ThirdPartyUtils.getWeiboData(MainActivity.this);
        }
        if(mSsoHandler == null){
            mSsoHandler = new SsoHandler(MainActivity.this, mWeiboAuth);
        }
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
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        } else if(id == R.id.action_login_by_third){
//            if (isExistWeibo){
                try {
                    mSsoHandler.authorize(ThirdPartyUtils.REQUEST_CODE_FOR_WEIBO, new WBAuthListener(), (String) null);
                }catch(Exception e){
                    e.printStackTrace();
                }
//            }else{
//                mWeiboAuth.anthorize(new WBAuthListener());
//            }
        }else if (id == R.id.action_add) {
            mAdapter.addData(1, "add one");
        } else if (id == R.id.action_remove) {
            mAdapter.removeData(1);
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

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ThirdPartyUtils.REQUEST_CODE_FOR_WEIBO && mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

//    private StringRequest getRequest(String url) {
//        return new StringRequest(
//                url
//                ,  new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                L.debug("request success : {}", response);
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                L.debug("request fail : {}", error);
//            }
//
//        });
//    }
    private GsonRequest<Topic> getRequest(String url) {
        return new GsonRequest<>(
                url
                , Topic.class
                , new Response.Listener<Topic>() {
            @Override
            public void onResponse(Topic response) {
               L.debug("request success : {}", response);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                L.debug("request fail : {}", error);
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

    class WBAuthListener extends CustomWeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            super.onComplete(values);
            if (mAccessToken.isSessionValid()) {
                UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBFriendTopicUrl());
                ub.addParameter("access_token", mAccessToken.getToken());
                ub.addParameter("count", 2);
                RequestManager.addToRequestQueue(getRequest(ub.toString()));
            }
        }
    }
}
