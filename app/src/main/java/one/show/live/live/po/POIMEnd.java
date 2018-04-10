package one.show.live.live.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 16/7/4.
 */
public class POIMEnd implements Parcelable {

//    uid        long
//    reason     int
//    viewed     int
//    liked      int
//    receive    int
    private String uid;

//    0异常断流
//    1直播流中断超时
//    2用户主动关闭
//    3审核人员关闭
//    4禁播
    private int reason;
    private int viewed;
    private int liked;
    private int receive;
    private String liveID;

    public String getLiveID() {
        return liveID;
    }

    public void setLiveID(String liveID) {
        this.liveID = liveID;
    }

    public String getMessageByReason(){
        switch (reason){
            case 0:
                return "系统异常，直播中断";
            case 1:
                return "网络异常，直播中断";
            case 2:
                return "直播结束";
            case 3:
                return "直播被审核人员关闭";
            case 4:
                return "活该，被禁播了~";
        }
        return "直播结束";
    }

    public String getUid() {
        return uid;
    }

    public int getReason() {
        return reason;
    }

    public int getViewed() {
        return viewed;
    }

    public int getLiked() {
        return liked;
    }

    public int getReceive() {
        return receive;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public void setViewed(int viewed) {
        this.viewed = viewed;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public void setReceive(int receive) {
        this.receive = receive;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.uid);
        dest.writeInt(this.reason);
        dest.writeInt(this.viewed);
        dest.writeInt(this.liked);
        dest.writeInt(this.receive);
    }

    public POIMEnd() {
    }

    public POIMEnd(int reason,int viewed,int liked,int receive) {
        setReason(reason);
        setViewed(viewed);
        setLiked(liked);
        setReceive(receive);
    }

    protected POIMEnd(Parcel in) {
        this.uid = in.readString();
        this.reason = in.readInt();
        this.viewed = in.readInt();
        this.liked = in.readInt();
        this.receive = in.readInt();
    }

    public static final Creator<POIMEnd> CREATOR = new Creator<POIMEnd>() {
        @Override
        public POIMEnd createFromParcel(Parcel source) {
            return new POIMEnd(source);
        }

        @Override
        public POIMEnd[] newArray(int size) {
            return new POIMEnd[size];
        }
    };

    @Override
    public String toString() {
        return "POIMEnd{" +
                "uid='" + uid + '\'' +
                ", reason=" + reason +
                ", viewed=" + viewed +
                ", liked=" + liked +
                ", receive=" + receive +
                '}';
    }
}