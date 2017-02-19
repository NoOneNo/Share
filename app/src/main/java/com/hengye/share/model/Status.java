package com.hengye.share.model;

import android.text.Spanned;
import android.widget.TextView;

import com.hengye.share.model.sina.WBStatus;
import com.hengye.share.model.sina.WBStatusComment;
import com.hengye.share.model.sina.WBStatusComments;
import com.hengye.share.model.sina.WBStatuses;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.DateUtil;
import com.hengye.share.util.thirdparty.WBUtil;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.GsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Status extends ParentInherit implements StatusId, StatusShortUrl, Serializable{

    public static ArrayList<Status> getStatuses(WBStatuses wbStatuses){
        return getStatuses(wbStatuses != null ? wbStatuses.getStatuses() : null);
    }

    public static ArrayList<Status> getStatuses(List<WBStatus> wbStatuses){
        if(CommonUtil.isEmpty(wbStatuses)){
            return null;
        }
        ArrayList<Status> topics = new ArrayList<>();
        for(WBStatus entity : wbStatuses){
            topics.add(getStatus(entity));
        }
        return topics;
    }

    public static Status getStatus(WBStatus entity){
        Status status = new Status();
        status.setParent(new Parent(GsonUtil.toJson(entity), Parent.TYPE_WEIBO));
        if(entity == null){
            return status;
        }
        status.setFromMobile(entity.isFromMobile());
        status.setUserInfo(UserInfo.getUserInfo(entity.getUser()));
        status.setDate(entity.getCreated_at());
        status.setChannel(entity.getSource());
        status.setContent(entity.getText());
        status.setId(entity.getIdstr());
        status.setFavorited(entity.isFavorited());
        status.setLiked(entity.isLiked());
        status.setRepostsCount(entity.getReposts_count());
        status.setCommentsCount(entity.getComments_count());
        status.setAttitudesCount(entity.getAttitudes_count());
        if(!CommonUtil.isEmpty(entity.getPic_urls())){
            List<String> imageUrls = new ArrayList<>();
            List<String> imageLargeUrls = new ArrayList<>();
            for(WBStatus.Pic_urlsEntity urlsEntity : entity.getPic_urls()){
                String wbImgType = WBUtil.getWBImgType();
                imageUrls.add(WBUtil.getWBImgUrl(urlsEntity.getThumbnail_pic(), wbImgType));
                imageLargeUrls.add(WBUtil.getWBLargeImgUrl(urlsEntity.getThumbnail_pic()));
            }
            status.setImageUrls(imageUrls);
            status.setImageLargeUrls(imageLargeUrls);
        }else if(!CommonUtil.isEmpty(entity.getPic_ids())){
            List<String> imageUrls = new ArrayList<>();
            List<String> imageLargeUrls = new ArrayList<>();
            for(String pic_id : entity.getPic_ids()){
                String wbImgType = WBUtil.getWBImgType();
                String wbImgUrl = WBUtil.getWBImgUrlById(pic_id);
                imageUrls.add(WBUtil.getWBImgUrl(wbImgUrl, wbImgType));
                imageLargeUrls.add(WBUtil.getWBLargeImgUrl(wbImgUrl));
            }
            status.setImageUrls(imageUrls);
            status.setImageLargeUrls(imageLargeUrls);
        }

        if(entity.getRetweeted_status() != null){
            //获得一条转发的微博, 防止一直递归
            entity.getRetweeted_status().setRetweeted_status(null);
            status.setRetweetedStatus(getStatus(entity.getRetweeted_status()));
        }
        return status;
    }

    public static ArrayList<Status> getStatuses(WBStatusComments wbStatusComments){
        if(wbStatusComments == null || CommonUtil.isEmpty(wbStatusComments.getComments())){
            return null;
        }
        ArrayList<Status> topics = new ArrayList<>();
        for(WBStatusComment entity : wbStatusComments.getComments()){
            topics.add(getStatus(entity));
        }
        return topics;
    }

    public static Status getStatus(WBStatusComment entity){
        Status status = new Status();
        status.setParent(new Parent(GsonUtil.toJson(entity), Parent.TYPE_WEIBO));
        if(entity == null){
            return status;
        }
        status.setUserInfo(UserInfo.getUserInfo(entity.getUser()));
        status.setDate(entity.getCreated_at());
        status.setChannel(entity.getSource());
        status.setContent(entity.getText());
        status.setId(entity.getIdstr());
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
            status.setRetweetedStatus(getStatus(entity.getStatus()));
        }

        //如果是回复评论，优先用回复评论覆盖转发微博
        if(entity.getReply_comment() != null){
            //获得一条转发的微博, 防止一直递归
            entity.getReply_comment().setReply_comment(null);
            status.setRetweetedStatus(getStatus(entity.getReply_comment()));
        }
        return status;
    }

    public static WBStatus getWBStatus(Status status){
        if(status.getParent() == null || status.getParent().getType() != Parent.TYPE_WEIBO){
            return null;
        }
        return GsonUtil.fromJson(status.getParent().getJson(), WBStatus.class);
    }

    /**
     * @param statuses
     * @return 返回包括当前微博和转发的微博的集合
     */
    public static HashSet<Status> getAllStatus(List<Status> statuses){
        if(CommonUtil.isEmpty(statuses)){
            return null;
        }

        HashSet<Status> set = new HashSet<>();
        for(Status topic : statuses){
            set.add(topic);
            if(topic.getRetweetedStatus() != null){
                set.add(topic.getRetweetedStatus());
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

    private Status retweetedStatus;//被转发的主题
    private UserInfo userInfo;//用户信息

    private boolean favorited;
    private boolean liked;

    private HashMap<String, StatusUrl> urlMap;

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

        Status topic = (Status) o;

        return date.equals(topic.date) && id.equals(topic.id);

    }

    @Override
    public int hashCode() {
        return date.hashCode();
    }

    @Override
    public String toString() {
        return "Status{" +
                ", date='" + date + '\'' +
                ", channel='" + channel + '\'' +
                ", content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", imageUrls=" + imageUrls +
                ", imageLargeUrls=" + imageLargeUrls +
                ", retweetedStatus=" + retweetedStatus +
                ", userInfo=" + userInfo +
                '}';
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Status getRetweetedStatus() {
        return retweetedStatus;
    }

    public void setRetweetedStatus(Status retweetedStatus) {
        this.retweetedStatus = retweetedStatus;
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
    public HashMap<String, StatusUrl> getUrlMap() {
        return urlMap;
    }

    public void setUrlMap(HashMap<String, StatusUrl> urlMap) {
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
            DataUtil.addStatusContentHighLightLinks((int)textView.getTextSize(), this, isRetweeted);
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
