package one.show.live.netutil.netmanager.common;


public interface NetResponseListener {

    /**
     *  run on main thread
     */
    void onNetResponse(BaseNetResponse response);

    /**
     * run on main thread
     */
    void onNetError(BaseNetResponse response);

}
