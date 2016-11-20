package com.hengye.share.module.groupmanage;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.module.topic.TopicPresenter.TopicGroup;
import com.hengye.share.module.topic.TopicPresenter.TopicType;
import com.hengye.share.module.util.encapsulation.fragment.RecyclerRefreshFragment;
import com.hengye.share.util.SPUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhy on 16/9/8.
 */
public class GroupListFragment extends RecyclerRefreshFragment<TopicGroup>{

    @Override
    public int getContentResId() {
        return R.layout.fragment_recycler_refresh_vertical;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAdapter(mAdapter = new GroupListAdapter(getContext(), new ArrayList<TopicGroup>()));
        mAdapter.setOnItemClickListener(this);
        mLayoutManager = (LinearLayoutManager)getRecyclerView().getLayoutManager();
        getRecyclerView().setScrollbarFadingEnabled(true);
        getPullToRefresh().setRefreshEnable(false);

        mMaxWidth = mDefaultOffset = ViewUtil.dp2px(175f);
        mMaxHeight = ViewUtil.dp2px(340f);
        mHalfScreenWidth = ViewUtil.getScreenWidth(getActivity()) / 2;

        mLastSelectPosition = SPUtil.getInt("GroupListSelectPosition@" + UserUtil.getUid(), 0);

    }

    @Override
    public void onPause() {
        SPUtil.putInt("GroupListSelectPosition@" + UserUtil.getUid(), mAdapter.getSelectPosition());
        super.onPause();
    }

    private void adjustSize(){
        if(getParent() == null){
            return;
        }

        // 设置列表的尺寸
        int width = mMaxWidth;
        if (width > mHalfScreenWidth) {
            width = mHalfScreenWidth;
        }

        if (mAdapter.getItemCount() > 7) {
            getParent().setLayoutParams(new FrameLayout.LayoutParams(width, mMaxHeight));
        }else {
            getParent().setLayoutParams(new FrameLayout.LayoutParams(width, FrameLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    public void load(boolean isRefresh, boolean selected){
        List<TopicGroup> groups = TopicGroup.getAllTopicGroups();
        TopicGroup tg = new TopicGroup(TopicType.NONE);
        groups.add(tg);

        mAdapter.refresh(groups);
        adjustSize();

        if(isRefresh){
            mLastSelectPosition = 0;
        }else if(mLastSelectPosition < 0 || mLastSelectPosition >= mAdapter.getBasicItemCount()){
            mLastSelectPosition = 0;
        }
        mAdapter.setSelectPosition(mLastSelectPosition);

        if(selected) {
            selectGroupByPosition(mLastSelectPosition);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        super.onItemClick(view, position);

        boolean isSelected = true;
        if(position != mAdapter.getLastItemPosition()){
            //当为最后的位置时,选择分组管理,不设置选中效果
            isSelected = mAdapter.setSelectPosition(position);
        }
        if (isSelected){
            selectGroupByPosition(position);
        }
    }

    protected void selectGroupByPosition(int position){
        if (getActivity() instanceof OnGroupSelectedCallback) {
            ((OnGroupSelectedCallback) getActivity()).onGroupSelected(position, mAdapter.getItem(position));
        }
    }

    GroupListAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    int mMaxWidth, mMaxHeight, mDefaultOffset, mHalfScreenWidth;
    int mLastSelectPosition;

    public void scrollToSelectPosition() {
        mLayoutManager.scrollToPositionWithOffset(mAdapter.getSelectPosition(), mDefaultOffset);
    }

    public interface OnGroupSelectedCallback {

        void onGroupSelected(int position, TopicGroup group);

    }

    public static class GroupListAdapter extends CommonAdapter<TopicGroup> {

        public GroupListAdapter(Context context, List<TopicGroup> data) {
            super(context, data);
        }

        @Override
        public GroupListViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
            return new GroupListViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_group_list, parent, false));
        }

        @Override
        public void onBindBasicItemView(ItemViewHolder holder, int position) {
            super.onBindBasicItemView(holder, position);
            holder.itemView.setBackgroundResource(isSelectPosition(position) ? R.drawable.ripple_grey : R.drawable.ripple_white);
        }

        public static class GroupListViewHolder extends ItemViewHolder<TopicGroup> {

            TextView text;

            public GroupListViewHolder(View v) {
                super(v);
                text = (TextView) findViewById(R.id.tv_text);
            }

            @Override
            public void bindData(Context context, TopicGroup topicGroup, int position) {
                text.setText(topicGroup.getName());

            }
        }
    }
}
