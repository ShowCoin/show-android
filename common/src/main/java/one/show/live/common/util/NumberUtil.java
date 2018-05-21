package one.show.live.common.util;

import java.text.DecimalFormat;

/**
 * 数字处理工具类
 */
public class NumberUtil {

    /**
     * 格式数据，如果大约1万的话，显示 x.x万
     *
     * @param num 多少数
     */
    public static String formatLikeNumK(int num) {
        if (num > 9999) {
            DecimalFormat df = new DecimalFormat("#.0");
            return df.format(num / 10000d) + "w";
        } else {
            return String.valueOf(num);
        }
    }

    /**
     * 格式数据，如果大约1万的话，显示 x.xx万
     *
     * @param num 多少数
     */
    public static String formatLikeNum(double num) {
        return (num < 10000) ? String.format("%.0f", num) : String.format("%.1f万", num / 10000);
    }

    /**
     * 格式数据，如果大约10万的话，显示 x.xx万
     *
     * @param num 多少数
     */
    public static String formatLikeNumFor10W(double num) {
        return (num < 100000) ? String.format("%.0f", num) : String.format("%.1f万", num / 10000);
    }

    /**
     * 格式数据，如果大约100万的话，显示 x.xx万
     *
     * @param num 多少数
     */
    public static String formatLikeNumFor100W(double num) {
        return (num < 1000000) ? String.format("%.0f", num) : String.format("%.1f万", num / 10000);
    }
}
