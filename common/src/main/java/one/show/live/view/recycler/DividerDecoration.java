package one.show.live.view.recycler;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class DividerDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public DividerDecoration(Context context) {
        final TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    public DividerDecoration(Context context, int resId) {
        mDivider = ContextCompat.getDrawable(context, resId);
    }

    public DividerDecoration(Drawable mDivider) {
        this.mDivider = mDivider;
    }

    private int getOrientation(RecyclerView parent) {
        LinearLayoutManager layoutManager;
        try {
            layoutManager = (LinearLayoutManager) parent.getLayoutManager();
        } catch (ClassCastException e) {
            throw new IllegalStateException("DividerDecoration can only be used with a " +
                    "LinearLayoutManager.", e);
        }
        return layoutManager.getOrientation();
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            drawVertical(c, parent);
        } else {
            drawHorizontal(c, parent);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int recyclerViewTop = parent.getPaddingTop();
        final int recyclerViewBottom = parent.getHeight() - parent.getPaddingBottom();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = Math.max(recyclerViewTop, child.getBottom() + params.bottomMargin);
            final int bottom = Math.min(recyclerViewBottom, top + mDivider.getIntrinsicHeight());
            if(childCount==1&&bottom==recyclerViewBottom){//此时可能是空页面或者网络错误页面。不需要绘制分割线(不排除可能以后某些情况，一个view的高度充满整个屏幕，如果真的是这种情况，这里需要换其他方案处理)
                continue;
            }else{
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }

        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop();
        final int bottom = parent.getHeight() - parent.getPaddingBottom();
        final int recyclerViewLeft = parent.getPaddingLeft();
        final int recyclerViewRight = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = Math.max(recyclerViewLeft, child.getRight() + params.rightMargin);
            final int right = Math.min(recyclerViewRight, left + mDivider.getIntrinsicHeight());
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (getOrientation(parent) == LinearLayoutManager.VERTICAL) {
            outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
        } else {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        }
    }
}