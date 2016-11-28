package com.hengye.share.module.topicdetail;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.TopicComments;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.model.TopicComment;
import com.hengye.share.module.topic.TopicAdapter;
import com.hengye.share.ui.support.textspan.TopicUrlOnTouchListener;
import com.hengye.share.ui.widget.util.DrawableLoader;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.ThemeUtil;

import java.util.List;

public class TopicCommentAdapter extends CommonAdapter<TopicComment> {

    private boolean mIsLikeMode;//是否支持点赞

    public TopicCommentAdapter(Context context, List<TopicComment> data, boolean isLikeMode) {
        super(context, data);
        this.mIsLikeMode = isLikeMode;
    }

    @Override
    public int getBasicItemType(int position) {
        TopicComment topicComment = getItem(position);
        if(TopicComments.getTopicHotCommentLabel() == topicComment){
            return R.layout.item_topic_comment_hot_label;
        }else{
            return R.layout.item_topic_comment;
        }
    }

    @Override
    public ItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        if(viewType == R.layout.item_topic_comment_hot_label) {
            return new ItemViewHolder(inflate(R.layout.item_topic_comment_hot_label, parent));
        }else{
            return new TopicCommentViewHolder(inflate(R.layout.item_topic_comment, parent), mIsLikeMode);
        }
    }


    public static class TopicCommentViewHolder extends ItemViewHolder<TopicComment> {

        public TopicAdapter.TopicContentViewHolder mTopic;
        public TopicCommentTitleViewHolder mTopicTitle;
        public View mTopicTotalItem;

        public TopicCommentViewHolder(View v, boolean isLikeMode) {
            super(v);
            if (mTopicTitle == null) {
                mTopicTitle = new TopicCommentTitleViewHolder(v, isLikeMode);
                registerOnClickListener(mTopicTitle.mLikeLayout);
            }
            if (mTopic == null) {
                mTopic = new TopicAdapter.TopicContentViewHolder(findViewById(R.id.ll_topic_content));
            }

            mTopicTotalItem = findViewById(R.id.item_topic_total);

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
//            registerOnLongClickListener(mTopicTotalItem);

            SelectorLoader.getInstance().setDefaultRippleBackground(mTopicTotalItem);

            mTopicTitle.mAvatar.setOnTouchListener(mTopicOnTouchListener);
            mTopicTitle.mUsername.setOnTouchListener(mTopicOnTouchListener);
            mTopicTitle.mDescription.setOnTouchListener(mTopicOnTouchListener);
            mTopicTitle.mTitle.setOnTouchListener(mTopicOnTouchListener);
            mTopic.mContent.setOnTouchListener(mTopicOnTouchListener);
            mTopic.mGallery.setOnTouchListener(mTopicOnTouchListener);
        }

        private View.OnTouchListener mTopicOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int id = v.getId();
                if (id == R.id.tv_topic_content) {
                    boolean result = TopicUrlOnTouchListener.getInstance().onTouch(v, event);
                    if (!result) {
                        mTopicTotalItem.onTouchEvent(event);
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    mTopicTotalItem.onTouchEvent(event);
                    return false;
                }
            }
        };

        @Override
        public void bindData(Context context, TopicComment topicComment, int position) {
            mTopicTitle.initTopicTitle(context, topicComment);
            initCommentContent(context, this, topicComment);
        }

        public void initCommentContent(final Context context, final TopicCommentViewHolder holder, TopicComment topicComment) {
            holder.mTopic.mContent.setText(topicComment.getUrlSpannableString());
        }
    }

    public static class TopicCommentTitleViewHolder extends TopicAdapter.TopicTitleViewHolder {

        public View mLikeLayout;
        public ImageButton mLikeBtn;
        public TextView mLikeCounts;
        public boolean mIsLikeMode;

        public TopicCommentTitleViewHolder(View v, boolean isLikeMode) {
            super(v);
            mIsLikeMode = isLikeMode;
            mLikeLayout = v.findViewById(R.id.layout_like);
            mLikeBtn = (ImageButton) v.findViewById(R.id.btn_like);
            mLikeCounts = (TextView) v.findViewById(R.id.tv_like_counts);

            if (!isLikeMode) {
                mLikeLayout.setVisibility(View.GONE);
            }
        }

        public void initTopicTitle(final Context context, TopicComment topicComment) {
            String time = DateUtil.getEarlyDateFormat(topicComment.getDate());
            if (TextUtils.isEmpty(topicComment.getChannel())) {
                mDescription.setText(time);
            } else {
                String str = String.format(context.getString(R.string.label_time_and_from), time, Html.fromHtml(topicComment.getChannel()));
                mDescription.setText(str);
            }

            UserInfo userInfo = topicComment.getUserInfo();
            if (userInfo != null) {
                mUsername.setText(userInfo.getName());
                if (SettingHelper.isShowCommentAndRepostAvatar()) {
                    mAvatar.setImageUrl(userInfo.getAvatar());
                } else {
                    mAvatar.setImageResource(R.drawable.ic_user_avatar);
                }
            }

            if (mIsLikeMode) {
                Drawable drawable = DrawableLoader.setTintDrawable(R.drawable.ic_thumb_up_white_48dp, ThemeUtil.getTintColor(topicComment.isLiked()));
                mLikeBtn.setImageDrawable(drawable);
                mLikeCounts.setText(DataUtil.getCounter(topicComment.getLikeCounts()));
            }
        }
    }
}
