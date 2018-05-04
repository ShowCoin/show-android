package one.show.live.showlive.im;


import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import one.show.live.R;
import one.show.live.common.po.POLogin;
import one.show.live.common.view.ToastUtils;
import one.show.live.log.Logger;
import one.show.live.po.PORyToken;

/**
 *  Adapt to third party IM SDK, handle messages related events dispatching.
 */
public enum IMDelegate {
    INSTANCE;

    private final static String TAG = "IMDelegate";

    private RongIMClient.OnReceiveMessageListener mOnRongMessageListener;

    /**
     * Connect to IM service, will update token when {@link RongIMClient.ConnectCallback#onTokenIncorrect()} invoked.
     * @param ctx
     */
    public void connect(final Context ctx) {

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
                    Logger.e(TAG, "onSuccess");
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Logger.e(TAG, "onError");
                }
            });

            RongIMClient.setConnectionStatusListener(new RongIMClient.ConnectionStatusListener() {
                @Override
                public void onChanged(ConnectionStatus connectionStatus) {
                    Logger.e(TAG, "onChanged");
                }
            });

        }
    }

    /**
     * IM SDK initial and setup inner event listener
     * @param ctx
     */
    public void init(Context ctx) {
        RongIMClient.init(ctx);
        initMessageListener();
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

    public static void registerMessageReceiver() {
        RongIMClient.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {
                Logger.e(TAG, "onReceived");
                return false;
            }
        });

    }


    private void initMessageListener() {

        mOnRongMessageListener = new RongIMClient.OnReceiveMessageListener() {
            @Override
            public boolean onReceived(Message message, int i) {

                switch (message.getConversationType()) {

                    case PRIVATE:
                        EventBus.getDefault()
                                .post(new IMReceivePrivateMsgEvent(0, message));
                        Logger.e(TAG, "received private msg");
                        break;
                    case CHATROOM:
                        //TODO handle live room message

                        break;
                    default:
                        break;
                }
                return true;
            }
        };

        RongIMClient.setOnReceiveMessageListener(mOnRongMessagerListener);
    }


}
