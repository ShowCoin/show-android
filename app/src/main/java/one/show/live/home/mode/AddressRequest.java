package one.show.live.home.mode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;



public abstract class AddressRequest extends BaseBizRequest<POAddress> {

    @Override
    public String getPath() {
        return BaseAPI.Path.getProvince;
    }

    @Override
    public void onRequestResult(String result) {

        Type type = new TypeToken<POCommonResp<POAddress>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }
    public void getData(Map<String,String> map){
        startRequest(map);
    }

}
