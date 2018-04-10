package com.samuel.mediachooser.Utilities;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.Thumbnails;
import android.support.v4.app.Fragment;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.ImageView;

import com.samuel.mediachooser.R;
import com.samuel.mediachooser.adapter.BucketGridAdapter;
import com.samuel.mediachooser.adapter.GridViewAdapter;
import com.samuel.mediachooser.fragment.BucketVideoFragment;
import com.samuel.mediachooser.fragment.VideoFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;


import static android.R.attr.fragment;


public class GalleryCache {

    private static ExecutorService mExecutorService;
    private LruCache<String, Bitmap> mBitmapCache;
    private ConcurrentLinkedQueue<String> mCurrentTasks;
    private int mMaxWidth;


    private static Map<ImageView, String> mImageViews;


    public static GalleryCache newInstance(Context context, int imgWidth, int imgHeight) {
        final int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                .getMemoryClass();
        final int size = 1024 * 1024 * memClass / 8;

        return new GalleryCache(size, imgWidth, imgHeight);
    }

    public GalleryCache(int size, int maxWidth, int maxHeight) {

        ThreadFactory sThreadFactory = new ThreadFactory() {
            private final AtomicInteger mCount = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "ImportHelper #" + mCount.getAndIncrement());
            }
        };

        mExecutorService = Executors.newSingleThreadExecutor(sThreadFactory);//单任务线程池，无边界

        mMaxWidth = maxWidth;

        mBitmapCache = new LruCache<String, Bitmap>(size) {
            @Override
            protected int sizeOf(String key, Bitmap b) {
                // Assuming that one pixel contains four bytes.
                return b.getHeight() * b.getWidth() * 4;
            }
        };

        mCurrentTasks = new ConcurrentLinkedQueue<>();
        mImageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    }

    private void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            mBitmapCache.put(key, bitmap);
        }
    }

    private Bitmap getBitmapFromCache(String key) {
        return mBitmapCache.get(key);
    }

    /**
     * Gets a bitmap from cache. <br/>
     * <br/>
     * If it is not in cache, this method will: <br/>
     * <b>1:</b> check if the bitmap url is currently being processed in the
     * BitmapLoaderTask and cancel if it is already in a task (a control to see
     * if it's inside the currentTasks list). <br/>
     * <b>2:</b> check if an internet connection is available and continue if
     * so. <br/>
     * <b>3:</b> download the bitmap, scale the bitmap if necessary and put it
     * into the memory cache. <br/>
     * <b>4:</b> Remove the bitmap url from the currentTasks list. <br/>
     * <b>5:</b> Notify the ListAdapter.
     *
     * @param mainActivity - Reference to activity object, in order to call
     *                     notifyDataSetChanged() on the ListAdapter.
     * @param imageKey     - The bitmap url (will be the key).
     * @param imageView    - The ImageView that should get an available bitmap or a
     *                     placeholder image.
     */
    public void loadBitmap(Activity mainActivity, long id, String imageKey,
                           ImageView imageView) {


        final Bitmap bitmap = getBitmapFromCache(imageKey);
        mImageViews.put(imageView, imageKey);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.img_def);

//            String tag = mImageViews.get(imageView);
//            if (tag != null && tag.equals(imageKey)) {
//                mExecutorService.
//            }
            if (!mCurrentTasks.contains(imageKey)) {
                if (mExecutorService != null && !mExecutorService.isShutdown()) {
                    mExecutorService.submit(new LoadingImage(id, imageKey, new WeakReference<>(mainActivity), new WeakReference<>(imageView)));
                    mCurrentTasks.add(imageKey);
                }
            } else {
                Log.e("samuel", "任务已经存在>>>imageKey:" + imageKey);
            }
        }
    }

    private class LoadingImage implements Runnable {
        private long mId;
        private WeakReference<Activity> mActWeakRef;
        private WeakReference<ImageView> mImgWeakRef;
        private String mImageKey;

        public LoadingImage(long id, String imageKey, WeakReference<Activity> actWeakRef, WeakReference<ImageView> imgWeakRef) {
            mId = id;
            mActWeakRef = actWeakRef;
            mImgWeakRef = imgWeakRef;
            mImageKey = imageKey;
        }


        private void callBack(final Bitmap bitmap) {
            if (Thread.currentThread().isInterrupted())
                return;
            Activity activity = mActWeakRef.get();
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("samuel", "刷新imageView>>>>" + mImageKey);
                        if (bitmap != null) {
                            ImageView img = mImgWeakRef.get();
                            if (img != null) {
                                String tag = mImageViews.get(img);
                                if (tag != null && tag.equals(mImageKey)) {
                                    img.setImageBitmap(bitmap);
                                }
                            }
                        }
                    }
                });
            }

        }

        @Override
        public void run() {

            Bitmap bitmap = null;


//            FFmpegMediaMetadataRetriever mmr = null;
//
//            try {
//                Activity activity = mActWeakRef.get();
//                if (activity != null&&mId!=-1) {
//                    final ContentResolver mContentResolver = activity.getContentResolver();
//                    bitmap = MediaStore.Video.Thumbnails.getThumbnail(mContentResolver, mId, MediaStore.Video.Thumbnails.MINI_KIND, null);
//                }
//
//                if (Thread.currentThread().isInterrupted())
//                    return;
//
//                if (bitmap == null) {
//                    mmr = new FFmpegMediaMetadataRetriever();
//                    mmr.setDataSource(mImageKey);
//                    mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
//                    mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
//                    bitmap = mmr.getFrameAtTime(); // frame at 2 seconds
//
//                }
//                if (Thread.currentThread().isInterrupted())
//                    return;
//
//
//                if (bitmap == null) {
//                    callBack(null);
//                    return;
//                }
//
//                int width = bitmap.getWidth();
//                int height = bitmap.getHeight();
//
//                if (width <= height) {
//                    height = (int) (mMaxWidth * 1.0f * height / width);
//                    width = mMaxWidth;
//                } else {
//                    width = (int) (mMaxWidth * 1.0f * width / height);
//                    height = mMaxWidth;
//                }
//                if (bitmap != null) {
//                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
//                    addBitmapToCache(mImageKey, bitmap);
//                    callBack(bitmap);
//                    return;
//                }
//                callBack(null);
//            } catch (Exception e) {
//                if (e != null) {
//                    e.printStackTrace();
//                }
//                callBack(null);
//            } finally {
//                try {
//                    if (mmr != null)
//                        mmr.release();
//                } catch (Exception ex) {
//                }
//                try {
//                    mCurrentTasks.remove(mImageKey);
//                } catch (Exception e) {
//                }
            }
//        }
    }


    public void release() {
        if (mExecutorService != null) {
            mExecutorService.shutdownNow();
            mExecutorService = null;
        }
    }

}
