package com.hengye.share.util;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Patterns;

import com.hengye.share.BuildConfig;
import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.model.TopicComment;
import com.hengye.share.model.TopicId;
import com.hengye.share.model.TopicShortUrl;
import com.hengye.share.model.TopicUrl;
import com.hengye.share.ui.support.textspan.CustomContentSpan;
import com.hengye.share.ui.support.textspan.EmotionSpan;
import com.hengye.share.ui.support.textspan.SimpleContentSpan;
import com.hengye.share.ui.support.textspan.TopicContentUrlSpan;
import com.hengye.share.ui.widget.emoticon.Emoticon;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {

    public static String getCounter(long count, String append) {
        if (count < 10000) {
            return String.valueOf(count) + append;
        } else if (count < 100 * 10000) {
            return new DecimalFormat("#.0").format(count * 1.0f / 10000) + append + ResUtil.getString(R.string.unit_ten_thousand);
        } else {
            return new DecimalFormat("#").format(count * 1.0f / 10000) + append + ResUtil.getString(R.string.unit_ten_thousand);
        }
    }

    public static String getCounter(long count) {
        return getCounter(count, "");
    }

    public static final Pattern WEB_URL = Pattern
            .compile("(http|https|Http|Https|rtsp|Rtsp)://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]");
    public static final Pattern WEB_URL2 = Patterns.WEB_URL;
    public static final Pattern TOPIC_URL = Pattern
            .compile("#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#");
    public static final Pattern MENTION_URL = Pattern
            .compile("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}");
//    public static final Pattern EMOTION_URL = Pattern
//            .compile("\\[[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^]+\\]");
    public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

    public static final String WEB_URL_REPLACE = "➢网页链接";

    public static final String WEB_SCHEME = BuildConfig.APPLICATION_ID + ".http:";
    public static final String TOPIC_SCHEME = BuildConfig.APPLICATION_ID + ".topic:";
    public static final String MENTION_SCHEME = BuildConfig.APPLICATION_ID + ".mention:";

    public static boolean isTopic(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith(TOPIC_SCHEME)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMention(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (url.startsWith(MENTION_SCHEME)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isHttpUrl(String url) {
        if (!TextUtils.isEmpty(url)) {
            Matcher m = WEB_URL.matcher(url);
            while (m.find()) {
                return true;
            }
            return false;
//                if (url.startsWith("http://") || url.startsWith("https://") || url.startsWith(WEB_SCHEME)) {
//                    return true;
//                }
        }
        return false;
    }
//    public static boolean isHttpUrl(String url) {
//        return matchLinks(url, Patterns.WEB_URL,
//                new String[]{"http://", "https://", "rtsp://"},
//                Linkify.sUrlMatchFilter, null);
//    }

    public static void addTopicContentHighLightLinks(TopicComment topicComment) {
        topicComment.setUrlSpannableString(convertNormalStringToSpannableString(topicComment, topicComment.getContent()));
    }

    public static void addTopicContentHighLightLinks(Topic topic, boolean isRetweeted) {
        String str = isRetweeted ? addRetweetedNamePrefix(topic) : topic.getContent();
        topic.setUrlSpannableString(convertNormalStringToSpannableString(topic, str));
    }

    public static <T extends TopicShortUrl & TopicId> SpannableString convertNormalStringToSpannableString(@Nullable T topic, CharSequence source) {
        return convertNormalStringToSpannableString(topic, source, true);
    }

    public static <T extends TopicShortUrl & TopicId> SpannableString convertNormalStringToSpannableString(@Nullable T topic, CharSequence source, boolean isReplaceWebUrl) {
        //hack to fix android imagespan bug,see http://stackoverflow.com/questions/3253148/imagespan-is-cut-off-incorrectly-aligned
        //if string only contains emotion tags,add a empty char to the end
        String txt = source.toString();
        String hackTxt;
        if (txt.startsWith("[") && txt.endsWith("]")) {
            hackTxt = txt + " ";
        } else {
            hackTxt = txt;
        }

        SpannableString value = SpannableString.valueOf(hackTxt);

        Linkify.addLinks(value, WEB_URL, WEB_SCHEME);
//        Linkify.addLinks(value, WEB_URL, null);

        if (isReplaceWebUrl) {
            value = replaceWebUrl(topic, value);
        }

        //添加表情
        addEmotions(value);

        Linkify.addLinks(value, MENTION_URL, MENTION_SCHEME);
        Linkify.addLinks(value, TOPIC_URL, TOPIC_SCHEME);

        URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
        if (urlSpans == null || urlSpans.length == 0) {
            return value;
        }

        for (URLSpan urlSpan : urlSpans) {
            int start = value.getSpanStart(urlSpan);
            int end = value.getSpanEnd(urlSpan);

            if (start >= 0 && end >= 0 && value.length() >= end) {
                value.removeSpan(urlSpan);
                TopicContentUrlSpan topicContentUrlSpan = new TopicContentUrlSpan(start, end, urlSpan.getURL());
                setTopicContentUrl(topic, topicContentUrlSpan);
                value.setSpan(topicContentUrlSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return value;
    }

    public static  <T extends TopicShortUrl & TopicId> SpannableString replaceWebUrl(T topic, SpannableString value) {
        URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
        if (urlSpans != null && urlSpans.length != 0) {
            List<CustomContentSpan> sps = new ArrayList<>();
            List<URLSpan> us = new ArrayList<>(Arrays.asList(urlSpans));
            int size = us.size();
            for (int i = 0; i < size; i++) {
                URLSpan urlSpan = us.get(i);
                int start = value.getSpanStart(urlSpan);
                int end = value.getSpanEnd(urlSpan);
                if (start >= 0 && end >= 0 && value.length() >= end) {
                    value.removeSpan(urlSpan);
                    us.remove(i);
                    i--;
                    size--;

                    CustomContentSpan sp = new CustomContentSpan(start, end, urlSpan.getURL());
                    sps.add(sp);
                }
            }

            if (!CommonUtil.isEmpty(sps)) {
                int totalIndentLength = 0;
                for (CustomContentSpan sp : sps) {
                    sp.start -= totalIndentLength;
                    sp.end -= totalIndentLength;

                    CharSequence cs1 = value.subSequence(0, sp.start);

                    CharSequence cs2 = value.subSequence(sp.end, value.length());

                    String replaceUrlContent = getReplaceUrlContent(topic, Uri.parse(sp.content).getSchemeSpecificPart());
                    value = SpannableString.valueOf(cs1.toString() + replaceUrlContent + cs2.toString());
                    totalIndentLength = totalIndentLength + (sp.end - sp.start) - replaceUrlContent.length();
                    sp.end = sp.start + replaceUrlContent.length();
                }

                for (CustomContentSpan sp : sps) {
                    TopicContentUrlSpan topicContentUrlSpan = new TopicContentUrlSpan(sp);
                    setTopicContentUrl(topic, topicContentUrlSpan);
                    value.setSpan(topicContentUrlSpan, sp.start, sp.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return value;
    }

    private static void setTopicContentUrl(TopicShortUrl topicShortUrl, TopicContentUrlSpan topicContentUrlSpan){
        if(topicShortUrl != null && topicShortUrl.getUrlMap() != null){
            String url = topicContentUrlSpan.getPath();
//            if(url != null && url.startsWith(WEB_SCHEME) && url.length() > WEB_SCHEME.length()){
//                url = url.substring(WEB_SCHEME.length());
//            }
            TopicUrl topicUrl = topicShortUrl.getUrlMap().get(url);
            topicContentUrlSpan.setTopicUrl(topicUrl);
            if(topicUrl != null) {
                L.debug("wbShortUrl type : {}, url : {}", topicUrl.getType(), url);
            }
        }
    }

    private static String getReplaceUrlContent(TopicShortUrl topicShortUrl, String url){
        if(topicShortUrl != null && topicShortUrl.getUrlMap() != null){
            TopicUrl topicUrl = topicShortUrl.getUrlMap().get(url);
            if(topicUrl != null){
                if(topicUrl.getDisplayName() != null){
                    return "➢" + topicUrl.getDisplayName();
                }else{
                    switch (topicUrl.getType()){
                        case TopicUrl.VIDEO:
                            return "➢视频";
                        case TopicUrl.MUSIC:
                            return "➢音乐";
                    }
                }
            }
        }
        return WEB_URL_REPLACE;
    }

    public static String addRetweetedNamePrefix(Topic topic) {
        String str;
        if (!TextUtils.isEmpty(topic.getUserInfo().getName())) {
            //如果微博已经被删除，则名字为空
            str = "@" + topic.getUserInfo().getName() + ":" + topic.getContent();
        } else {
            str = topic.getContent();
        }
        return str;
    }

    public static String addRetweetedNamePrefix(TopicComment topic) {
        String str;
        if (!TextUtils.isEmpty(topic.getUserInfo().getName())) {
            //如果微博已经被删除，则名字为空
            str = "@" + topic.getUserInfo().getName() + ":" + topic.getContent();
        } else {
            str = topic.getContent();
        }
        return str;
    }

    private static List<SimpleContentSpan> addEmotions(Spannable value) {
        List<SimpleContentSpan> simpleContentSpans = new ArrayList<>();
        Matcher localMatcher = EMOTION_URL.matcher(value);
        while (localMatcher.find()) {
            String str = localMatcher.group();
            int start = localMatcher.start();
            int end = localMatcher.end();
            if (end - start < 10) {
                //表情文字描述长度都小于10
                Bitmap bitmap = Emoticon.getInstance().getEmoticonBitmap().get(str);
                if (bitmap != null) {
                    CustomContentSpan customContentSpan = new CustomContentSpan(start, end, str);
//                    ImageSpan localImageSpan = new ImageSpan(ApplicationUtil.getContext(), bitmap, ImageSpan.ALIGN_BOTTOM);
                    EmotionSpan emotionSpan = new EmotionSpan(customContentSpan, ApplicationUtil.getContext(), bitmap, ImageSpan.ALIGN_BOTTOM);
                    simpleContentSpans.add(emotionSpan);
                    value.setSpan(emotionSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return simpleContentSpans;
    }

    public static List<SimpleContentSpan> convertNormalStringToSimpleContentUrlSpans(CharSequence source) {

        SpannableString value = SpannableString.valueOf(source);

        Linkify.addLinks(value, WEB_URL, WEB_SCHEME);

        //添加表情
        List<SimpleContentSpan> simpleContentSpans = addEmotions(value);

        Linkify.addLinks(value, MENTION_URL, MENTION_SCHEME);
        Linkify.addLinks(value, TOPIC_URL, TOPIC_SCHEME);

        URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
        if (urlSpans == null || urlSpans.length == 0) {
            return simpleContentSpans;
        }

        for (URLSpan urlSpan : urlSpans) {
            int start = value.getSpanStart(urlSpan);
            int end = value.getSpanEnd(urlSpan);

            if (start >= 0 && end >= 0 && value.length() >= end) {
                TopicContentUrlSpan topicContentUrlSpan = new TopicContentUrlSpan(start, end, urlSpan.getURL());
                simpleContentSpans.add(topicContentUrlSpan);
            }
        }

        return simpleContentSpans;
    }
}








