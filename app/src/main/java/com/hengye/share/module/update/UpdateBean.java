package com.hengye.share.module.update;

import com.hengye.share.BuildConfig;
import com.hengye.share.util.SPUtil;

/**
 * Created by yuhy on 2016/11/21.
 */

public class UpdateBean {

    public static final String IGNORE_VERSION = "ignore_version";

    public boolean isNeedUpdate(boolean isForce){
        if(!isForce){
            //查看不再提示更新的版本号是否大于当前可更新的版本
            int versionCode = SPUtil.getInt(IGNORE_VERSION, 0);
            if(versionCode != 0 && versionCode >= this.versionCode){
                return false;
            }else if(!isNeedPrompt){
                return false;
            }

        }

        if(BuildConfig.VERSION_CODE < versionCode){
            return true;
        }
        return false;
    }

    private int versionCode;
    private String versionName;
    private boolean isNeedPrompt = true;//是否需要弹框提示更新，默认为true
    private String appUrl;
    private String updateInfo;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public boolean isNeedPrompt() {
        return isNeedPrompt;
    }

    public void setNeedPrompt(boolean needPrompt) {
        isNeedPrompt = needPrompt;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public String getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(String updateInfo) {
        this.updateInfo = updateInfo;
    }

    @Override
    public String toString() {
        return "UpdateBean{" +
                "versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", isNeedPrompt=" + isNeedPrompt +
                ", appUrl='" + appUrl + '\'' +
                ", updateInfo='" + updateInfo + '\'' +
                '}';
    }
}
