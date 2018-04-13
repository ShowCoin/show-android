package one.show.live.wallet.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.widget.TitleView;
import one.show.live.widget.WordWrapView;
import one.show.live.ui.BaseFragmentActivity;

/**
 * Created by liuzehua on 2018/4/6.
 * <p>
 * 确认助记词词页面
 */

public class ConfirmBackupWordActivity extends BaseFragmentActivity {

    @BindView(R.id.confirmbackupword_title)
    TitleView confirmBackupwordTitle;
    @BindView(R.id.confirmbackupword_wordwrap_w)
    WordWrapView confirmbackupwordWordwrapW;
    @BindView(R.id.confirmbackupword_wordwrap_r)
    WordWrapView confirmbackupwordWordwrapR;
    @BindView(R.id.confirmbackupwallet_ok)
    TextView confirmbackupwalletOk;


    List<String> strsR = new ArrayList<>();
    List<String> strsw = new ArrayList<>();


    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, ConfirmBackupWordActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_confirmbackupword;
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
        confirmBackupwordTitle.setTitle(getString(R.string.backup_word)).setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        confirmBackupwordTitle.setLayBac(R.color.color_ffffff);

        strsR.add("Anhui");
        strsR.add("behalf");
        strsR.add("environmental");
        strsR.add("Macao");
        strsR.add("contains");
        strsR.add("protection");
        strsR.add("persistence");
        strsR.add("space");
        strsR.add("compare");
        strsR.add("little");
        strsR.add("science");
        strsR.add("school");

        //将打乱的助记词，添到备选框
        for (int i = 0; i < 12; i++) {
            TextView textview = new TextView(this);
            textview.setText(strsR.get(i));
            final int finalI = i;
            textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addW(strsR.get(finalI));
                    view.setBackgroundResource(R.color.color_d5ae8f);
                }
            });
            confirmbackupwordWordwrapR.addView(textview);
        }
    }

    @OnClick({R.id.confirmbackupwallet_ok})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirmbackupwallet_ok:
                break;
        }
    }

    /**
     * 选择助记词，添加进文本
     *
     * @param word
     */

    public void addW(final String word) {

        if (!strsw.contains(word)) {
            strsw.add(word);
            TextView textview = new TextView(this);
            textview.setText(word);
            textview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    removeW(word,view);
                }
            });
            confirmbackupwordWordwrapW.addView(textview);
        }
    }

    /**
     * 将文本中的助记词删掉
     *
     * @param word
     */
    public void removeW(final String word,View view) {
        strsw.remove(word);
        confirmbackupwordWordwrapW.removeView(view);
        int num = strsR.indexOf(word);
        confirmbackupwordWordwrapR.removeViewAt(num);

        TextView textview = new TextView(this);
        textview.setText(word);
        textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addW(word);
                view.setBackgroundResource(R.color.color_d5ae8f);
            }
        });
        confirmbackupwordWordwrapR.addView(textview,num);


    }
}
