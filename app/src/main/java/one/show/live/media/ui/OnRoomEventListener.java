package one.show.live.media.ui;

import one.show.live.media.po.POIMEnd;
import one.show.live.media.po.POIMGift;

public interface OnRoomEventListener {
        void exit(boolean isAsk);

        void streamInterrupt();

        void liveStart();

        void lookLiveEnd(POIMEnd imEnd);

        void liveEnd(POIMEnd endObj);

        void switchCamera(boolean isCamOn);

        void switchFlash();

        void smoothSkin();

        void switchMic();

        void showOrHideBigAnimation(boolean isShow);

        void addBigGift(POIMGift gift);
    }