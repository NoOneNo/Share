package com.hengye.share.module.topic;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
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
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.module.accountmanage.AccountManageActivity;
import com.hengye.share.module.base.ActivityHelper;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.draft.TopicDraftActivity;
import com.hengye.share.module.groupmanage.GroupListFragment;
import com.hengye.share.module.groupmanage.GroupManageActivity;
import com.hengye.share.module.profile.PersonalHomepageActivity;
import com.hengye.share.module.publish.TopicPublishActivity;
import com.hengye.share.module.search.SearchActivity;
import com.hengye.share.module.setting.SettingActivity;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.module.sso.UserMvpView;
import com.hengye.share.module.sso.UserPresenter;
import com.hengye.share.module.test.TestActivity;
import com.hengye.share.ui.support.actionbar.ActionBarDrawerToggleCustom;
import com.hengye.share.ui.widget.SearchView;
import com.hengye.share.ui.widget.fab.AnimatedFloatingActionButton;
import com.hengye.share.ui.widget.image.AvatarImageView;
import com.hengye.share.ui.widget.sheetfab.MaterialSheetFab;
import com.hengye.share.ui.widget.sheetfab.MaterialSheetFabEventListener;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.L;
import com.hengye.share.util.NetworkUtil;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.ThemeUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.ViewUtil;
import com.hengye.share.util.intercept.Action;
import com.hengye.share.util.intercept.AdTokenInterceptor;

public class TopicActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        AppBarLayout.OnOffsetChangedListener,
        UserMvpView, View.OnClickListener, GroupListFragment.OnGroupSelectedCallback {

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

        if (UserUtil.isUserEmpty()) {
            startActivityForResult(AccountManageActivity.class, AccountManageActivity.ACCOUNT_CHANGE);
        } else if (UserUtil.isUserNameEmpty()) {
            mPresenter.loadWBUserInfo();
        } else {
            loadSuccess(UserUtil.getCurrentUser());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppBar.addOnOffsetChangedListener(this);
        if(mUpdateUserInfo){
            mUpdateUserInfo = false;
            if (UserUtil.isUserNameEmpty()) {
                mPresenter.loadWBUserInfo();
            } else {
                loadSuccess(UserUtil.getCurrentUser());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAppBar.removeOnOffsetChangedListener(this);
        saveLastPosition();
    }

    protected void saveLastPosition() {
        if(mCurrentTopicFragment != null){
            mCurrentTopicFragment.getLayoutManager().onSaveInstanceState();
            int top = 0;
            int position = mCurrentTopicFragment.getLayoutManager().findFirstVisibleItemPosition();
            View View = mCurrentTopicFragment.getLayoutManager().findViewByPosition(position);

            if(View != null){
                top = View.getTop();
            }
            SPUtil.putInt("lastTopicPosition", position);
            SPUtil.putInt("lastTopicOffset", top);

//            L.debug("save lastPosition : {}, offset : {}", position, top);
        }
    }


    AvatarImageView mAvatar;
    AppBarLayout mAppBar;
    CoordinatorLayout mCoordinatorLayout;
    TextView mUsername, mSign;
    View mNoNetwork;
    DrawerLayout mDrawer;
    AnimatedFloatingActionButton mFab;

    GroupListFragment mGroupsFragment;
    MaterialSheetFab mMaterialSheetFab;

    TopicFragment mCurrentTopicFragment;

    SearchView mSearchView;
    String mContent;

    UserPresenter mPresenter;

    int testClickCount = 0;
    long mBackPressedTime = 0;


    private void initView() {
        mNoNetwork = findViewById(R.id.rl_no_network);
        mNoNetwork.setOnClickListener(this);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinator_layout);
        mAppBar = (AppBarLayout) findViewById(R.id.appbar);
        mFab = (AnimatedFloatingActionButton) findViewById(R.id.fab);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        initToolbar();
        initToolBarButton();
        initNavigationView();
        initSearch();
        initFab();

//        getTokenInterceptor().start();
    }

    private void initToolBarButton() {
//        if (getToolbar().getNavigation() != null) {
//            int actionBarHeight = ViewUtil.getActionBarHeight();
//            final TypedArray a = getTheme().obtainStyledAttributes(null,
//                    android.support.v7.appcompat.R.styleable.DrawerArrowToggle, android.support.v7.appcompat.R.attr.drawerArrowStyle,
//                    android.support.v7.appcompat.R.style.Base_Widget_AppCompat_DrawerArrowToggle);
//            int size = a.getDimensionPixelSize(android.support.v7.appcompat.R.styleable.DrawerArrowToggle_drawableSize, 0);
//            a.recycle();
////            int size = getResources().getDimensionPixelSize(R.dimen.icon_size_normal);
//            int padding = (actionBarHeight - size) / 2;
//            getToolbar().getNavigation().setPadding(padding, padding, padding, padding);
//            getToolbar().getNavigation().setScaleType(ImageView.ScaleType.MATRIX);
//        }

        ActionBarDrawerToggleCustom toggle = new ActionBarDrawerToggleCustom(
                this, mDrawer, getToolbar(), R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (toggle.getDrawerArrowDrawable() != null) {
            toggle.getDrawerArrowDrawable().setColor(ThemeUtil.getUntingedColor());
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

    private void initFab() {
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        //noinspection unchecked
        mMaterialSheetFab = new MaterialSheetFab(mFab, sheetView, overlay, ResUtil.getColor(R.color.white), ThemeUtil.getColor());
        mMaterialSheetFab.setEventListener(new MaterialSheetFabEventListener() {
            @Override
            public void onShowSheet() {
                super.onShowSheet();
                mGroupsFragment.scrollToSelectPosition();
            }

        });

        mMaterialSheetFab.showFab();
        mGroupsFragment = (GroupListFragment) getSupportFragmentManager().findFragmentById(R.id.group_list_fragment);
        //刷新当前选中的fragment

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content);
        if (fragment != null && fragment instanceof TopicFragment) {
            mCurrentTopicFragment = (TopicFragment) fragment;
            mGroupsFragment.load(false, false);
        } else {
            mGroupsFragment.load(false, true);
        }
    }

    boolean mIsFirstTime = true;
    public void onGroupSelected(int position, TopicPresenter.TopicGroup topicGroup) {

        if (topicGroup.getTopicType() == TopicPresenter.TopicType.NONE) {
            getAdTokenInterceptor().setAction(mStartGroup).start();
        } else {
            setFragment(TopicFragment.newInstance(topicGroup, mIsFirstTime), topicGroup.getName());
            mIsFirstTime = false;
        }
        if (mMaterialSheetFab.isSheetVisible()) {
            mMaterialSheetFab.hideSheet();
        }
    }

    private void setFragment(final TopicFragment fragment, String title) {
        if (fragment == null) {
            return;
        }
        mCurrentTopicFragment = fragment;
        updateToolbarTitle(title);
        View view = findViewById(R.id.content);
        view.setAlpha(0.0f);
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", view.getAlpha(), 1.0f);
        anim.setDuration(600);
        anim.start();
        getSupportFragmentManager().beginTransaction().replace(R.id.content, fragment, "MainFragment").commit();
    }

    @Override
    public void onToolbarDoubleClick(Toolbar toolbar) {
        TopicFragment topicFragment = mCurrentTopicFragment;
        if (topicFragment != null) {
            topicFragment.onScrollToTop(false);
        }
    }

    private void initSearch() {
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setMode(SearchView.MODE_ANIMATION, this);
        mSearchView.setSearchListener(new SearchView.onSearchListener() {
            @Override
            public void onSearch(String content) {
                mContent = content;
                if (!TextUtils.isEmpty(mContent.trim())) {
                    setHideAnimationOnStart();
                    getAdTokenInterceptor().setAction(mStartSearch).start();
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mCurrentTopicFragment == null) {
            return;
        }
        if (verticalOffset >= 0) {
            mCurrentTopicFragment.setRefreshEnable(true);
        } else {
            mCurrentTopicFragment.setRefreshEnable(false);
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
        if (id == R.id.action_search) {
            mSearchView.handleSearch();
        } else if (id == R.id.action_publish) {
            startActivity(TopicPublishActivity.class);
        }

        return super.onOptionsItemSelected(item);
    }

    boolean mUpdateUserInfo;

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
            mUpdateUserInfo = true;
            PersonalHomepageActivity.start(this, mAvatar, UserInfo.getUserInfo(UserUtil.getCurrentUser()));
        } else if (id == R.id.nav_at_me) {
            startActivity(TopicMentionActivity.class);
        } else if (id == R.id.nav_comment) {
            startActivity(TopicCommentActivity.class);
        } else if (id == R.id.nav_favorites) {
//            startActivity(FragmentActivity.getStartIntent(this, TopicFavoritesFragment.class));
            startActivity(TopicFavoriteActivity.class);
        } else if (id == R.id.nav_setting) {
//            mDrawer.closeDrawer(GravityCompat.END);
            startActivity(SettingActivity.class);
        } else if (id == R.id.nav_group_manage) {
            getAdTokenInterceptor().setAction(mStartGroup).start();
//            startActivityForResult(GroupManageActivity.class, GroupManageActivity.REQUEST_GROUP_UPDATE);
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

    @Override
    public void loadSuccess(User user) {
        if (user != null) {
            mAvatar.setImageUrl(user.getAvatar());
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
        loadSuccess(User.getUser(wbUserInfo));
    }

    @Override
    public void loadFail() {

    }

    public final static int REQUEST_GROUP_UPDATE = 11;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AccountManageActivity.ACCOUNT_CHANGE && resultCode == Activity.RESULT_OK) {
            updateView();
        } else if (requestCode == REQUEST_GROUP_UPDATE) {
//            忽略resultCode == Activity.RESULT_OK，只要打开分组管理返回后都刷新
            boolean isUpdate = resultCode == Activity.RESULT_OK;
            mGroupsFragment.load(true, isUpdate);
        }
    }

    private void updateView() {
        loadSuccess(UserUtil.getCurrentUser());
        mGroupsFragment.load(true, true);
    }


    @Override
    protected void onNetworkChange(boolean isConnected) {
        super.onNetworkChange(isConnected);
        mNoNetwork.setVisibility(isConnected ? View.GONE : View.VISIBLE);
    }

    Action mStartGroup, mStartSearch;
    AdTokenInterceptor mAdTokenInterceptor;

    public AdTokenInterceptor getAdTokenInterceptor() {
        if (mAdTokenInterceptor == null) {
            mAdTokenInterceptor = new AdTokenInterceptor(this);
            mStartGroup = new Action() {
                @Override
                public void run() {
                    startActivityForResult(GroupManageActivity.class, REQUEST_GROUP_UPDATE);
                }
            };
            mStartSearch = new Action() {
                @Override
                public void run() {
                    getActivityHelper().registerActivityLifecycleListener(new ActivityHelper.DefaultActivityLifecycleListener() {
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
        return mAdTokenInterceptor;
    }

}
