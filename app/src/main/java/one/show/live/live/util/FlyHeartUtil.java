package one.show.live.live.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;

import java.lang.ref.SoftReference;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

import one.show.live.R;
import one.show.live.log.Logger;


public class FlyHeartUtil {

    private static ConcurrentHashMap<Integer, SoftReference<Bitmap>> bitmaps = new ConcurrentHashMap<>();


    private static Bitmap getBitmap(Context context,int resId) {
        SoftReference<Bitmap> sef = bitmaps.get(resId);
        Bitmap bitmap = sef == null ? null : sef.get();
        if (bitmap != null && !bitmap.isRecycled()) {
            return bitmap;
        }
        try {
            bitmap = ((BitmapDrawable) ContextCompat.getDrawable(context, resId)).getBitmap();
            bitmaps.put(resId, new SoftReference<>(bitmap));
            return bitmap;
        } catch (Exception e) {
            Logger.e("samuel", "heart bitmap error:" + (e == null ? "null" : e.getMessage()));
        } catch (OutOfMemoryError ex) {
            Logger.e("samuel", "heart bitmap oom...");
        }
        return null;
    }


    public static int bitmapIds[] = new int[]{
            R.drawable.huangtao,R.drawable.lantao,R.drawable.jubei,R.drawable.lanbei,R.drawable.zibei,R.drawable.hongtao
    };

    public static Bitmap getFloats(Context context,int i) {
        if (i >= bitmapIds.length || i < 0) {
            return getBitmap( context,bitmapIds[0]);
        } else {
            return getBitmap( context,bitmapIds[i]);
        }
    }
}
