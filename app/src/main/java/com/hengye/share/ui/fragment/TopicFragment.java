package com.hengye.share.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.TopicAdapter;
import com.hengye.share.model.Topic;
import com.hengye.share.ui.base.BaseFragment;
import com.hengye.share.ui.mvpview.TopicMvpView;
import com.hengye.share.ui.presenter.TopicPresenter;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class TopicFragment extends BaseFragment implements TopicMvpView, View.OnClickListener {

    public static TopicFragment newInstance(TopicPresenter.TopicGroup topicGroup, String uid, String name) {
        TopicFragment fragment = new TopicFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("topicGroup", topicGroup);
        bundle.putSerializable("uid", uid);
        bundle.putSerializable("name", name);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static TopicFragment newInstance(TopicPresenter.TopicGroup topicGroup) {
        return newInstance(topicGroup, null, null);
    }

    public static TopicFragment newInstance(TopicPresenter.TopicType topicType, String uid, String name) {
        return newInstance(new TopicPresenter.TopicGroup(topicType), uid, name);
    }

    private PullToRefreshLayout mPullToRefreshLayout;
    private RecyclerView mRecyclerView;
    private ImageView mBackTopBtn;
    private Animation mBackTopDisappearAnimation, mBackTopAppearAnimation;

    private Handler mHandler = new Handler();

    private TopicAdapter mAdapter;
    private TopicPresenter mPresenter;
    private TopicPresenter.TopicGroup topicGroup;
    private String uid, name;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_topic;
    }

    @Override
    protected void handleBundleExtra() {
        topicGroup = (TopicPresenter.TopicGroup) getArguments().getSerializable("topicGroup");
        uid = getArguments().getString("uid");
        name = getArguments().getString("name");
    }

    @Override
    protected void onCreateView() {

        setupPresenter(mPresenter = new TopicPresenter(this, topicGroup));
        mPresenter.setUid(uid);
        mPresenter.setName(name);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter = new TopicAdapter(getContext(), new ArrayList<Topic>(), mRecyclerView));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy > 0){
                    if(mBackTopBtn.getVisibility() == View.GONE){
                        mBackTopAppearAnimation.reset();
                        mBackTopBtn.startAnimation(mBackTopAppearAnimation);
                        mBackTopBtn.setVisibility(View.VISIBLE);
                    }

                }else{
                    mBackTopBtn.setVisibility(View.GONE);
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

                if (!mAdapter.isEmpty()) {
                    String id = mAdapter.getData().get(0).getId();
//                    mPresenter.loadWBAllTopicIds(id);
                    mPresenter.loadWBTopic(id, true);
                } else {
                    mPresenter.loadWBTopic("0", true);
                }
            }
        });

        mPullToRefreshLayout.setOnLoadListener(new PullToRefreshLayout.OnLoadListener() {
            @Override
            public void onLoad() {
                if (!mAdapter.isEmpty()) {
                    String id = CommonUtil.getLastItem(mAdapter.getData()).getId();
                    mPresenter.loadWBTopic(id, false);
                } else {
                    mPullToRefreshLayout.setLoading(false);
                    mPullToRefreshLayout.setLoadEnable(false);
                }
            }
        });

        mBackTopBtn = (ImageView) findViewById(R.id.iv_back_top);
        mBackTopBtn.setOnClickListener(this);

        AlphaAnimation alphaAnim1 = new AlphaAnimation(1.0f, 0.3f);

        TranslateAnimation translateAnim1 = new TranslateAnimation
                (Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, -1.0f);

        AnimationSet as1 = new AnimationSet(true);
        as1.addAnimation(alphaAnim1);
        as1.addAnimation(translateAnim1);
        as1.setDuration(1000);
        as1.setInterpolator(new AccelerateInterpolator());

        AlphaAnimation alphaAnim2 = new AlphaAnimation(0.0f, 1.0f);

        TranslateAnimation translateAnim2 = new TranslateAnimation
                (Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 0.3f,
                        Animation.RELATIVE_TO_PARENT, 0f);

        AnimationSet as2 = new AnimationSet(true);
        as2.addAnimation(alphaAnim2);
        as2.addAnimation(translateAnim2);
        as2.setDuration(1000);
        as2.setInterpolator(new AccelerateInterpolator());

        mBackTopDisappearAnimation = as1;
        mBackTopAppearAnimation = as2;


        mPresenter.loadCacheData();
    }

    public void refresh() {
        mPresenter.loadCacheData();
    }

    @Override
    public void handleCache(List<Topic> data) {
        if (CommonUtil.isEmptyCollection(data)) {
            if (mAdapter.isEmpty() && !UserUtil.isUserEmpty()) {
                mPullToRefreshLayout.setRefreshing(true);
            }
        } else {
            mAdapter.refresh(data);
        }

    }

    @Override
    public void stopLoading(boolean isRefresh) {
        if (isRefresh) {
            mPullToRefreshLayout.setRefreshing(false);
        } else {
            mPullToRefreshLayout.setLoading(false);
        }
    }

    @Override
    public void handleNoMoreTopics() {
        Snackbar.make(mPullToRefreshLayout, "没有新的微博", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void handleTopicData(List<Topic> data, boolean isRefresh) {
        int type = DataUtil.handlePagingData(mAdapter.getData(), data, isRefresh);
        DataUtil.handleTopicAdapter(type, mAdapter, data);
        DataUtil.handlePullToRefresh(type, mPullToRefreshLayout);
        DataUtil.handleSnackBar(type, mPullToRefreshLayout, data == null ? 0 : data.size());
        if (type == DataUtil.REFRESH_DATA_SIZE_LESS
                || type == DataUtil.REFRESH_DATA_SIZE_EQUAL
                || type == DataUtil.LOAD_NO_MORE_DATA
                || type == DataUtil.LOAD_DATA_SIZE_EQUAL) {
            mPresenter.saveData(mAdapter.getData());
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_back_top) {
            mRecyclerView.smoothScrollToPosition(0);
            mBackTopBtn.startAnimation(mBackTopDisappearAnimation);
        }
    }

    private Animation.AnimationListener mBackTopAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.scrollToPosition(0);
                }
            });
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };
}
