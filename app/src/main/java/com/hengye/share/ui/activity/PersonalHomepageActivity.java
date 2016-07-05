package com.hengye.share.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.R;
import com.hengye.share.model.Parent;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.fragment.TopicFragment;
import com.hengye.share.ui.mvpview.UserMvpView;
import com.hengye.share.ui.presenter.TopicPresenter;
import com.hengye.share.ui.presenter.UserPresenter;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.UserUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.hengye.swiperefresh.SwipeRefreshLayout;

public class PersonalHomepageActivity extends BaseActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener, UserMvpView {

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    protected void handleBundleExtra() {
        mUserInfo = (UserInfo) getIntent().getSerializableExtra(UserInfo.class.getSimpleName());

        if (mUserInfo == null) {
            Uri data = getIntent().getData();
            if (data != null) {
                String value = data.toString();
                int index = value.lastIndexOf("@");
                if (index != -1) {
                    String newValue = value.substring(index + 1);
                    mUserInfo = new UserInfo();
                    mUserInfo.setName(newValue);
                    mUserInfo.setParent(new Parent(null, Parent.TYPE_WEIBO));
                }
            }
        }
    }

    public static Intent getStartIntent(Context context, UserInfo userInfo) {
        Intent intent = new Intent(context, PersonalHomepageActivity.class);
        intent.putExtra(UserInfo.class.getSimpleName(), userInfo);
        return intent;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_personal_homepage;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if (mUserInfo == null) {
            PersonalHomepageActivity.this.finish();
        } else {
            setupPresenter(mPresenter = new UserPresenter(this));
            initView();
        }
    }

    private TextView mDivision, mAttention, mFans, mSign;
    private NetworkImageViewPlus mCover, mAvatar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private SwipeRefreshLayout mSwipeRefresh;
    private AppBarLayout mAppBarLayout;
    private CoordinatorLayout mCoordinatorLayout;

    private UserInfo mUserInfo;

    private UserPresenter mPresenter;

    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mCollapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        mCover = (NetworkImageViewPlus) findViewById(R.id.iv_cover);
        mAvatar = (NetworkImageViewPlus) findViewById(R.id.iv_avatar);

        mDivision = (TextView) findViewById(R.id.tv_division);
        mAttention = (TextView) findViewById(R.id.tv_attention);
        mFans = (TextView) findViewById(R.id.tv_fans);
        mSign = (TextView) findViewById(R.id.tv_sign);
        if (mUserInfo.getParent().isWeiBo()) {
            WBUserInfo wbUserInfo = mUserInfo.getWBUserInfoFromParent();
            if (wbUserInfo != null) {
                initUserInfo(wbUserInfo);
            } else {
                mPresenter.loadWBUserInfo(mUserInfo.getUid(), mUserInfo.getName());
            }
        }

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        loadData();

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset >= -50) {
            mSwipeRefresh.setEnabled(true);
        } else {
            mSwipeRefresh.setEnabled(false);
        }
//        if (mCollapsingToolbarLayout.getHeight() + verticalOffset < 2 * ViewCompat.getMinimumHeight(mCollapsingToolbarLayout)) {
//            mSwipeRefresh.setEnabled(false);
//        } else {
//            mSwipeRefresh.setEnabled(true);
//        }
//        L.debug("vertical offset : {}, height : {}, minimumHeight : {}", verticalOffset, mCollapsingToolbarLayout.getHeight(), ViewCompat.getMinimumHeight(mCollapsingToolbarLayout));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAppBarLayout.addOnOffsetChangedListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAppBarLayout.removeOnOffsetChangedListener(this);
    }

    TopicFragment mTopicFragment;

    private void setupTopicFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, mTopicFragment = TopicFragment.newInstance(TopicPresenter.TopicType.HOMEPAGE, mUserInfo.getUid(), mUserInfo.getName()))
                .commit();

        mTopicFragment.setLoadDataCallBack(new TopicFragment.LoadDataCallBack() {
            @Override
            public void initView() {
                final PullToRefreshLayout pullToRefresh = (PullToRefreshLayout) mTopicFragment.findViewById(R.id.pull_to_refresh);
                pullToRefresh.setRefreshEnable(false);
                mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        pullToRefresh.getOnRefreshListener().onRefresh();
                    }
                });
            }

            @Override
            public void refresh(final boolean isRefresh) {
                if (isRefresh) {
                    mSwipeRefresh.setRefreshing(true);
                    mSwipeRefresh.getOnRefreshListener().onRefresh();
                } else {
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    private void initUserInfo(WBUserInfo wbUserInfo) {
        mCollapsingToolbarLayout.setTitle(wbUserInfo.getName());
        mCover.setImageUrl(wbUserInfo.getCover_image_phone(), RequestManager.getImageLoader());
        mAvatar.setImageUrl(wbUserInfo.getAvatar_large(), RequestManager.getImageLoader());

        mDivision.setVisibility(View.VISIBLE);
        mAttention.setText(String.format(getString(R.string.label_attention), wbUserInfo.getFriends_count()));
        mFans.setText(String.format(getString(R.string.label_fans), wbUserInfo.getFollowers_count()));
        mSign.setText(wbUserInfo.getDescription());
    }

    private boolean loadData() {
        if (mUserInfo == null || UserUtil.isUserEmpty()) {
            return false;
        }
        if (mUserInfo.getParent().isWeiBo()) {
            L.debug("userInfo : {}", mUserInfo.getParent().getJson());
            if (!TextUtils.isEmpty(mUserInfo.getUid()) || !TextUtils.isEmpty(mUserInfo.getName())) {
                setupTopicFragment();
//                SwipeRefreshLayout
//                PullToRefreshLayout pullToRefreshLayout = (PullToRefreshLayout) mTopicFragment.findViewById(R.id.pull_to_refresh);
//                pullToRefreshLayout.setRefreshEnable(false);
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }

    @Override
    public void handleUserInfo(WBUserInfo wbUserInfo) {
        if (wbUserInfo != null) {
            initUserInfo(wbUserInfo);
        }
    }

    @Override
    public void loadSuccess(User user) {

    }

    @Override
    public void loadFail() {

    }
}
