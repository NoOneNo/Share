package com.hengye.share.model;

import java.io.Serializable;

/**
 * Created by yuhy on 2016/12/6.
 */

public class Address implements Serializable{

    private static final long serialVersionUID = -6155254790188395273L;
    /**
     * 位置id
     */
    private String id;

    /**
     * 位置显示名
     */
    private String name;

    /**
     * 具体地址信息
     */
    private String address;

    /**
     * 经纬度
     */
    private float longitude, latitude;

    /**
     * 省市区(县)
     */
    private String province, city, county;

    /**
     * 和当前位置的距离，单位默认米
     */
    private long distance;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
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

    public long getDistance() {
        return distance;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }
}
