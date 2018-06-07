package one.show.live.personal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.personal.presenter.DataPresenter;
import one.show.live.personal.view.DataView;
import one.show.live.util.SystemUtils;
import one.show.live.widget.TitleView;

/**
 * Created by Nano on 2018/4/20.
 * 这个页面用来修改昵称和个性签名
 */

public class NickNameActivity extends BaseFragmentActivity implements DataView {


    public static final int NAME_TYPE = 0x1 << 1;
    public static final int DESC_TYPE = 0x1 << 2;


    int type;
    @BindView(R.id.name_title)
    TitleView nameTitle;
    @BindView(R.id.name_name)
    EditText nameName;
    @BindView(R.id.name_name_lay)
    LinearLayout nameNameLay;
    @BindView(R.id.name_desc)
    EditText nameDesc;

    DataPresenter dataPresenter;


    String name;
    String descriptions;
    @BindView(R.id.name_desc_left_img)
    ImageView nameDescLeftImg;
    @BindView(R.id.name_desc_center_img)
    ImageView nameDescCenterImg;
    @BindView(R.id.name_desc_lay)
    LinearLayout nameDescLay;


    public static Intent getCallingIntent(Context context, int type) {
        Intent intent = new Intent(context, NickNameActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        intent.putExtras(bundle);
        return intent;
    }

    private void initBaseView() {
        initStatusBarForLightTitle(ContextCompat.getColor(this, R.color.color_ffffff),nameTitle);

        Bundle bundle = getIntent().getExtras();
        type = bundle.getInt("type");
        dataPresenter = new DataPresenter(this);

        if (type == NAME_TYPE) {
            nameNameLay.setVisibility(View.VISIBLE);
            nameTitle.setTitle(getString(R.string.nick_name))
                    .setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        } else {
            nameDescLay.setVisibility(View.VISIBLE);
            nameTitle.setTitle(getString(R.string.desc))
                    .setTextColor(ContextCompat.getColor(this, R.color.color_333333));

            nameDesc.setText(POMember.getInstance().getDescriptions());
            nameDesc.setSelection(POMember.getInstance().getDescriptions().length());
        }

        nameTitle.setLeftImage(R.drawable.back_black);
        nameTitle.setRightText(getString(R.string.save), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map<String, String> map = new HashMap<>();
                if (type == NAME_TYPE) {
                    name = nameName.getText().toString();
                    map.put("nickname", name);
                    dataPresenter.uploadInfo(map, DataPresenter.NAME);
                } else {
                    descriptions = nameDesc.getText().toString();
                    map.put("descriptions", descriptions);
                    dataPresenter.uploadInfo(map, DataPresenter.DESC);
                }

            }
        }).setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        nameTitle.setLayBac(R.color.color_ffffff);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        initBaseView();
    }

    @Override
    public void onFinishUploadInfo(boolean isSuccess, int type) {
        if (isContextAlive() && isSuccess) {
            if (type == NAME_TYPE) {
                POMember.fixNickName(name);
            } else {
                POMember.fixDescriptions(descriptions);
            }
            finish();
        }

    }

    @OnClick({R.id.name_desc_left_img, R.id.name_desc_center_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.name_desc_left_img:
                nameDesc.setGravity(Gravity.LEFT);
                break;
            case R.id.name_desc_center_img:
                nameDesc.setGravity(Gravity.CENTER_HORIZONTAL);
                break;
        }
    }
}
