package one.show.live.netutil.okhttputils.cookie.store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import okhttp3.Cookie;
import okhttp3.HttpUrl;


public class MemoryCookieStore implements CookieStore {

    private final HashMap<String, List<Cookie>> memoryCookies = new HashMap<>();

    @Override
    public synchronized void saveCookies(HttpUrl url, List<Cookie> cookies) {
        List<Cookie> oldCookies = memoryCookies.get(url.host());
        List<Cookie> needRemove = new ArrayList<>();
        for (Cookie newCookie : cookies) {
            for (Cookie oldCookie : oldCookies) {
                if (newCookie.name().equals(oldCookie.name())) {
                    needRemove.add(oldCookie);
                }
            }
        }
        oldCookies.removeAll(needRemove);
        oldCookies.addAll(cookies);
    }

    @Override
    public synchronized List<Cookie> loadCookies(HttpUrl url) {
        List<Cookie> cookies = memoryCookies.get(url.host());
        if (cookies == null) {
            cookies = new ArrayList<>();
            memoryCookies.put(url.host(), cookies);
        }
        return cookies;
    }

    @Override
    public synchronized List<Cookie> getAllCookie() {
        List<Cookie> cookies = new ArrayList<>();
        Set<String> httpUrls = memoryCookies.keySet();
        for (String url : httpUrls) {
            cookies.addAll(memoryCookies.get(url));
        }
        return cookies;
    }

    @Override
    public synchronized boolean removeCookie(HttpUrl url, Cookie cookie) {
        List<Cookie> cookies = memoryCookies.get(url.host());
        return (cookie != null) && cookies.remove(cookie);
    }

    @Override
    public synchronized boolean removeCookies(HttpUrl url) {
        return memoryCookies.remove(url.host()) != null;
    }

    @Override
    public synchronized boolean removeAllCookie() {
        memoryCookies.clear();
        return true;
    }
}
