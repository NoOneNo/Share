package com.hengye.share.model.other;

import com.hengye.share.model.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhy on 2016/12/6.
 */

public class AMapAddresses {

    public ArrayList<Address> convert(){
        ArrayList<Address> addresses = new ArrayList<>();

        if(pois == null || pois.isEmpty()){
            return addresses;
        }

        for(AMapAddress aMapAddress : pois){
            addresses.add(aMapAddress.convert());
        }
        return addresses;
    }

    /**
     * status : 1
     * count : 609
     * info : OK
     * infocode : 10000
     * suggestion : {"keywords":[],"cities":[]}
     * pois : [{"id":"B00140ATE5","name":"高科大厦","type":"商务住宅;楼宇;商务写字楼","typecode":"120201","biz_type":[],"address":"石牌街道天河北路900号","location":"113.344723,23.140492","tel":[],"distance":"19","biz_ext":[],"importance":[],"shopid":[],"poiweight":[]},{"id":"B0FFG1JRNO","name":"高科大厦(正门)","type":"商务住宅;商务住宅相关;商务住宅相关","typecode":"120000","biz_type":[],"address":"石牌街道天河北路900号","location":"113.344576,23.140362","tel":[],"distance":"37","biz_ext":[],"importance":[],"shopid":[],"poiweight":[]}]
     */

    private String status;
    private String count;
    private String info;
    private String infocode;
    private SuggestionBean suggestion;
    /**
     * id : B00140ATE5
     * name : 高科大厦
     * type : 商务住宅;楼宇;商务写字楼
     * typecode : 120201
     * biz_type : []
     * address : 石牌街道天河北路900号
     * location : 113.344723,23.140492
     * tel : []
     * distance : 19
     * biz_ext : []
     * importance : []
     * shopid : []
     * poiweight : []
     */

    private List<AMapAddress> pois;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getInfocode() {
        return infocode;
    }

    public void setInfocode(String infocode) {
        this.infocode = infocode;
    }

    public SuggestionBean getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(SuggestionBean suggestion) {
        this.suggestion = suggestion;
    }

    public List<AMapAddress> getPois() {
        return pois;
    }

    public void setPois(List<AMapAddress> pois) {
        this.pois = pois;
    }

    public static class SuggestionBean {
        private List<?> keywords;
        private List<?> cities;

        public List<?> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<?> keywords) {
            this.keywords = keywords;
        }

        public List<?> getCities() {
            return cities;
        }

        public void setCities(List<?> cities) {
            this.cities = cities;
        }
    }
}
