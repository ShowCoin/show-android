package one.show.live.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class GiftAnimationUtil {

    /**
     * @param target
     * @param star
     * @param end
     * @param duration
     * @param startDelay
     * @return 向上飞 淡出
     */
    public static ObjectAnimator createFadeAnimator(final View target, float star, float end, int duration, int startDelay) {

        PropertyValuesHolder translationY = PropertyValuesHolder.ofFloat("translationY", star, end);
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, translationY, alpha);
        animator.setStartDelay(startDelay);
        animator.setDuration(duration);
        animator.setInterpolator(new AccelerateInterpolator());
        return animator;
    }


    /**
     * @param target
     * @return 送礼数字变化
     */
    public static ObjectAnimator scaleGiftNum(final View target) {
        PropertyValuesHolder anim4 = PropertyValuesHolder.ofFloat("scaleX",
                1.2f, 0.8f, 1f);
        PropertyValuesHolder anim5 = PropertyValuesHolder.ofFloat("scaleY",
                1.2f, 0.8f, 1f);
        PropertyValuesHolder anim6 = PropertyValuesHolder.ofFloat("alpha",
                1.0f, 0f, 1f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(target, anim4, anim5, anim6).setDuration(300);
        return animator;

    }

    /**
     * 按钮点击效果
     *
     * @param view
     * @param listenerAdapter
     * @return
     */
    public static ObjectAnimator startClickAnimator(View view, AnimatorListenerAdapter listenerAdapter) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 1f,
                0.95f, 1.1f, 0.96f, 1.05f, 0.98f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 1f,
                0.95f, 1.1f, 0.96f, 1.05f, 0.98f, 1f);
        ObjectAnimator objectAnimator = ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY)
                .setDuration(500);
        if (listenerAdapter != null) {
            objectAnimator.addListener(listenerAdapter);
        }
        objectAnimator.start();

        return objectAnimator;
    }


    // 关注动画
    public static void animT(final ImageView img1, final ImageView img2, final RelativeLayout imgLay) {
        int time = 500;

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(img1, "scaleY", 1f, 1.1f);//纵向放大1.1倍
        scaleY.setDuration(time);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(img1, "scaleX", 1f, 1.1f);//横向放大1.1倍
        scaleX.setDuration(time);
        AnimatorSet setScale = new AnimatorSet();
        setScale.play(scaleY).with(scaleX);//设置放大同时执行


        ObjectAnimator rotate0 = ObjectAnimator.ofFloat(img1, "rotation", 0f, 180f);//设置旋转180度
        rotate0.setDuration(time);
        AnimatorSet set1 = new AnimatorSet();
        set1.play(setScale).with(rotate0);//设置旋转和放大同时执行


        ObjectAnimator animator1 = ObjectAnimator.ofFloat(img2, "alpha", 1f, 0f);//设置渐隐
        animator1.setDuration(time);
        ObjectAnimator rotate1 = ObjectAnimator.ofFloat(img2, "rotation", 0f, 180f);//设置旋转180度
        rotate1.setDuration(time);
        AnimatorSet set2 = new AnimatorSet();
        set2.play(animator1).with(rotate1);//设置渐隐和旋转同时执行


        AnimatorSet set = new AnimatorSet();
        set.play(set1).with(set2);//设置上面两个动画同时执行
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endAnim(img1,img2,imgLay);
            }
        });

    }

    private static void endAnim(final ImageView img1, final ImageView img2, final RelativeLayout imgLay) {
        int time = 400;
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imgLay, "scaleY", 1f, 0f);
        scaleY.setDuration(time);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imgLay, "scaleX", 1f, 0f);
        scaleX.setDuration(time);
        AnimatorSet setScale = new AnimatorSet();
        setScale.play(scaleY).with(scaleX);
        setScale.setStartDelay(200);//设置延时1秒执行
        setScale.start();
        setScale.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                imgLay.setVisibility(View.GONE);
                isf(img1,img2,imgLay);
            }
        });
    }


    private static void  isf(final ImageView img1, final ImageView img2, final RelativeLayout imgLay){
        int time = 100;

        ObjectAnimator scaleY = ObjectAnimator.ofFloat(img1, "scaleY", 1.1f, 1f);//纵向放大1.1倍
        scaleY.setDuration(time);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(img1, "scaleX", 1.1f, 1f);//横向放大1.1倍
        scaleX.setDuration(time);
        AnimatorSet setScale = new AnimatorSet();
        setScale.play(scaleY).with(scaleX);//设置放大同时执行


        ObjectAnimator rotate0 = ObjectAnimator.ofFloat(img1, "rotation", 180f, 0f);//设置旋转180度
        rotate0.setDuration(time);
        AnimatorSet set1 = new AnimatorSet();
        set1.play(setScale).with(rotate0);//设置旋转和放大同时执行


        ObjectAnimator animator1 = ObjectAnimator.ofFloat(img2, "alpha", 0f, 1f);//设置渐隐
        animator1.setDuration(time);
        ObjectAnimator rotate1 = ObjectAnimator.ofFloat(img2, "rotation", 180f, 0f);//设置旋转180度
        rotate1.setDuration(time);
        AnimatorSet set2 = new AnimatorSet();
        set2.play(animator1).with(rotate1);//设置渐隐和旋转同时执行


        AnimatorSet set = new AnimatorSet();
        set.play(set1).with(set2);//设置上面两个动画同时执行
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isfendAnim(imgLay);
            }
        });

    }
    private static void isfendAnim( final RelativeLayout imgLay) {
        int time = 100;
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(imgLay, "scaleY", 0f, 1f);
        scaleY.setDuration(time);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(imgLay, "scaleX", 0f, 1f);
        scaleX.setDuration(time);
        AnimatorSet setScale = new AnimatorSet();
        setScale.play(scaleY).with(scaleX);
        setScale.start();

    }

}
