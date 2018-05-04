package one.show.live.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.show.live.R;

/**
 * title布局
 * Created by Nano on 2018/4/2.
 */

public class TitleView extends LinearLayout {
    Context context;

    @BindView(R.id.title_tltle)
    TextView titleTitle;
    @BindView(R.id.title_left)
    ImageView titleLeft;
    @BindView(R.id.title_right)
    ImageView titleRight;
    @BindView(R.id.title_lay)
    RelativeLayout titleLay;

    @BindView(R.id.title_right_text)
    TextView titleRightText;


    public TitleView(Context context) {
        super(context);
        initView(context);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.view_title, this);
        ButterKnife.bind(this);

    }

    /**
     * 设置title背景颜色
     *
     * @param resId
     */
    public void setLayBac(@ColorRes int resId) {
        titleLay.setBackgroundResource(resId);
    }

    /**
     * 添加title
     *
     * @param title
     * @return
     */
    public TextView setTitle(String title) {
        titleTitle.setText(title);
        return titleTitle;
    }

    public TextView setTitle(String title, OnClickListener listener) {
        titleTitle.setText(title);
        titleTitle.setOnClickListener(listener);
        return titleTitle;
    }

    /**
     * 添加左侧按钮的
     *
     * @param resId
     */
    public void setLeftImage(@DrawableRes int resId) {
        titleLeft.setImageResource(resId);
        titleLeft.setVisibility(VISIBLE);
        titleLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) context).finish();
            }
        });
    }

    public void setLeftImage(@DrawableRes int resId, OnClickListener listener) {
        titleLeft.setImageResource(resId);
        titleLeft.setVisibility(VISIBLE);
        titleLeft.setOnClickListener(listener);
    }

    /**
     * 添加右侧按钮的 图片的
     *
     * @param resId
     */
    public void setRightImage(@DrawableRes int resId, OnClickListener listener) {
        titleRight.setImageResource(resId);
        titleRight.setOnClickListener(listener);
        titleRight.setVisibility(VISIBLE);
    }
    /**
     * 添加右侧按钮的 按钮的
     *
     * @param string
     */
    public TextView setRightText(String string, OnClickListener listener) {
        titleRightText.setText(string);
        titleRightText.setOnClickListener(listener);
        return titleRightText;
    }
}
