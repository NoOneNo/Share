package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.view.View;

import com.hengye.share.model.AtUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhy on 16/8/22.
 */
public abstract class EditModeAdapter<T, VH extends EditModeAdapter.EditModeViewHolder> extends CommonAdapter<T, VH> {

    public EditModeAdapter(Context context) {
        this(context, new ArrayList<T>());
    }

    public EditModeAdapter(Context context, List<T> data) {
        super(context, data);
    }

    boolean mEditMode = false;

    public boolean isEditMode() {
        return mEditMode;
    }

    public void setEditMode(boolean editMode) {
        this.mEditMode = editMode;
        notifyDataSetChanged();
    }

    @Override
    public void onBindBasicItemView(VH holder, int position) {
        super.onBindBasicItemView(holder, position);
        holder.updateEditMode(isEditMode());
    }

    public static abstract class EditModeViewHolder<T> extends CommonAdapter.ItemViewHolder<T> {
        public EditModeViewHolder(View v) {
            super(v);
        }
        public abstract void updateEditMode(boolean isEditMode);
    }
}
