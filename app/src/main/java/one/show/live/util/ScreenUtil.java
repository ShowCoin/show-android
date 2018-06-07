package one.show.live.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import one.show.live.MeetApplication;

public class ScreenUtil {

    public static int mNotificationBarHeight;
    public static int mWidth;
    public static int mHeight;
    public static float densityDpi;
    public static float density;

    public static void initialize() {
        Context context = MeetApplication.getAppContext();
        if (mWidth == 0 || mHeight == 0 || density == 0) {
            Resources res = context.getResources();
            DisplayMetrics metrics = res.getDisplayMetrics();
            // TODO // - 50
            density = metrics.density;
            densityDpi = metrics.densityDpi;
            mNotificationBarHeight = (int) (35 * density);
            mWidth = metrics.widthPixels;// - (int)(50 * density)
            mHeight = metrics.heightPixels/* - mNotificationBarHeight */;// -
        }
    }
}