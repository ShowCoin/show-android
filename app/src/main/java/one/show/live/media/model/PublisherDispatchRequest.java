package one.show.live.media.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


import one.show.live.api.BaseAPI;
import one.show.live.api.BaseBizRequest;
import one.show.live.common.po.POCommonResp;
/**
 * Created by Administrator on ..7/19 0019.
 */
public abstract class PublisherDispatchRequest extends BaseBizRequest<Object> {

  @Override public String getPath() {
    return BaseAPI.Path.startLive;
  }

  @Override public void onRequestResult(String result) {
    Type type = new TypeToken<POCommonResp<Object>>() {
    }.getType();
    responseBean = new Gson().fromJson(result, type);
  }

}