package com.hengye.share.ui.activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.adapter.listview.TopicCommentAdapter;
import com.hengye.share.adapter.recyclerview.TopicAdapter;
import com.hengye.share.helper.TransitionHelper;
import com.hengye.share.model.Topic;
import com.hengye.share.model.TopicComment;
import com.hengye.share.model.greenrobot.TopicDraftHelper;
import com.hengye.share.service.TopicPublishService;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.mvpview.TopicDetailMvpView;
import com.hengye.share.ui.presenter.TopicDetailPresenter;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.ui.widget.listener.OnItemClickListener;
import com.hengye.share.ui.widget.fab.FabAnimator;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.thirdparty.WBUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.hengye.swiperefresh.listener.SwipeListener;

import java.util.ArrayList;
import java.util.List;

public class TopicDetailActivity extends BaseActivity implements TopicDetailMvpView, View.OnClickListener, DialogInterface.OnClickListener {

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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPublishResultBroadcastReceiver);
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        if (mTopic == null) {
            finish();
            return;
        }
        addPresenter(mPresenter = new TopicDetailPresenter(this));
        initView();
        initBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(mPublishResultBroadcastReceiver, new IntentFilter(TopicPublishService.ACTION_RESULT));
    }

    @Override
    public boolean onToolbarDoubleClick(Toolbar toolbar) {
        mListView.setSelection(0);
        return true;
    }

    TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            String str = (String) tab.getTag();
            if ("tab_layout_assist".equals(str)) {
                mTabLayout.getTabAt(tab.getPosition()).select();
            } else if ("tab_layout".equals(str)) {
                mTabLayoutAssist.getTabAt(tab.getPosition()).select();

                if (tab.getPosition() == 0) {
                    mAdapter.setData(mCommentData);
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(1);
                    mPullToRefreshLayout.setLoadEnable(mLoadCommentEnable);
                } else if (tab.getPosition() == 1) {
                    mAdapter.setData(mRepostData);
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(1);
                    mPullToRefreshLayout.setLoadEnable(mLoadReposttEnable);
                }
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
    };

    private PullToRefreshLayout mPullToRefreshLayout;
    private ListView mListView;
    private TopicCommentAdapter mAdapter;
    private TabLayout mTabLayout, mTabLayoutAssist;
    private int mTabLayoutHeight;
    private boolean mLoadCommentEnable = true;
    private boolean mLoadReposttEnable = true;

    private View mTopicContentLayout;
    private TextView mTopicContent;
    private FloatingActionButton mFab;
    private Dialog mTopicCommentDialog, mTopicRepostDialog;

    private TopicDetailPresenter mPresenter;
    private BroadcastReceiver mPublishResultBroadcastReceiver;

    private void initHeaderTab(View headerViewAssist) {
        mTabLayout = (TabLayout) headerViewAssist.findViewById(R.id.tab);
        mTabLayout.addTab((mTabLayout.newTab().setText(R.string.label_topic_comment).setTag("tab_layout")));
        mTabLayout.addTab((mTabLayout.newTab().setText(R.string.label_topic_repost).setTag("tab_layout")));
        mTabLayout.getTabAt(0).select();
        mTabLayout.setOnTabSelectedListener(mOnTabSelectedListener);

    }

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
        topicViewHolder.setOnChildViewItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id = view.getId();
                if (id == R.id.tv_topic_content || id == R.id.gl_topic_gallery || id == R.id.ll_topic_retweeted_content) {
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
//            startTopicDetail(isRetweeted, position);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
//        if(mTabLayoutAssist != null && mTabLayoutAssist.getVisibility() == View.GONE){
//            if(mTopicContentLayout != null && mTopicContent != null && mIsRetweet){
//
////                mTopicContent.setText(DataUtil.addRetweetedNamePrefix(mTopic));
//                mTopicContentLayout.setBackgroundColor(getResources().getColor(R.color.grey_50));
//            }
//            finishAfterTransition();
//        }else{
//            finish();
//        }
    }

    private void initView() {
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);

        mTabLayoutHeight = getResources().getDimensionPixelSize(R.dimen.tab_layout_height);
        mTabLayoutAssist = (TabLayout) findViewById(R.id.tab_layout_assist);
        mTabLayoutAssist.addTab((mTabLayoutAssist.newTab().setText(R.string.label_topic_comment).setTag("tab_layout_assist")));
        mTabLayoutAssist.addTab((mTabLayoutAssist.newTab().setText(R.string.label_topic_repost).setTag("tab_layout_assist")));
        mTabLayoutAssist.getTabAt(0).select();
        mTabLayoutAssist.setOnTabSelectedListener(mOnTabSelectedListener);

        View headerView = LayoutInflater.from(this).inflate(R.layout.item_topic_total, null);

        View headerViewAssist = LayoutInflater.from(this).inflate(R.layout.header_topic_detail, null);

        initHeaderTopic(headerView);
        initHeaderTab(headerViewAssist);

        mListView = (ListView) findViewById(R.id.list_view);
        mTopicCommentDialog = DialogBuilder.getTopicCommentDialog(this, this);
        mTopicRepostDialog = DialogBuilder.getTopicRepostDialog(this, this);
        AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {

                    View firstVisibleItemView = view.getChildAt(1);
                    if (firstVisibleItemView == null) {
                        return;
                    }

                    if (firstVisibleItemView.getBottom() <= mTabLayoutHeight) {
                        mTabLayoutAssist.setVisibility(View.VISIBLE);
                    } else {
                        mTabLayoutAssist.setVisibility(View.GONE);
                    }
                } else {
                    mTabLayoutAssist.setVisibility(View.VISIBLE);
                }
            }
        };

        FabAnimator.create(mFab).attachToListView(mListView, onScrollListener);
        mListView.setAdapter(mAdapter = new TopicCommentAdapter(this, new ArrayList<TopicComment>()));
        mListView.addHeaderView(headerView);
        mListView.addHeaderView(headerViewAssist);

        postponeEnterTransition();
        mListView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mListView.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return true;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPosition = position - mListView.getHeaderViewsCount();

                if(isSelectedCommentTab()) {
                    mTopicCommentDialog.show();
                }else{
                    mTopicRepostDialog.show();
                }
            }
        });
        mAdapter.setOnChildViewItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                int id = view.getId();
                if (id == R.id.iv_topic_avatar || id == R.id.tv_topic_username || id == R.id.tv_topic_description) {
                    //为了显示波纹效果再启动
//                    getHandler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
                    TopicComment tc = mAdapter.getItem(position);
                    int childPosition = position - (mListView.getFirstVisiblePosition() - mListView.getHeaderViewsCount());
                    View startView = null;
                    View convertView = mListView.getChildAt(childPosition);
                    if (convertView != null && convertView.getTag() != null) {
                        TopicCommentAdapter.TopicCommentViewHolder tcv = (TopicCommentAdapter.TopicCommentViewHolder) convertView.getTag();
                        if (tcv != null) {
                            startView = tcv.mTopicTitle.mAvatar;
                        }
                    }
                    if (startView == null) {
                        PersonalHomepageActivity.start(TopicDetailActivity.this, tc.getUserInfo());
                    } else {
                        PersonalHomepageActivity.start(TopicDetailActivity.this, startView, tc.getUserInfo());
                    }

//                        }
//                    }, 100);
                } else {
                    mListView.performItemClick(null, position + mListView.getHeaderViewsCount(), mAdapter.getItemId(position));
                }
            }
        });

        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
        mPullToRefreshLayout.setLoadEnable(true);
        mPullToRefreshLayout.setOnRefreshListener(new SwipeListener.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (UserUtil.isUserEmpty()) {
                    mPullToRefreshLayout.setRefreshing(false);
                    return;
                }
                mPresenter.loadWBCommentAndRepost(mTopic.getId(), "0", true);
            }
        });
        mPullToRefreshLayout.setOnLoadListener(new SwipeListener.OnLoadListener() {
            @Override
            public void onLoad() {
                if (!CommonUtil.isEmpty(mAdapter.getData())) {
                    String id = CommonUtil.getLastItem(mAdapter.getData()).getId();
                    mPresenter.loadWBCommentOrRepost(mTopic.getId(), id, false, isSelectedCommentTab());
                } else {
                    mPullToRefreshLayout.setLoading(false);
                    mPullToRefreshLayout.setLoadEnable(false);
                }
            }
        });

        if (UserUtil.isUserEmpty()) {
            mPullToRefreshLayout.setRefreshing(false);
            return;
        }
        mPullToRefreshLayout.getOnRefreshListener().onRefresh();
    }

    private void initBroadcastReceiver() {
        mPublishResultBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
//                boolean isSuccess = intent.getBooleanExtra(TopicPublishService.EXTRA_IS_SUCCESS, false);
//                int type = intent.getIntExtra(TopicPublishService.EXTRA_TYPE, -1);
//                if (isSuccess) {
//                    TopicComment tc = (TopicComment) intent.getSerializableExtra(TopicPublishService.EXTRA_RESULT);
//                    if (tc == null) {
//                        return;
//                    }
//                    if (type == TopicDraftHelper.REPOST_TOPIC) {
//                        mTabLayout.getTabAt(1).select();
//                    } else {
//                        mTabLayout.getTabAt(0).select();
//                    }
//                    mAdapter.addItem(0, tc);
//                } else {
//                    final TopicDraft topicDraft = (TopicDraft) intent.getSerializableExtra(TopicPublishService.EXTRA_DRAFT);
//                    if (topicDraft == null) {
//                        return;
//                    }
//
//                    int resId;
//                    if (type == TopicDraftHelper.REPOST_TOPIC) {
//                        resId = R.string.label_topic_publish_repost_fail;
//                    } else if (type == TopicDraftHelper.PUBLISH_COMMENT) {
//                        resId = R.string.label_topic_publish_comment_fail;
//                    } else {
//                        resId = R.string.label_topic_reply_comment_fail;
//                    }
//                    Snackbar sb = ToastUtil.getSnackBar(resId, mFab);
//                    sb.setDuration(Snackbar.LENGTH_LONG);
//                    sb.setAction(R.string.label_topic_publish_retry, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            TopicPublishService.publish(TopicDetailActivity.this, topicDraft);
//                        }
//                    });
//                    sb.show();
//                }
            }
        };
    }

    private int getPublishType() {
        if (mTabLayout != null) {
            if (mTabLayout.getSelectedTabPosition() == 0) {
                return TopicDraftHelper.PUBLISH_COMMENT;
            }
        }
        return TopicDraftHelper.REPOST_TOPIC;
    }

    private void updateTabLayout(boolean isComment, long totalNumber) {

        if (totalNumber == 0) {
            return;
        }

        TabLayout.Tab tab = mTabLayout.getTabAt(isComment ? 0 : 1);
        TabLayout.Tab tabAssist = mTabLayoutAssist.getTabAt(isComment ? 0 : 1);

        String str = String.format
                (getString(isComment ? R.string.label_topic_comment_number : R.string.label_topic_repost_number)
                        , DataUtil.getCounter(totalNumber));
        if (tab != null && tabAssist != null) {
            tab.setText(str);
            tabAssist.setText(str);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fab) {
            if(getPublishType() == TopicDraftHelper.REPOST_TOPIC) {
                IntentUtil.startActivity(this,
                        TopicPublishActivity.getStartIntent(this, TopicDraftHelper.getWBTopicDraftByRepostRepost(mTopic)));
            }else{
                IntentUtil.startActivity(this,
                        TopicPublishActivity.getStartIntent(this, TopicDraftHelper.getWBTopicDraftByRepostComment(mTopic)));
            }
//            TopicDraft topicDraft = new TopicDraft();
//            topicDraft.setType(getPublishType());
//            topicDraft.setTargetTopicId(mTopic.getId());
//            topicDraft.setTargetTopicJson(mTopic.toJson());
//            topicDraft.setIsCommentOrigin(false);
//            startActivity(TopicPublishActivity.getStartIntent(this, topicDraft));
        }
    }

    int mCurrentPosition;
    @Override
    public void onClick(DialogInterface dialog, int which) {
        TopicComment topicComment = mAdapter.getItem(mCurrentPosition);
        if(topicComment == null){
            return;
        }
        if(isSelectedCommentTab()){
            switch (which){
                case DialogBuilder.COMMENT_COPY:
                    ClipboardUtil.copyAndToast(topicComment.getContent());
                    break;
                case DialogBuilder.COMMENT_REPLY:
                    startActivity(TopicPublishActivity.getStartIntent(this, TopicDraftHelper.getWBTopicDraftByCommentReply(mTopic, topicComment)));
                    break;
                case DialogBuilder.COMMENT_REPOST:
                    startActivity(TopicPublishActivity.getStartIntent(this, TopicDraftHelper.getWBTopicDraftByCommentRepost(mTopic, topicComment)));
                    break;
            }

        }else{
            switch (which){
                case DialogBuilder.REPOST_COPY:
                    ClipboardUtil.copyAndToast(topicComment.getContent());
                    break;
                case DialogBuilder.REPOST_DETAIL:
                    startActivity(TopicDetailActivity.getStartIntent(this, topicComment.toTopic(), true));
                    break;
                case DialogBuilder.REPOST_REPOST:
                    startActivity(TopicPublishActivity.getStartIntent(this, TopicDraftHelper.getWBTopicDraftByRepostRepost(topicComment)));
                    break;
                case DialogBuilder.REPOST_COMMENT:
                    startActivity(TopicPublishActivity.getStartIntent(this, TopicDraftHelper.getWBTopicDraftByRepostComment(topicComment)));
                    break;
            }

        }

//            TopicComment tc = mAdapter.getItem(actualPosition);
//            if (tc == null) {
//                return;
//            }
//            if(isSelectedCommentTab()) {
//                startActivity(TopicPublishActivity.getStartIntent(TopicDetailActivity.this, TopicDraftHelper.getWBTopicDraftByCommentReply(mTopic, tc)));
//            }else{
//                startActivity(TopicPublishActivity.getStartIntent(TopicDetailActivity.this, TopicDraftHelper.getWBTopicDraftByRepostRepost(mTopic)));
//            }
    }

    private boolean isSelectedCommentTab() {
        return mTabLayout.getSelectedTabPosition() == 0;
    }

    private boolean isSelectedRepostTab() {
        return mTabLayout.getSelectedTabPosition() == 1;
    }

    List<TopicComment> mCommentData = new ArrayList<TopicComment>();
    List<TopicComment> mRepostData = new ArrayList<TopicComment>();

    @Override
    public void loadSuccess(boolean isRefresh) {
        stopLoading(isRefresh);
    }

    @Override
    public void loadFail(boolean isRefresh) {
        stopLoading(isRefresh);
    }

    @Override
    public void stopLoading(boolean isRefresh) {
        if (isRefresh) {
            mPullToRefreshLayout.setRefreshing(false);
        } else {
            mPullToRefreshLayout.setLoading(false);
        }
    }

    @Override
    public void handleCommentData(boolean isComment, List<TopicComment> data, boolean isRefresh, long totalNumber) {
        if (isRefresh) {
            mPullToRefreshLayout.setRefreshing(false);
            if (CommonUtil.isEmpty(data)) {
                return;
            }
            updateTabLayout(isComment, totalNumber);

            if (isComment) {
                mCommentData = data;
            } else {
                mRepostData = data;
            }
//            if(mHasCommentRequestSuccess && mHasRepostRequestSuccess){
            List<TopicComment> adapterData = isSelectedCommentTab() ? mCommentData : mRepostData;
            if (CommonUtil.isEmpty(adapterData)) {
//                    //内容为空
                handleLoadMore(false, isComment);
//                    mPullToRefreshLayout.setLoadEnable(false);
            } else if (data.size() < WBUtil.getWBTopicRequestCount()) {
                //结果小于请求条数
//                    mPullToRefreshLayout.setLoadEnable(false);
            } else {
                handleLoadMore(true, isComment);
            }
            mAdapter.refresh(adapterData);
//            }
        } else {
            List<TopicComment> targetData = isComment ? mCommentData : mRepostData;
            mPullToRefreshLayout.setLoading(false);
            if (CommonUtil.isEmpty(data)) {
                //没有数据可供加载
                handleLoadMore(false, isComment);
                Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
            } else {
                //成功加载更多
                if (data.size() < WBUtil.getWBTopicRequestCount()) {
                    //没有更多的数据可供加载
                    //不可靠，有可能继续加载还有数据
//                    mPullToRefreshLayout.setLoadEnable(false);
//                    Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
                }
                //因为请求的数据是小于或等于max_id，需要做是否重复判断处理
                if (data.get(0).getId() != null && data.get(0).getId().
                        equals(CommonUtil.getLastItem(targetData).getId())) {
                    data.remove(0);
                }

                //当只有1条数据并且重复，data会空
                if (CommonUtil.isEmpty(data)) {
                    handleLoadMore(false, isComment);
                    Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
                } else {
                    targetData.addAll(data);
                }
            }

            if (isComment && isSelectedCommentTab()) {
                mAdapter.notifyDataSetChanged();
            } else if (!isComment && isSelectedRepostTab()) {
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    public void handleLoadMore(boolean enable, boolean isComment) {
        if (isComment) {
            mLoadCommentEnable = false;
        } else {
            mLoadReposttEnable = false;
        }
        if (isSelectedCommentTab() && isComment) {
            mPullToRefreshLayout.setLoadEnable(enable);
        } else if (isSelectedRepostTab() && !isComment) {
            mPullToRefreshLayout.setLoadEnable(enable);
        }

    }

}
