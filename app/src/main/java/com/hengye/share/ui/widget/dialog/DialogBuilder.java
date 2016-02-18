package com.hengye.share.ui.widget.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.hengye.share.R;

public class DialogBuilder {

    public static Dialog getItemDialog(Context context, DialogInterface.OnClickListener onClickListener, CharSequence... items){

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setItems(items, onClickListener);
        return builder.create();
    }

    public final static int LONG_CLICK_TOPIC_REPOST = 0;
    public final static int LONG_CLICK_TOPIC_COMMENT = 1;
    public final static int LONG_CLICK_TOPIC_COLLECT = 2;
    public final static int LONG_CLICK_TOPIC_COPY = 3;
    public final static int LONG_CLICK_TOPIC_REPOST_ORIGIN = 4;
    public final static int LONG_CLICK_TOPIC_LENGTH = 4;
    public static Dialog getOnLongClickTopicDialog(Context context, DialogInterface.OnClickListener onClickListener){

        CharSequence[] cs = new String[LONG_CLICK_TOPIC_LENGTH];
        cs[LONG_CLICK_TOPIC_REPOST] = context.getString(R.string.label_topic_repost);
        cs[LONG_CLICK_TOPIC_COMMENT] = context.getString(R.string.label_topic_comment);
        cs[LONG_CLICK_TOPIC_COLLECT] = context.getString(R.string.label_topic_collect);
        cs[LONG_CLICK_TOPIC_COPY] = context.getString(R.string.label_topic_copy);
//        cs[LONG_CLICK_TOPIC_REPOST_ORIGIN] = context.getString(R.string.label_topic_repost_origin);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setItems(cs, onClickListener);
        return builder.create();
    }

}
