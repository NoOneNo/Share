package com.hengye.share.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.BaseApplication;
import com.hengye.share.R;
import com.hengye.share.ui.activity.TopicGalleryActivity;
import com.hengye.share.module.Topic;
import com.hengye.share.module.sina.WBUtil;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.ui.view.GridGalleryView;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.SimpleClickableSpan;
import com.hengye.share.util.SimpleLinkMovementMethod;
import com.hengye.share.util.ViewUtil;
import com.hengye.volleyplus.toolbox.RequestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RecyclerViewTopicAdapter extends RecyclerViewSimpleAdapter<Topic, RecyclerViewTopicAdapter.MainViewHolder>
        implements View.OnClickListener {

    public RecyclerViewTopicAdapter(Context context, List<Topic> data) {
        super(context, data);
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_topic, parent, false), getOnItemClickListener());
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {

        Topic topic = getItem(position);

        holder.bindData(mContext, topic);

    }

    @SuppressWarnings("unchecked")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_topic_content || id == R.id.tv_topic_retweeted_content) {
            View itemView = (View) v.getTag();
            if (itemView != null) {
                itemView.performClick();
            }
        }
    }

    public static class MainViewHolder extends RecyclerViewSimpleAdapter.ViewHolder<Topic> {

        NetworkImageViewPlus mAvator;
        TextView mUsername, mDescription;
        TopicContentViewHolder mTopic, mRetweetTopic;
        View mRetweetTopicLayout;

        public MainViewHolder(View v, ViewUtil.OnItemClickListener onItemClickListener) {
            super(v, onItemClickListener);
            if (mTopic == null) {
                mTopic = new TopicContentViewHolder();
            }
            if (mRetweetTopic == null) {
                mRetweetTopic = new TopicContentViewHolder();
            }
            mAvator = (NetworkImageViewPlus) v.findViewById(R.id.iv_topic_avator);
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
            mUsername.setText(topic.getUsername());
            String time = DateUtil.getLatestDateFormat(topic.getDate());
            if (TextUtils.isEmpty(topic.getChannel())) {
                mDescription.setText(time);
            } else {
                mDescription.setText(time + " 来自 " + Html.fromHtml(topic.getChannel()));
            }

            mAvator.setImageUrl(topic.getAvator(), RequestManager.getImageLoader());

            initTopicContent(context, mTopic, topic, false, itemView);
            if (topic.getRetweetedTopic() != null) {
                mRetweetTopicLayout.setVisibility(View.VISIBLE);
                initTopicContent(context, mRetweetTopic, topic.getRetweetedTopic(), true, itemView);
            } else {
                mRetweetTopicLayout.setVisibility(View.GONE);
            }
        }

        private void initTopicContent(final Context context, final TopicContentViewHolder holder, Topic topic, boolean isRetweeted, View itemView) {

            //不设置的话会被名字内容的点击事件覆盖，无法触发ItemView的onClick
            holder.mContent.setTag(itemView);
            holder.mContent.setOnClickListener(this);
            String str;
            if (isRetweeted && !TextUtils.isEmpty(topic.getUsername())) {
                //如果微博已经被删除，则名字为空
                str = "@" + topic.getUsername() + ":" + topic.getContent();
            } else {
                str = topic.getContent();
            }

            Map<Integer, String> atNames = WBUtil.getMatchAtWBName(str);
            if (!CommonUtil.isEmptyMap(atNames)) {
                SpannableString ss = new SpannableString(str);
                for (Map.Entry<Integer, String> entry : atNames.entrySet()) {
                    final int startIndex = entry.getKey();
                    final String atName = entry.getValue();
                    SimpleClickableSpan scs = new SimpleClickableSpan();
                    scs.setNormalColor(context.getResources().getColor(R.color.topic_name_at)).
                            setSelectedColor(context.getResources().getColor(R.color.topic_username)).
                            setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                            Intent intent = new Intent(mContext, LoginActivity.class);
//                            IntentUtil.startActivityIfTokenValid(mContext, intent);
                                    Toast.makeText(context, atName.substring(0, atName.length() - 1), Toast.LENGTH_SHORT).show();
                                }
                            });
                    //此处-1为了除去@name后面的判断符,(:|：| );
                    ss.setSpan(scs, startIndex, startIndex + atName.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                }
                holder.mContent.setText(ss);
                holder.mContent.setMovementMethod(SimpleLinkMovementMethod.getInstance());
            } else {
                holder.mContent.setText(str);
            }

            holder.mContent.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    TextView tv = (TextView) v;
                    switch (action) {
                        case MotionEvent.ACTION_MOVE:
                            Selection.removeSelection(SpannableString.valueOf(tv.getText()));
                    }
                    return false;
                }
            });

            if (!CommonUtil.isEmptyCollection(topic.getImageUrls())) {
                //加载图片
                final List<String> urls = topic.getImageUrls();
                holder.mGallery.setTag(urls);
                holder.mGallery.
                        setMargin(context.getResources().getDimensionPixelSize(R.dimen.topic_gallery_iv_margin)).
                        setChildCount(urls.size()).
                        setHandleData(new GridGalleryView.HandleData() {
                            @Override
                            public NetworkImageViewPlus getImageView() {
                                NetworkImageViewPlus iv = new NetworkImageViewPlus(context);
                                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                                iv.setBackgroundColor(context.getResources().getColor(R.color.material_grey_300));
                                iv.setTag(holder.mGallery);
                                iv.setId(View.NO_ID);
                                return iv;
                            }

                            @Override
                            public void handleChildItem(ImageView imageView, int position) {
                                NetworkImageViewPlus iv = (NetworkImageViewPlus) imageView;
                                iv.setImageUrl(urls.get(position), RequestManager.getImageLoader());
                            }
                        }).
                        setOnItemClickListener(new ViewUtil.OnItemClickListener() {
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
                        }).reset();
                holder.mGallery.setVisibility(View.VISIBLE);
            } else {
                holder.mGallery.setVisibility(View.GONE);
            }
        }

    }

    static class TopicContentViewHolder {
        TextView mContent;
        GridGalleryView mGallery;
    }
}













