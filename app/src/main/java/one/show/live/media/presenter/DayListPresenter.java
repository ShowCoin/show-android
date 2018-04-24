package one.show.live.media.presenter;

import one.show.live.common.api.BaseRequest;
import one.show.live.common.po.POListData;
import one.show.live.common.ui.BaseListPagePresenter;
import one.show.live.media.model.DayListRequest;
import one.show.live.media.view.DayListView;
import one.show.live.po.PODayList;

/**
 * 榜单
 */
public class DayListPresenter extends BaseListPagePresenter {

    DayListView view;
    int type;

    public DayListPresenter(DayListView view, int type) {
        this.view = view;
        this. type = type;
    }


    @Override
    protected BaseRequest getListDataRequest(final boolean isRefresh) {
        return new DayListRequest(type) {
            @Override
            public void onFinish(boolean isSuccess, String msg, POListData<PODayList> data) {
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
