package com.hengye.share.model;

import android.text.Spanned;
import android.widget.TextView;

import com.hengye.share.model.sina.WBStatus;
import com.hengye.share.model.sina.WBStatusComment;
import com.hengye.share.model.sina.WBStatusComments;
import com.hengye.share.model.sina.WBStatusReposts;
import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.DataUtil;
import com.hengye.share.util.GsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatusComment implements StatusId, StatusShortUrl, Serializable {

    public static StatusComments getComments(WBStatusComments wbStatusComments) {
        StatusComments statusComments = new StatusComments();
        statusComments.setComments(getCommentArrayList(wbStatusComments));
        statusComments.setTotalNumber(wbStatusComments.getTotal_number());
        return statusComments;
    }

    public static StatusComments getComments(WBStatusReposts wbStatusReposts) {
        StatusComments statusComments = new StatusComments();
        statusComments.setComments(getCommentArrayList(wbStatusReposts));
        statusComments.setTotalNumber(wbStatusReposts.getTotal_number());
        return statusComments;
    }

    public static ArrayList<StatusComment> getCommentArrayList(WBStatusComments wbStatusComments) {
        if (wbStatusComments == null || CommonUtil.isEmpty(wbStatusComments.getComments())) {
            return null;
        }
        ArrayList<StatusComment> statusComments = new ArrayList<>();
        for (WBStatusComment entity : wbStatusComments.getComments()) {
            statusComments.add(getComment(entity));
        }
        return statusComments;
    }

    public static StatusComment getComment(WBStatusComment entity) {
        StatusComment statusComment = new StatusComment();
        statusComment.setParent(new Parent(GsonUtil.toJson(entity), Parent.TYPE_WEIBO));
        if (entity == null) {
            return statusComment;
        }
        statusComment.setUserInfo(UserInfo.getUserInfo(entity.getUser()));
        statusComment.setDate(entity.getCreated_at());
        statusComment.setChannel(entity.getSource());
        statusComment.setContent(entity.getText());
        statusComment.setId(entity.getIdstr());
        statusComment.setLiked(entity.isLiked());
        statusComment.setLikeCounts(entity.getLike_counts());

        if (entity.getReply_comment() != null) {
            entity.getReply_comment().setReply_comment(null);
            statusComment.setReplyComment(getComment(entity.getReply_comment()));
            statusComment.setReplyed(true);
        } else {
            //判断是否是回复别人的评论，显示的时候要做区分；
            //如果不是根楼层，则是回复别人的评论，旧版是没有rootid的
            statusComment.setReplyed(entity.getText() != null && entity.getText().startsWith(REPLY_START_LABEL));
        }

        Status status = Status.getStatus(entity.getStatus());
        if (status.getRetweetedStatus() != null) {
            //这里拼接这条微博的内容+转发微博的内容
            Status retweetedStatus = status.getRetweetedStatus();
            status.setContent(status.getContent() +
                    "//@" + retweetedStatus.getUserInfo().getName() + ":" +
                    retweetedStatus.getContent());
        }
        statusComment.setStatus(status);

        return statusComment;
    }

    private static final String REPLY_START_LABEL = "回复";

    public static ArrayList<StatusComment> getCommentArrayList(WBStatusReposts wbStatusReposts) {
        if (wbStatusReposts == null || CommonUtil.isEmpty(wbStatusReposts.getReposts())) {
            return null;
        }
        ArrayList<StatusComment> statusComments = new ArrayList<>();
        for (WBStatus entity : wbStatusReposts.getReposts()) {
            statusComments.add(getComment(entity));
        }
        return statusComments;
    }

    public static StatusComment getComment(WBStatus entity) {
        StatusComment statusComment = new StatusComment();
        statusComment.setParent(new Parent(GsonUtil.toJson(entity), Parent.TYPE_WEIBO));
        if (entity == null) {
            return statusComment;
        }
        statusComment.setUserInfo(UserInfo.getUserInfo(entity.getUser()));
        statusComment.setDate(entity.getCreated_at());
        statusComment.setChannel(entity.getSource());
        statusComment.setContent(entity.getText());
        statusComment.setId(entity.getIdstr());
        statusComment.setStatus(Status.getStatus(entity.getRetweeted_status()));
        return statusComment;
    }

    public static class StatusCommentEvent {

        StatusComment statusComment;
        String targetStatusId;
        boolean isComment;

        public StatusCommentEvent(StatusComment statusComment, String targetStatusId, boolean isComment) {
            this.statusComment = statusComment;
            this.targetStatusId = targetStatusId;
            this.isComment = isComment;
        }

        public StatusComment getStatusComment() {
            return statusComment;
        }

        public String getTargetStatusId() {
            return targetStatusId;
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
    private String content;//评论内容, 如果是回复的话带"回复@XXX:"
    private String id;//评论或转发的唯一id
    private long likeCounts;//评论点赞数
    private boolean isLiked;//是否已经点赞
    private Status status;//评论或转发的主题
    private StatusComment replyComment;//回复的评论
    //是否是回复别人的评论，如果replyComment为空，但也可能是回复别人的评论
    //内容为"回复@XXX:"
    private boolean isReplyed;
    private List<String> imageUrls;//缩略图
    private List<String> imageLargeUrls;//原图

    private HashMap<String, StatusUrl> urlMap;

    private transient Spanned spanned;
    //评论微博的图片地址，如果是评论转发的微博，则是转发的微博的图片地址
    //如果没有则用评论微博用户的头像
    private transient String coverUrl;

    public void updateLiked(boolean isLiked) {
        setLiked(isLiked);
        setLikeCounts(getLikeCounts() + (isLiked ? 1 : -1));
    }

    @Override
    public String toString() {
        return "StatusComment{" +
                "parent=" + parent +
                ", userInfo=" + userInfo +
                ", date='" + date + '\'' +
                ", channel='" + channel + '\'' +
                ", content='" + content + '\'' +
                ", id='" + id + '\'' +
                ", likeCounts=" + likeCounts +
                ", isLiked=" + isLiked +
                ", status=" + status +
                ", imageUrls=" + imageUrls +
                ", imageLargeUrls=" + imageLargeUrls +
                ", urlMap=" + urlMap +
                ", spanned=" + spanned +
                '}';
    }

    public Status toStatus() {
        WBStatus wbStatus = GsonUtil.fromJson(getParent().getJson(), WBStatus.class);
        if (wbStatus != null) {
            return Status.getStatus(wbStatus);
        }
        return null;
    }

    public String toStatusJson() {
        Status status = toStatus();
        if (status != null) {
            return status.toJson();
        }
        return null;
    }

    public Spanned getSpanned(TextView textView) {
        return getSpanned(textView, false);
    }


    public Spanned getSpanned(TextView textView, boolean isReply) {
        return getSpanned(textView, isReply, true);
    }

    public Spanned getSpanned(TextView textView, boolean isReply, boolean largeEmoticon) {
        if (CommonUtil.isEmpty(spanned)) {
            int textSize = largeEmoticon ?
                    (int)(textView.getTextSize() * 1.5) :
                    (int)textView.getTextSize();
            DataUtil.addStatusContentHighLightLinks(textSize, this, isReply);
        }
        return spanned;
    }

    public void setSpanned(Spanned spanned) {
        this.spanned = spanned;
    }

    public String getCoverUrl() {
        if (coverUrl == null) {
            if (!CommonUtil.isEmpty(status.getImageUrls())) {
                coverUrl = status.getImageUrls().get(0);
            } else if (status.getRetweetedStatus() != null &&
                    !CommonUtil.isEmpty(status.getRetweetedStatus().getImageUrls())) {
                coverUrl = status.getRetweetedStatus().getImageUrls().get(0);
            } else {
                coverUrl = status.getUserInfo().getAvatar();
            }

            //避免重复获取
            if (coverUrl == null) {
                coverUrl = "";
            }
        }
        return coverUrl;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public StatusComment getReplyComment() {
        return replyComment;
    }

    public void setReplyComment(StatusComment replyComment) {
        this.replyComment = replyComment;
    }

    public boolean isReplyed() {
        return isReplyed;
    }

    public void setReplyed(boolean replyed) {
        isReplyed = replyed;
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
}
