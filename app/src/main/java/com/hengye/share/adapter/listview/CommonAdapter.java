package com.hengye.share.adapter.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {

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
        return getView(mData.get(position), position, convertView, parent);
    }

    abstract public View getView(T t, int position, View convertView, ViewGroup parent);

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