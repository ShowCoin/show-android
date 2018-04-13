package one.show.live.common.ui;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

import one.show.live.common.api.BaseRequest;

public abstract class BaseListPagePresenter extends BasePresenter{

    protected Map<String, String> customParams = new HashMap<>();

    protected int nextCursor = 0;

    public int count = 20;

    public String lastRefreshTime;

    protected abstract BaseRequest getListDataRequest(boolean isRefresh);

    /**
     * 设置nextCursor，为下一次请求的数据的位置
     */
    protected void onFinishRequest(int nextCursor){
        this.nextCursor = nextCursor;
    };

    /**
     * 修改count，
     */
    protected void onCountRequest(int count){
        this.count = count;
    };


    public void setParams(String key, String value) {
        customParams.put(key, value);
    }

    public void setParams(Map<String, String> maps) {
        customParams.putAll(maps);
    }

    public void getListData(boolean isRefresh) {
        BaseRequest request = getListDataRequest(isRefresh);
        if(request == null) {
            throw new IllegalArgumentException("request对象不能为null");
        }

        if(isRefresh) {
            nextCursor = 0;
        }

        if(!TextUtils.isEmpty(lastRefreshTime)){
            customParams.put("lastRefreshTime", lastRefreshTime);
        }
        customParams.put("count", count+"");
        customParams.put("cursor", nextCursor+"");
        request.startRequest(customParams);
//        getListDataRequest(isRefresh).startRequest(customParams);
    }

}
