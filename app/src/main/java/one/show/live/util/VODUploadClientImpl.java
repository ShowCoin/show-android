package one.show.live.upload;

import android.content.Context;

import com.alibaba.sdk.android.oss.common.OSSLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import one.show.live.log.Logger;
import one.show.live.upload.common.StringUtil;
import one.show.live.upload.common.UploadStateType;
import one.show.live.upload.common.VodUploadStateType;
import one.show.live.upload.exception.VODClientException;
import one.show.live.upload.internal.OSSUploadListener;
import one.show.live.upload.internal.OSSUploader;
import one.show.live.upload.internal.OSSUploaderImpl;
import one.show.live.upload.model.UploadFileInfo;


public class VODUploadClientImpl implements VODUploadClient, OSSUploadListener {

    private static final String TAG = "VODUploadClient";
    private OSSUploader upload;
    private Context context;
    private UploadFileInfo curFileInfo;
    private VodUploadStateType status;
    private VODUploadCallback callback;
    private List<UploadFileInfo> fileList;

    public VODUploadClientImpl(Context applicationContext) {
        this.context = applicationContext;
        this.fileList = Collections.synchronizedList(new ArrayList());
        this.upload = new OSSUploaderImpl(this.context);
    }

    @Override
    public void init(VODUploadCallback callback) {
        if (null == callback) {
            throw new VODClientException("MissingArgument", "The specified parameter \"callback\" cannot be null");
        } else {
            this.upload.init(this);
            this.callback = callback;
            this.status = VodUploadStateType.INIT;
        }
    }

    @Override
    public void addFile(UploadFileInfo uploadFileInfo) {
        if (uploadFileInfo == null || StringUtil.isEmpty(uploadFileInfo.getFilePath())) {
            throw new VODClientException("MissingArgument", "The specified parameter \"localFilePath\" cannot be null");
        } else {
            Iterator<UploadFileInfo> it = fileList.iterator();
            UploadFileInfo info;
            while (it.hasNext()) {
                info = it.next();
                if (info.equals(uploadFileInfo)) {
                    //TODO 这块逻辑可以考虑不要。因为有可能同时上传一个文件，但是做不同用处。
                    if (info.getStatus() == UploadStateType.INIT || info.getStatus() == UploadStateType.UPLOADING) {
//                        throw new VODClientException("FileAlreadyExist", "The file is already exist!");
                        Logger.e(TAG, "The file is already exist!----->>>" + uploadFileInfo.getFilePath());
                        return;
                    } else {
                        it.remove();
                        break;
                    }
                }
            }

            uploadFileInfo.setStatus(UploadStateType.INIT);
            this.fileList.add(uploadFileInfo);
        }
    }


    @Override
    public UploadFileInfo getNextUploadFileInfo(UploadFileInfo uploadFileInfo) {
        for (int i = 0; i < this.fileList.size(); i++) {
            if ((this.fileList.get(i)).equals(uploadFileInfo)) {
                int nextIndex = i + 1;
                if (nextIndex >= 0 && nextIndex < this.fileList.size()) {
                    return fileList.get(nextIndex);
                } else {
                    return null;
                }

            }
        }
        return null;
    }

    @Override
    public void deleteFile(int index) {
        if (index >= 0 && index < this.fileList.size()) {
            UploadFileInfo uploadFileInfo = this.fileList.get(index);
            if (uploadFileInfo.getStatus() == UploadStateType.UPLOADING && this.upload != null) {
                this.upload.cancel();
            }

            this.fileList.remove(index);
        } else {
            throw new VODClientException("InvalidArgument", "index out of range");
        }
    }

    @Override
    public void clearFiles() {
        this.fileList.clear();
        if (null != this.upload) {
            this.upload.cancel();
        }

        this.status = VodUploadStateType.INIT;
    }


    @Override
    public void clearUnUsedFiles() {
        Iterator<UploadFileInfo> it = fileList.iterator();
        UploadFileInfo info;
        while (it.hasNext()) {
            info = it.next();
            if (info.getStatus() != UploadStateType.INIT && info.getStatus() != UploadStateType.UPLOADING) {
                it.remove();
            }
        }
    }


    @Override
    public List<UploadFileInfo> listFiles() {
        return this.fileList;
    }

    @Override
    public void start() {
        OSSLog.logD("[VODUploadClientImpl] - start called status: " + this.status);
        if (VodUploadStateType.STARTED != this.status) {
            this.status = VodUploadStateType.STARTED;
            if (this.next(true)) {
            }
        } else {
            OSSLog.logD("[VODUploadClientImpl] - status: " + this.status + " cann\'t be start!");
        }
    }


    public VodUploadStateType getStatus() {
        return this.status;
    }

    @Override
    public boolean checkTaskIsAlive(String url) {
        if (VodUploadStateType.STARTED == this.status) {
            for (int i = 0; i < this.fileList.size(); i++) {
                if (this.fileList.get(i).getFilePath().equals(url)) {
                    if (this.fileList.get(i).getStatus() == UploadStateType.INIT || this.fileList.get(i).getStatus() == UploadStateType.UPLOADING) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public int getAliveTaskCount() {
        int size = 0;
        if (VodUploadStateType.STARTED == this.status) {
            for (int i = 0; i < this.fileList.size(); i++) {
                if (this.fileList.get(i).getStatus() == UploadStateType.INIT || this.fileList.get(i).getStatus() == UploadStateType.UPLOADING) {
                    size++;
                }
            }
        }
        return size;
    }

    @Override
    public void stop() {
        OSSLog.logD("[VODUploadClientImpl] - stop called status: " + this.status);
        if (VodUploadStateType.STARTED != this.status) {
            OSSLog.logD("[VODUploadClientImpl] - status: " + this.status + " cann\'t be stop!");
        } else {
            this.status = VodUploadStateType.STOPED;
            if (this.upload != null && this.curFileInfo.getStatus() == UploadStateType.UPLOADING) {
                this.upload.cancel();
            }

        }
    }

    @Override
    public void cancelFile(int index) {
        OSSLog.logD("[VODUploadClientImpl] - cancelFile called status: " + this.status);
        if (index >= 0 && index < this.fileList.size()) {
            UploadFileInfo uploadFileInfo = this.fileList.get(index);
            if (uploadFileInfo.getStatus() == UploadStateType.CANCELED) {
                throw new VODClientException("FileAlreadyCancel", "The file \"" + uploadFileInfo.getFilePath() + "\" is already canceled!");
            } else {
                if (uploadFileInfo.getStatus() == UploadStateType.UPLOADING) {
                    if (this.upload != null) {
                        this.upload.cancel();
                    }
                } else {
                    uploadFileInfo.setStatus(UploadStateType.CANCELED);
                }

            }
        } else {
            throw new VODClientException("InvalidArgument", "index out of range");
        }
    }

    @Override
    public void onUploadSucceed() {
        this.callback.onUploadSucceed(this.curFileInfo);
        this.next();
    }

    @Override
    public void onUploadFailed(String code, String message) {
        this.callback.onUploadFailed(this.curFileInfo, code, message);
        if (this.status == VodUploadStateType.STARTED) {
            this.next();
        } else if (this.status == VodUploadStateType.STOPED) {
            if (code.equals(UploadStateType.CANCELED.toString())) {
                this.curFileInfo.setStatus(UploadStateType.INIT);
            }
        }
    }

    @Override
    public void onUploadProgress(long uploadedSize, long totalSize) {
        this.callback.onUploadProgress(this.curFileInfo, uploadedSize, totalSize);
    }


    private boolean next() {
        return next(false);
    }


    private boolean next(boolean isShouldNewThread) {
        if (this.status != VodUploadStateType.STOPED) {
            for (int i = 0; i < this.fileList.size(); i++) {
                if ((this.fileList.get(i)).getStatus() == UploadStateType.INIT) {
                    this.curFileInfo = this.fileList.get(i);
                    this.callback.onUploadStarted(this.curFileInfo);

                    if (isShouldNewThread) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                VODUploadClientImpl.this.startTask();
                            }
                        }).start();
                    } else {
                        VODUploadClientImpl.this.startTask();
                    }
                    return true;
                }
            }

            this.status = VodUploadStateType.FINISHED;
            return false;
        } else {
            return false;
        }
    }

    private void startTask() {
        VODUploadClientImpl.this.upload.start(VODUploadClientImpl.this.curFileInfo);
    }

}
