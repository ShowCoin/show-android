package one.show.live.net;

/**
 * Created by lishizhong on 16/10/21.
 */
public interface OnUploadProgressListener {
    void onTotalSize(long totalSize);
    void onProgressChanged(long bytesWritten, long contentLength, boolean done);
    void onFinish();
}
