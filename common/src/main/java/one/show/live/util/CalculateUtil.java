package one.show.live.util;

import java.text.DecimalFormat;

public class CalculateUtil {

    public static String formatDataSize(long size) {
        if (size == 0) {
            return "0B";
        }
        DecimalFormat format = new DecimalFormat(".00");
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return format.format((double) size / 1024) + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            return format.format((double) size / (1024 * 1024)) + "MB";
        } else {
            return format.format((double) size / (1024 * 1024 * 1024)) + "GB";
        }
    }

    public native static float getPointF(float time, float start, float point1, float point2, float end);
}
