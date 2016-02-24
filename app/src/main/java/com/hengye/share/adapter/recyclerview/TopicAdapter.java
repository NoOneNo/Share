package com.hengye.share.adapter.recyclerview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.R;
import com.hengye.share.helper.TransitionHelper;
import com.hengye.share.model.Topic;
import com.hengye.share.model.TopicComment;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.TopicDraftHelper;
import com.hengye.share.model.sina.WBTopic;
import com.hengye.share.ui.activity.PersonalHomepageActivity;
import com.hengye.share.ui.activity.TopicDetailActivity;
import com.hengye.share.ui.activity.TopicGalleryActivity;
import com.hengye.share.ui.activity.TopicPublishActivity;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.ui.support.textspan.LongClickableLinkMovementMethod;
import com.hengye.share.ui.support.textspan.TopicContentUrlOnTouchListener;
import com.hengye.share.ui.view.GridGalleryView;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.ViewUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopicAdapter extends CommonAdapter<Topic, TopicAdapter.TopicViewHolder>
        implements ViewUtil.OnItemClickListener, ViewUtil.OnItemLongClickListener, DialogInterface.OnClickListener {

    public static int mGalleryMaxWidth;
    private Dialog mLongClickDialog;
    private int mLongClickPosition;
    private RecyclerView mRecyclerView;

    public TopicAdapter(Context context, List<Topic> data, RecyclerView recyclerView) {
        super(context, data);
        mRecyclerView = recyclerView;

        int galleryMargin = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        mGalleryMaxWidth = context.getResources().getDisplayMetrics().widthPixels - 2 * galleryMargin;

        mLongClickDialog = DialogBuilder.getOnLongClickTopicDialog(getContext(), this);
        setOnChildViewItemClickListener(this);
        setOnItemLongClickListener(this);
        setOnChildViewItemLongClickListener(this);
    }

    @Override
    public TopicViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new TopicViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_topic_total, parent, false));
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        Topic topic = getItem(mLongClickPosition);
        if (topic == null) {
            return;
        }
        switch (which) {
            case DialogBuilder.LONG_CLICK_TOPIC_REPOST:
                IntentUtil.startActivity(getContext(),
                        TopicPublishActivity.getStartIntent(getContext(), TopicDraftHelper.getWBTopicDraftByTopicRepost(topic.getId())));
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_COMMENT:
                IntentUtil.startActivity(getContext(),
                        TopicPublishActivity.getStartIntent(getContext(), TopicDraftHelper.getWBTopicDraftByTopicComment(topic.getId())));
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_COLLECT:
                WBTopic wbTopic = Topic.getWBTopic(topic);
                if (wbTopic != null) {
                    RequestManager.addToRequestQueue(getWBCollectTopicRequest(topic.getId(), wbTopic.isFavorited()));
                }
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_REPOST_ORIGIN:
                Topic retweet = topic.getRetweetedTopic();
                if (retweet == null) {
                    break;
                }
                IntentUtil.startActivity(getContext(),
                        TopicPublishActivity.getStartIntent(getContext(), TopicDraftHelper.getWBTopicDraftByTopicRepost(retweet.getId())));
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_COPY:
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onItemLongClick(View view, int position) {

        mLongClickPosition = position;
        mLongClickDialog.show();
        return true;
    }

    @Override
    public void onItemClick(View view, int position) {
        int id = view.getId();
        if (id == R.id.iv_topic_avatar || id == R.id.tv_topic_username || id == R.id.tv_topic_description) {
            IntentUtil.startActivity(getContext(), PersonalHomepageActivity.getStartIntent(getContext(), getItem(position).getUserInfo()));
        } else if (id == R.id.tv_topic_content || id == R.id.gl_topic_gallery || id == R.id.ll_topic || id == R.id.rl_topic_title) {
            startTopicDetail(false, position);
        } else if (id == R.id.tv_topic_retweeted_content || id == R.id.gl_topic_retweeted_gallery || id == R.id.ll_topic_retweeted) {
            startTopicDetail(true, position);
        }
    }

    public void startTopicDetail(boolean isRetweet, int position) {
        Topic topic = isRetweet ? getItem(position).getRetweetedTopic() : getItem(position);
        if (topic == null) {
            return;
        }

        TopicViewHolder vh = (TopicViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (vh == null) {
            IntentUtil.startActivity(getContext(), TopicDetailActivity.getStartIntent(getContext(), topic, isRetweet));
        } else {
            Activity activity = (Activity) getContext();
            final Pair[] pairs = TransitionHelper.createSafeTransitionParticipants(activity, false,
                    new Pair<>(isRetweet ? vh.mRetweetTopic.mTopicLayout : vh.mTopicItem, activity.getString(R.string.transition_name_topic)));
            ActivityOptionsCompat activityOptions = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(activity, pairs);
            ActivityCompat.startActivity(activity, TopicDetailActivity.getStartIntent(getContext(), topic, isRetweet), activityOptions.toBundle());
        }
    }

    public static class TopicViewHolder extends CommonAdapter.ItemViewHolder<Topic> {

        TopicTitleViewHolder mTopicTitle;
        TopicContentViewHolder mTopic, mRetweetTopic;
        View mTopicItem;

        public TopicViewHolder(View v) {
            super(v);
            if (mTopicTitle == null) {
                mTopicTitle = new TopicTitleViewHolder(v);
            }
            if (mTopic == null) {
                mTopic = new TopicContentViewHolder();
            }
            if (mRetweetTopic == null) {
                mRetweetTopic = new TopicContentViewHolder();
            }

            mTopicItem = findViewById(R.id.item_topic);

            mTopic.mContent = (TextView) findViewById(R.id.tv_topic_content);
            mTopic.mGallery = (GridGalleryView) findViewById(R.id.gl_topic_gallery);
            mTopic.mTopicLayout = findViewById(R.id.ll_topic);

            mRetweetTopic.mContent = (TextView) findViewById(R.id.tv_topic_retweeted_content);
            mRetweetTopic.mGallery = (GridGalleryView) findViewById(R.id.gl_topic_retweeted_gallery);
            mRetweetTopic.mTopicLayout = findViewById(R.id.ll_topic_retweeted);
        }

        @Override
        public void bindData(Context context, Topic topic, int position) {
            if (topic == null) {
                return;
            }

            mTopicTitle.initTopicTitle(context, topic);
            mTopic.initTopicContent(context, topic, false);

            registerChildViewItemClick(mTopicTitle.mAvatar);
            registerChildViewItemClick(mTopicTitle.mUsername);
            registerChildViewItemClick(mTopicTitle.mDescription);

            registerChildViewItemClick(mTopicTitle.mTitle);
            registerChildViewItemClick(mTopic.mContent);
            registerChildViewItemClick(mTopic.mGallery);
            registerChildViewItemClick(mTopic.mTopicLayout);

            registerChildViewItemLongClick(mTopicTitle.mTitle);
            registerChildViewItemLongClick(mTopic.mContent);
            registerChildViewItemLongClick(mTopic.mGallery);
            registerChildViewItemLongClick(mTopic.mTopicLayout);
            if (topic.getRetweetedTopic() != null) {
                mRetweetTopic.mTopicLayout.setVisibility(View.VISIBLE);
                mRetweetTopic.initTopicContent(context, topic.getRetweetedTopic(), true);
                registerChildViewItemClick(mRetweetTopic.mContent);
                registerChildViewItemClick(mRetweetTopic.mGallery);
                registerChildViewItemClick(mRetweetTopic.mTopicLayout);

                registerChildViewItemLongClick(mRetweetTopic.mContent);
                registerChildViewItemLongClick(mRetweetTopic.mGallery);
                registerChildViewItemLongClick(mRetweetTopic.mTopicLayout);
            } else {
                mRetweetTopic.mTopicLayout.setVisibility(View.GONE);
            }
        }
    }

    public static class TopicTitleViewHolder {

        NetworkImageViewPlus mAvatar;
        TextView mUsername, mDescription;
        View mTitle;

        public TopicTitleViewHolder() {
        }

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
            if (userInfo != null) {
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
            if (userInfo != null) {
                mUsername.setText(userInfo.getName());
                mAvatar.setImageUrl(userInfo.getAvatar(), RequestManager.getImageLoader());
            }
        }
    }

    public static class TopicContentViewHolder {
        public TextView mContent;
        public GridGalleryView mGallery;
        View mTopicLayout;


        public TopicContentViewHolder() {
        }

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

    private GsonRequest getWBCollectTopicRequest(final String topicId, boolean isFavorite) {
        final UrlBuilder ub;
        if (isFavorite) {
            ub = new UrlBuilder(UrlFactory.getInstance().getWBDestroyFavoritesUrl());
        } else {
            ub = new UrlBuilder(UrlFactory.getInstance().getWBCreateFavoritesUrl());
        }

        ub.addParameter("access_token", UserUtil.getToken());
        ub.addParameter("id", topicId);
        return new GsonRequest<WBTopic>(Request.Method.POST,
                WBTopic.class,
                ub.getUrl()
                , new Response.Listener<WBTopic>() {
            @Override
            public void onResponse(WBTopic response) {
                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
                ToastUtil.showToast(response != null ? "收藏成功" : "收藏失败");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ToastUtil.showToast("收藏失败");
                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), volleyError);
            }
        }) {
            @Override
            public byte[] getBody() {
                return ub.getBody();
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }
        };
    }
}













