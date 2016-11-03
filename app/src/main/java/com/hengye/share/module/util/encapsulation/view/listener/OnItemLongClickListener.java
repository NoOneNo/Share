package com.hengye.share.module.util.encapsulation.view.listener;

import android.view.View;

/**
 * Interface definition for a callback to be invoked when an item in this
 * RecyclerView has been clicked.
 */
public interface OnItemLongClickListener {

    /**
     * Callback method to be invoked when an item in this RecyclerView has
     * been clicked.
     * <p>
     * Implementers can call getItemAtPosition(position) if they need
     * to access the data associated with the selected item.
     *
     * @param view     The view within the AdapterView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     */
    boolean onItemLongClick(View view, int position);
}
