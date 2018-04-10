package one.show.live.live.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import one.show.live.R;
import one.show.live.util.ConvertToUtils;
import one.show.live.util.DeviceUtil;
import one.show.live.live.po.POIMGift;


public class AnimShipView extends RelativeLayout {

    private SimpleDraweeView rainbow, waves1_1, waves1_2, waves2_1, waves2_2, waves3;

    private View shipParentLay;

    private View firework1, firework2, firework3;

    private View shipLayout;

    private int screenWidth;
    private int screenHeight;
    private int shipLayWidth;
    private int shipParentLayHeight;


    private WeakReference<AnimationDrawable> fireworAnimDrawable;

    public AnimShipView(Context context) {
        super(context);
        init(context);
    }

    public AnimShipView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnimShipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.gift_ship, this);

        shipParentLay = findViewById(R.id.shipParentLay);

        rainbow = (SimpleDraweeView) findViewById(R.id.rainbow);
        waves1_1 = (SimpleDraweeView) findViewById(R.id.waves1_1);
        waves1_2 = (SimpleDraweeView) findViewById(R.id.waves1_2);


        waves2_1 = (SimpleDraweeView) findViewById(R.id.waves2_1);
        waves2_2 = (SimpleDraweeView) findViewById(R.id.waves2_2);
        waves3 = (SimpleDraweeView) findViewById(R.id.waves3);

        shipLayout = findViewById(R.id.ship_layout);

        firework1 = findViewById(R.id.firework1);
        firework2 = findViewById(R.id.firework2);
        firework3 = findViewById(R.id.firework3);




        screenWidth = DeviceUtil.getScreenSize(getContext()).widthPixels;
        screenHeight = DeviceUtil.getScreenSize(getContext()).heightPixels;
        shipLayWidth = ConvertToUtils.dipToPX(getContext(), 255);

        shipParentLayHeight = shipParentLay.getLayoutParams().height = screenWidth * 741 / 1080;


        firework1.setX(screenWidth / 2 + shipLayWidth / 2 - ConvertToUtils.dipToPX(getContext(), 95));
        firework2.setX(screenWidth / 2 + shipLayWidth / 2 - ConvertToUtils.dipToPX(getContext(), 90));
        firework3.setX(screenWidth / 2 + shipLayWidth / 2 - ConvertToUtils.dipToPX(getContext(), 118));

        int wavesHeight = screenWidth * 576 / 1080;
        waves1_1.getLayoutParams().height = wavesHeight;
        waves1_2.getLayoutParams().height = wavesHeight;

        waves2_1.getLayoutParams().height = wavesHeight;
        waves2_2.getLayoutParams().height = wavesHeight;

        reset();
    }


    public void release(){
        hanlder.removeCallbacksAndMessages(null);
        frameEndHandler.removeCallbacksAndMessages(null);
    }

    public Animator buildAnimtion() {

        ObjectAnimator o1_1 = ObjectAnimator.ofFloat(waves1_1, View.TRANSLATION_X, -screenWidth, 0f);
        ObjectAnimator o1_2 = ObjectAnimator.ofFloat(waves1_2, View.TRANSLATION_X, 0f, screenWidth);

        o1_1.setInterpolator(new LinearInterpolator());
        o1_2.setInterpolator(new LinearInterpolator());
        o1_1.setDuration(5000);
        o1_2.setDuration(5000);


        ObjectAnimator o2_1 = ObjectAnimator.ofFloat(waves2_1, View.TRANSLATION_X, screenWidth, 0f);
        ObjectAnimator o2_2 = ObjectAnimator.ofFloat(waves2_2, View.TRANSLATION_X, 0f, -screenWidth);

        o2_1.setInterpolator(new LinearInterpolator());
        o2_2.setInterpolator(new LinearInterpolator());
        o2_1.setDuration(5000);
        o2_2.setDuration(5000);


        ObjectAnimator o3 = ObjectAnimator.ofFloat(shipLayout, View.TRANSLATION_X, -shipLayWidth, screenWidth / 2 - shipLayWidth / 2);
        o3.setDuration(1500);
        o3.setInterpolator(new DecelerateInterpolator());
        o3.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //播放帧动画，帧动画2s
                showFrameAnim(1);
            }
        });

        //占位动画，等待帧动画结束
        ObjectAnimator o4 = ObjectAnimator.ofFloat(shipLayout, View.ALPHA, 1f, 1f);
        o4.setDuration(2000);


        ObjectAnimator o5 = ObjectAnimator.ofFloat(shipLayout, View.TRANSLATION_X, screenWidth / 2 - shipLayWidth / 2, screenWidth + ConvertToUtils.dipToPX(getContext(), 80));
        o5.setDuration(1500);
        o5.setInterpolator(new AccelerateInterpolator());


        final AnimatorSet set1 = new AnimatorSet();
        set1.setInterpolator(new LinearInterpolator());
        set1.playSequentially(o3, o4, o5);


        PropertyValuesHolder phAlpha = PropertyValuesHolder.ofFloat(View.ALPHA, .5f, 1f);

        //浪花闪烁动画
        ObjectAnimator flashAnimtor =
                ObjectAnimator.ofPropertyValuesHolder(waves3, phAlpha).setDuration(200);
        flashAnimtor.setInterpolator(new LinearInterpolator());
        flashAnimtor.setRepeatCount(25);
        flashAnimtor.setRepeatMode(ValueAnimator.REVERSE);

        final AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(o1_1, o1_2, o2_1, o2_2, set1, flashAnimtor);
        return set2;
    }


    private Handler hanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AnimationDrawable animDrawable = (AnimationDrawable) msg.obj;
            if (animDrawable != null) {
                switch (msg.what) {
                    case 1:
                        firework1.setVisibility(View.VISIBLE);
                        firework1.setBackgroundDrawable(animDrawable);
                        break;
                    case 2:
                        firework2.setVisibility(View.VISIBLE);
                        firework2.setBackgroundDrawable(animDrawable);
                        break;
                    case 3:
                        firework3.setVisibility(View.VISIBLE);
                        firework3.setBackgroundDrawable(animDrawable);
                        break;

                }
                animDrawable.start();
                frameEndHandler.sendMessageDelayed(Message.obtain(frameEndHandler, msg.what), 660);
            }
        }
    };


    private Handler frameEndHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    firework1.setVisibility(View.GONE);
                    showFrameAnim(2);
                    break;
                case 2:
                    firework2.setVisibility(View.GONE);
                    showFrameAnim(3);
                    break;
                case 3:
                    firework3.setVisibility(View.GONE);
                    break;
            }
        }
    };


    private void showFrameAnim(final int index) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                AnimationDrawable animationDrawable = getFireworAnimDrawable();
                if (animationDrawable != null) {
                    hanlder.sendMessage(Message.obtain(hanlder, index, animationDrawable));
                }
            }
        }).start();
    }

    private AnimationDrawable getFireworAnimDrawable() {

//        if (fireworAnimDrawable != null && fireworAnimDrawable.get() != null) {
//            return fireworAnimDrawable.get();
//        }

        fireworAnimDrawable = new WeakReference<>(new AnimationDrawable());

        AnimationDrawable frameAnim = fireworAnimDrawable.get();

        for (int i = 1; i <= 10; i++) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), getContext().getResources().getIdentifier("firework" + i, "drawable", getContext().getPackageName()));
            WeakReference<Drawable> weakReference = new WeakReference<Drawable>(drawable);
            drawable = null;
            frameAnim.addFrame(weakReference.get(), 66);
        }
        frameAnim.setOneShot(true);
        return frameAnim;
    }


    public void reset() {
        waves1_1.setTranslationX(-screenWidth);
        waves1_2.setTranslationX(0f);

        waves2_1.setTranslationX(screenWidth);
        waves2_2.setTranslationX(0f);

        waves3.setAlpha(.5f);
        shipLayout.setTranslationX(-shipLayWidth);

        waves3.clearAnimation();
        shipLayout.clearAnimation();
        waves1_1.clearAnimation();
        waves1_2.clearAnimation();
        waves2_1.clearAnimation();
        waves2_2.clearAnimation();
    }


    public void setInfo(POIMGift gift) {
        //设置图片
    }


}
