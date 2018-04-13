package one.show.live.common.ui;

public interface BaseListPageView<T> {

    /**
     * 刷新操作完成回调
     * @param isSuccess
     * @param t
     */
    public void onRefreshComplete(boolean isSuccess,T t);

    /**
     * 加载更多完成回调
     * @param isSuccess
     * @param t
     */
    public void onLoadMoreComplete(boolean isSuccess,T t);

}
