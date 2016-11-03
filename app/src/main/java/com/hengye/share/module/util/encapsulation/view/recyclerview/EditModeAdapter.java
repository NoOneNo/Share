package com.hengye.share.module.util.encapsulation.view.recyclerview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhy on 16/8/22.
 */
public abstract class EditModeAdapter<T> extends CommonAdapter<T> {

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
    public abstract EditModeViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindBasicItemView(ItemViewHolder holder, int position) {
        super.onBindBasicItemView(holder, position);
        if(holder instanceof EditModeViewHolder) {
            ((EditModeViewHolder)holder).updateEditMode(isEditMode());
        }
    }

    public static abstract class EditModeViewHolder<T> extends ItemViewHolder<T> {
        public EditModeViewHolder(View v) {
            super(v);
        }

        public abstract void updateEditMode(boolean isEditMode);
    }
}
