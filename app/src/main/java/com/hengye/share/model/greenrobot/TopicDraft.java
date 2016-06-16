package com.hengye.share.model.greenrobot;

import com.hengye.share.model.Topic;
import com.hengye.share.model.greenrobot.DaoSession;
import com.hengye.share.model.sina.WBTopic;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS

// KEEP INCLUDES - put your custom includes here
import de.greenrobot.dao.AbstractDao;
// KEEP INCLUDES END
/**
 * Entity mapped to table "TOPIC_DRAFT".
 */
public class TopicDraft implements java.io.Serializable {

    private Long id;
    /** Not-null value. */
    private String content;
    private java.util.Date date;
    private String urls;
    /** Not-null value. */
    private String uid;
    private String targetTopicId;
    private String targetCommentId;
    private Integer isCommentOrigin;
    private Integer isMention;
    private Integer type;
    private Integer parentType;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient TopicDraftDao myDao;


    // KEEP FIELDS - put your custom fields here
    private static final long serialVersionUID = -3380622281318260025L;
    // KEEP FIELDS END

    public TopicDraft() {
    }

    public TopicDraft(Long id) {
        this.id = id;
    }

    public TopicDraft(Long id, String content, java.util.Date date, String urls, String uid, String targetTopicId, String targetCommentId, Integer isCommentOrigin, Integer isMention, Integer type, Integer parentType) {
        this.id = id;
        this.content = content;
        this.date = date;
        this.urls = urls;
        this.uid = uid;
        this.targetTopicId = targetTopicId;
        this.targetCommentId = targetCommentId;
        this.isCommentOrigin = isCommentOrigin;
        this.isMention = isMention;
        this.type = type;
        this.parentType = parentType;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTopicDraftDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getContent() {
        return content;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setContent(String content) {
        this.content = content;
    }

    public java.util.Date getDate() {
        return date;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    /** Not-null value. */
    public String getUid() {
        return uid;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTargetTopicId() {
        return targetTopicId;
    }

    public void setTargetTopicId(String targetTopicId) {
        this.targetTopicId = targetTopicId;
    }

    public String getTargetCommentId() {
        return targetCommentId;
    }

    public void setTargetCommentId(String targetCommentId) {
        this.targetCommentId = targetCommentId;
    }

    public Integer getIsCommentOrigin() {
        return isCommentOrigin;
    }

    public void setIsCommentOrigin(Integer isCommentOrigin) {
        this.isCommentOrigin = isCommentOrigin;
    }

    public Integer getIsMention() {
        return isMention;
    }

    public void setIsMention(Integer isMention) {
        this.isMention = isMention;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getParentType() {
        return parentType;
    }

    public void setParentType(Integer parentType) {
        this.parentType = parentType;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    public void setIsCommentOrigin(boolean isCommentOrigin){
        setIsCommentOrigin(isCommentOrigin ? 1 : 0);
    }

    public void setIsMention(boolean isMention){
        setIsMention(isMention ? 0 : 1);
    }

    private transient Topic mTopic;

    public Topic getTopic(){
        if(mTopic == null){
            mTopic = generateTopic();
        }
        return mTopic;
    }

    public Topic generateTopic(){
        return TopicDraftHelper.getTopic(this);
    }

    // KEEP METHODS END

}
