package com.hengye.share.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.google.gson.reflect.TypeToken;
import com.hengye.share.adapter.recyclerview.TopicFavoritesAdapter;
import com.hengye.share.handler.data.DefaultDataHandler;
import com.hengye.share.handler.data.NumberPager;
import com.hengye.share.handler.data.base.DataHandler;
import com.hengye.share.handler.data.base.DataType;
import com.hengye.share.handler.data.base.Pager;
import com.hengye.share.model.TopicFavorites;
import com.hengye.share.model.sina.WBTopicFavorites;
import com.hengye.share.ui.fragment.encapsulation.paging.RecyclerRefreshFragment;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.ArrayList;
import java.util.List;

public class TopicFavoritesFragmentBackup extends RecyclerRefreshFragment<TopicFavorites.TopicFavorite> {

    @Override
    public String getTitle() {
        return "我收藏的微博";
    }

    private TopicFavoritesAdapter mAdapter;
    private NumberPager mPager;
    DefaultDataHandler<TopicFavorites.TopicFavorite> mHandler;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setAdapter(mAdapter = new TopicFavoritesAdapter(getActivity(), getData()));
        mPager = new NumberPager();
        mHandler = new DefaultDataHandler<>(mAdapter);

        if(mAdapter.getData().isEmpty()) {
            setRefreshing(true);
        }else{
            setLoadEnable(false);
        }
    }

    private ArrayList<TopicFavorites.TopicFavorite> getData() {
        ArrayList<TopicFavorites.TopicFavorite> data = SPUtil.getModule(new TypeToken<ArrayList<TopicFavorites.TopicFavorite>>() {
        }.getType(), TopicFavorites.TopicFavorite.class.getSimpleName() + UserUtil.getUid());
        if (data == null) {
            data = new ArrayList<>();
        }
        return data;
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (UserUtil.isUserEmpty()) {
            setRefreshing(false);
            return;
        }

        RequestManager.addToRequestQueue(getWBTopicFavoritesRequest(UserUtil.getToken(), true), getRequestTag());
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (!CommonUtil.isEmpty(mAdapter.getData())) {
            RequestManager.addToRequestQueue(getWBTopicFavoritesRequest(UserUtil.getToken(), false), getRequestTag());
        }
    }

    @Override
    public Pager getPager() {
        return mPager;
    }

    @Override
    public DataHandler<TopicFavorites.TopicFavorite> getDataHandler() {
        return mHandler;
    }

    private void handleData(List<TopicFavorites.TopicFavorite> data, boolean isRefresh){
        int type = handleData(isRefresh, data);

        if(DataType.hasNewData(type)){
            SPUtil.setModule(mAdapter.getData(), TopicFavorites.TopicFavorite.class.getSimpleName() + UserUtil.getUid());
        }
    }

    private GsonRequest getWBTopicFavoritesRequest(String token, final boolean isRefresh) {
        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBTopicFavoritesUrl());
        ub.addParameter("access_token", token);
        ub.addParameter("page", mPager.getPage(isRefresh));
        ub.addParameter("count", WBUtil.getWBTopicRequestCount());
        return new GsonRequest<>(
                WBTopicFavorites.class,
                ub.getRequestUrl(),
                new Response.Listener<WBTopicFavorites>() {
            @Override
            public void onResponse(WBTopicFavorites response) {
                L.debug("request success , url : {}, data : {}", ub, response);
                handleData(TopicFavorites.getTopicFavorites(response), isRefresh);
                onTaskComplete();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                onTaskComplete();
                L.debug("request fail , url : {}, error : {}", ub, error);
            }

        });
    }

}
