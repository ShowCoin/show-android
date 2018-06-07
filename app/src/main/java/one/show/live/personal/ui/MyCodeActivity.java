package one.show.live.personal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import one.show.live.R;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.DeviceUtils;
import one.show.live.widget.HeadImageView;
import one.show.live.widget.TitleView;

/**
 * Created by Nano on 2018/4/19.
 * <p>
 * 我的二维码页面
 */

public class MyCodeActivity extends BaseFragmentActivity {


    @BindView(R.id.code_title)
    TitleView codeTitle;
    @BindView(R.id.code_header)
    HeadImageView codeHeader;
    @BindView(R.id.code_name)
    TextView codeName;
    @BindView(R.id.code_id)
    TextView codeId;
    @BindView(R.id.code_image)
    ImageView codeImage;

    int size60;
    POMember poMember;
    @BindView(R.id.code_show_num)
    TextView codeShowNum;


    public static Intent getCallingInteng(Context context) {
        Intent intent = new Intent(context, MyCodeActivity.class);
        return intent;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);
        initBaseView();
    }

    private void initBaseView() {

        initStatusBarForLightTitle(ContextCompat.getColor(this, R.color.color_ffffff), codeTitle);
        size60 = DeviceUtils.dipToPX(this, 60);

        codeTitle.setTitle(getString(R.string.my_code))
                .setTextColor(ContextCompat.getColor(this, R.color.color_333333));
        codeTitle.setLayBac(R.color.color_ffffff);
        codeTitle.setLeftImage(R.drawable.back_black);


        poMember = POMember.getInstance();
        if (poMember == null) {
            return;
        }
        handler.sendEmptyMessage(1);
        codeName.setText(poMember.getNickname());
        codeId.setText(String.format("ID:%s", poMember.getPopularNo()));
        codeHeader.setImage(poMember.getAvatar(),poMember.getUid());
        codeShowNum.setText(poMember.getShowCoinNum() + "");
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //执行逻辑
            codeImage.setImageBitmap(QRCodeEncoder.syncEncodeQRCode(POMember.getInstance().getUid(), DeviceUtils.dipToPX(MyCodeActivity.this, 250)));
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

}
