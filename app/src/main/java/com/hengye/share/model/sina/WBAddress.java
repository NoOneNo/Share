package com.hengye.share.model.sina;

import com.hengye.share.model.Address;

import java.util.List;

/**
 * Created by yuhy on 2016/12/6.
 */

public class WBAddress {

    public Address convert() {
        Address address = new Address();
        address.setId(getPoiid());
        address.setName(getTitle());
        address.setAddress(getAddress());
        try {
            address.setLongitude(Float.valueOf(getLon()));
            address.setLatitude(Float.valueOf(getLat()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (getDistrict_info() != null) {
            address.setProvince(getDistrict_info().getProvince());
            address.setCity(getDistrict_info().getCity());
            address.setCounty(getDistrict_info().getCounty());
        }
        address.setDistance(getDistance());
        return address;
    }

    /**
     * poiid : B2094654D76CA7F5429E
     * title : 高科大厦
     * address : 广州市天河区天河北路900号
     * lon : 113.344141
     * lat : 23.14095
     * category : 46
     * city : 0020
     * province : null
     * country : 80086
     * phone : 020-87503468,020-87579888
     * district : 8008644010600000014
     * source : 5
     * cityid_new : 8008644010000000000
     * categorys : 44 46
     * category_name : 楼宇
     * icon : http://u1.sinaimg.cn/upload/lbs/poi/icon/88/46.png
     * map : http://maps.google.cn/maps/api/staticmap?center=23.14095,113.344141&zoom=15&size=120x120&maptype=roadmap&markers=23.14095,113.344141&sensor=true
     * poi_pic : http://ww3.sinaimg.cn/large/86369a41jw1f05e1o6g8dj205k05kdg0.jpg
     * pintu : 0
     * district_name : 石牌
     * district_info : {"id":"8008644010600000014","title":"石牌","intro":"位于广州市天河区","lng":"113.341026","lat":"23.1357","pic_1":"http://u1.sinaimg.cn/upload/lbs/SQ/197_1.jpg","country":"中国","province":"广东省","city":"广州市","county":"天河区","checkin_user_num":256711}
     * poi_street_address : 广东省,广州市,天河区,天科路
     * checkin_user_num : 399
     * herenow_user_num : 1
     * selected : 0
     * icon_show : []
     * enterprise : 0
     * checkin_num : 852
     * tip_num : 0
     * photo_num : 523
     * todo_num : 0
     * dianping_num : 0
     * trend :
     * distance : 99
     */

    private String poiid;
    private String title;
    private String address;
    private String lon;
    private String lat;
    private String category;
    private String city;
    private Object province;
    private String country;
    private String phone;
    private String district;
    private String source;
    private String cityid_new;
    private String categorys;
    private String category_name;
    private String icon;
    private String map;
    private String poi_pic;
    private String pintu;
    private String district_name;
    /**
     * id : 8008644010600000014
     * title : 石牌
     * intro : 位于广州市天河区
     * lng : 113.341026
     * lat : 23.1357
     * pic_1 : http://u1.sinaimg.cn/upload/lbs/SQ/197_1.jpg
     * country : 中国
     * province : 广东省
     * city : 广州市
     * county : 天河区
     * checkin_user_num : 256711
     */

    private DistrictInfoBean district_info;
    private String poi_street_address;
    private String checkin_user_num;
    private String herenow_user_num;
    private int selected;
    private int enterprise;
    private int checkin_num;
    private int tip_num;
    private int photo_num;
    private int todo_num;
    private int dianping_num;
    private String trend;
    private long distance;
    private List<?> icon_show;

    public String getPoiid() {
        return poiid;
    }

    public void setPoiid(String poiid) {
        this.poiid = poiid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Object getProvince() {
        return province;
    }

    public void setProvince(Object province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getCityid_new() {
        return cityid_new;
    }

    public void setCityid_new(String cityid_new) {
        this.cityid_new = cityid_new;
    }

    public String getCategorys() {
        return categorys;
    }

    public void setCategorys(String categorys) {
        this.categorys = categorys;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getPoi_pic() {
        return poi_pic;
    }

    public void setPoi_pic(String poi_pic) {
        this.poi_pic = poi_pic;
    }

    public String getPintu() {
        return pintu;
    }

    public void setPintu(String pintu) {
        this.pintu = pintu;
    }

    public String getDistrict_name() {
        return district_name;
    }

    public void setDistrict_name(String district_name) {
        this.district_name = district_name;
    }

    public DistrictInfoBean getDistrict_info() {
        return district_info;
    }

    public void setDistrict_info(DistrictInfoBean district_info) {
        this.district_info = district_info;
    }

    public String getPoi_street_address() {
        return poi_street_address;
    }

    public void setPoi_street_address(String poi_street_address) {
        this.poi_street_address = poi_street_address;
    }

    public String getCheckin_user_num() {
        return checkin_user_num;
    }

    public void setCheckin_user_num(String checkin_user_num) {
        this.checkin_user_num = checkin_user_num;
    }

    public String getHerenow_user_num() {
        return herenow_user_num;
    }

    public void setHerenow_user_num(String herenow_user_num) {
        this.herenow_user_num = herenow_user_num;
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getEnterprise() {
        return enterprise;
    }

    public void setEnterprise(int enterprise) {
        this.enterprise = enterprise;
    }

    public int getCheckin_num() {
        return checkin_num;
    }

    public void setCheckin_num(int checkin_num) {
        this.checkin_num = checkin_num;
    }

    public int getTip_num() {
        return tip_num;
    }

    public void setTip_num(int tip_num) {
        this.tip_num = tip_num;
    }

    public int getPhoto_num() {
        return photo_num;
    }

    public void setPhoto_num(int photo_num) {
        this.photo_num = photo_num;
    }

    public int getTodo_num() {
        return todo_num;
    }

    public void setTodo_num(int todo_num) {
        this.todo_num = todo_num;
    }

    public int getDianping_num() {
        return dianping_num;
    }

    public void setDianping_num(int dianping_num) {
        this.dianping_num = dianping_num;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    public List<?> getIcon_show() {
        return icon_show;
    }

    public void setIcon_show(List<?> icon_show) {
        this.icon_show = icon_show;
    }

    public static class DistrictInfoBean {
        private String id;
        private String title;
        private String intro;
        private String lng;
        private String lat;
        private String pic_1;
        private String country;
        private String province;
        private String city;
        private String county;
        private int checkin_user_num;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getPic_1() {
            return pic_1;
        }

        public void setPic_1(String pic_1) {
            this.pic_1 = pic_1;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public int getCheckin_user_num() {
            return checkin_user_num;
        }

        public void setCheckin_user_num(int checkin_user_num) {
            this.checkin_user_num = checkin_user_num;
        }
    }
}
