package com.hengye.share.model;

import android.text.Spanned;
import android.widget.TextView;

import com.hengye.share.R;
import com.hengye.share.model.sina.WBTopic;
import com.hengye.share.model.sina.WBTopicComment;
import com.hengye.share.model.sina.WBTopicComments;
import com.hengye.share.model.sina.WBTopics;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.ResUtil;
import com.hengye.share.util.thirdparty.WBUtil;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.GsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Topic extends ParentInherit implements TopicId, TopicShortUrl, Serializable{

    public static ArrayList<Topic> getTopics(WBTopics wbTopics){
        return getTopics(wbTopics != null ? wbTopics.getStatuses() : null);
    }

    public static ArrayList<Topic> getTopics(List<WBTopic> wbTopics){
        if(CommonUtil.isEmpty(wbTopics)){
            return null;
        }
        ArrayList<Topic> topics = new ArrayList<>();
        for(WBTopic entity : wbTopics){
            topics.add(getTopic(entity));
        }
        return topics;
    }

    public static Topic getTopic(WBTopic entity){
        Topic topic = new Topic();
        topic.setParent(new Parent(GsonUtil.toJson(entity), Parent.TYPE_WEIBO));
        if(entity == null){
            return topic;
        }
        topic.setFromMobile(entity.isFromMobile());
        topic.setUserInfo(UserInfo.getUserInfo(entity.getUser()));
        topic.setDate(entity.getCreated_at());
        topic.setChannel(entity.getSource());
        topic.setContent(entity.getText());
        topic.setId(entity.getIdstr());
        topic.setFavorited(entity.isFavorited());
        topic.setLiked(entity.isLiked());
        topic.setRepostsCount(entity.getReposts_count());
        topic.setCommentsCount(entity.getComments_count());
        topic.setAttitudesCount(entity.getAttitudes_count());
        if(!CommonUtil.isEmpty(entity.getPic_urls())){
            List<String> imageUrls = new ArrayList<>();
            List<String> imageLargeUrls = new ArrayList<>();
            for(WBTopic.Pic_urlsEntity urlsEntity : entity.getPic_urls()){
                String wbImgType = WBUtil.getWBImgType();
                imageUrls.add(WBUtil.getWBImgUrl(urlsEntity.getThumbnail_pic(), wbImgType));
                imageLargeUrls.add(WBUtil.getWBLargeImgUrl(urlsEntity.getThumbnail_pic()));
            }
            topic.setImageUrls(imageUrls);
            topic.setImageLargeUrls(imageLargeUrls);
        }else if(!CommonUtil.isEmpty(entity.getPic_ids())){
            List<String> imageUrls = new ArrayList<>();
            List<String> imageLargeUrls = new ArrayList<>();
            for(String pic_id : entity.getPic_ids()){
                String wbImgType = WBUtil.getWBImgType();
                String wbImgUrl = WBUtil.getWBImgUrlById(pic_id);
                imageUrls.add(WBUtil.getWBImgUrl(wbImgUrl, wbImgType));
                imageLargeUrls.add(WBUtil.getWBLargeImgUrl(wbImgUrl));
            }
            topic.setImageUrls(imageUrls);
            topic.setImageLargeUrls(imageLargeUrls);
        }

        if(entity.getRetweeted_status() != null){
            //获得一条转发的微博, 防止一直递归
            entity.getRetweeted_status().setRetweeted_status(null);
            topic.setRetweetedTopic(getTopic(entity.getRetweeted_status()));
        }
        return topic;
    }

    public static ArrayList<Topic> getTopics(WBTopicComments wbTopicComments){
        if(wbTopicComments == null || CommonUtil.isEmpty(wbTopicComments.getComments())){
            return null;
        }
        ArrayList<Topic> topics = new ArrayList<>();
        for(WBTopicComment entity : wbTopicComments.getComments()){
            topics.add(getTopic(entity));
        }
        return topics;
    }

    public static Topic getTopic(WBTopicComment entity){
        Topic topic = new Topic();
        topic.setParent(new Parent(GsonUtil.toJson(entity), Parent.TYPE_WEIBO));
        if(entity == null){
            return topic;
        }
        topic.setUserInfo(UserInfo.getUserInfo(entity.getUser()));
        topic.setDate(entity.getCreated_at());
        topic.setChannel(entity.getSource());
        topic.setContent(entity.getText());
        topic.setId(entity.getIdstr());
//        if(!CommonUtil.isEmpty(entity.getPic_urls())){
//            List<String> imageUrls = new ArrayList<>();
//            List<String> imageLargeUrls = new ArrayList<>();
//            for(WBTopic.Pic_urlsEntity urlsEntity : entity.getPic_urls()){
//                imageUrls.add(WBUtil.getWBImgUrl(urlsEntity.getThumbnail_pic(), WBUtil.IMAGE_TYPE_BMIDDLE));
//                imageLargeUrls.add(WBUtil.getWBImgUrl(urlsEntity.getThumbnail_pic(), WBUtil.IMAGE_TYPE_LARGE));
//            }
//            topic.setImageUrls(imageUrls);
//            topic.setImageLargeUrls(imageLargeUrls);
//        }

        if(entity.getStatus() != null){
            //获得一条转发的微博, 防止一直递归
            entity.getStatus().setRetweeted_status(null);
            topic.setRetweetedTopic(getTopic(entity.getStatus()));
        }

        //如果是回复评论，优先用回复评论覆盖转发微博
        if(entity.getReply_comment() != null){
            //获得一条转发的微博, 防止一直递归
            entity.getReply_comment().setReply_comment(null);
            topic.setRetweetedTopic(getTopic(entity.getReply_comment()));
        }
        return topic;
    }

    public static WBTopic getWBTopic(Topic topic){
        if(topic.getParent() == null || topic.getParent().getType() != Parent.TYPE_WEIBO){
            return null;
        }
        return GsonUtil.fromJson(topic.getParent().getJson(), WBTopic.class);
    }

    /**
     * @param topics
     * @return 返回包括当前微博和转发的微博的集合
     */
    public static HashSet<Topic> getAllTopic(List<Topic> topics){
        if(CommonUtil.isEmpty(topics)){
            return null;
        }

        HashSet<Topic> set = new HashSet<>();
        for(Topic topic : topics){
            set.add(topic);
            if(topic.getRetweetedTopic() != null){
                set.add(topic.getRetweetedTopic());
            }
        }
        return set;
    }

    private static final long serialVersionUID = 971288752432928272L;

    private String date;//创建日期
    private String channel;//渠道，通过什么发表
    private String content;//内容
    private String id;//主题的唯一id
    private List<String> imageUrls;//缩略图
    private List<String> imageLargeUrls;//原图
    private int repostsCount;
    private int commentsCount;
    private int attitudesCount;

    private Topic retweetedTopic;//被转发的主题
    private UserInfo userInfo;//用户信息

    private boolean favorited;
    private boolean liked;

    private HashMap<String, TopicUrl> urlMap;

    private boolean fromMobile;//如果请求wap端的接口，格式是网页格式需要额外处理，创建日期也已经格式化；
    private transient Spanned spanned;
    private transient String formatDate;

    public String toJson(){
        return GsonUtil.toJson(this);
    }

    public void updateLiked(boolean liked){
        setLiked(liked);
        setAttitudesCount(getAttitudesCount() + (liked ? 1 : -1));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        if(date == null){
            return false;
        }

        Topic topic = (Topic) o;

        return date.equals(topic.date) && id.equals(topic.id);

    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public String toString() {
        return "Topic{" +
                ", date='" + date + '\'' +
                ", channel='" + channel + '\'' +
                ", content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", imageUrls=" + imageUrls +
                ", imageLargeUrls=" + imageLargeUrls +
                ", retweetedTopic=" + retweetedTopic +
                ", userInfo=" + userInfo +
                '}';
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Topic getRetweetedTopic() {
        return retweetedTopic;
    }

    public void setRetweetedTopic(Topic retweetedTopic) {
        this.retweetedTopic = retweetedTopic;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getRepostsCount() {
        return repostsCount;
    }

    public void setRepostsCount(int repostsCount) {
        this.repostsCount = repostsCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getAttitudesCount() {
        return attitudesCount;
    }

    public void setAttitudesCount(int attitudesCount) {
        this.attitudesCount = attitudesCount;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isFromMobile() {
        return fromMobile;
    }

    public void setFromMobile(boolean fromMobile) {
        this.fromMobile = fromMobile;
    }

    //    public SpannableString getSpanned() {
//        return getSpanned(false);
//    }

    public Spanned getSpanned(TextView textView, boolean isRetweeted) {
        if (CommonUtil.isEmpty(spanned)) {
            DataUtil.addTopicContentHighLightLinks((int)textView.getTextSize(), this, isRetweeted);
        }
        return spanned;
    }

    public void setSpanned(Spanned spanned) {
        this.spanned = spanned;
    }

    public String getFormatDate() {
        if(formatDate == null){
            formatDate = isFromMobile() ? getDate() : DateUtil.getEarlyDateFormat(getDate());
        }
        return formatDate;
    }

//    public String getfAttitudesCount() {
//        if(fAttitudesCount == null){
//            fAttitudesCount = ResUtil.getString(R.string.label_topic_repost_number, DataUtil.getCounter(getRepostsCount()));
//        }
//        return fAttitudesCount;
//    }
//
//    public String getfCommentsCount() {
//        return fCommentsCount;
//    }
//
//    public String getfRepostsCount() {
//        return fRepostsCount;
//    }
}
