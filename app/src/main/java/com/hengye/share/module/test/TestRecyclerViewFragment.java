package com.hengye.share.module.test;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.paging.PagingConfig;
import com.hengye.share.module.util.encapsulation.paging.RecyclerFragment;
import com.hengye.share.ui.widget.recyclerview.ItemTouchHelperAdapter;
import com.hengye.share.ui.widget.recyclerview.SimpleItemTouchHelperCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhy on 2016/9/27.
 */

public class TestRecyclerViewFragment extends RecyclerFragment<String>{


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<String> data = new ArrayList<>();
        data.add("a");
        data.add("b");
        data.add("c");
        data.add("d");
        data.add("e");
        data.add("f");
        data.add("h");
        data.add("i");
        data.add("j");
        data.add("k");
        data.add("l");
        data.add("a");
        data.add("b");
        data.add("c");
        data.add("d");
        data.add("e");
        data.add("f");
        data.add("h");
        data.add("i");
        data.add("j");
        data.add("k");
        data.add("l");


        getRecyclerView().setAdapter(mAdapter = new TestAdapter(getContext(), data));

        mLayoutManager.setSpanCount(2);

        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(mAdapter);
        callback.setDragFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(getRecyclerView());
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager = new GridLayoutManager(getContext(), 3);
    }

    GridLayoutManager mLayoutManager;
    TestAdapter mAdapter;

    @Override
    public void handleDataType(int type) {

    }

    @Override
    public void updatePagingConfig(PagingConfig pagingConfig) {

    }

    public static class TestAdapter extends CommonAdapter<String, TestAdapter.TestHolder>{

        public TestAdapter(Context context, List<String> data){
            super(context, data);
        }

        @Override
        public TestHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
            return new TestHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text_key_value_h, parent, false));
        }

        public static class TestHolder extends CommonAdapter.ItemViewHolder<String>{

            TextView key, value;
            public TestHolder(View v){
                super(v);
                key = (TextView) findViewById(R.id.tv_key);
                value = (TextView) findViewById(R.id.tv_value);
            }

            @Override
            public void bindData(Context context, String str, int position) {
                key.setText(str);
                value.setText(str);
            }
        }
    }
}
