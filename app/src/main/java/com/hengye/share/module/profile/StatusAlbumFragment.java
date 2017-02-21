package com.hengye.share.module.profile;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.Status;
import com.hengye.share.module.status.ShareLoadDataCallbackFragment;
import com.hengye.share.module.base.ShareRecyclerFragment;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.module.util.image.GalleryActivity;
import com.hengye.share.util.L;
import com.hengye.share.util.handler.StatusIdPager;
import com.hengye.share.util.handler.StatusRefreshIdHandler;
import com.hengye.share.util.thirdparty.WBUtil;
import com.hengye.swiperefresh.listener.SwipeListener;

import java.util.ArrayList;

public class StatusAlbumFragment extends ShareRecyclerFragment<String> implements StatusAlbumContract.View, SwipeListener.OnRefreshListener {

    public static StatusAlbumFragment newInstance(String uid, String name) {
        StatusAlbumFragment fragment = new StatusAlbumFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("uid", uid);
        bundle.putSerializable("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static ArrayList<Status> statuses;
    public static ArrayList<String> urls;

    StatusAlbumAdapter mAdapter;
    StatusAlbumPresenter mPresenter;
    ArrayList<Status> mStatuses;
    String uid, name;

    StatusIdPager mStatusPager;

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_content;
    }

    @Override
    public int getLoadingResId(){
        return R.layout.state_loading_top;
    }

    @Override
    public int getEmptyResId(){
        return R.layout.state_empty_top;
    }

    @Override
    public int getNoNetworkResId(){
        return R.layout.state_no_network_top;
    }

    @Override
    public int getServiceErrorResId() {
        return R.layout.state_service_error_top;
    }

    @Override
    public int getContentResId() {
        return R.layout.fragment_status_album;
    }

    @Override
    protected void handleBundleExtra(Bundle bundle) {
        uid = bundle.getString("uid");
        name = bundle.getString("name");
    }

    public void handleStaticData(boolean isReset){
        if(isReset){
            statuses = null;
            urls = null;
        }else{
            statuses = mStatuses;
            urls = (ArrayList<String>) mAdapter.getData();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handleStaticData(true);
        L.debug("album onResume invoke()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handleStaticData(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAdapter(mAdapter = new StatusAlbumAdapter(getContext(), new ArrayList<String>()));
        setPager(mStatusPager = new StatusIdPager(mStatuses));
        setDataHandler(new StatusRefreshIdHandler<>(mAdapter));

        mPresenter = new StatusAlbumPresenter(this);
        mPresenter.setUid(uid);
        mPresenter.setName(name);

        getPullToRefresh().setRefreshEnable(false);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                handleStaticData(false);
                GalleryActivity.startWithIntent(getActivity(), null, position, null, null);
            }
        });

        showLoading();
        markLazyLoadPreparedAndLazyLoadIfCan();
    }

    protected void onLazyLoad() {
        mPresenter.loadCacheData();
    }

    @Override
    protected RecyclerView.LayoutManager generateLayoutManager() {
        return new GridLayoutManager(getContext(), 3, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public void onRefresh() {
        mPresenter.loadStatusAlbum(mStatusPager.getFirstPage(), true);
    }

    @Override
    public void onLoad() {
        mPresenter.loadStatusAlbum(mStatusPager.getNextPage(), false);
    }

    @Override
    public void onTaskComplete(boolean isRefresh, int taskState) {
        super.onTaskComplete(isRefresh, taskState);
        if(isRefresh){
            getLoadDataCallBack().refresh(false);
        }
    }

    @Override
    public void handleAlbumData(ArrayList<Status> statuses, ArrayList<String> urls, boolean isRefresh) {

        onLoadListData(isRefresh, urls);

        handleStatuses(statuses, isRefresh);

        if(statuses != null && statuses.size() < WBUtil.getWBStatusRequestCount()){
            setLoadEnable(false);
        }
    }

    public void handleStatuses(ArrayList<Status> statuses, boolean isRefresh){
        if(isRefresh) {
            if(mStatuses != null) {
                mStatuses.clear();
            }
            mStatuses = statuses;
            mStatusPager.refreshIds(mStatuses);
        }else{
            if(statuses != null) {
                mStatuses.addAll(statuses);
            }
        }
    }

    ShareLoadDataCallbackFragment.LoadDataCallback mLoadDataCallback;

    public ShareLoadDataCallbackFragment.LoadDataCallback getLoadDataCallBack() {
        if(mLoadDataCallback == null){
            mLoadDataCallback = new DefaultLoadDataCallback();
        }
        return mLoadDataCallback;
    }

    public void setLoadDataCallBack(ShareLoadDataCallbackFragment.LoadDataCallback loadDataCallback) {
        this.mLoadDataCallback = loadDataCallback;
    }

    public class DefaultLoadDataCallback implements ShareLoadDataCallbackFragment.LoadDataCallback {
        @Override
        public void initView() {

        }

        @Override
        public void refresh(boolean isRefreshing) {
            getPullToRefresh().setRefreshing(isRefreshing);
        }
    }
}
