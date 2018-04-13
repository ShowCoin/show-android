package one.show.live.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.List;

/**
 * Created by apple on 16/6/30.
 */
public class WifiMacAddress {
    //获取wifi状态下的mac地址
    public static String getWifiMac(Application app) {
        try {
            if (SXUtil.getNetworkType(app) == SXUtil.SXDEVICE_NETWROK_TYPE_WIFI) {
                WifiManager wifi = (WifiManager) app.getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = wifi.getConnectionInfo();
                return info.getMacAddress();
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";

    }

    /**
     * 获取imei
     *
     * @param app
     * @return
     */
    public static String getIMEI(Application app) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) app.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取SIM卡运营商
     *
     * @param app
     * @return
     */
    public static String getOperators(Application app) {
        try {
            TelephonyManager tm = (TelephonyManager) app
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String operator = null;
            String IMSI = tm.getSubscriberId();
            if (IMSI == null || IMSI.equals("")) {
                return operator;
            }
            if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
                operator = "1";//中国移动
            } else if (IMSI.startsWith("46001")) {
                operator = "2";//中国联通
            } else if (IMSI.startsWith("46003")) {
                operator = "3";//中国电信
            }
            return operator;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取分辨率
     *
     * @param app
     * @return
     */
    public static String getresolution(Context app) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) app).getWindowManager().getDefaultDisplay().getMetrics(metric);


        int width = metric.widthPixels;  // 宽度（PX）
        int height = metric.heightPixels;  // 高度（PX）
        return width + "*" + height;
    }


    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                    /*
                    BACKGROUND=400 EMPTY=500 FOREGROUND=100
                    GONE=1000 PERCEPTIBLE=130 SERVICE=300 ISIBLE=200
                     */
//                Log.i(context.getPackageName(), "此appimportace ="
//                        + appProcess.importance
//                        + ",context.getClass().getName()="
//                        + context.getClass().getName());
                if (appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
//                    Log.i(context.getPackageName(), "处于后台"
//                            + appProcess.processName);
                    return true;
                } else {
//                    Log.i(context.getPackageName(), "处于前台"
//                            + appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * 获取顶部activity的名称
     *
     * @param context
     * @return
     */
    public static String getTopActivity(Context context) {

        ActivityManager manager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

        if (runningTaskInfos != null)

            return (runningTaskInfos.get(0).topActivity).toString();
        else
            return null;

    }
}
