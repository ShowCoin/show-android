package one.show.live.common.util;

import android.graphics.Bitmap;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import one.show.live.common.po.POMember;
import one.show.live.common.common.R;

/**
 * Created by wanglushan on 2017/1/23.
 */

public class ImageLoaderUtils {


   private static DisplayImageOptions defOptions =null;

    public static DisplayImageOptions getDisplayImageOptions() {
        if (defOptions == null) {

            synchronized (ImageLoaderUtils.class) {
                if (defOptions == null) {
                    defOptions = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.img_def) //设置图片在下载期间显示的图片
//                .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
                            .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                            .cacheOnDisk(true)
                            .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                            .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
//                .displayer(new RoundedBitmapDisplayer(360))//是否设置为圆角，弧度为多少
                            .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                            .build();//构建完成
                }
            }
        }
        return defOptions;
    }



    private static DisplayImageOptions defOptions2 =null;

    public static DisplayImageOptions getDisplayImageOptions2() {
        if (defOptions2 == null) {
            synchronized (ImageLoaderUtils.class) {
                if (defOptions2 == null) {
                    defOptions2 = new DisplayImageOptions.Builder()
                            .showImageOnLoading(R.drawable.img_def) //设置图片在下载期间显示的图片
//                .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
                            .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                            .cacheOnDisk(true)
                            .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                            .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                            .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
//                .displayer(new RoundedBitmapDisplayer(360))//是否设置为圆角，弧度为多少
//                            .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                            .build();//构建完成
                }
            }
        }
        return defOptions2;
    }


    public static DisplayImageOptions getDisplayImageWithRoundedOptions(int px) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.ic_launcher) //设置图片在下载期间显示的图片
//                .showImageForEmptyUri(R.drawable.ic_launcher)//设置图片Uri为空或是错误的时候显示的图片
//                .showImageOnFail(R.drawable.ic_launcher)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)
                .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                .displayer(new RoundedBitmapDisplayer(px))//是否设置为圆角，弧度为多少
//                .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                .build();//构建完成

        return options;
    }


}
