package one.show.live.common.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import one.show.live.common.util.StringUtils;

public class OkHttpRequest {
    private static OkHttpRequest instance;
    private OkHttpClient client;

    private static Object lockObj = new Object();

    public static OkHttpRequest getInstance(HttpsUtils.SSLParams sslParams) {

        if (instance != null) {
            return instance;
        }
        synchronized (lockObj) {
            if (instance != null) {
                return instance;
            }

            instance = new OkHttpRequest();

            try {
                OkHttpClient.Builder builder = new OkHttpClient.Builder();
                instance.client = builder.sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .writeTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return instance;
    }


    public InputStream post(String url, Map<String, String> headParams, Map<String, String> params) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), StringUtils.ifNullReturnEmpty(entry.getValue()));
            }
        }

        RequestBody formBody = builder.build();

        Request.Builder reqBuilder = new Request.Builder();

        reqBuilder.addHeader("User-agent", "android");

        if (headParams != null) {
            for (Map.Entry<String, String> entry : headParams.entrySet()) {
                String value = entry.getValue();
                reqBuilder.addHeader(entry.getKey(), value);
            }
        }

        Request request = reqBuilder.url(url)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().byteStream();
        }
        throw new IOException("server error：" + response.code());
    }

    public InputStream post(String url, Map<String, String> headParams, Map<String, String> params, Map<String, String> files, final OnUploadProgressListener listener) throws IOException {

        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }

        if (files != null) {
            long totalSize = 0;
            for (Map.Entry<String, String> entry : files.entrySet()) {
                File file = new File(entry.getValue());
                if (file.exists()) {
                    totalSize += file.length();
                    builder.addFormDataPart(entry.getKey(), file.getName(),

//                            new UploadRequestBody(RequestBody.create(MediaType.parse(file.getName()), file), new UploadRequestBody.UploadDataListener() {
//                        @Override
//                        public void onProgress(long byteCount) {
//                            listener.onProgressChanged(byteCount);
//                        }
//                    }
                            new ProgressRequestBody(RequestBody.create(MediaType.parse(file.getName()), file), new ProgressRequestBody.ProgressRequestListener() {
                                @Override
                                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                                    listener.onProgressChanged(bytesWritten, contentLength, done);
                                    if (done) {
                                        listener.onFinish();
                                    }
                                }
                            })
                    );
                }
            }
            listener.onTotalSize(totalSize);
        }

        RequestBody formBody = builder.build();

        Request.Builder reqBuilder = new Request.Builder();

        reqBuilder.addHeader("User-agent", "android");

        if (headParams != null) {
            for (Map.Entry<String, String> entry : headParams.entrySet()) {
                reqBuilder.addHeader(entry.getKey(), StringUtils.ifNullReturnEmpty(entry.getValue()));
            }
        }

        Request request = reqBuilder
                .url(url)
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().byteStream();
        }
        throw new IOException("server error：" + response.code());
    }

    public static void downLoadFile(String url, String fileDir, String fileName, OnReceiveProgress progressListener) {

        OkHttpClient httpClient = new OkHttpClient();

        Call call = httpClient.newCall(new Request.Builder().url(url).get().build());
        Response response = null;
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            response = call.execute();
            if (response.code() == 200) {

                byte[] buf = new byte[4096];

                is = response.body().byteStream();
                final long total = response.body().contentLength();
                int len = 0;
                long sum = 0;

                File dir = new File(fileDir);
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, fileName);
                fos = new FileOutputStream(file);

                progressListener.onProgress(0);

                while ((len = is.read(buf)) != -1 && !progressListener.needCancel()) {
                    sum += len;
                    fos.write(buf, 0, len);
                    progressListener.onProgress((int) (sum * 100 / total));
                }
                fos.flush();
                progressListener.onProgress(100);
                progressListener.onComplete(file.getAbsolutePath());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (response != null)
                    response.body().close();
                if (is != null) is.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }

    public InputStream get(String url) throws IOException{
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().byteStream();
        }
        throw new IOException("server error：" + response.code());
    }


    public interface OnReceiveProgress {
        public void onProgress(int progress);

        public void onComplete(String filePath);

        public boolean needCancel();
    }

    /**
     * 上传、下载进度监听
     */

}
