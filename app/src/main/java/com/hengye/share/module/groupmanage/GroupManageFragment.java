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
import com.hengye.share.module.util.encapsulation.fragment.RecyclerRefreshFragment;
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
public class GroupManageFragment extends RecyclerRefreshFragment implements GroupManageMvpView {

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        addPresenter(mPresenter = new GroupManagePresenter(this, new GroupManageRepository()));

        initView();

        mPresenter.loadGroupList();
    }

    private GroupManageAdapter mAdapter;

    private Dialog mConfirmDialog, mLoadingDialog;

    private GroupManagePresenter mPresenter;

    private EditText mCreateGroupName, mCreateGroupDesc;

    private void initView() {
        setHasOptionsMenu(true);
        setAdapter(mAdapter = new GroupManageAdapter(getContext(), new ArrayList<GroupList>()));

        ItemTouchUtil.attachByDrag(getRecyclerView(), mAdapter);

        initUpdateGroupOrderDialog();
    }

    public void initUpdateGroupOrderDialog() {
        mLoadingDialog = new LoadingDialog(getContext());

        SimpleTwoBtnDialog builder = new SimpleTwoBtnDialog();

        builder.setContent(getString(R.string.label_update_group_order));

        builder.setNegativeButtonClickListener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mPresenter.setIsGroupUpdate(false);
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

    @Override
    public boolean onBackPressed() {
        mPresenter.checkGroupOrderIsChange(mAdapter.getData());
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_group_manager_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
            mConfirmDialog.show();
        } else {
            finish();
        }
    }

    @Override
    public void finish() {
        if (mPresenter.isGroupUpdate()) {
            setResult(Activity.RESULT_OK);
        }
        super.finish();
    }

    @Override
    public void onRefresh() {
        mPresenter.refreshGroupList();
    }

    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadFail() {

    }

    @Override
    public void handleGroupList(boolean isCache, List<GroupList> groupLists) {
        if (!CommonUtil.isEmpty(groupLists)) {
            if (groupLists.get(0).getVisible() == -1) {
                groupLists.remove(0);
            }

            if (!isCache) {
                setResult(Activity.RESULT_OK);
            }
        }
        mAdapter.refresh(groupLists);
        setTaskComplete(true);
//        L.debug("wbGroups : {}", wbGroups.toString());
    }

    @Override
    public void updateGroupOrderCallBack(boolean isSuccess) {
        mLoadingDialog.dismiss();
        if (isSuccess) {
            ToastUtil.showToast(R.string.label_update_group_success);
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            ToastUtil.showToast(R.string.label_update_group_fail);
        }
    }

    @Override
    public void createGroupResult(int taskState, GroupList groupList) {
        if(TaskState.STATE_SUCCESS == taskState){
            mAdapter.addItem(groupList);
            UserUtil.insertGroupList(groupList);
        }else{
            ToastUtil.showToast(TaskState.toString(taskState));
        }
    }

    AlertDialog mCreateGroupDialog;

    public AlertDialog getCreateGroupDialog() {
        if (mCreateGroupDialog == null) {
            mCreateGroupDialog = new AlertDialog.Builder(getContext())
                    .setTitle(R.string.label_create_group)
                    .setNegativeButton(R.string.tip_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setPositiveButton(R.string.tip_confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //如果这里设置确定会对话框会自动取消，如果内容为空时应该提示不能为空，不自动取消对话框
                        }
                    })
                    .setView(R.layout.dialog_item_edit_group)
                    .create();
            mCreateGroupDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface d) {
                    if(mCreateGroupName != null){
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
                                    }else{
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
}
