package com.hengye.share.module.profile;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.Parent;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.User;
import com.hengye.share.model.sina.WBUserInfo;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.sso.UserContract;
import com.hengye.share.module.sso.UserPresenter;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.ui.widget.PersonalHomePageToolbarLayout;
import com.hengye.share.ui.widget.ScrollChildSwipeRefreshLayout;
import com.hengye.share.ui.widget.image.AvatarImageView;
import com.hengye.share.ui.widget.image.SuperImageView;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.TransitionHelper;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.ViewUtil;

public class PersonalHomepageActivity extends BaseActivity
        implements View.OnClickListener,
        AppBarLayout.OnOffsetChangedListener,
        UserContract.View, UserAttentionContract.View {

    public static void start(Context context, View startView, UserInfo userInfo) {
        if (startView == null) {
            start(context, userInfo);
        } else {
            Intent intent = getStartIntent(context, userInfo);
            TransitionHelper.startTransitionActivity(context, intent, startView, R.string.transition_name_avatar);
        }
    }

    public static void start(Context context, UserInfo userInfo) {
        context.startActivity(getStartIntent(context, userInfo));
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
    protected void handleBundleExtra(Intent intent) {
        mUserInfo = (UserInfo) intent.getSerializableExtra(UserInfo.class.getSimpleName());

        if (mUserInfo == null) {
            Uri data = intent.getData();
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
    public int getLayoutResId() {
        return R.layout.activity_personal_homepage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initStatusBar();
        super.onCreate(savedInstanceState);
        if (mUserInfo == null) {
            PersonalHomepageActivity.this.finish();
        } else {
            mPresenter = new UserPresenter(this);
            mUserAttentionPresenter = new UserAttentionPresenter(this);
            initView();
        }
    }

    @Override
    public void onToolbarDoubleClick(Toolbar toolbar) {
        if (mFragment != null) {
            mFragment.onToolbarDoubleClick(toolbar);
        }
    }

    private View mAssistNavigationBtn;
    private TextView mDivision, mAttention, mFans;
    //    private TextView mSign;
    private SuperImageView mCover;
    private AvatarImageView mAvatar;
    private PersonalHomePageToolbarLayout mCollapsingToolbarLayout;
    private ScrollChildSwipeRefreshLayout mSwipeRefresh;
    private AppBarLayout mAppBarLayout;
    private CoordinatorLayout mCoordinatorLayout;
    private View mUserInfoLayout;
    private View mUserDescription;
    //    private FloatingActionButton mFollowButton;
//    private CheckableFab mFollowButton;
    private TextView mFollowButton;
    private TabLayout mTab;

    private UserInfo mUserInfo;
//    private WBUserInfo mWBUserInfo;

    private UserContract.Presenter mPresenter;
    private UserAttentionContract.Presenter mUserAttentionPresenter;

    @SuppressWarnings("ConstantConditions")
    private void initView() {

        initToolbar();
        updateToolbarTitle("");
        if (getToolbar() != null) {
            getToolbar().setBackground(null);
        }

        mAssistNavigationBtn = findViewById(R.id.btn_navigation_assist);
        mAssistNavigationBtn.setOnClickListener(this);
        mFollowButton = (TextView) findViewById(R.id.btn_attention);
        mFollowButton.setOnClickListener(this);
        mCollapsingToolbarLayout =
                (PersonalHomePageToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mCollapsingToolbarLayout.setTitle("");
        mUserDescription = findViewById(R.id.ll_user_desc);
        mCover = (SuperImageView) findViewById(R.id.iv_cover);
        mCover.setFadeInImage(false);
        mCover.setAutoClipBitmap(false);

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
        mAvatar = (AvatarImageView) findViewById(R.id.iv_avatar);
        mAvatar.setTransitionName(getString(R.string.transition_name_avatar));
        mAvatar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                setUpAvatarToToolbar();
            }
        });

        mUserInfoLayout = findViewById(R.id.fl_user_info);
        mDivision = (TextView) findViewById(R.id.tv_division);
        mAttention = (TextView) findViewById(R.id.tv_attention);
        mFans = (TextView) findViewById(R.id.tv_fans);

        mUserInfoLayout.setOnTouchListener(mOnAssistToolbarTouchListener);
//        mSign = (TextView) findViewById(R.id.tv_sign);

        mAttention.setOnClickListener(this);
        mFans.setOnClickListener(this);

        mTab = (TabLayout) findViewById(R.id.tab);

        mSwipeRefresh = (ScrollChildSwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        mSwipeRefresh.setColorSchemeResources(android.R.color.holo_blue_dark,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mSwipeRefresh.setOnTouchListener(new SwipeRefreshOnTouchListener(mCollapsingToolbarLayout, ResUtil.getDimensionPixelSize(R.dimen.header_personal_homepage_height)));


        initUserInfo(mUserInfo);
        if(UserUtil.isCurrentUser(mUserInfo.getUid())){
            updateUserInfo();
        }

//        if (mUserInfo.getParent().isWeiBo()) {
//            WBUserInfo wbUserInfo = mUserInfo.getWBUserInfoFromParent();
//            if (wbUserInfo != null) {
//                initUserInfo(wbUserInfo);
//            } else {
//                mSwipeRefresh.setRefreshing(true);
//                updateUserInfo();
//            }
//
//            //如果在刷新中代表前面自己信息是空的，已经在更新信息了；
//            if(!mSwipeRefresh.isRefreshing() && UserUtil.isCurrentUser(mUserInfo.getUid())){
//                updateUserInfo();
//            }
//
//        }
        postponeEnterTransition();
        mAvatar.setVisibility(View.INVISIBLE);
        setUpAvatar();
    }

    private View.OnTouchListener mOnAssistToolbarTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (mCollapsingToolbarLayout.isShowScrims()) {
                //当是闭合的时候，模拟toolbar的触摸事件
                return getToolbar().onTouchEvent(event);
            } else {
                return false;
            }
        }
    };

    private void initStatusBar() {
//        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        Window window = getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
//                | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        );//| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
////            window.setStatusBarColor(Utils.resolveColor(activity, R.attr.theme_statusbar_color, Color.BLUE));
//        window.setStatusBarColor(Color.parseColor("#20000000"));
////        window.setNavigationBarColor(getResources().getColor(R.attr.theme_color));
    }

    private void setUpAvatar() {
        mUserInfoLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mUserInfoLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int height = mUserInfoLayout.getHeight();

                //调整辅助导航按钮点击的高度
                mAssistNavigationBtn.getLayoutParams().height = height;
                mAssistNavigationBtn.requestLayout();

                //调整头像位置
                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mAvatar.getLayoutParams();
                int targetHeight = height - mAvatar.getHeight() / 2;
                if (targetHeight != lp.bottomMargin) {
                    lp.bottomMargin = targetHeight;
                    mAvatar.requestLayout();
                }
                mAvatar.setVisibility(View.VISIBLE);
                startPostponedEnterTransition();
            }
        });
    }

    private void setUpAvatarToToolbar() {
        if (mAvatar.getDrawable() == null || !(mAvatar.getDrawable() instanceof BitmapDrawable)) {
            return;
        }
        BitmapDrawable bd = (BitmapDrawable) mAvatar.getDrawable();
        final Bitmap bitmap = bd.getBitmap();
        //等待头像的转场动画完毕再设置bitmap
        getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mCollapsingToolbarLayout.setAvatarBitmap(bitmap);
                mAvatar.setVisibility(View.INVISIBLE);
            }
        }, 350);
    }

    Animator coverShowAnimator;

    private void startCoverShowAnimation() {

        coverShowAnimator = ViewAnimationUtils.createCircularReveal(mAppBarLayout,
                mAppBarLayout.getWidth() / 2,
                mAppBarLayout.getHeight() / 2,
                0,
                (float) Math.hypot(mAppBarLayout.getWidth(), mAppBarLayout.getWidth()));
        coverShowAnimator.setInterpolator(new FastOutLinearInInterpolator());
        coverShowAnimator.setDuration(500);
        coverShowAnimator.start();
    }

    boolean mHasSetSelection;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset >= -50) {

            mAssistNavigationBtn.setVisibility(View.GONE);

            mSwipeRefresh.setRefreshEnable(true);
            if (!mHasSetSelection && mFragment != null) {
                mHasSetSelection = true;
                mFragment.setOtherTabScrollToTop();
            }

        } else {

            mAssistNavigationBtn.setVisibility(View.VISIBLE);

            mHasSetSelection = false;
            mSwipeRefresh.setRefreshEnable(false);
        }

//        if(mFragment != null && mFragment.getViewPager() != null) {
//
//            boolean isEnabled = true;
//
//            //当收缩时
//            if(verticalOffset < -50){
//                //如果刚好收缩到底部
//                isEnabled = Math.abs(verticalOffset) == mCollapsingToolbarLayout.getMaxVerticalOffset();
//            }
//
////            mFragment.getViewPager().setEnabled(isEnabled);
//            L.debug("viewpager set enbaled : {}", isEnabled);
//        }
//
//        L.debug("maxVerticalOffset : {}", mCollapsingToolbarLayout.getMaxVerticalOffset());
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

    private void setupFragment(WBUserInfo wbUserInfo) {
        if (isDestroyed() || isFinishing()) {
            return;
        }
        if(mFragment != null){
            mFragment.updateUserInfo(wbUserInfo);
        }else{
            mFragment = PersonalHomepageFragment.newInstance(wbUserInfo);
            mFragment.setSwipeRefresh(mSwipeRefresh);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content, mFragment)
                    .commit();
        }
    }

    private void initUserInfo(UserInfo userInfo) {
//        mWBUserInfo = wbUserInfo;
        mCollapsingToolbarLayout.setTitle(" " + userInfo.getName());
//        mCover.setImageResource(R.drawable.bg_test);
        mCover.setImageUrl(userInfo.getCover());
        mAvatar.setImageUrl(userInfo.getAvatar());
        mDivision.setVisibility(View.VISIBLE);
        mAttention.setText(String.format(getString(R.string.label_attention_count), DataUtil.getCounter(userInfo.getFriendCount())));
        mFans.setText(String.format(getString(R.string.label_fans_count), DataUtil.getCounter(userInfo.getFollowerCount())));
//        mSign.setText(wbUserInfo.getDescription());
//        mFollowButton.setChecked(mWBUserInfo.isFollowing());
        updateFollowButton();
        mUserDescription.setVisibility(View.VISIBLE);
        mUserDescription.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mUserDescription.getViewTreeObserver().removeOnPreDrawListener(this);
                float textHeight = ViewUtil.sp2px(R.dimen.text_large);
                int avatarMarginTop = ViewUtil.dp2px(R.dimen.header_personal_avatar_margin_top);
                int descHeight = mUserDescription.getHeight();
                float marginBottom = descHeight + (avatarMarginTop - textHeight) / 2;
                //设置展开时标题的位置
                mCollapsingToolbarLayout.setExpandedTitleMarginBottom((int) marginBottom);

//                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mFollowButton.getLayoutParams();
//                lp.bottomMargin = descHeight;
//                mFollowButton.setLayoutParams(lp);
//                mFollowButton.requestLayout();
                return true;
            }
        });

        loadData(userInfo);
    }

    public void updateUserInfo(){
        mPresenter.loadWBUserInfo(mUserInfo.getUid(), mUserInfo.getName());
    }

    private void updateFollowButton() {
        mFollowButton.setText(mUserInfo.getFollowRelation());
    }

    private void loadData(final UserInfo userInfo) {
        if (userInfo == null || userInfo.getParent().getType() != Parent.TYPE_WEIBO) {
            return;
        }

        final WBUserInfo wbUserInfo = userInfo.getWBUserInfoFromParent();
        if(wbUserInfo == null){
            return;
        }

        if (mFragment == null) {
            if (coverShowAnimator != null && !coverShowAnimator.isRunning()) {
                setupFragment(wbUserInfo);
            } else {
                getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setupFragment(wbUserInfo);
                    }
                }, 500);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_navigation_assist) {
            if (mCollapsingToolbarLayout.isShowScrims()) {
                //当时闭合的时候，模拟点击toolbar的导航按钮
                onNavigationClick(getToolbar());
            }
        } else if (mUserInfo!= null) {
            if (id == R.id.btn_attention) {
                mUserAttentionPresenter.followUser(mUserInfo);
            } else if (id == R.id.tv_attention) {
                if (mUserInfo.getUid() != null) {
                    UserAttentionsFragment.start(this, mUserInfo.getUid());
                }
            } else if (id == R.id.tv_fans) {
                if (mUserInfo.getUid() != null) {
                    UserFollowersFragment.start(this, mUserInfo.getUid());
                }
            }
        }


    }

    @Override
    public void handleUserInfo(UserInfo wbUserInfo) {
        mSwipeRefresh.setRefreshing(false);
        if (wbUserInfo != null) {
            initUserInfo(wbUserInfo);
            setUpAvatar();
        }
    }

    @Override
    public void loadSuccess(User user) {

    }

    @Override
    public void loadFail() {
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    public void onFollowStart() {
        mFollowButton.setEnabled(false);
    }

    @Override
    public void onFollowComplete(int taskState) {
        mFollowButton.setEnabled(true);
        if(taskState == TaskState.STATE_SUCCESS){
            if (mUserInfo.isFollowing()) {
                ToastUtil.showToastSuccess(R.string.label_follow_create_success);
            } else {
                ToastUtil.showToastSuccess(R.string.label_follow_destroy_success);
            }
        }else {
            TaskState.toastFailState(taskState);
        }
    }

    @Override
    public void onFollowSuccess(UserInfo userInfo) {
        updateFollowButton();
    }

    public static class SwipeRefreshOnTouchListener implements View.OnTouchListener {

        public SwipeRefreshOnTouchListener(CollapsingToolbarLayout collapsingToolbarLayout, int headerHeight) {
            mHeaderHeight = headerHeight;
            mCollapsingToolbarLayout = collapsingToolbarLayout;
        }

        private int getMaxHeaderHeight() {
            if (mMaxHeaderHeight == 0) {
                mMaxHeaderHeight = (int) (mHeaderHeight * MAX_APP_BAR_HEIGHT_RATE);
            }
            return mMaxHeaderHeight;
        }

        private static final float MAX_APP_BAR_HEIGHT_RATE = 1.5f;
        private static final float DRAG_RATE = .5f;
        private int mHeaderHeight, mMaxHeaderHeight;
        private float mInitialDownY;
        private boolean mIsBeingDragged;
        private ValueAnimator mAnimateToCorrectHeight;
        private CollapsingToolbarLayout mCollapsingToolbarLayout;

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
                    mAnimateToCorrectHeight = ValueAnimator.ofInt(lp.height, mHeaderHeight);
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
                        break;
                    }

                    float currentY = event.getRawY();
                    int distance = (int) (DRAG_RATE * (currentY - mInitialDownY));

                    if (distance > 0) {
                        ViewGroup.LayoutParams lp_ = mCollapsingToolbarLayout.getLayoutParams();
                        int height = mHeaderHeight + distance;
                        if (height > getMaxHeaderHeight()) {
                            if (lp_.height == getMaxHeaderHeight()) {
                                //如果已经到达高度的最大值
                                break;
                            }
                            height = getMaxHeaderHeight();
                        }
                        lp_.height = height;
                        mCollapsingToolbarLayout.requestLayout();

                    }

                    break;
            }
            return false;
        }
    }

}















