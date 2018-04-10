package one.show.live.netutil.netmanager.schedulers;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class Schedulers {

    private static final String TAG = Schedulers.class.getSimpleName();

    private final Handler mMainThread;

    private static Schedulers mInstance = new Schedulers();

    private final Map<Object, List<Future>> mFutureMap = new WeakHashMap<>();

    private final Map<Object, List<Runnable>> mRunnableMap = new WeakHashMap<>();

    private final ScheduledThreadPoolExecutor mThreadPool;

    private Schedulers () {
        this.mMainThread = new Handler(Looper.getMainLooper());
        this.mThreadPool = new ScheduledThreadPoolExecutor(1, new DefaultThreadFactory(TAG));
    }

    public static Schedulers getInstance() {
        return mInstance;
    }

    public void io(Runnable runnable) {
        Future future = threadPool().submit(runnable);
    }

    public void io(Runnable runnable, Object token) {
        Future future = threadPool().submit(runnable);

        List<Future> futureList = mFutureMap.get(token);
        if (futureList == null) {
            futureList = new ArrayList<>();
            mFutureMap.put(token, futureList);
        }
        futureList.add(future);
    }

    public void mainThread(Runnable runnable) {
        mainThread().post(runnable);
    }

    public void mainThread(Runnable runnable, Object token) {
        mainThread().postAtTime(runnable, token, 0);
        /*
        mainThread().post(runnable);
        List<Runnable> runnableList = mRunnableMap.get(token);
        if (runnableList == null) {
            runnableList = new ArrayList<>();
            mRunnableMap.put(token, runnableList);
        }
        runnableList.add(runnable);
        //*/
    }

    public void background(Runnable runnable) {
        Future future = threadPool().submit(runnable);
    }

    public void background(Runnable runnable, Object token) {
        Future future = threadPool().submit(runnable);

        List<Future> futureList = mFutureMap.get(token);
        if (futureList == null) {
            futureList = new ArrayList<>();
            mFutureMap.put(token, futureList);
        }
        futureList.add(future);
    }

    public Handler mainThread() {
        return mMainThread;
    }

    public ScheduledThreadPoolExecutor threadPool() {
        return mThreadPool;
    }

    public void cancel(Object token) {
        mainThread().removeCallbacksAndMessages(token);

        if (token == null) {
            return;
        }

        List<Future> futureList = mFutureMap.get(token);
        if (futureList != null) {
            for (int i = 0; i < futureList.size(); i++) {
                Future future = futureList.get(i);
                if (future != null && !future.isCancelled() && !future.isDone()) {
                    future.cancel(false);
                }
            }
        }
        mFutureMap.remove(token);
    }

}
