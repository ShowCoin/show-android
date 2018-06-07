package one.show.live.personal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.show.live.R;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.widget.TitleView;

/**
 * Created by Nano on Show.
 * 修改资金密码页面
 */

public class ModifyMoneyPasswordActivity extends BaseFragmentActivity {


    @BindView(R.id.modify_money_title)
    TitleView modifyMoneyTitle;

    public static Intent getCallingIntengt(Context context) {
        Intent intent = new Intent(context, ModifyMoneyPasswordActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_modify_money);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initStatusBarForLightTitle(ContextCompat.getColor(this, R.color.color_e8e8e8), modifyMoneyTitle);

        modifyMoneyTitle.setTitle(getString(R.string.modify_the_money_password)).setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        modifyMoneyTitle.setLayBac(R.color.color_e8e8e8);
        modifyMoneyTitle.setLeftImage(R.drawable.back_black);
    }
}
