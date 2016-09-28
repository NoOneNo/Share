package com.hengye.share.ui.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by yuhy on 2016/9/28.
 */

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private final ItemTouchHelperAdapter mAdapter;
    private int mDragFlags, mSwipeFlags;

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(mDragFlags, mSwipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.removeItem(viewHolder.getAdapterPosition());
    }

    public int getSwipeFlags() {
        return mSwipeFlags;
    }

    /**
     * ItemTouchHelper.START | ItemTouchHelper.END
     * @param swipeFlags
     */
    public void setSwipeFlags(int swipeFlags) {
        this.mSwipeFlags = swipeFlags;
    }

    public int getDragFlags() {
        return mDragFlags;
    }

    /**
     * ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END
     * @param dragFlags
     */
    public void setDragFlags(int dragFlags) {
        this.mDragFlags = dragFlags;
    }
}