package one.show.live.netutil.lsn;


public interface OnResponseListener {
    void onComplete(Object result, int code, String msg);

    void onInternError(int errorCode, String errorMessage);
}

