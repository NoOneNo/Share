package com.hengye.share.ui.activity;

import android.animation.Animator;
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
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
import com.hengye.share.util.NetworkUtil;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.SettingHelper;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class TopicActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, UserMvpView, View.OnClickListener {

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    protected boolean canSwipeBack() {
        return false;
    }

    @Override
    protected boolean observeNetworkChange() {
        return true;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_topic;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setupPresenter(mPresenter = new UserPresenter(this));
        initView();
    }

    private ViewPager mViewPager;
    private TabLayout mTab;
    private NetworkImageViewPlus mAvatar;
    private TextView mUsername, mSign;
    private View mNoNetwork, mAppBar;

    private TopicFragmentPager mTopicFragmentAdapter;

    private UserPresenter mPresenter;


    private void initView() {
        mNoNetwork = findViewById(R.id.rl_no_network);
        mNoNetwork.setOnClickListener(this);

        mAppBar = findViewById(R.id.appbar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTab = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mViewPager.setAdapter(mTopicFragmentAdapter = new TopicFragmentPager(getSupportFragmentManager(), this, getTopicGroups()));
        adjustTabLayout();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

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

        initNavigationView();
        initSearch();

        if (UserUtil.isUserNameEmpty()) {
            mPresenter.loadWBUserInfo();
        } else {
            loadSuccess(UserUtil.getCurrentUser());
        }

    }

    private void initNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //修复因为布局使用fitsSystemWindows而导致DrawLayout内容间距不对的问题
        RelativeLayout navHeader = (RelativeLayout) navigationView.findViewById(R.id.rl_header);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) navHeader.getLayoutParams();
        lp.topMargin = ViewUtil.getStatusBarHeight();
        navHeader.setLayoutParams(lp);

        ImageButton moreAccount = (ImageButton) navigationView.findViewById(R.id.iv_more_account);
        SelectorLoader.getInstance().setImageSelector(this, moreAccount, R.drawable.compose_more_account_add, R.drawable.compose_more_account_add_highlighted);
        moreAccount.setOnClickListener(this);

        mAvatar = (NetworkImageViewPlus) navigationView.findViewById(R.id.iv_avatar);
        mUsername = (TextView) navigationView.findViewById(R.id.tv_username);
        mSign = (TextView) navigationView.findViewById(R.id.tv_sign);

        mUsername.setOnClickListener(this);
        mSign.setOnClickListener(this);
    }

    private CardView mSearch;
    private View mSearchLayout, mSearchBack, mSearchDividerLine;
    private EditText mSearchContent;
    private ListView mSearchResult;

    private void initSearch() {
        mSearch = (CardView) findViewById(R.id.card_search);

        mSearchLayout = findViewById(R.id.ll_search);
        mSearchBack = findViewById(R.id.iv_search_back);
        mSearchDividerLine = findViewById(R.id.divider_line);
        mSearchContent = (EditText) findViewById(R.id.et_search_content);
        mSearchResult = (ListView) findViewById(R.id.list_view);

        mSearchBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.rl_no_network) {
            NetworkUtil.startSystemSetting(TopicActivity.this);
        } else if (id == R.id.fab) {
            startActivity(TopicPublishActivity.class);
        } else if (id == R.id.iv_more_account) {
            startActivityForResult(AccountManageActivity.class, AccountManageActivity.ACCOUNT_CHANGE);
        } else if (id == R.id.tv_username || id == R.id.tv_sign) {
            String str = mUsername.getText().toString();
            if (str.contains("小小小鸡仔") || str.contains("Jayden")) {
                testClickCount++;
                if (testClickCount == 3) {
                    testClickCount = 0;
                    startActivity(TestActivity.class);
                }
            }
        } else if (id == R.id.iv_search_back) {
            handleSearch();
        }
    }

    private int testClickCount = 0;

    private void adjustTabLayout() {
        mTab.setTabMode(mViewPager.getAdapter().getCount() > 3 ? TabLayout.MODE_SCROLLABLE : TabLayout.MODE_FIXED);
        mTab.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            if (SettingHelper.isExitOnBackPressed()) {
                if (System.currentTimeMillis() - mBackPressedTime > 2000) {
                    mBackPressedTime = System.currentTimeMillis();
                    ToastUtil.showToast(R.string.tip_exit_on_back_pressed);
                } else {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(0);
                }
            } else {
                moveTaskToBack(false);
            }
        }
    }

    private long mBackPressedTime = 0;

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
        if (id == R.id.action_search) {
            handleSearch();
//            startActivity(SearchActivity.class);
//            Intent intent = new Intent(this, SearchActivity.class);
//            startActivity(intent);
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
            startActivityForResult(GroupManageActivity.class, GroupManageActivity.GROUP_UPDATE);
        } else if (id == R.id.nav_draft) {
            startActivity(TopicDraftActivity.class);
        } else if (id == R.id.nav_share) {
            startActivity(TopicPublishActivity.getStartIntent(this, "#Share#Share微博客户端, 非常好用[赞]"));
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
        if (user != null) {
            mAvatar.setImageUrl(user.getAvatar(), RequestManager.getImageLoader());
            mUsername.setText(user.getName());
            mSign.setText(user.getSign());
        } else {
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
            updateView();
        } else if (requestCode == GroupManageActivity.GROUP_UPDATE && resultCode == Activity.RESULT_OK) {
            updateViewPager();
        }
    }

    private void updateView() {
        loadSuccess(UserUtil.getCurrentUser());
        if (mViewPager != null) {
            mTopicFragmentAdapter.refresh(getTopicGroups());

            //全部微博
            String str1 = mTopicFragmentAdapter.getFragmentTags().get(0);
            //互相关注
            String str2 = mTopicFragmentAdapter.getFragmentTags().get(1);

            refreshTopicFragment(getSupportFragmentManager().findFragmentByTag(str1));
            refreshTopicFragment(getSupportFragmentManager().findFragmentByTag(str2));
//                mViewPager.setAdapter(mTopicFragmentAdapter = new TopicFragmentPager(getSupportFragmentManager(), this, getTopicGroups()));
            adjustTabLayout();
        }
    }

    private void refreshTopicFragment(Fragment fragment) {
        if (fragment != null && fragment instanceof TopicFragment) {
            TopicFragment topicFragment = (TopicFragment) fragment;
            topicFragment.refresh();
        }
    }

    private void updateViewPager() {
        if (mViewPager != null) {
            mTopicFragmentAdapter.refresh(getTopicGroups());
            adjustTabLayout();
        }
    }

    @Override
    protected void onNetworkChange(boolean isConnected) {
        super.onNetworkChange(isConnected);
        mNoNetwork.setVisibility(isConnected ? View.GONE : View.VISIBLE);
    }

    private void handleSearch() {
        handleSearch(mSearch.getVisibility() != View.VISIBLE);
    }

    private void handleSearch(boolean isShow) {
        if (isShow) {
            startSearchShowAnimation();
        } else {
            startSearchHideAnimation();
        }
    }


    private void startSearchHideAnimation() {
            final Animator searchHideAnimator = ViewAnimationUtils.createCircularReveal(mSearch,
                    mSearch.getWidth(),
                    ViewUtil.getActionBarHeight() / 2,
                    (float) Math.hypot(mSearch.getWidth(), mSearch.getHeight()),
                    0);
            searchHideAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mSearchContent.setText("");
                    mSearch.setEnabled(false);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mSearch.setVisibility(View.GONE);
                    ViewUtil.hideKeyBoard(mSearchContent);
                    mSearchResult.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            searchHideAnimator.setDuration(300);
            searchHideAnimator.start();
    }

    private void startSearchShowAnimation() {
        final Animator searchShowAnimator = ViewAnimationUtils.createCircularReveal(mSearch,
                    mSearchContent.getWidth(),
                    ViewUtil.getActionBarHeight() / 2,
                    0,
                    (float) Math.hypot(mSearch.getWidth(), mSearch.getHeight()));
            searchShowAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mSearch.setVisibility(View.VISIBLE);
                    mSearch.setEnabled(true);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mSearchResult.setVisibility(View.VISIBLE);
                    ViewUtil.showKeyBoard(mSearchContent);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            searchShowAnimator.setDuration(300);
            searchShowAnimator.start();
    }
}
