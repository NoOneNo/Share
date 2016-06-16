package com.hengye.share.model.greenrobot;

import com.hengye.share.model.Parent;
import com.hengye.share.model.Topic;
import com.hengye.share.model.UserInfo;
import com.hengye.share.model.sina.WBTopic;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

public class TopicDraftHelper {

    public final static int PUBLISH_TOPIC = 0;
    public final static int PUBLISH_COMMENT = 1;
    public final static int REPLY_COMMENT = 2;
    public final static int REPOST_TOPIC = 3;

    public static Topic getTopic(TopicDraft topicDraft){
        Topic topic = new Topic();
        topic.setContent(topicDraft.getContent());
        topic.setDate(topicDraft.getDate().toString());
        topic.setUserInfo(UserInfo.getUserInfo(UserUtil.getCurrentUser()));

        List<String> imageUrls = new ArrayList<>();
        List<String> imageLargeUrls = new ArrayList<>();

        List<String> img = CommonUtil.split(topicDraft.getUrls(), ",");
        if(!CommonUtil.isEmpty(img)) {
            for (String url : img) {
                imageUrls.add(WBUtil.getWBTopicImgUrl(url));
                imageLargeUrls.add(WBUtil.getWBTopicLargeImgUrl(url));
            }
            topic.setImageUrls(imageUrls);
            topic.setImageLargeUrls(imageLargeUrls);
        }
        return topic;
    }

    public static TopicDraft getWBTopicDraftByTopicPublish(String content){
        TopicDraft topicDraft = new TopicDraft();
        topicDraft.setContent(content);
        topicDraft.setType(TopicDraftHelper.PUBLISH_TOPIC);
        topicDraft.setParentType(Parent.TYPE_WEIBO);
        return topicDraft;
    }

    public static TopicDraft getWBTopicDraftByTopicRepost(String targetTopicId){
        TopicDraft topicDraft = new TopicDraft();
        topicDraft.setTargetTopicId(targetTopicId);
        topicDraft.setType(TopicDraftHelper.REPOST_TOPIC);
        topicDraft.setParentType(Parent.TYPE_WEIBO);
        return topicDraft;
    }

    public static TopicDraft getWBTopicDraftByTopicComment(String targetTopicId){
        TopicDraft topicDraft = new TopicDraft();
        topicDraft.setTargetTopicId(targetTopicId);
        topicDraft.setType(TopicDraftHelper.PUBLISH_COMMENT);
        topicDraft.setParentType(Parent.TYPE_WEIBO);
        return topicDraft;
    }

    public static List<TopicDraft> getTopicDraft(){
        QueryBuilder<TopicDraft> qb = GreenDaoManager.getDaoSession().getTopicDraftDao().queryBuilder();
        qb.where(TopicDraftDao.Properties.Uid.eq(UserUtil.getUid()));
        qb.orderDesc(TopicDraftDao.Properties.Date);

        try{
            return qb.list();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static void saveTopicDraft(TopicDraft topicDraft){
        GreenDaoManager.getDaoSession().getTopicDraftDao().insertOrReplace(topicDraft);
    }

    public static void removeTopicDraft(TopicDraft topicDraft){
        GreenDaoManager.getDaoSession().getTopicDraftDao().delete(topicDraft);
    }
}
