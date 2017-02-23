package com.hengye.share.module.status;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.Status;
import com.hengye.share.model.greenrobot.StatusDraftHelper;
import com.hengye.share.module.publish.StatusPublishActivity;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.module.statusdetail.StatusDetailActivity;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemClickListener;
import com.hengye.share.module.util.encapsulation.view.listener.OnItemLongClickListener;
import com.hengye.share.module.util.encapsulation.view.recyclerview.CommonAdapter;
import com.hengye.share.module.util.encapsulation.view.recyclerview.ItemViewHolder;
import com.hengye.share.module.util.image.GalleryActivity;
import com.hengye.share.ui.support.AnimationRect;
import com.hengye.share.ui.support.textspan.StatusUrlOnTouchListener;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.ui.widget.image.GridGalleryView;
import com.hengye.share.ui.widget.image.SuperImageView;
import com.hengye.share.ui.widget.util.DrawableLoader;
import com.hengye.share.ui.widget.util.SelectorLoader;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.IntentUtil;
import com.hengye.share.util.ThemeUtil;
import com.hengye.share.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

public class StatusAdapter extends CommonAdapter<Status>
        implements OnItemClickListener,
        OnItemLongClickListener,
        DialogInterface.OnClickListener {

    private static int mGalleryMaxWidth;
    //    private Callback mCallback;
    private StatusPresenter mPresenter;
    private StatusActionContract.Presenter mStatusActionPresenter;
    private boolean mShowDeleteStatusOption;
    private Status mLongClickStatus;
    private boolean mIsRetweetedLongClick;
    private RecyclerView mRecyclerView;

    public StatusAdapter(Context context, List<Status> data, RecyclerView recyclerView) {
        super(context, data);
        mRecyclerView = recyclerView;

        int galleryMargin = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        mGalleryMaxWidth = context.getResources().getDisplayMetrics().widthPixels - 2 * galleryMargin;

        setOnItemClickListener(this);
        setOnItemLongClickListener(this);

//        if (!BuildConfig.DEBUG) {
//            setCheckDiffMode(true);
//        }
    }

    @Override
    public StatusDefaultViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        return new StatusDefaultViewHolder(inflate(R.layout.item_status_total, parent));
    }


    @Override
    public void onClick(DialogInterface dialog, int which) {
        Status status = mLongClickStatus;
        if (status == null) {
            return;
        }

        switch (which) {
            case DialogBuilder.LONG_CLICK_TOPIC_REPOST:
                repostStatus(status);
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_COMMENT:
                commentStatus(status);
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_LIKE:
                likeStatus(status);
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_FAVORITE:
                collectStatus(status);
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_COPY:
                copyStatus(status);
                break;
            case DialogBuilder.LONG_CLICK_TOPIC_DESTROY:
                destroyStatus(status);
            default:
                break;
        }
    }

    public void setShowDeleteStatusOption(boolean show) {
        mShowDeleteStatusOption = show;
    }

    public void setStatusPresenter(StatusPresenter statusPresenter) {
        mPresenter = statusPresenter;
    }

    public void setStatusActionPresenter(StatusActionContract.Presenter statusActionPresenter) {
        mStatusActionPresenter = statusActionPresenter;
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
                if (mLongClickStatus.getRetweetedStatus() != null) {
                    mLongClickStatus = mLongClickStatus.getRetweetedStatus();
                }
            }
            getLongClickDialog().show();
        }

        return true;
    }

    private Dialog getLongClickDialog() {
        boolean showDeleteStatusOption = mShowDeleteStatusOption;
        boolean isMine = false;
        if (showDeleteStatusOption && !mIsRetweetedLongClick) {
            isMine = mLongClickStatus != null && mLongClickStatus.getUserInfo() != null && UserUtil.isCurrentUser(mLongClickStatus.getUserInfo().getUid());
        }
        return DialogBuilder.getOnLongClickStatusDialog(getContext(), this, mLongClickStatus, isMine);
    }

    private Handler mHandler = new Handler();

    @Override
    public void onItemClick(final View view, final int position) {
        final int id = view.getId();

        //为了显示波纹效果再启动
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (StatusTitleViewHolder.isClickStatusTitle(id)) {
                    StatusTitleViewHolder.onClickStatusTitle(getContext(), StatusAdapter.this, view, position, getItem(position).getUserInfo());
                } else if (id == R.id.tv_status_content ||
                        id == R.id.gl_status_gallery ||
                        id == R.id.layout_status_title ||
                        id == R.id.ll_status_content ||
                        id == R.id.item_status_retweeted_content ||
                        id == R.id.layout_status_options) {
                    final boolean isRetweeted = (Boolean) view.getTag();
                    startStatusDetail(isRetweeted, position);
                } else if (id == R.id.layout_repost) {
                    repostStatus(getItem(position));
                } else if (id == R.id.layout_comment) {
                    commentStatus(getItem(position));
                } else if (id == R.id.layout_attitude) {
                    likeStatus(getItem(position));
                }

            }
        }, 150);
    }

    private void repostStatus(Status status) {
        IntentUtil.startActivity(getContext(),
                StatusPublishActivity.getStartIntent(getContext(), StatusDraftHelper.getWBStatusDraftByRepostRepost(status)));
    }

    private void commentStatus(Status status) {
        IntentUtil.startActivity(getContext(),
                StatusPublishActivity.getStartIntent(getContext(), StatusDraftHelper.getWBStatusDraftByRepostComment(status)));
    }

    private void likeStatus(Status status) {
        if (mStatusActionPresenter != null) {
            mStatusActionPresenter.likeStatus(status);
        }
    }

    private void collectStatus(Status status) {
        if (mStatusActionPresenter != null) {
            mStatusActionPresenter.collectStatus(status);
        }
    }

    private void copyStatus(Status status) {
        ClipboardUtil.copyWBContent(status.getContent());
    }

    private void destroyStatus(Status status) {
        if (mPresenter != null) {
            mPresenter.deleteStatus(status);
        }
    }

    private void startStatusDetail(boolean isRetweet, int position) {
        Status status = isRetweet ? getItem(position).getRetweetedStatus() : getItem(position);
        StatusDefaultViewHolder vh = (StatusDefaultViewHolder) findViewHolderForLayoutPosition(position);
        if (vh != null) {
            StatusViewHolder.startStatusDetail(getContext(), vh, isRetweet, status);
        }
    }

    public static class StatusDefaultViewHolder extends StatusViewHolder<Status> {

        public StatusDefaultViewHolder(View v) {
            super(v);
        }

        @Override
        public void bindData(Context context, Status status, int position) {
            if (status == null) {
                return;
            }

            mStatusTitle.initStatusTitle(context, status);
            mStatus.initStatusContent(context, status, false);

            if (status.getRetweetedStatus() != null) {
                mRetweetStatus.mStatusLayout.setVisibility(View.VISIBLE);
                mRetweetStatus.initStatusContent(context, status.getRetweetedStatus(), true);
            } else {
                mRetweetStatus.mStatusLayout.setVisibility(View.GONE);
            }

            if (mStatusOptions != null) {
                mStatusOptions.initStatusOptions(context, status);
            }
        }
    }

    public static class StatusViewHolder<T> extends ItemViewHolder<T> implements StatusTitleViewHolder.StatusTitle {

        public StatusTitleViewHolder mStatusTitle;
        public StatusContentViewHolder mStatus, mRetweetStatus;
        public StatusOptionsViewHolder mStatusOptions;
        public View mStatusTotalItem, mStatusItem;

        public StatusViewHolder(View v) {
            super(v);
            mStatusTitle = new StatusTitleViewHolder(v);
            mStatus = new StatusContentViewHolder(findViewById(R.id.ll_status_content));
            mRetweetStatus = new StatusContentViewHolder(findViewById(R.id.item_status_retweeted_content), true);

            //布尔值，如果false则表示点击的不是转发的微博
            mStatusTitle.mTitle.setTag(false);

            mStatusItem = findViewById(R.id.item_status);
            mStatusTotalItem = findViewById(R.id.item_status_total);

            View statusOptions = findViewById(R.id.layout_status_options);
            if (statusOptions != null) {
                mStatusOptions = new StatusOptionsViewHolder(statusOptions);
                registerOnClickListener(mStatusOptions.mRepostCount);
                registerOnClickListener(mStatusOptions.mCommentCount);
                registerOnClickListener(mStatusOptions.mAttitudeCount);
                statusOptions.setTag(false);
                registerOnClickListener(statusOptions);
                statusOptions.setOnTouchListener(mStatusOnTouchListener);
            }

            registerOnClickListener(mStatusTitle.mAvatar);
            registerOnClickListener(mStatusTitle.mUsername);
            registerOnClickListener(mStatusTitle.mDescription);
            registerOnClickListener(mStatusTitle.mTitle);

            registerOnClickListener(mStatus.mContent);
            registerOnClickListener(mStatus.mGallery);
            registerOnClickListener(mStatus.mStatusLayout);

            registerOnClickListener(mRetweetStatus.mContent);
            registerOnClickListener(mRetweetStatus.mGallery);
            registerOnClickListener(mRetweetStatus.mStatusLayout);

            //不设置长按没法解决点击效果
            //如果设置多个点击事件，则会造成重复点击；
//            registerOnLongClickListener(mStatusTitle.mTitle);
//            registerOnLongClickListener(mStatus.mContent);
//            registerOnLongClickListener(mStatus.mGallery);
//            registerOnLongClickListener(mStatus.mStatusLayout);

            //如果其他部位也设置长按会导致发生两次长按
            registerOnLongClickListener(mStatusItem);

//            registerOnLongClickListener(mRetweetStatus.mContent);
//            registerOnLongClickListener(mRetweetStatus.mGallery);
            //如果其他部位也设置长按会导致发生两次长按
            registerOnLongClickListener(mRetweetStatus.mStatusLayout);

            mStatusTitle.mAvatar.setOnTouchListener(mStatusOnTouchListener);
            mStatusTitle.mUsername.setOnTouchListener(mStatusOnTouchListener);
            mStatusTitle.mDescription.setOnTouchListener(mStatusOnTouchListener);
            mStatusTitle.mTitle.setOnTouchListener(mStatusOnTouchListener);
            mStatus.mContent.setOnTouchListener(mStatusOnTouchListener);
            mStatus.mGallery.setOnTouchListener(mStatusOnTouchListener);

            mRetweetStatus.mContent.setOnTouchListener(mRetweetedStatusOnTouchListener);
            mRetweetStatus.mGallery.setOnTouchListener(mRetweetedStatusOnTouchListener);

//            SelectorLoader.getInstance().setDefaultRippleBackground(mStatusItem);
//            SelectorLoader.getInstance().setDefaultRippleWhiteBackground(mRetweetStatus.mStatusLayout);

            SelectorLoader.getInstance().setDefaultRippleBackground(mRetweetStatus.mStatusLayout);
            SelectorLoader.getInstance().setDefaultRippleWhiteBackground(mStatusTotalItem);
//            SelectorLoader.getInstance().setDefaultRippleWhiteBackground(mStatusItem);
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

        private View.OnTouchListener mRetweetedStatusOnTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int id = v.getId();
                if (id == R.id.tv_status_content) {
                    boolean result = StatusUrlOnTouchListener.getInstance().onTouch(v, event);
                    if (!result) {
                        mRetweetStatus.mStatusLayout.onTouchEvent(event);
                        return false;
                    } else {
                        return true;
                    }
                } else {
                    mRetweetStatus.mStatusLayout.onTouchEvent(event);
                    return false;
                }
            }
        };

        public static void startStatusDetail(Context context, StatusViewHolder tvh, boolean isRetweet, Status status) {
            if (status == null || tvh == null) {
                return;
            }

            StatusDetailActivity.start(context,
                    isRetweet ? tvh.mRetweetStatus.mStatusLayout : tvh.mStatusTotalItem,
                    status,
                    isRetweet);
        }

        @Override
        public StatusTitleViewHolder getStatusTitleViewHolder() {
            return mStatusTitle;
        }
    }

    public static class StatusContentViewHolder {
        public TextView mContent;
        public GridGalleryView mGallery;
        public View mStatusLayout;

        public StatusContentViewHolder(View parent) {
            this(parent, false);
        }

        public StatusContentViewHolder(View parent, boolean isRetweeted) {
            mContent = (TextView) parent.findViewById(R.id.tv_status_content);
            mGallery = (GridGalleryView) parent.findViewById(R.id.gl_status_gallery);
            mStatusLayout = parent;

            mContent.setTag(isRetweeted);
            mGallery.setTag(isRetweeted);
            mStatusLayout.setTag(isRetweeted);

            initGallery();
        }

        private void initGallery() {
            final Context context = mGallery.getContext();
            mGallery.setMargin(context.getResources().getDimensionPixelSize(R.dimen.status_gallery_iv_margin));
            mGallery.setMaxWidth(mGalleryMaxWidth);
            mGallery.setHandleData(new GridGalleryView.HandleData() {
                @Override
                public ImageView getImageView() {
                    SuperImageView iv = new SuperImageView(context);
                    iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    iv.setBackgroundColor(context.getResources().getColor(R.color.image_default_bg));
                    iv.setTag(mGallery);
                    iv.setId(View.NO_ID);
                    return iv;
                }

                @Override
                public void handleChildItem(ImageView imageView, int position) {
                    ArrayList<String> urls = (ArrayList<String>) mGallery.getTag(View.NO_ID);
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
                        //noinspection unchecked
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
                        GalleryActivity
                                .startWithIntent(context, urls, position, animationRectArrayList);
                    }
                }
            });
        }

        public void initStatusContent(final Context context, Status status, boolean isRetweeted) {

            //不设置的话会被名字内容的点击事件覆盖，无法触发ItemView的onClick
            mContent.setText(status.getSpanned(mContent, isRetweeted));

//            mContent.setMovementMethod(SimpleLinkMovementMethod.getInstance());
//            mContent.setOnTouchListener(StatusUrlOnTouchListener.getInstance());

            initStatusGallery(status.getImageUrls());
        }

        public void initStatusGallery(List<String> urls) {
            if (!CommonUtil.isEmpty(urls)) {
                //加载图片
                mGallery.removeAllViews();
                mGallery.setTag(View.NO_ID, urls);
                mGallery.setGridCount(urls.size());
                mGallery.reset();
                mGallery.setVisibility(View.VISIBLE);
            } else{
                mGallery.setVisibility(View.GONE);
            }
        }

    }

    public static class StatusOptionsViewHolder {

        public View mStatusOptionsLayout;
        public View mRepostCount, mCommentCount, mAttitudeCount;
        public TextView mRepostCountTxt, mCommentCountTxt, mAttitudeCountTxt;
        public ImageView mRepostIcon, mCommentIcon, mAttitudeIcon;

        public StatusOptionsViewHolder(View v) {
            mStatusOptionsLayout = v;

            mRepostCount = v.findViewById(R.id.layout_repost);
            mCommentCount = v.findViewById(R.id.layout_comment);
            mAttitudeCount = v.findViewById(R.id.layout_attitude);
            SelectorLoader.getInstance().setTransparentRippleBackground(mRepostCount);
            SelectorLoader.getInstance().setTransparentRippleBackground(mCommentCount);
            SelectorLoader.getInstance().setTransparentRippleBackground(mAttitudeCount);
            mRepostCountTxt = (TextView) v.findViewById(R.id.tv_repost_count);
            mCommentCountTxt = (TextView) v.findViewById(R.id.tv_comment_count);
            mAttitudeCountTxt = (TextView) v.findViewById(R.id.tv_attitude_count);
            mRepostIcon = (ImageView) v.findViewById(R.id.iv_repost);
            mCommentIcon = (ImageView) v.findViewById(R.id.iv_comment);
            mAttitudeIcon = (ImageView) v.findViewById(R.id.iv_attitude);

            mRepostIcon.setImageDrawable(
                    DrawableLoader.setTintResource(R.drawable.ic_repost_white_48dp,
                            R.color.icon_grey));

            mCommentIcon.setImageDrawable(
                    DrawableLoader.setTintResource(R.drawable.ic_comment_white_48dp,
                            R.color.icon_grey));
        }

        public void initStatusOptions(final Context context, Status status) {

            if (!SettingHelper.isShowStatusOptions()) {
                mStatusOptionsLayout.setVisibility(View.GONE);
                return;
            } else {
                mStatusOptionsLayout.setVisibility(View.VISIBLE);
            }

            mAttitudeIcon.setImageDrawable(
                    DrawableLoader.setTintDrawable(R.drawable.ic_thumb_up_white_48dp,
                            status.isLiked() ? ThemeUtil.getColor() : ThemeUtil.getIconTintColor()));

            if (status.getRepostsCount() <= 0) {
                mRepostCountTxt.setText(R.string.label_status_repost);
            } else {
                mRepostCountTxt.setText(DataUtil.getCounter(status.getRepostsCount()));
            }

            if (status.getCommentsCount() <= 0) {
                mCommentCountTxt.setText(R.string.label_status_comment);
            } else {
                mCommentCountTxt.setText(DataUtil.getCounter(status.getCommentsCount()));
            }

            if (status.getAttitudesCount() <= 0) {
                mAttitudeCountTxt.setText(R.string.label_status_attitude);
            } else {
                mAttitudeCountTxt.setText(DataUtil.getCounter(status.getAttitudesCount()));
            }
        }

    }
}













