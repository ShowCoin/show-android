package one.show.live.po;

/**
 * Created by apple on 16/7/6.
 */
public class POLaunch {

    public static String url;//打点的url，APP启动的时候从application中赋值过来的

    /**
     * app启动时间
     */
    public static String startTime;


    /**
     * 打点的方法名
     */
    //app启动
    public static String launch = "/stat/launch";
    //app退出
    public static String exit = "/stat/exit";
    //视频播放时长
    public static String play = "/stat/play";


    /**
     * 参数名
     */
//  (time 从app上次启动到退出的时长，单位：秒)
    public static String time = "time";

}
