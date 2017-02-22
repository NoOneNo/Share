package com.hengye.share.module.statusdetail;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.StatusComments;
import com.hengye.share.model.UserInfo;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.module.status.StatusTitleViewHolder;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.model.StatusComment;
import com.hengye.share.module.status.StatusAdapter;
import com.hengye.share.module.util.image.GalleryActivity;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.ui.support.textspan.StatusUrlOnTouchListener;
import com.hengye.share.ui.widget.image.GridGalleryView;
import com.hengye.share.ui.widget.image.SuperImageView;
import com.hengye.share.ui.widget.util.DrawableLoader;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.ThemeUtil;

import java.util.ArrayList;
import java.util.List;

public class StatusCommentAdapter extends CommonAdapter<StatusComment> {

    private static int mGalleryMaxWidth;

    private boolean mIsLikeMode;//是否支持点赞

    public StatusCommentAdapter(Context context, List<StatusComment> data, boolean isLikeMode) {
        super(context, data);
        this.mIsLikeMode = isLikeMode;

        mGalleryMaxWidth = context.getResources().getDisplayMetrics().widthPixels / 3 * 2;
    }

    @Override
    public int getBasicItemType(int position) {
        StatusComment statusComment = getItem(position);
        if (StatusComments.getStatusHotCommentLabel() == statusComment) {
            return R.layout.item_status_comment_hot_label;
        } else {
            return R.layout.item_status_comment;
        }
    }

    @Override
    public ItemViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.layout.item_status_comment_hot_label) {
            return new ItemViewHolder(inflate(R.layout.item_status_comment_hot_label, parent));
        } else {
            return new StatusCommentViewHolder(inflate(R.layout.item_status_comment, parent), mIsLikeMode);
        }
    }


    public static class StatusCommentViewHolder extends ItemViewHolder<StatusComment> implements StatusTitleViewHolder.StatusTitle {

        public StatusAdapter.StatusContentViewHolder mStatus;
        public StatusCommentTitleViewHolder mStatusTitle;
        public View mStatusTotalItem;
        private boolean mShowCommentPhoto;

        public StatusCommentViewHolder(View v, boolean isLikeMode) {
            super(v);
            mStatusTitle = new StatusCommentTitleViewHolder(v, isLikeMode);
            mStatus = new StatusAdapter.StatusContentViewHolder(findViewById(R.id.ll_status_content));
            mStatusTotalItem = findViewById(R.id.item_status_total);

            mShowCommentPhoto = SettingHelper.isShowCommentPhoto();
            mStatus.mGallery.setMaxWidth(mGalleryMaxWidth);

            //不设置的话会被名字内容的点击事件覆盖，无法触发ItemView的onClick
            registerOnClickListener(mStatusTitle.mAttitudeLayout);
            registerOnClickListener(mStatusTitle.mAvatar);
            registerOnClickListener(mStatusTitle.mUsername);
            registerOnClickListener(mStatusTitle.mDescription);
            registerOnClickListener(mStatusTitle.mTitle);

            registerOnClickListener(mStatus.mContent);
            registerOnClickListener(mStatus.mGallery);
            registerOnClickListener(mStatus.mStatusLayout);

            //如果其他部位也设置长按会导致发生两次长按
            registerOnLongClickListener(mStatusTotalItem);

            SelectorLoader.getInstance().setDefaultRippleBackground(mStatusTotalItem);

            //扩大点赞按钮触摸区域
            mStatusTitle.mAttitudeAssist.setOnTouchListener(mOnTouchLikeAssistListener);

            mStatusTitle.mAvatar.setOnTouchListener(mStatusOnTouchListener);
            mStatusTitle.mUsername.setOnTouchListener(mStatusOnTouchListener);
            mStatusTitle.mDescription.setOnTouchListener(mStatusOnTouchListener);
            mStatusTitle.mTitle.setOnTouchListener(mStatusOnTouchListener);
            mStatus.mContent.setOnTouchListener(mStatusOnTouchListener);
            mStatus.mGallery.setOnTouchListener(mStatusOnTouchListener);
        }

        private View.OnTouchListener mStatusOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int id = v.getId();
                if (id == R.id.tv_status_content) {
                    boolean result = StatusUrlOnTouchListener.getInstance().onTouch(v, event);
                    if (!result) {
                        mStatusTotalItem.onTouchEvent(event);
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    mStatusTotalItem.onTouchEvent(event);
                    return false;
                }
            }
        };

        private View.OnTouchListener mOnTouchLikeAssistListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mStatusTitle.mAttitudeLayout.onTouchEvent(event);
                return true;
//                return mStatusTitle.mAttitudeLayout.onTouchEvent(event);
            }
        };

        @Override
        public void bindData(Context context, StatusComment statusComment, int position) {
            mStatusTitle.initStatusTitle(context, statusComment);
            initCommentContent(context, this, statusComment);
        }

        public void initCommentContent(final Context context, final StatusCommentViewHolder holder, StatusComment statusComment) {
            holder.mStatus.mContent.setText(statusComment.getSpanned(holder.mStatus.mContent));
            if(mShowCommentPhoto) {
                holder.mStatus.initStatusGallery(statusComment.getImageUrls());
            }
        }

        @Override
        public StatusTitleViewHolder getStatusTitleViewHolder() {
            return mStatusTitle;
        }
    }

    public static class StatusCommentTitleViewHolder extends StatusTitleViewHolder {

        public View mAttitudeLayout, mAttitudeAssist;
        public ImageButton mAttitudeBtn;
        public TextView mAttitudeCount;
        public boolean mIsLikeMode;

        public StatusCommentTitleViewHolder(View v, boolean isLikeMode) {
            super(v);
            mIsLikeMode = isLikeMode;
            mAttitudeAssist = v.findViewById(R.id.layout_attitude_assist);
            mAttitudeLayout = v.findViewById(R.id.layout_attitude);
            mAttitudeBtn = (ImageButton) v.findViewById(R.id.btn_like);
            mAttitudeCount = (TextView) v.findViewById(R.id.tv_attitude_count);

            if (!isLikeMode) {
                mAttitudeLayout.setVisibility(View.GONE);
            }
        }

        public void initStatusTitle(final Context context, StatusComment topicComment) {
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
                int color = topicComment.isLiked() ? ThemeUtil.getColor() : ResUtil.getColor(R.color.grey_850);
                mAttitudeBtn.setImageDrawable(DrawableLoader.setTintDrawable(R.drawable.ic_thumb_up_white_48dp, color).mutate());
                mAttitudeCount.setText(DataUtil.getCounter(topicComment.getLikeCounts()));

                mAttitudeCount.setVisibility(topicComment.getLikeCounts() < 1 ? View.INVISIBLE : View.VISIBLE);
            }
        }
    }
}
