package one.show.live.ui;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import com.baidu.mapapi.SDKInitializer;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.NoOpMemoryTrimmableRegistry;
import com.facebook.common.soloader.SoLoaderShim;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.cache.MemoryCacheParams;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import java.io.File;

import one.show.live.po.POBaiduLocation;
import one.show.live.util.AppUtil;
import one.show.live.util.FileUtils;
import one.show.live.util.FrescoUtils;
import one.show.live.util.StringUtils;
import one.show.live.util.UserInfoCacheManager;


public class BaseApplication extends Application {

    static {
        System.loadLibrary("meet2016");
    }


    /**
     * 当前Application
     */
    protected static BaseApplication baseApplication;


    @Override
    public void onCreate() {
        super.onCreate();
        if (!AppUtil.isMainProcess(getApplicationContext())) {
            return;
        }


//        SoLoaderShim.setHandler(new SoLoaderShim.Handler() {
//            @Override
//            public void loadLibrary(String libraryName) {
//                SoLoader.loadLibrary(libraryName);
//            }
//        });


//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("fonts/arial_unicode.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );

        baseApplication = this;
        initLocation();
        initImageLoader(getApplicationContext());
        initFresco();
        UserInfoCacheManager.init(getApplicationContext());
        com.nostra13.universalimageloader.utils.L.writeDebugLogs(false);
        com.nostra13.universalimageloader.utils.L.writeLogs(false);
    }

    /**
     * 获取当前Application
     */
    public static BaseApplication getInstance() {
        return baseApplication;
    }

    /**
     * 获取当前Application
     */
    public static Context getContext() {
        return baseApplication;
    }


    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 480) // default = device screen dimensions
                .diskCacheExtraOptions(480, 480, null)
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSize(5 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiskCache(FileUtils.getCacheDiskPath(context, "imageloader")))
                .diskCacheSize(1024 * 1024 * 1024)
                .diskCacheFileCount(100000)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .imageDecoder(new BaseImageDecoder(true)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .build();
        ImageLoader.getInstance().init(config);
    }


    private void initFresco() {
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(getApplicationContext())
                .setSmallImageDiskCacheConfig(getAlwaysDiskCacheConfig(getApplicationContext()))
                .setDownsampleEnabled(true)
                .setBitmapMemoryCacheParamsSupplier(new MyBitmapMemoryCacheParamsSupplier((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE)))
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .setMemoryTrimmableRegistry(NoOpMemoryTrimmableRegistry.getInstance())
                .setMainDiskCacheConfig(getDefaultMainDiskCacheConfig(getApplicationContext()))
                .build();


        NoOpMemoryTrimmableRegistry.getInstance().registerMemoryTrimmable(new MemoryTrimmable() {
            @Override
            public void trim(MemoryTrimType trimType) {
                final double suggestedTrimRatio = trimType.getSuggestedTrimRatio();

                if (MemoryTrimType.OnCloseToDalvikHeapLimit.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInBackground.getSuggestedTrimRatio() == suggestedTrimRatio
                        || MemoryTrimType.OnSystemLowMemoryWhileAppInForeground.getSuggestedTrimRatio() == suggestedTrimRatio
                        ) {
                    Fresco.getImagePipeline().clearMemoryCaches();
                }
            }
        });

        Fresco.initialize(getApplicationContext(), config);
    }


    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        trimMemory(level);
    }


    public static void trimMemory(int level) {
        try {
            if (level >= ComponentCallbacks2.TRIM_MEMORY_MODERATE) { // 60
                Fresco.getImagePipeline().clearMemoryCaches();
            }
        } catch (Exception e) {

        }
    }

    public class MyBitmapMemoryCacheParamsSupplier implements Supplier<MemoryCacheParams> {
        private static final int MAX_CACHE_ENTRIES = 56;
        private static final int MAX_CACHE_ASHM_ENTRIES = 256;
        private static final int MAX_CACHE_EVICTION_SIZE = 100;
        private static final int MAX_CACHE_EVICTION_ENTRIES = 100;
        private final ActivityManager mActivityManager;

        public MyBitmapMemoryCacheParamsSupplier(ActivityManager activityManager) {
            mActivityManager = activityManager;
        }

        @Override
        public MemoryCacheParams get() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return new MemoryCacheParams(getMaxCacheSize(), MAX_CACHE_ENTRIES,
                        Integer.MAX_VALUE,
                        Integer.MAX_VALUE,
                        Integer.MAX_VALUE);
            } else {
                return new MemoryCacheParams(
                        getMaxCacheSize(),
                        MAX_CACHE_ASHM_ENTRIES,
                        Integer.MAX_VALUE,
                        Integer.MAX_VALUE,
                        Integer.MAX_VALUE);
            }
        }

        private int getMaxCacheSize() {
            final int maxMemory =
                    Math.min(mActivityManager.getMemoryClass() * ByteConstants.MB, Integer.MAX_VALUE);
            if (maxMemory < 32 * ByteConstants.MB) {
                return 4 * ByteConstants.MB;
            } else if (maxMemory < 64 * ByteConstants.MB) {
                return 6 * ByteConstants.MB;
            } else {
                return maxMemory / 5;
            }
        }
    }

    private static DiskCacheConfig getDefaultMainDiskCacheConfig(final Context context) {
        return DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPathSupplier(new Supplier<File>() {
                    @Override
                    public File get() {
                        return FileUtils.getCacheDiskPath(context, "fresco");
                    }
                })
                .setBaseDirectoryName("image_cache")
                .setMaxCacheSize(200 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(10 * ByteConstants.MB)
                .setMaxCacheSizeOnVeryLowDiskSpace(2 * ByteConstants.MB)
                .build();
    }

    private static DiskCacheConfig getAlwaysDiskCacheConfig(final Context context) {
        return DiskCacheConfig.newBuilder(context)
                .setBaseDirectoryPathSupplier(new Supplier<File>() {
                    @Override
                    public File get() {
                        return FileUtils.getCacheDiskPath(context, "fresco_always");
                    }
                })
                .setBaseDirectoryName("image_cache")
                .setMaxCacheSize(100 * ByteConstants.MB)
                .setMaxCacheSizeOnLowDiskSpace(10 * ByteConstants.MB)
                .setMaxCacheSizeOnVeryLowDiskSpace(2 * ByteConstants.MB)
                .build();
    }

    private void initLocation() {
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        new POBaiduLocation().initLocation(getApplicationContext());
//        SDKInitializer.initialize(this);
    }


    private String videoCachePath;
    private String oldVideoCachePath;

    public String getVideoCachePath() {
        if (StringUtils.isNotEmpty(videoCachePath)) {
            return videoCachePath;
        } else {
            return videoCachePath = Environment.getExternalStorageDirectory() + "/SEEUMeet/local/";
        }
    }

    public String getOldVideoCachePath() {
        if (StringUtils.isNotEmpty(oldVideoCachePath)) {
            return oldVideoCachePath;
        } else {
            return oldVideoCachePath = Environment.getExternalStorageDirectory() + "/Meet/local/";
        }
    }
}
