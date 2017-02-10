package com.hengye.share.module.groupmanage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.hengye.share.R;
import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.module.groupmanage.data.GroupManageRepository;
import com.hengye.share.module.util.encapsulation.base.TaskState;
import com.hengye.share.module.base.ShareRecyclerFragment;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.ui.widget.dialog.LoadingDialog;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemTouchUtil;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhy on 16/9/19.
 */
public class GroupManageFragment extends ShareRecyclerFragment implements GroupManageContract.View {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new GroupManagePresenter(this, new GroupManageRepository());
        initView();

        showLoading();
        mPresenter.loadGroupList();
    }

    private GroupManageAdapter mAdapter;

    private Dialog mConfirmDialog, mLoadingDialog;

    private GroupManageContract.Presenter mPresenter;

    private EditText mCreateGroupName, mCreateGroupDesc;
    private EditText mEditGroupName, mEditGroupDesc;

    private GroupList mCurrentGroupList;

    private void initView() {
        setHasOptionsMenu(true);
        setAdapter(mAdapter = new GroupManageAdapter(getContext(), new ArrayList<GroupList>()));

        ItemTouchUtil.attachByDrag(getRecyclerView(), mAdapter);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mCurrentGroupList = mAdapter.getItem(position);
                int id = view.getId();
                if(id == R.id.btn_delete){
                    getDeleteGroupDialog().show();
                }else if(id == R.id.btn_edit){
                    getEditGroupDialog().show();
                }
            }
        });
    }

    public Dialog getUpdateGroupOrderDialog() {
        if(mConfirmDialog == null) {
            mLoadingDialog = new LoadingDialog(getContext());

            SimpleTwoBtnDialog builder = new SimpleTwoBtnDialog();

            builder.setContent(getString(R.string.tip_update_group_order));

            builder.setNegativeButtonClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setPositiveButtonClickListener(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mLoadingDialog.show();
                    mPresenter.updateGroupOrder(new ArrayList<>(mAdapter.getData()));
                }
            });
            mConfirmDialog = builder.create(getContext());
        }
        return mConfirmDialog;
    }

    @Override
    public boolean onBackPressed() {
        mPresenter.checkGroupOrderIsChange(mAdapter.getData());
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_group_manager_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_update) {
            getCreateGroupDialog().show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void checkGroupOrder(boolean isChange) {
        if (isChange) {
            getUpdateGroupOrderDialog().show();
        } else {
            finish();
        }
    }

    private void setIsGroupUpdate(){
        setResult(Activity.RESULT_OK);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshGroupList();
    }

    @Override
    public void loadGroupList(boolean isCache, List<GroupList> groupLists) {
        if (!CommonUtil.isEmpty(groupLists)) {
            if (groupLists.get(0).getVisible() == -1) {
                groupLists.remove(0);
            }

            if (!isCache) {
                setIsGroupUpdate();
            }
        }
        mAdapter.refresh(groupLists);
        setTaskComplete(true);
//        L.debug("wbGroups : {}", wbGroups.toString());
    }

    @Override
    public void updateGroupOrderCallBack(int taskState) {
        mLoadingDialog.dismiss();
        if (TaskState.isSuccess(taskState)) {
            ToastUtil.showToastSuccess(R.string.tip_update_group_success);
            setIsGroupUpdate();
            finish();
        } else {
            TaskState.toastState(taskState);
        }
    }

    @Override
    public void createGroupResult(int taskState, GroupList groupList) {
        if (TaskState.isSuccess(taskState)) {
            mAdapter.addItem(groupList);
            UserUtil.insertGroupList(groupList);
            ToastUtil.showToastSuccess(R.string.tip_create_group_success);
        } else {
            ToastUtil.showToast(TaskState.toString(taskState));
        }
    }

    @Override
    public void updateGroupResult(int taskState, GroupList groupList) {
        if (TaskState.isSuccess(taskState)) {
            mAdapter.updateItem(groupList);
            UserUtil.updateGroupList(groupList);
            ToastUtil.showToastSuccess(R.string.tip_update_group_success);
            setIsGroupUpdate();
        } else {
            TaskState.toastState(taskState);
        }
    }

    @Override
    public void deleteGroupResult(int taskState, GroupList groupList) {
        if (TaskState.isSuccess(taskState)) {
            mAdapter.removeItem(groupList);
            UserUtil.deleteGroupList(groupList);
            ToastUtil.showToastSuccess(R.string.tip_delete_group_success);
        } else {
            TaskState.toastState(taskState);
        }
    }

    AlertDialog mCreateGroupDialog;
    public AlertDialog getCreateGroupDialog() {
        if (mCreateGroupDialog == null) {
            mCreateGroupDialog = new AlertDialog.Builder(getContext())
                    .setTitle(R.string.label_create_group)
                    .setNegativeButton(R.string.tip_cancel, null)
                    .setPositiveButton(R.string.tip_confirm, null)//如果这里设置确定会对话框会自动取消，如果内容为空时应该提示不能为空，不自动取消对话框
                    .setView(R.layout.dialog_item_edit_group)
                    .create();
            mCreateGroupDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface d) {
                    if (mCreateGroupName != null) {
                        //已经初始化
                        return;
                    }
                    mCreateGroupName = (EditText) mCreateGroupDialog.findViewById(R.id.et_name);
                    mCreateGroupDesc = (EditText) mCreateGroupDialog.findViewById(R.id.et_desc);
                    mCreateGroupDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String name = mCreateGroupName.getText().toString();
                                    String desc = mCreateGroupDesc.getText().toString();
                                    if (TextUtils.isEmpty(name)) {
                                        ToastUtil.showToast(R.string.error_group_name_null);
                                    } else {
                                        mPresenter.createGroup(name, desc);
                                        mCreateGroupDialog.dismiss();
                                    }
                                }
                            });
                }
            });
        }
        return mCreateGroupDialog;
    }

    AlertDialog mEditGroupDialog;
    public AlertDialog getEditGroupDialog() {
        if (mEditGroupDialog == null) {
            mEditGroupDialog = new AlertDialog.Builder(getContext())
                    .setTitle(R.string.label_create_group)
                    .setNegativeButton(R.string.dialog_text_cancel, null)
                    .setPositiveButton(R.string.dialog_text_confirm, null)//如果这里设置确定会对话框会自动取消，如果内容为空时应该提示不能为空，不自动取消对话框
                    .setView(R.layout.dialog_item_edit_group)
                    .create();
            mEditGroupDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface d) {
                    if (mCreateGroupName == null) {
                        //还没初始化
                        mEditGroupName = (EditText) mEditGroupDialog.findViewById(R.id.et_name);
                        mEditGroupDesc = (EditText) mEditGroupDialog.findViewById(R.id.et_desc);
                        mEditGroupDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                                .setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        String name = mEditGroupName.getText().toString();
                                        String desc = mEditGroupDesc.getText().toString();
                                        if (TextUtils.isEmpty(name)) {
                                            ToastUtil.showToast(R.string.error_group_name_null);
                                        } else {
                                            if(mCurrentGroupList != null) {
                                                mPresenter.updateGroup(mCurrentGroupList.getGid(), name, desc);
                                            }
                                            mEditGroupDialog.dismiss();
                                        }
                                    }
                                });
                    }

                    if(mCurrentGroupList != null) {
                        mEditGroupName.setText(mCurrentGroupList.getName());
                        mEditGroupDesc.setText(mCurrentGroupList.getDescription());
                    }

                }
            });
        }
        return mEditGroupDialog;
    }

    AlertDialog mDeleteGroupDialog;
    private AlertDialog getDeleteGroupDialog() {
        if (mDeleteGroupDialog == null) {
            mDeleteGroupDialog = new AlertDialog.Builder(getContext())
                    .setTitle(R.string.dialog_text_tip)
                    .setMessage(R.string.tip_delete_group_confirm)
                    .setNegativeButton(R.string.dialog_text_cancel, null)
                    .setPositiveButton(R.string.dialog_text_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(mCurrentGroupList != null) {
                                mPresenter.deleteGroup(mCurrentGroupList.getGid());
                            }
                        }
                    })
                    .create();
        }
        return mDeleteGroupDialog;
    }
}







