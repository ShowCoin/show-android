package one.show.live.live.record.presenter;

import java.util.HashMap;
import java.util.Map;
import one.show.live.po.POMember;
import one.show.live.util.StringUtils;
import one.show.live.live.model.LiveStatusRequest;
import one.show.live.live.model.StartLiveRequest;
import one.show.live.live.po.POInitLive;
import one.show.live.live.po.POLiveStatus;
import one.show.live.live.record.view.ReadyToLiveView;

/**
 * Created by apple on 18/3/20.
 *
 * 准备直播页面的实现
 */
public class ReadyToLivePresenter {

  ReadyToLiveView view;

  public ReadyToLivePresenter(ReadyToLiveView view) {
    this.view = view;
  }

  /**
   * @param title 标题
   * @param pos 1显示定位0不显示定位
   */
  public void startLive(String title, int pos) {

    HashMap<String, String> param = new HashMap<>();
    param.put("title", title);
    param.put("pos", pos + "");
    new StartLiveRequest() {
      @Override public void onFinish(boolean isSuccess, String msg, POInitLive data) {
        if (isSuccess) {
          view.onReqStartLiveFinish(data);
        } else {
          view.onReqStartLiveFailed(msg);
        }
      }
    }.startRequest(param);
  }

  public void checkPublisherStatus(String liveId) {
    if (StringUtils.isNotEmpty(liveId)) {
      POMember member = POMember.getInstance();
      Map<String, String> params = new HashMap<>();
      params.put("uid", member.getBeke_userid());
      params.put("liveid", liveId);
      new LiveStatusRequest() {
        @Override public void onFinish(boolean isSuccess, String msg, POLiveStatus data) {
          if (isSuccess) {
            view.checkPublisherSuccess(data);
          } else {
            view.checkPublisherFailed();
          }
        }
      }.startRequest(params);
    }
  }
}
