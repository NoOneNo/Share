package com.hengye.share.ui.support.textspan;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.style.ImageSpan;

/**
 * Created by yuhy on 2016/11/25.
 */

public class EmotionSpan extends ImageSpan implements SimpleContentSpan{

    public int start, end;
    public String desc;

    public EmotionSpan(CustomContentSpan customContentSpan, Context context, Bitmap b) {
        this(customContentSpan, context, b, ALIGN_BOTTOM);
    }

    public EmotionSpan(CustomContentSpan customContentSpan, Context context, Bitmap b, int verticalAlignment) {
       super(context, b, verticalAlignment);
        start = customContentSpan.getStart();
        end = customContentSpan.getEnd();
        desc = customContentSpan.getContent();
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public int getEnd() {
        return end;
    }

    @Override
    public String getContent() {
        return desc;
    }
}
