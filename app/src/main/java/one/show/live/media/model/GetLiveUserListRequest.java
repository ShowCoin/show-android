package one.show.live.media.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import one.show.live.api.BaseAPI;
import one.show.live.api.BaseBizRequest;
import one.show.live.common.po.POCommonResp;
import one.show.live.common.po.POListData;
import one.show.live.media.po.POIMUser;
import one.show.live.media.po.POIMUserList;
import one.show.live.po.PORankLive;

public abstract class GetLiveUserListRequest extends BaseBizRequest<POIMUserList<POIMUser>>{
    @Override
    public String getPath() {
        return BaseAPI.Path.live_userlist;
    }

    @Override
    public void onRequestResult(String result) {
        System.out.println(result);
        Type type = new TypeToken<POCommonResp<POIMUserList<POIMUser>>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }


    public void getData(String roomId){
        Map<String,String> params = new HashMap<>();
        params.put("roomId",roomId);
        params.put("cursor","0");
        params.put("count","20");
        startRequest(params);
    }

}
