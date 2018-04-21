package one.show.live.media.widget.danmu.DanmuBase;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import one.show.live.R;
import one.show.live.common.util.AppUtil;
import one.show.live.common.util.DeviceUtil;
import one.show.live.common.util.DeviceUtils;
import one.show.live.common.util.FrescoUtils;
import one.show.live.common.util.WeakHandler;
import one.show.live.media.po.POIMDanmu;
import one.show.live.media.widget.danmu.AnimationHelper;
import one.show.live.media.widget.danmu.ScreenUtils;
import one.show.live.util.SystemUtils;


/**
 * Created by walkingMen on 2016/5/12.
 */
public class DanmakuChannel extends RelativeLayout {

    public boolean isRunning = false;
    public POIMDanmu mEntity;
    private DanmakuActionInter danAction;
    private OnClickListener mOnClickListener;

    private Handler mHander = new Handler();

    public void release(){
        mHander.removeCallbacksAndMessages(null);
    }

    public DanmakuActionInter getDanAction() {
        return danAction;
    }

    public void setDanAction(DanmakuActionInter danAction) {
        this.danAction = danAction;
    }

    public DanmakuChannel(Context context) {
        super(context);
        init();
    }


    public interface OnClickListener {
        void onClickHead(POIMDanmu entity);
    }


    public DanmakuChannel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DanmakuChannel(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DanmakuChannel(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(R.layout.danmaku_channel_layout, null);
        if (DeviceUtils.hasLollipop()) {
            this.setClipToOutline(false);
        }
    }


    public void setDanmakuEntity(POIMDanmu entity) {
        mEntity = entity;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void mStartAnimation(POIMDanmu entity) {
        isRunning = true;
        setDanmakuEntity(entity);
        if (mEntity != null) {
            final View view = View.inflate(getContext(), R.layout.item_live_danmu, null);

            SimpleDraweeView avatar = (SimpleDraweeView) view.findViewById(R.id.avatar);
            ImageView avatarVip = (ImageView)view.findViewById(R.id.avatar_vip);
            TextView nickname = (TextView) view.findViewById(R.id.nickname);
            TextView contentView = (TextView) view.findViewById(R.id.content);
            avatar.setImageURI(FrescoUtils.getUri(entity.getProfileImg()));
            avatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickListener != null)
                        mOnClickListener.onClickHead(mEntity);
                }
            });
            avatarVip.setVisibility(entity.getIsVip()==1?VISIBLE:GONE);

            nickname.setText(entity.getNickName());
            contentView.setText(entity.getContent());
            view.measure(-1, -1);
            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();
            int leftMargin = ScreenUtils.getScreenW(getContext());
            Animation anim = AnimationHelper.createTranslateAnim(getContext(), leftMargin, -measuredWidth);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (!SystemUtils.isDestroyed((Activity) getContext())) {//防止内存溢出
                        mHander.post(new Runnable() {
                            public void run() {
                                view.clearAnimation();
                                DanmakuChannel.this.removeView(view);
                                if (danAction != null) {
                                    danAction.pollDanmu();
                                }
                            }
                        });
                    }
                    isRunning = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            view.startAnimation(anim);
            this.addView(view);
        }
    }
}
