package one.show.live.login.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.home.ui.HomeActivity;
import one.show.live.login.presenter.SplashPresenter;
import one.show.live.login.view.SplashView;

/**
 * Created by Nano on 2018/4/11.
 */

public class SplashActivity extends BaseFragmentActivity implements SplashView {

    SplashPresenter splashPresenter;
    @BindView(R.id.splash_activity_image)
    SimpleDraweeView splashActivityImage;
    @BindView(R.id.splash_activity_next)
    TextView splashActivityNext;
    @BindView(R.id.splash_advertising)
    RelativeLayout splashAdvertising;

    @Override
    protected int getContentView() {
        return R.layout.splash_activity;
    }

    @Override
    protected void initBaseView() {
        super.initBaseView();
        splashPresenter = new SplashPresenter(this);
        splashPresenter.getConfigs();//获取数据
    }

    @Override
    public void onGetConfigFinish(boolean isSuccess) {
        if (isContextAlive()) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.splash_activity_image, R.id.splash_activity_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.splash_activity_image:
                break;
            case R.id.splash_activity_next:
                startActivity(HomeActivity.getCallingIntent(this));
                finish();
                break;
        }
    }


//    /**
//     * 加载广告图
//     */
//    private synchronized void checkIsShowAd() {
//        if (isContextAlive()) {
//
//            if (isShowAD) {
//                return;
//            }
//            isShowAD = true;
//            if (POConfig.getInstance().getShow_ad() == 1 && POConfig.getInstance().getStartAdvertisement() != null // 广告页面开启，并且广告数据不为空
//                    && POConfig.getInstance().getStartAdvertisement().size()>0 && POMember.getInstance().isLogin()//广告数据>0，并且还得是登录状态
//                    && DifferenceUtil.differenceData()) {//并且今天处在还可以显示的状态
//                splashAdvertising.setVisibility(View.VISIBLE);
//                picture();
//            } else {
//
//                if (!LoginUtils.isNotLogin(this, false)) {
//                    splashHeader.setHead(FrescoUtils.getUri(POMember.getInstance().getProfileImg()), size);
//                    splashHeader.isVip(POMember.getInstance().isVip());
//                    splashHeader.isAuth(!TextUtils.isEmpty(POMember.getInstance().getSinaVerifyInfo()));
//                    splashTitle.setText("Hi " + POMember.getInstance().getNickName());
//                    splashData.setVisibility(View.VISIBLE);
//                    if (POMember.getInstance().isVip()) {
//                        splashTitle.setTextColor(ContextCompat.getColor(this, R.color.color_vip_name));
//                        splashWelcome.setTextColor(ContextCompat.getColor(this, R.color.color_vip_name));
//                    }
//                    handler.sendEmptyMessageDelayed(1, 3 * 1000);
//                } else {
//                    handler.sendEmptyMessageDelayed(1, 1 * 500);
//                }
//
//
//            }
//        }
//    }
}
