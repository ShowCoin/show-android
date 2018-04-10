package one.show.live.netutil.netmanager.common;

import java.util.Map;

public abstract class BaseRequest<L, N extends BaseRequest> extends AbsRequest<Integer, N> {

    private String mUrl;
    private Map<String, String> mHeaders;
    private Map<String, String> mParams;
    private String mBody;

    private Object mTag;
    private Class mCls;
    private L mListener;
    private boolean mCanceled = false;
    private boolean mFinished = false;

    public String getUrl() {
        return mUrl;
    }

    @SuppressWarnings("unchecked")
    public N setUrl(String url) {
        this.mUrl = url;
        return (N) this;
    }

    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    @SuppressWarnings("unchecked")
    public N setHeaders(Map<String, String> headers) {
        this.mHeaders = headers;
        return (N) this;
    }

    public Map<String, String> getParams() {
        return mParams;
    }

    @SuppressWarnings("unchecked")
    public N setParams(Map<String, String> params) {
        this.mParams = params;
        return (N) this;
    }

    public String getBody() {
        return mBody;
    }

    @SuppressWarnings("unchecked")
    public N setBody(String body) {
        this.mBody = body;
        return (N) this;
    }

    public Object getTag() {
        return mTag;
    }

    @SuppressWarnings("unchecked")
    public N setTag(Object tag) {
        this.mTag = tag;
        return (N) this;
    }

    public Class getCls() {
        return mCls;
    }

    @SuppressWarnings("unchecked")
    public N setCls(Class cls) {
        this.mCls = cls;
        return (N) this;
    }

    public L getListener() {
        return mListener;
    }

    @SuppressWarnings("unchecked")
    public N setListener(L listener) {
        this.mListener = listener;
        return (N) this;
    }

    public boolean isCanceled() {
        return mCanceled;
    }

    public boolean isFinished() {
        return mFinished;
    }

    public void cancel() {
        mCanceled = true;
    }

    public void finish() {
        mFinished = true;
    }

}
