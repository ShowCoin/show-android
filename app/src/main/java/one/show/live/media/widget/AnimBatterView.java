package one.show.live.media.widget;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import one.show.live.R;
import one.show.live.common.util.FrescoUtils;
import one.show.live.common.util.WeakHandler;
import one.show.live.media.po.POIMGift;
import one.show.live.log.Logger;
import one.show.live.po.POConfig;


public class AnimBatterView extends RelativeLayout {
    private final int ADD_ANIM = 0x11;
    private final int EXIT_ANIM = 0x12;

    private SimpleDraweeView headerIV, coverIV;
    private TextView nameTV, msgTV, numberTV;
    private int num = 1;
    private int exNum = 1;
    private POIMGift bean;

    private int index = 1;


    public interface OnAnimFinishListener {
        void onAnimationEnd(View view,int index);
    }

    private OnAnimFinishListener listener;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case EXIT_ANIM:
                    startExitAnimation();
                    break;
                case ADD_ANIM:
                    startAddNumAnimation();
                    break;
            }
            return true;
        }
    });

    public AnimBatterView(Context context) {
        super(context);
        init(context);
    }


    public void release(){
        handler.removeCallbacksAndMessages(null);
    }

    public AnimBatterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnimBatterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_anim_batter, this);
        setClipChildren(false);
        headerIV = (SimpleDraweeView) findViewById(R.id.header_iv);
        nameTV = (TextView) findViewById(R.id.name_tv);
        msgTV = (TextView) findViewById(R.id.msg_tv);
        coverIV = (SimpleDraweeView) findViewById(R.id.cover_iv);
        numberTV = (TextView) findViewById(R.id.number_tv);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }


    public POIMGift getGiftBean() {
        return bean;
    }

    /**
     * 设置Bean
     *
     * @param bean 礼物
     */
    public void setGiftBean(POIMGift bean,int index) {
        this.bean = bean;
        this.index = index;
        FrescoUtils.smallReqImage(headerIV, bean.getFromUserImg());
        FrescoUtils.smallReqImage(coverIV, bean.getGiftImg());
        //if (bean.getFromNickName().length() > 9) {
        //    nameTV.setText(String.format("%s...", bean.getFromNickName().substring(0, 7)));
        //} else {
        //    nameTV.setText(bean.getFromNickName());
        //}
            nameTV.setText(bean.getFromNickName());
        num = exNum = bean.getCount();
        Logger.e("samuel","动画num>>"+num);
        numberTV.setText(String.format("x %d", num));
        msgTV.setText(String.format("送了%d个%s", bean.getNum(), bean.getGiftName()));

    }

    /**
     * 添加一个连击动作
     */
    public boolean addBatter(int count) {
        if (numberTV.getAnimation() == null&&num<count) {
            exNum = num;
            num = count;
            startAddNumAnimation();
            return true;
        }
        num = count;
        return true;
    }

    /**
     * 设置动画监听
     *
     * @param listener 动画完成的监听
     */
    public void setOnAnimationFinishListener(OnAnimFinishListener listener) {
        this.listener = listener;
    }

    /**
     * 开始进入动画
     */
    public void startEnterAnimation() {
        Animator enter = AnimatorInflater.loadAnimator(getContext(), R.animator.gift_batter_enter);
        enter.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                handler.sendEmptyMessageDelayed(EXIT_ANIM, 2000);
                handler.sendEmptyMessageDelayed(ADD_ANIM, 100);
            }
        });
        enter.setTarget(this);
        enter.start();
    }

    /**
     * 开始推出动画
     */
    public void startExitAnimation() {
        Animator exit = index == 2?AnimatorInflater.loadAnimator(getContext(), R.animator.gift_batter_exit):AnimatorInflater.loadAnimator(getContext(), R.animator.gift_batter_exit_idx1);
        exit.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onAnimationEnd(AnimBatterView.this,index);
                }
            }
        });
        exit.setTarget(this);
        exit.start();
    }

    /**
     * 开始一个连击动画
     */
    private void startAddNumAnimation() {
        if (exNum >= num) {
            return;
        }

        handler.removeMessages(EXIT_ANIM);
        numberTV.setText(String.format("x %d", ++exNum));

        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.gift_batter_add_num);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                handler.sendEmptyMessageDelayed(ADD_ANIM, 200);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                handler.removeMessages(EXIT_ANIM);
                numberTV.clearAnimation();
                handler.sendEmptyMessageDelayed(EXIT_ANIM, 2000);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        anim.setInterpolator(new OvershootInterpolator());
        numberTV.startAnimation(anim);
    }
}
