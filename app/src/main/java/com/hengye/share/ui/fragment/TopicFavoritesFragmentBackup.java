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
//import com.google.gson.reflect.TypeToken;
//import com.hengye.share.R;
//import com.hengye.share.adapter.recyclerview.TopicFavoritesAdapter;
//import com.hengye.share.model.TopicFavorites;
//import com.hengye.share.model.sina.WBTopicFavorites;
//import com.hengye.share.ui.base.BaseFragment;
//import com.hengye.share.util.CommonUtil;
//import com.hengye.share.util.DataUtil;
//import com.hengye.share.util.L;
//import com.hengye.share.util.RequestManager;
//import com.hengye.share.util.SPUtil;
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
//public class TopicFavoritesFragmentBackup extends BaseFragment {
//
//    @Override
//    public String getTitle() {
//        return "我收藏的微博";
//    }
//
//    private PullToRefreshLayout mPullToRefreshLayout;
//    private TopicFavoritesAdapter mAdapter;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_topic_notify, null);
//
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(mAdapter = new TopicFavoritesAdapter(getActivity(), getData()));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        mPullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.pull_to_refresh);
//        mPullToRefreshLayout.setOnRefreshListener(new SwipeListener.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (UserUtil.isUserEmpty()) {
//                    mPullToRefreshLayout.setRefreshing(false);
//                    return;
//                }
//
//                RequestManager.addToRequestQueue(getWBTopicFavoritesRequest(UserUtil.getToken(), true), getRequestTag());
//            }
//        });
//        mPullToRefreshLayout.setOnLoadListener(new SwipeListener.OnLoadListener() {
//            @Override
//            public void onLoad() {
//                if (!CommonUtil.isEmpty(mAdapter.getData())) {
////                    String id = CommonUtil.getLastItem(mAdapter.getData()).getId();
//                    RequestManager.addToRequestQueue(getWBTopicFavoritesRequest(UserUtil.getToken(), false), getRequestTag());
//                } else {
//                    mPullToRefreshLayout.setLoading(false);
//                    mPullToRefreshLayout.setLoadEnable(false);
//                }
//            }
//        });
//
//        mAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
////                Toast.makeText(MainActivity.this, "click item : " + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        if(mAdapter.getData().isEmpty()) {
//            mPullToRefreshLayout.setRefreshing(true);
//        }else{
//            mPullToRefreshLayout.setLoadEnable(false);
//        }
//        return view;
//    }
//
//    private ArrayList<TopicFavorites.TopicFavorite> getData() {
//
//        ArrayList<TopicFavorites.TopicFavorite> data = SPUtil.getModule(new TypeToken<ArrayList<TopicFavorites.TopicFavorite>>() {
//        }.getType(), TopicFavorites.TopicFavorite.class.getSimpleName() + UserUtil.getUid());
//        if (data == null) {
//            data = new ArrayList<>();
//        }
//        return data;
//    }
//
//    private void handleData(List<TopicFavorites.TopicFavorite> data, boolean isRefresh){
//        if(isRefresh) {
//            mPullToRefreshLayout.setRefreshing(false);
//        }else {
//            mPullToRefreshLayout.setLoading(false);
//        }
//        int type = DataUtil.handlePagingData(mAdapter.getData(), data, isRefresh);
//        DataUtil.handleCommonAdapter(type, mAdapter, data);
//        DataUtil.handlePullToRefresh(type, mPullToRefreshLayout);
////        DataUtil.handleSnackBar(type, mPullToRefreshLayout, data == null ? 0 : data.size());
//        if(type == DataUtil.REFRESH_DATA_SIZE_LESS
//                || type == DataUtil.REFRESH_DATA_SIZE_EQUAL
//                || type == DataUtil.LOAD_NO_MORE_DATA
//                || type == DataUtil.LOAD_DATA_SIZE_EQUAL){
//            SPUtil.setModule(mAdapter.getData(), TopicFavorites.TopicFavorite.class.getSimpleName() + UserUtil.getUid());
//        }
//    }
//
//    private int mPageStart = 1;
//    private int mPageNo = mPageStart;
//
//    private GsonRequest getWBTopicFavoritesRequest(String token, final boolean isRefresh) {
//        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBTopicFavoritesUrl());
//        ub.addParameter("access_token", token);
//        ub.addParameter("page", isRefresh ? mPageStart : mPageNo + 1);
//        ub.addParameter("count", WBUtil.getWBTopicRequestCount());
//        return new GsonRequest<>(
//                WBTopicFavorites.class,
//                ub.getRequestUrl(),
//                new Response.Listener<WBTopicFavorites>() {
//            @Override
//            public void onResponse(WBTopicFavorites response) {
//                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
//
//                if(isRefresh){
//                    mPageNo = mPageStart;
//                }else{
//                    mPageNo++;
//                }
//                handleData(TopicFavorites.getTopicFavorites(response), isRefresh);
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                if (isRefresh) {
//                    mPullToRefreshLayout.setRefreshing(false);
//                } else {
//                    mPullToRefreshLayout.setLoading(false);
//                }
//                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), error);
//            }
//
//        });
//    }
//
//}