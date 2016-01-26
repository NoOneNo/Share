package com.hengye.share.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.hengye.draglistview.DragSortController;
import com.hengye.draglistview.DragSortListView;
import com.hengye.share.R;
import com.hengye.share.adapter.listview.GroupManageAdapter;
import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.model.sina.WBGroups;
import com.hengye.share.ui.base.BaseActivity;
import com.hengye.share.ui.mvpview.GroupManageMvpView;
import com.hengye.share.ui.presenter.GroupManagePresenter;
import com.hengye.share.util.L;
import com.hengye.share.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class GroupManageActivity extends BaseActivity implements GroupManageMvpView{

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
        super.afterCreate(savedInstanceState);

        setupPresenter(mPresenter = new GroupManagePresenter(this));

        initView();

        mPresenter.loadGroupList();
    }

    private DragSortListView mDragSortListView;
    private DragSortController mDragSortController;
    private GroupManageAdapter mAdapter;

    private GroupManagePresenter mPresenter;

    private void initView(){
        mDragSortListView = (DragSortListView) findViewById(R.id.drag_sort_listview);

        mDragSortListView.setAdapter(mAdapter = new GroupManageAdapter(this, new ArrayList<GroupList>()));

        mDragSortController = buildController(mDragSortListView);
        mDragSortListView.setFloatViewManager(mDragSortController);
        mDragSortListView.setOnTouchListener(mDragSortController);
        mDragSortListView.setDragEnabled(true);

    }

    protected DragSortController buildController(DragSortListView dslv) {
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.ic_nav);
//        controller.setClickRemoveId(R.id.click_remove);
        controller.setRemoveEnabled(false);
        controller.setSortEnabled(true);
        controller.setDragInitMode(DragSortController.ON_DOWN);
        controller.setRemoveMode(DragSortController.FLING_REMOVE);
        controller.setBackgroundColor(Color.parseColor("#50ffffff"));
        return controller;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_update) {

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void loadSuccess() {

    }

    @Override
    public void loadFail() {

    }

    @Override
    public void handleGroupList(List<GroupList> groupLists) {
        mAdapter.refresh(groupLists);
//        L.debug("wbGroups : {}", wbGroups.toString());
    }
}
