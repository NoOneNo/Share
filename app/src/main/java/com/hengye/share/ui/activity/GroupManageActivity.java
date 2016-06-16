package com.hengye.share.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hengye.draglistview.DragSortListView;
import com.hengye.share.R;
import com.hengye.share.adapter.listview.GroupManageAdapter;
import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.mvpview.GroupManageMvpView;
import com.hengye.share.ui.presenter.GroupManagePresenter;
import com.hengye.share.ui.widget.dialog.LoadingDialog;
import com.hengye.share.ui.widget.dialog.SimpleTwoBtnDialog;
import com.hengye.share.ui.widget.listview.DragSortListViewBuilder;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class GroupManageActivity extends BaseActivity implements GroupManageMvpView{

    public final static int GROUP_UPDATE = 11;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_group_manager_menu, menu);
        return true;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_group_manage;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        setupPresenter(mPresenter = new GroupManagePresenter(this));

        initView();

        mPresenter.loadGroupList();
    }

    private DragSortListView mDragSortListView;
    private GroupManageAdapter mAdapter;

    private Dialog mConfirmDialog, mLoadingDialog;

    private GroupManagePresenter mPresenter;

    private void initView(){
        mDragSortListView = (DragSortListView) findViewById(R.id.drag_sort_list_view);
        mDragSortListView.setAdapter(mAdapter = new GroupManageAdapter(this, new ArrayList<GroupList>()));
        DragSortListViewBuilder.build(mDragSortListView);

        initUpdateGroupOrderDialog();
    }

    public void initUpdateGroupOrderDialog() {
        mLoadingDialog = new LoadingDialog(this);

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
                mPresenter.updateGroupOrder(mAdapter.getData());
            }
        });
        mConfirmDialog = builder.create(this);
    }

    @Override
    public void onBackPressed() {
        mPresenter.checkGroupOrderIsChange(mAdapter.getData());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_update) {
            mPresenter.loadGroupList(false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void checkGroupOrder(boolean isChange) {
        if(isChange){
            mConfirmDialog.show();
        }else{
            finish();
        }
    }

    @Override
    public void finish() {
        if(mPresenter.isGroupUpdate()){
            setResult(RESULT_OK);
        }
        super.finish();
    }

    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadFail() {

    }

    @Override
    public void handleGroupList(List<GroupList> groupLists) {
        if(!CommonUtil.isEmpty(groupLists)){
            if(groupLists.get(0).getVisible() == -1){
                groupLists.remove(0);
            }
        }
        mAdapter.refresh(groupLists);
//        L.debug("wbGroups : {}", wbGroups.toString());
    }

    @Override
    public void updateGroupOrderCallBack(boolean isSuccess) {
        mLoadingDialog.dismiss();
        if(isSuccess){
            ToastUtil.showToast("更新成功");
            setResult(Activity.RESULT_OK);
            finish();
        }else{
            ToastUtil.showToast("更新失败");
        }
    }
}
