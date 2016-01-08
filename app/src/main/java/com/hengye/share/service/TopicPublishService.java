package com.hengye.share.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.GsonRequest;
import com.hengye.share.R;
import com.hengye.share.model.TopicPublish;
import com.hengye.share.model.greenrobot.TopicDraft;
import com.hengye.share.model.greenrobot.TopicDraftHelper;
import com.hengye.share.model.sina.WBTopic;
import com.hengye.share.ui.activity.TopicDraftActivity;
import com.hengye.share.util.L;
import com.hengye.share.util.NotificationUtil;
import com.hengye.share.util.RequestManager;
import com.hengye.share.util.UrlBuilder;
import com.hengye.share.util.UrlFactory;

import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class TopicPublishService extends Service{

    private HashMap<TopicPublish, Boolean> mPublishQueue = new HashMap<>();
    private HashMap<TopicPublish, Integer> mPublishNotificationQueue = new HashMap<>();

    private Handler mHandler = new Handler();

    public final static int MAX_PUBLISH_SIZE = 10;

//    public static void publish(Context context, Topic topic, String token){
//        Intent intent = new Intent(context, TopicPublishService.class);
//        intent.putExtra("topic", topic);
//        intent.putExtra("token", token);
//        context.startService(intent);
//    }

    public static void publish(Context context, TopicDraft topicDraft, String token){
        Intent intent = new Intent(context, TopicPublishService.class);
        intent.putExtra("topicDraft", topicDraft);
        intent.putExtra("token", token);
        context.startService(intent);
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
        if(topicDraft != null && !TextUtils.isEmpty(token)){
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

    private void addTopicPublishRequestToQueue(TopicPublish tp){
        mPublishQueue.put(tp, false);
        mPublishNotificationQueue.put(tp, new Random().nextInt(Integer.MAX_VALUE));
        RequestManager.addToRequestQueue(getWBTopicPublishRequest(tp));
        showTopicPublishStartNotification(tp);
    }

    private GsonRequest getWBTopicPublishRequest(final TopicPublish tp) {


        final UrlBuilder ub = new UrlBuilder(UrlFactory.getInstance().getWBTopicPublishUrl());
        ub.addParameter("access_token", tp.getToken());
        ub.addParameter("status", tp.getTopicDraft().getContent());
        return new GsonRequest<WBTopic>(Request.Method.POST,
                WBTopic.class,
                ub.getRequestUrl()
                , new Response.Listener<WBTopic>() {
            @Override
            public void onResponse(WBTopic response) {
                L.debug("request success , url : {}, data : {}", ub.getRequestUrl(), response);
                if(response != null){
                    showTopicPublishSuccessNotification(tp);
                    mPublishQueue.put(tp, true);
                    stopServiceIfQueueIsAllFinish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                L.debug("request fail , url : {}, error : {}", ub.getRequestUrl(), volleyError);
                mPublishQueue.remove(tp);
                showTopicPublishFailNotification(tp);
                TopicDraftHelper.saveTopicDraft(tp.getTopicDraft());
                stopServiceIfQueueIsAllFinish();
            }
        }){
            @Override
            public byte[] getBody() {
//                return ub.getBody();
                return null;
            }
        };
    }

    private void showTopicPublishStartNotification(TopicPublish tp){
        Notification.Builder builder = new Notification.Builder(this)
                .setTicker(getString(R.string.label_topic_publish_start))
                .setContentTitle(getString(R.string.label_topic_publish_start))
                .setContentText("topic publish")
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setSmallIcon(R.drawable.notification_icon_upload_white);
        NotificationUtil.show(builder.build(), mPublishNotificationQueue.get(tp));
    }

    private void showTopicPublishSuccessNotification(final TopicPublish tp){
        Notification.Builder builder = new Notification.Builder(this)
                .setTicker(getString(R.string.label_topic_publish_success))
                .setContentTitle(getString(R.string.label_topic_publish_success))
                .setContentText("topic publish")
                .setOnlyAlertOnce(true)
                .setOngoing(false)
                .setSmallIcon(R.drawable.notification_icon_upload_white);
        NotificationUtil.show(builder.build(), mPublishNotificationQueue.get(tp));

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                NotificationUtil.cancel(mPublishNotificationQueue.get(tp));
            }
        }, 3000);
    }

    private void showTopicPublishFailNotification(TopicPublish tp){
        Notification.Builder builder = new Notification.Builder(this)
                .setTicker(getString(R.string.label_topic_publish_fail))
                .setContentTitle(getString(R.string.label_topic_publish_fail))
                .setContentText("topic publish")
                .setOnlyAlertOnce(true)
                .setOngoing(false)
                .setSmallIcon(R.drawable.notification_icon_upload_white);
        NotificationUtil.show(builder.build(), mPublishNotificationQueue.get(tp));
    }

    private void stopServiceIfQueueIsAllFinish(){
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

























