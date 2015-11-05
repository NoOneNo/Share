package com.hengye.share.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public class RecyclerViewSimpleAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{

    protected Context mContext;
    protected List<T> mDatas;

    public RecyclerViewSimpleAdapter(Context context, List<T> datas){
        mContext = context;
        mDatas = datas;
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
        return mDatas.size();
    }

    public void add(int position, T data){
        if(mDatas != null){
            mDatas.add(position, data);
            notifyItemInserted(position);
        }
    }

    public void remove(int position){
        if(mDatas != null){
            mDatas.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void refresh(List<T> datas){
        if(datas == null){
            mDatas.clear();
        }else{
            mDatas = datas;
        }
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mDatas;
    }

    public void setDatas(List<T> datas) {
        refresh(datas);
    }
}
