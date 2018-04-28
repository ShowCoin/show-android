package one.show.live.media.presenter;

import java.util.HashMap;

import one.show.live.common.ui.BasePresenter;
import one.show.live.media.model.PublisherBreakRequest;
import one.show.live.media.model.PublisherCloseRequest;
import one.show.live.media.model.PublisherDispatchRequest;
import one.show.live.media.po.POPublisherEnd;
import one.show.live.media.view.PublisherView;

/**
 * Created by Administrator on ..7/19 0019.
 */
public class PublisherPresenter extends BasePresenter {

  private PublisherView publisherView;

  public PublisherPresenter(PublisherView view){
      this.publisherView = view;
  }

  /**
   * 推流成功后，调用分发接口
   * @param streamName
   */
  public void dispatchVideoStream(String streamName){
    HashMap<String,String> param = new HashMap<>();
    param.put("streamName",streamName);
    new PublisherDispatchRequest(){
      @Override public void onFinish(boolean isSuccess, String msg, Object data) {
        if(isSuccess){
          publisherView.dispatchStreamSuccess();
        }else{
          publisherView.dispatchStreamFailed(msg);
        }
      }
    }.startRequest(param);
  }

  /**
   * 押后台等操作，需要中断推流时，通知服务器break
   * @param streamName
   */
  public void breakVideoStream(String streamName){
    HashMap<String,String> param = new HashMap<>();
    param.put("streamName",streamName);
    new PublisherBreakRequest(){
      @Override public void onFinish(boolean isSuccess, String msg, Object data) {
        if(isSuccess){
          publisherView.breakSuccess();
        }else{
          publisherView.breakFailed(msg);
        }
      }
    }.startRequest(param);
  }

  public void closePublisher(String liveID){
    HashMap<String,String> param = new HashMap<>();
    param.put("liveId",liveID);
    new PublisherCloseRequest(){
      @Override public void onFinish(boolean isSuccess, String msg, POPublisherEnd data) {
        if(isSuccess){
          publisherView.publisherEndSuccess(data);
        }else{
          publisherView.publisherEndFailed(msg);
        }
      }
    }.startRequest(param);
  }

}
