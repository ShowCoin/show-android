//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package one.show.live.upload.model;


import one.show.live.upload.common.StringUtil;
import one.show.live.upload.common.UploadStateType;

public class UploadFileInfo {
    protected String filePath;
    private String bucket;
    private String object;
    private VodInfo vodInfo;
    private UploadStateType status;
    private String smallPath;

    public UploadFileInfo() {

    }

    public UploadStateType getStatus() {
        return this.status;
    }

    public void setStatus(UploadStateType status) {
        this.status = status;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getBucket() {
        return this.bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getObject() {
        return this.object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getSmallPath() {
        return smallPath;
    }

    public void setSmallPath(String smallPath) {
        this.smallPath = smallPath;
    }

    public boolean equals(UploadFileInfo uploadFileInfo) {
        return null == uploadFileInfo ? false : (!StringUtil.isEmpty(uploadFileInfo.filePath) && uploadFileInfo.filePath.equals(this.filePath)
                ? true : false);
    }

    public VodInfo getVodInfo() {
        return this.vodInfo;
    }

    public void setVodInfo(VodInfo vodInfo) {
        this.vodInfo = vodInfo;
    }

}
