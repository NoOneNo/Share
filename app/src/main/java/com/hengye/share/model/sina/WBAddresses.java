package com.hengye.share.model.sina;

import com.hengye.share.model.Address;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhy on 2016/12/6.
 */

public class WBAddresses {

    public ArrayList<Address> convert(){
        ArrayList<Address> addresses = new ArrayList<>();

        if(pois == null || pois.isEmpty()){
            return addresses;
        }

        for(WBAddress  wbAddress: pois){
            addresses.add(wbAddress.convert());
        }
        return addresses;
    }

    /**
     * pois : [{"poiid":"B2094654D76CA7F5429E","title":"高科大厦","address":"广州市天河区天河北路900号","lon":"113.344141","lat":"23.14095","category":"46","city":"0020","province":null,"country":"80086","phone":"020-87503468,020-87579888","district":"8008644010600000014","source":"5","cityid_new":"8008644010000000000","categorys":"44 46","category_name":"楼宇","icon":"http://u1.sinaimg.cn/upload/lbs/poi/icon/88/46.png","map":"http://maps.google.cn/maps/api/staticmap?center=23.14095,113.344141&zoom=15&size=120x120&maptype=roadmap&markers=23.14095,113.344141&sensor=true","poi_pic":"http://ww3.sinaimg.cn/large/86369a41jw1f05e1o6g8dj205k05kdg0.jpg","pintu":"0","district_name":"石牌","district_info":{"id":"8008644010600000014","title":"石牌","intro":"位于广州市天河区","lng":"113.341026","lat":"23.1357","pic_1":"http://u1.sinaimg.cn/upload/lbs/SQ/197_1.jpg","country":"中国","province":"广东省","city":"广州市","county":"天河区","checkin_user_num":256711},"poi_street_address":"广东省,广州市,天河区,天科路","checkin_user_num":"399","herenow_user_num":"1","selected":0,"icon_show":[],"enterprise":0,"checkin_num":852,"tip_num":0,"photo_num":523,"todo_num":0,"dianping_num":0,"trend":"","distance":99},{"poiid":"B2094757D76AA6FF459B","title":"林海食街（高科大厦店）","address":"天河区天河北路900号高科大厦1楼108铺(近五山路)","lon":"113.34443","lat":"23.14064","category":"68","city":"0020","province":null,"country":"80086","phone":"020-22381885,020-22381886","district":"8008644010600000014","source":"3","cityid_new":"8008644010000000000","categorys":"64 68","category_name":"快餐厅","spent":"23","icon":"http://u1.sinaimg.cn/upload/lbs/poi/icon/88/68.png","map":"http://maps.google.cn/maps/api/staticmap?center=23.14064,113.34443&zoom=15&size=120x120&maptype=roadmap&markers=23.14064,113.34443&sensor=true","poi_pic":"http://ww2.sinaimg.cn/large/4e704b16jw1f2lrs0qgs8j205k05kmxl.jpg","pintu":"0","district_name":"石牌","district_info":{"id":"8008644010600000014","title":"石牌","intro":"位于广州市天河区","lng":"113.341026","lat":"23.1357","pic_1":"http://u1.sinaimg.cn/upload/lbs/SQ/197_1.jpg","country":"中国","province":"广东省","city":"广州市","county":"天河区","checkin_user_num":256711},"poi_street_address":"","checkin_user_num":"9","herenow_user_num":0,"selected":0,"icon_show":[],"enterprise":0,"checkin_num":22,"tip_num":0,"photo_num":13,"todo_num":0,"dianping_num":0,"trend":"","distance":56}]
     * total_number : 7
     * geo : {"lon":"113.344723","lat":"23.140667"}
     */

    private int total_number;
    /**
     * lon : 113.344723
     * lat : 23.140667
     */

    private GeoBean geo;
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

    private List<WBAddress> pois;

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }

    public GeoBean getGeo() {
        return geo;
    }

    public void setGeo(GeoBean geo) {
        this.geo = geo;
    }

    public List<WBAddress> getPois() {
        return pois;
    }

    public void setPois(List<WBAddress> pois) {
        this.pois = pois;
    }

    public static class GeoBean {
        private String lon;
        private String lat;

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
    }

}
