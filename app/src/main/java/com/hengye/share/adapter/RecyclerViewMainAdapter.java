package com.hengye.share.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hengye.share.R;

import java.util.ArrayList;

public class RecyclerViewMainAdapter extends RecyclerView.Adapter<RecyclerViewMainAdapter.MainViewHolder>{

    private Context mContext;
    private ArrayList<String> mDatas;

    public RecyclerViewMainAdapter(Context context, ArrayList<String> datas){
        mContext = context;
        mDatas = datas;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recylerview_main, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        holder.tv.setText(mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public void addData(int position, String data){
        if(mDatas != null){
            mDatas.add(position, data);
            notifyItemInserted(position);
        }
    }

    public void removeData(int position){
        if(mDatas != null){
            mDatas.remove(position);
            notifyItemRemoved(position);
        }
    }

    class MainViewHolder extends RecyclerView.ViewHolder{
        TextView tv;
        public MainViewHolder(View view){
            super(view);
            tv = (TextView) view.findViewById(R.id.item_recylerview_main_tv);
        }
    }
}
