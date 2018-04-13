package one.show.live.util;

/**
 * Created by apple on 18/3/23.
 */
public class EventBusKey {

    public static final int EVENT_BUS_LOGIN_BY_WX = 0x110;       //微信登录

    public static final int EVENT_SHARE_BY_WEI_XIN = 0x113;
    public static final int EVENT_SHARE_BY_WEIBO_SUCCESS = 0x122; //微博分享成功
    public static final int EVENT_SHARE_BY_WEIBO_FAILED = 0x123; //微博分享失败
    public static final int EVENT_SHARE_BY_QQ_SUCCESS = 0x124; //QQ分享成功
    public static final int EVENT_SHARE_BY_QQ_FAILED = 0x125; //QQ分享失败
    public static final int EVENT_SHARE_BY_QQZONE_SUCCESS = 0x126; //QQ空间分享成功
    public static final int EVENT_SHARE_BY_QQZONE_FAILED = 0x127; //QQ空间分享失败
    public static final int EVENT_SHARE_BY_WEI_XIN_FAILED = 0x128; //微信分享取消

    public static final int EVENT_VIEW_WORK_KEY = 0X129;//消息已读
}
