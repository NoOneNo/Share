package com.hengye.share.model.sina;

import java.util.List;

/**
 * Created by yuhy on 2017/2/11.
 */

public class WBHotSearch {

    /**
     * data : []
     * info :
     * retcode : 0
     */

    private String info;
    private int retcode;
    private List<DataBean> data;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getRetcode() {
        return retcode;
    }

    public void setRetcode(int retcode) {
        this.retcode = retcode;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * title : 林心如晒一家三口萌照
         * number : 842652
         */

        private String title;
        private String number;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }
    }
}
