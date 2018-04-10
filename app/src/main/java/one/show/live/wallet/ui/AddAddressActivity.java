package one.show.live.wallet.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.po.POAddress;
import one.show.live.po.POAddressList;
import one.show.live.util.SharedPreferenceUtil;
import one.show.live.widget.TitleView;
import one.show.live.ui.BaseFragmentActivity;

/**
 * Created by liuzehua on 2018/4/3.
 */

public class AddAddressActivity extends BaseFragmentActivity {

    @BindView(R.id.addaddress_title)
    TitleView addaddressTitle;
    @BindView(R.id.addaddress_title_nickname)
    EditText addaddressTitleNickname;
    @BindView(R.id.addaddress_title_address)
    EditText addaddressTitleAddress;
    @BindView(R.id.addaddress_title_password)
    EditText addaddressTitlePassword;
    @BindView(R.id.addaddress_title_ok)
    TextView addaddressTitleOk;

    List<POAddress> AddressList;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, AddAddressActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_addaddress;
    }

    @Override
    protected void initBaseView() {
        super.initBaseView();
        addaddressTitle.setLayBac(R.color.color_ffffff);
        addaddressTitle.setTitle(getString(R.string.add_address)).setTextColor(ContextCompat.getColor(this, R.color.color_333333));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);


        Object object = SharedPreferenceUtil.get(SharedPreferenceUtil.ADDRESS_LIST);
        if(object != null) {
          POAddressList  poAddressList = (POAddressList) object;
            AddressList = new ArrayList<>(poAddressList.getPoAddressList());
        }else{
            AddressList = new ArrayList<>();
        }

    }

    @OnClick({ R.id.addaddress_title_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addaddress_title_ok:
                POAddress poAddress = new POAddress();
                poAddress.setName(addaddressTitleNickname.getText().toString());
                poAddress.setAddress(addaddressTitleAddress.getText().toString());
                poAddress.setPassword(addaddressTitlePassword.getText().toString());
                poAddress.setCertification(false);
                AddressList.add(poAddress);
                POAddressList  poAddressList = new POAddressList();
                poAddressList.setPoAddressList(AddressList);
                SharedPreferenceUtil.save(SharedPreferenceUtil.ADDRESS_LIST,poAddressList);
                //添加成功发送通知
                EventBus.getDefault().post(poAddress);
                setResult(RESULT_OK);//通知地址列表页面关闭
                finish();
                break;
        }
    }
}
