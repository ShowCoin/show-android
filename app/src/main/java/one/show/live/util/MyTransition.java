package one.show.live.util;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.app.Activity;
import android.view.animation.DecelerateInterpolator;

import com.kogitune.activity_transition.core.MoveData;
import com.kogitune.activity_transition.core.TransitionAnimation;

/**
 * Created by Nano on 2017/5/25.
 */

public class MyTransition {

    private final MoveData moveData;
    private TimeInterpolator interpolator;
    private Animator.AnimatorListener listener;


    public MyTransition(MoveData moveData) {
        this.moveData = moveData;
    }

    public MyTransition interpolator(TimeInterpolator interpolator) {
        this.interpolator = interpolator;
        return this;
    }
    public MyTransition exitListener(Animator.AnimatorListener listener) {
        this.listener = listener;
        return this;
    }

    public void exit(final Activity activity) {
        if (interpolator == null) {
            interpolator = new DecelerateInterpolator();
        }
        moveData.duration=300;
        TransitionAnimation.startExitAnimation(moveData, interpolator, new Runnable() {
            @Override
            public void run() {
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }
        },listener);
    }
}
