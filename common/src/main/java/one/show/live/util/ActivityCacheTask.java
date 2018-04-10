//package one.show.live.util;
//
//import android.app.Activity;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 一键关闭集合内的所有页面
// */
//public class ActivityCacheTask {
//
//    public static List<Activity> mLst = new ArrayList<Activity>();
//
//    /**
//     * 删除所有Activity
//     */
//    public static void romoveList() {
//        for (Activity activity : mLst) activity.finish();
//    }
//
//    /**
//     * 添加Activity到集合
//     */
//    public static void addActivity(Activity activity) {
//        mLst.add(activity);
//    }
//    /**
//     * 删除关闭的页面
//     */
//    public static void deleteActivity(Activity activity) {
//        mLst.remove(activity);
//    }
//}
