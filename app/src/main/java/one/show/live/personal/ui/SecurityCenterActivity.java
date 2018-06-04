package one.show.live.personal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;

import com.suke.widget.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.widget.TitleView;

/**
 * Created by Nano on Show.
 * 安全中心页面
 */

public class SecurityCenterActivity extends BaseFragmentActivity {


    @BindView(R.id.center_title)
    TitleView centerTitle;
    @BindView(R.id.center_gestures_switch)
    SwitchButton centerGesturesSwitch;
    @BindView(R.id.center_fingerprint_switch)
    SwitchButton centerFingerprintSwitch;
    @BindView(R.id.center_title_money_password)
    LinearLayout centerTitleMoneyPassword;
    @BindView(R.id.center_title_email_lay)
    LinearLayout centerTitleEmailLay;
    @BindView(R.id.center_confirmation_switch)
    SwitchButton centerConfirmationSwitch;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, SecurityCenterActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);
        ButterKnife.bind(this);

        initStatusBarForLightTitle(ContextCompat.getColor(this, R.color.color_e8e8e8), centerTitle);
        initView();
    }

    private void initView() {
        centerTitle.setTitle(getString(R.string.security_center)).setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        centerTitle.setLayBac(R.color.color_e8e8e8);
        centerTitle.setLeftImage(R.drawable.back_black);
    }

    @OnClick({R.id.center_title_money_password, R.id.center_title_email_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.center_title_money_password:
                startActivity(ModifyMoneyPasswordActivity.getCallingIntengt(this));
                break;
            case R.id.center_title_email_lay:
                break;
        }
    }
}
