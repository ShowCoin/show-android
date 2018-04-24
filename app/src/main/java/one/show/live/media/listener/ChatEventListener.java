package one.show.live.media.listener;

import io.rong.imlib.model.Conversation;

public interface ChatEventListener {
    //void onReceiveInfo(LiveRoomInfoBean infoBean);
    //void onReceiveInfo(int onliens, int online);
    //void onReceiveGift(GiftBean bean);
    //void onReceivePraise(int num);

    void conversListItemClick(String targetID, String conversationTitle, Conversation.ConversationType conversationType);
}
