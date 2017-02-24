package com.hengye.share.module.statuscomment;

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
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.Status;
import com.hengye.share.model.StatusComment;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.greenrobot.StatusDraftHelper;
import com.hengye.share.module.publish.StatusPublishActivity;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.module.status.StatusAdapter;
import com.hengye.share.module.status.StatusTitleViewHolder;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemLongClickListener;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.ui.support.textspan.StatusUrlOnTouchListener;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.ui.widget.image.SuperImageView;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.ResUtil;

import java.util.List;

public class CommentAdapter extends CommonAdapter<StatusComment>
        implements OnItemClickListener,
        OnItemLongClickListener,
        DialogInterface.OnClickListener {

    private static int mGalleryMaxWidth;

    //    private StatusPresenter mPresenter;
    private StatusComment mLongClickStatus;
    private boolean mIsRetweetedLongClick;
    private RecyclerView mRecyclerView;

    public CommentAdapter(Context context, List<StatusComment> data, RecyclerView recyclerView) {
        super(context, data);

        mGalleryMaxWidth = context.getResources().getDisplayMetrics().widthPixels / 3 * 2;

        mRecyclerView = recyclerView;
        setOnItemClickListener(this);
        setOnItemLongClickListener(this);
    }

    @Override
    public CommentViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false));
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        StatusComment status = mLongClickStatus;
        if (status == null) {
            return;
        }

        switch (which) {
            case DialogBuilder.LONG_CLICK_TOPIC_REPOST:
                IntentUtil.startActivity(getContext(),
                        StatusPublishActivity.getStartIntent(getContext(), StatusDraftHelper.getWBStatusDraftByRepostRepost(status)));
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_COMMENT:
                IntentUtil.startActivity(getContext(),
                        StatusPublishActivity.getStartIntent(getContext(), StatusDraftHelper.getWBStatusDraftByRepostComment(status)));
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_COPY:
                ClipboardUtil.copyWBContent(status.getContent());
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onItemLongClick(View view, int position) {
        boolean isRetweeted = false;
        if (view.getTag() != null && view.getTag() instanceof Boolean) {
            isRetweeted = (Boolean) view.getTag();
        }

        mLongClickStatus = getItem(position);
        mIsRetweetedLongClick = isRetweeted;

        if (mLongClickStatus != null) {
            if (mIsRetweetedLongClick) {
//                if (mLongClickStatus.getRetweetedStatus() != null) {
//                    mLongClickStatus = mLongClickStatus.getRetweetedStatus();
//                }
            }
//            getLongClickDialog().show();
        }

        return true;
    }

//    private Dialog getLongClickDialog() {
////        boolean showDeleteStatusOption = mShowDeleteStatusOption;
////        boolean isMine = false;
////        if(showDeleteStatusOption && !mIsRetweetedLongClick){
////            isMine = mLongClickStatus != null && mLongClickStatus.getUserInfo() != null && UserUtil.isCurrentUser(mLongClickStatus.getUserInfo().getUid());
////        }
//        return DialogBuilder.getOnLongClickStatusDialog(getContext(), this, mLongClickStatus, true);
//    }

    private Handler mHandler = new Handler();

    @Override
    public void onItemClick(final View view, final int position) {
        final int id = view.getId();

        //为了显示波纹效果再启动
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (StatusTitleViewHolder.isClickStatusTitle(id)) {
                    StatusTitleViewHolder.onClickStatusTitle(getContext(), CommentAdapter.this, view, position, getItem(position).getUserInfo());
                } else if (id == R.id.tv_status_content || id == R.id.gl_status_gallery || id == R.id.layout_status_title || id == R.id.layout_status_content || id == R.id.item_status_retweeted_content) {
                    final boolean isRetweeted = (Boolean) view.getTag();
                }

            }
        }, 150);
    }

    public static class CommentViewHolder extends ItemViewHolder<StatusComment> implements StatusTitleViewHolder.StatusTitle {

        public CommentTitleViewHolder mCommentTitle;
        public StatusAdapter.StatusContentViewHolder mStatus;//评论内容以及评论图片
        public CommentContentViewHolder mComment;
        public View mStatusTotalItem;
        private boolean mShowCommentPhoto;

        public CommentViewHolder(View v) {
            super(v);
            mCommentTitle = new CommentTitleViewHolder(v.findViewById(R.id.layout_status_title));
            mStatus = new StatusAdapter.StatusContentViewHolder(findViewById(R.id.layout_status_content));
            mComment = new CommentContentViewHolder(findViewById(R.id.item_comment_repost_content));
            mStatusTotalItem = v;

            mShowCommentPhoto = SettingHelper.isShowCommentPhoto();
            mStatus.mContent.setTextSize(ResUtil.getDimenFloatValue(R.dimen.text_small));
            mStatus.mGallery.setMaxWidth(mGalleryMaxWidth);
            //布尔值，如果false则表示点击的不是转发的微博
            mCommentTitle.mTitle.setTag(false);

//            mStatusItem = findViewById(R.id.item_status);
//            mCommentTotalItem = findViewById(R.id.item_status_total);

            registerOnClickListener(mCommentTitle.mAvatar);
            registerOnClickListener(mCommentTitle.mUsername);
            registerOnClickListener(mCommentTitle.mDescription);
            registerOnClickListener(mCommentTitle.mTitle);


            registerOnClickListener(mStatus.mContent);
            registerOnClickListener(mStatus.mGallery);
            registerOnClickListener(mStatus.mStatusLayout);
            //如果其他部位也设置长按会导致发生两次长按

            mCommentTitle.mAvatar.setOnTouchListener(mStatusOnTouchListener);
            mCommentTitle.mUsername.setOnTouchListener(mStatusOnTouchListener);
            mCommentTitle.mDescription.setOnTouchListener(mStatusOnTouchListener);
            mCommentTitle.mTitle.setOnTouchListener(mStatusOnTouchListener);
            mStatus.mContent.setOnTouchListener(mStatusOnTouchListener);
            mStatus.mGallery.setOnTouchListener(mStatusOnTouchListener);

            mComment.retweetedContent.setOnTouchListener(mStatusOnTouchListener);
            mComment.content.setOnTouchListener(mStatusOnTouchListener);
            SelectorLoader.getInstance().setDefaultRippleWhiteBackground(v);
        }

        @Override
        public void bindData(Context context, StatusComment statusComment, int position) {

            mCommentTitle.initCommentTitle(context, statusComment);
            mStatus.mContent.setText(statusComment.getSpanned(mStatus.mContent));
            if(mShowCommentPhoto) {
                mStatus.initStatusGallery(statusComment.getImageUrls());
            }
            mComment.initCommentContent(context, statusComment);
        }

        private View.OnTouchListener mStatusOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int id = v.getId();
                if (id == R.id.tv_status_content ||
                        id == R.id.tv_retweeted_content ||
                        id == R.id.tv_content) {
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

//        public static void startStatusDetail(Context context, StatusViewHolder tvh, boolean isRetweet, Status status) {
//            if (status == null || tvh == null) {
//                return;
//            }
//
//            StatusDetailActivity.start(context,
//                    isRetweet ? tvh.mRetweetStatus.mStatusLayout : tvh.mCommentTotalItem,
//                    status,
//                    isRetweet);
//        }

        @Override
        public StatusTitleViewHolder getStatusTitleViewHolder() {
            return mCommentTitle;
        }
    }

    public static class CommentContentViewHolder {

        public TextView retweetedContent, content, username;
        public SuperImageView cover;
        public View retweetedContentLayout, contentTxtLayout;

        public CommentContentViewHolder(View v) {
            cover = (SuperImageView) v.findViewById(R.id.iv_cover);
            cover.setDefaultImageResId(R.drawable.ic_user_avatar);
            retweetedContent = (TextView) v.findViewById(R.id.tv_retweeted_content);
            content = (TextView) v.findViewById(R.id.tv_content);
            username = (TextView) v.findViewById(R.id.tv_username);
            retweetedContentLayout = v;
            contentTxtLayout = v.findViewById(R.id.item_comment_content_text);
        }

        public void initCommentContent(final Context context, StatusComment statusComment) {

            cover.setImageUrl(statusComment.getCoverUrl());
            if (statusComment.getReplyComment() != null) {
                //属于回复别人的评论的评论，
                retweetedContent.setVisibility(View.VISIBLE);

                StatusComment replyComment = statusComment.getReplyComment();
                retweetedContent.setText(replyComment.getSpanned(retweetedContent, true));

                retweetedContentLayout.setBackgroundResource(R.color.background_default);
                contentTxtLayout.setBackgroundResource(R.color.background_default_white);
            } else {
                retweetedContent.setVisibility(View.GONE);

                retweetedContentLayout.setBackgroundResource(0);
                contentTxtLayout.setBackgroundResource(R.color.background_default);
            }

            Status status = statusComment.getStatus();
            username.setText(status.getUserInfo().getAtName());
            content.setText(status.getSpanned(content, false, false));
        }
    }

    public static class CommentTitleViewHolder extends StatusTitleViewHolder {

        public CommentTitleViewHolder(View v) {
            super(v);
        }

        public void initCommentTitle(final Context context, StatusComment topicComment) {
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
        }
    }

}













