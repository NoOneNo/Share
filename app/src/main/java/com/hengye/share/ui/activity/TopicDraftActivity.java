package com.hengye.share.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.TopicDraftAdapter;
import com.hengye.share.model.greenrobot.TopicDraft;
import com.hengye.share.model.greenrobot.TopicDraftHelper;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class TopicDraftActivity extends BaseActivity{

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
        return super.setToolBar();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_topic_draft;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        super.afterCreate(savedInstanceState);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private RecyclerView mRecyclerView;
    private TopicDraftAdapter mAdapter;
    private List<TopicDraft> mTopicDraft;

    private void initView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new TopicDraftAdapter(this, getTopicDraftData()));

        mAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IntentUtil.startActivityForResult(TopicDraftActivity.this,
                        TopicPublishActivity.getIntentToStart(TopicDraftActivity.this, mAdapter.getItem(position)), 1);
            }
        });
    }

    private List<TopicDraft> getTopicDraftData(){

        mTopicDraft = TopicDraftHelper.getTopicDraft();
        if(CommonUtil.isEmptyCollection(mTopicDraft)){
            mTopicDraft = new ArrayList<>();
        }
        return mTopicDraft;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == Activity.RESULT_OK){
            mAdapter.refresh(getTopicDraftData());
        }
    }
}
