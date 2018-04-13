package one.show.live.netutil.lsn;

/**
 * Created by J-King on 2018/1/25.
 */

public interface DownloadResultListener {

    void downloadSuccess();

    void downFailed(int errCode, String msg);

}
