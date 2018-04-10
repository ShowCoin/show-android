package one.show.live.api;

import one.show.live.BuildConfig;

public class BaseAPI {

    public static class Domain {
        public static String bizDomain = BuildConfig.API_DOMAIN;
//        public static String payDomain = bizDomain;
//        public static String loginDomain = bizDomain;
    }

    public static class Path {
        /**
         * test
         */
        public static final String getNewVersion = "version_control";
        //配置参数接口
        public static final String config = "config";
        //搜索接口
        public static final String search_user = "search_user";
        //最近访客
        public static final String visitors = "visit_list";
        //获取用户资料接口
        public static final String getUserInfo = "getUserInfo";
        //登陆接口
        public static final String login = "login";
        //获取验证码的接口
        public static final String getVerifiedCode = "get_verified_code";
        //注册接口
        public static final String reg = "reg";
        //忘记密码
        public static final String forgetPwd = "forgetPwd";
        //分享
        public static final String share = "stat/share";
        //修改用户资料接口
        public static final String updateUserInfo = "updateUserInfo";
        //充值记录
        public static final String chargeList = "bill/chargeList";
        //获取融云token
        public static final String rongToken = "getRyToken";
        //开屏广告的曝光次数
        public static final String popup_exposure_times = "popup_exposure_times";
        /**
         * 上传头像
         */
        public static final String uploadAvatar = "uploadAvatar";
        /**
         * 上传背景
         */
        public static final String changeBackground = "changeBackground";
        //意见反馈
        public static final String report = "report";
        //绑定第三方接口
        public static final String bind_account = "bind_account";
        //解绑第三方接口
        public static final String unbind_account = "unbind_account";
        //增加修改消息提醒配置的接口
        public static final String updateNotifyConfig = "updateNotifyConfig";
        /**
         * 取订单信息
         */
        public static String orderInfo = "addOrder";
        /**
         * 校验支付结果
         */
        public static String checkOrderResult = "getOrder";
        //提现记录
        public static final String extractRmbList = "bill/extractRmbList";
        //珍珠兑换贝壳的接口
        public static final String exchange = "exchange";
        //兑换记录的接口
        public static final String exchangeList = "bill/exchangeList";
        //获取用户关注列表
        public static final String user_follow_list = "user_follow_list";
        //获取用户粉丝列表
        public static final String user_fans_list = "user_fans_list";
        //关注或者取消关注接口
        public static final String follow_user = "follow_user";
        //添加黑名单和移除黑名单
        public static final String black_list = "black/list";
        //黑名单列表
        public static final String isBinded = "black/black";
        //获取上传token
        public static final String getUploadToken = "getUploadToken";
        //上传作品
        public static final String upload_works = "uploadWorks";
        //真实素材认证接口，车产，视频
        public static final String verify = "verify";
        //搜索列表默认的数据就是推荐用户
        public static final String suggest_user = "suggest_user";
        //推荐页面头部话题列表
        public static final String topic_list = "topic_list";
        //查看消息内容
        public static final String view_works = "viewWorks";
        //关注首页推荐用户
        public static final String follow_users = "follow_users";
        //删除数据接口
        public static final String delFeeds = "delFeeds";
        //获取标签
        public static final String tags_list = "tags_list";
        //校验是否合法
        public static final String check_sensitive = "check_sensitive";
        //单独请求消息数据
        public static final String works = "works";
        //单独请求消息数据
        public static final String update_work = "update_work";
        //官方消息列表
        public static final String message_list = "message/listNew";
        //清除消息
        public static final String message_clear = "message/clear";
        //消息设为已读
        public static final String message_read = "message/read";
        //回复官方消息
        public static final String message_chat = "message/chat_system";
        //清除官方消息与新的消息的未读状态
        public static final String change_message_list_state = "message/changeReadNew";
        //新的消息列表
        public static final String new_work_list = "new_work_list";
        //未读官方消息数
        public static final String message_unread = "message/unreadNew";
        //购买vip
        public static final String buy_vip = "buy_vip";
        //隐藏VIP
        public static final String vip_hide = "vip_hide";
        //添加照片墙
        public static String savePhotos = "savePhotos";
        //照片墙删除
        public static String deletePhotos = "deletePhotos";
        //汽车品牌型号
        public static String car_type_list = "car_type_list";
        //车产认证 添加车标之后 调用此接口
        public static String saveCar = "saveCar";
        //朋友圈消息
        public static String friends_message_list = "feed_list";
        //新首页接口
        public static String newHomePage_work_list = "work_list";
        //话题列表页面
        public static String topicWorks_list = "topicWorks_list";
        // 首页同城(最新)列表(新版 4.0)
        public static String new_list = "new_list";
        //解锁聊天窗口
        public static String chat_unlock = "unlockChat";
        //个人中心页面作品列表
        public static String works_list = "works_list";
        //个人中心页面我喜欢的列表
        public static String get_like_works = "get_like_works";
        //相似作品
        public static String similar_works_list = "similar_works";
        //精选推荐列表
        public static String suggest_list = "suggest_list";
        //评论列表
        public static String commentList = "comment/list";
        //评论操作
        public static String commentOption = "comment/comment";
        //评论点赞操作
        public static String commentLike = "comment/like";
        //打赏人列表
        public static String rewardList = "rank/defenderList";
        //删除作品接口
        public static String delete_works = "delete_works";

        //人脸识别
        public static String checkFace = "detection/face";
        //点赞接口
        public static String to_like_work = "stat/like";
        //取消点赞
        public static String dislike = "stat/dislike";
        //动态圈里面的消息点赞
        public static String message_like = "message/like";
        //动态圈取消点赞
        public static String message_unLike = "message/unLike";
        //获取阿里云配置接口
        public static String getAliyunConfig = "aliyun/getToken";
        //关注的人的动态的接口
        public static String follow_work_list = "follow_work_list";
        //添加不感兴趣的人
        public static String add_uninterested_user = "add_uninterested_user";

        //获取用户关系接口()
        public static String relation = "relation";

        //获取通讯录用户关系
        public static String invite_from_contact = "invite_from_contact";

        //获取用户好友
        public static String user_friend_list = "user_friend_list";

        //好友申请列表
        public static String friend_apply_list = "friend_apply_list";

        //删除好友申请
        public static String delete_friend_apply = "delete_friend_apply";


        //发送私聊打点
        public static String chat_dot = "stat/chat";
        //获取签到状态
        public static String sign_info = "mission/sign_info";
        //签到
        public static String sign_in = "mission/sign_in";
        //官方消息的个人中心页面
        public static String official_list = "official_list";


        //获取最近在线人数
        public static String online_suggest_user = "online_suggest_user";
        //查询用户是否在线(融云)
        public static String user_online_status = "user_online_status";
        //删除最近在线的用户
        public static String delete_online_suggest = "delete_online_suggest";


        //发送礼物
        public static String gift_send = "sendGift";
        //礼物列表
        public static String gift_list = "resource_list";
        //修改照片顺序的
        public static String updatePhotoSort = "updatePhotoSort";
        //服务器时间
        public static String synchro_time = "synchro_time";
        //获取地区信息的
        public static String getProvince = "getProvince";
        //获取录制时音乐素材列表
        public static String getRecorderMusicList = "musicList";

        //服务端打点
        public static String stat_action = "stat/action";
        //动态圈
        public static String feed_list = "feed_list";
        //ar红包
        public static String ar_config = "resource/ar_config";
        public static String ar_model_config = "resource/ar_model_config";
        public static String matchPlayGame = "game/ar/matchPlayGame";
        //话题吃红包列表的数据
        public static String topicGameVideoList = "game/ar/topicGameVideoList";


        //获取游戏状态
        public static String ar_game_info = "game/ar/getUserPlayGameInfo";
        //开启AR游戏
        public static String ar_game_start = "game/ar/playGame";
        //结束AR游戏
        public static String ar_game_end = "game/ar/game_over";

        //魔法列表
        public static String magic_list = "resource_list";
        //魔法特效资源包下载
        public static String resource_magic = "resource/magic";

        //获得用户分享页
        public static String get_personal_share_picture = "get_personal_share_picture";

    }
}
