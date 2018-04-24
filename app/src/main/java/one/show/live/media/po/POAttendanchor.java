package one.show.live.media.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/7/4 0004.
 */
public class POAttendanchor implements Parcelable {

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


  public POAttendanchor() {

  }

  protected POAttendanchor(Parcel in) {
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

  @Override
  public String toString() {
    return "POIMLike{" +
        "uid='" + uid + '\'' +
        ", nickName='" + nickName + '\'' +
        ", fanLevel=" + fanLevel +
        '}';
  }
}
