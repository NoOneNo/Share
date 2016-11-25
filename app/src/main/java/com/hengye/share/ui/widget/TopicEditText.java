package com.hengye.share.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

import com.hengye.share.ui.support.textspan.CustomContentSpan;
import com.hengye.share.ui.support.textspan.SimpleContentSpan;
import com.hengye.share.ui.support.textspan.TopicContentUrlSpan;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhy on 2016/10/24.
 */

public class TopicEditText extends AppCompatEditText {

    private boolean mIsSelected;
    private Range mLastSelectedRange;
    private ArrayList<Range> mRangeArrayList = new ArrayList<>();
    
    public TopicEditText(Context context) {
        super(context);
        init();
    }

    public TopicEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TopicEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    
    private void init(){
        addTextChangedListener(new TopicTextWatcher());
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
//        ensureRange(text);
    }


    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new HackInputConnection(super.onCreateInputConnection(outAttrs), true, this);
    }

    @Override
    protected void onSelectionChanged(int selStart, int selEnd) {
        super.onSelectionChanged(selStart, selEnd);
        //avoid infinite recursion after calling setSelection()
        if (mLastSelectedRange != null && mLastSelectedRange.isEqual(selStart, selEnd)) {
            return;
        }

        //if user cancel a selection of mention string, reset the state of 'mIsSelected'
        Range closestRange = getRangeOfClosestMentionString(selStart, selEnd);
        if (closestRange != null && closestRange.end == selEnd) {
            mIsSelected = false;
        }

        Range nearbyRange = getRangeOfNearbyMentionString(selStart, selEnd);
        //if there is no mention string nearby the cursor, just skip
        if (nearbyRange == null) {
            return;
        }

        //forbid cursor located in the mention string.
        if (selStart == selEnd) {
            setSelection(nearbyRange.getAnchorPosition(selStart));
        } else {
            if (selEnd < nearbyRange.end) {
                setSelection(selStart, nearbyRange.end);
            }
            if (selStart > nearbyRange.start) {
                setSelection(nearbyRange.start, selEnd);
            }
        }
    }

    /**
     * 在text改变后调用此方法重新确认span的位置
     * @param text
     */
    public void ensureRange(CharSequence text){
        //reset state
        mIsSelected = false;
        if (mRangeArrayList != null) {
            mRangeArrayList.clear();
        }

        if (TextUtils.isEmpty(text)) {
            return;
        }
        SpannableString spannableText = SpannableString.valueOf(text);
        SimpleContentSpan[] spans = spannableText.getSpans(0, spannableText.length(), SimpleContentSpan.class);

        for (SimpleContentSpan span : spans) {
            mRangeArrayList.add(new Range(span));
        }
    }

    public void ensureRange(List<SimpleContentSpan> spans){
        //reset state
        mIsSelected = false;
        if (mRangeArrayList != null) {
            mRangeArrayList.clear();
        }

        if(spans == null || spans.isEmpty()){
            return;
        }

        for (SimpleContentSpan span : spans) {
            mRangeArrayList.add(new Range(span));
        }
    }

    private Range getRangeOfClosestMentionString(int selStart, int selEnd) {
        if (mRangeArrayList == null) {
            return null;
        }
        for (Range range : mRangeArrayList) {
            if (range.contains(selStart, selEnd)) {
                return range;
            }
        }
        return null;
    }

    private Range getRangeOfNearbyMentionString(int selStart, int selEnd) {
        if (mRangeArrayList == null) {
            return null;
        }
        for (Range range : mRangeArrayList) {
            if (range.isWrappedBy(selStart, selEnd)) {
                return range;
            }
        }
        return null;
    }
    
    //handle the deletion action for mention string, such as '@test'
    private class HackInputConnection extends InputConnectionWrapper {
        private EditText editText;

        public HackInputConnection(InputConnection target, boolean mutable, TopicEditText editText) {
            super(target, mutable);
            this.editText = editText;
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                int selectionStart = editText.getSelectionStart();
                int selectionEnd = editText.getSelectionEnd();
                Range closestRange = getRangeOfClosestMentionString(selectionStart, selectionEnd);
                if (closestRange == null) {
                    mIsSelected = false;
                    return super.sendKeyEvent(event);
                }
                //if mention string has been selected or the cursor is at the beginning of mention string, just use default action(delete)
                if (mIsSelected || selectionStart == closestRange.start) {
                    mIsSelected = false;
                    return super.sendKeyEvent(event);
                } else {
                    //select the mention string
                    mIsSelected = true;
                    mLastSelectedRange = closestRange;
                    setSelection(closestRange.end, closestRange.start);
                }
                return true;
            }
            return super.sendKeyEvent(event);
        }

        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }
    }


    //helper class to record the position of mention string in EditText
    private class Range extends CustomContentSpan{

        public Range(SimpleContentSpan span) {
            super(span);
        }

        public Range(int start, int end, String url) {
            super(start, end, url);
        }
        public boolean isWrappedBy(int start, int end) {
            return (start > this.start && start < this.end) || (end > this.start && end < this.end);
        }

        public boolean contains(int start, int end) {
            return this.start <= start && this.end >= end;
        }

        public boolean isEqual(int start, int end) {
            return (this.start == start && this.end == end) || (this.start == end && this.end == start);
        }

        public int getAnchorPosition(int value) {
            if ((value - start) - (end - value) >= 0) {
                return end;
            } else {
                return start;
            }
        }
    }

    private class TopicTextWatcher implements TextWatcher{
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
