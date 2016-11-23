package com.hengye.share.module.topic;

import com.hengye.share.model.Topic;
import com.hengye.share.model.TopicUrl;
import com.hengye.share.model.sina.WBShortUrl;
import com.hengye.share.model.sina.WBShortUrls;
import com.hengye.share.util.L;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.retrofit.RetrofitManager;
import com.hengye.share.util.rxjava.datasource.ObservableHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import retrofit2.Call;
import retrofit2.Response;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

import static com.hengye.share.util.DataUtil.WEB_URL;

/**
 * Created by yuhy on 2016/11/10.
 */

public class TopicRxUtil {


    static Function<ArrayList<Topic>, Observable<ArrayList<Topic>>> mFlatShortUrl;

    /**
     * 把微博的短链转换成长链
     */
    public static Function<ArrayList<Topic>, Observable<ArrayList<Topic>>> flatShortUrl() {
        if (mFlatShortUrl == null) {
            mFlatShortUrl = new Function<ArrayList<Topic>, Observable<ArrayList<Topic>>>() {
                @Override
                public Observable<ArrayList<Topic>> apply(ArrayList<Topic> topics) {

                    if (topics != null) {
                        Map<Topic, Set<String>> contents = new HashMap<>();

                        //获得所有微博
                        Set<Topic> topicSet = Topic.getAllTopic(topics);
                        if (topicSet != null) {

                            //解析微博的url
                            for (Topic topic : topicSet) {

                                Matcher matcher = WEB_URL.matcher(topic.getContent());
                                Set<String> set = contents.get(topic);
                                while (matcher.find()) {
                                    if (set == null) {
                                        set = new HashSet<>();
                                        contents.put(topic, set);
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
                                    Set<Map.Entry<Topic, Set<String>>> entrySet = contents.entrySet();
                                    for (Map.Entry<Topic, Set<String>> entry : entrySet) {
                                        Topic topic = entry.getKey();

                                        if (topic.getContent() == null) {
                                            continue;
                                        }

                                        HashMap<String, TopicUrl> topicUrlMap = new HashMap<>();
                                        for (String shortUrl : entry.getValue()) {
                                            WBShortUrl wbShortUrl = shortExpandUrlMap.get(shortUrl);
                                            if (wbShortUrl != null && wbShortUrl.getUrl_long() != null) {
                                                topic.setContent(topic.getContent().replace(shortUrl, wbShortUrl.getUrl_long()));
                                                topicUrlMap.put(wbShortUrl.getUrl_long(), TopicUrl.getTopicUrl(topic.getId(), wbShortUrl));
//                                                L.debug("shortUrl : {}", shortUrl);
//                                                L.debug("convert to");
//                                                L.debug("longUrl : {}", longUrl);
                                            }
                                        }
                                        topic.setUrlMap(topicUrlMap);
                                    }
                                    L.debug("convert shortUrl to longUrl, total count : {}", shortExpandUrlMap.size());
                                }

                            }
                        }
                    }

                    return ObservableHelper.justArrayList(topics);
                }
            };
        }
        return mFlatShortUrl;
    }
}
