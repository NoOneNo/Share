package com.hengye.share.module.util.encapsulation.base;

import java.io.Serializable;

/**
 * Created by yuhy on 16/7/27.
 */
public class PagingConfig implements Serializable {

    private boolean refreshEnable = true; //是否可以刷新

    private boolean loadEnable = false; //是否可以加载更多

    private boolean autoLoad = false; //是否自动加载更多(如果是,当滑动到底部时,不需要松开再滑动去加载)

    private boolean change = false; //用于判断配置是否发生变更;

    public void resetChange(){
        change = false;
    }

    public void change(){
        change = true;
    }

    public boolean isRefreshEnable() {
        return refreshEnable;
    }

    public void setRefreshEnable(boolean refreshEnable) {
        if(this.refreshEnable != refreshEnable){
            this.refreshEnable = refreshEnable;
            change();
        }
    }

    public boolean isLoadEnable() {
        return loadEnable;
    }

    public void setLoadEnable(boolean loadEnable) {
        if(this.loadEnable != loadEnable){
            change();
            this.loadEnable = loadEnable;
        }
    }

    public boolean isAutoLoad() {
        return autoLoad;
    }

    public void setAutoLoad(boolean autoLoad) {
        if(this.autoLoad != autoLoad){
            change();
            this.autoLoad = autoLoad;
        }
    }

    //        public boolean displayWhenScrolling = true;// 滚动的时候加载图片


//    public static class RefreshConfig implements Serializable {
//
//        private static final long serialVersionUID = 6244426943442129360L;
//
//        public boolean pagingEnd = false;// 分页是否结束
//
//        public String positionKey = null;// 最后阅读坐标的Key，null-不保存，针对缓存数据有效
//
//        public boolean displayWhenScrolling = true;// 滚动的时候加载图片
//
//        public int releaseDelay = 5 * 1000;// 当配置了releaseItemIds参数时，离开页面后自动释放资源
//
//        public int[] releaseItemIds = null;// 离开页面时，释放图片的控件，针对ItemView
//
//        public String emptyHint = "数据为空";// 如果EmptyLayout中有R.id.txtLoadEmpty这个控件，将这个提示绑定显示
//
//        public boolean footerMoreEnable = true;// FooterView加载更多
//
//    }
}
