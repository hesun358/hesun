package com.hoppen.uvcctest;

public class DetectInfo {

    public int detType;                   // 检测项目
    public String area;                   // 区域(大洲）
    public String race;                   // 人种
    public float resistance;              // 电阻
    public float elastic;                 // 弹力
    public int sex;                       // 性别 0：女  1：男
    public int season;                    // 季节 0：春  1：夏  2：秋  3：冬
    public int deviceType;                // 设备类型
    public int lightType;                 // 灯光类型
    public String sdkVersion;             // sdk版本号
    public int score;                     // 得分
    public int finalScore;                // 综合评分
    public int level;                     // 级别

    public DetectInfo(int detType, String area, String race, float resistance, float elastic, int sex, int season, int deviceType, int lightType, String sdkVersion, int score, int finalScore, int level) {
        this.detType = detType;
        this.area = area;
        this.race = race;
        this.resistance = resistance;
        this.elastic = elastic;
        this.sex = sex;
        this.season = season;
        this.deviceType = deviceType;
        this.lightType = lightType;
        this.sdkVersion = sdkVersion;
        this.score = score;
        this.finalScore = finalScore;
        this.level = level;
    }

    public void setDetType(int detType) {
        this.detType = detType;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public void setResistance(float resistance) {
        this.resistance = resistance;
    }

    public void setElastic(float elastic) {
        this.elastic = elastic;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setSeason(int season) {
        this.season = season;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public void setLightType(int lightType) {
        this.lightType = lightType;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getDetType() {
        return detType;
    }

    public String getArea() {
        return area;
    }

    public String getRace() {
        return race;
    }

    public float getResistance() {
        return resistance;
    }

    public float getElastic() {
        return elastic;
    }

    public int getSex() {
        return sex;
    }

    public int getSeason() {
        return season;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public int getLightType() {
        return lightType;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public int getScore() {
        return score;
    }

    public int getFinalScore() {
        return finalScore;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "DetectInfo{" +
                "detType=" + detType +
                ", area='" + area + '\'' +
                ", race='" + race + '\'' +
                ", resistance=" + resistance +
                ", elastic=" + elastic +
                ", sex=" + sex +
                ", season=" + season +
                ", deviceType=" + deviceType +
                ", lightType=" + lightType +
                ", sdkVersion='" + sdkVersion + '\'' +
                ", score=" + score +
                ", finalScore=" + finalScore +
                ", level=" + level +
                '}';
    }
}
