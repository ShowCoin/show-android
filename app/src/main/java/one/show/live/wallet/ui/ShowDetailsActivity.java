package one.show.live.wallet.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;


import butterknife.BindView;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.widget.TitleView;
import one.show.live.ui.BaseFragmentActivity;
import one.show.live.view.ToastUtils;

/**
 * Created by liuzehua on 2018/4/3.
 * 虚拟币详情
 */

public class ShowDetailsActivity extends BaseFragmentActivity {


    @BindView(R.id.showdetails_title)
    TitleView showDetailsTitle;

    @BindView(R.id.showdetails_topup)
    LinearLayout showDetailsTopup;
    @BindView(R.id.showdetails_withdrawal)
    LinearLayout showDetailswWthdrawal;

    public static Intent getcallingIntent(Context context) {
        Intent intent = new Intent(context, ShowDetailsActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_showdetails;
    }

    @Override
    protected void initBaseView() {
        super.initBaseView();

        showDetailsTitle.setTitle("SHOW");
        showDetailsTitle.setLeftImage(R.drawable.back_white);
    }

    @OnClick({R.id.showdetails_topup, R.id.showdetails_withdrawal})
    public void onclick(View v) {

        switch (v.getId()) {
            case R.id.showdetails_topup:
//                startActivity(QrCodeActivity.getCallingIntent(this));
                startActivity(CreateWalletActivity.getCallingIntent(this));
                break;
            case R.id.showdetails_withdrawal:
                startActivity(WithdrawalActivity.getCallinfIntent(this));
                break;
        }

    }
}
