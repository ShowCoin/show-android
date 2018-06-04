package one.show.live.personal.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.show.live.R;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.DeviceUtils;
import one.show.live.common.util.FrescoUtils;
import one.show.live.log.Logger;

/**
 * Created by Nano on Show.
 */

public class ZoomActivity extends BaseFragmentActivity {
    @BindView(R.id.zoom_img)
    SimpleDraweeView zoomImg;

    int DEFAULT_DURATION = 300;

    public final static AccelerateInterpolator ACC_INTERPOLATOR = new AccelerateInterpolator(
            10.0f);
    int deltaX;
    int deltaY;
    float scaleX;
    float scaleY;


    int originViewLeft;
    int originViewTop;
    int originViewWidth;
    int originViewHeight;

    String imageUrl;
    @BindView(R.id.zoom_lay)
    RelativeLayout zoomLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        ButterKnife.bind(this);
        initStatusBar(ContextCompat.getColor(this, R.color.color_333333), zoomLay);

        // 取出传递过来的originView信息
        extractViewInfoFromBundle();
        FrescoUtils.bind(zoomImg, imageUrl);
        onUiReady();

        zoomLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void onUiReady() {
        zoomImg.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // remove previous listener
                zoomImg.getViewTreeObserver().removeOnPreDrawListener(this);
                //准备场景
                prepareScene();
                //播放动画
                runEnterAnimation();
                return true;
            }
        });
    }


    private void extractViewInfoFromBundle() {
        Bundle bundle = getIntent().getExtras();
        originViewLeft = bundle.getInt("left");
        originViewTop = bundle.getInt("top");
        originViewWidth = bundle.getInt("width");
        originViewHeight = bundle.getInt("height");
        imageUrl = bundle.getString("img");

        //设置需要放大的图片和放大图片一样大
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) zoomImg.getLayoutParams();
        layoutParams.width = originViewWidth;
        layoutParams.height = originViewHeight;
        zoomImg.setLayoutParams(layoutParams);

    }

    private void prepareScene() {

       scaleX = DeviceUtils.getScreenWidth(this)/zoomImg.getWidth();
       scaleY = DeviceUtils.getScreenHeight(this)/zoomImg.getHeight();

        BigDecimal s1 = new BigDecimal(scaleX+"");
        BigDecimal s2 = new BigDecimal(scaleY+"");

        if (s1.compareTo(s2) > 0) {//s1大
            scaleX = scaleY;
        }else if (s1.compareTo(s2) < 0){
            scaleY = scaleX;
        }

        int[] screenLocation = new int[2];
        zoomImg.getLocationOnScreen(screenLocation);
        //移动到起始view位置
        deltaX = originViewLeft - screenLocation[0];
        deltaY = originViewTop - screenLocation[1];
        zoomImg.setTranslationX(deltaX);
        zoomImg.setTranslationY(deltaY);

    }

    private void runEnterAnimation() {
        zoomImg.setVisibility(View.VISIBLE);
        //执行动画
        zoomImg.animate()
                .setDuration(DEFAULT_DURATION)
//                .setInterpolator(ACC_INTERPOLATOR)
                .scaleX(scaleX)
                .scaleY(scaleY)
                .translationX(0)
                .translationY(0)
                .start();

    }

    @Override
    public void onBackPressed() {
        runExitAnimation();
    }

    private void runExitAnimation() {
        zoomImg.animate()
                .setDuration(DEFAULT_DURATION)
//                .setInterpolator(ACC_INTERPOLATOR)
                .scaleX(1f)
                .scaleY(1f)
                .translationX(deltaX)
                .translationY(deltaY)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        overridePendingTransition(0, 0);
                    }
                }).start();
    }

}
