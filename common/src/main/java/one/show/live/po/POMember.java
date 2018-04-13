package one.show.live.po;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.List;

import one.show.live.po.event.EBORefreshBeike;
import one.show.live.po.event.EFixLabel;
import one.show.live.util.StringUtils;
import one.show.live.util.UserInfoCacheManager;

public class POMember extends POLogin implements Serializable {


    /**
     * birthday : 1988-09-20
     * constellation :
     * thirdBinds : ["phone"]
     * fans : 0
     * city : 北京市
     * height : 0
     * description : 这个人很懒，什么都没留下
     * age : 29
     * isFollowed : false
     * carTypeName : AC Schnitzer ACS6
     * carTypeUrl : http://video-01.ws.seeulive.cn/5912c1243a07ac651c235c54.png
     * follow : 2
     * tags : ["白领"]
     * lastLoginType :
     * carBrandUrl : http://video-01.ws.seeulive.cn/5909606159f3ac654f19e789.png
     * notifyConfig : 63
     * carBrand : 1041
     * isFans : false
     * unreadMsg : 0
     */

    private String birthday;
    private String constellation;
    private int fans;
    private String city;
    private int height;
    private int age;
    private boolean isFollowed;
    private String carTypeName;
    private String carTypeUrl;
    private int follow;
    private String lastLoginType;
    private String carBrandUrl;
    private String distance;
    private int notifyConfig;
    private int carBrand;
    private int isVipHide;//是否隐藏VIP
    private boolean isFans;
    private int unreadMsg;
    private int officialVerifyStatus;//官方身份认证状态  //状态值。 0。未提交。 1 提交待审核。2。认证通过。 3认证拒绝
    private String officialVerifyInfo;//身份认证信息
//    private String sinaVerifyInfo;//新浪认证信息
    private List<String> thirdBinds;
    private List<POPhotos> photos;//照片墙
    private List<Leaber> me_tags;//我的标签
    private List<Leaber> like_tags;//我喜欢的标签
    private String signature;
    private POinviteUser inviteUser;//邀请人的数据
    private String taskSlogan;//新手任务入口提示语
    private String createTime;//注册时间
    private int workCount;
    private int shared;//活动分享 0没有分享过 1分享过
    private int gameShared;//AR红包分享 0没有分享过 1分享过
    private int sharePoints;//积分分享的时候，显示在右上角的积分数
    private int gamePoints;//吃红包活动的积分
    private int gameShareTimes;//吃红包分享能获得游戏次数
    private int profileImgChanged;//判断微信登录的用户有没有修改过头像
    private int points;//我的积分
    private int showExtractRmb;//是否显示可提现人民币数，1：显示，0：不显示

    public int getShowExtractRmb() {
        return showExtractRmb;
    }

    public boolean isShowExtractRmb(){
        return showExtractRmb==1?true:false;
    }

    public void setShowExtractRmb(int showExtractRmb) {
        this.showExtractRmb = showExtractRmb;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getProfileImgChanged() {
        return profileImgChanged;
    }

    public void setProfileImgChanged(int profileImgChanged) {
        this.profileImgChanged = profileImgChanged;
    }

    public int getGameShareTimes() {
        return gameShareTimes;
    }

    public void setGameShareTimes(int gameShareTimes) {
        this.gameShareTimes = gameShareTimes;
    }

    public int getGamePoints() {
        return gamePoints;
    }

    public void setGamePoints(int gamePoints) {
        this.gamePoints = gamePoints;
    }

    public int getSharePoints() {
        return sharePoints;
    }

    public void setSharePoints(int sharePoints) {
        this.sharePoints = sharePoints;
    }

    public int getGameShared() {
        return gameShared;
    }

    public boolean isGameShared(){

        return gameShared==0;
    }

    public void setGameShared(int gameShared) {
        this.gameShared = gameShared;
    }

    public int getShared() {
        return shared;
    }

    public boolean isShared(){//为0就是没有分享过
        return shared==0;
    }

    public void setShared(int shared) {
        this.shared = shared;
    }


    public int getWorkCount() {
        return workCount;
    }

    public void setWorkCount(int workCount) {
        this.workCount = workCount;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getTaskSlogan() {
        return taskSlogan;
    }

    public void setTaskSlogan(String taskSlogan) {
        this.taskSlogan = taskSlogan;
    }

    public POinviteUser getInviteUser() {
        return inviteUser;
    }

    public void setInviteUser(POinviteUser inviteUser) {
        this.inviteUser = inviteUser;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    private boolean unlock;//true/false       //是否解锁过他的作品
    private int unlockChatAmount;// 解锁所需要的星币

    private int isBlack;//1  是拉黑

    private int shareRewardTimes;//分享奖励剩余次数

    private int visitCount;//最近访问人数

    private long lastActiveTime;//离线时间

    public long getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(long lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public int getOfficialVerifyStatus() {
        return officialVerifyStatus;
    }

    public void setOfficialVerifyStatus(int officialVerifyStatus) {
        this.officialVerifyStatus = officialVerifyStatus;
    }

    public String getOfficialVerifyInfo() {
        return officialVerifyInfo;
    }

    public void setOfficialVerifyInfo(String officialVerifyInfo) {
        this.officialVerifyInfo = officialVerifyInfo;
    }

//    public String getSinaVerifyInfo() {
//        return sinaVerifyInfo;
//    }
//
//    public void setSinaVerifyInfo(String sinaVerifyInfo) {
//        this.sinaVerifyInfo = sinaVerifyInfo;
//    }

    public boolean isBoy() {
        return getGender() == 1;
    }


    public boolean isBlack() {
        return isBlack == 1;
    }

    public int getIsBlack() {
        return isBlack;
    }

    public int getIsVipHide() {
        return isVipHide;
    }

    public void setIsVipHide(int isVipHide) {
        this.isVipHide = isVipHide;
    }

    public boolean isVipHide(){
        return isVipHide == 1;
    }

    public void setIsBlack(int isBlack) {
        this.isBlack = isBlack;
    }

    public boolean isUnlock() {
        return unlock;
    }

    public int getUnlockChatAmount() {
        return unlockChatAmount;
    }

    public void setUnlock(boolean unlock) {
        this.unlock = unlock;
    }

    public void setUnlockChatAmount(int unlockChatAmount) {
        this.unlockChatAmount = unlockChatAmount;
    }

    public List<POPhotos> getPhotos() {
        return photos;
    }

    public void setMe_tags(List<Leaber> me_tags) {
        this.me_tags = me_tags;
    }

    public void setLike_tags(List<Leaber> like_tags) {
        this.like_tags = like_tags;
    }

    public List<Leaber> getMe_tags() {

        return me_tags;
    }

    public List<Leaber> getLike_tags() {
        return like_tags;
    }

    public void setPhotos(List<POPhotos> photos) {
        this.photos = photos;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getDistance() {

        return distance;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getConstellation() {
        return constellation;
    }

    public void setConstellation(String constellation) {
        this.constellation = constellation;
    }

    public int getFans() {
        return fans;
    }

    public void setFans(int fans) {
        this.fans = fans;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isIsFollowed() {
        return isFollowed;
    }

    public void setIsFollowed(boolean isFollowed) {
        this.isFollowed = isFollowed;
    }

    public String getCarTypeName() {
        return carTypeName;
    }

    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    public String getCarTypeUrl() {
        return carTypeUrl;
    }

    public void setCarTypeUrl(String carTypeUrl) {
        this.carTypeUrl = carTypeUrl;
    }

    public int getFollow() {
        return follow;
    }

    public void setFollow(int follow) {
        this.follow = follow;
    }

    public String getLastLoginType() {
        return lastLoginType;
    }

    public void setLastLoginType(String lastLoginType) {
        this.lastLoginType = lastLoginType;
    }

    public String getCarBrandUrl() {
        return carBrandUrl;
    }

    public void setCarBrandUrl(String carBrandUrl) {
        this.carBrandUrl = carBrandUrl;
    }

    public int getNotifyConfig() {
        return notifyConfig;
    }

    public void setNotifyConfig(int notifyConfig) {
        this.notifyConfig = notifyConfig;
    }

    public int getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(int carBrand) {
        this.carBrand = carBrand;
    }

    public boolean isIsFans() {
        return isFans;
    }

    public void setIsFans(boolean isFans) {
        this.isFans = isFans;
    }

    public int getUnreadMsg() {
        return unreadMsg;
    }

    public void setUnreadMsg(int unreadMsg) {
        this.unreadMsg = unreadMsg;
    }

    public List<String> getThirdBinds() {
        return thirdBinds;
    }

    public void setThirdBinds(List<String> thirdBinds) {
        this.thirdBinds = thirdBinds;
    }

    public int getShareRewardTimes() {
        return shareRewardTimes;
    }

    public void setShareRewardTimes(int shareRewardTimes) {
        this.shareRewardTimes = shareRewardTimes;
    }


    public static enum LoginType implements Serializable {
        unknow("unknow"), sina("sina"), qq("qq"), weixin("weixin"), phone("phone");

        public String type;

        LoginType(String type) {
            this.type = type;
        }

    }

    private static POMember mInstanceMember;


    /**
     * 获取当前登陆的用户实例
     */
    public static POMember getInstance() {
        if (mInstanceMember == null) {
            synchronized (POMember.class) {
                if (mInstanceMember == null) {
                    mInstanceMember = new Gson().fromJson(UserInfoCacheManager.getInstance().getValue("member", "{}"), POMember.class);
                }
            }
        }

        if (mInstanceMember == null) {
            return new POMember();
        }

        return mInstanceMember;
    }

    /**
     * 是否登陆
     */
    public static boolean isLogin() {
        return StringUtils.isNotEmpty(getInstance().getUid());
    }

    public static void login(POMember POMember) {
        login(POMember, LoginType.unknow);
    }

    /**
     * 登陆
     *
     * @param poMember 登陆接口返回数据，见POLogin
     * @param type     登陆类型
     */
    public static void login(POMember poMember, LoginType type) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setUid(poMember.getUid());
        POMember.mInstanceMember.setIsVip(poMember.getIsVip());
        POMember.mInstanceMember.setIsNew(poMember.getIsNew());
        POMember.mInstanceMember.setVideoVerifyStatus(poMember.getVideoVerifyStatus());
        POMember.mInstanceMember.setVipEndTime(poMember.getVipEndTime());
        POMember.mInstanceMember.setProfileImg(poMember.getProfileImg());
        POMember.mInstanceMember.setThumbnail(poMember.getThumbnail());
        POMember.mInstanceMember.setPopularNo(poMember.getPopularNo());
        POMember.mInstanceMember.setPhoneNumber(poMember.getPhoneNumber());
        POMember.mInstanceMember.setNickName(poMember.getNickName());
        POMember.mInstanceMember.setDiamond(poMember.getDiamond());
        POMember.mInstanceMember.setBackground(poMember.getBackground());
        POMember.mInstanceMember.setGender(poMember.getGender());
        POMember.mInstanceMember.setCarVerifyStatus(poMember.getCarVerifyStatus());
        POMember.mInstanceMember.setGold(poMember.getGold());
        POMember.mInstanceMember.setBeke_token(poMember.getBeke_token());
        POMember.mInstanceMember.setRy_token(poMember.getRy_token());
        POMember.mInstanceMember.setCity(poMember.getCity());
        POMember.mInstanceMember.setIsVipHide(poMember.getIsVipHide());
        POMember.mInstanceMember.lastLoginType = poMember.lastLoginType = type.type;

        //下面这是邀请人的数据
        POMember.mInstanceMember.setInviteUser(poMember.getInviteUser());

        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }

    /**
     * 添加个人用户资料
     *
     * @param memberInfo
     */

    public static void loginData(POMember memberInfo) {
        POMember.mInstanceMember = getInstance();

        POMember.mInstanceMember.setUid(memberInfo.getUid());
        POMember.mInstanceMember.setIsVip(memberInfo.getIsVip());
        POMember.mInstanceMember.setVideoVerifyStatus(memberInfo.getVideoVerifyStatus());
        POMember.mInstanceMember.setVipEndTime(memberInfo.getVipEndTime());
        POMember.mInstanceMember.setProfileImg(memberInfo.getProfileImg());
        POMember.mInstanceMember.setThumbnail(memberInfo.getThumbnail());
        POMember.mInstanceMember.setPopularNo(memberInfo.getPopularNo());
        POMember.mInstanceMember.setPhoneNumber(memberInfo.getPhoneNumber());
        POMember.mInstanceMember.setNickName(memberInfo.getNickName());
        POMember.mInstanceMember.setDiamond(memberInfo.getDiamond());
        POMember.mInstanceMember.setGender(memberInfo.getGender());
        POMember.mInstanceMember.setCarVerifyStatus(memberInfo.getCarVerifyStatus());
        POMember.mInstanceMember.setGold(memberInfo.getGold());
        POMember.mInstanceMember.setCity(memberInfo.getCity());
        ///上面是和login共有的参数
        POMember.mInstanceMember.setBirthday(memberInfo.getBirthday());
        POMember.mInstanceMember.setConstellation(memberInfo.getConstellation());
        POMember.mInstanceMember.setFans(memberInfo.getFans());
        POMember.mInstanceMember.setHeight(memberInfo.getHeight());
        POMember.mInstanceMember.setDescription(memberInfo.getDescription());
        POMember.mInstanceMember.setAge(memberInfo.getAge());
        POMember.mInstanceMember.setIsFollowed(memberInfo.isIsFollowed());
        POMember.mInstanceMember.setCarTypeName(memberInfo.getCarTypeName());
        POMember.mInstanceMember.setCarTypeUrl(memberInfo.getCarTypeUrl());
        POMember.mInstanceMember.setFollow(memberInfo.getFollow());
        POMember.mInstanceMember.setLastLoginType(memberInfo.getLastLoginType());
        POMember.mInstanceMember.setCarBrandUrl(memberInfo.getCarBrandUrl());
        POMember.mInstanceMember.setNotifyConfig(memberInfo.getNotifyConfig());
        POMember.mInstanceMember.setCarBrand(memberInfo.getCarBrand());
        POMember.mInstanceMember.setIsFans(memberInfo.isIsFans());
        POMember.mInstanceMember.setUnreadMsg(memberInfo.getUnreadMsg());
        POMember.mInstanceMember.setThirdBinds(memberInfo.getThirdBinds());
        POMember.mInstanceMember.setPhotos(memberInfo.getPhotos());
        POMember.mInstanceMember.setLike_tags(memberInfo.getLike_tags());
        POMember.mInstanceMember.setMe_tags(memberInfo.getMe_tags());
        POMember.mInstanceMember.setDistance(memberInfo.getDistance());
        POMember.mInstanceMember.setUnlock(memberInfo.isUnlock());
        POMember.mInstanceMember.setUnlockChatAmount(memberInfo.getUnlockChatAmount());
        POMember.mInstanceMember.setOfficialVerifyInfo(memberInfo.getOfficialVerifyInfo());
        POMember.mInstanceMember.setOfficialVerifyStatus(memberInfo.getOfficialVerifyStatus());
        POMember.mInstanceMember.setSinaVerifyInfo(memberInfo.getSinaVerifyInfo());
        POMember.mInstanceMember.setShareRewardTimes(memberInfo.getShareRewardTimes());
        POMember.mInstanceMember.setIsVipHide(memberInfo.getIsVipHide());
        POMember.mInstanceMember.setTaskSlogan(memberInfo.getTaskSlogan());
        POMember.mInstanceMember.setCreateTime(memberInfo.getCreateTime());
        POMember.mInstanceMember.setWorkCount(memberInfo.getWorkCount());
        POMember.mInstanceMember.setShared(memberInfo.getShared());
        POMember.mInstanceMember.setGameShared(memberInfo.getGameShared());
        POMember.mInstanceMember.setSharePoints(memberInfo.getSharePoints());
        POMember.mInstanceMember.setGamePoints(memberInfo.getGamePoints());
        POMember.mInstanceMember.setGameShareTimes(memberInfo.getGameShareTimes());
        POMember.mInstanceMember.setProfileImgChanged(memberInfo.getProfileImgChanged());
        POMember.mInstanceMember.setPoints(memberInfo.getPoints());
        POMember.mInstanceMember.setShowExtractRmb(memberInfo.getShowExtractRmb());

        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));

    }

    public POLogin getPoLogin() {
        return new POLogin(getId(), getUid(), getIsVip(), getIsNew(), getVideoVerifyStatus(), getVipEndTime(), getProfileImg(), getThumbnail(), getPopularNo(), getPhoneNumber(), getNickName(), getDiamond(), getBackground(), getGender(), getCarVerifyStatus(), getGold(), getRy_token(), getBeke_token(), getRemark(), getDescription(),getSinaVerifyInfo());
    }

    public POMember() {

    }

    public POMember(POLogin poLogin) {
        if (poLogin != null) {
            setId(poLogin.getId());
            setUid(poLogin.getUid());
            setIsVip(poLogin.getIsVip());
            setIsNew(poLogin.getIsNew());
            setVideoVerifyStatus(poLogin.getVideoVerifyStatus());
            setVipEndTime(poLogin.getVipEndTime());
            setProfileImg(poLogin.getProfileImg());
            setThumbnail(poLogin.getThumbnail());
            setPopularNo(poLogin.getPopularNo());
            setPhoneNumber(poLogin.getPhoneNumber());
            setNickName(poLogin.getNickName());
            setBackground(poLogin.getBackground());
            setGender(poLogin.getGender());
            setCarVerifyStatus(poLogin.getCarVerifyStatus());
            setGold(poLogin.getGold());
            setDiamond(poLogin.getDiamond());
            setRy_token(poLogin.getRy_token());
            setRemark(poLogin.getRemark());
            setDescription(poLogin.getDescription());
        }


    }

    /**
     * 这个是谁邀请我，邀请人的数据
     */
    public class POinviteUser implements Serializable{

        /**
         * uid : 903221716942192640
         * profileImg : http://images.supe.tv/59bbd1ceca90dcc2b00e35e6.jpg
         * nickName : hank0454
         */

        private String uid;
        private String profileImg;
        private String nickName;
        private String popularNo;

        public String getPopularNo() {
            return popularNo;
        }

        public void setPopularNo(String popularNo) {
            this.popularNo = popularNo;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getProfileImg() {
            return profileImg;
        }

        public void setProfileImg(String profileImg) {
            this.profileImg = profileImg;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }
    }

    /**
     * 修改用户资料
     *
     * @param memberInfo
     */

    public static void loginModify(POMember memberInfo) {
        POMember.mInstanceMember = getInstance();
//        POMember.mInstanceMember.setConstellation(memberInfo.getConstellation());
        POMember.mInstanceMember.setBirthday(memberInfo.getBirthday());
//        POMember.mInstanceMember.setCity(memberInfo.getCity());
//        POMember.mInstanceMember.setDescription(memberInfo.getDescription());
//        POMember.mInstanceMember.setGender(memberInfo.getGender());

        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));

    }

    /**
     * 修改性别
     *
     * @param sex
     */
    public static void fixGender(int sex) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setGender(sex);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }

    /**
     * 修改新浪认证状态
     *
     * @param SinaVerify
     */
    public static void fixSinaVerify(String SinaVerify) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setSinaVerifyInfo(SinaVerify);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }

    /**
     * 修改关注人数
     *
     * @param bool true加关注   false 取消关注
     * @param num
     */
    public static void fixFocus(boolean bool, int num) {
        POMember.mInstanceMember = getInstance();
        if (bool) {
            POMember.mInstanceMember.setFollow(getInstance().follow + num);
        } else {
            POMember.mInstanceMember.setFollow(getInstance().follow - num >= 0 ? getInstance().follow - num : 0);
        }

        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }

    /**
     * 修改头像
     *
     * @param ProfileImgUrl
     */

    public static void loginProfileImg(String ProfileImgUrl) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setProfileImg(ProfileImgUrl);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));

    }

    /**
     * 修改背景
     *
     * @param ProfileImgUrl
     */

    public static void loginBackground(String ProfileImgUrl) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setBackground(ProfileImgUrl);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));

    }

    /**
     * 修改新用户状态
     *
     * @param type
     */

    public static void fixIsNew(int type) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setIsNew(type);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));

    }

    /**
     * 修改分享完成之后返回的数据
     *
     * @param shared
     * @param gameShared
     * @param sharePoints
     */

    public static void fixShared(int shared,int gameShared,int sharePoints) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setShared(shared);
        POMember.mInstanceMember.setGameShared(gameShared);
        POMember.mInstanceMember.setSharePoints(sharePoints);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));

    }

    /**
     * 修改相册
     *
     * @param photos
     */

    public static void fixPhotos(List<POPhotos> photos) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setPhotos(photos);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));

    }


    /**
     * 增加第三方绑定状态
     *
     * @param thirdType
     */
    public static void bindingThird(String thirdType) {
        POMember.mInstanceMember = getInstance();
        List<String> mList = getInstance().getThirdBinds();
        mList.add(thirdType);
        POMember.mInstanceMember.setThirdBinds(mList);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }


    public static void updateUserName(String userName) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setNickName(userName);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }

    public static void updateProfileImg(String profileImg) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setProfileImg(profileImg);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }

    public static void updateDiamond(int beike) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setDiamond(beike);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
        EBORefreshBeike.sendRefreshBeikeEvent();//有星钻的变动发通知
    }

    public static void updateGold(int zhenzhu) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setGold(zhenzhu);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
//        EBORefreshBeike.sendRefreshBeikeEvent();//有星币的变动发送通知
    }


    public static void updatePopularNo(String popularNo) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setPopularNo(popularNo);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }

    public static void updateDescription(String desc) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setDescription(desc);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }


    /**
     * 去掉第三方绑定状态
     *
     * @param thirdType
     */
    public static void unBindingThird(String thirdType) {
        POMember.mInstanceMember = getInstance();
        List<String> mList = getInstance().getThirdBinds();
        mList.remove(thirdType);
        POMember.mInstanceMember.setThirdBinds(mList);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));

    }

    /**
     * 绑定完成的手机号添加
     *
     * @param phoneNum
     */
    public static void addPhoneNum(String phoneNum) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setPhoneNumber(phoneNum);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }

    /**
     * 修改我的标签
     *
     * @param leabers
     */
    public static void fixMyLeaber(List<Leaber> leabers) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setMe_tags(leabers);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }

    /**
     * 修改我喜欢的标签
     *
     * @param leabers
     */
    public static void fixLikeLeaber(List<Leaber> leabers) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setLike_tags(leabers);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
        EFixLabel.sendRefreshBeikeEvent(EFixLabel.FIX_LEABEL);
    }

    /**
     * 修改消息通知的状态
     *
     * @param num
     */
    public static void fixNotifyConfig(int num) {

        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setNotifyConfig(num);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }

    /**
     * 修改视频认证状态
     *
     * @param state
     */

    public static void fixVideoState(int state) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setVideoVerifyStatus(state);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }

    /**
     * 修改车产认证状态
     *
     * @param state
     */

    public static void fixCarState(int state) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setCarVerifyStatus(state);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }

    /**
     * 修改官方认证状态
     *
     * @param state
     */

    public static void fixInfoState(int state) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setOfficialVerifyStatus(state);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }

    /**
     * 修改VIP隐藏状态
     *
     * @param state
     */

    public static void fixVipHideState(int state) {
        POMember.mInstanceMember = getInstance();
        POMember.mInstanceMember.setIsVipHide(state);
        getInstance().setIsVipHide(state);
        UserInfoCacheManager.getInstance().setValue("member", new Gson().toJson(POMember.mInstanceMember));
    }

    /**
     * 退出登录
     */
    public static void logout() {
        POMember.mInstanceMember = new POMember();
        UserInfoCacheManager.getInstance().remove("member");
    }

    public class Leaber implements Serializable {
        int catalog;
        String name;

        public void setCatalog(int catalog) {
            this.catalog = catalog;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCatalog() {

            return catalog;
        }

        public String getName() {
            return name;
        }
    }

    public static boolean isCurrentUser(String userId) {
//        return StringUtils.isNotEmpty(userId) ? getInstance().getUid().equals(userId) : false;
        return TextUtils.equals(userId, getInstance().getUid());
    }

    public boolean isNewUser() {
        //手机用户登录并且isnew>0才会弹出去修改资料的弹窗  或者微信用户登录isnew>1
        if(lastLoginType!=null){

            return (lastLoginType.equals("phone") && isNew > 0) || (lastLoginType.equals("weixin") && isNew > 1);
        }else{
            return false;
        }
    }
}
