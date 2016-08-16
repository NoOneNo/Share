package com.hengye.share.adapter.listview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hengye.share.ui.widget.listener.OnItemClickListener;
import com.hengye.share.ui.widget.listener.OnItemLongClickListener;

import java.util.List;

public abstract class CommonAdapter<T, V extends ViewHolder<T>> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mData;
    private OnItemClickListener mOnChildViewItemClickListener;
    private OnItemLongClickListener mOnChildViewItemLongClickListener;

    public CommonAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        V viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(getItemLayoutResId(), parent, false);
            viewHolder = getViewHolder(convertView);
            viewHolder.setOnChildViewItemClickListener(getOnChildViewItemClickListener());
            viewHolder.setOnChildViewItemLongClickListener(getOnChildViewItemLongClickListener());
        } else {
            viewHolder = (V) convertView.getTag();
        }

        viewHolder.bindItemPosition(position);
        viewHolder.bindData(getContext(), mData.get(position), position);
        return convertView;

    }

    abstract public V getViewHolder(View convertView);

    abstract public @LayoutRes int getItemLayoutResId();

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        if (0 <= position && position < mData.size()) {
            return mData.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public boolean isEmpty() {
        return mData.size() == 0;
    }


    public int getItemPosition(T item) {
        return mData.indexOf(item);
    }

    public T getLastItem() {
        if (mData.isEmpty()) {
            return null;
        }
        return mData.get(mData.size() - 1);
    }

    public int getLastItemPosition() {
        return mData.size() - 1;
    }

    public void addItem(int position, T item) {
        mData.add(position, item);
        notifyDataSetChanged();
    }

    public void addItem(T item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void updateItem(int position, T item){
        mData.set(position, item);
        notifyDataSetChanged();
    }

    public T removeItem(int position) {
        T t = mData.remove(position);
        notifyDataSetChanged();
        return t;
    }

    public T removeItem(T item) {
        int index = mData.indexOf(item);
        if (index != -1) {
            return removeItem(index);
        }
        return null;
    }

    public void addAll(List<T> data) {
        mData.addAll(data);
        notifyDataSetChanged();
    }

    public void addAll(int position, List<T> data) {
        mData.addAll(position, data);
        notifyDataSetChanged();
    }


    public void refresh(List<T> data) {
        if (data == null) {
            this.mData.clear();
        } else {
            this.mData = data;
        }
        notifyDataSetChanged();
    }

    public List<T> getData() {
        return mData;
    }

    public void setData(List<T> data) {
        mData = data;
    }

    public Context getContext() {
        return mContext;
    }

    public OnItemClickListener getOnChildViewItemClickListener() {
        return mOnChildViewItemClickListener;
    }

    public void setOnChildViewItemClickListener(OnItemClickListener onChildViewItemClickListener) {
        this.mOnChildViewItemClickListener = onChildViewItemClickListener;
    }

    public OnItemLongClickListener getOnChildViewItemLongClickListener() {
        return mOnChildViewItemLongClickListener;
    }

    public void setmOnChildViewItemLongClickListener(OnItemLongClickListener onChildViewItemLongClickListener) {
        this.mOnChildViewItemLongClickListener = onChildViewItemLongClickListener;
    }
}