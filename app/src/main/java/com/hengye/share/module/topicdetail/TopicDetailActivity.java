package com.hengye.share.module.topicdetail;

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
import com.hengye.share.module.topic.StatusActionContract;
import com.hengye.share.module.topic.StatusActionMvpImpl;
import com.hengye.share.module.topic.StatusActonPresenter;
import com.hengye.share.ui.widget.util.DrawableLoader;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.TransitionHelper;
import com.hengye.share.model.Topic;
import com.hengye.share.model.greenrobot.TopicDraftHelper;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.profile.PersonalHomepageActivity;
import com.hengye.share.module.publish.TopicPublishActivity;
import com.hengye.share.module.topic.TopicAdapter;
import com.hengye.share.ui.widget.OverLayView;
import com.hengye.share.ui.widget.fab.FabAnimator;
import com.hengye.share.ui.widget.pulltorefresh.PullToRefreshLayout;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.IntentUtil;

import me.imid.swipebacklayout.lib.Utils;

public class TopicDetailActivity extends BaseActivity
        implements View.OnClickListener,
        AppBarLayout.OnOffsetChangedListener,
        StatusActionContract.View {

    public static void start(Context context, View startView, Topic topic, boolean isRetweet) {
        Intent intent = getStartIntent(context, topic, isRetweet);
        TransitionHelper.startTransitionActivity(context, intent, startView, R.string.transition_name_topic);
    }

    public static Intent getStartIntent(Context context, Topic topic, boolean isRetweet) {
        Intent intent = new Intent(context, TopicDetailActivity.class);
        intent.putExtra("topic", topic);
        intent.putExtra("isRetweet", isRetweet);
        return intent;
    }

    Topic mTopic;
    boolean mIsRetweet;

    @Override
    protected void handleBundleExtra(Intent intent) {
        mTopic = (Topic) intent.getSerializableExtra("topic");
        mIsRetweet = intent.getBooleanExtra("isRetweet", false);
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_topic_detail;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mTopic == null) {
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
    View mTopicContentLayout;
    TextView mTopicContent;
    FloatingActionsMenu mActionsMenu;
    FloatingActionButton mCommentBtn, mRepostBtn, mAttitudeBtn, mCollectBtn, mCopyBtn;
    OverLayView mOverLay;
    TopicCommentAndRepostFragment mFragment;
    StatusActionMvpImpl mStatusActionMvpImpl;
    StatusActionContract.Presenter mStatusActionPresenter;
    boolean mHeaderViewVisibleInScreen;
//    TopicDetailPresenter mPresenter;

    private void initHeaderTopic(View headerView) {
        View topicLayout = headerView.findViewById(R.id.item_topic_total);
        if (mIsRetweet) {
            mTopicContentLayout = headerView.findViewById(R.id.ll_topic_content);
            mTopicContent = (TextView) headerView.findViewById(R.id.tv_topic_content);
            mTopicContentLayout.setTransitionName(getString(R.string.transition_name_topic));
        } else {
            topicLayout.setTransitionName(getString(R.string.transition_name_topic));
        }
        final TopicAdapter.TopicDefaultViewHolder topicViewHolder = new TopicAdapter.TopicDefaultViewHolder(topicLayout);
        topicViewHolder.bindData(this, mTopic, 0);
        topicViewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.iv_topic_avatar || id == R.id.tv_topic_username || id == R.id.tv_topic_description) {
                    //为了显示波纹效果再启动
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            PersonalHomepageActivity.start(TopicDetailActivity.this, topicViewHolder.mTopicTitle.mAvatar, mTopic.getUserInfo());
                        }
                    }, 100);
                } else if (id == R.id.tv_topic_content || id == R.id.gl_topic_gallery || id == R.id.item_topic_retweeted_content) {
                    final boolean isRetweeted = (Boolean) view.getTag();
                    if (!isRetweeted) {
                        return;
                    }
                    //为了显示波纹效果再启动
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TopicDetailActivity.start(TopicDetailActivity.this,
                                    topicViewHolder.mRetweetTopic.mTopicLayout,
                                    mTopic.getRetweetedTopic(),
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
        //评论的点赞是用黑色的按钮，有时候会导致这里的按钮也是黑色的，所以在着色一次
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

        mHeaderView = findViewById(R.id.item_topic_total);
        initHeaderTopic(mHeaderView);

        mPullToRefresh = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
        mFragment = TopicCommentAndRepostFragment.newInstance(mTopic);
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
                        TopicPublishActivity.getStartIntent(this, TopicDraftHelper.getWBTopicDraftByRepostComment(mTopic)));
            } else if (id == R.id.action_repost) {
                IntentUtil.startActivity(this,
                        TopicPublishActivity.getStartIntent(this, TopicDraftHelper.getWBTopicDraftByRepostRepost(mTopic)));
            } else if (id == R.id.action_attitude) {
                mStatusActionPresenter.likeStatus(mTopic);
            } else if (id == R.id.action_collect) {
                mStatusActionPresenter.collectStatus(mTopic);
            } else if (id == R.id.action_copy) {
                ClipboardUtil.copyWBContent(mTopic.getContent());
            }
        }
    }

    @Override
    public void onLikeStatusComplete(Topic topic, int taskState) {
        mStatusActionMvpImpl.onLikeStatusComplete(topic, taskState);
        updateAttitudeBtn();
    }

    @Override
    public void onCollectStatusComplete(Topic topic, int taskState) {
        mStatusActionMvpImpl.onCollectStatusComplete(topic, taskState);
        updateCollectBtn();
    }

    private void updateAttitudeBtn() {
        String attitude = getString(
                mTopic.isLiked() ?
                        R.string.label_topic_attitude_cancel :
                        R.string.label_topic_attitude);
        mAttitudeBtn.setTitle(attitude);
    }

    private void updateCollectBtn() {
        String favorite = getString(mTopic.isFavorited() ?
                R.string.label_topic_collect_cancel :
                R.string.label_topic_collect);
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
//            if (mTopicContentLayout != null && mTopicContent != null && mIsRetweet) {
////                mTopicContent.setText(DataUtil.addRetweetedNamePrefix(mTopic));
//                mTopicContentLayout.setBackgroundColor(getResources().getColor(R.color.grey_50));
//            }
//        }
//        super.onBackPressed();
    }

}
