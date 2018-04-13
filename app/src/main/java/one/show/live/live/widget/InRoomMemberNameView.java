package one.show.live.live.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import one.show.live.R;


/**
 * 有人进入房间后的动画提示
 */
public class InRoomMemberNameView extends FrameLayout {
    private List<String> list = new ArrayList<>();
    private AnimationSet animation;

    public InRoomMemberNameView(Context context) {
        super(context);
    }

    public InRoomMemberNameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void addName(String name) {
        list.add(name);
        startAnim();
    }

    private synchronized void startAnim() {
        if (animation != null) {
            return;
        }

        if (getVisibility() != VISIBLE) {
            setVisibility(VISIBLE);
        }

        final View animView = getView(list.get(0));
        list.remove(0);
        animView.setVisibility(INVISIBLE);
        addView(animView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        Animation enterAnim = AnimationUtils.loadAnimation(getContext(), R.anim.push_up_in);
        Animation endAnim = AnimationUtils.loadAnimation(getContext(), R.anim.push_up_out);
        animation = new AnimationSet(true);
        animation.addAnimation(enterAnim);
        animation.addAnimation(endAnim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                animView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                removeView(animView);
                InRoomMemberNameView.this.animation = null;
                if (list.size() > 0) {
                    startAnim();
                } else {
                    setVisibility(INVISIBLE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animView.startAnimation(animation);

    }

    private View getView(String name) {
        TextView nameView = new TextView(getContext());
        nameView.setTextSize(13);
        nameView.setGravity(Gravity.CENTER);
        nameView.setTextColor(Color.WHITE);
        nameView.setSingleLine(true);

        if (name != null && name.length()>9){
            name = name.substring(0,7) + "...";
        }
        nameView.setText(String.format("%s 进来了", name));
        return nameView;
    }
}
