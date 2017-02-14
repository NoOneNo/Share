package com.hengye.share.ui.widget.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.util.DataUtil;

public class DialogBuilder {

    public static Dialog getItemDialog(Context context, DialogInterface.OnClickListener onClickListener, CharSequence... items){

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setItems(items, onClickListener);
        return builder.create();
    }

    public final static int LONG_CLICK_TOPIC_REPOST = 0;
    public final static int LONG_CLICK_TOPIC_COMMENT = 1;
    public final static int LONG_CLICK_TOPIC_LIKE = 2;
    public final static int LONG_CLICK_TOPIC_FAVORITE = 3;
    public final static int LONG_CLICK_TOPIC_COPY = 4;
    public final static int LONG_CLICK_TOPIC_DESTROY = 5;
    public final static int LONG_CLICK_TOPIC_LENGTH = 5;

    public static Dialog getOnLongClickTopicDialog(Context context, DialogInterface.OnClickListener onClickListener, Topic topic, boolean isMine){

        CharSequence[] cs = new String[isMine ? LONG_CLICK_TOPIC_LENGTH + 1 : LONG_CLICK_TOPIC_LENGTH];

        String repost = context.getString(R.string.label_topic_repost_number, DataUtil.getCounter(topic.getRepostsCount()));
        String comment = context.getString(R.string.label_topic_comment_number, DataUtil.getCounter(topic.getCommentsCount()));
        String attitude = context.getString(
                topic.isLiked() ?
                R.string.label_topic_attitude_cancel_number :
                R.string.label_topic_attitude_number, DataUtil.getCounter(topic.getAttitudesCount()));
        String favorite = context.getString(topic.isFavorited() ? R.string.label_topic_collect_cancel : R.string.label_topic_collect);

        cs[LONG_CLICK_TOPIC_REPOST] = repost;
        cs[LONG_CLICK_TOPIC_COMMENT] = comment;
        cs[LONG_CLICK_TOPIC_LIKE] = attitude;
        cs[LONG_CLICK_TOPIC_FAVORITE] = favorite;
        cs[LONG_CLICK_TOPIC_COPY] = context.getString(R.string.label_topic_copy);

        if(isMine){
            cs[LONG_CLICK_TOPIC_DESTROY] = context.getString(R.string.label_topic_destroy);
        }
//        cs[LONG_CLICK_TOPIC_REPOST_ORIGIN] = context.getString(R.string.label_topic_repost_origin);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setItems(cs, onClickListener);
        return builder.create();
    }


    public final static int COMMENT_REPLY = 0;
    public final static int COMMENT_REPOST = 1;
    public final static int COMMENT_COPY = 2;
    public final static int COMMENT_LENGTH = 3;

    public static Dialog getTopicCommentDialog(Context context, DialogInterface.OnClickListener onClickListener){

        CharSequence[] cs = new String[COMMENT_LENGTH];
        cs[COMMENT_REPLY] = context.getString(R.string.label_comment_reply);
        cs[COMMENT_REPOST] = context.getString(R.string.label_topic_repost);
        cs[COMMENT_COPY] = context.getString(R.string.label_topic_copy);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setItems(cs, onClickListener);
        return builder.create();
    }


    public final static int REPOST_DETAIL = 0;
    public final static int REPOST_REPOST = 1;
    public final static int REPOST_COMMENT = 2;
    public final static int REPOST_COPY = 3;
    public final static int REPOST_LENGTH = 4;

    public static Dialog getTopicRepostDialog(Context context, DialogInterface.OnClickListener onClickListener){

        CharSequence[] cs = new String[REPOST_LENGTH];
        cs[REPOST_DETAIL] = context.getString(R.string.label_repost_detail);
        cs[REPOST_REPOST] = context.getString(R.string.label_topic_repost);
        cs[REPOST_COMMENT] = context.getString(R.string.label_topic_comment);
        cs[REPOST_COPY] = context.getString(R.string.label_topic_copy);

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setItems(cs, onClickListener);
        return builder.create();
    }

    public static Dialog getOnLongClickTopicContentUrlSpanDialog(Context context, String scheme, DialogInterface.OnClickListener onClickListener){

        if(scheme == null){
            return null;
        }

        CharSequence[] cs;
        if (scheme.startsWith(DataUtil.MENTION_SCHEME)) {
            //@name
            cs = new String[3];
            cs[0] = context.getString(R.string.label_url_at_user);
            cs[1] = context.getString(R.string.label_url_copy_content);
            cs[2] = context.getString(R.string.label_url_open_current_topic);
        }else if(scheme.startsWith(DataUtil.TOPIC_SCHEME)){
            //话题
            cs = new String[3];
            cs[0] = context.getString(R.string.label_url_search_topic);
            cs[1] = context.getString(R.string.label_url_copy_content);
            cs[2] = context.getString(R.string.label_url_open_current_topic);
        }else{
            //超链接
            cs = new String[3];
            cs[0] = context.getString(R.string.label_url_open_link);
            cs[1] = context.getString(R.string.label_url_copy_link);
            cs[2] = context.getString(R.string.label_url_open_current_topic);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setItems(cs, onClickListener);
        return builder.create();
    }

}
