package com.hengye.share.module.groupmanage;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.adapter.listview.DropAdapter;
import com.hengye.share.adapter.listview.ViewHolder;
import com.hengye.share.model.greenrobot.GroupList;
import com.hengye.share.ui.widget.dialog.DialogBuilder;

import java.util.List;

public class GroupManageAdapter extends DropAdapter<GroupList, GroupManageAdapter.GroupManageViewHolder>
        implements DialogInterface.OnClickListener {

    private Dialog mDeleteDialog;

    public GroupManageAdapter(Context context, List<GroupList> data) {
        super(context, data);

        mDeleteDialog = DialogBuilder.getItemDialog(context, this, "删除");
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which){
            case 0:
            default:

                break;
        }
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_group_manage;
    }

    @Override
    public GroupManageViewHolder getViewHolder(View convertView) {
        return new GroupManageViewHolder(convertView);
    }

    public static class GroupManageViewHolder extends ViewHolder<GroupList> {

        TextView mGroupName, mGroupCount;

        public GroupManageViewHolder(View v) {
            super(v);

            mGroupName = (TextView) findViewById(R.id.tv_group_name);
            mGroupCount = (TextView) findViewById(R.id.tv_group_count);
        }


        @Override
        public void bindData(Context context, GroupList gl, int position) {
            mGroupName.setText(gl.getName());
            mGroupCount.setText(String.format(context.getString(R.string.label_group_count), gl.getCount()));
        }
    }

}
