package com.hengye.share.ui.widget.emoticon;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hengye.share.module.base.BaseApplication;
import com.hengye.share.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Emoticon {

    private static Emoticon sInstance = new Emoticon();

    public static Emoticon getInstance() {
        return sInstance;
    }

    public final static String EMOTICON_DIRECTORY_NAME = "emotion";
    public final static String EMOTICON_TYPE_EMOJI = "emoji";
    public final static String EMOTICON_TYPE_WEIBO = "weibo";
    public final static String EMOTICON_TYPE_LXH = "lxh";

    private HashMap<String, Bitmap> mEmoticonBitmap;
    private HashMap<String, LinkedHashMap<String, Bitmap>> mSortedEmoticonBitmap;
    private LinkedHashMap<String, String> mEmojiEmotion, mWeiBoEmoticon, mLXHEmotcion;

    private Emoticon() {
        getEmoticonBitmap();
    }

    public HashMap<String, Bitmap> getEmoticonBitmap(){
        if(mEmoticonBitmap != null){
            return mEmoticonBitmap;
        }

        mEmoticonBitmap = new HashMap<>();

        mEmoticonBitmap.putAll(getSortedEmoticonBitmap().get(EMOTICON_TYPE_WEIBO));
        mEmoticonBitmap.putAll(getSortedEmoticonBitmap().get(EMOTICON_TYPE_LXH));

        return mEmoticonBitmap;
    }

    public HashMap<String, LinkedHashMap<String, Bitmap>> getSortedEmoticonBitmap(){
        if(mSortedEmoticonBitmap != null) {
            return mSortedEmoticonBitmap;
        }
        mSortedEmoticonBitmap = new HashMap<>();

        mSortedEmoticonBitmap.put(EMOTICON_TYPE_WEIBO, generateEmoticonBitmap(getWeiBoEmoticon(), getEmoticonDirectoryName(EMOTICON_TYPE_WEIBO)));
        mSortedEmoticonBitmap.put(EMOTICON_TYPE_LXH, generateEmoticonBitmap(getLXHEmoction(), getEmoticonDirectoryName(EMOTICON_TYPE_LXH)));

        return mSortedEmoticonBitmap;
    }

    private String getEmoticonDirectoryName(String emoticonType){
        String prefix = EMOTICON_DIRECTORY_NAME;
        String suffix = emoticonType;
        return prefix + File.separator + suffix + File.separator;
    }

    private LinkedHashMap<String, Bitmap> generateEmoticonBitmap(Map<String, String> emoticon, String directory) {
        List<String> index = new ArrayList<>();
        index.addAll(emoticon.keySet());
        LinkedHashMap<String, Bitmap> emotionBitmap = new LinkedHashMap<String, Bitmap>();
        for (String str : index) {
            String name = emoticon.get(str);
            AssetManager assetManager = BaseApplication.getInstance().getAssets();
            InputStream inputStream;
            try {
                inputStream = assetManager.open(directory + name);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                if (bitmap != null) {
                    int size = BaseApplication.getInstance().getResources().getDimensionPixelSize(R.dimen.emotion_size);
                    Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);
                    if (bitmap != scaledBitmap) {
                        bitmap.recycle();
                        bitmap = scaledBitmap;
                    }
                    emotionBitmap.put(str, bitmap);
                }
            } catch (IOException ignored) {

            }
        }

        return emotionBitmap;
    }

    public LinkedHashMap<String, String> getEmojiEmoticon(){
        if(mEmojiEmotion != null){
            return mEmojiEmotion;
        }
        return null;
    }

    public LinkedHashMap<String, String> getWeiBoEmoticon(){
        if(mWeiBoEmoticon != null){
            return mWeiBoEmoticon;
        }

        mWeiBoEmoticon = new LinkedHashMap<>();

        //哆啦A梦
        mWeiBoEmoticon.put("[哆啦A梦吃惊]", "dora_chijing.png");
        mWeiBoEmoticon.put("[哆啦A梦花心]", "dora_huaxin.png");
        mWeiBoEmoticon.put("[哆啦A梦微笑]", "dora_weixiao.png");
        //---
        mWeiBoEmoticon.put("[最右]", "d_zuiyou.png");
        mWeiBoEmoticon.put("[微笑]", "d_hehe.png");
        mWeiBoEmoticon.put("[嘻嘻]", "d_xixi.png");
        mWeiBoEmoticon.put("[哈哈]", "d_haha.png");
        mWeiBoEmoticon.put("[爱你]", "d_aini.png");
        mWeiBoEmoticon.put("[挖鼻]", "d_wabishi.png");
        mWeiBoEmoticon.put("[吃惊]", "d_chijing.png");
        mWeiBoEmoticon.put("[晕]", "d_yun.png");
        mWeiBoEmoticon.put("[泪]", "d_lei.png");
        mWeiBoEmoticon.put("[馋嘴]", "d_chanzui.png");
        mWeiBoEmoticon.put("[抓狂]", "d_zhuakuang.png");
        mWeiBoEmoticon.put("[哼]", "d_heng.png");
        mWeiBoEmoticon.put("[可爱]", "d_keai.png");
        mWeiBoEmoticon.put("[怒]", "d_nu.png");
        mWeiBoEmoticon.put("[汗]", "d_han.png");
        mWeiBoEmoticon.put("[害羞]", "d_haixiu.png");
        mWeiBoEmoticon.put("[睡]", "d_shuijiao.png");
        mWeiBoEmoticon.put("[钱]", "d_qian.png");
        mWeiBoEmoticon.put("[偷笑]", "d_touxiao.png");
        mWeiBoEmoticon.put("[笑cry]", "d_xiaoku.png");
        mWeiBoEmoticon.put("[doge]", "d_doge.png");
        mWeiBoEmoticon.put("[喵喵]", "d_miao.png");
        mWeiBoEmoticon.put("[酷]", "d_ku.png");
        mWeiBoEmoticon.put("[衰]", "d_shuai.png");
        mWeiBoEmoticon.put("[闭嘴]", "d_bizui.png");
        mWeiBoEmoticon.put("[鄙视]", "d_bishi.png");
        mWeiBoEmoticon.put("[花心]", "d_huaxin.png");
        mWeiBoEmoticon.put("[鼓掌]", "d_guzhang.png");
        mWeiBoEmoticon.put("[悲伤]", "d_beishang.png");
        mWeiBoEmoticon.put("[思考]", "d_sikao.png");
        mWeiBoEmoticon.put("[生病]", "d_shengbing.png");
        mWeiBoEmoticon.put("[亲亲]", "d_qinqin.png");
        mWeiBoEmoticon.put("[怒骂]", "d_numa.png");
        mWeiBoEmoticon.put("[太开心]", "d_taikaixin.png");
        mWeiBoEmoticon.put("[懒得理你]", "d_landelini.png");
        mWeiBoEmoticon.put("[右哼哼]", "d_youhengheng.png");
        mWeiBoEmoticon.put("[左哼哼]", "d_zuohengheng.png");
        mWeiBoEmoticon.put("[嘘]", "d_xu.png");
        mWeiBoEmoticon.put("[委屈]", "d_weiqu.png");
        mWeiBoEmoticon.put("[吐]", "d_tu.png");
        mWeiBoEmoticon.put("[可怜]", "d_kelian.png");
        mWeiBoEmoticon.put("[打哈欠]", "d_dahaqi.png");
        mWeiBoEmoticon.put("[挤眼]", "d_jiyan.png");
        mWeiBoEmoticon.put("[失望]", "d_shiwang.png");
        mWeiBoEmoticon.put("[顶]", "d_ding.png");
        mWeiBoEmoticon.put("[疑问]", "d_yiwen.png");
        mWeiBoEmoticon.put("[困]", "d_kun.png");
        mWeiBoEmoticon.put("[感冒]", "d_ganmao.png");
        mWeiBoEmoticon.put("[拜拜]", "d_baibai.png");
        mWeiBoEmoticon.put("[黑线]", "d_heixian.png");
        mWeiBoEmoticon.put("[阴险]", "d_yinxian.png");
        mWeiBoEmoticon.put("[打脸]", "d_dalian.png");
        mWeiBoEmoticon.put("[傻眼]", "d_shayan.png");
        //--16.11.22添加
        mWeiBoEmoticon.put("[马到成功]", "d_madaochenggong.png");
        mWeiBoEmoticon.put("[骷髅]", "d_kulou.png");
        mWeiBoEmoticon.put("[摊手]", "d_tanshou.png");
        mWeiBoEmoticon.put("[坏笑]", "d_huaixiao.png");
        mWeiBoEmoticon.put("[舔]", "d_tian.png");
        mWeiBoEmoticon.put("[污]", "d_wu.png");
        mWeiBoEmoticon.put("[抱抱]", "d_baobao.png");
        mWeiBoEmoticon.put("[二哈]", "d_erha.png");
        //-----
        mWeiBoEmoticon.put("[互粉]", "f_hufen.png");
        mWeiBoEmoticon.put("[心]", "l_xin.png");
        mWeiBoEmoticon.put("[伤心]", "l_shangxin.png");
        mWeiBoEmoticon.put("[猪头]", "d_zhutou.png");
        mWeiBoEmoticon.put("[熊猫]", "d_xiongmao.png");
        mWeiBoEmoticon.put("[兔子]", "d_tuzi.png");
        mWeiBoEmoticon.put("[握手]", "h_woshou.png");
        mWeiBoEmoticon.put("[作揖]", "h_zuoyi.png");
        mWeiBoEmoticon.put("[赞]", "h_zan.png");
        mWeiBoEmoticon.put("[耶]", "h_ye.png");
        mWeiBoEmoticon.put("[good]", "h_good.png");
        mWeiBoEmoticon.put("[弱]", "h_ruo.png");
        mWeiBoEmoticon.put("[NO]", "h_buyao.png");
        mWeiBoEmoticon.put("[ok]", "h_ok.png");
        mWeiBoEmoticon.put("[haha]", "h_haha.png");
        mWeiBoEmoticon.put("[来]", "h_lai.png");
        mWeiBoEmoticon.put("[拳头]", "h_quantou.png");
        mWeiBoEmoticon.put("[威武]", "f_v5.png");
        mWeiBoEmoticon.put("[鲜花]", "w_xianhua.png");
        mWeiBoEmoticon.put("[钟]", "o_zhong.png");
        mWeiBoEmoticon.put("[浮云]", "w_fuyun.png");
        mWeiBoEmoticon.put("[飞机]", "o_feiji.png");
        mWeiBoEmoticon.put("[月亮]", "w_yueliang.png");
        mWeiBoEmoticon.put("[太阳]", "w_taiyang.png");
        mWeiBoEmoticon.put("[微风]", "w_weifeng.png");
        mWeiBoEmoticon.put("[下雨]", "w_xiayu.png");
        mWeiBoEmoticon.put("[给力]", "f_geili.png");
        mWeiBoEmoticon.put("[神马]", "f_shenma.png");
        mWeiBoEmoticon.put("[围观]", "o_weiguan.png");
        mWeiBoEmoticon.put("[话筒]", "o_huatong.png");
        mWeiBoEmoticon.put("[奥特曼]", "d_aoteman.png");
        mWeiBoEmoticon.put("[草泥马]", "d_shenshou.png");
        mWeiBoEmoticon.put("[萌]", "f_meng.png");
        mWeiBoEmoticon.put("[囧]", "f_jiong.png");
        mWeiBoEmoticon.put("[织]", "f_zhi.png");
        mWeiBoEmoticon.put("[礼物]", "o_liwu.png");
        mWeiBoEmoticon.put("[囍]", "f_xi.png");
        mWeiBoEmoticon.put("[围脖]", "o_weibo.png");
        mWeiBoEmoticon.put("[音乐]", "o_yinyue.png");
        mWeiBoEmoticon.put("[绿丝带]", "o_lvsidai.png");
        mWeiBoEmoticon.put("[蛋糕]", "o_dangao.png");
        mWeiBoEmoticon.put("[蜡烛]", "o_lazhu.png");
        mWeiBoEmoticon.put("[干杯]", "o_ganbei.png");
        mWeiBoEmoticon.put("[男孩儿]", "d_nanhaier.png");
        mWeiBoEmoticon.put("[女孩儿]", "d_nvhaier.png");
        mWeiBoEmoticon.put("[肥皂]", "d_feizao.png");
        mWeiBoEmoticon.put("[照相机]", "o_zhaoxiangji.png");
        mWeiBoEmoticon.put("[浪]", "d_lang.png");
        mWeiBoEmoticon.put("[沙尘暴]", "w_shachenbao.png");

        return mWeiBoEmoticon;
    }

    public LinkedHashMap<String, String> getLXHEmoction(){
        if(mLXHEmotcion != null){
            return mLXHEmotcion;
        }

        mLXHEmotcion = new LinkedHashMap<>();

        mLXHEmotcion.put("[笑哈哈]", "lxh_xiaohaha.png");
        mLXHEmotcion.put("[好爱哦]", "lxh_haoaio.png");
        mLXHEmotcion.put("[噢耶]", "lxh_oye.png");
        mLXHEmotcion.put("[偷乐]", "lxh_toule.png");
        mLXHEmotcion.put("[泪流满面]", "lxh_leiliumanmian.png");
        mLXHEmotcion.put("[巨汗]", "lxh_juhan.png");
        mLXHEmotcion.put("[抠鼻屎]", "lxh_koubishi.png");
        mLXHEmotcion.put("[求关注]", "lxh_qiuguanzhu.png");
        mLXHEmotcion.put("[好喜欢]", "lxh_haoxihuan.png");
        mLXHEmotcion.put("[崩溃]", "lxh_bengkui.png");
        mLXHEmotcion.put("[好囧]", "lxh_haojiong.png");
        mLXHEmotcion.put("[震惊]", "lxh_zhenjing.png");
        mLXHEmotcion.put("[别烦我]", "lxh_biefanwo.png");
        mLXHEmotcion.put("[不好意思]", "lxh_buhaoyisi.png");
        mLXHEmotcion.put("[羞嗒嗒]", "lxh_xiudada.png");
        mLXHEmotcion.put("[得意地笑]", "lxh_deyidexiao.png");
        mLXHEmotcion.put("[纠结]", "lxh_jiujie.png");
        mLXHEmotcion.put("[给劲]", "lxh_feijin.png");
        mLXHEmotcion.put("[悲催]", "lxh_beicui.png");
        mLXHEmotcion.put("[甩甩手]", "lxh_shuaishuaishou.png");
        mLXHEmotcion.put("[好棒]", "lxh_haobang.png");
        mLXHEmotcion.put("[瞧瞧]", "lxh_qiaoqiao.png");
        mLXHEmotcion.put("[不想上班]", "lxh_buxiangshangban.png");
        mLXHEmotcion.put("[困死了]", "lxh_kunsile.png");
        mLXHEmotcion.put("[许愿]", "lxh_xuyuan.png");
        mLXHEmotcion.put("[丘比特]", "lxh_qiubite.png");
        mLXHEmotcion.put("[有鸭梨]", "lxh_youyali.png");
        mLXHEmotcion.put("[想一想]", "lxh_xiangyixiang.png");
        mLXHEmotcion.put("[躁狂症]", "lxh_kuangzaozheng.png");
        mLXHEmotcion.put("[转发]", "lxh_zhuanfa.png");
        mLXHEmotcion.put("[互相膜拜]", "lxh_xianghumobai.png");
        mLXHEmotcion.put("[雷锋]", "lxh_leifeng.png");
        mLXHEmotcion.put("[杰克逊]", "lxh_jiekexun.png");
        mLXHEmotcion.put("[玫瑰]", "lxh_meigui.png");
        mLXHEmotcion.put("[hold住]", "lxh_holdzhu.png");
        mLXHEmotcion.put("[群体围观]", "lxh_quntiweiguan.png");
        mLXHEmotcion.put("[推荐]", "lxh_tuijian.png");
        mLXHEmotcion.put("[赞啊]", "lxh_zana.png");
        mLXHEmotcion.put("[被电]", "lxh_beidian.png");
        mLXHEmotcion.put("[霹雳]", "lxh_pili.png");
        return mLXHEmotcion;
    }
}
