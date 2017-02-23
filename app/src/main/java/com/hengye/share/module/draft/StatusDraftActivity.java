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
import com.hengye.share.module.status.StatusAdapter;
import com.hengye.share.model.Status;
import com.hengye.share.model.greenrobot.StatusDraft;
import com.hengye.share.model.greenrobot.StatusDraftHelper;
import com.hengye.share.service.StatusPublishService;
import com.hengye.share.module.publish.StatusPublishActivity;
import com.hengye.share.module.base.BaseActivity;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemLongClickListener;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

public class StatusDraftActivity extends BaseActivity implements DialogInterface.OnClickListener {

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
        return R.layout.activity_status_draft;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mPublishResultBroadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        mPublishResultBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                StatusDraft statusDraft = (StatusDraft) intent.getSerializableExtra(StatusPublishService.EXTRA_DRAFT);
                int status = intent.getIntExtra(StatusPublishService.EXTRA_STATUS, StatusPublishService.STATUS_FAIL);
                if (statusDraft != null) {
                    int index = mAdapter.getData().indexOf(statusDraft);
                    if (status == StatusPublishService.STATUS_FAIL) {
                        //显示发送失败的草稿;
                        if (index != -1) {
                            mAdapter.updateItem(index, statusDraft);
                        } else {
                            mAdapter.addItem(0, statusDraft);
                            mRecyclerView.getLayoutManager().scrollToPosition(0);
                        }
                    }else if(index != -1){
                        //移除已显示的草稿当草稿状态为发送中等状态;
                        mAdapter.removeItem(index);
                    }

                }
            }
        };
        LocalBroadcastManager.getInstance(this).registerReceiver(mPublishResultBroadcastReceiver, new IntentFilter(StatusPublishService.ACTION_PUBLISH));
    }

    private RecyclerView mRecyclerView;
    private StatusDraftAdapter mAdapter;
    private List<StatusDraft> mStatusDraft;
    private BroadcastReceiver mPublishResultBroadcastReceiver;
    private Dialog mItemLongClickDialog, mConfirmToDeleteDialog;
    private int mItemClickPosition;

    private void initView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new StatusDraftAdapter(this, getStatusDraftData()));

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_topic_send_again) {
                    StatusDraft statusDraft = mAdapter.getItem(position);
//                    TopicDraftHelper.saveStatusDraft(topicDraft, TopicDraft.SENDING);
                    mAdapter.removeItem(position);
                    StatusPublishService.publish(StatusDraftActivity.this, statusDraft, UserUtil.getToken());
                } else if (id == R.id.tv_status_content || id == R.id.gl_status_gallery || id == R.id.layout_status_title || id == R.id.layout_status_content || id == R.id.item_status_retweeted_content) {
                    final boolean isRetweeted = (Boolean) view.getTag();
                    if (!isRetweeted) {
                        startPublishStatus(position);
                    }
                    //为了显示波纹效果再启动
                    final Status topic = mAdapter.getItem(position).getTargetStatus();
                    final StatusAdapter.StatusViewHolder vh = (StatusAdapter.StatusViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
                    getHandler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            StatusAdapter.StatusViewHolder.startStatusDetail(StatusDraftActivity.this, vh, isRetweeted, topic);
                        }
                    }, 200);
                } else {
                    startPublishStatus(position);
                }
            }
        });

        mAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(View view, int position) {
                mItemClickPosition = position;
                getItemLongClickDialog().show();
                return false;
            }
        });

    }

    private void startPublishStatus(int position){
        mItemClickPosition = position;
        IntentUtil.startActivityForResult(StatusDraftActivity.this,
                StatusPublishActivity.getStartIntent(StatusDraftActivity.this, mAdapter.getItem(position)), 1);
    }

    private Dialog getItemLongClickDialog() {
        if (mItemLongClickDialog == null) {
            mItemLongClickDialog = DialogBuilder.getItemDialog(this, this, getString(R.string.label_delete_draft));
        }
        return mItemLongClickDialog;
    }

    private Dialog getConfirmToDeleteDialog() {
        if (mConfirmToDeleteDialog == null) {
            SimpleTwoBtnDialog stbd = new SimpleTwoBtnDialog();
            stbd.setContent(R.string.label_delete_all_drafts);
            stbd.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (!mAdapter.isEmpty()) {
                        StatusDraftHelper.removeAllStatusDraft();
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
                StatusDraft statusDraft = mAdapter.getItem(mItemClickPosition);
                StatusDraftHelper.removeStatusDraft(statusDraft);
                mAdapter.removeItem(mItemClickPosition);
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

    private List<StatusDraft> getStatusDraftData() {

        mStatusDraft = StatusDraftHelper.getStatusDraft();
        if (CommonUtil.isEmpty(mStatusDraft)) {
            mStatusDraft = new ArrayList<>();
        }
        return mStatusDraft;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            StatusDraft draft = mAdapter.getItem(mItemClickPosition);
            if(draft != null){
                StatusDraft newDraft = StatusDraftHelper.loadStatusDraft(draft.getId());
                mAdapter.updateItem(mItemClickPosition, newDraft);
            }
//            mAdapter.loadTopic(getStatusDraftData());
        }
    }
}
