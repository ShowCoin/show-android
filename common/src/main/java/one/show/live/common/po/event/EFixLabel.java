package one.show.live.common.po.event;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Nano on 2017/7/29.
 */

public class EFixLabel {
    /**
     * 修改我喜欢的标签的通知
     */
    public static final int FIX_LEABEL = 0x100<<1;

    public int leaber;

    public static void sendRefreshBeikeEvent(int leaber){
        EventBus.getDefault().postSticky(new EFixLabel(leaber));
    }

    public EFixLabel(int leaber){
        this.leaber = leaber;
    }

    public int getLeaber() {
        return leaber;
    }

    public void setLeaber(int leaber) {
        this.leaber = leaber;
    }

}
