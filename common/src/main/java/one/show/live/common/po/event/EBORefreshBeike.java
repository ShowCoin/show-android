package one.show.live.common.po.event;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by apple on 16/6/19.
 */
public class EBORefreshBeike {

    public static void sendRefreshBeikeEvent(){
        EventBus.getDefault().postSticky(new EBORefreshBeike());
    }

    public static void removeRefreshBeikePraiseEvent(){
        EBORefreshBeike oldEvent = EventBus.getDefault().getStickyEvent(EBORefreshBeike.class);
        if(oldEvent != null) {
            EventBus.getDefault().removeStickyEvent(oldEvent);
        }
    }

}
