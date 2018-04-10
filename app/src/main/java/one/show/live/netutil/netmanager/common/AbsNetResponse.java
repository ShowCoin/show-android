package one.show.live.netutil.netmanager.common;


public abstract class AbsNetResponse<F, N extends AbsNetResponse> {

    private Exception mException;
    private String mMessage;

    private F mFlag;

    public F getFlag() {
        return this.mFlag;
    }

    @SuppressWarnings("unchecked")
    public N setFlag(F flag) {
        this.mFlag = flag;
        return (N) this;
    }

    public Exception getException() {
        return mException;
    }

    @SuppressWarnings("unchecked")
    public N setException(Exception exception) {
        mException = exception;
        return (N) this;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }

}
