//package com.hengye.share.ui.fragment;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.android.volley.Response;
//import com.android.volley.error.VolleyError;
//import com.android.volley.request.GsonRequest;
//import com.hengye.share.R;
//import com.hengye.share.adapter.recyclerview.TopicNotifyAdapter;
//import com.hengye.share.handler.data.DefaultDataHandler;
//import com.hengye.share.handler.data.NumberPager;
//import com.hengye.share.handler.data.TopicIdHandler;
//import com.hengye.share.handler.data.TopicAdapterIdPager;
//import com.hengye.share.handler.data.base.DataHandler;
//import com.hengye.share.handler.data.base.Pager;
//import com.hengye.share.model.Topic;
//import com.hengye.share.model.TopicFavorites;
//import com.hengye.share.model.sina.WBTopicComments;
//import com.hengye.share.model.sina.WBTopics;
//import com.hengye.share.ui.base.BaseFragment;
//import com.hengye.share.ui.fragment.encapsulation.paging.RecyclerRefreshFragment;
//import com.hengye.share.util.CommonUtil;
//import com.hengye.share.util.DataUtil;
//import com.hengye.share.util.L;
//import com.hengye.share.util.RequestManager;
//import com.hengye.share.util.UrlBuilder;
//import com.hengye.share.util.UrlFactory;
//import com.hengye.share.util.UserUtil;
//import com.hengye.share.util.ViewUtil;
//import com.hengye.share.util.thirdparty.WBUtil;
//import com.hengye.swiperefresh.PullToRefreshLayout;
//import com.hengye.swiperefresh.listener.SwipeListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TopicNotifyFragment extends RecyclerRefreshFragment<Topic> {
//
//    public static final int NOTIFY_COMMENT = 1;
//    public static final int NOTIFY_MENTION = 2;
//
//    public static final int URL_COMMENT_TO_ME = 1;
//    public static final int URL_COMMENT_BY_ME = 2;
//    public static final int URL_COMMENT_MENTION = 3;
//
//    public static TopicNotifyFragment newInstance(int notifyType) {
//        TopicNotifyFragment fragment = new TopicNotifyFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt("notifyType", notifyType);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    int mNotifyType;
//
//    @Override
//    protected void handleBundleExtra(Bundle bundle) {
//        mNotifyType = getArguments().getInt("notifyType");
//    }
//
//    private TopicNotifyAdapter mAdapter;
//    private TopicAdapterIdPager mPager;
//    TopicIdHandler<Topic> mHandler;
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        mPager = new TopicAdapterIdPager(mAdapter);
//        mHandler = new TopicIdHandler<>(mAdapter);
//        setAdapter(mAdapter = new TopicNotifyAdapter(getActivity(), new ArrayList<Topic>()));
//        mAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
////                Toast.makeText(MainActivity.this, "click item : " + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        setRefreshing(true);
//    }
//
//    @Override
//    public void onRefresh() {
//        super.onRefresh();
//        if (UserUtil.isUserEmpty()) {
//            setRefreshing(false);
//            return;
//        }
//
//        if(mNotifyType == NOTIFY_COMMENT) {
//            RequestManager.addToRequestQueue(getWBCommentRequest(UserUtil.getToken(), "0", true, URL_COMMENT_TO_ME), getRequestTag());
//        }else{
//            RequestManager.addToRequestQueue(getWBTopicRequest(UserUtil.getToken(), "0", true), getRequestTag());
//        }
//    }
//
//    @Override
//    public void onLoad() {
//        super.onLoad();
//        if (!CommonUtil.isEmpty(mAdapter.getData())) {
//            String id = CommonUtil.getLastItem(mAdapter.getData()).getId();
//            if(mNotifyType == NOTIFY_COMMENT) {
//                RequestManager.addToRequestQueue(getWBCommentRequest(UserUtil.getToken(), id, false, URL_COMMENT_TO_ME), getRequestTag());
//            }else{
//                RequestManager.addToRequestQueue(getWBTopicRequest(UserUtil.getToken(), id, false), getRequestTag());
//            }
//        }
//    }
//
//    @Override
//    public Pager getPager() {
//        return mPager;
//    }
//
//    @Override
//    public DataHandler<Topic> getDataHandler() {
//        return mHandler;
//    }
//
//    private void handleData(List<Topic> data, boolean isRefresh){
//        int type = handleData(isRefresh, data);
//
////        int type = DataUtil.handlePagingData(mAdapter.getData(), data, isRefresh);
////        DataUtil.handleTopicAdapter(type, mAdapter, data);
////        DataUtil.handlePullToRefresh(type, mPullToRefreshLayout);
////        DataUtil.handleSnackBar(type, mPullToRefreshLayout, data == null ? 0 : data.size());
////        if(type == DataUtil.REFRESH_DATA_SIZE_LESS
////                || type == DataUtil.REFRESH_DATA_SIZE_EQUAL
////                || type == DataUtil.LOAD_NO_MORE_DATA
////                || type == DataUtil.LOAD_DATA_SIZE_EQUAL){
////            SPUtil.setModule(mAdapter.getData(), Topic.class.getSimpleName());
////        }
//    }
//
//    private GsonRequest getWBTopicRequest(String token, String id, final boolean isRefresh) {
//        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBTopicMentionUrl());
//        ub.addParameter("access_token", token);
//        if (isRefresh) {
//            ub.addParameter("since_id", id);
//        } else {
//            ub.addParameter("max_id", id);
//        }
//        ub.addParameter("count", WBUtil.getWBTopicRequestCount());
//        return new GsonRequest<>(
//                WBTopics.class,
//                ub.getRequestUrl(),
//                new Response.Listener<WBTopics>() {
//            @Override
//            public void onResponse(WBTopics response) {
//                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
//                handleData(Topic.getTopics(response), isRefresh);
//                onTaskComplete();
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                onTaskComplete();
//                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), error);
//            }
//
//        });
//    }
//
//    private String getCommentRequestUrlByType(int urlType){
//        switch (urlType){
//            case URL_COMMENT_TO_ME:
//                return UrlFactory.getInstance().getWBCommentToMeUrl();
//            case URL_COMMENT_BY_ME:
//                return UrlFactory.getInstance().getWBCommentByMeUrl();
//            case URL_COMMENT_MENTION:
//                return UrlFactory.getInstance().getWBCommentMentionUrl();
//            default:
//                return UrlFactory.getInstance().getWBCommentMentionUrl();
//        }
//    }
//
//    private GsonRequest getWBCommentRequest(String token, String id, final boolean isRefresh, int urlType) {
//        final UrlBuilder ub = new UrlBuilder(getCommentRequestUrlByType(urlType));
//        ub.addParameter("access_token", token);
//        if (isRefresh) {
//            ub.addParameter("since_id", id);
//        } else {
//            ub.addParameter("max_id", id);
//        }
//        ub.addParameter("count", WBUtil.getWBTopicRequestCount());
//        return new GsonRequest<>(
//                WBTopicComments.class,
//                ub.getRequestUrl(),
//                new Response.Listener<WBTopicComments>() {
//            @Override
//            public void onResponse(WBTopicComments response) {
//                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
//                handleData(Topic.getTopics(response), isRefresh);
//                onTaskComplete();
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                onTaskComplete();
//                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), error);
//            }
//
//        });
//    }
//
//}
