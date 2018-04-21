package one.show.live.media.view;


import one.show.live.media.po.POInitLive;
import one.show.live.media.po.POLiveStatus;

/**
 * Created by apple on 16/5/20.
 * 准备直播页面的接口
 */
public interface ReadyToLiveView {

    void onReqStartLiveFinish(POInitLive live);
    void onReqStartLiveFailed(String msg);
    void checkPublisherSuccess(POLiveStatus liveStatus);
    void checkPublisherFailed();
}
