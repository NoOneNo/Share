package com.hengye.share.ui.support.textspan;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.hengye.share.service.VideoPlayService;
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
        if(topicUrl != null){
            //长链地址
            return topicUrl.getLinkUrl();
        }
        return url;
    }

    public String getPath(){
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
        // TODO: 16/9/20 需要优化
        //如果是网页已经替换成DataUtil.WEB_SCHEME
        String url = uri.toString();

        L.debug("onClick url : {}", url);
        if (WBUtil.isWBAccountIdLink(url)) {
            Intent intent = new Intent(context, PersonalHomepageActivity.class);
            intent.putExtra("id", WBUtil.getIdFromWBAccountLink(url));
            context.startActivity(intent);
        } else {
            if(topicUrl != null){
                if(topicUrl.getType() == TopicUrl.VIDEO) {
                    VideoPlayService.start(context, topicUrl.getTopicId(), getURL());
                }else{
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                    context.startActivity(intent);
                }
            }else {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.putExtra(Browser.EXTRA_APPLICATION_ID, context.getPackageName());
                context.startActivity(intent);
            }
        }
    }

    @Override
    public void onLongClick(View widget) {
        Uri data = Uri.parse(getURL());
        if (data != null) {
            String value = data.toString();
            String newValue = "";
            if (value.startsWith(DataUtil.MENTION_SCHEME)) {
//                int index = value.lastIndexOf("/");
//                newValue = value.substring(index + 1);
                newValue = value.substring(DataUtil.MENTION_SCHEME.length());
            } else if (value.startsWith("http")) {
                newValue = value;
            }

            L.debug("long click value : {}", newValue);
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