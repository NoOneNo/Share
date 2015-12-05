package com.hengye.share.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.util.ViewUtil;

import java.util.List;

public class RecyclerViewSimpleAdapter<T, VH extends RecyclerViewSimpleAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {

    protected Context mContext;
    protected List<T> mData;
    protected ViewUtil.OnItemClickListener mOnItemClickListener;

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
            itemView.setOnClickListener(this);
        }

        public void bindData(Context context, T t){

        }

        ViewUtil.OnItemClickListener mOnItemClickListener;

        @Override
        public void onClick(View v) {
            onItemClick(v, getAdapterPosition());
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public void onItemClick(View view, int position) {

        }
    }

    public T getItem(int position){
        return mData.get(position);
    }

    public void add(int position, T item) {
        mData.add(position, item);
        notifyItemInserted(position);
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

}
