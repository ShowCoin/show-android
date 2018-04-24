package one.show.live.media.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import one.show.live.common.util.ConvertToUtils;
import one.show.live.common.util.WeakHandler;
import one.show.live.media.po.POIMGift;
import one.show.live.log.Logger;

public class AnimBatterContainer extends FrameLayout implements AnimBatterView.OnAnimFinishListener {

    private static final int ADD_QUEUE = 0x01;

    private ConcurrentLinkedQueue<POIMGift> mQueue = new ConcurrentLinkedQueue<>();
    private ConcurrentHashMap<Integer, Boolean> indexUsedMap = new ConcurrentHashMap<>();

    private int topMargin1;
    private int topMargin2;


    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            addAnim();
            return true;
        }
    });

    public void release() {
        if (mQueue != null) {
            mQueue.clear();
        }
        if (getChildCount() > 0) {
            try {
                for (int i = 0; i < getChildCount(); i++) {
                    AnimBatterView view = (AnimBatterView) getChildAt(i);
                    view.release();
                }
            } catch (Exception e) {
                Logger.e("samuel", "batterview release error");
                e.printStackTrace();
            }
        }
        removeAllViews();
    }

    public AnimBatterContainer(Context context) {
        super(context);
        init(context);
    }

    public AnimBatterContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AnimBatterContainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        topMargin1 = ConvertToUtils.dipToPX(context, 45);
        topMargin2 = ConvertToUtils.dipToPX(context, 100);
        indexUsedMap.put(1, false);
        indexUsedMap.put(2, false);
    }

    /**
     * 添加礼物
     *
     * @param bean 礼物
     */
    public void addGift(POIMGift bean) {
        mQueue.offer(bean);
        mHandler.sendEmptyMessage(ADD_QUEUE);
    }

    private static class BatterBean {
        AnimBatterView batterView;
        int count;

        private BatterBean(AnimBatterView view, int count) {
            this.batterView = view;
            this.count = count;
        }

        private void addBatter() {
            batterView.addBatter(count);
        }
    }


    /**
     * 添加动画
     */
    private void addAnim() {

        POIMGift bean = null;

        synchronized (mQueue) {
            if (!mQueue.isEmpty()) {
                bean = mQueue.poll();
            }
        }

        if (bean == null) {
            return;
        }

        AnimBatterView anim = (AnimBatterView) findViewWithTag(getGiftTag(bean));
        if (anim != null && bean.getCount() > 1) {
            Logger.e("simon", "addBatteraddBatteraddBatteraddBatteraddBatter");
            anim.addBatter(bean.getCount());
        } else {
            if (getChildCount() >= 2) {
                return;
            }

            int index = 0;
            for (int i = 2; i > 0; i--) {
                if (!indexUsedMap.get(i)) {
                    index = i;
                    indexUsedMap.put(index, true);
                    Logger.e("simon", "indexUseMap{" + "index:" + index + ";true");
                    break;
                }
            }
            Logger.e("samuel", "show index>>>>>>" + index);

            if (index == 0)
                return;


            String tag = getGiftTag(bean);
            LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


            AnimBatterView animView = new AnimBatterView(getContext());
            if (index == 1) {
                animView.setY(topMargin1);
            } else {
                animView.setY(topMargin2);
            }
            animView.setTag(tag);
            animView.setGiftBean(bean, index);
            animView.setOnAnimationFinishListener(this);
            animView.setAlpha(0);
            animView.setClipChildren(false);
            addView(animView, params);
            animView.startEnterAnimation();
        }
    }

    @Override
    public void onAnimationEnd(View view, int index) {
        removeView(view);
        indexUsedMap.put(index, false);
        Logger.e("simon", "indexUseMap{" + "index:" + index + ";false");
//        addAnim();
    }

    /**
     * 得到礼物的Tag
     *
     * @param bean 礼物bean
     */
    private String getGiftTag(POIMGift bean) {
        return String.format("%s%d", bean.getFromUid(), bean.getGiftId());
    }
}
