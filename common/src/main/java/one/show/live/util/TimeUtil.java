package one.show.live.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间处理工具
 */
public class TimeUtil {

    /**
     * 时间格式化
     *
     * @param timestamp 时间戳
     * @return 格式化的时间
     */
    public static String getHowAgo(long timestamp) {
        long second = System.currentTimeMillis() / 1000 - timestamp;
        if (second < 0) {
            return "在线";
        } else if (second < 180) {
            return "在线";
        } else if (second < 3600) {
            //1小时内 显示X分钟
            return second / 60 + "分钟前";
        } else if (second < 86400) {
            //24小时内 显示x小时
            return second / 3600 + "小时前";
        } else if (second < 259200) {
            return second / 86400 + "天前";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
            return format.format(new Date(timestamp));
        }
    }

    /**
     * 时间格式化
     *
     * @param timestamp 时间戳
     * @return 格式化的时间
     */
    public static String getMsgListDuration(long timestamp) {
        long second = System.currentTimeMillis() / 1000 - timestamp / 1000;
        if (second < 0) {
            return "在线";
        } else if (second < 180) {
            return "在线";
        } else if (second < 3600) {
            //1小时内 显示X分钟
            return second / 60 + "分钟前";
        } else if (second < 86400) {
            //24小时内 显示x小时
            return second / 3600 + "小时前";
        } else if (second < 259200) {
            return second / 86400 + "天前";
        } else {
            SimpleDateFormat format = new SimpleDateFormat("MM-dd", Locale.getDefault());
            return format.format(new Date(timestamp));
        }
    }

    /**
     * 消息详情页显示的时间
     *
     * @param time
     * @return
     */
    public static String formatMsgDuration(long time) {
//        Date date = new Date(time);
//        Calendar current = Calendar.getInstance();
//
//        Calendar today = Calendar.getInstance();    //今天
//
//        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
//        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
//        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
//        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
//        today.set(Calendar.HOUR_OF_DAY, 0);
//        today.set(Calendar.MINUTE, 0);
//        today.set(Calendar.SECOND, 0);
//
//        Calendar yesterday = Calendar.getInstance();    //昨天
//
//        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
//        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
//        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
//        yesterday.set(Calendar.HOUR_OF_DAY, 0);
//        yesterday.set(Calendar.MINUTE, 0);
//        yesterday.set(Calendar.SECOND, 0);
//
//        current.setTime(date);
//
//        if (current.after(today)) {//今天
//            return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
//        } else if (current.before(today) && current.after(yesterday)) {//昨天
//            return "昨天 " + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
//        } else {
//            return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date);
//        }
        long data = System.currentTimeMillis() - time;
        if (time < 0) {
            return "刚刚";
        }
        int second;
        int minute;
        int hour;
        int day;
        int week;
        int month;
        int year;
        second = (int) (data / 1000);
        minute = second / 60;
        if (minute <= 3) {
            return "刚刚";
        }
        hour = minute / 60;
        if (hour < 1) {
            return minute + "分钟前";
        }
        day = hour / 24;
        if (day < 1) {
            return hour + "小时前";
        }

        week = day / 7;
        if (week < 1) {
            return day + "天前";
        }
        month = day / 30;
        if (month < 1) {
            return week + "周前";
        }
        year = day / 365;
        if (year < 1) {
            return month + "月前";
        }
        return year + "年前";
    }

    /**
     * 消息详情页显示的时间
     *
     * @param time
     * @return
     */
    public static String formatOnLineDuration(long time) {
//        Date date = new Date(time);
//        Calendar current = Calendar.getInstance();
//
//        Calendar today = Calendar.getInstance();    //今天
//
//        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
//        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
//        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
//        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
//        today.set(Calendar.HOUR_OF_DAY, 0);
//        today.set(Calendar.MINUTE, 0);
//        today.set(Calendar.SECOND, 0);
//
//        Calendar yesterday = Calendar.getInstance();    //昨天
//
//        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
//        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
//        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
//        yesterday.set(Calendar.HOUR_OF_DAY, 0);
//        yesterday.set(Calendar.MINUTE, 0);
//        yesterday.set(Calendar.SECOND, 0);
//
//        current.setTime(date);
//
//        if (current.after(today)) {//今天
//            return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
//        } else if (current.before(today) && current.after(yesterday)) {//昨天
//            return "昨天 " + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(date);
//        } else {
//            return new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(date);
//        }
        long data = System.currentTimeMillis() - time;
        if (time < 0) {
            return "在线";
        }
        int second;
        int minute;
        int hour;
        int day;
        int week;
        int month;
        int year;
        second = (int) (data / 1000);
        minute = second / 60;
        if (minute <= 3) {
            return "在线";
        }
        hour = minute / 60;
        if (hour < 1) {
            return minute + "分钟前";
        }
        day = hour / 24;
        if (day < 1) {
            return hour + "小时前";
        }

        week = day / 7;
        if (week < 1) {
            return day + "天前";
        }
        month = day / 30;
        if (month < 1) {
            return week + "周前";
        }
        year = day / 365;
        if (year < 1) {
            return month + "月前";
        }
        return year + "年前";
    }

    /**
     * 判断是否是今天
     *
     * @param time
     * @return
     */
    public static boolean isToday(long time) {
        Date date = new Date(time);
        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        current.setTime(date);
        return current.after(today);
    }


    public static String formatDuration(long startTime) {
        if (startTime == 0) {
            return "--:--";
        }
        long duration = System.currentTimeMillis() / 1000 - startTime;
        long hours = duration / 3600;
        long minutes = (duration % 3600) / 60;
        long seconds = duration % 60;
        if (duration < 0) {
            return "--:--";
        }
        if (hours >= 100) {
            return String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds);
        } else if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds);
        }
    }


    /**
     * 视频播放进度格式化
     *
     * @param position 以秒为单位时长
     * @return 格式化时长
     */
    public static String generateTime(long position) {
        if (position <= 0) {
            return "00:00";
        }

        int totalSeconds = (int) ((position / 1000.0) - 0.5);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.CHINA, "%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format(Locale.CHINA, "%02d:%02d", minutes, seconds);
        }
    }

    public static String getTime(long times) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        String ss = formatter.format(new Date(times * 1000));
        return ss;
    }

    public static String getDay(long times) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        String ss = formatter.format(new Date(times * 1000));
        return ss;
    }

    public static String getDate(long times) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM月yyyy年");
        String ss = formatter.format(new Date(times * 1000));
        return ss;
    }
}
