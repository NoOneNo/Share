package com.hengye.share.module.topicdetail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.TextView;

import com.hengye.floatingactionbutton.FloatingActionButton;
import com.hengye.floatingactionbutton.FloatingActionsMenu;
import com.hengye.share.R;
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

public class TopicDetail2Activity extends BaseActivity implements View.OnClickListener, AppBarLayout.OnOffsetChangedListener {

    public static void start(Context context, View startView, Topic topic, boolean isRetweet) {
        Intent intent = getStartIntent(context, topic, isRetweet);
        TransitionHelper.startTransitionActivity(context, intent, startView, R.string.transition_name_topic);
    }

    public static Intent getStartIntent(Context context, Topic topic, boolean isRetweet) {
        Intent intent = new Intent(context, TopicDetail2Activity.class);
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
        return R.layout.activity_topic_detail2;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if (mTopic == null) {
            finish();
            return;
        }
//        addPresenter(mPresenter = new TopicDetailPresenter(this));
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
    FloatingActionButton mCopyBtn, mCommentBtn, mRepostBtn;
    OverLayView mOverLay;
    TopicCommentAndRepostFragment mFragment;

//    TopicDetailPresenter mPresenter;

    private void initHeaderTopic(View headerView) {
        View topicLayout = headerView.findViewById(R.id.item_topic);
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
                            PersonalHomepageActivity.start(TopicDetail2Activity.this, topicViewHolder.mTopicTitle.mAvatar, mTopic.getUserInfo());
                        }
                    }, 100);
                } else if (id == R.id.tv_topic_content || id == R.id.gl_topic_gallery || id == R.id.ll_topic_retweeted_content) {
                    final boolean isRetweeted = (Boolean) view.getTag();
                    if (!isRetweeted) {
                        return;
                    }
                    //为了显示波纹效果再启动
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TopicDetail2Activity.start(TopicDetail2Activity.this,
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
        mCopyBtn = (FloatingActionButton) findViewById(R.id.action_copy);
        mRepostBtn = (FloatingActionButton) findViewById(R.id.action_repost);
        mCommentBtn = (FloatingActionButton) findViewById(R.id.action_comment);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mTabLayout = (TabLayout) findViewById(R.id.tab);
//        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mOverLay.setOnClickListener(this);
        mCopyBtn.setOnClickListener(this);
        mRepostBtn.setOnClickListener(this);
        mCommentBtn.setOnClickListener(this);

        FabAnimator.adjustFloatingActionsMenuUpdate(mActionsMenu, mOverLay);

        mHeaderView = findViewById(R.id.item_topic);
        initHeaderTopic(mHeaderView);

        mPullToRefresh = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
        mFragment = TopicCommentAndRepostFragment.newInstance(mTopic);
        mFragment.setPullToRefresh(mPullToRefresh);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, mFragment)
                .commit();

//        FabAnimator
//                .create(mActionsMenu)
//                .attachToListView(mListView, onScrollListener)
//                .setCustomAnimator(new FabAnimator.CustomAnimator() {
//                    @Override
//                    public int getViewHeight() {
//                        return mActionsMenu.getAddButtion().getHeight();
//                    }
//                });

    }

    boolean mHasSetSelection;
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (verticalOffset >= 0) {
            mPullToRefresh.setEnabled(true);
        } else {
            mPullToRefresh.setEnabled(false);
        }

        if(!mHasSetSelection && Math.abs(verticalOffset) < mHeaderView.getHeight()){
            //看见头部，让viewpager其他tab也选中第一个位置;
            if(mFragment != null) {
                mHasSetSelection = true;
                mFragment.setOtherTabScrollToTop();
            }
        }else{
            mHasSetSelection = false;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.over_lay) {
            mActionsMenu.collapse();
        } else if (id == R.id.action_copy) {
            ClipboardUtil.copyWBContent(mTopic.getContent());
            mActionsMenu.collapseImmediately();
        } else if (id == R.id.action_repost) {
            mActionsMenu.collapseImmediately();
            IntentUtil.startActivity(this,
                    TopicPublishActivity.getStartIntent(this, TopicDraftHelper.getWBTopicDraftByRepostRepost(mTopic)));
        } else if (id == R.id.action_comment) {
            mActionsMenu.collapseImmediately();
            IntentUtil.startActivity(this,
                    TopicPublishActivity.getStartIntent(this, TopicDraftHelper.getWBTopicDraftByRepostComment(mTopic)));
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
