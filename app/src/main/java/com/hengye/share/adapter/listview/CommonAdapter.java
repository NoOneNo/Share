package com.hengye.share.adapter.listview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.hengye.share.R;

import java.util.List;

public abstract class CommonAdapter<T, V extends ViewHolder<T>> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mData;

    public CommonAdapter(Context context, List<T> data){
        this.mContext = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        V viewHolder;
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(getItemLayoutResId(), parent, false);
            viewHolder = getViewHolder(convertView);
        }else{
            viewHolder = (V) convertView.getTag();
        }
        viewHolder.bindData(getContext(), mData.get(position));
        return convertView;

    }

    abstract public V getViewHolder(View convertView);

    abstract public @LayoutRes int getItemLayoutResId();

    public void refresh(List<T> data){
        if(data == null){
            this.mData.clear();
        }else{
            this.mData = data;
        }
        notifyDataSetChanged();
    }

    public List<T> getData(){
        return mData;
    }

    public void setData(List<T> data){
        mData = data;
    }

    public Context getContext(){
        return mContext;
    }
}