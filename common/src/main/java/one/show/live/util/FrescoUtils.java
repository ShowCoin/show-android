package one.show.live.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.image.CloseableBitmap;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequest.CacheChoice;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.ImageRequest.RequestLevel;
import com.facebook.imagepipeline.request.Postprocessor;

import one.show.live.common.R;

public class FrescoUtils {

    private ExecutorService mExecutorService;
    private ImagePipeline imagePipeline;

    public FrescoUtils() {
        ThreadFactory sThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "ImportHelper #" + mCount.getAndIncrement());
            }
        };
        mExecutorService = Executors.newSingleThreadExecutor(sThreadFactory);//单任务线程池，无边界
        imagePipeline = Fresco.getImagePipeline();

    }

    public static ImageRequest buildRequest(Uri uri) {
        return buildRequest(uri, 0, 0);
    }

    //	public static ImageRequest buildLocalRequest(Uri uri) {
    ////		return buildRequest(uri, RequestLevel.DISK_CACHE, 0, 0);
    //	}

    public static ImageRequest buildRequest(Uri uri, int width, int height) {

        //RequestLevel.DISK_CACHE，不管用，暂时不知道解决办法,已经给facebook提交了issue

        //	ImageRequest request = ImageRequestBuilder
        //		    .newBuilderWithSource(uri)
        //		    .setAutoRotateEnabled(true)
        //		    .setLocalThumbnailPreviewsEnabled(true)
        //		    .setLowestPermittedRequestLevel(RequestLevel.FULL_FETCH)
        //		    .setProgressiveRenderingEnabled(false)
        //		    .setResizeOptions(new ResizeOptions(width, height))
        //		    .build();
        return (width != 0 && height != 0) ? ImageRequestBuilder.newBuilderWithSource(uri).setAutoRotateEnabled(true).setLowestPermittedRequestLevel(RequestLevel.FULL_FETCH).setResizeOptions(new ResizeOptions(width, height)).build() : ImageRequestBuilder.newBuilderWithSource(uri).setLocalThumbnailPreviewsEnabled(true).setLowestPermittedRequestLevel(RequestLevel.FULL_FETCH).build();
    }


    public static ImageRequest buildSmallRequest(Uri uri) {

        return ImageRequestBuilder.newBuilderWithSource(uri).setAutoRotateEnabled(true).setCacheChoice(CacheChoice.SMALL).setLowestPermittedRequestLevel(RequestLevel.FULL_FETCH).build();
    }

    public static ImageRequest buildSmallRequest(Uri uri, int width, int height) {
        return ImageRequestBuilder.newBuilderWithSource(uri).setAutoRotateEnabled(true).setCacheChoice(CacheChoice.SMALL).setLowestPermittedRequestLevel(RequestLevel.FULL_FETCH).setResizeOptions(new ResizeOptions(width, height)).build();
    }


    public static Uri getUri(String url) {
        return StringUtils.isEmpty(url) ? Uri.parse("") : Uri.parse(url);
    }

    public static Uri getFileUri(String path) {
        return StringUtils.isEmpty(path) ? Uri.parse("") : Uri.parse("file://" + path);
    }

    public static Uri getResUri(int resId) {
        return Uri.parse("res:///" + resId);
    }

    public static Uri getAssesUri(String path) {
        return StringUtils.isEmpty(path) ? Uri.parse("") : Uri.parse("asset:///" + path);
    }

    public void loadImage(Activity context, Uri uri, OnLoadListener listener) {
        if (checkParameter(context, uri)) {
            return;
        }
        loadImage(context, uri, buildRequest(uri), listener);
    }

    /**
     * 加载视频截图
     */
    public void loadImage(final Activity context, final Uri uri, final ImageRequest request, final OnLoadListener listener) {

        DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(request, context);
        DataSubscriber dataSubscriber = new BaseDataSubscriber<CloseableReference<CloseableImage>>() {

            @Override
            public void onNewResultImpl(final DataSource<CloseableReference<CloseableImage>> dataSource) {

                if (!dataSource.isFinished()) {
                    return;
                }
                if (context != null) {
                    if (listener != null) {
                        if (!listener.onFullResult(context, uri, dataSource)) {
                            CloseableReference<CloseableImage> closeableImageRef = dataSource.getResult();
                            Bitmap bitmap = null;
                            if (closeableImageRef != null && closeableImageRef.get() instanceof CloseableBitmap) {
                                bitmap = ((CloseableBitmap) closeableImageRef.get()).getUnderlyingBitmap();
                            }

                            try {
                                listener.onResult(context, uri, bitmap);
                            } finally {
                                CloseableReference.closeSafely(closeableImageRef);
                            }
                        }
                    }
                }

            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                Throwable throwable = dataSource.getFailureCause();
                if (listener != null)
                    listener.onFailure(context, uri, throwable);
            }
        };

        dataSource.subscribe(dataSubscriber, mExecutorService);
    }

    //	public void loadLocalImage(Context context, Uri uri, OnLoadListener listener) {
    //		if (checkParameter(context, uri)) {
    //			return;
    //		}
    //
    //		loadImage(context, uri, buildLocalRequest(uri), listener);
    //	}

    private boolean checkParameter(Context context, Uri uri) {
        if (context == null || uri == null || mExecutorService == null) {
            return true;
        }
        if (mExecutorService == null || mExecutorService.isShutdown()) {
            return true;
        }
        return false;
    }

    /**
     * 释放
     */
    public void onDestory() {
        if (mExecutorService != null) {
            mExecutorService.shutdownNow();
        }
        mExecutorService = null;
    }

    public interface OnLoadListener {

        /**
         * 适用于用完bitmap后就不需要再次使用的情况
         *
         * @param context
         * @param uri
         * @param bitmap
         */
        void onResult(Context context, Uri uri, Bitmap bitmap);

        /**
         * 适用于获取bitmap，并需要多次使用或者需要设置到imageView的情况
         *
         * @param context
         * @param uri
         * @param dataSource
         * @return
         */
        boolean onFullResult(Context context, Uri uri, DataSource<CloseableReference<CloseableImage>> dataSource);

        void onFailure(Context context, Uri uri, Throwable throwable);
    }


    public static GenericDraweeHierarchy getNormalHierarchy(Context context) {
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(context.getResources());

        ColorDrawable holderImage = new ColorDrawable(ContextCompat.getColor(context, R.color.image_back_color));


        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setPlaceholderImage(holderImage)
                .setPlaceholderImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                .setActualImageScaleType(ScalingUtils.ScaleType.FIT_XY)
                .setBackground(holderImage)
                .build();

        return hierarchy;
    }


    public static void smallReqImage(SimpleDraweeView view, String url) {
        view.setController(Fresco.newDraweeControllerBuilder()
                .setImageRequest(buildSmallRequest(getUri(url)))
                .setOldController(view.getController())
                .build());
    }

    public static void smallReqImageWithListener(SimpleDraweeView view, String url, ControllerListener listener) {
        view.setController(Fresco.newDraweeControllerBuilder()
                .setImageRequest(buildSmallRequest(getUri(url)))
                .setOldController(view.getController())
                .setControllerListener(listener)
                .build());


    }

    public static long Fib(int n) {
        if (n < 2) {
            return n;
        } else {
            return Fib(n - 1) + Fib(n - 2);
        }
    }


    public static void bind(SimpleDraweeView draweeView, String uriStr) {
        bind(draweeView, getUri(uriStr));
    }

    public static void bind(SimpleDraweeView draweeView, String uriStr, int width, int height) {
        bind(draweeView, getUri(uriStr), width, height);
    }

    public static void bind(SimpleDraweeView draweeView, Uri uri) {
        bind(draweeView, uri, 0);
    }

    public static void bind(SimpleDraweeView draweeView, Uri uri, int size) {
        bind(draweeView, uri, size, size);
    }


    public static void bind(SimpleDraweeView draweeView, Uri uri, int width, int height) {
        bind(draweeView, uri, width, height, null);
    }

    /**
     * 高斯模糊
     *
     * @param draweeView
     * @param uriStr
     */
    public static void bindWithBlur(SimpleDraweeView draweeView, String uriStr) {
        bind(draweeView, getUri(uriStr), 0, 0, new BasePostprocessor() {
            @Override
            public void process(Bitmap bitmap) {
                super.process(bitmap);
                ImageBlur.blurBitMap(bitmap, 20);

            }
        });
    }

    /**
     * 高斯模糊
     *
     * @param draweeView
     * @param uriStr
     * @param size       模糊程度
     */
    public static void bindWithBlur(SimpleDraweeView draweeView, String uriStr, final int size) {
        bind(draweeView, getUri(uriStr), 0, 0, new BasePostprocessor() {
            @Override
            public void process(Bitmap bitmap) {
                super.process(bitmap);
                ImageBlur.blurBitMap(bitmap, size);

            }
        });
    }

    /**
     * 高斯模糊
     *
     * @param draweeView
     * @param uriStr
     * @param size       模糊程度
     * @param width      图片宽高
     */
    public static void bindWithBlur(SimpleDraweeView draweeView, String uriStr, final int size, int width) {
        bind(draweeView, getUri(uriStr), width, width, new BasePostprocessor() {
            @Override
            public void process(Bitmap bitmap) {
                super.process(bitmap);
                ImageBlur.blurBitMap(bitmap, size);

            }
        });
    }

    public static void bindWithBlur(SimpleDraweeView draweeView, Uri uri, final int size, int photoSize) {
        bind(draweeView, uri, photoSize, photoSize, new BasePostprocessor() {
            @Override
            public void process(Bitmap bitmap) {
                super.process(bitmap);
                ImageBlur.blurBitMap(bitmap, size);

            }
        });
    }

    public static void bindWithBlur(SimpleDraweeView draweeView, Uri uri, final int size, int width, int height) {
        bind(draweeView, uri, width, height, new BasePostprocessor() {
            @Override
            public void process(Bitmap bitmap) {
                super.process(bitmap);
                ImageBlur.blurBitMap(bitmap, size);

            }
        });
    }


    public static void bind(SimpleDraweeView draweeView, Uri uri, int width, int height, Postprocessor processor) {
        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder
                .newBuilderWithSource(uri).setLocalThumbnailPreviewsEnabled(true).setAutoRotateEnabled(true);
//        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder
//                .newBuilderWithSource(uri).setLocalThumbnailPreviewsEnabled(true);

        if (width != 0 && height != 0) {
            imageRequestBuilder.setResizeOptions(
                    new ResizeOptions(
                            width,
                            height));
        }

        if (processor != null) {
            imageRequestBuilder.setPostprocessor(processor);
        }

//        DelayPostprocessor postprocessor = DelayPostprocessor.getFastPostprocessor();
//        imageRequestBuilder.setPostprocessor(postprocessor);

        // Create the Builder
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(true)
                .setImageRequest(imageRequestBuilder.build());


        builder.setOldController(draweeView.getController());
        draweeView.setController(builder.build());
    }


    public static void bind(SimpleDraweeView draweeView, Uri uri, int width, int height, boolean isSmall, Postprocessor processor) {
        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder
                .newBuilderWithSource(uri).setLocalThumbnailPreviewsEnabled(true).setProgressiveRenderingEnabled(true).setAutoRotateEnabled(true);

        if (width != 0 && height != 0) {
            imageRequestBuilder.setResizeOptions(
                    new ResizeOptions(
                            width,
                            height));
        }


        if (isSmall) {
            imageRequestBuilder.setCacheChoice(CacheChoice.SMALL);
        }

        if (processor != null) {
            imageRequestBuilder.setPostprocessor(processor);
        }

//        DelayPostprocessor postprocessor = DelayPostprocessor.getFastPostprocessor();
//        imageRequestBuilder.setPostprocessor(postprocessor);

        // Create the Builder
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder()
                .setImageRequest(imageRequestBuilder.build());
        builder.setOldController(draweeView.getController());
        draweeView.setController(builder.build());
    }

    public static void loadWebpAnim(SimpleDraweeView simpleDraweeView, String url) {
        if (TextUtils.isEmpty(url)) {
            Log.e("ImageLoadUtil", "loadImageNetWork, url is empty!");
            return;
        }

        Uri uri = Uri.parse(url);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(uri)
                .setAutoPlayAnimations(true)
                .build();
        simpleDraweeView.setController(controller);
    }

    public static void bind(SimpleDraweeView draweeView, String url, boolean isAuto) {
        bind(draweeView,getUri(url),0,0,null,isAuto);
    }

    public static void bind(SimpleDraweeView draweeView, Uri uri, int width, int height, Postprocessor processor, boolean isAuto) {
        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder
                .newBuilderWithSource(uri).setLocalThumbnailPreviewsEnabled(true).setAutoRotateEnabled(true);
//        ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder
//                .newBuilderWithSource(uri).setLocalThumbnailPreviewsEnabled(true);

        if (width != 0 && height != 0) {
            imageRequestBuilder.setResizeOptions(
                    new ResizeOptions(
                            width,
                            height));
        }

        if (processor != null) {
            imageRequestBuilder.setPostprocessor(processor);
        }

//        DelayPostprocessor postprocessor = DelayPostprocessor.getFastPostprocessor();
//        imageRequestBuilder.setPostprocessor(postprocessor);

        // Create the Builder
        PipelineDraweeControllerBuilder builder = Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(isAuto)
                .setImageRequest(imageRequestBuilder.build());


        builder.setOldController(draweeView.getController());
        draweeView.setController(builder.build());
    }

}
