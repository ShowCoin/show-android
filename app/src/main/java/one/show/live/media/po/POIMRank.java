package one.show.live.media.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 16/5/31.
 */
public class POIMRank implements Parcelable {

//    {"uid":"","nickName":"","profileImg":"","spend":1000000}


    /**
     * 用户id
     */
    private String uid;
    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像url
     */
    private String profileImg;
    /**
     * 贡献值
     */
    private long spend;


    protected POIMRank(Parcel in) {
        uid = in.readString();
        nickName = in.readString();
        profileImg = in.readString();
        spend = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(nickName);
        dest.writeString(profileImg);
        dest.writeLong(spend);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<POIMRank> CREATOR = new Creator<POIMRank>() {
        @Override
        public POIMRank createFromParcel(Parcel in) {
            return new POIMRank(in);
        }

        @Override
        public POIMRank[] newArray(int size) {
            return new POIMRank[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public long getSpend() {
        return spend;
    }

    public void setSpend(long spend) {
        this.spend = spend;
    }

    @Override
    public String toString() {
        return "POIMRank{" +
                "uid='" + uid + '\'' +
                ", nickName='" + nickName + '\'' +
                ", profileImg='" + profileImg + '\'' +
                ", spend=" + spend +
                '}';
    }
}
