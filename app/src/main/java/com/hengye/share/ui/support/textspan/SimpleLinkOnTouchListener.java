package com.hengye.share.ui.support.textspan;

import android.text.Layout;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.hengye.share.util.L;

public class SimpleLinkOnTouchListener implements View.OnTouchListener {

    private boolean find = false;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Layout layout = ((TextView) v).getLayout();

        if (layout == null) {
            return false;
        }

        int x = (int) event.getX();
        int y = (int) event.getY();

        int line = layout.getLineForVertical(y);
        int offset = layout.getOffsetForHorizontal(line, x);

        TextView tv = (TextView) v;
        SpannableString value = SpannableString.valueOf(tv.getText());

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                SimpleClickableSpan[] urlSpans = value.getSpans(0, value.length(), SimpleClickableSpan.class);
                int findStart = 0;
                int findEnd = 0;
                for (SimpleClickableSpan urlSpan : urlSpans) {
                    int start = value.getSpanStart(urlSpan);
                    int end = value.getSpanEnd(urlSpan);
                    if (start <= offset && offset <= end) {
                        find = true;
                        findStart = start;
                        findEnd = end;

                        break;
                    }
                }

                float lineWidth = layout.getLineWidth(line);

                find &= (lineWidth >= x);

                if (find) {
                    SimpleLinkMovementMethod.getInstance().onTouchEvent(tv, value, event);
                    BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(getPressColor());
                    value.setSpan(backgroundColorSpan, findStart, findEnd,
                            Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    //Android has a bug, sometime TextView wont change its value when you modify SpannableString,
                    // so you must setText again, test on Android 4.3 Nexus4
                    tv.setText(value);
                }

                return find;
            case MotionEvent.ACTION_MOVE:
                if (find) {
                    return SimpleLinkMovementMethod.getInstance().onTouchEvent(tv, value, event);
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                boolean result = false;
                if (find) {
                    result = SimpleLinkMovementMethod.getInstance().onTouchEvent(tv, value, event);
                    SimpleLinkMovementMethod.getInstance().removeLongClickCallback();

                    BackgroundColorSpan[] backgroundColorSpans = value
                            .getSpans(0, value.length(), BackgroundColorSpan.class);
                    for (BackgroundColorSpan backgroundColorSpan : backgroundColorSpans) {
                        value.removeSpan(backgroundColorSpan);
                    }
                    tv.setText(value);
                    find = false;
                }
                L.debug("result : {}", result);
                return result;
        }

        return false;
    }

    public int getPressColor(){
        return 0x000000;
    }
}
