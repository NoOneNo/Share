package com.hengye.share.module.topic;

import com.hengye.share.model.Topic;
import com.hengye.share.model.TopicComments;
import com.hengye.share.model.TopicShortUrl;
import com.hengye.share.model.TopicUrl;
import com.hengye.share.model.sina.WBShortUrl;
import com.hengye.share.model.sina.WBShortUrls;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.ObservableHelper;
import com.hengye.share.util.rxjava.datasource.SingleHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import retrofit2.Call;
import retrofit2.Response;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static com.hengye.share.util.DataUtil.WEB_URL;

/**
 * Created by yuhy on 2016/11/10.
 */

public class TopicRxUtil {


    static Function<ArrayList<Topic>, SingleSource<ArrayList<Topic>>> mFlatTopicShortUrl;

    /**
     * 把微博的短链转换成长链
     */
    public static Function<ArrayList<Topic>, SingleSource<ArrayList<Topic>>> flatShortUrl() {
        if (mFlatTopicShortUrl == null) {
            mFlatTopicShortUrl = new Function<ArrayList<Topic>, SingleSource<ArrayList<Topic>>>() {
                @Override
                public SingleSource<ArrayList<Topic>> apply(ArrayList<Topic> topics) {
                    //获得所有微博，再转换短链，因为微博可能包含转发的微博
                    flatShortUrl(Topic.getAllTopic(topics));
                    return SingleHelper.justArrayList(topics);
                }
            };
        }
        return mFlatTopicShortUrl;
    }

    static Function<TopicComments, SingleSource<TopicComments>> mFlatTopicCommentsShortUrl;

    /**
     * 把微博的短链转换成长链
     */
    public static Function<TopicComments, SingleSource<TopicComments>> flatTopicCommentsShortUrl() {
        if (mFlatTopicCommentsShortUrl == null) {
            mFlatTopicCommentsShortUrl = new Function<TopicComments, SingleSource<TopicComments>>() {
                @Override
                public SingleSource<TopicComments> apply(TopicComments topicComments) {
                    if(topicComments != null && !CommonUtil.isEmpty(topicComments.getComments())) {
                        flatShortUrl(topicComments.getComments());
                    }

                    if(topicComments == null){
                        topicComments = new TopicComments();
                    }
                    return Single.just(topicComments);
                }
            };
        }
        return mFlatTopicCommentsShortUrl;
    }


    public static void flatShortUrl(Collection<? extends TopicShortUrl> topics) {
        if (topics != null) {
            Map<TopicShortUrl, Set<String>> contents = new HashMap<>();

            //解析微博的url
            for (TopicShortUrl topicShortUrl : topics) {

                Matcher matcher = WEB_URL.matcher(topicShortUrl.getContent());
                Set<String> set = contents.get(topicShortUrl);
                while (matcher.find()) {
                    if (set == null) {
                        set = new HashSet<>();
                        contents.put(topicShortUrl, set);
                    }
                    set.add(matcher.group());
                }
            }

            if (!contents.isEmpty()) {
                Collection<Set<String>> contentValues = contents.values();

                List<String> shortUrls = new ArrayList<>();
                //把所有短链放到集合里
                for (Set<String> stringSet : contentValues) {
                    shortUrls.addAll(stringSet);
                }

                HashMap<String, WBShortUrl> shortExpandUrlMap = new HashMap<>();
                boolean isFinish = false;

                //因为每次解析短链数量限制，循环获取长链
                while (!isFinish) {

                    if (shortUrls.size() <= 20) {
                        isFinish = true;
                    }
                    int end = isFinish ? shortUrls.size() : 20;

                    final List<String> shortUrlPart = shortUrls.subList(0, end);
                    if (!isFinish) {
                        shortUrls = shortUrls.subList(end, shortUrls.size());
                    }

                    //因为url_short有多个，请求参数用map拼接的话不能重复，所以手动拼接字符串；
                    StringBuilder shortUrlRequest = new StringBuilder(UrlFactory.WB_EXPAND_URL);
                    shortUrlRequest.append("?access_token=");
                    shortUrlRequest.append(UserUtil.getToken());
                    for (String shortUrl : shortUrlPart) {
                        shortUrlRequest.append("&url_short=");
                        shortUrlRequest.append(shortUrl);
                    }
                    Call<WBShortUrls> expandUrlCall = RetrofitManager.getWBService().expandUrl(shortUrlRequest.toString());
                    try {
                        Response<WBShortUrls> response = expandUrlCall.execute();
                        WBShortUrls wbShortUrls = response.body();

                        if (wbShortUrls != null && wbShortUrls.getUrls() != null) {
                            List<WBShortUrl> urls = wbShortUrls.getUrls();

                            for (WBShortUrl wbShortUrl : urls) {
                                shortExpandUrlMap.put(wbShortUrl.getUrl_short(), wbShortUrl);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (!shortExpandUrlMap.isEmpty()) {
                    //把微博的短链换成长链
                    Set<Map.Entry<TopicShortUrl, Set<String>>> entrySet = contents.entrySet();
                    for (Map.Entry<TopicShortUrl, Set<String>> entry : entrySet) {
                        TopicShortUrl topic = entry.getKey();

                        if (topic.getContent() == null) {
                            continue;
                        }

                        HashMap<String, TopicUrl> topicUrlMap = new HashMap<>();
                        for (String shortUrl : entry.getValue()) {
                            WBShortUrl wbShortUrl = shortExpandUrlMap.get(shortUrl);
                            if (wbShortUrl != null && wbShortUrl.getUrl_long() != null) {
                                //保留短链内容
                                topicUrlMap.put(wbShortUrl.getUrl_short(), TopicUrl.create(topic.getId(), wbShortUrl));
//                                L.debug("shortUrl : {} convert to longUrl : {}", wbShortUrl.getUrl_short(), wbShortUrl.getUrl_long());
                            }
                        }
                        topic.setUrlMap(topicUrlMap);
                    }
                    L.debug("convert shortUrl to longUrl, total count : %s", shortExpandUrlMap.size());
                }

            }
        }
    }
}
