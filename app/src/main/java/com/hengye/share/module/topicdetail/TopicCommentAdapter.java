package com.hengye.share.module.topicdetail;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.CommonAdapter;
import com.hengye.share.adapter.recyclerview.ItemViewHolder;
import com.hengye.share.model.TopicComment;
import com.hengye.share.module.topic.TopicAdapter;
import com.hengye.share.ui.support.textspan.TopicUrlOnTouchListener;
import com.hengye.share.ui.widget.util.SelectorLoader;

import java.util.List;

public class TopicCommentAdapter extends CommonAdapter<TopicComment> {

    public TopicCommentAdapter(Context context, List<TopicComment> data){
        super(context, data);
    }

    @Override
    public ItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new TopicCommentViewHolder(inflate(R.layout.item_topic_total, parent));
    }

    public static class TopicCommentViewHolder extends ItemViewHolder<TopicComment> {

        public TopicAdapter.TopicContentViewHolder mTopic;
        public TopicAdapter.TopicTitleViewHolder mTopicTitle;
        public View mTopicItem;

        public TopicCommentViewHolder(View v) {
            super(v);
            if (mTopicTitle == null) {
                mTopicTitle = new TopicAdapter.TopicTitleViewHolder(v);
            }
            if (mTopic == null) {
                mTopic = new TopicAdapter.TopicContentViewHolder(findViewById(R.id.ll_topic_content));
            }
            findViewById(R.id.ll_topic_retweeted_content).setVisibility(View.GONE);

            mTopicItem = findViewById(R.id.ll_topic);
//            mTopic.mContent = (TextView) findViewById(R.id.tv_topic_content);
//            mTopic.mGallery = (GridGalleryView) findViewById(R.id.gl_topic_gallery);

            //不设置的话会被名字内容的点击事件覆盖，无法触发ItemView的onClick
            registerOnClickListener(mTopicTitle.mAvatar);
            registerOnClickListener(mTopicTitle.mUsername);
            registerOnClickListener(mTopicTitle.mDescription);
            registerOnClickListener(mTopicTitle.mTitle);

            registerOnClickListener(mTopic.mContent);
            registerOnClickListener(mTopic.mGallery);
            registerOnClickListener(mTopic.mTopicLayout);

            //不设置长按没法解决点击效果
            registerOnLongClickListener(mTopicTitle.mTitle);
            registerOnLongClickListener(mTopic.mContent);
            registerOnLongClickListener(mTopic.mGallery);
            registerOnLongClickListener(mTopic.mTopicLayout);
            registerOnLongClickListener(mTopicItem);

            SelectorLoader.getInstance().setDefaultRippleBackground(mTopicItem);

            mTopicTitle.mAvatar.setOnTouchListener(mTopicOnTouchListener);
            mTopicTitle.mUsername.setOnTouchListener(mTopicOnTouchListener);
            mTopicTitle.mDescription.setOnTouchListener(mTopicOnTouchListener);
            mTopicTitle.mTitle.setOnTouchListener(mTopicOnTouchListener);
            mTopic.mContent.setOnTouchListener(mTopicOnTouchListener);
            mTopic.mGallery.setOnTouchListener(mTopicOnTouchListener);
//            mTopic.mContent.setOnTouchListener(TopicUrlOnTouchListener.getInstance());
        }

        private View.OnTouchListener mTopicOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int id = v.getId();
                if(id == R.id.tv_topic_content) {
                    //如果需要拦截长按关键字（比如@名字）则这样返回；
//                    if(!TopicUrlOnTouchListener.getInstance().onTouch(v, event)) {
//                        mTopicItem.onTouchEvent(event);
//                        return false;
//                    }else{
//                        return true;
//                    }

                    if(!TopicUrlOnTouchListener.getInstance().onTouch(v, event)) {
                        mTopicItem.onTouchEvent(event);
                    }
                    return false;
                }else{
                    mTopicItem.onTouchEvent(event);
                    return false;
                }
            }
        };

        @Override
        public void bindData(Context context, TopicComment topicComment, int position) {
            mTopicTitle.initTopicTitle(context, topicComment);
            initCommentContent(context, mTopic, topicComment);
        }

        public void initCommentContent(final Context context, final TopicAdapter.TopicContentViewHolder holder, TopicComment topicComment) {

            holder.mContent.setText(topicComment.getUrlSpannableString());
        }
    }
}
