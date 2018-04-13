package one.show.live.api;

import java.util.Map;

import one.show.live.api.CommonRequestSync;
import one.show.live.po.POCommonResp;

public abstract class BaseRequestSync<T> extends CommonRequestSync<T> {

    /**
     * 获取URL
     * <p/>
     */
    public String getRequestUrl() {
        return BaseAPI.Domain.bizDomain + getPath();
    }

    /**
     * 固定服务器的path地址
     */
    public abstract String getPath();


    public POCommonResp<T> requestSync(final Map<String, String> params) {
        startRequest(params, null, null);
        return responseBean;
    }

}
