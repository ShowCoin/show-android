package one.show.live.live.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 18/3/31.
 */
public class POIMPermission implements Parcelable {

//    {"forbid_type":1,"uid":"","nickName":""}

//    POIMMsg{content='{"type":12,"data":{"uid":738414345649790976,"forbid_type":2,"opt_uid":745164869472493568,"opt_nickName":"samuel","nickName":"小小痞子LIU"}}'}


    /**
     * 权限id   1:踢出房间  2:禁言
     */
    private int forbid_type;

    /**
     * 用户id
     */
    private String uid;
    /**
     * 昵称
     */
    private String nickName;

    /**
     * 操作人用户id
     */
    private String opt_uid;
    /**
     * 操作人昵称
     */
    private String opt_nickName;

    protected POIMPermission(Parcel in) {
        forbid_type = in.readInt();
        uid = in.readString();
        nickName = in.readString();
        opt_uid = in.readString();
        opt_nickName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(forbid_type);
        dest.writeString(uid);
        dest.writeString(nickName);
        dest.writeString(opt_uid);
        dest.writeString(opt_nickName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<POIMPermission> CREATOR = new Creator<POIMPermission>() {
        @Override
        public POIMPermission createFromParcel(Parcel in) {
            return new POIMPermission(in);
        }

        @Override
        public POIMPermission[] newArray(int size) {
            return new POIMPermission[size];
        }
    };

    public int getForbid_type() {
        return forbid_type;
    }

    public void setForbid_type(int forbid_type) {
        this.forbid_type = forbid_type;
    }

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

    public String getOpt_uid() {
        return opt_uid;
    }

    public void setOpt_uid(String opt_uid) {
        this.opt_uid = opt_uid;
    }

    public String getOpt_nickName() {
        return opt_nickName;
    }

    public void setOpt_nickName(String opt_nickName) {
        this.opt_nickName = opt_nickName;
    }

}
