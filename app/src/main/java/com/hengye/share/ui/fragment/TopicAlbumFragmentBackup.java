//package com.hengye.share.ui.fragment;
//
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//
//import com.hengye.share.R;
//import com.hengye.share.adapter.recyclerview.TopicAlbumAdapter;
//import com.hengye.share.model.Topic;
//import com.hengye.share.ui.activity.GalleryActivity;
//import com.hengye.share.ui.base.BaseFragment;
//import com.hengye.share.ui.fragment.PersonalHomepageFragment.LoadDataCallBack;
//import com.hengye.share.ui.mvpview.TopicAlbumMvpView;
//import com.hengye.share.ui.presenter.TopicAlbumPresenter;
//import com.hengye.share.util.CommonUtil;
//import com.hengye.share.util.ViewUtil;
//import com.hengye.swiperefresh.PullToRefreshLayout;
//import com.hengye.swiperefresh.listener.SwipeListener;
//
//import java.util.ArrayList;
//
//public class TopicAlbumFragmentBackup extends BaseFragment implements TopicAlbumMvpView, SwipeListener.OnRefreshListener {
//
//    public static TopicAlbumFragmentBackup newInstance(String uid, String name) {
//        TopicAlbumFragmentBackup fragment = new TopicAlbumFragmentBackup();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("uid", uid);
//        bundle.putSerializable("name", name);
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    public static ArrayList<Topic> topics;
//    public static ArrayList<String> urls;
//
//    PullToRefreshLayout mPullToRefresh;
//    RecyclerView mRecyclerView;
//    TopicAlbumAdapter mAdapter;
//    TopicAlbumPresenter mPresenter;
//    ArrayList<Topic> mTopics;
//    String uid, name;
//
//    @Override
//    public int getLayoutResId() {
//        return R.layout.fragment_topic_album;
//    }
//
//    @Override
//    protected void handleBundleExtra(Bundle bundle) {
//        uid = bundle.getString("uid");
//        name = bundle.getString("name");
//    }
//
//    @Override
//    public boolean onToolbarDoubleClick(Toolbar toolbar) {
//        mRecyclerView.getLayoutManager().scrollToPosition(0);
//        return true;
//    }
//
//    public void handleStaticData(boolean isReset){
//        if(isReset){
//            topics = null;
//            urls = null;
//        }else{
//            topics = mTopics;
//            urls = (ArrayList<String>) mAdapter.getData();
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        handleStaticData(true);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        handleStaticData(true);
//    }
//
//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        addPresenter(mPresenter = new TopicAlbumPresenter(this));
//        mPresenter.setUid(uid);
//        mPresenter.setName(name);
//
//        mPullToRefresh = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);
//        mPullToRefresh.setLoadEnable(true);
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
//
//        GridLayoutManager staggeredGridLayoutManager = new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
//
//        mRecyclerView.setLayoutManager(staggeredGridLayoutManager);
//        mRecyclerView.setAdapter(mAdapter = new TopicAlbumAdapter(getContext(), new ArrayList<String>()));
//
//        mPullToRefresh.setRefreshEnable(false);
//        mPullToRefresh.setOnLoadListener(new SwipeListener.OnLoadListener() {
//            @Override
//            public void onLoad() {
//                mPresenter.loadTopicAlbum(false);
//            }
//        });
//
//        mAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                handleStaticData(false);
//                GalleryActivity.startWithIntent(getActivity(), null, position, null, null);
//            }
//        });
//
//
//        mPresenter.loadCacheData();
////        onRefresh();
//    }
//
//    @Override
//    public void handleCache(ArrayList<Topic> topics, ArrayList<String> urls) {
//        if(CommonUtil.isEmpty(urls)){
//            onRefresh();
//        }else{
//            handleTopics(topics, true);
//            mAdapter.refresh(urls);
//        }
//    }
//
//    @Override
//    public void stopLoading(boolean isRefresh) {
//        if (isRefresh) {
//            getLoadDataCallBack().refresh(false);
//        } else {
//            mPullToRefresh.setLoading(false);
//        }
//    }
//
//    @Override
//    public void handleAlbumData(ArrayList<Topic> topics, ArrayList<String> urls, boolean isRefresh) {
//        if (isRefresh) {
//            mAdapter.refresh(urls);
//            mPullToRefresh.setLoadEnable(true);
//        } else {
//            mAdapter.addAll(urls);
//            if (CommonUtil.isEmpty(urls)) {
//                mPullToRefresh.setLoadEnable(false);
//            }
//        }
//        handleTopics(topics, isRefresh);
//    }
//
//    @Override
//    public void onRefresh() {
//        mPresenter.loadTopicAlbum(true);
//    }
//
//    public void handleTopics(ArrayList<Topic> topics, boolean isRefresh){
//        if(isRefresh) {
//            if(mTopics != null) {
//                mTopics.clear();
//            }
//            mTopics = topics;
//        }else{
//            mTopics.addAll(topics);
//        }
//    }
//
//    LoadDataCallBack mLoadDataCallBack;
//
//    public LoadDataCallBack getLoadDataCallBack() {
//        if(mLoadDataCallBack == null){
//            mLoadDataCallBack = new DefaultLoadDataCallBack();
//        }
//        return mLoadDataCallBack;
//    }
//
//    public void setLoadDataCallBack(LoadDataCallBack loadDataCallBack) {
//        this.mLoadDataCallBack = loadDataCallBack;
//    }
//
//    public PullToRefreshLayout getPullToRefresh(){
//        return mPullToRefresh;
//    }
//
//    public class DefaultLoadDataCallBack implements LoadDataCallBack {
//        @Override
//        public void initView() {
//
//        }
//
//        @Override
//        public void refresh(boolean isRefreshing) {
//            mPullToRefresh.setRefreshing(isRefreshing);
//        }
//    }
//}
