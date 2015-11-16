package com.hengye.share.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.view.NetworkImageView;
import com.hengye.share.LoginActivity;
import com.hengye.share.R;
import com.hengye.share.TopicGalleryActivity;
import com.hengye.share.module.Topic;
import com.hengye.share.module.sina.WBUtil;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.SimpleClickableSpan;
import com.hengye.share.util.SimpleLinkMovementMethod;
import com.hengye.share.util.ViewUtil;
import com.hengye.volleyplus.toolbox.RequestManager;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerViewTopicAdapter extends RecyclerViewSimpleAdapter<Topic, RecyclerViewTopicAdapter.MainViewHolder> implements View.OnClickListener{

    private int mScreenWidth;
    private int mImageViewMaxWidth;
    private int mImageViewMargin;

    public RecyclerViewTopicAdapter(Context context, List<Topic> datas) {
        super(context, datas);

        mScreenWidth = context.getResources().getDisplayMetrics().widthPixels;
        int marginHorizontal = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) * 2;
        mImageViewMaxWidth = mScreenWidth - marginHorizontal;
        mImageViewMargin = context.getResources().getDimensionPixelSize(R.dimen.topic_gallery_iv_margin);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_topic, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        Topic topic = getItem(position);
        holder.mUsername.setText(topic.getUsername());
        String time = DateUtil.getLatestDateFormat(topic.getDate());
        if (TextUtils.isEmpty(topic.getChannel())) {
            holder.mDescription.setText(time);
        } else {
            holder.mDescription.setText(time + " 来自 " + Html.fromHtml(topic.getChannel()));
        }

        holder.mAvator.setImageUrl(topic.getAvator(), RequestManager.getImageLoader());

        initTopicContent(holder.mTopic, topic, false, holder.itemView);
        if (topic.getRetweetedTopic() != null) {
            holder.mRetweetTopicLayout.setVisibility(View.VISIBLE);
            initTopicContent(holder.mRetweetTopic, topic.getRetweetedTopic(), true, holder.itemView);
        } else {
            holder.mRetweetTopicLayout.setVisibility(View.GONE);
        }
    }


    private void initTopicContent(TopicContentViewHolder holder, Topic topic, boolean isRetweeted, View itemView) {

        holder.mContent.setTag(itemView);
        holder.mContent.setOnClickListener(this);
        String str;
        if (isRetweeted && !TextUtils.isEmpty(topic.getUsername())) {
            //如果微博已经被删除，则没有名字
            str = "@" + topic.getUsername() + ":" + topic.getContent();
        } else {
            str = topic.getContent();
        }

        Map<Integer, String> atNames = WBUtil.getMatchAtWBName(str);
        if(!CommonUtil.isEmptyMap(atNames)){
            SpannableString ss = new SpannableString(str);
            for(Map.Entry<Integer, String> entry : atNames.entrySet()){
                final int startIndex = entry.getKey();
                final String atName = entry.getValue();
                SimpleClickableSpan scs = new SimpleClickableSpan();
                scs.setNormalColor(mContext.getResources().getColor(R.color.topic_name_at)).
                    setSelectedColor(mContext.getResources().getColor(R.color.topic_username)).
                    setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            Intent intent = new Intent(mContext, LoginActivity.class);
//                            IntentUtil.startActivityIfTokenValid(mContext, intent);
                            Toast.makeText(mContext, atName.substring(0, atName.length() - 1), Toast.LENGTH_SHORT).show();
                        }
                    });
                ss.setSpan(scs, startIndex, startIndex + atName.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
            holder.mContent.setText(ss);
            holder.mContent.setMovementMethod(SimpleLinkMovementMethod.getInstance());
        }else{
            holder.mContent.setText(str);
        }

        holder.mContent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                TextView tv = (TextView) v;
                switch (action){
                    case MotionEvent.ACTION_MOVE:
                        Selection.removeSelection(SpannableString.valueOf(tv.getText()));
                }
                return false;
            }
        });

        if (!CommonUtil.isEmptyList(topic.getImageUrls())) {

            holder.mGallery.removeAllViews();
            holder.mGallery.setColumnCount(ViewUtil.getTopicImageColumnCount(topic.getImageUrls().size()));
            for (int i = 0; i < topic.getImageUrls().size(); i++) {
                String url = topic.getImageUrls().get(i);
                NetworkImageView iv = new NetworkImageView(mContext);
                ViewUtil.setTopicImageViewLayoutParams(iv, mImageViewMaxWidth, mImageViewMargin, topic.getImageUrls().size());
//                String key = ViewUtil.getCacheKey(url, is.width, is.height, ImageView.ScaleType.CENTER_CROP);
                iv.setImageUrl(url, RequestManager.getImageLoader());
                iv.setTag(R.id.gl_topic_gallery, topic.getImageUrls());
//                iv.setTag(R.id.gl_topic_retweeted_gallery, key);
                iv.setTag(i);
                iv.setId(View.NO_ID);
                iv.setOnClickListener(this);
                holder.mGallery.addView(iv);
            }

            holder.mGallery.setVisibility(View.VISIBLE);
        } else {
            holder.mGallery.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.tv_topic_content || id == R.id.tv_topic_retweeted_content){
            View itemView = (View) v.getTag();
            if(itemView != null){
                itemView.performClick();
            }
        } else if(id == View.NO_ID){
            Intent intent = new Intent(mContext, TopicGalleryActivity.class);
            ArrayList<String> paths = (ArrayList<String>)v.getTag(R.id.gl_topic_gallery);
            intent.putExtra(TopicGalleryActivity.IMG_PATHS, paths);
            intent.putExtra(TopicGalleryActivity.IMG_INDEX, (int)v.getTag());
            mContext.startActivity(intent);
        }
    }

    class MainViewHolder extends RecyclerViewSimpleAdapter.ViewHolder {

        NetworkImageView mAvator;
        TextView mUsername, mDescription;
        TopicContentViewHolder mTopic, mRetweetTopic;
        View mRetweetTopicLayout;

        public MainViewHolder(View v) {
            super(v);
            if (mTopic == null) {
                mTopic = new TopicContentViewHolder();
            }
            if (mRetweetTopic == null) {
                mRetweetTopic = new TopicContentViewHolder();
            }
            mAvator = (NetworkImageView) v.findViewById(R.id.iv_topic_avator);
            mUsername = (TextView) v.findViewById(R.id.tv_topic_username);
            mDescription = (TextView) v.findViewById(R.id.tv_topic_description);
            mTopic.mContent = (TextView) v.findViewById(R.id.tv_topic_content);
            mTopic.mGallery = (GridLayout) v.findViewById(R.id.gl_topic_gallery);
            mRetweetTopicLayout = v.findViewById(R.id.ll_topic_retweeted);
            mRetweetTopic.mContent = (TextView) v.findViewById(R.id.tv_topic_retweeted_content);
            mRetweetTopic.mGallery = (GridLayout) v.findViewById(R.id.gl_topic_retweeted_gallery);
        }
    }

    static class TopicContentViewHolder {
        TextView mContent;
        GridLayout mGallery;
    }

}




































