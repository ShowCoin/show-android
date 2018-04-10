package one.show.live.netutil.netmanager.common;

import java.util.List;
import java.util.Map;

public abstract class BaseNetResponse<R, N extends BaseNetResponse> extends AbsNetResponse<Integer, N> {

    private R mResponse;
    private Object mExtra;
    private boolean mIsFromCache;
    private String mContent;

    //save message from server
    private String mServerMsg;
    //save message from app
    private String mLocalMsg;

    private Integer state;

    private String msg;

    @Override
    public N setFlag(Integer flag) {
        return super.setFlag(flag);
    }

    @Override
    public Integer getFlag() {
        return super.getFlag();
    }

    public R getResponse() {
        return mResponse;
    }

    @SuppressWarnings("unchecked")
    public N setResponse(R response) {
        mResponse = response;
        return (N) this;
    }

    public Object getExtra() {
        return mExtra;
    }

    @SuppressWarnings("unchecked")
    public N setExtra(Object extra) {
        mExtra = extra;
        return (N) this;
    }

    public boolean isFromCache() {
        return mIsFromCache;
    }

    @SuppressWarnings("unchecked")
    public N setFromCache(boolean fromCache) {
        mIsFromCache = fromCache;
        return (N) this;
    }

    public String getContent() {
        return mContent;
    }

    @SuppressWarnings("unchecked")
    public N setContent(String content) {
        mContent = content;
        return (N) this;
    }

    public String getServerMsg() {
        return mServerMsg;
    }

    @SuppressWarnings("unchecked")
    public N setServerMsg(String serverMsg) {
        mServerMsg = serverMsg;
        return (N) this;
    }

    public String getLocalMsg() {
        return mLocalMsg;
    }

    @SuppressWarnings("unchecked")
    public N setLocalMsg(String localMsg) {
        mLocalMsg = localMsg;
        return (N) this;
    }

    @SuppressWarnings("unchecked")
    public N setMsg(String message) {
        msg = message;
        return (N) this;
    }

    public String getMsg() {
        return msg;
    }

    @SuppressWarnings("unchecked")
    public N setState(Integer mState) {
        state = mState;
        return (N) this;
    }

    public Integer getState() {
        return state;
    }

    public abstract Map<String, List<String>> getHeaders();

    public abstract int getStatusCode();

}
