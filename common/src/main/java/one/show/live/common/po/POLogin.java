package one.show.live.common.po;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.Unique;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class POLogin implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * uid : 863937740662767616
     * isVip : 1
     * isNew : 0
     * videoVerifyStatus : 0
     * vipEndTime : 2033-02-23
     * profileImg : http://video-01.ws.seeulive.cn/591e8e062d5fdcc2c8c02832.jpg
     * thumbnail : http://video-01.ws.seeulive.cn/591e8e062d5fdcc2c9c02832.jpg
     * popularNo : 23241724
     * phoneNumber : 18231512425
     * nickName : 特特特
     * diamond : 125   星钻
     * background :
     * gender : 2
     * carVerifyStatus : 1   //车产认证状态   //状态值。 0。未提交。 1 提交待审核。2。认证通过。 3认证拒绝
     * gold : 174     星币
     * ry_token : AlIlGurssY1AEzKFYEcylg4nVBAjLKBtrtzpRWiQ1raq1QXK3Ug+55mydUMiYKpAE/1wOAYN5LA5FMVwO3gG7RswIFhOCz/quWh8DtLGjLw=
     * beke_token : aefa04266af7273151600c6c44f0e2f18b59e944086b65194aa907d283385eed
     */

    @Id(autoincrement = true)
    private Long id;
    @Unique
    private String uid;
    private int isVip;
    public int isNew;   //这个字段userinfo接口没有
    private int videoVerifyStatus;
    private String vipEndTime;
    private String profileImg;
    private String thumbnail;
    private String popularNo;
    private String phoneNumber;
    private String nickName;
    private int diamond;//消费使用的金钱
    private String background; //这个字段userinfo接口没有
    private int gender;
    private int carVerifyStatus;
    private int gold;//收到的钱
    private String ry_token;//这个字段userinfo接口没有
    private String beke_token;//这个字段userinfo接口没有
    private String remark;//be
    private String description;//描述
    private String sinaVerifyInfo;//新浪认证信息

    @Keep
    public POLogin(Long id, String uid, int isVip, int isNew, int videoVerifyStatus, String vipEndTime, String profileImg,
            String thumbnail, String popularNo, String phoneNumber, String nickName, int diamond, String background, int gender,
            int carVerifyStatus, int gold, String ry_token, String beke_token, String remark, String description,String sinaVerifyInfo) {
        this.id = id;
        this.uid = uid;
        this.isVip = isVip;
        this.isNew = isNew;
        this.videoVerifyStatus = videoVerifyStatus;
        this.vipEndTime = vipEndTime;
        this.profileImg = profileImg;
        this.thumbnail = thumbnail;
        this.popularNo = popularNo;
        this.phoneNumber = phoneNumber;
        this.nickName = nickName;
        this.diamond = diamond;
        this.background = background;
        this.gender = gender;
        this.carVerifyStatus = carVerifyStatus;
        this.gold = gold;
        this.ry_token = ry_token;
        this.beke_token = beke_token;
        this.remark = remark;
        this.sinaVerifyInfo = sinaVerifyInfo;
        this.description = description;
    }

    @Keep
    public POLogin() {
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }



    public int getIsNew() {
        return isNew;
    }

    public void setIsNew(int isNew) {
        this.isNew = isNew;
    }

    public int getVideoVerifyStatus() {
        return videoVerifyStatus;
    }

    public void setVideoVerifyStatus(int videoVerifyStatus) {
        this.videoVerifyStatus = videoVerifyStatus;
    }

    public String getUid() {
        return uid;
    }


    public String getVipEndTime() {
        return vipEndTime;
    }

    public void setVipEndTime(String vipEndTime) {
        this.vipEndTime = vipEndTime;
    }

    public String getProfileImg() {
        return TextUtils.isEmpty(profileImg) ? "" : profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getThumbnail() {
        return TextUtils.isEmpty(thumbnail) ? getProfileImg() : thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPopularNo() {
        return popularNo;
    }

    public void setPopularNo(String popularNo) {
        this.popularNo = popularNo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNickName() {
        return nickName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getDiamond() {
        return diamond;
    }

    public void setDiamond(int diamond) {
        this.diamond = diamond;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public int getGender() {
        return gender;
    }

    public boolean isMan(){
        return gender == 1;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getCarVerifyStatus() {
        return carVerifyStatus;
    }

    public void setCarVerifyStatus(int carVerifyStatus) {
        this.carVerifyStatus = carVerifyStatus;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public String getRy_token() {
        return ry_token;
    }

    public void setRy_token(String ry_token) {
        this.ry_token = ry_token;
    }

    public String getBeke_token() {
        return beke_token;
    }

    public void setBeke_token(String beke_token) {
        this.beke_token = beke_token;
    }

    public boolean isVip() {

        return isVip == 1;
    }

    public boolean isAuth(){
        return !TextUtils.isEmpty(sinaVerifyInfo);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSinaVerifyInfo() {
        return sinaVerifyInfo;
    }

    public void setSinaVerifyInfo(String sinaVerifyInfo) {
        this.sinaVerifyInfo = sinaVerifyInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        POLogin login = (POLogin) o;

        return uid != null ? uid.equals(login.uid) : login.uid == null;

    }

    @Override
    public int hashCode() {
        return uid != null ? uid.hashCode() : 0;
    }
}
