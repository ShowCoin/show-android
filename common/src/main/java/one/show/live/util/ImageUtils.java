package one.show.live.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.ref.SoftReference;
import java.util.concurrent.ConcurrentHashMap;

import one.show.live.common.R;

/**
 *
 */
public class ImageUtils {

    public static Bitmap toHexagonRoundBitmap(Context context, Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float left, top, right, bottom;

        if (width <= height) {
            float clip = (height - width) * 1.0f / 2;
            top = clip;
            bottom = height - clip;
            left = 0;
            right = width;
            height = width;
        } else {
            float clip = (width - height) * 1.0f / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
        }


        int strokeWidth = ConvertToUtils.dipToPX(context, 2);
        int size = width + strokeWidth * 2;

        int radius = ConvertToUtils.dipToPX(context, 6);

        Bitmap output = Bitmap.createBitmap(size,
                size, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect(strokeWidth, strokeWidth, size - strokeWidth, size - strokeWidth);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#FF0000"));
        paint.setStyle(Paint.Style.FILL);
        canvas.save();
        CornerPathEffect corEffect = new CornerPathEffect(radius);
        paint.setPathEffect(corEffect);
        canvas.rotate(30, size / 2, size / 2);
        canvas.drawPath(getPath(size), paint);
        canvas.restore();

        canvas.saveLayer(0F, 0F, size, size, null, Canvas.ALL_SAVE_FLAG);
        Paint paint1 = new Paint();
        paint1.setAntiAlias(true);
        canvas.drawBitmap(getMaskBitmap(width, radius), strokeWidth, strokeWidth, paint1);
        paint1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint1);
        canvas.restore();

        bitmap.recycle();
        return output;
    }


//    private static Bitmap getBitmap(Bitmap bitmap){}


    private static class MaskUtils {

        private static ConcurrentHashMap<String, SoftReference<Bitmap>> bitmaps = new ConcurrentHashMap<>();

        private static Bitmap createMask(int size, float radius) {
            String key = size + "_size";
            SoftReference<Bitmap> sef = bitmaps.get(key);
            Bitmap mMaskBitmap = sef == null ? null : sef.get();
            if (mMaskBitmap != null && !mMaskBitmap.isRecycled()) {
                return mMaskBitmap;
            }
            try {
                mMaskBitmap = Bitmap.createBitmap(size, size, android.graphics.Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(mMaskBitmap);
                Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                paint.setARGB(255, 0, 0, 0);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);

                CornerPathEffect corEffect = new CornerPathEffect(radius);
                paint.setPathEffect(corEffect);

                canvas.rotate(30, size / 2, size / 2);
                canvas.drawPath(getPath(size), paint);
                bitmaps.put(key, new SoftReference<>(mMaskBitmap));
            } catch (Exception e) {
//                Log.e("samuel", "mask bitmap error:" + (e == null ? "null" : e.getMessage()));
            } catch (OutOfMemoryError ex) {
//                Log.e("samuel", "mask bitmap oom...");
            }
            return mMaskBitmap;
        }
    }


    private static Path getPath(int size) {
        int mLength = size / 2;

        double radian30 = 30 * Math.PI / 180;
        float a = (float) (mLength * Math.sin(radian30));
        float b = (float) (mLength * Math.cos(radian30));
        float c = (size - 2 * b) / 2;


        Path path = new Path();
        path.moveTo(size, mLength);
        path.lineTo(size - a, size - c);
        path.lineTo(size - a - mLength, size - c);
        path.lineTo(0, mLength);
        path.lineTo(a, c);
        path.lineTo(size - a, c);
        path.close();
        return path;
    }


    static Bitmap maskBitmap;

    private static Bitmap getMaskBitmap(int size, int radius) {
        if (maskBitmap == null) {
            maskBitmap = MaskUtils.createMask(size, radius);
        }
        return maskBitmap;
    }

    /**
     * 绘制圆角正方形
     * @param context
     * @param source
     * @param Rounded 圆角的度数
     * @return
     */

    public static Bitmap createCircleImage(Context context,Bitmap source,int Rounded) {

        int width = source.getWidth();
        int height = source.getHeight();
        float left, top, right, bottom;

        if (width <= height) {
            top = 0;
            left = 0;
            right = width;
            bottom = width;
            height = width;
        } else {
            left = 0;
            top = 0;
            right = height;
            bottom = height;
            width = height;
        }


        int r = ConvertToUtils.dipToPX(context, Rounded);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setARGB(255,0,0,0);
// 注意一定要用ARGB_8888，否则因为背景不透明导致遮罩失败
        Bitmap target = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
// 产生一个同样大小的画布
        Canvas canvas = new Canvas(target);
        canvas.save();
//    //画圆角矩形
        RectF oval3 = new RectF(left, top, right, bottom);// 设置个新的长方形
        canvas.drawRoundRect(oval3, r, r, paint);//第二个参数是x半径，第三个参数是y半径
// 使用SRC_IN
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
// 绘制图片
        canvas.drawBitmap(source, 0, 0, paint);

        canvas.restore();
//
//        source.recycle();
        return target;
    }

}
