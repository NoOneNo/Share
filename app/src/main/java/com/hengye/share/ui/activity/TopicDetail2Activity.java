package com.hengye.share.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.TopicCommentWithHeaderAdapter;
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
import com.hengye.share.util.ViewUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.hengye.volleyplus.toolbox.RequestManager;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.ArrayList;
import java.util.List;

public class TopicDetail2Activity extends BaseActivity {

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
        Intent intent = new Intent(context, TopicDetail2Activity.class);
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

        setContentView(R.layout.activity_topic_detail2);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private PullToRefreshLayout mPullToRefreshLayout;
    private TopicCommentWithHeaderAdapter mAdapter;
    private TabLayout mTabLayout;
    private RecyclerView mRecyclerView;
    private int mTabLayoutHeight;

    private Oauth2AccessToken mWBAccessToken;

    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    TabLayout.OnTabSelectedListener mOnTabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            String str = (String)tab.getTag();
            if("tablayout_assist".equals(str)){
                return;
            }else if("tablayout".equals(str)){
                mTabLayout.getTabAt(tab.getPosition()).select();
            }
            if(tab.getPosition() == 0){
                mAdapter.setData(mCommentData);
//                if(!mCommentData.isEmpty()){
//                    mAdapter.notifyItemRangeChanged(1, mAdapter.getItemCount() - 2);
//                }else{
                    mAdapter.notifyDataSetChanged();
//                }

            }else if(tab.getPosition() == 1){
                mAdapter.setData(mRepostData);
//                if(!mRepostData.isEmpty()){
//                    mAdapter.notifyItemRangeChanged(1, mAdapter.getItemCount() - 2);
//                }else{
                    mAdapter.notifyDataSetChanged();
//                }
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {}

        @Override
        public void onTabReselected(TabLayout.Tab tab) {}
    };

    public TabLayout.OnTabSelectedListener getOnTabSelectedListener() {
        return mOnTabSelectedListener;
    }

    private void initView() {
        if (mTopic == null) {
            return;
        }

        mTabLayoutHeight = getResources().getDimensionPixelSize(R.dimen.tab_layout_height);
        mTabLayout = (TabLayout) findViewById(R.id.tablayout_assist);
        mTabLayout.addTab((mTabLayout.newTab().setText("评论").setTag("tablayout_assist")));
        mTabLayout.addTab((mTabLayout.newTab().setText("转发").setTag("tablayout_assist")));
        mTabLayout.setOnTabSelectedListener(mOnTabSelectedListener);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new TopicCommentWithHeaderAdapter(this, new ArrayList<TopicComment>(), mTopic, mTabLayout));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if(firstVisibleItemPosition == 0){
                    View firstVisibleItem = recyclerView.getChildAt(0);
                    int firstItemBottom = layoutManager.getDecoratedBottom(firstVisibleItem);
                    L.debug("onScrolled firstItemBottom : {}", firstItemBottom);
                    if(firstItemBottom <= mTabLayoutHeight){
                        mTabLayout.setVisibility(View.VISIBLE);
                    }else{
                        mTabLayout.setVisibility(View.GONE);
                    }
                }
                L.debug("onScrolled firstVisibleItemPosition : {}", firstVisibleItemPosition);
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
//                if (!CommonUtil.isEmptyCollection(mAdapter.getData())) {
//                    String id = mAdapter.getData().get(0).getId();
//                    RequestManager.addToRequestQueue(getWBTopicIdsRequest(mWBAccessToken.getToken(), id), getRequestTag());
//                }else{
//                    RequestManager.addToRequestQueue(getWBTopicRequest(mWBAccessToken.getToken(), 0 + "", true), getRequestTag());
//                }
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

        mAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(MainActivity.this, "click item : " + position, Toast.LENGTH_SHORT).show();
            }
        });
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
//        if(isComment){
//            targetData = mCommentData;
//        }else{
//            targetData = mRepostData;
//        }
        if(CommonUtil.isEmptyCollection(data)){
            return;
        }

        if(isRefresh){
            mPullToRefreshLayout.setRefreshing(false);
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
                    mPullToRefreshLayout.setLoadEnable(false);
                }else{
                    mPullToRefreshLayout.setLoadEnable(true);
                }
                mAdapter.refresh(adapterData);
            }
        }else{
            List<TopicComment> targetData = isComment ? mCommentData : mRepostData;
            mPullToRefreshLayout.setLoading(false);
            if (data.get(0).getId() != null && data.get(0).getId().
                    equals(CommonUtil.getLastItem(targetData).getId())) {
                data.remove(0);
            }
            targetData.addAll(data);

            if(isComment && isSelectedCommentTab()){
                mAdapter.notifyDataSetChanged();
            }else if(!isComment && isSelectedRepostTab()){
                mAdapter.notifyDataSetChanged();
            }
        }

    }


    private void handleCommentData2(boolean isComment, List<TopicComment> data, boolean isRefresh){
        if(!mHasCommentRequestSuccess || !mHasRepostRequestSuccess){
            return;
        }

        List<TopicComment> adapterData;
        if(mTabLayout.getSelectedTabPosition() == 0){
            adapterData = mCommentData;
        }else{
            adapterData = mRepostData;
        }

        if (isRefresh) {
            //下拉刷新
            mPullToRefreshLayout.setRefreshing(false);
            if (!CommonUtil.isEmptyCollection(mAdapter.getData())) {
                //微博属于刷新
                if (CommonUtil.isEmptyCollection(data)) {
                    //没有内容更新
                    Snackbar.make(mPullToRefreshLayout, "暂时没有内容", Snackbar.LENGTH_SHORT).show();
                    return;
                } else if (data.size() < WBUtil.MAX_COUNT_REQUEST) {
                    //结果小于请求条数
                    mAdapter.addAll(0, data);
//                            Snackbar.make(mPullToRefreshLayout, datas.size() + "条新微博", Snackbar.LENGTH_SHORT).show();
                } else {
                    //结果大于或等于请求条数
                    mPullToRefreshLayout.setLoadEnable(true);
                    mAdapter.refresh(data);
//                            Snackbar.make(mPullToRefreshLayout, "超过" + WBUtil.MAX_COUNT_REQUEST + "条新微博", Snackbar.LENGTH_SHORT).show();
                }
            } else {
                //属于第一次加载
                if (CommonUtil.isEmptyCollection(data)) {
                    //内容为空
                    mPullToRefreshLayout.setLoadEnable(false);
                }else if (data.size() < WBUtil.MAX_COUNT_REQUEST) {
                    //结果小于请求条数
                    mPullToRefreshLayout.setLoadEnable(false);
                }else{
                    mPullToRefreshLayout.setLoadEnable(true);
                }
                mAdapter.refresh(data);
            }
            //存储数据
//                    SPUtil.setModule(mAdapter.getData(), TopicComment.class.getSimpleName());
        } else {
            //上拉加载
            mPullToRefreshLayout.setLoading(false);
            if (CommonUtil.isEmptyCollection(data)) {
                //没有数据可供加载
                mPullToRefreshLayout.setLoadEnable(false);
                Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
            } else {
                //成功加载更多
                if (data.size() < WBUtil.MAX_COUNT_REQUEST) {
                    //没有更多的数据可供加载
                    mPullToRefreshLayout.setLoadEnable(false);
                    Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
                }
                //因为请求的数据是小于或等于max_id，需要做是否重复判断处理
                if (data.get(0).getId() != null && data.get(0).getId().
                        equals(CommonUtil.getLastItem(mAdapter.getData()).getId())) {
                    data.remove(0);
                }
                mAdapter.addAll(data);
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
//                mRepostData = TopicComment.getComments(response);
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
//                mCommentData = TopicComment.getComments(response);
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
