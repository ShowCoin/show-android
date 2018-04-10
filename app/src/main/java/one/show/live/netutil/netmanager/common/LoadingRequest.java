package one.show.live.netutil.netmanager.common;

import java.util.Map;


public class LoadingRequest extends BaseRequest<NetLoadingListener, LoadingRequest> {

    private String mDestFileDir;
    private String mDestFileName;
    private Map<String, String> mFiles;

    public String getDestFileDir() {
        return mDestFileDir;
    }

    public LoadingRequest setDestFileDir(String destFileDir) {
        this.mDestFileDir = destFileDir;
        return this;
    }

    public String getDestFileName() {
        return mDestFileName;
    }

    public LoadingRequest setDestFileName(String destFileName) {
        this.mDestFileName = destFileName;
        return this;
    }

    public Map<String, String> getFiles() {
        return mFiles;
    }

    public LoadingRequest setFiles(Map<String, String> files) {
        this.mFiles = files;
        return this;
    }
}
