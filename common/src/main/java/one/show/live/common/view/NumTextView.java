package one.show.live.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 显示数字的TextView
 */
public class NumTextView extends TextView {
    private double num;
    public NumTextView(Context context) {
        super(context);
    }

    public NumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NumTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setNum(double num) {
        this.num = num;
        setText(formatLikeNum(num));
    }

    public void setNumFor10W(double num) {
        this.num = num;
        setText(formatLikeNumFor10W(num));
    }

    public void addNumFor10W() {
        setText(formatLikeNumFor10W(++num));
    }
    /**
     * 格式数据，如果大约1万的话，显示 x.xx万
     *
     * @param num 多少数
     */
    public static String formatLikeNum(double num) {
        return (num < 10000) ? String.format("%.0f", num) : String.format("%.1f万", num / 10000);
    }

    /**
     * 格式数据，如果大约10万的话，显示 x.xx万
     *
     * @param num 多少数
     */
    public static String formatLikeNumFor10W(double num) {
        return (num < 100000) ? String.format("%.0f", num) : String.format("%.1f万", num / 10000);
    }
}
