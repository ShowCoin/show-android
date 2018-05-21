package one.show.live.common.net;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadRequest {
    private int connectTimeout = 5000;

    public boolean get(String url, File file,OnProgressChangedListener listener) throws IOException {

        HttpURLConnection connect = (HttpURLConnection) new URL(url).openConnection();
        connect.setRequestMethod("GET");
        connect.setConnectTimeout(connectTimeout);
        connect.connect();
        int code = connect.getResponseCode();
        if (code != 200) {
            throw new IOException("Error Response:" + code);
        }

        FileOutputStream outputStream = new FileOutputStream(file);
        InputStream inputStream = connect.getInputStream();

        byte[] buf = new byte[1024];
        int len;
        int progress = 0;
        if (listener != null){
            listener.onTotalSize(connect.getContentLength());
        }
        while ((len = inputStream.read(buf)) != -1){
            outputStream.write(buf, 0, len);
            outputStream.flush();
            if (listener != null){
                progress += len;
                listener.onProgressChanged(progress);
            }
        }
        outputStream.close();
        inputStream.close();

        if (listener != null){
            listener.onFinish();
        }
        return true;
    }
}
