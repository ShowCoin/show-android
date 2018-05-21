package one.show.live.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class UserBean implements Parcelable, Cloneable {
    private long memberid;
    private String nickname;
    private String avatar;
    private String desc;
    private String scid;
    private int sex;
    private int mtype;
    private int isfocus;
    private int online;
    private int onlines;
    private int max_online;
    private int hits;

    public long getMemberid() {
        return memberid;
    }

    public void setMemberid(long memberid) {
        this.memberid = memberid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getMtype() {
        return mtype;
    }

    public void setMtype(int mtype) {
        this.mtype = mtype;
    }

    public int getIsfocus() {
        return isfocus;
    }

    public void setIsfocus(int isfocus) {
        this.isfocus = isfocus;
    }

    public String getScid() {
        return scid;
    }

    public void setScid(String scid) {
        this.scid = scid;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public int getOnlines() {
        return onlines;
    }

    public void setOnlines(int onlines) {
        this.onlines = onlines;
    }

    public int getMax_online() {
        return max_online;
    }

    public void setMax_online(int max_online) {
        this.max_online = max_online;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public static final Creator<UserBean> CREATOR = new Creator<UserBean>() {
        @Override
        public UserBean createFromParcel(Parcel source) {

            UserBean bean = new UserBean();
            bean.setMemberid(source.readLong());
            bean.setNickname(source.readString());
            bean.setAvatar(source.readString());
            bean.setDesc(source.readString());
            bean.setScid(source.readString());
            bean.setSex(source.readInt());
            bean.setMtype(source.readInt());
            bean.setIsfocus(source.readInt());
            bean.setOnline(source.readInt());
            bean.setOnlines(source.readInt());
            bean.setMax_online(source.readInt());
            bean.setHits(source.readInt());
            return bean;
        }

        @Override
        public UserBean[] newArray(int size) {
            return new UserBean[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(memberid);
        dest.writeString(nickname);
        dest.writeString(avatar);
        dest.writeString(desc);
        dest.writeString(scid);
        dest.writeInt(sex);
        dest.writeInt(mtype);
        dest.writeInt(isfocus);
        dest.writeInt(online);
        dest.writeInt(onlines);
        dest.writeInt(max_online);
        dest.writeInt(hits);
    }

    public void readFromParcel(Parcel in) {
        memberid = in.readLong();
        nickname = in.readString();
        avatar = in.readString();
        desc = in.readString();
        scid = in.readString();
        sex = in.readInt();
        mtype = in.readInt();
        isfocus = in.readInt();
        online = in.readInt();
        onlines = in.readInt();
    }

    @Override
    public String toString() {
        return memberid+"";
    }
}
