package com.hengye.share.module.hottopic;


import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.HotTopic;
import com.hengye.share.model.Status;
import com.hengye.share.module.base.ShareRecyclerFragment;
import com.hengye.share.module.status.StatusActionFragment;
import com.hengye.share.module.status.StatusAdapter;
import com.hengye.share.module.status.StatusTopicActivity;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.handler.StatusRefreshIdHandler;

import java.util.ArrayList;

public class HotTopicAndStatusFragment extends StatusActionFragment
        implements HotTopicAndStatusContract.View, View.OnClickListener{

    @Override
    public boolean setToolBar() {
        return false;
    }

    HotTopicAndStatusContract.Presenter mPresenter;
    HotStatusPager mPager;
    View mHeader, mHotTopicContent, mHotTopicEmpty;
    ArrayList<HotTopic> mHotTopics;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setDataHandler(new StatusRefreshIdHandler<>(mAdapter));
        setPager(mPager = new HotStatusPager());
        mPresenter = new HotTopicAndStatusPresenter(this);

        initHeader();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        mPresenter.loadWBTopic(mPager.getFirstPage(), true);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        mPresenter.loadWBTopic(mPager.getNextPage(), false);
    }

    @Override
    public void onLoadHotTopic(ArrayList<HotTopic> hotTopics) {
        L.debug("onLoadHotTopic, size : %s", CommonUtil.size(hotTopics));
        mHotTopics = hotTopics;
        initHotTopic();
    }

    private void initHeader(){
        mHeader = LayoutInflater.from(getContext()).inflate(R.layout.fragment_hot_topic_header, getRecyclerView(), false);
        mHotTopicContent = mHeader.findViewById(R.id.hot_topic_content);
        mHotTopicEmpty = mHeader.findViewById(R.id.empty_layout);
        mAdapter.setHeader(mHeader);

        setTitle(R.id.item_hot_topic_title, R.string.label_hot_topic);
        setTitle(R.id.item_hot_status_title, R.string.label_hot_status);
    }

    private void initHotTopic(){
        if(CommonUtil.size(mHotTopics) >= 2){
            mHotTopicEmpty.setVisibility(View.GONE);
            mHotTopicContent.setVisibility(View.VISIBLE);
            HotTopic hotTopic1 = mHotTopics.get(0);
            HotTopic hotTopic2 = mHotTopics.get(1);
            setHotTopic(R.id.item_hot_topic_1, hotTopic1);
            setHotTopic(R.id.item_hot_topic_2, hotTopic2);
        }else{
            mHotTopicEmpty.setVisibility(View.VISIBLE);
            mHotTopicContent.setVisibility(View.GONE);
        }
    }

    private void setTitle(@IdRes int titleViewId, @StringRes int titleContentId){
        View title = mHeader.findViewById(titleViewId);
        TextView header = (TextView) title.findViewById(R.id.tv_headline);
        header.setText(titleContentId);
        title.setOnClickListener(this);
    }

    private void setHotTopic(@IdRes int hotTopicItemId, HotTopic hotTopic){
        View hotTopicItem = mHeader.findViewById(hotTopicItemId);
        HotTopicAdapter.HotTopicViewHolder hotTopicViewHolder = new HotTopicAdapter.HotTopicViewHolder(hotTopicItem);
        hotTopicViewHolder.bindData(getContext(), hotTopic, 0);
        hotTopicItem.setTag(hotTopic);
        hotTopicItem.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.item_hot_topic_title){
            HotTopicFragment.start(getContext(), mHotTopics);
        }else if(id == R.id.item_hot_status_title){
//            dd
        }else if(id == R.id.item_hot_topic_1 || id == R.id.item_hot_topic_2){
            HotTopic hotTopic = (HotTopic) v.getTag();
            startActivity(StatusTopicActivity.getStartIntent(getContext(), hotTopic.getTopic()));
        }
    }
}













