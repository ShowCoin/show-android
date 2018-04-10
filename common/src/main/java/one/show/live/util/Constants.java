package one.show.live.util;

public class Constants {


    /**
     * apk签名信息
     */
    public final static String packageSignStr =  "e3f814f10bfbaf89373edfd1628be73d";

    /**
     * 三方平台账号信息
     */
    public final static String WEI_XIN_APP_ID =  "wx40050a1e5840e700";
    public final static String WEI_XIN_APP_SECRET =  "534db56136d5db8bbb06c67d5f35410d";


    public static final String Weibo_APP_KEY      = "2178911023";		   // 应用的APP_KEY
    public static final String Weibo_REDIRECT_URL = "http://sns.whalecloud.com/sina2/callback";// 应用的回调页
    public static final String Weibo_SCOPE = 							   // 应用申请的高级权限
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    /**
     * 融云连接之后本地存储成功的token
     */
    public static final String RONGYUN_TOKEN = "RONGYUN_TOKEN";
    /**
     * QQAPPID
     */
    public static final String QQAPPID = "1105352619";
    /**
     * QQAPPKEY
     */
    public static final String QQAPPKEY = "NohBQw0YywMIwuNQ";


    /**
     * URL
     */
    public final static String KEY_CHECK_VERSION_INTERVAL = "KEY_CHECK_VERSION_INTERVAL";
    public final static String KEY_CURRENT_TIME = "KEY_CURRENT_TIME";

    public final static long CHECK_READ_MSG_INTERVAL = 72000;

    public final static String NAME_APP_SETTING = "appInfo";

    public final static int AVAILABLE_SPACE = 200;
    public final static String VIDEO_DOWNLOADCACHE = "/beke/downloads";//下载视频



    public static final int TYPE_PHOTO = 1;
    public static final int TYPE_VIDEO = 0;
    public static final int STATUS_VIDEO = 0;

    public static final String BASE_URL = "";
    public final static int RESPONSE_CODE_TOKEN_FAIL = 5005;//其他设备登陆
    public final static int RESPONSE_CODE_USER_SEALED = 5002;//封停账号
    public final static int RESPONSE_CODE_SHOW_GIFT = 0x100;
    public final static int RESPONSE_CODE_LOOUT = 2019;//lostToken

    public static final int EVENT_BUS_INDEX_TABLE = 0x200;
    public static final int EVENT_BUS_UN_FOLLOW = 0x201;
    public static final int EVENT_BUS_FOLLOW = 0x202;

    public static final int EVENT_BUS_CHANGE_WALLET = 0x203;


    /**
     * 网络错误错误码
     */
    public static final int NETWORK_ERROR_STATUS = -1;

    /**
     * 服务器未知异常
     */
    public static final int SERVER_UNKNOW_ERROR_STATUS = -2;

    /**
     * 其他需要信息提示的异常
     */
    public static final int OTHER_REQUEST_ERROR = -3;



}
