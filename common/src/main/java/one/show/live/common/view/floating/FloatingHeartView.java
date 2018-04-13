package one.show.live.common.view.floating;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Random;

import one.show.live.common.util.ConvertToUtils;
import one.show.live.common.util.UIUtils;

/**
 * 飘心 动画
 * <p/>
 */
public class FloatingHeartView extends RelativeLayout {
    private FloatsUtil floatsUtils;
    private Random random = new Random();
    private LayoutParams mLayoutParams;

    private int mHeight;    //FavorLayout的高度
    private int mWidth;     //FavorLayout的宽度

    private int mHeartSize;

    public FloatingHeartView(Context context) {
        super(context);
        initView(context);
    }

    public FloatingHeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);

    }

    public FloatingHeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        floatsUtils = new FloatsUtil(context);
        mHeartSize = ConvertToUtils.dipToPX(context, 35);
        mLayoutParams = new LayoutParams(mHeartSize, mHeartSize);
        mLayoutParams.addRule(CENTER_HORIZONTAL, TRUE);
        mLayoutParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);

        mWidth = ConvertToUtils.dipToPX(context, 210);
        mHeight = ConvertToUtils.dipToPX(context, 200);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private Drawable getHeartDrawable() {
        return floatsUtils.getFloats(random.nextInt(7));
    }

    public void addFavor() {
        startAnim(getDefaultImage());
    }

    /**
     * 开始动画
     *
     * @param imageView 飘的ImageView
     */
    private void startAnim(final ImageView imageView) {
        addView(imageView);
        final Animator set = getAnimator(imageView);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                imageView.clearAnimation();
                removeView(imageView);
            }
        });
        set.start();
    }


    private ImageView getDefaultImage() {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageDrawable(getHeartDrawable());
        imageView.setLayoutParams(mLayoutParams);
        return imageView;
    }

    /**
     * 获取整体动画
     *
     * @param target 飘荡的心
     * @return 动画
     */
    private Animator getAnimator(View target) {
        AnimatorSet set = getEnterAnimator(target);

        ValueAnimator bezierValueAnimator = getBezierValueAnimator(target);

        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playTogether(set, bezierValueAnimator);
        finalSet.setTarget(target);

        return finalSet;
    }

    /**
     * 实现 alpha以及x,y轴的缩放功能
     *
     * @param target 爱心
     * @return AnimatorSet来
     */
    private AnimatorSet getEnterAnimator(final View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.3f, 1.0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 1.0f);
        alpha.setDuration(500);
        scaleX.setDuration(500);
        scaleY.setDuration(500);

        AnimatorSet mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(alpha, scaleX, scaleY);
        mAnimatorSet.setTarget(target);
        return mAnimatorSet;
    }


    private ValueAnimator getBezierValueAnimator(final View target) {

        int p3X = random.nextInt(mWidth / 4 + 30) + mWidth / 4;

        PointF pointF2 = getPointF(2, p3X - 50, mWidth / 2);

        PointF pointF1 = getPointF(1, (int) (pointF2.x), mWidth / 2);


        PointF pointF0 = new PointF(mWidth / 2 - mHeartSize / 2, mHeight - mHeartSize);

        PointF pointF3 = new PointF(p3X, 0);

        BezierEvaluator mBezierEvalutor = new BezierEvaluator(pointF1, pointF2);

        ValueAnimator animator = ValueAnimator.ofObject(mBezierEvalutor, pointF0, pointF3);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new BezierListener(target));
        animator.setTarget(target);
        animator.setDuration(3000);
        return animator;
    }

    private PointF getPointF(int i, int startx, int endx) {
        if (startx < 0)
            startx = 1;
        PointF pointF = new PointF();
        pointF.x = random.nextInt(endx - startx) + startx;
        //为了美观,建议尽量保证P2在P1上面

        if (i == 1) {
            pointF.y = random.nextInt(mHeight / 2) + mHeight / 2;//P1点Y轴坐标变化
        } else if (i == 2) {//P2点Y轴坐标变化
            pointF.y = random.nextInt(mHeight / 2);
        }

        return pointF;
    }


    private class BezierListener implements ValueAnimator.AnimatorUpdateListener {

        private View target;

        public BezierListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            //获取到贝塞尔曲线计算出来的的x y值 赋值给view 这样就能让爱心随着曲线走啦
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            // 这里顺便做一个alpha动画
            target.setAlpha(1.0f - animation.getAnimatedFraction());
        }
    }


}
