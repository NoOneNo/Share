package com.hengye.share.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.R;
import com.hengye.share.adapter.viewpager.TopicFragmentPager;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.ui.activity.setting.SettingActivity;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.fragment.TopicFavoritesFragment;
import com.hengye.share.ui.fragment.TopicFragment;
import com.hengye.share.ui.mvpview.UserMvpView;
import com.hengye.share.ui.presenter.TopicPresenter;
import com.hengye.share.ui.presenter.UserPresenter;
import com.hengye.share.ui.support.actionbar.ActionBarDrawerToggleCustom;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class TopicActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, UserMvpView {

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    protected boolean canSwipeBack() {
        return false;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_topic;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);
        setupPresenter(mPresenter = new UserPresenter(this));
        initView();
    }

    private ViewPager mViewPager;
    private TabLayout mTablayout;
    private NetworkImageViewPlus mAvatar;
    private TextView mUsername, mSign;

    private TopicFragmentPager mTopicFragmentAdapter;

    private UserPresenter mPresenter;


    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTablayout = (TabLayout) findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mViewPager.setAdapter(mTopicFragmentAdapter = new TopicFragmentPager(getSupportFragmentManager(), this, getTopicGroups()));
        adjustTabLayout();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        //修复因为布局使用fitsSystemWindows而导致DrawLayout内容间距不对的问题
        RelativeLayout navHeader = (RelativeLayout) navigationView.findViewById(R.id.rl_header);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams)navHeader.getLayoutParams();
        lp.topMargin = ViewUtil.getStatusBarHeight(this);
        navHeader.setLayoutParams(lp);

        ImageButton moreAccount = (ImageButton) navigationView.findViewById(R.id.iv_more_account);
        SelectorLoader.getInstance().setImageSelector(this, moreAccount, R.drawable.compose_more_account_add, R.drawable.compose_more_account_add_highlighted);
        moreAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(AccountManageActivity.class, AccountManageActivity.ACCOUNT_CHANGE);
            }
        });

        mAvatar = (NetworkImageViewPlus) navigationView.findViewById(R.id.iv_avatar);
        mUsername = (TextView) navigationView.findViewById(R.id.tv_username);
        mSign = (TextView) navigationView.findViewById(R.id.tv_sign);

        if (UserUtil.isUserNameEmpty()) {
            mPresenter.loadWBUserInfo();
        } else {
            loadSuccess(UserUtil.getCurrentUser());
        }

    }

    private void adjustTabLayout() {
        mTablayout.setTabMode(mViewPager.getAdapter().getCount() > 3 ? TabLayout.MODE_SCROLLABLE : TabLayout.MODE_FIXED);
        mTablayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            moveTaskToBack(false);
//            super.onBackPressed();
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
            startActivityForResult(AccountManageActivity.class, AccountManageActivity.ACCOUNT_CHANGE);
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

        if (id == R.id.nav_homepage) {
//            UserInfo userInfo = getUserInfo();
            if (UserUtil.getCurrentUser() == null) {
                Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                return true;
            }
            startActivity(PersonalHomepageActivity.getStartIntent(this, UserInfo.getUserInfo(UserUtil.getCurrentUser())));
        } else if (id == R.id.nav_at_me) {
            startActivity(TopicMentionActivity.class);
        } else if (id == R.id.nav_comment) {
            startActivity(TopicCommentActivity.class);
        } else if (id == R.id.nav_favorites) {
            startActivity(FragmentActivity.getStartIntent(this, TopicFavoritesFragment.class));
        } else if (id == R.id.nav_setting) {
            startActivity(SettingActivity.class);
        } else if (id == R.id.nav_group_manage) {
//            startActivity(GroupManageActivity.class);
            startActivityForResult(GroupManageActivity.class, GroupManageActivity.GROUP_UPDATE);
        } else if (id == R.id.nav_send) {
            startActivity(TopicDraftActivity.class);
        } else if (id == R.id.nav_share) {
            startActivity(TestActivity.class);
        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.END);
        return true;
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
            loadSuccess(UserUtil.getCurrentUser());
        }
    }

    private List<TopicPresenter.TopicGroup> getTopicGroups() {
        ArrayList<TopicPresenter.TopicGroup> topicGroupGroups = new ArrayList<>();
        topicGroupGroups.add(new TopicPresenter.TopicGroup(TopicPresenter.TopicType.ALL));
        topicGroupGroups.add(new TopicPresenter.TopicGroup(TopicPresenter.TopicType.BILATERAL));
        List<TopicPresenter.TopicGroup> temp = TopicPresenter.TopicGroup.getTopicGroup();
        if (!CommonUtil.isEmptyCollection(temp)) {
            topicGroupGroups.addAll(temp);
        }

        return topicGroupGroups;
    }

    @Override
    public void loadSuccess(User user) {
        if(user != null) {
            mAvatar.setImageUrl(user.getAvatar(), RequestManager.getImageLoader());
            mUsername.setText(user.getName());
            mSign.setText(user.getSign());
        }else{
            mAvatar.setImageResource(R.drawable.ic_user_avatar);
            mUsername.setText("");
            mSign.setText("");
        }
    }

    @Override
    public void handleUserInfo(WBUserInfo wbUserInfo) {

    }

    @Override
    public void loadFail() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AccountManageActivity.ACCOUNT_CHANGE && resultCode == Activity.RESULT_OK) {
            loadSuccess(UserUtil.getCurrentUser());
            if(mViewPager != null) {
                mTopicFragmentAdapter.refresh(getTopicGroups());

                //全部微博
                String str1 = mTopicFragmentAdapter.getFragmentTags().get(0);
                //互相关注
                String str2 = mTopicFragmentAdapter.getFragmentTags().get(1);

                Fragment fragment1 = getSupportFragmentManager().findFragmentByTag(str1);
                Fragment fragment2 = getSupportFragmentManager().findFragmentByTag(str2);

                if(fragment1 != null && fragment1 instanceof TopicFragment){
                    TopicFragment topicFragment = (TopicFragment) fragment1;
                    topicFragment.refresh();
                }

                if(fragment2 != null && fragment2 instanceof TopicFragment){
                    TopicFragment topicFragment = (TopicFragment) fragment2;
                    topicFragment.refresh();
                }
//                mViewPager.setAdapter(mTopicFragmentAdapter = new TopicFragmentPager(getSupportFragmentManager(), this, getTopicGroups()));
                adjustTabLayout();
            }
        } else if (requestCode == GroupManageActivity.GROUP_UPDATE && resultCode == Activity.RESULT_OK) {
            updateViewPager();
        }
    }

    private void updateViewPager() {
        if (mViewPager != null) {
            mTopicFragmentAdapter.refresh(getTopicGroups());
            adjustTabLayout();
        }
    }

}
