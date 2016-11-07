package com.hengye.share.util;

import android.graphics.Bitmap;
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
import com.hengye.share.ui.support.textspan.CustomContentSpan;
import com.hengye.share.ui.widget.emoticon.Emoticon;
import com.hengye.share.ui.support.textspan.TopicContentUrlSpan;

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

//    public static final Pattern WEB_URL = Pattern
//            .compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]");
    public static final Pattern WEB_URL = Patterns.WEB_URL;
    public static final Pattern TOPIC_URL = Pattern
            .compile("#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#");
    public static final Pattern MENTION_URL = Pattern
            .compile("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}");
    public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

    public static final String WEB_URL_REPLACE = "➢网页链接";

    public static final String WEB_SCHEME = BuildConfig.APPLICATION_ID + ".http://";
    public static final String TOPIC_SCHEME = BuildConfig.APPLICATION_ID + ".topic://";
    public static final String MENTION_SCHEME = BuildConfig.APPLICATION_ID + ".mention://";

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
        topicComment.setUrlSpannableString(convertNormalStringToSpannableString(topicComment.getContent()));
    }

    public static void addTopicContentHighLightLinks(Topic topic, boolean isRetweeted) {
        String str = isRetweeted ? addRetweetedNamePrefix(topic) : topic.getContent();
        topic.setUrlSpannableString(convertNormalStringToSpannableString(str));
    }

    public static SpannableString convertNormalStringToSpannableString(CharSequence source) {
        return convertNormalStringToSpannableString(source, true);
    }

    public static SpannableString convertNormalStringToSpannableString(CharSequence source, boolean isReplaceWebUrl) {
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

//        Linkify.addLinks(value, Linkify.WEB_URLS);
        Linkify.addLinks(value, WEB_URL, WEB_SCHEME);

        if (isReplaceWebUrl) {
            value = replaceWebUrl(value);
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
                value.setSpan(new TopicContentUrlSpan(start, end, urlSpan.getURL()), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return value;
    }

    public static SpannableString replaceWebUrl(SpannableString value) {
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

                    CustomContentSpan sp = new CustomContentSpan();
                    sp.start = start;
                    sp.end = end;
                    sp.content = urlSpan.getURL();
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

                    value = SpannableString.valueOf(cs1.toString() + WEB_URL_REPLACE + cs2.toString());
                    totalIndentLength = totalIndentLength + (sp.end - sp.start) - WEB_URL_REPLACE.length();
                    sp.end = sp.start + WEB_URL_REPLACE.length();
                }

                for (CustomContentSpan sp : sps) {
                    value.setSpan(new TopicContentUrlSpan(sp), sp.start, sp.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return value;
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

    private static void addEmotions(SpannableString value) {
        Matcher localMatcher = EMOTION_URL.matcher(value);
        while (localMatcher.find()) {
            String str = localMatcher.group();
            int start = localMatcher.start();
            int end = localMatcher.end();
            if (end - start < 8) {
                Bitmap bitmap = Emoticon.getInstance().getEmoticonBitmap().get(str);
                if (bitmap != null) {
                    ImageSpan localImageSpan = new ImageSpan(ApplicationUtil.getContext(), bitmap, ImageSpan.ALIGN_BOTTOM);
//                    TopicHttpUrlSpan localImageSpan = new TopicHttpUrlSpan("www.baidu.com",context, bitmap, ImageSpan.ALIGN_BOTTOM);
                    value.setSpan(localImageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }
}

//        for (URLSpan urlSpan : urlSpans) {
//            int start = value.getSpanStart(urlSpan);
//            int end = value.getSpanEnd(urlSpan);
////            value.removeSpan(urlSpan);
//
//            if (isHttpUrl(urlSpan.getURL())) {
////                if (start >= 0 && end >= 0 && value.length() >= end) {
//////                    CharSequence cs1 = value.subSequence(0, start);
//////                    CharSequence cs2 = value.subSequence(end, value.length());
//////                    value = SpannableString.valueOf(cs1.toString() + "网页链接" + cs2.toString());
////
//////                    value.setSpan(new TopicHttpUrlSpan(urlSpan.getURL(), context, R.drawable.compose_mentionbutton_background_highlighted),
//////                            start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//////                    value.setSpan(new TopicHttpUrlSpan(urlSpan.getURL()), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////                    value.setSpan(new TopicContentUrlSpan(urlSpan.getURL()), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
////                }
//            }else {
//                value.removeSpan(urlSpan);
//                value.setSpan(new TopicContentUrlSpan(urlSpan.getURL()), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//        }



























