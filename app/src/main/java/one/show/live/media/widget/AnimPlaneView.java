package one.show.live.media.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import one.show.live.R;
import one.show.live.common.util.ConvertToUtils;
import one.show.live.media.po.POIMGift;


public class AnimPlaneView extends RelativeLayout {

    private SimpleDraweeView plane, sky;


    public AnimPlaneView(Context context) {
        super(context);
        init(context);
    }

    public AnimPlaneView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnimPlaneView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.gift_plane, this);
        plane =(SimpleDraweeView)findViewById(R.id.plane);
        sky = (SimpleDraweeView)findViewById(R.id.sky);
        reset();


    }


    public Animator buildAnimtion(int width,int height){

        int imageWidth = ConvertToUtils.dipToPX(getContext(),262);
        int screenWidth = width;
        int screenHeight = height;

        int skyHeight = screenWidth*1380/1080;
        sky.getLayoutParams().height = skyHeight;

        int startPointOffset = ConvertToUtils.dipToPX(getContext(),40);
        int centerPointOffset = ConvertToUtils.dipToPX(getContext(),40);
        int endPointOffset = ConvertToUtils.dipToPX(getContext(),300);

        int runwayLeftTopPointY = screenHeight/2 - skyHeight/2;
        int startY =  runwayLeftTopPointY+ startPointOffset;
        int pointY =runwayLeftTopPointY+centerPointOffset;
        int endY =runwayLeftTopPointY+endPointOffset;

        ObjectAnimator o1 = ObjectAnimator.ofFloat(plane,View.TRANSLATION_X,-imageWidth,screenWidth/2-imageWidth/2);
        ObjectAnimator o2 = ObjectAnimator.ofFloat(plane,View.TRANSLATION_Y,startY,pointY);
        ObjectAnimator o3 = ObjectAnimator.ofFloat(plane, View.SCALE_X, 0.6f, 1.0f);
        ObjectAnimator o4 = ObjectAnimator.ofFloat(plane, View.SCALE_Y, 0.6f, 1.0f);

        o1.setDuration(1500);
        o2.setDuration(1500);
        o3.setDuration(1500);
        o4.setDuration(1500);

        final AnimatorSet set = new AnimatorSet();
        set.setInterpolator(new DecelerateInterpolator());
        set.playTogether(o1,o2,o3,o4);


        ObjectAnimator o5 = ObjectAnimator.ofFloat(plane, View.SCALE_Y, 1.0f, 1.0f);
        o5.setDuration(2000);

        ObjectAnimator o6 = ObjectAnimator.ofFloat(plane,View.TRANSLATION_X,screenWidth/2-imageWidth/2,screenWidth+imageWidth);
        ObjectAnimator o7 = ObjectAnimator.ofFloat(plane,View.TRANSLATION_Y,pointY,endY);

        o6.setDuration(1500);
        o7.setDuration(1500);

        final AnimatorSet set1 = new AnimatorSet();
        set1.setInterpolator(new AccelerateInterpolator());
        set1.playTogether(o6,o7);

        final AnimatorSet set2 = new AnimatorSet();

        set2.playSequentially(set,o5,set1);

        return set2;
    }


    public void reset(){

        plane.setTranslationX(0f);
        plane.setTranslationY(0f);
        plane.setScaleX(0f);
        plane.setScaleY(0f);
        plane.clearAnimation();
    }


    public void setInfo(POIMGift gift) {
        //设置图片
    }



}
