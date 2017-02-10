package com.hengye.share.model.sina;

import java.util.List;

/**
 * Created by yuhy on 2017/2/10.
 * 微博热门话题
 */

public class WBHotTopic {

    private List<TopicsBean> topics;

    public List<TopicsBean> getTopics() {
        return topics;
    }

    public void setTopics(List<TopicsBean> topics) {
        this.topics = topics;
    }

    public static class TopicsBean {
        /**
         * card_type_name : 血液透析室疑乙肝感染
         * container_id : 100808751c9b4597704d7afd8deb8385654121
         * pic : https://wx2.sinaimg.cn/thumbnail/bdb42105ly1fcka4vtyc9j2050050dfx.jpg
         * desc1 : 据微博@丁香园 信息，2017 年 1 月，山东省卫计委通报，山东省某三级综合医院通报一起血液透析室疑似乙肝感染爆发事件。经调查，初步判定是一起因院感管理不到位导致的严重医院感染事件。
         * desc2 : 4986讨论　876.7万阅读
         * scheme : sinaweibo://pageinfo?containerid=100808751c9b4597704d7afd8deb8385654121&extparam=%E8%A1%80%E6%B6%B2%E9%80%8F%E6%9E%90%E5%AE%A4%E7%96%91%E4%B9%99%E8%82%9D%E6%84%9F%E6%9F%93
         */

        private String card_type_name;
        private String container_id;
        private String pic;
        private String desc1;
        private String desc2;
        private String scheme;

        public String getCard_type_name() {
            return card_type_name;
        }

        public void setCard_type_name(String card_type_name) {
            this.card_type_name = card_type_name;
        }

        public String getContainer_id() {
            return container_id;
        }

        public void setContainer_id(String container_id) {
            this.container_id = container_id;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getDesc1() {
            return desc1;
        }

        public void setDesc1(String desc1) {
            this.desc1 = desc1;
        }

        public String getDesc2() {
            return desc2;
        }

        public void setDesc2(String desc2) {
            this.desc2 = desc2;
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }
    }
}
