package com.hengye.share.module.topic;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.profile.PersonalHomepageActivity;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.ui.widget.image.AvatarImageView;

/**
 * Created by yuhy on 2016/11/29.
 */
public class TopicTitleViewHolder {

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

//            mTitle.setTag(false);
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
            if (SettingHelper.isShowTopicAvatar()) {
                mAvatar.setImageUrl(userInfo.getAvatar());
            } else {
                mAvatar.setImageResource(R.drawable.ic_user_avatar);
            }
        }
    }

    public static boolean isClickTopicTitle(int id){
        if (id == R.id.iv_topic_avatar || id == R.id.tv_topic_username || id == R.id.tv_topic_description) {
            return true;
        }
        return false;
    }

    public static boolean onClickTopicTitle(Context context, CommonAdapter adapter, View view, int position, UserInfo userInfo) {
        int id = view.getId();
        if (id == R.id.iv_topic_avatar || id == R.id.tv_topic_username || id == R.id.tv_topic_description) {
            View startView = null;
            if (id == R.id.iv_topic_avatar) {
                //如果点击的是头像
                startView = view;
            } else {
                RecyclerView.ViewHolder viewHolder = adapter.findViewHolderForLayoutPosition(position);
                if (viewHolder != null && viewHolder instanceof TopicTitle) {
                    TopicTitle topicTitle = (TopicTitle) viewHolder;
                    startView = topicTitle.getTopicTitleViewHolder().mAvatar;
                }
            }
            PersonalHomepageActivity.start(context, startView, userInfo);
            return true;
        }
        return false;
    }

    /**
     * Created by yuhy on 2016/11/29.
     */
    public static interface TopicTitle {
        TopicTitleViewHolder getTopicTitleViewHolder();
    }
}
