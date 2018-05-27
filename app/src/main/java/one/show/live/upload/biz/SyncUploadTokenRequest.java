package one.show.live.upload.biz;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import one.show.live.api.BaseAPI;
import one.show.live.api.BaseRequestSync;
import one.show.live.common.po.POCommonResp;
import one.show.live.po.POUploadToken;


public class SyncUploadTokenRequest extends BaseRequestSync<POUploadToken> {

    @Override
    public String getPath() {
        return BaseAPI.Path.getUploadToken;
    }

    @Override
    public void onRequestResult(String result) {
        Type type = new TypeToken<POCommonResp<POUploadToken>>() {
        }.getType();
        responseBean = new Gson().fromJson(result, type);
    }

    /**
     * @param ext  文件扩展名
     * @param type 0上传作品。1。素材认证
     */
    public POCommonResp<POUploadToken> getToken(String ext, int type) {
        Map<String, String> params = new HashMap<>();
        params.put("ext", ext);
        params.put("type", type + "");
        params.put("cdnType", "1");
        return requestSync(params);
    }
}
