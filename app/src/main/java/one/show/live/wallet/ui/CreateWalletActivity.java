package one.show.live.wallet.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.widget.TitleView;
import one.show.live.ui.BaseFragmentActivity;

/**
 * Created by Nano on 2018/4/4.
 * 创建钱包
 */

public class CreateWalletActivity extends BaseFragmentActivity {

    @BindView(R.id.createwallet_title)
    TitleView createwalletTitle;
    @BindView(R.id.createwallet_terms)
    TextView createwalletTerms;
    @BindView(R.id.createwallet_ok)
    TextView createwalletOk;


    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, CreateWalletActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_createwallet;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initBaseView() {
        super.initBaseView();
        createwalletTitle.setTitle(getString(R.string.crate_wallet)).setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        createwalletTitle.setLayBac(R.color.color_ffffff);
    }

    @OnClick({R.id.createwallet_terms, R.id.createwallet_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.createwallet_terms:
                break;
            case R.id.createwallet_ok://创建钱包按钮,暂时先跳转备份钱包页面
                startActivity(BackupWalletActivity.getCallingIntent(this));

                break;
        }
    }
}
