package one.show.live.media.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import one.show.live.api.BaseAPI;
import one.show.live.api.BaseBizRequest;
import one.show.live.common.po.POCommonResp;
import one.show.live.common.util.StringUtils;
import one.show.live.media.po.POPublisherEnd;
import one.show.live.log.Logger;

/**
 * Created by Administrator on ..7/19 0019.
 */
public abstract class PublisherCloseRequest extends BaseBizRequest<POPublisherEnd> {
  @Override public String getPath() {
    return BaseAPI.Path.closeLive;
  }

  @Override public void onRequestResult(String result) {
    if(StringUtils.isNotEmpty(result)){
      Logger.e("PublisherCloseRequest", "onRequestResult result: "+result);
    }
    Type type = new TypeToken<POCommonResp<POPublisherEnd>>() {
    }.getType();
    responseBean = new Gson().fromJson(result, type);
  }

}