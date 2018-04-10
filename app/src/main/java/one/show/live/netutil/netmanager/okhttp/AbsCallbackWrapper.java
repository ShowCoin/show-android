package one.show.live.netutil.netmanager.okhttp;


import one.show.live.netutil.okhttputils.callback.AbsCallback;

public abstract class AbsCallbackWrapper<T, F, L> extends AbsCallback<T> {

    private F mFlag;
    private L mListener;

    public AbsCallbackWrapper(L listener, F flag) {
        this.mListener = listener;
        this.mFlag = flag;
    }

    public L getListener() {
        if (mListener != null) {
            return mListener;
        }
        return null;
    }

    public F getFlag() {
        return mFlag;
    }

}
