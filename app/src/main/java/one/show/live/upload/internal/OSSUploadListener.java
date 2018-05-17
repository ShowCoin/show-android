package one.show.live.upload.internal;

public interface OSSUploadListener {
    void onUploadSucceed();

    void onUploadFailed(String code, String message);

    void onUploadProgress(long uploadedSize, long totalSize);

}
