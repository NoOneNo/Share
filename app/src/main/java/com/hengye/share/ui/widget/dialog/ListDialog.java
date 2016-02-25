package com.hengye.share.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.CommonAdapter;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class ListDialog extends Dialog {

    public ListDialog(Context context, List<KeyValue> keyValues) {
        this(context, new SimpleListAdapter(context, keyValues));
    }

    public ListDialog(Context context, ListAdapter listAdapter) {
        super(context, R.style.ListDialog);

        mAdapter = listAdapter;

        List<KeyValue> data = new ArrayList<>();
        data.add(new KeyValue(R.drawable.compose_camerabutton_background_highlighted, "camera"));
        data.add(new KeyValue(R.drawable.compose_camerabutton_background_highlighted, "photo"));
        data.add(new KeyValue(R.drawable.compose_camerabutton_background_highlighted, "camera"));
        mAdapter = new SimpleListAdapter(getContext(), data);

        setOnItemListener(new ViewUtil.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtil.showToast("click : " + position);
            }
        });

        initDefaultWidth();
        initView();
    }

    private ListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private ViewUtil.OnItemClickListener mOnItemListener;

    private int mWidth, mMaxHeight;

    private void initDefaultWidth(){
        mWidth = getContext().getResources().getDimensionPixelSize(R.dimen.dialog_list_default_width);
        mMaxHeight = getContext().getResources().getDimensionPixelSize(R.dimen.dialog_list_default_max_height);
    }

    private void initView(){
        setContentView(R.layout.dialog_list);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        if(getOnItemListener() != null){
            mAdapter.setOnItemClickListener(getOnItemListener());
        }


        adjustDialogSize();

    }

    public void adjustDialogSize(){
        int width, height;

        width = getWidth();
        height = mAdapter.getItemHeight() * mAdapter.getItemCount();
        if(height > getMaxHeight()){
            height = getMaxHeight();
        }

        ViewGroup.LayoutParams lp = mRecyclerView.getLayoutParams();
        lp.width = width;
        lp.height = height;
        mRecyclerView.setLayoutParams(lp);
    }

    public void setOverScrollMode(int overScrollMode){
        mRecyclerView.setOverScrollMode(overScrollMode);
    }

    public ViewUtil.OnItemClickListener getOnItemListener() {
        return mOnItemListener;
    }

    public void setOnItemListener(ViewUtil.OnItemClickListener onItemListener) {
        this.mOnItemListener = onItemListener;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public int getMaxHeight() {
        return mMaxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.mMaxHeight = maxHeight;
    }

    public static class SimpleListAdapter extends ListAdapter<KeyValue, SimpleListAdapter.MainViewHolder> {

        public SimpleListAdapter(Context context, List<KeyValue> data) {
            super(context, data);
            mItemHeight = getContext().getResources().getDimensionPixelSize(R.dimen.dialog_list_item_height);
        }

        private int mItemHeight;

        @Override
        protected int getItemHeight() {
            return mItemHeight;
        }

        @Override
        public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.dialog_list_item, parent, false));
        }

        public static class MainViewHolder extends CommonAdapter.ItemViewHolder<KeyValue> {

            ImageView mIcon;
            TextView mText;

            public MainViewHolder(View v) {
                super(v);

                mIcon = (ImageView) findViewById(R.id.iv_item);
                mText = (TextView) findViewById(R.id.tv_item);
            }

            @Override
            public void bindData(Context context, KeyValue keyValue, int position) {
                mIcon.setImageResource(keyValue.getIconResId());
                mText.setText(keyValue.getText());
            }
        }
    }

    public static class KeyValue{
        private @DrawableRes int mIconResId;
        private String mText;

        public KeyValue(int mIconResId, String mText) {
            this.mIconResId = mIconResId;
            this.mText = mText;
        }

        public int getIconResId() {
            return mIconResId;
        }

        public void setIconResId(int iconResId) {
            this.mIconResId = iconResId;
        }

        public String getText() {
            return mText;
        }

        public void setText(String text) {
            this.mText = text;
        }
    }

    public static abstract class ListAdapter<T, VH extends CommonAdapter.ItemViewHolder> extends CommonAdapter<T, VH> {

        public ListAdapter(Context context, List<T> data) {
            super(context, data);
        }

        abstract protected int getItemHeight();
    }
}
