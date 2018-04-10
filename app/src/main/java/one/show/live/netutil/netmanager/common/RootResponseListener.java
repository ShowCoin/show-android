package one.show.live.netutil.netmanager.common;


import org.json.JSONObject;


public class RootResponseListener implements NetResponseListener {

    private NetCache mNetCache;
    private DataRequest mDataRequest;

    public RootResponseListener(NetCache netCache, DataRequest dataRequest) {
        this.mNetCache = netCache;
        this.mDataRequest = dataRequest;
    }

    @Override
    public void onNetResponse(BaseNetResponse response) {
        if (!mDataRequest.isCanceled() && mDataRequest.getListener() != null) {
            mDataRequest.getListener().onNetResponse(response);
        }
        mDataRequest.finish();
        mNetCache.removeRequest(mDataRequest);
    }

    @Override
    public void onNetError(BaseNetResponse response) {
        if (!mDataRequest.isCanceled() && mDataRequest.getListener() != null) {
            String temp = response.getContent();
            if (temp != null) {
                try {
                    JSONObject jsonObject = new JSONObject(temp);
                    String errorLog = (String) jsonObject.opt("msg");
                    response.setServerMsg(errorLog);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            mDataRequest.getListener().onNetError(response);
            //拦截403，跳转至登录页
//            OAuthInterceptor.getInstance().intercept(response);
        }
        mDataRequest.finish();
        mNetCache.removeRequest(mDataRequest);
    }

}
