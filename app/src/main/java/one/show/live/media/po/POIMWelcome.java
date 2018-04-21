package one.show.live.media.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 16/5/31.
 */
public class POIMWelcome implements Parcelable {


    /**
     * 用户id
     */
    private String uid;
    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 等级
     */
    private int fanLevel;


    public POIMWelcome() {

    }

    protected POIMWelcome(Parcel in) {
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

    public static final Creator<POIMWelcome> CREATOR = new Creator<POIMWelcome>() {
        @Override
        public POIMWelcome createFromParcel(Parcel in) {
            return new POIMWelcome(in);
        }

        @Override
        public POIMWelcome[] newArray(int size) {
            return new POIMWelcome[size];
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
        return "POIMWelcome{" +
                "uid='" + uid + '\'' +
                ", nickName='" + nickName + '\'' +
                ", fanLevel=" + fanLevel +
                '}';
    }
}
