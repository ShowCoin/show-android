package one.show.live.common.net;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class BaseFormRequest {
    protected int connectTimeout = 5000;
    protected int readTimeout = 5000;

    private final String boundary = "---------------------------" + System.currentTimeMillis();
    private static final String LINE_FEED = "\r\n";
    private HttpURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;
    private OnProgressChangedListener listener;
    private int progress;


    public BaseFormRequest() {

    }

    public BaseFormRequest(OnProgressChangedListener listener) {
        this.listener = listener;
    }

    /**
     * post 文件到服务器
     *
     * @param url    请求地址
     * @param params 请求参数
     * @param files  发送的文件
     * @return 返回的流
     * @throws IOException 网络异常
     */
    public InputStream postFile(String url, Map<String, String> params, Map<String, String> files) throws IOException {

        createConnection(url, "UTF-8");
        if (params != null) {
            for (String key : params.keySet()) {
                addFormField(key, params.get(key));
            }
        }

        if (files != null && listener != null) {
            int totalSize = 0;
            File file;
            for (String key : files.keySet()) {
                file = new File(files.get(key));
                totalSize += file.length();
            }
            listener.onTotalSize(totalSize);
        }

        if (files != null) {
            for (String key : files.keySet()) {
                addFilePart(key, new File(files.get(key)));
            }
        }
//        InputStream inputStream = connect();
//        httpConn.disconnect();
        return connect();
    }

    private void createConnection(String requestURL, String charset) throws IOException {
        this.charset = charset;
        createHttpConnection(requestURL);
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true); // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setConnectTimeout(connectTimeout);
        httpConn.setReadTimeout(connectTimeout);

        httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        httpConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.7.6)");
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
    }

    private void createHttpConnection(String url) throws IOException {
        URL u = new URL(url);
        if ("https".equals(u.getProtocol())) {
            httpConn = new HttpsRequest().getURLConnection(u);
        } else {
            httpConn = new HttpRequest().getURLConnection(u);
        }

    }

    /**
     * 添加提交的字段
     *
     * @param name  field name
     * @param value field value
     */
    private void addFormField(String name, String value) {
        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"").append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=").append(charset).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * 添加提交的文件
     *
     * @param fieldName  字段名
     * @param uploadFile 提交的文件
     * @throws IOException
     */
    private void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--").append(boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"").append(fieldName).append("\"; filename=\"").append(fileName).append("\"").append(LINE_FEED);
        writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);

        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[1024];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
            if (listener != null) {
                progress += bytesRead;
                listener.onProgressChanged(progress);
            }

        }
        outputStream.flush();
        inputStream.close();
        writer.flush();
    }

    /**
     * 添加Header字段
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name).append(": ").append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * 完成该请求并接收来自服务器的响应。
     *
     * @return 服务器返回流
     */
    private InputStream connect() throws IOException {

        writer.append(LINE_FEED).flush();
        writer.append("--").append(boundary).append("--").append(LINE_FEED);
        writer.close();

        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            return httpConn.getInputStream();
        } else {
            throw new IOException("服务器返回错误：" + httpConn.getResponseCode());
        }
    }
}
