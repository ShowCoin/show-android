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
 * Created by liuzehua on 2018/4/5.
 * 备份钱包页面
 */

public class BackupWalletActivity extends BaseFragmentActivity {
    @BindView(R.id.backupwallet_title)
    TitleView backupwalletTitle;
    @BindView(R.id.backupwallet_ok)
    TextView backupwalletOk;
    @BindView(R.id.backupwallet_tutorial)
    TextView backupwalletTutorial;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, BackupWalletActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_backupwallet;
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

        backupwalletTitle.setTitle(getString(R.string.backup_wallet)).setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        backupwalletTitle.setLayBac(R.color.color_ffffff);
    }

    @OnClick({R.id.backupwallet_ok, R.id.backupwallet_tutorial})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backupwallet_ok:
                startActivity(BackupWordActivity.getCallingIntent(this));
                break;
            case R.id.backupwallet_tutorial:
                break;
        }
    }
}
