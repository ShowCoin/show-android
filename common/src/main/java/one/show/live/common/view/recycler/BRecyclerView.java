package one.show.live.common.view.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;
import one.show.live.common.api.BaseRequest;
import one.show.live.common.ui.BaseListPagePresenter;
import one.show.live.common.view.RefreshHeadImage;
import one.show.live.common.common.R;

/**
 * 自定义recyclerView，封装header和footer的触发机制
 */
public class BRecyclerView extends FrameLayout implements PtrHandler {

    private Context mContext;
    public PtrFrameLayout ptrFrame;
    private RecyclerView recyclerview;
    private RelativeLayout loadingLay;

    private RecyclerView.LayoutManager mLayoutManager;
    private BaseBizAdapter mAdapter;

    private BaseListPagePresenter mPresenter;

    private boolean mIsFirstFetch = true;

    private boolean isTop = true;
    int firstVisibleItemPosition = 0;

    private boolean mIsHasRefreshAnimation = true;
    private boolean mIsRefreshable = true, mIsHasHeadView = false, mIsLoadingData = false, mHasMore = true;

    public BRecyclerView(Context context) {
        super(context);
        init(context);
    }

    public BRecyclerView(Context context, AttributeSet att) {
        super(context, att);
        init(context);
    }

    public void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(
                R.layout.recycleview_with_refresh, this);

        ptrFrame = (PtrFrameLayout) findViewById(R.id.ptr_frame);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        loadingLay = (RelativeLayout) findViewById(R.id.loading_layout);
        initView();
    }

    private void initView() {
        initPtrFrame();
        initRecyclerView();
    }


    private void initPtrFrame() {
//        final MaterialHeader header = new MaterialHeader(mContext);
//        //header.setColorSchemeColors(new int[]{R.color.line_color_run_speed_13});
//        int[] colors = getResources().getIntArray(R.array.refresh_progress_bar_colors);
//        header.setColorSchemeColors(colors);
//        header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
//        header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
//        header.setPtrFrameLayout(ptrFrame);

        RefreshHeadImage header = new RefreshHeadImage(getContext());

        ptrFrame.setDurationToCloseHeader(500);
        ptrFrame.setHeaderView(header);
        ptrFrame.addPtrUIHandler(header);
        ptrFrame.setEnabledNextPtrAtOnce(false);

        ptrFrame.setPtrHandler(this);
        ptrFrame.setEnabledNextPtrAtOnce(false);
    }

    private void initRecyclerView() {
        recyclerview.setHasFixedSize(true);
        //默认配置
        mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScrollStateChanged(recyclerView, newState);
                }

                if (recyclerview.getAdapter() != null
                        && newState == RecyclerView.SCROLL_STATE_IDLE
                        && mAdapter.getCanLoadMore()) {

                    int lastVisibleItemPosition = 0;

                    if (mLayoutManager instanceof LinearLayoutManager) {
                        lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                        //因为动态圈要时时获取item所在的位置，所以不能放在这里，这里每次点击滑动抬起只会处理一次，
//                        firstVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();

                    } else if (mLayoutManager instanceof GridLayoutManager) {
                        lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
                    } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                        int[] into = new int[((StaggeredGridLayoutManager) mLayoutManager).getSpanCount()];
                        ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(into);
                        lastVisibleItemPosition = findMax(into);
                    }

                    if (mHasMore && mLayoutManager.getChildCount() > 0
                            && lastVisibleItemPosition >= mAdapter.getItemCount() - (mPresenter.count / 2) && mAdapter.getItemCount() > mLayoutManager.getChildCount() && !mIsLoadingData) {
                        if (mAdapter.getFooter() != null && mAdapter.getFooter() instanceof FooterStateChangeListener) {
                            ((FooterStateChangeListener) mAdapter.getFooter()).setState(LoadState.STATE_LOADING);
                        }
                        fetch(false);
                    }

                }

            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mOnScrollListener != null) {
                    mOnScrollListener.onScrolled(recyclerView, dx, dy);
                }
                if (recyclerview.getAdapter() != null
                        && mAdapter.getCanLoadMore()) {

                    if (mLayoutManager instanceof LinearLayoutManager) {
                        firstVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();

                    } else if (mLayoutManager instanceof GridLayoutManager) {
                    } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
                        int[] into = new int[((StaggeredGridLayoutManager) mLayoutManager).getSpanCount()];
                        ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(into);
                    }
                }

                //判断是不是在顶部
                if (firstVisibleItemPosition == 1 || firstVisibleItemPosition == 0) {
                    isTop = true;
                } else {
                    isTop = false;
                }
            }
        });
    }



    public interface OnScrollListener {

        void onScrollStateChanged(RecyclerView recyclerView, int newState);
        void onScrolled(RecyclerView recyclerView, float dx, float dy);

    }

    OnScrollListener mOnScrollListener;


    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.mOnScrollListener = onScrollListener;
    }

    public boolean getisTop() {
        return isTop;
    }

    /**
     * 当前显示的第一个item
     *
     * @return
     */
    public int getitem() {
        return firstVisibleItemPosition;
    }


    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    public BRecyclerView disableWhenHorizontalMove() {
        ptrFrame.disableWhenHorizontalMove(true);
        return this;
    }


    /**
     * 可以不指定，默认LinearLayoutManager.VERTICAL
     * <p>
     * 如果当前指定了，那么必须放到setAdapter之前
     *
     * @param manager
     * @return
     */
    public BRecyclerView setLayoutManager(RecyclerView.LayoutManager manager) {
        mLayoutManager = manager;
        recyclerview.setLayoutManager(manager);
        return this;
    }


    public RecyclerView.LayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    /**
     * 重要方法，必须在setLayoutManager之后,必须在setHeadView,setFooterView,setLoadingMoreEnabled之前
     *
     * @param adapter
     * @return
     */
    public BRecyclerView setAdapter(BaseBizAdapter adapter) {
        this.mAdapter = adapter;
        recyclerview.setAdapter(adapter);
        return this;
    }


    /**
     * 重要方法，必须指定，否则无法请求数据
     *
     * @param mPresenter
     * @return
     */
    public BRecyclerView setPresenter(BaseListPagePresenter mPresenter) {
        this.mPresenter = mPresenter;
        return this;
    }

    /**
     * 可以不指定，间距可以通过itemview的padding或者margin来控制
     *
     * @param itemDecoration
     * @return
     */
    public BRecyclerView addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerview.addItemDecoration(itemDecoration);
        return this;
    }

    public BRecyclerView removeItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        recyclerview.removeItemDecoration(itemDecoration);
        return this;
    }

    /**
     * 是否需要支持下拉刷新
     * <p>
     * 可以不指定，默认true
     *
     * @param i
     * @return
     */
    public BRecyclerView setIsRefreshable(boolean i) {
        mIsRefreshable = i;
        return this;
    }


    public BRecyclerView setIsHashRefreshAnimation(boolean isAnimation) {
        mIsHasRefreshAnimation = isAnimation;
        return this;
    }

    /**
     * 设置头部view
     *
     * @param headView
     * @return
     */
    public BRecyclerView setHeadView(View headView) {
        if (mAdapter == null)
            throw new IllegalArgumentException("请先设置adapter");
        this.mAdapter.setHeader(headView);
        mIsHasHeadView = (headView != null);
        return this;
    }

    /**
     * 设置自动加载下一页的底部栏。可以不设置
     * <p>
     * 默认只有一个加载效果，没有其他提示，如：没有更多数据了
     *
     * @param footerView
     * @return
     */
    public BRecyclerView setFooterView(View footerView) {
        if (mAdapter == null)
            throw new IllegalArgumentException("请先设置adapter");
        this.mAdapter.setFooter(footerView);
        return this;
    }


    /**
     * 设置列表为空时的ui布局，不设置的话使用默认布局 R.layout.list_empty_view
     * @param emptyView
     * @return
     */
    public BRecyclerView setEmptyView(View emptyView) {
        if (mAdapter == null)
            throw new IllegalArgumentException("请先设置adapter");
        mAdapter.setEmptyView(emptyView);
        return this;
    }


    /**
     * 设置列表加载出错时的ui布局，不设置的话使用默认布局 R.layout.net_error
     * @param errorView
     * @return
     */
    public BRecyclerView setErrorView(View errorView) {
        if (mAdapter == null)
            throw new IllegalArgumentException("请先设置adapter");
        mAdapter.setErrorView(errorView);
        return this;
    }

    /**
     * 是否需要设置可以自动加载下一页数据。默认false
     * <p>
     * 默认不需要设置。但是在refresh完成后一定要设置，不然无法自动加载数据
     *
     * @param enabled
     * @return
     */
    public BRecyclerView setLoadingMoreEnabled(boolean enabled) {
        if (mAdapter == null)
            throw new IllegalArgumentException("请先设置adapter");

        if (mAdapter.getFooter() != null) {
            if (enabled)
                mAdapter.getFooter().setVisibility(VISIBLE);
            else
                mAdapter.getFooter().setVisibility(GONE);
        }
        mAdapter.setCanLoadMore(enabled);
        return this;
    }

    public RecyclerView getRecyclerview() {
        return recyclerview;
    }

    public void reset() {
        setHasMore(true);
        loadMoreComplete();
        refreshComplete();
    }

    /**
     * 检查在没有更多数据时，ui的响应
     *
     * @param hasMore
     */
    public void checkHasMore(boolean hasMore) {
        mIsLoadingData = false;
        setHasMore(hasMore);
    }

    public void setHasMore(boolean hasMore) {
        this.mHasMore = hasMore;
        if (mAdapter.getFooter() != null && mAdapter.getFooter() instanceof FooterStateChangeListener)
            ((FooterStateChangeListener) mAdapter.getFooter()).setState(this.mHasMore ? LoadState.STATE_COMPLETE : LoadState.STATE_NOMORE);
    }

    /**
     * 刷新完成，必须回调
     */
    public void refreshComplete() {
        mIsLoadingData = false;
        checkRecyclerUI();
        if (mIsRefreshable)
            ptrFrame.refreshComplete();
    }


    /**
     * 加载更多完成，必须回调
     */
    public void loadMoreComplete() {
        mIsLoadingData = false;
        if (mAdapter.getFooter() != null && mAdapter.getFooter() instanceof FooterStateChangeListener) {
            ((FooterStateChangeListener) mAdapter.getFooter()).setState(LoadState.STATE_COMPLETE);
        }

    }

    /**
     * 清空列表数据
     * <p>
     */
    public void setEmptyWithNoToast() {
        mAdapter.clear();
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 设置列表为空状态
     */
    public void setEmpty() {
        mAdapter.setStatus(BaseBizAdapter.STATUS_EMPTY);
        mAdapter.notifyDataSetChanged();
    }


    /**
     * 暂时保留，兼容以前
     * @param msg
     */
    public void setEmpty(String msg) {
        setEmpty();
    }


    public void setError() {
        mAdapter.setStatus(BaseBizAdapter.STATUS_ERROR);
        mAdapter.notifyDataSetChanged();
    }

    private void checkRecyclerUI() {
//        Log.e("samuel","checkRecyclerUI...");
        if (mIsFirstFetch) {
//            Log.e("samuel","checkRecyclerUI...hide");
            showRecyclerView();
            mIsFirstFetch = false;
        }
    }

    private void showLoadingLay() {
        ptrFrame.setVisibility(View.GONE);
        loadingLay.setVisibility(View.VISIBLE);
    }


    private void showRecyclerView() {
        loadingLay.setVisibility(View.GONE);
        ptrFrame.setVisibility(View.VISIBLE);
    }

    public void setFirstRequest() {
        mIsFirstFetch = true;
    }


    public void setOnItemClickListener(BaseAdapter.OnItemClickListener onItemClickListener) {
        if (mAdapter == null)
            throw new IllegalArgumentException("请先设置adapter");
        mAdapter.setOnItemClickListener(recyclerview, onItemClickListener);
    }

    public void setOnItemLongClickListener(BaseAdapter.OnItemClickListener onItemClickListener) {
        if (mAdapter == null)
            throw new IllegalArgumentException("请先设置adapter");
        mAdapter.setItemOnLongClickListener(recyclerview, onItemClickListener);
    }

    public boolean checkPositionVisible(int position) {
        return RecyclerUtils.checkPositionVisible(mLayoutManager, position);
    }


    /**
     * 触发下拉刷新或者加载更多，可以根据自己的规则获取数据的执行者
     * <p>
     * 默认为BaseListPagePresenter的请求方式
     */
    public interface CustomFetchExecutor {
        void fetch(boolean isRefresh);
    }

    private CustomFetchExecutor mExecutor;

    public void setCustomFetchExecutor(CustomFetchExecutor executor) {
        mExecutor = executor;
    }

    /**
     * 重新刷新列表
     */
    public void reFetch() {
        request(true);
    }

    /**
     * 拉取数据
     *
     * @param isRefresh 是否为刷新列表
     */
    public void fetch(boolean isRefresh) {
        if (mPresenter == null && mExecutor == null) {
            throw new IllegalArgumentException("presenter 或 CustomFetchExecutor不能为空");
        }
        if (mIsFirstFetch) {
            Log.e("samuel", "fetch showloading...");
            if (mIsHasRefreshAnimation) {
                if (mIsRefreshable) {
                    ptrFrame.post(new Runnable() {
                        @Override
                        public void run() {
                            ptrFrame.autoRefresh();
                        }
                    });
                } else {
                    showLoadingLay();
                    request(isRefresh);
                }
            } else {
                request(isRefresh);
            }
        } else {
            request(isRefresh);
        }
    }


    private void request(boolean isRefresh) {
        mAdapter.setStatus(BaseBizAdapter.STATUS_NORMAL);
        mIsLoadingData = true;
        if (mExecutor != null) {
            mExecutor.fetch(isRefresh);
        } else {
            mPresenter.getListData(isRefresh);
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//        return mIsRefreshable && !ViewCompat.canScrollVertically(recyclerview, -1);
//        if(mLayoutManager.getItemCount() == 0) return true;
//        int firstVisiblePosition = ((LinearLayoutManager)mLayoutManager).findFirstVisibleItemPosition();
//        if(firstVisiblePosition == 0) {
//            View firstVisibleView = ((LinearLayoutManager)mLayoutManager).findViewByPosition(firstVisiblePosition);
//            int top = firstVisibleView.getTop();
//            return top >= 0;
//        } else {
//            return false;
//        }

        return mIsRefreshable && PtrDefaultHandler.checkContentCanBePulledDown(frame, recyclerview, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        reFetch();
    }


}