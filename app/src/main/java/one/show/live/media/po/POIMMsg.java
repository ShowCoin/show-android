package one.show.live.media.po;



public abstract class POIMMsg {

    public enum MsgType {
        UNKNOW(-1,"未知消息"),GIFT(1, "送礼"), LEVEL(2, "升级"), DANMU(3, "飞屏"),
        STARTLIVE(4, "直播开始"), ENDLIVE(5, "直播结束"), INTERRUPTLIVE(6, "直播流中断"),
        LIKE(7, "飘心"), USERLIST(8, "用户列表更新"), WELCOME(9, "欢迎语"),
        CONTENT(10, "公告"), RANKLIST(11, "排行榜"), PERMISSION(12, "权限"),ADMIN(13, "设置管理员"),ATTENDANCHOR(14, "关注主播") ;




        public int type;
        public String desc;

        MsgType(int type, String desc) {
            this.type = type;
            this.desc = desc;
        }
    }


    private int type;
    private String roomId;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
