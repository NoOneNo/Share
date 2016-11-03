package com.hengye.share.module.util.encapsulation.view.recyclerview;

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
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = mAdapter.isItemDragEnabled(getBasicItemPosition(viewHolder)) ? mDragFlags : ItemTouchHelper.ACTION_STATE_IDLE;
        int swipeFlags = mAdapter.isItemSwipeEnabled(getBasicItemPosition(viewHolder)) ? mSwipeFlags : ItemTouchHelper.ACTION_STATE_IDLE;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                          RecyclerView.ViewHolder target) {
        mAdapter.moveItem(getBasicItemPosition(viewHolder), getBasicItemPosition(target));
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        mAdapter.removeItem(viewHolder.getAdapterPosition());
    }

    public int getBasicItemPosition(RecyclerView.ViewHolder viewHolder){
        return mAdapter.getBasicItemPosition(viewHolder.getAdapterPosition());
    }

    public int getSwipeFlags() {
        return mSwipeFlags;
    }

    /**
     * ItemTouchHelper.START | ItemTouchHelper.END
     *
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
     *
     * @param dragFlags
     */
    public void setDragFlags(int dragFlags) {
        this.mDragFlags = dragFlags;
    }

    public ItemTouchHelperAdapter getItemTouchHelperAdapter() {
        return mAdapter;
    }
}