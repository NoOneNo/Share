package com.hengye.share.ui.widget.listview;

import com.hengye.draglistview.DragSortController;
import com.hengye.draglistview.DragSortListView;
import com.hengye.share.R;

public class DragSortListViewBuilder {

    public static void build(DragSortListView dslv){
        DragSortController dsc = getDefaultController(dslv);
        dslv.setFloatViewManager(dsc);
        dslv.setOnTouchListener(dsc);
    }

    public static DragSortController getDefaultController(DragSortListView dslv){
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.drag_handle);
        controller.setClickRemoveId(R.id.click_remove);
        controller.setRemoveEnabled(true);
        controller.setDragInitMode(DragSortController.ON_DOWN);
        controller.setRemoveMode(DragSortController.FLING_REMOVE);
//        controller.setBackgroundColor(Color.parseColor("#50ffffff"));
        controller.setBackgroundColor(dslv.getResources().getColor(R.color.background_default));
        return controller;
    }
}
