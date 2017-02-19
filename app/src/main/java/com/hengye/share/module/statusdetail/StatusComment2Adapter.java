package com.hengye.share.module.statusdetail;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.module.status.StatusAdapter;
import com.hengye.share.module.util.encapsulation.view.listview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.listview.ViewHolder;
import com.hengye.share.model.StatusComment;
import com.hengye.share.ui.support.textspan.StatusUrlOnTouchListener;
import com.hengye.share.ui.widget.util.SelectorLoader;

import java.util.List;

public class StatusComment2Adapter extends CommonAdapter<StatusComment, StatusComment2Adapter.StatusCommentViewHolder> {

    public StatusComment2Adapter(Context context, List<StatusComment> data){
        super(context, data);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_status_comment;
    }

    @Override
    public StatusCommentViewHolder getViewHolder(View convertView) {
        return new StatusCommentViewHolder(convertView);
    }

    public static class StatusCommentViewHolder extends ViewHolder<StatusComment> {

        public StatusAdapter.StatusContentViewHolder mStatus;
        public StatusCommentAdapter.StatusCommentTitleViewHolder mStatusTitle;
        public View mStatusItem;

        public StatusCommentViewHolder(View v) {
            super(v);
            if (mStatusTitle == null) {
                mStatusTitle = new StatusCommentAdapter.StatusCommentTitleViewHolder(v, false);
            }
            if (mStatus == null) {
                mStatus = new StatusAdapter.StatusContentViewHolder(findViewById(R.id.ll_status_content));
            }
            findViewById(R.id.item_status_retweeted_content).setVisibility(View.GONE);

            mStatusItem = findViewById(R.id.item_status);
//            mStatus.mContent = (TextView) findViewById(R.id.tv_topic_content);
//            mStatus.mGallery = (GridGalleryView) findViewById(R.id.gl_topic_gallery);

            //不设置的话会被名字内容的点击事件覆盖，无法触发ItemView的onClick
            mStatus.mContent.setTag(v);
            mStatus.mGallery.setTag(v);
            registerChildViewItemClick(mStatusTitle.mAvatar);
            registerChildViewItemClick(mStatusTitle.mUsername);
            registerChildViewItemClick(mStatusTitle.mDescription);

//            registerOnClickListener(mStatus.mGallery);

            SelectorLoader.getInstance().setDefaultRippleBackground(mStatusItem);

            mStatusTitle.mAvatar.setOnTouchListener(mStatusOnTouchListener);
            mStatusTitle.mUsername.setOnTouchListener(mStatusOnTouchListener);
            mStatusTitle.mDescription.setOnTouchListener(mStatusOnTouchListener);
//            mStatusTitle.mTitle.setOnTouchListener(mStatusOnTouchListener);
            mStatus.mContent.setOnTouchListener(mStatusOnTouchListener);
//            mStatus.mGallery.setOnTouchListener(mStatusOnTouchListener);
//            mStatus.mContent.setOnTouchListener(TopicUrlOnTouchListener.getInstance());
        }

        private View.OnTouchListener mStatusOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int id = v.getId();
                if(id == R.id.tv_status_content) {
                    if(!StatusUrlOnTouchListener.getInstance().onTouch(v, event)) {
                        return mStatusItem.onTouchEvent(event);
                    }else{
                        return true;
                    }

                }else{
                    return mStatusItem.onTouchEvent(event);
                }
            }
        };

        @Override
        public void bindData(Context context, StatusComment statusComment, int position) {
            mStatusTitle.initStatusTitle(context, statusComment);
            initCommentContent(context, mStatus, statusComment);
        }

        public void initCommentContent(final Context context, final StatusAdapter.StatusContentViewHolder holder, StatusComment topicComment) {

            holder.mContent.setText(topicComment.getSpanned(holder.mContent));
        }
    }
}
