package one.show.live.live.record.view;

import one.show.live.live.po.POInitLive;
import one.show.live.live.po.POLiveStatus;

/**
 * Created by apple on 18/3/20.
 * 准备直播页面的接口
 */
public interface ReadyToLiveView {

    void onReqStartLiveFinish(POInitLive live);
    void onReqStartLiveFailed(String msg);
    void checkPublisherSuccess(POLiveStatus liveStatus);
    void checkPublisherFailed();
}
