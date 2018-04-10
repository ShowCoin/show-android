package one.show.live.util;

import android.text.TextUtils;

/**
 * Created by seeu on 2018/1/24.
 */

public class StringUtil {

    public static String getUrlName(String str){
        if (!TextUtils.isEmpty(str)){
            if (str.contains("/")){
                return str.substring(str.lastIndexOf("/") + 1, str.length());
            }
        }

        return null;
    }
    public static String getFileName(String str){
        if (!TextUtils.isEmpty(str)){
            if (str.contains("/")){
                return str.substring(0, str.lastIndexOf("."));
            }
        }

        return null;
    }
}
