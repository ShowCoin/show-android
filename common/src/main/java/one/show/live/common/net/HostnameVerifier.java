package one.show.live.common.net;

import javax.net.ssl.SSLSession;

/**
 */
public class HostnameVerifier implements javax.net.ssl.HostnameVerifier {
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}
