package com.hengye.share.ui.support.textspan;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Browser;
import android.text.ParcelableSpan;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.view.View;

import com.hengye.share.R;
import com.hengye.share.model.TopicUrl;
import com.hengye.share.module.profile.PersonalHomepageActivity;
import com.hengye.share.module.publish.TopicPublishActivity;
import com.hengye.share.module.util.image.GalleryActivity;
import com.hengye.share.service.VideoPlayService;
import com.hengye.share.ui.widget.dialog.DialogBuilder;
import com.hengye.share.util.ClipboardUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.thirdparty.WBUtil;

@SuppressLint("ParcelCreator")
public class TopicContentUrlSpan extends CharacterStyle implements ParcelableSpan, SimpleClickableSpan, SimpleContentSpan {

    public int start, end;
    public final String url;
    public TopicUrl topicUrl;

    public TopicContentUrlSpan(CustomContentSpan ccs) {
        this(ccs.start, ccs.end, ccs.content);
    }

    public TopicContentUrlSpan(int start, int end, String url) {
        this.start = start;
        this.end = end;
        this.url = url;
    }

    public TopicContentUrlSpan(Parcel src) {
        start = src.readInt();
        end = src.readInt();
        url = src.readString();
        topicUrl = (TopicUrl) src.readSerializable();
    }

    @Override
    public int getSpanTypeId() {
        return 11;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(start);
        dest.writeInt(end);
        dest.writeString(url);
        dest.writeSerializable(topicUrl);
    }

    public String getURL() {
        if (topicUrl != null) {
            //长链地址
            return DataUtil.WEB_SCHEME + topicUrl.getUrl();
        }
        return url;
    }

    public String getPath() {
        return Uri.parse(url).getSchemeSpecificPart();
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
        return getURL();
    }

    @Override
    public void onClick(View widget) {
        Uri uri = Uri.parse(getURL());
        Context context = widget.getContext();
        //如果是网页已经替换成DataUtil.WEB_SCHEME
        String url = uri.toString();

        L.debug("onClick url : %s", url);
        if (WBUtil.isWBAccountIdLink(url)) {
            Intent intent = new Intent(context, PersonalHomepageActivity.class);
            intent.putExtra("id", WBUtil.getIdFromWBAccountLink(url));
            context.startActivity(intent);
        } else {
            if (topicUrl != null) {
                if (topicUrl.getType() == TopicUrl.VIDEO) {
                    VideoPlayService.start(context, topicUrl.getTopicId(), getURL());
                } else if(topicUrl.getType() == TopicUrl.PHOTO){
                    if(topicUrl.getAnnotation() != null && topicUrl.getAnnotation() instanceof String) {
                        GalleryActivity.startWithIntent(context, (String) topicUrl.getAnnotation());
                    }
                }else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                    context.startActivity(intent);
                }
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                context.startActivity(intent);
            }
        }
    }

    @Override
    public void onLongClick(final View widget) {
        final String scheme = getURL();
        if (scheme != null) {
            final String path = getPath();
            final Context context = widget.getContext();
            DialogBuilder.getOnLongClickTopicContentUrlSpanDialog(context, scheme, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        if (scheme.startsWith(DataUtil.MENTION_SCHEME)) {
                            //@name
                            context.startActivity(TopicPublishActivity.getAtTaStartIntent(context, path, true));
                        } else {
                            //话题
                            //超链接
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getURL()));
                            intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                            context.startActivity(intent);
                        }
                    } else if (which == 1) {
                        //复制内容
                        ClipboardUtil.copyContent(path);
                    } else {
                        //查看微博
                        widget.callOnClick();
                    }
                }
            }).show();
            L.debug("long click path : %s", path);
            if(topicUrl != null){
                L.debug("long click topicUrl : %s", topicUrl);
            }
        }

    }

    @Override
    public void updateDrawState(TextPaint tp) {
        tp.setColor(ResUtil.getColor(R.color.topic_content_span));
//        tp.setUnderlineText(true);
    }

    public TopicUrl getTopicUrl() {
        return topicUrl;
    }

    public void setTopicUrl(TopicUrl topicUrl) {
        this.topicUrl = topicUrl;
    }

    public static final Parcelable.Creator<TopicContentUrlSpan> CREATOR = new Creator<TopicContentUrlSpan>() {
        @Override
        public TopicContentUrlSpan[] newArray(int size) {
            return new TopicContentUrlSpan[size];
        }

        @Override
        public TopicContentUrlSpan createFromParcel(Parcel in) {
            return new TopicContentUrlSpan(in);
        }
    };

}