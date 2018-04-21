package one.show.live.media.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 16/5/31.
 */
public class POIMUser implements Parcelable {


    /**
     * uid : 323213
     * profileImg : dfsdfdsfsd
     * fanLevel : 3
     */

    private String uid;
    private String profileImg;
    private int fanLevel;

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

    public int getFanLevel() {
        return fanLevel;
    }

    public void setFanLevel(int fanLevel) {
        this.fanLevel = fanLevel;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeString(this.profileImg);
        dest.writeInt(this.fanLevel);
    }

    public POIMUser() {
    }

    protected POIMUser(Parcel in) {
        this.uid = in.readString();
        this.profileImg = in.readString();
        this.fanLevel = in.readInt();
    }

    public static final Creator<POIMUser> CREATOR = new Creator<POIMUser>() {
        @Override
        public POIMUser createFromParcel(Parcel source) {
            return new POIMUser(source);
        }

        @Override
        public POIMUser[] newArray(int size) {
            return new POIMUser[size];
        }
    };

    @Override
    public String toString() {
        return "POIMUser{" +
                "uid='" + uid + '\'' +
                ", profileImg='" + profileImg + '\'' +
                ", fanLevel=" + fanLevel +
                '}';
    }
}
