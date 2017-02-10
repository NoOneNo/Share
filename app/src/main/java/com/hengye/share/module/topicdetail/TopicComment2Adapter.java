package com.hengye.share.module.topicdetail;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.module.topic.TopicAdapter;
import com.hengye.share.module.util.encapsulation.view.listview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.listview.ViewHolder;
import com.hengye.share.model.TopicComment;
import com.hengye.share.ui.support.textspan.TopicUrlOnTouchListener;
import com.hengye.share.ui.widget.util.SelectorLoader;

import java.util.List;

public class TopicComment2Adapter extends CommonAdapter<TopicComment, TopicComment2Adapter.TopicCommentViewHolder> {

    public TopicComment2Adapter(Context context, List<TopicComment> data){
        super(context, data);
    }

    @Override
    public int getItemLayoutResId() {
        return R.layout.item_topic_total;
    }

    @Override
    public TopicCommentViewHolder getViewHolder(View convertView) {
        return new TopicCommentViewHolder(convertView);
    }

    public static class TopicCommentViewHolder extends ViewHolder<TopicComment> {

        public TopicAdapter.TopicContentViewHolder mTopic;
        public TopicCommentAdapter.TopicCommentTitleViewHolder mTopicTitle;
        public View mTopicItem;

        public TopicCommentViewHolder(View v) {
            super(v);
            if (mTopicTitle == null) {
                mTopicTitle = new TopicCommentAdapter.TopicCommentTitleViewHolder(v, false);
            }
            if (mTopic == null) {
                mTopic = new TopicAdapter.TopicContentViewHolder(findViewById(R.id.ll_topic_content));
            }
            findViewById(R.id.item_topic_retweeted_content).setVisibility(View.GONE);

            mTopicItem = findViewById(R.id.item_topic);
//            mTopic.mContent = (TextView) findViewById(R.id.tv_topic_content);
//            mTopic.mGallery = (GridGalleryView) findViewById(R.id.gl_topic_gallery);

            //不设置的话会被名字内容的点击事件覆盖，无法触发ItemView的onClick
            mTopic.mContent.setTag(v);
            mTopic.mGallery.setTag(v);
            registerChildViewItemClick(mTopicTitle.mAvatar);
            registerChildViewItemClick(mTopicTitle.mUsername);
            registerChildViewItemClick(mTopicTitle.mDescription);

//            registerOnClickListener(mTopic.mGallery);

            SelectorLoader.getInstance().setDefaultRippleBackground(mTopicItem);

            mTopicTitle.mAvatar.setOnTouchListener(mTopicOnTouchListener);
            mTopicTitle.mUsername.setOnTouchListener(mTopicOnTouchListener);
            mTopicTitle.mDescription.setOnTouchListener(mTopicOnTouchListener);
//            mTopicTitle.mTitle.setOnTouchListener(mTopicOnTouchListener);
            mTopic.mContent.setOnTouchListener(mTopicOnTouchListener);
//            mTopic.mGallery.setOnTouchListener(mTopicOnTouchListener);
//            mTopic.mContent.setOnTouchListener(TopicUrlOnTouchListener.getInstance());
        }

        private View.OnTouchListener mTopicOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int id = v.getId();
                if(id == R.id.tv_topic_content) {
                    if(!TopicUrlOnTouchListener.getInstance().onTouch(v, event)) {
                        return mTopicItem.onTouchEvent(event);
                    }else{
                        return true;
                    }

                }else{
                    return mTopicItem.onTouchEvent(event);
                }
            }
        };

        @Override
        public void bindData(Context context, TopicComment topicComment, int position) {
            mTopicTitle.initTopicTitle(context, topicComment);
            initCommentContent(context, mTopic, topicComment);
        }

        public void initCommentContent(final Context context, final TopicAdapter.TopicContentViewHolder holder, TopicComment topicComment) {

            holder.mContent.setText(topicComment.getSpanned(holder.mContent));
        }
    }
}
