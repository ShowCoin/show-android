package one.show.live.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nano on 2017/3/30.
 */

public class MyFlowLayout extends ViewGroup {
    private List<int[]> children;

    public MyFlowLayout(Context context) {
        super(context);
        children = new ArrayList<int[]>();
    }

    public MyFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        children = new ArrayList<int[]>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        final int count = getChildCount(); // tag的数量
        int left = 0; // 当前的左边距离
        int top = 0; // 当前的上边距离
        int beforeBottom = 0;//上一个控件的下边距.
        int totalHeight = 0; // WRAP_CONTENT时控件总高度
        int totalWidth = 0; // WRAP_CONTENT时控件总宽度
        boolean isHH = false;//是否换行

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
//当前child的高度
            int curH = params.topMargin + child.getMeasuredHeight() + params.bottomMargin;
            if (i == 0) { // 第一行的高度
                totalHeight = curH;
            }
// 换行
            if (left + params.leftMargin + child.getMeasuredWidth() + params.rightMargin > getMeasuredWidth()) {
                left = 0;
// 每个TextView的高度都一样，随便取一个都行
                isHH = true;
                top = beforeBottom;//将上一个控件的底部位置赋值当前控件的顶部
                totalHeight = top + curH;
            }
//取当前高度跟之前值中的最大值
            beforeBottom = Math.max(curH, beforeBottom) + ((isHH) ? curH : 0);
            isHH = false;

            children.add(new int[]{
                    left + params.leftMargin,
                    top + params.topMargin,
                    left + params.leftMargin + child.getMeasuredWidth(),
                    top + params.topMargin + child.getMeasuredHeight()});

            left += params.leftMargin + child.getMeasuredWidth() + params.rightMargin;

            if (left > totalWidth) { // 当宽度为WRAP_CONTENT时，取宽度最大的一行
                totalWidth = left;
            }
        }

        int height = 0;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = totalHeight;
        }

        int width = 0;
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = totalWidth;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            int[] position = children.get(i);
            child.layout(position[0], position[1], position[2], position[3]);
        }
    }

}
