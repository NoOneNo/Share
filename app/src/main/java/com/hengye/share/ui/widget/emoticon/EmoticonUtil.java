package com.hengye.share.ui.widget.emoticon;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.StringDef;
import android.text.Spannable;
import android.text.style.DynamicDrawableSpan;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;

import com.hengye.share.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yuhy on 2017/2/8.
 */

public class EmoticonUtil {

    public final static String TYPE_EMOJI = "emoji";
    public final static String TYPE_WEIBO = "weibo";
    public final static String TYPE_LXH = "lxh";

    @StringDef({TYPE_EMOJI, TYPE_WEIBO, TYPE_LXH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    private static final Pattern CUSTOM_EMOTICON_URL = Pattern.compile("\\[(\\S+?)\\]");

//    private static final SparseIntArray mEmojiMap = new SparseIntArray(101);
    private static final SparseIntArray mSoftbankMap = new SparseIntArray(89);
    private static final Map<Integer, Integer> mEmojiMap = new LinkedHashMap<>(101);
    private static Map<String, Integer> mWeiBoEmoticonMap = new LinkedHashMap<>();
    private static Map<String, Integer> mLxhEmoticonMap = new LinkedHashMap<>();

    private static List<Emoticon> mWeiboEmoticons = null;
    private static List<Emoticon> mLxhEmoticons = null;
    private static List<Emoticon> mEmojiEmoticons = null;

    private static boolean isSoftBankEmoji(char c) {
        return ((c >> 12) == 0xe);
    }

    private static int getEmojiResId(int codePoint) {
        Integer resId = mEmojiMap.get(codePoint);
        return resId == null ? 0 : resId;
    }

    private static int getSoftbankEmojiResId(char c) {
        return mSoftbankMap.get(c);
    }

    public static List<Emoticon> getEmojicons(@Type String type) {
        switch (type) {
            case TYPE_EMOJI:
                return listEmojiEmoticon();
            case TYPE_WEIBO:
                return listWeiboEmoticon();
            case TYPE_LXH:
                return listLxhEmoticon();
        }
        throw new IllegalArgumentException("Invalid emoticon type: " + type);
    }

    public static void addEmoticon(Context context, Spannable text, int textSize) {
        addEmoticon(context, text, textSize, textSize, DynamicDrawableSpan.ALIGN_BASELINE, 0, -1, false);
    }

    public static void addEmoticon(Context context, Spannable text, int size, int textSize) {
        addEmoticon(context, text, size, textSize, DynamicDrawableSpan.ALIGN_BASELINE, 0, -1, false);
    }

    public static void addEmoticon(Context context, Spannable text, int size, int textSize, int alignment) {
        addEmoticon(context, text, size, textSize, alignment, 0, -1, false);
    }

    /**
     * Convert emoji characters of the given Spannable to the according emojicon.
     *
     * @param context
     * @param text
     * @param size
     * @param alignment
     * @param textSize
     * @param index
     * @param length
     * @param useSystemDefault
     */
    public static void addEmoticon(Context context, Spannable text, int size, int textSize, int alignment, int index, int length, boolean useSystemDefault) {
        // remove spans throughout all text
        EmoticonSpan[] oldSpans = text.getSpans(0, text.length(), EmoticonSpan.class);
        for (int i = 0; i < oldSpans.length; i++) {
            text.removeSpan(oldSpans[i]);
        }

        addCustomEmoticon(context, text, size, textSize, alignment);
        addEmoji(context, text, size, textSize, alignment, index, length, useSystemDefault);
    }

    /**
     * 添加自定义表情
     */
    private static void addCustomEmoticon(Context context, Spannable text, int size, int textSize, int alignment) {
        Matcher localMatcher = CUSTOM_EMOTICON_URL.matcher(text);
        while (localMatcher.find()) {
            String key = localMatcher.group();
            int start = localMatcher.start();
            int end = localMatcher.end();
            if (end - start < 10) {
                //表情文字描述长度都小于10
                Integer emoticonResId = mWeiBoEmoticonMap.get(key);
                if(emoticonResId == null){
                    emoticonResId = mLxhEmoticonMap.get(key);
                }
                if(emoticonResId != null){
                    EmoticonSpan emoticonSpan = new EmoticonSpan(context, emoticonResId, size, textSize, alignment);
                    text.setSpan(emoticonSpan, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
    }

    /**
     * 添加Emoji表情
     */
    private static void addEmoji(Context context, Spannable text, int size, int textSize, int alignment, int index, int length, boolean useSystemDefault) {
        if (useSystemDefault || text == null) {
            return;
        }

        int textLength = text.length();
        int textLengthToProcessMax = textLength - index;
        int textLengthToProcess = length < 0 || length >= textLengthToProcessMax ? textLength : (length + index);

        int skip;
        for (int i = index; i < textLengthToProcess; i += skip) {
            skip = 0;
            int icon = 0;
            char c = text.charAt(i);
            if (isSoftBankEmoji(c)) {
                icon = getSoftbankEmojiResId(c);
                skip = icon == 0 ? 0 : 1;
            }

            if (icon == 0) {
                int unicode = Character.codePointAt(text, i);
                skip = Character.charCount(unicode);

                if (unicode > 0xff) {
                    icon = getEmojiResId(unicode);
                }
            }

            if (icon > 0) {
                text.setSpan(new EmoticonSpan(context, icon, size, textSize, alignment), i, i + skip, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public static List<Emoticon> listWeiboEmoticon(){
        if(mWeiboEmoticons != null){
            return mWeiboEmoticons;
        }

        mWeiboEmoticons = new ArrayList<>();
        for(Map.Entry<String, Integer> entry : mWeiBoEmoticonMap.entrySet()){
            mWeiboEmoticons.add(new Emoticon(entry.getKey(), entry.getValue()));
        }
        return mWeiboEmoticons;
    }

    public static List<Emoticon> listLxhEmoticon(){
        if(mLxhEmoticons != null){
            return mLxhEmoticons;
        }

        mLxhEmoticons = new ArrayList<>();
        for(Map.Entry<String, Integer> entry : mLxhEmoticonMap.entrySet()){
            mLxhEmoticons.add(new Emoticon(entry.getKey(), entry.getValue()));
        }
        return mLxhEmoticons;
    }

    public static List<Emoticon> listEmojiEmoticon(){
        if(mEmojiEmoticons != null){
            return mEmojiEmoticons;
        }

        mEmojiEmoticons = new ArrayList<>();
        for(Map.Entry<Integer, Integer> entry : mEmojiMap.entrySet()){
            mEmojiEmoticons.add(new Emoticon(newEmojiStr(entry.getKey()), entry.getValue()));
        }
        return mEmojiEmoticons;
    }

    private static final String newEmojiStr(int codePoint) {
        if (Character.charCount(codePoint) == 1) {
            return String.valueOf(codePoint);
        } else {
            return new String(Character.toChars(codePoint));
        }
    }

    /**
     * <p>
     * A class, that can be used as a TouchListener on any view (e.g. a Button).
     * It cyclically runs a clickListener, emulating keyboard-like behaviour. First
     * click is fired immediately, next before initialInterval, and subsequent before
     * normalInterval.
     * </p>
     * <p>
     * Interval is scheduled before the onClick completes, so it has to run fast.
     * If it runs slow, it does not generate skipped onClicks.
     * </p>
     */
    public static class RepeatListener implements View.OnTouchListener {

        private Handler handler = new Handler();

        private int initialInterval;
        private final int normalInterval;
        private final View.OnClickListener clickListener;

        private Runnable handlerRunnable = new Runnable() {
            @Override
            public void run() {
                if (downView == null) {
                    return;
                }
                handler.removeCallbacksAndMessages(downView);
                handler.postDelayed(this, normalInterval);
                clickListener.onClick(downView);
            }
        };

        private View downView;

        /**
         * @param initialInterval The interval before first click event
         * @param normalInterval  The interval before second and subsequent click
         *                        events
         * @param clickListener   The OnClickListener, that will be called
         *                        periodically
         */
        public RepeatListener(int initialInterval, int normalInterval, View.OnClickListener clickListener) {
            if (clickListener == null)
                throw new IllegalArgumentException("null runnable");
            if (initialInterval < 0 || normalInterval < 0)
                throw new IllegalArgumentException("negative interval");

            this.initialInterval = initialInterval;
            this.normalInterval = normalInterval;
            this.clickListener = clickListener;
        }

        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downView = view;
                    downView.setSelected(true);
                    handler.removeCallbacks(handlerRunnable);
                    handler.postDelayed(handlerRunnable, initialInterval);
                    clickListener.onClick(view);
                    return true;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_OUTSIDE:
                    handler.removeCallbacksAndMessages(downView);
                    downView.setSelected(false);
                    downView = null;
                    return true;
            }
            return false;
        }
    }

    static{
        //微博表情

        //哆啦A梦
        mWeiBoEmoticonMap.put("[哆啦A梦吃惊]", R.drawable.dora_chijing);
        mWeiBoEmoticonMap.put("[哆啦A梦花心]", R.drawable.dora_huaxin);
        mWeiBoEmoticonMap.put("[哆啦A梦微笑]", R.drawable.dora_weixiao);
        //---
        mWeiBoEmoticonMap.put("[最右]", R.drawable.d_zuiyou);
        mWeiBoEmoticonMap.put("[摊手]", R.drawable.d_tanshou);
        mWeiBoEmoticonMap.put("[抱抱]", R.drawable.d_baobao);
        mWeiBoEmoticonMap.put("[二哈]", R.drawable.d_erha);
        mWeiBoEmoticonMap.put("[微笑]", R.drawable.d_hehe);
        mWeiBoEmoticonMap.put("[嘻嘻]", R.drawable.d_xixi);
        mWeiBoEmoticonMap.put("[哈哈]", R.drawable.d_haha);
        mWeiBoEmoticonMap.put("[爱你]", R.drawable.d_aini);
        mWeiBoEmoticonMap.put("[挖鼻]", R.drawable.d_wabishi);
        mWeiBoEmoticonMap.put("[吃惊]", R.drawable.d_chijing);
        mWeiBoEmoticonMap.put("[晕]", R.drawable.d_yun);
        mWeiBoEmoticonMap.put("[泪]", R.drawable.d_lei);
        mWeiBoEmoticonMap.put("[馋嘴]", R.drawable.d_chanzui);
        mWeiBoEmoticonMap.put("[抓狂]", R.drawable.d_zhuakuang);
        mWeiBoEmoticonMap.put("[哼]", R.drawable.d_heng);
        mWeiBoEmoticonMap.put("[可爱]", R.drawable.d_keai);
        mWeiBoEmoticonMap.put("[怒]", R.drawable.d_nu);
        mWeiBoEmoticonMap.put("[汗]", R.drawable.d_han);
        mWeiBoEmoticonMap.put("[害羞]", R.drawable.d_haixiu);
        mWeiBoEmoticonMap.put("[睡]", R.drawable.d_shuijiao);
        mWeiBoEmoticonMap.put("[钱]", R.drawable.d_qian);
        mWeiBoEmoticonMap.put("[偷笑]", R.drawable.d_touxiao);
        mWeiBoEmoticonMap.put("[笑cry]", R.drawable.d_xiaoku);
        mWeiBoEmoticonMap.put("[doge]", R.drawable.d_doge);
        mWeiBoEmoticonMap.put("[喵喵]", R.drawable.d_miao);
        mWeiBoEmoticonMap.put("[酷]", R.drawable.d_ku);
        mWeiBoEmoticonMap.put("[衰]", R.drawable.d_shuai);
        mWeiBoEmoticonMap.put("[闭嘴]", R.drawable.d_bizui);
        mWeiBoEmoticonMap.put("[鄙视]", R.drawable.d_bishi);
        //花心->色
        mWeiBoEmoticonMap.put("[色]", R.drawable.d_huaxin);
        mWeiBoEmoticonMap.put("[鼓掌]", R.drawable.d_guzhang);
        mWeiBoEmoticonMap.put("[悲伤]", R.drawable.d_beishang);
        mWeiBoEmoticonMap.put("[思考]", R.drawable.d_sikao);
        mWeiBoEmoticonMap.put("[生病]", R.drawable.d_shengbing);
        mWeiBoEmoticonMap.put("[亲亲]", R.drawable.d_qinqin);
        mWeiBoEmoticonMap.put("[怒骂]", R.drawable.d_numa);
        mWeiBoEmoticonMap.put("[太开心]", R.drawable.d_taikaixin);
        mWeiBoEmoticonMap.put("[懒得理你]", R.drawable.d_landelini);
        mWeiBoEmoticonMap.put("[右哼哼]", R.drawable.d_youhengheng);
        mWeiBoEmoticonMap.put("[左哼哼]", R.drawable.d_zuohengheng);
        mWeiBoEmoticonMap.put("[嘘]", R.drawable.d_xu);
        mWeiBoEmoticonMap.put("[委屈]", R.drawable.d_weiqu);
        mWeiBoEmoticonMap.put("[吐]", R.drawable.d_tu);
        mWeiBoEmoticonMap.put("[可怜]", R.drawable.d_kelian);
        mWeiBoEmoticonMap.put("[打哈欠]", R.drawable.d_dahaqi);
        mWeiBoEmoticonMap.put("[挤眼]", R.drawable.d_jiyan);
        mWeiBoEmoticonMap.put("[失望]", R.drawable.d_shiwang);
        mWeiBoEmoticonMap.put("[顶]", R.drawable.d_ding);
        mWeiBoEmoticonMap.put("[疑问]", R.drawable.d_yiwen);
        mWeiBoEmoticonMap.put("[困]", R.drawable.d_kun);
        mWeiBoEmoticonMap.put("[感冒]", R.drawable.d_ganmao);
        mWeiBoEmoticonMap.put("[拜拜]", R.drawable.d_baibai);
        mWeiBoEmoticonMap.put("[黑线]", R.drawable.d_heixian);
        mWeiBoEmoticonMap.put("[阴险]", R.drawable.d_yinxian);
        mWeiBoEmoticonMap.put("[打脸]", R.drawable.d_dalian);
        mWeiBoEmoticonMap.put("[傻眼]", R.drawable.d_shayan);
        //----
//        mWeiBoEmoticonMap.put("[马到成功]", R.drawable.d_madaochenggong);
        mWeiBoEmoticonMap.put("[骷髅]", R.drawable.d_kulou);
        mWeiBoEmoticonMap.put("[坏笑]", R.drawable.d_huaixiao);
        //舔-舔屏
        mWeiBoEmoticonMap.put("[舔屏]", R.drawable.d_tian);
        mWeiBoEmoticonMap.put("[污]", R.drawable.d_wu);
        //-----
        mWeiBoEmoticonMap.put("[互粉]", R.drawable.f_hufen);
        mWeiBoEmoticonMap.put("[心]", R.drawable.l_xin);
        mWeiBoEmoticonMap.put("[伤心]", R.drawable.l_shangxin);
        mWeiBoEmoticonMap.put("[猪头]", R.drawable.d_zhutou);
        mWeiBoEmoticonMap.put("[熊猫]", R.drawable.d_xiongmao);
        mWeiBoEmoticonMap.put("[兔子]", R.drawable.d_tuzi);
        mWeiBoEmoticonMap.put("[握手]", R.drawable.h_woshou);
        mWeiBoEmoticonMap.put("[作揖]", R.drawable.h_zuoyi);
        mWeiBoEmoticonMap.put("[赞]", R.drawable.h_zan);
        mWeiBoEmoticonMap.put("[耶]", R.drawable.h_ye);
        mWeiBoEmoticonMap.put("[good]", R.drawable.h_good);
        mWeiBoEmoticonMap.put("[弱]", R.drawable.h_ruo);
        mWeiBoEmoticonMap.put("[NO]", R.drawable.h_buyao);
        mWeiBoEmoticonMap.put("[ok]", R.drawable.h_ok);
        mWeiBoEmoticonMap.put("[haha]", R.drawable.h_haha);
        mWeiBoEmoticonMap.put("[来]", R.drawable.h_lai);
        mWeiBoEmoticonMap.put("[拳头]", R.drawable.h_quantou);
        mWeiBoEmoticonMap.put("[加油]", R.drawable.h_jiayou);
        mWeiBoEmoticonMap.put("[威武]", R.drawable.f_v5);
        mWeiBoEmoticonMap.put("[鲜花]", R.drawable.w_xianhua);
        mWeiBoEmoticonMap.put("[钟]", R.drawable.o_zhong);
        mWeiBoEmoticonMap.put("[浮云]", R.drawable.w_fuyun);
        mWeiBoEmoticonMap.put("[飞机]", R.drawable.o_feiji);
        mWeiBoEmoticonMap.put("[月亮]", R.drawable.w_yueliang);
        mWeiBoEmoticonMap.put("[太阳]", R.drawable.w_taiyang);
        mWeiBoEmoticonMap.put("[微风]", R.drawable.w_weifeng);
        mWeiBoEmoticonMap.put("[下雨]", R.drawable.w_xiayu);
        mWeiBoEmoticonMap.put("[给力]", R.drawable.f_geili);
        mWeiBoEmoticonMap.put("[神马]", R.drawable.f_shenma);
        mWeiBoEmoticonMap.put("[围观]", R.drawable.o_weiguan);
        mWeiBoEmoticonMap.put("[话筒]", R.drawable.o_huatong);
        mWeiBoEmoticonMap.put("[奥特曼]", R.drawable.d_aoteman);
        mWeiBoEmoticonMap.put("[草泥马]", R.drawable.d_shenshou);
        mWeiBoEmoticonMap.put("[萌]", R.drawable.f_meng);
        mWeiBoEmoticonMap.put("[囧]", R.drawable.f_jiong);
        mWeiBoEmoticonMap.put("[织]", R.drawable.f_zhi);
        mWeiBoEmoticonMap.put("[礼物]", R.drawable.o_liwu);
        mWeiBoEmoticonMap.put("[囍]", R.drawable.f_xi);
        mWeiBoEmoticonMap.put("[围脖]", R.drawable.o_weibo);
        mWeiBoEmoticonMap.put("[音乐]", R.drawable.o_yinyue);
        mWeiBoEmoticonMap.put("[绿丝带]", R.drawable.o_lvsidai);
        mWeiBoEmoticonMap.put("[蛋糕]", R.drawable.o_dangao);
        mWeiBoEmoticonMap.put("[蜡烛]", R.drawable.o_lazhu);
        mWeiBoEmoticonMap.put("[干杯]", R.drawable.o_ganbei);
        mWeiBoEmoticonMap.put("[男孩儿]", R.drawable.d_nanhaier);
        mWeiBoEmoticonMap.put("[女孩儿]", R.drawable.d_nvhaier);
        mWeiBoEmoticonMap.put("[肥皂]", R.drawable.d_feizao);
        mWeiBoEmoticonMap.put("[照相机]", R.drawable.o_zhaoxiangji);
        mWeiBoEmoticonMap.put("[浪]", R.drawable.d_lang);
        mWeiBoEmoticonMap.put("[沙尘暴]", R.drawable.w_shachenbao);


        //浪小花表情
        mLxhEmoticonMap.put("[笑哈哈]", R.drawable.lxh_xiaohaha);
        mLxhEmoticonMap.put("[好爱哦]", R.drawable.lxh_haoaio);
        mLxhEmoticonMap.put("[噢耶]", R.drawable.lxh_oye);
        mLxhEmoticonMap.put("[偷乐]", R.drawable.lxh_toule);
        mLxhEmoticonMap.put("[泪流满面]", R.drawable.lxh_leiliumanmian);
        mLxhEmoticonMap.put("[巨汗]", R.drawable.lxh_juhan);
        mLxhEmoticonMap.put("[抠鼻屎]", R.drawable.lxh_koubishi);
        mLxhEmoticonMap.put("[求关注]", R.drawable.lxh_qiuguanzhu);
        mLxhEmoticonMap.put("[好喜欢]", R.drawable.lxh_haoxihuan);
        mLxhEmoticonMap.put("[崩溃]", R.drawable.lxh_bengkui);
        mLxhEmoticonMap.put("[好囧]", R.drawable.lxh_haojiong);
        mLxhEmoticonMap.put("[震惊]", R.drawable.lxh_zhenjing);
        mLxhEmoticonMap.put("[别烦我]", R.drawable.lxh_biefanwo);
        mLxhEmoticonMap.put("[不好意思]", R.drawable.lxh_buhaoyisi);
        mLxhEmoticonMap.put("[羞嗒嗒]", R.drawable.lxh_xiudada);
        mLxhEmoticonMap.put("[得意地笑]", R.drawable.lxh_deyidexiao);
        mLxhEmoticonMap.put("[纠结]", R.drawable.lxh_jiujie);
        mLxhEmoticonMap.put("[给劲]", R.drawable.lxh_feijin);
        mLxhEmoticonMap.put("[悲催]", R.drawable.lxh_beicui);
        mLxhEmoticonMap.put("[甩甩手]", R.drawable.lxh_shuaishuaishou);
        mLxhEmoticonMap.put("[好棒]", R.drawable.lxh_haobang);
        mLxhEmoticonMap.put("[瞧瞧]", R.drawable.lxh_qiaoqiao);
        mLxhEmoticonMap.put("[不想上班]", R.drawable.lxh_buxiangshangban);
        mLxhEmoticonMap.put("[困死了]", R.drawable.lxh_kunsile);
        mLxhEmoticonMap.put("[许愿]", R.drawable.lxh_xuyuan);
        mLxhEmoticonMap.put("[丘比特]", R.drawable.lxh_qiubite);
        mLxhEmoticonMap.put("[有鸭梨]", R.drawable.lxh_youyali);
        mLxhEmoticonMap.put("[想一想]", R.drawable.lxh_xiangyixiang);
        mLxhEmoticonMap.put("[躁狂症]", R.drawable.lxh_kuangzaozheng);
        mLxhEmoticonMap.put("[转发]", R.drawable.lxh_zhuanfa);
        mLxhEmoticonMap.put("[互相膜拜]", R.drawable.lxh_xianghumobai);
        mLxhEmoticonMap.put("[雷锋]", R.drawable.lxh_leifeng);
        mLxhEmoticonMap.put("[杰克逊]", R.drawable.lxh_jiekexun);
        mLxhEmoticonMap.put("[玫瑰]", R.drawable.lxh_meigui);
        mLxhEmoticonMap.put("[hold住]", R.drawable.lxh_holdzhu);
        mLxhEmoticonMap.put("[群体围观]", R.drawable.lxh_quntiweiguan);
        mLxhEmoticonMap.put("[推荐]", R.drawable.lxh_tuijian);
        mLxhEmoticonMap.put("[赞啊]", R.drawable.lxh_zana);
        mLxhEmoticonMap.put("[被电]", R.drawable.lxh_beidian);
        mLxhEmoticonMap.put("[霹雳]", R.drawable.lxh_pili);
    }

    static{
        //softbank 89个

        mSoftbankMap.put(0xe001, R.drawable.emoji_0x1f466);
        mSoftbankMap.put(0xe002, R.drawable.emoji_0x1f467);
        mSoftbankMap.put(0xe004, R.drawable.emoji_0x1f468);
        mSoftbankMap.put(0xe005, R.drawable.emoji_0x1f469);
        mSoftbankMap.put(0xe00a, R.drawable.emoji_0x1f4f1);
        mSoftbankMap.put(0xe00d, R.drawable.emoji_0x1f44a);
        mSoftbankMap.put(0xe00e, R.drawable.emoji_0x1f44d);
        mSoftbankMap.put(0xe010, R.drawable.emoji_0x270a);
        mSoftbankMap.put(0xe011, R.drawable.emoji_0x270c);
        mSoftbankMap.put(0xe012, R.drawable.emoji_0x1f64b);
        mSoftbankMap.put(0xe018, R.drawable.emoji_0x26bd);
        mSoftbankMap.put(0xe01b, R.drawable.emoji_0x1f697);
        mSoftbankMap.put(0xe022, R.drawable.emoji_0x2764);
        mSoftbankMap.put(0xe023, R.drawable.emoji_0x1f494);
        mSoftbankMap.put(0xe02b, R.drawable.emoji_0x1f557);
        mSoftbankMap.put(0xe02d, R.drawable.emoji_0x1f559);
        mSoftbankMap.put(0xe032, R.drawable.emoji_0x1f339);
        mSoftbankMap.put(0xe033, R.drawable.emoji_0x1f384);
        mSoftbankMap.put(0xe035, R.drawable.emoji_0x1f48e);
        mSoftbankMap.put(0xe036, R.drawable.emoji_0x1f3e0);
        mSoftbankMap.put(0xe045, R.drawable.emoji_0x2615);
        mSoftbankMap.put(0xe047, R.drawable.emoji_0x1f37a);
        mSoftbankMap.put(0xe049, R.drawable.emoji_0x2601);
        mSoftbankMap.put(0xe04a, R.drawable.emoji_0x2600);
        mSoftbankMap.put(0xe04b, R.drawable.emoji_0x2614);
        mSoftbankMap.put(0xe04f, R.drawable.emoji_0x1f431);
        mSoftbankMap.put(0xe056, R.drawable.emoji_0x1f60a);
        mSoftbankMap.put(0xe057, R.drawable.emoji_0x1f603);
        mSoftbankMap.put(0xe058, R.drawable.emoji_0x1f61e);
        mSoftbankMap.put(0xe059, R.drawable.emoji_0x1f620);
        mSoftbankMap.put(0xe05a, R.drawable.emoji_0x1f4a9);
        mSoftbankMap.put(0xe105, R.drawable.emoji_0x1f61c);
        mSoftbankMap.put(0xe106, R.drawable.emoji_0x1f60d);
        mSoftbankMap.put(0xe107, R.drawable.emoji_0x1f631);
        mSoftbankMap.put(0xe108, R.drawable.emoji_0x1f613);
        mSoftbankMap.put(0xe111, R.drawable.emoji_0x1f48f);
        mSoftbankMap.put(0xe112, R.drawable.emoji_0x1f381);
        mSoftbankMap.put(0xe114, R.drawable.emoji_0x1f50d);
        mSoftbankMap.put(0xe118, R.drawable.emoji_0x1f341);
        mSoftbankMap.put(0xe11a, R.drawable.emoji_0x1f47f);
        mSoftbankMap.put(0xe11b, R.drawable.emoji_0x1f47b);
        mSoftbankMap.put(0xe13d, R.drawable.emoji_0x26a1);
        mSoftbankMap.put(0xe14c, R.drawable.emoji_0x1f4aa);
        mSoftbankMap.put(0xe22e, R.drawable.emoji_0x1f446);
        mSoftbankMap.put(0xe22f, R.drawable.emoji_0x1f447);
        mSoftbankMap.put(0xe230, R.drawable.emoji_0x1f448);
        mSoftbankMap.put(0xe231, R.drawable.emoji_0x1f449);
        mSoftbankMap.put(0xe305, R.drawable.emoji_0x1f33b);
        mSoftbankMap.put(0xe30c, R.drawable.emoji_0x1f37b);
        mSoftbankMap.put(0xe30f, R.drawable.emoji_0x1f48a);
        mSoftbankMap.put(0xe311, R.drawable.emoji_0x1f4a3);
        mSoftbankMap.put(0xe314, R.drawable.emoji_0x1f380);
        mSoftbankMap.put(0xe319, R.drawable.emoji_0x1f457);
        mSoftbankMap.put(0xe335, R.drawable.emoji_0x1f31f);
        mSoftbankMap.put(0xe340, R.drawable.emoji_0x1f35c);
        mSoftbankMap.put(0xe345, R.drawable.emoji_0x1f34e);
        mSoftbankMap.put(0xe347, R.drawable.emoji_0x1f353);
        mSoftbankMap.put(0xe348, R.drawable.emoji_0x1f349);
        mSoftbankMap.put(0xe34b, R.drawable.emoji_0x1f382);
        mSoftbankMap.put(0xe401, R.drawable.emoji_0x1f625);
        mSoftbankMap.put(0xe402, R.drawable.emoji_0x1f60f);
        mSoftbankMap.put(0xe403, R.drawable.emoji_0x1f614);
        mSoftbankMap.put(0xe404, R.drawable.emoji_0x1f601);
        mSoftbankMap.put(0xe405, R.drawable.emoji_0x1f609);
        mSoftbankMap.put(0xe406, R.drawable.emoji_0x1f623);
        mSoftbankMap.put(0xe407, R.drawable.emoji_0x1f616);
        mSoftbankMap.put(0xe408, R.drawable.emoji_0x1f62a);
        mSoftbankMap.put(0xe40a, R.drawable.emoji_0x1f606);
        mSoftbankMap.put(0xe40b, R.drawable.emoji_0x1f628);
        mSoftbankMap.put(0xe40c, R.drawable.emoji_0x1f637);
        mSoftbankMap.put(0xe40d, R.drawable.emoji_0x1f633);
        mSoftbankMap.put(0xe40e, R.drawable.emoji_0x1f612);
        mSoftbankMap.put(0xe40f, R.drawable.emoji_0x1f630);
        mSoftbankMap.put(0xe410, R.drawable.emoji_0x1f632);
        mSoftbankMap.put(0xe411, R.drawable.emoji_0x1f62d);
        mSoftbankMap.put(0xe412, R.drawable.emoji_0x1f602);
        mSoftbankMap.put(0xe413, R.drawable.emoji_0x1f622);
        mSoftbankMap.put(0xe415, R.drawable.emoji_0x1f605);
        mSoftbankMap.put(0xe416, R.drawable.emoji_0x1f621);
        mSoftbankMap.put(0xe417, R.drawable.emoji_0x1f61a);
        mSoftbankMap.put(0xe418, R.drawable.emoji_0x1f618);
        mSoftbankMap.put(0xe41c, R.drawable.emoji_0x1f444);
        mSoftbankMap.put(0xe41d, R.drawable.emoji_0x1f64f);
        mSoftbankMap.put(0xe41f, R.drawable.emoji_0x1f44f);
        mSoftbankMap.put(0xe420, R.drawable.emoji_0x1f44c);
        mSoftbankMap.put(0xe421, R.drawable.emoji_0x1f44e);
        mSoftbankMap.put(0xe423, R.drawable.emoji_0x1f645);
        mSoftbankMap.put(0xe447, R.drawable.emoji_0x1f343);
        mSoftbankMap.put(0xe448, R.drawable.emoji_0x1f385);

        //unicode 101个

        mEmojiMap.put(0x1f604, R.drawable.emoji_0x1f604);
        mEmojiMap.put(0x1f603, R.drawable.emoji_0x1f603);
        mEmojiMap.put(0x1f60a, R.drawable.emoji_0x1f60a);
        mEmojiMap.put(0x1f609, R.drawable.emoji_0x1f609);
        mEmojiMap.put(0x1f60d, R.drawable.emoji_0x1f60d);
        mEmojiMap.put(0x1f618, R.drawable.emoji_0x1f618);
        mEmojiMap.put(0x1f61a, R.drawable.emoji_0x1f61a);
        mEmojiMap.put(0x1f61c, R.drawable.emoji_0x1f61c);
        mEmojiMap.put(0x1f61d, R.drawable.emoji_0x1f61d);
        mEmojiMap.put(0x1f633, R.drawable.emoji_0x1f633);
        mEmojiMap.put(0x1f601, R.drawable.emoji_0x1f601);
        mEmojiMap.put(0x1f614, R.drawable.emoji_0x1f614);
        mEmojiMap.put(0x1f60c, R.drawable.emoji_0x1f60c);
        mEmojiMap.put(0x1f612, R.drawable.emoji_0x1f612);
        mEmojiMap.put(0x1f61e, R.drawable.emoji_0x1f61e);
        mEmojiMap.put(0x1f623, R.drawable.emoji_0x1f623);
        mEmojiMap.put(0x1f913, R.drawable.emoji_0x1f913);
        mEmojiMap.put(0x1f914, R.drawable.emoji_0x1f914);
        mEmojiMap.put(0x1f917, R.drawable.emoji_0x1f917);
        mEmojiMap.put(0x1f622, R.drawable.emoji_0x1f622);
        mEmojiMap.put(0x1f602, R.drawable.emoji_0x1f602);
        mEmojiMap.put(0x1f62d, R.drawable.emoji_0x1f62d);
        mEmojiMap.put(0x1f62a, R.drawable.emoji_0x1f62a);
        mEmojiMap.put(0x1f625, R.drawable.emoji_0x1f625);
        mEmojiMap.put(0x1f630, R.drawable.emoji_0x1f630);
        mEmojiMap.put(0x1f605, R.drawable.emoji_0x1f605);
        mEmojiMap.put(0x1f613, R.drawable.emoji_0x1f613);
        mEmojiMap.put(0x1f628, R.drawable.emoji_0x1f628);
        mEmojiMap.put(0x1f631, R.drawable.emoji_0x1f631);
        mEmojiMap.put(0x1f620, R.drawable.emoji_0x1f620);
        mEmojiMap.put(0x1f621, R.drawable.emoji_0x1f621);
        mEmojiMap.put(0x1f616, R.drawable.emoji_0x1f616);
        mEmojiMap.put(0x1f606, R.drawable.emoji_0x1f606);
        mEmojiMap.put(0x1f60b, R.drawable.emoji_0x1f60b);
        mEmojiMap.put(0x1f637, R.drawable.emoji_0x1f637);
        mEmojiMap.put(0x1f60e, R.drawable.emoji_0x1f60e);
        mEmojiMap.put(0x1f635, R.drawable.emoji_0x1f635);
        mEmojiMap.put(0x1f632, R.drawable.emoji_0x1f632);
        mEmojiMap.put(0x1f47f, R.drawable.emoji_0x1f47f);
        mEmojiMap.put(0x1f60f, R.drawable.emoji_0x1f60f);
        mEmojiMap.put(0x1f466, R.drawable.emoji_0x1f466);
        mEmojiMap.put(0x1f467, R.drawable.emoji_0x1f467);
        mEmojiMap.put(0x1f468, R.drawable.emoji_0x1f468);
        mEmojiMap.put(0x1f469, R.drawable.emoji_0x1f469);

        mEmojiMap.put(0x1f641, R.drawable.emoji_0x1f641);
        mEmojiMap.put(0x1f643, R.drawable.emoji_0x1f643);
        mEmojiMap.put(0x1f644, R.drawable.emoji_0x1f644);
        mEmojiMap.put(0x1f648, R.drawable.emoji_0x1f648);
        mEmojiMap.put(0x1f649, R.drawable.emoji_0x1f649);
        mEmojiMap.put(0x1f64a, R.drawable.emoji_0x1f64a);
        mEmojiMap.put(0x1f4a9, R.drawable.emoji_0x1f4a9);
        mEmojiMap.put(0x1f31f, R.drawable.emoji_0x1f31f);
        mEmojiMap.put(0x1f444, R.drawable.emoji_0x1f444);
        mEmojiMap.put(0x1f44d, R.drawable.emoji_0x1f44d);
        mEmojiMap.put(0x1f44e, R.drawable.emoji_0x1f44e);
        mEmojiMap.put(0x1f44c, R.drawable.emoji_0x1f44c);
        mEmojiMap.put(0x1f44a, R.drawable.emoji_0x1f44a);
        mEmojiMap.put(0x270a, R.drawable.emoji_0x270a);
        mEmojiMap.put(0x270c, R.drawable.emoji_0x270c);
        mEmojiMap.put(0x1f446, R.drawable.emoji_0x1f446);
        mEmojiMap.put(0x1f447, R.drawable.emoji_0x1f447);
        mEmojiMap.put(0x1f449, R.drawable.emoji_0x1f449);
        mEmojiMap.put(0x1f448, R.drawable.emoji_0x1f448);
        mEmojiMap.put(0x1f64f, R.drawable.emoji_0x1f64f);
        mEmojiMap.put(0x1f44f, R.drawable.emoji_0x1f44f);
        mEmojiMap.put(0x1f4aa, R.drawable.emoji_0x1f4aa);
        mEmojiMap.put(0x1f48f, R.drawable.emoji_0x1f48f);
        mEmojiMap.put(0x1f645, R.drawable.emoji_0x1f645);
        mEmojiMap.put(0x1f64b, R.drawable.emoji_0x1f64b);
        mEmojiMap.put(0x1f457, R.drawable.emoji_0x1f457);
        mEmojiMap.put(0x1f380, R.drawable.emoji_0x1f380);
        mEmojiMap.put(0x2764, R.drawable.emoji_0x2764);
        mEmojiMap.put(0x1f494, R.drawable.emoji_0x1f494);
        mEmojiMap.put(0x1f48e, R.drawable.emoji_0x1f48e);
        mEmojiMap.put(0x1f436, R.drawable.emoji_0x1f436);
        mEmojiMap.put(0x1f431, R.drawable.emoji_0x1f431);
        mEmojiMap.put(0x1f339, R.drawable.emoji_0x1f339);
        mEmojiMap.put(0x1f33b, R.drawable.emoji_0x1f33b);
        mEmojiMap.put(0x1f341, R.drawable.emoji_0x1f341);
        mEmojiMap.put(0x1f343, R.drawable.emoji_0x1f343);
        mEmojiMap.put(0x1f319, R.drawable.emoji_0x1f319);
        mEmojiMap.put(0x2600, R.drawable.emoji_0x2600);
        mEmojiMap.put(0x2601, R.drawable.emoji_0x2601);
        mEmojiMap.put(0x26a1, R.drawable.emoji_0x26a1);
        mEmojiMap.put(0x2614, R.drawable.emoji_0x2614);
        mEmojiMap.put(0x1f47b, R.drawable.emoji_0x1f47b);
        mEmojiMap.put(0x1f385, R.drawable.emoji_0x1f385);
        mEmojiMap.put(0x1f384, R.drawable.emoji_0x1f384);
        mEmojiMap.put(0x1f381, R.drawable.emoji_0x1f381);
        mEmojiMap.put(0x1f4f1, R.drawable.emoji_0x1f4f1);
        mEmojiMap.put(0x1f50d, R.drawable.emoji_0x1f50d);
        mEmojiMap.put(0x1f4a3, R.drawable.emoji_0x1f4a3);
        mEmojiMap.put(0x1f48a, R.drawable.emoji_0x1f48a);
        mEmojiMap.put(0x26bd, R.drawable.emoji_0x26bd);
        mEmojiMap.put(0x2615, R.drawable.emoji_0x2615);
        mEmojiMap.put(0x1f37a, R.drawable.emoji_0x1f37a);
        mEmojiMap.put(0x1f37b, R.drawable.emoji_0x1f37b);
        mEmojiMap.put(0x1f357, R.drawable.emoji_0x1f357);
        mEmojiMap.put(0x1f35c, R.drawable.emoji_0x1f35c);
        mEmojiMap.put(0x1f382, R.drawable.emoji_0x1f382);
        mEmojiMap.put(0x1f34e, R.drawable.emoji_0x1f34e);
        mEmojiMap.put(0x1f349, R.drawable.emoji_0x1f349);
        mEmojiMap.put(0x1f353, R.drawable.emoji_0x1f353);
        mEmojiMap.put(0x1f3e0, R.drawable.emoji_0x1f3e0);
        mEmojiMap.put(0x1f697, R.drawable.emoji_0x1f697);
        mEmojiMap.put(0x1f557, R.drawable.emoji_0x1f557);
        mEmojiMap.put(0x1f559, R.drawable.emoji_0x1f559);
    }
}