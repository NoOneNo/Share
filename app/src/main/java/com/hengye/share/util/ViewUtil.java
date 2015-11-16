package com.hengye.share.util;

import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

public class ViewUtil {

    public static class ImageSize{
        public int width;
        public int height;

        public ImageSize(int width) {
            this.width = width;
            this.height = width;
        }

        public ImageSize(int width, int height) {
            this.width = width;
            this.height = height;
        }

    }

    public static ImageSize getTopicImageWidth(int maxWidth, int margin, int size){
        int marginLength = margin * 2;
        if(size == 1){
//            return ViewGroup.LayoutParams.WRAP_CONTENT;
            return new ImageSize((maxWidth - marginLength) / 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        }else if(size == 2 || size == 4){
            return new ImageSize((maxWidth - marginLength) / 3);
        }else{
            return new ImageSize((maxWidth - marginLength) / 3);
        }
    }

    public static int getTopicImageColumnCount(int size){
        if(size == 1){
            return 1;
        }else if(size == 2 || size == 4){
            return 2;
        }else{
            return 3;
        }
    }

    public static ImageSize setTopicImageViewLayoutParams(ImageView iv, int maxWidth, int margin, int size){
        ImageSize imageSize = getTopicImageWidth(maxWidth, margin, size);

        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        lp.width = imageSize.width;
        lp.height = imageSize.height;
        lp.rightMargin = margin;
        lp.bottomMargin = margin;
        lp.isMarginRelative();
        if(size == 1){
            iv.setMaxHeight((int)(maxWidth / 2 * 1.5));
        }
        iv.setLayoutParams(lp);

        return imageSize;
    }

    public static String getCacheKey(String url, int maxWidth, int maxHeight, ImageView.ScaleType scaleType) {
        if(maxWidth == ViewGroup.LayoutParams.WRAP_CONTENT){
            maxWidth = 0;
        }
        if(maxHeight == ViewGroup.LayoutParams.WRAP_CONTENT){
            maxHeight = 0;
        }
        return new StringBuilder(url.length() + 12).append("#W").append(maxWidth)
                .append("#H").append(maxHeight).append("#S").append(scaleType.ordinal()).append(url)
                .toString();
    }
}
