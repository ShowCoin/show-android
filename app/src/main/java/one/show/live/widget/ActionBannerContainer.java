package one.show.live.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by clarkM1ss1on on 2018/6/12.
 */

public class ActionBannerContainer
        extends FrameLayout {

    private final static String TAG = "ActionBannerContainer";
    final static int MAX_RUNNING_BANNERS = 2;
    private CopyOnWriteArrayList<ActionItemOperator> operators;
    private CopyOnWriteArrayList<BannerData> queue;
    private int counter = 0;

    public ActionBannerContainer(@NonNull Context context) {
        super(context);
        init();
    }

    public ActionBannerContainer(@NonNull Context context
            , @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActionBannerContainer(@NonNull Context context
            , @Nullable AttributeSet attrs
            , int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        operators = new CopyOnWriteArrayList<>();
        queue = new CopyOnWriteArrayList<>();
        setClipChildren(false);
    }

    public void addActionBanner(BannerData data) {
        boolean hasSameItemQueue = false;
        //如果有运行中的横幅队列在响应同一个物品的效果，添加到横幅队列中
        Log.e(TAG, "addActionBanner " + operators.size());
        for (ActionItemOperator operator : operators) {
            if (operator.isSameItem(data)) {
                operator.addData(data);
                hasSameItemQueue = true;
                Log.e(TAG, "has same item queue");
                break;
            }
        }
        //如果没有同一物品横幅队列在运行，则 添加至横幅外队列
        if (!hasSameItemQueue) {
            queue.add(data);
        }
        actionNextItem();
    }

    private void actionNextItem() {
        //加载下一个
        if (operators.size() < MAX_RUNNING_BANNERS
                && queue.size() > 0) {
            final ActionItemOperator operator = new ActionItemOperator(this);
            Log.e(TAG, "add operator inner queue");
            operator.addData(queue.remove(0));
            //取出队列中同样物品的
            if (queue.size() > 0) {
                for (BannerData data : queue) {
                    if (operator.isSameItem(data)) {
                        operator.addData(data);
                        queue.remove(data);
                    }
                }
            }

            operators.add(operator);
            operator.setPerformOffset(applyForPerformOffset());
            operator.actionStart();
            counter++;
        }
    }

    private int applyForPerformOffset() {
        return counter % MAX_RUNNING_BANNERS;
    }

    void onOperatorDone(ActionItemOperator operator) {
        operators.remove(operator);
        actionNextItem();
    }

    public void onPause() {
        for (ActionItemOperator operator : operators) {
            operator.onPause();
        }
    }

    public void onResume() {
        for (ActionItemOperator operator: operators){
            operator.onResume();
        }
    }

    public void recycle(){
        for(ActionItemOperator operator:operators){
            operator.recycle();
        }
    }
}
