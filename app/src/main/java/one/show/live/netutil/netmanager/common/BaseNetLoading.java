package one.show.live.netutil.netmanager.common;


public abstract class BaseNetLoading<N extends BaseNetLoading> extends AbsNetResponse<Integer, N> {

    private long mCurrentSize;
    private long mTotalSize;
    private float mProgress;
    private long mNetworkSpeed;

    @Override
    public N setFlag(Integer flag) {
        return super.setFlag(flag);
    }

    @Override
    public Integer getFlag() {
        return super.getFlag();
    }

    public long getCurrentSize() {
        return mCurrentSize;
    }

    @SuppressWarnings("unchecked")
    public N setCurrentSize(long currentSize) {
        mCurrentSize = currentSize;
        return (N) this;
    }

    public long getTotalSize() {
        return mTotalSize;
    }

    @SuppressWarnings("unchecked")
    public N setTotalSize(long totalSize) {
        mTotalSize = totalSize;
        return (N) this;
    }

    public float getProgress() {
        return mProgress;
    }

    @SuppressWarnings("unchecked")
    public N setProgress(float progress) {
        mProgress = progress;
        return (N) this;
    }

    public long getNetworkSpeed() {
        return mNetworkSpeed;
    }

    @SuppressWarnings("unchecked")
    public N setNetworkSpeed(long networkSpeed) {
        mNetworkSpeed = networkSpeed;
        return (N) this;
    }

}
