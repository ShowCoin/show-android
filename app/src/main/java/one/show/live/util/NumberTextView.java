package com.dreamer.tv.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
public class NumberTextView extends TextView {
    private int number;

    private int number;

    public NumberTextView(Context context, int number) {
        super(context);
        setNumber(number);
        setTextColor(Color.BLACK);
        setBackgroundColor(Color.WHITE);
        setGravity(Gravity.CENTER);
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
        setText(String.valueOf(number));
    }

    @Override
    public String toString() {
        return "NumberTextView: " + number;
    }
}
