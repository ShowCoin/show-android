package one.show.live.media.view;

import one.show.live.media.po.POPublisherEnd;

/**
 * Created by Administrator on ..7/19 0019.
 */
public interface PublisherView {
  void dispatchStreamSuccess();
  void dispatchStreamFailed(String msg);

  void breakSuccess();
  void breakFailed(String msg);

  void publisherEndSuccess(POPublisherEnd data);
  void publisherEndFailed(String msg);
}
