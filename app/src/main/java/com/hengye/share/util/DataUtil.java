package com.hengye.share.util;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Patterns;

import com.hengye.share.BuildConfig;
import com.hengye.share.R;
import com.hengye.share.model.Status;
import com.hengye.share.model.StatusComment;
import com.hengye.share.model.StatusId;
import com.hengye.share.model.StatusShortUrl;
import com.hengye.share.model.StatusUrl;
import com.hengye.share.module.base.BaseApplication;
import com.hengye.share.ui.support.textspan.CustomContentSpan;
import com.hengye.share.ui.support.textspan.SimpleContentSpan;
import com.hengye.share.ui.support.textspan.StatusContentUrlSpan;
import com.hengye.share.ui.widget.emoticon.EmoticonSpan;
import com.hengye.share.ui.widget.emoticon.EmoticonUtil;
import com.hengye.share.util.thirdparty.WBUtil;

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
            return new DecimalFormat("#.0").format(count * 1.0f / 10000) + ResUtil.getString(R.string.unit_ten_thousand) + append;
        } else {
            return new DecimalFormat("#").format(count * 1.0f / 10000) + ResUtil.getString(R.string.unit_ten_thousand) + append;
        }
    }

    public static String getCounter(long count) {
        return getCounter(count, "");
    }

    public static final Pattern WEB_URL = Pattern
            .compile("(http|https|Http|Https|rtsp|Rtsp)://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]");
    public static final Pattern WEB_URL2 = Patterns.WEB_URL;
    public static final Pattern STATUS_URL = Pattern
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

    public static final String WEB_LONG_TEXT = "全文";

    public static boolean isStatus(CharSequence url) {
        if (!TextUtils.isEmpty(url)) {
            Matcher m = STATUS_URL.matcher(url);
            //noinspection LoopStatementThatDoesntLoop
            while (m.find()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMention(CharSequence url) {
        if (!TextUtils.isEmpty(url)) {
            Matcher m = MENTION_URL.matcher(url);
            //noinspection LoopStatementThatDoesntLoop
            while (m.find()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isHttpUrl(CharSequence url) {
        if (!TextUtils.isEmpty(url)) {
            Matcher m = WEB_URL.matcher(url);
            //noinspection LoopStatementThatDoesntLoop
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

    public static void addStatusContentHighLightLinks(int textSize, StatusComment statusComment, boolean isReply) {
        String content = isReply ? addReplyCommentNamePrefix(statusComment) : statusComment.getContent();
        statusComment.setSpanned(convertNormalStringToSpannableString(textSize, statusComment, content));
    }

    public static void addStatusContentHighLightLinks(int textSize, Status status, boolean isRetweeted) {
        String content = isRetweeted ? addRetweetedStatusNamePrefix(status) : status.getContent();
        status.setSpanned(convertNormalStringToSpannableString(textSize, status, content, true, status.isFromMobile()));
    }

    private static <T extends StatusShortUrl & StatusId> Spanned convertNormalStringToSpannableString(int textSize, @Nullable T topic, CharSequence source) {
        return convertNormalStringToSpannableString(textSize, topic, source, true, false);
    }

    private static <T extends StatusShortUrl & StatusId> Spanned convertNormalStringToSpannableString(int textSize, @Nullable T status, CharSequence source, boolean isReplaceWebUrl, boolean isHtml) {
        //hack to fix android imagespan bug,see http://stackoverflow.com/questions/3253148/imagespan-is-cut-off-incorrectly-aligned
        //if string only contains emotion tags,add a empty char to the end
        if (source == null) {
            return null;
        }

        Spannable value;
        if (isHtml) {
            Spanned spanned = Html.fromHtml(source.toString());
            value = SpannableStringBuilder.valueOf(spanned);
        } else {
            String txt = source.toString();
            String hackTxt;
            if (txt.startsWith("[") && txt.endsWith("]")) {
                hackTxt = txt + " ";
            } else {
                hackTxt = txt;
            }

            value = SpannableString.valueOf(hackTxt);
        }

        Linkify.addLinks(value, WEB_URL, WEB_SCHEME);
//        Linkify.addLinks(value, WEB_URL, null);

        if (isReplaceWebUrl) {
            value = replaceWebUrl(status, value);
        }

        //添加表情
        addEmotions(textSize, value);

        if(isHtml){
            //替换微博移动内容Html的图片资源
            //必须在添加表情后再替换ImageSpan，因为添加表情时旧的EmoticonSpan会被清除
            ImageSpan[] imageSpans = value.getSpans(0, value.length(), ImageSpan.class);
            if (imageSpans != null && imageSpans.length != 0) {
                for (ImageSpan imageSpan : imageSpans) {
//                    String imageSpanSource = imageSpan.getSource();
//                    if(imageSpanSource != null && imageSpanSource.startsWith("//")){
//                        imageSpan.ge
//                    }
                    int start = value.getSpanStart(imageSpan);
                    int end = value.getSpanEnd(imageSpan);

                    if (start >= 0 && end >= 0 && value.length() >= end) {
                        value.removeSpan(imageSpan);
                        EmoticonSpan emoticonSpan = new EmoticonSpan(BaseApplication.getInstance(),
                                R.drawable.ic_timeline_card_small_web_default,
                                textSize, textSize, ImageSpan.ALIGN_BASELINE);
                        value.setSpan(emoticonSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }
        }

        Linkify.addLinks(value, MENTION_URL, MENTION_SCHEME);
        Linkify.addLinks(value, STATUS_URL, TOPIC_SCHEME);

        URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
        if (urlSpans == null || urlSpans.length == 0) {
            return value;
        }

        for (URLSpan urlSpan : urlSpans) {
            int start = value.getSpanStart(urlSpan);
            int end = value.getSpanEnd(urlSpan);

            if (start >= 0 && end >= 0 && value.length() >= end) {
                value.removeSpan(urlSpan);
                StatusContentUrlSpan statusContentUrlSpan = new StatusContentUrlSpan(start, end, urlSpan.getURL());
                setStatusContentUrl(status, statusContentUrlSpan);
                value.setSpan(statusContentUrlSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return value;
    }

    private static <T extends StatusShortUrl & StatusId> Spannable replaceWebUrl(T topic, Spannable value) {
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

                    CharSequence spanValue = value.subSequence(sp.start, sp.end);
                    //主要处理如果是热门微博，接口获取的数据是html。
                    if (!isHttpUrl(spanValue)) {
                        if (isStatus(spanValue)) {
                            sp.setContent(TOPIC_SCHEME + spanValue);
                        } else if (isMention(spanValue)) {
                            sp.setContent(MENTION_SCHEME + spanValue);
                        } else if (WEB_LONG_TEXT.equals(spanValue.toString()) && !isHttpUrl(sp.getContent())) {
                            //如果是点击全文，链接不是全链接，只有部分开头:/status/4073277108353952，需要拼接新浪移动的域名
                            sp.setContent(WEB_SCHEME + WBUtil.URL_HTTP_MOBILE + sp.getContent());
                        } else {
                            sp.setContent(WEB_SCHEME + sp.getContent());
                        }
                        continue;
                    }

                    CharSequence cs1 = value.subSequence(0, sp.start);

                    CharSequence cs2 = value.subSequence(sp.end, value.length());

                    String replaceUrlContent = getReplaceUrlContent(topic, Uri.parse(sp.content).getSchemeSpecificPart());
                    value = SpannableString.valueOf(cs1.toString() + replaceUrlContent + cs2.toString());
                    totalIndentLength = totalIndentLength + (sp.end - sp.start) - replaceUrlContent.length();
                    sp.end = sp.start + replaceUrlContent.length();
                }

                for (CustomContentSpan sp : sps) {
                    StatusContentUrlSpan topicContentUrlSpan = new StatusContentUrlSpan(sp);
                    setStatusContentUrl(topic, topicContentUrlSpan);
                    value.setSpan(topicContentUrlSpan, sp.start, sp.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return value;
    }

    private static void setStatusContentUrl(StatusShortUrl topicShortUrl, StatusContentUrlSpan topicContentUrlSpan) {
        if (topicShortUrl != null && topicShortUrl.getUrlMap() != null) {
            String url = topicContentUrlSpan.getPath();
//            if(url != null && url.startsWith(WEB_SCHEME) && url.length() > WEB_SCHEME.length()){
//                url = url.substring(WEB_SCHEME.length());
//            }
            StatusUrl topicUrl = topicShortUrl.getUrlMap().get(url);
            topicContentUrlSpan.setStatusUrl(topicUrl);
        }
    }

    private static String getReplaceUrlContent(StatusShortUrl topicShortUrl, String url) {
        if (topicShortUrl != null && topicShortUrl.getUrlMap() != null) {
            StatusUrl topicUrl = topicShortUrl.getUrlMap().get(url);
            if (topicUrl != null) {
                String prefix, suffix;
                switch (topicUrl.getType()) {
                    case StatusUrl.LOCATION:
                        prefix = "➷地点:";
                        break;
                    default:
                        prefix = "➢";
                }

                if (topicUrl.getDisplayName() != null) {
                    suffix = topicUrl.getDisplayName();
                } else {
                    switch (topicUrl.getType()) {
                        case StatusUrl.VIDEO:
                            suffix = "视频";
                            break;
                        case StatusUrl.MUSIC:
                            suffix = "音乐";
                            break;
                        default:
                            suffix = "链接";
                    }
                }
                return prefix + suffix;
            }
        }
        return WEB_URL_REPLACE;
    }

    public static String addRetweetedStatusNamePrefix(Status status) {
        String str;
        if (!TextUtils.isEmpty(status.getUserInfo().getName())) {
            //如果微博已经被删除，则名字为空
            str = "@" + status.getUserInfo().getName() + ":" + status.getContent();
        } else {
            str = status.getContent();
        }
        return str;
    }

    public static String addRetweetedStatusNamePrefix(StatusComment statusComment) {
        String str;
        if (!TextUtils.isEmpty(statusComment.getUserInfo().getName())) {
            //如果微博已经被删除，则名字为空
            str = "@" + statusComment.getUserInfo().getName() + ":" + statusComment.getContent();
        } else {
            str = statusComment.getContent();
        }
        return str;
    }

    public static String addReplyCommentNamePrefix(StatusComment statusComment) {
        String str;
        if (!TextUtils.isEmpty(statusComment.getUserInfo().getName())) {
            //如果不是回复别人的评论-@XXXXX:
            //如果是回复别人的评论-@XXXX 回复@XXX
            str = "@" + statusComment.getUserInfo().getName() +
                    (statusComment.isReplyed() ? " " : ":") + statusComment.getContent();
        } else {
            str = statusComment.getContent();
        }
        return str;
    }

    private static void addEmotions(int textSize, Spannable value) {
        EmoticonUtil.addEmoticon(BaseApplication.getInstance(), value, textSize);
    }

//    private static void addCustomEmotions(Spannable value) {
//        Matcher localMatcher = EMOTION_URL.matcher(value);
//        while (localMatcher.find()) {
//            String str = localMatcher.group();
//            int start = localMatcher.start();
//            int end = localMatcher.end();
//            if (end - start < 10) {
//                //表情文字描述长度都小于10
//                Bitmap bitmap = Emoticon.getInstance().getEmoticonBitmap(str);
//                if (bitmap != null) {
//                    CustomContentSpan customContentSpan = new CustomContentSpan(start, end, str);
////                    ImageSpan localImageSpan = new ImageSpan(ApplicationUtil.getContext(), bitmap, ImageSpan.ALIGN_BOTTOM);
//                    EmotionSpan emotionSpan = new EmotionSpan(customContentSpan, ApplicationUtil.getContext(), bitmap, ImageSpan.ALIGN_BOTTOM);
//                    value.setSpan(emotionSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }
//            }
//        }
//    }

    public static List<SimpleContentSpan> convertNormalStringToSimpleContentUrlSpans(int textSize, Spannable value) {

        if (value == null || value.length() == 0) {
            return null;
        }

//        SpannableString value = SpannableString.valueOf(source);

        Linkify.addLinks(value, WEB_URL, WEB_SCHEME);

        //添加表情
        addEmotions(textSize, value);

        Linkify.addLinks(value, MENTION_URL, MENTION_SCHEME);
        Linkify.addLinks(value, STATUS_URL, TOPIC_SCHEME);

        URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
        if (urlSpans == null || urlSpans.length == 0) {
            return null;
        }

        List<SimpleContentSpan> simpleContentSpans = new ArrayList<>();
        for (URLSpan urlSpan : urlSpans) {
            int start = value.getSpanStart(urlSpan);
            int end = value.getSpanEnd(urlSpan);

            if (start >= 0 && end >= 0 && value.length() >= end) {
                value.removeSpan(urlSpan);
                StatusContentUrlSpan topicContentUrlSpan = new StatusContentUrlSpan(start, end, urlSpan.getURL());
                simpleContentSpans.add(topicContentUrlSpan);
                value.setSpan(topicContentUrlSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }

        return simpleContentSpans;
    }
}








