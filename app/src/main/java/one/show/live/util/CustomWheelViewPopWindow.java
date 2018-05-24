package com.dreamer.tv.person.util;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dreamer.tv.R;
import com.dreamer.tv.person.util.wheelview.OnWheelScrollListener;
import com.dreamer.tv.person.util.wheelview.WheelView;
import com.dreamer.tv.person.util.wheelview.adapter.CustomWheelAdapter;

import java.util.List;


public class CustomWheelViewPopWindow<T> extends PopupWindow implements View.OnClickListener {
    private Context context;
    private LayoutInflater mInflater;
    private View dateView;
    private TextView tvCancel;
    private TextView tvDone;
    private LinearLayout wheelViewLayout;
    private List<List<T>> dataList;
    private List<OnWheelScrollListener> scrollingListenerList;
    private OnChooseItemListener onChooseItemListener;

    public CustomWheelViewPopWindow(Context context, List<List<T>> dataList, List<OnWheelScrollListener> scrollingListenerList) {
        this.context = context;
        this.dataList = dataList;
        this.scrollingListenerList = scrollingListenerList;
        initWindow();
    }

    private void initWindow() {
        mInflater = LayoutInflater.from(context);
        dateView = mInflater.inflate(R.layout.custom_wheel_view, null);
        wheelViewLayout = (LinearLayout) dateView.findViewById(R.id.layout_wheel_view);

        tvCancel = (TextView) dateView.findViewById(R.id.tv_datepicker_cancle);
        tvDone = (TextView) dateView.findViewById(R.id.tv_datepicker_done);
        tvCancel.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        initWheel();
    }

    private void initWheel() {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        for (List<T> list : dataList) {
            WheelView wheelView = new WheelView(context);
            CustomWheelAdapter<T> adapter = new CustomWheelAdapter<T>(context, list);
            adapter.setTextSize(14);
            wheelView.setAdatper(adapter);
            wheelView.setCyclic(false);
            LayoutParams layoutParams = new LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;

            wheelView.setLayoutParams(layoutParams);
            wheelViewLayout.addView(wheelView);
            if (scrollingListenerList != null && scrollingListenerList.size() >= wheelViewLayout.getChildCount()) {
                wheelView.addScrollingListener(scrollingListenerList.get(wheelViewLayout.getChildCount() - 1));
            }
            wheelView.addScrollingListener(new OnWheelScrollListener() {
                @Override
                public void onScrollingStarted(WheelView wheel) {

                }

                @Override
                public void onScrollingFinished(WheelView wheel) {

                }
            });
            wheelView.setCurrentItem(0);
            wheelView.setVisibleItems(5);
        }
        setContentView(dateView);
        setWidth(LayoutParams.FILL_PARENT);
        setHeight(LayoutParams.WRAP_CONTENT);
        ColorDrawable dw = new ColorDrawable(0xFFFFFFFF);
        setBackgroundDrawable(dw);
        setFocusable(true);
    }

    public void show(View parent) {
        showAtLocation(parent, Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_datepicker_cancle:
                if (onChooseItemListener != null) {
                    onChooseItemListener.onCanceled();
                }
                this.dismiss();
                break;
            case R.id.tv_datepicker_done:
                Object[] items = new Object[wheelViewLayout.getChildCount()];

                for (int i = 0; i < wheelViewLayout.getChildCount(); i++) {
                    WheelView wheelView = (WheelView) wheelViewLayout.getChildAt(i);
                    T data = ((CustomWheelAdapter<T>) wheelView.getViewAdapter()).getItemData(wheelView.getCurrentItem());
                    items[i] = data;
                }
                if (onChooseItemListener != null) {
                    onChooseItemListener.onChoose(items);
                }
                dismiss();
                break;
        }
    }

    public void setOnChooseItemListener(OnChooseItemListener onChooseItemListener) {
        this.onChooseItemListener = onChooseItemListener;
    }

    public interface OnChooseItemListener {
        void onChoose(Object... content);

        void onCanceled();
    }
}
