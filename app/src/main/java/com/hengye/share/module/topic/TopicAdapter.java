package com.hengye.share.module.topic;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.hengye.share.R;
import com.hengye.share.adapter.recyclerview.CommonAdapter;
import com.hengye.share.adapter.recyclerview.ItemViewHolder;
import com.hengye.share.model.Topic;
import com.hengye.share.model.TopicComment;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.TopicDraftHelper;
import com.hengye.share.model.sina.WBTopic;
import com.hengye.share.module.profile.PersonalHomepageActivity;
import com.hengye.share.module.publish.TopicPublishActivity;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.module.util.image.GalleryActivity;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.ui.support.textspan.TopicUrlOnTouchListener;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.ui.widget.image.AvatarImageView;
import com.hengye.share.ui.widget.image.GridGalleryView;
import com.hengye.share.ui.widget.image.SuperImageView;
import com.hengye.share.ui.widget.listener.OnItemClickListener;
import com.hengye.share.ui.widget.listener.OnItemLongClickListener;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.ToastUtil;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends CommonAdapter<Topic>
        implements OnItemClickListener, OnItemLongClickListener, DialogInterface.OnClickListener {

    public static int mGalleryMaxWidth;
    private Dialog mLongClickDialog;
    private int mLongClickPosition;
    private boolean mIsRetweetedLongClick;
    private RecyclerView mRecyclerView;

    public TopicAdapter(Context context, List<Topic> data, RecyclerView recyclerView) {
        super(context, data);
        mRecyclerView = recyclerView;

        int galleryMargin = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        mGalleryMaxWidth = context.getResources().getDisplayMetrics().widthPixels - 2 * galleryMargin;

        mLongClickDialog = DialogBuilder.getOnLongClickTopicDialog(getContext(), this);
        setOnItemClickListener(this);
        setOnItemLongClickListener(this);
        setCheckDiffMode(true);
    }

    @Override
    public TopicDefaultViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new TopicDefaultViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_topic_total, parent, false));
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        Topic topic = getItem(mLongClickPosition);
        if (topic == null) {
            return;
        }
        if(mIsRetweetedLongClick){
            if(topic.getRetweetedTopic() == null){
                return;
            }else {
                topic = topic.getRetweetedTopic();
            }
        }
        switch (which) {
            case DialogBuilder.LONG_CLICK_TOPIC_REPOST:
                IntentUtil.startActivity(getContext(),
                        TopicPublishActivity.getStartIntent(getContext(), TopicDraftHelper.getWBTopicDraftByRepostRepost(topic)));
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_COMMENT:
                IntentUtil.startActivity(getContext(),
                        TopicPublishActivity.getStartIntent(getContext(), TopicDraftHelper.getWBTopicDraftByRepostComment(topic)));
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_COLLECT:
                WBTopic wbTopic = Topic.getWBTopic(topic);
                if (wbTopic != null) {
                    RequestManager.addToRequestQueue(getWBCollectTopicRequest(topic.getId(), wbTopic.isFavorited()));
                }
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_COPY:
                ClipboardUtil.copyWBContent(topic.getContent());
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        boolean isRetweeted = false;
        if(view.getTag() != null && view.getTag() instanceof Boolean){
            isRetweeted = (Boolean) view.getTag();
        }

        mLongClickPosition = position;
        mIsRetweetedLongClick = isRetweeted;
        mLongClickDialog.show();
        return true;
    }

    private Handler mHandler = new Handler();

    @Override
    public void onItemClick(View view, final int position) {
        int id = view.getId();
        if (id == R.id.iv_topic_avatar || id == R.id.tv_topic_username || id == R.id.tv_topic_description) {
            //为了显示波纹效果再启动
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                   startPersonHomePage(position);
                }
            }, 100);
//            IntentUtil.startActivity(getContext(), PersonalHomepageActivity.getStartIntent(getContext(), getItem(position).getUserInfo()));
        } else if (id == R.id.tv_topic_content || id == R.id.gl_topic_gallery || id == R.id.rl_topic_title || id == R.id.ll_topic_content || id == R.id.ll_topic_retweeted_content) {
            final boolean isRetweeted = (Boolean) view.getTag();
            //为了显示波纹效果再启动
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startTopicDetail(isRetweeted, position);
                }
            }, 200);
//            startTopicDetail(isRetweeted, position);
        }

//        else if (id == R.id.tv_topic_retweeted_content || id == R.id.gl_topic_retweeted_gallery || id == R.id.ll_topic_retweeted_content) {
//            startTopicDetail(true, position);
//        }
    }

    public void startPersonHomePage(int position){
        Topic topic = getItem(position);
        TopicDefaultViewHolder vh = (TopicDefaultViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        if (topic == null || vh == null) {
            return;
        }
        PersonalHomepageActivity.start(getContext(), vh.mTopicTitle.mAvatar, topic.getUserInfo());
    }

    public void startTopicDetail(boolean isRetweet, int position) {
        Topic topic = isRetweet ? getItem(position).getRetweetedTopic() : getItem(position);
        TopicDefaultViewHolder vh = (TopicDefaultViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
        TopicViewHolder.startTopicDetail(getContext(), vh, isRetweet, topic);
    }

    public static class TopicDefaultViewHolder extends TopicViewHolder<Topic> {

        public TopicDefaultViewHolder(View v) {
            super(v);
        }

        @Override
        public void bindData(Context context, Topic topic, int position) {
            if (topic == null) {
                return;
            }

            mTopicTitle.initTopicTitle(context, topic);
            mTopic.initTopicContent(context, topic, false);

            if (topic.getRetweetedTopic() != null) {
                mRetweetTopic.mTopicLayout.setVisibility(View.VISIBLE);
                mRetweetTopic.initTopicContent(context, topic.getRetweetedTopic(), true);
            } else {
                mRetweetTopic.mTopicLayout.setVisibility(View.GONE);
            }
        }
    }

    public static class TopicViewHolder<T> extends ItemViewHolder<T> {

        public TopicTitleViewHolder mTopicTitle;
        public TopicContentViewHolder mTopic, mRetweetTopic;
        public View mTopicTotalItem, mTopicItem;

        public TopicViewHolder(View v) {
            super(v);
            if (mTopicTitle == null) {
                mTopicTitle = new TopicTitleViewHolder(v);
            }
            if (mTopic == null) {
                mTopic = new TopicContentViewHolder(findViewById(R.id.ll_topic_content));
            }
            if (mRetweetTopic == null) {
                mRetweetTopic = new TopicContentViewHolder(findViewById(R.id.ll_topic_retweeted_content), true);
            }

            mTopicItem = findViewById(R.id.ll_topic);
            mTopicTotalItem = findViewById(R.id.item_topic);

//            mTopic.mContent = (TextView) findViewById(R.id.tv_topic_content);
//            mTopic.mGallery = (GridGalleryView) findViewById(R.id.gl_topic_gallery);
//            mTopic.mTopicLayout = findViewById(R.id.ll_topic_content);
//
//            mRetweetTopic.mContent = (TextView) findViewById(R.id.tv_topic_retweeted_content);
//            mRetweetTopic.mGallery = (GridGalleryView) findViewById(R.id.gl_topic_retweeted_gallery);
//            mRetweetTopic.mTopicLayout = findViewById(R.id.ll_topic_retweeted_content);


            registerOnClickListener(mTopicTitle.mAvatar);
            registerOnClickListener(mTopicTitle.mUsername);
            registerOnClickListener(mTopicTitle.mDescription);

            registerOnClickListener(mTopicTitle.mTitle);
            registerOnClickListener(mTopic.mContent);
            registerOnClickListener(mTopic.mGallery);
            registerOnClickListener(mTopic.mTopicLayout);

            registerOnLongClickListener(mTopicTitle.mTitle);
            registerOnLongClickListener(mTopic.mContent);
            registerOnLongClickListener(mTopic.mGallery);
            registerOnLongClickListener(mTopic.mTopicLayout);

            registerOnClickListener(mRetweetTopic.mContent);
            registerOnClickListener(mRetweetTopic.mGallery);
            registerOnClickListener(mRetweetTopic.mTopicLayout);

            registerOnLongClickListener(mRetweetTopic.mContent);
            registerOnLongClickListener(mRetweetTopic.mGallery);
            registerOnLongClickListener(mRetweetTopic.mTopicLayout);


            registerOnLongClickListener(mTopicItem);

            SelectorLoader.getInstance().setDefaultRippleBackground(mTopicItem);
            SelectorLoader.getInstance().setDefaultRippleWhiteBackground(mRetweetTopic.mTopicLayout);

            mTopicTitle.mAvatar.setOnTouchListener(mTopicOnTouchListener);
            mTopicTitle.mUsername.setOnTouchListener(mTopicOnTouchListener);
            mTopicTitle.mDescription.setOnTouchListener(mTopicOnTouchListener);
            mTopicTitle.mTitle.setOnTouchListener(mTopicOnTouchListener);
            mTopic.mContent.setOnTouchListener(mTopicOnTouchListener);
            mTopic.mGallery.setOnTouchListener(mTopicOnTouchListener);
            mRetweetTopic.mContent.setOnTouchListener(mRetweetedTopicOnTouchListener);
            mRetweetTopic.mGallery.setOnTouchListener(mRetweetedTopicOnTouchListener);
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

        private View.OnTouchListener mRetweetedTopicOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int id = v.getId();
                if(id == R.id.tv_topic_content) {
                    if(!TopicUrlOnTouchListener.getInstance().onTouch(v, event)){
                        mRetweetTopic.mTopicLayout.onTouchEvent(event);
                    }
                    return false;
                }else{
                    mRetweetTopic.mTopicLayout.onTouchEvent(event);
                    return false;
                }
            }
        };

        public static void startTopicDetail(Context context, TopicViewHolder tvh, boolean isRetweet, Topic topic) {
            if (topic == null || tvh == null) {
                return;
            }

            TopicDetailActivity.start(context,
                    isRetweet ? tvh.mRetweetTopic.mTopicLayout : tvh.mTopicTotalItem,
                    topic,
                    isRetweet);
        }
    }

    public static class TopicTitleViewHolder {

        public AvatarImageView mAvatar;
        public TextView mUsername, mDescription;
        public View mTitle;

        public TopicTitleViewHolder() {
        }

        public TopicTitleViewHolder(View v) {
            mTitle = v.findViewById(R.id.rl_topic_title);
            mAvatar = (AvatarImageView) v.findViewById(R.id.iv_topic_avatar);
            mUsername = (TextView) v.findViewById(R.id.tv_topic_username);
            mDescription = (TextView) v.findViewById(R.id.tv_topic_description);

            mTitle.setTag(false);
        }

        public void initTopicTitle(final Context context, Topic topic) {
            if (TextUtils.isEmpty(topic.getChannel())) {
                mDescription.setText(topic.getFormatDate());
            } else {
                String str = String.format(context.getString(R.string.label_time_and_from), topic.getFormatDate(), Html.fromHtml(topic.getChannel()));
                mDescription.setText(str);
            }

            UserInfo userInfo = topic.getUserInfo();
            if (userInfo != null) {
                mUsername.setText(userInfo.getName());
                if(SettingHelper.isShowTopicAvatar()){
                    mAvatar.setImageUrl(userInfo.getAvatar(), RequestManager.getImageLoader());
                }else{
                    mAvatar.setImageResource(R.drawable.ic_user_avatar);
                }
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
                if(SettingHelper.isShowCommentAndRepostAvatar()) {
                    mAvatar.setImageUrl(userInfo.getAvatar(), RequestManager.getImageLoader());
                }else{
                    mAvatar.setImageResource(R.drawable.ic_user_avatar);
                }
            }
        }
    }

    public static class TopicContentViewHolder {
        public TextView mContent;
        public GridGalleryView mGallery;
        public View mTopicLayout;

        public TopicContentViewHolder(View parent) {
            this(parent, false);
        }

        public TopicContentViewHolder(View parent, boolean isRetweeted) {
            mContent = (TextView) parent.findViewById(R.id.tv_topic_content);
            mGallery = (GridGalleryView) parent.findViewById(R.id.gl_topic_gallery);
            mTopicLayout = parent;

            mContent.setTag(isRetweeted);
            mGallery.setTag(isRetweeted);
            mTopicLayout.setTag(isRetweeted);
        }

        public void initTopicContent(final Context context, Topic topic, boolean isRetweeted) {

            //不设置的话会被名字内容的点击事件覆盖，无法触发ItemView的onClick
            mContent.setText(topic.getUrlSpannableString(isRetweeted));
//            mContent.setMovementMethod(SimpleLinkMovementMethod.getInstance());
//            mContent.setOnTouchListener(TopicUrlOnTouchListener.getInstance());


            if (!CommonUtil.isEmpty(topic.getImageUrls())) {
                //加载图片
                final List<String> urls = topic.getImageUrls();
                mGallery.removeAllViews();
                mGallery.setTag(View.NO_ID, urls);
                mGallery.setMargin(context.getResources().getDimensionPixelSize(R.dimen.topic_gallery_iv_margin));
                mGallery.setMaxWidth(mGalleryMaxWidth);
                mGallery.setGridCount(urls.size());
                mGallery.setHandleData(new GridGalleryView.HandleData() {
                    @Override
                    public ImageView getImageView() {
                        SuperImageView iv = new SuperImageView(context);
//                                iv.setFadeInImage(mIsFadeInImage);
                        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        iv.setBackgroundColor(context.getResources().getColor(R.color.image_default_bg));
                        iv.setTag(mGallery);
                        iv.setId(View.NO_ID);
                        return iv;
                    }

                    @Override
                    public void handleChildItem(ImageView imageView, int position) {
                        SuperImageView iv = (SuperImageView) imageView;
                        iv.setImageUrl(urls.get(position));
                    }
                });
                mGallery.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        int id = view.getId();
                        if (id == View.NO_ID) {
                            GridLayout gridLayout = (GridLayout) view.getTag();
                            ArrayList<String> urls = (ArrayList<String>) gridLayout.getTag(View.NO_ID);
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

//                            TopicGalleryActivity
//                                    .startWithIntent(context, urls, position, animationRectArrayList);
                            GalleryActivity
                                    .startWithIntent(context, urls, position, animationRectArrayList);
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













