package com.hengye.share.ui.support;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.hengye.share.module.base.BaseApplication;
import com.hengye.share.ui.widget.image.ClipImageView;
import com.hengye.share.util.AnimationUtil;

/**
 * User: qii
 * Date: 14-4-1
 */
public class AnimationRect implements Parcelable {

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(scaledBitmapRect, flags);
        dest.writeParcelable(imageViewEntireRect, flags);
        dest.writeParcelable(imageViewVisibleRect, flags);
        dest.writeInt(type);
        dest.writeBooleanArray(new boolean[]{isTotalVisible});
        dest.writeBooleanArray(new boolean[]{isTotalInvisible});
        dest.writeBooleanArray(new boolean[]{isScreenPortrait});
        dest.writeFloat(thumbnailWidthHeightRatio);
        dest.writeInt(thumbnailWidth);
        dest.writeInt(thumbnailHeight);
        dest.writeInt(widgetWidth);
        dest.writeInt(widgetHeight);
        dest.writeFloat(clipByParentRectTop);
        dest.writeFloat(clipByParentRectBottom);
        dest.writeFloat(clipByParentRectLeft);
        dest.writeFloat(clipByParentRectRight);
    }

    public static final Parcelable.Creator<AnimationRect> CREATOR =
            new Parcelable.Creator<AnimationRect>() {
                public AnimationRect createFromParcel(Parcel in) {
                    AnimationRect rect = new AnimationRect();
                    rect.scaledBitmapRect = in.readParcelable(Rect.class.getClassLoader());
                    rect.imageViewEntireRect = in.readParcelable(Rect.class.getClassLoader());
                    rect.imageViewVisibleRect = in.readParcelable(Rect.class.getClassLoader());
                    rect.type = in.readInt();

                    boolean[] booleans = new boolean[1];
                    in.readBooleanArray(booleans);
                    rect.isTotalVisible = booleans[0];

                    boolean[] isTotalInvisibleBooleans = new boolean[1];
                    in.readBooleanArray(isTotalInvisibleBooleans);
                    rect.isTotalInvisible = isTotalInvisibleBooleans[0];

                    boolean[] isScreenPortraitArray = new boolean[1];
                    in.readBooleanArray(isScreenPortraitArray);
                    rect.isScreenPortrait = isScreenPortraitArray[0];

                    rect.thumbnailWidthHeightRatio = in.readFloat();
                    rect.thumbnailWidth = in.readInt();
                    rect.thumbnailHeight = in.readInt();

                    rect.widgetWidth = in.readInt();
                    rect.widgetHeight = in.readInt();

                    rect.clipByParentRectTop = in.readFloat();
                    rect.clipByParentRectBottom = in.readFloat();
                    rect.clipByParentRectLeft = in.readFloat();
                    rect.clipByParentRectRight = in.readFloat();

                    return rect;
                }

                public AnimationRect[] newArray(int size) {
                    return new AnimationRect[size];
                }
            };

    public static final int TYPE_CLIP_V = 0;
    public static final int TYPE_CLIP_H = 1;
    public static final int TYPE_EXTEND_V = 2;
    public static final int TYPE_EXTEND_H = 3;

    public float clipByParentRectTop;
    public float clipByParentRectBottom;
    public float clipByParentRectLeft;
    public float clipByParentRectRight;

    public Rect imageViewEntireRect;
    public Rect imageViewVisibleRect;
    public Rect scaledBitmapRect;

    public int type = -1;

    public boolean isTotalVisible;
    public boolean isTotalInvisible;

    public boolean isScreenPortrait;

    public float thumbnailWidthHeightRatio;
    public int thumbnailWidth;
    public int thumbnailHeight;
    public int widgetWidth;
    public int widgetHeight;

    public static AnimationRect buildFromImageView(ImageView imageView) {
        AnimationRect rect = new AnimationRect();

        rect.isScreenPortrait = BaseApplication.getInstance().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;

        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }

        if (bitmap == null) {
            return null;
        }

        rect.widgetWidth = imageView.getWidth();

        rect.widgetHeight = imageView.getHeight();

        rect.thumbnailWidthHeightRatio = (float) bitmap.getWidth() / (float) bitmap.getHeight();

        rect.thumbnailWidth = bitmap.getWidth();

        rect.thumbnailHeight = bitmap.getHeight();

        rect.imageViewEntireRect = new Rect();
        int[] location = new int[2];
        imageView.getLocationOnScreen(location);
        rect.imageViewEntireRect.left = location[0];
        rect.imageViewEntireRect.top = location[1];
        rect.imageViewEntireRect.right = rect.imageViewEntireRect.left + imageView.getWidth();
        rect.imageViewEntireRect.bottom = rect.imageViewEntireRect.top + imageView.getHeight();

        rect.imageViewVisibleRect = new Rect();
        boolean isVisible = imageView.getGlobalVisibleRect(rect.imageViewVisibleRect);

        boolean checkWidth = rect.imageViewVisibleRect.width() < imageView.getWidth();
        boolean checkHeight = rect.imageViewVisibleRect.height() < imageView.getHeight();

        rect.isTotalVisible = isVisible && !checkWidth && !checkHeight;

        rect.isTotalInvisible = !isVisible;

        ImageView.ScaleType scaledType = imageView.getScaleType();

        Rect scaledBitmapRect = new Rect(rect.imageViewEntireRect);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();

        int imageViewWidth = imageView.getWidth();
        int imageViewHeight = imageView.getHeight();

        float startScale;

        int deltaX;

        int deltaY;

        switch (scaledType) {
            case CENTER_CROP:

                if ((float) imageViewWidth / bitmapWidth
                        > (float) imageViewHeight / bitmapHeight) {

                    startScale = (float) imageViewWidth / bitmapWidth;
                    rect.type = TYPE_CLIP_V;
                } else {
                    startScale = (float) imageViewHeight / bitmapHeight;
                    rect.type = TYPE_CLIP_H;
                }

                bitmapHeight = (int) (bitmapHeight * startScale);
                bitmapWidth = (int) (bitmapWidth * startScale);

                deltaX = (imageViewWidth - bitmapWidth) / 2;
                deltaY = (imageViewHeight - bitmapHeight) / 2;

                scaledBitmapRect.set(scaledBitmapRect.left + deltaX, scaledBitmapRect.top + deltaY,
                        scaledBitmapRect.right - deltaX,
                        scaledBitmapRect.bottom - deltaY);

                break;

            case FIT_CENTER:

                if ((float) imageViewWidth / bitmapWidth
                        > (float) imageViewHeight / bitmapHeight) {
                    // Extend start bounds horizontally
                    startScale = (float) imageViewHeight / bitmapHeight;

                    rect.type = TYPE_EXTEND_V;
                } else {
                    startScale = (float) imageViewWidth / bitmapWidth;
                    rect.type = TYPE_EXTEND_H;
                }

                bitmapHeight = (int) (bitmapHeight * startScale);
                bitmapWidth = (int) (bitmapWidth * startScale);

                deltaX = (imageViewWidth - bitmapWidth) / 2;
                deltaY = (imageViewHeight - bitmapHeight) / 2;

                scaledBitmapRect
                        .set(scaledBitmapRect.left + deltaX, scaledBitmapRect.top + deltaY,
                                scaledBitmapRect.right - deltaX,
                                scaledBitmapRect.bottom - deltaY);

                break;
        }

        rect.scaledBitmapRect = scaledBitmapRect;

        return rect;
    }

    public static Rect getBitmapRectFromImageView(ImageView imageView) {
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = null;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        }

        Rect rect = new Rect();
        boolean isVisible = imageView.getGlobalVisibleRect(rect);
        if (!isVisible) {
            int[] location = new int[2];
            imageView.getLocationOnScreen(location);

            rect.left = location[0];
            rect.top = location[1];
            rect.right = rect.left + imageView.getWidth();
            rect.bottom = rect.top + imageView.getHeight();
        }

        if (bitmap != null) {

            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();

            int imageViewWidth = imageView.getWidth() - imageView.getPaddingLeft() - imageView
                    .getPaddingRight();
            int imageviewHeight = imageView.getHeight() - imageView.getPaddingTop() - imageView
                    .getPaddingBottom();

            float startScale;
            if ((float) imageViewWidth / bitmapWidth
                    > (float) imageviewHeight / bitmapHeight) {
                // Extend start bounds horizontally
                startScale = (float) imageviewHeight / bitmapHeight;
            } else {
                startScale = (float) imageViewWidth / bitmapWidth;
            }

            bitmapHeight = (int) (bitmapHeight * startScale);
            bitmapWidth = (int) (bitmapWidth * startScale);

            int deltaX = (imageViewWidth - bitmapWidth) / 2;
            int deltaY = (imageviewHeight - bitmapHeight) / 2;

            rect.set(rect.left + deltaX, rect.top + deltaY, rect.right - deltaX,
                    rect.bottom - deltaY);

            return rect;
        } else {
            return null;
        }
    }

    public static float getClipLeft(AnimationRect animationRect, Rect finalBounds, float startScale) {
        final Rect startBounds = animationRect.scaledBitmapRect;

//        float startScale;
//        if ((float) finalBounds.width() / finalBounds.height()
//                > (float) startBounds.width() / startBounds.height()) {
//            startScale = (float) startBounds.height() / finalBounds.height();
//        } else {
//            startScale = (float) startBounds.width() / finalBounds.width();
//        }

        int oriBitmapScaledWidth = (int) (finalBounds.width() * startScale);

        //sina server may cut thumbnail's right or bottom
        int thumbnailAndOriDeltaRightSize = Math
                .abs(animationRect.scaledBitmapRect.width() - oriBitmapScaledWidth);

        float serverClipThumbnailRightSizePercent = (float) thumbnailAndOriDeltaRightSize
                / (float) oriBitmapScaledWidth;

        float deltaH = (float) (oriBitmapScaledWidth
                - oriBitmapScaledWidth * serverClipThumbnailRightSizePercent
                - animationRect.widgetWidth);

        float deltaLeft = deltaH / 2;

        if (!animationRect.isTotalVisible && !animationRect.isTotalInvisible) {
            float deltaInvisibleLeft = Math
                    .abs(animationRect.imageViewVisibleRect.left
                            - animationRect.imageViewEntireRect.left);
            deltaLeft += deltaInvisibleLeft;
        }

        deltaLeft += thumbnailAndOriDeltaRightSize / 2;
//        L.debug("getClipLeft : {}", (deltaLeft) / (float) oriBitmapScaledWidth);
        return (deltaLeft) / (float) oriBitmapScaledWidth;
    }

    public static float getClipTop(AnimationRect animationRect, Rect finalBounds, float startScale) {

        final Rect startBounds = animationRect.scaledBitmapRect;

//        float startScale;
//        if ((float) finalBounds.width() / finalBounds.height()
//                > (float) startBounds.width() / startBounds.height()) {
//            startScale = (float) startBounds.height() / finalBounds.height();
//        } else {
//            startScale = (float) startBounds.width() / finalBounds.width();
//        }

        int oriBitmapScaledHeight = (int) (finalBounds.height() * startScale);

        //sina server may cut thumbnail's right or bottom
        int thumbnailAndOriDeltaBottomSize = Math
                .abs(animationRect.scaledBitmapRect.height() - oriBitmapScaledHeight);

        float serverClipThumbnailBottomSizePercent = (float) thumbnailAndOriDeltaBottomSize
                / (float) oriBitmapScaledHeight;

        float deltaV = (float) (oriBitmapScaledHeight
                - oriBitmapScaledHeight * serverClipThumbnailBottomSizePercent
                - animationRect.widgetHeight);

        float deltaTop = deltaV / 2;

        if (!animationRect.isTotalVisible && !animationRect.isTotalInvisible) {

            float deltaInvisibleTop = Math
                    .abs(animationRect.imageViewVisibleRect.top
                            - animationRect.imageViewEntireRect.top);

            deltaTop += deltaInvisibleTop;
        }

//        deltaTop += thumbnailAndOriDeltaBottomSize;

//        L.debug("getClipTop : {}", (deltaTop) / (float) oriBitmapScaledHeight);
        return (deltaTop) / (float) oriBitmapScaledHeight;
    }

    public static float getClipRight(AnimationRect animationRect, Rect finalBounds, float startScale) {
        final Rect startBounds = animationRect.scaledBitmapRect;

//        float startScale;
//        if ((float) finalBounds.width() / finalBounds.height()
//                > (float) startBounds.width() / startBounds.height()) {
//            startScale = (float) startBounds.height() / finalBounds.height();
//        } else {
//            startScale = (float) startBounds.width() / finalBounds.width();
//        }

        int oriBitmapScaledWidth = (int) (finalBounds.width() * startScale);

        //sina server may cut thumbnail's right or bottom
        int thumbnailAndOriDeltaRightSize = Math
                .abs(animationRect.scaledBitmapRect.width() - oriBitmapScaledWidth);

        float serverClipThumbnailRightSizePercent = (float) thumbnailAndOriDeltaRightSize
                / (float) oriBitmapScaledWidth;

        float deltaH = (float) (oriBitmapScaledWidth
                - oriBitmapScaledWidth * serverClipThumbnailRightSizePercent
                - animationRect.widgetWidth);

        float deltaRight = deltaH / 2;

        if (!animationRect.isTotalVisible && !animationRect.isTotalInvisible) {
            float deltaInvisibleRight = Math
                    .abs(animationRect.imageViewVisibleRect.right
                            - animationRect.imageViewEntireRect.right);
            deltaRight += deltaInvisibleRight;
        }

        deltaRight += thumbnailAndOriDeltaRightSize / 2;

//        L.debug("getClipRight : {}", (deltaRight) / (float) oriBitmapScaledWidth);
        return (deltaRight) / (float) oriBitmapScaledWidth;
    }

    public static float getClipBottom(AnimationRect animationRect, Rect finalBounds, float startScale) {
        final Rect startBounds = animationRect.scaledBitmapRect;

//        float startScale;
//        if ((float) finalBounds.width() / finalBounds.height()
//                > (float) startBounds.width() / startBounds.height()) {
//            startScale = (float) startBounds.height() / finalBounds.height();
//        } else {
//            startScale = (float) startBounds.width() / finalBounds.width();
//        }

        int oriBitmapScaledHeight = (int) (finalBounds.height() * startScale);

        //sina server may cut thumbnail's right or bottom
        int thumbnailAndOriDeltaBottomSize = Math
                .abs(animationRect.scaledBitmapRect.height() - oriBitmapScaledHeight);

        float serverClipThumbnailBottomSizePercent = (float) thumbnailAndOriDeltaBottomSize
                / (float) oriBitmapScaledHeight;

        float deltaV = (float) (oriBitmapScaledHeight
                - oriBitmapScaledHeight * serverClipThumbnailBottomSizePercent
                - animationRect.widgetHeight);

        float deltaBottom = deltaV / 2;

        if (!animationRect.isTotalVisible && !animationRect.isTotalInvisible) {

            float deltaInvisibleBottom = Math
                    .abs(animationRect.imageViewVisibleRect.bottom
                            - animationRect.imageViewEntireRect.bottom);

            deltaBottom += deltaInvisibleBottom;
        }

        deltaBottom += thumbnailAndOriDeltaBottomSize;

//        L.debug("getClipBottom : {}", (deltaBottom) / (float) oriBitmapScaledHeight);
        return (deltaBottom) / (float) oriBitmapScaledHeight;
    }

    public static void runEnterAnimation(final ClipImageView photoView, final AnimationRect rect) {
        runEnterAnimation(photoView, rect, null);
    }

    public static void runEnterAnimation(final ClipImageView photoView, final AnimationRect rect, final View target) {
        photoView.getViewTreeObserver()
                .addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        photoView.getViewTreeObserver().removeOnPreDrawListener(this);
                        if (rect == null) {
                            return true;
                        }

                        final Rect startBounds = new Rect(rect.scaledBitmapRect);
                        final Rect finalBounds = AnimationRect
                                .getBitmapRectFromImageView(photoView);

                        if (finalBounds == null) {
                            return true;
                        }

                        int deltaTop = startBounds.top - finalBounds.top;
                        int deltaLeft = startBounds.left - finalBounds.left;

                        float startScale;
                        if ((float) finalBounds.width() / finalBounds.height()
                                > (float) startBounds.width() / startBounds.height()) {
                            startScale = (float) startBounds.height() / finalBounds.height();
                        } else {
                            startScale = (float) startBounds.width() / finalBounds.width();
                        }

                        int oriBitmapScaledWidth = (int) (finalBounds.width() * startScale);

                        //sina server may cut thumbnail's right or bottom
                        int thumbnailAndOriDeltaRightSize = Math
                                .abs(rect.scaledBitmapRect.width() - oriBitmapScaledWidth);

                        photoView.setPivotY(
                                (photoView.getHeight() - finalBounds.height()) / 2);
                        photoView.setPivotX((photoView.getWidth() - finalBounds.width()) / 2);

                        photoView.setTranslationX(deltaLeft - thumbnailAndOriDeltaRightSize / 2);
                        photoView.setTranslationY(deltaTop);

                        photoView.setScaleX(startScale);
                        photoView.setScaleY(startScale);

                        ViewPropertyAnimator vpa = photoView.animate().translationY(0).translationX(0)
                                .scaleY(1)
                                .scaleX(1).setDuration(AnimationUtil.GALLERY_ANIMATION_DURATION)
                                .setInterpolator(
                                        new AccelerateDecelerateInterpolator());

                        if(target != null){
                            vpa.withEndAction(new Runnable() {
                                @Override
                                public void run() {
                                    target.setVisibility(View.VISIBLE);
                                    photoView.setVisibility(View.INVISIBLE);
                                }
                            });
                        }

                        AnimatorSet animationSet = new AnimatorSet();
                        animationSet.setDuration(AnimationUtil.GALLERY_ANIMATION_DURATION);
                        animationSet
                                .setInterpolator(new AccelerateDecelerateInterpolator());

                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipBottom",
                                AnimationRect.getClipBottom(rect, finalBounds, startScale), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipRight",
                                AnimationRect.getClipRight(rect, finalBounds, startScale), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipTop", AnimationRect.getClipTop(rect, finalBounds, startScale), 0));
                        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                                "clipLeft", AnimationRect.getClipLeft(rect, finalBounds, startScale), 0));

                        animationSet.start();

                        return true;
                    }
                });
    }

    public static void runExitAnimation(final ClipImageView photoView, final AnimationRect rect, ObjectAnimator backgroundAnimator) {

//        if (Math.abs(photoView.getScale() - 1.0f) > 0.1f) {
//            photoView.setScale(1, true);
//            return;
//        }

        if (rect == null) {
            photoView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }

        final Rect startBounds = rect.scaledBitmapRect;
        final Rect finalBounds = AnimationRect.getBitmapRectFromImageView(photoView);

        if (finalBounds == null) {
            photoView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }

        if (isDevicePort() != rect.isScreenPortrait) {
            photoView.animate().alpha(0);
            backgroundAnimator.start();
            return;
        }

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            startScale = (float) startBounds.height() / finalBounds.height();
        } else {
            startScale = (float) startBounds.width() / finalBounds.width();
        }

        final float startScaleFinal = startScale;

        int deltaTop = startBounds.top - finalBounds.top;
        int deltaLeft = startBounds.left - finalBounds.left;

        photoView.setPivotY((photoView.getHeight() - finalBounds.height()) / 2);
        photoView.setPivotX((photoView.getWidth() - finalBounds.width()) / 2);


        int oriBitmapScaledWidth = (int) (finalBounds.width() * startScale);

        //sina server may cut thumbnail's right or bottom
        int thumbnailAndOriDeltaRightSize = Math
                .abs(rect.scaledBitmapRect.width() - oriBitmapScaledWidth);

        deltaLeft = (deltaLeft - thumbnailAndOriDeltaRightSize / 2);


        photoView.animate().translationX(deltaLeft).translationY(deltaTop)
                .scaleY(startScaleFinal)
                .scaleX(startScaleFinal).setDuration(AnimationUtil.GALLERY_ANIMATION_DURATION)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        photoView.animate().alpha(0.0f).setDuration(200);
                    }
                });

        AnimatorSet animationSet = new AnimatorSet();
        animationSet.setDuration(AnimationUtil.GALLERY_ANIMATION_DURATION);
        animationSet.setInterpolator(new AccelerateDecelerateInterpolator());

        animationSet.playTogether(backgroundAnimator);

        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipBottom", 0,
                AnimationRect.getClipBottom(rect, finalBounds, startScale)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipRight", 0,
                AnimationRect.getClipRight(rect, finalBounds, startScale)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipTop", 0, AnimationRect.getClipTop(rect, finalBounds, startScale)));
        animationSet.playTogether(ObjectAnimator.ofFloat(photoView,
                "clipLeft", 0, AnimationRect.getClipLeft(rect, finalBounds, startScale)));

        animationSet.start();
    }

    public static boolean isDevicePort() {
        return BaseApplication.getInstance().getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_PORTRAIT;
    }
}
