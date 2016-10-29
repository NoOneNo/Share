package com.hengye.share.util.retrofit.weibo;

import com.google.gson.annotations.SerializedName;
import com.hengye.share.R;
import com.hengye.share.util.ResUtil;

/**
 * Created by yuhy on 2016/10/18.
 */

public class WBApiException extends RuntimeException{

    public static WBApiException get503ServiceError(){
        WBApiException wbApiException = new WBApiException();
        wbApiException.setError(ResUtil.getString(R.string.tip_error_service_unavailable));
        wbApiException.setErrorCode(503);
        wbApiException.setRequest("");
        return wbApiException;
    }

    /**
     * @return 如果返回true, 则表示需要高级授权;
     */
    public boolean isNeedAdPermission(){
        switch (errorCode){
            case 10014:
            case 21335:
                return true;
            default:
                return false;
        }
    }

    public boolean isTokenExpired(){
        switch (errorCode){
            case 21301:
            case 21317:
            case 21327:
            case 21332:
                return true;
            default:
                return false;
        }
    }

    String error;

    @SerializedName("error_code")
    int errorCode;

    String request;

    /**
     * @return 如果属于API异常，则返回true;
     */
    public boolean isLegal(){
        return errorCode != 0;
    }

    @Override
    public String toString() {
        return "WBApiException{" +
                "error='" + error + '\'' +
                ", errorCode=" + errorCode +
                ", request='" + request + '\'' +
                '}';
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorcodeStr(){
        return String.valueOf(errorCode);
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

}
