package one.show.live.netutil.netmanager.common;


public interface NetLoadingListener extends NetResponseListener {

    void onBefore();

    void onProgress(BaseNetLoading netLoading);

    void onAfter(BaseNetResponse netResponse);

}
