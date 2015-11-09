package com.hengye.share.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.view.NetworkImageView;
import com.hengye.share.R;
import com.hengye.share.module.Topic;
import com.hengye.share.util.DateUtil;
import com.hengye.volleyplus.toolbox.RequestManager;

import java.util.List;

public class RecyclerViewTopicAdapter extends RecyclerViewSimpleAdapter<Topic, RecyclerViewTopicAdapter.MainViewHolder>{


    public RecyclerViewTopicAdapter(Context context, List<Topic> datas){
        super(context, datas);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recylerview_topic, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        Topic topic = mDatas.get(position);
        holder.mUsername.setText(topic.getUsername());
        String time = DateUtil.getLatestDateFormat(topic.getDate());
        if(TextUtils.isEmpty(topic.getChannel())){
            holder.mDescription.setText(time);
        }else{
            holder.mDescription.setText(time + " 来自 " + Html.fromHtml(topic.getChannel()));
        }
        holder.mContent.setText(topic.getContent());
        holder.mAvator.setImageUrl(topic.getAvator(), RequestManager.getImageLoader());
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }


    class MainViewHolder extends RecyclerViewSimpleAdapter.ViewHolder{

        NetworkImageView mAvator;
        TextView mUsername, mDescription, mContent;
        public MainViewHolder(View v){
            super(v);
            mAvator = (NetworkImageView) v.findViewById(R.id.iv_topic_avator);
            mUsername = (TextView) v.findViewById(R.id.tv_topic_username);
            mDescription = (TextView) v.findViewById(R.id.tv_topic_description);
            mContent = (TextView) v.findViewById(R.id.tv_topic_content);
        }
    }
}




































