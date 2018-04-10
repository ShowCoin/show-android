package one.show.live.util;

/**
 * Created by ydeng on 2017/9/21.
 * 内存对象管理
 */

public class CacheUtils {
    private static long clientServerTimeDelta = 0;//服务器与客户端的系统时差
    public static void setClientServerTimeDelta(long timeDelta){
        clientServerTimeDelta = timeDelta;
    }

    public static long getClientServerTimeDelta(){
        return clientServerTimeDelta;
    }
}
