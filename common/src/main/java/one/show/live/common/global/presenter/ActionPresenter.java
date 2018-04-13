package one.show.live.common.global.presenter;

import android.text.TextUtils;

import java.util.Map;

import one.show.live.common.po.POLaunch;
import one.show.live.common.ui.BasePresenter;
import one.show.live.common.global.request.ActionRequest;

/**
 * 打点统计的 2017年07月13日17:37:40
 */
public class ActionPresenter extends BasePresenter{


    public void actionUp(Map<String,String> maps,final String methods){

        if(!TextUtils.isEmpty(POLaunch.url)){
            new ActionRequest() {
                @Override
                protected String getUrl() {
                    return methods;
                }

                @Override
                public void onFinish(boolean isSuccess, String msg, Object data) {

                }
            }.startRequest(maps);
        }

    }
}
