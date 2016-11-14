package com.hengye.share.ui.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;

import com.hengye.share.R;
import com.hengye.share.util.ViewUtil;

/**
 * Created by yuhy on 16/7/6.
 */
public class PersonalHomePageToolbarLayout extends CollapsingToolbarLayout {

    public PersonalHomePageToolbarLayout(Context context) {
        this(context, null);
    }

    public PersonalHomePageToolbarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PersonalHomePageToolbarLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        if (isInEditMode()) {
            return;
        }
        avatarSize = ViewUtil.dp2px(R.dimen.header_personal_expanded_avatar);
        finalAvatarSize = ViewUtil.dp2px(R.dimen.header_personal_collapsed_avatar);
        avatarFinalMarginLeft = ViewUtil.dp2px(R.dimen.spacing_double);

//        avatarBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_user_avatar);
    }

    /**
     * Set whether the content scrim and/or status bar scrim should be shown or not. Any change
     * in the vertical scroll may overwrite this value. Any visibility change will be animated if
     * this view has already been laid out.
     *
     * @param shown whether the scrims should be shown
     * @see #getStatusBarScrim()
     * @see #getContentScrim()
     */
    boolean mScrimsShownFlag = true;

    public void setScrimsShown(boolean shown) {
        super.setScrimsShown(shown);
        if (mScrimsShownFlag == shown) {
            return;
        } else {
            mScrimsShownFlag = shown;
        }
        if (getContext() instanceof Activity) {
            if (mScrimsShownFlag) {
                ((Activity) getContext()).getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            } else {
                ((Activity) getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
    }

    /**
     * @return 如果是闭合的，则返回true，如果是展开的，则返回false；
     */
    public boolean isShowScrims(){
        return mScrimsShownFlag;
    }

    AppBarLayout appBarLayout;
    Toolbar toolbar;
    View avatar, userInfoBgLayout, userInfoLayout;
    boolean onCoverSet;
    int mAppBarHeight;
    Matrix avatarMatrix;
    int maxVerticalOffset;
    int verticalOffset;
    Bitmap avatarBitmap;

    private int avatarSize;// 头像尺寸
    private int finalAvatarSize;// 缩小后的头像尺寸
    private int avatarMarginLeft;// 头像左侧Margin
    private int avatarFinalMarginLeft;// 头像缩小后的Margin

//    private Bitmap layNameBitmap;// 包括名字、性别、认证三个要素的截图
//    private Matrix layNameMatrix;
//    private int layNameMarginTop;// 名字顶部的margin
//    private int layNameMarginLeft;
//    private int finalLayNameSize;

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Add an OnOffsetChangedListener if possible
        final ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            ((AppBarLayout) parent).addOnOffsetChangedListener(innerOffsetChangedListener);

//            layDetail = findViewById(R.id.layDetail);
//            layRealDetail = findViewById(R.id.layRealDetail);
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            appBarLayout = (AppBarLayout) parent;
            avatar = findViewById(R.id.iv_avatar);
            userInfoBgLayout = findViewById(R.id.fl_user_info);
            userInfoLayout = findViewById(R.id.ll_user_desc);
//            userInfoBgLayout = findViewById(R.id.fl_user_info_bg);
//            collapsingToolbarLayout = this;
//            imgCover = (ImageView) findViewById(R.id.imgCover);
//            tabLayout = (TabLayout) appBarLayout.findViewById(R.id.tabLayout);
//            layName = findViewById(R.id.layName);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // Remove our OnOffsetChangedListener if possible and it exists
        final ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            ((AppBarLayout) parent).removeOnOffsetChangedListener(innerOffsetChangedListener);
        }

        super.onDetachedFromWindow();
    }

    public void setAvatarBitmap(Bitmap bitmap) {
        avatarBitmap = bitmap;
        innerOffsetChangedListener.onOffsetChanged(appBarLayout, verticalOffset);
        invalidate();
    }

    private int getToolbarHeight(){
        return toolbar == null ? 0 : toolbar.getHeight();
    }

    private void maybeSetup() {
        if (appBarLayout == null) {
            return;
        }

        // 将Cover的高度重新Measure一次
//        if (!onCoverSet && appBarLayout.getHeight() > 0) {
//            onCoverSet = true;
//
//            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) imgCover.getLayoutParams();
//            lp.height = layDetail.getHeight() + coverHeight;
//            imgCover.setLayoutParams(lp);
//            imgCover.setPadding(imgCover.getPaddingLeft(), imgCover.getPaddingTop(), imgCover.getPaddingRight(), layDetail.getHeight());
//        }

        if (mAppBarHeight != appBarLayout.getHeight()) {
            mAppBarHeight = appBarLayout.getHeight();

            maxVerticalOffset = getHeight() - (ViewUtil.getStatusBarHeight() + getToolbarHeight());
            // 计算Detail的layout_collapseParallaxMultiplier，使其收起来时刚好高度为ToolBar的高度
            CollapsingToolbarLayout.LayoutParams params = (CollapsingToolbarLayout.LayoutParams) userInfoBgLayout.getLayoutParams();
            // 最大移动的距离
            // 计算移动后的top减去移动前的top就是需要offset，再用offset计算出multiplier
            float multiplier = ((maxVerticalOffset + ViewUtil.getStatusBarHeight()) -
                    (getHeight() - userInfoBgLayout.getHeight())) * 1.0f / maxVerticalOffset;
            if (params.getParallaxMultiplier() != multiplier) {
                params.setParallaxMultiplier(multiplier);
            }

            avatarMatrix = new Matrix();

//            setNameBitmap();
        }
    }

    private AppBarLayout.OnOffsetChangedListener innerOffsetChangedListener = new AppBarLayout.OnOffsetChangedListener() {

        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            maybeSetup();

//            if (layNameBitmap == null) {
//                setNameBitmap();
//            }
            if (avatarBitmap == null) {
                return;
            }

            PersonalHomePageToolbarLayout.this.verticalOffset = verticalOffset;

            // offset移动的比例
            float factor = -verticalOffset * 1.0f / maxVerticalOffset;

            if (userInfoBgLayout.getBackground() != null) {
                float bgAlpha = (float) (0.8 + 0.2 * factor);
//                userInfoBgLayout.getBackground().setAlpha((int) (255 * bgAlpha));
            }

            float descAlpha = 1 - factor;
            userInfoLayout.setAlpha(descAlpha);

            userInfoLayout.setVisibility(descAlpha == 0 ? INVISIBLE : VISIBLE);

            if (avatarMatrix != null) {
                // 从avatarSize变化到finalAvatarSize，是根据这个factor逐渐变化的，计算它的dsize变化值
                float dsize = (avatarSize - finalAvatarSize) * factor;
                // 计算现在需要显示的avatar尺寸
                float avatarToSize = avatarSize - dsize;
                // 缩放
                float scale = avatarToSize * 1.0f / avatarBitmap.getWidth();
                avatarMatrix.setScale(scale, scale);

                // 初始化Top
//                float startAvatarTop = getHeight() - layDetail.getHeight() - avatarToSize * 3.0f / 4;
                float startAvatarTop = userInfoBgLayout.getTop() - avatar.getHeight() / 2;
                avatarMarginLeft = avatar.getLeft();
                // 最终显示的Top
                float toAvatarTop = maxVerticalOffset + ViewUtil.getStatusBarHeight() + (getToolbarHeight() - finalAvatarSize) * 1.0f / 2;
                float avatarTop = startAvatarTop - (startAvatarTop - toAvatarTop) * factor;

                // 初始化MarginLeft
                float startMargin = avatarMarginLeft;
                float toMargin = avatarFinalMarginLeft;
                float margin = startMargin - (startMargin - toMargin) * factor;

                // 平移
                avatarMatrix.postTranslate(margin, avatarTop);
            }

//            if (layNameMatrix != null) {
//                float dsize = (layNameBitmap.getHeight() - finalLayNameSize) * factor;
//                float nameToSize = layNameBitmap.getHeight() - dsize;
//                float scale = nameToSize * 1.0f / layNameBitmap.getHeight();
//                layNameMatrix.setScale(scale, scale);
//
//                float startNameTop = userInfoBgLayout.getTop();
//                float toNameTop = maxVerticalOffset + ViewUtil.getStatusBarHeight() + (toolbar.getHeight() - finalAvatarSize) * 1.0f / 2;
//                float nameTop = startNameTop - (startNameTop - toNameTop) * factor;
//
//                float startMargin = avatarMarginLeft;
//                float toMargin = avatarFinalMarginLeft + finalAvatarSize + layNameMarginLeft;
//                float margin = startMargin - (startMargin - toMargin) * factor;
//
//                layNameMatrix.postTranslate(margin, nameTop);
//            }

//            if (layRealDetail != null) {
//                layRealDetail.setAlpha(1.0f - factor * 0.7f);
//            }
        }

    };

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if (avatarMatrix != null && avatarBitmap != null) {
            final int saveCount = canvas.save();

//            if (!isScrimsShown()) {
//                mTempRect.set(0, -verticalOffset, getWidth(), statusbarHeight + -verticalOffset);
//                mInsetForeground.setBounds(mTempRect);
//                mInsetForeground.draw(canvas);
//            }
//
//            if (layNameBitmap != null) {
//                canvas.drawBitmap(layNameBitmap, layNameMatrix, null);
//            }

            canvas.drawBitmap(avatarBitmap, avatarMatrix, null);

            canvas.restoreToCount(saveCount);
        }
    }

    public int getMaxVerticalOffset(){
        return maxVerticalOffset;
    }
}
