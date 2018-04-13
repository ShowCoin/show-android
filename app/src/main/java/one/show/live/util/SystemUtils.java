package one.show.live.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import one.show.live.log.Logger;
import one.show.live.po.POMember;
import one.show.live.util.AppUtil;
import one.show.live.util.ConvertToUtils;
import one.show.live.util.DeviceUtils;
import one.show.live.util.StringUtils;

public class SystemUtils {

    /**
     * 获取设备号
     *
     * @return
     */
    public static String getDeviceId(final Context ctx) {
        return StringUtils.trim(((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId());
    }

    /**
     * 获取系统IMSI(国际移动用户识别码 International Mobile Subscriber Identity )
     *
     * @return
     */
    public static String getIMSI(final Context ctx) {
        return StringUtils.trim(((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId());
    }

    public static String getSim(final Context ctx) {
        return StringUtils.trim(((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getSimSerialNumber());
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

    public static boolean isDestroyed(Activity context) {
        if (context == null)
            return true;
        if (DeviceUtils.hasJellyBeanMr1())
            return context.isDestroyed();
        else
            return context.isFinishing();
    }

    public static boolean isWifi(Context mContext) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo != null
                    && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }


    private static int statusbarHeight;

    /**
     * 获取状态栏高度
     * @param context
     * @return
     */

    public static int getStatusBarHeight(Context context) {
        if (statusbarHeight == 0) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                statusbarHeight = res.getDimensionPixelSize(resourceId);
            } else {
                statusbarHeight = ConvertToUtils.dipToPX(context, 25);
            }
        }
        return statusbarHeight;
    }


    private static int navigationBarHeight = -1;


    public static int getNavigationBarHeight(Context context) {
        if (navigationBarHeight == -1) {
            if (hasNavBar(context)) {
                Resources res = context.getResources();
                int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
                if (resourceId > 0) {
                    navigationBarHeight = res.getDimensionPixelSize(resourceId);
                }
            }
        }
        return navigationBarHeight;
    }

    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    private static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }


    public static boolean isNavigationBarShow(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {

            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);

            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * 判断底部navigator是否已经显示,主要是华为这款反人类手机
     *
     * @param windowManager
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean hasSoftKeys(WindowManager windowManager) {
        Display d = windowManager.getDefaultDisplay();


        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);


        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;


        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);


        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;


        return (realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0;
    }


    public static void clearWebViewCache(Activity context) {
// 清除cookie即可彻底清除缓存
        CookieSyncManager.createInstance(context);
        CookieManager.getInstance().removeAllCookie();
    }

    /**
     * 设置激光别名 和标签
     * @param context
     */
    public static  void  setJpush(Activity context){
        //设置极光的别名
        JPushInterface.setAlias(context, POMember.getInstance().getUid(), new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Logger.d("极光别名", i + "》》》》"+ s);
            }
        });
        //设置标签
        Set<String> set = new HashSet<>();
        if(StringUtils.isNotEmpty(POMember.getInstance().getCity())){
            set.add(POMember.getInstance().getCity());//添加城市标签
        }

        AppUtil appUtil = new AppUtil(context);
        set.add(appUtil.getVersionName());//添加版本号标签
        JPushInterface.setTags(context, set, new TagAliasCallback() {
            @Override
            public void gotResult(int i, String s, Set<String> set) {
                Logger.d("极光标签", i + "》》》》"+ s);
            }
        });
    }


    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(final EditText editText, final int num) {

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {


                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？] ";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());

                String ss = source.toString();

                while (matcher.find()) {
                    ss = ss.replace(matcher.group(), "");
                }


                if (num != 0) {

                    if (editText.getText().toString().length() >= num) {
                        return "";
                    }

                    if (editText.getText().toString().length() + ss.length() > num) {
                        ss = ss.substring(0, num - editText.getText().toString().length());
                        return ss;
                    }

                }

                return ss;

            }
        };
        editText.setFilters(new InputFilter[]{filter});
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return true;
            }
        });

    }

    /**
     * 检测touch事件是否在view区域
     * @param view
     * @param ev
     * @param outRect
     * @param location
     * @return
     */
    public static boolean isInViewBounds(View view, MotionEvent ev, Rect outRect, int[] location) {
        if (view == null) {
            return false;
        }
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains((int) ev.getRawX(), (int) ev.getRawY());
    }
}
