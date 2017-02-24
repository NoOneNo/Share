package com.hengye.share.model.greenrobot;

import com.hengye.share.model.Parent;
import com.hengye.share.model.Status;
import com.hengye.share.model.StatusComment;
import com.hengye.share.model.UserInfo;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.UserUtil;
import com.hengye.share.util.thirdparty.WBUtil;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class StatusDraftHelper {

    public final static int PUBLISH_TOPIC = 0;
    public final static int PUBLISH_COMMENT = 1;
    public final static int REPLY_COMMENT = 2;
    public final static int REPOST_TOPIC = 3;

    public static Status getStatus(StatusDraft statusDraft) {
        Status status = new Status();
        status.setContent(statusDraft.getContent());
        Date date = statusDraft.getDate() == null ?
                DateUtil.getChinaGMTDate() :
                statusDraft.getDate();
        status.setDate(date.toString());
        status.setUserInfo(UserInfo.getUserInfo(UserUtil.getCurrentUser()));

        List<String> imageUrls = new ArrayList<>();
        List<String> imageLargeUrls = new ArrayList<>();

        List<String> img = statusDraft.getUrlList();
        if (!CommonUtil.isEmpty(img)) {
            for (String url : img) {
                String wbImgType = WBUtil.getWBImgType();
                imageUrls.add(WBUtil.getWBImgUrl(url, wbImgType));
                imageLargeUrls.add(WBUtil.getWBLargeImgUrl(url));
            }
            status.setImageUrls(imageUrls);
            status.setImageLargeUrls(imageLargeUrls);
        }
        return status;
    }

    public static StatusDraft getWBStatusDraftByStatusPublish(String content) {
        StatusDraft statusDraft = new StatusDraft();
        statusDraft.setContent(content);
        statusDraft.setType(StatusDraftHelper.PUBLISH_TOPIC);
        statusDraft.setParentType(Parent.TYPE_WEIBO);
        return statusDraft;
    }

    /**
     * 转发微博时,只会转发该微博的原始微博, 比如微博A是一条转发微博B的, 就算传微博A的ID也是转发微博B;
     * 此处本来可以直接保存微博B的ID,但是为了实现转发微博B的同时评论给微博A,所以保存微博A的ID;
     *
     * @param status
     * @return
     */
    public static StatusDraft getWBStatusDraftByRepostRepost(Status status) {
        StatusDraft statusDraft = new StatusDraft();
        Status targetStatus;
        if (status.getRetweetedStatus() != null) {
            //添加转发微博时微博A的内容;
            statusDraft.setContent(DataUtil.addRetweetedStatusNamePrefix(status));
        }
        if (status.getRetweetedStatus() != null) {
            targetStatus = status.getRetweetedStatus();
        } else {
            targetStatus = status;
        }
        statusDraft.setTargetStatusId(status.getId());
        statusDraft.setTargetStatusJson(targetStatus.toJson());
        statusDraft.setType(StatusDraftHelper.REPOST_TOPIC);
        statusDraft.setParentType(Parent.TYPE_WEIBO);
        return statusDraft;
    }

    public static StatusDraft getWBStatusDraftByRepostComment(Status status) {
        StatusDraft statusDraft = new StatusDraft();
        statusDraft.setTargetStatusId(status.getId());
        statusDraft.setTargetStatusJson(status.toJson());
        statusDraft.setType(StatusDraftHelper.PUBLISH_COMMENT);
        statusDraft.setParentType(Parent.TYPE_WEIBO);
        return statusDraft;
    }

    /**
     * 在转发列表发表评论
     *
     * @param statusComment
     * @return
     */
    public static StatusDraft getWBStatusDraftByRepostComment(StatusComment statusComment) {
        StatusDraft statusDraft = new StatusDraft();
        statusDraft.setTargetStatusId(statusComment.getId());
        statusDraft.setTargetStatusJson(statusComment.toStatusJson());
        statusDraft.setTargetCommentId(statusComment.getId());
        statusDraft.setTargetCommentUserName(statusComment.getUserInfo().getName());
        statusDraft.setTargetCommentContent(statusComment.getContent());
        statusDraft.setType(StatusDraftHelper.PUBLISH_COMMENT);
        statusDraft.setParentType(Parent.TYPE_WEIBO);
        return statusDraft;
    }

    /**
     * 在转发列表里转发微博
     *
     * @param statusComment
     * @return
     */
    public static StatusDraft getWBStatusDraftByRepostRepost(StatusComment statusComment) {
        StatusDraft statusDraft = new StatusDraft();
        //添加转发微博时微博A的内容;
        statusDraft.setContent(DataUtil.addRetweetedStatusNamePrefix(statusComment));
        statusDraft.setTargetStatusId(statusComment.getId());
        statusDraft.setTargetStatusJson(statusComment.toStatusJson());
        statusDraft.setType(StatusDraftHelper.REPOST_TOPIC);
        statusDraft.setParentType(Parent.TYPE_WEIBO);
        return statusDraft;
    }

    /**
     * 在评论列表里回复评论
     *
     * @param status
     * @param statusComment
     * @return
     */
    public static StatusDraft getWBStatusDraftByCommentReply(Status status, StatusComment statusComment) {
        StatusDraft statusDraft = new StatusDraft();
        statusDraft.setTargetStatusId(status.getId());
        statusDraft.setTargetStatusJson(status.toJson());
        statusDraft.setTargetCommentId(statusComment.getId());
        statusDraft.setTargetCommentUserName(statusComment.getUserInfo().getName());
        statusDraft.setTargetCommentContent(statusComment.getContent());
        statusDraft.setIsCommentOrigin(false);
        statusDraft.setType(StatusDraftHelper.REPLY_COMMENT);
        statusDraft.setParentType(Parent.TYPE_WEIBO);
        return statusDraft;
    }

    /**
     * 在评论列表里转发微博
     *
     * @param statusComment
     * @return
     */
    public static StatusDraft getWBStatusDraftByCommentRepost(Status status, StatusComment statusComment) {
        StatusDraft statusDraft = new StatusDraft();
        //添加转发微博时微博A的内容;
        statusDraft.setContent(DataUtil.addRetweetedStatusNamePrefix(statusComment));
        statusDraft.setTargetStatusId(status.getId());
        statusDraft.setTargetStatusJson(status.toJson());
        statusDraft.setType(StatusDraftHelper.REPOST_TOPIC);
        statusDraft.setParentType(Parent.TYPE_WEIBO);
        return statusDraft;
    }

    /**
     * @return 返回当前用户可见状态的草稿
     */
    public static List<StatusDraft> getStatusDraft() {
        return getStatusDraftByStatus(StatusDraft.VISIBLE);
    }

    public static List<StatusDraft> getTimingStatusDraft() {
        return getStatusDraftByStatus(StatusDraft.TIMING);
    }

    public static List<StatusDraft> getAllStatusDraft() {
        return getStatusDraftByStatus((Integer[]) null);
    }

    /**
     * {@link StatusDraft#NORMAL}->获得正常状态的草稿
     * 传null ->获得所有草稿
     *
     * @param statusArrays
     * @return 返回当前用户的草稿
     */
    public static List<StatusDraft> getStatusDraftByStatus(Integer... statusArrays) {
        QueryBuilder<StatusDraft> qb = GreenDaoManager.getDaoSession().getStatusDraftDao().queryBuilder();
        qb.where(StatusDraftDao.Properties.Uid.eq(UserUtil.getUid()));

        if (statusArrays != null) {
            qb.where(StatusDraftDao.Properties.Status.in((Object[])statusArrays));
        }

        qb.orderDesc(StatusDraftDao.Properties.Date);

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
     * @param statusDraft
     * @param status     statusDraft的status, see as {@link StatusDraft#NORMAL}
     */
    public static void saveStatusDraft(StatusDraft statusDraft, int status) {
        statusDraft.mark(status);
        updateStatusDraft(statusDraft);
    }

    public static void updateStatusDraft(StatusDraft statusDraft) {
        GreenDaoManager.getDaoSession().getStatusDraftDao().save(statusDraft);
    }

    public static void removeStatusDraft(StatusDraft statusDraft) {
        GreenDaoManager.getDaoSession().getStatusDraftDao().delete(statusDraft);
    }

    public static void removeAllStatusDraft() {
        GreenDaoManager.getDaoSession().getStatusDraftDao().deleteAll();
    }

    public static StatusDraft loadStatusDraft(Long id){
        return GreenDaoManager.getDaoSession().getStatusDraftDao().load(id);
    }
}
