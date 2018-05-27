package com.dreamer.tv.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.widget.GridView;

import com.dreamer.tv.R;

public class MyGridView extends GridView {
    private int rownum;

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mExpandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, mExpandSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int colnum = getNumColumns(); //获取列数
        int total = getChildCount();  //获取Item总数
        //计算行数
        if (total % colnum == 0) {
            rownum = total / colnum;
        } else {
            rownum = (total / colnum) + 1; //当余数不为0时，要把结果加上1
        }
        Paint localPaint; //设置画笔
        localPaint = new Paint();
        localPaint.setStyle(Paint.Style.STROKE); //画笔实心
        localPaint.setColor(getContext().getResources().getColor(R.color.gridview_bg));//画笔颜色
        View view0 = getChildAt(0); //第一个view
        View viewColLast = getChildAt(colnum - 1);//第一行最后一个view
        if (view0 != null && viewColLast != null) {
            for (int i = 1, c = 1; i < rownum || c < colnum; i++, c++) {
                //画横线
                canvas.drawLine(view0.getLeft(), view0.getBottom() * i, viewColLast.getRight(), viewColLast.getBottom() * i, localPaint);
            }
        }

    }


}
