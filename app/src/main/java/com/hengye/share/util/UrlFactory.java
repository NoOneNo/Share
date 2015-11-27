package com.hengye.share.util;

public class UrlFactory {
    private static class UrlFactoryHolder{
        private final static UrlFactory INSTANCE = new UrlFactory();
    }

    private UrlFactory(){}

    public static UrlFactory getInstance(){
        return UrlFactoryHolder.INSTANCE;
    }

    //weibo url prefix
    private static final String URL_PREFIX_WEIBO = "https://api.weibo.com/2/";

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
    public String getWBFriendTopicUrl(){
        return URL_PREFIX_WEIBO + "statuses/friends_timeline.json";
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
    public String getWBFriendTopicIdsUrl(){
        return URL_PREFIX_WEIBO + "statuses/friends_timeline/ids.json";
    }

//    必选	类型及范围	说明
//    source	false	string	采用OAuth授权方式不需要此参数，其他授权方式为必填参数，数值为应用的AppKey。
//    access_token	false	string	采用OAuth授权方式为必填参数，其他授权方式不需要此参数，OAuth授权后获得。
//    uid	true	int64	需要获取消息未读数的用户UID，必须是当前登录用户。
//    callback	false	string	JSONP回调函数，用于前端调用返回JS格式的信息。
//    unread_message	false	boolean	未读数版本。0：原版未读数，1：新版未读数。默认为0。
//    获取某个用户的各种消息未读数
    public String getWBUserUnReadCountUrl(){
        return URL_PREFIX_WEIBO + "remind/unread_count.json";
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
    public String getWBUserInfoUrl(){
        return URL_PREFIX_WEIBO + "users/show.json";
    }
}
