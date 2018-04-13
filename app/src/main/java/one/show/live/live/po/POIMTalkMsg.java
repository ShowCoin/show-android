package one.show.live.live.po;

import java.io.Serializable;

/**
 * 滚动聊天区域消息
 */
public class POIMTalkMsg implements Serializable {


    public enum POIMTalkMsgType {
        SystemType, NormalType , LiveRoomType,LiveRoomAndWithUser, SendGift,LikeType
    }

    private POIMTalkMsgType type;


    private POIMMsg.MsgType msgType;

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
    private int level;


    private String content;

    /**
     * 名字的起始坐标
     */
    private int nameStartIndex;

    /**
     * 礼物名称
     */
    private String giftName;
    /**
     * 礼物图片url
     */
    private String giftImgUrl;


    private String optUid;


    private String optUserName;

    private int optStartIndexOffset;

    private boolean isAdmin;


    private POIMTalkMsg() {

    }

    public static POIMTalkMsg buildSystemMsg(String content) {
        return buildMsg(POIMTalkMsgType.SystemType, content, null);
    }


    public static POIMTalkMsg buildRoomMsg(String content) {
        return buildMsg(POIMTalkMsgType.LiveRoomType, content, null);
    }

    public static POIMTalkMsg buildRoomMsg(String uid,String nikeName,String content,int nameStartIndex,POIMMsg.MsgType msgType) {
        return buildRoomMsg(uid,nikeName, content, nameStartIndex,null,null,0,msgType);
    }


    public static POIMTalkMsg buildRoomMsg(String uid,String nikeName,String content,int nameStartIndex,String optUid,String optUserName,int optStartIndexOffset,POIMMsg.MsgType msgType) {
        POIMTalkMsg msg = new POIMTalkMsg();
        msg.type = POIMTalkMsgType.LiveRoomAndWithUser;
        msg.uid = uid;
        msg.content = content;
        msg.nickName = nikeName;
        msg.nameStartIndex = nameStartIndex;

        msg.optUid = optUid;
        msg.optUserName = optUserName;
        msg.optStartIndexOffset = optStartIndexOffset;

        msg.msgType = msgType;
        return msg;
    }


    public static POIMTalkMsg buildNormalMsg(String content, POIMTalkMsgExtra extra) {
        return buildMsg(POIMTalkMsgType.NormalType, content, extra);
    }

    public static POIMTalkMsg buildSendGiftMsg(POIMGift gift,POIMMsg.MsgType msgType) {
        POIMTalkMsg msg = new POIMTalkMsg();
        msg.type = POIMTalkMsgType.SendGift;
        msg.content = String.format("我送了%d个%s", gift.getNum(), gift.getGiftName());
        msg.uid = gift.getFromUid();
        msg.nickName = gift.getFromNickName();
        msg.level = gift.getFromUserLevel();
        msg.giftImgUrl = gift.getGiftImg();
        msg.msgType = msgType;
        msg.isAdmin = gift.isAdmin();

        return msg;
    }

    public static POIMTalkMsg buildLikeMsg(POIMLike like,POIMMsg.MsgType msgType) {
        POIMTalkMsg msg = new POIMTalkMsg();
        msg.type = POIMTalkMsgType.LikeType;
        msg.content = "我为主播点了个赞";
        msg.uid = like.getUid();
        msg.nickName = like.getNickName();
        msg.level = like.getFanLevel();
        msg.giftImgUrl = null;
        msg.msgType = msgType;
        msg.isAdmin = like.isAdmin();
        return msg;
    }


    private static POIMTalkMsg buildMsg(POIMTalkMsgType type, String content, POIMTalkMsgExtra extra) {
        POIMTalkMsg msg = new POIMTalkMsg();
        msg.type = type;
        msg.content = content;
        if (extra != null) {
            msg.uid = extra.getId();
            msg.nickName = extra.getNickName();
            msg.level = extra.getFanLevel();
            msg.isAdmin = extra.isAdmin();
        }
        msg.giftImgUrl = null;
        return msg;
    }


    public POIMTalkMsgType getType() {
        return type;
    }

    public void setType(POIMTalkMsgType type) {
        this.type = type;
    }

    public POIMMsg.MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(POIMMsg.MsgType msgType) {
        this.msgType = msgType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getGiftImgUrl() {
        return giftImgUrl;
    }

    public void setGiftImgUrl(String giftImgUrl) {
        this.giftImgUrl = giftImgUrl;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getNameStartIndex() {
        return nameStartIndex;
    }

    public void setNameStartIndex(int nameStartIndex) {
        this.nameStartIndex = nameStartIndex;
    }

    public String getOptUid() {
        return optUid;
    }

    public void setOptUid(String optUid) {
        this.optUid = optUid;
    }

    public String getOptUserName() {
        return optUserName;
    }

    public void setOptUserName(String optUserName) {
        this.optUserName = optUserName;
    }

    public int getOptStartIndexOffset() {
        return optStartIndexOffset;
    }

    public void setOptStartIndexOffset(int optStartIndexOffset) {
        this.optStartIndexOffset = optStartIndexOffset;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean admin) {
        isAdmin = admin;
    }
}
