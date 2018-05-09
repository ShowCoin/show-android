package one.show.live.util;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by ydeng on 2017/6/23.
 */

public class WeakHandler extends Handler {

    public interface IHandler {
        void handleMessage(Message msg);
    }

    private WeakReference<IHandler> mWeakRef;

    public WeakHandler(IHandler handlermessage) {
        mWeakRef = new WeakReference<>(handlermessage);
    }
    public WeakHandler(IHandler handlermessage, Looper looper) {
        super(looper);
        mWeakRef = new WeakReference<>(handlermessage);
    }

    @Override
    public void handleMessage(Message msg) {
        if (mWeakRef == null) {
            return;
        }
        if (mWeakRef.get() != null) {
            try {
                mWeakRef.get().handleMessage(msg);
            }catch (NullPointerException e){//防止垃圾回收时的线程问题
            }
        }
    }
}