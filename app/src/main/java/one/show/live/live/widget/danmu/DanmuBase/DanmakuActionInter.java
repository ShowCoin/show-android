package one.show.live.live.widget.danmu.DanmuBase;

import one.show.live.live.po.POIMDanmu;

/**
 * Created by walkingMen on 2018/3/12.
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
