package one.show.live.netutil.okhttputils.cookie.store;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.HttpUrl;


public interface CookieStore {

    /** 保存url对应所有cookie */
    void saveCookies(HttpUrl url, List<Cookie> cookie);

    /** 加载url所有的cookie */
    List<Cookie> loadCookies(HttpUrl url);

    /** 获取当前所有保存的cookie */
    List<Cookie> getAllCookie();

    /** 根据url和cookie移除对应的cookie */
    boolean removeCookie(HttpUrl url, Cookie cookie);

    /** 根据url移除所有的cookie */
    boolean removeCookies(HttpUrl url);

    /** 移除所有的cookie */
    boolean removeAllCookie();
}
