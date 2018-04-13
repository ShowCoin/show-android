package one.show.live.netutil.okhttputils;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import one.show.live.netutil.okhttputils.cache.CacheEntity;
import one.show.live.netutil.okhttputils.cache.CacheMode;
import one.show.live.netutil.okhttputils.cookie.CookieJarImpl;
import one.show.live.netutil.okhttputils.cookie.store.CookieStore;
import one.show.live.netutil.okhttputils.https.HttpsUtils;
import one.show.live.netutil.okhttputils.interceptor.LoggerInterceptor;
import one.show.live.netutil.okhttputils.model.HttpHeaders;
import one.show.live.netutil.okhttputils.model.HttpParams;
import one.show.live.netutil.okhttputils.request.DeleteRequest;
import one.show.live.netutil.okhttputils.request.GetRequest;
import one.show.live.netutil.okhttputils.request.HeadRequest;
import one.show.live.netutil.okhttputils.request.OptionsRequest;
import one.show.live.netutil.okhttputils.request.PostRequest;
import one.show.live.netutil.okhttputils.request.PutRequest;
import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.Util;
import okio.Buffer;
import one.show.live.po.POBaiduLocation;
import one.show.live.po.POMember;
import one.show.live.util.AppUtil;
import one.show.live.util.ChannelUtil;
import one.show.live.util.ConstantKey;
import one.show.live.util.DeviceUtils;
import one.show.live.util.DeviceUuidFactory;
import one.show.live.util.MD5;
import one.show.live.util.SXUtil;
import one.show.live.util.StringUtils;
import one.show.live.util.WifiMacAddress;

/**
 * Created by J-King on 2018/1/22.
 */

public class OkHttpUtils {

    public static final int DEFAULT_MILLISECONDS = 15000; //默认的超时时间
    private static OkHttpUtils mInstance;                 //单例
    private Handler mDelivery;                            //用于在主线程执行的调度器
    private OkHttpClient.Builder okHttpClientBuilder;     //ok请求的客户端
    private HttpParams mCommonParams;                     //全局公共请求参数
    private HttpHeaders mCommonHeaders;                   //全局公共请求头
    private CacheMode mCacheMode;                         //全局缓存模式
    private long mCacheTime = CacheEntity.CACHE_NEVER_EXPIRE;  //全局缓存过期时间,默认永不过期
    private static Application context;                   //全局上下文
    private CookieJarImpl cookieJar;                      //全局 Cookie 实例
    private ExecutorService executorService;              //取消请求的线程池


    private static String mDeviceId, versionName, channel;
    private static String versioncode;
    private static String os;

    private OkHttpUtils() {
        okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.hostnameVerifier(new DefaultHostnameVerifier());
        mDelivery = new Handler(Looper.getMainLooper());
    }

    public static OkHttpUtils getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpUtils();
                }
            }
        }
        return mInstance;
    }

    /** 必须在全局Application先调用，获取context上下文，否则缓存无法使用 */
    public static void init(Application app) {
        context = app;
        AppUtil appUtil = new AppUtil(context);
        channel = ChannelUtil.getChannel(context);
        if (StringUtils.isEmpty(channel)) {
            channel = "beke";
        }
        mDeviceId = new DeviceUuidFactory(context).getDeviceUuid().toString();
        versionName = appUtil.getVersionName();
        versioncode = appUtil.getVersionName();
        os = StringUtils.isNotEmpty(DeviceUtils.getSystemProperty("ro.miui.ui.version.name")) ? "MIUI" : (DeviceUtils.isFlyme() ? "FlymeOs" : "android");

    }

    /** 获取全局上下文 */
    public static Context getContext() {
        if (context == null) throw new IllegalStateException("请先在全局Application中调用 OkHttpUtils.init() 初始化！");
        return context;
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClientBuilder.build();
    }

    /** get请求 */
    public static GetRequest get(String url) {
        return new GetRequest(url);
    }

    /** post请求 */
    public static PostRequest post(String url) {
        return new PostRequest(url);
    }

    /** put请求 */
    public static PutRequest put(String url) {
        return new PutRequest(url);
    }

    /** head请求 */
    public static HeadRequest head(String url) {
        return new HeadRequest(url);
    }

    /** delete请求 */
    public static DeleteRequest delete(String url) {
        return new DeleteRequest(url);
    }

    /** patch请求 */
    public static OptionsRequest options(String url) {
        return new OptionsRequest(url);
    }

    /** 调试模式 */
    public OkHttpUtils debug(String tag) {
        okHttpClientBuilder.addInterceptor(new LoggerInterceptor(tag, true));
        return this;
    }

    /**
     * 此类是用于主机名验证的基接口。 在握手期间，如果 URL 的主机名和服务器的标识主机名不匹配，
     * 则验证机制可以回调此接口的实现程序来确定是否应该允许此连接。策略可以是基于证书的或依赖于其他验证方案。
     * 当验证 URL 主机名使用的默认规则失败时使用这些回调。如果主机名是可接受的，则返回 true
     */
    public class DefaultHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /** https的全局访问规则 */
    public OkHttpUtils setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        okHttpClientBuilder.hostnameVerifier(hostnameVerifier);
        return this;
    }

    /** https的全局自签名证书 */
    public OkHttpUtils setCertificates(InputStream... certificates) {
        okHttpClientBuilder.sslSocketFactory(HttpsUtils.getSslSocketFactory(certificates, null, null));
        return this;
    }

    /** https的全局自签名证书 */
    public OkHttpUtils setCertificates(String... certificates) {
        for (String certificate : certificates) {
            InputStream inputStream = new Buffer().writeUtf8(certificate).inputStream();
            setCertificates(inputStream);
        }
        return this;
    }

    /** 全局cookie存取规则 */
    public OkHttpUtils setCookieStore(CookieStore cookieStore) {
        cookieJar = new CookieJarImpl(cookieStore);
        okHttpClientBuilder.cookieJar(cookieJar);
        return this;
    }

    /** 获取全局的cookie实例 */
    public CookieJarImpl getCookieJar() {
        return cookieJar;
    }

    /** 全局读取超时时间 */
    public OkHttpUtils setReadTimeOut(int readTimeOut) {
        okHttpClientBuilder.readTimeout(readTimeOut, TimeUnit.MILLISECONDS);
        return this;
    }

    /** 全局写入超时时间 */
    public OkHttpUtils setWriteTimeOut(int writeTimeout) {
        okHttpClientBuilder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    /** 全局连接超时时间 */
    public OkHttpUtils setConnectTimeout(int connectTimeout) {
        okHttpClientBuilder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        return this;
    }

    /** 全局的缓存模式 */
    public OkHttpUtils setCacheMode(CacheMode cacheMode) {
        mCacheMode = cacheMode;
        return this;
    }

    /** 获取全局的缓存模式 */
    public CacheMode getCacheMode() {
        return mCacheMode;
    }

    /** 全局的缓存过期时间 */
    public OkHttpUtils setCacheTime(long cacheTime) {
        if (cacheTime <= -1) cacheTime = CacheEntity.CACHE_NEVER_EXPIRE;
        mCacheTime = cacheTime;
        return this;
    }

    /** 获取全局的缓存过期时间 */
    public long getCacheTime() {
        return mCacheTime;
    }

    /** 获取全局公共请求参数 */
    public HttpParams getCommonParams() {
        return mCommonParams;
    }

    /** 添加全局公共请求参数 */
    public OkHttpUtils addCommonParams(HttpParams commonParams) {
        if (mCommonParams == null) mCommonParams = new HttpParams();
        mCommonParams.put(commonParams);
        return this;
    }

    /** 获取全局公共请求头 */
    public HttpHeaders getCommonHeaders() {
        return mCommonHeaders;
    }

    /** 添加全局公共请求参数 */
    public OkHttpUtils addCommonHeaders(HttpHeaders commonHeaders) {
        if (mCommonHeaders == null) mCommonHeaders = new HttpHeaders();
        mCommonHeaders.put(commonHeaders);
        return this;
    }

    /** 添加全局拦截器 */
    public OkHttpUtils addInterceptor(@Nullable Interceptor interceptor) {
        okHttpClientBuilder.addInterceptor(interceptor);
        return this;
    }

    /** 根据Tag取消请求 */
    public void cancelTag(Object tag) {
        for (final Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (final Call call : getOkHttpClient().dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public synchronized ExecutorService executorService() {
        if (executorService == null) {
            executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                    new SynchronousQueue<Runnable>(), Util.threadFactory("OkHttpUtils Executor", false));
        }
        return executorService;
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
        params.put("Net-Type", String.valueOf(SXUtil.getNetworkType(context)));


        String userIdStr = StringUtils.ifNullReturnEmpty(POMember.getInstance().getUid());
        String userToken = StringUtils.ifNullReturnEmpty(POMember.getInstance().getBeke_token());
        String userPhoneNumber = POMember.getInstance().getPhoneNumber();
        //尝试通过系统api获取手机号
        userPhoneNumber = userPhoneNumber == null ? DeviceUtils.getDeviceNumber(context) : userPhoneNumber;


        //登陆后用户id(登陆成功后由服务端返回)
        params.put("Beke-Userid", userIdStr);
        //登陆后用户token(登陆成功后由服务端返回)
        params.put("Beke-UserToken", userToken);
        //手机号码
        params.put("Phone-Number", userPhoneNumber);

        //推送id
        params.put("Push-Id", "");
        //渠道
        params.put("Channel", channel);
        //时间
        params.put("Time", String.valueOf(System.currentTimeMillis() / 1000));
        //MAC地址，仅对WIFI连接时有效
        params.put("MAC", WifiMacAddress.getWifiMac(context));
        //IMEI串号
        params.put("IM", WifiMacAddress.getIMEI(context));
        //ios用的android用不到
        params.put("IDFA", "");
        //ios用的android用不到
        params.put("IDFY", "");
        //终端运营商名称
        params.put("OP", StringUtils.ifNullReturnEmpty(WifiMacAddress.getOperators(context)));
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
        params.put("SC", StringUtils.ifNullReturnEmpty(DeviceUtils.getScreenResolution(context)));
        //应用版本名，字符串型，自定义的
        params.put("VN", versionName);
        //扩展字段
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
            return MD5.MD5Encode(result).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



}
