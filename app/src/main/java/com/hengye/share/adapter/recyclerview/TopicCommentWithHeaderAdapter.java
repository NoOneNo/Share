package com.hengye.share.adapter.recyclerview;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.view.NetworkImageViewPlus;
import com.hengye.share.R;
import com.hengye.share.module.Topic;
import com.hengye.share.module.TopicComment;
import com.hengye.share.ui.activity.TopicDetail2Activity;
import com.hengye.share.ui.support.SimpleClickableSpan;
import com.hengye.share.ui.support.SimpleLinkMovementMethod;
import com.hengye.share.ui.view.GridGalleryView;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.List;
import java.util.Map;

public class TopicCommentWithHeaderAdapter extends CommonAdapter<TopicComment, TopicCommentWithHeaderAdapter.CommentViewHolder> {

    private Topic mTopic;
    private TabLayout mTabLayout;

    public TopicCommentWithHeaderAdapter(Context context, List<TopicComment> data, Topic topic, TabLayout tabLayout) {
        super(context, data);
        mTopic = topic;
        mTabLayout = tabLayout;
    }

    @Override
    public boolean isAddHeaderView() {
        return true;
    }

    @Override
    public boolean isAddFooterView() {
        return true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return new HeaderViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.header_topic_detail, parent, false), mTabLayout);
    }

    @Override
    public void onBindHeaderView(RecyclerView.ViewHolder holder, int position) {
        HeaderViewHolder headerViewHolder = ((HeaderViewHolder)holder);
        headerViewHolder.mTopicViewHolder.bindData(getContext(), mTopic);
        headerViewHolder.mTabLayout.setOnTabSelectedListener(((TopicDetail2Activity) getContext()).getOnTabSelectedListener());
        TabLayout tabLayoutAssist = ((TopicDetail2Activity) getContext()).getTabLayout();
        headerViewHolder.mTabLayout.getTabAt(tabLayoutAssist.getSelectedTabPosition()).select();
    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {

        TopicAdapter.TopicViewHolder mTopicViewHolder;
        TabLayout mTabLayout, mTargetTabLayout;

        public HeaderViewHolder(View v, TabLayout targetTabLayout) {
            super(v);
            mTargetTabLayout = targetTabLayout;

            mTopicViewHolder = new TopicAdapter.TopicViewHolder(v.findViewById(R.id.item_topic), false);
            mTabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
            mTabLayout.addTab((mTabLayout.newTab().setText("评论").setTag("tablayout")));
            mTabLayout.addTab((mTabLayout.newTab().setText("转发").setTag("tablayout")));

        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return new FooterViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.footer_null, parent, false));
    }

    @Override
    public void onBindFooterView(RecyclerView.ViewHolder holder, int position) {
        super.onBindFooterView(holder, position);
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder{
        public FooterViewHolder(View v){
            super(v);
        }
    }

    @Override
    public CommentViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_topic_comment, parent, false));
    }

    @Override
    public void onBindBasicItemView(CommentViewHolder holder, int position) {
        super.onBindBasicItemView(holder, position);
    }

    public static class CommentViewHolder extends CommonAdapter.ItemViewHolder<TopicComment> {

        NetworkImageViewPlus mAvatar;
        TextView mUsername, mDescription;
        TopicAdapter.TopicContentViewHolder mTopic;
        View mTopicTitle;

        public CommentViewHolder(View v) {
            super(v, true);

            if (mTopic == null) {
                mTopic = new TopicAdapter.TopicContentViewHolder();
            }

            mTopicTitle = v.findViewById(R.id.rl_topic_title);
            mAvatar = (NetworkImageViewPlus) v.findViewById(R.id.iv_topic_avatar);
            mUsername = (TextView) v.findViewById(R.id.tv_topic_username);
            mDescription = (TextView) v.findViewById(R.id.tv_topic_description);
            mTopic.mContent = (TextView) v.findViewById(R.id.tv_topic_content);
            mTopic.mGallery = (GridGalleryView) v.findViewById(R.id.gl_topic_gallery);
        }

        @Override
        public void bindData(Context context, TopicComment topicComment) {
            registerChildViewItemClick(mTopicTitle);
            mUsername.setText(topicComment.getUserInfo().getName());
            String time = DateUtil.getLatestDateFormat(topicComment.getDate());
            if (TextUtils.isEmpty(topicComment.getChannel())) {
                mDescription.setText(time);
            } else {
                mDescription.setText(time + " 来自 " + Html.fromHtml(topicComment.getChannel()));
            }

            mAvatar.setImageUrl(topicComment.getUserInfo().getAvatar(), RequestManager.getImageLoader());

            initCommentContent(context, mTopic, topicComment);
        }

        public void initCommentContent(final Context context, final TopicAdapter.TopicContentViewHolder holder, TopicComment topicComment) {

            //不设置的话会被名字内容的点击事件覆盖，无法触发ItemView的onClick
            registerItemClick(holder.mContent);
            String str = topicComment.getContent();


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
        }
    }
}
