package com.dreamer.tv.live.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dreamer.tv.base.util.ConvertToUtils;


public class TestView extends View {
    private final Paint mGesturePaint = new Paint();
    private final Path mPath = new Path();
    int giftW,giftH;
    int giftPW;
    public TestView(Context context) {
        super(context);
        initView(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);

    }
    private void initView(Context context){
        mGesturePaint.setAntiAlias(true);
        mGesturePaint.setStyle(Paint.Style.STROKE);
        mGesturePaint.setStrokeWidth(5);
        mGesturePaint.setColor(Color.WHITE);
//        mPath.moveTo(100, 500);
//        mPath.quadTo(300, 100, 600, 500);
        int [] posi = {288,1526};
        giftW = ConvertToUtils.dipToPX(context,44);
        giftPW = ConvertToUtils.dipToPX(context,72);
        giftH = giftW;
        mPath.moveTo(posi[0] + giftPW / 2   ,posi[1]);
        mPath.quadTo(posi[0] + giftPW / 2  - giftPW ,posi[1]  - giftPW/2,posi[0] + giftPW / 2 ,posi[1] - giftPW);
        mPath.quadTo(posi[0] + giftPW / 2  + giftPW ,posi[1]  - giftPW* 3/2,posi[0] + giftPW / 2 ,posi[1]  - 2*giftPW);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mGesturePaint);
    }
}
