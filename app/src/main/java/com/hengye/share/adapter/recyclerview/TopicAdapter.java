package com.hengye.share.adapter.recyclerview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.R;
import com.hengye.share.module.Topic;
import com.hengye.share.module.TopicComment;
import com.hengye.share.module.TopicDraft;
import com.hengye.share.module.UserInfo;
import com.hengye.share.ui.activity.PersonalHomepageActivity;
import com.hengye.share.ui.activity.TopicDetailActivity;
import com.hengye.share.ui.activity.TopicGalleryActivity;
import com.hengye.share.ui.activity.TopicPublishActivity;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.ui.support.LongClickableLinkMovementMethod;
import com.hengye.share.ui.support.TopicContentUrlOnTouchListener;
import com.hengye.share.ui.view.GridGalleryView;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends CommonAdapter<Topic, TopicAdapter.TopicViewHolder>
    implements ViewUtil.OnItemClickListener, ViewUtil.OnItemLongClickListener, DialogInterface.OnClickListener{

    public static int mGalleryMaxWidth;
    private Dialog mLongClickDialog;
    private int mLongClickPosition;
    public TopicAdapter(Context context, List<Topic> data) {
        super(context, data);
        int galleryMargin = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        mGalleryMaxWidth = context.getResources().getDisplayMetrics().widthPixels - 2 * galleryMargin;

        mLongClickDialog = DialogBuilder.getOnLongClickTopicDialog(getContext(), this);
        setOnChildViewItemClickListener(this);
        setOnItemLongClickListener(this);
        setOnChildViewItemLongClickListener(this);
    }

    @Override
    public TopicViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new TopicViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_topic, parent, false));
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        Topic topic = getItem(mLongClickPosition);
        if(topic == null){
            return;
        }
        switch (which){
            case DialogBuilder.LONG_CLICK_TOPIC_REPOST:
                IntentUtil.startActivity(getContext(),
                        TopicPublishActivity.getIntentToStart(getContext(), TopicDraft.getTopicDraftByTopicRepost(topic.getId())));
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_COMMENT:
                IntentUtil.startActivity(getContext(),
                        TopicPublishActivity.getIntentToStart(getContext(), TopicDraft.getTopicDraftByTopicComment(topic.getId())));
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_COLLECT:
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_REPOST_ORIGIN:
                Topic retweet = topic.getRetweetedTopic();
                if(retweet == null){
                    break;
                }
                IntentUtil.startActivity(getContext(),
                        TopicPublishActivity.getIntentToStart(getContext(), TopicDraft.getTopicDraftByTopicRepost(retweet.getId())));
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_COPY:
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        int id = view.getId();
        if(id == R.id.rl_topic_title){
            IntentUtil.startActivity(getContext(), PersonalHomepageActivity.getIntentToStart(getContext(), getItem(position).getUserInfo()));
        }else if(id == R.id.tv_topic_content){
            IntentUtil.startActivity(getContext(), TopicDetailActivity.getIntentToStart(getContext(), getItem(position)));
        }else if(id == R.id.tv_topic_retweeted_content){
            Topic topic = getItem(position).getRetweetedTopic();
            if(topic != null) {
                IntentUtil.startActivity(getContext(), TopicDetailActivity.getIntentToStart(getContext(), topic));
            }
        }
    }

    @Override
    public boolean onItemLongClick(View view, int position) {

        mLongClickPosition = position;
        mLongClickDialog.show();
        return true;
    }

    public static class TopicViewHolder extends CommonAdapter.ItemViewHolder<Topic> {

        TopicTitleViewHolder mTopicTitle;
        TopicContentViewHolder mTopic, mRetweetTopic;
        View mRetweetTopicLayout;

        public TopicViewHolder(View v) {
            super(v);
            if(mTopicTitle == null){
                mTopicTitle = new TopicTitleViewHolder(v);
            }
            if (mTopic == null) {
                mTopic = new TopicContentViewHolder();
            }
            if (mRetweetTopic == null) {
                mRetweetTopic = new TopicContentViewHolder();
            }
            mTopic.mContent = (TextView) v.findViewById(R.id.tv_topic_content);
            mTopic.mGallery = (GridGalleryView) v.findViewById(R.id.gl_topic_gallery);
            mRetweetTopicLayout = v.findViewById(R.id.ll_topic_retweeted);
            mRetweetTopic.mContent = (TextView) v.findViewById(R.id.tv_topic_retweeted_content);
            mRetweetTopic.mGallery = (GridGalleryView) v.findViewById(R.id.gl_topic_retweeted_gallery);
        }

        @Override
        public void bindData(Context context, Topic topic) {
            if(topic == null){
                return;
            }

            mTopicTitle.initTopicTitle(context, topic);
            registerChildViewItemClick(mTopicTitle.mTitle);

            mTopic.initTopicContent(context, topic, false);
            registerChildViewItemClick(mTopic.mContent);

            registerChildViewItemLongClick(mTopicTitle.mTitle);
            registerChildViewItemLongClick(mTopic.mContent);
            if (topic.getRetweetedTopic() != null) {
                mRetweetTopicLayout.setVisibility(View.VISIBLE);
                mRetweetTopic.initTopicContent(context, topic.getRetweetedTopic(), true);
                registerChildViewItemClick(mRetweetTopic.mContent);
                registerChildViewItemLongClick(mRetweetTopic.mContent);
            } else {
                mRetweetTopicLayout.setVisibility(View.GONE);
            }
        }
    }

    public static class TopicTitleViewHolder {

        NetworkImageViewPlus mAvatar;
        TextView mUsername, mDescription;
        View mTitle;

        public TopicTitleViewHolder() {}

        public TopicTitleViewHolder(View v) {
            mTitle = v.findViewById(R.id.rl_topic_title);
            mAvatar = (NetworkImageViewPlus) v.findViewById(R.id.iv_topic_avatar);
            mUsername = (TextView) v.findViewById(R.id.tv_topic_username);
            mDescription = (TextView) v.findViewById(R.id.tv_topic_description);
        }

        public void initTopicTitle(final Context context, Topic topic) {
            String time = DateUtil.getLatestDateFormat(topic.getDate());
            if (TextUtils.isEmpty(topic.getChannel())) {
                mDescription.setText(time);
            } else {
                String str = String.format(context.getString(R.string.label_time_and_from), time, Html.fromHtml(topic.getChannel()));
                mDescription.setText(str);
            }

            UserInfo userInfo = topic.getUserInfo();
            if(userInfo != null){
                mUsername.setText(userInfo.getName());
                mAvatar.setImageUrl(userInfo.getAvatar(), RequestManager.getImageLoader());
            }
        }

        public void initTopicTitle(final Context context, TopicComment topicComment) {
            String time = DateUtil.getLatestDateFormat(topicComment.getDate());
            if (TextUtils.isEmpty(topicComment.getChannel())) {
                mDescription.setText(time);
            } else {
                String str = String.format(context.getString(R.string.label_time_and_from), time, Html.fromHtml(topicComment.getChannel()));
                mDescription.setText(str);
            }

            UserInfo userInfo = topicComment.getUserInfo();
            if(userInfo != null){
                mUsername.setText(userInfo.getName());
                mAvatar.setImageUrl(userInfo.getAvatar(), RequestManager.getImageLoader());
            }
        }
    }

    public static class TopicContentViewHolder {
        public TextView mContent;
        public GridGalleryView mGallery;

        public TopicContentViewHolder(){}

        TopicContentUrlOnTouchListener mTopicContentUrlOnTouchListener = new TopicContentUrlOnTouchListener();

        public void initTopicContent(final Context context, Topic topic, boolean isRetweeted) {

            //不设置的话会被名字内容的点击事件覆盖，无法触发ItemView的onClick
            mContent.setText(topic.getUrlSpannableString(context));
            mContent.setMovementMethod(LongClickableLinkMovementMethod.getInstance());
            mContent.setOnTouchListener(mTopicContentUrlOnTouchListener);

            if (!CommonUtil.isEmptyCollection(topic.getImageUrls())) {
                //加载图片
                final List<String> urls = topic.getImageUrls();
                mGallery.removeAllViews();
                mGallery.setTag(urls);
                mGallery.setMargin(context.getResources().getDimensionPixelSize(R.dimen.topic_gallery_iv_margin));
                mGallery.setMaxWidth(mGalleryMaxWidth);
                mGallery.setGridCount(urls.size());
                mGallery.setHandleData(new GridGalleryView.HandleData() {
                    @Override
                    public NetworkImageViewPlus getImageView() {
                        NetworkImageViewPlus iv = new NetworkImageViewPlus(context);
//                                iv.setFadeInImage(mIsFadeInImage);
                        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        iv.setBackgroundColor(context.getResources().getColor(R.color.image_default_bg));
                        iv.setTag(mGallery);
                        iv.setId(View.NO_ID);
                        return iv;
                    }

                    @Override
                    public void handleChildItem(ImageView imageView, int position) {
                        NetworkImageViewPlus iv = (NetworkImageViewPlus) imageView;
                        iv.setImageUrl(urls.get(position), RequestManager.getImageLoader());
                    }
                        });
                mGallery.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int id = view.getId();
                        if (id == View.NO_ID) {
                            GridLayout gridLayout = (GridLayout) view.getTag();
                            ArrayList<String> urls = (ArrayList<String>) gridLayout.getTag();
                            ArrayList<AnimationRect> animationRectArrayList
                                    = new ArrayList<>();
                            for (int i = 0; i < urls.size(); i++) {
                                final ImageView imageView = (ImageView) gridLayout
                                        .getChildAt(i);
                                if (imageView.getVisibility() == View.VISIBLE) {
                                    AnimationRect rect = AnimationRect.buildFromImageView(imageView);
                                    animationRectArrayList.add(rect);
                                }
                            }

                            TopicGalleryActivity
                                    .startWithIntent(context, urls, animationRectArrayList, position);
                        }
                    }
                });
                mGallery.reset();
                mGallery.setVisibility(View.VISIBLE);
            } else {
                mGallery.setVisibility(View.GONE);
            }
        }
    }
}













