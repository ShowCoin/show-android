package one.show.live;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.content.res.Resources;
import android.support.multidex.MultiDex;

import com.bugtags.library.Bugtags;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.umeng.analytics.MobclickAgent;
import java.io.IOException;
import java.io.InputStream;
import java.net.Proxy;
import cn.jpush.android.api.JPushInterface;
import one.show.live.log.Logger;
import one.show.live.netutil.okhttputils.OkHttpUtils;
import one.show.live.util.ConfigCacheManager;
import one.show.live.util.PublicCacheManager;
import one.show.live.api.BaseRequest;
import one.show.live.greendao.DaoSession;
import one.show.live.ui.BaseApplication;
import one.show.live.util.AppUtil;
import one.show.live.util.ChannelUtil;
import one.show.live.util.Constants;
import one.show.live.util.DeviceUtils;
import one.show.live.util.FileUtils;
import one.show.live.util.StringUtils;

public class MeetApplication extends BaseApplication {

    private static MeetApplication meetApplication;
    private static DaoSession daoSession;
    private static Context context;
    public PackageInfo packageInfo;
    public static String mUmengChannel;
    public String versionName;
    public int versionCode;

    private String miui;
    public static Tencent mTencent;
    public static boolean hasNewVersion;//判断是否有新版本的
    private IWXAPI iwxapi;
    public static boolean isStart;//判断是否启动应用
    public static boolean fixWindow;//判断是否显示修改资料页面顶部的提示条
    private static Handler uiHandler = new Handler(Looper.getMainLooper());


    public static Context getAppContext() {
        return context;
    }
    public static Resources getAppResources() {
        return context.getResources();
    }



    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();

        if (!AppUtil.isMainProcess(getApplicationContext())) {
            return;
        }


        meetApplication = this;

        //这个必须在最前方
        initAppInfo();

        initOkHttp();

        if (StringUtils.isNotEmpty(Constants.QQAPPID) && mTencent == null) {
            mTencent = Tencent.createInstance(Constants.QQAPPID, this);
        }

//        MediaSdk.init(this, false);
        ConfigCacheManager.init(getApplicationContext());
        PublicCacheManager.init(getApplicationContext());

        initbugtags();//初始化bugtags
        initNewRecorder();
        jpuscInit();//初始化极光
//        openStrickMode();

        try {
            setIwxapi(WXAPIFactory.createWXAPI(this, Constants.WEI_XIN_APP_ID, false));
            getIwxapi().registerApp(Constants.WEI_XIN_APP_ID);
        } catch (Exception e) {
            Logger.e(e);
        }
        Initialize();
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        MobclickAgent.setDebugMode(true);
        MobclickAgent.setCatchUncaughtExceptions(false);

//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);//leaks监控内存泄漏
        //初始化微博
        WbSdk.install(this, new AuthInfo(this, Constants.Weibo_APP_KEY, Constants.Weibo_REDIRECT_URL, Constants.Weibo_SCOPE));
    }

    private void initbugtags() {
        Bugtags.start("23ef3d2f0d5438c2e8cabea28d5cd1ae", this, Bugtags.BTGInvocationEventBubble);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    /**
     * 队列下载配置
     */
    public void Initialize() {

        String path = FileUtils.FILE_DOWNLOAD_PATH;
        FileDownloadUtils.setDefaultSaveRootPath(path);//所有下载文件的存储路径
        FileDownloader.setupOnApplicationOnCreate(this)
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                        .proxy(Proxy.NO_PROXY) // set proxy
                ))
                .commit();
    }

    public static DaoSession getDaoInstant() {
        return daoSession;
    }


    /**
     * 获取当前Application
     */
    public static MeetApplication getInstance() {
        return meetApplication;
    }

    public IWXAPI getIwxapi() {
        return iwxapi;
    }

    public void setIwxapi(IWXAPI iwxapi) {
        this.iwxapi = iwxapi;
    }

    /**
     * 提取应用版本信息
     */
    private void initAppInfo() {
        BaseRequest.init(this);

        try {
            mUmengChannel = ChannelUtil.getChannel(this);
        } catch (Exception e) {

        }

        if (StringUtils.isEmpty(mUmengChannel)) {
            mUmengChannel = "100000";
        }

        try {
//            MobclickAgent.setDebugMode(Logger.getIsDebug());
//            MobclickAgent.setCatchUncaughtExceptions(false);//设置umeng错误统计，发布时请开启友盟的错误统计，也就是把这一行注释掉就行了

            packageInfo =
                    getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
            miui = DeviceUtils.getSystemProperty("ro.miui.ui.version.name");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    private void initRecorder(){
//        // 设置拍摄视频缓存路径
//        File dcim = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//        if (DeviceUtils.isZte()) {
//            if (dcim.exists()) {
//                VCamera.setVideoCachePath(dcim + "/Beke/Meet/");
//            } else {
//                VCamera.setVideoCachePath(dcim.getPath().replace("/sdcard/", "/sdcard-ext/") + "/Beke/Meet/");
//            }
//        } else {
//            VCamera.setVideoCachePath(dcim + "/Beke/Meet/");
//        }
//        // 开启log输出,ffmpeg输出到logcat
//        VCamera.setDebugMode(true);
//        // 初始化拍摄SDK，必须
//        VCamera.initialize(this);
//    }

    private void initNewRecorder() {
//        //初始化底层库，必需调用！！！
//        UtilityAdapter.FFmpegInit(this, "");
//        //准备测试用的数据
//        GlobalSettings.copyFilesFormAssets(this, "ffmpeg", GlobalSettings.getCachePath());
//        setVideoCachePath();
    }

    /**
     * 极光推送初始化
     */
    public void jpuscInit() {
        JPushInterface.setDebugMode(false);    // 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);            // 初始化 JPush
    }

//    private void setVideoCachePath(){
//        // 设置拍摄视频缓存路径
//        String cachePath = GlobalSettings.getCachePath();
////        File dcim;
////        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
////            dcim = Environment.getExternalStorageDirectory();
////        } else {
////            dcim = Environment.getDownloadCacheDirectory();
////        }
////        if (DeviceUtils.isZte()) {
////            if (dcim.exists()) {
////                videoCachePath = dcim + "/Meet/video/";
////            } else {
////                videoCachePath = dcim.getPath().replace("/sdcard/", "/sdcard-ext/") + "/Meet/video/";
////            }
////        } else {
////            videoCachePath = dcim + "/Meet/video/";
////        }
//        videoCachePath = cachePath + "/local/";
//    }

    /**
     * 关闭应用程序
     */
    public static void finishApp() {
        baseApplication = null;
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private void initOkHttp(){
        OkHttpUtils.init(this);
        try {
            InputStream[] mInputStream = {this.getAssets().open("beke.crt")};
            OkHttpUtils.getInstance().setCertificates(mInputStream)
                    .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                    .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                    .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS);
        } catch (IOException e) {
            OkHttpUtils.getInstance()
                    .setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                    .setReadTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                    .setWriteTimeOut(OkHttpUtils.DEFAULT_MILLISECONDS);
        }
    }

    public static void runOnUiThread(Runnable r) {
        uiHandler.post(r);
    }

    public static void runOnUiThread(Runnable r, long delayMillis) {
        uiHandler.postDelayed(r, delayMillis);
    }
}
