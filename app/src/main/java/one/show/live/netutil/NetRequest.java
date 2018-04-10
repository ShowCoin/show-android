package one.show.live.netutil;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.Map;

import one.show.live.api.BaseAPI;
import one.show.live.netutil.lsn.DownloadPregressListener;
import one.show.live.netutil.lsn.DownloadResultListener;
import one.show.live.netutil.lsn.OnResponseListener;
import one.show.live.netutil.netmanager.common.BaseNetLoading;
import one.show.live.netutil.netmanager.common.BaseNetResponse;
import one.show.live.netutil.netmanager.common.NetExecutor;
import one.show.live.netutil.netmanager.common.NetLoadingListener;
import one.show.live.netutil.netmanager.common.NetMethod;
import one.show.live.netutil.netmanager.common.NetResponseListener;
import one.show.live.netutil.netmanager.common.NetStatusCode;
import one.show.live.netutil.netmanager.utils.NetUtils;
import one.show.live.netutil.okhttputils.OkHttpUtils;

/**
 * Created by J-King on 2018/1/23.
 */

public class NetRequest {

    private static int flag = 0;

    private static Object tag = "Request";

    private static NetRequest mInstance;

    private static final String REQUESTTAG = "SEEUHTTP";

    public static NetRequest getInstance() {
        if (mInstance == null) {
            synchronized (NetRequest.class) {
                if (mInstance == null) {
                    mInstance = new NetRequest();
                }
            }
        }
        return mInstance;
    }

    public static void postFormRequest(final String url, Map<String, String> params, final Class cls, final OnResponseListener mListener) {
        formRequest(NetMethod.POST, url, params, flag, tag, cls, mListener);
    }

    public static void getFormRequest(final String url, Map<String, String> params, final Class cls, final OnResponseListener mListener) {
        formRequest(NetMethod.GET, url, params, flag, tag, cls, mListener);
    }


    /**
     * 不带进度只有成功失败回调的 下载 方法
     *
     * @param url
     * @param destFileDir  要保存的目标文件夹  示例 ： Environment.getExternalStorageDirectory() + "/download"
     * @param destFileName 要保存的文件名     示例：： xxx.apk
     * @param mListener
     */
    public static void downloadFile(String url, String destFileDir, String destFileName, final DownloadResultListener mListener) {
        if (null != mListener) {
            mListener.downFailed(NetStatusCode.UNKNOWN_ERROR, "网络未连接");
            return;
        }
        downloadFile(url, destFileDir, destFileName, new NetLoadingListener() {
            @Override
            public void onBefore() {

            }

            @Override
            public void onProgress(BaseNetLoading netLoading) {

            }

            @Override
            public void onAfter(BaseNetResponse netResponse) {

            }

            @Override
            public void onNetResponse(BaseNetResponse response) {
                if (null != mListener) {
                    mListener.downloadSuccess();
                }
            }

            @Override
            public void onNetError(BaseNetResponse response) {
                if (null != mListener) {
                    mListener.downFailed(NetStatusCode.SERVER_UNKNOW_ERROR_STATUS, "下载出错");
                }
            }
        });
    }

    /**
     * 带进度的 下载 方法
     *
     * @param url
     * @param destFileDir  要保存的目标文件夹  示例 ： Environment.getExternalStorageDirectory() + "/download"
     * @param destFileName 要保存的文件名     示例：： xxx.apk
     * @param mListener
     */
    public static void downloadFile(String url, String destFileDir, String destFileName, final DownloadPregressListener mListener) {
        downloadFile(url, destFileDir, destFileName, new NetLoadingListener() {
            @Override
            public void onBefore() {
                if (null != mListener) {
                    mListener.onBefore();
                }
            }

            @Override
            public void onProgress(BaseNetLoading netLoading) {
                if (null != mListener) {
                    mListener.onProgress(netLoading.getCurrentSize(), netLoading.getTotalSize()
                            , netLoading.getProgress(), netLoading.getNetworkSpeed());
                }
            }

            @Override
            public void onAfter(BaseNetResponse netResponse) {
                if (null != mListener) {
                    mListener.onAfter();
                }
            }

            @Override
            public void onNetResponse(BaseNetResponse response) {

            }

            @Override
            public void onNetError(BaseNetResponse response) {

            }
        });
    }

    /**
     * 即有进度，又有成功失败回调的下载方法
     *
     * @param url
     * @param listener
     * @param destFileDir  要保存的目标文件夹  示例 ： Environment.getExternalStorageDirectory() + "/download"
     * @param destFileName 要保存的文件名     示例：： xxx.apk
     */
    public static void downloadFile(String url, String destFileDir, String destFileName, NetLoadingListener listener) {
        NetExecutor.getInstance().downloadFile(url, null, flag, null, listener, destFileDir, destFileName);
    }

    private static void formRequest(int method, final String url, Map<String, String> params, int flag, Object tag, final Class cls, final OnResponseListener mListener) {
        if (!NetUtils.isNetworkConnected(OkHttpUtils.getContext())) {
            mListener.onInternError(NetStatusCode.UNKNOWN_ERROR, "网络未连接");
            return;
        }
        Log.d(REQUESTTAG, "requestUrl--->>" + BaseAPI.Domain.bizDomain + url + ",mParams---->>" + ((null == params) ? "" : params.toString()));
        NetExecutor.getInstance().formRequest(method, BaseAPI.Domain.bizDomain + url, params, flag, tag, null, new NetResponseListener() {
            @Override
            public void onNetResponse(BaseNetResponse response) {
                try {
                    Log.d(REQUESTTAG, "response--->>" + response.getExtra().toString());
                }catch (Exception e){

                }
                if (TextUtils.isEmpty(url)) {
                    handleResponse(response, cls, mListener, url);
                } else {
                    handleCommonResponse(response, cls, mListener);
                }
            }

            @Override
            public void onNetError(BaseNetResponse response) {
                try {
                    Log.d(REQUESTTAG, "error--->>" + response.getExtra().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (null != mListener) {
                    mListener.onInternError(NetStatusCode.UNKNOWN_ERROR, "访问人数过多，请稍后再试！");
                }
            }
        });
    }

    private static void handleCommonResponse(BaseNetResponse response, final Class cls, OnResponseListener mListener) {
        if (null != response && !TextUtils.isEmpty(response.getExtra().toString())) {
            try {
                JSONObject mJson = new JSONObject(response.getExtra().toString());
                if (1 == mJson.getInt("state")) {
                    if (mJson.has("data")) {
                        if (!TextUtils.isEmpty(mJson.getString("data"))) {
                            if (null != cls) {
                                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                                Object pk = gson.fromJson(mJson.getString("data"), cls);
                                mListener.onComplete(pk, mJson.getInt("state"), mJson.getString("msg"));
                                return;
                            } else {
                                mListener.onComplete(response.getExtra().toString(), mJson.getInt("state"), mJson.getString("msg"));
                                return;
                            }
                        } else {
                            mListener.onComplete("", mJson.getInt("state"), mJson.getString("msg"));
                            return;
                        }
                    } else {
                        mListener.onComplete("", mJson.getInt("state"), mJson.getString("msg"));
                        return;
                    }
                } else {
                    try {
                        Log.d(REQUESTTAG, "errorState--->>" + mJson.getInt("state") + ",error--->>" + mJson.getString("msg"));
                    }catch (Exception e){

                    }
                    mListener.onInternError(mJson.getInt("state"), mJson.getString("msg"));
                    return;
                }
            } catch (Exception e) {
                Log.d(REQUESTTAG, "error--->>" + "数据解析错误");
                mListener.onInternError(NetStatusCode.ANALYTIC_DATSA_ERROR, "数据解析错误");
                return;
            }
        } else {
            Log.d(REQUESTTAG, "error--->>" + "服务器返回数据是空");
            mListener.onInternError(NetStatusCode.ANALYTIC_DATSA_ERROR, "服务器返回数据是空");
            return;
        }
    }


    private static void handleResponse(BaseNetResponse response, final Class cls, OnResponseListener mListener, String url) {
        if (null != response && !TextUtils.isEmpty(response.getExtra().toString())) {
            try {
                JSONObject mJson = new JSONObject(response.getExtra().toString());
                if (1 == mJson.getInt("state") || 0 == mJson.getInt("state")) {
                    if (mJson.has("data")) {
                        if (!TextUtils.isEmpty(mJson.getString("data"))) {
                            if (null != cls) {
                                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                                Object pk = gson.fromJson(mJson.getString("data"), cls);
                                mListener.onComplete(pk, mJson.getInt("state"), mJson.getString("msg"));
                                return;
                            } else {
                                mListener.onComplete(response.getExtra().toString(), mJson.getInt("state"), mJson.getString("msg"));
                                return;
                            }
                        } else {
                            mListener.onComplete("", mJson.getInt("state"), mJson.getString("msg"));
                            return;
                        }
                    } else {
                        mListener.onComplete("", mJson.getInt("state"), mJson.getString("msg"));
                        return;
                    }
                } else {
                    try {
                        Log.d(REQUESTTAG, "errorState--->>" + mJson.getInt("state") + ",error--->>" + mJson.getString("msg"));
                    }catch (Exception e){

                    }
                    mListener.onInternError(mJson.getInt("state"), mJson.getString("msg"));
                    return;
                }
            } catch (Exception e) {
                Log.d(REQUESTTAG, "error--->>" + "数据解析错误");
                mListener.onInternError(NetStatusCode.ANALYTIC_DATSA_ERROR, "数据解析错误");
                return;
            }
        } else {
            Log.d(REQUESTTAG, "error--->>" + "服务器返回数据是空");
            mListener.onInternError(NetStatusCode.ANALYTIC_DATSA_ERROR, "服务器返回数据是空");
            return;
        }
    }


}
