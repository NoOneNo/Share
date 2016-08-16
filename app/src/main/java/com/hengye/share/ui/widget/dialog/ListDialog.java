package com.hengye.share.ui.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.CommonAdapter;
import com.hengye.share.ui.widget.listener.OnItemClickListener;
import com.hengye.share.ui.widget.util.SelectorLoader;

import java.util.List;

public class ListDialog extends Dialog {

    public ListDialog(Context context, List<KeyValue> keyValues) {
        this(context, new SimpleListAdapter(context, keyValues));
    }

    public ListDialog(Context context, ListAdapter listAdapter) {
        this(context, listAdapter, 0 , 0);
    }

    public ListDialog(Context context, ListAdapter listAdapter, int width, int maxHeight) {
        super(context, R.style.ListDialog);

        mAdapter = listAdapter;

        if(mWidth == 0 && maxHeight == 0){
            initDefaultSize();
        }else{
            mWidth = width;
            mMaxHeight = maxHeight;
        }

        initView();
    }

    private ListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private FrameLayout mRootLayout;
    private View mShade;
    private LinearLayoutManager mLayoutManager;

    private int mWidth, mMaxHeight;

    private void initDefaultSize() {
        mWidth = getContext().getResources().getDimensionPixelSize(R.dimen.dialog_list_default_width);
        mMaxHeight = getContext().getResources().getDimensionPixelSize(R.dimen.dialog_list_default_max_height);
    }

    private void initView() {
        setContentView(R.layout.dialog_list);

        mShade = findViewById(R.id.shade);

        mRootLayout = (FrameLayout) findViewById(R.id.fl_root);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(mLayoutManager = new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (mAdapter.getLastItemPosition() == mLayoutManager.findLastCompletelyVisibleItemPosition()) {
                    mShade.setVisibility(View.GONE);
                } else {
                    mShade.setVisibility(View.VISIBLE);
                }
            }
        });

        adjustDialogSize();

        this.setCanceledOnTouchOutside(true);
    }

    public void adjustDialogSize() {
        int width, height;

        width = getWidth();
        height = mAdapter.getItemHeight() * mAdapter.getItemCount();
        if (height > getMaxHeight()) {
            height = getMaxHeight();
        }else{
            mShade.setVisibility(View.GONE);
        }

        ViewGroup.LayoutParams lp = mRootLayout.getLayoutParams();
        lp.width = width;
        lp.height = height;
        mRootLayout.setLayoutParams(lp);
    }

    public void setOverScrollMode(int overScrollMode) {
        mRecyclerView.setOverScrollMode(overScrollMode);
    }

    public void setBackgroundColor(@ColorRes int colorResId) {
        mAdapter.setBackground(colorResId);
//        mRecyclerView.setBackgroundColor(getContext().getResources().getColor(colorResId));
    }

    public void setOnItemListener(OnItemClickListener onItemListener) {
        if(mAdapter != null){
            mAdapter.setOnItemClickListener(onItemListener);
        }
    }

    public ListAdapter getAdapter() {
        return mAdapter;
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

        public void setItemHeight(int itemHeight) {
            this.mItemHeight = itemHeight;
        }

        @Override
        public MainViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_list_item, parent, false);

            if(getBackground() != 0) {
                SelectorLoader
                        .getInstance()
                        .setRippleBackground(view, R.color.grey_300, R.color.grey_300, getBackground());
            }
            return new MainViewHolder(view);
        }

        @Override
        public void onBindBasicItemView(MainViewHolder holder, int position) {
            super.onBindBasicItemView(holder, position);

            if(getTextColor() != 0){
                holder.mText.setTextColor(getTextColor());
            }

            holder.mDivider.setVisibility(position == getLastItemPosition() ? View.INVISIBLE : View.VISIBLE);
        }

        public static class MainViewHolder extends CommonAdapter.ItemViewHolder<KeyValue> {

            ImageView mIcon;
            TextView mText;
            View mDivider;

            public MainViewHolder(View v) {
                super(v);

                mIcon = (ImageView) findViewById(R.id.iv_item);
                mText = (TextView) findViewById(R.id.tv_item);
                mDivider = findViewById(R.id.divider_line);
            }

            @Override
            public void bindData(Context context, KeyValue keyValue, int position) {
                mIcon.setImageResource(keyValue.getIconResId());
                mText.setText(keyValue.getText());

            }
        }
    }

    public static class KeyValue {
        private
        @DrawableRes
        int mIconResId;
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

        private int mTextColor, mBackground;

        public int getTextColor() {
            return mTextColor;
        }

        public void setTextColor(int textColor) {
            this.mTextColor = textColor;
        }

        public int getBackground() {
            return mBackground;
        }

        public void setBackground(@ColorRes int background) {
            this.mBackground = background;
        }
    }
}
