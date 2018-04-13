package one.show.live.login.presenter;

import java.util.HashMap;
import java.util.Map;

import one.show.live.common.ui.BasePresenter;
import one.show.live.login.mode.SplashRequest;
import one.show.live.login.view.SplashView;
import one.show.live.po.POConfig;

/**
 * Created by apple on 16/6/3.
 */
public class SplashPresenter extends BasePresenter {

    SplashView view;

    long startTime;

    public SplashPresenter(SplashView view) {
        initContext(view);
        this.view = view;
    }

    public void getConfigs() {
        startTime = System.currentTimeMillis();
        Map<String, String> map = new HashMap<>();
        new SplashRequest() {
            @Override
            public void onFinish(boolean isSuccess, String msg, POConfig data) {

                if (isSuccess) {
                    POConfig.save(data);//保存基本信息
                }
                view.onGetConfigFinish(isSuccess);
            }

        }.startRequest(map);
    }

}