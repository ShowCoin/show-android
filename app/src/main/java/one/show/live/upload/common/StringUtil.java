package one.show.live.upload.common;

public class StringUtil {
    public StringUtil() {
    }

    public static boolean isEmpty(String str) {
        return str == null?true:str.trim().length() == 0;
    }
}