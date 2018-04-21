package one.show.live.media.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spanned;
import android.util.AttributeSet;

/**
 * Like {@link com.facebook.drawee.view.DraweeView} that displays drawables {@link DraweeSpan} but surrounded with text.
 *
 * @author yrom
 */
public class DraweeTextView extends android.support.v7.widget.AppCompatTextView {
    public DraweeTextView(Context context) {
        super(context);
    }

    public DraweeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DraweeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private boolean mHasDraweeInText;

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (mHasDraweeInText) {
            onDetach(); // detach all old images
            mHasDraweeInText = false;
        }
        if (text instanceof Spanned) {
            // find DraweeSpan in text
            DraweeSpan[] spans = ((Spanned) text).getSpans(0, text.length(), DraweeSpan.class);
            mHasDraweeInText = spans.length > 0;
        }
        super.setText(text, type);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onAttach();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onDetach();
    }


    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        onDetach();
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        onAttach();
    }

    @Override
    public void invalidateDrawable(Drawable dr) {
        if (mHasDraweeInText) {
            /* invalidate the whole view in this case because it's very
             * hard to know what the bounds of drawables actually is.
             */
            invalidate();
        } else {
            super.invalidateDrawable(dr);
        }
    }

    /**
     * Attach DraweeSpans in text
     */
    final void onAttach() {
        DraweeSpan[] images = getImages();
        for (DraweeSpan image : images) {
            image.onAttach(this);
        }
    }

    private DraweeSpan[] getImages() {
        if (mHasDraweeInText && length() > 0)
            return ((Spanned) getText()).getSpans(0, length(), DraweeSpan.class);
        return new DraweeSpan[0]; //TODO: pool empty typed array
    }


    private MyImageSpan[] getLocalImages() {
        if (length() > 0)
            return ((Spanned) getText()).getSpans(0, length(), MyImageSpan.class);
        return new MyImageSpan[0]; //TODO: pool empty typed array
    }

    /**
     * Detach all of the DraweeSpans in text
     */
    final void onDetach() {

        MyImageSpan[] localImages = getLocalImages();
        for (MyImageSpan image : localImages) {
            Drawable drawable = image.getDrawable();
            // reset callback first
            if (drawable != null) {
                unscheduleDrawable(drawable);
            }
        }

        DraweeSpan[] images = getImages();
        for (DraweeSpan image : images) {
            Drawable drawable = image.getDrawable();
            // reset callback first
            if (drawable != null) {
                unscheduleDrawable(drawable);
            }
            image.onDetach();
        }
    }
}