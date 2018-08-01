package one.show.live.personal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.StringUtils;
import one.show.live.common.view.ToastUtils;
import one.show.live.login.view.GoogleBindsView;
import one.show.live.login.view.VerifyCodeView;
import one.show.live.personal.presenter.BindsPresenter;
import one.show.live.po.eventbus.BindEventBean;
import one.show.live.widget.TitleView;

/**
 * Created by lihui on 2018/7/28.
 */
public class GoogleCodeActivity extends BaseFragmentActivity implements GoogleBindsView {


    @BindView(R.id.google_code_title)
    TitleView googleCodeTitle;
    @BindView(R.id.google_code)
    LinearLayout googleCode;
    @BindView(R.id.verify_code_view)
    VerifyCodeView verifyCodeView;

    BindsPresenter googleBindsPresenter;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, GoogleCodeActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_code);
        ButterKnife.bind(this);
        googleBindsPresenter = new BindsPresenter(this);
        initBaseView();
    }

    private void initBaseView() {
        initStatusBar(ContextCompat.getColor(this, R.color.color_0c0c0c), googleCodeTitle);
        googleCodeTitle.setTitle(getString(R.string.google_code))
                .setTextColor(ContextCompat.getColor(this, R.color.color_ffffff));
        googleCodeTitle.setLeftImage(R.drawable.back_white);
        googleCodeTitle.setLayBac(R.color.color_0c0c0c);
        googleCodeTitle.setRightText(getString(R.string.determine), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!StringUtils.isEmpty(verifyCodeView.getEditContent())){
                    googleBindsPresenter.googleBinds(verifyCodeView.getEditContent());
                }else{
                    ToastUtils.showCenterToast(GoogleCodeActivity.this
                            , Toast.LENGTH_SHORT, R.string.googleerrcode);
                }
            }
        });
        verifyCodeView.showSoftInput(this);
        verifyCodeView.setInputCompleteListener(new VerifyCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {

            }

            @Override
            public void invalidContent() {

            }
        });

    }

    @OnClick({R.id.verify_code_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.verify_code_view:
                break;
        }
    }

    @Override
    public void googleBindsFailed(String msg) {
        if (isContextAlive()) {
            ToastUtils.showCenterToast(this
                    , Toast.LENGTH_SHORT, msg);
        }
    }

    @Override
    public void googleBindsSuccess() {
        if (isContextAlive()) {
            ToastUtils.showCenterToast(this
                    , Toast.LENGTH_SHORT, R.string.google_success);
            POMember.getInstance().setIs_bind_auth(1);
            EventBus.getDefault().post(new BindEventBean(BindEventBean.GOOGLEBIND));
            Intent intent = new Intent(mActivity, SecurityCenterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }
}
