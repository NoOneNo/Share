package com.hengye.share.module.status;

import com.hengye.share.model.Status;
import com.hengye.share.model.StatusComments;
import com.hengye.share.model.StatusShortUrl;
import com.hengye.share.model.StatusUrl;
import com.hengye.share.model.sina.WBShortUrl;
import com.hengye.share.model.sina.WBShortUrls;
import com.hengye.share.model.sina.WBStatusComments;
import com.hengye.share.model.sina.WBStatuses;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
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
import io.reactivex.functions.Function;

import static com.hengye.share.util.DataUtil.WEB_URL;

/**
 * Created by yuhy on 2016/11/10.
 */

public class StatusRxUtil {

    static Function<WBStatuses, SingleSource<ArrayList<Status>>> mFlatWBStatuses;

    /**
     * 把微博实体转换成自定义的实体
     * @return
     */
    public static Function<WBStatuses, SingleSource<ArrayList<Status>>> flatWBStatuses() {
        if (mFlatWBStatuses == null) {
            mFlatWBStatuses = new Function<WBStatuses, SingleSource<ArrayList<Status>>>() {
                @Override
                public SingleSource<ArrayList<Status>> apply(WBStatuses wbStatuses) {
                    return SingleHelper.justArrayList(Status.getStatuses(wbStatuses));
                }
            };
        }
        return mFlatWBStatuses;
    }

    static Function<WBStatusComments, SingleSource<ArrayList<Status>>> mFlatWBStatusComments;

    public static Function<WBStatusComments, SingleSource<ArrayList<Status>>> flatWBStatusComments() {
        if (mFlatWBStatusComments == null) {
            mFlatWBStatusComments = new Function<WBStatusComments, SingleSource<ArrayList<Status>>>() {
                @Override
                public SingleSource<ArrayList<Status>> apply(WBStatusComments wbComments) {
                    return SingleHelper.justArrayList(Status.getStatuses(wbComments));
                }
            };
        }
        return mFlatWBStatusComments;
    }

    static Function<ArrayList<Status>, SingleSource<ArrayList<Status>>> mFlatStatusShortUrl;

    /**
     * 把微博的短链转换成长链
     */
    public static Function<ArrayList<Status>, SingleSource<ArrayList<Status>>> flatStatusShortUrl() {
        if (mFlatStatusShortUrl == null) {
            mFlatStatusShortUrl = new Function<ArrayList<Status>, SingleSource<ArrayList<Status>>>() {
                @Override
                public SingleSource<ArrayList<Status>> apply(ArrayList<Status> topics) {
                    //获得所有微博，再转换短链，因为微博可能包含转发的微博
                    flatShortUrl(Status.getAllStatus(topics));
                    return SingleHelper.justArrayList(topics);
                }
            };
        }
        return mFlatStatusShortUrl;
    }

    static Function<StatusComments, SingleSource<StatusComments>> mFlatStatusCommentsShortUrl;

    /**
     * 把微博的短链转换成长链
     */
    public static Function<StatusComments, SingleSource<StatusComments>> flatStatusCommentsShortUrl() {
        if (mFlatStatusCommentsShortUrl == null) {
            mFlatStatusCommentsShortUrl = new Function<StatusComments, SingleSource<StatusComments>>() {
                @Override
                public SingleSource<StatusComments> apply(StatusComments topicComments) {
                    if(topicComments != null && !CommonUtil.isEmpty(topicComments.getComments())) {
                        flatShortUrl(topicComments.getComments());
                    }

                    if(topicComments == null){
                        topicComments = new StatusComments();
                    }
                    return Single.just(topicComments);
                }
            };
        }
        return mFlatStatusCommentsShortUrl;
    }


    private static void flatShortUrl(Collection<? extends StatusShortUrl> topics) {
        if (topics != null) {
            Map<StatusShortUrl, Set<String>> contents = new HashMap<>();

            //解析微博的url
            for (StatusShortUrl topicShortUrl : topics) {

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
                    Set<Map.Entry<StatusShortUrl, Set<String>>> entrySet = contents.entrySet();
                    for (Map.Entry<StatusShortUrl, Set<String>> entry : entrySet) {
                        StatusShortUrl topic = entry.getKey();

                        if (topic.getContent() == null) {
                            continue;
                        }

                        HashMap<String, StatusUrl> topicUrlMap = new HashMap<>();
                        for (String shortUrl : entry.getValue()) {
                            WBShortUrl wbShortUrl = shortExpandUrlMap.get(shortUrl);
                            if (wbShortUrl != null && wbShortUrl.getUrl_long() != null) {
                                //保留短链内容
                                topicUrlMap.put(wbShortUrl.getUrl_short(), StatusUrl.create(topic.getId(), wbShortUrl));
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
