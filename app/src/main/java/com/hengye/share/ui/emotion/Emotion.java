package com.hengye.share.ui.emotion;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.hengye.share.BaseApplication;
import com.hengye.share.R;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Emotion {

    private static Emotion sInstance = new Emotion();

    public static Emotion getInstance() {
        return sInstance;
    }

    public final static String EMOTION_DIRECTORY_NAME = "emotion";
    public final static String EMOTION_TYPE_EMOJI = "emoji";
    public final static String EMOTION_TYPE_WEIBO = "weibo";
    public final static String EMOTION_TYPE_LXH = "lxh";

    private HashMap<String, Bitmap> mEmotionBitmap;
    private HashMap<String, LinkedHashMap<String, Bitmap>> mSortedEmotionBitmap;
    private LinkedHashMap<String, String> mEmojiEmotion, mWeiBoEmotion, mLXHEmotion;

    private Emotion() {
        getEmotionBitmap();
    }

    public HashMap<String, Bitmap> getEmotionBitmap(){
        if(mEmotionBitmap != null){
            return mEmotionBitmap;
        }

        mEmotionBitmap = new HashMap<>();

        mEmotionBitmap.putAll(getSortedEmotionBitmap().get(EMOTION_TYPE_WEIBO));
        mEmotionBitmap.putAll(getSortedEmotionBitmap().get(EMOTION_TYPE_LXH));

        return mEmotionBitmap;
    }

    public HashMap<String, LinkedHashMap<String, Bitmap>> getSortedEmotionBitmap(){
        if(mSortedEmotionBitmap != null) {
            return mSortedEmotionBitmap;
        }
        mSortedEmotionBitmap = new HashMap<>();

        mSortedEmotionBitmap.put(EMOTION_TYPE_WEIBO, generateEmotionBitmap(getWeiBoEmotion(), getEmotionDirectoryName(EMOTION_TYPE_WEIBO)));
        mSortedEmotionBitmap.put(EMOTION_TYPE_LXH, generateEmotionBitmap(getLXHEmotion(), getEmotionDirectoryName(EMOTION_TYPE_LXH)));

        return mSortedEmotionBitmap;
    }

    private String getEmotionDirectoryName(String emotionType){
        String prefix = EMOTION_DIRECTORY_NAME;
        String suffix = emotionType;
        return prefix + File.separator + suffix + File.separator;
    }

    private LinkedHashMap<String, Bitmap> generateEmotionBitmap(Map<String, String> emotion, String directory) {
        List<String> index = new ArrayList<>();
        index.addAll(emotion.keySet());
        LinkedHashMap<String, Bitmap> emotionBitmap = new LinkedHashMap<String, Bitmap>();
        for (String str : index) {
            String name = emotion.get(str);
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

    public LinkedHashMap<String, String> getEmojiEmotion(){
        if(mEmojiEmotion != null){
            return mEmojiEmotion;
        }
        return null;
    }

    public LinkedHashMap<String, String> getWeiBoEmotion(){
        if(mWeiBoEmotion != null){
            return mWeiBoEmotion;
        }

        mWeiBoEmotion = new LinkedHashMap<>();

        mWeiBoEmotion.put("[最右]", "d_zuiyou.png");
        mWeiBoEmotion.put("[微笑]", "d_hehe.png");
        mWeiBoEmotion.put("[嘻嘻]", "d_xixi.png");
        mWeiBoEmotion.put("[哈哈]", "d_haha.png");
        mWeiBoEmotion.put("[爱你]", "d_aini.png");
        mWeiBoEmotion.put("[挖鼻]", "d_wabishi.png");
        mWeiBoEmotion.put("[吃惊]", "d_chijing.png");
        mWeiBoEmotion.put("[晕]", "d_yun.png");
        mWeiBoEmotion.put("[泪]", "d_lei.png");
        mWeiBoEmotion.put("[馋嘴]", "d_chanzui.png");
        mWeiBoEmotion.put("[抓狂]", "d_zhuakuang.png");
        mWeiBoEmotion.put("[哼]", "d_heng.png");
        mWeiBoEmotion.put("[可爱]", "d_keai.png");
        mWeiBoEmotion.put("[怒]", "d_nu.png");
        mWeiBoEmotion.put("[汗]", "d_han.png");
        mWeiBoEmotion.put("[害羞]", "d_haixiu.png");
        mWeiBoEmotion.put("[睡]", "d_shuijiao.png");
        mWeiBoEmotion.put("[钱]", "d_qian.png");
        mWeiBoEmotion.put("[偷笑]", "d_touxiao.png");
        mWeiBoEmotion.put("[笑cry]", "d_xiaoku.png");
        mWeiBoEmotion.put("[doge]", "d_doge.png");
        mWeiBoEmotion.put("[喵喵]", "d_miao.png");
        mWeiBoEmotion.put("[酷]", "d_ku.png");
        mWeiBoEmotion.put("[衰]", "d_shuai.png");
        mWeiBoEmotion.put("[闭嘴]", "d_bizui.png");
        mWeiBoEmotion.put("[鄙视]", "d_bishi.png");
        mWeiBoEmotion.put("[花心]", "d_huaxin.png");
        mWeiBoEmotion.put("[鼓掌]", "d_guzhang.png");
        mWeiBoEmotion.put("[悲伤]", "d_beishang.png");
        mWeiBoEmotion.put("[思考]", "d_sikao.png");
        mWeiBoEmotion.put("[生病]", "d_shengbing.png");
        mWeiBoEmotion.put("[亲亲]", "d_qinqin.png");
        mWeiBoEmotion.put("[怒骂]", "d_numa.png");
        mWeiBoEmotion.put("[太开心]", "d_taikaixin.png");
        mWeiBoEmotion.put("[懒得理你]", "d_landelini.png");
        mWeiBoEmotion.put("[右哼哼]", "d_youhengheng.png");
        mWeiBoEmotion.put("[左哼哼]", "d_zuohengheng.png");
        mWeiBoEmotion.put("[嘘]", "d_xu.png");
        mWeiBoEmotion.put("[委屈]", "d_weiqu.png");
        mWeiBoEmotion.put("[吐]", "d_tu.png");
        mWeiBoEmotion.put("[可怜]", "d_kelian.png");
        mWeiBoEmotion.put("[打哈欠]", "d_dahaqi.png");
        mWeiBoEmotion.put("[挤眼]", "d_jiyan.png");
        mWeiBoEmotion.put("[失望]", "d_shiwang.png");
        mWeiBoEmotion.put("[顶]", "d_ding.png");
        mWeiBoEmotion.put("[疑问]", "d_yiwen.png");
        mWeiBoEmotion.put("[困]", "d_kun.png");
        mWeiBoEmotion.put("[感冒]", "d_ganmao.png");
        mWeiBoEmotion.put("[拜拜]", "d_baibai.png");
        mWeiBoEmotion.put("[黑线]", "d_heixian.png");
        mWeiBoEmotion.put("[阴险]", "d_yinxian.png");
        mWeiBoEmotion.put("[打脸]", "d_dalian.png");
        mWeiBoEmotion.put("[傻眼]", "d_shayan.png");
        //-----
        mWeiBoEmotion.put("[互粉]", "f_hufen.png");
        mWeiBoEmotion.put("[心]", "l_xin.png");
        mWeiBoEmotion.put("[伤心]", "l_shangxin.png");
        mWeiBoEmotion.put("[猪头]", "d_zhutou.png");
        mWeiBoEmotion.put("[熊猫]", "d_xiongmao.png");
        mWeiBoEmotion.put("[兔子]", "d_tuzi.png");
        mWeiBoEmotion.put("[握手]", "h_woshou.png");
        mWeiBoEmotion.put("[作揖]", "h_zuoyi.png");
        mWeiBoEmotion.put("[赞]", "h_zan.png");
        mWeiBoEmotion.put("[耶]", "h_ye.png");
        mWeiBoEmotion.put("[good]", "h_good.png");
        mWeiBoEmotion.put("[弱]", "h_ruo.png");
        mWeiBoEmotion.put("[NO]", "h_buyao.png");
        mWeiBoEmotion.put("[ok]", "h_ok.png");
        mWeiBoEmotion.put("[haha]", "h_haha.png");
        mWeiBoEmotion.put("[来]", "h_lai.png");
        mWeiBoEmotion.put("[拳头]", "h_quantou.png");
        mWeiBoEmotion.put("[威武]", "f_v5.png");
        mWeiBoEmotion.put("[鲜花]", "w_xianhua.png");
        mWeiBoEmotion.put("[钟]", "o_zhong.png");
        mWeiBoEmotion.put("[浮云]", "w_fuyun.png");
        mWeiBoEmotion.put("[飞机]", "o_feiji.png");
        mWeiBoEmotion.put("[月亮]", "w_yueliang.png");
        mWeiBoEmotion.put("[太阳]", "w_taiyang.png");
        mWeiBoEmotion.put("[微风]", "w_weifeng.png");
        mWeiBoEmotion.put("[下雨]", "w_xiayu.png");
        mWeiBoEmotion.put("[给力]", "f_geili.png");
        mWeiBoEmotion.put("[神马]", "f_shenma.png");
        mWeiBoEmotion.put("[围观]", "o_weiguan.png");
        mWeiBoEmotion.put("[话筒]", "o_huatong.png");
        mWeiBoEmotion.put("[奥特曼]", "d_aoteman.png");
        mWeiBoEmotion.put("[草泥马]", "d_shenshou.png");
        mWeiBoEmotion.put("[萌]", "f_meng.png");
        mWeiBoEmotion.put("[囧]", "f_jiong.png");
        mWeiBoEmotion.put("[织]", "f_zhi.png");
        mWeiBoEmotion.put("[礼物]", "o_liwu.png");
        mWeiBoEmotion.put("[囍]", "f_xi.png");
        mWeiBoEmotion.put("[围脖]", "o_weibo.png");
        mWeiBoEmotion.put("[音乐]", "o_yinyue.png");
        mWeiBoEmotion.put("[绿丝带]", "o_lvsidai.png");
        mWeiBoEmotion.put("[蛋糕]", "o_dangao.png");
        mWeiBoEmotion.put("[蜡烛]", "o_lazhu.png");
        mWeiBoEmotion.put("[干杯]", "o_ganbei.png");
        mWeiBoEmotion.put("[男孩儿]", "d_nanhaier.png");
        mWeiBoEmotion.put("[女孩儿]", "d_nvhaier.png");
        mWeiBoEmotion.put("[肥皂]", "d_feizao.png");
        mWeiBoEmotion.put("[照相机]", "o_zhaoxiangji.png");
        mWeiBoEmotion.put("[浪]", "d_lang.png");
        mWeiBoEmotion.put("[沙尘暴]", "w_shachenbao.png");

        return mWeiBoEmotion;
    }

    public LinkedHashMap<String, String> getLXHEmotion(){
        if(mLXHEmotion != null){
            return mLXHEmotion;
        }

        mLXHEmotion = new LinkedHashMap<>();

        mLXHEmotion.put("[笑哈哈]", "lxh_xiaohaha.png");
        mLXHEmotion.put("[好爱哦]", "lxh_haoaio.png");
        mLXHEmotion.put("[噢耶]", "lxh_oye.png");
        mLXHEmotion.put("[偷乐]", "lxh_toule.png");
        mLXHEmotion.put("[泪流满面]", "lxh_leiliumanmian.png");
        mLXHEmotion.put("[巨汗]", "lxh_juhan.png");
        mLXHEmotion.put("[抠鼻屎]", "lxh_koubishi.png");
        mLXHEmotion.put("[求关注]", "lxh_qiuguanzhu.png");
        mLXHEmotion.put("[好喜欢]", "lxh_haoxihuan.png");
        mLXHEmotion.put("[崩溃]", "lxh_bengkui.png");
        mLXHEmotion.put("[好囧]", "lxh_haojiong.png");
        mLXHEmotion.put("[震惊]", "lxh_zhenjing.png");
        mLXHEmotion.put("[别烦我]", "lxh_biefanwo.png");
        mLXHEmotion.put("[不好意思]", "lxh_buhaoyisi.png");
        mLXHEmotion.put("[羞嗒嗒]", "lxh_xiudada.png");
        mLXHEmotion.put("[得意地笑]", "lxh_deyidexiao.png");
        mLXHEmotion.put("[纠结]", "lxh_jiujie.png");
        mLXHEmotion.put("[给劲]", "lxh_feijin.png");
        mLXHEmotion.put("[悲催]", "lxh_beicui.png");
        mLXHEmotion.put("[甩甩手]", "lxh_shuaishuaishou.png");
        mLXHEmotion.put("[好棒]", "lxh_haobang.png");
        mLXHEmotion.put("[瞧瞧]", "lxh_qiaoqiao.png");
        mLXHEmotion.put("[不想上班]", "lxh_buxiangshangban.png");
        mLXHEmotion.put("[困死了]", "lxh_kunsile.png");
        mLXHEmotion.put("[许愿]", "lxh_xuyuan.png");
        mLXHEmotion.put("[丘比特]", "lxh_qiubite.png");
        mLXHEmotion.put("[有鸭梨]", "lxh_youyali.png");
        mLXHEmotion.put("[想一想]", "lxh_xiangyixiang.png");
        mLXHEmotion.put("[躁狂症]", "lxh_kuangzaozheng.png");
        mLXHEmotion.put("[转发]", "lxh_zhuanfa.png");
        mLXHEmotion.put("[互相膜拜]", "lxh_xianghumobai.png");
        mLXHEmotion.put("[雷锋]", "lxh_leifeng.png");
        mLXHEmotion.put("[杰克逊]", "lxh_jiekexun.png");
        mLXHEmotion.put("[玫瑰]", "lxh_meigui.png");
        mLXHEmotion.put("[hold住]", "lxh_holdzhu.png");
        mLXHEmotion.put("[群体围观]", "lxh_quntiweiguan.png");
        mLXHEmotion.put("[推荐]", "lxh_tuijian.png");
        mLXHEmotion.put("[赞啊]", "lxh_zana.png");
        mLXHEmotion.put("[被电]", "lxh_beidian.png");
        mLXHEmotion.put("[霹雳]", "lxh_pili.png");
        return mLXHEmotion;
    }
}
