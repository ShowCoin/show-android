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
import one.show.live.common.po.POEventBus;
import one.show.live.common.po.POMember;
import one.show.live.common.po.POCommonResp;
import one.show.live.common.util.Constants;

/**
 * 网络请求创建抽象方法
 * <p>
 * 此request必须在ui线程创建和调用，因为这里类成员包含handler。
 */
public abstract class CommonRequest<T> extends BaseRequest {
    protected POCommonResp<T> responseBean;

    private static final int ON_REQUEST_FINISH = 0x11;


    /**
     * JSON解析
     */
    public abstract void onRequestResult(String result);

    public void startRequest(final Map<String, String> params) {
        startRequest(params, null, null);
    }

    public void startRequest(final Map<String, String> params, final Map<String, String> files, final OnUploadProgressListener listener) {
        new Thread() {
            @Override
            public void run() {
                request(params, files, listener);
                onRequestFinish();
            }
        }.start();
    }

    public void startRequestByGet() {
        new Thread() {
            @Override
            public void run() {
                requestByGet();
                onRequestFinish();
            }
        }.start();
    }

    public void failRequest(String result){

    }

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


        if (responseBean.getState() == Constants.RESPONSE_CODE_TOKEN_FAIL) {//其他设备登陆
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    POMember.login(new POMember());
                    EventBus.getDefault().post(new POEventBus(Constants.RESPONSE_CODE_TOKEN_FAIL, responseBean.getMsg()));
                }
            }.start();
            return;
        }
        else if (responseBean.getState() == Constants.RESPONSE_CODE_USER_SEALED) {//封禁
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    POMember.login(new POMember());
                    EventBus.getDefault().post(new POEventBus(Constants.RESPONSE_CODE_USER_SEALED, responseBean.getMsg()));
                }
            }.start();
            return;
        }
//        else if (responseBean.getState() == Constants.RESPONSE_CODE_LOOUT) {
//            new Thread() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    POMember.login(new POMember());
//                    EventBus.getDefault().post(new POEventBus(Constants.RESPONSE_CODE_TOKEN_FAIL, responseBean.getMsg()));
//                }
//            }.start();
//            return;
//        }

        if (responseBean.getExt() == null) {
            return;
        }
        POCommonResp.Message message = responseBean.getExt().getMessage();
        if (message == null) {
            return;
        }

        if (message.getGift_message() > 0) {
            EventBus.getDefault().post(new POEventBus(Constants.RESPONSE_CODE_SHOW_GIFT, responseBean.getMsg()));
        }
        if (message.getTotal() > 0) {
            EventBus.getDefault().post(message);
        }

    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == ON_REQUEST_FINISH) {
                Log.e("samuel", "请求回调");
                try {
                    onFinish(responseBean.isSuccess(), responseBean.getMsg(), responseBean.getData());
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
            }
            return false;
        }
    });


    @Override
    protected void onRequestFinish() {
        handler.sendEmptyMessage(ON_REQUEST_FINISH);
    }

    public abstract void onFinish(boolean isSuccess, String msg, T data);
}
