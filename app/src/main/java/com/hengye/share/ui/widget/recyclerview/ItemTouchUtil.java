package com.hengye.share.ui.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by yuhy on 2016/10/5.
 */

public class ItemTouchUtil {

    public static ItemTouchHelper attachByDrag(RecyclerView recyclerView, ItemTouchHelperAdapter adapter){
        return attachByDrag(recyclerView, adapter, ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END);
    }

    public static ItemTouchHelper attachByDrag(RecyclerView recyclerView, ItemTouchHelperAdapter adapter, int dragFlags){
        return attach(recyclerView, adapter, dragFlags, 0);
    }

    public static ItemTouchHelper attachBySwipe(RecyclerView recyclerView, ItemTouchHelperAdapter adapter){
        return attachBySwipe(recyclerView, adapter, ItemTouchHelper.START | ItemTouchHelper.END);
    }

    public static ItemTouchHelper attachBySwipe(RecyclerView recyclerView, ItemTouchHelperAdapter adapter, int swipeFlags){
        return attach(recyclerView, adapter, 0, swipeFlags);
    }

    public static ItemTouchHelper attach(RecyclerView recyclerView, ItemTouchHelperAdapter adapter, int dragFlags, int swipeFlags){
        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(adapter);
        if(dragFlags != 0){
            callback.setDragFlags(dragFlags);
        }
        if(swipeFlags != 0) {
            callback.setSwipeFlags(swipeFlags);
        }
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return itemTouchHelper;
    }
}
