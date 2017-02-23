package com.hengye.share.module.statuscomment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.Status;
import com.hengye.share.model.StatusComment;
import com.hengye.share.model.greenrobot.StatusDraftHelper;
import com.hengye.share.module.publish.StatusPublishActivity;
import com.hengye.share.module.status.StatusPresenter;
import com.hengye.share.module.status.StatusTitleViewHolder;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemLongClickListener;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.ui.support.textspan.StatusUrlOnTouchListener;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.ui.widget.image.AvatarImageView;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.ThemeUtil;

import java.util.List;

public class CommentAdapter extends CommonAdapter<StatusComment>
        implements OnItemClickListener,
        OnItemLongClickListener,
        DialogInterface.OnClickListener {

//    private StatusPresenter mPresenter;
    private StatusComment mLongClickStatus;
    private boolean mIsRetweetedLongClick;
    private RecyclerView mRecyclerView;

    public CommentAdapter(Context context, List<StatusComment> data, RecyclerView recyclerView) {
        super(context, data);
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
                } else if (id == R.id.tv_status_content || id == R.id.gl_status_gallery || id == R.id.layout_status_title || id == R.id.ll_status_content || id == R.id.item_status_retweeted_content) {
                    final boolean isRetweeted = (Boolean) view.getTag();
                }

            }
        }, 150);
    }

    public static class CommentViewHolder extends ItemViewHolder<StatusComment> implements StatusTitleViewHolder.StatusTitle {

        public StatusTitleViewHolder mStatusTitle;
        public CommentContentViewHolder mStatus;
        public View mStatusTotalItem, mStatusItem;

        public CommentViewHolder(View v) {
            super(v);
            mStatusTitle = new StatusTitleViewHolder(v.findViewById(R.id.layout_status_title));
            mStatus = new CommentContentViewHolder(findViewById(R.id.item_comment_repost_content));

            //布尔值，如果false则表示点击的不是转发的微博
            mStatusTitle.mTitle.setTag(false);

            mStatusItem = findViewById(R.id.item_status);
            mStatusTotalItem = findViewById(R.id.item_status_total);

            registerOnClickListener(mStatusTitle.mAvatar);
            registerOnClickListener(mStatusTitle.mUsername);
            registerOnClickListener(mStatusTitle.mDescription);
            registerOnClickListener(mStatusTitle.mTitle);

            registerOnLongClickListener(mStatusItem);

//            registerOnLongClickListener(mRetweetStatus.mContent);
//            registerOnLongClickListener(mRetweetStatus.mGallery);
            //如果其他部位也设置长按会导致发生两次长按

            mStatusTitle.mAvatar.setOnTouchListener(mStatusOnTouchListener);
            mStatusTitle.mUsername.setOnTouchListener(mStatusOnTouchListener);
            mStatusTitle.mDescription.setOnTouchListener(mStatusOnTouchListener);
            mStatusTitle.mTitle.setOnTouchListener(mStatusOnTouchListener);

            SelectorLoader.getInstance().setDefaultRippleWhiteBackground(v);
        }

        @Override
        public void bindData(Context context, StatusComment statusComment, int position) {

            mStatusTitle.initStatusTitle();
        }

        private View.OnTouchListener mStatusOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int id = v.getId();
                if (id == R.id.tv_status_content) {
                    //如果需要拦截长按关键字（比如@名字）则这样返回；
//                    if(!StatusUrlOnTouchListener.getInstance().onTouch(v, event)) {
//                        mStatusTotalItem.onTouchEvent(event);
//                        return false;
//                    }else{
//                        return true;
//                    }

                    boolean result = StatusUrlOnTouchListener.getInstance().onTouch(v, event);
                    if (!result) {
                        mStatusItem.onTouchEvent(event);
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    mStatusItem.onTouchEvent(event);
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
//                    isRetweet ? tvh.mRetweetStatus.mStatusLayout : tvh.mStatusTotalItem,
//                    status,
//                    isRetweet);
//        }

        @Override
        public StatusTitleViewHolder getStatusTitleViewHolder() {
            return mStatusTitle;
        }
    }

    public static class CommentContentViewHolder{

        public TextView retweetedContent, content, username;
        public AvatarImageView cover;
        public View retweetedContentLayout, contentTxtLayout;

        public CommentContentViewHolder(View v) {
            cover = (AvatarImageView) v.findViewById(R.id.iv_status_avatar);
            retweetedContent = (TextView) v.findViewById(R.id.tv_retweeted_content);
            content = (TextView) v.findViewById(R.id.tv_content);
            username = (TextView) v.findViewById(R.id.tv_username);
            retweetedContentLayout = v;
            contentTxtLayout = v.findViewById(R.id.item_comment_content_text);
        }

        public void initCommentContentTitle(final Context context, StatusComment statusComment) {

            Status status = statusComment.getStatus();

            if(status == null){
                return;
            }

            if(status.getRetweetedStatus() != null){
                //有抓发的内容，
                retweetedContent.setVisibility(View.VISIBLE);

                Status retweetedStatus = status.getRetweetedStatus();
                retweetedContent.setText(retweetedStatus.getContent());


                username.setText(retweetedStatus.getUserInfo().getName());
                content.setText(retweetedStatus.getContent());

                SelectorLoader.getInstance().setDefaultRippleBackground(retweetedContentLayout);
                contentTxtLayout.setBackgroundColor(ThemeUtil.getUntingedColor());
            }else{
                retweetedContent.setVisibility(View.GONE);

                username.setText(status.getUserInfo().getName());
                content.setText(status.getContent());

                retweetedContentLayout.setBackgroundResource(0);
                contentTxtLayout.setBackgroundResource(0);
            }


        }
    }

}













