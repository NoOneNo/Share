package com.hengye.share.adapter.recyclerview;

import android.content.Context;
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
import com.hengye.share.ui.activity.PersonalHomepageActivity;
import com.hengye.share.ui.activity.TopicGalleryActivity;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.ui.support.LongClickableLinkMovementMethod;
import com.hengye.share.ui.support.TopicContentUrlOnTouchListener;
import com.hengye.share.ui.view.GridGalleryView;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends CommonAdapter<Topic, TopicAdapter.TopicViewHolder>
    implements ViewUtil.OnItemClickListener{

    public static int mGalleryMaxWidth;
    public TopicAdapter(Context context, List<Topic> data) {
        super(context, data);
        int galleryMargin = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        mGalleryMaxWidth = context.getResources().getDisplayMetrics().widthPixels - 2 * galleryMargin;
        setOnChildViewItemClickListener(this);
    }

    @Override
    public TopicViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new TopicViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_topic, parent, false));
    }

    @Override
    public void onItemClick(View view, int position) {
        int id = view.getId();
        if(id == R.id.rl_topic_title){
            IntentUtil.startActivity(getContext(), PersonalHomepageActivity.getIntentToStart(getContext(), getItem(position).getUserInfo()));
        }
    }

    public static class TopicViewHolder extends CommonAdapter.ItemViewHolder<Topic> {

        NetworkImageViewPlus mAvatar;
        TextView mUsername, mDescription;
        TopicContentViewHolder mTopic, mRetweetTopic;
        View mTopicTitle, mRetweetTopicLayout;

        boolean mIsFadeInImage;

        TopicContentUrlOnTouchListener mTopicContentUrlOnTouchListener = new TopicContentUrlOnTouchListener();

        public TopicViewHolder(View v){
            this(v, true);
        }

        public TopicViewHolder(View v, boolean isFadeInImage) {
            super(v);
            mIsFadeInImage = isFadeInImage;
            if (mTopic == null) {
                mTopic = new TopicContentViewHolder();
            }
            if (mRetweetTopic == null) {
                mRetweetTopic = new TopicContentViewHolder();
            }
            mTopicTitle = v.findViewById(R.id.rl_topic_title);
            mAvatar = (NetworkImageViewPlus) v.findViewById(R.id.iv_topic_avatar);
            mUsername = (TextView) v.findViewById(R.id.tv_topic_username);
            mDescription = (TextView) v.findViewById(R.id.tv_topic_description);
            mTopic.mContent = (TextView) v.findViewById(R.id.tv_topic_content);
            mTopic.mGallery = (GridGalleryView) v.findViewById(R.id.gl_topic_gallery);
            mRetweetTopicLayout = v.findViewById(R.id.ll_topic_retweeted);
            mRetweetTopic.mContent = (TextView) v.findViewById(R.id.tv_topic_retweeted_content);
            mRetweetTopic.mGallery = (GridGalleryView) v.findViewById(R.id.gl_topic_retweeted_gallery);
        }

        @Override
        public void bindData(Context context, Topic topic) {
            registerChildViewItemClick(mTopicTitle);
            mUsername.setText(topic.getUserInfo().getName());
            String time = DateUtil.getLatestDateFormat(topic.getDate());
            if (TextUtils.isEmpty(topic.getChannel())) {
                mDescription.setText(time);
            } else {
                mDescription.setText(time + " 来自 " + Html.fromHtml(topic.getChannel()));
            }

            mAvatar.setImageUrl(topic.getUserInfo().getAvatar(), RequestManager.getImageLoader());

            initTopicContent(context, mTopic, topic, false);
            if (topic.getRetweetedTopic() != null) {
                mRetweetTopicLayout.setVisibility(View.VISIBLE);
                initTopicContent(context, mRetweetTopic, topic.getRetweetedTopic(), true);
            } else {
                mRetweetTopicLayout.setVisibility(View.GONE);
            }
        }

        public void initTopicContent(final Context context, final TopicContentViewHolder holder, Topic topic, boolean isRetweeted) {

            //不设置的话会被名字内容的点击事件覆盖，无法触发ItemView的onClick
            registerItemClick(holder.mContent);
            holder.mContent.setText(topic.getUrlSpannableString(context));
            holder.mContent.setMovementMethod(LongClickableLinkMovementMethod.getInstance());
            holder.mContent.setOnTouchListener(mTopicContentUrlOnTouchListener);

            if (!CommonUtil.isEmptyCollection(topic.getImageUrls())) {
                //加载图片
                final List<String> urls = topic.getImageUrls();
                holder.mGallery.removeAllViews();
                holder.mGallery.setTag(urls);
                holder.mGallery.
                        setMargin(context.getResources().getDimensionPixelSize(R.dimen.topic_gallery_iv_margin));
                holder.mGallery.setMaxWidth(mGalleryMaxWidth);
                holder.mGallery.setGridCount(urls.size());
                holder.mGallery.
                        setHandleData(new GridGalleryView.HandleData() {
                            @Override
                            public NetworkImageViewPlus getImageView() {
                                NetworkImageViewPlus iv = new NetworkImageViewPlus(context);
                                iv.setFadeInImage(mIsFadeInImage);
                                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                                iv.setBackgroundColor(context.getResources().getColor(R.color.image_default_bg));
                                iv.setTag(holder.mGallery);
                                iv.setId(View.NO_ID);
                                return iv;
                            }

                            @Override
                            public void handleChildItem(ImageView imageView, int position) {
                                NetworkImageViewPlus iv = (NetworkImageViewPlus) imageView;
                                iv.setImageUrl(urls.get(position), RequestManager.getImageLoader());
                            }
                        });
                holder.mGallery.setOnItemClickListener(new ViewUtil.OnItemClickListener() {
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
                holder.mGallery.reset();
                holder.mGallery.setVisibility(View.VISIBLE);
            } else {
                holder.mGallery.setVisibility(View.GONE);
            }
        }
    }

    public static class TopicContentViewHolder {
        public TextView mContent;
        public GridGalleryView mGallery;
    }
}













