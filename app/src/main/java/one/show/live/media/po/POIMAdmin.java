package one.show.live.media.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 16/5/31.
 */
public class POIMAdmin implements Parcelable {

//    {"uid”,"xxxxx”,"type":1}


    /**
     * 用户id
     */
    private String uid;
    /**
     * 类型  1：设置管理员 0：取消管理员
     */
    private int type;
    /**
     * 昵称
     */
    private String nickName;


    protected POIMAdmin(Parcel in) {
        uid = in.readString();
        type = in.readInt();
        nickName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uid);
        dest.writeInt(type);
        dest.writeString(nickName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<POIMAdmin> CREATOR = new Creator<POIMAdmin>() {
        @Override
        public POIMAdmin createFromParcel(Parcel in) {
            return new POIMAdmin(in);
        }

        @Override
        public POIMAdmin[] newArray(int size) {
            return new POIMAdmin[size];
        }
    };

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
