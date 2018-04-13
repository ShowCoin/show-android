package one.show.live.api;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import one.show.live.net.HttpsUtils;
import one.show.live.net.OkHttpRequest;
import one.show.live.net.OnUploadProgressListener;
import one.show.live.po.POBaiduLocation;
import one.show.live.po.POMember;
import one.show.live.util.AppUtil;
import one.show.live.util.ChannelUtil;
import one.show.live.util.ConstantKey;
import one.show.live.util.Constants;
import one.show.live.util.DeviceUtils;
import one.show.live.util.DeviceUuidFactory;
import one.show.live.util.MD5;
import one.show.live.util.NetworkUtils;
import one.show.live.util.SXUtil;
import one.show.live.util.StringUtils;
import one.show.live.util.WifiMacAddress;

/**
 * 基础网络请求
 */
public abstract class BaseRequest {
    public static String BASE_DOMAIN;
    public static double latitude, longitude;
    public static String city;

    private static long lastCheckTime = 0;
    private static String mDeviceId, versionName, channel;
    private static String versioncode;

    private static Application application;
    private boolean requesting;
    private static String os;


    public static HttpsUtils.SSLParams sslParams;


    public static void init(Application application) {
        BaseRequest.application = application;

        try {
            sslParams = HttpsUtils.getSslSocketFactory(new InputStream[]{application.getAssets().open("beke.crt")}, null, null);
        } catch (IOException e) {
            e.printStackTrace();
            sslParams = HttpsUtils.getSslSocketFactory(null, null, null);
        }

        AppUtil appUtil = new AppUtil(application);
        channel = ChannelUtil.getChannel(application);
        if (StringUtils.isEmpty(channel)) {
            channel = "beke";
        }
        mDeviceId = new DeviceUuidFactory(application).getDeviceUuid().toString();
        versionName = appUtil.getVersionName();
        versioncode = appUtil.getVersionName();
        os = StringUtils.isNotEmpty(DeviceUtils.getSystemProperty("ro.miui.ui.version.name")) ? "MIUI" : (DeviceUtils.isFlyme() ? "FlymeOs" : "android");
    }


    public abstract void startRequest(final Map<String, String> params);

    public abstract void startRequest(final Map<String, String> params, final Map<String, String> files, final OnUploadProgressListener listener);

    public abstract String getRequestUrl();

    public abstract void processResult(String result);

    protected abstract void onRequestFinish();

    public boolean isRequesting() {
        return requesting;
    }


    protected void request(final Map<String, String> params, final Map<String, String> files, final OnUploadProgressListener listener) {
        requesting = true;
        InputStream inputStream = null;

        long time = System.currentTimeMillis();
        try {
            if (files != null) {
                inputStream = OkHttpRequest.getInstance(sslParams).post(getRequestUrl(), getIsSign() ? getHeadParams(params) : null, params, files, listener);
            } else {
                inputStream = OkHttpRequest.getInstance(sslParams).post(getRequestUrl(), getIsSign() ? getHeadParams(params) : null, params);
            }
            //不需要解压缩
//            byte[] response = ZLibUtil.decompress(new NetworkUtils().readInputStream(inputStream));
//            processResult(new String(response, "UTF-8"));
            processResult(new String(new NetworkUtils().readInputStream(inputStream), "UTF-8"));
        } catch (Exception e) {
            Log.e("requestError", getRequestUrl());
            e.printStackTrace();
            processResult("{\"state\":" + Constants.NETWORK_ERROR_STATUS + ",\"msg\":\"啊哦!SeeU君也不知道怎么了，稍后再试一下吧~\"}");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("请求时间>>" + (System.currentTimeMillis() - time));
        }
        requesting = false;
    }


    protected void requestByGet() {
        requesting = true;
        InputStream inputStream = null;

        long time = System.currentTimeMillis();
        try {
            inputStream = OkHttpRequest.getInstance(sslParams).get(getRequestUrl());
            //不需要解压缩
//            byte[] response = ZLibUtil.decompress(new NetworkUtils().readInputStream(inputStream));
//            processResult(new String(response, "UTF-8"));
            processResult(new String(new NetworkUtils().readInputStream(inputStream), "UTF-8"));
        } catch (Exception e) {
            Log.e("requestError", getRequestUrl());
            e.printStackTrace();
            processResult("{\"state\":" + Constants.NETWORK_ERROR_STATUS + ",\"msg\":\"啊哦!秀播君也不知道怎么了，稍后再试一下吧~\"}");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("请求时间>>" + (System.currentTimeMillis() - time));
        }
        requesting = false;
    }

    /**
     * 整理需要提交的参数
     *
     * @param params 参数的map集合
     * @return 服务器需要的
     */
    public static Map<String, String> getHeadParams(Map<String, String> params) {
        Map<String, String> headParams = new HashMap<>();

        appendCommonParams(headParams);

        headParams.put("Sign", getSignData(headParams, params));
        return headParams;
    }


    public boolean getIsSign() {
        return true;
    }


    /**
     * 添加公共参数
     * <p>
     * 此方法不允许抛出任何异常，并且保证每个key都必须存在
     *
     * @param params
     */
    public static void appendCommonParams(Map<String, String> params) {
        //设备名称ID
        params.put("Device-Uuid", StringUtils.ifNullReturnEmpty(mDeviceId));
        //设备名称
        params.put("Device-Name", StringUtils.ifNullReturnEmpty(Build.BRAND));
        //手机系统
        params.put("OS", os);
        //内核版本
        params.put("Kernel-Version", StringUtils.ifNullReturnEmpty(Build.VERSION.RELEASE));
        //经度
        params.put("Longitude", String.valueOf(POBaiduLocation.longitude));
        //纬度
        params.put("Latitude", String.valueOf(POBaiduLocation.latitude));
        //城市
        String city = "";
        try {
            if (StringUtils.isNotEmpty(POBaiduLocation.address)) {
                city = URLEncoder.encode(POBaiduLocation.address, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("City", city);
        //app版本
        params.put("App-Version", String.valueOf(versioncode));
        //手机类型
        params.put("Phone-Type", StringUtils.ifNullReturnEmpty(Build.MODEL));
        //网络类型
        params.put("Net-Type", String.valueOf(SXUtil.getNetworkType(application)));


        String userIdStr = StringUtils.ifNullReturnEmpty(POMember.getInstance().getUid());
        String userToken = StringUtils.ifNullReturnEmpty(POMember.getInstance().getBeke_token());
        String userPhoneNumber = POMember.getInstance().getPhoneNumber();
        //尝试通过系统api获取手机号
        userPhoneNumber = userPhoneNumber == null ? DeviceUtils.getDeviceNumber(application) : userPhoneNumber;


        //登陆后用户id(登陆成功后由服务端返回)
        params.put("User-Id", userIdStr);
        //登陆后用户token(登陆成功后由服务端返回)
        params.put("User-Token", userToken);
        //手机号码
        params.put("Phone-Number", userPhoneNumber);

        //推送id
        params.put("Push-Id", "");
        //渠道
        params.put("Channel", channel);
        //时间
        params.put("Time", String.valueOf(System.currentTimeMillis() / 1000));
        //MAC地址，仅对WIFI连接时有效
        params.put("MAC", WifiMacAddress.getWifiMac(application));
        //IMEI串号
        params.put("IM", WifiMacAddress.getIMEI(application));
        //ios用的android用不到
        params.put("IDFA", "");
        //ios用的android用不到
        params.put("IDFY", "");
        //终端运营商名称
        params.put("OP", StringUtils.ifNullReturnEmpty(WifiMacAddress.getOperators(application)));
        //终端所处国家
        String country = "";
        try {
            if (StringUtils.isNotEmpty(POBaiduLocation.country)) {
                country = URLEncoder.encode(POBaiduLocation.country, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        params.put("CO", country);
        //屏幕分辨率
        params.put("SC", StringUtils.ifNullReturnEmpty(DeviceUtils.getScreenResolution(application)));
        //应用版本名，字符串型，自定义的
        params.put("VN", versionName);
        //扩展字段(马甲包使用的字段 正式版本不需要传入)
        params.put("TAG", "");
    }

    /**
     * 加密参数
     *
     * @param headParams
     * @return
     */
    public static String getSignData(Map<String, String> headParams, Map<String, String> bodyParams) {

        try {
            ArrayList<String> list = new ArrayList();
            for (Map.Entry<String, String> entry : headParams.entrySet()) {
                list.add(entry.getKey() + "=" + StringUtils.ifNullReturnEmpty(entry.getValue()));
            }

            if (bodyParams != null) {
                for (Map.Entry<String, String> entry : bodyParams.entrySet()) {
                    list.add(entry.getKey() + "=" + StringUtils.ifNullReturnEmpty(entry.getValue()));
                }
            }

            Collections.sort(list);
            StringBuilder sb = new StringBuilder();
            for (String str : list) {
                sb.append(str);
            }
            String result = sb.toString();
            result = result + ConstantKey.getKey();
            result = URLEncoder.encode(result, "UTF-8");
//            Log.e("samuel", "unsign>>>>>" + result);

            return MD5.MD5Encode(result).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
