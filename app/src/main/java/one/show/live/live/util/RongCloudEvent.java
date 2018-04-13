package one.show.live.live.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.view.View;

import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.provider.CameraInputProvider;
import io.rong.imkit.widget.provider.ImageInputProvider;
import io.rong.imkit.widget.provider.InputProvider;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.PublicServiceMultiRichContentMessage;
import io.rong.message.PublicServiceRichContentMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

import java.util.HashMap;
import java.util.Map;

import org.greenrobot.eventbus.EventBus;

import one.show.live.R;
import one.show.live.po.POEventBus;
import one.show.live.po.POMember;
import one.show.live.util.ActivityCacheTask;
import one.show.live.util.Constants;
import one.show.live.util.WeakHandler;
import one.show.live.view.ToastUtils;
import one.show.live.conversation.ui.PhotoActivity;
import one.show.live.conversation.widget.GiftProvider;
import one.show.live.live.po.POIMCustomeMsgType;
import one.show.live.live.po.POIMMsg;
import one.show.live.live.po.POIMSystemMsg;
import one.show.live.live.po.POIMSystemMsgType;
import one.show.live.log.Logger;
import one.show.live.login.ui.LoginActivity;
import one.show.live.personal.model.MyPersonalRequest;
import one.show.live.personal.ui.OthersDetailsActivity;
import one.show.live.conversation.widget.TextInputProvider;
import one.show.live.util.EventBusKey;
import one.show.live.util.PreferenceUtils;

/**
 * Created by zhjchen on 1/29/15.
 */

/**
 * 融云SDK事件监听处理。
 * 把事件统一处理，开发者可直接复制到自己的项目中去使用。
 * <p/>
 * 该类包含的监听事件有：
 * 1、消息接收器：OnReceiveMessageListener。
 * 2、发出消息接收器：OnSendMessageListener。
 * 3、用户信息提供者：GetUserInfoProvider。
 * 4、好友信息提供者：GetFriendsProvider。
 * 5、群组信息提供者：GetGroupInfoProvider。
 * 7、连接状态监听器，以获取连接相关状态：ConnectionStatusListener。
 * 8、地理位置提供者：LocationProvider。
 * 9、自定义 push 通知： OnReceivePushMessageListener。
 * 10、会话列表界面操作的监听器：ConversationListBehaviorListener。
 */
public final class RongCloudEvent
        implements RongIMClient.OnReceiveMessageListener, RongIM.OnSendMessageListener,
        RongIM.UserInfoProvider, RongIM.ConversationBehaviorListener,
        RongIMClient.ConnectionStatusListener, RongIM.ConversationListBehaviorListener {

    private static final String TAG = RongCloudEvent.class.getSimpleName();

    public static final int KICKED_BY_OTHER_CLIENT = 0x10;

    private static RongCloudEvent mRongCloudInstance;

    private Context mContext;

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {

        if (mRongCloudInstance == null) {

            synchronized (RongCloudEvent.class) {

                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongCloudEvent(context);
                }
            }
        }
    }

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    private RongCloudEvent(Context context) {
        mContext = context;
        initDefaultListener();
        setOtherListener();
        setCurrentUserInfo();
        RLog.setLevel(RLog.ERROR);
    }

    @Override
    public boolean onConversationPortraitClick(Context context,
                                               Conversation.ConversationType conversationType, String targetId) {
        return false;
    }

    @Override
    public boolean onConversationPortraitLongClick(Context context,
                                                   Conversation.ConversationType conversationType, String targetId) {
        return false;
    }

    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static RongCloudEvent getInstance() {
        return mRongCloudInstance;
    }

    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
    private void initDefaultListener() {
        RongIM.setUserInfoProvider(this, true);//设置用户信息提供者。
        RongIM.setOnReceiveMessageListener(this);//设置消息接收监听器。
        RongIM.setConnectionStatusListener(this);//设置连接状态监听器。
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器。
        RongIM.getInstance().setSendMessageListener(this);

        //        RongIM.setGroupInfoProvider(this, true);//设置群组信息提供者。
        //        RongIM.setLocationProvider(this);//设置地理位置提供者,不用位置的同学可以注掉此行代码
        //        RongIM.setConversationListBehaviorListener(this);//会话列表界面操作的监听器
        //设置发出消息接收监听器.

        //RongIM.setOnReceivePushMessageListener(this);//自定义 push 通知。
        //        RongIM.setGroupUserInfoProvider(this, true);
        //消息体内是否有 userinfo 这个属性
        //        RongIM.getInstance().setMessageAttachedUserInfo(true);
    }

    /**
     * 连接成功注册。
     * <p/>
     * 在RongIM-connect-onSuccess后调用。
     */
    public void setOtherListener() {
        TextInputProvider textInputProvider = new TextInputProvider(RongContext.getInstance());
        RongIM.setPrimaryInputProvider(textInputProvider);

        //        扩展功能自定义
        InputProvider.ExtendProvider[] provider = {
                new ImageInputProvider(RongContext.getInstance()),//图片
                new CameraInputProvider(RongContext.getInstance()),//相机
                new GiftProvider(RongContext.getInstance()) //礼物O
        };

        RongIM.resetInputExtensionProvider(Conversation.ConversationType.PRIVATE, provider);
    }

    private Bitmap getAppIcon() {
        BitmapDrawable bitmapDrawable;
        Bitmap appIcon;
        bitmapDrawable = (BitmapDrawable) RongContext.getInstance()
                .getApplicationInfo()
                .loadIcon(RongContext.getInstance().getPackageManager());
        appIcon = bitmapDrawable.getBitmap();
        return appIcon;
    }

    /**
     * 接收消息的监听器：OnReceiveMessageListener 的回调方法，接收到消息后执行。
     *
     * @param message 接收到的消息的实体信息。
     * @param left    剩余未拉取消息数目。
     */
    @Override
    public boolean onReceived(Message message, int left) {
        MessageContent messageContent = message.getContent();
        if (messageContent instanceof TextMessage && message.getConversationType() == Conversation.ConversationType.CHATROOM) {//文本消息
                EventBus.getDefault().post(message);
        } else if (messageContent instanceof POIMCustomeMsgType) {//服务器自定义消息
            if (message.getConversationType() == Conversation.ConversationType.CHATROOM) {
                POIMCustomeMsgType poImMsg = (POIMCustomeMsgType) messageContent;
                POIMMsg msg = poImMsg.parseMsg();
                if (msg != null) {
                    EventBus.getDefault().post(msg);
                }
            }
        } else if (messageContent instanceof POIMSystemMsgType) {//服务器自定义消息，系统消息
            POIMSystemMsgType poImMsg = (POIMSystemMsgType) messageContent;
            POIMSystemMsg msg = poImMsg.parseMsg();
            updateSystemMsg(msg);
        } else if (messageContent instanceof ImageMessage) {//图片消息
            ImageMessage imageMessage = (ImageMessage) messageContent;
            Logger.d(TAG, "onReceived-ImageMessage:" + imageMessage.getRemoteUri());
        } else if (messageContent instanceof VoiceMessage) {//语音消息
            VoiceMessage voiceMessage = (VoiceMessage) messageContent;
            Logger.d(TAG, "onReceived-voiceMessage:" + voiceMessage.getUri().toString());
        } else if (messageContent instanceof RichContentMessage) {//图文消息
            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
            Logger.d(TAG, "onReceived-RichContentMessage:" + richContentMessage.getContent());
        } else if (messageContent instanceof InformationNotificationMessage) {//小灰条消息
            InformationNotificationMessage informationNotificationMessage =
                    (InformationNotificationMessage) messageContent;
            Logger.e(TAG, "onReceived-informationNotificationMessage:"
                    + informationNotificationMessage.getMessage());
            //            if (DemoContext.getInstance() != null)
            //                getFriendByUserIdHttpRequest = DemoContext.getInstance().getDemoApi().getUserInfoByUserId(message.getSenderUserId(), (ApiCallback<User>) this);
        }

        return false;
    }

    private void updateSystemMsg(POIMSystemMsg msg) {
        if (msg == null)
            return;

        try {
            switch (msg.getType()) {
                case 1:
                    POMember.updateUserName(msg.getValue());
                    break;
                case 2:
                    POMember.updateProfileImg(msg.getValue());
                    break;
                case 3:
                    POMember.updateBeike(Integer.parseInt(msg.getValue()));
                    break;
                case 4:
                    POMember.updateZhenzhu(Integer.parseInt(msg.getValue()));
                    break;
                case 5:
                    POMember.updateVip(Integer.parseInt(msg.getValue()));
                    break;
                case 6:
                    POMember.updateAdmin(Integer.parseInt(msg.getValue()));
                    break;
                case 7:
                    POMember.updatePopularNo(msg.getValue());
                    break;
                case 8:
                    POMember.updateDescription(msg.getValue());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息发送前监听器处理接口（是否发送成功可以从SentStatus属性获取）。
     *
     * @param message 发送的消息实例。
     * @return 处理后的消息实例。
     */
    @Override
    public Message onSend(Message message) {
        MessageContent messageContent = message.getContent();
        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            Logger.e(TAG, "--onSend:" + textMessage.getContent() + ", extra=" + message.getExtra());
        }
        return message;
    }

    /**
     * 消息在UI展示后执行/自己的消息发出后执行,无论成功或失败。
     *
     * @param message 消息。
     */
    @Override
    public boolean onSent(Message message,
                          RongIM.SentMessageErrorCode sentMessageErrorCode) {
        Logger.e(TAG, "onSent:" + message.getObjectName() + ", extra=" + message.getExtra() + "jjjjjj<<<" + message.getSentStatus().getValue() + "sentMessageErrorCode>>>>" + ((sentMessageErrorCode != null) ? sentMessageErrorCode.getValue() : "000"));


        if (message.getSentStatus() == Message.SentStatus.SENT) {
            if (message.getContent() instanceof TextMessage && message.getConversationType() == Conversation.ConversationType.CHATROOM) {//文本消息并且是聊天室消息
                EventBus.getDefault().post(message);
            }
        } else if (message.getSentStatus() == Message.SentStatus.FAILED) {
            if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_CHATROOM) {//不在聊天室
                ToastUtils.showToast(mContext, "聊天室已经断开");
            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_DISCUSSION) {//不在讨论组

            } else if (sentMessageErrorCode == RongIM.SentMessageErrorCode.NOT_IN_GROUP) {//不在群组

            } else if (sentMessageErrorCode
                    == RongIM.SentMessageErrorCode.REJECTED_BY_BLACKLIST) {//你在他的黑名单中
                ToastUtils.showToast(mContext, "你在对方的黑名单中");
            }

            //聊天室的发送消息如果是黑名单的话，错误是UNKNOW，所以暂时不提示
        } else {
//      MessageContent messageContent = message.getContent();
//      if (messageContent instanceof TextMessage) {//文本消息
//        TextMessage textMessage = (TextMessage) messageContent;
//        if(message.getConversationType() == Conversation.ConversationType.CHATROOM) {
//          EventBus.getDefault().post(textMessage);
//        }
//        Logger.e(TAG, "onSent-TextMessage:" + textMessage.getContent());
//      } else if (messageContent instanceof ImageMessage) {//图片消息
//        ImageMessage imageMessage = (ImageMessage) messageContent;
//        Logger.e(TAG, "onSent-ImageMessage:" + imageMessage.getRemoteUri());
//      } else if (messageContent instanceof VoiceMessage) {//语音消息
//        VoiceMessage voiceMessage = (VoiceMessage) messageContent;
//        Logger.e(TAG, "onSent-voiceMessage:" + voiceMessage.getUri().toString());
//      } else if (messageContent instanceof RichContentMessage) {//图文消息
//        RichContentMessage richContentMessage = (RichContentMessage) messageContent;
//        Logger.e(TAG, "onSent-RichContentMessage:" + richContentMessage.getContent());
//      } else {
//        Logger.e(TAG, "onSent-其他消息，自己来判断处理");
//      }
        }

        return false;
    }

    public static void setCurrentUserInfo() {
        if (POMember.isLogin()) {
            POMember user = POMember.getInstance();
            String uid = user.getBeke_userid();
            String name = user.getBeke_nickname();
            Uri userIcon = Uri.parse(user.getProfileImg());
            UserInfo userInfo = new UserInfo(uid, name, userIcon);
            RongIM.getInstance().setCurrentUserInfo(userInfo);
        }
    }

    /**
     * 用户信息的提供者：GetUserInfoProvider 的回调方法，获取用户信息。
     *
     * @param userId 用户 Id。
     * @return 用户信息，（注：由开发者提供用户信息）。
     */
    @Override
    public UserInfo getUserInfo(String userId) {
        /**
         * demo 代码  开发者需替换成自己的代码。
         */
        if (userId == null) {
            return null;
        }
        POMember user = POMember.getInstance();
        if (user != null && userId.equals(user.getBeke_userid())) {
            String uid = user.getBeke_userid();
            String name = user.getBeke_nickname();
            Uri userIcon = Uri.parse(user.getProfileImg());
            UserInfo userInfo = new UserInfo(uid, name, userIcon);

            return userInfo;
        }
        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        new MyPersonalRequest() {
            @Override
            public void onFinish(boolean isSuccess, String msg, POMember data) {
                if (isSuccess && data != null) {
                    UserInfo userInfo = new UserInfo(data.getBeke_userid(), data.getBeke_nickname(),
                            Uri.parse(data.getProfileImg()));
                    RongIM.getInstance().refreshUserInfoCache(userInfo);
                }
            }
        }.startRequest(map);

        return null;
    }

    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击用户头像后执行。
     *
     * @param context          应用当前上下文。
     * @param conversationType 会话类型。
     * @param user             被点击的用户的信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onUserPortraitClick(Context context,
                                       Conversation.ConversationType conversationType, UserInfo user) {
        //如果是系统消息，不需要进行跳转
        if (conversationType.equals(Conversation.ConversationType.SYSTEM)) {
            return true;
        }
        if (user != null) {
            context.startActivity(OthersDetailsActivity.getCallingIntent(context, user.getUserId(),
                    OthersDetailsActivity.OTHER));
            return true;
        }

        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context,
                                           Conversation.ConversationType conversationType, UserInfo userInfo) {
        Logger.e(TAG, "----onUserPortraitLongClick");

        return true;
    }

    /**
     * 会话界面操作的监听器：ConversationBehaviorListener 的回调方法，当点击消息时执行。
     *
     * @param context 应用当前上下文。
     * @param message 被点击的消息的实体信息。
     * @return 返回True不执行后续SDK操作，返回False继续执行SDK操作。
     */
    @Override
    public boolean onMessageClick(final Context context, final View view,
                                  final Message message) {
        Logger.e(TAG, "----onMessageClick");

        if (message.getContent() instanceof RichContentMessage) {
            RichContentMessage mRichContentMessage = (RichContentMessage) message.getContent();
            Logger.d("Begavior", "extra:" + mRichContentMessage.getExtra());
            Logger.e(TAG, "----RichContentMessage-------");
        } else if (message.getContent() instanceof ImageMessage) {
            Intent intent = new Intent(context, PhotoActivity.class);
            intent.putExtra("message", message);
            context.startActivity(intent);
            ((Activity)context).overridePendingTransition(R.anim.activity_bottom_in,0);
        } else if (message.getContent() instanceof PublicServiceMultiRichContentMessage) {
            Logger.e(TAG, "----PublicServiceMultiRichContentMessage-------");
        } else if (message.getContent() instanceof PublicServiceRichContentMessage) {
            Logger.e(TAG, "----PublicServiceRichContentMessage-------");
        }

        Logger.d("Begavior", message.getObjectName() + ":" + message.getMessageId());

        return false;
    }

    /**
     * 当点击链接消息时执行。
     *
     * @param context 上下文。
     * @param link    被点击的链接。
     * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
     */
    @Override
    public boolean onMessageLinkClick(Context context, String link) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {

        Logger.e(TAG, "----onMessageLongClick");
        return false;
    }

    /**
     * 连接状态监听器，以获取连接相关状态:ConnectionStatusListener 的回调方法，网络状态变化时执行。
     *
     * @param status 网络状态。
     */
    @Override
    public void onChanged(ConnectionStatus status) {
        Logger.d(TAG, "onChanged:" + status);
        switch (status) {

            case CONNECTED://连接成功。

                break;
            case DISCONNECTED://断开连接。

                break;
            case CONNECTING://连接中。

                break;
            case NETWORK_UNAVAILABLE://网络不可用。

                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://用户账户在其他设备登录，本机会被踢掉线
                EventBus.getDefault().post(new POEventBus(EventBusKey.KICKED_BY_OTHER_CLIENT, null));
                break;
        }
    }

    /**
     * 点击会话列表 item 后执行。
     *
     * @param context      上下文。
     * @param view         触发点击的 View。
     * @param conversation 会话条目。
     * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
     */
    @Override
    public boolean onConversationClick(Context context, View view,
                                       UIConversation conversation) {
        MessageContent messageContent = conversation.getMessageContent();

        Logger.e(TAG, "--------onConversationClick-------");
        if (messageContent instanceof TextMessage) {//文本消息
            TextMessage textMessage = (TextMessage) messageContent;
            textMessage.getExtra();
        } else if (messageContent instanceof ContactNotificationMessage) {
            Logger.e(TAG, "---onConversationClick--ContactNotificationMessage-");

            return true;
        }
        return false;
    }

    /**
     * 长按会话列表 item 后执行。
     *
     * @param context      上下文。
     * @param view         触发点击的 View。
     * @param conversation 长按会话条目。
     * @return 返回 true 不再执行融云 SDK 逻辑，返回 false 先执行融云 SDK 逻辑再执行该方法。
     */
    @Override
    public boolean onConversationLongClick(Context context, View view,
                                           UIConversation conversation) {
        return false;
    }

}
