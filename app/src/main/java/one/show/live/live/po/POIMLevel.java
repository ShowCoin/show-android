package one.show.live.live.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 18/3/31.
 */
public class POIMLevel implements Parcelable {

//    {"uid":"33","nickName":"ces","fanLevel":4}

    /**
     * 用户id
     */
    private String uid;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 等级
     */
    private int fanLevel;


    public POIMLevel() {

    }

    protected POIMLevel(Parcel in) {
        uid = in.readString();
        nickName = in.readString();
        fanLevel = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(nickName);
        dest.writeInt(fanLevel);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<POIMLevel> CREATOR = new Creator<POIMLevel>() {
        @Override
        public POIMLevel createFromParcel(Parcel in) {
            return new POIMLevel(in);
        }

        @Override
        public POIMLevel[] newArray(int size) {
            return new POIMLevel[size];
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

    public int getFanLevel() {
        return fanLevel;
    }

    public void setFanLevel(int fanLevel) {
        this.fanLevel = fanLevel;
    }


    @Override
    public String toString() {
        return "POIMLevel{" +
                "uid='" + uid + '\'' +
                ", nickName='" + nickName + '\'' +
                ", fanLevel=" + fanLevel +
                '}';
    }


}
