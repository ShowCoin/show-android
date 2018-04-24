package one.show.live.media.widget;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.RelativeLayout;


import java.lang.ref.WeakReference;

import one.show.live.media.listener.OnAnimFinishListener;
import one.show.live.media.po.POIMGift;
import one.show.live.media.util.AnimBigFrameConfigs;


/**
 * 大礼物全屏帧动画
 */
public class AnimBigFrameView extends RelativeLayout{

    private AnimBigFrameConfigs configs;

    private AnimationDrawable giftAnim = null;

    private OnAnimFinishListener listener;

    private Context mContext;

    private GiftHandler handler = new GiftHandler(this);

    public AnimBigFrameView(Context context) {
        super(context);
        init(context);
    }

    public AnimBigFrameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        configs = new AnimBigFrameConfigs(context);
    }

    static class GiftHandler extends Handler{

        private final WeakReference<AnimBigFrameView> giftView;

        public GiftHandler(AnimBigFrameView gift) {
            giftView = new WeakReference<AnimBigFrameView>(gift);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            AnimBigFrameView frameView = giftView.get();

            if (frameView!=null && frameView.giftAnim!=null){
                frameView.setBackgroundDrawable(frameView.giftAnim);
            }
            frameView.startFrameAnim();
        }
    }

    public void startFrameAnim(final POIMGift bean){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (bean.getGiftId() == 20){
                    giftAnim = configs.loadAnimationDrawable(AnimBigFrameConfigs.Yellow_Car);
                }
                handler.sendMessage(handler.obtainMessage(0,bean));
            }
        }).start();
    }



    public void setOnAnimationFinishListener(OnAnimFinishListener listener) {
        this.listener = listener;
    }

    private void startFrameAnim(){
        if (giftAnim == null){
            return ;
        }
        giftAnim.start();
        int duration = 0;
        for(int i=0;i<giftAnim.getNumberOfFrames();i++){
            duration += giftAnim.getDuration(i);
        }
        handler.postDelayed(new Runnable() {
            public void run() {
                animationEnd();
            }
        }, duration);
    }


    public void reset(){
        clearAnimation();
        setBackgroundDrawable(null);
        release();
    }

    private void animationEnd(){
        if (listener != null){
            listener.onAnimationEnd(this);
        }
        handler.removeCallbacksAndMessages(null);
    }

    public void release(){
        if(giftAnim!=null){
            giftAnim.stop();
            for (int i = 0; i < giftAnim.getNumberOfFrames(); ++i){
                Drawable frame = giftAnim.getFrame(i);
                if (frame instanceof BitmapDrawable) {
                    ((BitmapDrawable)frame).getBitmap().recycle();
                }
                frame.setCallback(null);
            }
            giftAnim.setCallback(null);
        }

        handler.removeCallbacksAndMessages(null);
    }
}
