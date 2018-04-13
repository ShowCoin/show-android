package one.show.live.live.service;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by samuel on 15/12/10.
 */
public class CanCancelThread {

  protected volatile boolean isFinished;
  protected final AtomicBoolean isCancelled = new AtomicBoolean();

  public boolean cancel(boolean mayInterruptIfRunning) {
    isCancelled.set(true);
    return isCancelled();
  }

  public boolean isDone() {
    return isCancelled() || isFinished;
  }

  public boolean isCancelled() {
    return isCancelled.get();
  }
}
