package com.hengye.share.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hengye.share.R;
import com.hengye.share.adapter.viewpager.TopicFragmentPager;
import com.hengye.share.helper.SettingHelper;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.ui.activity.setting.SettingActivity;
import com.hengye.share.ui.base.ActivityHelper;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.fragment.TopicFavoritesFragment;
import com.hengye.share.ui.fragment.TopicFragment;
import com.hengye.share.ui.mvpview.UserMvpView;
import com.hengye.share.ui.presenter.TopicPresenter;
import com.hengye.share.ui.presenter.UserPresenter;
import com.hengye.share.ui.support.actionbar.ActionBarDrawerToggleCustom;
import com.hengye.share.ui.widget.SearchView;
import com.hengye.share.ui.widget.image.AvatarImageView;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.NetworkUtil;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.ThemeUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.ViewUtil;
import com.hengye.share.util.intercept.Action;
import com.hengye.share.util.intercept.AdTokenInterceptor;
import com.hengye.share.util.intercept.TokenInterceptor;

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
    public int getLayoutResId() {
        return R.layout.activity_topic;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        addPresenter(mPresenter = new UserPresenter(this));
        initView();
    }

    private ViewPager mViewPager;
    private TabLayout mTab;
    private AvatarImageView mAvatar;
    private TextView mUsername, mSign;
    private View mNoNetwork, mAppBar;
    private DrawerLayout mDrawer;

    private TopicFragmentPager mTopicFragmentAdapter;

    private UserPresenter mPresenter;


    private void initView() {
        mNoNetwork = findViewById(R.id.rl_no_network);
        mNoNetwork.setOnClickListener(this);

        mAppBar = findViewById(R.id.appbar);

        initToolbar();
        if(getToolbar().getNavigation() != null){
            int actionBarHeight = ViewUtil.getActionBarHeight();
            final TypedArray a = getTheme().obtainStyledAttributes(null,
                    android.support.v7.appcompat.R.styleable.DrawerArrowToggle, android.support.v7.appcompat.R.attr.drawerArrowStyle,
                    android.support.v7.appcompat.R.style.Base_Widget_AppCompat_DrawerArrowToggle);
            int size = a.getDimensionPixelSize(android.support.v7.appcompat.R.styleable.DrawerArrowToggle_drawableSize, 0);
            a.recycle();
//            int size = getResources().getDimensionPixelSize(R.dimen.icon_size_normal);
            int padding = (actionBarHeight - size) / 2;
            getToolbar().getNavigation().setPadding(padding, padding, padding, padding);
            getToolbar().getNavigation().setScaleType(ImageView.ScaleType.MATRIX);
        }
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        mTab = (TabLayout) findViewById(R.id.tab);
        mViewPager = (ViewPager) findViewById(R.id.view_pager);

        mViewPager.setAdapter(mTopicFragmentAdapter = new TopicFragmentPager(getSupportFragmentManager(), this, getTopicGroups()));
        adjustTabLayout();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggleCustom toggle = new ActionBarDrawerToggleCustom(
                this, mDrawer, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if(toggle.getDrawerArrowDrawable() != null){
            toggle.getDrawerArrowDrawable().setColor(ThemeUtil.getTintColor());
        }

        toggle.setGravityCompat(GravityCompat.END);
        mDrawer.addDrawerListener(toggle);
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

        View header = navigationView.getHeaderView(0);
        //修复因为布局使用fitsSystemWindows而导致DrawLayout内容间距不对的问题
        int contentPadding = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin);
        RelativeLayout navHeader = (RelativeLayout) header.findViewById(R.id.rl_header);
        navHeader.setPadding(contentPadding,
                contentPadding + ViewUtil.getStatusBarHeight(),
                contentPadding,
                contentPadding);
//        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) navHeader.getLayoutParams();
//        lp.topMargin = ViewUtil.getStatusBarHeight();
//        navHeader.setLayoutParams(lp);

        ImageView moreAccount = (ImageView) header.findViewById(R.id.iv_more_account);
        SelectorLoader.getInstance().setImageSelector(this, moreAccount, R.drawable.compose_more_account_add, R.drawable.compose_more_account_add_highlighted);
        moreAccount.setOnClickListener(this);

        mAvatar = (AvatarImageView) header.findViewById(R.id.iv_avatar);
        mAvatar.setAutoClipBitmap(false);
        mAvatar.setFadeInImage(false);
        mAvatar.setDefaultImageResId(R.drawable.ic_user_avatar);
        mUsername = (TextView) header.findViewById(R.id.tv_username);
        mSign = (TextView) header.findViewById(R.id.tv_sign);

        mUsername.setOnClickListener(this);
        mSign.setOnClickListener(this);
    }

    @Override
    public boolean onToolbarDoubleClick(Toolbar toolbar) {
        TopicFragment topicFragment = mTopicFragmentAdapter.getItem(mViewPager.getCurrentItem());
        if(topicFragment != null){
            topicFragment.scrollToTop();
            return true;
        }
        return false;
    }

    private SearchView mSearchView;
    private String mContent;

    private void initSearch() {
        mSearchView = (SearchView) findViewById(R.id.search_view);
//        mSearchView.setVisibility(View.GONE);
        mSearchView.setMode(SearchView.MODE_ANIMATION, this);
        mSearchView.setSearchListener(new SearchView.onSearchListener() {
            @Override
            public void onSearch(String content) {
                mContent = content;
                if (!TextUtils.isEmpty(mContent.trim())) {
                    setHideAnimationOnStart();
                    getTokenInterceptor().setAction(mStartSearch).start();
//                    startActivity(SearchActivity.getStartIntent(TopicActivity.this, mContent));
//                        overridePendingTransition(R.anim.fade_in, 0);
                }
            }
        });
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
        }
    }

    private int testClickCount = 0;

    private void adjustTabLayout() {
        mTab.setTabMode(mViewPager.getAdapter().getCount() > 3 ? TabLayout.MODE_SCROLLABLE : TabLayout.MODE_FIXED);
        mTab.setupWithViewPager(mViewPager);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.END)) {
            mDrawer.closeDrawer(GravityCompat.END);
        } else if (mSearchView.isSearchShow()) {
            mSearchView.handleSearch(false);
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
            mSearchView.handleSearch();
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
                return false;
            }
            PersonalHomepageActivity.start(this, mAvatar, UserInfo.getUserInfo(UserUtil.getCurrentUser()));
        } else if (id == R.id.nav_at_me) {
            startActivity(TopicMentionActivity.class);
        } else if (id == R.id.nav_comment) {
            startActivity(TopicCommentActivity.class);
        } else if (id == R.id.nav_favorites) {
            startActivity(FragmentActivity.getStartIntent(this, TopicFavoritesFragment.class));
        } else if (id == R.id.nav_setting) {
//            mDrawer.closeDrawer(GravityCompat.END);
            startActivity(SettingActivity.class);
        } else if (id == R.id.nav_group_manage) {
            getTokenInterceptor().setAction(mStartGroup).start();
//            startActivityForResult(GroupManageActivity.class, GroupManageActivity.GROUP_UPDATE);
        } else if (id == R.id.nav_draft) {
            startActivity(TopicDraftActivity.class);
        } else if (id == R.id.nav_share) {
            startActivity(TopicPublishActivity.getStartIntent(this, "#Share#Share微博客户端, 非常好用[赞]"));
        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.END);
        return false;
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
        if (!CommonUtil.isEmpty(temp)) {
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

    Action mStartGroup, mStartSearch;
    AdTokenInterceptor mAdTokenInterceptor;
    TokenInterceptor mTokenInterceptor;
    public TokenInterceptor getTokenInterceptor(){
        if(mTokenInterceptor == null){
            mAdTokenInterceptor = new AdTokenInterceptor(this);
            mTokenInterceptor = new TokenInterceptor(this);
            mTokenInterceptor.with(mAdTokenInterceptor);
            mStartGroup = new Action() {
                @Override
                public void run() {
                    startActivityForResult(GroupManageActivity.class, GroupManageActivity.GROUP_UPDATE);
                }
            };
            mStartSearch = new Action() {
                @Override
                public void run() {
                    getActivityHelper().registerActivityLifecycleListener(new ActivityHelper.DefaultActivityLifecycleListener(){
                        @Override
                        public void onActivityResumed(Activity activity) {
                            mSearchView.handleSearch(false);
                            getActivityHelper().unregisterActivityLifecycleListener(this);
                        }
                    });
                    startActivity(SearchActivity.getStartIntent(TopicActivity.this, mContent));
                }
            };
        }
        return mTokenInterceptor;
    }

}
