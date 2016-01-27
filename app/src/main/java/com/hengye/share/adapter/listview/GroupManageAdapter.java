package com.hengye.share.adapter.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hengye.draglistview.DragSortListView;
import com.hengye.share.R;
import com.hengye.share.model.greenrobot.GroupList;

import java.util.List;

public class GroupManageAdapter extends DropAdapter<GroupList, GroupManageAdapter.GroupManageViewHolder>{

    public GroupManageAdapter(Context context, List<GroupList> data){
        super(context, data);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_group_manage;
    }

    @Override
    public GroupManageViewHolder getViewHolder(View convertView) {
        return new GroupManageViewHolder(convertView);
    }

    public static class GroupManageViewHolder extends ViewHolder<GroupList>{

        TextView mGroupName, mGroupCount;

        public GroupManageViewHolder(View v) {
            super(v);

            mGroupName = (TextView) findViewById(R.id.tv_group_name);
            mGroupCount = (TextView) findViewById(R.id.tv_group_count);
        }

        @Override
        public void bindData(Context context, GroupList gl) {
            mGroupName.setText(gl.getName());
            mGroupCount.setText(String.format(context.getString(R.string.label_group_count), gl.getCount()));
        }
    }

}
