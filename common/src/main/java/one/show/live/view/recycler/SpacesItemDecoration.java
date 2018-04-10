package one.show.live.view.recycler;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * @author samuel
 *
 * modify by
 *
 * https://github.com/ataulm/rv-tools/blob/dev/core/src/main/java/com/ataulm/rv/SpacesItemDecoration.java
 */
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private final int itemSplitMarginEven;
    private final int itemSplitMarginLarge;
    private final int itemSplitMarginSmall;

    private final int verticalSpacing;

    private int leftMargin;

    private int rightMargin;



    public static SpacesItemDecoration newLinearInstance(int spacing){
        return newInstance(0,spacing,0,0,1);
    }

    public static SpacesItemDecoration newInstance(int spacing,int spanCount) {
        return newInstance(spacing,spacing,0,0,spanCount);
    }

    public static SpacesItemDecoration newInstance(int spacing,int margin,int spanCount) {
        return newInstance(spacing,spacing,margin,margin,spanCount);
    }

    /**
     * 设置间隔参数
     *
     * @param horizontalSpacing 横向间隔(item中间的间隔)
     * @param verticalSpacing 纵向间隔
     * @param leftMargin 布局的左间距 default：0
     * @param rightMargin 布局的右间距 default：0
     * @param spanCount 每行的item个数
     * @return
     */
    public static SpacesItemDecoration newInstance(int horizontalSpacing, int verticalSpacing,int leftMargin,int rightMargin, int spanCount) {
        int maxNumberOfSpaces = spanCount - 1;
        int totalSpaceToSplitBetweenItems = maxNumberOfSpaces * horizontalSpacing;

        int itemSplitMarginEven = (int) (0.5f * horizontalSpacing);
        int itemSplitMarginLarge = totalSpaceToSplitBetweenItems / spanCount;
        int itemSplitMarginSmall = horizontalSpacing - itemSplitMarginLarge;

        return new SpacesItemDecoration(itemSplitMarginEven, itemSplitMarginLarge, itemSplitMarginSmall, verticalSpacing,leftMargin,rightMargin);
    }

    private SpacesItemDecoration(int itemSplitMarginEven, int itemSplitMarginLarge, int itemSplitMarginSmall, int verticalSpacing,int leftMargin,int rightMargin) {
        this.itemSplitMarginEven = itemSplitMarginEven;
        this.itemSplitMarginLarge = itemSplitMarginLarge;
        this.itemSplitMarginSmall = itemSplitMarginSmall;
        this.verticalSpacing = verticalSpacing;
        this.leftMargin = leftMargin;
        this.rightMargin = rightMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int itemPosition = layoutParams.getViewAdapterPosition();
        int childCount = parent.getAdapter().getItemCount();

        SpanLookup spanLookup = getSpanLookup(view, parent);
        applyItemHorizontalOffsets(spanLookup, itemPosition, outRect);
        applyItemVerticalOffsets(outRect, itemPosition, childCount, spanLookup.getSpanCount(), spanLookup);
    }

    protected SpanLookup getSpanLookup(View view, RecyclerView parent) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return SpanLookupFactory.gridLayoutSpanLookup((GridLayoutManager) layoutManager);
        }
        return SpanLookupFactory.singleSpan();
    }

    private void applyItemVerticalOffsets(Rect outRect, int itemPosition, int childCount, int spanCount, SpanLookup spanLookup) {
        outRect.top = getItemTopSpacing(spanLookup, verticalSpacing, itemPosition, spanCount, childCount);
        outRect.bottom = getItemBottomSpacing(spanLookup, verticalSpacing, itemPosition, childCount);
    }

    private void applyItemHorizontalOffsets(SpanLookup spanLookup, int itemPosition, Rect offsets) {
        //header or footer都默认设置为0了，如果需要有间距，自己在view里面控制最好。这里不区分header和footer
        if (itemIsFullSpan(spanLookup, itemPosition)) {
            offsets.left = 0;
            offsets.right = 0;
            return;
        }

        if (itemStartsAtTheLeftEdge(spanLookup, itemPosition)) {
            offsets.left = leftMargin;
            offsets.right = itemSplitMarginLarge;
            return;
        }

        if (itemEndsAtTheRightEdge(spanLookup, itemPosition)) {
            offsets.left = itemSplitMarginLarge;
            offsets.right = rightMargin;
            return;
        }

        if (itemIsNextToAnItemThatStartsOnTheLeftEdge(spanLookup, itemPosition)) {
            offsets.left = itemSplitMarginSmall;
        } else {
            offsets.left = itemSplitMarginEven;
        }

        if (itemIsNextToAnItemThatEndsOnTheRightEdge(spanLookup, itemPosition)) {
            offsets.right = itemSplitMarginSmall;
        } else {
            offsets.right = itemSplitMarginEven;
        }
    }

    private static boolean itemIsNextToAnItemThatStartsOnTheLeftEdge(SpanLookup spanLookup, int itemPosition) {
        return !itemStartsAtTheLeftEdge(spanLookup, itemPosition) && itemStartsAtTheLeftEdge(spanLookup, itemPosition - 1);
    }

    private static boolean itemIsNextToAnItemThatEndsOnTheRightEdge(SpanLookup spanLookup, int itemPosition) {
        return !itemEndsAtTheRightEdge(spanLookup, itemPosition) && itemEndsAtTheRightEdge(spanLookup, itemPosition + 1);
    }

    private static boolean itemIsFullSpan(SpanLookup spanLookup, int itemPosition) {
        return itemStartsAtTheLeftEdge(spanLookup, itemPosition) && itemEndsAtTheRightEdge(spanLookup, itemPosition);
    }

    private static boolean itemStartsAtTheLeftEdge(SpanLookup spanLookup, int itemPosition) {
        return spanLookup.getSpanIndex(itemPosition) == 0;
    }

    private static boolean itemEndsAtTheRightEdge(SpanLookup spanLookup, int itemPosition) {
        return spanLookup.getSpanIndex(itemPosition) + spanLookup.getSpanSize(itemPosition) == spanLookup.getSpanCount();
    }

    private static int getItemTopSpacing(SpanLookup spanLookup, int verticalSpacing, int itemPosition, int spanCount, int childCount) {
        if (itemIsOnTheTopRow(spanLookup, itemPosition, spanCount, childCount)) {
            return 0;
        } else {
            return (int) (.5f * verticalSpacing);
        }
    }

    private static boolean itemIsOnTheTopRow(SpanLookup spanLookup, int itemPosition, int spanCount, int childCount) {
        int latestCheckedPosition = 0;
        for (int i = 0; i < childCount; i++) {
            latestCheckedPosition = i;
            int spanEndIndex = spanLookup.getSpanIndex(i) + spanLookup.getSpanSize(i) - 1;
            if (spanEndIndex == spanCount - 1) {
                break;
            }
        }
        return itemPosition <= latestCheckedPosition;
    }

    private static int getItemBottomSpacing(SpanLookup spanLookup, int verticalSpacing, int itemPosition, int childCount) {
        if (itemIsOnTheBottomRow(spanLookup, itemPosition, childCount)) {
            return 0;
        } else {
            return (int) (.5f * verticalSpacing);
        }
    }

    private static boolean itemIsOnTheBottomRow(SpanLookup spanLookup, int itemPosition, int childCount) {
        int latestCheckedPosition = 0;
        for (int i = childCount - 1; i >= 0; i--) {
            latestCheckedPosition = i;
            int spanIndex = spanLookup.getSpanIndex(i);
            if (spanIndex == 0) {
                break;
            }
        }
        return itemPosition >= latestCheckedPosition;
    }

}