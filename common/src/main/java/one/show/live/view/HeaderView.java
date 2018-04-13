package one.show.live.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import one.show.live.util.ConvertToUtils;
import one.show.live.util.DeviceUtils;
import one.show.live.common.R;


/**
 * 头部View
 */
public class HeaderView extends RelativeLayout {


    private int dp10;
    private int dp50;
    private Context context;

    public HeaderView(Context context) {
        super(context);
        initView(context);
    }

    public HeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public HeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        if (getId() <= 0) {
            setId(R.id.header_view);
        }
        this.context = context;
        dp10 = ConvertToUtils.dipToPX(getContext(),10);
        dp50 = ConvertToUtils.dipToPX(getContext(),50);

    }

    public TextView setTitle(CharSequence title) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.setMargins(DeviceUtils.dipToPX(context,50),0,DeviceUtils.dipToPX(context,50),0);
        TextView textView = new TextView(getContext());
        textView.setText(title);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(16);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(ContextCompat.getColor(context,R.color.head_title_color));
        addView(textView);
        return textView;
    }
    public TextView getTitle(CharSequence title) {//将添加的title控件返回出去
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.setMargins(DeviceUtils.dipToPX(context,50),0,DeviceUtils.dipToPX(context,50),0);
        TextView textView = new TextView(getContext());
        textView.setText(title);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(16);
        textView.setMaxLines(1);
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setTextColor(ContextCompat.getColor(context,R.color.whiteColor));
        addView(textView);
        return textView;
    }

    public void setLeftButton(String title) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        Button button = new Button(getContext());
        button.setId(R.id.left_btn);
        button.setPadding(dp10,0,dp10,0);
        button.setText(title);
        button.setLayoutParams(params);
        button.setGravity(Gravity.CENTER);
        button.setTextSize(15);
        button.setTextColor(Color.WHITE);
        addView(button);
    }


    public void setLeftButton(@DrawableRes int resId) {
        setLeftButton(resId, null);
    }

    public void setLeftButton(@DrawableRes int resId, OnClickListener l) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);

        ImageButton button = new ImageButton(getContext());
        button.setId(R.id.left_btn);
        button.setLayoutParams(params);
        button.setPadding(dp10,0,dp10,0);
        button.setImageResource(resId);
        button.setScaleType(ImageView.ScaleType.CENTER);
        button.setBackgroundResource(0);
        button.setOnClickListener(l != null ? l : new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
        addView(button);
    }

    public ImageButton setLeftButtonTwo(@DrawableRes int resId, OnClickListener l) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(dp50,0,0,0);

        ImageButton button = new ImageButton(getContext());
        button.setId(R.id.left_btn_two);
        button.setLayoutParams(params);
        button.setPadding(dp10,0,dp10,0);
        button.setImageResource(resId);
        button.setScaleType(ImageView.ScaleType.CENTER);
        button.setBackgroundResource(0);
        button.setOnClickListener(l != null ? l : new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
        addView(button);
        return button;
    }

    public void setRightButton(String title, OnClickListener l) {
        setRightButton(title, LayoutParams.WRAP_CONTENT, l);
    }

    public void setRightPublishButton(String title, OnClickListener l) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        Button button = new Button(getContext());
        button.setId(R.id.right_btn);
        button.setText(title);
        button.setLayoutParams(params);
        button.setGravity(Gravity.CENTER);
        button.setTextSize(17);
        button.setTextColor(Color.argb(255, 0xf9, 0x74, 0x3a));
        button.setBackgroundResource(0);
        button.setOnClickListener(l);
        addView(button);
    }

    public void setRightButton(String title, int width, OnClickListener l) {
        LayoutParams params = new LayoutParams(width, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        Button button = new Button(getContext());
        button.setId(R.id.right_btn);
        button.setText(title);
        button.setLayoutParams(params);
        button.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        button.setPadding(dp10,0,dp10,0);
        button.setTextSize(14);
        button.setTextColor(ContextCompat.getColor(context,R.color.color_b6b6b6));
        button.setBackgroundResource(0);
        button.setOnClickListener(l);
        addView(button);
    }

    public void setRightButton(String title, int width, OnClickListener l,int color) {
        LayoutParams params = new LayoutParams(width, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        Button button = new Button(getContext());
        button.setId(R.id.right_btn);
        button.setText(title);
        button.setLayoutParams(params);
        button.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        button.setPadding(dp10,0,dp10,0);
        button.setTextSize(14);
        button.setTextColor(ContextCompat.getColor(context,color));
        button.setBackgroundResource(0);
        button.setOnClickListener(l);
        addView(button);
    }
    public void setRightText(@DrawableRes int resId,String title, OnClickListener l) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        Drawable nav_up=ContextCompat.getDrawable(context,resId);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());


        TextView textView = new TextView(getContext());
        textView.setId(R.id.right_btn);
        textView.setText(title);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        textView.setPadding(dp10,0,dp10,0);
        textView.setTextSize(15);
        textView.setTextColor(ContextCompat.getColor(context,R.color.whiteColor));
        textView.setBackgroundResource(0);
        textView.setOnClickListener(l);
        textView.setCompoundDrawables(nav_up, null, null, null);
        addView(textView);
    }

    public TextView setRightTextReturn(@DrawableRes int resId,String title, OnClickListener l) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        Drawable nav_up=ContextCompat.getDrawable(context,resId);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());


        TextView textView = new TextView(getContext());
        textView.setId(R.id.right_btn);
        textView.setText(title);
        textView.setLayoutParams(params);
        textView.setGravity(Gravity.CENTER_VERTICAL|Gravity.RIGHT);
        textView.setPadding(dp10,0,dp10,0);
        textView.setTextSize(15);
        textView.setTextColor(ContextCompat.getColor(context,R.color.whiteColor));
        textView.setBackgroundResource(0);
        textView.setOnClickListener(l);
        textView.setCompoundDrawables(nav_up, null, null, null);
        addView(textView);
        return textView;
    }

    public void setRightButton(@DrawableRes int resId, OnClickListener l) {
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);

        ImageButton button = new ImageButton(getContext());
        button.setId(R.id.right_btn);
        button.setLayoutParams(params);
        button.setImageResource(resId);
        button.setScaleType(ImageView.ScaleType.CENTER);
        button.setBackgroundResource(0);
        button.setOnClickListener(l);
        addView(button);
    }
}
