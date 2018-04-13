package one.show.live.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.AnimationDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrUIHandler;
import in.srain.cube.views.ptr.indicator.PtrIndicator;
import one.show.live.util.UIUtils;
import one.show.live.common.R;

/**
 * Created by apple on 16/8/24.
 */
public class RefreshHeadImage extends RelativeLayout implements PtrUIHandler {

    private static final String TAG = "RefreshHeadImage";

    Context mContext;

    //    ImageView mIvRobot;
    ImageView mIvCircle;
    TextView mTVContent;
    ImageView mIVLoading;

    AnimationDrawable loadingAnim;

//    int[] robotRes = new int[]{R.drawable.dropdown_robot_0, R.drawable.dropdown_robot_1, R.drawable.dropdown_robot_2
//            , R.drawable.dropdown_robot_3, R.drawable.dropdown_robot_4, R.drawable.dropdown_robot_5, R.drawable.dropdown_robot_6
//            , R.drawable.dropdown_robot_7, R.drawable.dropdown_robot_8, R.drawable.dropdown_robot_9, R.drawable.dropdown_robot_10
//            , R.drawable.dropdown_robot_11, R.drawable.dropdown_robot_12, R.drawable.dropdown_robot_13, R.drawable.dropdown_robot_14
//            , R.drawable.dropdown_robot_15, R.drawable.dropdown_robot_16, R.drawable.dropdown_robot_17, R.drawable.dropdown_robot_18
//            , R.drawable.dropdown_robot_19, R.drawable.dropdown_robot_20};

    int max_height_robot;//下拉的时候  显示最后一张机器人图片的高度

    int draw_start_height;//开始画的高度

    int rectangle_height;//矩形的高度


    int draw_start_circle_height = draw_start_height + rectangle_height;//开始画曲线的高度

    private Paint mBackPaint;

    public RefreshHeadImage(Context context) {
        super(context);
        initview(context);
    }

    public void initview(Context context) {
        this.mContext = context;
        inflate(mContext, R.layout.refresh_head_image_new, this);
        setWillNotDraw(false);
//        mIvRobot = (ImageView) findViewById(R.id.refresh_robot);
        mIvCircle = (ImageView) findViewById(R.id.refresh_circle);
        mIVLoading = (ImageView) findViewById(R.id.refresh_loading);
        mTVContent = (TextView) findViewById(R.id.refresh_content);
        loadingAnim = (AnimationDrawable) mIVLoading.getBackground();
        max_height_robot = UIUtils.dip2px(mContext, 100);

        mBackPaint = new Paint();
        mBackPaint.setAntiAlias(true);
        mBackPaint.setStyle(Paint.Style.FILL);
        mBackPaint.setColor(0xFFFEC9BF);
        draw_start_height = UIUtils.dip2px(mContext, 0);
        rectangle_height = UIUtils.dip2px(mContext, 30);
    }

    /**
     * 当内容视图已达到顶部和更新已经完成,视图将被重新设置。
     *
     * @param frame
     */
    @Override
    public void onUIReset(PtrFrameLayout frame) {
//        Log.e(TAG,"onUIReset 刷新完毕");
//        circleAnim.stop();
//        if (mIvRobot.getBackground() instanceof AnimationDrawable) {
//            ((AnimationDrawable) mIvRobot.getBackground()).stop();
//        }
//        mIvRobot.setBackgroundResource(robotRes[0]);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        if (getHeight() > draw_start_height) {
//            startDraw(canvas);
//        }
    }

    private void startDraw(Canvas canvas) {

        if (getHeight() >= draw_start_height + rectangle_height) {
            canvas.drawRect(0, draw_start_height, getWidth(), draw_start_height + rectangle_height, mBackPaint);

            Path path2 = new Path();
            path2.moveTo(0, draw_start_height + rectangle_height);//设置Path的起点
            path2.quadTo(getWidth() / 2, getHeight(), getWidth(), draw_start_height + rectangle_height); //设置贝塞尔曲线的控制点坐标和终点坐标
            canvas.drawPath(path2, mBackPaint);
        } else {
            canvas.drawRect(0, draw_start_height, getWidth(), getHeight(), mBackPaint);
        }

    }

    boolean isRunning;

    private void startAnimUp() {
        if (isRunning) {
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(mIvCircle, View.ROTATION, 0f, -180f);
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isRunning = false;
                if(!isUp){
                    startAnimDown();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    boolean isUp;

    private void startAnimDown() {
        if (isRunning) {
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(mIvCircle, View.ROTATION, -180f, 0f);
        animator.setDuration(300);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isRunning = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isRunning = false;
                if(isUp){
                    startAnimUp();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isRunning = false;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    /**
     * prepare for loading
     * 准备刷新
     *
     * @param frame
     */
    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
//        circleAnim.start();
//        Log.e(TAG,"onUIReset 准备刷新");
    }

    /**
     * perform refreshing UI
     * 执行刷新界面
     *
     * @param frame
     */
    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
//        mIvRobot.setBackgroundResource(R.drawable.refresh_head_image_robit_loading);
//        if (mIvRobot.getBackground() instanceof AnimationDrawable) {
//            ((AnimationDrawable) mIvRobot.getBackground()).start();
//        }
    }

    /**
     * perform UI after refresh
     * 执行用户界面刷新后
     *
     * @param frame
     */
    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
//        getLayoutParams().height = ptrIndicator.getCurrentPosY();
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = ptrIndicator.getCurrentPosY();
        setLayoutParams(params);
//        Log.e(TAG, "onUIPositionChange  isUnderTouch:" + isUnderTouch + "   status:" + status + "  ptrIndicatorY:" + ptrIndicator.getCurrentPosY() + "刷新高度是:" + ptrIndicator.getOffsetToKeepHeaderWhileLoading());
        if (status == PtrFrameLayout.PTR_STATUS_PREPARE) {//下拉中
            mIVLoading.setVisibility(View.GONE);
            mIvCircle.setVisibility(View.VISIBLE);
            if (ptrIndicator.getCurrentPosY() > ptrIndicator.getOffsetToKeepHeaderWhileLoading()) {
                mTVContent.setText("可以了，松手吧");
                if(!isUp){
                    startAnimUp();
                }
                isUp = true;
            } else {
                if(isUp){
                    startAnimDown();
                }
                isUp = false;
                mTVContent.setText("下拉刷新");
            }
        } else if (status == PtrFrameLayout.PTR_STATUS_LOADING) {//下载中
            mTVContent.setText("努力加载中");
            mIVLoading.setVisibility(View.VISIBLE);
            mIvCircle.setVisibility(View.GONE);
            mIvCircle.clearAnimation();
            loadingAnim.start();
        } else if (status == PtrFrameLayout.PTR_STATUS_COMPLETE) {//回弹
            mIVLoading.setVisibility(View.GONE);
            mIvCircle.setVisibility(View.VISIBLE);
            mIvCircle.clearAnimation();
            mTVContent.setText("下拉刷新");
            loadingAnim.stop();
        }
//        if (status == PtrFrameLayout.PTR_STATUS_PREPARE) {//下拉中
//            int position = ptrIndicator.getCurrentPosY() / (max_height_robot / robotRes.length);
//            if (position >= robotRes.length) {
//                mIvRobot.setBackgroundResource(robotRes[robotRes.length - 1]);
//            } else {
//                mIvRobot.setBackgroundResource(robotRes[position]);
//            }
//        }
    }
}
