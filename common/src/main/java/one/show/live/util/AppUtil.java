package one.show.live.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

/**
 * 获取App信息的工具
 */
public class AppUtil {
    private Context context;

    public AppUtil(Context context) {
        this.context = context;
    }

    /**
     * 获取 AndroidManifest的值
     *
     * @param name meta-data:name
     * @return meta-data:value
     */
    public String getMetaData(String name) {
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return info.metaData.getString(name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getVersionName() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取当前Apk版本号 android:versionCode
     *
     * @return
     */
    public int getVersionCode() {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
        }
        return verCode;
    }



    public static boolean isMainProcess(Context context){
        return "one.show.live".equals(getCurProcessName(context));
    }



    /* 一个获得当前进程的名字的方法      IM判读 框架是否初始化*/
    public static String getCurProcessName(Context context) {
        if (context == null) return null;
        int pid = android.os.Process.myPid();
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
//                Log.d("package",appProcess.processName);
                return appProcess.processName;
            }
        }
        return null;
    }

}
