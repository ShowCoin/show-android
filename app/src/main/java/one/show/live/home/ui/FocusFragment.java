package one.show.live.home.ui;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import one.show.live.R;
import one.show.live.home.adapter.FocusAdapter;
import one.show.live.home.presenter.FocusPresenter;
import one.show.live.home.view.FocusView;
import one.show.live.po.POFocus;
import one.show.live.util.ContextUtils;
import one.show.live.common.po.POListData;
import one.show.live.common.ui.BaseFragment;
import one.show.live.common.util.ConvertToUtils;
import one.show.live.common.util.DeviceUtils;
import one.show.live.common.view.recycler.BRecyclerView;
import one.show.live.common.view.recycler.LoadingMoreFooter;
import one.show.live.common.view.recycler.SpacesItemDecoration;

/**
 * Created by liuzehua on 2018/4/8.
 * 首页关注
 */

public class FocusFragment extends BaseFragment implements FocusView{

    public final static int FOCUS = 0x1<<1;
    public final static int LATEST = 0x1<<2;

    @BindView(R.id.focus_recycle)
    BRecyclerView focusRecycle;
    Unbinder unbinder;

    FocusAdapter focusAdapter;

    FocusPresenter focusPresenter;

    int type;

    public static FocusFragment newInstance(int type) {
        FocusFragment focusFragment = new FocusFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type",type);
        focusFragment.setArguments(bundle);
        return focusFragment;
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

        type = getArguments().getInt("type");
        int num;
        if(type == FOCUS){
            num=2;
        }else{
            num=3;
        }

        focusAdapter=  new FocusAdapter(num);
        focusPresenter= new FocusPresenter(this,type);




        focusRecycle.setLayoutManager(new GridLayoutManager(activity, num))
                .setAdapter(focusAdapter)
                .setEmptyView(inflateEmptyView())
                .setPresenter(focusPresenter)
                .addItemDecoration(SpacesItemDecoration.newInstance(ConvertToUtils.dipToPX(activity, 1), 0, num))
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
    public void onRefreshComplete(boolean isSuccess, POListData<POFocus> mData) {

        if (ContextUtils.isContextAlive(this)) {
            focusRecycle.refreshComplete();
            focusAdapter.clear();
            if (isSuccess) {
                if (mData.getList() != null && mData.getList().size() > 0) {
                    focusAdapter.addAll(mData.getList());
                    if (mData.getList().size() < 10) {
                        focusRecycle.setLoadingMoreEnabled(false);
                    } else {
                        focusRecycle.setLoadingMoreEnabled(true);
                        focusRecycle.checkHasMore(mData.hasMore());
                    }

                    focusAdapter.notifyDataSetChanged();
                } else {
                    focusRecycle.setEmpty();
                    focusAdapter.notifyDataSetChanged();

                }

            } else {
                focusRecycle.setError();
            }
        }

    }

    @Override
    public void onLoadMoreComplete(boolean isSuccess, POListData<POFocus> mData) {

        if (ContextUtils.isContextAlive(this)) {
            focusRecycle.loadMoreComplete();
            if (isSuccess) {
                if (mData.getList() != null && mData.getList().size() > 0) {
                    int startPosition = focusAdapter.getItemCount();
                    int notifyCount = mData.getList().size();
                    focusAdapter.addAll(mData.getList());
                    focusAdapter.notifyItemRangeInserted(startPosition, notifyCount);
                }
                focusRecycle.checkHasMore(mData.hasMore());
            }
        }

    }
}
