package com.hengye.share.adapter.listview;

import android.content.Context;

import com.hengye.draglistview.DragSortListView;

import java.util.List;

public abstract class DropAdapter<T, V extends ViewHolder<T>> extends CommonAdapter<T, V>
        implements DragSortListView.DropListener, DragSortListView.RemoveListener {

    public DropAdapter(Context context, List<T> data) {
        super(context,  data);
    }

    @Override
    public void drop(int from, int to) {
        addItem(to, removeItem(from));
    }

    @Override
    public void remove(int which) {
        removeItem(which);
    }
}
