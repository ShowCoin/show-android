package one.show.live.live.widget.danmu;

import android.view.animation.Interpolator;

/**
 * 先减速，后加速的Interpolator.
 * Created by hanj on 15-6-4.
 */
public class DecelerateAccelerateInterpolator implements Interpolator {

    //input从0～1，返回值也从0～1.返回值的曲线表征速度加减趋势
    @Override
    public float getInterpolation(float input) {
        //速度会降至0
        if (input < 0.5f) {
            return -2 * input * (input - 1);
        } else {
            return 2 * input * (input - 1) + 1;
        }
    }
}
