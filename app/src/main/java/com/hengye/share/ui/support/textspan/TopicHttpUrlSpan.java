package com.hengye.share.ui.support.textspan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Browser;
import android.support.annotation.DrawableRes;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.view.View;

import com.hengye.share.util.L;

/**
 * Created by yuhy on 16/6/20.
 */
public class TopicHttpUrlSpan extends ImageSpan implements SimpleClickableSpan{

    private final String mURL;

    public TopicHttpUrlSpan(String url, Context context, @DrawableRes int resourceId) {
        super(context, resourceId, ALIGN_BOTTOM);
        mURL = url;
    }

    /**
     * @param verticalAlignment one of {@link DynamicDrawableSpan#ALIGN_BOTTOM} or
     * {@link DynamicDrawableSpan#ALIGN_BASELINE}.
     */
    public TopicHttpUrlSpan(String url, Context context, @DrawableRes int resourceId, int verticalAlignment) {
        super(context, resourceId, verticalAlignment);
        mURL = url;
    }

    public TopicHttpUrlSpan(String url, Context context, Bitmap bitmap, int verticalAlignment) {
        super(context, bitmap, verticalAlignment);
        mURL = url;
    }

    public String getURL() {
        return mURL;
    }

    @Override
    public void onClick(View widget) {
        L.debug("image span onClick invoke");
        Uri uri = Uri.parse(getURL());
        Context context = widget.getContext();
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
        context.startActivity(intent);
    }

    @Override
    public void onLongClick(View widget) {
        L.debug("image span onLongClick invoke");
    }

    @Override
    public void draw(Canvas canvas, CharSequence text,
                     int start, int end, float x,
                     int top, int y, int bottom, Paint paint) {
//        Drawable b = getCachedDrawable();
//        canvas.save();
//
//        int transY = bottom - b.getBounds().bottom;
//        if (mVerticalAlignment == ALIGN_BASELINE) {
//            transY -= paint.getFontMetricsInt().descent;
//        }
//
//        canvas.translate(x, transY);
//        b.draw(canvas);
//        canvas.restore();
        super.draw(canvas, text, start, end, x , top, y, bottom, paint);
//        canvas.drawText("链接", x, y, paint);
    }
}
