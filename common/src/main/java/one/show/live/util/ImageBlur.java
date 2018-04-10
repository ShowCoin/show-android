package one.show.live.util;

import android.graphics.Bitmap;

/**
 * 高斯模糊工具类
 *
 * https://github.com/qiujuer/ImageBlurring
 *
 */
public class ImageBlur {

    public static native void blurIntArray(int[] pImg, int w, int h, int r);
    public static native void blurBitMap(Bitmap bitmap, int r);
}
