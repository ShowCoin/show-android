package one.show.live.live.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 18/3/31.
 */
public class POIMDanmu implements Parcelable {


    /**
     * content : xkkx
     * uid : 742286112919265300
     * profileImg : http://file0.beke.tv/avatar/201606/28/14//1467094949.jpg
     * isVip : 1
     * nickName : 贝壳
     */

    /**
     * 弹幕详情
     */
    private String content;
    /**
     * uid
     */
    private String uid;
    /**
     *头像
     */
    private String profileImg;
    /**
     *是不是vip 1是0不是
     */
    private int isVip;
    /**
     *昵称
     */
    private String nickName;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public int getIsVip() {
        return isVip;
    }

    public void setIsVip(int isVip) {
        this.isVip = isVip;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String toString() {
        return "POIMDanmu{" +
                "content='" + content + '\'' +
                ", uid='" + uid + '\'' +
                ", profileImg='" + profileImg + '\'' +
                ", isVip=" + isVip +
                ", nickName='" + nickName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeString(this.uid);
        dest.writeString(this.profileImg);
        dest.writeInt(this.isVip);
        dest.writeString(this.nickName);
    }

    public POIMDanmu() {
    }

    protected POIMDanmu(Parcel in) {
        this.content = in.readString();
        this.uid = in.readString();
        this.profileImg = in.readString();
        this.isVip = in.readInt();
        this.nickName = in.readString();
    }

    public static final Creator<POIMDanmu> CREATOR = new Creator<POIMDanmu>() {
        @Override
        public POIMDanmu createFromParcel(Parcel source) {
            return new POIMDanmu(source);
        }

        @Override
        public POIMDanmu[] newArray(int size) {
            return new POIMDanmu[size];
        }
    };
}
