package one.show.live.live.listener;


import io.rong.imlib.model.Conversation;
import one.show.live.po.GiftBean;
import one.show.live.po.LiveRoomInfoBean;

public interface ChatEventListener {
    //void onReceiveInfo(LiveRoomInfoBean infoBean);
    //void onReceiveInfo(int onliens, int online);
    //void onReceiveGift(GiftBean bean);
    //void onReceivePraise(int num);

    void conversListItemClick(String targetID, String conversationTitle, Conversation.ConversationType conversationType);
}
