package com.dreamer.tv.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
public class Filter  {
    private List<SubFilter> subFilters = new ArrayList<>();

    public Filter(Filter filter) {
        this.subFilters = filter.subFilters;
    }

    public Filter() {
    }

    /**
     * Adds a Subfilter to the Main Filter
     *
     * @param subFilter Subfilter like contrast, brightness, tone Curve etc. subfilter
     * @see BrightnessSubfilter
     * @see ColorOverlaySubfilter
     * @see ContrastSubfilter
     * @see ToneCurveSubfilter
     * @see VignetteSubfilter
     * @see com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter
     */
    public void addSubFilter(SubFilter subFilter) {
        subFilters.add(subFilter);
    }

    /**
     * Clears all the subfilters from the Parent Filter
     */
    public void clearSubFilters() {
        subFilters.clear();
    }

    /**
     * Removes the subfilter containing Tag from the Parent Filter
     */
    public void removeSubFilterWithTag(String tag) {
        Iterator<SubFilter> iterator = subFilters.iterator();
        while (iterator.hasNext()) {
            SubFilter subFilter = iterator.next();
            if (subFilter.getTag().equals(tag)) {
                iterator.remove();
            }
        }
    }

    /**
     * Returns The filter containing Tag
     */
    public SubFilter getSubFilterByTag(String tag) {
        for (SubFilter subFilter : subFilters) {
            if (subFilter.getTag().equals(tag)) {
                return subFilter;
            }
        }
        return null;
    }

    /**
     * Give the output Bitmap by applying the defined filter
     *
     * @param inputImage Input Bitmap on which filter is to be applied
     * @return filtered Bitmap
     */
    public Bitmap processFilter(Bitmap inputImage) {
        Bitmap outputImage = inputImage;
        if (outputImage != null) {
            for (SubFilter subFilter : subFilters) {
                try {
                    outputImage = subFilter.process(outputImage);
                } catch (OutOfMemoryError oe) {
                    System.gc();
                    try {
                        outputImage = subFilter.process(outputImage);
                    } catch (OutOfMemoryError ignored) {
                    }
                }
            }
        }

        return outputImage;
    }
}
