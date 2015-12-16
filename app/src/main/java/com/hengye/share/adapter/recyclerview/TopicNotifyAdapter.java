package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.R;
import com.hengye.share.module.Topic;
import com.hengye.share.module.TopicComment;
import com.hengye.share.module.sina.WBUtil;
import com.hengye.share.ui.activity.TopicDetail2Activity;
import com.hengye.share.ui.view.GridGalleryView;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.SimpleClickableSpan;
import com.hengye.share.util.SimpleLinkMovementMethod;
import com.hengye.volleyplus.toolbox.RequestManager;

import java.util.List;
import java.util.Map;

public class TopicNotifyAdapter extends CommonAdapter<Topic, TopicAdapter.TopicViewHolder> {


    public TopicNotifyAdapter(Context context, List<Topic> data) {
        super(context, data);
    }

    @Override
    public boolean isAddHeaderView() {
        return true;
    }

    @Override
    public boolean isAddFooterView() {
        return true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return new HeaderViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.header_topic_notify, parent, false));
    }

    @Override
    public void onBindHeaderView(RecyclerView.ViewHolder holder, int position) {
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View v) {
            super(v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return new FooterViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.footer_null, parent, false));
    }

    @Override
    public void onBindFooterView(RecyclerView.ViewHolder holder, int position) {
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder{
        public FooterViewHolder(View v){
            super(v);
        }
    }

    @Override
    public TopicAdapter.TopicViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new TopicAdapter.TopicViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_topic_notify, parent, false));
    }

    @Override
    public void onBindBasicItemView(TopicAdapter.TopicViewHolder holder, int position) {
        super.onBindBasicItemView(holder, position);
    }

}
