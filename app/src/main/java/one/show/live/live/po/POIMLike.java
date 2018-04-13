package one.show.live.live.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 18/3/31.
 */
public class POIMLike implements Parcelable {

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

    /**
     * 是否管理员
     */
    private int isAdmin;


    public POIMLike() {

    }

    protected POIMLike(Parcel in) {
        uid = in.readString();
        nickName = in.readString();
        fanLevel = in.readInt();
        isAdmin = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeString(nickName);
        dest.writeInt(fanLevel);
        dest.writeInt(isAdmin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<POIMLike> CREATOR = new Creator<POIMLike>() {
        @Override
        public POIMLike createFromParcel(Parcel in) {
            return new POIMLike(in);
        }

        @Override
        public POIMLike[] newArray(int size) {
            return new POIMLike[size];
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


    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isAdmin(){
        return isAdmin == 1;
    }

    @Override
    public String toString() {
        return "POIMLike{" +
                "uid='" + uid + '\'' +
                ", nickName='" + nickName + '\'' +
                ", fanLevel=" + fanLevel +
                '}';
    }
}
