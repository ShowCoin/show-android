package one.show.live.personal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import one.show.live.R;
import one.show.live.api.BaseAPI;
import one.show.live.common.po.POListData;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.view.recycler.BRecyclerView;
import one.show.live.common.view.recycler.DividerDecoration;
import one.show.live.common.view.recycler.LoadingMoreFooter;
import one.show.live.personal.adapter.FansAdapter;
import one.show.live.personal.presenter.FansPresenter;
import one.show.live.personal.view.FansView;
import one.show.live.po.POFansList;
import one.show.live.po.eventbus.FocusOnEventBean;
import one.show.live.util.ContextUtils;
import one.show.live.widget.TitleView;

/**
 * Created by Nano on 2018/4/19.
 */

public class FansActivity extends BaseFragmentActivity implements FansView {
    @BindView(R.id.fans_title)
    TitleView fansTitle;
    @BindView(R.id.fans_recycle)
    BRecyclerView fansRecycle;

    FansAdapter fansAdapter;
    FansPresenter fansPresenter;

    int type;
    String uid;

    public static final int FANS_VIEW = 0x1 << 1;
    public static final int FOCUS_VIEW = 0x1 << 2;

    LoadingMoreFooter showLoadingMoreFooter;

    public static Intent getCallingIntent(Context context, int type,String uid) {

        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putString("uid", uid);

        Intent intent = new Intent(context, FansActivity.class);
        intent.putExtras(bundle);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);
        EventBus.getDefault().register(this);
        initBaseView();
    }

    private void initBaseView() {

        initStatusBarForLightTitle(ContextCompat.getColor(this, R.color.color_ffffff), fansTitle);

        type = getIntent().getExtras().getInt("type");
        uid = getIntent().getExtras().getString("uid");

        if(type == FANS_VIEW){
            fansTitle.setTitle(getString(R.string.fans))
                    .setTextColor(ContextCompat.getColor(this, R.color.color_333333));
            fansPresenter = new FansPresenter(this,BaseAPI.Path.user_fans_list);//粉丝

        }else{
            fansTitle.setTitle(getString(R.string.focus))
                    .setTextColor(ContextCompat.getColor(this, R.color.color_333333));
            fansPresenter = new FansPresenter(this,BaseAPI.Path.user_follow_list);//关注

        }

        fansTitle.setLayBac(R.color.color_ffffff);
        fansTitle.setLeftImage(R.drawable.back_black);

        fansPresenter.setParams("uid",uid);

        fansAdapter = new FansAdapter(type);
        showLoadingMoreFooter = new LoadingMoreFooter(this);

        fansRecycle.setAdapter(fansAdapter)
                .setPresenter(fansPresenter)
                .addItemDecoration(new DividerDecoration(this, R.drawable.shape_divider))
                .setFooterView(showLoadingMoreFooter);
        fansRecycle.fetch(true);
    }

    @Override
    public void onRefreshComplete(boolean isSuccess, POListData<POFansList> mData) {
        if (ContextUtils.isContextAlive(this)) {
            fansRecycle.refreshComplete();
            fansAdapter.clear();
            if (isSuccess) {
                if (mData.getList() != null && mData.getList().size() > 0) {
                    fansAdapter.addAll(mData.getList());
                    if (mData.getList().size() < 10) {
                        fansRecycle.setLoadingMoreEnabled(false);
                    } else {
                        fansRecycle.setLoadingMoreEnabled(true);
                        fansRecycle.checkHasMore(mData.hasMore());
                    }

                    fansAdapter.notifyDataSetChanged();
                } else {
                    fansRecycle.setEmpty();
                    fansAdapter.notifyDataSetChanged();

                }

            } else {
                fansRecycle.setError();
            }
        }
    }

    @Override
    public void onLoadMoreComplete(boolean isSuccess, POListData<POFansList> mData) {
        if (ContextUtils.isContextAlive(this)) {
            fansRecycle.loadMoreComplete();
            if (isSuccess) {
                if (mData.getList() != null && mData.getList().size() > 0) {
                    int startPosition = fansAdapter.getItemCount();
                    int notifyCount = mData.getList().size();
                    fansAdapter.addAll(mData.getList());
                    fansAdapter.notifyItemRangeInserted(startPosition, notifyCount);
                }
                fansRecycle.checkHasMore(mData.hasMore());
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FocusOnEventBean event) { //接收关注状态的
        if (event != null && event.isFocuson()) {
//            if (event.getType() == FocusOnEventBean.FOCUS_ON) {//关注成功
//                meTopHeader.setFocusStatus(true);
//            } else if (event.getType() == FocusOnEventBean.UN_FOCUS_ON) {//取消关注成功
//                meTopHeader.setFocusStatus(false);
//            }
            POFansList poFansList = new POFansList();
            poFansList.setUid(event.getUid());
           int ii =  fansAdapter.getItems().indexOf(poFansList);
            if(ii!=-1){
                if(event.getType() == FocusOnEventBean.FOCUS_ON){
                    fansAdapter.getItem(ii).setIsFollowed(1);
                }else{
                    fansAdapter.getItem(ii).setIsFollowed(0);
                }
                fansAdapter.notifyDataSetChanged();
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
