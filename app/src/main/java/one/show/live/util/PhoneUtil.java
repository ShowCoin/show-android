package com.dreamer.tv.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.dreamer.tv.DreamingApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
public class PhoneUtil {

    private static PhoneUtil instance;
    private TelephonyManager tm;

    private PhoneUtil() {
        tm = (TelephonyManager) DreamingApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static PhoneUtil getInstance() {
        if (instance == null) {
            synchronized (PhoneUtil.class) {
                if (instance == null) {
                    instance = new PhoneUtil();
                }
            }
        }
        return instance;
    }


    /**
     * 获取手机号码
     */
    public String getPhoneNumber() {
        return tm == null ? null : tm.getLine1Number();
    }

    /**
     * 获取网络类型（暂时用不到）
     */
    public int getNetWorkType() {
        return tm == null ? 0 : tm.getNetworkType();
    }

    /**
     * 获取手机sim卡的序列号（IMSI）
     */
    public String getIMSI() {
        return tm == null ? null : tm.getSubscriberId();
    }

    /**
     * 获取手机IMEI
     */
    public String getIMEI() {
        return tm == null ? null : tm.getDeviceId();
    }

    /**
     * 获取手机型号
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机品牌
     */
    public static String getBrand() {
        return android.os.Build.BRAND;
    }

    /**
     * 获取手机系统版本
     */
    public static String getVersion() {
        return android.os.Build.VERSION.RELEASE;
    }


    /**
     * 获取手机屏幕宽
     */
    public int getScreenWidth(Activity act) {
        return act.getWindowManager().getDefaultDisplay().getWidth();
    }


    /**
     * 获取手机CPU信息 //1-cpu型号  //2-cpu频率
     */
    public String[] getCpuInfo() {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        String[] cpuInfo = {"", ""};  //1-cpu型号  //2-cpu频率  
        String[] arrayOfString;
        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr, 8192);
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            for (int i = 2; i < arrayOfString.length; i++) {
                cpuInfo[0] = cpuInfo[0] + arrayOfString[i] + " ";
            }
            str2 = localBufferedReader.readLine();
            arrayOfString = str2.split("\\s+");
            cpuInfo[1] += arrayOfString[2];
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return cpuInfo;
    }



    //获取当前版本号
    public String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static boolean isBackground(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
//                    Log.i("后台", appProcess.processName);
                    return true;
                } else {
//                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }


    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean checkApkExist(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        final PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals(packageName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    static public Locale getSystemLocaleLegacy(Configuration config) {
        return config.locale;
    }

    @TargetApi(Build.VERSION_CODES.N)
    static public Locale getSystemLocale(Configuration config) {
        return config.getLocales().get(0);
    }


    public static String getSystemLanguage(Context context) {
        if (context == null)
            return "";

        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = getSystemLocale(context.getResources().getConfiguration());
        } else {
            locale = getSystemLocaleLegacy(context.getResources().getConfiguration());
        }

        return locale.getLanguage();
    }

//    public static int getAppSomeWebUrlIndex(Context context) {
//        String ss = PreferenceUtils.getString("language", "xt");
//        if (context == null) {
//            return 0;
//        }
//        if (ss.equals("xt")) {
//            String lan = PhoneUtil.getSystemLanguage(context);
//            if (lan.equalsIgnoreCase("en")) {
//                return 0;
//            } else if (lan.equalsIgnoreCase("zh")) {
//                return 1;
//            } else if (lan.equalsIgnoreCase("ft")) {
//                return 2;
//            } else {
//                return 1; // default zh
//            }
//        } else if (ss.equals("en")) {
//            return 0;
//        } else if (ss.equals("zh")) {
//            return 1;
//        } else {
//            return 2;
//        }
//    }


    /**
     * 获取本地应用语言
     *
     * @return
     */
    public static String getLocalLanguage(Context context) {
        String language = "cn";
//                cn简体中文，en英文，tw中文繁体
//        switch (PhoneUtil.getAppSomeWebUrlIndex(context)) {
//            case 0:
//                language = "en";
//                break;
//            case 1:
//                language = "cn";
//                break;
//            case 2:
//                language = "tw";
//                break;
//        }
        return language;
    }

    //弹出软键盘

    public void phoneEditListener(final EditText editText) {
        //延迟0.5秒弹出输入框
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager) editText.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }

        }, 500);
    }

}
