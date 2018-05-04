package one.show.live.showlive.network;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Base network request
 */
public class BaseURLRequest {
    protected int connectTimeout = 5000;
    protected int readTimeout = 5000;

    public BaseURLRequest() {

    }

    public BaseURLRequest(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public BaseURLRequest(int connectTimeout, int readTimeout) {
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    /**
     * Synchronized to obtain input stream from remote server through GET method
     */
    public InputStream get(String url, String params) throws IOException {

        String strURL = (TextUtils.isEmpty(params)) ? url : (url + "?" + params);

        HttpURLConnection connect = getURLConnection(strURL);
        connect.setRequestMethod("GET");
        connect.setConnectTimeout(connectTimeout);
        connect.setReadTimeout(connectTimeout);
        connect.connect();
        int code = connect.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK) {
            return connect.getInputStream();
        } else {
            throw new IOException("Error Response:" + code);
        }
    }

    public InputStream get(String url, Map<String, String> params) throws IOException {
        if (params != null && params.size() > 0) {
            StringBuilder sb = new StringBuilder();

            for (String key : params.keySet()) {
                sb.append(key).append('=').append(URLEncoder.encode(params.get(key), "UTF-8")).append('&');
            }

            sb.deleteCharAt(sb.length() - 1);
            String paramsStr = sb.toString();
            return get(url, paramsStr);
        } else {
            return get(url, "");
        }
    }

    /**
     * Synchronized to obtain input stream from remote server through POST method
     */
    public InputStream post(String url, String param) throws IOException {
        if (param == null) {
            param = "";
        }

        HttpURLConnection connect = getURLConnection(url);
        connect.setConnectTimeout(connectTimeout);
        connect.setReadTimeout(connectTimeout);
        connect.setDoInput(true);
        connect.setDoOutput(true);
        byte[] formData = param.getBytes();
        connect.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connect.setRequestProperty("Content-Length", formData.length + "");
        connect.connect();
        OutputStream output = connect.getOutputStream();
        output.write(formData);
        output.flush();
        output.close();

        int code = connect.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK) {
            return connect.getInputStream();
        } else {
            throw new IOException("server error：" + code);
        }
    }

    public InputStream post(String url, Map<String, String> params) throws IOException {
        if (params == null || params.size() == 0)
            throw new IOException("error : the 'params' must not be null");

        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            sb.append(key).append('=').append(params.get(key)).append('&');
        }

        sb.deleteCharAt(sb.length() - 1);
        return post(url, sb.toString());
    }


    private HttpURLConnection getURLConnection(String url) throws IOException {
        URL u = new URL(url);
        if ("https".equals(u.getProtocol())) {
            return new HttpsRequest().getURLConnection(u);
        } else {
            return new HttpRequest().getURLConnection(u);
        }
    }
}
