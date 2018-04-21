package one.show.live.media.po;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by apple on 16/5/31.
 *
 *
 *  用户聊天信息附加消息
 */
public class POIMTalkMsgExtra implements Parcelable {

//    {"id":"33","nickName":"ces","fanLevel":4}

    /**
     * 用户id
     */
    private String id;
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


    public POIMTalkMsgExtra() {

    }

    public POIMTalkMsgExtra(String uid,String nickName,int level) {
        setId(uid);
        setNickName(nickName);
        setFanLevel(level);
    }

    public POIMTalkMsgExtra(String uid,String nickName,int level,int isAdmin) {
        setId(uid);
        setNickName(nickName);
        setFanLevel(level);
        setIsAdmin(isAdmin);
    }


    protected POIMTalkMsgExtra(Parcel in) {
        id = in.readString();
        nickName = in.readString();
        fanLevel = in.readInt();
        isAdmin = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(nickName);
        dest.writeInt(fanLevel);
        dest.writeInt(isAdmin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<POIMTalkMsgExtra> CREATOR = new Creator<POIMTalkMsgExtra>() {
        @Override
        public POIMTalkMsgExtra createFromParcel(Parcel in) {
            return new POIMTalkMsgExtra(in);
        }

        @Override
        public POIMTalkMsgExtra[] newArray(int size) {
            return new POIMTalkMsgExtra[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String toJson(){
        try {
            return new Gson().toJson(this);
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static POIMTalkMsgExtra fromJson(String json){
        try {
            return new Gson().fromJson(json,new TypeToken<POIMTalkMsgExtra>() {
            }.getType());
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
