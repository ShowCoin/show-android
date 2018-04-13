package one.show.live.util;

import android.graphics.Bitmap;

import one.show.live.util.ImageBlur;

/**
 * Created by wanglushan on 2017/1/23.
 */

public class ImageBlurUtils {

    private static Bitmap buildBitmap(Bitmap original) {

        Bitmap.Config config = original.getConfig();
        if (config != Bitmap.Config.ARGB_8888 && config != Bitmap.Config.RGB_565) {
            return null;
        }

        Bitmap rBitmap;
        // 是否可改变像素
        if (original.isMutable()) {
            rBitmap = original;
        } else {
            rBitmap = original.copy(config, true);
        }
        return rBitmap;
    }


    public static Bitmap blurBitmap(Bitmap bitmap){
        return blurBitmap(bitmap,20);
    }

    public static Bitmap blurBitmap(Bitmap bitmap,int radius) {

        if(bitmap == null)
            return null;

        bitmap = buildBitmap(bitmap);

        if(bitmap == null)
            return null;

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);
        ImageBlur.blurIntArray(pix, w, h, radius);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return bitmap;
    }


}
