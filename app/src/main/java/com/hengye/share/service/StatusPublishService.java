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
import com.hengye.share.model.Status;
import com.hengye.share.model.StatusComment;
import com.hengye.share.model.StatusPublish;
import com.hengye.share.model.greenrobot.StatusDraft;
import com.hengye.share.model.greenrobot.StatusDraftHelper;
import com.hengye.share.model.sina.WBStatus;
import com.hengye.share.model.sina.WBStatusComment;
import com.hengye.share.model.sina.WBUploadPicture;
import com.hengye.share.module.draft.StatusDraftActivity;
import com.hengye.share.module.setting.SettingHelper;
import com.hengye.share.util.ApplicationUtil;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.L;
import com.hengye.share.util.NotificationUtil;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.UrlFactory;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.ViewUtil;
import com.hengye.share.util.http.retrofit.RetrofitManager;
import com.hengye.share.util.http.retrofit.api.WBService;
import com.hengye.share.util.rxjava.ObjectConverter;
import com.hengye.share.util.rxjava.schedulers.SchedulerProvider;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleSource;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class StatusPublishService extends Service {

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

    private HashMap<StatusPublish, Boolean> mPublishQueue = new HashMap<>();
    private HashMap<StatusPublish, Integer> mPublishNotificationQueue = new HashMap<>();

    private Handler mHandler = new Handler();
    private LocalBroadcastManager mLocalBroadcastManager;

    public static void publish(Context context, StatusDraft statusDraft) {
        publish(context, statusDraft, UserUtil.getToken());
    }

    public static void publish(Context context, StatusDraft statusDraft, String token) {
        Intent intent = new Intent(context, StatusPublishService.class);
        intent.putExtra("topicDraft", statusDraft);
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
        L.debug("StatusPublishService onCreate invoke");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.debug("StatusPublishService onStartCommand invoke");

        StatusDraft statusDraft = (StatusDraft) intent.getSerializableExtra("topicDraft");
        String token = intent.getStringExtra("token");
        if (statusDraft != null && !TextUtils.isEmpty(token)) {
            addPublishStatusRequestToQueue(StatusPublish.getWBStatusPublish(statusDraft, token));
        }

        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        L.debug("StatusPublishService onDestroy invoke");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected void addPublishStatusRequestToQueue(StatusPublish sp) {
        handlePublishStart(sp);
    }

    protected void publish(StatusPublish sp) {
        try {
            switch (sp.getStatusDraft().getType()) {
                case StatusDraftHelper.PUBLISH_COMMENT:
                    publishWBComment(sp);
                    break;
                case StatusDraftHelper.REPLY_COMMENT:
                    replyWBComment(sp);
                    break;
                case StatusDraftHelper.REPOST_TOPIC:
                    repostWBStatus(sp);
                    break;
                case StatusDraftHelper.PUBLISH_TOPIC:
                default:
                    publishWBStatus(sp);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            handlePublishFail(sp);
        }
    }

    protected void handlePublishStart(StatusPublish sp) {
        StatusDraft sd = sp.getStatusDraft();
        if (sd.isPublishTiming() && !sd.isTimingExpired()) {
            //如果是定时发送
            publishTimingStatus(sp);
            return;
        }
        mPublishQueue.put(sp, false);
        showPublishStartNotification(sp);
        StatusDraftHelper.saveStatusDraft(sp.getStatusDraft(), StatusDraft.SENDING);
        sendBroadcast(sp, STATUS_SENDING, null);
        publish(sp);
    }

    protected void handlePublishSuccess(StatusPublish sp, Serializable result) {
        mPublishQueue.put(sp, true);
        showPublishSuccessNotification(sp);
        StatusDraftHelper.removeStatusDraft(sp.getStatusDraft());
        sendBroadcast(sp, STATUS_SUCCESS, result);
        handlePublishFinish();
        stopServiceIfQueueIsAllFinish();
    }

    protected void handlePublishFail(StatusPublish sp) {
        mPublishQueue.remove(sp);
        showPublishFailNotification(sp);
        StatusDraftHelper.saveStatusDraft(sp.getStatusDraft(), StatusDraft.ERROR);
        sendBroadcast(sp, STATUS_FAIL, null);
        handlePublishFinish();
        stopServiceIfQueueIsAllFinish();
    }

    protected void handlePublishFinish() {
        if (SettingHelper.isVibrationFeedBack()) {
            ViewUtil.vibrate();
        }
    }

    protected void sendBroadcast(StatusPublish sp, int status, Serializable data){
        StatusDraft draft = sp.getStatusDraft();

        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_TYPE, draft.getType());
        bundle.putInt(EXTRA_STATUS, status);
        bundle.putSerializable(EXTRA_DRAFT, draft);
        bundle.putSerializable(EXTRA_DATA, data);

        Intent intent = new Intent(ACTION_PUBLISH);
        intent.putExtras(bundle);
        sendLocalBroadcast(intent);

        if(!CommonUtil.isEmpty(draft.getTargetStatusId())){
            Intent intentWithType = new Intent(ACTION_PUBLISH + CommonUtil.UNDERLINE + draft.getTargetStatusId());
            intentWithType.putExtras(bundle);
            sendLocalBroadcast(intentWithType);
        }
    }

    protected void sendLocalBroadcast(Intent intent){
        getLocalBroadcastManager().sendBroadcast(intent);
    }

    protected void publishTimingStatus(final StatusPublish sp) {

        StatusDraftHelper.saveStatusDraft(sp.getStatusDraft(), StatusDraft.TIMING);
        sendBroadcast(sp, STATUS_TIMING, null);
        List<StatusDraft> drafts = StatusDraftHelper.getTimingStatusDraft();
        AlarmManager am = (AlarmManager) ApplicationUtil.getContext().getSystemService(ALARM_SERVICE);
        for (StatusDraft draft : drafts) {
            if(!draft.isTimingExpired()){
                PublishStatusTimingIntent intent = new PublishStatusTimingIntent(draft.getTiming());
                int requestCode = (int)draft.getTiming();
                PendingIntent sender = PendingIntent.getBroadcast(ApplicationUtil.getContext(), requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                am.set(AlarmManager.RTC_WAKEUP, draft.getTiming(), sender);
            }else{
                //定时任务已过期
                StatusDraftHelper.saveStatusDraft(sp.getStatusDraft(), StatusDraft.ERROR);
                L.debug("TimingTask is expired");
            }
        }
    }

    protected void publishWBStatus(final StatusPublish sp) throws Exception {
        if (sp.getStatusDraft().getUrls() != null) {
            publishWBStatusWithPhoto(sp);
        } else {
            publishWBStatusContentOnly(sp);
        }
    }

    protected void publishWBStatusContentOnly(final StatusPublish sp) {
        RetrofitManager
                .getWBService()
                .publishStatus(UrlFactory.getPublishStatusParams(sp))
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new PublishStatusSubscriber(sp));
    }

    protected void publishWBStatusWithPhoto(final StatusPublish sp) throws Exception {
        List<String> urls = sp.getStatusDraft().getUrlList();

        if (urls == null) {
            throw new Exception("photo url is invalid!");
        }
        if (urls.size() == 1) {
            publishWBStatusWithSinglePhoto(sp);
        } else {
            publishWBStatusWithMultiplePhoto(sp, urls);
        }
    }

    protected void publishWBStatusWithSinglePhoto(final StatusPublish sp) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"),
                new File(sp.getStatusDraft().getUrls()));

        MultipartBody.Part body = MultipartBody.Part.createFormData("pic", "share.png", requestFile);
        RetrofitManager
                .getWBService()
                .publishStatusWithSinglePhoto(
                        UrlFactory.getPublishStatusParamsWrapToMultiPart(sp),
                        body)
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new PublishStatusSubscriber(sp));
    }

//    protected void publishWBStatusWithSinglePhoto(final TopicPublish sp) {
//        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"),
//                new File(sp.getStatusDraft().getUrls()));
//        MultipartBody.Part body = MultipartBody.Part.createFormData("pic", "share.png", requestFile);
//        RetrofitManager
//                .getWBService()
//                .publishStatusWithSinglePhoto(
//                        RequestBody.create(MediaType.parse("multipart/form-data"),
//                                sp.getToken()),
//                        RequestBody.create(MediaType.parse("multipart/form-data"),
//                                sp.getStatusDraft().getContent()),
//                        body)
//                .subscribeOn(SchedulerProvider.io())
//                .observeOn(SchedulerProvider.ui())
//                .subscribe(new PublishTopicSubscriber(sp));
//    }

    protected void publishWBStatusWithMultiplePhoto(final StatusPublish sp, List<String> urls) throws Exception {

        Single.zip(
                Observable
                        .fromIterable(urls)
                        .map(new Function<String, SingleSource<WBUploadPicture>>() {
                            @Override
                            public SingleSource<WBUploadPicture> apply(String s) {

                                RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"),
                                        new File(s));
                                MultipartBody.Part body = MultipartBody.Part.createFormData("pic", "share.png", requestFile);
                                WBService service = RetrofitManager.getWBService();
                                return service.uploadPicture(RequestBody.create(MediaType.parse("multipart/form-data"),
                                        UserUtil.getPriorToken()), body);
                            }
                        })
                        .blockingIterable()
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
                .flatMap(new Function<List<String>, SingleSource<WBStatus>>() {
                    @Override
                    public SingleSource<WBStatus> apply(List<String> urls) {

                        sp.setToken(UserUtil.getPriorToken());
                        return RetrofitManager
                                .getWBService()
                                .publishStatusWithMultiplePhoto(UrlFactory.getPublishStatusParams(sp, CommonUtil.toSplit(urls, StatusDraft.DELIMITER_URL)));
//                        return RetrofitManager
//                                .getWBService()
//                                .publishStatusWithMultiplePhoto(UserUtil.getPriorToken(), sp.getStatusDraft().getContent(), CommonUtil.toSplit(urls, ","));

                    }
                })
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new PublishStatusSubscriber(sp));

    }

    protected void publishWBComment(final StatusPublish sp) {
        if (sp.getStatusDraft().isCommentOrRepostConcurrently()) {
            WBService service = RetrofitManager.getWBService();

            Single.zip(
                    service.publishComment(sp.getToken(), sp.getStatusDraft().getContent(), sp.getStatusDraft().getTargetStatusId(), 0),
                    service.repostStatus(sp.getToken(), sp.getStatusDraft().getRepostContent(), sp.getStatusDraft().getTargetStatusId(), 0),
                    ObjectConverter.getObjectConverter2())
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe(new PublishCommentAndRepostSubscriber(sp));

        } else {
            RetrofitManager
                    .getWBService()
                    .publishComment(sp.getToken(), sp.getStatusDraft().getContent(), sp.getStatusDraft().getTargetStatusId(), sp.getStatusDraft().getIsCommentOrigin())
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe(new PublishCommentSubscriber(sp));
        }
    }

    protected void repostWBStatus(final StatusPublish sp) {
        RetrofitManager
                .getWBService()
                .repostStatus(sp.getToken(), sp.getStatusDraft().getContent(), sp.getStatusDraft().getTargetStatusId(), sp.getStatusDraft().getIsCommentOrigin())
                .subscribeOn(SchedulerProvider.io())
                .observeOn(SchedulerProvider.ui())
                .subscribe(new PublishRepostSubscriber(sp));
    }

    protected void replyWBComment(final StatusPublish sp) {
        if (sp.getStatusDraft().isCommentOrRepostConcurrently()) {
            WBService service = RetrofitManager.getWBService();
            Single.zip(
                    service.replyComment(sp.getToken(), sp.getStatusDraft().getContent(), sp.getStatusDraft().getTargetStatusId(), sp.getStatusDraft().getTargetCommentId(), 0),
                    service.repostStatus(sp.getToken(), sp.getStatusDraft().getRepostContent(), sp.getStatusDraft().getTargetStatusId(), 0),
                    ObjectConverter.getObjectConverter2())
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe(new PublishCommentAndRepostSubscriber(sp));
        } else {
            RetrofitManager
                    .getWBService()
                    .replyComment(sp.getToken(), sp.getStatusDraft().getContent(), sp.getStatusDraft().getTargetStatusId(), sp.getStatusDraft().getTargetCommentId(), sp.getStatusDraft().getIsCommentOrigin())
                    .subscribeOn(SchedulerProvider.io())
                    .observeOn(SchedulerProvider.ui())
                    .subscribe(new PublishCommentSubscriber(sp));
        }
    }


    public abstract class PublishSubscriber<T> implements SingleObserver<T> {

        StatusPublish sp;

        public PublishSubscriber(StatusPublish sp) {
            this.sp = sp;
        }

        @Override
        public void onSubscribe(Disposable d) {

        }

        @Override
        public void onSuccess(T value) {
            L.debug("onCompleted invoke");
        }

        @Override
        public void onError(Throwable e) {
            L.debug("request fail, error : %s", e);
            handlePublishFail(sp);
        }

    }

    public class PublishStatusSubscriber extends PublishSubscriber<WBStatus> {

        public PublishStatusSubscriber(StatusPublish sp) {
            super(sp);
        }

        @Override
        public void onSuccess(WBStatus wbStatus) {
            L.debug("request success , data : %s", wbStatus);
            if (wbStatus != null) {
                handlePublishSuccess(sp, Status.getStatus(wbStatus));
            }
        }
    }

    public class PublishRepostSubscriber extends PublishSubscriber<WBStatus> {

        public PublishRepostSubscriber(StatusPublish sp) {
            super(sp);
        }

        @Override
        public void onSuccess(WBStatus wbStatus) {
            L.debug("request success , data : %s", wbStatus);
            if (wbStatus != null) {
                StatusComment topicComment = StatusComment.getComment(wbStatus);
                StatusComment.StatusCommentEvent event = new StatusComment.StatusCommentEvent(topicComment, sp.getStatusDraft().getTargetStatusId(), false);
                EventBus.getDefault().post(event);
                handlePublishSuccess(sp, topicComment);
            }
        }
    }

    public class PublishCommentSubscriber extends PublishSubscriber<WBStatusComment> {

        public PublishCommentSubscriber(StatusPublish sp) {
            super(sp);
        }

        @Override
        public void onSuccess(WBStatusComment wbStatusComment) {
            L.debug("request success , data : %s", wbStatusComment);
            if (wbStatusComment != null) {
                StatusComment topicComment = StatusComment.getComment(wbStatusComment);
                StatusComment.StatusCommentEvent event = new StatusComment.StatusCommentEvent(topicComment, sp.getStatusDraft().getTargetStatusId(), true);
                EventBus.getDefault().post(event);
                handlePublishSuccess(sp, topicComment);
            }
        }
    }

    public class PublishCommentAndRepostSubscriber extends PublishSubscriber<Object[]> {

        public PublishCommentAndRepostSubscriber(StatusPublish sp) {
            super(sp);
        }

        @Override
        public void onSuccess(Object[] objects) {
            WBStatusComment wbStatusComment = null;
            WBStatus wbStatus = null;
            if (objects[0] instanceof WBStatusComment) {
                wbStatusComment = (WBStatusComment) objects[0];
            }

            if (objects[1] instanceof WBStatus) {
                wbStatus = (WBStatus) objects[1];
            }

            if (wbStatusComment != null && wbStatus != null) {
                StatusComment topicComment = StatusComment.getComment(wbStatusComment);
                StatusComment.StatusCommentEvent event = new StatusComment.StatusCommentEvent(topicComment, sp.getStatusDraft().getTargetStatusId(), true);
                EventBus.getDefault().post(event);
                handlePublishSuccess(sp, topicComment);
            } else {
                L.debug("request fail , wbStatusComment : %s, wbStatus : %s", wbStatusComment, wbStatus);
                throw new IllegalStateException("publish error");
            }
        }
    }

    protected void showPublishStartNotification(StatusPublish sp) {
        Notification.Builder builder = getNotificationBuilder()
                .setTicker(getString(R.string.label_status_publish_start))
                .setContentTitle(getString(R.string.label_status_publish_start))
                .setContentText(getString(R.string.label_status_publish));

        int id = sp.getStatusDraft().getNotificationId();
        if (id == -1) {
            id = new Random().nextInt(Integer.MAX_VALUE);
            sp.getStatusDraft().setNotificationId(id);
        }
        NotificationUtil.show(builder.build(), id);

        addNotificationId(sp, id);
    }

    protected void showPublishSuccessNotification(final StatusPublish sp) {
        Notification.Builder builder = getNotificationBuilder()
                .setTicker(getString(R.string.label_status_publish_success))
                .setContentTitle(getString(R.string.label_status_publish_success))
                .setContentText(sp.getStatusDraft().getDesc())
                .setVibrate(new long[0]);
        NotificationUtil.show(builder.build(), getNotificationId(sp));

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NotificationUtil.cancel(getNotificationId(sp));
                removeNotificationId(sp);
            }
        }, 3000);
    }

    protected void showPublishFailNotification(StatusPublish sp) {
//        try {
//            Thread.sleep(2000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        Intent intent = new Intent(this, StatusDraftActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = getNotificationBuilder()
                .setTicker(getString(R.string.label_status_publish_fail))
                .setContentTitle(getString(R.string.label_status_publish_fail))
                .setContentText(sp.getStatusDraft().getDesc())
                .setVibrate(new long[0])
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationUtil.show(builder.build(), getNotificationId(sp));
        removeNotificationId(sp);
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

        Bitmap bitmap = UserUtil.getUserAvatarBitmap();
        if (bitmap != null) {
            builder.setLargeIcon(bitmap);
        }
        return builder;
    }

    protected int getNotificationId(StatusPublish sp) {
        Integer id = mPublishNotificationQueue.get(sp);
        if (id == null) {
            return 0;
        }
        return id;
    }

    protected void addNotificationId(StatusPublish sp, int id) {
        mPublishNotificationQueue.put(sp, id);
    }

    protected void removeNotificationId(StatusPublish sp) {
        mPublishNotificationQueue.remove(sp);
    }

    protected void stopServiceIfQueueIsAllFinish() {
        boolean isAllFinish = true;
        Set<StatusPublish> set = mPublishQueue.keySet();
        for (StatusPublish sp : set) {
            if (!mPublishQueue.get(sp)) {
                isAllFinish = false;
                break;
            }
        }
        if (isAllFinish) {
            stopForeground(true);
            stopSelf();
            L.debug("StatusPublishService QueueIsAllFinish");
        }
    }
}


//    private StringRequest getWBTopicPublishRequest(final TopicPublish sp) {
//
//
//        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBTopicPublishUrl());
//        ub.addParameter("access_token", sp.getToken());
//        ub.addParameter("status", sp.getStatusDraft().getContent());
//        return new StringRequest(Request.Method.POST,
////                WBTopic.class,
//                ub.getUrl()
//                , new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
//                if (response != null) {
//                    showPublishSuccessNotification(sp);
//                    mPublishQueue.put(sp, true);
//                    stopServiceIfQueueIsAllFinish();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), volleyError);
//                mPublishQueue.remove(sp);
//                showPublishFailNotification(sp);
//                TopicDraftHelper.saveStatusDraft(sp.getStatusDraft());
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



























