package one.show.live.common.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * 动画工具类
 */
public class AnimUtil {

    /**
     * 动画显示隐藏View
     *
     * @param view     要动画的View
     * @param isHide   是否隐藏
     * @param duration 动画时间
     */
    public static void hideView(final View view, final boolean isHide, long duration) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.ALPHA, isHide ? 0 : 1);
        animator.setDuration(duration);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(isHide ? View.GONE : View.VISIBLE);
            }
        });
        animator.start();
    }
}
