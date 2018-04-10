package one.show.live.live.widget;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.facebook.drawee.view.SimpleDraweeView;

import one.show.live.R;
import one.show.live.util.FrescoUtils;
import one.show.live.live.listener.OnAnimFinishListener;
import one.show.live.po.GiftBean;


/**
 * 连击的动画
 */
public class AnimPopView extends RelativeLayout {
    private final int EXIT_ANIM = 0x11;

    private SimpleDraweeView headerIV, coverIV;
    private TextView msgTV;
    private int num = 1;

    private OnAnimFinishListener listener;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case EXIT_ANIM:
                    startExitAnimation();
                    break;
            }
            return true;
        }
    });

    public AnimPopView(Context context) {
        super(context);
        init(context);
    }

    public AnimPopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnimPopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_anim_pop, this);
        setClipChildren(false);
        headerIV = (SimpleDraweeView) findViewById(R.id.header_iv);
        msgTV = (TextView) findViewById(R.id.msg_tv);
        coverIV = (SimpleDraweeView) findViewById(R.id.cover_iv);
    }

    /**
     * 设置Bean
     *
     * @param bean 礼物
     */
    public void setInfo(GiftBean bean) {
        headerIV.setImageURI(FrescoUtils.getUri(bean.getAvatar()));
        coverIV.setImageURI(FrescoUtils.getUri(bean.getCover()));
        String msg = String.format("%s  送了一个%s", bean.getNickname(), bean.getName());
        msgTV.setText(msg);
    }

    public void setOnAnimationFinishListener(OnAnimFinishListener listener) {
        this.listener = listener;
    }

    /**
     * 开始动画
     */
    public void startEnterAnimation() {

        Animator enter = AnimatorInflater.loadAnimator(getContext(), R.animator.gift_pop_enter);
        enter.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                handler.sendEmptyMessageDelayed(EXIT_ANIM, 1000);
            }
        });
        enter.setInterpolator(new OvershootInterpolator());
        enter.setTarget(this);
        enter.start();
    }

    public void startExitAnimation() {
        Animator enter = AnimatorInflater.loadAnimator(getContext(), R.animator.gift_pop_exit);
        enter.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onAnimationEnd(AnimPopView.this);
                }
            }
        });
        enter.setTarget(this);
        enter.start();
    }

}
