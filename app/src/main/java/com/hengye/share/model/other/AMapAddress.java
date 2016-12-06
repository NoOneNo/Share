package com.hengye.share.model.other;

import com.hengye.share.model.Address;

/**
 * Created by yuhy on 2016/12/6.
 */

public class AMapAddress {

    /**
     * id : B00140UF12
     * name : 华南师范大学(石牌校区)
     * tag : []
     * type : 科教文化服务;学校;高等院校
     * typecode : 141201
     * biz_type : []
     * address : 中山大道西55号
     * location : 113.351236,23.139153
     * tel : 020-85211114;020-85211098;020-85211097;020-83966066
     * postcode : []
     * website : []
     * email : []
     * pcode : 440000
     * pname : 广东省
     * citycode : 020
     * cityname : 广州市
     * adcode : 440106
     * adname : 天河区
     * importance : []
     * shopid : []
     * poiweight : []
     * gridcode : 3413526820
     * distance : 612
     * navi_poiid : F49F011043_588020
     * entr_location : 113.347992,23.136358
     * business_area : 五山
     * exit_location : []
     * match : 0
     * recommend : 3
     * timestamp : []
     * alias : 华南师大
     * indoor_map : 0
     * indoor_data : {"cpid":[],"floor":[],"truefloor":[],"cmsid":[]}
     * groupbuy_num : 0
     * discount_num : 0
     * biz_ext : {"rating":"4.6","cost":[]}
     * event : []
     * children : []
     * photos : [{"title":"外景图","url":"http://store.is.autonavi.com/showpic/549fbc79d49e65377c5c526d195c453b"},{"title":"特色图","url":"http://store.is.autonavi.com/showpic/4471fc1fdfd3bd72e70f98538dda9d5b"},{"title":[],"url":"http://store.is.autonavi.com/showpic/252f4f0e1a44bac4922f53d959db90d4"}]
     */

    private String id;
    private String name;
    private String type;
    private String typecode;
    private String address;
    private String location;
    private Object tel;
    private String pcode;
    private String pname;
    private String citycode;
    private String cityname;
    private String adcode;
    private String adname;
    private String distance;
    private Object navi_poiid;
    private Object entr_location;

    public Address convert(){
        Address address = new Address();
        address.setId(getId());
        address.setName(getName());
        address.setAddress(getAddress());
        if(getLocation() != null){
            String[] locations = getLocation().split(",");
            if(locations.length >= 2){
                try {
                    address.setLongitude(Float.valueOf(locations[0]));
                    address.setLatitude(Float.valueOf(locations[1]));
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
            }
        }
        address.setProvince(getPname());
        address.setCity(getCityname());
        address.setCounty(getAdname());

        try {
            address.setDistance(Long.valueOf(getDistance()));
        }catch (NumberFormatException e){
            e.printStackTrace();
        }
        return address;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTypecode() {
        return typecode;
    }

    public void setTypecode(String typecode) {
        this.typecode = typecode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Object getTel() {
        return tel;
    }

    public void setTel(Object tel) {
        this.tel = tel;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getCityname() {
        return cityname;
    }

    public void setCityname(String cityname) {
        this.cityname = cityname;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getAdname() {
        return adname;
    }

    public void setAdname(String adname) {
        this.adname = adname;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public Object getNavi_poiid() {
        return navi_poiid;
    }

    public void setNavi_poiid(Object navi_poiid) {
        this.navi_poiid = navi_poiid;
    }

    public Object getEntr_location() {
        return entr_location;
    }

    public void setEntr_location(Object entr_location) {
        this.entr_location = entr_location;
    }
}
