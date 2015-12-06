package com.hengye.share.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.util.ViewUtil;

import java.util.List;

public class RecyclerViewSimpleAdapter<T, VH extends RecyclerViewSimpleAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {

    private Context mContext;
    private List<T> mData;
    private ViewUtil.OnItemClickListener mOnItemClickListener;

    public RecyclerViewSimpleAdapter(Context context, List<T> data) {
        mContext = context;
        mData = data;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener, ViewUtil.OnItemClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public ViewHolder(View itemView, ViewUtil.OnItemClickListener onItemClickListener) {
            super(itemView);
            mOnItemClickListener = onItemClickListener;
            itemView.setOnClickListener(mOnClickForItemListener);
        }

        public void bindData(Context context, T t){

        }

        ViewUtil.OnItemClickListener mOnItemClickListener, mOnChildViewClickListener;

        @Override
        public void onClick(View v) {
            onItemClick(v, getAdapterPosition());
        }

        /**
         * 用于实现Item的子view的OnClick, 子类重载此方法必须调用父类的onItemClick;
         */
        @Override
        public void onItemClick(View view, int position) {
            if (mOnChildViewClickListener != null) {
                mOnChildViewClickListener.onItemClick(view, getAdapterPosition());
            }
        }

        /**
         * 用于提供给外部调用的ItemClick;
         */
        View.OnClickListener mOnClickForItemListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(v, getAdapterPosition());
                }
            }
        };

        public ViewUtil.OnItemClickListener getOnItemClickListener() {
            return mOnItemClickListener;
        }

        public void setOnItemClickListener(ViewUtil.OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public View.OnClickListener getOnClickForItemListener() {
            return mOnClickForItemListener;
        }

        public ViewUtil.OnItemClickListener getOnChildViewClickListener() {
            return mOnChildViewClickListener;
        }

        public void setOnChildViewClickListener(ViewUtil.OnItemClickListener onChildViewClickListener) {
            this.mOnChildViewClickListener = onChildViewClickListener;
        }
    }

    public T getItem(int position){
        return mData.get(position);
    }

    public void add(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(position);
    }

    public void add(T item) {
        mData.add(item);
        notifyItemInserted(mData.size() - 1);
    }

    public void remove(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void refresh(List<T> data) {
        if (data == null) {
            mData.clear();
        } else {
            mData = data;
        }
        notifyDataSetChanged();
    }

    public void addAll(List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addAll(int position, List<T> data) {
        mData.addAll(position, data);
        notifyDataSetChanged();
    }

    public ViewUtil.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public void setOnItemClickListener(ViewUtil.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        refresh(data);
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
}
