package one.show.live.live.po;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by apple on 18/3/31.
 */
public class POIMGift implements Parcelable {


//    {"type":1,
//
//            "data":{"fromUserImg":"http:\/\/tva1.sinaimg.cn\/default\/images\/default_avatar_male_180.gif","giftType":4,"count":1,"fromUid":"738414345649790976",
//
//            "fromUserLevel":11,"num":1,"giftIcon":"http:\/\/file0.beke.tv\/gifticon\/201606\/11\/15\/\/1465629526.png",
//
//            "giftImg":"http:\/\/file0.beke.tv\/gift\/201606\/11\/15\/\/1465629525.png","giftName":"兰博基尼",
//
//            "giftId":6,"toNickName":"大象无形",
//            "giftExtend":{"wheel_double1":"http:\/\/file0.beke.tv\/system\/201606\/11\/15\/\/1465629155.png",
//            "wheel_double2":"http:\/\/file0.beke.tv\/system\/201606\/11\/15\/\/1465629167.png",
//            "car_light3":"http:\/\/file0.beke.tv\/system\/201606\/11\/15\/\/1465629218.png",
//            "car_body":"http:\/\/file0.beke.tv\/system\/201606\/11\/15\/\/1465629114.png",
//            "wheel":"http:\/\/file0.beke.tv\/system\/201606\/11\/15\/\/1465629182.png",
//            "car_light1":"http:\/\/file0.beke.tv\/system\/201606\/11\/15\/\/1465629196.png",
//            "car_light2":"http:\/\/file0.beke.tv\/system\/201606\/11\/15\/\/1465629208.png"},
//            "receive":1200,"fromNickName":"小小痞子LIU"}}

    /**
     * 礼物id
     */
    private int giftId;
    /**
     * 礼物名称
     */
    private String giftName;

    /**
     * 礼物类型
     */
    private int giftType;

    private String giftIcon;
    /**
     * 礼物icon
     */
    private String giftImg;
    /**
     *
     */
    private int num;
    /**
     * 送礼人id
     */
    private String fromUid;
    /**
     * 送礼人名称
     */
    private String fromNickName;
    /**
     * 送礼人等级
     */
    private int fromUserLevel;

    private String toNickName;

    /**
     * 礼物对应收益
     */
    private int receive;
    /**
     * 送礼人头像url
     */
    private String fromUserImg;
    /**
     * 礼物数量
     */
    private int count;


    private int isAdmin;



    public POIMGift() {

    }

    protected POIMGift(Parcel in) {
        giftId = in.readInt();
        giftName = in.readString();
        giftType = in.readInt();
        giftIcon = in.readString();
        giftImg = in.readString();
        num = in.readInt();
        fromUid = in.readString();
        fromNickName = in.readString();
        fromUserLevel = in.readInt();
        toNickName = in.readString();
        receive = in.readInt();
        fromUserImg = in.readString();
        count = in.readInt();
        isAdmin = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(giftId);
        dest.writeString(giftName);
        dest.writeInt(giftType);
        dest.writeString(giftIcon);
        dest.writeString(giftImg);
        dest.writeInt(num);
        dest.writeString(fromUid);
        dest.writeString(fromNickName);
        dest.writeInt(fromUserLevel);
        dest.writeString(toNickName);
        dest.writeInt(receive);
        dest.writeString(fromUserImg);
        dest.writeInt(count);
        dest.writeInt(isAdmin);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<POIMGift> CREATOR = new Creator<POIMGift>() {
        @Override
        public POIMGift createFromParcel(Parcel in) {
            return new POIMGift(in);
        }

        @Override
        public POIMGift[] newArray(int size) {
            return new POIMGift[size];
        }
    };

    public int getGiftId() {
        return giftId;
    }

    public void setGiftId(int giftId) {
        this.giftId = giftId;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getGiftType() {
        return giftType;
    }

    public void setGiftType(int giftType) {
        this.giftType = giftType;
    }

    public String getGiftIcon() {
        return giftIcon;
    }

    public void setGiftIcon(String giftIcon) {
        this.giftIcon = giftIcon;
    }

    public String getGiftImg() {
        return giftImg;
    }

    public void setGiftImg(String giftImg) {
        this.giftImg = giftImg;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getFromUid() {
        return fromUid;
    }

    public void setFromUid(String fromUid) {
        this.fromUid = fromUid;
    }

    public String getFromNickName() {
        return fromNickName;
    }

    public void setFromNickName(String fromNickName) {
        this.fromNickName = fromNickName;
    }

    public int getFromUserLevel() {
        return fromUserLevel;
    }

    public void setFromUserLevel(int fromUserLevel) {
        this.fromUserLevel = fromUserLevel;
    }

    public String getToNickName() {
        return toNickName;
    }

    public void setToNickName(String toNickName) {
        this.toNickName = toNickName;
    }

    public int getReceive() {
        return receive;
    }

    public void setReceive(int receive) {
        this.receive = receive;
    }

    public String getFromUserImg() {
        return fromUserImg;
    }

    public void setFromUserImg(String fromUserImg) {
        this.fromUserImg = fromUserImg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
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

}
