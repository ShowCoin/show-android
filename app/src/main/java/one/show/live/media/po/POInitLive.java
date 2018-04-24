package one.show.live.media.po;

import com.google.gson.Gson;
import java.io.Serializable;
import one.show.live.common.util.UserInfoCacheManager;

/**
 * Created by apple on 16/6/16.
 */
public class POInitLive implements Serializable {

  /**
   * cdnType : 0
   * liveId : 755383644914327552
   * roomId : 755383644914327552
   * rtmp : rtmp://111.202.74.130/live/755383644914327552_1468932469?wsRecord=on&wsiphost=ipdb&wsHost=push1.beke.tv
   * share_addr : http://www.beke.tv/share/live/755383644914327552
   * streamName : 755383644914327552_1468932469
   */

  private static POInitLive publisherInfo;
  private int cdnType;
  private long liveId;
  private long roomId;
  private String rtmp;
  private String share_addr;
  private String streamName;

  public static POInitLive getInstance() {
    publisherInfo =
        new Gson().fromJson(UserInfoCacheManager.getInstance().getValue("PUBLISHER_INFO", "{}"),
            POInitLive.class);
    return publisherInfo;
  }

  public int getCdnType() {
    return cdnType;
  }

  public void setCdnType(int cdnType) {
    this.cdnType = cdnType;
  }

  public long getLiveId() {
    return liveId;
  }

  public void setLiveId(long liveId) {
    this.liveId = liveId;
  }

  public long getRoomId() {
    return roomId;
  }

  public void setRoomId(long roomId) {
    this.roomId = roomId;
  }

  public String getRtmp() {
    return rtmp;
  }

  public void setRtmp(String rtmp) {
    this.rtmp = rtmp;
  }

  public String getShare_addr() {
    return share_addr;
  }

  public void setShare_addr(String share_addr) {
    this.share_addr = share_addr;
  }

  public String getStreamName() {
    return streamName;
  }

  public void setStreamName(String streamName) {
    this.streamName = streamName;
  }
}
