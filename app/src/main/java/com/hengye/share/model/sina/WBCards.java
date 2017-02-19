package com.hengye.share.model.sina;

import com.hengye.share.util.CommonUtil;
import com.hengye.share.util.thirdparty.WBUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhy on 2017/2/9.
 */

public class WBCards {

    public static List<WBStatus> listWBStatuses(WBCards wbCards) {
        if (wbCards != null && !CommonUtil.isEmpty(wbCards.getCards())) {
            List<CardsBean> cards = wbCards.getCards();
            List<WBStatus> topics = new ArrayList<>();
            for (CardsBean cardsBean : cards) {
                if (cardsBean != null && cardsBean.getMblog() != null) {
                    topics.add(getWBStatus(cardsBean.getMblog()));
                }
            }
            return topics;
        }
        return null;
    }

    public static WBStatus getWBStatus(CardsBean.MBlog mblog) {
        if (mblog == null) {
            return null;
        }

        WBStatus wbStatus = new WBStatus();

        wbStatus.setCreated_at(mblog.getCreated_at());
        wbStatus.setIdstr(mblog.getId());
        wbStatus.setText(mblog.getText());
        wbStatus.setSource(mblog.getSource());
        wbStatus.setFavorited(mblog.isFavorited());
        wbStatus.setLiked(mblog.isLiked());
        wbStatus.setLongText(mblog.isLongText());

        WBUserInfo wbUserInfo = mblog.getUser();
        if(wbUserInfo.getIdstr() == null){
            wbUserInfo.setIdstr(String.valueOf(wbUserInfo.getId()));
        }
        if(wbUserInfo.getAvatar_large() == null){
            wbUserInfo.setAvatar_large(wbUserInfo.getProfile_image_url());
        }

        wbUserInfo.setFriends_count(wbUserInfo.getFollow_count());

        wbStatus.setUser(mblog.getUser());
        wbStatus.setReposts_count(mblog.getReposts_count());
        wbStatus.setComments_count(mblog.getComments_count());
        wbStatus.setAttitudes_count(mblog.getAttitudes_count());
        wbStatus.setFromMobile(true);

        //解析图片
        if (!CommonUtil.isEmpty(mblog.getPics())) {
            List<WBStatus.Pic_urlsEntity> pic_urls = new ArrayList<>();
            for (CardsBean.MBlog.Pics pics : mblog.getPics()) {
                WBStatus.Pic_urlsEntity pic_urlsEntity = new WBStatus.Pic_urlsEntity();
                pic_urlsEntity.setThumbnail_pic(WBUtil.getWBImgUrl(pics.getUrl(), pics.getSize(), WBUtil.IMAGE_TYPE_OR_480));
                pic_urls.add(pic_urlsEntity);
            }
            wbStatus.setPic_urls(pic_urls);
        }
        return wbStatus;
    }

    /**
     * cardlistInfo : {"v_p":"42","statistics_from":"hotweibo","containerid":"102803","title_top":"热门微博","show_style":1,"total":300,"can_shared":1,"since_id":1,"cardlist_title":"","desc":""}
     * ok : 1
     * seeLevel : 3
     * showAppTips : 0
     * scheme : sinaweibo://cardlist?containerid=102803&luicode=10000011&lfid=102803&featurecode=
     */

    private CardlistInfoBean cardlistInfo;
    private int ok;
    private int seeLevel;
    private int showAppTips;
    private String scheme;
    private List<CardsBean> cards;

    public CardlistInfoBean getCardlistInfo() {
        return cardlistInfo;
    }

    public void setCardlistInfo(CardlistInfoBean cardlistInfo) {
        this.cardlistInfo = cardlistInfo;
    }

    public int getOk() {
        return ok;
    }

    public void setOk(int ok) {
        this.ok = ok;
    }

    public int getSeeLevel() {
        return seeLevel;
    }

    public void setSeeLevel(int seeLevel) {
        this.seeLevel = seeLevel;
    }

    public int getShowAppTips() {
        return showAppTips;
    }

    public void setShowAppTips(int showAppTips) {
        this.showAppTips = showAppTips;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public List<CardsBean> getCards() {
        return cards;
    }

    public void setCards(List<CardsBean> cards) {
        this.cards = cards;
    }

    public static class CardlistInfoBean {
        /**
         * v_p : 42
         * statistics_from : hotweibo
         * containerid : 102803
         * title_top : 热门微博
         * show_style : 1
         * total : 300
         * can_shared : 1
         * since_id : 1
         * cardlist_title :
         * desc :
         */

        private String v_p;
        private String statistics_from;
        private String containerid;
        private String title_top;
        private int show_style;
        private int total;
        private int can_shared;
        private int since_id;
        private String cardlist_title;
        private String desc;

        public String getV_p() {
            return v_p;
        }

        public void setV_p(String v_p) {
            this.v_p = v_p;
        }

        public String getStatistics_from() {
            return statistics_from;
        }

        public void setStatistics_from(String statistics_from) {
            this.statistics_from = statistics_from;
        }

        public String getContainerid() {
            return containerid;
        }

        public void setContainerid(String containerid) {
            this.containerid = containerid;
        }

        public String getTitle_top() {
            return title_top;
        }

        public void setTitle_top(String title_top) {
            this.title_top = title_top;
        }

        public int getShow_style() {
            return show_style;
        }

        public void setShow_style(int show_style) {
            this.show_style = show_style;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getCan_shared() {
            return can_shared;
        }

        public void setCan_shared(int can_shared) {
            this.can_shared = can_shared;
        }

        public int getSince_id() {
            return since_id;
        }

        public void setSince_id(int since_id) {
            this.since_id = since_id;
        }

        public String getCardlist_title() {
            return cardlist_title;
        }

        public void setCardlist_title(String cardlist_title) {
            this.cardlist_title = cardlist_title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public static class CardsBean {

        /**
         * card_type : 9
         * itemid : 102803_-_mbloglist_4072994991551480
         * scheme : http://m.weibo.cn/status/4072994991551480?mblogid=4072994991551480&luicode=10000011&lfid=102803
         * weibo_need : mblog
         * show_type : 1
         * openurl :
         */

        private int card_type;
        private String itemid;
        private String scheme;
        private String weibo_need;
        private int show_type;
        private String openurl;
        private MBlog mblog;

        public int getCard_type() {
            return card_type;
        }

        public void setCard_type(int card_type) {
            this.card_type = card_type;
        }

        public String getItemid() {
            return itemid;
        }

        public void setItemid(String itemid) {
            this.itemid = itemid;
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        public String getWeibo_need() {
            return weibo_need;
        }

        public void setWeibo_need(String weibo_need) {
            this.weibo_need = weibo_need;
        }

        public int getShow_type() {
            return show_type;
        }

        public void setShow_type(int show_type) {
            this.show_type = show_type;
        }

        public String getOpenurl() {
            return openurl;
        }

        public void setOpenurl(String openurl) {
            this.openurl = openurl;
        }

        public MBlog getMblog() {
            return mblog;
        }

        public void setMblog(MBlog mblog) {
            this.mblog = mblog;
        }

        public static class MBlog {

            /**
             * created_at : 今天 16:20
             * id : 4073277108353952
             * text : 【学生撞宝马后留道歉信 车主寻肇事学生欲资助】近日，河南新密一学生骑车不慎撞上一停在路边的宝马，致宝马倒车镜损坏，多处划伤。他给车主写了一封道歉信，并用信包着打工挣来的311元，粘在车前门的把手内侧。车主被其真诚感动，希望找到该学生将钱还给他，并资助其完成学业。（大河报） ​​​...<a href="/status/4073277108353952">全文</a>
             * textLength : 287
             * source : 微博 weibo.com
             * favorited : false
             * thumbnail_pic : http://wx1.sinaimg.cn/thumbnail/75b1a75fly1fckadk7136j20dw09975c.jpg
             * bmiddle_pic : http://wx1.sinaimg.cn/bmiddle/75b1a75fly1fckadk7136j20dw09975c.jpg
             * original_pic : http://wx1.sinaimg.cn/large/75b1a75fly1fckadk7136j20dw09975c.jpg
             * reposts_count : 675
             * comments_count : 1155
             * attitudes_count : 7301
             * isLongText : true
             */

            private String created_at;
            private String id;
            private String text;
            private int textLength;
            private String source;
            private boolean favorited;
            private boolean liked;
            private String thumbnail_pic;
            private String bmiddle_pic;
            private String original_pic;
            private int reposts_count;
            private int comments_count;
            private int attitudes_count;
            private boolean isLongText;
            private WBUserInfo user;
            private List<Pics> pics;

            public String getCreated_at() {
                return created_at;
            }

            public void setCreated_at(String created_at) {
                this.created_at = created_at;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getText() {
                return text;
            }

            public void setText(String text) {
                this.text = text;
            }

            public int getTextLength() {
                return textLength;
            }

            public void setTextLength(int textLength) {
                this.textLength = textLength;
            }

            public String getSource() {
                return source;
            }

            public void setSource(String source) {
                this.source = source;
            }

            public boolean isFavorited() {
                return favorited;
            }

            public void setFavorited(boolean favorited) {
                this.favorited = favorited;
            }

            public boolean isLiked() {
                return liked;
            }

            public void setLiked(boolean liked) {
                this.liked = liked;
            }

            public String getThumbnail_pic() {
                return thumbnail_pic;
            }

            public void setThumbnail_pic(String thumbnail_pic) {
                this.thumbnail_pic = thumbnail_pic;
            }

            public String getBmiddle_pic() {
                return bmiddle_pic;
            }

            public void setBmiddle_pic(String bmiddle_pic) {
                this.bmiddle_pic = bmiddle_pic;
            }

            public String getOriginal_pic() {
                return original_pic;
            }

            public void setOriginal_pic(String original_pic) {
                this.original_pic = original_pic;
            }

            public int getReposts_count() {
                return reposts_count;
            }

            public void setReposts_count(int reposts_count) {
                this.reposts_count = reposts_count;
            }

            public int getComments_count() {
                return comments_count;
            }

            public void setComments_count(int comments_count) {
                this.comments_count = comments_count;
            }

            public int getAttitudes_count() {
                return attitudes_count;
            }

            public void setAttitudes_count(int attitudes_count) {
                this.attitudes_count = attitudes_count;
            }

            public boolean isLongText() {
                return isLongText;
            }

            public void setIsLongText(boolean isLongText) {
                this.isLongText = isLongText;
            }

            public List<Pics> getPics() {
                return pics;
            }

            public void setPics(List<Pics> pics) {
                this.pics = pics;
            }

            public WBUserInfo getUser() {
                return user;
            }

            public void setUser(WBUserInfo user) {
                this.user = user;
            }

            public static class Pics {

                /**
                 * pid : 75b1a75fly1fckadk7136j20dw09975c
                 * url : http://wx1.sinaimg.cn/orj360/75b1a75fly1fckadk7136j20dw09975c.jpg
                 * size : orj360
                 * geo : {"width":405,"height":270,"croped":false}
                 * large : {"size":"large","url":"http://wx1.sinaimg.cn/large/75b1a75fly1fckadk7136j20dw09975c.jpg","geo":{"width":"500","height":"333","croped":false}}
                 */

                private String pid;
                private String url;
                private String size;

                public String getPid() {
                    return pid;
                }

                public void setPid(String pid) {
                    this.pid = pid;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getSize() {
                    return size;
                }

                public void setSize(String size) {
                    this.size = size;
                }
            }
        }
    }
}
