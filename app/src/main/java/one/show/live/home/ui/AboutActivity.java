package one.show.live.personal.ui;

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
import one.show.live.common.api.BaseRequest;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.AppUtil;
import one.show.live.widget.TitleView;

/**
 * Created by Nano on 2018/4/18.
 * 关于我们
 */

public class AboutActivity extends BaseFragmentActivity {


    @BindView(R.id.about_title)
    TitleView aboutTitle;
    @BindView(R.id.about_version)
    TextView aboutVersion;
    @BindView(R.id.about_community_convention)
    RelativeLayout aboutCommunityConvention;
    @BindView(R.id.about_privacy_policy)
    RelativeLayout aboutPrivacyPolicy;
    @BindView(R.id.about_terms_of_service)
    RelativeLayout aboutTermsOfService;
    @BindView(R.id.about_management_specification)
    RelativeLayout aboutManagementSpecification;
    @BindView(R.id.about_management_rules)
    RelativeLayout aboutManagementRules;
    @BindView(R.id.about_contact_us)
    RelativeLayout aboutContactUs;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        initBaseView();
    }

    private void initBaseView() {
        initStatusBarForLightTitle(ContextCompat.getColor(this, R.color.color_ffffff), aboutTitle);
        aboutTitle.setTitle(getString(R.string.about))
                .setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        aboutTitle.setLayBac(R.color.color_ffffff);
        aboutTitle.setLeftImage(R.drawable.back_black);
        aboutVersion.setText(String.format(getString(R.string.version),new AppUtil(this).getVersionName()));

    }

    @OnClick({R.id.about_community_convention, R.id.about_privacy_policy, R.id.about_terms_of_service, R.id.about_management_specification, R.id.about_management_rules, R.id.about_contact_us})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.about_community_convention:
                break;
            case R.id.about_privacy_policy:
                break;
            case R.id.about_terms_of_service:
                break;
            case R.id.about_management_specification:
                break;
            case R.id.about_management_rules:
                break;
            case R.id.about_contact_us:
                break;
        }
    }
}
