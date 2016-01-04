package com.hengye.share.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.hengye.share.BaseActivity;
import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.TopicDraftAdapter;
import com.hengye.share.module.AtUser;
import com.hengye.share.module.Topic;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.SPUtil;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_topic_draft);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private RecyclerView mRecyclerView;
    private TopicDraftAdapter mAdapter;
    private ArrayList<Topic> mTopicDraft;

    private void initView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new TopicDraftAdapter(this, getTopicDraftData()));

        mAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                IntentUtil.startActivity(TopicDraftActivity.this,
                        TopicPublishActivity.getIntentToStart(TopicDraftActivity.this, mAdapter.getItem(position)));
            }
        });
    }

    private ArrayList<Topic> getTopicDraftData(){
        mTopicDraft = SPUtil.getModule(new TypeToken<ArrayList<Topic>>() {
        }.getType(), TopicDraftActivity.class.getSimpleName() + SPUtil.getSinaUid());

        if(CommonUtil.isEmptyCollection(mTopicDraft)){
            mTopicDraft = new ArrayList<>();
        }
        return mTopicDraft;
    }

    public static void saveTopicToDraft(Topic topic){
        ArrayList<Topic> temp = SPUtil.getModule(new TypeToken<ArrayList<Topic>>() {
        }.getType(), TopicDraftActivity.class.getSimpleName() + SPUtil.getSinaUid());

        if(CommonUtil.isEmptyCollection(temp)){
            temp = new ArrayList<>();
        }

        temp.add(topic);
        SPUtil.setModule(temp, TopicDraftActivity.class.getSimpleName() + SPUtil.getSinaUid());
    }

    public static void removeTopicFromDraft(Topic topic){
        ArrayList<Topic> temp = SPUtil.getModule(new TypeToken<ArrayList<Topic>>() {
        }.getType(), TopicDraftActivity.class.getSimpleName() + SPUtil.getSinaUid());

        if(!CommonUtil.isEmptyCollection(temp)){
            temp.remove(topic);
        }
        SPUtil.setModule(temp, TopicDraftActivity.class.getSimpleName() + SPUtil.getSinaUid());
    }
}
