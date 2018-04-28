package one.show.live.media.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import one.show.live.R;
import one.show.live.common.po.POListData;
import one.show.live.common.ui.BaseFragment;
import one.show.live.common.util.ConvertToUtils;
import one.show.live.common.util.DeviceUtils;
import one.show.live.common.view.recycler.BRecyclerView;
import one.show.live.common.view.recycler.LoadingMoreFooter;
import one.show.live.common.view.recycler.SpacesItemDecoration;
import one.show.live.media.adapter.DayListAdapter;
import one.show.live.media.presenter.DayListPresenter;
import one.show.live.media.view.DayListView;
import one.show.live.po.PODayList;
import one.show.live.util.ContextUtils;

/**
 * Created by Nano on 2018/4/14.
 *
 * 日榜页面，周榜  总榜  都是一个页面
 */

public class DayListFragment extends BaseFragment implements DayListView{

    @BindView(R.id.focus_recycle)
    BRecyclerView focusRecycle;
    Unbinder unbinder;

    DayListAdapter dayListAdapter;
    DayListPresenter dayListPresenter;

    public static DayListFragment newInstance() {
        DayListFragment dayListFragment = new DayListFragment();
        return dayListFragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_focus;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected void initView() {
        super.initView();

        dayListAdapter = new DayListAdapter(1);
        dayListPresenter = new DayListPresenter(this,1);



        focusRecycle.setLayoutManager(new GridLayoutManager(activity, 1))
                .setAdapter(dayListAdapter)
                .setEmptyView(inflateEmptyView())
                .setPresenter(dayListPresenter)
                .addItemDecoration(SpacesItemDecoration.newInstance(0,ConvertToUtils.dipToPX(activity, 1), 1))
                .setFooterView(new LoadingMoreFooter(getContext()));
        focusRecycle.getRecyclerview().setOverScrollMode(View.OVER_SCROLL_NEVER);
        focusRecycle.fetch(true);
    }

    private View inflateEmptyView() {
        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.view_home_empty, null);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                DeviceUtils.getScreenHeight(getContext()) - DeviceUtils.getStatusBarHeight(getContext()) - DeviceUtils.dipToPX(getContext(), 110 + 50 + 47 + 45 + 2));
        emptyView.setLayoutParams(params);
        return emptyView;
    }

    @Override
    public void onRefreshComplete(boolean isSuccess, POListData<PODayList> mData) {
        if (ContextUtils.isContextAlive(this)) {
            focusRecycle.refreshComplete();
            dayListAdapter.clear();
            if (isSuccess) {
                if (mData.getList() != null && mData.getList().size() > 0) {
                    dayListAdapter.addAll(mData.getList());
                    if (mData.getList().size() < 10) {
                        focusRecycle.setLoadingMoreEnabled(false);
                    } else {
                        focusRecycle.setLoadingMoreEnabled(true);
                        focusRecycle.checkHasMore(mData.hasMore());
                    }

                    dayListAdapter.notifyDataSetChanged();
                } else {
                    focusRecycle.setEmpty();
                    dayListAdapter.notifyDataSetChanged();

                }

            } else {
                focusRecycle.setError();
            }
        }
    }

    @Override
    public void onLoadMoreComplete(boolean isSuccess, POListData<PODayList> mData) {
        if (ContextUtils.isContextAlive(this)) {
            focusRecycle.loadMoreComplete();
            if (isSuccess) {
                if (mData.getList() != null && mData.getList().size() > 0) {
                    int startPosition = dayListAdapter.getItemCount();
                    int notifyCount = mData.getList().size();
                    dayListAdapter.addAll(mData.getList());
                    dayListAdapter.notifyItemRangeInserted(startPosition, notifyCount);
                }
                focusRecycle.checkHasMore(mData.hasMore());
            }
        }
    }
}
