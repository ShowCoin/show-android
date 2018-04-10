package one.show.live.netutil.netmanager.common;


public abstract class AbsRequest<F, N extends AbsRequest> {

    private F mFlag;
    private int mMethod;
//    private NetPolicy mNetPolicy;

    public F getFlag() {
        return mFlag;
    }

    @SuppressWarnings("unchecked")
    public N setFlag(F flag) {
        this.mFlag = flag;
        return (N) this;
    }

    public int getMethod() {
        return mMethod;
    }

    @SuppressWarnings("unchecked")
    public N setMethod(int method) {
        this.mMethod = method;
        return (N) this;
    }

//    public NetPolicy getNetPolicy() {
//        return mNetPolicy;
//    }
//
//    @SuppressWarnings("unchecked")
//    public N setNetPolicy(NetPolicy netPolicy) {
//        this.mNetPolicy = netPolicy;
//        return (N) this;
//    }
}
