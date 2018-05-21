package one.show.live.wallet.ui;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import one.show.live.R;
import one.show.live.widget.TitleView;
import one.show.live.ui.BaseFragmentActivity;

/**
 * Created by Nano on 2018/3/29.
 */

public class WalletActivity extends BaseFragmentActivity {

    TitleView walletTitle;

    @BindView(R.id.item_wallet_lay)
    LinearLayout itemWalletLay;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, WalletActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void initBaseView() {
        super.initBaseView();

        walletTitle = (TitleView)findViewById(R.id.wallet_title);
        walletTitle.setTitle("钱包");
        walletTitle.setLeftImage(R.drawable.back_white);

        itemWalletLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(ShowDetailsActivity.getcallingIntent(WalletActivity.this));
            }
        });
    }
}
