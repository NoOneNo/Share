package com.hengye.share.module.sina;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Topic implements Serializable{

    private static final long serialVersionUID = -6946098874302619957L;

    /**
     * https://api.weibo.com/2/statuses/friends_timeline.json
     */
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


    /**
     * statuses : [{"created_at":"Mon Nov 02 22:27:36 +0800 2015","id":3904859252596728,"mid":"3904859252596728","idstr":"3904859252596728","text":"这个视频一点都不萌，就看了好几遍~[笑cry] (cr.梅梅盼达) http://t.cn/RUIlA32","source_allowclick":0,"source_type":1,"source":"<a href=\"http://weibo.com/\" rel=\"nofollow\">微博 weibo.com<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":1713926427,"idstr":"1713926427","class":1,"screen_name":"微博搞笑排行榜","name":"微博搞笑排行榜","province":"44","city":"1000","location":"广东","description":"微博搞笑中心！每天搜罗最搞笑最好玩的微博。关注我，获得每日新鲜笑料！（欢迎合作，投稿）↖(^ω^)↗","url":"http://weibo.com/mkdqs","profile_image_url":"http://tp4.sinaimg.cn/1713926427/50/40000875722/0","cover_image_phone":"http://ww3.sinaimg.cn/crop.0.0.640.640.640/6ce2240djw1e9oaaziu7sj20hs0hs0ui.jpg","profile_url":"topgirls8","domain":"topgirls8","weihao":"","gender":"f","followers_count":27158185,"friends_count":837,"pagefriends_count":0,"statuses_count":88408,"favourites_count":7056,"created_at":"Fri Mar 19 00:42:49 +0800 2010","following":true,"allow_all_act_msg":true,"geo_enabled":false,"verified":false,"verified_type":-1,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/1713926427/180/40000875722/0","avatar_hd":"http://tp4.sinaimg.cn/1713926427/180/40000875722/0","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"online_status":0,"bi_followers_count":297,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":6,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":37},"reposts_count":10,"comments_count":20,"attitudes_count":46,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_ids":[230444],"biz_feature":0,"darwin_tags":[],"rid":"0_0_1_2666885038303173078","userType":0},{"created_at":"Mon Nov 02 22:26:40 +0800 2015","id":3904859017291576,"mid":"3904859017291576","idstr":"3904859017291576","text":"跟着笑起来惹[笑cry] //@亮亮若水:笑面人生 //@用户tnku4ory3n://@榮生若夢:弟弟的笑声好萌啊，哈哈吼吼吼[笑cry][笑cry][笑cry]","source_allowclick":0,"source_type":2,"source":"","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":1602704111,"idstr":"1602704111","class":1,"screen_name":"__槿","name":"__槿","province":"52","city":"1","location":"贵州 贵阳","description":"【轻旁人之言 重笃于己心】【並不是\u201c明天將會到來\u201d 而是\u201c我自己將要前去\u201d啊 】【2.5次元】","url":"http://blog.sina.com.cn/plutojinkshi","profile_image_url":"http://tp4.sinaimg.cn/1602704111/50/5741369928/0","cover_image_phone":"http://ww2.sinaimg.cn/crop.0.0.0.0/5f8752efjw1er68w2g5ukj20hs0hrq3h.jpg","profile_url":"42750399","domain":"jinkpluto","weihao":"42750399","gender":"f","followers_count":1347,"friends_count":576,"pagefriends_count":0,"statuses_count":15414,"favourites_count":7262,"created_at":"Wed Apr 07 15:37:37 +0800 2010","following":true,"allow_all_act_msg":false,"geo_enabled":true,"verified":false,"verified_type":220,"remark":"","ptype":0,"allow_all_comment":false,"avatar_large":"http://tp4.sinaimg.cn/1602704111/180/5741369928/0","avatar_hd":"http://ww3.sinaimg.cn/crop.0.0.750.750.1024/5f8752efjw8exm1eizx7uj20ku0kuq3r.jpg","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":true,"online_status":0,"bi_followers_count":289,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":4,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"cardid":"vip_002","urank":32},"pid":3904856542674047,"retweeted_status":{"created_at":"Mon Nov 02 18:36:31 +0800 2015","id":3904801110269378,"mid":"3904801110269378","idstr":"3904801110269378","text":"(o≖◡≖) http://t.cn/RUx4zwl .","source_allowclick":0,"source_type":2,"source":"<a href=\"http://weibo.com/\" rel=\"nofollow\">BKK吴彦祖iPhone<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":2955953283,"idstr":"2955953283","class":1,"screen_name":"肉兔兔兔兔","name":"肉兔兔兔兔","province":"100","city":"1000","location":"其他","description":"关爱Captain成长协会 | Kise | 🚫真人  | 大自然的搬运工* 【翻译请勿二改】 一点都不敬业的营销号一枚😒 有事评论@or私信✨你管我喜欢谁呢😃","url":"","profile_image_url":"http://tp4.sinaimg.cn/2955953283/50/5738906449/1","cover_image":"http://ww2.sinaimg.cn/crop.0.0.920.300/b0304483jw1euxu9drs1qj20pk08cq5w.jpg","cover_image_phone":"http://ww4.sinaimg.cn/crop.0.0.0.0/b0304483jw1ew7ncv4jghj20yi0yi42m.jpg","profile_url":"u/2955953283","domain":"","weihao":"","gender":"m","followers_count":9938,"friends_count":80,"pagefriends_count":0,"statuses_count":8570,"favourites_count":233,"created_at":"Sat Sep 22 23:12:34 +0800 2012","following":false,"allow_all_act_msg":false,"geo_enabled":false,"verified":false,"verified_type":-1,"remark":"","ptype":0,"allow_all_comment":false,"avatar_large":"http://tp4.sinaimg.cn/2955953283/180/5738906449/1","avatar_hd":"http://ww3.sinaimg.cn/crop.0.0.1242.1242.1024/b0304483jw8ewp2q13n85j20yi0yi0ut.jpg","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"online_status":0,"bi_followers_count":43,"lang":"zh-cn","star":0,"mbtype":11,"mbrank":2,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"cardid":"vip_002","urank":25},"annotations":[{"client_mblogid":"iPhone-6E625640-FE74-4444-BECD-5F1DFE25B644"},{"mapi_request":true}],"reposts_count":50,"comments_count":14,"attitudes_count":35,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_ids":[230444],"biz_feature":4294967304,"darwin_tags":[],"userType":0,"cardid":"vip_002"},"annotations":[{"mapi_request":true}],"reposts_count":0,"comments_count":0,"attitudes_count":0,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"rid":"1_0_1_2666885038303173078","userType":0,"cardid":"vip_002"},{"created_at":"Mon Nov 02 22:22:25 +0800 2015","id":3904857947665673,"mid":"3904857947665673","idstr":"3904857947665673","text":"迷醉！哈哈哈哈","source_allowclick":0,"source_type":1,"source":"<a href=\"http://app.weibo.com/t/feed/5yiHuw\" rel=\"nofollow\">iPhone 6 Plus<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":1954132215,"idstr":"1954132215","class":1,"screen_name":"卧槽看什么看","name":"卧槽看什么看","province":"100","city":"1000","location":"其他","description":"GoGoGoGoGo","url":"","profile_image_url":"http://tp4.sinaimg.cn/1954132215/50/5740032145/0","cover_image_phone":"http://ww4.sinaimg.cn/crop.0.0.640.640.640/6ce2240djw1e8iktk4ohij20hs0hsmz6.jpg","profile_url":"u/1954132215","domain":"","weihao":"","gender":"f","followers_count":551,"friends_count":313,"pagefriends_count":0,"statuses_count":267,"favourites_count":9,"created_at":"Sun Feb 06 20:03:27 +0800 2011","following":true,"allow_all_act_msg":false,"geo_enabled":true,"verified":false,"verified_type":220,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/1954132215/180/5740032145/0","avatar_hd":"http://ww1.sinaimg.cn/crop.0.0.1242.1242.1024/7479b0f7jw8ex44zbeah3j20yi0yi0vy.jpg","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":true,"online_status":0,"bi_followers_count":94,"lang":"zh-cn","star":0,"mbtype":0,"mbrank":0,"block_word":0,"block_app":0,"credit_score":80,"user_ability":0,"urank":21},"retweeted_status":{"created_at":"Mon Nov 02 22:02:15 +0800 2015","id":3904852868867960,"mid":"3904852868867960","idstr":"3904852868867960","text":"#阿木资源社# 一位推主说家里的猫头鹰笑声特别魔性，听完不自觉的跟着哈哈哈哈起来[笑cry][笑cry][笑cry][笑cry]http://t.cn/RUxJOOu","source_allowclick":0,"source_type":1,"source":"<a href=\"http://weibo.com/\" rel=\"nofollow\">微博 weibo.com<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":2726601057,"idstr":"2726601057","class":1,"screen_name":"进击的阿木君","name":"进击的阿木君","province":"44","city":"1000","location":"广东","description":"发布动漫一手信息，P图小能手，酷爱搬运日推汤不热，偶尔制作点动图。","url":"","profile_image_url":"http://tp2.sinaimg.cn/2726601057/50/40079628039/1","cover_image":"http://ww4.sinaimg.cn/crop.0.0.920.300/a284a161gw1eoil3tep9rj20pk08cgon.jpg","cover_image_phone":"http://ww2.sinaimg.cn/crop.0.0.0.0/a284a161jw1esiuc03tsaj20ku0kut9u.jpg","profile_url":"u/2726601057","domain":"","weihao":"","gender":"m","followers_count":2938204,"friends_count":517,"pagefriends_count":1,"statuses_count":8408,"favourites_count":3,"created_at":"Mon May 07 23:26:59 +0800 2012","following":false,"allow_all_act_msg":false,"geo_enabled":false,"verified":true,"verified_type":0,"remark":"","ptype":8,"allow_all_comment":true,"avatar_large":"http://tp2.sinaimg.cn/2726601057/180/40079628039/1","avatar_hd":"http://ww4.sinaimg.cn/crop.0.9.645.645.1024/a284a161gw1epmm5zttphj20k00k0n36.jpg","verified_reason":"微博知名动漫博主 微博签约自媒体","verified_trade":"3370","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":507,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":5,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"cardid":"star_005","urank":28},"reposts_count":2069,"comments_count":434,"attitudes_count":206,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_ids":[230442],"biz_feature":0,"darwin_tags":[],"userType":0,"cardid":"star_005"},"annotations":[{"mapi_request":true}],"reposts_count":0,"comments_count":0,"attitudes_count":1,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"rid":"2_0_1_2666885038303173078","userType":0},{"created_at":"Mon Nov 02 22:21:28 +0800 2015","id":3904857704142393,"mid":"3904857704142393","idstr":"3904857704142393","text":"如果可以每条做到，就可以出家了","source_allowclick":0,"source_type":2,"source":"","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":3099016097,"idstr":"3099016097","class":1,"screen_name":"英国报姐","name":"英国报姐","province":"81","city":"2","location":"香港 中西区","description":"订阅号：ukuk520,国外各种好玩哒，我啥都发~【注：玻璃心，喜释梗，广告狗","url":"","profile_image_url":"http://tp2.sinaimg.cn/3099016097/50/5727275657/0","cover_image":"http://ww4.sinaimg.cn/crop.0.0.920.300/b8b73ba1gw1esdg2pmvabj20pk08cdlp.jpg","cover_image_phone":"http://ww3.sinaimg.cn/crop.0.0.0.0/b8b73ba1jw1emojcngmjlj20e80e8q49.jpg","profile_url":"uktimes","domain":"uktimes","weihao":"","gender":"f","followers_count":8062644,"friends_count":457,"pagefriends_count":2,"statuses_count":7455,"favourites_count":128,"created_at":"Sat Nov 10 14:28:36 +0800 2012","following":true,"allow_all_act_msg":false,"geo_enabled":true,"verified":true,"verified_type":0,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp2.sinaimg.cn/3099016097/180/5727275657/0","avatar_hd":"http://ww2.sinaimg.cn/crop.0.0.300.300.1024/b8b73ba1jw8esdg2b03k5j208c08d400.jpg","verified_reason":"微博知名中英文化自由撰稿人","verified_trade":"3370","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":446,"lang":"zh-tw","star":0,"mbtype":11,"mbrank":6,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":31},"pid":3904855334323075,"retweeted_status":{"created_at":"Mon Nov 02 22:08:08 +0800 2015","id":3904854348868625,"mid":"3904854348868625","idstr":"3904854348868625","text":"如果可以接受自己也不那么完美，就不用忙着去粉饰了；如果可以承认自己并不那么伟大，就不用急着去证明；如果可以去放弃自己的种种成见，就不用吵着去反驳了；如果可以不在乎别人怎么看自己，就不用哭着去申诉了；如果可以慢半拍，静半刻，低半头，就可以一直微笑了。\u2014\u2014扎西拉姆·多多 《喃喃》","source_allowclick":0,"source_type":2,"source":"","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":3517499214,"idstr":"3517499214","class":1,"screen_name":"别人家的那些事","name":"别人家的那些事","province":"11","city":"1","location":"北京 东城区","description":"除你以外，皆是他人。别人的故事，你的参照物。","url":"","profile_image_url":"http://tp3.sinaimg.cn/3517499214/50/22865556289/1","cover_image_phone":"http://ww3.sinaimg.cn/crop.0.0.640.640.640/6ce2240djw1e8ysl7x6ikj20hs0hswgo.jpg","profile_url":"u/3517499214","domain":"","weihao":"","gender":"m","followers_count":1350843,"friends_count":39,"pagefriends_count":1,"statuses_count":2822,"favourites_count":3,"created_at":"Sat Jun 08 14:37:50 +0800 2013","following":false,"allow_all_act_msg":false,"geo_enabled":false,"verified":true,"verified_type":0,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp3.sinaimg.cn/3517499214/180/22865556289/1","avatar_hd":"http://ww1.sinaimg.cn/crop.7.4.171.171.1024/d1a8c74etw1ecwyh6m5snj205k05kaa5.jpg","verified_reason":"微博知名读物博主","verified_trade":"3370","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":25,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":6,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":27},"annotations":[{"shooting":1,"client_mblogid":"iPhone-8CD00CF0-4B2C-4D96-9005-08F5D24D5214"},{"mapi_request":true}],"reposts_count":317,"comments_count":14,"attitudes_count":143,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"userType":0},"annotations":[{"mapi_request":true}],"reposts_count":86,"comments_count":22,"attitudes_count":177,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"rid":"3_0_1_2666885038303173078","userType":0},{"created_at":"Mon Nov 02 22:21:05 +0800 2015","id":3904857608108560,"mid":"3904857608108560","idstr":"3904857608108560","text":"[心]//@月光下飞跃过的银色:真的好美～很想在一个这个样子的银河下等待日出，一定也很美[心]//@颜文字君:(´ ㅂ `;)//@S-Deng: 您的航班因流量控制，目前无法起飞，起飞时间待定。。","source_allowclick":0,"source_type":2,"source":"","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":3217179555,"idstr":"3217179555","class":1,"screen_name":"回忆专用小马甲","name":"回忆专用小马甲","province":"100","city":"1000","location":"其他","description":"愿无岁月可回头","url":"","profile_image_url":"http://tp4.sinaimg.cn/3217179555/50/5693967295/1","cover_image":"http://ww3.sinaimg.cn/crop.0.135.980.300/bfc243a3gw1edm6ymenftj20r80c376d.jpg","cover_image_phone":"http://ww4.sinaimg.cn/crop.0.0.0.0/bfc243a3jw1edcdj6uj59j20hs0hsdgq.jpg","profile_url":"u/3217179555","domain":"","weihao":"","gender":"m","followers_count":18834510,"friends_count":580,"pagefriends_count":1,"statuses_count":8167,"favourites_count":3609,"created_at":"Thu Jan 10 13:37:59 +0800 2013","following":true,"allow_all_act_msg":true,"geo_enabled":false,"verified":true,"verified_type":0,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/3217179555/180/5693967295/1","avatar_hd":"http://ww3.sinaimg.cn/crop.0.0.640.640.1024/bfc243a3jw8efzr4c9ajij20hs0hsaav.jpg","verified_reason":"微博人气博主","verified_trade":"3370","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":542,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":6,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":30},"pid":3904857473723245,"retweeted_status":{"created_at":"Mon Nov 02 22:01:03 +0800 2015","id":3904852566247397,"mid":"3904852566247397","idstr":"3904852566247397","text":"(✧◡✧)这些照片是岛国网友tttks在伊丹空港用长时间曝光摄影所拍，简直美哭，像通往银河的路......[心]","source_allowclick":0,"source_type":1,"source":"<a href=\"http://weibo.com/\" rel=\"nofollow\">微博 weibo.com<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/bfc243a3gw1exmz5f1r73j20sg0jhjta.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/bfc243a3gw1exmz5fsc4ej20sg0izjt6.jpg"},{"thumbnail_pic":"http://ww3.sinaimg.cn/thumbnail/bfc243a3gw1exmz5gequyj20sg0izab2.jpg"},{"thumbnail_pic":"http://ww3.sinaimg.cn/thumbnail/bfc243a3gw1exmz5h2dtgj20sg0iyq61.jpg"},{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/bfc243a3gw1exmz5hoqc1j20sg0hz41n.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/bfc243a3gw1exmz5i8ahjj20sg0jtwg9.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/bfc243a3gw1exmz5j3xzjj20rs0il0vu.jpg"},{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/bfc243a3gw1exmz5jo60cj20sg0iy0v0.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/bfc243a3gw1exmz5k7k5wj20rs0iltbg.jpg"}],"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/bfc243a3gw1exmz5f1r73j20sg0jhjta.jpg","bmiddle_pic":"http://ww1.sinaimg.cn/bmiddle/bfc243a3gw1exmz5f1r73j20sg0jhjta.jpg","original_pic":"http://ww1.sinaimg.cn/large/bfc243a3gw1exmz5f1r73j20sg0jhjta.jpg","geo":null,"user":{"id":3217179555,"idstr":"3217179555","class":1,"screen_name":"回忆专用小马甲","name":"回忆专用小马甲","province":"100","city":"1000","location":"其他","description":"愿无岁月可回头","url":"","profile_image_url":"http://tp4.sinaimg.cn/3217179555/50/5693967295/1","cover_image":"http://ww3.sinaimg.cn/crop.0.135.980.300/bfc243a3gw1edm6ymenftj20r80c376d.jpg","cover_image_phone":"http://ww4.sinaimg.cn/crop.0.0.0.0/bfc243a3jw1edcdj6uj59j20hs0hsdgq.jpg","profile_url":"u/3217179555","domain":"","weihao":"","gender":"m","followers_count":18834510,"friends_count":580,"pagefriends_count":1,"statuses_count":8167,"favourites_count":3609,"created_at":"Thu Jan 10 13:37:59 +0800 2013","following":true,"allow_all_act_msg":true,"geo_enabled":false,"verified":true,"verified_type":0,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/3217179555/180/5693967295/1","avatar_hd":"http://ww3.sinaimg.cn/crop.0.0.640.640.1024/bfc243a3jw8efzr4c9ajij20hs0hsaav.jpg","verified_reason":"微博人气博主","verified_trade":"3370","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":542,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":6,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":30},"reposts_count":1219,"comments_count":362,"attitudes_count":1945,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"userType":0},"annotations":[{"mapi_request":true}],"reposts_count":191,"comments_count":92,"attitudes_count":386,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"rid":"4_0_1_2666885038303173078","userType":0},{"created_at":"Mon Nov 02 22:17:54 +0800 2015","id":3904856810858734,"mid":"3904856810858734","idstr":"3904856810858734","text":"小公举[爱你]","source_allowclick":0,"source_type":2,"source":"","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":3069348215,"idstr":"3069348215","class":1,"screen_name":"同道大叔","name":"同道大叔","province":"11","city":"1000","location":"北京","description":"星座只是娱乐，理想是带着粉丝发财。工作联系 邮箱：tongdaodashu@peiwo.me","url":"","profile_image_url":"http://tp4.sinaimg.cn/3069348215/50/5721712236/1","cover_image":"http://ww1.sinaimg.cn/crop.0.0.920.300/b6f28977gw1equ63hmar1j20pk08c783.jpg","cover_image_phone":"http://ww1.sinaimg.cn/crop.0.0.0.0/b6f28977jw1ev4f724ypqj20e80e8wh0.jpg","profile_url":"tongdaodashu","domain":"tongdaodashu","weihao":"","gender":"m","followers_count":6741623,"friends_count":884,"pagefriends_count":0,"statuses_count":6419,"favourites_count":64,"created_at":"Thu Oct 25 13:47:18 +0800 2012","following":true,"allow_all_act_msg":false,"geo_enabled":true,"verified":true,"verified_type":0,"remark":"","ptype":10,"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/3069348215/180/5721712236/1","avatar_hd":"http://ww4.sinaimg.cn/crop.0.0.512.512.1024/b6f28977jw8eqb042gir6j20e80e8aa9.jpg","verified_reason":"微博知名星座博主","verified_trade":"3370","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":805,"lang":"zh-cn","star":0,"mbtype":13,"mbrank":5,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":29},"retweeted_status":{"created_at":"Mon Nov 02 22:00:09 +0800 2015","id":3904852339626914,"mid":"3904852339626914","idstr":"3904852339626914","text":"周杰伦演唱会最美版《手语》，全息投影配合帅气的舞步真是完美，简直是一场视听双重盛宴！[爱你]http://t.cn/RUxXqKj","source_allowclick":0,"source_type":1,"source":"<a href=\"http://weibo.com/\" rel=\"nofollow\">微博 weibo.com<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":1764222885,"idstr":"1764222885","class":1,"screen_name":"当时我就震惊了","name":"当时我就震惊了","province":"400","city":"1000","location":"海外","description":"深扒娱乐热点，每天给你惊喜。","url":"http://t.qq.com/zhenjing55555","profile_image_url":"http://tp2.sinaimg.cn/1764222885/50/5714301852/1","cover_image":"http://ww2.sinaimg.cn/crop.0.0.980.300/6927e7a5jw1e17afsv2ivj.jpg","cover_image_phone":"http://ww1.sinaimg.cn/crop.0.0.640.640/549d0121tw1egm1kjly3jj20hs0hsq4f.jpg","profile_url":"gaoxiaotop","domain":"gaoxiaotop","weihao":"","gender":"m","followers_count":21169428,"friends_count":1158,"pagefriends_count":0,"statuses_count":68648,"favourites_count":17471,"created_at":"Mon Jul 12 17:38:17 +0800 2010","following":false,"allow_all_act_msg":true,"geo_enabled":false,"verified":true,"verified_type":0,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp2.sinaimg.cn/1764222885/180/5714301852/1","avatar_hd":"http://ww2.sinaimg.cn/crop.0.0.638.638.1024/6927e7a5jw1enjugo11vwj20hs0hsq4t.jpg","verified_reason":"微博知名创意人","verified_trade":"3370","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":1031,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":6,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":36},"reposts_count":1596,"comments_count":275,"attitudes_count":709,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_ids":[230442],"biz_feature":0,"darwin_tags":[],"userType":0},"annotations":[{"mapi_request":true}],"reposts_count":307,"comments_count":63,"attitudes_count":354,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"rid":"5_0_1_2666885038303173078","userType":0},{"created_at":"Mon Nov 02 22:15:29 +0800 2015","id":3904856198639493,"mid":"3904856198639493","idstr":"3904856198639493","text":"我们公司前台[doge]想来坐坐吗[doge]","source_allowclick":0,"source_type":1,"source":"<a href=\"http://app.weibo.com/t/feed/3o33sO\" rel=\"nofollow\">iPhone 6<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/9ece0757gw1exmzlhkkbzj20qo0zkdhd.jpg"},{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/9ece0757gw1exmzlgm8pnj20qo0zk4bi.jpg"},{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/9ece0757gw1exmzlihcdkj20qo0zkmyi.jpg"}],"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/9ece0757gw1exmzlhkkbzj20qo0zkdhd.jpg","bmiddle_pic":"http://ww4.sinaimg.cn/bmiddle/9ece0757gw1exmzlhkkbzj20qo0zkdhd.jpg","original_pic":"http://ww4.sinaimg.cn/large/9ece0757gw1exmzlhkkbzj20qo0zkdhd.jpg","geo":null,"user":{"id":2664302423,"idstr":"2664302423","class":1,"screen_name":"超级课程表","name":"超级课程表","province":"44","city":"1","location":"广东 广州","description":"超级课程表-表表，上大学，一个app就够了！不仅仅是课程表，还有社团活动、校园新鲜事、教务通知、二手市场、失物招领、成绩查询、空教室查询、校内树洞等功能！","url":"http://www.super.cn","profile_image_url":"http://tp4.sinaimg.cn/2664302423/50/5740969060/1","cover_image":"http://ww2.sinaimg.cn/crop.0.0.920.300/9ece0757gw1evmxr2ja0jj20pk08c0u8.jpg","profile_url":"tofriday","domain":"tofriday","weihao":"","gender":"m","followers_count":1066293,"friends_count":951,"pagefriends_count":22,"statuses_count":6690,"favourites_count":208,"created_at":"Thu Mar 15 16:42:46 +0800 2012","following":true,"allow_all_act_msg":true,"geo_enabled":true,"verified":true,"verified_type":2,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/2664302423/180/5740969060/1","avatar_hd":"http://ww2.sinaimg.cn/crop.0.0.512.512.1024/9ece0757jw8exgoaunl9bj20e80e8mz7.jpg","verified_reason":"\u201c超级课程表\u201d官方微博","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"表表","verified_contact_email":"biaobiao@myfriday.cn","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":687,"lang":"zh-cn","star":0,"mbtype":0,"mbrank":0,"block_word":0,"block_app":0,"credit_score":80,"user_ability":0,"urank":28},"annotations":[{"client_mblogid":"iPhone-BDC0D878-0F2B-4DDA-A403-E01EF17EC91D"},{"mapi_request":true}],"reposts_count":0,"comments_count":26,"attitudes_count":76,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":4294967300,"darwin_tags":[],"rid":"6_0_1_2666885038303173078","userType":0},{"created_at":"Mon Nov 02 22:15:03 +0800 2015","id":3904856089473006,"mid":"3904856089473006","idstr":"3904856089473006","text":"胖死了[doge]","source_allowclick":0,"source_type":1,"source":"<a href=\"http://app.weibo.com/t/feed/3o33sO\" rel=\"nofollow\">iPhone 6<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":2993873502,"idstr":"2993873502","class":1,"screen_name":"我整个人都感到不太好了","name":"我整个人都感到不太好了","province":"100","city":"1000","location":"其他","description":"站住！别跑！","url":"","profile_image_url":"http://tp3.sinaimg.cn/2993873502/50/5735292796/1","cover_image":"http://ww4.sinaimg.cn/crop.0.0.920.300/b272e25egw1ep6qncusltj20pk08ct96.jpg","cover_image_phone":"http://ww4.sinaimg.cn/crop.0.0.0.0/0064e8mdjw1ewx3j7ha0fj30yi0yit94.jpg","profile_url":"u/2993873502","domain":"","weihao":"","gender":"m","followers_count":603074,"friends_count":689,"pagefriends_count":0,"statuses_count":21707,"favourites_count":156,"created_at":"Sat Sep 08 11:57:26 +0800 2012","following":true,"allow_all_act_msg":true,"geo_enabled":true,"verified":true,"verified_type":0,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp3.sinaimg.cn/2993873502/180/5735292796/1","avatar_hd":"http://ww2.sinaimg.cn/crop.0.0.1242.1242.1024/b272e25ejw8evcpzr1bz9j20yi0yiwfp.jpg","verified_reason":"知名搞笑幽默博主","verified_trade":"484","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":558,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":5,"block_word":0,"block_app":1,"credit_score":76,"user_ability":0,"cardid":"star_001","urank":29},"pid":3904855963576322,"retweeted_status":{"created_at":"Mon Nov 02 22:00:03 +0800 2015","id":3904852315109982,"mid":"3904852315109982","idstr":"3904852315109982","text":"老鼠的孩子打洞特别6！至于仓鼠的孩子\u2026\u2026对不起，有点太胖了[doge]","source_allowclick":0,"source_type":1,"source":"<a href=\"http://weibo.com/\" rel=\"nofollow\">微博 weibo.com<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[{"thumbnail_pic":"http://ww3.sinaimg.cn/thumbnail/4b807446gw1exmyl0zxbkg208904n7wi.gif"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/4b807446gw1exmyl45419g208904nu0x.gif"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/4b807446gw1exmyhroc3dg208904nkjl.gif"},{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/4b807446gw1exmyhsx5srg208904nqv5.gif"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/4b807446gw1exmyhumpwag208904nkjm.gif"},{"thumbnail_pic":"http://ww3.sinaimg.cn/thumbnail/4b807446gw1exmyhw1jrig208904nb29.gif"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/4b807446gw1exmyhx9k5pg208904n4qp.gif"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/4b807446gw1exmyhxytmig208904n1kx.gif"},{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/4b807446gw1exmyhz40qmg208904n1kx.gif"}],"thumbnail_pic":"http://ww3.sinaimg.cn/thumbnail/4b807446gw1exmyl0zxbkg208904n7wi.gif","bmiddle_pic":"http://ww3.sinaimg.cn/bmiddle/4b807446gw1exmyl0zxbkg208904n7wi.gif","original_pic":"http://ww3.sinaimg.cn/large/4b807446gw1exmyl0zxbkg208904n7wi.gif","geo":null,"user":{"id":1266709574,"idstr":"1266709574","class":1,"screen_name":"夏影","name":"夏影","province":"400","city":"1","location":"海外 美国","description":"精分段子手，推特搬运工，偶尔画点小东西，谢谢你的喜欢。工作请联系QQ2086392940","url":"","profile_image_url":"http://tp3.sinaimg.cn/1266709574/50/40092979880/1","cover_image":"http://ww2.sinaimg.cn/crop.0.0.920.300/4b807446gw1es9wsbfcxwj20pk08cq51.jpg","cover_image_phone":"http://ww4.sinaimg.cn/crop.0.0.0.0/4b807446jw1es9tkyf7anj20w00w0the.jpg","profile_url":"torinouta","domain":"torinouta","weihao":"234524315","gender":"m","followers_count":1675252,"friends_count":1468,"pagefriends_count":0,"statuses_count":16060,"favourites_count":1458,"created_at":"Fri Feb 18 11:57:46 +0800 2011","following":false,"allow_all_act_msg":false,"geo_enabled":false,"verified":true,"verified_type":0,"remark":"","ptype":8,"allow_all_comment":true,"avatar_large":"http://tp3.sinaimg.cn/1266709574/180/40092979880/1","avatar_hd":"http://ww3.sinaimg.cn/crop.0.0.179.179.1024/4b807446gw1eul9vcs6gaj20500500sx.jpg","verified_reason":"新浪微漫画漫画作者  微博签约自媒体","verified_trade":"922","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":1471,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":6,"block_word":0,"block_app":1,"ability_tags":"Coser,段子手,重口味,三俗,幽默艺术,搞笑,日本动漫,动漫摄影师,画师,新闻趣事","credit_score":80,"user_ability":2,"cardid":"vip_003","urank":33},"reposts_count":712,"comments_count":83,"attitudes_count":359,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"userType":0,"cardid":"vip_003"},"annotations":[{"mapi_request":true}],"reposts_count":69,"comments_count":2,"attitudes_count":13,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"rid":"7_0_1_2666885038303173078","userType":0,"cardid":"star_001"},{"created_at":"Mon Nov 02 22:13:04 +0800 2015","id":3904855598765260,"mid":"3904855598765260","idstr":"3904855598765260","text":"【投稿：@_橙子喵_ 】#黄子韬表情包##法法表情包# 晚上好[doge]我把这个系列放一起了[doge]","source_allowclick":0,"source_type":1,"source":"<a href=\"http://app.weibo.com/t/feed/3o33sO\" rel=\"nofollow\">iPhone 6<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/bbf11c6bjw1exmzjiqgb1j20k00k03zv.jpg"},{"thumbnail_pic":"http://ww3.sinaimg.cn/thumbnail/bbf11c6bjw1exmzjjarakj20k00k0758.jpg"},{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/bbf11c6bjw1exmzjiafdoj20k00k0wet.jpg"},{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/bbf11c6bjw1exmzjjqbzoj20k00k0gly.jpg"},{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/bbf11c6bjw1exmzjjz7cgj20k00k0dg6.jpg"},{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/bbf11c6bjw1exmzk73dxlj20k00k074n.jpg"},{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/bbf11c6bjw1exmzk775vcj20k00k00t5.jpg"},{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/bbf11c6bjw1exmzk7jxlhj20k00k0t9m.jpg"},{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/bbf11c6bjw1exmzk7ziipj20k00k0t9p.jpg"}],"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/bbf11c6bjw1exmzjiqgb1j20k00k03zv.jpg","bmiddle_pic":"http://ww1.sinaimg.cn/bmiddle/bbf11c6bjw1exmzjiqgb1j20k00k03zv.jpg","original_pic":"http://ww1.sinaimg.cn/large/bbf11c6bjw1exmzjiqgb1j20k00k03zv.jpg","geo":null,"user":{"id":3153140843,"idstr":"3153140843","class":1,"screen_name":"黄子韬表情包","name":"黄子韬表情包","province":"100","city":"1000","location":"其他","description":"欢迎投稿，爱本骑你怕了吗？","url":"","profile_image_url":"http://tp4.sinaimg.cn/3153140843/50/5734669761/0","profile_url":"u/3153140843","domain":"","weihao":"","gender":"f","followers_count":137226,"friends_count":34,"pagefriends_count":0,"statuses_count":40,"favourites_count":9,"created_at":"Fri Nov 30 19:20:18 +0800 2012","following":true,"allow_all_act_msg":false,"geo_enabled":false,"verified":false,"verified_type":-1,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/3153140843/180/5734669761/0","avatar_hd":"http://ww1.sinaimg.cn/crop.0.3.750.750.1024/bbf11c6bjw8ev4dvdkeuxj20ku0l0dgm.jpg","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"online_status":0,"bi_followers_count":32,"lang":"zh-cn","star":0,"mbtype":0,"mbrank":0,"block_word":0,"block_app":0,"credit_score":80,"user_ability":0,"urank":11},"annotations":[{"client_mblogid":"iPhone-B53A74A4-B8AA-4A28-8F7C-C5324C7334DA"},{"mapi_request":true}],"reposts_count":24,"comments_count":36,"attitudes_count":125,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":4294967300,"darwin_tags":[],"rid":"8_0_1_2666885038303173078","userType":0},{"created_at":"Mon Nov 02 22:07:20 +0800 2015","id":3904854148049759,"mid":"3904854148049759","idstr":"3904854148049759","text":"哈哈哈哈哈哈哈哈哈哈","source_allowclick":0,"source_type":1,"source":"<a href=\"http://app.weibo.com/t/feed/3o33sO\" rel=\"nofollow\">iPhone 6<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":2993873502,"idstr":"2993873502","class":1,"screen_name":"我整个人都感到不太好了","name":"我整个人都感到不太好了","province":"100","city":"1000","location":"其他","description":"站住！别跑！","url":"","profile_image_url":"http://tp3.sinaimg.cn/2993873502/50/5735292796/1","cover_image":"http://ww4.sinaimg.cn/crop.0.0.920.300/b272e25egw1ep6qncusltj20pk08ct96.jpg","cover_image_phone":"http://ww4.sinaimg.cn/crop.0.0.0.0/0064e8mdjw1ewx3j7ha0fj30yi0yit94.jpg","profile_url":"u/2993873502","domain":"","weihao":"","gender":"m","followers_count":603074,"friends_count":689,"pagefriends_count":0,"statuses_count":21707,"favourites_count":156,"created_at":"Sat Sep 08 11:57:26 +0800 2012","following":true,"allow_all_act_msg":true,"geo_enabled":true,"verified":true,"verified_type":0,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp3.sinaimg.cn/2993873502/180/5735292796/1","avatar_hd":"http://ww2.sinaimg.cn/crop.0.0.1242.1242.1024/b272e25ejw8evcpzr1bz9j20yi0yiwfp.jpg","verified_reason":"知名搞笑幽默博主","verified_trade":"484","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":558,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":5,"block_word":0,"block_app":1,"credit_score":76,"user_ability":0,"cardid":"star_001","urank":29},"retweeted_status":{"created_at":"Mon Nov 02 22:02:15 +0800 2015","id":3904852868867960,"mid":"3904852868867960","idstr":"3904852868867960","text":"#阿木资源社# 一位推主说家里的猫头鹰笑声特别魔性，听完不自觉的跟着哈哈哈哈起来[笑cry][笑cry][笑cry][笑cry]http://t.cn/RUxJOOu","source_allowclick":0,"source_type":1,"source":"<a href=\"http://weibo.com/\" rel=\"nofollow\">微博 weibo.com<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":2726601057,"idstr":"2726601057","class":1,"screen_name":"进击的阿木君","name":"进击的阿木君","province":"44","city":"1000","location":"广东","description":"发布动漫一手信息，P图小能手，酷爱搬运日推汤不热，偶尔制作点动图。","url":"","profile_image_url":"http://tp2.sinaimg.cn/2726601057/50/40079628039/1","cover_image":"http://ww4.sinaimg.cn/crop.0.0.920.300/a284a161gw1eoil3tep9rj20pk08cgon.jpg","cover_image_phone":"http://ww2.sinaimg.cn/crop.0.0.0.0/a284a161jw1esiuc03tsaj20ku0kut9u.jpg","profile_url":"u/2726601057","domain":"","weihao":"","gender":"m","followers_count":2938204,"friends_count":517,"pagefriends_count":1,"statuses_count":8408,"favourites_count":3,"created_at":"Mon May 07 23:26:59 +0800 2012","following":false,"allow_all_act_msg":false,"geo_enabled":false,"verified":true,"verified_type":0,"remark":"","ptype":8,"allow_all_comment":true,"avatar_large":"http://tp2.sinaimg.cn/2726601057/180/40079628039/1","avatar_hd":"http://ww4.sinaimg.cn/crop.0.9.645.645.1024/a284a161gw1epmm5zttphj20k00k0n36.jpg","verified_reason":"微博知名动漫博主 微博签约自媒体","verified_trade":"3370","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":507,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":5,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"cardid":"star_005","urank":28},"reposts_count":2069,"comments_count":434,"attitudes_count":206,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_ids":[230442],"biz_feature":0,"darwin_tags":[],"userType":0,"cardid":"star_005"},"annotations":[{"mapi_request":true}],"reposts_count":289,"comments_count":7,"attitudes_count":28,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"rid":"9_0_1_2666885038303173078","userType":0,"cardid":"star_001"},{"created_at":"Mon Nov 02 22:05:04 +0800 2015","id":3904853585495068,"mid":"3904853585495068","idstr":"3904853585495068","text":"之前发布的话题是\u201c你买过哪些坑爹货_______\u201d想问大家是被坑的多惨,刻骨铭心,乌云夹着笑点duang一下过来，看的我哈哈哈哈大笑停不下！#11月只有11天#要想不被坑享品质网购,还得看这真快好赞,买！ http://t.cn/RUcOe4V","source_allowclick":0,"source_type":1,"source":"<a href=\"http://app.weibo.com/t/feed/5g0B8s\" rel=\"nofollow\">微博 weibo.com<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/8d4f74e4jw1exmxkh1bq3j20v91uaqbt.jpg"},{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/8d4f74e4jw1exmxkvn3ddj20vt1wogu6.jpg"},{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/8d4f74e4jw1exmxl40mr5j20v91tv7bz.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/8d4f74e4jw1exmxlg61yjj20v91i112n.jpg"},{"thumbnail_pic":"http://ww3.sinaimg.cn/thumbnail/8d4f74e4jw1exmxlsngx3j20v91hztit.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/8d4f74e4jw1exmxm4gu27j20v91hm7dh.jpg"},{"thumbnail_pic":"http://ww3.sinaimg.cn/thumbnail/8d4f74e4jw1exmxmlbnhrj20v91ghqc5.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/8d4f74e4jw1exmxmwx5azj20v91g5tgc.jpg"},{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/8d4f74e4jw1exmxna8o0cj20v91iawmh.jpg"}],"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/8d4f74e4jw1exmxkh1bq3j20v91uaqbt.jpg","bmiddle_pic":"http://ww2.sinaimg.cn/bmiddle/8d4f74e4jw1exmxkh1bq3j20v91uaqbt.jpg","original_pic":"http://ww2.sinaimg.cn/large/8d4f74e4jw1exmxkh1bq3j20v91uaqbt.jpg","geo":null,"user":{"id":1713926427,"idstr":"1713926427","class":1,"screen_name":"微博搞笑排行榜","name":"微博搞笑排行榜","province":"44","city":"1000","location":"广东","description":"微博搞笑中心！每天搜罗最搞笑最好玩的微博。关注我，获得每日新鲜笑料！（欢迎合作，投稿）↖(^ω^)↗","url":"http://weibo.com/mkdqs","profile_image_url":"http://tp4.sinaimg.cn/1713926427/50/40000875722/0","cover_image_phone":"http://ww3.sinaimg.cn/crop.0.0.640.640.640/6ce2240djw1e9oaaziu7sj20hs0hs0ui.jpg","profile_url":"topgirls8","domain":"topgirls8","weihao":"","gender":"f","followers_count":27158185,"friends_count":837,"pagefriends_count":0,"statuses_count":88408,"favourites_count":7056,"created_at":"Fri Mar 19 00:42:49 +0800 2010","following":true,"allow_all_act_msg":true,"geo_enabled":false,"verified":false,"verified_type":-1,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/1713926427/180/40000875722/0","avatar_hd":"http://tp4.sinaimg.cn/1713926427/180/40000875722/0","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"online_status":0,"bi_followers_count":297,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":6,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":37},"annotations":[{},{}],"reposts_count":1425,"comments_count":1295,"attitudes_count":1601,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_ids":[0],"biz_feature":0,"darwin_tags":[],"rid":"10_0_1_2666885038303173078","userType":0},{"created_at":"Mon Nov 02 22:04:28 +0800 2015","id":3904853426211167,"mid":"3904853426211167","idstr":"3904853426211167","text":"一哥们分享的自己亲身经历，多亏了老婆临场反应镇定，救了他一命，想说急救常识和心理素质真的挺重要的","source_allowclick":0,"source_type":1,"source":"<a href=\"http://weibo.com/\" rel=\"nofollow\">微博 weibo.com<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/b8b73ba1gw1exmzbqgqdgj20cs1s9qbh.jpg"}],"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/b8b73ba1gw1exmzbqgqdgj20cs1s9qbh.jpg","bmiddle_pic":"http://ww2.sinaimg.cn/bmiddle/b8b73ba1gw1exmzbqgqdgj20cs1s9qbh.jpg","original_pic":"http://ww2.sinaimg.cn/large/b8b73ba1gw1exmzbqgqdgj20cs1s9qbh.jpg","geo":null,"user":{"id":3099016097,"idstr":"3099016097","class":1,"screen_name":"英国报姐","name":"英国报姐","province":"81","city":"2","location":"香港 中西区","description":"订阅号：ukuk520,国外各种好玩哒，我啥都发~【注：玻璃心，喜释梗，广告狗","url":"","profile_image_url":"http://tp2.sinaimg.cn/3099016097/50/5727275657/0","cover_image":"http://ww4.sinaimg.cn/crop.0.0.920.300/b8b73ba1gw1esdg2pmvabj20pk08cdlp.jpg","cover_image_phone":"http://ww3.sinaimg.cn/crop.0.0.0.0/b8b73ba1jw1emojcngmjlj20e80e8q49.jpg","profile_url":"uktimes","domain":"uktimes","weihao":"","gender":"f","followers_count":8062644,"friends_count":457,"pagefriends_count":2,"statuses_count":7455,"favourites_count":128,"created_at":"Sat Nov 10 14:28:36 +0800 2012","following":true,"allow_all_act_msg":false,"geo_enabled":true,"verified":true,"verified_type":0,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp2.sinaimg.cn/3099016097/180/5727275657/0","avatar_hd":"http://ww2.sinaimg.cn/crop.0.0.300.300.1024/b8b73ba1jw8esdg2b03k5j208c08d400.jpg","verified_reason":"微博知名中英文化自由撰稿人","verified_trade":"3370","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":446,"lang":"zh-tw","star":0,"mbtype":11,"mbrank":6,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":31},"reposts_count":214,"comments_count":206,"attitudes_count":773,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"rid":"11_0_1_2666885038303173078","userType":0},{"created_at":"Mon Nov 02 22:01:03 +0800 2015","id":3904852566247397,"mid":"3904852566247397","idstr":"3904852566247397","text":"(✧◡✧)这些照片是岛国网友tttks在伊丹空港用长时间曝光摄影所拍，简直美哭，像通往银河的路......[心]","source_allowclick":0,"source_type":1,"source":"<a href=\"http://weibo.com/\" rel=\"nofollow\">微博 weibo.com<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/bfc243a3gw1exmz5f1r73j20sg0jhjta.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/bfc243a3gw1exmz5fsc4ej20sg0izjt6.jpg"},{"thumbnail_pic":"http://ww3.sinaimg.cn/thumbnail/bfc243a3gw1exmz5gequyj20sg0izab2.jpg"},{"thumbnail_pic":"http://ww3.sinaimg.cn/thumbnail/bfc243a3gw1exmz5h2dtgj20sg0iyq61.jpg"},{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/bfc243a3gw1exmz5hoqc1j20sg0hz41n.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/bfc243a3gw1exmz5i8ahjj20sg0jtwg9.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/bfc243a3gw1exmz5j3xzjj20rs0il0vu.jpg"},{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/bfc243a3gw1exmz5jo60cj20sg0iy0v0.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/bfc243a3gw1exmz5k7k5wj20rs0iltbg.jpg"}],"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/bfc243a3gw1exmz5f1r73j20sg0jhjta.jpg","bmiddle_pic":"http://ww1.sinaimg.cn/bmiddle/bfc243a3gw1exmz5f1r73j20sg0jhjta.jpg","original_pic":"http://ww1.sinaimg.cn/large/bfc243a3gw1exmz5f1r73j20sg0jhjta.jpg","geo":null,"user":{"id":3217179555,"idstr":"3217179555","class":1,"screen_name":"回忆专用小马甲","name":"回忆专用小马甲","province":"100","city":"1000","location":"其他","description":"愿无岁月可回头","url":"","profile_image_url":"http://tp4.sinaimg.cn/3217179555/50/5693967295/1","cover_image":"http://ww3.sinaimg.cn/crop.0.135.980.300/bfc243a3gw1edm6ymenftj20r80c376d.jpg","cover_image_phone":"http://ww4.sinaimg.cn/crop.0.0.0.0/bfc243a3jw1edcdj6uj59j20hs0hsdgq.jpg","profile_url":"u/3217179555","domain":"","weihao":"","gender":"m","followers_count":18834510,"friends_count":580,"pagefriends_count":1,"statuses_count":8167,"favourites_count":3609,"created_at":"Thu Jan 10 13:37:59 +0800 2013","following":true,"allow_all_act_msg":true,"geo_enabled":false,"verified":true,"verified_type":0,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/3217179555/180/5693967295/1","avatar_hd":"http://ww3.sinaimg.cn/crop.0.0.640.640.1024/bfc243a3jw8efzr4c9ajij20hs0hsaav.jpg","verified_reason":"微博人气博主","verified_trade":"3370","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":542,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":6,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":30},"reposts_count":1219,"comments_count":362,"attitudes_count":1945,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"rid":"12_0_1_2666885038303173078","userType":0},{"created_at":"Mon Nov 02 22:00:03 +0800 2015","id":3904852319305645,"mid":"3904852319305645","idstr":"3904852319305645","text":"#深夜猫吧#怎么可以在没有睡意情况下，十分钟内睡着。。。（举起手，打晕自己咯）","source_allowclick":0,"source_type":1,"source":"<a href=\"http://weibo.com/\" rel=\"nofollow\">微博 weibo.com<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/62f87eb4gw1exmp185902g209l05u49v.gif"}],"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/62f87eb4gw1exmp185902g209l05u49v.gif","bmiddle_pic":"http://ww2.sinaimg.cn/bmiddle/62f87eb4gw1exmp185902g209l05u49v.gif","original_pic":"http://ww2.sinaimg.cn/large/62f87eb4gw1exmp185902g209l05u49v.gif","geo":null,"user":{"id":1660452532,"idstr":"1660452532","class":1,"screen_name":"猫扑","name":"猫扑","province":"11","city":"1","location":"北京 东城区","description":"猫扑网是中国领先的网络社区，目前已成为集大杂烩、贴贴、乐加和猫扑地方站等产品为一体的综合性富媒体娱乐互动平台。","url":"http://www.mop.com","profile_image_url":"http://tp1.sinaimg.cn/1660452532/50/40060008980/0","cover_image":"http://ww1.sinaimg.cn/crop.0.0.920.300/62f87eb4gw1eqs74eltr1j20pk08caak.jpg","cover_image_phone":"http://ww4.sinaimg.cn/crop.0.0.640.640.640/6ce2240djw1e9oagec35lj20hs0hsgmj.jpg","profile_url":"moptt","domain":"moptt","weihao":"","gender":"f","followers_count":4232296,"friends_count":476,"pagefriends_count":7,"statuses_count":96481,"favourites_count":205,"created_at":"Thu Nov 12 22:09:28 +0800 2009","following":true,"allow_all_act_msg":false,"geo_enabled":false,"verified":true,"verified_type":3,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp1.sinaimg.cn/1660452532/180/40060008980/0","avatar_hd":"http://ww1.sinaimg.cn/crop.0.0.179.179.1024/62f87eb4gw1eic3hhy5jpj2050050mxb.jpg","verified_reason":"猫扑网","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":364,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":2,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":33},"reposts_count":13,"comments_count":26,"attitudes_count":18,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"rid":"13_0_1_2666885038303173078","userType":0},{"created_at":"Mon Nov 02 21:41:13 +0800 2015","id":3904847575258867,"mid":"3904847575258867","idstr":"3904847575258867","text":"艺高人胆大\u2026诺贝尔奖手到擒来[笑cry]//@M大王叫我来巡山:逆向研发ˊ_>ˋ 这么说来养啥死啥的我爸很可能就是对抗癌细胞的终极武器😂////@柳三便:癌症患者的福音//@福大笑长: 估计是吃了食堂的饭[doge]//@我的大几把岁了: 中国医学史上的重大突破[笑cry]","source_allowclick":0,"source_type":2,"source":"","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":3217179555,"idstr":"3217179555","class":1,"screen_name":"回忆专用小马甲","name":"回忆专用小马甲","province":"100","city":"1000","location":"其他","description":"愿无岁月可回头","url":"","profile_image_url":"http://tp4.sinaimg.cn/3217179555/50/5693967295/1","cover_image":"http://ww3.sinaimg.cn/crop.0.135.980.300/bfc243a3gw1edm6ymenftj20r80c376d.jpg","cover_image_phone":"http://ww4.sinaimg.cn/crop.0.0.0.0/bfc243a3jw1edcdj6uj59j20hs0hsdgq.jpg","profile_url":"u/3217179555","domain":"","weihao":"","gender":"m","followers_count":18834510,"friends_count":580,"pagefriends_count":1,"statuses_count":8167,"favourites_count":3609,"created_at":"Thu Jan 10 13:37:59 +0800 2013","following":true,"allow_all_act_msg":true,"geo_enabled":false,"verified":true,"verified_type":0,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/3217179555/180/5693967295/1","avatar_hd":"http://ww3.sinaimg.cn/crop.0.0.640.640.1024/bfc243a3jw8efzr4c9ajij20hs0hsaav.jpg","verified_reason":"微博人气博主","verified_trade":"3370","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":542,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":6,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":30},"pid":3904725466334735,"retweeted_status":{"created_at":"Sun Nov 01 19:48:06 +0800 2015","id":3904456720836661,"mid":"3904456720836661","idstr":"3904456720836661","text":"[笑cry]刚接到电话说研究生把肿瘤细胞养死了\u2026\u2026养死了\u2026\u2026这得是什么样的技术能把这么结实的细胞给养死\u2026\u2026","source_allowclick":0,"source_type":1,"source":"<a href=\"http://weibo.com/\" rel=\"nofollow\">微博 weibo.com<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":2729107341,"idstr":"2729107341","class":1,"screen_name":"magicianmaster","name":"magicianmaster","province":"32","city":"5","location":"江苏 苏州","description":"Yaoi.Shotacon.Furry.本子爱好者.E绅士.详细请看置顶_(:з」∠)_","url":"","profile_image_url":"http://tp2.sinaimg.cn/2729107341/50/5719636214/1","cover_image_phone":"http://ww1.sinaimg.cn/crop.0.0.640.640.640/a1d3feabjw1ecat4uqw77j20hs0hsacp.jpg","profile_url":"magicianmaster","domain":"magicianmaster","weihao":"","gender":"m","followers_count":3020,"friends_count":351,"pagefriends_count":0,"statuses_count":1015,"favourites_count":4,"created_at":"Sat May 12 21:46:48 +0800 2012","following":false,"allow_all_act_msg":false,"geo_enabled":false,"verified":false,"verified_type":-1,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp2.sinaimg.cn/2729107341/180/5719636214/1","avatar_hd":"http://ww4.sinaimg.cn/crop.0.0.800.800.1024/a2aadf8djw8epj82o8dpej20m80m80zn.jpg","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"online_status":0,"bi_followers_count":174,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":2,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":20},"reposts_count":13149,"comments_count":1453,"attitudes_count":1885,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"userType":0},"annotations":[{"mapi_request":true}],"reposts_count":2487,"comments_count":626,"attitudes_count":3708,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"rid":"14_0_1_2666885038303173078","userType":0},{"created_at":"Mon Nov 02 21:39:29 +0800 2015","id":3904847139161722,"mid":"3904847139161722","idstr":"3904847139161722","text":"我刚在@微盘 发现了一个很不错的文件\"第七集脸型数据.rar\"，推荐你也来看看哦！ http://t.cn/RUcQH4a","source_allowclick":0,"source_type":1,"source":"<a href=\"http://app.weibo.com/t/feed/2R0oBt\" rel=\"nofollow\">微盘<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":1750820364,"idstr":"1750820364","class":1,"screen_name":"夙玖玖的酒","name":"夙玖玖的酒","province":"33","city":"9","location":"浙江 舟山","description":"","url":"","profile_image_url":"http://tp1.sinaimg.cn/1750820364/50/5736182229/0","profile_url":"u/1750820364","domain":"","weihao":"","gender":"f","followers_count":67,"friends_count":56,"pagefriends_count":0,"statuses_count":76,"favourites_count":6,"created_at":"Fri Jun 11 10:47:39 +0800 2010","following":true,"allow_all_act_msg":false,"geo_enabled":true,"verified":false,"verified_type":-1,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp1.sinaimg.cn/1750820364/180/5736182229/0","avatar_hd":"http://ww3.sinaimg.cn/crop.0.0.640.640.1024/685b660cjw8evomfvzgr8j20hs0hsq3v.jpg","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":true,"online_status":0,"bi_followers_count":7,"lang":"zh-cn","star":0,"mbtype":0,"mbrank":0,"block_word":0,"block_app":0,"credit_score":80,"user_ability":0,"urank":18},"reposts_count":0,"comments_count":0,"attitudes_count":0,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_ids":[0],"biz_feature":0,"darwin_tags":[],"rid":"15_0_1_2666885038303173078","userType":0},{"created_at":"Mon Nov 02 21:39:10 +0800 2015","id":3904847059400798,"mid":"3904847059400798","idstr":"3904847059400798","text":"转发微博","source_allowclick":0,"source_type":1,"source":"<a href=\"http://app.weibo.com/t/feed/4CpTyG\" rel=\"nofollow\">荣耀畅玩4<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":2092500601,"idstr":"2092500601","class":1,"screen_name":"hi大头鬼hi","name":"hi大头鬼hi","province":"31","city":"5","location":"上海 长宁区","description":"全栈工程师，Android&大前端","url":"http://github.com/lzyzsd","profile_image_url":"http://tp2.sinaimg.cn/2092500601/50/5633318570/1","cover_image_phone":"http://ww1.sinaimg.cn/crop.0.0.640.640.640/a1d3feabjw1ecat4uqw77j20hs0hsacp.jpg","profile_url":"brucefromsdu","domain":"brucefromsdu","weihao":"","gender":"m","followers_count":2845,"friends_count":1429,"pagefriends_count":0,"statuses_count":2766,"favourites_count":2532,"created_at":"Sun Apr 17 18:38:11 +0800 2011","following":true,"allow_all_act_msg":false,"geo_enabled":true,"verified":false,"verified_type":-1,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp2.sinaimg.cn/2092500601/180/5633318570/1","avatar_hd":"http://tp2.sinaimg.cn/2092500601/180/5633318570/1","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"online_status":0,"bi_followers_count":376,"lang":"zh-cn","star":0,"mbtype":0,"mbrank":0,"block_word":0,"block_app":0,"credit_score":80,"user_ability":0,"urank":23},"retweeted_status":{"created_at":"Sun Nov 01 18:15:51 +0800 2015","id":3904433504986998,"mid":"3904433504986998","idstr":"3904433504986998","text":"【Atom 1.1 is out】http://t.cn/RUcGWNE 编辑神器Atom正式发布1.1，标志着通过新的Atom Beta Channel的首个稳定版本诞生，有许多重大改善，其中包括Markdown预览，模糊搜索...同时发布的Atom 1.2.0-beta支持CJK softwrap（参见配图8，点击大图可动画）","source_allowclick":0,"source_type":1,"source":"<a href=\"http://weibo.com/\" rel=\"nofollow\">微博 weibo.com<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/663aa05agw1exlmvxlf4fj20h8040gm3.jpg"},{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/663aa05agw1exlmwc0h05j21bk0s4q9a.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/663aa05agw1exlmwgmw5zj217e066tag.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/663aa05agw1exlmwn7r7ij20nu04q3z2.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/663aa05agw1exlmwx7nkaj21ds0z4jzh.jpg"},{"thumbnail_pic":"http://ww3.sinaimg.cn/thumbnail/663aa05agw1exlmwxstzwj21040raal4.jpg"},{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/663aa05agw1exlmwydmawj21040ra7e9.jpg"},{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/663aa05agw1exlmx2vd1tg20ml0d348p.gif"}],"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/663aa05agw1exlmvxlf4fj20h8040gm3.jpg","bmiddle_pic":"http://ww1.sinaimg.cn/bmiddle/663aa05agw1exlmvxlf4fj20h8040gm3.jpg","original_pic":"http://ww1.sinaimg.cn/large/663aa05agw1exlmvxlf4fj20h8040gm3.jpg","geo":null,"user":{"id":1715118170,"idstr":"1715118170","class":1,"screen_name":"网路冷眼","name":"网路冷眼","province":"11","city":"8","location":"北京 海淀区","description":"网下架构师，网上围观客。","url":"http://blog.sina.com/lewhwa","profile_image_url":"http://tp3.sinaimg.cn/1715118170/50/5653230631/1","cover_image_phone":"http://ww1.sinaimg.cn/crop.0.0.640.640/549d0121tw1egm1kjly3jj20hs0hsq4f.jpg","profile_url":"lewhwa","domain":"lewhwa","weihao":"","gender":"m","followers_count":30970,"friends_count":3020,"pagefriends_count":0,"statuses_count":20738,"favourites_count":4474,"created_at":"Sat Mar 20 20:28:36 +0800 2010","following":false,"allow_all_act_msg":false,"geo_enabled":true,"verified":true,"verified_type":0,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp3.sinaimg.cn/1715118170/180/5653230631/1","avatar_hd":"http://tp3.sinaimg.cn/1715118170/180/5653230631/1","verified_reason":"互联网资讯博主","verified_trade":"1186","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":2461,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":6,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"cardid":"star_005","urank":34},"reposts_count":152,"comments_count":21,"attitudes_count":313,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"userType":0,"cardid":"star_005"},"annotations":[{"client_mblogid":"1d8c759a-59df-474f-8d18-49c290ceaae6"},{"mapi_request":true}],"reposts_count":1,"comments_count":1,"attitudes_count":1,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"rid":"16_0_1_2666885038303173078","userType":0},{"created_at":"Mon Nov 02 21:32:12 +0800 2015","id":3904845305686047,"mid":"3904845305686047","idstr":"3904845305686047","text":"好美啊 //@电影热搜令:卧槽这画风！！！","source_allowclick":0,"source_type":1,"source":"<a href=\"http://app.weibo.com/t/feed/3o33sO\" rel=\"nofollow\">iPhone 6<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":2993873502,"idstr":"2993873502","class":1,"screen_name":"我整个人都感到不太好了","name":"我整个人都感到不太好了","province":"100","city":"1000","location":"其他","description":"站住！别跑！","url":"","profile_image_url":"http://tp3.sinaimg.cn/2993873502/50/5735292796/1","cover_image":"http://ww4.sinaimg.cn/crop.0.0.920.300/b272e25egw1ep6qncusltj20pk08ct96.jpg","cover_image_phone":"http://ww4.sinaimg.cn/crop.0.0.0.0/0064e8mdjw1ewx3j7ha0fj30yi0yit94.jpg","profile_url":"u/2993873502","domain":"","weihao":"","gender":"m","followers_count":603074,"friends_count":689,"pagefriends_count":0,"statuses_count":21707,"favourites_count":156,"created_at":"Sat Sep 08 11:57:26 +0800 2012","following":true,"allow_all_act_msg":true,"geo_enabled":true,"verified":true,"verified_type":0,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp3.sinaimg.cn/2993873502/180/5735292796/1","avatar_hd":"http://ww2.sinaimg.cn/crop.0.0.1242.1242.1024/b272e25ejw8evcpzr1bz9j20yi0yiwfp.jpg","verified_reason":"知名搞笑幽默博主","verified_trade":"484","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":558,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":5,"block_word":0,"block_app":1,"credit_score":76,"user_ability":0,"cardid":"star_001","urank":29},"pid":3904844701875659,"retweeted_status":{"created_at":"Mon Nov 02 21:01:03 +0800 2015","id":3904837466609734,"mid":"3904837466609734","idstr":"3904837466609734","text":"#阿木资源社# 一位波兰画师绘制的作品，画风也是超级赞的(●´∀｀●)","source_allowclick":0,"source_type":1,"source":"<a href=\"http://weibo.com/\" rel=\"nofollow\">微博 weibo.com<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[{"thumbnail_pic":"http://ww3.sinaimg.cn/thumbnail/a284a161gw1exmsyd9w5xj20jw0ruwkn.jpg"},{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/a284a161gw1exmsyii6jmj20jc0qedkj.jpg"},{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/a284a161gw1exmsyrjflvj20jn0rsguj.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/a284a161gw1exmsyx6rqgj20uk0jlwj3.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/a284a161gw1exmsz0olyoj20ma0fctbe.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/a284a161gw1exmsz55oqjj20dx0jg780.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/a284a161gw1exmsz9qbavj20tm0ku78i.jpg"},{"thumbnail_pic":"http://ww1.sinaimg.cn/thumbnail/a284a161gw1exmszolpxpj20gv0nmjwz.jpg"},{"thumbnail_pic":"http://ww4.sinaimg.cn/thumbnail/a284a161gw1exmt0eg9dnj20ho0p0jzk.jpg"}],"thumbnail_pic":"http://ww3.sinaimg.cn/thumbnail/a284a161gw1exmsyd9w5xj20jw0ruwkn.jpg","bmiddle_pic":"http://ww3.sinaimg.cn/bmiddle/a284a161gw1exmsyd9w5xj20jw0ruwkn.jpg","original_pic":"http://ww3.sinaimg.cn/large/a284a161gw1exmsyd9w5xj20jw0ruwkn.jpg","geo":null,"user":{"id":2726601057,"idstr":"2726601057","class":1,"screen_name":"进击的阿木君","name":"进击的阿木君","province":"44","city":"1000","location":"广东","description":"发布动漫一手信息，P图小能手，酷爱搬运日推汤不热，偶尔制作点动图。","url":"","profile_image_url":"http://tp2.sinaimg.cn/2726601057/50/40079628039/1","cover_image":"http://ww4.sinaimg.cn/crop.0.0.920.300/a284a161gw1eoil3tep9rj20pk08cgon.jpg","cover_image_phone":"http://ww2.sinaimg.cn/crop.0.0.0.0/a284a161jw1esiuc03tsaj20ku0kut9u.jpg","profile_url":"u/2726601057","domain":"","weihao":"","gender":"m","followers_count":2938204,"friends_count":517,"pagefriends_count":1,"statuses_count":8408,"favourites_count":3,"created_at":"Mon May 07 23:26:59 +0800 2012","following":false,"allow_all_act_msg":false,"geo_enabled":false,"verified":true,"verified_type":0,"remark":"","ptype":8,"allow_all_comment":true,"avatar_large":"http://tp2.sinaimg.cn/2726601057/180/40079628039/1","avatar_hd":"http://ww4.sinaimg.cn/crop.0.9.645.645.1024/a284a161gw1epmm5zttphj20k00k0n36.jpg","verified_reason":"微博知名动漫博主 微博签约自媒体","verified_trade":"3370","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":507,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":5,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"cardid":"star_005","urank":28},"reposts_count":149,"comments_count":8,"attitudes_count":230,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"userType":0,"cardid":"star_005"},"annotations":[{"mapi_request":true}],"reposts_count":25,"comments_count":1,"attitudes_count":38,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"rid":"17_0_1_2666885038303173078","userType":0,"cardid":"star_001"},{"created_at":"Mon Nov 02 21:23:42 +0800 2015","id":3904843171748946,"mid":"3904843171748946","idstr":"3904843171748946","text":"哦","source_allowclick":0,"source_type":2,"source":"","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":3069348215,"idstr":"3069348215","class":1,"screen_name":"同道大叔","name":"同道大叔","province":"11","city":"1000","location":"北京","description":"星座只是娱乐，理想是带着粉丝发财。工作联系 邮箱：tongdaodashu@peiwo.me","url":"","profile_image_url":"http://tp4.sinaimg.cn/3069348215/50/5721712236/1","cover_image":"http://ww1.sinaimg.cn/crop.0.0.920.300/b6f28977gw1equ63hmar1j20pk08c783.jpg","cover_image_phone":"http://ww1.sinaimg.cn/crop.0.0.0.0/b6f28977jw1ev4f724ypqj20e80e8wh0.jpg","profile_url":"tongdaodashu","domain":"tongdaodashu","weihao":"","gender":"m","followers_count":6741623,"friends_count":884,"pagefriends_count":0,"statuses_count":6419,"favourites_count":64,"created_at":"Thu Oct 25 13:47:18 +0800 2012","following":true,"allow_all_act_msg":false,"geo_enabled":true,"verified":true,"verified_type":0,"remark":"","ptype":10,"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/3069348215/180/5721712236/1","avatar_hd":"http://ww4.sinaimg.cn/crop.0.0.512.512.1024/b6f28977jw8eqb042gir6j20e80e8aa9.jpg","verified_reason":"微博知名星座博主","verified_trade":"3370","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":805,"lang":"zh-cn","star":0,"mbtype":13,"mbrank":5,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":29},"retweeted_status":{"created_at":"Mon Nov 02 21:21:55 +0800 2015","id":3904842718229498,"mid":"3904842718229498","idstr":"3904842718229498","text":"\u201c如何洒脱的从失恋中走出来\u201d","source_allowclick":0,"source_type":1,"source":"<a href=\"http://app.weibo.com/t/feed/5B6hUc\" rel=\"nofollow\">iPhone 6s Plus<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[{"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/005AZjD9jw1exmt8ldxq0j30yi0u10ze.jpg"}],"thumbnail_pic":"http://ww2.sinaimg.cn/thumbnail/005AZjD9jw1exmt8ldxq0j30yi0u10ze.jpg","bmiddle_pic":"http://ww2.sinaimg.cn/bmiddle/005AZjD9jw1exmt8ldxq0j30yi0u10ze.jpg","original_pic":"http://ww2.sinaimg.cn/large/005AZjD9jw1exmt8ldxq0j30yi0u10ze.jpg","geo":null,"user":{"id":5127225727,"idstr":"5127225727","class":1,"screen_name":"一只鸡腿子","name":"一只鸡腿子","province":"11","city":"1000","location":"北京","description":"我挺好的。希望你也吃饱。","url":"","profile_image_url":"http://tp4.sinaimg.cn/5127225727/50/5734775631/0","cover_image":"http://ww3.sinaimg.cn/crop.0.0.920.300/005AZjD9jw1ent8ozt6gjj30pk08cwn3.jpg","cover_image_phone":"http://ww2.sinaimg.cn/crop.0.0.0.0/005AZjD9jw1ev737m8pz6j30v90v9n02.jpg","profile_url":"u/5127225727","domain":"","weihao":"","gender":"f","followers_count":909595,"friends_count":190,"pagefriends_count":2,"statuses_count":3029,"favourites_count":3,"created_at":"Fri Oct 10 15:06:40 +0800 2014","following":false,"allow_all_act_msg":false,"geo_enabled":true,"verified":false,"verified_type":-1,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/5127225727/180/5734775631/0","avatar_hd":"http://ww1.sinaimg.cn/crop.0.0.512.512.1024/005AZjD9jw8ev5svbepw4j30e80e8jsg.jpg","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"online_status":0,"bi_followers_count":181,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":4,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":23},"annotations":[{"client_mblogid":"iPhone-89A6CA17-5B2C-4296-B554-35234174C3D4"},{"mapi_request":true}],"reposts_count":5587,"comments_count":1477,"attitudes_count":2240,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":4294967300,"darwin_tags":[],"userType":0},"annotations":[{"mapi_request":true}],"reposts_count":3682,"comments_count":766,"attitudes_count":2704,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_feature":0,"darwin_tags":[],"rid":"18_0_1_2666885038303173078","userType":0},{"created_at":"Mon Nov 02 21:20:47 +0800 2015","id":3904842433275380,"mid":"3904842433275380","idstr":"3904842433275380","text":"打开美.拍瞬间被帅得少女心都碎掉，唱歌还分分钟让耳朵怀孕，我要报警了[doge]http://t.cn/RUxcqXf","source_allowclick":0,"source_type":1,"source":"<a href=\"http://weibo.com/\" rel=\"nofollow\">微博 weibo.com<\/a>","favorited":false,"truncated":false,"in_reply_to_status_id":"","in_reply_to_user_id":"","in_reply_to_screen_name":"","pic_urls":[],"geo":null,"user":{"id":3099016097,"idstr":"3099016097","class":1,"screen_name":"英国报姐","name":"英国报姐","province":"81","city":"2","location":"香港 中西区","description":"订阅号：ukuk520,国外各种好玩哒，我啥都发~【注：玻璃心，喜释梗，广告狗","url":"","profile_image_url":"http://tp2.sinaimg.cn/3099016097/50/5727275657/0","cover_image":"http://ww4.sinaimg.cn/crop.0.0.920.300/b8b73ba1gw1esdg2pmvabj20pk08cdlp.jpg","cover_image_phone":"http://ww3.sinaimg.cn/crop.0.0.0.0/b8b73ba1jw1emojcngmjlj20e80e8q49.jpg","profile_url":"uktimes","domain":"uktimes","weihao":"","gender":"f","followers_count":8062644,"friends_count":457,"pagefriends_count":2,"statuses_count":7455,"favourites_count":128,"created_at":"Sat Nov 10 14:28:36 +0800 2012","following":true,"allow_all_act_msg":false,"geo_enabled":true,"verified":true,"verified_type":0,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp2.sinaimg.cn/3099016097/180/5727275657/0","avatar_hd":"http://ww2.sinaimg.cn/crop.0.0.300.300.1024/b8b73ba1jw8esdg2b03k5j208c08d400.jpg","verified_reason":"微博知名中英文化自由撰稿人","verified_trade":"3370","verified_reason_url":"","verified_source":"","verified_source_url":"","verified_state":0,"verified_level":3,"verified_reason_modified":"","verified_contact_name":"","verified_contact_email":"","verified_contact_mobile":"","follow_me":false,"online_status":0,"bi_followers_count":446,"lang":"zh-tw","star":0,"mbtype":11,"mbrank":6,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":31},"reposts_count":579,"comments_count":902,"attitudes_count":1182,"mlevel":0,"visible":{"type":0,"list_id":0},"biz_ids":[230444],"biz_feature":0,"darwin_tags":[],"rid":"19_0_1_2666885038303173078","userType":0}]
     * advertises : []
     * ad : []
     * hasvisible : false
     * previous_cursor : 0
     * next_cursor : 3904841749174397
     * total_number : 150
     * interval : 2000
     * uve_blank : -1
     * since_id : 3904859252596728
     * max_id : 3904841749174397
     * has_unread : 0
     */

    private boolean hasvisible;
    private int previous_cursor;
    private long next_cursor;
    private int total_number;
    private int interval;
    private int uve_blank;
    private long since_id;
    private long max_id;
    private int has_unread;
    /**
     * created_at : Mon Nov 02 22:27:36 +0800 2015
     * id : 3904859252596728
     * mid : 3904859252596728
     * idstr : 3904859252596728
     * text : 这个视频一点都不萌，就看了好几遍~[笑cry] (cr.梅梅盼达) http://t.cn/RUIlA32
     * source_allowclick : 0
     * source_type : 1
     * source : <a href="http://weibo.com/" rel="nofollow">微博 weibo.com</a>
     * favorited : false
     * truncated : false
     * in_reply_to_status_id :
     * in_reply_to_user_id :
     * in_reply_to_screen_name :
     * pic_urls : []
     * geo : null
     * user : {"id":1713926427,"idstr":"1713926427","class":1,"screen_name":"微博搞笑排行榜","name":"微博搞笑排行榜","province":"44","city":"1000","location":"广东","description":"微博搞笑中心！每天搜罗最搞笑最好玩的微博。关注我，获得每日新鲜笑料！（欢迎合作，投稿）↖(^ω^)↗","url":"http://weibo.com/mkdqs","profile_image_url":"http://tp4.sinaimg.cn/1713926427/50/40000875722/0","cover_image_phone":"http://ww3.sinaimg.cn/crop.0.0.640.640.640/6ce2240djw1e9oaaziu7sj20hs0hs0ui.jpg","profile_url":"topgirls8","domain":"topgirls8","weihao":"","gender":"f","followers_count":27158185,"friends_count":837,"pagefriends_count":0,"statuses_count":88408,"favourites_count":7056,"created_at":"Fri Mar 19 00:42:49 +0800 2010","following":true,"allow_all_act_msg":true,"geo_enabled":false,"verified":false,"verified_type":-1,"remark":"","ptype":0,"allow_all_comment":true,"avatar_large":"http://tp4.sinaimg.cn/1713926427/180/40000875722/0","avatar_hd":"http://tp4.sinaimg.cn/1713926427/180/40000875722/0","verified_reason":"","verified_trade":"","verified_reason_url":"","verified_source":"","verified_source_url":"","follow_me":false,"online_status":0,"bi_followers_count":297,"lang":"zh-cn","star":0,"mbtype":12,"mbrank":6,"block_word":0,"block_app":1,"credit_score":80,"user_ability":0,"urank":37}
     * reposts_count : 10
     * comments_count : 20
     * attitudes_count : 46
     * mlevel : 0
     * visible : {"type":0,"list_id":0}
     * biz_ids : [230444]
     * biz_feature : 0
     * darwin_tags : []
     * rid : 0_0_1_2666885038303173078
     * userType : 0
     */

    private List<StatusesEntity> statuses;
    private List<?> advertises;
    private List<?> ad;

    public void setHasvisible(boolean hasvisible) {
        this.hasvisible = hasvisible;
    }

    public void setPrevious_cursor(int previous_cursor) {
        this.previous_cursor = previous_cursor;
    }

    public void setNext_cursor(long next_cursor) {
        this.next_cursor = next_cursor;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setUve_blank(int uve_blank) {
        this.uve_blank = uve_blank;
    }

    public void setSince_id(long since_id) {
        this.since_id = since_id;
    }

    public void setMax_id(long max_id) {
        this.max_id = max_id;
    }

    public void setHas_unread(int has_unread) {
        this.has_unread = has_unread;
    }

    public void setStatuses(List<StatusesEntity> statuses) {
        this.statuses = statuses;
    }

    public void setAdvertises(List<?> advertises) {
        this.advertises = advertises;
    }

    public void setAd(List<?> ad) {
        this.ad = ad;
    }

    public boolean isHasvisible() {
        return hasvisible;
    }

    public int getPrevious_cursor() {
        return previous_cursor;
    }

    public long getNext_cursor() {
        return next_cursor;
    }

    public int getTotal_number() {
        return total_number;
    }

    public int getInterval() {
        return interval;
    }

    public int getUve_blank() {
        return uve_blank;
    }

    public long getSince_id() {
        return since_id;
    }

    public long getMax_id() {
        return max_id;
    }

    public int getHas_unread() {
        return has_unread;
    }

    public List<StatusesEntity> getStatuses() {
        return statuses;
    }

    public List<?> getAdvertises() {
        return advertises;
    }

    public List<?> getAd() {
        return ad;
    }

    public static class StatusesEntity {
        private String created_at;
        private long id;
        private String mid;
        private String idstr;
        private String text;
        private int source_allowclick;
        private int source_type;
        private String source;
        private boolean favorited;
        private boolean truncated;
        private String in_reply_to_status_id;
        private String in_reply_to_user_id;
        private String in_reply_to_screen_name;
        private Object geo;
        /**
         * id : 1713926427
         * idstr : 1713926427
         * class : 1
         * screen_name : 微博搞笑排行榜
         * name : 微博搞笑排行榜
         * province : 44
         * city : 1000
         * location : 广东
         * description : 微博搞笑中心！每天搜罗最搞笑最好玩的微博。关注我，获得每日新鲜笑料！（欢迎合作，投稿）↖(^ω^)↗
         * url : http://weibo.com/mkdqs
         * profile_image_url : http://tp4.sinaimg.cn/1713926427/50/40000875722/0
         * cover_image_phone : http://ww3.sinaimg.cn/crop.0.0.640.640.640/6ce2240djw1e9oaaziu7sj20hs0hs0ui.jpg
         * profile_url : topgirls8
         * domain : topgirls8
         * weihao :
         * gender : f
         * followers_count : 27158185
         * friends_count : 837
         * pagefriends_count : 0
         * statuses_count : 88408
         * favourites_count : 7056
         * created_at : Fri Mar 19 00:42:49 +0800 2010
         * following : true
         * allow_all_act_msg : true
         * geo_enabled : false
         * verified : false
         * verified_type : -1
         * remark :
         * ptype : 0
         * allow_all_comment : true
         * avatar_large : http://tp4.sinaimg.cn/1713926427/180/40000875722/0
         * avatar_hd : http://tp4.sinaimg.cn/1713926427/180/40000875722/0
         * verified_reason :
         * verified_trade :
         * verified_reason_url :
         * verified_source :
         * verified_source_url :
         * follow_me : false
         * online_status : 0
         * bi_followers_count : 297
         * lang : zh-cn
         * star : 0
         * mbtype : 12
         * mbrank : 6
         * block_word : 0
         * block_app : 1
         * credit_score : 80
         * user_ability : 0
         * urank : 37
         */

        private UserEntity user;
        private int reposts_count;
        private int comments_count;
        private int attitudes_count;
        private int mlevel;
        /**
         * type : 0
         * list_id : 0
         */

        private VisibleEntity visible;
        private int biz_feature;
        private String rid;
        private int userType;
        private List<?> pic_urls;
        private List<Integer> biz_ids;
        private List<?> darwin_tags;

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public void setId(long id) {
            this.id = id;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public void setIdstr(String idstr) {
            this.idstr = idstr;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setSource_allowclick(int source_allowclick) {
            this.source_allowclick = source_allowclick;
        }

        public void setSource_type(int source_type) {
            this.source_type = source_type;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public void setFavorited(boolean favorited) {
            this.favorited = favorited;
        }

        public void setTruncated(boolean truncated) {
            this.truncated = truncated;
        }

        public void setIn_reply_to_status_id(String in_reply_to_status_id) {
            this.in_reply_to_status_id = in_reply_to_status_id;
        }

        public void setIn_reply_to_user_id(String in_reply_to_user_id) {
            this.in_reply_to_user_id = in_reply_to_user_id;
        }

        public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
            this.in_reply_to_screen_name = in_reply_to_screen_name;
        }

        public void setGeo(Object geo) {
            this.geo = geo;
        }

        public void setUser(UserEntity user) {
            this.user = user;
        }

        public void setReposts_count(int reposts_count) {
            this.reposts_count = reposts_count;
        }

        public void setComments_count(int comments_count) {
            this.comments_count = comments_count;
        }

        public void setAttitudes_count(int attitudes_count) {
            this.attitudes_count = attitudes_count;
        }

        public void setMlevel(int mlevel) {
            this.mlevel = mlevel;
        }

        public void setVisible(VisibleEntity visible) {
            this.visible = visible;
        }

        public void setBiz_feature(int biz_feature) {
            this.biz_feature = biz_feature;
        }

        public void setRid(String rid) {
            this.rid = rid;
        }

        public void setUserType(int userType) {
            this.userType = userType;
        }

        public void setPic_urls(List<?> pic_urls) {
            this.pic_urls = pic_urls;
        }

        public void setBiz_ids(List<Integer> biz_ids) {
            this.biz_ids = biz_ids;
        }

        public void setDarwin_tags(List<?> darwin_tags) {
            this.darwin_tags = darwin_tags;
        }

        public String getCreated_at() {
            return created_at;
        }

        public long getId() {
            return id;
        }

        public String getMid() {
            return mid;
        }

        public String getIdstr() {
            return idstr;
        }

        public String getText() {
            return text;
        }

        public int getSource_allowclick() {
            return source_allowclick;
        }

        public int getSource_type() {
            return source_type;
        }

        public String getSource() {
            return source;
        }

        public boolean isFavorited() {
            return favorited;
        }

        public boolean isTruncated() {
            return truncated;
        }

        public String getIn_reply_to_status_id() {
            return in_reply_to_status_id;
        }

        public String getIn_reply_to_user_id() {
            return in_reply_to_user_id;
        }

        public String getIn_reply_to_screen_name() {
            return in_reply_to_screen_name;
        }

        public Object getGeo() {
            return geo;
        }

        public UserEntity getUser() {
            return user;
        }

        public int getReposts_count() {
            return reposts_count;
        }

        public int getComments_count() {
            return comments_count;
        }

        public int getAttitudes_count() {
            return attitudes_count;
        }

        public int getMlevel() {
            return mlevel;
        }

        public VisibleEntity getVisible() {
            return visible;
        }

        public int getBiz_feature() {
            return biz_feature;
        }

        public String getRid() {
            return rid;
        }

        public int getUserType() {
            return userType;
        }

        public List<?> getPic_urls() {
            return pic_urls;
        }

        public List<Integer> getBiz_ids() {
            return biz_ids;
        }

        public List<?> getDarwin_tags() {
            return darwin_tags;
        }

        public static class UserEntity {
            private int id;
            private String idstr;
            @SerializedName("class")
            private int classX;
            private String screen_name;
            private String name;
            private String province;
            private String city;
            private String location;
            private String description;
            private String url;
            private String profile_image_url;
            private String cover_image_phone;
            private String profile_url;
            private String domain;
            private String weihao;
            private String gender;
            private int followers_count;
            private int friends_count;
            private int pagefriends_count;
            private int statuses_count;
            private int favourites_count;
            private String created_at;
            private boolean following;
            private boolean allow_all_act_msg;
            private boolean geo_enabled;
            private boolean verified;
            private int verified_type;
            private String remark;
            private int ptype;
            private boolean allow_all_comment;
            private String avatar_large;
            private String avatar_hd;
            private String verified_reason;
            private String verified_trade;
            private String verified_reason_url;
            private String verified_source;
            private String verified_source_url;
            private boolean follow_me;
            private int online_status;
            private int bi_followers_count;
            private String lang;
            private int star;
            private int mbtype;
            private int mbrank;
            private int block_word;
            private int block_app;
            private int credit_score;
            private int user_ability;
            private int urank;

            public void setId(int id) {
                this.id = id;
            }

            public void setIdstr(String idstr) {
                this.idstr = idstr;
            }

            public void setClassX(int classX) {
                this.classX = classX;
            }

            public void setScreen_name(String screen_name) {
                this.screen_name = screen_name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public void setProfile_image_url(String profile_image_url) {
                this.profile_image_url = profile_image_url;
            }

            public void setCover_image_phone(String cover_image_phone) {
                this.cover_image_phone = cover_image_phone;
            }

            public void setProfile_url(String profile_url) {
                this.profile_url = profile_url;
            }

            public void setDomain(String domain) {
                this.domain = domain;
            }

            public void setWeihao(String weihao) {
                this.weihao = weihao;
            }

            public void setGender(String gender) {
                this.gender = gender;
            }

            public void setFollowers_count(int followers_count) {
                this.followers_count = followers_count;
            }

            public void setFriends_count(int friends_count) {
                this.friends_count = friends_count;
            }

            public void setPagefriends_count(int pagefriends_count) {
                this.pagefriends_count = pagefriends_count;
            }

            public void setStatuses_count(int statuses_count) {
                this.statuses_count = statuses_count;
            }

            public void setFavourites_count(int favourites_count) {
                this.favourites_count = favourites_count;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public void setFollowing(boolean following) {
                this.following = following;
            }

            public void setAllow_all_act_msg(boolean allow_all_act_msg) {
                this.allow_all_act_msg = allow_all_act_msg;
            }

            public void setGeo_enabled(boolean geo_enabled) {
                this.geo_enabled = geo_enabled;
            }

            public void setVerified(boolean verified) {
                this.verified = verified;
            }

            public void setVerified_type(int verified_type) {
                this.verified_type = verified_type;
            }

            public void setRemark(String remark) {
                this.remark = remark;
            }

            public void setPtype(int ptype) {
                this.ptype = ptype;
            }

            public void setAllow_all_comment(boolean allow_all_comment) {
                this.allow_all_comment = allow_all_comment;
            }

            public void setAvatar_large(String avatar_large) {
                this.avatar_large = avatar_large;
            }

            public void setAvatar_hd(String avatar_hd) {
                this.avatar_hd = avatar_hd;
            }

            public void setVerified_reason(String verified_reason) {
                this.verified_reason = verified_reason;
            }

            public void setVerified_trade(String verified_trade) {
                this.verified_trade = verified_trade;
            }

            public void setVerified_reason_url(String verified_reason_url) {
                this.verified_reason_url = verified_reason_url;
            }

            public void setVerified_source(String verified_source) {
                this.verified_source = verified_source;
            }

            public void setVerified_source_url(String verified_source_url) {
                this.verified_source_url = verified_source_url;
            }

            public void setFollow_me(boolean follow_me) {
                this.follow_me = follow_me;
            }

            public void setOnline_status(int online_status) {
                this.online_status = online_status;
            }

            public void setBi_followers_count(int bi_followers_count) {
                this.bi_followers_count = bi_followers_count;
            }

            public void setLang(String lang) {
                this.lang = lang;
            }

            public void setStar(int star) {
                this.star = star;
            }

            public void setMbtype(int mbtype) {
                this.mbtype = mbtype;
            }

            public void setMbrank(int mbrank) {
                this.mbrank = mbrank;
            }

            public void setBlock_word(int block_word) {
                this.block_word = block_word;
            }

            public void setBlock_app(int block_app) {
                this.block_app = block_app;
            }

            public void setCredit_score(int credit_score) {
                this.credit_score = credit_score;
            }

            public void setUser_ability(int user_ability) {
                this.user_ability = user_ability;
            }

            public void setUrank(int urank) {
                this.urank = urank;
            }

            public int getId() {
                return id;
            }

            public String getIdstr() {
                return idstr;
            }

            public int getClassX() {
                return classX;
            }

            public String getScreen_name() {
                return screen_name;
            }

            public String getName() {
                return name;
            }

            public String getProvince() {
                return province;
            }

            public String getCity() {
                return city;
            }

            public String getLocation() {
                return location;
            }

            public String getDescription() {
                return description;
            }

            public String getUrl() {
                return url;
            }

            public String getProfile_image_url() {
                return profile_image_url;
            }

            public String getCover_image_phone() {
                return cover_image_phone;
            }

            public String getProfile_url() {
                return profile_url;
            }

            public String getDomain() {
                return domain;
            }

            public String getWeihao() {
                return weihao;
            }

            public String getGender() {
                return gender;
            }

            public int getFollowers_count() {
                return followers_count;
            }

            public int getFriends_count() {
                return friends_count;
            }

            public int getPagefriends_count() {
                return pagefriends_count;
            }

            public int getStatuses_count() {
                return statuses_count;
            }

            public int getFavourites_count() {
                return favourites_count;
            }

            public String getCreated_at() {
                return created_at;
            }

            public boolean isFollowing() {
                return following;
            }

            public boolean isAllow_all_act_msg() {
                return allow_all_act_msg;
            }

            public boolean isGeo_enabled() {
                return geo_enabled;
            }

            public boolean isVerified() {
                return verified;
            }

            public int getVerified_type() {
                return verified_type;
            }

            public String getRemark() {
                return remark;
            }

            public int getPtype() {
                return ptype;
            }

            public boolean isAllow_all_comment() {
                return allow_all_comment;
            }

            public String getAvatar_large() {
                return avatar_large;
            }

            public String getAvatar_hd() {
                return avatar_hd;
            }

            public String getVerified_reason() {
                return verified_reason;
            }

            public String getVerified_trade() {
                return verified_trade;
            }

            public String getVerified_reason_url() {
                return verified_reason_url;
            }

            public String getVerified_source() {
                return verified_source;
            }

            public String getVerified_source_url() {
                return verified_source_url;
            }

            public boolean isFollow_me() {
                return follow_me;
            }

            public int getOnline_status() {
                return online_status;
            }

            public int getBi_followers_count() {
                return bi_followers_count;
            }

            public String getLang() {
                return lang;
            }

            public int getStar() {
                return star;
            }

            public int getMbtype() {
                return mbtype;
            }

            public int getMbrank() {
                return mbrank;
            }

            public int getBlock_word() {
                return block_word;
            }

            public int getBlock_app() {
                return block_app;
            }

            public int getCredit_score() {
                return credit_score;
            }

            public int getUser_ability() {
                return user_ability;
            }

            public int getUrank() {
                return urank;
            }
        }

        public static class VisibleEntity {
            private int type;
            private int list_id;

            public void setType(int type) {
                this.type = type;
            }

            public void setList_id(int list_id) {
                this.list_id = list_id;
            }

            public int getType() {
                return type;
            }

            public int getList_id() {
                return list_id;
            }
        }
    }
}
