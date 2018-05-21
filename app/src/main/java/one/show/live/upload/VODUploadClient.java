package one.show.live.upload;

import java.util.List;

import one.show.live.upload.common.VodUploadStateType;
import one.show.live.upload.model.UploadFileInfo;


public interface VODUploadClient {

    void init(VODUploadCallback callback);

    void addFile(UploadFileInfo fileInfo);

    UploadFileInfo getNextUploadFileInfo(UploadFileInfo uploadFileInfo);

    void deleteFile(int index);

    void clearFiles();

    void clearUnUsedFiles();

    List<UploadFileInfo> listFiles();

    void cancelFile(int index);

    void start();

    void stop();

    VodUploadStateType getStatus();

    boolean checkTaskIsAlive(String url);

    int getAliveTaskCount();

}