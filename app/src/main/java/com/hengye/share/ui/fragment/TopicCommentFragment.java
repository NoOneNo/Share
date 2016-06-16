//package com.hengye.share.ui.fragment;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.design.widget.Snackbar;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.android.volley.Response;
//import com.android.volley.error.VolleyError;
//import com.android.volley.request.GsonRequest;
//import com.hengye.share.R;
//import com.hengye.share.adapter.recyclerview.TopicCommentAdapter;
//import com.hengye.share.module.TopicComment;
//import com.hengye.share.module.sina.WBTopicComments;
//import com.hengye.share.util.thirdparty.WBUtil;
//import com.hengye.share.util.CommonUtil;
//import com.hengye.share.util.L;
//import com.hengye.share.util.SPUtil;
//import com.hengye.share.util.UrlBuilder;
//import com.hengye.share.util.UrlFactory;
//import com.hengye.share.util.ViewUtil;
//import com.hengye.swiperefresh.PullToRefreshLayout;
//import com.hengye.volleyplus.toolbox.RequestManager;
//import com.sina.weibo.sdk.auth.Oauth2AccessToken;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TopicCommentFragment extends BaseFragment{
//
//    public static TopicCommentFragment newInstance(String topicId) {
//        TopicCommentFragment fragment = new TopicCommentFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("topicId", topicId);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    String mTopicId;
//
//    @Override
//    protected void handleBundleExtra() {
//        mTopicId = getArguments().getString("topicId");
//    }
//
//    private PullToRefreshLayout mPullToRefreshLayout;
//    private TopicCommentAdapter mAdapter;
//    private Oauth2AccessToken mWBAccessToken;
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_topic_comment, null);
//
//        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(mAdapter = new TopicCommentAdapter(getActivity(), new ArrayList<TopicComment>()));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        mWBAccessToken = SPUtil.getSinaAccessToken();
//        mPullToRefreshLayout = (PullToRefreshLayout) view.findViewById(R.id.pull_to_refresh);
//        mPullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                if (mWBAccessToken == null || TextUtils.isEmpty(mWBAccessToken.getToken())) {
//                    mPullToRefreshLayout.setRefreshing(false);
//                    return;
//                }
//
//                RequestManager.addToRequestQueue(getWBCommentRequest(mWBAccessToken.getToken(), mTopicId, "0", true), getRequestTag());
////                if (!CommonUtil.isEmpty(mAdapter.getData())) {
////                    String id = mAdapter.getData().get(0).getId();
////                    RequestManager.addToRequestQueue(getWBTopicIdsRequest(mWBAccessToken.getToken(), id), getRequestTag());
////                }else{
////                    RequestManager.addToRequestQueue(getWBTopicRequest(mWBAccessToken.getToken(), 0 + "", true), getRequestTag());
////                }
//            }
//        });
//        mPullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
//            @Override
//            public void onLoad() {
//                if (!CommonUtil.isEmpty(mAdapter.getData())) {
//                    String id = CommonUtil.getLastItem(mAdapter.getData()).getId();
//                    RequestManager.addToRequestQueue(getWBCommentRequest(mWBAccessToken.getToken(), mTopicId, id, false), getRequestTag());
//                } else {
//                    mPullToRefreshLayout.setLoading(false);
//                    mPullToRefreshLayout.setLoadEnable(false);
//                }
//            }
//        });
//
//        mAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
//            @Override
//            public void onItemLongClick(View view, int position) {
////                Toast.makeText(MainActivity.this, "click item : " + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        return view;
//    }
//
//    private GsonRequest getWBCommentRequest(String token, String topicId, String id, final boolean isRefresh) {
//        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBCommentUrl());
//        ub.addParameter("access_token", token);
//        ub.addParameter("id", topicId);
//        if (isRefresh) {
//            ub.addParameter("since_id", id);
//        } else {
//            ub.addParameter("max_id", id);
//        }
//        ub.addParameter("count", WBUtil.MAX_COUNT_REQUEST);
//        return new GsonRequest<>(
//                ub.getRequestUrl()
//                , WBTopicComments.class
//                , new Response.Listener<WBTopicComments>() {
//            @Override
//            public void onResponse(WBTopicComments response) {
//                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
//                List<TopicComment> datas = TopicComment.getComments(response);
//                if (isRefresh) {
//                    //下拉刷新
//                    mPullToRefreshLayout.setRefreshing(false);
//                    if (!CommonUtil.isEmpty(mAdapter.getData())) {
//                        //微博属于刷新
//                        if (CommonUtil.isEmpty(datas)) {
//                            //没有内容更新
//                            Snackbar.make(mPullToRefreshLayout, "暂时没有内容", Snackbar.LENGTH_SHORT).show();
//                            return;
//                        } else if (datas.size() < WBUtil.MAX_COUNT_REQUEST) {
//                            //结果小于请求条数
//                            mAdapter.addAll(0, datas);
////                            Snackbar.make(mPullToRefreshLayout, datas.size() + "条新微博", Snackbar.LENGTH_SHORT).show();
//                        } else {
//                            //结果大于或等于请求条数
//                            mPullToRefreshLayout.setLoadEnable(true);
//                            mAdapter.refresh(datas);
////                            Snackbar.make(mPullToRefreshLayout, "超过" + WBUtil.MAX_COUNT_REQUEST + "条新微博", Snackbar.LENGTH_SHORT).show();
//                        }
//                    } else {
//                        //属于第一次加载
//                        if (CommonUtil.isEmpty(datas)) {
//                            //内容为空
//                            mPullToRefreshLayout.setLoadEnable(false);
//                        }else if (datas.size() < WBUtil.MAX_COUNT_REQUEST) {
//                            //结果小于请求条数
//                            mPullToRefreshLayout.setLoadEnable(false);
//                        }else{
//                            mPullToRefreshLayout.setLoadEnable(true);
//                        }
//                        mAdapter.refresh(datas);
//                    }
//                    //存储数据
////                    SPUtil.setModule(mAdapter.getData(), TopicComment.class.getSimpleName());
//                } else {
//                    //上拉加载
//                    mPullToRefreshLayout.setLoading(false);
//                    if (CommonUtil.isEmpty(datas)) {
//                        //没有数据可供加载
//                        mPullToRefreshLayout.setLoadEnable(false);
//                        Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
//                    } else {
//                        //成功加载更多
//                        if (datas.size() < WBUtil.MAX_COUNT_REQUEST) {
//                            //没有更多的数据可供加载
//                            mPullToRefreshLayout.setLoadEnable(false);
//                            Snackbar.make(mPullToRefreshLayout, "已经是最后内容", Snackbar.LENGTH_SHORT).show();
//                        }
//                        //因为请求的数据是小于或等于max_id，需要做是否重复判断处理
//                        if (datas.get(0).getId() != null && datas.get(0).getId().
//                                equals(CommonUtil.getLastItem(mAdapter.getData()).getId())) {
//                            datas.remove(0);
//                        }
//                        mAdapter.addAll(datas);
//                    }
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                if (isRefresh) {
//                    mPullToRefreshLayout.setRefreshing(false);
//                } else {
//                    mPullToRefreshLayout.setLoading(false);
//                }
//                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), volleyError);
//            }
//        });
//    }
//}
