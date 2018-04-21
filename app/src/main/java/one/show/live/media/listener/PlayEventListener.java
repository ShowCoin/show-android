package one.show.live.media.listener;

/**
 * 播放时,事件的监听
 */
public interface PlayEventListener {
    int STATUS_START = 0x11;
    int STATUS_PLAY = 0x12;
    int STATUS_BUFFERING = 0x13;
    int STATUS_PAUSE = 0x14;
    int STATUS_STOP = 0x15;
    int STATUS_FINISH = 0x16;
    int GET_VIDEOSIZE = 0x17;
    
    void onEvent(int what);
}
