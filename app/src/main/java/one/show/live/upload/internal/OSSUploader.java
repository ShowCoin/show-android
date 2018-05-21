package one.show.live.upload.internal;


import one.show.live.upload.model.UploadFileInfo;

public interface OSSUploader {
    void init(OSSUploadListener ossUploadListener);

    void start(UploadFileInfo uploadFileInfo);

    void cancel();

}
