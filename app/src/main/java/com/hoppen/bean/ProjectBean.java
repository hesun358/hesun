package com.hoppen.bean;

import java.io.Serializable;

public class ProjectBean implements Serializable {

    private static final long serialVersionUID = -4083503801443301445L;

    private String detType;     //检测项目
    private String area;        //区域(大洲）
    private String race;        //人种
    private String resistance;  //电阻值
    private String elastic;     //弹力
    private String sex;         //性别
    private String season;      //季节
    private String deviceType;  //设备类型
    private String lightType;   //灯光类型

    public ProjectBean() { }

    public ProjectBean(String detType, String area, String race, String resistance, String elastic, String sex, String season, String deviceType, String lightType) {
        this.detType = detType;
        this.area = area;
        this.race = race;
        this.resistance = resistance;
        this.elastic = elastic;
        this.sex = sex;
        this.season = season;
        this.deviceType = deviceType;
        this.lightType = lightType;
    }

    public void setDetType(String detType) {
        this.detType = detType;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setResistance(String resistance) {
        this.resistance = resistance;
    }

    public void setElastic(String elastic) {
        this.elastic = elastic;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setLightType(String lightType) {
        this.lightType = lightType;
    }

    public String getDetType() {
        return detType;
    }

    public String getArea() {
        return area;
    }

    public String getRace() {
        return race;
    }

    public String getResistance() {
        return resistance;
    }

    public String getElastic() {
        return elastic;
    }

    public String getSex() {
        return sex;
    }

    public String getSeason() {
        return season;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getLightType() {
        return lightType;
    }

    @Override
    public String toString() {
        return "ProjectBean{" +
                "detType='" + detType + '\'' +
                ", area='" + area + '\'' +
                ", race='" + race + '\'' +
                ", resistance='" + resistance + '\'' +
                ", elastic='" + elastic + '\'' +
                ", sex='" + sex + '\'' +
                ", season='" + season + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", lightType='" + lightType + '\'' +
                '}';
    }
}
