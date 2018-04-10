package one.show.live.netutil.lsn;

/**
 * Created by J-King on 2018/1/25.
 */

public interface DownloadPregressListener {

    void onBefore();

    void onProgress(long mCurrentSize, long mTotalSize, float mProgress, long mNetworkSpeed);

    void onAfter();

}
