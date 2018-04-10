package one.show.live.netutil.netmanager.common;

public class RootLoadingListener implements NetLoadingListener {

    private NetCache mNetCache;
    private LoadingRequest mLoadingRequest;

    public RootLoadingListener(NetCache netCache, LoadingRequest loadingRequest) {
        this.mNetCache = netCache;
        this.mLoadingRequest = loadingRequest;
    }

    @Override
    public void onBefore() {
        if (!mLoadingRequest.isCanceled() && mLoadingRequest.getListener() != null) {
            mLoadingRequest.getListener().onBefore();
        }
    }

    @Override
    public void onProgress(BaseNetLoading netLoading) {
        if (!mLoadingRequest.isCanceled() && mLoadingRequest.getListener() != null) {
            mLoadingRequest.getListener().onProgress(netLoading);
        }
    }

    @Override
    public void onAfter(BaseNetResponse netResponse) {
        if (!mLoadingRequest.isCanceled() && mLoadingRequest.getListener() != null) {
            mLoadingRequest.getListener().onAfter(netResponse);
        }
    }

    @Override
    public void onNetResponse(BaseNetResponse response) {
        if (!mLoadingRequest.isCanceled() && mLoadingRequest.getListener() != null) {
            mLoadingRequest.getListener().onNetResponse(response);
        }
        mLoadingRequest.finish();
        mNetCache.removeRequest(mLoadingRequest);
    }

    @Override
    public void onNetError(BaseNetResponse response) {
        if (!mLoadingRequest.isCanceled() && mLoadingRequest.getListener() != null) {
            mLoadingRequest.getListener().onNetError(response);
        }
        mLoadingRequest.finish();
        mNetCache.removeRequest(mLoadingRequest);
    }

}
