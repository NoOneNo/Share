package com.hengye.share.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.view.NetworkImageView;
import com.google.gson.reflect.TypeToken;
import com.hengye.share.R;
import com.hengye.share.adapter.viewpager.TopicFragmentPager;
import com.hengye.share.model.Topic;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.ui.activity.setting.SettingActivity;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.fragment.TopicFavoritesFragment;
import com.hengye.share.ui.mvpview.UserMvpView;
import com.hengye.share.ui.presenter.TopicPresenter;
import com.hengye.share.ui.presenter.UserPresenter;
import com.hengye.share.ui.support.ActionBarDrawerToggleCustom;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.thirdparty.ParseTokenWeiboAuthListener;
import com.hengye.share.util.thirdparty.ThirdPartyUtils;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import java.util.ArrayList;
import java.util.List;

public class TopicActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, UserMvpView {

    @Override
    protected boolean setCustomTheme() {
        return false;
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
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

//        getWindow().setFeatureInt(Window.feature_);
        setContentView(R.layout.activity_topic);
        setupPresenter(mPresenter = new UserPresenter(this));
        initView();
        initData();

    }

    private ViewPager mViewPager;
    private NetworkImageView mAvatar;
    private TextView mUsername, mSign;
    private UserPresenter mPresenter;


    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final TabLayout tablayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setAdapter(new TopicFragmentPager(getSupportFragmentManager(), this, getTopicGroups()));
        tablayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startActivity(TopicPublishActivity.class);
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
        mAvatar = (NetworkImageView) navigationView.findViewById(R.id.iv_avatar);
        mUsername = (TextView) navigationView.findViewById(R.id.tv_username);
        mSign = (TextView) navigationView.findViewById(R.id.tv_sign);

        if (UserUtil.isUserNameEmpty()) {
            mPresenter.loadWBUserInfo();
        } else {
            handleUserInfo(UserUtil.getCurrentUser());
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
        if (id == R.id.action_login) {
            startActivity(LoginActivity.class);
        } else if (id == R.id.action_login_by_third) {
//            if (isExistWeibo){
            try {
                mSsoHandler.authorize(ThirdPartyUtils.REQUEST_CODE_FOR_WEIBO, new WBAuthListener(), null);
            } catch (Exception e) {
                e.printStackTrace();
            }
//            }else{
//                mWeiboAuth.anthorize(new WBAuthListener());
//            }
        } else if (id == R.id.action_search) {
            startActivity(SearchActivity.class);
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.set_token) {
            startActivity(SetTokenActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
//            UserInfo userInfo = getUserInfo();
            if (UserUtil.getCurrentUser() == null) {
                Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                return true;
            }
            startActivity(PersonalHomepageActivity.getIntentToStart(this, UserInfo.getUserInfo(UserUtil.getCurrentUser())));
        } else if (id == R.id.nav_at_me) {
            startActivity(TopicMentionActivity.class);
        } else if (id == R.id.nav_comment) {
            startActivity(TopicCommentActivity.class);
        }else if (id == R.id.nav_slideshow) {
            startActivity(FragmentActivity.getStartIntent(this, TopicFavoritesFragment.class));
        } else if (id == R.id.nav_manage) {
            startActivity(SettingActivity.class);
        } else if (id == R.id.nav_share) {
            startActivity(TestActivity.class);
        } else if (id == R.id.nav_send) {
            startActivity(TopicDraftActivity.class);
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

    private ArrayList<Topic> getData() {

        ArrayList<Topic> data = SPUtil.getModule(new TypeToken<ArrayList<Topic>>() {
        }.getType(), Topic.class.getSimpleName() + UserUtil.getUid());
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    private void initData() {
        if (mWeiboAuth == null) {
            mWeiboAuth = ThirdPartyUtils.getWeiboData(TopicActivity.this);
        }
        if (mSsoHandler == null) {
            mSsoHandler = new SsoHandler(TopicActivity.this, mWeiboAuth);
        }
    }

    @Override
    public void handleUserInfo(User user) {
        mAvatar.setImageUrl(user.getAvatar(), RequestManager.getImageLoader());
        mUsername.setText(user.getName());
        mSign.setText(user.getSign());
    }

    private void updateNavigationView() {

        if (UserUtil.getCurrentUser() == null) {
            return;
        }
        String uid = mUsername.getText().toString();
        if (!TextUtils.isEmpty(uid) && uid.equals(UserUtil.getUid())) {
            //此ID数据已经更新过
            L.debug("updateNavigationView invoke, UserInfo has updated");
        } else if (UserUtil.isUserNameEmpty()) {
            mPresenter.loadWBUserInfo();
        } else {
            handleUserInfo(UserUtil.getCurrentUser());
        }
    }

    private List<TopicPresenter.TopicGroup> getTopicGroups(){
        ArrayList<TopicPresenter.TopicGroup> topicGroupGroups = new ArrayList<>();
        topicGroupGroups.add(TopicPresenter.TopicGroup.ALL);
        topicGroupGroups.add(TopicPresenter.TopicGroup.BILATERAL);
        return topicGroupGroups;
    }

    @Override
    public void handleUserInfo(WBUserInfo wbUserInfo) {

    }

    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadFail() {

    }

    /**
     * 微博 Web 授权类，提供登陆等功能
     */
    private WeiboAuth mWeiboAuth;

    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;

    class WBAuthListener extends ParseTokenWeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            super.onComplete(values);
            if (mAccessToken != null && mAccessToken.isSessionValid()) {
                UserUtil.updateUser(mAccessToken);
                mPresenter.loadWBUserInfo();
//                mPullToRefreshLayout.setRefreshing(true);
//                RequestManager.addToRequestQueue(getWBTopicRequest(mAccessToken.getToken(), 0 + "", true));
            }
        }
    }

}
