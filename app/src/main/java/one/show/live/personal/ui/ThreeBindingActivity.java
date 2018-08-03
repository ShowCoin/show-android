package one.show.live.personal.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.MeetApplication;
import one.show.live.R;
import one.show.live.common.po.POEventBus;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.Constants;
import one.show.live.common.util.StringUtils;
import one.show.live.common.view.BaseAlertDialog;
import one.show.live.common.view.ToastUtils;
import one.show.live.log.Logger;
import one.show.live.login.ui.LoginActivity;
import one.show.live.login.view.WxBindsView;
import one.show.live.personal.presenter.BindsPresenter;
import one.show.live.po.POBind;
import one.show.live.po.eventbus.BindEventBean;
import one.show.live.po.eventbus.CaptitalBean;
import one.show.live.util.CheckOnDoubleClickUtils;
import one.show.live.util.EventBusKey;
import one.show.live.util.StringUtil;
import one.show.live.util.ToolUtil;
import one.show.live.widget.TitleView;

public class ThreeBindingActivity extends BaseFragmentActivity implements WxBindsView {


    @BindView(R.id.three_title)
    TitleView threeTitle;
    @BindView(R.id.three_top)
    LinearLayout threeTop;
    @BindView(R.id.iv_wechat)
    ImageView ivWechat;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;
    @BindView(R.id.re_wechat)
    RelativeLayout reWechat;
    @BindView(R.id.iv_sina)
    ImageView ivSina;
    @BindView(R.id.tv_sina)
    TextView tvSina;
    @BindView(R.id.re_sina)
    RelativeLayout reSina;
    @BindView(R.id.iv_qq)
    ImageView ivQq;
    @BindView(R.id.tv_qq)
    TextView tvQq;
    @BindView(R.id.re_qq)
    RelativeLayout reQq;
    List<String> list;
    BindsPresenter bindsPresenter;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, ThreeBindingActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_threebinding);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initBaseView();
    }

    private void initBaseView() {
        initStatusBar(ContextCompat.getColor(this, R.color.color_0c0c0c), threeTitle);
        threeTitle.setTitle(getString(R.string.third_party_bind))
                .setTextColor(ContextCompat.getColor(this, R.color.color_ffffff));
        threeTitle.setLeftImage(R.drawable.back_white);
        threeTitle.setLayBac(R.color.color_0c0c0c);
        bindsPresenter = new BindsPresenter(this);
        if (POMember.getInstance().getThirdBinds() != null &&
                POMember.getInstance().getThirdBinds().size() > 0 &&
                !POMember.getInstance().getThirdBinds().contains("weixin")
                ) {
            unWXBind();
        } else {
            wxSucces();
        }
    }


    private void unWXBind() {
        tvWechat.setText(R.string.unbound);
        tvWechat.setTextColor(ContextCompat.getColor(this, R.color.color_4d98ff));
        reWechat.setEnabled(true);
        ivWechat.setVisibility(View.VISIBLE);

    }

    private void wxSucces() {
        tvWechat.setText(R.string.okbound);
        tvWechat.setTextColor(ContextCompat.getColor(this, R.color.holo_yellow_dark));
        reWechat.setEnabled(false);
        ivWechat.setVisibility(View.GONE);
    }

    @OnClick({R.id.re_wechat, R.id.re_qq, R.id.re_sina})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.re_wechat:
                weixin();
                break;
            case R.id.re_qq:
                ToastUtils.showCenterToast(this
                        , Toast.LENGTH_SHORT, R.string.coming_soon);
                break;
            case R.id.re_sina:
                ToastUtils.showCenterToast(this
                        , Toast.LENGTH_SHORT, R.string.coming_soon);
                break;
        }
    }

    private void weixin() {
        CheckOnDoubleClickUtils.onClick(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (MeetApplication.getInstance().iwxapi == null) {
                    MeetApplication.getInstance().iwxapi = WXAPIFactory.createWXAPI(ThreeBindingActivity.this, Constants.WEI_XIN_APP_ID, true);
                }
                if (!MeetApplication.getInstance().iwxapi.isWXAppInstalled()) {
                    ToastUtils.showToast(R.string.sns_weixin_uninstall);
                    return;
                }
                if (!MeetApplication.getInstance().iwxapi.isWXAppSupportAPI()) {
                    ToastUtils.showToast(R.string.sns_weixin_version_low);
                    return;
                }

                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "SHOW:" + new Date().getTime();
                MeetApplication.getInstance().iwxapi.sendReq(req);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//反注册EventBus
    }


    /**
     * 微信认证成功之后的回调
     *
     * @param bean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(POEventBus bean) {
        switch (bean.getId()) {
            case EventBusKey.EVENT_BUS_LOGIN_BY_WX:
                bindsPresenter.wxBinds(bean.getData());
                break;
        }
    }


    @Override
    public void wxBindsFailed(String msg) {

    }

    @Override
    public void wxBindsSuccess(POBind data) {
        wxSucces();
        for (int i = 0; i < data.getThird_part().size(); i++) {
            String str = data.getThird_part().get(i).getType();
            if (StringUtil.equal(str, "weixin")) {
                List<String> list = new ArrayList<>();
                list.add(str);
                POMember.getInstance().setThirdBinds(list);
                break;
            }
        }
    }
}
