package com.hengye.share.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.adapter.listview.TopicCommentAdapter;
import com.hengye.share.adapter.recyclerview.TopicAdapter;
import com.hengye.share.model.Topic;
import com.hengye.share.model.TopicComment;
import com.hengye.share.model.greenrobot.TopicDraft;
import com.hengye.share.model.greenrobot.TopicDraftHelper;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.mvpview.TopicDetailMvpView;
import com.hengye.share.ui.presenter.TopicDetailPresenter;
import com.hengye.share.ui.view.BackTopButton;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.ViewUtil;
import com.hengye.share.util.thirdparty.WBUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class TopicDetailActivity extends BaseActivity implements TopicDetailMvpView, View.OnClickListener{

    public static Intent getStartIntent(Context context, Topic topic, boolean isRetweet) {
        Intent intent = new Intent(context, TopicDetailActivity.class);
        intent.putExtra("topic", topic);
        intent.putExtra("isRetweet", isRetweet);
        return intent;
    }

    Topic mTopic;
    boolean mIsRetweet;

    @Override
    protected void handleBundleExtra() {
        mTopic = (Topic) getIntent().getSerializableExtra("topic");
        mIsRetweet = getIntent().getBooleanExtra("isRetweet", false);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_topic_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setupPresenter(mPresenter = new TopicDetailPresenter(this));
        initView();
    }

    TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            String str = (String)tab.getTag();
            if("tab_layout_assist".equals(str)){
                mTabLayout.getTabAt(tab.getPosition()).select();
            }else if("tab_layout".equals(str)){
                mTabLayoutAssist.getTabAt(tab.getPosition()).select();

                if(tab.getPosition() == 0){
                    mAdapter.setData(mCommentData);
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(1);
                }else if(tab.getPosition() == 1){
                    mAdapter.setData(mRepostData);
                    mAdapter.notifyDataSetChanged();
                    mListView.setSelection(1);
                }
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {}

        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    };

    private PullToRefreshLayout mPullToRefreshLayout;
    private ListView mListView;
    private TopicCommentAdapter mAdapter;
    private TabLayout mTabLayout, mTabLayoutAssist;
    private int mTabLayoutHeight;

    private View mTopicContentLayout;
    private TextView mTopicContent;

    private TopicDetailPresenter mPresenter;

    private void initHeaderView(View headerView, View headerViewAssist){
        mTabLayout = (TabLayout) headerViewAssist.findViewById(R.id.tab);
        mTabLayout.addTab((mTabLayout.newTab().setText(R.string.label_topic_comment).setTag("tab_layout")));
        mTabLayout.addTab((mTabLayout.newTab().setText(R.string.label_topic_repost).setTag("tab_layout")));
        mTabLayout.getTabAt(0).select();
        mTabLayout.setOnTabSelectedListener(mOnTabSelectedListener);

        View topicLayout = headerView.findViewById(R.id.item_topic);

        if(mIsRetweet){
            mTopicContentLayout = headerView.findViewById(R.id.ll_topic_content);
            mTopicContent = (TextView) headerView.findViewById(R.id.tv_topic_content);
            mTopicContentLayout.setTransitionName(getString(R.string.transition_name_topic));
        }else{
            topicLayout.setTransitionName(getString(R.string.transition_name_topic));
        }
        TopicAdapter.TopicViewHolder topicViewHolder = new TopicAdapter.TopicViewHolder(topicLayout);
        topicViewHolder.bindData(this, mTopic, 0);
    }

    @Override
    public void onBackPressed() {
        if(mTabLayoutAssist != null && mTabLayoutAssist.getVisibility() == View.GONE){
            if(mTopicContentLayout != null && mTopicContent != null && mIsRetweet){

//                mTopicContent.setText(DataUtil.addRetweetedNamePrefix(mTopic));
                mTopicContentLayout.setBackgroundColor(getResources().getColor(R.color.grey_50));
            }
            finishAfterTransition();
        }else{
            finish();
        }
    }

    private void initView() {
        if (mTopic == null) {
            return;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);

        mTabLayoutHeight = getResources().getDimensionPixelSize(R.dimen.tab_layout_height);
        mTabLayoutAssist = (TabLayout) findViewById(R.id.tab_layout_assist);
        mTabLayoutAssist.addTab((mTabLayoutAssist.newTab().setText(R.string.label_topic_comment).setTag("tab_layout_assist")));
        mTabLayoutAssist.addTab((mTabLayoutAssist.newTab().setText(R.string.label_topic_repost).setTag("tab_layout_assist")));
        mTabLayoutAssist.getTabAt(0).select();
        mTabLayoutAssist.setOnTabSelectedListener(mOnTabSelectedListener);

        View headerView = LayoutInflater.from(this).inflate(R.layout.item_topic_total, null);

        View headerViewAssist = LayoutInflater.from(this).inflate(R.layout.header_topic_detail, null);

        initHeaderView(headerView, headerViewAssist);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.addHeaderView(headerView);
        mListView.addHeaderView(headerViewAssist);
        mListView.setAdapter(mAdapter = new TopicCommentAdapter(this, new ArrayList<TopicComment>()));

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int actualPosition = position - mListView.getHeaderViewsCount();
                TopicComment tc = mAdapter.getItem(actualPosition);
                if(tc == null){
                    return;
                }
                TopicDraft topicDraft = new TopicDraft();
                topicDraft.setType(TopicDraftHelper.REPLY_COMMENT);
                topicDraft.setTargetTopicId(mTopic.getId());
                topicDraft.setTargetCommentId(tc.getId());
                topicDraft.setIsCommentOrigin(false);
                startActivity(TopicPublishActivity.getStartIntent(TopicDetailActivity.this, topicDraft));
            }
        });
        mAdapter.setOnChildViewItemClickListener(new ViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                view.performClick();
                mListView.performItemClick(null, position + mListView.getHeaderViewsCount(), mAdapter.getItemId(position));
            }
        });

        BackTopButton backTopBtn = (BackTopButton) findViewById(R.id.iv_back_top);
        backTopBtn.setup(mListView);

        backTopBtn.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem == 0){

                    View firstVisibleItemView = view.getChildAt(1);
                    if(firstVisibleItemView == null){
                        return;
                    }

                    if(firstVisibleItemView.getBottom() <= mTabLayoutHeight){
                        mTabLayoutAssist.setVisibility(View.VISIBLE);
                    }else{
                        mTabLayoutAssist.setVisibility(View.GONE);
                    }
                }else{
                    mTabLayoutAssist.setVisibility(View.VISIBLE);
                }
            }
        });

        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (UserUtil.isUserEmpty()) {
                    mPullToRefreshLayout.setRefreshing(false);
                    return;
                }
                mPresenter.loadWBCommentAndRepost(mTopic.getId(), "0", true);
            }
        });
        mPullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                if (!CommonUtil.isEmptyCollection(mAdapter.getData())) {
                    String id = CommonUtil.getLastItem(mAdapter.getData()).getId();
                    mPresenter.loadWBCommentOrRepost(mTopic.getId(), id, false, isSelectedCommentTab());
                } else {
                    mPullToRefreshLayout.setLoading(false);
                    mPullToRefreshLayout.setLoadEnable(false);
                }
            }
        });
        mPullToRefreshLayout.setRefreshing(true);
    }

    private int getPublishType(){
        if(mTabLayout != null){
            if(mTabLayout.getSelectedTabPosition() == 0){
                return TopicDraftHelper.PUBLISH_COMMENT;
            }
        }
        return TopicDraftHelper.REPOST_TOPIC;
    }

    private void updateTabLayout(boolean isComment, long totalNumber){

        if(totalNumber == 0){
            return;
        }

        TabLayout.Tab tab = mTabLayout.getTabAt(isComment ? 0 : 1);
        TabLayout.Tab tabAssist = mTabLayoutAssist.getTabAt(isComment ? 0 : 1);

        String str = String.format
                (getString(isComment ? R.string.label_topic_comment_number : R.string.label_topic_repost_number)
                        , totalNumber);
        if(tab != null && tabAssist != null){
            tab.setText(str);
            tabAssist.setText(str);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.fab){
            TopicDraft topicDraft = new TopicDraft();
            topicDraft.setType(getPublishType());
            topicDraft.setTargetTopicId(mTopic.getId());
            topicDraft.setIsCommentOrigin(false);
            startActivity(TopicPublishActivity.getStartIntent(this, topicDraft));
        }
    }

    private boolean isSelectedCommentTab(){
        return mTabLayout.getSelectedTabPosition() == 0;
    }

    private boolean isSelectedRepostTab(){
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
        if(isRefresh){
            mPullToRefreshLayout.setRefreshing(false);
        }else{
            mPullToRefreshLayout.setLoading(false);
        }
    }

    @Override
    public void handleCommentData(boolean isComment, List<TopicComment> data, boolean isRefresh, long totalNumber){
        if(isRefresh){
            mPullToRefreshLayout.setRefreshing(false);
            if(CommonUtil.isEmptyCollection(data)){
                return;
            }
            updateTabLayout(isComment, totalNumber);

            if(isComment){
                mCommentData = data;
            }else{
                mRepostData = data;
            }
//            if(mHasCommentRequestSuccess && mHasRepostRequestSuccess){
                List<TopicComment> adapterData = isSelectedCommentTab() ? mCommentData : mRepostData;
                if (CommonUtil.isEmptyCollection(adapterData)) {
//                    //内容为空
//                    mPullToRefreshLayout.setLoadEnable(false);
                }else if (data.size() < WBUtil.getWBTopicRequestCount()) {
                    //结果小于请求条数
//                    mPullToRefreshLayout.setLoadEnable(false);
                }else{
                    mPullToRefreshLayout.setLoadEnable(true);
                }
                mAdapter.refresh(adapterData);
//            }
        }else{
            List<TopicComment> targetData = isComment ? mCommentData : mRepostData;
            mPullToRefreshLayout.setLoading(false);
            if (CommonUtil.isEmptyCollection(data)) {
                //没有数据可供加载
                mPullToRefreshLayout.setLoadEnable(false);
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
                if (CommonUtil.isEmptyCollection(data)) {
                    mPullToRefreshLayout.setLoadEnable(false);
                    Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
                }else{
                    targetData.addAll(data);
                }
            }

            if(isComment && isSelectedCommentTab()){
                mAdapter.notifyDataSetChanged();
            }else if(!isComment && isSelectedRepostTab()){
                mAdapter.notifyDataSetChanged();
            }
        }

    }

}
