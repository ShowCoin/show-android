package one.show.live.netutil.netmanager.okhttp;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import one.show.live.netutil.netmanager.common.BaseNetResponse;
import one.show.live.netutil.netmanager.common.NetLoadingListener;
import one.show.live.netutil.okhttputils.OkHttpUtils;
import one.show.live.netutil.okhttputils.request.BaseRequest;
import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;


public class FileCallbackWrapper extends AbsCallbackWrapper<File, Integer, NetLoadingListener> {

    public static final String DM_TARGET_FOLDER = File.separator + "download" + File.separator; //下载目标文件夹

    /** 目标文件存储的文件夹路径 */
    private String destFileDir;
    /** 目标文件存储的文件名 */
    private String destFileName;

    public FileCallbackWrapper(String destFileName, NetLoadingListener listener, Integer flag) {
        this(Environment.getExternalStorageDirectory() + DM_TARGET_FOLDER, destFileName, listener, flag);
    }

    /**
     * @param destFileDir  要保存的目标文件夹
     * @param destFileName 要保存的文件名
     */
    public FileCallbackWrapper(@NonNull String destFileDir, @NonNull String destFileName, NetLoadingListener listener, Integer flag) {
        super(listener, flag);
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    @Override
    public File parseNetworkResponse(Response response) throws Exception {
        try {
            return saveFile(response);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        NetLoadingListener listener = getListener();
        if (listener != null) {
            listener.onBefore();
        }
    }

    @Override
    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        super.upProgress(currentSize, totalSize, progress, networkSpeed);
        NetLoadingListener listener = getListener();
        if (listener != null) {
            listener.onProgress(
                    new OkhttpNetLoading().setFlag(getFlag())
                            .setCurrentSize(currentSize)
                            .setTotalSize(totalSize)
                            .setProgress(progress)
                            .setNetworkSpeed(networkSpeed)
            );
        }
    }

    @Override
    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
        NetLoadingListener listener = getListener();
        if (listener != null) {
            listener.onProgress(
                    new OkhttpNetLoading().setFlag(getFlag())
                            .setCurrentSize(currentSize)
                            .setTotalSize(totalSize)
                            .setProgress(progress)
                            .setNetworkSpeed(networkSpeed)
            );
        }
    }

    @Override
    public void onResponse(boolean isFromCache, File file, Request request, @Nullable Response response) {
        NetLoadingListener listener = getListener();
        if (listener != null) {
            BaseNetResponse netResponse = new OkhttpNetResponse()
                    .setFlag(getFlag())
                    .setExtra(file)
                    .setFromCache(isFromCache)
                    .setResponse(response);

            listener.onNetResponse(netResponse);
        }
    }

    @Override
    public void onError(boolean isFromCache, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onError(isFromCache, call, response, e);
        NetLoadingListener listener = getListener();
        if (listener != null) {
            BaseNetResponse netResponse = new OkhttpNetResponse()
                    .setFlag(getFlag())
                    .setFromCache(isFromCache)
                    .setResponse(response)
                    .setException(e);

            listener.onNetError(netResponse);
        }
    }

    @Override
    public void onAfter(boolean isFromCache, @Nullable File file, Call call, @Nullable Response response, @Nullable Exception e) {
        super.onAfter(isFromCache, file, call, response, e);
        NetLoadingListener listener = getListener();
        if (listener != null) {
            BaseNetResponse netResponse = new OkhttpNetResponse()
                    .setFlag(getFlag())
                    .setExtra(file)
                    .setFromCache(isFromCache)
                    .setResponse(response)
                    .setException(e);

            listener.onAfter(netResponse);
        }
    }

    private File saveFile(Response response) throws IOException {
        File dir = new File(destFileDir);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, destFileName);
        if (file.exists()) file.delete();

        long lastRefreshUiTime = 0;  //最后一次刷新的时间
        long lastWriteBytes = 0;     //最后一次写入字节数据

        InputStream is = null;
        byte[] buf = new byte[2048];
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;
            int len;
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;

                long curTime = System.currentTimeMillis();
                //每200毫秒刷新一次数据
                if (curTime - lastRefreshUiTime >= 200 || finalSum == total) {
                    //计算下载速度
                    long diffTime = (curTime - lastRefreshUiTime) / 1000;
                    if (diffTime == 0) diffTime += 1;
                    long diffBytes = finalSum - lastWriteBytes;
                    final long networkSpeed = diffBytes / diffTime;
                    OkHttpUtils.getInstance().getDelivery().post(new Runnable() {
                        @Override
                        public void run() {
                            downloadProgress(finalSum, total, finalSum * 1.0f / total, networkSpeed);   //进度回调的方法
                        }
                    });

                    lastRefreshUiTime = System.currentTimeMillis();
                    lastWriteBytes = finalSum;
                }
            }
            fos.flush();
            return file;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** 通过 ‘？’ 和 ‘/’ 判断文件名 */
    private String getUrlFileName(String url) {
        int index = url.lastIndexOf('?');
        String filename;
        if (index > 1) {
            filename = url.substring(url.lastIndexOf('/') + 1, index);
        } else {
            filename = url.substring(url.lastIndexOf('/') + 1);
        }
        return filename;
    }

}
