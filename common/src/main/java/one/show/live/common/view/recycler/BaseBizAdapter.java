package one.show.live.common.view.recycler;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import one.show.live.common.common.R;


/**
 * 可以设置头部view和加载更多功能的Adapter封装
 * <p/>
 * 满足大部分使用情况都用此adapter
 *
 * @param <T> 业务实体
 * @param <H> 自定义ViewHolder
 */
public abstract class BaseBizAdapter<T, H extends RecyclerView.ViewHolder>
        extends BaseAdapter<T, RecyclerView.ViewHolder> {

    private View header;
    private View footer;

    private View emptyView;
    private View errorView;
    public boolean isCanLoadMore = false;

    public static final int STATUS_NORMAL = 0;
    public static final int STATUS_EMPTY = 1;
    public static final int STATUS_ERROR = 2;

    private int status = STATUS_NORMAL;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return this.status;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == ItemType.TYPE_HEADER.ordinal()) {
            return new SimpleHolder(header);
        } else if (viewType == ItemType.TYPE_FOOTER.ordinal()) {
            return new SimpleHolder(footer);
        } else if (viewType == ItemType.TYPE_EMPTY.ordinal()) {
            return new SimpleHolder(emptyView != null ? emptyView : LayoutInflater.from(parent.getContext()).inflate(R.layout.list_empty_view, parent, false));
        } else if (viewType == ItemType.TYPE_ERROR.ordinal()) {
            return new SimpleHolder(errorView != null ? errorView : LayoutInflater.from(parent.getContext()).inflate(R.layout.net_error, parent, false));
        } else {
            SimpleHolder holder = (SimpleHolder) onCreateItemViewHolder(parent);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        runEnterAnimation(holder.itemView, position);
        if (status == STATUS_EMPTY || status == STATUS_ERROR)
            return;

        if (getItemViewType(position) == ItemType.TYPE_HEADER.ordinal())
            return;

        if (getItemViewType(position) == ItemType.TYPE_FOOTER.ordinal())
            return;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemClick(v, position);
                    return true;
                }
                return false;
            }
        });


        onBindItemViewHolder((H) holder, position);
    }

    public boolean isHeader(int position) {
        return position == 0 && header != null;
    }


    public boolean isFooter(int position) {
        return position < getItemCount() && position >= getItemCount() - (isCanLoadMore ? 1 : 0);
    }

    private int lastAnimatedPosition = -1;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = false;

    //item加载动画
    private void runEnterAnimation(View view, int position) {
        if (getItemViewType(position) != ItemType.TYPE_CONTENT.ordinal()) {
            return;
        }

//        if (animationsLocked) return;//animationsLocked是布尔类型变量，一开始为false，确保仅屏幕一开始能够显示的item项才开启动画
        if (view == null) {
            return;
        }

//        if (position > lastAnimatedPosition) {//lastAnimatedPosition是int类型变量，一开始为-1，这两行代码确保了recycleview滚动式回收利用视图时不会出现不连续的效果
        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            if (lastAnimatedPosition < 4) {
                return;
            }
        }
        view.clearAnimation();
        view.setTranslationY(0);//相对于原始位置下方500
        view.setAlpha(0.2f);//完全透明
        //每个item项两个动画，从透明到不透明，从下方移动到原来的位置
        //并且根据item的位置设置延迟的时间，达到一个接着一个的效果
        view.animate()
                .translationY(0).alpha(1.f)//设置最终效果为完全不透明，并且在原来的位置
                .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)//根据item的位置设置延迟时间，达到依次动画一个接一个进行的效果
                .setInterpolator(new DecelerateInterpolator(0.5f))//设置动画效果为在动画开始的地方快然后慢
                .setDuration(500)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animationsLocked = true;//确保仅屏幕一开始能够显示的item项才开启动画，也就是说屏幕下方还没有显示的item项滑动时是没有动画效果
                    }
                })
                .start();
//        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (status == STATUS_EMPTY || status == STATUS_ERROR)
                        return gridManager.getSpanCount();

                    return (isHeader(position) || isFooter(position))
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && (isHeader(holder.getLayoutPosition()) || isFooter(holder.getLayoutPosition()))) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (isHeader(position)) {
            return ItemType.TYPE_HEADER.ordinal();
        } else if (isFooter(position)) {
            return ItemType.TYPE_FOOTER.ordinal();
        } else {
            switch (status) {
                case STATUS_EMPTY:
                    return ItemType.TYPE_EMPTY.ordinal();
                case STATUS_ERROR:
                    return ItemType.TYPE_ERROR.ordinal();
                case STATUS_NORMAL:
                default:
                    return ItemType.TYPE_CONTENT.ordinal();

            }

        }

    }

    @Override
    public int getItemCount() {

        int size = (status == STATUS_EMPTY || status == STATUS_ERROR) ? 1 : size();
        if (header != null)
            size++;
        if (isCanLoadMore)
            size++;

        return size;
    }

    public T getItem(int position) {
        return items.get(getRealPosition(position));
    }

    public T getItemByRealPosition(int realPosition) {
        return items.get(realPosition);
    }

    public int getAdapterPositionByRealPosition(int realPosition) {
        if (header != null){
            return realPosition + 1;
        }
        return realPosition;
    }

    public int getRealPosition(int position) {
        return header != null ? position - 1 : position;
    }

    public void setHeader(View header) {
        this.header = header;
    }

    public View getHeader() {
        return header;
    }

    public View getEmptyView() {
        return emptyView;
    }

    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
    }

    public View getErrorView() {
        return errorView;
    }

    public void setErrorView(View errorView) {
        this.errorView = errorView;
    }

    public void setFooter(View footer) {
        this.footer = footer;
    }

    public View getFooter() {
        return footer;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.isCanLoadMore = canLoadMore;
    }


    public boolean getCanLoadMore() {
        return isCanLoadMore;
    }

    public abstract H onCreateItemViewHolder(ViewGroup parent);

    public abstract void onBindItemViewHolder(H holder, int position);
}
