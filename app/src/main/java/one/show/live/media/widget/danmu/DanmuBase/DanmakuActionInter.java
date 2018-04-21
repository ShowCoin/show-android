package one.show.live.media.widget.danmu.DanmuBase;

import one.show.live.media.po.POIMDanmu;

/**
 * Created by walkingMen on 2016/5/12.
 * 弹幕动作类
 */
public interface DanmakuActionInter {
    /**
     * 添加弹幕
     */
    void addDanmu(POIMDanmu dan);

    /**
     * 移出弹幕
     */
    void pollDanmu();
}
