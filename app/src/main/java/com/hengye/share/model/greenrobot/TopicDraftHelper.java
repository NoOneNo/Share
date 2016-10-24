package com.hengye.share.model.greenrobot;

import com.hengye.share.model.Parent;
import com.hengye.share.model.Topic;
import com.hengye.share.model.TopicComment;
import com.hengye.share.model.UserInfo;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.thirdparty.WBUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;


public class TopicDraftHelper {

    public final static int PUBLISH_TOPIC = 0;
    public final static int PUBLISH_COMMENT = 1;
    public final static int REPLY_COMMENT = 2;
    public final static int REPOST_TOPIC = 3;

    public static Topic getTopic(TopicDraft topicDraft) {
        Topic topic = new Topic();
        topic.setContent(topicDraft.getContent());
        topic.setDate(topicDraft.getDate().toString());
        topic.setUserInfo(UserInfo.getUserInfo(UserUtil.getCurrentUser()));

        List<String> imageUrls = new ArrayList<>();
        List<String> imageLargeUrls = new ArrayList<>();

        List<String> img = topicDraft.getUrlList();
        if (!CommonUtil.isEmpty(img)) {
            for (String url : img) {
                imageUrls.add(WBUtil.getWBTopicImgUrl(url));
                imageLargeUrls.add(WBUtil.getWBTopicLargeImgUrl(url));
            }
            topic.setImageUrls(imageUrls);
            topic.setImageLargeUrls(imageLargeUrls);
        }
        return topic;
    }

    public static TopicDraft getWBTopicDraftByTopicPublish(String content) {
        TopicDraft topicDraft = new TopicDraft();
        topicDraft.setContent(content);
        topicDraft.setType(TopicDraftHelper.PUBLISH_TOPIC);
        topicDraft.setParentType(Parent.TYPE_WEIBO);
        return topicDraft;
    }

    /**
     * 转发微博时,只会转发该微博的原始微博, 比如微博A是一条转发微博B的, 就算传微博A的ID也是转发微博B;
     * 此处本来可以直接保存微博B的ID,但是为了实现转发微博B的同时评论给微博A,所以保存微博A的ID;
     *
     * @param topic
     * @return
     */
    public static TopicDraft getWBTopicDraftByRepostRepost(Topic topic) {
        TopicDraft topicDraft = new TopicDraft();
        Topic targetTopic;
        if (topic.getRetweetedTopic() != null) {
            //添加转发微博时微博A的内容;
            topicDraft.setContent(DataUtil.addRetweetedNamePrefix(topic));
        }
        if (topic.getRetweetedTopic() != null) {
            targetTopic = topic.getRetweetedTopic();
        } else {
            targetTopic = topic;
        }
        topicDraft.setTargetTopicId(topic.getId());
        topicDraft.setTargetTopicJson(targetTopic.toJson());
        topicDraft.setType(TopicDraftHelper.REPOST_TOPIC);
        topicDraft.setParentType(Parent.TYPE_WEIBO);
        return topicDraft;
    }

    public static TopicDraft getWBTopicDraftByRepostComment(Topic topic) {
        TopicDraft topicDraft = new TopicDraft();
        topicDraft.setTargetTopicId(topic.getId());
        topicDraft.setTargetTopicJson(topic.toJson());
        topicDraft.setType(TopicDraftHelper.PUBLISH_COMMENT);
        topicDraft.setParentType(Parent.TYPE_WEIBO);
        return topicDraft;
    }

    /**
     * 在转发列表发表评论
     *
     * @param topicComment
     * @return
     */
    public static TopicDraft getWBTopicDraftByRepostComment(TopicComment topicComment) {
        TopicDraft topicDraft = new TopicDraft();
        topicDraft.setTargetTopicId(topicComment.getId());
        topicDraft.setTargetTopicJson(topicComment.toTopicJson());
        topicDraft.setTargetCommentId(topicComment.getId());
        topicDraft.setTargetCommentUserName(topicComment.getUserInfo().getName());
        topicDraft.setTargetCommentContent(topicComment.getContent());
        topicDraft.setType(TopicDraftHelper.PUBLISH_COMMENT);
        topicDraft.setParentType(Parent.TYPE_WEIBO);
        return topicDraft;
    }

    /**
     * 在转发列表里转发微博
     *
     * @param topicComment
     * @return
     */
    public static TopicDraft getWBTopicDraftByRepostRepost(TopicComment topicComment) {
        TopicDraft topicDraft = new TopicDraft();
        //添加转发微博时微博A的内容;
        topicDraft.setContent(DataUtil.addRetweetedNamePrefix(topicComment));
        topicDraft.setTargetTopicId(topicComment.getId());
        topicDraft.setTargetTopicJson(topicComment.toTopicJson());
        topicDraft.setType(TopicDraftHelper.REPOST_TOPIC);
        topicDraft.setParentType(Parent.TYPE_WEIBO);
        return topicDraft;
    }

    /**
     * 在评论列表里回复评论
     *
     * @param topic
     * @param topicComment
     * @return
     */
    public static TopicDraft getWBTopicDraftByCommentReply(Topic topic, TopicComment topicComment) {
        TopicDraft topicDraft = new TopicDraft();
        topicDraft.setTargetTopicId(topic.getId());
        topicDraft.setTargetTopicJson(topic.toJson());
        topicDraft.setTargetCommentId(topicComment.getId());
        topicDraft.setTargetCommentUserName(topicComment.getUserInfo().getName());
        topicDraft.setTargetCommentContent(topicComment.getContent());
        topicDraft.setIsCommentOrigin(false);
        topicDraft.setType(TopicDraftHelper.REPLY_COMMENT);
        topicDraft.setParentType(Parent.TYPE_WEIBO);
        return topicDraft;
    }

    /**
     * 在评论列表里转发微博
     *
     * @param topicComment
     * @return
     */
    public static TopicDraft getWBTopicDraftByCommentRepost(Topic topic, TopicComment topicComment) {
        TopicDraft topicDraft = new TopicDraft();
        //添加转发微博时微博A的内容;
        topicDraft.setContent(DataUtil.addRetweetedNamePrefix(topicComment));
        topicDraft.setTargetTopicId(topic.getId());
        topicDraft.setTargetTopicJson(topic.toJson());
        topicDraft.setType(TopicDraftHelper.REPOST_TOPIC);
        topicDraft.setParentType(Parent.TYPE_WEIBO);
        return topicDraft;
    }

    /**
     * @return 返回当前用户可见状态的草稿
     */
    public static List<TopicDraft> getTopicDraft() {
        return getTopicDraftByStatus(TopicDraft.VISIBLE);
    }

    public static List<TopicDraft> getTimingTopicDraft() {
        return getTopicDraftByStatus(TopicDraft.TIMING);
    }

    public static List<TopicDraft> getAllTopicDraft() {
        return getTopicDraftByStatus((Integer[]) null);
    }

    /**
     * {@link TopicDraft#NORMAL}->获得正常状态的草稿
     * 传null ->获得所有草稿
     *
     * @param statusArrays
     * @return 返回当前用户的草稿
     */
    public static List<TopicDraft> getTopicDraftByStatus(Integer... statusArrays) {
        QueryBuilder<TopicDraft> qb = GreenDaoManager.getDaoSession().getTopicDraftDao().queryBuilder();
        qb.where(TopicDraftDao.Properties.Uid.eq(UserUtil.getUid()));

        if (statusArrays != null) {
            qb.where(TopicDraftDao.Properties.Status.in((Object[])statusArrays));
        }

        qb.orderDesc(TopicDraftDao.Properties.Date);

        try {
            return qb.list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 如果isNeedToSend为true, 则标记草稿为不可见
     *
     * @param topicDraft
     * @param status     topicDraft的status, see as {@link TopicDraft#NORMAL}
     */
    public static void saveTopicDraft(TopicDraft topicDraft, int status) {
        topicDraft.mark(status);
        updateTopicDraft(topicDraft);
    }

    public static void updateTopicDraft(TopicDraft topicDraft) {
        GreenDaoManager.getDaoSession().getTopicDraftDao().save(topicDraft);
    }

    public static void removeTopicDraft(TopicDraft topicDraft) {
        GreenDaoManager.getDaoSession().getTopicDraftDao().delete(topicDraft);
    }

    public static void removeAllTopicDraft() {
        GreenDaoManager.getDaoSession().getTopicDraftDao().deleteAll();
    }

    public static TopicDraft loadTopicDraft(Long id){
        return GreenDaoManager.getDaoSession().getTopicDraftDao().load(id);
    }
}
