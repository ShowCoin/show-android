package one.show.live.media.po;

/**
 * Created by apple on 16/6/25.
 */
public class POLiveStatus extends POIMEnd{


    /**
     * 1. 用户自己开直播，调取接口判断status，如果为1，表示上次直播未正常关闭，需要提示用户是否继续

     2. 用户进入别人直播间，因为首页是定时刷新，不能保证直播状态的实时更新，此时需要status和stream_status组合使用，当status=0时，提示用户直播关闭， 当status=1并且stream_status=2时，提示用户主播暂时离开，其它状态正常看直播
     */

    /**
     * 0 直播关闭  1正在直播
     */
    private int status;
    /**
     * 1流正常 2流中断 3流正常(中间断过又续上了) 4结束
     */
    private int stream_status;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStream_status() {
        return stream_status;
    }

    public void setStream_status(int stream_status) {
        this.stream_status = stream_status;
    }



    public boolean isLiveClose(){
        return status == 0;
    }

    public boolean isLiveSteamStop(){
        return status == 1 && stream_status == 2;
    }

}
