package one.show.live.common.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by ydeng on 2017/12/12.
 */

public class StatusBarUtil {


    public static final int DEFAULT_STATUS_BAR_ALPHA = 112;
    private static final String TAG = "StatusBarUtil";


    public static void showStatusBar(Activity activity, int statusBarAlpha) {
        setTransparentForWindow(activity);
        showStatusBarView(activity, statusBarAlpha);
    }

    public static void showStatusBarNoPadding(Activity activity, int statusBarAlpha) {
        setTransparentForWindow(activity);
        showStatusBarViewNoPaaaing(activity, statusBarAlpha);
    }

    public static void hideStatusBar(Activity activity, int statusBarAlpha) {
        setTransparentForWindow(activity);
        hideStatusBarViewWithClose(activity);
    }

    public static void hideStatusBarSwitchFullScreen(Activity activity) {
        setImmersiveMode(activity);
        hideStatusBarView(activity);
    }


    /**
     * 显示自定义的StatusBar
     *
     * @param activity       需要设置的 activity
     * @param statusBarAlpha 透明值
     */
    private static void showStatusBarView(Activity activity, int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        if (contentView.getChildCount() == 0) {
            throw new IllegalArgumentException("contentView children should not null!!!");
        }
        boolean isAlreadyAdd = false;
        int count = contentView.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = contentView.getChildAt(i);
            if (view instanceof ViewGroup) {
                //set setPadding
                ((ViewGroup) view).setPadding(0, getStatusBarHeight(activity), 0, 0);
            } else {
                Log.e(TAG, "contentView should not contain other views !!!");
            }
        }
        if (!isAlreadyAdd) {
            //add statusBar
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
        }

    }

    /**
     * 显示自定义的StatusBar
     *
     * @param activity       需要设置的 activity
     * @param statusBarAlpha 透明值
     */
    private static void showStatusBarViewNoPaaaing(Activity activity, int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        if (contentView.getChildCount() == 0) {
            throw new IllegalArgumentException("contentView children should not null!!!");
        }
        boolean isAlreadyAdd = false;
        int count = contentView.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = contentView.getChildAt(i);
            if (view instanceof StatusBarView) {
                isAlreadyAdd = true;
                view.setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
                if (view.getVisibility() != View.VISIBLE) {
                    view.setVisibility(View.VISIBLE);
                }
            }
        }
        if (!isAlreadyAdd) {
            //add statusBar
            contentView.addView(createTranslucentStatusBarView(activity, statusBarAlpha));
        }

    }

    private static void hideStatusBarView(Activity activity) {
        Log.e(TAG, "hideStatusBarView");
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        int count = contentView.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = contentView.getChildAt(i);
            if (view instanceof StatusBarView) {
                view.setVisibility(View.INVISIBLE);
            }

        }
    }

    private static void hideStatusBarViewWithClose(Activity activity) {
        Log.e(TAG, "hideStatusBarViewWithClose");
        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
        int count = contentView.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = contentView.getChildAt(i);
            if (view instanceof StatusBarView) {
                view.setVisibility(View.INVISIBLE);
            } else if (view instanceof ViewGroup) {
                ((ViewGroup) view).setPadding(0, 0, 0, 0);
            }
        }
    }


    /**
     * 设置透明
     */
    public static void setTransparentForWindow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            activity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置全屏沉浸式模式
     */
    public static void setImmersiveMode(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        decorView.setFitsSystemWindows(false);

    }

    /**
     * 创建半透明矩形 View
     *
     * @param alpha 透明值
     * @return 半透明 View
     */
    private static StatusBarView createTranslucentStatusBarView(Activity activity, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        StatusBarView statusBarView = new StatusBarView(activity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(activity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        return statusBarView;
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取NavigationBarHeight
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        // 获得状态栏高度
        int result = 0;
        int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 如果navigation 显示获取高度
     *
     * @param activity
     * @return
     */
    public static int hasNavigationBarHeight(Activity activity) {
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        if (decorView != null && decorView.getChildCount() > 2) {
            return getNavigationBarHeight(activity);
        } else {
            return 0;
        }
    }


    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }



    public static void setNavigationBarColor(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Android 5.0 以上 全透明
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // 状态栏（以上几行代码必须，参考setStatusBarColor|setNavigationBarColor方法源码）
            window.setStatusBarColor(Color.TRANSPARENT);
            // 虚拟导航键
            window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // Android 4.4 以上 半透明
            Window window = activity.getWindow();
            // 状态栏
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 虚拟导航键
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public static void matchActionBar(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

    }

}
