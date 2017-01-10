package com.hengye.share.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.hengye.share.R;
import com.hengye.share.model.Topic;
import com.hengye.share.model.TopicComment;
import com.hengye.share.model.TopicPublish;
import com.hengye.share.model.greenrobot.TopicDraft;
import com.hengye.share.model.greenrobot.TopicDraftHelper;
import com.hengye.share.model.sina.WBTopic;
import com.hengye.share.model.sina.WBTopicComment;
import com.hengye.share.model.sina.WBUploadPicture;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.util.ApplicationUtil;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.NotificationUtil;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.ViewUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.http.retrofit.api.WBService;
import com.hengye.share.util.rxjava.DefaultSubscriber;
import com.hengye.share.util.rxjava.ObjectConverter;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class TopicPublishService extends Service {

    public static final int MAX_PUBLISH_SIZE = 10;
    public static final String ACTION_PUBLISH = "PUBLISH_ACTION";
    public static final String EXTRA_DATA = "PUBLISH_DATA";
    public static final String EXTRA_DRAFT = "TOPIC_DRAFT";
    public static final String EXTRA_TYPE = "TOPIC_TYPE";
    public static final String EXTRA_STATUS = "PUBLISH_STATUS";

    public static final int STATUS_SENDING = 1;
    public static final int STATUS_SUCCESS = 2;
    public static final int STATUS_FAIL = 3;
    public static final int STATUS_TIMING = 4;

    private HashMap<TopicPublish, Boolean> mPublishQueue = new HashMap<>();
    private HashMap<TopicPublish, Integer> mPublishNotificationQueue = new HashMap<>();

    private Handler mHandler = new Handler();
    private LocalBroadcastManager mLocalBroadcastManager;

    public static void publish(Context context, TopicDraft topicDraft) {
        publish(context, topicDraft, UserUtil.getToken());
    }

    public static void publish(Context context, TopicDraft topicDraft, String token) {
        Intent intent = new Intent(context, TopicPublishService.class);
        intent.putExtra("topicDraft", topicDraft);
        intent.putExtra("token", token);
        context.startService(intent);
    }

    protected LocalBroadcastManager getLocalBroadcastManager() {
        if (mLocalBroadcastManager == null) {
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
        }
        return mLocalBroadcastManager;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.debug("TopicPublishService onCreate invoke");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.debug("TopicPublishService onStartCommand invoke");

        TopicDraft topicDraft = (TopicDraft) intent.getSerializableExtra("topicDraft");
        String token = intent.getStringExtra("token");
        if (topicDraft != null && !TextUtils.isEmpty(token)) {
            addTopicPublishRequestToQueue(TopicPublish.getWBTopicPublish(topicDraft, token));
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        L.debug("TopicPublishService onDestroy invoke");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void addTopicPublishRequestToQueue(TopicPublish tp) {
        handlePublishStart(tp);
    }

    protected void publish(TopicPublish tp) {
        try {
            switch (tp.getTopicDraft().getType()) {
                case TopicDraftHelper.PUBLISH_COMMENT:
                    publishWBComment(tp);
                    break;
                case TopicDraftHelper.REPLY_COMMENT:
                    replyWBComment(tp);
                    break;
                case TopicDraftHelper.REPOST_TOPIC:
                    repostWBTopic(tp);
                    break;
                case TopicDraftHelper.PUBLISH_TOPIC:
                default:
                    publishWBTopic(tp);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            handlePublishFail(tp);
        }
    }

    protected void handlePublishStart(TopicPublish tp) {
        TopicDraft td = tp.getTopicDraft();
        if (td.isPublishTiming() && !td.isTimingExpired()) {
            //如果是定时发送
            publishTimingTopic(tp);
            return;
        }
        mPublishQueue.put(tp, false);
        showTopicPublishStartNotification(tp);
        TopicDraftHelper.saveTopicDraft(tp.getTopicDraft(), TopicDraft.SENDING);
        sendBroadcast(tp, STATUS_SENDING, null);
        publish(tp);
    }

    protected void handlePublishSuccess(TopicPublish tp, Serializable result) {
        mPublishQueue.put(tp, true);
        showTopicPublishSuccessNotification(tp);
        TopicDraftHelper.removeTopicDraft(tp.getTopicDraft());
        stopServiceIfQueueIsAllFinish();
        sendBroadcast(tp, STATUS_SUCCESS, result);
        handlePublishFinish();
    }

    protected void handlePublishFail(TopicPublish tp) {
        mPublishQueue.remove(tp);
        showTopicPublishFailNotification(tp);
        TopicDraftHelper.saveTopicDraft(tp.getTopicDraft(), TopicDraft.ERROR);
        stopServiceIfQueueIsAllFinish();
        sendBroadcast(tp, STATUS_FAIL, null);
        handlePublishFinish();
    }

    protected void handlePublishFinish() {
        if (SettingHelper.isVibrationFeedBack()) {
            ViewUtil.vibrate();
        }
    }

    protected void sendBroadcast(TopicPublish tp, int status, Serializable data){
        TopicDraft draft = tp.getTopicDraft();

        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_TYPE, draft.getType());
        bundle.putInt(EXTRA_STATUS, status);
        bundle.putSerializable(EXTRA_DRAFT, draft);
        bundle.putSerializable(EXTRA_DATA, data);

        Intent intent = new Intent(ACTION_PUBLISH);
        intent.putExtras(bundle);
        sendLocalBroadcast(intent);

        if(!CommonUtil.isEmpty(draft.getTargetTopicId())){
            Intent intentWithType = new Intent(ACTION_PUBLISH + CommonUtil.UNDERLINE + draft.getTargetTopicId());
            intentWithType.putExtras(bundle);
            sendLocalBroadcast(intentWithType);
        }
    }

    protected void sendLocalBroadcast(Intent intent){
        getLocalBroadcastManager().sendBroadcast(intent);
    }

    protected void publishTimingTopic(final TopicPublish tp) {

        TopicDraftHelper.saveTopicDraft(tp.getTopicDraft(), TopicDraft.TIMING);
        sendBroadcast(tp, STATUS_TIMING, null);
        List<TopicDraft> drafts = TopicDraftHelper.getTimingTopicDraft();
        AlarmManager am = (AlarmManager) ApplicationUtil.getContext().getSystemService(ALARM_SERVICE);
        for (TopicDraft draft : drafts) {
            if(!draft.isTimingExpired()){
                TopicPublishTimingIntent intent = new TopicPublishTimingIntent(draft.getTiming());
                int requestCode = (int)draft.getTiming();
                PendingIntent sender = PendingIntent.getBroadcast(ApplicationUtil.getContext(), requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                am.set(AlarmManager.RTC_WAKEUP, draft.getTiming(), sender);
            }else{
                //定时任务已过期
                TopicDraftHelper.saveTopicDraft(tp.getTopicDraft(), TopicDraft.ERROR);
                L.debug("TimingTask is expired");
            }
        }
    }

    protected void publishWBTopic(final TopicPublish tp) throws Exception {
        if (tp.getTopicDraft().getUrls() != null) {
            publishWBTopicWithPhoto(tp);
        } else {
            publishWBTopicContentOnly(tp);
        }
    }

    protected void publishWBTopicContentOnly(final TopicPublish tp) {
        RetrofitManager
                .getWBService()
                .publishTopic(UrlFactory.getPublishTopicParams(tp))
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new PublishTopicSubscriber(tp));
    }

    protected void publishWBTopicWithPhoto(final TopicPublish tp) throws Exception {
        List<String> urls = tp.getTopicDraft().getUrlList();

        if (urls == null) {
            throw new Exception("photo url is invalid!");
        }
        if (urls.size() == 1) {
            publishWBTopicWithSinglePhoto(tp);
        } else {
            publishWBTopicWithMultiplePhoto(tp, urls);
        }
    }

    protected void publishWBTopicWithSinglePhoto(final TopicPublish tp) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"),
                new File(tp.getTopicDraft().getUrls()));

        MultipartBody.Part body = MultipartBody.Part.createFormData("pic", "share.png", requestFile);
        RetrofitManager
                .getWBService()
                .publishTopicWithSinglePhoto(
                        UrlFactory.getPublishTopicParamsWrapToMultiPart(tp),
                        body)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new PublishTopicSubscriber(tp));
    }

//    protected void publishWBTopicWithSinglePhoto(final TopicPublish tp) {
//        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"),
//                new File(tp.getTopicDraft().getUrls()));
//        MultipartBody.Part body = MultipartBody.Part.createFormData("pic", "share.png", requestFile);
//        RetrofitManager
//                .getWBService()
//                .publishTopicWithSinglePhoto(
//                        RequestBody.create(MediaType.parse("multipart/form-data"),
//                                tp.getToken()),
//                        RequestBody.create(MediaType.parse("multipart/form-data"),
//                                tp.getTopicDraft().getContent()),
//                        body)
//                .subscribeOn(SchedulerProvider.io())
//                .observeOn(SchedulerProvider.ui())
//                .subscribe(new PublishTopicSubscriber(tp));
//    }

    protected void publishWBTopicWithMultiplePhoto(final TopicPublish tp, List<String> urls) throws Exception {

        Observable.zip(
                Observable
                        .fromIterable(urls)
                        .map(new Function<String, Observable<WBUploadPicture>>() {
                            @Override
                            public Observable<WBUploadPicture> apply(String s) {

                                RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"),
                                        new File(s));
                                MultipartBody.Part body = MultipartBody.Part.createFormData("pic", "share.png", requestFile);
                                WBService service = RetrofitManager.getWBService();
                                return service.uploadPicture(RequestBody.create(MediaType.parse("multipart/form-data"),
                                        UserUtil.getPriorToken()), body);
                            }
                        })
                , new Function<Object[], List<String>>() {
                    @Override
                    public List<String> apply(Object[] objects) throws Exception {
                        if (objects == null) {
                            return null;
                        }
                        List<String> urls = new ArrayList<>();
                        for (Object obj : objects) {
                            urls.add(((WBUploadPicture) obj).getPic_id());
                        }
                        return urls;
                    }
                })
                .flatMap(new Function<List<String>, Observable<WBTopic>>() {
                    @Override
                    public Observable<WBTopic> apply(List<String> urls) {

                        tp.setToken(UserUtil.getPriorToken());
                        return RetrofitManager
                                .getWBService()
                                .publishTopicWithMultiplePhoto(UrlFactory.getPublishTopicParams(tp, CommonUtil.toSplit(urls, TopicDraft.DELIMITER_URL)));
//                        return RetrofitManager
//                                .getWBService()
//                                .publishTopicWithMultiplePhoto(UserUtil.getPriorToken(), tp.getTopicDraft().getContent(), CommonUtil.toSplit(urls, ","));

                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new PublishTopicSubscriber(tp));

    }

    protected void publishWBComment(final TopicPublish tp) {
        if (tp.getTopicDraft().isCommentOrRepostConcurrently()) {
            WBService service = RetrofitManager.getWBService();

            Observable.zip(
                    service.publishComment(tp.getToken(), tp.getTopicDraft().getContent(), tp.getTopicDraft().getTargetTopicId(), 0),
                    service.repostTopic(tp.getToken(), tp.getTopicDraft().getRepostContent(), tp.getTopicDraft().getTargetTopicId(), 0),
                    ObjectConverter.getObjectConverter2())
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe(new PublishCommentAndRepostSubscriber(tp));

        } else {
            RetrofitManager
                    .getWBService()
                    .publishComment(tp.getToken(), tp.getTopicDraft().getContent(), tp.getTopicDraft().getTargetTopicId(), tp.getTopicDraft().getIsCommentOrigin())
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe(new PublishCommentSubscriber(tp));
        }
    }

    protected void repostWBTopic(final TopicPublish tp) {
        RetrofitManager
                .getWBService()
                .repostTopic(tp.getToken(), tp.getTopicDraft().getContent(), tp.getTopicDraft().getTargetTopicId(), tp.getTopicDraft().getIsCommentOrigin())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new PublishRepostSubscriber(tp));
    }

    protected void replyWBComment(final TopicPublish tp) {
        if (tp.getTopicDraft().isCommentOrRepostConcurrently()) {
            WBService service = RetrofitManager.getWBService();
            Observable.zip(
                    service.replyComment(tp.getToken(), tp.getTopicDraft().getContent(), tp.getTopicDraft().getTargetTopicId(), tp.getTopicDraft().getTargetCommentId(), 0),
                    service.repostTopic(tp.getToken(), tp.getTopicDraft().getRepostContent(), tp.getTopicDraft().getTargetTopicId(), 0),
                    ObjectConverter.getObjectConverter2())
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe(new PublishCommentAndRepostSubscriber(tp));
        } else {
            RetrofitManager
                    .getWBService()
                    .replyComment(tp.getToken(), tp.getTopicDraft().getContent(), tp.getTopicDraft().getTargetTopicId(), tp.getTopicDraft().getTargetCommentId(), tp.getTopicDraft().getIsCommentOrigin())
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe(new PublishCommentSubscriber(tp));
        }
    }


    public abstract class PublishSubscriber<T> extends DefaultSubscriber<T> {

        TopicPublish tp;

        public PublishSubscriber(TopicPublish tp) {
            this.tp = tp;
        }

        @Override
        public void onComplete() {
            L.debug("onCompleted invoke");
        }

        @Override
        public void onError(Throwable e) {
            L.debug("request fail, error : {}", e);
            handlePublishFail(tp);
        }

    }

    public class PublishTopicSubscriber extends PublishSubscriber<WBTopic> {

        public PublishTopicSubscriber(TopicPublish tp) {
            super(tp);
        }

        @Override
        public void onNext(WBTopic wbTopic) {
            L.debug("request success , data : {}", wbTopic);
            if (wbTopic != null) {
                handlePublishSuccess(tp, Topic.getTopic(wbTopic));
            }
        }
    }

    public class PublishRepostSubscriber extends PublishSubscriber<WBTopic> {

        public PublishRepostSubscriber(TopicPublish tp) {
            super(tp);
        }

        @Override
        public void onNext(WBTopic wbTopic) {
            L.debug("request success , data : {}", wbTopic);
            if (wbTopic != null) {
                handlePublishSuccess(tp, TopicComment.getComment(wbTopic));
            }
        }
    }

    public class PublishCommentSubscriber extends PublishSubscriber<WBTopicComment> {

        public PublishCommentSubscriber(TopicPublish tp) {
            super(tp);
        }

        @Override
        public void onNext(WBTopicComment wbTopicComment) {
            L.debug("request success , data : {}", wbTopicComment);
            if (wbTopicComment != null) {
                handlePublishSuccess(tp, TopicComment.getComment(wbTopicComment));
            }
        }
    }

    public class PublishCommentAndRepostSubscriber extends PublishSubscriber<Object[]> {

        public PublishCommentAndRepostSubscriber(TopicPublish tp) {
            super(tp);
        }

        @Override
        public void onNext(Object[] objects) {
            WBTopicComment wbTopicComment = null;
            WBTopic wbTopic = null;
            if (objects[0] instanceof WBTopicComment) {
                wbTopicComment = (WBTopicComment) objects[0];
            }

            if (objects[1] instanceof WBTopic) {
                wbTopic = (WBTopic) objects[1];
            }

            if (wbTopicComment != null && wbTopic != null) {
                handlePublishSuccess(tp, TopicComment.getComment(wbTopicComment));
            } else {
                L.debug("request fail , wbTopicComment : {}, wbTopic : {}", wbTopicComment, wbTopic);
                throw new IllegalStateException("publish error");
            }
        }
    }

    protected void showTopicPublishStartNotification(TopicPublish tp) {
        Notification.Builder builder = getNotificationBuilder()
                .setTicker(getString(R.string.label_topic_publish_start))
                .setContentTitle(getString(R.string.label_topic_publish_start))
                .setContentText(getString(R.string.label_topic_publish));

        int id = tp.getTopicDraft().getNotificationId();
        if (id == -1) {
            id = new Random().nextInt(Integer.MAX_VALUE);
            tp.getTopicDraft().setNotificationId(id);
        }
        NotificationUtil.show(builder.build(), id);

        addNotificationId(tp, id);
    }

    protected void showTopicPublishSuccessNotification(final TopicPublish tp) {
        Notification.Builder builder = getNotificationBuilder()
                .setTicker(getString(R.string.label_topic_publish_success))
                .setContentTitle(getString(R.string.label_topic_publish_success))
                .setContentText(tp.getTopicDraft().getDesc())
                .setVibrate(new long[0]);
        NotificationUtil.show(builder.build(), getNotificationId(tp));

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NotificationUtil.cancel(getNotificationId(tp));
                removeNotificationId(tp);
            }
        }, 3000);
    }

    protected void showTopicPublishFailNotification(TopicPublish tp) {
//        try {
//            Thread.sleep(2000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Notification.Builder builder = getNotificationBuilder()
                .setTicker(getString(R.string.label_topic_publish_fail))
                .setContentTitle(getString(R.string.label_topic_publish_fail))
                .setContentText(tp.getTopicDraft().getDesc())
                .setVibrate(new long[0]);

        NotificationUtil.show(builder.build(), getNotificationId(tp));
        removeNotificationId(tp);
    }

    protected Notification.Builder getNotificationBuilder() {
        Notification.Builder builder = new Notification.Builder(this)
                .setOnlyAlertOnce(true)
                .setOngoing(false)
                .setPriority(Notification.PRIORITY_HIGH)
                .setSmallIcon(R.drawable.notification_upload_white_48dp);

        if (SettingHelper.isNotifyLightsOn()) {
            builder.setLights(Color.WHITE, 2000, 2000);
        }

        Bitmap bitmap = UserUtil.getCurrentUser().getUserAvatarBitmap();
        if (bitmap != null) {
            builder.setLargeIcon(bitmap);
        }
        return builder;

    }

    protected int getNotificationId(TopicPublish tp) {
        Integer id = mPublishNotificationQueue.get(tp);
        if (id == null) {
            return 0;
        }
        return id;
    }

    protected void addNotificationId(TopicPublish tp, int id) {
        mPublishNotificationQueue.put(tp, id);
    }

    protected void removeNotificationId(TopicPublish tp) {
        mPublishNotificationQueue.remove(tp);
    }

    protected void stopServiceIfQueueIsAllFinish() {
        boolean isAllFinish = true;
        Set<TopicPublish> set = mPublishQueue.keySet();
        for (TopicPublish tp : set) {
            if (!mPublishQueue.get(tp)) {
                isAllFinish = false;
                break;
            }
        }
        if (isAllFinish) {
            stopForeground(true);
            stopSelf();
            L.debug("TopicPublishService QueueIsAllFinish");
        }
    }
}


//    private StringRequest getWBTopicPublishRequest(final TopicPublish tp) {
//
//
//        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBTopicPublishUrl());
//        ub.addParameter("access_token", tp.getToken());
//        ub.addParameter("status", tp.getTopicDraft().getContent());
//        return new StringRequest(Request.Method.POST,
////                WBTopic.class,
//                ub.getUrl()
//                , new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
//                if (response != null) {
//                    showTopicPublishSuccessNotification(tp);
//                    mPublishQueue.put(tp, true);
//                    stopServiceIfQueueIsAllFinish();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), volleyError);
//                mPublishQueue.remove(tp);
//                showTopicPublishFailNotification(tp);
//                TopicDraftHelper.saveTopicDraft(tp.getTopicDraft());
//                stopServiceIfQueueIsAllFinish();
//            }
//        }) {
//            @Override
//            public byte[] getBody() {
//                return ub.getBody();
////                return null;
//            }
//
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//
////            @Override
////            public Map<String, String> getHeaders() throws AuthFailureError {
////                HashMap<String, String> header = new HashMap<>();
////                header.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
////                header.put("Connection", "Keep-Alive");
////                header.put("Charset", "UTF-8");
////                header.put("Accept-Encoding", "gzip, deflate");
////                return header;
////            }
//        };
//    }



























