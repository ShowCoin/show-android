package one.show.live.wallet.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.po.POAddress;
import one.show.live.widget.TitleView;
import one.show.live.ui.BaseFragmentActivity;
import one.show.live.view.ToastUtils;

/**
 * Created by Nano on 2018/4/3.
 * 提现页面
 */

public class WithdrawalActivity extends BaseFragmentActivity {

    public final static int RESULT_CODE = 1;

    @BindView(R.id.withdrawal_title)
    TitleView withdrawalTitle;
    @BindView(R.id.withdrawal_address_lay)
    LinearLayout withdrawalAddress;

    @BindView(R.id.withdrawal_address_name)
    TextView withdrawalAddressName;
    @BindView(R.id.withdrawal_address_address)
    TextView withdrawalAddressAddress;

    POAddress poAddress;//选中的地址
    @BindView(R.id.withdrawal_ok)
    TextView withdrawalOk;


    public static Intent getCallinfIntent(Context context) {
        Intent intent = new Intent(context, WithdrawalActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_withdrawal;

    }

    @Override
    protected void initBaseView() {
        super.initBaseView();
        withdrawalTitle.setLayBac(R.color.color_e8e8e8);
        withdrawalTitle.setTitle(getString(R.string.withdrawal)).setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        withdrawalTitle.setRightText(getString(R.string.withdrawal_record), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast(getString(R.string.withdrawal_record));
            }
        }).setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.withdrawal_address_lay,R.id.withdrawal_ok})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.withdrawal_address_lay:
                startActivityForResult(SelectddressActivity.getCallingIntent(this), RESULT_CODE);
                break;
            case R.id.withdrawal_ok:

                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    /**
     * 接收刚添加的地址
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(POAddress event) {
        if (event != null) {
            poAddress = event;
            withdrawalAddressName.setText(poAddress.getName());
            withdrawalAddressAddress.setText(poAddress.getAddress());
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case RESULT_CODE:

                    Bundle bundle = data.getExtras();
                    poAddress = (POAddress) bundle.getSerializable("poaddress");
                    withdrawalAddressName.setText(poAddress.getName());
                    withdrawalAddressAddress.setText(poAddress.getAddress());
                    break;
            }

        }

    }
}
