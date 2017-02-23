package com.hengye.share.module.statusdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.hengye.floatingactionbutton.FloatingActionButton;
import com.hengye.floatingactionbutton.FloatingActionsMenu;
import com.hengye.share.R;
import com.hengye.share.module.status.StatusActionContract;
import com.hengye.share.module.status.StatusActionMvpImpl;
import com.hengye.share.module.status.StatusActonPresenter;
import com.hengye.share.ui.widget.util.DrawableLoader;
import com.hengye.share.util.TransitionHelper;
import com.hengye.share.model.Status;
import com.hengye.share.model.greenrobot.StatusDraftHelper;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.profile.PersonalHomepageActivity;
import com.hengye.share.module.publish.StatusPublishActivity;
import com.hengye.share.module.status.StatusAdapter;
import com.hengye.share.ui.widget.OverLayView;
import com.hengye.share.ui.widget.fab.FabAnimator;
import com.hengye.share.ui.widget.pulltorefresh.PullToRefreshLayout;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.IntentUtil;

public class StatusDetailActivity extends BaseActivity
        implements View.OnClickListener,
        AppBarLayout.OnOffsetChangedListener,
        StatusActionContract.View {

    public static void start(Context context, View startView, Status topic, boolean isRetweet) {
        Intent intent = getStartIntent(context, topic, isRetweet);
        TransitionHelper.startTransitionActivity(context, intent, startView, R.string.transition_name_status);
    }

    public static Intent getStartIntent(Context context, Status topic, boolean isRetweet) {
        Intent intent = new Intent(context, StatusDetailActivity.class);
        intent.putExtra("status", topic);
        intent.putExtra("isRetweet", isRetweet);
        return intent;
    }

    Status mStatus;
    boolean mIsRetweet;

    @Override
    protected void handleBundleExtra(Intent intent) {
        mStatus = (Status) intent.getSerializableExtra("status");
        mIsRetweet = intent.getBooleanExtra("isRetweet", false);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_status_detail;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mStatus == null) {
            finish();
            return;
        }
        initView();
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

    //    CollapsingToolbarLayout mCollapsingToolbarLayout;
    PullToRefreshLayout mPullToRefresh;
    AppBarLayout mAppBarLayout;
    TabLayout mTabLayout;
    View mHeaderView;
    View mStatusContentLayout;
    TextView mStatusContent;
    FloatingActionsMenu mActionsMenu;
    FloatingActionButton mCommentBtn, mRepostBtn, mAttitudeBtn, mCollectBtn, mCopyBtn;
    OverLayView mOverLay;
    StatusCommentAndRepostFragment mFragment;
    StatusActionMvpImpl mStatusActionMvpImpl;
    StatusActionContract.Presenter mStatusActionPresenter;
    boolean mHeaderViewVisibleInScreen;
//    TopicDetailPresenter mPresenter;

    private void initHeaderStatus(View headerView) {
        View statusLayout = headerView.findViewById(R.id.item_status_total);
        if (mIsRetweet) {
            mStatusContentLayout = headerView.findViewById(R.id.layout_status_content);
            mStatusContent = (TextView) headerView.findViewById(R.id.tv_status_content);
            mStatusContentLayout.setTransitionName(getString(R.string.transition_name_status));
        } else {
            statusLayout.setTransitionName(getString(R.string.transition_name_status));
        }
        final StatusAdapter.StatusDefaultViewHolder statusViewHolder = new StatusAdapter.StatusDefaultViewHolder(statusLayout);
        statusViewHolder.bindData(this, mStatus, 0);
        if(statusViewHolder.mStatusOptions != null){
            statusViewHolder.mStatusOptions.mStatusOptionsLayout.setVisibility(View.GONE);
        }
        statusViewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.iv_status_avatar || id == R.id.tv_status_username || id == R.id.tv_status_description) {
                    //为了显示波纹效果再启动
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            PersonalHomepageActivity.start(StatusDetailActivity.this, statusViewHolder.mStatusTitle.mAvatar, mStatus.getUserInfo());
                        }
                    }, 100);
                } else if (id == R.id.tv_status_content || id == R.id.gl_status_gallery || id == R.id.item_status_retweeted_content) {
                    final boolean isRetweeted = (Boolean) view.getTag();
                    if (!isRetweeted) {
                        return;
                    }
                    //为了显示波纹效果再启动
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            StatusDetailActivity.start(StatusDetailActivity.this,
                                    statusViewHolder.mRetweetStatus.mStatusLayout,
                                    mStatus.getRetweetedStatus(),
                                    true);
                        }
                    }, 200);
                }
            }
        });
    }

    private void initView() {
        mOverLay = (OverLayView) findViewById(R.id.over_lay);
        mActionsMenu = (FloatingActionsMenu) findViewById(R.id.fab);
        mCommentBtn = (FloatingActionButton) findViewById(R.id.action_comment);
        mRepostBtn = (FloatingActionButton) findViewById(R.id.action_repost);
        mAttitudeBtn = (FloatingActionButton) findViewById(R.id.action_attitude);
        mCollectBtn = (FloatingActionButton) findViewById(R.id.action_collect);
        mCopyBtn = (FloatingActionButton) findViewById(R.id.action_copy);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab);
        //转发、评论、点赞在外部用过的话可能不是白色，所以需要着色
        mRepostBtn.setIconDrawable(DrawableLoader.setTintResource(R.drawable.ic_repost_white_48dp, R.color.white));
        mCommentBtn.setIconDrawable(DrawableLoader.setTintResource(R.drawable.ic_comment_white_48dp, R.color.white));
        mAttitudeBtn.setIconDrawable(DrawableLoader.setTintResource(R.drawable.ic_thumb_up_white_48dp, R.color.white));
//        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mStatusActionPresenter = new StatusActonPresenter(this);
        mStatusActionMvpImpl = new StatusActionMvpImpl(this, mStatusActionPresenter);
        updateAttitudeBtn();
        updateCollectBtn();

        mOverLay.setOnClickListener(this);
        mCommentBtn.setOnClickListener(this);
        mRepostBtn.setOnClickListener(this);
        mAttitudeBtn.setOnClickListener(this);
        mCollectBtn.setOnClickListener(this);
        mCopyBtn.setOnClickListener(this);

        FabAnimator.adjustFloatingActionsMenuUpdate(mActionsMenu, mOverLay);
        mFabAnimator = FabAnimator.create(mActionsMenu);

        mHeaderView = findViewById(R.id.item_status_total);
        initHeaderStatus(mHeaderView);

        mPullToRefresh = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
        mFragment = StatusCommentAndRepostFragment.newInstance(mStatus);
        mFragment.setPullToRefresh(mPullToRefresh);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, mFragment)
                .commit();
    }

    FabAnimator mFabAnimator;
    boolean mHasSetSelection;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//        L.debug("verticalOffset %s ", verticalOffset);
        if (verticalOffset >= -200) {
            mFabAnimator.show();
        } else {
            mFabAnimator.hide();
        }

        if (verticalOffset >= -50) {
            mPullToRefresh.setRefreshEnable(true);
        } else {
            mPullToRefresh.setRefreshEnable(false);
        }

        mHeaderViewVisibleInScreen = Math.abs(verticalOffset) < mHeaderView.getHeight();

        if (!mHasSetSelection && mHeaderViewVisibleInScreen) {
            //看见头部，让viewpager其他tab也选中第一个位置;
            if (mFragment != null) {
                mHasSetSelection = true;
                mFragment.setOtherTabScrollToTop();
            }
        } else {
            mHasSetSelection = false;
        }
    }

    @Override
    public void onToolbarDoubleClick(Toolbar toolbar) {
        mAppBarLayout.setExpanded(true);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.over_lay) {
            mActionsMenu.collapse();
        } else {
            mActionsMenu.collapseImmediately();
            if (id == R.id.action_comment) {
                IntentUtil.startActivity(this,
                        StatusPublishActivity.getStartIntent(this, StatusDraftHelper.getWBStatusDraftByRepostComment(mStatus)));
            } else if (id == R.id.action_repost) {
                IntentUtil.startActivity(this,
                        StatusPublishActivity.getStartIntent(this, StatusDraftHelper.getWBStatusDraftByRepostRepost(mStatus)));
            } else if (id == R.id.action_attitude) {
                mStatusActionPresenter.likeStatus(mStatus);
            } else if (id == R.id.action_collect) {
                mStatusActionPresenter.collectStatus(mStatus);
            } else if (id == R.id.action_copy) {
                ClipboardUtil.copyWBContent(mStatus.getContent());
            }
        }
    }

    @Override
    public void onLikeStatusComplete(Status topic, int taskState) {
        mStatusActionMvpImpl.onLikeStatusComplete(topic, taskState);
        updateAttitudeBtn();
    }

    @Override
    public void onCollectStatusComplete(Status topic, int taskState) {
        mStatusActionMvpImpl.onCollectStatusComplete(topic, taskState);
        updateCollectBtn();
    }

    private void updateAttitudeBtn() {
        String attitude = getString(
                mStatus.isLiked() ?
                        R.string.label_status_attitude_cancel :
                        R.string.label_status_attitude);
        mAttitudeBtn.setTitle(attitude);
    }

    private void updateCollectBtn() {
        String favorite = getString(mStatus.isFavorited() ?
                R.string.label_status_collect_cancel :
                R.string.label_status_collect);
        mCollectBtn.setTitle(favorite);
    }

    @Override
    protected boolean canSwipeBack() {
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
        //不设置转场动画是因为跟滑动退出冲突，有滑动退出时，转场会黑屏
//        if (mHeaderViewVisibleInScreen) {
//            if (mStatusContentLayout != null && mStatusContent != null && mIsRetweet) {
////                mStatusContent.setText(DataUtil.addRetweetedStatusNamePrefix(mComment));
//                mStatusContentLayout.setBackgroundColor(getResources().getColor(R.color.grey_50));
//            }
//        }
//        super.onBackPressed();
    }

}
