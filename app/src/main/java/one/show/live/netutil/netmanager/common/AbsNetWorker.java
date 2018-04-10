package one.show.live.netutil.netmanager.common;

import java.util.Map;


public abstract class AbsNetWorker<F, R, L> {

    public abstract void cancel(Object tag);

    public abstract void requestDelete(String url, Map<String, String> headers, String body,
                                       F flag, Object tag, Class cls, R listener);

    public abstract void formRequest(int method, String url, Map<String, String> headers, Map<String, String> params,
                                     F flag, Object tag, Class cls, R listener);

    public abstract void jsonRequestPost(String url, Map<String, String> headers, String json,
                                         F flag, Object tag, Class cls, R listener);

    public abstract void downloadFile(String url, Map<String, String> headers, Map<String, String> params,
                                      F flag, Object tag, L listener, String destFileDir, String destFileName);

    public abstract void uploadFile(String url, Map<String, String> headers, Map<String, String> params, Map<String, String> files,
                                    F flag, Object tag, L listener);

}
