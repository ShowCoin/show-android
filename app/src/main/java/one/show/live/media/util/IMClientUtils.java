package one.show.live.media.util;

import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;
import org.greenrobot.eventbus.EventBus;
import one.show.live.common.po.POEventBus;
import one.show.live.common.po.POMember;
import one.show.live.common.util.Constants;
import one.show.live.common.util.StringUtils;
import one.show.live.media.po.POIMTalkMsgExtra;
import one.show.live.media.po.POIMUser;
import one.show.live.log.Logger;
import one.show.live.po.eventbus.EBOJoinRoomCallBack;
import one.show.live.util.EventBusKey;
import one.show.live.util.PreferenceUtils;

/**
 * Created by apple on 16/5/31.
 */
public class IMClientUtils {

    private static final String TAG = "rongyun";

    public interface OnConnectListener {
        void onConnectSuccess();

        void onConnectFailed();
    }


    public static class MyLiveRoomOnConnectListener implements IMClientUtils.OnConnectListener{

        public String roomId;

        public MyLiveRoomOnConnectListener(String roomId){
            super();
            this.roomId = roomId;
        }

        @Override
        public void onConnectSuccess() {
            IMClientUtils.join2ChatRoom(roomId, new RongIMClient.OperationCallback() {
                @Override
                public void onSuccess() {
                    EventBus.getDefault().post(new EBOJoinRoomCallBack(roomId, true));
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    EventBus.getDefault().post(new EBOJoinRoomCallBack(roomId, false,errorCode));
                }
            });
        }

        @Override
        public void onConnectFailed() {

        }
    }



    public static void checkRoomConect(String roomId) {
        MyLiveRoomOnConnectListener listener = new MyLiveRoomOnConnectListener(roomId);
        String token = PreferenceUtils.getString(Constants.RONGYUN_TOKEN, null);
        if (StringUtils.isEmpty(token)||RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            reconnect(POMember.getInstance().getRy_token(), listener);
        } else {
            EventBus.getDefault().post(new POEventBus(EventBusKey.RONG_CLOUD_CONNECTED, null));
            listener.onConnectSuccess();
        }
    }



    public static void checkConect(OnConnectListener listener) {
        String token = PreferenceUtils.getString(Constants.RONGYUN_TOKEN, null);
        if (StringUtils.isEmpty(token)||RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
            reconnect(POMember.getInstance().getRy_token(), listener);
        } else {
            EventBus.getDefault().post(new POEventBus(EventBusKey.RONG_CLOUD_CONNECTED, null));
            listener.onConnectSuccess();
        }
    }

    /**
     * @param token
     */
    public static void reconnect(final String token, final OnConnectListener listener) {

        Logger.e("simon","connect 融云");
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
                //TOKEN过期的处理需要考虑
                Logger.e(TAG, "---onTokenIncorrect--");
            }

            @Override
            public void onSuccess(String s) {
                Logger.i(TAG, "---onSuccess--" + s);
                PreferenceUtils.put(Constants.RONGYUN_TOKEN, token);
                EventBus.getDefault().post(new POEventBus(EventBusKey.RONG_CLOUD_CONNECTED, null));
                listener.onConnectSuccess();
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
                Log.e(TAG, "---onError--" + e);
                listener.onConnectFailed();
            }
        });

    }


    // 加入聊天室
    public static void join2ChatRoom(final String roomId, final RongIMClient.OperationCallback  callback) {

        int defMsgCount = -1;
        RongIM.getInstance().joinChatRoom(roomId, defMsgCount,callback);
    }


    /*
	 * 退出聊天室
	 */
	public static boolean quitChatRoom(final String roomId) {
//		TextMessage tmsg = new TextMessage(getIMUserinfo().getName() + "退出聊天室");
//		tmsg.setExtra(context.getResources().getString(R.string.live_im_send_quit));
//		tmsg.setUserInfo(getIMUserinfo());
//		// 退出聊天室之前 先发送退出消息
//		RongIMClient.getInstance().sendMessage(
//		    Conversation.ConversationType.CHATROOM, roomId, tmsg, "推送content",
//		    "推送Data", null, null);
		// 退出聊天室
        RongIM.getInstance().quitChatRoom(roomId, null);
		return true;
	}


    public static void sendTextMessage(String text, final String roomId,int isAdmin) {
        TextMessage tmsg = new TextMessage(text);
        POMember poMember = POMember.getInstance();
        if(poMember.getFanLevel() < 1){
            poMember.setFanLevel(1);
            POMember.getInstance().updateFanLevel(1);
        }
        String json = new Gson().toJson(new POIMTalkMsgExtra(poMember.getBeke_userid(), poMember.getBeke_nickname(), poMember.getFanLevel(),isAdmin));
        if (StringUtils.isNotEmpty(json)) {
            tmsg.setExtra(json);
            RongIM.getInstance().sendMessage(
                    Conversation.ConversationType.CHATROOM, roomId, tmsg, "推送content",
                    "推送Data", null, null);
        }
    }


}
