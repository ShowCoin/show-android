package one.show.live.po;

import java.io.Serializable;

/**
 * Created by Nano on 2018/4/17.
 */

public class POMaster implements Serializable {


    /**
     * master_level : 1
     * uid : 984772429270351872
     * gender : 0
     * nickname : ShowCoin新人16437069
     * fan_level : 1
     * birth : 0
     * popularNo : 23320785
     * avatar : http://show-live.oss-cn-beijing.aliyuncs.com/avatar/5ad5ac7d3b190ec2895f9548.jpeg
     * descriptions : null
     * isFollowed : 0
     */

    private int master_level;
    private String uid;
    private int gender;
    private String nickname;
    private int fan_level;
    private int birth;
    private int popularNo;
    private String avatar;
    private String descriptions;
    private int isFollowed;

    public int getMaster_level() {
        return master_level;
    }

    public void setMaster_level(int master_level) {
        this.master_level = master_level;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getFan_level() {
        return fan_level;
    }

    public void setFan_level(int fan_level) {
        this.fan_level = fan_level;
    }

    public int getBirth() {
        return birth;
    }

    public void setBirth(int birth) {
        this.birth = birth;
    }

    public int getPopularNo() {
        return popularNo;
    }

    public void setPopularNo(int popularNo) {
        this.popularNo = popularNo;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public int getIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(int isFollowed) {
        this.isFollowed = isFollowed;
    }
}
