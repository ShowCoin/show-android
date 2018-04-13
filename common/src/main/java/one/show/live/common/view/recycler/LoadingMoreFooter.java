package one.show.live.common.view.recycler;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import one.show.live.common.util.ConvertToUtils;
import one.show.live.common.util.DeviceUtils;
import one.show.live.common.common.R;

public class LoadingMoreFooter extends LinearLayout implements FooterStateChangeListener {

    private TextView mText;

    private AnimationDrawable anim;

    private ImageView loadMoreImage;


    public LoadingMoreFooter(Context context) {
        super(context);
        initview(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initview(context);
    }


    public void initview(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.myloadmore_layout, this);
        loadMoreImage = (ImageView)view.findViewById(R.id.loadmore_image);
        anim = (AnimationDrawable) loadMoreImage.getBackground();
        mText = (TextView)findViewById(R.id.loadmore_text);
        ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(
                DeviceUtils.getScreenWidth(context), ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams1.height = ConvertToUtils.dipToPX(getContext(), 35);
        setLayoutParams(layoutParams1);

    }


    public void setState(LoadState state) {
        switch (state) {
            case STATE_LOADING:
                loadMoreImage.setVisibility(View.VISIBLE);
                if(!anim.isRunning()){
                    anim.start();
                }
                mText.setText("载入中...");
                this.setVisibility(View.VISIBLE);
                mText.setVisibility(View.GONE);
                break;
            case STATE_COMPLETE:
                if(anim.isRunning()){
                    anim.stop();
                }
                mText.setText("载入中...");
                this.setVisibility(View.GONE);
                break;
            case STATE_NOMORE:
                if(anim.isRunning()){
                    anim.stop();
                }
                mText.setVisibility(View.VISIBLE);
                mText.setText("已全部加载");
                loadMoreImage.setVisibility(View.GONE);
                this.setVisibility(View.VISIBLE);
                break;
        }
    }

}
