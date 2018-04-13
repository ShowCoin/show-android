package one.show.live.common.api;


import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.Map;

import one.show.live.common.net.OnUploadProgressListener;
import one.show.live.common.po.POCommonResp;
import one.show.live.common.po.POEventBus;
import one.show.live.common.po.POMember;
import one.show.live.common.util.Constants;

/**
 * 网络请求创建抽象方法
 *
 * 此request必须在工作线程调用
 */
public abstract class CommonRequestSync<T> extends BaseRequest {

    protected POCommonResp<T> responseBean;

    /**
     * JSON解析
     */
    public abstract void onRequestResult(String result);

    public void startRequest(final Map<String, String> params) {
        startRequest(params, null, null);
    }

    public void startRequest(final Map<String, String> params, final Map<String, String> files, final OnUploadProgressListener listener) {
                request(params, files, listener);
                onRequestFinish();
    }

    @Override
    public void processResult(String result) {
        try {
            onRequestResult(result);

            if (responseBean == null) {
                Type type = new TypeToken<POCommonResp<T>>() {
                }.getType();
                responseBean = new Gson().fromJson(result, type);
            }
        } catch (Exception e) {
            responseBean = new POCommonResp<>();
            responseBean.setState(Constants.SERVER_UNKNOW_ERROR_STATUS);
            responseBean.setMsg("访问人数过多，请稍后再试！");
            e.printStackTrace();
            return;
        }
    }


    @Override
    protected void onRequestFinish() {

    }

}
