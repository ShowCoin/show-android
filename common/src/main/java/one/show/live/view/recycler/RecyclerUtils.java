package one.show.live.view.recycler;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import one.show.live.util.DeviceUtils;
import one.show.live.util.StringUtils;
import one.show.live.common.R;

/**
 * Created by samuel on 2017/7/5.
 */

public class RecyclerUtils {

    public static boolean checkPositionVisible(RecyclerView.LayoutManager layoutManager, int position) {
        int firstItemPosition = 0;
        int lastItemPosition = 0;

        if (layoutManager instanceof LinearLayoutManager) {
            firstItemPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            lastItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager) {
            firstItemPosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
            lastItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {

            int[] firstVisibleItems = null;
            firstVisibleItems = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(firstVisibleItems);
            if (firstVisibleItems != null && firstVisibleItems.length > 0) {
                firstItemPosition = firstVisibleItems[0];
            }


            int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
            ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
            lastItemPosition = findMax(into);
        }

        if (position >= firstItemPosition && position <= lastItemPosition)
            return true;
        return false;

    }

    private static int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 计算RecyclerView滑动的距离  没有header的,并且一行有两条数据
     *
     * @param recyclerview
     * @return
     */
    public static int getScollYDistance(RecyclerView recyclerview, Context context) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerview.getLayoutManager();
        int position = layoutManager.findFirstVisibleItemPosition();
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        int itemHeight = firstVisiableChildView.getHeight();
        if (position == 0) {
            return (position) * itemHeight - firstVisiableChildView.getTop();
        } else {
            return (position / 2) * itemHeight - firstVisiableChildView.getTop() + (DeviceUtils.dipToPX(context, 6) * (position / 2));//加上一个间隔的高度
        }

    }

    /**
     * 计算RecyclerView滑动的距离
     *
     * @param hasHead      是否有头部
     * @param headerHeight RecyclerView的头部高度
     * @return 滑动的距离
     */
    public static int getScollYHeight(RecyclerView recyclerview, boolean hasHead, int headerHeight) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerview.getLayoutManager();
        //获取到第一个可见的position,其添加的头部不算其position当中
        int position = layoutManager.findFirstVisibleItemPosition();
        //通过position获取其管理器中的视图
        View firstVisiableChildView = layoutManager.findViewByPosition(position);
        //获取自身的高度
        int itemHeight = 0;
        int firstVisiableChildViewTop = 0;
        if (firstVisiableChildView != null) {
            itemHeight = firstVisiableChildView.getHeight();
            firstVisiableChildViewTop = firstVisiableChildView.getTop();
        }
        //有头部
        if (hasHead) {

            if (position == 0) {
                return itemHeight * position - firstVisiableChildViewTop;
            } else {
                return headerHeight + itemHeight * (position - 1) - firstVisiableChildViewTop;
            }

        } else {
            return itemHeight * position - firstVisiableChildViewTop;
        }
    }


    public static View inflateEmptyView(Context context) {
        return inflateEmptyView(context, null);
    }

    public static View inflateEmptyView(Context context, String text) {
        View emptyView = LayoutInflater.from(context).inflate(R.layout.list_empty_view, null);
        TextView emptyview_text = (TextView) emptyView.findViewById(R.id.emptyview_text);
        if (StringUtils.isNotEmpty(text)) {
            emptyview_text.setText(text);
        }
        return emptyView;
    }
}
