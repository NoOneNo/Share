package com.hengye.share.model.sina;

import java.util.List;

/**
 * Created by yuhy on 2016/11/4.
 */

public class WBShortUrl {

    /**
     * result : true
     * last_modified : 1478169659
     * title :
     * description :
     * url_short : http://t.cn/RVeQycz
     * annotations : [{"timestamp":1478169659532,"object_type":"webpage","activate_status":"0","last_modified":"Thu Nov 03 18:40:59 CST 2016","safe_status":1,"containerid":"","object_id":"2026736001:comos:fxxnety7248984","act_status":"00","object":{"summary":"三星此举是在环保机构绿色和平组织(Greenpeace)发出警告之后做出的。绿色和平组织本周一发表声明称，三星不要随意丢弃已召回的Note 7手机，以免造成\u201c环境灾害\u201d。","id":"2026736001:comos:fxxnety7248984","object_type":"webpage","display_name":"三星:降低Note 7的环境影响","image":{"height":423,"width":630,"url":"http://n.sinaimg.cn/tech/transform/20161103/6dju-fxxnety7248684.jpg"},"url_oid_bind":0,"target_url":"http://tech.sina.com.cn/t/2016-11-03/doc-ifxxnety7248984.shtml","biz":{"biz_id":"","containerid":""},"url":"http://tech.sina.com.cn/t/2016-11-03/doc-ifxxnety7248984.shtml"},"uuid":4037798565813549,"show_status":"11","object_domain_id":"2026736001"}]
     * url_long : http://tech.sina.com.cn/t/2016-11-03/doc-ifxxnety7248984.shtml
     * type : 39
     * transcode : 2
     */

    private boolean result;
    private int last_modified;
    private String title;
    private String description;
    private String url_short;
    private String url_long;
    private int type;
    private int transcode;
    /**
     * timestamp : 1478169659532
     * object_type : webpage
     * activate_status : 0
     * last_modified : Thu Nov 03 18:40:59 CST 2016
     * safe_status : 1
     * containerid :
     * object_id : 2026736001:comos:fxxnety7248984
     * act_status : 00
     * object : {"summary":"三星此举是在环保机构绿色和平组织(Greenpeace)发出警告之后做出的。绿色和平组织本周一发表声明称，三星不要随意丢弃已召回的Note 7手机，以免造成\u201c环境灾害\u201d。","id":"2026736001:comos:fxxnety7248984","object_type":"webpage","display_name":"三星:降低Note 7的环境影响","image":{"height":423,"width":630,"url":"http://n.sinaimg.cn/tech/transform/20161103/6dju-fxxnety7248684.jpg"},"url_oid_bind":0,"target_url":"http://tech.sina.com.cn/t/2016-11-03/doc-ifxxnety7248984.shtml","biz":{"biz_id":"","containerid":""},"url":"http://tech.sina.com.cn/t/2016-11-03/doc-ifxxnety7248984.shtml"}
     * uuid : 4037798565813549
     * show_status : 11
     * object_domain_id : 2026736001
     */

    private List<AnnotationsBean> annotations;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getLast_modified() {
        return last_modified;
    }

    public void setLast_modified(int last_modified) {
        this.last_modified = last_modified;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl_short() {
        return url_short;
    }

    public void setUrl_short(String url_short) {
        this.url_short = url_short;
    }

    public String getUrl_long() {
        return url_long;
    }

    public void setUrl_long(String url_long) {
        this.url_long = url_long;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTranscode() {
        return transcode;
    }

    public void setTranscode(int transcode) {
        this.transcode = transcode;
    }

    public List<AnnotationsBean> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<AnnotationsBean> annotations) {
        this.annotations = annotations;
    }

    public static class AnnotationsBean {
        private long timestamp;
        private String object_type;
        private String activate_status;
        private String last_modified;
        private int safe_status;
        private String containerid;
        private String object_id;
        private String act_status;
        /**
         * summary : 三星此举是在环保机构绿色和平组织(Greenpeace)发出警告之后做出的。绿色和平组织本周一发表声明称，三星不要随意丢弃已召回的Note 7手机，以免造成“环境灾害”。
         * id : 2026736001:comos:fxxnety7248984
         * object_type : webpage
         * display_name : 三星:降低Note 7的环境影响
         * image : {"height":423,"width":630,"url":"http://n.sinaimg.cn/tech/transform/20161103/6dju-fxxnety7248684.jpg"}
         * url_oid_bind : 0
         * target_url : http://tech.sina.com.cn/t/2016-11-03/doc-ifxxnety7248984.shtml
         * biz : {"biz_id":"","containerid":""}
         * url : http://tech.sina.com.cn/t/2016-11-03/doc-ifxxnety7248984.shtml
         */

        private ObjectBean object;
        private long uuid;
        private String show_status;
        private String object_domain_id;

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getObject_type() {
            return object_type;
        }

        public void setObject_type(String object_type) {
            this.object_type = object_type;
        }

        public String getActivate_status() {
            return activate_status;
        }

        public void setActivate_status(String activate_status) {
            this.activate_status = activate_status;
        }

        public String getLast_modified() {
            return last_modified;
        }

        public void setLast_modified(String last_modified) {
            this.last_modified = last_modified;
        }

        public int getSafe_status() {
            return safe_status;
        }

        public void setSafe_status(int safe_status) {
            this.safe_status = safe_status;
        }

        public String getContainerid() {
            return containerid;
        }

        public void setContainerid(String containerid) {
            this.containerid = containerid;
        }

        public String getObject_id() {
            return object_id;
        }

        public void setObject_id(String object_id) {
            this.object_id = object_id;
        }

        public String getAct_status() {
            return act_status;
        }

        public void setAct_status(String act_status) {
            this.act_status = act_status;
        }

        public ObjectBean getObject() {
            return object;
        }

        public void setObject(ObjectBean object) {
            this.object = object;
        }

        public long getUuid() {
            return uuid;
        }

        public void setUuid(long uuid) {
            this.uuid = uuid;
        }

        public String getShow_status() {
            return show_status;
        }

        public void setShow_status(String show_status) {
            this.show_status = show_status;
        }

        public String getObject_domain_id() {
            return object_domain_id;
        }

        public void setObject_domain_id(String object_domain_id) {
            this.object_domain_id = object_domain_id;
        }

        public static class ObjectBean {
            private String summary;
            private String id;
            private String object_type;
            private String display_name;
            /**
             * height : 423
             * width : 630
             * url : http://n.sinaimg.cn/tech/transform/20161103/6dju-fxxnety7248684.jpg
             */

            private ImageBean image;
            private int url_oid_bind;
            private String target_url;
            /**
             * biz_id :
             * containerid :
             */

            private BizBean biz;
            private String url;

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getObject_type() {
                return object_type;
            }

            public void setObject_type(String object_type) {
                this.object_type = object_type;
            }

            public String getDisplay_name() {
                return display_name;
            }

            public void setDisplay_name(String display_name) {
                this.display_name = display_name;
            }

            public ImageBean getImage() {
                return image;
            }

            public void setImage(ImageBean image) {
                this.image = image;
            }

            public int getUrl_oid_bind() {
                return url_oid_bind;
            }

            public void setUrl_oid_bind(int url_oid_bind) {
                this.url_oid_bind = url_oid_bind;
            }

            public String getTarget_url() {
                return target_url;
            }

            public void setTarget_url(String target_url) {
                this.target_url = target_url;
            }

            public BizBean getBiz() {
                return biz;
            }

            public void setBiz(BizBean biz) {
                this.biz = biz;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public static class ImageBean {
                private int height;
                private int width;
                private String url;

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }
            }

            public static class BizBean {
                private String biz_id;
                private String containerid;

                public String getBiz_id() {
                    return biz_id;
                }

                public void setBiz_id(String biz_id) {
                    this.biz_id = biz_id;
                }

                public String getContainerid() {
                    return containerid;
                }

                public void setContainerid(String containerid) {
                    this.containerid = containerid;
                }
            }
        }
    }
}
