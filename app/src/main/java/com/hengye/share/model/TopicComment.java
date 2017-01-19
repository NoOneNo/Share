package com.hengye.share.model;

import android.text.SpannableString;
import android.text.TextUtils;

import com.hengye.share.model.sina.WBTopic;
import com.hengye.share.model.sina.WBTopicComment;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopicReposts;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.GsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TopicComment implements TopicId, TopicShortUrl, Serializable{

    public static TopicComments getComments(WBTopicComments wbTopicComments){
        TopicComments topicComments = new TopicComments();
        topicComments.setComments(getCommentArrayList(wbTopicComments));
        topicComments.setTotalNumber(wbTopicComments.getTotal_number());
        return topicComments;
    }

    public static TopicComments getComments(WBTopicReposts wbTopicReposts){
        TopicComments topicComments = new TopicComments();
        topicComments.setComments(getCommentArrayList(wbTopicReposts));
        topicComments.setTotalNumber(wbTopicReposts.getTotal_number());
        return topicComments;
    }

    public static ArrayList<TopicComment> getCommentArrayList(WBTopicComments wbTopicComments){
        if(wbTopicComments == null || CommonUtil.isEmpty(wbTopicComments.getComments())){
            return null;
        }
        ArrayList<TopicComment> topicComments = new ArrayList<>();
        for(WBTopicComment entity : wbTopicComments.getComments()){
            topicComments.add(getComment(entity));
        }
        return topicComments;
    }

    public static TopicComment getComment(WBTopicComment entity){
        TopicComment topicComment = new TopicComment();
        topicComment.setParent(new Parent(GsonUtil.toJson(entity), Parent.TYPE_WEIBO));
        if(entity == null){
            return topicComment;
        }
        topicComment.setUserInfo(UserInfo.getUserInfo(entity.getUser()));
        topicComment.setDate(entity.getCreated_at());
        topicComment.setChannel(entity.getSource());
        topicComment.setContent(entity.getText());
        topicComment.setId(entity.getIdstr());
        topicComment.setTopic(Topic.getTopic(entity.getStatus()));
        topicComment.setLiked(entity.isLiked());
        topicComment.setLikeCounts(entity.getLike_counts());
//        if(!CommonUtil.isEmpty(entity.getPic_urls())){
//            List<String> imageUrls = new ArrayList<>();
//            List<String> imageLargeUrls = new ArrayList<>();
//            for(WBTopic.Pic_urlsEntity urlsEntity : entity.getPic_urls()){
//                imageUrls.add(WBUtil.getWBImgUrl(urlsEntity.getThumbnail_pic(), WBUtil.IMAGE_TYPE_BMIDDLE));
//                imageLargeUrls.add(WBUtil.getWBImgUrl(urlsEntity.getThumbnail_pic(), WBUtil.IMAGE_TYPE_LARGE));
//            }
//            topicComment.setImageUrls(imageUrls);
//            topicComment.setImageLargeUrls(imageLargeUrls);
//        }

        return topicComment;
    }

    public static ArrayList<TopicComment> getCommentArrayList(WBTopicReposts wbTopicReposts){
        if(wbTopicReposts == null || CommonUtil.isEmpty(wbTopicReposts.getReposts())){
            return null;
        }
        ArrayList<TopicComment> topicComments = new ArrayList<>();
        for(WBTopic entity : wbTopicReposts.getReposts()){
            topicComments.add(getComment(entity));
        }
        return topicComments;
    }

    public static TopicComment getComment(WBTopic entity){
        TopicComment topicComment = new TopicComment();
        topicComment.setParent(new Parent(GsonUtil.toJson(entity), Parent.TYPE_WEIBO));
        if(entity == null){
            return topicComment;
        }
        topicComment.setUserInfo(UserInfo.getUserInfo(entity.getUser()));
        topicComment.setDate(entity.getCreated_at());
        topicComment.setChannel(entity.getSource());
        topicComment.setContent(entity.getText());
        topicComment.setId(entity.getIdstr());
        topicComment.setTopic(Topic.getTopic(entity.getRetweeted_status()));
        return topicComment;
    }

    public static class TopicCommentEvent{

        TopicComment topicComment;
        String targetTopicId;
        boolean isComment;

        public TopicCommentEvent(TopicComment topicComment, String targetTopicId, boolean isComment) {
            this.topicComment = topicComment;
            this.targetTopicId = targetTopicId;
            this.isComment = isComment;
        }

        public TopicComment getTopicComment() {
            return topicComment;
        }

        public String getTargetTopicId() {
            return targetTopicId;
        }

        public boolean isComment() {
            return isComment;
        }
    }

    private static final long serialVersionUID = -4250789290948278492L;

    private Parent parent;

    private UserInfo userInfo;//用户信息
    private String date;//创建日期
    private String channel;//渠道，通过什么发表
    private String content;//内容
    private String id;//评论或转发的唯一id
    private long likeCounts;//评论点赞数
    private boolean isLiked;//是否已经点赞
    private Topic topic;//评论或转发的主题
    private List<String> imageUrls;//缩略图
    private List<String> imageLargeUrls;//原图

    private HashMap<String, TopicUrl> urlMap;

    private transient SpannableString urlSpannableString;

    public void updateLiked(boolean isLiked){
        setLiked(isLiked);
        setLikeCounts(getLikeCounts() + (isLiked ? 1 : -1));
    }

    @Override
    public String toString() {
        return "TopicComment{" +
                "parent=" + parent +
                ", userInfo=" + userInfo +
                ", date='" + date + '\'' +
                ", channel='" + channel + '\'' +
                ", content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", likeCounts=" + likeCounts +
                ", isLiked=" + isLiked +
                ", topic=" + topic +
                ", imageUrls=" + imageUrls +
                ", imageLargeUrls=" + imageLargeUrls +
                ", urlMap=" + urlMap +
                ", urlSpannableString=" + urlSpannableString +
                '}';
    }

    public Topic toTopic(){
        WBTopic wbTopic = GsonUtil.fromJson(getParent().getJson(), WBTopic.class);
        if(wbTopic != null){
            return Topic.getTopic(wbTopic);
        }
        return null;
    }

    public String toTopicJson(){
        Topic topic = toTopic();
        if(topic != null){
            return topic.toJson();
        }
        return null;
    }

    public SpannableString getUrlSpannableString() {
        if (!TextUtils.isEmpty(urlSpannableString)) {
            return urlSpannableString;
        } else {
            DataUtil.addTopicContentHighLightLinks(this);
            return urlSpannableString;
        }
    }

    public void setUrlSpannableString(SpannableString urlSpannableString) {
        this.urlSpannableString = urlSpannableString;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getLikeCounts() {
        return likeCounts;
    }

    public void setLikeCounts(long likeCounts) {
        this.likeCounts = likeCounts;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<String> getImageLargeUrls() {
        return imageLargeUrls;
    }

    public void setImageLargeUrls(List<String> imageLargeUrls) {
        this.imageLargeUrls = imageLargeUrls;
    }

    @Override
    public HashMap<String, TopicUrl> getUrlMap() {
        return urlMap;
    }

    public void setUrlMap(HashMap<String, TopicUrl> urlMap) {
        this.urlMap = urlMap;
    }
}
