package com.hengye.share.util;

import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

public class ViewUtil {

    public static int getTopicImageWidth(int maxWidth, int margin, int size){
        int marginLength = margin * 2;
        if(size == 1){
//            return ViewGroup.LayoutParams.WRAP_CONTENT;
            return (maxWidth - marginLength) / 2;
        }else if(size == 2 || size == 4){
            return (maxWidth - marginLength) / 3;
        }else{
            return (maxWidth - marginLength) / 3;
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

    public static void setTopicImageViewLayoutParams(ImageView iv, int maxWidth, int margin, int position, int size){
        int imageWidth = getTopicImageWidth(maxWidth, margin, size);

        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
        lp.width = imageWidth;
        lp.height = imageWidth;
        lp.rightMargin = margin;
        lp.bottomMargin = margin;
        lp.isMarginRelative();
//        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) lp;
        if(size == 1){
            iv.setMaxHeight((int)(maxWidth / 2 * 1.5));
        }
        iv.setLayoutParams(lp);
    }


}
