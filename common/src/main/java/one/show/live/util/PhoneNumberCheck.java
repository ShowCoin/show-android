package one.show.live.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by apple on 18/3/20.
 *
 * 手机号校验
 */
public class PhoneNumberCheck {

    public static boolean PhoneOf(String mobiles){
        boolean flag = false;
        try {
            Pattern p = Pattern.compile("(1)[3456789][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9][0-9]");
            Matcher m = p.matcher(mobiles);
            flag = m.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }
}
