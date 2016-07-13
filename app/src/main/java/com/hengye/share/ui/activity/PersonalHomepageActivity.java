package com.hengye.share.ui.activity;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.R;
import com.hengye.share.helper.TransitionHelper;
import com.hengye.share.model.Parent;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.fragment.TopicFragment;
import com.hengye.share.ui.mvpview.UserMvpView;
import com.hengye.share.ui.presenter.TopicPresenter;
import com.hengye.share.ui.presenter.UserPresenter;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
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
            setupPresenter(mPresenter = new UserPresenter(this));
            initView();
            loadData();
        }
    }

    private TextView mDivision, mAttention, mFans, mSign;
    private NetworkImageViewPlus mCover, mAvatar;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private SwipeRefreshLayout mSwipeRefresh;
    private AppBarLayout mAppBarLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private FloatingActionButton mFollowButton;

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

        mFollowButton = (FloatingActionButton) findViewById(R.id.fab);
        mFollowButton.setOnClickListener(this);
        mCollapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        mCover = (NetworkImageViewPlus) findViewById(R.id.iv_cover);
        mCover.setFadeInImage(false);
//        mCover.setScaleType(ImageView.ScaleType.MATRIX);
//        mCover.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                mCover.setImageMatrix(setCoverMatrix());
//            }
//        });

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
//        postponeEnterTransition();
        mCover.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mCover.getViewTreeObserver().removeOnPreDrawListener(this);
//                startPostponedEnterTransition();
                startCoverShowAnimation();
                return true;
            }
        });
        mAvatar = (NetworkImageViewPlus) findViewById(R.id.iv_avatar);
        mAvatar.setTransitionName(getString(R.string.transition_name_avatar));

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

                        final ViewGroup.LayoutParams lp = mAppBarLayout.getLayoutParams();
                        mAnimateToCorrectHeight = ValueAnimator.ofInt(lp.height, mAppBarHeight);
                        mAnimateToCorrectHeight.setDuration(200);
                        mAnimateToCorrectHeight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                lp.height = (int) animation.getAnimatedValue();
                                mAppBarLayout.requestLayout();
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
                            mAppBarHeight = mAppBarLayout.getLayoutParams().height;

                            break;
                        }

                        float currentY = event.getRawY();
                        int distance = (int) (DRAG_RATE * (currentY - mInitialDownY));

                        if (distance > 0) {
                            ViewGroup.LayoutParams lp_ = mAppBarLayout.getLayoutParams();
                            int height = mAppBarHeight + distance;
                            if (height > getAppBarMaxHeight()) {
                                if (lp_.height == getAppBarMaxHeight()) {
                                    //如果已经到达高度的最大值
                                    break;
                                }
                                height = getAppBarMaxHeight();
                            }
                            lp_.height = height;
                            mAppBarLayout.requestLayout();

                        }

                        break;
                }
                return false;
            }
        });

    }

    private void startCoverShowAnimation() {

        final Animator coverShowAnimator = ViewAnimationUtils.createCircularReveal(mAppBarLayout,
                mAppBarLayout.getWidth() / 2,
                mAppBarLayout.getHeight() / 2,
                0,
                (float) Math.hypot(mAppBarLayout.getWidth(), mAppBarLayout.getWidth()));
        coverShowAnimator.setInterpolator(new FastOutLinearInInterpolator());
        coverShowAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
//                startPostponedEnterTransition();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        coverShowAnimator.setDuration(500);
        coverShowAnimator.start();
    }

    private int getAppBarMaxHeight() {
        if (mAppBarMaxHeight == 0) {
            mAppBarMaxHeight = (int) (mAppBarHeight * MAX_APP_BAR_HEIGHT_RATE);
        }
        return mAppBarMaxHeight;
    }

    private static final float MAX_APP_BAR_HEIGHT_RATE = 1.5f;
    private static final float DRAG_RATE = .5f;
    private int mAppBarHeight, mAppBarMaxHeight;
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

    TopicFragment mTopicFragment;

    private void setupTopicFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, mTopicFragment = TopicFragment.newInstance(TopicPresenter.TopicType.HOMEPAGE, mUserInfo.getUid(), mUserInfo.getName()))
                .commit();

        mTopicFragment.setLoadDataCallBack(new TopicFragment.LoadDataCallBack() {
            @Override
            public void initView() {
                RecyclerView recyclerView = (RecyclerView) mTopicFragment.findViewById(R.id.recycler_view);
//                recyclerView.setNestedScrollingEnabled(false);
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
        mWBUserInfo = wbUserInfo;
        mCollapsingToolbarLayout.setTitle(wbUserInfo.getName());
//        mCover.setImageResource(R.drawable.bg_test);
        mCover.setAutoClipBitmap(false);
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

















