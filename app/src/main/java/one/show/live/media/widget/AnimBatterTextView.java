package one.show.live.media.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

import one.show.live.common.util.ConvertToUtils;

public class AnimBatterTextView extends TextView {
    private static int strokeColor = Color.parseColor("#fbfc00");
    private static int textColor = Color.parseColor("#2abdae");

    public AnimBatterTextView(Context context) {
        super(context);
    }

    public AnimBatterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimBatterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas pCanvas) {
        setTextColor(strokeColor); // your stroke's color
        getPaint().setStrokeWidth(ConvertToUtils.dipToPX(getContext(), 2));
        getPaint().setStyle(Paint.Style.STROKE);
        super.onDraw(pCanvas);

        setTextColor(textColor);
        getPaint().setStrokeWidth(0);
//        getPaint().setShader(new LinearGradient(0, 0, getMeasuredWidth(), 0, Color.BLUE, Color.GREEN, Shader.TileMode.REPEAT));
        getPaint().setStyle(Paint.Style.FILL);
        super.onDraw(pCanvas);


    }
}
