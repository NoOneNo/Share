package com.hengye.share.ui.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.R;
import com.hengye.share.helper.TransitionHelper;
import com.hengye.share.model.Parent;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.fragment.PersonalHomepageFragment;
import com.hengye.share.ui.fragment.TopicFragment;
import com.hengye.share.ui.mvpview.UserMvpView;
import com.hengye.share.ui.presenter.TopicPresenter;
import com.hengye.share.ui.presenter.UserPresenter;
import com.hengye.share.ui.widget.PersonalHomePageToolbarLayout;
import com.hengye.share.ui.widget.fab.CheckableFab;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.ViewUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.hengye.swiperefresh.SwipeRefreshLayout;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PersonalHomepageActivity extends BaseActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener, UserMvpView {

    public static void start(Context context, View startView, UserInfo userInfo) {
        Intent intent = getStartIntent(context, userInfo);
        TransitionHelper.startTransitionActivity(context, intent, startView, R.string.transition_name_avatar);
    }

    public static Intent getStartIntent(Context context, UserInfo userInfo) {
        Intent intent = new Intent(context, PersonalHomepageActivity.class);
        intent.putExtra(UserInfo.class.getSimpleName(), userInfo);
        return intent;
    }

    @Override
    protected boolean setToolBar() {
        return false;
    }

    @Override
    public void onBackPressed() {
        finish();
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

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_personal_homepage;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if (mUserInfo == null) {
            PersonalHomepageActivity.this.finish();
        } else {
            addPresenter(mPresenter = new UserPresenter(this));
            initView();
            loadData();
        }
    }

    private TextView mDivision, mAttention, mFans, mSign;
    private NetworkImageViewPlus mCover, mAvatar;
    private PersonalHomePageToolbarLayout mCollapsingToolbarLayout;
    private SwipeRefreshLayout mSwipeRefresh;
    private AppBarLayout mAppBarLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private View mUserDescription;
    //    private FloatingActionButton mFollowButton;
    private CheckableFab mFollowButton;
    private TabLayout mTab;

    private UserInfo mUserInfo;
    private WBUserInfo mWBUserInfo;

    private UserPresenter mPresenter;

    @SuppressWarnings("ConstantConditions")
    private void initView() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });

//        toolbar.setBackgroundResource(R.drawable.gradient_toolbar_grey);

        mFollowButton = (CheckableFab) findViewById(R.id.fab);
        mFollowButton.setOnClickListener(this);
        mCollapsingToolbarLayout =
                (PersonalHomePageToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mUserDescription = findViewById(R.id.ll_user_desc);
//        mCollapsingToolbarLayout.setexpan
        mCover = (NetworkImageViewPlus) findViewById(R.id.iv_cover);
        mCover.setFadeInImage(false);

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mCover.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mCover.getViewTreeObserver().removeOnPreDrawListener(this);
                startCoverShowAnimation();
                return true;
            }
        });
        mAvatar = (NetworkImageViewPlus) findViewById(R.id.iv_avatar);
        mAvatar.setTransitionName(getString(R.string.transition_name_avatar));
        mAvatar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setUpAvatarToToolbar();
            }
        });

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

        mTab = (TabLayout) findViewById(R.id.tab);

        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        mSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mSwipeRefresh.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int action = MotionEventCompat.getActionMasked(event);

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        //ACTION_DOWN不会被触发被SwipeRefreshLayout拦截了, 所以由ACTION_MOVE开始时获取当前AppBar高度
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:

                        mIsBeingDragged = false;

                        final ViewGroup.LayoutParams lp = mCollapsingToolbarLayout.getLayoutParams();
                        mAnimateToCorrectHeight = ValueAnimator.ofInt(lp.height, mToolBarHeight);
                        mAnimateToCorrectHeight.setDuration(200);
                        mAnimateToCorrectHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                lp.height = (int) animation.getAnimatedValue();
                                mCollapsingToolbarLayout.requestLayout();
                            }
                        });
                        mAnimateToCorrectHeight.start();
                        break;
                    case MotionEvent.ACTION_MOVE:

                        if (!mIsBeingDragged) {
                            mIsBeingDragged = true;
                            if (mAnimateToCorrectHeight != null) {
                                mAnimateToCorrectHeight.pause();
                            }
                            mInitialDownY = event.getRawY();
                            mToolBarHeight = mCollapsingToolbarLayout.getLayoutParams().height;

                            break;
                        }

                        float currentY = event.getRawY();
                        int distance = (int) (DRAG_RATE * (currentY - mInitialDownY));

                        if (distance > 0) {
                            ViewGroup.LayoutParams lp_ = mCollapsingToolbarLayout.getLayoutParams();
                            int height = mToolBarHeight + distance;
                            if (height > getToolBarMaxHeight()) {
                                if (lp_.height == getToolBarMaxHeight()) {
                                    //如果已经到达高度的最大值
                                    break;
                                }
                                height = getToolBarMaxHeight();
                            }
                            lp_.height = height;
                            mCollapsingToolbarLayout.requestLayout();

                        }

                        break;
                }
                return false;
            }
        });

    }

    private void setUpAvatarToToolbar(){
        if (mAvatar.getDrawable() == null || !(mAvatar.getDrawable() instanceof BitmapDrawable)) {
            return;
        }
        BitmapDrawable bd = (BitmapDrawable) mAvatar.getDrawable();
        final Bitmap bitmap = bd.getBitmap();
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCollapsingToolbarLayout.setAvatarBitmap(bitmap);
                mAvatar.setVisibility(View.INVISIBLE);
            }
        }, 350);
    }

    private void startCoverShowAnimation() {

        final Animator coverShowAnimator = ViewAnimationUtils.createCircularReveal(mAppBarLayout,
                mAppBarLayout.getWidth() / 2,
                mAppBarLayout.getHeight() / 2,
                0,
                (float) Math.hypot(mAppBarLayout.getWidth(), mAppBarLayout.getWidth()));
        coverShowAnimator.setInterpolator(new FastOutLinearInInterpolator());
        coverShowAnimator.setDuration(500);
        coverShowAnimator.start();
    }

    private int getToolBarMaxHeight() {
        if (mToolBarMaxHeight == 0) {
            mToolBarMaxHeight = (int) (mToolBarHeight * MAX_APP_BAR_HEIGHT_RATE);
        }
        return mToolBarMaxHeight;
    }

    private static final float MAX_APP_BAR_HEIGHT_RATE = 1.5f;
    private static final float DRAG_RATE = .5f;
    private int mToolBarHeight, mToolBarMaxHeight;
    private float mInitialDownY;
    private boolean mIsBeingDragged;
    private ValueAnimator mAnimateToCorrectHeight;

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

//    TopicFragment mTopicFragment;
    PersonalHomepageFragment mFragment;

    private void setupTopicFragment(WBUserInfo wbUserInfo) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, mFragment = PersonalHomepageFragment.newInstance(wbUserInfo))
                .commit();

        mFragment.setCallBack(new TopicFragment.LoadDataCallBack() {
            @Override
            public void initView() {
                RecyclerView recyclerView = (RecyclerView) mFragment.getTopicFragment().findViewById(R.id.recycler_view);
//                recyclerView.setNestedScrollingEnabled(false);
                final PullToRefreshLayout pullToRefresh = (PullToRefreshLayout) mFragment.getTopicFragment().findViewById(R.id.pull_to_refresh);
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
        mWBUserInfo = wbUserInfo;
        mCollapsingToolbarLayout.setTitle(wbUserInfo.getName());
//        mCover.setImageResource(R.drawable.bg_test);
        mCover.setAutoClipBitmap(false);
        mCover.setImageUrl(wbUserInfo.getCover_image_phone(), RequestManager.getImageLoader());
        mAvatar.setImageUrl(wbUserInfo.getAvatar_large(), RequestManager.getImageLoader());
//        mAvatar.setDefaultImageResId(R.drawable.ic_user_avatar);
        mDivision.setVisibility(View.VISIBLE);
        mAttention.setText(String.format(getString(R.string.label_attention), DataUtil.getCounter(wbUserInfo.getFriends_count())));
        mFans.setText(String.format(getString(R.string.label_fans), DataUtil.getCounter(wbUserInfo.getFollowers_count())));
        mSign.setText(wbUserInfo.getDescription());
        mFollowButton.setChecked(mWBUserInfo.isFollowing());
//        mFollowButton.setVisibility(View.VISIBLE);

        mUserDescription.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mUserDescription.getViewTreeObserver().removeOnPreDrawListener(this);
                float textHeight = ViewUtil.sp2px(R.dimen.text_large);
                int avatarMarginTop = ViewUtil.dp2px(R.dimen.header_personal_avatar_margin_top);
                int descHeight = mUserDescription.getHeight();
                float marginBottom = descHeight + (avatarMarginTop - textHeight) / 2;
                mCollapsingToolbarLayout.setExpandedTitleMarginBottom((int)marginBottom);

                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mFollowButton.getLayoutParams();
                lp.bottomMargin = descHeight;
                mFollowButton.setLayoutParams(lp);
                mFollowButton.requestLayout();
                return true;
            }
        });
    }

    private boolean loadData() {
        if (mUserInfo == null || UserUtil.isUserEmpty()) {
            return false;
        }
        if (mUserInfo.getParent().isWeiBo()) {
            L.debug("userInfo : {}", mUserInfo.getParent().getJson());
            if (!TextUtils.isEmpty(mUserInfo.getUid()) || !TextUtils.isEmpty(mUserInfo.getName())) {
                setupTopicFragment(mUserInfo.getWBUserInfoFromParent());
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

        if (id == R.id.fab) {
            if (mWBUserInfo != null) {
                follow(!mWBUserInfo.isFollowing(), mWBUserInfo.getIdstr());
            }
        }
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

    public void follow(final boolean isFollow, String uid) {

        Observable<WBUserInfo> observable;

        observable = isFollow ?
                RetrofitManager
                        .getWBService()
                        .followCreate(UserUtil.getToken(), uid) :
                RetrofitManager
                        .getWBService()
                        .followDestroy(UserUtil.getToken(), uid);

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<WBUserInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showNetWorkErrorToast();
                    }

                    @Override
                    public void onNext(WBUserInfo wbUserInfo) {
                        if (wbUserInfo != null) {
                            if (isFollow) {
                                ToastUtil.showToast(R.string.label_follow_create_success);
                            } else {
                                ToastUtil.showToast(R.string.label_follow_destroy_success);
                            }
                            mFollowButton.setChecked(isFollow);
                            if (mWBUserInfo != null) {
                                mWBUserInfo.setFollowing(isFollow);
                            }
//                            initUserInfo(wbUserInfo);
                        }
                    }
                });
    }

//    private Matrix setCoverMatrix() {
//
//        Matrix drawMatrix = new Matrix();
//
//        if (mCover.getDrawable() == null) {
//            return drawMatrix;
//        }
//
//        int dWidth = mCover.getDrawable().getIntrinsicWidth();
//        int dHeight = mCover.getDrawable().getIntrinsicHeight();
//
//        int vWidth = mCover.getWidth();
//        int vHeight = mCover.getHeight();
//        float scale;
//        float dx = 0, dy = 0;
//        scale = (float) vWidth / (float) dWidth;
//        dy = vHeight - dHeight * scale;
//        if (dy < 0) {
//            dy /= 2;
//        } else {
//            dy = 0;
//            if (dWidth * vHeight * 1.5 > vWidth * dHeight) {
//                scale = (float) vHeight / (float) dHeight;
//
//                dx = (vWidth - dWidth * scale) * 0.5f;
//            }
////            已经计算scale, dy
////            else {
////                scale = (float) vWidth / (float) dWidth;
////                dy = (vHeight - dHeight * scale) * 0.5f;
////            }
//        }
//
//        drawMatrix.setScale(scale, scale);
//        drawMatrix.postTranslate(Math.round(dx), Math.round(dy));
//        return drawMatrix;
//    }
}

















