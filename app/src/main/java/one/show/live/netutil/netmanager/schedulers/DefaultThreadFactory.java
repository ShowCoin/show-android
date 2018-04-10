package one.show.live.netutil.netmanager.schedulers;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory extends AtomicInteger implements ThreadFactory {

    private static final long serialVersionUID = -7789753024099756196L;

    final String prefix;

    final int priority;

    final boolean nonBlocking;

    public DefaultThreadFactory(String prefix) {
        this(prefix, Thread.NORM_PRIORITY, false);
    }

    public DefaultThreadFactory(String prefix, int priority) {
        this(prefix, priority, false);
    }

    public DefaultThreadFactory(String prefix, int priority, boolean nonBlocking) {
        this.prefix = prefix;
        this.priority = priority;
        this.nonBlocking = nonBlocking;
    }

    @Override
    public Thread newThread(@NonNull Runnable r) {
        StringBuilder nameBuilder = new StringBuilder(prefix).append('-').append(incrementAndGet());

        String name = nameBuilder.toString();
        Thread t = new Thread(r, name);
        t.setPriority(priority);
        t.setDaemon(true);
        return t;
    }

    @Override
    public String toString() {
        return "RxThreadFactory[" + prefix + "]";
    }

}
