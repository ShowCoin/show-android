package one.show.live.live.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.concurrent.ConcurrentLinkedQueue;

import one.show.live.util.DeviceUtil;
import one.show.live.live.listener.OnAnimFinishListener;
import one.show.live.live.po.POIMGift;
import one.show.live.log.Logger;


/**
 * 大动画
 */
public class BigAnimContainer extends RelativeLayout implements OnAnimFinishListener {

    private ConcurrentLinkedQueue<POIMGift> list = new ConcurrentLinkedQueue();

    private volatile boolean isRunning;

    private Context mContext;


    private AnimCarView animCarView;
    private Animator mCarAnimator;


    private AnimShipView shipView;
    private Animator mShipAnimator;


    private AnimPlaneView planeView;
    private Animator mPlaneAnimator;

    private AnimBigFrameView bigFrameView;


    public BigAnimContainer(Context context) {
        super(context);
        init(context);
    }

    public BigAnimContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BigAnimContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
    }

    public void addGift(POIMGift gift) {
        if (checkGiftSource(gift)) {
            list.offer(gift);
            addAnim();
        }
    }

    /**
     * 检查礼物资源，开启任务下载
     *
     * @param gift
     * @return
     */
    private boolean checkGiftSource(POIMGift gift) {
//        return GiftSourceUtils.checkNeedDownloadGift(getContext(),gift);
        return true;
    }

    /**
     * 添加动画
     */
    private void addAnim() {
        if (isRunning) {
            return;
        }
        if (list.size() < 1) {
            return;
        }

        isRunning = true;

        POIMGift gift = list.poll();
        int type = gift.getGiftType();

        switch (type) {
            case 4://汽车
                addCarAnim(gift);
                break;
            case 5://飞机
                addPlaneAnim(gift);
                break;
            case 6://轮船
                addShipAnim(gift);
                break;
            case 7://帧动画
                addBigFrameAnim(gift);
                break;
        }
    }

    @Override
    public synchronized void onAnimationEnd(View view) {
        Logger.e("samuel","轮船动画结束");
        view.clearAnimation();
        isRunning = false;
        try {
//            if (((Integer) view.getTag()) == 4) {
//                removeView(view);
//            }else{
                view.setVisibility(View.GONE);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        addAnim();
    }


    public void release(){
        if(mCarAnimator != null){
            mCarAnimator.cancel();
        }
        if(mPlaneAnimator != null){
            mPlaneAnimator.cancel();
        }
        if(mShipAnimator != null){
            mShipAnimator.cancel();
        }
        if(shipView!=null){
            shipView.release();
        }

        if(bigFrameView!=null){
            bigFrameView.release();
        }

        removeAllViews();
    }

    private void addCarAnim(POIMGift bean) {
        if(animCarView==null||mCarAnimator==null) {
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            animCarView = new AnimCarView(mContext);
            params.addRule(ALIGN_PARENT_BOTTOM);
            addView(animCarView, params);
        }else{
            animCarView.reset();
        }

        int width = DeviceUtil.getScreenSize(getContext()).widthPixels;
        int height = DeviceUtil.getScreenSize(getContext()).heightPixels;
        mCarAnimator = animCarView.buildAnimtion(width,height);
        mCarAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                BigAnimContainer.this.onAnimationEnd(animCarView);
            }
        });


        animCarView.setInfo(bean);
        animCarView.setVisibility(View.VISIBLE);
        mCarAnimator.start();
    }


    private void addPlaneAnim(POIMGift bean) {
        if(planeView==null) {
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            planeView = new AnimPlaneView(mContext);
            params.addRule(ALIGN_PARENT_BOTTOM);
            addView(planeView, params);
        }else{
            planeView.reset();
        }

        int width = DeviceUtil.getScreenSize(getContext()).widthPixels;
        int height = DeviceUtil.getScreenSize(getContext()).heightPixels;
        mPlaneAnimator = planeView.buildAnimtion(width,height);
        mPlaneAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                BigAnimContainer.this.onAnimationEnd(planeView);
            }
        });


        planeView.setInfo(bean);
        planeView.setVisibility(View.VISIBLE);
        mPlaneAnimator.start();
    }


    private void addShipAnim(POIMGift bean) {
        if(shipView==null||mShipAnimator==null) {
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(ALIGN_PARENT_BOTTOM);
            shipView = new AnimShipView(mContext);
            addView(shipView, params);
        }else{
            shipView.reset();
        }

        mShipAnimator = shipView.buildAnimtion();
        mShipAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                BigAnimContainer.this.onAnimationEnd(shipView);
            }
        });

        shipView.setInfo(bean);
        shipView.setVisibility(View.VISIBLE);
        mShipAnimator.start();
    }


    private void addBigFrameAnim(POIMGift bean) {
        if(bigFrameView==null) {
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(ALIGN_PARENT_BOTTOM);
            bigFrameView = new AnimBigFrameView(mContext);
            addView(shipView, params);
        }else{
            bigFrameView.reset();
        }

        bigFrameView.setOnAnimationFinishListener(new OnAnimFinishListener() {
            @Override
            public void onAnimationEnd(View view) {
                BigAnimContainer.this.onAnimationEnd(bigFrameView);
            }
        });

        bigFrameView.setVisibility(View.VISIBLE);
        bigFrameView.startFrameAnim(bean);
    }

}
