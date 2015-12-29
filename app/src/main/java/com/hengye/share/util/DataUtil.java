package com.hengye.share.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.view.View;

import com.hengye.share.BuildConfig;
import com.hengye.share.adapter.recyclerview.CommonAdapter;
import com.hengye.share.module.Topic;
import com.hengye.share.module.TopicComment;
import com.hengye.share.ui.emoticon.Emoticon;
import com.hengye.share.ui.support.TopicContentUrlSpan;
import com.hengye.share.util.thirdparty.WBUtil;
import com.hengye.swiperefresh.PullToRefreshLayout;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {


    public final static int REFRESH_NO_MORE_DATA = 1;//刷新前有内容，刷新后没有新的内容
    public final static int REFRESH_NO_DATA = 2;//刷新前后都没有内容
    public final static int REFRESH_DATA_SIZE_LESS = 3;//刷新结果小于请求条数
    public final static int REFRESH_DATA_SIZE_EQUAL = 4;//刷新结果大于或等于请求条数
    public final static int LOAD_NO_DATA = 5;//没有数据可加载
    public final static int LOAD_NO_MORE_DATA = 6;//没有更多的数据可加载，即结果小于请求条数
    public final static int LOAD_DATA_SIZE_EQUAL = 7;//加载结果大于或等于请求条数

    public static <T> int handlePagingData(List<T> adapterData, List<T> data, boolean isRefresh) {

        if (isRefresh) {
            //下拉刷新
            if (CommonUtil.isEmptyCollection(data)) {
                //没有内容更新
                if (CommonUtil.isEmptyCollection(adapterData)) {
                    return REFRESH_NO_DATA;
                } else {
                    return REFRESH_NO_MORE_DATA;
                }
            } else if (data.size() < WBUtil.MAX_COUNT_REQUEST) {
                //结果小于请求条数
                return REFRESH_DATA_SIZE_LESS;
            } else {
                //结果大于或等于请求条数
                return REFRESH_DATA_SIZE_EQUAL;
            }
        } else {
            //上拉加载
            if (CommonUtil.isEmptyCollection(data)) {
                //没有数据可供加载
                return LOAD_NO_DATA;
            } else {
                //成功加载更多
                if (data.size() < WBUtil.MAX_COUNT_REQUEST) {
                    //没有更多的数据可供加载
                    return LOAD_NO_MORE_DATA;
                }else{
                    return LOAD_DATA_SIZE_EQUAL;
                }
            }
        }

    }

    public static <T, VH extends CommonAdapter.ItemViewHolder>
    void handleCommonAdapter(int type, CommonAdapter<T, VH> adapter, List<T> data){
        switch (type){
            case REFRESH_DATA_SIZE_LESS:
                adapter.addAll(0, data);
                break;
            case REFRESH_DATA_SIZE_EQUAL:
                adapter.refresh(data);
                break;
            case LOAD_NO_MORE_DATA:
            case LOAD_DATA_SIZE_EQUAL:
                adapter.addAll(data);
                break;
        }
    }

    public static <VH extends CommonAdapter.ItemViewHolder>
    void handleTopicAdapter(int type, CommonAdapter<Topic, VH> adapter, List<Topic> data){
        switch (type){
            case REFRESH_DATA_SIZE_LESS:
                adapter.addAll(0, data);
                break;
            case REFRESH_DATA_SIZE_EQUAL:
                adapter.refresh(data);
                break;
            case LOAD_NO_MORE_DATA:
            case LOAD_DATA_SIZE_EQUAL:
                //因为请求的数据是小于或等于max_id，需要做是否重复判断处理
                if (data.get(0).getId() != null && data.get(0).getId().
                        equals(CommonUtil.getLastItem(adapter.getData()).getId())) {
                    data.remove(0);
                }
                adapter.addAll(data);
                break;
        }
    }

    public static void handleSnackBar(int type, View v, int size){
        switch (type) {
            case REFRESH_NO_MORE_DATA:
                ToastUtil.showSnackBar("没有新的微博", v);
                break;
            case REFRESH_NO_DATA:
                ToastUtil.showSnackBar("暂时没有微博", v);
                break;
            case REFRESH_DATA_SIZE_LESS:
                ToastUtil.showSnackBar(size + "条新微博", v);
                //存储数据
                break;
            case REFRESH_DATA_SIZE_EQUAL:
                ToastUtil.showSnackBar("超过" + WBUtil.MAX_COUNT_REQUEST + "条新微博", v);
                break;
            case LOAD_NO_DATA:
            case LOAD_NO_MORE_DATA:
                ToastUtil.showSnackBar("已经是最后内容", v);
                break;
        }
    }

    public static void handlePullToRefresh(int type, PullToRefreshLayout pullToRefreshLayout) {
        switch (type) {
            case REFRESH_DATA_SIZE_EQUAL:
                pullToRefreshLayout.setLoadEnable(true);
                break;
            case LOAD_NO_DATA:
                pullToRefreshLayout.setLoadEnable(false);
                break;
            case LOAD_NO_MORE_DATA:
                pullToRefreshLayout.setLoadEnable(false);
                break;
        }
    }


    public static final Pattern WEB_URL = Pattern
            .compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]");
    public static final Pattern TOPIC_URL = Pattern
            .compile("#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#");
    public static final Pattern MENTION_URL = Pattern
            .compile("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}");
    public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

    public static final String WEB_SCHEME = "http://";
    public static final String TOPIC_SCHEME = BuildConfig.APPLICATION_ID + ".topic://";
    public static final String MENTION_SCHEME = BuildConfig.APPLICATION_ID + ".mention://";

    public static void addTopicContentHighLightLinks(Context context, TopicComment topicComment) {
        topicComment.setUrlSpannableString(convertNormalStringToSpannableString(context, topicComment.getContent()));
    }

    public static void addTopicContentHighLightLinks(Context context, Topic topic) {
        topic.setUrlSpannableString(convertNormalStringToSpannableString(context, topic.getContent()));

        if (topic.getRetweetedTopic() != null) {
            String str = addRetweetedNamePrefix(topic.getRetweetedTopic());
            topic.getRetweetedTopic().setUrlSpannableString(convertNormalStringToSpannableString(context, str));
        }
    }

    public static SpannableString convertNormalStringToSpannableString(Context context, String txt) {
        //hack to fix android imagespan bug,see http://stackoverflow.com/questions/3253148/imagespan-is-cut-off-incorrectly-aligned
        //if string only contains emotion tags,add a empty char to the end
        String hackTxt;
        if (txt.startsWith("[") && txt.endsWith("]")) {
            hackTxt = txt + " ";
        } else {
            hackTxt = txt;
        }

        SpannableString value = SpannableString.valueOf(hackTxt);

        //添加表情
        addEmotions(context, value);

        Linkify.addLinks(value, MENTION_URL, MENTION_SCHEME);
        Linkify.addLinks(value, WEB_URL, WEB_SCHEME);
        Linkify.addLinks(value, TOPIC_URL, TOPIC_SCHEME);

        URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
        if(urlSpans == null || urlSpans.length == 0){
            return value;
        }

        TopicContentUrlSpan topicContentUrlSpan = null;
        for (URLSpan urlSpan : urlSpans) {
            topicContentUrlSpan = new TopicContentUrlSpan(urlSpan.getURL());
            int start = value.getSpanStart(urlSpan);
            int end = value.getSpanEnd(urlSpan);
            value.removeSpan(urlSpan);
            value.setSpan(topicContentUrlSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return value;
    }

    private static String addRetweetedNamePrefix(Topic topic) {
        String str;
        if (!TextUtils.isEmpty(topic.getUserInfo().getName())) {
            //如果微博已经被删除，则名字为空
            str = "@" + topic.getUserInfo().getName() + ":" + topic.getContent();
        } else {
            str = topic.getContent();
        }
        return str;
    }

    private static void addEmotions(Context context, SpannableString value) {
        Matcher localMatcher = EMOTION_URL.matcher(value);
        while (localMatcher.find()) {
            String str = localMatcher.group();
            int start = localMatcher.start();
            int end = localMatcher.end();
            if (end - start < 8) {
                Bitmap bitmap = Emoticon.getInstance().getEmoticonBitmap().get(str);
                if (bitmap != null) {
                    ImageSpan localImageSpan = new ImageSpan(context, bitmap, ImageSpan.ALIGN_BOTTOM);
                    value.setSpan(localImageSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }
}



























