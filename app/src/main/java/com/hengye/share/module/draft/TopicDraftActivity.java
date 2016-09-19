package com.hengye.share.module.draft;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.module.topic.TopicAdapter;
import com.hengye.share.model.Topic;
import com.hengye.share.model.greenrobot.TopicDraft;
import com.hengye.share.model.greenrobot.TopicDraftHelper;
import com.hengye.share.service.TopicPublishService;
import com.hengye.share.module.publish.TopicPublishActivity;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.ui.widget.listener.OnItemClickListener;
import com.hengye.share.ui.widget.listener.OnItemLongClickListener;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

public class TopicDraftActivity extends BaseActivity implements DialogInterface.OnClickListener {

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
    public int getLayoutResId() {
        return R.layout.activity_topic_draft;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPublishResultBroadcastReceiver);
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        initView();
        mPublishResultBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                TopicDraft topicDraft = (TopicDraft) intent.getSerializableExtra(TopicPublishService.EXTRA_DRAFT);
                boolean isSuccess = intent.getBooleanExtra(TopicPublishService.EXTRA_IS_SUCCESS, false);
                if (topicDraft != null && !isSuccess) {
                    int index = mAdapter.getData().indexOf(topicDraft);
                    if (index != -1) {
                        mAdapter.updateItem(index, topicDraft);
                    } else {
                        mAdapter.addItem(0, topicDraft);
                        mRecyclerView.getLayoutManager().scrollToPosition(0);
                    }

                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mPublishResultBroadcastReceiver, new IntentFilter(TopicPublishService.ACTION_RESULT));
    }

    private RecyclerView mRecyclerView;
    private TopicDraftAdapter mAdapter;
    private List<TopicDraft> mTopicDraft;
    private BroadcastReceiver mPublishResultBroadcastReceiver;
    private Dialog mItemLongClickDialog, mConfirmToDeleteDialog;
    private int mItemLongClickPosition;

    private void initView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new TopicDraftAdapter(this, getTopicDraftData()));

//        mAdapter.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                IntentUtil.startActivityForResult(TopicDraftActivity.this,
//                        TopicPublishActivity.getStartIntent(TopicDraftActivity.this, mAdapter.getItem(position)), 1);
//            }
//        });
        mAdapter.setOnChildViewItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_topic_send_again) {
                    TopicDraft topicDraft = mAdapter.getItem(position);
                    TopicDraftHelper.removeTopicDraft(topicDraft);
                    mAdapter.removeItem(position);
                    TopicPublishService.publish(TopicDraftActivity.this, topicDraft, UserUtil.getToken());
                } else if (id == R.id.tv_topic_content || id == R.id.gl_topic_gallery || id == R.id.rl_topic_title || id == R.id.ll_topic_content || id == R.id.ll_topic_retweeted_content) {
                    final boolean isRetweeted = (Boolean) view.getTag();
                    if(!isRetweeted){
                        IntentUtil.startActivityForResult(TopicDraftActivity.this,
                                TopicPublishActivity.getStartIntent(TopicDraftActivity.this, mAdapter.getItem(position)), 1);
                    }
                    //为了显示波纹效果再启动
                    final Topic topic = mAdapter.getItem(position).getTargetTopic();
                    final TopicAdapter.TopicViewHolder vh = (TopicAdapter.TopicViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            TopicAdapter.TopicViewHolder.startTopicDetail(TopicDraftActivity.this, vh, isRetweeted, topic);
                        }
                    }, 200);
                } else {
                    IntentUtil.startActivityForResult(TopicDraftActivity.this,
                            TopicPublishActivity.getStartIntent(TopicDraftActivity.this, mAdapter.getItem(position)), 1);
                }
            }
        });

        mAdapter.setOnChildViewItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                mItemLongClickPosition = position;
                getItemLongClickDialog().show();
                return false;
            }
        });

    }

    private Dialog getItemLongClickDialog(){
        if(mItemLongClickDialog == null){
            mItemLongClickDialog = DialogBuilder.getItemDialog(this, this, getString(R.string.label_delete_draft));
        }
        return mItemLongClickDialog;
    }

    private Dialog getConfirmToDeleteDialog(){
        if(mConfirmToDeleteDialog == null){
            SimpleTwoBtnDialog stbd = new SimpleTwoBtnDialog();
            stbd.setContent(R.string.label_delete_all_drafts);
            stbd.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!mAdapter.isEmpty()) {
                        TopicDraftHelper.removeAllTopicDraft();
                        mAdapter.refresh(null);
                    }
                }
            });

            mConfirmToDeleteDialog = stbd.create(this);

        }
        return mConfirmToDeleteDialog;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
            default:
                TopicDraft topicDraft = mAdapter.getItem(mItemLongClickPosition);
                TopicDraftHelper.removeTopicDraft(topicDraft);
                mAdapter.removeItem(mItemLongClickPosition);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_topic_draft, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            getConfirmToDeleteDialog().show();
        }
        return super.onOptionsItemSelected(item);
    }

    private List<TopicDraft> getTopicDraftData() {

        mTopicDraft = TopicDraftHelper.getTopicDraft();
        if (CommonUtil.isEmpty(mTopicDraft)) {
            mTopicDraft = new ArrayList<>();
        }
        return mTopicDraft;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            mAdapter.refresh(getTopicDraftData());
        }
    }
}
