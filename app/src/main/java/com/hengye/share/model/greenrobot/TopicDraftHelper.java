package com.hengye.share.model.greenrobot;

import com.hengye.share.model.Parent;
import com.hengye.share.model.Topic;
import com.hengye.share.util.UserUtil;

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
        return topic;
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
        return qb.list();
    }

    public static void saveTopicDraft(TopicDraft topicDraft){
        GreenDaoManager.getDaoSession().getTopicDraftDao().insertOrReplace(topicDraft);
    }

    public static void removeTopicDraft(TopicDraft topicDraft){
        GreenDaoManager.getDaoSession().getTopicDraftDao().delete(topicDraft);
    }
}
