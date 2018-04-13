package one.show.live.common.po;

import java.io.Serializable;

/**
 * Created by liuzehua on 2017/9/2.
 */

public class PORecommends implements Serializable
{
    /**
     * uid : 900312771097395200
     * constellation : 摩羯座
     * carBrand : 0
     * pid : 23242618
     * videoVerifyStatus : 0
     * avatar : http://images.seeulive.cn/599d61db9b42dcc2ee7682b9.jpg
     * intro :
     * city : 北京市
     * photos : []
     * nick_name : hhh
     * fan_level : 1
     * age : 18
     * gender : 1
     * longitude : 116.468943
     * carVerifyStatus : 0
     * latitude : 39.955655
     */

    private String uid;
    private String constellation;
    private int carBrand;
    private String pid;
    private int videoVerifyStatus;
    private String avatar;
    private String intro;
    private String city;
    private String nick_name;
    private int fan_level;
    private int age;
    private int gender;
    private double longitude;
    private int carVerifyStatus;
    private double latitude;
    private String workId;
    private String distance;
//    private List<?> photos;//暂时用不着


    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public int getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(int carBrand) {
        this.carBrand = carBrand;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public int getVideoVerifyStatus() {
        return videoVerifyStatus;
    }

    public void setVideoVerifyStatus(int videoVerifyStatus) {
        this.videoVerifyStatus = videoVerifyStatus;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public int getFan_level() {
        return fan_level;
    }

    public void setFan_level(int fan_level) {
        this.fan_level = fan_level;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getCarVerifyStatus() {
        return carVerifyStatus;
    }

    public void setCarVerifyStatus(int carVerifyStatus) {
        this.carVerifyStatus = carVerifyStatus;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

//    public List<?> getPhotos() {
//        return photos;
//    }
//
//    public void setPhotos(List<?> photos) {
//        this.photos = photos;
//    }

    public POLogin getPologin(long dbId){
        POLogin poLogin = new POLogin(dbId,getUid(),0,0,getVideoVerifyStatus(),"",getAvatar(),getAvatar(),"","",
                getNick_name(),0,"",getGender(),getCarVerifyStatus(),0,"","","",getDistance(),"");
        return poLogin;
    }
}
