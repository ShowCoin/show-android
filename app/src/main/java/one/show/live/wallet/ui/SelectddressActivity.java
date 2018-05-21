package one.show.live.wallet.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.po.POAddress;
import one.show.live.po.POAddressList;
import one.show.live.util.SharedPreferenceUtil;
import one.show.live.widget.ItemSelectAddress;
import one.show.live.widget.TitleView;
import one.show.live.ui.BaseFragmentActivity;

/**
 * Created by Nano on 2018/4/3.
 * 选择地址页面
 */

public class SelectddressActivity extends BaseFragmentActivity {

    public final static int RESULT_CODE = 1;

    @BindView(R.id.select_address_title)
    TitleView selectAddressTitle;
    @BindView(R.id.select_address_add)
    TextView selectAddressAdd;
    @BindView(R.id.select_address_lin)
    LinearLayout selectAddressLin;

    POAddressList poAddressList;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, SelectddressActivity.class);
        return intent;

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_select_address;
    }

    @Override
    protected void initBaseView() {
        super.initBaseView();
        selectAddressTitle.setLayBac(R.color.color_e8e8e8);
        selectAddressTitle.setTitle(getString(R.string.select_address)).setTextColor(ContextCompat.getColor(this, R.color.color_333333));
    }

    @OnClick({R.id.select_address_add})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.select_address_add:
                startActivityForResult(AddAddressActivity.getCallingIntent(this),RESULT_CODE);
                break;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);

        Object object = SharedPreferenceUtil.get(SharedPreferenceUtil.ADDRESS_LIST);
        if (object != null) {
            poAddressList = (POAddressList) object;
        } else {
            startActivity(AddAddressActivity.getCallingIntent(this));
            finish();
            return;
        }

        if (poAddressList.getPoAddressList().size() < 1) {
            startActivity(AddAddressActivity.getCallingIntent(this));
            finish();
            return;
        }
        selectAddressLin.removeAllViews();
        for (int i = 0; i < poAddressList.getPoAddressList().size(); i++) {
            final POAddress poAddress = poAddressList.getPoAddressList().get(i);
            ItemSelectAddress itemSelectAddress = new ItemSelectAddress(this);
            itemSelectAddress.setData(poAddress);
            itemSelectAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("poaddress", poAddress);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            selectAddressLin.addView(itemSelectAddress);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case RESULT_CODE:

                    finish();
                    break;
            }

        }

    }
}
