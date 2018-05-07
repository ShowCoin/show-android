package one.show.live.im;


import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Collection;
import java.util.HashMap;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import one.show.live.R;
import one.show.live.common.po.POEventBus;
import one.show.live.common.po.POLogin;
import one.show.live.common.ui.BaseApplication;
import one.show.live.common.util.Constants;
import one.show.live.common.util.StringUtils;
import one.show.live.common.view.ToastUtils;
import one.show.live.log.Logger;
import one.show.live.media.util.ChatRoomIMUtils;
import one.show.live.po.PORyToken;
import one.show.live.util.PreferenceUtils;
import one.show.live.util.SystemUtils;

/**
 * Adapt to third party IM SDK, handle messages related events dispatching.
 * Created by clarkM1ss1on on 2018/4/27
 */
public enum IMDelegate {
    INSTANCE;

    public final static String TYPING_TYPE_TXT = "RC:TxtMsg";
    public final static String TYPING_TYPE_VOICE = "";

    private final static String TAG = "IMDelegate";

    private RongIMClient.OnReceiveMessageListener mOnRongMessageListener;/
    private RongIMClient.OnRecallMessageListener mOnRongMessagerRecallListener;/
    private RongIMClient.ReadReceiptListener mReadReceiptListener;
    private RongIMClient.TypingStatusListener mTypingStatusListener;
    private RongIMClient.ConnectionStatusListener mConnectionStatusListner;

    /**
     * Connect to IM service, will update token when {@link RongIMClient.ConnectCallback#onTokenIncorrect()} invoked,
     * register {@link IMConnectStatusEvent} to get connection status through event bus;
     *
     * @param ctx
     */
    public void connect(final Context ctx) {
        //避免意外重复连接
        if (!RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED
                .equals(RongIMClient.getInstance().getCurrentConnectionStatus())) {
            return;
        }
        Logger.e(TAG, "connect");
        final POLogin loginInfo = POLogin
                .getInstance();
        if (TextUtils.isEmpty(loginInfo.getRy_token())) {
            ToastUtils.showLongToast(R.string.login_first_plz);
        } else {
            RongIMClient.connect(loginInfo.getRy_token(), new RongIMClient.ConnectCallback() {

                @Override
                public void onTokenIncorrect() {
                    Logger.e(TAG, "onTokenIncorrect");
                    new Handler(ctx.getMainLooper())
                            .post(new Runnable() {
                                @Override
                                public void run() {
                                    updateTokenAndReconnect(ctx);
                                }
                            });
                }

                @Override
                public void onSuccess(String s) {
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Logger.e(TAG, "IM CONNECTION ERROR CODE :" + errorCode.getValue()
                            + " : " + errorCode.getMessage());
                }
            });

        }
    }

    /**
     * Disconnect from IM service
     */
    public void disconnect() {
        RongIMClient.getInstance()
                .disconnect();
    }

    public RongIMClient.ConnectionStatusListener.ConnectionStatus getCurrentConnectionStatus() {
        return RongIMClient.getInstance()
                .getCurrentConnectionStatus();
    }

     * @return
     */
    public boolean isIMConnected() {
        return getCurrentConnectionStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED;
    }


    /**
     * IM SDK initial and setup inner event listener
     * @param ctx
     */
    public void init(Context ctx) {
        RongIMClient.init(ctx);
        initMessageListener();
        initIMConnectionStatusListener();
        initReadReceiptListener();
        initTappingStatusChangedListener();
    }

    private void updateTokenAndReconnect(final Context ctx) {

        new UpdateRyTokenRequest() {
            @Override
            public void onFinish(boolean isSuccess, String msg, PORyToken data) {
                final POLogin loginInfo = POLogin
                        .getInstance();

                if (null != data.getRyToken()) {
                    loginInfo.setRy_token(data.getRyToken());
                    POLogin.save(loginInfo);
                    connect(ctx);
                }

            }
        }.startRequest(new HashMap<String, String>());

    }

    public void sendTextTypingStatus(String targetId) {
        RongIMClient.getInstance().sendTypingStatus(Conversation.ConversationType.PRIVATE
                , targetId, TYPING_TYPE_TXT);
    }


    public static Message obtainPrivateMsgObj(String targetId, MessageContent content) {
        return Message.obtain(targetId
                , Conversation.ConversationType.PRIVATE
                , content);
    }

    public static Message obtainMsgObjByType(String targetId, Conversation.ConversationType type, MessageContent content) {
        return Message.obtain(targetId
                , type
                , content);
    }

    private void initMessageListener() {
        mOnRongMessageListener = new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                switch (message.getConversationType()) {
                    case PRIVATE:
                        EventBus.getDefault()
                                .post(new IMReceivePrivateMsgEvent(0, message));
                        break;
                    case CHATROOM:
                        ChatRoomIMUtils.onReceived(message);
                        break;
                    default:
                        break;
                }
                return true;
            }
        };
        RongIMClient.setOnReceiveMessageListener(mOnRongMessageListener);
    }


    private void initIMConnectionStatusListener() {
        mConnectionStatusListner = new RongIMClient.ConnectionStatusListener() {
            @Override
            public void onChanged(ConnectionStatus connectionStatus) {

                if (ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT == connectionStatus
                        || ConnectionStatus.CONN_USER_BLOCKED == connectionStatus) {
                    EventBus.getDefault()
                            .post(new POEventBus(Constants.RESPONSE_CODE_TOKEN_FAIL, "Kicked out by other terminal or account blocked"));
                } else {
                    EventBus.getDefault()
                            .post(new IMConnectStatusEvent(connectionStatus));
                }
            }
        };
        RongIMClient.setConnectionStatusListener(mConnectionStatusListner);
    }

    private void initTappingStatusChangedListener() {
        mTypingStatusListener = new RongIMClient.TypingStatusListener() {
            @Override
            public void onTypingStatusChanged(Conversation.ConversationType conversationType, String s, Collection<TypingStatus> collection) {
                EventBus.getDefault()
                        .post(new IMTypingStatusEvent(conversationType, s, collection));
            }
        };
        RongIMClient.setTypingStatusListener(mTypingStatusListener);

    }

    private void initReadReceiptListener() {
        mReadReceiptListener = new RongIMClient.ReadReceiptListener() {
            @Override
            public void onReadReceiptReceived(Message message) {
                EventBus.getDefault()
                        .post(new IMReadReceiptStatusEvent(message));
            }

            @Override
            public void onMessageReceiptRequest(Conversation.ConversationType conversationType, String s, String s1) {
                EventBus.getDefault()
                        .post(new IMReadReceiptStatusEvent(conversationType, s, s1));
            }

            @Override
            public void onMessageReceiptResponse(Conversation.ConversationType conversationType, String s, String s1, HashMap<String, Long> hashMap) {
                EventBus.getDefault()
                        .post(new IMReadReceiptStatusEvent(conversationType, s, s1, hashMap));
            }
        };
        RongIMClient.setReadReceiptListener(mReadReceiptListener);
    }
}
