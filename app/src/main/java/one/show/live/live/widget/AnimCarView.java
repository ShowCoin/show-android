package one.show.live.live.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import one.show.live.R;
import one.show.live.util.ConvertToUtils;
import one.show.live.util.DeviceUtil;
import one.show.live.live.listener.OnAnimFinishListener;
import one.show.live.live.po.POIMGift;


public class AnimCarView extends RelativeLayout {

    private View carLay;
    private SimpleDraweeView backTyre, carsBody,frontTyre,light,runway;


    public AnimCarView(Context context) {
        super(context);
        init(context);
    }

    public AnimCarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnimCarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.gift_car, this);
        carLay =findViewById(R.id.car_lay);
        runway = (SimpleDraweeView)findViewById(R.id.runway);
        backTyre = (SimpleDraweeView) findViewById(R.id.backTyre);
        carsBody = (SimpleDraweeView) findViewById(R.id.carsBody);
        frontTyre = (SimpleDraweeView) findViewById(R.id.frontTyre);
        light = (SimpleDraweeView) findViewById(R.id.light);
        reset();

//        set2.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                lay.setLayerType(View.LAYER_TYPE_NONE, null);
//            }
//
//            @Override
//            public void onAnimationStart(Animator animation) {
//                lay.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//            }
//        });
    }


    public Animator buildAnimtion(int width,int height){


        int imageWidth = ConvertToUtils.dipToPX(getContext(),240);
        int screenWidth = width;
        int screenHeight = height;

        runway.getLayoutParams().height = screenWidth;

        int startPointOffset = 0;
        int centerPointOffset = ConvertToUtils.dipToPX(getContext(),165);
        int endPointOffset = ConvertToUtils.dipToPX(getContext(),190);

        int runwayLeftTopPointY = screenHeight/2 - screenWidth/2;
        int startY =  runwayLeftTopPointY+ startPointOffset;
        int pointY =runwayLeftTopPointY+centerPointOffset;
        int endY =runwayLeftTopPointY+endPointOffset;

        ObjectAnimator o1 = ObjectAnimator.ofFloat(backTyre, View.ROTATION,0f,1440f);
        ObjectAnimator o2 = ObjectAnimator.ofFloat(frontTyre,View.ROTATION,0F,1440f);
        ObjectAnimator o3 = ObjectAnimator.ofFloat(carLay,View.TRANSLATION_X,-imageWidth,screenWidth/2-imageWidth/2);
        ObjectAnimator o4 = ObjectAnimator.ofFloat(carLay,View.TRANSLATION_Y,startY,pointY);
        ObjectAnimator o5 = ObjectAnimator.ofFloat(carLay, View.SCALE_X, 0.6f, 1.0f);
        ObjectAnimator o6 = ObjectAnimator.ofFloat(carLay, View.SCALE_Y, 0.6f, 1.0f);

        o1.setDuration(1500);
        o2.setDuration(1500);
        o3.setDuration(1500);
        o4.setDuration(1500);
        o5.setDuration(1500);
        o6.setDuration(1500);

        final AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new DecelerateInterpolator());
        set.playTogether(o1,o2,o3,o4,o5,o6);




        PropertyValuesHolder phAlpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f);

//        Keyframe kf0 = Keyframe.ofFloat(0f, 0f);
//        Keyframe kf1 = Keyframe.ofFloat(.4f, 0f);
//        Keyframe kf2 = Keyframe.ofFloat(.6f, 1f);
//        Keyframe kf3 = Keyframe.ofFloat(1f, 1f);
//        PropertyValuesHolder phAlpha = PropertyValuesHolder.ofKeyframe("alpha", kf0, kf1, kf2, kf3);

        ObjectAnimator flashAnimtor =
                ObjectAnimator.ofPropertyValuesHolder(light, phAlpha).setDuration(200);
        flashAnimtor.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                light.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                light.setVisibility(View.VISIBLE);
            }
        });
        flashAnimtor.setRepeatCount(5);
        flashAnimtor.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator o7 = ObjectAnimator.ofFloat(backTyre,View.ROTATION,1440f,2880f);
        ObjectAnimator o8 = ObjectAnimator.ofFloat(frontTyre,View.ROTATION,1440f,2880f);

        ObjectAnimator o9 = ObjectAnimator.ofFloat(carLay,View.TRANSLATION_X,screenWidth/2-imageWidth/2,screenWidth+imageWidth);
        ObjectAnimator o10 = ObjectAnimator.ofFloat(carLay,View.TRANSLATION_Y,pointY,endY);

        o7.setDuration(1500);
        o8.setDuration(1500);
        o9.setDuration(1500);
        o10.setDuration(1500);

        final AnimatorSet set1 = new AnimatorSet();
        set1.setInterpolator(new AccelerateInterpolator());
        set1.playTogether(o7,o8,o9,o10);

        final AnimatorSet set2 = new AnimatorSet();

        set2.playSequentially(set,flashAnimtor,set1);

        return set2;
    }


    public void reset(){
//        backTyre.setRotationY(90f);
//        backTyre.setRotationX(5f);
//        frontTyre.setRotationX(12f);
//        frontTyre.setRotationY(53f);

        backTyre.setRotationY(-70f);
        frontTyre.setRotationX(-5f);
        frontTyre.setRotationY(-58f);

        backTyre.setRotation(0f);
        frontTyre.setRotation(0f);

        carLay.setTranslationX(0f);
        carLay.setTranslationY(0f);
        carLay.setScaleX(0f);
        carLay.setScaleY(0f);

        backTyre.clearAnimation();
        frontTyre.clearAnimation();
        carLay.clearAnimation();
        light.clearAnimation();
    }


    public void setInfo(POIMGift gift) {
        //设置图片
    }



}
