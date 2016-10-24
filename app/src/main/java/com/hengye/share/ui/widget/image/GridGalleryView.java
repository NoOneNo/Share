package com.hengye.share.ui.widget.image;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.hengye.share.ui.widget.listener.OnItemClickListener;

public class GridGalleryView extends GridLayout implements View.OnClickListener {

    public GridGalleryView(Context context) {
        this(context, null);
    }

    public GridGalleryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridGalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    protected void init() {
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);

        ensureSize();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

    }

    int mMaxWidth = -1;

    protected void ensureSize() {
        if (mMaxWidth == -1) {
            mMaxWidth = getMeasuredWidth();
//            generateGridView();
        }
    }

    protected void generateGridView() {
        if (!mReset) {
            return;
        }

        mReset = false;
        if (mHandleData == null) {
            throw new NullPointerException("HandleData Interface has not initialize");
        }
        removeAllViews();
        setColumnCount(ImageSize.getImageColumnCount(mGridCount));
        for (int i = 0; i < mGridCount; i++) {
            ImageView imageView = mHandleData.getImageView();
            imageView.setTag(View.NO_ID, i);
            ImageSize.handleImageView(imageView, mMaxWidth, mMargin, mGridCount, i);
            imageView.setOnClickListener(this);
            addView(imageView);
            mHandleData.handleChildItem(imageView, i);
        }
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(View.NO_ID);
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, position);
        }
    }

    boolean mReset = true;
    int mGridCount;
    int mMargin;
    OnItemClickListener mOnItemClickListener;
    HandleData mHandleData;

    public void reset() {
        mReset = true;
        generateGridView();
    }

    public HandleData getHandleData() {
        return mHandleData;
    }

    public GridGalleryView setHandleData(HandleData handleData) {
        this.mHandleData = handleData;
        return this;
    }

    public int getGridCount() {
        return mGridCount;
    }

    public void setGridCount(int gridCount) {
        this.mGridCount = gridCount;
    }

    public int getMaxWidth() {
        return mMaxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.mMaxWidth = maxWidth;
    }

    public OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public GridGalleryView setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
        return this;
    }

    public int getMargin() {
        return mMargin;
    }

    public GridGalleryView setMargin(int margin) {
        this.mMargin = margin;
        return this;
    }

    public interface HandleData {
        <T extends ImageView> T getImageView();

        void handleChildItem(ImageView imageView, int position);
    }

    public static class ImageSize {
        public int width, height, minWidth, minHeight, maxWidth, maxHeight;

        public ImageSize(int width) {
            this.width = width;
            this.height = width;
        }

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public ImageSize(int width, int height, int minWidth, int minHeight, int maxWidth, int maxHeight) {
            this.width = width;
            this.height = height;
            this.minWidth = minWidth;
            this.minHeight = minHeight;
            this.maxWidth = maxWidth;
            this.maxHeight = maxHeight;
        }

        public static int getImageColumnCount(int count) {
            if (count == 1) {
                return 1;
            } else if (count == 2 || count == 4) {
                return 2;
            } else {
                return 3;
            }
        }

        public static ImageSize getImageSize(int maxWidth, int margin, int count) {
            int marginLength = margin * 2;
            if (count == 1) {
                int size = ViewGroup.LayoutParams.WRAP_CONTENT;
                int minSize = maxWidth / 2;
                return new ImageSize(minSize, minSize, minSize, minSize, maxWidth, minSize);
            } else if (count == 2 || count == 4) {
                return new ImageSize((maxWidth - marginLength) / 3);
            } else {
                return new ImageSize((maxWidth - marginLength) / 3);
            }
        }

        public static void handleImageView(ImageView imageView, int maxWidth, int margin, int count, int position) {
            ImageSize is = getImageSize(maxWidth, margin, count);

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();

            lp.width = is.width;
            lp.height = is.height;

            if(count % 3 == 0) {
                int actualPosition = position % 3;
                if (actualPosition == 0 || actualPosition == 1){
                    lp.rightMargin = margin;
                }
            }else{
                lp.rightMargin = margin;
            }

            lp.bottomMargin = margin;
            if (count == 1) {
//                imageView.setMinimumWidth(is.minWidth);
//                imageView.setMinimumHeight(is.minHeight);
//                imageView.setMaxWidth(is.maxWidth);
//                imageView.setMaxHeight(is.maxHeight);
            }
            imageView.setLayoutParams(lp);
        }
    }
}
