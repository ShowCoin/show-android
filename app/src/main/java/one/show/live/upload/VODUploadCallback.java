package one.show.live.upload;


import one.show.live.upload.model.UploadFileInfo;

public interface VODUploadCallback {
    void onUploadSucceed(UploadFileInfo fileInfo);

    void onUploadFailed(UploadFileInfo fileInfo, String code, String message);

    void onUploadProgress(UploadFileInfo fileInfo, long uploadedSize, long totalSize);

    void onUploadStarted(UploadFileInfo fileInfo);
}
