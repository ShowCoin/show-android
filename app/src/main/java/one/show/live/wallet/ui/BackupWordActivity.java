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
import one.show.live.widget.WordWrapView;
import one.show.live.ui.BaseFragmentActivity;

/**
 * Created by Nano on 2018/4/6.
 * <p>
 * 备份助记词页面
 */

public class BackupWordActivity extends BaseFragmentActivity {

    @BindView(R.id.backupword_title)
    TitleView backupwordTitle;
    @BindView(R.id.backupword_wordwrap)
    WordWrapView backupwordWordwrap;
    @BindView(R.id.backupwallet_ok)
    TextView backupwalletOk;

    private String[] strs = new String[]{"Anhui", "behalf", "environmental", "Macao",
            "contains", "protection", "persistence", "space", "compare", "little", "science",
            "school"};


    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, BackupWordActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_backupword;
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
        backupwordTitle.setTitle(getString(R.string.backup_word)).setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        backupwordTitle.setLayBac(R.color.color_ffffff);

        for (int i = 0; i < 12; i++) {
            TextView textview = new TextView(this);
            textview.setText(strs[i]);
            backupwordWordwrap.addView(textview);
        }
    }

    @OnClick({ R.id.backupwallet_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.backupwallet_ok:

                startActivity(  ConfirmBackupWordActivity.getCallingIntent(this));
                break;
        }
    }
}
