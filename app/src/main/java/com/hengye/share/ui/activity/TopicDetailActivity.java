package com.hengye.share.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.adapter.listview.TopicCommentAdapter;
import com.hengye.share.adapter.recyclerview.TopicAdapter;
import com.hengye.share.module.Topic;
import com.hengye.share.module.TopicComment;
import com.hengye.share.module.sina.WBTopicComments;
import com.hengye.share.module.sina.WBTopicReposts;
import com.hengye.share.util.thirdparty.WBUtil;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.hengye.volleyplus.toolbox.RequestManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.ArrayList;
import java.util.List;

public class TopicDetailActivity extends BaseActivity {

    @Override
    protected String getRequestTag() {
        return super.getRequestTag();
    }

    @Override
    protected boolean setCustomTheme() {
        return super.setCustomTheme();
    }

    @Override
    protected boolean setToolBar() {
        return true;
    }

    public static Intent getIntentToStart(Context context, Topic topic) {
        Intent intent = new Intent(context, TopicDetailActivity.class);
        intent.putExtra(Topic.class.getSimpleName(), topic);
        return intent;
    }

    Topic mTopic;

    @Override
    protected void handleBundleExtra() {
        mTopic = (Topic) getIntent().getSerializableExtra(Topic.class.getSimpleName());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_topic_detail);

        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            String str = (String)tab.getTag();
            if("tablayout_assist".equals(str)){
                mTabLayout.getTabAt(tab.getPosition()).select();
                return;
            }else if("tablayout".equals(str)){
                mTabLayoutAssist.getTabAt(tab.getPosition()).select();

                if(tab.getPosition() == 0){
                    mAdapter.setData(mCommentData);
                    mAdapter.notifyDataSetChanged();

                }else if(tab.getPosition() == 1){
                    mAdapter.setData(mRepostData);
                    mAdapter.notifyDataSetChanged();
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

    private Oauth2AccessToken mWBAccessToken;


    private void initHeaderView(View headerView){
        mTabLayout = (TabLayout) headerView.findViewById(R.id.tab_layout);
        mTabLayout.addTab((mTabLayout.newTab().setText("评论").setTag("tablayout")));
        mTabLayout.addTab((mTabLayout.newTab().setText("转发").setTag("tablayout")));
        mTabLayout.getTabAt(0).select();
        mTabLayout.setOnTabSelectedListener(mOnTabSelectedListener);
        TopicAdapter.TopicViewHolder topicViewHolder = new TopicAdapter.TopicViewHolder(headerView.findViewById(R.id.item_topic));
        topicViewHolder.bindData(this, mTopic);
    }

    private void initView() {
        if (mTopic == null) {
            return;
        }

        mTabLayoutHeight = getResources().getDimensionPixelSize(R.dimen.tab_layout_height);
        mTabLayoutAssist = (TabLayout) findViewById(R.id.tab_layout_assist);
        mTabLayoutAssist.addTab((mTabLayoutAssist.newTab().setText("评论").setTag("tablayout_assist")));
        mTabLayoutAssist.addTab((mTabLayoutAssist.newTab().setText("转发").setTag("tablayout_assist")));
        mTabLayoutAssist.getTabAt(0).select();
        mTabLayoutAssist.setOnTabSelectedListener(mOnTabSelectedListener);

        View headerView = LayoutInflater.from(this).inflate(R.layout.header_topic_detail, null);
        initHeaderView(headerView);
        mListView = (ListView) findViewById(R.id.list_view);
        mListView.addHeaderView(headerView);
        mListView.setAdapter(mAdapter = new TopicCommentAdapter(this, new ArrayList<TopicComment>()));

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem == 0){
//                    L.debug("firstVisibleItem position is 0");

                    View firstVisibleItemView = view.getChildAt(0);
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

        mWBAccessToken = SPUtil.getSinaAccessToken();
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mWBAccessToken == null || TextUtils.isEmpty(mWBAccessToken.getToken())) {
                    mPullToRefreshLayout.setRefreshing(false);
                    return;
                }

                RequestManager.addToRequestQueue(getWBCommentRequest(mWBAccessToken.getToken(), mTopic.getId(), "0", true), getRequestTag());
                RequestManager.addToRequestQueue(getWBRepostRequest(mWBAccessToken.getToken(), mTopic.getId(), "0", true), getRequestTag());
            }
        });
        mPullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                if (!CommonUtil.isEmptyCollection(mAdapter.getData())) {
                    String id = CommonUtil.getLastItem(mAdapter.getData()).getId();
                    GsonRequest gsonRequest;
                    if(isSelectedCommentTab()){
                        gsonRequest = getWBCommentRequest(mWBAccessToken.getToken(), mTopic.getId(), id, false);
                    }else{
                        gsonRequest = getWBRepostRequest(mWBAccessToken.getToken(), mTopic.getId(), id, false);
                    }
                    RequestManager.addToRequestQueue(gsonRequest, getRequestTag());
                } else {
                    mPullToRefreshLayout.setLoading(false);
                    mPullToRefreshLayout.setLoadEnable(false);
                }
            }
        });
        mPullToRefreshLayout.setRefreshing(true);
    }


    private boolean isSelectedCommentTab(){
        return mTabLayout.getSelectedTabPosition() == 0;
    }

    private boolean isSelectedRepostTab(){
        return mTabLayout.getSelectedTabPosition() == 1;
    }

    boolean mHasCommentRequestSuccess, mHasRepostRequestSuccess;
    List<TopicComment> mCommentData = new ArrayList<TopicComment>();
    List<TopicComment> mRepostData = new ArrayList<TopicComment>();

    private void handleCommentData(boolean isComment, List<TopicComment> data, boolean isRefresh){
        if(isRefresh){
            mPullToRefreshLayout.setRefreshing(false);
            if(CommonUtil.isEmptyCollection(data)){
                return;
            }
            if(isComment){
                mCommentData = data;
            }else{
                mRepostData = data;
            }
            if(mHasCommentRequestSuccess && mHasRepostRequestSuccess){
                List<TopicComment> adapterData = isSelectedCommentTab() ? mCommentData : mRepostData;
                if (CommonUtil.isEmptyCollection(adapterData)) {
//                    //内容为空
//                    mPullToRefreshLayout.setLoadEnable(false);
                }else if (data.size() < WBUtil.MAX_COUNT_REQUEST) {
                    //结果小于请求条数
//                    mPullToRefreshLayout.setLoadEnable(false);
                }else{
                    mPullToRefreshLayout.setLoadEnable(true);
                }
                mAdapter.refresh(adapterData);
            }
        }else{
            List<TopicComment> targetData = isComment ? mCommentData : mRepostData;
            mPullToRefreshLayout.setLoading(false);
            if (CommonUtil.isEmptyCollection(data)) {
                //没有数据可供加载
                mPullToRefreshLayout.setLoadEnable(false);
                Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
            } else {
                //成功加载更多
                if (data.size() < WBUtil.MAX_COUNT_REQUEST) {
                    //没有更多的数据可供加载
//                    mPullToRefreshLayout.setLoadEnable(false);
//                    Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
                }
                //因为请求的数据是小于或等于max_id，需要做是否重复判断处理
                if (data.get(0).getId() != null && data.get(0).getId().
                        equals(CommonUtil.getLastItem(targetData).getId())) {
                    data.remove(0);
                    if (CommonUtil.isEmptyCollection(data)) {
                        mPullToRefreshLayout.setLoadEnable(false);
                        Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
                    }else{
                        targetData.addAll(data);
                    }
                }

            }

            if(isComment && isSelectedCommentTab()){
                mAdapter.notifyDataSetChanged();
            }else if(!isComment && isSelectedRepostTab()){
                mAdapter.notifyDataSetChanged();
            }
        }

    }

    private GsonRequest getWBRepostRequest(String token, String topicId, String id, final boolean isRefresh) {
        mHasRepostRequestSuccess = false;

        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBRepostUrl());
        ub.addParameter("access_token", token);
        ub.addParameter("id", topicId);
        if (isRefresh) {
            ub.addParameter("since_id", id);
        } else {
            ub.addParameter("max_id", id);
        }
        ub.addParameter("count", WBUtil.MAX_COUNT_REQUEST);
        return new GsonRequest<>(
                ub.getRequestUrl()
                , WBTopicReposts.class
                , new Response.Listener<WBTopicReposts>() {
            @Override
            public void onResponse(WBTopicReposts response) {
                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
                mHasRepostRequestSuccess = true;
                handleCommentData(false, TopicComment.getComments(response), isRefresh);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (isRefresh) {
                    mPullToRefreshLayout.setRefreshing(false);
                } else {
                    mPullToRefreshLayout.setLoading(false);
                }
                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), volleyError);
            }
        });
    }

    private GsonRequest getWBCommentRequest(String token, String topicId, String id, final boolean isRefresh) {
        mHasCommentRequestSuccess = false;

        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBCommentUrl());
        ub.addParameter("access_token", token);
        ub.addParameter("id", topicId);
        if (isRefresh) {
            ub.addParameter("since_id", id);
        } else {
            ub.addParameter("max_id", id);
        }
        ub.addParameter("count", WBUtil.MAX_COUNT_REQUEST);
        return new GsonRequest<>(
                ub.getRequestUrl()
                , WBTopicComments.class
                , new Response.Listener<WBTopicComments>() {
            @Override
            public void onResponse(WBTopicComments response) {
                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
                mHasCommentRequestSuccess = true;
                handleCommentData(true, TopicComment.getComments(response), isRefresh);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (isRefresh) {
                    mPullToRefreshLayout.setRefreshing(false);
                } else {
                    mPullToRefreshLayout.setLoading(false);
                }
                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), volleyError);
            }
        });
    }
}
