/*
 * Copyright (C) 2016 Bilibili
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package one.show.live.live.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.style.DynamicDrawableSpan;
import android.util.Log;
import android.view.View;

import com.facebook.common.executors.UiThreadImmediateExecutorService;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.datasource.DataSubscriber;
import com.facebook.drawable.base.DrawableWithCaches;
import com.facebook.drawee.components.DeferredReleaser;
import com.facebook.drawee.drawable.ForwardingDrawable;
import com.facebook.drawee.drawable.OrientedDrawable;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.image.CloseableStaticBitmap;
import com.facebook.imagepipeline.request.ImageRequest;

import one.show.live.BuildConfig;
import one.show.live.util.ConvertToUtils;
import one.show.live.util.FrescoUtils;

/**
 * Like {@link com.facebook.drawee.interfaces.DraweeHierarchy} that displays a placeholder
 * until actual image is set.
 * <p/>
 * Usage in DraweeTextView's text.
 *
 * @author yrom
 */
public class DraweeSpan extends DynamicDrawableSpan implements DeferredReleaser.Releasable {


    private int width,height;

    private static Drawable createEmptyDrawable() {
        ColorDrawable d = new ColorDrawable(Color.TRANSPARENT);
        d.setBounds(0, 0, 69, 69);
        return d;
    }

    private final DeferredReleaser mDeferredReleaser;
    private final ForwardingDrawable mActualDrawable;
    private CloseableReference<CloseableImage> mFetchedImage;
    private DataSource<CloseableReference<CloseableImage>> mDataSource;
    private boolean mIsRequestSubmitted;
    private Drawable mDrawable;
    private Drawable mPlaceHolder;
    private View mAttachedView;
    private String mImageUri;
    private boolean mIsAttached;

    /**
     * @see ImageRequest#fromUri(String)
     * @see com.facebook.common.util.UriUtil
     */
    public DraweeSpan(String uri) {
        this(uri, createEmptyDrawable());
    }

    public DraweeSpan(String uri, Drawable placeHolder) {
        super(ALIGN_BOTTOM);
        mImageUri = uri;
        mDeferredReleaser = DeferredReleaser.getInstance();
        mPlaceHolder = placeHolder;
        // create forwarding drawable with placeholder
        mActualDrawable = new ForwardingDrawable(mPlaceHolder);
        Rect bounds = mPlaceHolder.getBounds();
        if (bounds.right == 0 || bounds.bottom == 0) {
            mActualDrawable.setBounds(0, 0, mPlaceHolder.getIntrinsicWidth(), mPlaceHolder.getIntrinsicHeight());
        } else {
            mActualDrawable.setBounds(bounds);
        }
    }

    @Override
    public Drawable getDrawable() {
        return mActualDrawable;
    }


    public int getSize(Paint paint, CharSequence text, int start, int end,
                       Paint.FontMetricsInt fm) {
            Rect rect = mPlaceHolder.getBounds();
            if (fm != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;
                int drHeight = rect.bottom - rect.top;

                int top = drHeight / 2 - fontHeight / 4;
                int bottom = drHeight / 2 + fontHeight / 4;

                fm.ascent = -bottom;
                fm.top = -bottom;
                fm.bottom = top;
                fm.descent = top;
            }
            return rect.right;
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        Drawable b = getDrawable();
        if(b!=null) {
            canvas.save();
            int transY = 0;
            transY = ((bottom - top) - b.getBounds().bottom) / 2 + top;
            canvas.translate(x, transY);
            b.draw(canvas);
            canvas.restore();
        }else{
            super.draw(canvas,text,start,end,x,top,y,bottom,paint);
        }
    }
    public void setImageWithIntrinsicBounds(Drawable drawable) {
        if (mDrawable != drawable) {
            releaseDrawable(mDrawable);
            mActualDrawable.setDrawable(drawable);
            mDrawable = drawable;
        }
    }

    public void reset() {
        mActualDrawable.setDrawable(mPlaceHolder);
    }

    /**
     * set bounds
     */
    public DraweeSpan setSize(int width, int height) {
        this.width = width;
        this.height = height;
        mActualDrawable.setBounds(0, 0, width, height);
        return this;
    }

    public void onAttach(@NonNull View view) {
        mIsAttached = true;
        if (mAttachedView != view) {
            mActualDrawable.setCallback(null);
            if (mAttachedView != null) {
                throw new IllegalStateException("has been attached to view:" + mAttachedView);
            }
            mAttachedView = view;
            mActualDrawable.setCallback(mAttachedView);
        }
        mDeferredReleaser.cancelDeferredRelease(this);
        if (!mIsRequestSubmitted) {
            try {
                ImagePipelineFactory.getInstance();
            } catch (NullPointerException e) {
                // Image pipeline is not initialized
                ImagePipelineFactory.initialize(mAttachedView.getContext().getApplicationContext());
            }
            submitRequest();
        }
    }

    private void submitRequest() {
        mIsRequestSubmitted = true;
        final String id = getId();

        mDataSource = ImagePipelineFactory.getInstance().getImagePipeline()
                .fetchDecodedImage(FrescoUtils.buildSmallRequest(FrescoUtils.getUri(getImageUri()),width,height), null);
        DataSubscriber<CloseableReference<CloseableImage>> subscriber
                = new BaseDataSubscriber<CloseableReference<CloseableImage>>() {
            @Override
            protected void onNewResultImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                boolean isFinished = dataSource.isFinished();
                CloseableReference<CloseableImage> result = dataSource.getResult();
                if (result != null) {
                    onNewResultInternal(id, dataSource, result, isFinished);
                } else if (isFinished) {
                    onFailureInternal(id, dataSource, new NullPointerException(), /* isFinished */ true);
                }
            }

            @Override
            protected void onFailureImpl(DataSource<CloseableReference<CloseableImage>> dataSource) {
                onFailureInternal(id, dataSource, dataSource.getFailureCause(), /* isFinished */ true);
            }
        };
        mDataSource.subscribe(subscriber, UiThreadImmediateExecutorService.getInstance());
    }

    @NonNull
    protected String getImageUri() {
        return mImageUri;
    }

    protected String getId() {
        return String.valueOf(getImageUri().hashCode());
    }

    private void onFailureInternal(String id,
            DataSource<CloseableReference<CloseableImage>> dataSource,
            Throwable throwable, boolean isFinished) {
        if (BuildConfig.DEBUG) {
            Log.e("ImageSpan", id + " load failure", throwable);
        }
        // ignored this result
        if (!getId().equals(id)
                || dataSource != mDataSource
                || !mIsRequestSubmitted) {
            dataSource.close();
            return;
        }
        if (isFinished) {
            mDataSource = null;
            // Set the previously available image if available.
            if (mDrawable != null) {
                mActualDrawable.setDrawable(mDrawable);
            }
        }
    }

    private void onNewResultInternal(String id,
            DataSource<CloseableReference<CloseableImage>> dataSource,
            CloseableReference<CloseableImage> result,
            boolean isFinished) {
        // ignored this result
        if (!getId().equals(id)
                || dataSource != mDataSource
                || !mIsRequestSubmitted) {
            CloseableReference.closeSafely(result);
            dataSource.close();
            return;
        }
        Drawable drawable;
        try {
            drawable = createDrawable(result);
        } catch (Exception exception) {
            CloseableReference.closeSafely(result);
            onFailureInternal(id, dataSource, exception, isFinished);
            return;
        }
        CloseableReference previousImage = mFetchedImage;
        Drawable previousDrawable = mDrawable;
        mFetchedImage = result;
        try {
            // set the new image
            if (isFinished) {
                mDataSource = null;
                setImageWithIntrinsicBounds(drawable);
            }
        } finally {
            if (previousDrawable != null && previousDrawable != drawable) {
                releaseDrawable(previousDrawable);
            }
            if (previousImage != null && previousImage != result) {
                CloseableReference.closeSafely(previousImage);
            }
        }
    }

    private Drawable createDrawable(CloseableReference<CloseableImage> result) {
        CloseableImage closeableImage = result.get();
        if (closeableImage instanceof CloseableStaticBitmap) {
            CloseableStaticBitmap closeableStaticBitmap = (CloseableStaticBitmap) closeableImage;
            BitmapDrawable bitmapDrawable = createBitmapDrawable(closeableStaticBitmap.getUnderlyingBitmap());
            return (closeableStaticBitmap.getRotationAngle() != 0 && closeableStaticBitmap.getRotationAngle() != -1
                    ? new OrientedDrawable(bitmapDrawable, closeableStaticBitmap.getRotationAngle()) : bitmapDrawable);
        }
//        else if (closeableImage instanceof CloseableAnimatedImage) {
//            AnimatedImageResult image = ((CloseableAnimatedImage) closeableImage).getImageResult();
//            int frame = image.getFrameForPreview();
//            CloseableReference<Bitmap> bitmap;
//            if (frame >= 0) {
//                bitmap = image.getDecodedFrame(frame);
//            } else {
//                bitmap = image.getPreviewBitmap();
//            }
//            if (bitmap != null && bitmap.get() != null) {
//                BitmapDrawable bitmapDrawable = createBitmapDrawable(bitmap.get());
//                return bitmapDrawable;
//            }
//        }
        throw new UnsupportedOperationException("Unrecognized image class: " + closeableImage);
    }

    protected BitmapDrawable createBitmapDrawable(Bitmap bitmap) {
        BitmapDrawable drawable;
        if (mAttachedView != null) {
            final Context context = mAttachedView.getContext();
            drawable = new BitmapDrawable(context.getResources(), bitmap);
        } else {
            // can't happen
            drawable = new BitmapDrawable(null, bitmap);
        }
        return drawable;
    }

    public void onDetach() {
        if (!mIsAttached)
            return;
        mActualDrawable.setCallback(null);
        mAttachedView = null;
        reset();
        mDeferredReleaser.scheduleDeferredRelease(this);
    }

    @Override
    public void release() {
        mIsRequestSubmitted = false;
        mIsAttached = false;
        mAttachedView = null;
        if (mDataSource != null) {
            mDataSource.close();
            mDataSource = null;
        }
        if (mDrawable != null) {
            releaseDrawable(mDrawable);
        }
        mDrawable = null;
        if (mFetchedImage != null) {
            CloseableReference.closeSafely(mFetchedImage);
            mFetchedImage = null;
        }
    }

    void releaseDrawable(@Nullable Drawable drawable) {
        if (drawable instanceof DrawableWithCaches) {
            ((DrawableWithCaches) drawable).dropCaches();
        }
    }
}