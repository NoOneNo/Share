//package com.hengye.share.ui.widget.emoticon;
//
//import com.hengye.share.R;
//
///**
// * Created by yuhy on 2017/2/23.
// */
//
//public class Test {
//
//    //---
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[最右]"), R.drawable.d_zuiyou);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[摊手]"), R.drawable.d_tanshou);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[抱抱]"), R.drawable.d_baobao);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[二哈]"), R.drawable.d_erha);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[doge]"), R.drawable.d_doge);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[喵喵]"), R.drawable.d_miao);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[微笑]"), R.drawable.d_hehe);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[嘻嘻]"), R.drawable.d_xixi);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[哈哈]"), R.drawable.d_haha);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[爱你]"), R.drawable.d_aini);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[挖鼻]"), R.drawable.d_wabishi);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[吃惊]"), R.drawable.d_chijing);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[晕]"), R.drawable.d_yun);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[泪]"), R.drawable.d_lei);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[馋嘴]"), R.drawable.d_chanzui);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[抓狂]"), R.drawable.d_zhuakuang);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[哼]"), R.drawable.d_heng);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[可爱]"), R.drawable.d_keai);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[怒]"), R.drawable.d_nu);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[汗]"), R.drawable.d_han);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[害羞]"), R.drawable.d_haixiu);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[睡]"), R.drawable.d_shuijiao);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[钱]"), R.drawable.d_qian);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[偷笑]"), R.drawable.d_touxiao);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[笑cry]"), R.drawable.d_xiaoku);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[酷]"), R.drawable.d_ku);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[衰]"), R.drawable.d_shuai);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[闭嘴]"), R.drawable.d_bizui);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[鄙视]"), R.drawable.d_bishi);
//    //花心->色
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[色]"), R.drawable.d_huaxin);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[鼓掌]"), R.drawable.d_guzhang);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[悲伤]"), R.drawable.d_beishang);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[思考]"), R.drawable.d_sikao);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[生病]"), R.drawable.d_shengbing);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[亲亲]"), R.drawable.d_qinqin);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[怒骂]"), R.drawable.d_numa);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[太开心]"), R.drawable.d_taikaixin);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[白眼]"), R.drawable.d_landelini);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[右哼哼]"), R.drawable.d_youhengheng);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[左哼哼]"), R.drawable.d_zuohengheng);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[嘘]"), R.drawable.d_xu);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[委屈]"), R.drawable.d_weiqu);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[吐]"), R.drawable.d_tu);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[可怜]"), R.drawable.d_kelian);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[哈欠]"), R.drawable.d_dahaqi);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[挤眼]"), R.drawable.d_jiyan);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[失望]"), R.drawable.d_shiwang);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[顶]"), R.drawable.d_ding);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[疑问]"), R.drawable.d_yiwen);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[困]"), R.drawable.d_kun);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[感冒]"), R.drawable.d_ganmao);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[拜拜]"), R.drawable.d_baibai);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[黑线]"), R.drawable.d_heixian);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[阴险]"), R.drawable.d_yinxian);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[打脸]"), R.drawable.d_dalian);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[傻眼]"), R.drawable.d_shayan);
////        mWeiBoEmoticonMap.put(new WBEmoticonKey("[马到成功]), R.drawable.d_madaochenggong);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[骷髅]"), R.drawable.d_kulou);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[坏笑]"), R.drawable.d_huaixiao);
//    //舔-舔屏
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[舔屏]"), R.drawable.d_tian);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[污]"), R.drawable.d_wu);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[互粉]"), R.drawable.f_hufen);
//
//    //哆啦A梦
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[哆啦A梦吃惊]"), R.drawable.dora_chijing);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[哆啦A梦花心]"), R.drawable.dora_huaxin);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[哆啦A梦微笑]"), R.drawable.dora_weixiao);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[哆啦A梦汗]"), R.drawable.dora_han);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[哆啦A梦害怕]"), R.drawable.dora_haipa);
//
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[心]"), R.drawable.l_xin);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[伤心]"), R.drawable.l_shangxin);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[猪头]"), R.drawable.d_zhutou);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[熊猫]"), R.drawable.d_xiongmao);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[兔子]"), R.drawable.d_tuzi);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[握手]"), R.drawable.h_woshou);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[作揖]"), R.drawable.h_zuoyi);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[赞]"), R.drawable.h_zan);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[耶]"), R.drawable.h_ye);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[good]"), R.drawable.h_good);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[弱]"), R.drawable.h_ruo);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[NO]"), R.drawable.h_buyao);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[ok]"), R.drawable.h_ok);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[haha]"), R.drawable.h_haha);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[来]"), R.drawable.h_lai);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[拳头]"), R.drawable.h_quantou);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[加油]"), R.drawable.h_jiayou);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[威武]"), R.drawable.f_v5);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[鲜花]"), R.drawable.w_xianhua);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[钟]"), R.drawable.o_zhong);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[浮云]"), R.drawable.w_fuyun);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[飞机]"), R.drawable.o_feiji);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[月亮]"), R.drawable.w_yueliang);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[太阳]"), R.drawable.w_taiyang);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[微风]"), R.drawable.w_weifeng);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[下雨]"), R.drawable.w_xiayu);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[带着微博去旅行]"), R.drawable.f_eventtravel);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[给力]"), R.drawable.f_geili);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[广告]"), R.drawable.f_guanggao);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[神马]"), R.drawable.f_shenma);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[围观]"), R.drawable.o_weiguan);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[话筒]"), R.drawable.o_huatong);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[奥特曼]"), R.drawable.d_aoteman);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[草泥马]"), R.drawable.d_shenshou);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[萌]"), R.drawable.f_meng);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[囧]"), R.drawable.f_jiong);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[织]"), R.drawable.f_zhi);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[礼物]"), R.drawable.o_liwu);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[发红包]"), R.drawable.f_fahongbao);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[囍]"), R.drawable.f_xi);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[围脖]"), R.drawable.o_weibo);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[音乐]"), R.drawable.o_yinyue);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[绿丝带]"), R.drawable.o_lvsidai);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[蛋糕]"), R.drawable.o_dangao);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[蜡烛]"), R.drawable.o_lazhu);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[干杯]"), R.drawable.o_ganbei);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[男孩儿]"), R.drawable.d_nanhaier);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[女孩儿]"), R.drawable.d_nvhaier);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[肥皂]"), R.drawable.d_feizao);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[照相机]"), R.drawable.o_zhaoxiangji);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[浪]"), R.drawable.d_lang);
//    mWeiBoEmoticonMap.put(new WBEmoticonKey("[沙尘暴]"), R.drawable.w_shachenbao);
//}
