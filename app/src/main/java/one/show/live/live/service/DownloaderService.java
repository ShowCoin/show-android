package one.show.live.live.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import one.show.live.net.OkHttpRequest;
import one.show.live.util.FileUtils;
import one.show.live.util.StringUtils;
import one.show.live.util.WeakHandler;
import one.show.live.log.Logger;
import one.show.live.util.ZipUtils;

public class DownloaderService extends Service {




    public static class DownLoadBean implements Cloneable {

        public static final int DOWNLOAD_STATUS_INIT = 100;
        public static final int DOWNLOAD_STATUS_DOWNLOADING = 101;
        public static final int DOWNLOAD_STATUS_COMPLETE = 102;
        public static final int DOWNLOAD_STATUS_ERROR = 103;


        public String url;

        public String fileName;

        public String fileDirPath;

        public String fileNameEndType;

        public int progress;

        public int status = DOWNLOAD_STATUS_INIT;

        public String msg;


        public boolean isDownLoadComplete() {
            return status == DOWNLOAD_STATUS_COMPLETE;
        }

        public boolean isDownloading() {
            return status == DOWNLOAD_STATUS_INIT || status == DOWNLOAD_STATUS_DOWNLOADING;
        }


        public DownLoadBean setStatus(int status) {
            this.status = status;
            return this;
        }

        public DownLoadBean setProgress(int progress) {
            this.progress = progress;
            return this;
        }

        public DownLoadBean setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        private DownLoadBean() {
        }

        public DownLoadBean(String url, String fileName, String fileDirPath, String fileNameEndType) {
            this.url = url;
            this.fileDirPath = fileDirPath;
            this.fileName = fileName;
            this.fileNameEndType = fileNameEndType;
        }


        public String getFileName() {
            return fileName + fileNameEndType;
        }


        public String getFilePath() {
            return fileDirPath + File.pathSeparator + fileName + fileNameEndType;
        }


        public String getUnzipFilePath() {
            if (fileNameEndType.endsWith(".zip")) {
                return fileDirPath + File.pathSeparator + fileName;
            }
            return null;
        }


        public DownLoadBean clone() {
            DownLoadBean o = null;
            try {
                o = (DownLoadBean) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return o;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DownLoadBean that = (DownLoadBean) o;

            if (!url.equals(that.url)) return false;
            if (!fileName.equals(that.fileName)) return false;
            if (!fileDirPath.equals(that.fileDirPath)) return false;
            return fileNameEndType.equals(that.fileNameEndType);

        }

        @Override
        public int hashCode() {
            int result = url.hashCode();
            result = 31 * result + fileName.hashCode();
            result = 31 * result + fileDirPath.hashCode();
            result = 31 * result + fileNameEndType.hashCode();
            return result;
        }
    }
    private ThreadPool mThreadPool;

    private WeakHandler mHandler;

    public static ConcurrentHashMap<String, DownLoadBean> downLoadUrls = new ConcurrentHashMap<>();


    protected static final String TAG = "DownloaderService";
    private WifiReceiver mReceiver;
    private static DownloaderService serviceInstance;

    @Override
    public void onCreate() {
        Logger.e("DownloaderService", "onCreate()");
        super.onCreate();
        serviceInstance = this;
        mHandler = new WeakHandler(callback);
        mReceiver = new WifiReceiver(this);
        registerReceiver(mReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
        mThreadPool = new ThreadPool();
    }


    private Handler.Callback callback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            checkIfHasNoTaskStop();
            return false;
        }
    };

    public static DownloaderService getInstance() {
        return serviceInstance;
    }


    private void checkIfHasNoTaskStop() {
        if (!isRunning()) {
            Logger.i(TAG, "onUnbind - stopSelf");
            stopSelf();
        }
    }

    @Override
    public void onDestroy() {
        Logger.e("DownloaderService", "onDestroy()");
        stopAll();
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Logger.e("DownloaderService", "onBind()");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Logger.e("DownloaderService", "onUnbind()");
        boolean result = super.onUnbind(intent);
        checkIfHasNoTaskStop();
        return result;
    }

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        public DownloaderService getService() {
            return DownloaderService.this;
        }
    }



    public boolean isRunning() {
        synchronized (this) {
            return mThreadPool != null && mThreadPool.isRunning();
        }
    }

    public void stopAll() {
        synchronized (this) {
            if (mThreadPool != null) {
                mThreadPool.shutdown();
            }
            mThreadPool = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            int dotype = intent.getIntExtra("dotype", -1);
            Logger.e("DownloaderService", "dotype=" + dotype);
            switch (dotype) {
                case 0://下载
                {
                    Logger.e("DownloaderService", "下载");
                    String url = intent.getStringExtra("url");
                    String fileName = intent.getStringExtra("fileName");
                    String fileDirPath = intent.getStringExtra("fileDirPath");
                    String fileNameEndType = intent.getStringExtra("fileNameEndType");
                    startDownload(url, fileName, fileDirPath, fileNameEndType);
                    break;
                }
                case 1://删除
                {
                    Logger.e("DownloaderService", "删除");
                    String url = intent.getStringExtra("url");
                    delete(url);
                    break;
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    private void startDownload(final String url, final String fileName, final String fileDirPath, final String fileNameEndType) {
        if (mThreadPool == null) {
            synchronized (this) {
                if (mThreadPool == null)
                    mThreadPool = new ThreadPool();
            }
        }

        DownLoadBean bean = getDownLoadBean(url, fileName, fileDirPath, fileNameEndType);
        FileUtils.deleteFile(bean.getFilePath());
        DownloaderThread thread = new DownloaderThread(bean, mHandler);
        mThreadPool.queue(thread, url);
    }



    private void delete(String... urls) {
        if (urls != null) {
            for (String url : urls) {
                if (StringUtils.isEmpty(url)) return;
                if (mThreadPool != null) {
                    mThreadPool.remove(url);
                }
            }
        }
    }

    private DownLoadBean getDownLoadBean(String url, String fileName, String fileDirPath, String fileNameEndType) {
        DownLoadBean bean = downLoadUrls.get(url);
        if (bean == null) {
            DownLoadBean newBean = new DownLoadBean(url, fileName, fileDirPath, fileNameEndType);
            bean = downLoadUrls.putIfAbsent(url, newBean);
            if (bean == null) {
                bean = newBean;
            }
        }
        return bean;
    }

    public static class DownloaderThread extends CanCancelThread implements Runnable {
        private DownLoadBean downLoadBean;
        private boolean isComplete;
        private WeakHandler handler;


        public DownloaderThread(DownLoadBean downLoadBean, WeakHandler handler) {
            this.downLoadBean = downLoadBean;
            this.handler = handler;
        }


        public boolean unZip(final DownLoadBean bean) {
            if (FileUtils.checkFile(bean.getFilePath())) {
                String dir = new File(bean.getFilePath()).getParent();
                if (FileUtils.checkFile(dir)) {
                    File oldFile = new File(dir, bean.getFileName());
                    try {
                        FileUtils.deleteDir(oldFile);
                        ZipUtils.UnZipFolder(bean.getFilePath(), dir);
                        FileUtils.deleteFile(bean.getFilePath());
                        return true;
                    } catch (Exception e) {
                        FileUtils.deleteDir(oldFile);
                        FileUtils.deleteFile(bean.getFilePath());
                        Logger.e(e);
                    }
                }
            }
            return false;
        }


        @Override
        public void run() {
            try {
                if (isCancelled()) {
                    return;
                }

                EventBus.getDefault().post(downLoadBean.setStatus(DownLoadBean.DOWNLOAD_STATUS_DOWNLOADING));

                if (isCancelled()) {
                    return;
                }

                OkHttpRequest.downLoadFile(downLoadBean.url, downLoadBean.fileDirPath, downLoadBean.getFileName(), new OkHttpRequest.OnReceiveProgress() {

                    @Override
                    public void onProgress(int progress) {
                        if (isCancelled()) {
                            return;
                        }
                        //不需要进度
//            EventBus.getDefault().post(getDownLoadBean().setProgress(progress));
                    }

                    @Override
                    public void onComplete(String filePath) {
                        isComplete = true;
                        if (downLoadBean.fileNameEndType.equals(".zip")) {
                            if (unZip(downLoadBean)) {
                                EventBus.getDefault().post(downLoadBean.setStatus(DownLoadBean.DOWNLOAD_STATUS_COMPLETE));
                            } else {
                                EventBus.getDefault().post(downLoadBean.setStatus(DownLoadBean.DOWNLOAD_STATUS_ERROR).setMsg("文件解压失败"));
                            }
                        } else {
                            EventBus.getDefault().post(downLoadBean.setStatus(DownLoadBean.DOWNLOAD_STATUS_COMPLETE));
                        }
                    }

                    @Override
                    public boolean needCancel() {
                        return Thread.currentThread().isInterrupted() || isCancelled();
                    }
                });

                if (isCancelled()) {
                    return;
                }

                if (!isComplete) {
                    EventBus.getDefault().post(downLoadBean.setStatus(DownLoadBean.DOWNLOAD_STATUS_ERROR).setMsg("文件下载未完成"));
                }

            } catch (Exception e) {
                EventBus.getDefault().post(downLoadBean.setStatus(DownLoadBean.DOWNLOAD_STATUS_ERROR).setMsg(e.getMessage()));
                e.printStackTrace();
            } finally {
                //线程执行完毕
                isFinished = true;
                handler.sendEmptyMessage(0);
            }
        }
    }

    private static class ThreadPool {
        private boolean mStopped = false;
        private ThreadPoolExecutor mQueue;
        private Map<String, RequestHandle> mTasks;

        public ThreadPool() {
            mQueue =
                    new ThreadPoolExecutor(3, 6, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                            sThreadFactory, new ThreadPoolExecutor.DiscardPolicy());

            mTasks = Collections.synchronizedMap(new WeakHashMap<String, RequestHandle>());
        }

        public void queue(DownloaderThread run, String url) {
            mQueue.submit(run);

            if (!StringUtils.isNotEmpty(url)) {
                RequestHandle task = mTasks.get(url);

                if (task != null) {
                    task.cancel(true);
                }

                for (String urlStr : mTasks.keySet()) {
                    if (mTasks.get(urlStr).shouldBeGarbageCollected()) {
                        mTasks.remove(urlStr);
                    }
                }

                task = new RequestHandle(run);
                mTasks.put(url, task);
            }
        }

        public void cancelRequests(final String action, final boolean mayInterruptIfRunning) {
            if (!TextUtils.isEmpty(action)) {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        for (RequestHandle requestHandle : mTasks.values()) {
                            requestHandle.cancel(mayInterruptIfRunning);
                        }
                        mTasks.remove(action);
                    }
                };
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    new Thread(r).start();
                } else {
                    r.run();
                }
            }
        }

        public void cancelAllRequests(boolean mayInterruptIfRunning) {
            for (RequestHandle requestHandle : mTasks.values()) {
                requestHandle.cancel(mayInterruptIfRunning);
            }
            mTasks.clear();
        }

        public boolean remove(String url) {
            if (mTasks != null && mTasks.containsKey(url)) {
                return mTasks.get(url).cancel(true);
            }
            return false;
        }

        public boolean isRunning() {
            synchronized (this) {
                return !mStopped && mQueue.getActiveCount() > 0;
            }
        }

        public void shutdown() {
            synchronized (this) {
                if (!mStopped) {
                    cancelAllRequests(true);
                    mQueue.shutdownNow();
                    mStopped = true;
                }
            }
        }

        private static final ThreadFactory sThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "DownloaderThread #" + mCount.getAndIncrement());
            }
        };
    }

    private static class WifiReceiver extends BroadcastReceiver {
        private WeakReference<DownloaderService> mService;

        private WifiReceiver(DownloaderService service) {
            mService = new WeakReference<DownloaderService>(service);
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            final DownloaderService service = mService.get();
            if (service == null) {
                return;
            }
            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            switch (state) {
                case WifiManager.WIFI_STATE_ENABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                default:
                    // ToastHelper.showToast(service, Toast.LENGTH_LONG,
                    // R.string.wifi_not_available);
                    service.stopAll();
                    break;
            }
        }
    }

}
