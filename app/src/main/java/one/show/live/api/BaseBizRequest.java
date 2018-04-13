package one.show.live.api;

import one.show.live.common.api.CommonRequest;

public abstract class BaseBizRequest<T> extends CommonRequest<T> {

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

}
