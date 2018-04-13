package one.show.live.net;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络请求
 */
public class HttpRequest {

    protected HttpURLConnection getURLConnection(URL url) throws IOException {
        return (HttpURLConnection)url.openConnection();
    }

}
