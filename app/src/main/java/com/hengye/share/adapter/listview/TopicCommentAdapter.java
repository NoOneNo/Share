package com.hengye.share.adapter.listview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.TopicAdapter;
import com.hengye.share.model.TopicComment;
import com.hengye.share.ui.support.textspan.LongClickableLinkMovementMethod;
import com.hengye.share.ui.support.textspan.TopicContentUrlOnTouchListener;
import com.hengye.share.ui.view.GridGalleryView;

import java.util.List;

public class TopicCommentAdapter extends CommonAdapter<TopicComment, TopicCommentAdapter.TopicCommentViewHolder> {

    public TopicCommentAdapter(Context context, List<TopicComment> data){
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

    public static class TopicCommentViewHolder extends ViewHolder<TopicComment>{

        TopicAdapter.TopicContentViewHolder mTopic;
        TopicAdapter.TopicTitleViewHolder mTopicTitle;

        TopicContentUrlOnTouchListener mTopicContentUrlOnTouchListener = new TopicContentUrlOnTouchListener();

        public TopicCommentViewHolder(View v) {
            super(v);
            if (mTopicTitle == null) {
                mTopicTitle = new TopicAdapter.TopicTitleViewHolder(v);
            }
            if (mTopic == null) {
                mTopic = new TopicAdapter.TopicContentViewHolder();
            }
            mTopic.mContent = (TextView) findViewById(R.id.tv_topic_content);
            mTopic.mGallery = (GridGalleryView) findViewById(R.id.gl_topic_gallery);
        }

        @Override
        public void bindData(Context context, TopicComment topicComment) {
//            registerChildViewItemClick(mTopicTitle);
            mTopicTitle.initTopicTitle(context, topicComment);
            initCommentContent(context, mTopic, topicComment);
        }

        public void initCommentContent(final Context context, final TopicAdapter.TopicContentViewHolder holder, TopicComment topicComment) {

            //不设置的话会被名字内容的点击事件覆盖，无法触发ItemView的onClick
//            registerItemClick(holder.mContent);

            holder.mContent.setText(topicComment.getUrlSpannableString(context));
            holder.mContent.setMovementMethod(LongClickableLinkMovementMethod.getInstance());
            holder.mContent.setOnTouchListener(mTopicContentUrlOnTouchListener);
        }
    }
}
