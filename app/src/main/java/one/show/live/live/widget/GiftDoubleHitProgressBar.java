package one.show.live.live.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import one.show.live.util.ConvertToUtils;

/**
 * 圆形的进度条
 */
public class GiftDoubleHitProgressBar extends View {
    private int maxProgress = 100;
    private int progress = 100;
    private int progressStrokeWidth;

    //画圆所在的距形区域
    private RectF oval;
    private Paint paint;

    public GiftDoubleHitProgressBar(Context context) {
        super(context);
        init();
    }

    public GiftDoubleHitProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GiftDoubleHitProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        oval = new RectF();
        paint = new Paint();
        progressStrokeWidth = ConvertToUtils.dipToPX(getContext(), 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO 自动生成的方法存根
        super.onDraw(canvas);
        int width = this.getWidth();
        int height = this.getHeight();

        if (width != height) {
            int min = Math.min(width, height);
            width = min;
            height = min;
        }

        paint.setAntiAlias(true);                       // 设置画笔为抗锯齿
        paint.setColor(Color.parseColor("#f9743a"));    // 设置画笔颜色
        canvas.drawColor(Color.TRANSPARENT);            // 白色背景
        paint.setStrokeWidth(progressStrokeWidth);      //线宽
        paint.setStyle(Paint.Style.STROKE);

        oval.left = progressStrokeWidth / 2;            // 左上角x
        oval.top = progressStrokeWidth / 2;             // 左上角y
        oval.right = width - progressStrokeWidth / 2;   // 左下角x
        oval.bottom = height - progressStrokeWidth / 2; // 右下角y

        canvas.drawArc(oval, -90, 360, false, paint);   //绘制白色圆圈，即进度条背景

        paint.setColor(Color.parseColor("#99ffffff"));
        canvas.drawArc(oval, -90, ((float) progress / maxProgress) * 360, false, paint); // 绘制进度圆弧，这里是蓝色

        paint.setStrokeWidth(6);

//        int textHeight = height / 4;
//        paint.setTextSize(textHeight);
//        paint.setStyle(Paint.Style.FILL);
    }

    public int getMaxProgress() {
        return maxProgress;
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        this.invalidate();
    }

    /**
     * 非ＵＩ线程调用
     */
    public void setProgressNotInUiThread(int progress) {
        this.progress = progress;
        this.postInvalidate();
    }
}
