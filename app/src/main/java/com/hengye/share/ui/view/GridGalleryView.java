package com.hengye.share.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.hengye.share.util.ViewUtil;

public class GridGalleryView extends GridLayout implements View.OnClickListener{

    public GridGalleryView(Context context) {
        super(context);
        init(context);
    }

    public GridGalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GridGalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    protected void init(Context context) {
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);

        ensureSize();

//        generateGridView();
    }

    int mMaxWidth;
    protected void ensureSize(){
        if(mMaxWidth == 0) {
            mMaxWidth = getMeasuredWidth();
        }
    }



    protected void generateGridView(){
        if(!mReset){
            return;
        }
        mReset = false;

        if(mHandleData == null){
            throw new NullPointerException("HandleData Interface has not initialize");
        }

        removeAllViews();
        for(int i = 0; i < mChildCount; i++){
            ImageView imageView = mHandleData.getImageView();
            imageView.setTag(View.NO_ID, i);
            ImageSize.handleImageView(imageView, mMaxWidth, mMargin, mChildCount);
            imageView.setOnClickListener(this);
            addView(imageView);
            mHandleData.handleChildItem(imageView, i);
        }
        setColumnCount(ImageSize.getImageColumnCount(mChildCount));
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag(View.NO_ID);
        if(mOnItemClickListener != null){
            mOnItemClickListener.onItemClick(v, position);
        }
    }

    boolean mReset = true;
    int mChildCount;
    int mMargin;
    ViewUtil.OnItemClickListener mOnItemClickListener;
    HandleData mHandleData;

    public void reset(){
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

    public int getChildCount() {
        return mChildCount;
    }

    public GridGalleryView setChildCount(int childCount) {
        this.mChildCount = childCount;
        return this;
    }

    public ViewUtil.OnItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    public GridGalleryView setOnItemClickListener(ViewUtil.OnItemClickListener onItemClickListener) {
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

    public interface HandleData{
        <T extends ImageView> T getImageView();
        void handleChildItem(ImageView imageView, int position);
    }

    public static class ImageSize{
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

        public static int getImageColumnCount(int count){
            if(count == 1){
                return 1;
            }else if(count == 2 || count == 4){
                return 2;
            }else{
                return 3;
            }
        }

        public static ImageSize getImageSize(int maxWidth, int margin, int count){
            int marginLength = margin * 2;
            if(count == 1){
                int size = ViewGroup.LayoutParams.WRAP_CONTENT;
                int minSize = maxWidth / 2;
                return new ImageSize(minSize, minSize, minSize, minSize, maxWidth, minSize);
            }else if(count == 2 || count == 4){
                return new ImageSize((maxWidth - marginLength) / 3);
            }else{
                return new ImageSize((maxWidth - marginLength) / 3);
            }
        }

        public static void handleImageView(ImageView imageView, int maxWidth, int margin, int count){
            ImageSize is = getImageSize(maxWidth, margin, count);

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = is.width;
            lp.height = is.height;
            lp.rightMargin = margin;
            lp.bottomMargin = margin;
//            MarginLayoutParams lp = new MarginLayoutParams(is.width, is.height);
//            lp.rightMargin = margin;
//            lp.bottomMargin = margin;
//        iv.setAdjustViewBounds(true);
            if(count == 1){
                imageView.setMinimumWidth(is.minWidth);
                imageView.setMinimumHeight(is.minHeight);
                imageView.setMaxWidth(is.maxWidth);
                imageView.setMaxHeight(is.maxHeight);
            }
            imageView.setLayoutParams(lp);
        }
    }
}
