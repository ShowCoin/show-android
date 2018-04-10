package one.show.live.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by apple on 16/6/6.
 * 正则校验
 */
public class ToolRegular {
    /**
     * 校验QQ 手机号  邮箱
     *
     * @param msg
     * @return
     */
    public static boolean relgularThred(String msg) {

        if (qQRelgular(msg)) {
            return true;
        }
        if (emailRelgular(msg)) {
            return true;
        }
        if (phoneRelgular(msg)) {
            return true;
        }
        return false;
    }


    /**
     * 校验QQ号
     *
     * @param msg
     * @return
     */
    public static boolean qQRelgular(String msg) {
        String regex = "[1-9]([0-9]{5,11})";
        String mailname = msg;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mailname);
        return matcher.matches();
    }

    /**
     * 校验邮箱
     *
     * @param msg
     * @return
     */
    public static boolean emailRelgular(String msg) {
        String regex = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";
        String mailname = msg;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mailname);
        return matcher.matches();
    }

    /**
     * 校验手机号
     *
     * @param msg
     * @return
     */
    public static boolean phoneRelgular(String msg) {
        String regex = "(1)[34578][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]";
        String mailname = msg;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mailname);
        return matcher.matches();
    }
}
