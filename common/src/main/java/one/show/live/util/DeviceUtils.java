package one.show.live.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * 系统版本信息类
 */
public class DeviceUtils {

    public static final int SDK_VERSION_1_5 = 3;

    public static final int SDK_VERSION_1_6 = 4;

    public static final int SDK_VERSION_2_0 = 5;

    public static final int SDK_VERSION_2_0_1 = 6;

    public static final int SDK_VERSION_2_1 = 7;

    public static final int SDK_VERSION_2_2 = 8;

    public static final int SDK_VERSION_2_3 = 9;

    public static final int SDK_VERSION_2_3_3 = 10;

    public static final int SDK_VERSION_3_0 = 11;

    public static final int SDK_VERSION_3_1 = 12;

    public static final int SDK_VERSION_3_2 = 13;

    public static final int SDK_VERSION_4_0 = 14;

    public static final int SDK_VERSION_4_0_3 = 15;

    public static final int SDK_VERSION_4_1_2 = 16;

    public static final int SDK_VERSION_4_2_2 = 17;

    public static final int SDK_VERSION_4_3 = 18;

    public static final int SDK_VERSION_4_4_2 = 19;

    //	/** 缓存屏幕的宽度 */
    //	private static int mCacheWindowWidth = -1;
    //	/** 缓存屏幕的高度 */
    //	private static int mCacheWindowHeight = -1;


    /**
     * >=5.0 21
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static String getDivice() {
        return StringUtils.trim(Build.DEVICE).replace(" ", "_");
    }

    public static String getHardware() {
        return StringUtils.trim(Build.HARDWARE).replace(" ", "_");
    }

    public static String printDeviceInfo(boolean isPrint) {
        String temp = "Build.CPU_ABI = "
                + Build.CPU_ABI
                + "\nBuild.CPU_ABI2 = "
                + Build.CPU_ABI2
                + "\nBuild.BRAND = "
                + Build.BRAND
                + "\nBuild.DEVICE = "
                + Build.DEVICE
                + "\nBuild.DISPLAY = "
                + Build.DISPLAY
                + "\nBuild.HARDWARE = "
                + Build.HARDWARE
                + "\nBuild.PRODUCT = "
                + Build.PRODUCT
                + "\nBuild.MANUFACTURER = "
                + Build.MANUFACTURER
                + "\nBuild.MODEL = "
                + Build.MODEL;
        if (isPrint) {
            android.util.Log.e("miaopai", String.format(temp, ""));
        }
        return String.format(temp, "");
    }

    /***
     * 检测是LG-F160手机 分辨率：480*800
     */
    public static boolean isLGF160() {
        //型号:LG-F160
        final String model = getDeviceModel().toLowerCase();
        return model.startsWith("lg-f160");
    }

    /**
     * 检测是否魅族手机
     */
    public static boolean isMeizu() {
        //M040
        final String model = getDeviceModel().toLowerCase();
        return model.startsWith("m040");
    }

    public static boolean isXiaomi() {
        return StringUtils.equalsIgnoreCase("xiaomi", getManufacturer());
    }

    /**
     * 检测是否是红米
     */
    public static boolean isHongmi() {
        //BRAND:Xiaomi DEVICE:HM2013022
        return isXiaomi() && getDivice().toLowerCase().indexOf("hm") != -1;
    }

    /**
     * @return 480_800
     */
    @SuppressWarnings("deprecation")
    public static String getScreenResolution(Context context) {
        try {
            Display display =
                    ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            return "" + display.getWidth() + "*" + display.getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获得设备屏幕密度
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics metrics = context.getApplicationContext().getResources().getDisplayMetrics();
        return metrics.density;
    }

    public static int[] getScreenSize(int w, int h, Context context) {
        int phoneW = getScreenWidth(context);
        int phoneH = getScreenHeight(context);

        if (w * phoneH > phoneW * h) {
            phoneH = phoneW * h / w;
        } else if (w * phoneH < phoneW * h) {
            phoneW = phoneH * w / h;
        }

        return new int[]{phoneW, phoneH};
    }

    public static int[] getScreenSize(int w, int h, int phoneW, int phoneH) {
        if (w * phoneH > phoneW * h) {
            phoneH = phoneW * h / w;
        } else if (w * phoneH < phoneW * h) {
            phoneW = phoneH * w / h;
        }
        return new int[]{phoneW, phoneH};
    }

    /**
     * 设置屏幕亮度
     */
    public static void setBrightness(final Activity context, float f) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.screenBrightness = f;
        if (lp.screenBrightness > 1.0f) {
            lp.screenBrightness = 1.0f;
        } else if (lp.screenBrightness < 0.01f) lp.screenBrightness = 0.01f;
        context.getWindow().setAttributes(lp);
    }

    // private static final long NO_STORAGE_ERROR = -1L;
    private static final long CANNOT_STAT_ERROR = -2L;

    /**
     * 检测磁盘状态
     */
    // public static int getStorageStatus(boolean mayHaveSd) {
    // long remaining = mayHaveSd ? getAvailableStorage() : NO_STORAGE_ERROR;
    // if (remaining == NO_STORAGE_ERROR) {
    // return CommonStatus.STORAGE_STATUS_NONE;
    // }
    // return remaining < CommonConstants.LOW_STORAGE_THRESHOLD ?
    // CommonStatus.STORAGE_STATUS_LOW : CommonStatus.STORAGE_STATUS_OK;
    // }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static long getSDAllStorage() {
        try {
            String storageDirectory = Environment.getExternalStorageDirectory().toString();
            StatFs stat = new StatFs(storageDirectory);
            return stat.getBlockSizeLong() * stat.getBlockCountLong();
        } catch (RuntimeException ex) {
            // if we can't stat the filesystem then we don't know how many
            // free bytes exist. It might be zero but just leave it
            // blank since we really don't know.
            return CANNOT_STAT_ERROR;
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(Context ctx, View v) {
        if (v != null && ctx != null) {
            InputMethodManager m =
                    (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            m.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void hideSoftInput(Activity ctx) {
        if (ctx == null) {
            return;
        }
        if (ctx.isFinishing()) {
            return;
        }
        View view = ctx.getWindow().peekDecorView();
        if (view != null && view.getWindowToken() != null) {
            InputMethodManager imm =
                    (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘 已经显示则隐藏
     */
    public static void showSoftInputOrHide(Context ctx) {
        InputMethodManager imm =
                (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);// (v,
    }

    /**
     * 显示输入法
     */
    public static void showSoftInput(Context ctx, View v) {
        v.requestFocus();
        InputMethodManager m =
                (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        m.showSoftInput(v, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 软键盘是否已经打开
     */
    protected boolean isHardKeyboardOpen(Context ctx) {
        return ctx.getResources().getConfiguration().hardKeyboardHidden
                == Configuration.HARDKEYBOARDHIDDEN_NO;
    }

    /**
     * 获取内存信息，单位Kb
     */
    public static int getTotalMemory() {
        int total = 0;
        try {
            if (new File("/proc/meminfo").exists()) {
                FileReader fr = new FileReader("/proc/meminfo");
                BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
                String meminfo = localBufferedReader.readLine();
                localBufferedReader.close();

                if (meminfo != null) {
                    meminfo = meminfo.split(":")[1].trim().split(" ")[0];
                    total = ConvertToUtils.toInt(StringUtils.trim(meminfo));
                }
            }
        } catch (IOException e) {
        } catch (Exception e) {
        }
        return total;
    }

    /**
     * 启动指定包名的程序
     */
    public static void startApkActivity(final Context ctx, String packageName) {
        PackageManager pm = ctx.getPackageManager();
        PackageInfo pi;
        try {
            pi = pm.getPackageInfo(packageName, 0);
            Intent intent = new Intent(Intent.ACTION_MAIN, null);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setPackage(pi.packageName);

            List<ResolveInfo> apps = pm.queryIntentActivities(intent, 0);

            ResolveInfo ri = apps.iterator().next();
            if (ri != null) {
                String className = ri.activityInfo.name;
                intent.setComponent(new ComponentName(packageName, className));
                ctx.startActivity(intent);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得android_id
     */
    public static String getAndroidId(Context context) {
        return Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
    }

    /**
     * 判断是否支持闪光灯
     */
    public static boolean isSupportCameraLedFlash(PackageManager pm) {
        if (pm != null) {
            FeatureInfo[] features = pm.getSystemAvailableFeatures();
            if (features != null) {
                for (FeatureInfo f : features) {
                    if (f != null && PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) //判断设备是否支持闪光灯
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检测设备是否支持相机
     */
    public static boolean isSupportCameraHardware(Context context) {
        if (context != null && context.getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * 显示或者隐藏状态栏
     */
    @TargetApi(11)
    public static void showSystemUi(View mViewRoot, boolean visible) {
        if (DeviceUtils.hasHoneycomb() && mViewRoot != null) {
            int flag =
                    visible ? 0 : View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LOW_PROFILE;
            mViewRoot.setSystemUiVisibility(flag);
        }
    }

    /**
     * 获取外置的sdcard列表
     * 参考：http://blog.csdn.net/boystarzq09/article/details/9837535
     */
    public static ArrayList<String> getExtExternalStorage() {
        ArrayList<String> result = new ArrayList<String>();
        Process proc = null;
        InputStream is = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            proc = runtime.exec("mount");//检测系统挂载
            is = proc.getInputStream();
            isr = new InputStreamReader(is);
            String line;

            final String sdcard = Environment.getExternalStorageDirectory().getAbsolutePath();
            br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                if (line.contains("secure") || line.contains("asec"))//	/mnt/secure/asec
                {
                    continue;
                }

                if (line.contains("fat")) {//line.contains("fuse")
                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        //ZET[/mnt/sdcard-ext, /mnt/sdcard]
                        //SAMSUNG
                        final String path = columns[1];
                        File f = new File(path);
                        if (f != null && f.canRead() && f.canWrite() && f.exists() && !sdcard.equals(path)) {
                            result.add(path);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
                br = null;
            }

            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                }
                isr = null;
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
                is = null;
            }

            if (proc != null) {
                try {
                    proc.destroy();
                } catch (Exception e) {

                }
                proc = null;
            }
        }
        return result;
    }

    /**
     * 判断rom是什么系统
     * <p>
     * getSystemProperty("ro.miui.ui.version.name")  是miui系統  返回空则不是
     */
    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }
        return line;
    }


    public static boolean isFlyme() {
        try {
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }
    }


    /**
     * 获取屏幕状态栏高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {

        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, sbar = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return sbar;
    }

    /**
     * 手机内存是否可用
     */
    public static boolean externalMemoryAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取手机可用的内存
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }

    /**
     * 获取手机全部的内存
     */
    public static long getTotalExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return -1;
        }
    }

    /**
     * 获取屏幕的信息
     * <p/>
     * DisplayMetrics.widthPixels   宽
     * DisplayMetrics.heightPixels  高
     *
     * @param activity Activity
     * @return DisplayMetrics
     */
    public static DisplayMetrics getScreenSize(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 获取屏幕的信息
     * <p/>
     * DisplayMetrics.widthPixels   宽
     * DisplayMetrics.heightPixels  高
     *
     * @param context Context
     * @return DisplayMetrics
     */
    public static DisplayMetrics getScreenSize(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    public static String getDeviceId(Context context) {
        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            return manager.getDeviceId();
        } catch (Exception e) {
            return e.toString();
        }
    }


    public static String getDeviceMacAddress(Application application) {
        if (application != null) {
            try {
                WifiManager wifi = (WifiManager) application.getApplicationContext()
                        .getSystemService(Context.WIFI_SERVICE);
                return wifi.getConnectionInfo().getMacAddress();
            } catch (Exception ignored) {
            }
        }
        return "";
    }

    public static String getDeviceNumber(Context context) {
        try {
            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return StringUtils.ifNullReturnEmpty(manager.getLine1Number());
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * 判断设备是否有前置或者后置摄像头
     *
     * @param facing CameraInfo.CAMERA_FACING_BACK 或 CameraInfo.CAMERA_FACING_FRONT
     * @return 是否存在
     */
    @SuppressWarnings("deprecation")
    public static boolean checkCameraFacing(final int facing) {
        final int cameraCount = Camera.getNumberOfCameras();
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < cameraCount; i++) {
            Camera.getCameraInfo(i, info);
            if (facing == info.facing) {
                return true;
            }
        }
        return false;
    }

    /**
     * >=2.2
     */
    public static boolean hasFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    /**
     * >=2.3
     */
    public static boolean hasGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    /**
     * >=3.0 LEVEL:11
     */
    public static boolean hasHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    /**
     * >=3.1
     */
    public static boolean hasHoneycombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    /**
     * >=4.0 14
     */
    public static boolean hasICS() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    /**
     * >= 4.1 16
     *
     * @return
     */
    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * >= 4.2 17
     */
    public static boolean hasJellyBeanMr1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    /**
     * >= 4.3 18
     */
    public static boolean hasJellyBeanMr2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    /**
     * >=4.4 19
     */
    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static int getSDKVersionInt() {
        return Build.VERSION.SDK_INT;
    }

    @SuppressWarnings("deprecation")
    public static String getSDKVersion() {
        return Build.VERSION.SDK;
    }

    /**
     * 获得设备的固件版本号
     */
    public static String getReleaseVersion() {
        String rel = Build.VERSION.RELEASE;
        if (rel == null) rel = "";
        return rel;
    }

    /**
     * 检测是否是中兴机器
     */
    public static boolean isZte() {
        return getDeviceModel().toLowerCase().indexOf("zte") != -1;
    }

    /**
     * 检测是否是中兴机器
     */
    public static boolean isZteU9180() {
        return getDeviceModel().toLowerCase().indexOf("U9180") != -1;
    }

    /**
     * s820e 需要默认分辨率
     *
     * @return
     */
    public static boolean isLenovoS820e() {
        return getDeviceModel().contains("S820e");
    }

    /**
     * 判断是否是三星的手机
     */
    public static boolean isSamsung() {
        return getManufacturer().toLowerCase().indexOf("samsung") != -1;
    }

    /**
     * 检测是否HTC手机
     */
    public static boolean isHTC() {
        return getManufacturer().toLowerCase().indexOf("htc") != -1;
    }

    /**
     * 检测当前设备是否是特定的设备
     *
     * @param devices
     * @return
     */
    public static boolean isDevice(String... devices) {
        String model = DeviceUtils.getDeviceModel();
        if (devices != null && model != null) {
            for (String device : devices) {
                if (model.indexOf(device) != -1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获得设备型号
     *
     * @return
     */
    public static String getDeviceModel() {
        String model = Build.MODEL;
        if (model == null) model = "";
        return model.trim();
    }

    /**
     * 获取厂商信息
     */
    public static String getManufacturer() {
        String man = Build.MANUFACTURER;
        if (man == null) man = "";
        return man.trim();
    }

    /**
     * 判断是否是平板电脑
     *
     * @param context
     * @return
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    /**
     * 检测是否是平板电脑
     *
     * @param context
     * @return
     */
    public static boolean isHoneycombTablet(Context context) {
        return hasHoneycomb() && isTablet(context);
    }

    public static int dipToPX(final Context ctx, float dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, ctx.getResources().getDisplayMetrics());
    }

    /**
     * 获取CPU的信息
     *
     * @return
     */
    public static String getCpuInfo() {
        String cpuInfo = "";
        try {
            if (new File("/proc/cpuinfo").exists()) {
                FileReader fr = new FileReader("/proc/cpuinfo");
                BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
                cpuInfo = localBufferedReader.readLine();
                localBufferedReader.close();

                if (cpuInfo != null) {
                    cpuInfo = cpuInfo.split(":")[1].trim().split(" ")[0];
                }
            }
        } catch (IOException e) {
        } catch (Exception e) {
        }
        return cpuInfo;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * @return
     */
    public static boolean checkIsHasSdcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static int freeSpaceOnSd() {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double sdFreeMB = ((double) stat.getAvailableBlocks() * (double) stat.getBlockSize()) / 1024 * 1024;// MB

        return (int) sdFreeMB;
    }


    private static int mScreenWidth;
    private static int mScreenHeight;
    private static int mRealScreenHeight;


    public static int getScreenWidth(Context context) {
        if (mScreenWidth == 0) {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            mScreenWidth = outMetrics.widthPixels;
        }
        return mScreenWidth;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        if (mScreenHeight == 0) {
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics outMetrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(outMetrics);
            mScreenHeight = outMetrics.heightPixels;
        }
        return mScreenHeight;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static int getRealScreenHeight(Context context) {

        if (mRealScreenHeight == 0) {
            int ver = Build.VERSION.SDK_INT;
            DisplayMetrics dm = new DisplayMetrics();
            android.view.Display display =
                    ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            display.getMetrics(dm);
            mRealScreenHeight = 0;

            if (ver >= 17) {
                final Point size = new Point();
                display.getRealSize(size);
                mRealScreenHeight = size.y;
            } else if (ver > 13 && ver < 17) {
                try {
                    Method mt = display.getClass().getMethod("getRawHeight");
                    mRealScreenHeight = (Integer) mt.invoke(display);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ver == 13) {
                try {
                    Method mt = display.getClass().getMethod("getRealHeight");
                    mRealScreenHeight = (Integer) mt.invoke(display);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (ver < 13) {
                mRealScreenHeight = dm.heightPixels;
            }
        }

        return mRealScreenHeight;
    }


}

