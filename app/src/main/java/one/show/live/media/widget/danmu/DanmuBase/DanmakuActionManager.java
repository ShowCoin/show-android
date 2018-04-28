package one.show.live.media.widget.danmu.DanmuBase;


import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import one.show.live.media.po.POIMDanmu;

/**
 * Created by walkingMen on ..5/12.
 */
public class DanmakuActionManager implements DanmakuActionInter {
    public List<DanmakuChannel> channels = new LinkedList<>();

    public ConcurrentLinkedQueue<POIMDanmu> danEntities = new ConcurrentLinkedQueue<>();

    @Override
    public void addDanmu(POIMDanmu dan) {
        danEntities.offer(dan);
        looperDan();
    }

    @Override
    public void pollDanmu() {
        looperDan();
    }

    public void addChannel(DanmakuChannel channel) {
        channels.add(channel);
    }

    public DanmakuActionManager() {

    }

    public void release(){
        danEntities.clear();
        for (int i = channels.size() -1; i >=0 ; i--) {
            channels.get(i).release();
        }
    }

    public void looperDan() {
        for (int i = channels.size() -1; i >=0 ; i--) {
            if (!channels.get(i).isRunning && danEntities.size() > 0) {
                POIMDanmu poll = danEntities.poll();
                channels.get(i).mStartAnimation(poll);
            }
        }
    }
}
