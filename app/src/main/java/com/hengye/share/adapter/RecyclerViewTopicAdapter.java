package com.hengye.share.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.view.NetworkImageView;
import com.hengye.share.R;
import com.hengye.share.module.Topic;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.L;
import com.hengye.volleyplus.toolbox.RequestManager;

import java.util.List;

public class RecyclerViewTopicAdapter extends RecyclerViewSimpleAdapter<Topic, RecyclerViewTopicAdapter.MainViewHolder>{

    private int mScreenWidth;
    private int mImageViewMaxWidth;
    public RecyclerViewTopicAdapter(Context context, List<Topic> datas){
        super(context, datas);

        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int marginHorizontal = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) * 2;
        mImageViewMaxWidth = mScreenWidth - marginHorizontal;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recylerview_topic, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        Topic topic = getItem(position);
        holder.mUsername.setText(topic.getUsername());
        String time = DateUtil.getLatestDateFormat(topic.getDate());
        if(TextUtils.isEmpty(topic.getChannel())){
            holder.mDescription.setText(time);
        }else{
            holder.mDescription.setText(time + " 来自 " + Html.fromHtml(topic.getChannel()));
        }
        holder.mContent.setText(topic.getContent());
        holder.mAvator.setImageUrl(topic.getAvator(), RequestManager.getImageLoader());

        if(!CommonUtil.isEmptyList(topic.getImageUrls())){
            L.debug("find topic urls, position : {}, first url : {}", position, topic.getImageUrls().get(0));

            int imageWidth = getImageWidth(topic.getImageUrls().size());

            holder.mGallery.removeAllViews();
            for(String url : topic.getImageUrls()){
                NetworkImageView iv = new NetworkImageView(mContext);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(imageWidth, imageWidth);
                iv.setLayoutParams(lp);
                iv.setImageUrl(url, RequestManager.getImageLoader());
                holder.mGallery.addView(iv);
            }

            holder.mGallery.setVisibility(View.VISIBLE);
        }else{
            holder.mGallery.setVisibility(View.GONE);
        }

    }

    class MainViewHolder extends RecyclerViewSimpleAdapter.ViewHolder{

        NetworkImageView mAvator, mTest;
        TextView mUsername, mDescription, mContent;
        GridLayout mGallery;

        public MainViewHolder(View v){
            super(v);
            mAvator = (NetworkImageView) v.findViewById(R.id.iv_topic_avator);
            mUsername = (TextView) v.findViewById(R.id.tv_topic_username);
            mDescription = (TextView) v.findViewById(R.id.tv_topic_description);
            mContent = (TextView) v.findViewById(R.id.tv_topic_content);
            mGallery = (GridLayout) v.findViewById(R.id.gl_gallery);
//            mTest = (NetworkImageView) v.findViewById(R.id.iv_gallery_item);
        }
    }

    private int getImageWidth(int size){
        if(size == 1){
            return ViewGroup.LayoutParams.WRAP_CONTENT;
        }else if(size == 2 || size == 4){
            return mImageViewMaxWidth / 2;
        }else{
            return mImageViewMaxWidth / 3;
        }
    }
}




































