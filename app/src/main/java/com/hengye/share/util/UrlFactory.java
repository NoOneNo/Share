package com.hengye.share.util;

public class UrlFactory {
    private static class UrlFactoryHolder {
        private final static UrlFactory INSTANCE = new UrlFactory();
    }

    private UrlFactory() {
    }

    public static UrlFactory getInstance() {
        return UrlFactoryHolder.INSTANCE;
    }

    //weibo url prefix
    private static final String URL_PREFIX_WEIBO = "https://api.weibo.com/2/";

    /**
     * 微博接口
     */
    //获得微博
    public static final String WB_USER_TOPIC = "statuses/user_timeline.json";
    //获得微博
    public static final String WB_FRIEND_TOPIC = "statuses/friends_timeline.json";
    //获得微博 ID列表
    public static final String WB_FRIEND_TOPIC_IDS = "statuses/friends_timeline/ids.json";
    //获得好友圈微博
    public static final String WB_BILATERAL_TOPIC = "statuses/bilateral_timeline.json";
    //获取某个用户的各种消息未读数
    public static final String WB_UNREAD_COUNT = "remind/unread_count.json";
    //获取用户信息
    public static final String WB_USER_INFO = "users/show.json";
    //获取某个微博的评论列表
    public static final String WB_COMMENT_SHOW = "comments/show.json";
    //获取某个微博的转发列表
    public static final String WB_REPOST_SHOW = "statuses/repost_timeline.json";
    //我发出的评论列表
    public static final String WB_COMMENT_BY_ME = "comments/by_me.json";
    //我收到的评论列表
    public static final String WB_COMMENT_TO_ME = "comments/to_me.json";
    //@我的评论列表
    public static final String WB_COMMENT_MENTION = "comments/mentions.json";
    //@我的微博
    public static final String WB_TOPIC_MENTION = "statuses/mentions.json";
    //我收藏的微博
    public static final String WB_FAVORITES_TOPIC = "favorites.json";
    //发表微博
    public static final String WB_PUBLISH_TOPIC = "statuses/update.json";
    //获取用户的关注列表
    public static final String WB_USER_ATTENTION = "friendships/friends.json";
    //对微博进行评论
    public static final String WB_COMMENT_TOPIC = "comments/create.json";
    //回复评论
    public static final String WB_COMMENT_REPLY = "comments/reply.json";
    //转发微博
    public static final String WB_REPOST_TOPIC = "statuses/repost.json";
    //搜索用户
    public static final String WB_SEARCH_USER = "search/users.json";
//    https://api.weibo.com/2/search/users.json?q=uu&user_id=2207519004&count=20&page=1&filter_ori=0&filter_pic=0&access_token=2.00OXW56C06XASOc5a146b1b0pVluLB
    //联想用户
    public static final String WB_SEARCH_USER_SUGGESTION = "http://s.weibo.com/ajax/suggestion";
//    http://s.weibo.com/ajax/suggestion?where=gs_weibo&type=gs_weibo&key=uue&access_token=2.00OXW56C06XASOc5a146b1b0pVluLB
    //搜索微博
    public static final String WB_SEARCH_PUBLIC = "search/public.json";
//    https://api.weibo.com/2/search/public.json?q=uu&sid=o_weico&sort=social&source=211160679&user_id=2207519004&count=20&page=1&access_token=2.00OXW56C06XASOc5a146b1b0pVluLB
    //搜索话题
    public static final String WB_SEARCH_TOPIC = "search/topics.json";
    //添加收藏微博
    public static final String WB_FAVORITES_CREATE = "favorites/create.json";
    //删除收藏微博
    public static final String WB_FAVORITES_DESTROY = "favorites/destroy.json";
    /**
     * 微博接口
     */

    public static String getWBUrlPrefix(){
        return URL_PREFIX_WEIBO;
    }

    public String getWBUserTopicUrl() {
        return getWBUrlPrefix() + WB_USER_TOPIC;
    }

    //    必选 	类型及范围 	说明
//    source 	false 	string 	采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
//    access_token 	false 	string 	采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
//    since_id 	false 	int64 	若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
//    max_id 	false 	int64 	若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
//    count 	false 	int 	单页返回的记录条数，最大不超过100，默认为20。
//    page 	false 	int 	返回结果的页码，默认为1。
//    base_app 	false 	int 	是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0。
//    feature 	false 	int 	过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
//    trim_user 	false 	int 	返回值中user字段开关，0：返回完整user字段、1：user字段仅返回user_id，默认为0。
//    获得微博
    public String getWBFriendTopicUrl() {
        return getWBUrlPrefix() + WB_FRIEND_TOPIC;
    }

    //获得好友圈微博
    public String getWBBilateralTopicUrl() {
        return getWBUrlPrefix() + WB_BILATERAL_TOPIC;
    }

    //    必选 	类型及范围 	说明
//    source 	false 	string 	采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
//    access_token 	false 	string 	采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
//    since_id 	false 	int64 	若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
//    max_id 	false 	int64 	若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
//    count 	false 	int 	单页返回的记录条数，最大不超过100，默认为20。
//    page 	false 	int 	返回结果的页码，默认为1。
//    base_app 	false 	int 	是否只获取当前应用的数据。0为否（所有数据），1为是（仅当前应用），默认为0。
//    feature 	false 	int 	过滤类型ID，0：全部、1：原创、2：图片、3：视频、4：音乐，默认为0。
//    获得微博的ID数
    public String getWBFriendTopicIdsUrl() {
        return getWBUrlPrefix() + WB_FRIEND_TOPIC_IDS;
    }

    //    必选	类型及范围	说明
//    source	false	string	采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
//    access_token	false	string	采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
//    uid	true	int64	需要获取消息未读数的用户UID，必须是当前登录用户。
//    callback	false	string	JSONP回调函数，用于前端调用返回JS格式的信息。
//    unread_message	false	boolean	未读数版本。0：原版未读数，1：新版未读数。默认为0。
//    获取某个用户的各种消息未读数
    public String getWBUserUnReadCountUrl() {
        return getWBUrlPrefix() + WB_UNREAD_COUNT;
    }

    //    必选 	类型及范围 	说明
//    source 	false 	string 	采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
//    access_token 	false 	string 	采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
//    uid 	false 	int64 	需要查询的用户ID。
//    screen_name 	false 	string 	需要查询的用户昵称。
//    注意事项
//
//    参数uid与screen_name二者必选其一，且只能选其一；
//    接口升级后，对未授权本应用的uid，将无法获取其个人简介、认证原因、粉丝数、关注数、微博数及最近一条微博内容
    public String getWBUserInfoUrl() {
        return getWBUrlPrefix() + WB_USER_INFO;
    }


    //    必选	类型及范围	说明
//    source	false	string	采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
//    access_token	false	string	采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
//    id	true	int64	需要查询的微博ID。
//    since_id	false	int64	若指定此参数，则返回ID比since_id大的评论（即比since_id时间晚的评论），默认为0。
//    max_id	false	int64	若指定此参数，则返回ID小于或等于max_id的评论，默认为0。
//    count	false	int	单页返回的记录条数，默认为50。
//    page	false	int	返回结果的页码，默认为1。
//    filter_by_author	false	int	作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。
    public String getWBCommentUrl() {
        return getWBUrlPrefix() + WB_COMMENT_SHOW;
    }

    //    必选	类型及范围	说明
//    source	false	string	采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
//    access_token	false	string	采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
//    id	true	int64	需要查询的微博ID。
//    since_id	false	int64	若指定此参数，则返回ID比since_id大的微博（即比since_id时间晚的微博），默认为0。
//    max_id	false	int64	若指定此参数，则返回ID小于或等于max_id的微博，默认为0。
//    count	false	int	单页返回的记录条数，最大不超过200，默认为20。
//    page	false	int	返回结果的页码，默认为1。
//    filter_by_author	false	int	作者筛选类型，0：全部、1：我关注的人、2：陌生人，默认为0。
    public String getWBRepostUrl() {
        return getWBUrlPrefix() + WB_REPOST_SHOW;
    }

    //我发出的评论列表
    public String getWBCommentByMeUrl() {
        return getWBUrlPrefix() + WB_COMMENT_BY_ME;
    }

    //我收到的评论列表
    public String getWBCommentToMeUrl() {
        return getWBUrlPrefix() + WB_COMMENT_TO_ME;
    }

    //@我的评论列表
    public String getWBCommentMentionUrl() {
        return getWBUrlPrefix() + WB_COMMENT_MENTION;
    }

    //@我的微博
    public String getWBTopicMentionUrl() {
        return getWBUrlPrefix() + WB_TOPIC_MENTION;
    }

    //我收藏的微博
    public String getWBTopicFavoritesUrl() {
        return getWBUrlPrefix() + WB_FAVORITES_TOPIC;
    }

    //发表微博
    public String getWBTopicPublishUrl() {
        return getWBUrlPrefix() + WB_PUBLISH_TOPIC;
    }

    //获取用户的关注列表
    public String getWBAttentionUrl() {
        return getWBUrlPrefix() + WB_USER_ATTENTION;
    }

    //    comment	true	string	评论内容，必须做URLencode，内容不超过140个汉字。
    //    id	true	int64	需要评论的微博ID。
    //    comment_ori	false	int	当评论转发微博时，是否评论给原微博，0：否、1：是，默认为0。
    //对微博进行评论
    public String getWBTopicCommentCreateUrl() {
        return getWBUrlPrefix() + WB_COMMENT_TOPIC;
    }

    //    cid	true	int64	需要回复的评论ID。
    //    id	true	int64	需要评论的微博ID。
    //    comment	true	string	回复评论内容，必须做URLencode，内容不超过140个汉字。
    //    without_mention	false	int	回复中是否自动加入“回复@用户名”，0：是、1：否，默认为0。
    //    comment_ori	false	int	当评论转发微博时，是否评论给原微博，0：否、1：是，默认为0。
    //回复评论
    public String getWBTopicCommentReplyUrl() {
        return getWBUrlPrefix() + WB_COMMENT_REPLY;
    }

    //    id	true	int64	要转发的微博ID。
//    status	false	string	添加的转发文本，必须做URLencode，内容不超过140个汉字，不填则默认为“转发微博”。
//    is_comment	false	int	是否在转发的同时发表评论，0：否、1：评论给当前微博、2：评论给原微博、3：都评论，默认为0 。
    //转发微博
    public String getWBTopicRepostUrl() {
        return getWBUrlPrefix() + WB_REPOST_TOPIC;
    }

    //搜索用户
    public String getWBSearchUserUrl() {
        return getWBUrlPrefix() + WB_SEARCH_USER;
    }

    //搜索用户建议
    public String getWBSearchUserSuggestionUrl() {
        return WB_SEARCH_USER_SUGGESTION;
    }

    //搜索微博
    public String getWBSearchPublicUrl() {
        return getWBUrlPrefix() + WB_SEARCH_PUBLIC;
    }

    //搜索话题
    public String getWBSearchTopicUrl() {
        return getWBUrlPrefix() + WB_SEARCH_TOPIC;
    }

    //添加收藏微博
    public String getWBCreateFavoritesUrl() {
        return getWBUrlPrefix() + WB_FAVORITES_CREATE;
    }

    //删除收藏微博
    public String getWBDestroyFavoritesUrl() {
        return getWBUrlPrefix() + WB_FAVORITES_DESTROY;
    }
}