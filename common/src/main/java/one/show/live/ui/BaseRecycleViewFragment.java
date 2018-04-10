package one.show.live.ui;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import one.show.live.view.recycler.BRecyclerView;


/**
 * 当前页面只有一个recyclerView的情况
 */
public abstract class BaseRecycleViewFragment extends BaseFragment {

    protected BRecyclerView mListView;


    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        mInflater = inflater;
        hideSoftInput();
        mListView = new BRecyclerView(getContext());
        initRecyclerView();
        return mListView;
    }

    @Override
    protected int getContentView() {
        return 0;
    }

    protected abstract void initRecyclerView();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mListView != null) mListView.fetch(true);
    }


}
