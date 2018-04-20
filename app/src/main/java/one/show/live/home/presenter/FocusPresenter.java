package one.show.live.home.presenter;

import one.show.live.home.mode.FocusRequest;
import one.show.live.home.view.FocusView;
import one.show.live.common.api.BaseRequest;
import one.show.live.common.po.POListData;
import one.show.live.common.ui.BaseListPagePresenter;

/**
 * 关注
 */
public class FocusPresenter extends BaseListPagePresenter {

    FocusView view;
    int type;

    public FocusPresenter(FocusView view,int type) {
        this.view = view;
        this. type = type;
    }


    @Override
    protected BaseRequest getListDataRequest(final boolean isRefresh) {
        return new FocusRequest(type) {
            @Override
            public void onFinish(boolean isSuccess, String msg, POListData<POFocus> data) {
                if (isSuccess) {
                    onFinishRequest(data.getNext_cursor());
                }
                if(isRefresh){
                    view.onRefreshComplete(isSuccess,data);
                }else{
                    view.onLoadMoreComplete(isSuccess,data);
                }
            }
        };
    }
}
