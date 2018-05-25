package one.show.live.message.presenter;

import one.show.live.common.ui.BasePresenter;
import one.show.live.message.adapter.ContractAdapter;
import one.show.live.message.binder.IContractBinder;
import one.show.live.message.view.IContractView;

/**
 * Created by clarkM1ss1on on 2018/5/4
 */
public class ContractPresenter<V extends IContractView> extends BasePresenter implements IContractBinder {

    private ContractAdapter adapter;

    private V mView;

    public ContractPresenter(V mView) {
        this.mView = mView;
        adapter = new ContractAdapter(this);
    }

    public void requestToGetContract() {

    }
}
