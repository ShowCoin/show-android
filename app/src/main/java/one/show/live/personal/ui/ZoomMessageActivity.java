package one.show.live.personal.ui;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.show.live.R;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.FrescoUtils;

/**
 * Created by Nano on Show.
 * 这个是聊天页面暂时用的，等到聊天页面页面能拿到图片尺寸的话，在用zoomActivity，之后把这个删掉
 */

public class ZoomMessageActivity extends BaseFragmentActivity {
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
//        actualImageScaleType
        zoomImg.getHierarchy().setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);

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
    }

    private void prepareScene() {
        //缩放到起始view大小
        scaleX = (float) originViewWidth / zoomImg.getWidth();
        scaleY = (float) originViewHeight / zoomImg.getHeight();
        zoomImg.setScaleX(scaleX);
        zoomImg.setScaleY(scaleY);

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
                .scaleX(1f)
                .scaleY(1f)
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
                .scaleX(scaleX)
                .scaleY(scaleY)
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
