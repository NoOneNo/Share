package com.hengye.share.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.module.Topic;

import java.util.List;

public class RecyclerViewMainAdapter extends RecyclerViewSimpleAdapter<Topic, RecyclerViewMainAdapter.MainViewHolder>{


    public RecyclerViewMainAdapter(Context context, List<Topic> datas){
        super(context, datas);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recylerview_main, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        Topic topic = mDatas.get(position);
        holder.mUsername.setText(topic.getUsername());
        holder.mDescription.setText("来自: " + topic.getChannel());
        holder.mContent.setText(topic.getContent());

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class MainViewHolder extends RecyclerView.ViewHolder{

        ImageView mPortrait;
        TextView mUsername, mDescription, mContent;
        public MainViewHolder(View v){
            super(v);
            mPortrait = (ImageView) v.findViewById(R.id.iv_topic_portrait);
            mUsername = (TextView) v.findViewById(R.id.tv_topic_username);
            mDescription = (TextView) v.findViewById(R.id.tv_topic_description);
            mContent = (TextView) v.findViewById(R.id.tv_topic_content);
        }
    }
}




































