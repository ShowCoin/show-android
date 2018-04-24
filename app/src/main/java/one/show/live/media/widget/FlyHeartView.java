package one.show.live.media.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;


import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import one.show.live.log.Logger;


public class FlyHeartView extends SurfaceView
        implements SurfaceHolder.Callback, Runnable {

    private Context mContext;

    private static final int mDruation = 5000;
    private static final int mScaleDruation = 500;


    private int mWidth;
    private int mHeight;


    private SurfaceHolder mHolder;

    private Paint mPaint = new Paint();
    private Random random = new Random();

    private boolean mCanRun;

    private ConcurrentLinkedQueue<HeartBean> heartLineQueue = new ConcurrentLinkedQueue();
    private ConcurrentLinkedQueue<HeartBean> heartDrawQueue = new ConcurrentLinkedQueue();


    public FlyHeartView(Context context) {
        super(context);
        init(context);
    }

    public FlyHeartView(Context context, AttributeSet paramAttributeSet) {
        super(context, paramAttributeSet);
        init(context);
    }

    public FlyHeartView(Context context, AttributeSet paramAttributeSet, int paramInt) {
        super(context, paramAttributeSet, paramInt);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mHolder = getHolder();
        mHolder.addCallback(this);
        setZOrderOnTop(true);
        mHolder.setFormat(PixelFormat.TRANSLUCENT);

    }

    public void addHeart() {
        addHeart(false);
    }

    public void addHeart(boolean isLocal) {
        boolean endRight = false;
        if (this.random.nextInt(2) == 0)
            endRight = true;
        Bitmap map = FlyHeartUtil.getFloats(getContext(),isLocal ? 5 : random.nextInt(5));
        if(map!=null) {
            HeartBean localHeartBean = new HeartBean(endRight, map);
            localHeartBean.startTime = System.currentTimeMillis();
            heartLineQueue.offer(localHeartBean);
            start();
        }
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        mHolder = holder;
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        mCanRun = true;
    }

    public void surfaceDestroyed(SurfaceHolder arg0) {
        mCanRun = false;
        stop();
    }


    private Thread mThread;
    private boolean mIsRun;

    public void start() {
        if (mIsRun)
            return;
        Logger.e("samuel", "heart view start");
        setVisibility(View.VISIBLE);
        mIsRun = true;
        mThread = new Thread(this);
        try {
            mThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stop() {
        Logger.e("samuel", "heart view stop");
        mIsRun = false;
        mThread = null;
        clear();
    }

    private Object lockObj = new Object();
    private int frameRate = 30;

    public void run() {
        Canvas canvas = null;
        while (mCanRun && mIsRun && mThread != null && !mThread.isInterrupted() && mHolder != null) {
            synchronized (lockObj) {
                if (!mHolder.getSurface().isValid()) {
                    continue;
                }

                long start = System.currentTimeMillis();
                try {
                    canvas = mHolder.lockCanvas();
                    if (canvas == null) {
                        continue;
                    }
                    sysncData();

                    canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    mPaint.reset();
                    Iterator iterator = heartDrawQueue.iterator();
                    HeartBean hearItem;
                    while (iterator.hasNext()) {
                        hearItem = (HeartBean) iterator.next();
                        Rect srcRect = new Rect();
                        Rect dstRect = new Rect();
                        int w = hearItem.heartWidth;
                        int h = hearItem.heartHeight;
                        long executeTime = System.currentTimeMillis() - hearItem.startTime;
                        boolean isShouldScale = executeTime < mScaleDruation;
                        int xOffset = 0;
                        int yOffset = 0;
                        if (isShouldScale) {
                            float f = executeTime * 1.0f / mScaleDruation;
                            w = (int) (f * hearItem.heartWidth);
                            h = (int) (f * hearItem.heartHeight);
                            xOffset = (hearItem.heartWidth - w) / 2;
                            yOffset = (hearItem.heartHeight - h) / 2;
                        }

                        srcRect.left = 0;
                        srcRect.top = 0;
                        srcRect.right = hearItem.heartWidth;
                        srcRect.bottom = hearItem.heartHeight;

                        dstRect.left = (xOffset + (int) hearItem.drawX);
                        dstRect.top = (yOffset + (int) hearItem.drawY);
                        dstRect.right = (int) (hearItem.drawX + xOffset + w);
                        dstRect.bottom = (int) (hearItem.drawY + yOffset + h);
                        mPaint.setAlpha(hearItem.drawAlpha);

                        canvas.drawBitmap(hearItem.bitmap, srcRect, dstRect, mPaint);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (canvas != null) {
                        mHolder.unlockCanvasAndPost(canvas);
                    }
                }

                long excuteTime = System.currentTimeMillis() - start;
                try {
                    if (excuteTime < frameRate) {
                        Thread.sleep(frameRate - excuteTime);
                    }
                } catch (Exception e) {
                }

            }
        }
    }


    private void sysncData() {
        try {
            while (!heartLineQueue.isEmpty()) {
                heartDrawQueue.add(heartLineQueue.poll());
            }
        } catch (Exception e) {
            heartLineQueue.clear();
            e.printStackTrace();
            return;
        }

        if (heartDrawQueue.size() == 0) {
            stop();
        }

        /**
         * 移除已经结束的动画
         */
        LinkedList endAnimList = new LinkedList();
        long nowTime = System.currentTimeMillis();
        Iterator iterator = heartDrawQueue.iterator();
        HeartBean hearItem;

        while (iterator.hasNext()) {
            hearItem = (HeartBean) iterator.next();
            if (nowTime - hearItem.startTime < mDruation) {
                hearItem.evaluate((nowTime - hearItem.startTime) * 1.0f / mDruation);
            } else {
                endAnimList.add(hearItem);
            }
        }

        synchronized (heartDrawQueue) {
            this.heartDrawQueue.removeAll(endAnimList);
            return;
        }
    }


    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        clear();
    }

    private void clear() {
        heartLineQueue.clear();
        heartDrawQueue.clear();
    }


    private class HeartBean {
        public int heartWidth;
        public int heartHeight;

        public Bitmap bitmap;

        private Random random = new Random();
        public long startTime = System.currentTimeMillis();

        private PointF[] pointFs = new PointF[4];
        public int drawAlpha;
        public float drawX;
        public float drawY;

        public HeartBean(boolean endRight, Bitmap bitmap) {
            this.bitmap = bitmap;
            this.heartWidth = bitmap.getWidth();
            this.heartHeight = bitmap.getHeight();

            int w = mWidth <= 2 ? 2 : mWidth;
            int h = mHeight <= 4 ? 4 : mHeight;

            int p0X = (mWidth - heartWidth) / 2;

            int p1p2X = random.nextInt(w / 2);


            int p3Y = random.nextInt(h / 4);

            int p1p2yOffset = mHeight / 6;


            /**
             * 为了美观，p2在p1的上方。***并且p1Y和p2Y相对p3Y。
             */
            int p1Y = p3Y + p1p2yOffset * 2;
            int p2Y = p3Y + p1p2yOffset;

            //p3x坐标偏移的中心点
            //p3x坐标活动范围0~p0x
            int p3xOffsetPoint = p0X / 2;

            pointFs[0] = new PointF(p0X, mHeight);

            //p3的x坐标往右便宜
            if (endRight) {
                pointFs[1] = new PointF(p1p2X, p1Y);
                pointFs[2] = new PointF(p1p2X, p2Y);
                pointFs[3] = new PointF(p3xOffsetPoint + random.nextInt(p3xOffsetPoint), p3Y);
            } else {
                pointFs[1] = new PointF(p0X - p1p2X, p1Y);
                pointFs[2] = new PointF(p0X - p1p2X, p2Y);
                pointFs[3] = new PointF(p3xOffsetPoint - random.nextInt(p3xOffsetPoint), p3Y);
            }

        }

        public void evaluate(float progress) {
            float f = 1.0f - progress;
            drawAlpha = (int) (255.0f * f);

            float f1 = 3.0f * (pointFs[1].x - pointFs[0].x);
            float f2 = 3.0f * (pointFs[2].x - pointFs[1].x) - f1;
            float f3 = pointFs[3].x - pointFs[0].x - f1 - f2;
            float f4 = 3.0f * (pointFs[1].y - pointFs[0].y);
            float f5 = 3.0f * (pointFs[2].y - pointFs[1].y) - f4;
            float f6 = pointFs[3].y - pointFs[0].y - f4 - f5;
            float f7 = progress * progress;
            float f8 = f7 * progress;
            drawX = (f3 * f8 + f2 * f7 + f1 * progress + pointFs[0].x);
            drawY = (f6 * f8 + f5 * f7 + f4 * progress + pointFs[0].y);

            //贝塞尔公式
//            drawX=pointFs[0].x*(1-progress)*(1-progress)*(1-progress)+3*pointFs[1].x*progress*(1-progress)*(1-progress)+3*pointFs[2].x*(1-progress)*progress*progress+pointFs[3].x*progress*progress*progress;
//            drawY=pointFs[0].y*(1-progress)*(1-progress)*(1-progress)+3*pointFs[1].y*progress*(1-progress)*(1-progress)+3*pointFs[2].y*(1-progress)*progress*progress+pointFs[3].y*progress*progress*progress;
            if(drawX <=0)
                drawX = 0;
            if(drawY<=0)
                drawY = 0;

        }
    }


}