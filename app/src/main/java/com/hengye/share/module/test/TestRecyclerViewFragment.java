package com.hengye.share.module.test;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.hengye.share.adapter.recyclerview.ItemViewHolder;
import com.hengye.share.module.util.encapsulation.paging.PagingConfig;
import com.hengye.share.module.util.encapsulation.paging.RecyclerFragment;
import com.hengye.share.ui.widget.pulltorefresh.PullToRefreshLayout;
import com.hengye.share.ui.widget.recyclerview.SimpleItemTouchHelperCallback;
import com.hengye.swiperefresh.listener.SwipeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhy on 2016/9/27.
 */

public class TestRecyclerViewFragment extends RecyclerFragment<String>{

    @Override
    public int getContentResId() {
        return R.layout.fragment_test_recyclerview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Object> data = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            if(i % 2 == 0){
                data.add("" + i);
            }else{
                data.add(i);
            }
        }

//        data.add("a");
//        data.add("b");
//        data.add("c");
//        data.add("d");
//        data.add("e");
//        data.add("f");
//        data.add("h");
//        data.add("i");
//        data.add("j");
//        data.add("k");
//        data.add("l");
//        data.add("a");
//        data.add("b");
//        data.add("c");
//        data.add("d");
//        data.add("e");
//        data.add("f");
//        data.add("h");
//        data.add("i");
//        data.add("j");
//        data.add("k");
//        data.add("l");


        getRecyclerView().setAdapter(mAdapter = new TestAdapter(getContext(), data));

        mLayoutManager.setSpanCount(1);

        SimpleItemTouchHelperCallback callback = new SimpleItemTouchHelperCallback(mAdapter);
        callback.setDragFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(getRecyclerView());

        findViewById(R.id.btn_add_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View header = LayoutInflater.from(getContext()).inflate(R.layout.footer_load_more, null);
                mAdapter.setHeader(header);
//                mLayoutManager.scrollToPosition(0);

//                mAdapter.removeHeader();
            }
        });
        findViewById(R.id.btn_add_footer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                mAdapter.addItem(2, "haha");
//
                View header = LayoutInflater.from(getContext()).inflate(R.layout.footer_load_more, null);

//                mAdapter.setFooter(header);

                mAdapter.removeHeader();
//                mLayoutManager.scrollToPosition(mLayoutManager.getItemCount() - 1);

            }
        });

        final PullToRefreshLayout pullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.pull_to_refresh);

//        pullToRefreshLayout.setLoadEnable(true);
        pullToRefreshLayout.setOnLoadListener(new SwipeListener.OnLoadListener() {
            @Override
            public void onLoad() {

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        List<Object> data = new ArrayList<>();
                        data.add(1);
                        data.add(2);
                        data.add(3);
                        mAdapter.addAll(data);
                        pullToRefreshLayout.stopLoading(true);
                    }
                },1000);
            }
        });


    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager = new GridLayoutManager(getContext(), 3);
    }

    Handler mHandler = new Handler();
    GridLayoutManager mLayoutManager;
    TestAdapter mAdapter;

    @Override
    public void handleDataType(int type) {

    }

    @Override
    public void updatePagingConfig(PagingConfig pagingConfig) {

    }

    public static class TestAdapter extends CommonAdapter<Object> {

        public TestAdapter(Context context, List<Object> data){
            super(context, data);
        }

        @Override
        public int getBasicItemType(int position) {
            if(getItem(position) instanceof String){
                return 1;
            }else{
                return 2;
            }
        }

        @Override
        public ItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
            if(viewType == 1) {
                return new TestHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_text_key_value_h, parent, false));
            }else{
                return new Test2Holder(LayoutInflater.from(getContext()).inflate(R.layout.item_text_key_value_v, parent, false));
            }
        }

        public static class TestHolder extends ItemViewHolder<String> {

            TextView key, value;
            public TestHolder(View v){
                super(v);
                key = (TextView) findViewById(R.id.tv_key);
                value = (TextView) findViewById(R.id.tv_value);
            }

            @Override
            public void bindData(Context context, String str, int position) {
                key.setText("string");
                value.setText(str);
            }
        }

        public static class Test2Holder extends ItemViewHolder<Integer> {

            TextView key, value;
            public Test2Holder(View v){
                super(v);
                key = (TextView) findViewById(R.id.tv_key);
                value = (TextView) findViewById(R.id.tv_value);
            }

            @Override
            public void bindData(Context context, Integer integer, int position) {
                key.setText("int");
                value.setText("" + integer);
            }
        }
    }
}
