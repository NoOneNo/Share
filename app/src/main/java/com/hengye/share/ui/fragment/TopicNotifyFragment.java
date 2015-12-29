package com.hengye.share.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.TopicNotifyAdapter;
import com.hengye.share.module.Topic;
import com.hengye.share.module.sina.WBTopicComments;
import com.hengye.share.module.sina.WBTopics;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.ViewUtil;
import com.hengye.share.util.thirdparty.WBUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.ArrayList;
import java.util.List;

public class TopicNotifyFragment extends BaseFragment{

    public static final int NOTIFY_COMMENT = 1;
    public static final int NOTIFY_MENTION = 2;

    public static final int URL_COMMENT_TO_ME = 1;
    public static final int URL_COMMENT_BY_ME = 2;
    public static final int URL_COMMENT_MENTION = 3;

    public static TopicNotifyFragment newInstance(int notifyType) {
        TopicNotifyFragment fragment = new TopicNotifyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("notifyType", notifyType);
        fragment.setArguments(bundle);
        return fragment;
    }

    int mNotifyType;

    @Override
    protected void handleBundleExtra() {
        mNotifyType = getArguments().getInt("notifyType");
    }

    private PullToRefreshLayout mPullToRefreshLayout;
    private TopicNotifyAdapter mAdapter;
    private Oauth2AccessToken mWBAccessToken;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_notify, null);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(mAdapter = new TopicNotifyAdapter(getActivity(), new ArrayList<Topic>()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mWBAccessToken = SPUtil.getSinaAccessToken();
        mPullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.pull_to_refresh);
        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mWBAccessToken == null || TextUtils.isEmpty(mWBAccessToken.getToken())) {
                    mPullToRefreshLayout.setRefreshing(false);
                    return;
                }

                if(mNotifyType == NOTIFY_COMMENT) {
                    RequestManager.addToRequestQueue(getWBCommentRequest(mWBAccessToken.getToken(), "0", true, URL_COMMENT_TO_ME), getRequestTag());
                }else{
                    RequestManager.addToRequestQueue(getWBTopicRequest(mWBAccessToken.getToken(), "0", true), getRequestTag());
                }
            }
        });
        mPullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                if (!CommonUtil.isEmptyCollection(mAdapter.getData())) {
                    String id = CommonUtil.getLastItem(mAdapter.getData()).getId();
                    if(mNotifyType == NOTIFY_COMMENT) {
                        RequestManager.addToRequestQueue(getWBCommentRequest(mWBAccessToken.getToken(), id, false, URL_COMMENT_TO_ME), getRequestTag());
                    }else{
                        RequestManager.addToRequestQueue(getWBTopicRequest(mWBAccessToken.getToken(), id, false), getRequestTag());
                    }
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

        mPullToRefreshLayout.setRefreshing(true);
        return view;
    }

    private void handleData(List<Topic> data, boolean isRefresh){
        if(isRefresh) {
            mPullToRefreshLayout.setRefreshing(false);
        }else {
            mPullToRefreshLayout.setLoading(false);
        }
        int type = DataUtil.handlePagingData(mAdapter.getData(), data, isRefresh);
        DataUtil.handleTopicAdapter(type, mAdapter, data);
        DataUtil.handlePullToRefresh(type, mPullToRefreshLayout);
//        DataUtil.handleSnackBar(type, mPullToRefreshLayout, data == null ? 0 : data.size());
//        if(type == DataUtil.REFRESH_DATA_SIZE_LESS
//                || type == DataUtil.REFRESH_DATA_SIZE_EQUAL
//                || type == DataUtil.LOAD_NO_MORE_DATA
//                || type == DataUtil.LOAD_DATA_SIZE_EQUAL){
//            SPUtil.setModule(mAdapter.getData(), Topic.class.getSimpleName());
//        }
    }

    private GsonRequest getWBTopicRequest(String token, String id, final boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBTopicMentionUrl());
        ub.addParameter("access_token", token);
        if (isRefresh) {
            ub.addParameter("since_id", id);
        } else {
            ub.addParameter("max_id", id);
        }
        ub.addParameter("count", WBUtil.MAX_COUNT_REQUEST);
        return new GsonRequest<>(
                WBTopics.class,
                ub.getRequestUrl(),
                new Response.Listener<WBTopics>() {
            @Override
            public void onResponse(WBTopics response) {
                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
                handleData(Topic.getTopics(response), isRefresh);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (isRefresh) {
                    mPullToRefreshLayout.setRefreshing(false);
                } else {
                    mPullToRefreshLayout.setLoading(false);
                }
                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), error);
            }

        });
    }

    private String getCommentRequestUrlByType(int urlType){
        switch (urlType){
            case URL_COMMENT_TO_ME:
                return UrlFactory.getInstance().getWBCommentToMeUrl();
            case URL_COMMENT_BY_ME:
                return UrlFactory.getInstance().getWBCommentByMeUrl();
            case URL_COMMENT_MENTION:
                return UrlFactory.getInstance().getWBCommentMentionUrl();
            default:
                return UrlFactory.getInstance().getWBCommentMentionUrl();
        }
    }

    private GsonRequest getWBCommentRequest(String token, String id, final boolean isRefresh, int urlType) {
        final UrlBuilder ub = new UrlBuilder(getCommentRequestUrlByType(urlType));
        ub.addParameter("access_token", token);
        if (isRefresh) {
            ub.addParameter("since_id", id);
        } else {
            ub.addParameter("max_id", id);
        }
        ub.addParameter("count", WBUtil.MAX_COUNT_REQUEST);
        return new GsonRequest<>(
                WBTopicComments.class,
                ub.getRequestUrl(),
                new Response.Listener<WBTopicComments>() {
            @Override
            public void onResponse(WBTopicComments response) {
                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
                handleData(Topic.getTopics(response), isRefresh);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (isRefresh) {
                    mPullToRefreshLayout.setRefreshing(false);
                } else {
                    mPullToRefreshLayout.setLoading(false);
                }
                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), error);
            }

        });
    }

//    private void handleData(List<Topic> data, boolean isRefresh){
//        if (isRefresh) {
//            //下拉刷新
//            mPullToRefreshLayout.setRefreshing(false);
//            if (!CommonUtil.isEmptyCollection(mAdapter.getData())) {
//                //微博属于刷新
//                if (CommonUtil.isEmptyCollection(data)) {
//                    //没有内容更新
//                    Snackbar.make(mPullToRefreshLayout, "没有新的微博", Snackbar.LENGTH_SHORT).show();
//                    return;
//                } else if (data.size() < WBUtil.MAX_COUNT_REQUEST) {
//                    //结果小于请求条数
//                    mAdapter.addAll(0, data);
//                    Snackbar.make(mPullToRefreshLayout, data.size() + "条新微博", Snackbar.LENGTH_SHORT).show();
//                } else {
//                    //结果大于或等于请求条数
//                    mPullToRefreshLayout.setLoadEnable(true);
//                    mAdapter.refresh(data);
//                    Snackbar.make(mPullToRefreshLayout, "超过" + WBUtil.MAX_COUNT_REQUEST + "条新微博", Snackbar.LENGTH_SHORT).show();
//                }
//            } else {
//                //属于第一次加载
//                if (CommonUtil.isEmptyCollection(data)) {
//                    //内容为空
//                    mPullToRefreshLayout.setLoadEnable(false);
//                }
//                mAdapter.refresh(data);
//            }
//            //存储数据
////            SPUtil.setModule(mAdapter.getData(), Topic.class.getSimpleName());
//        } else {
//            //上拉加载
//            mPullToRefreshLayout.setLoading(false);
//            if (CommonUtil.isEmptyCollection(data)) {
//                //没有数据可供加载
//                mPullToRefreshLayout.setLoadEnable(false);
//                Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
//            } else {
//                //成功加载更多
//                if (data.size() < WBUtil.MAX_COUNT_REQUEST) {
//                    //没有更多的数据可供加载
//                    mPullToRefreshLayout.setLoadEnable(false);
//                    Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
//                }
//                //因为请求的数据是小于或等于max_id，需要做是否重复判断处理
//                if (data.get(0).getId() != null && data.get(0).getId().
//                        equals(CommonUtil.getLastItem(mAdapter.getData()).getId())) {
//                    data.remove(0);
//                }
//                mAdapter.addAll(data);
//            }
//        }
//    }
}
