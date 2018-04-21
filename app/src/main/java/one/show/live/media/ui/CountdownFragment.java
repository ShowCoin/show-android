package one.show.live.media.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.show.live.R;
import one.show.live.common.ui.BaseFragment;
import one.show.live.media.listener.CountdownInterface;

/**
 * Created by apple on 16/7/20.
 * 倒计时的fragment
 */
public class CountdownFragment extends BaseFragment {


    public static CountdownFragment getCallingFragemnt(){
        CountdownFragment fragment = new CountdownFragment();
        return fragment;
    }

    @BindView(R.id.countdown_text)
    TextView countdownText;

    Animation animation;
    int num = 3;
    boolean iswhile = true;

    CountdownInterface countdownInterface;

    @Override
    protected int getContentView() {
        return R.layout.countdown;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new Thread(new ShowRunnable()).start();
    }

    @Override
    protected void initView() {
        super.initView();
        countdownInterface = (CountdownInterface)activity;
    }

    public class ShowRunnable implements Runnable {

        @Override
        public void run() {
            while (iswhile) {
                Message msg = new Message();
                msg.what = 1;
                handlerAD.sendMessage(msg);
                try {
                    Thread.sleep(1300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    Handler handlerAD = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(isContextAlive()) {
                        animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim);
                        animation.setAnimationListener(new endAnimation());
                        countdownText.startAnimation(animation);
                    }
                    break;
            }
            super.handleMessage(msg);
        }

    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handlerAD.removeCallbacksAndMessages(null);
    }

    public class endAnimation implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            num--;
            if (num == 0) {
                countdownText.setText("Live");
                return;
            }
            if (num == -1) {
                iswhile=false;
                countdownText.setVisibility(View.GONE);
                countdownInterface.countdownclose();
                return;
            }
            countdownText.setText(num + "");
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
