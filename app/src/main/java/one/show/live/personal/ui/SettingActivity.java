package one.show.live.personal.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.view.BaseAlertDialog;
import one.show.live.util.SystemUtils;
import one.show.live.widget.ShareDialog;
import one.show.live.widget.TitleView;

/**
 * Created by Nano on 2018/4/18.
 */

public class SettingActivity extends BaseFragmentActivity {
    @BindView(R.id.setting_title)
    TitleView settingTitle;
    @BindView(R.id.setting_score)
    RelativeLayout settingScore;
    @BindView(R.id.setting_feedback)
    RelativeLayout settingFeedback;
    @BindView(R.id.setting_language)
    RelativeLayout settingLanguage;
    @BindView(R.id.setting_currency)
    RelativeLayout settingCurrency;
    @BindView(R.id.setting_diagnosis)
    RelativeLayout settingDiagnosis;
    @BindView(R.id.setting_about)
    RelativeLayout settingAbout;
    @BindView(R.id.setting_exit)
    TextView settingExit;


    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initBaseView();
    }

    private void initBaseView() {
        initStatusBarForLightTitle(ContextCompat.getColor(this, R.color.color_ffffff),settingTitle);
        settingTitle.setTitle(getString(R.string.setting))
                .setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        settingTitle.setLayBac(R.color.color_ffffff);
        settingTitle.setLeftImage(R.drawable.back_black);


    }

    @OnClick({R.id.setting_score, R.id.setting_feedback, R.id.setting_language, R.id.setting_currency, R.id.setting_diagnosis, R.id.setting_about, R.id.setting_exit,R.id.setting_center})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.setting_score:

                break;
            case R.id.setting_feedback:

                break;
            case R.id.setting_language:
                break;
            case R.id.setting_currency:
                break;
            case R.id.setting_diagnosis:
                break;
            case R.id.setting_about:
                startActivity(AboutActivity.getCallingIntent(this));
                break;
            case R.id.setting_exit:

                getAlertDialog().setTitle(getString(R.string.whether_to_log_out))
                        .setmLeftButtonListener(getString(R.string.cancel), new BaseAlertDialog.onCancelListener() {
                            @Override
                            public void onCancel(Dialog dialog) {
                                dialog.dismiss();
                            }
                        }).setmRightButtonListener(getString(R.string.determine), new BaseAlertDialog.onSuccessListener() {
                    @Override
                    public void onSuccess(Dialog dialog) {
                        dialog.dismiss();
                        SystemUtils.exitLogin(SettingActivity.this);
                        //清空数据之后的操作..........
                    }
                }).show();
                break;
            case R.id.setting_center:
                startActivity(SecurityCenterActivity.getCallingIntent(this));
                break;
        }
    }
}
