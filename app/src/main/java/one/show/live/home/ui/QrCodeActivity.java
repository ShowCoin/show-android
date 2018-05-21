package one.show.live.home.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import one.show.live.common.po.POLogin;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.DeviceUtils;
import one.show.live.common.view.ToastUtils;
import one.show.live.widget.HeadImageView;
import one.show.live.widget.TitleView;

/**
 * Created by Nano on 2018/3/29.
 * <p>
 * 二维码页面
 */

public class QrCodeActivity extends BaseFragmentActivity {

    @BindView(one.show.live.R.id.qr_code_title)
    TitleView qrCodeTitle;
    @BindView(one.show.live.R.id.qr_code_image)
    ImageView qrCodeImage;
    @BindView(one.show.live.R.id.qr_code_address)
    TextView qrCodeAddress;
    @BindView(one.show.live.R.id.qr_code_copy)
    TextView qrCodeCopy;
    @BindView(one.show.live.R.id.qr_code_head)
    HeadImageView qrCodeHead;
    @BindView(one.show.live.R.id.qr_code_name)
    TextView qrCodeName;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, one.show.live.wallet.ui.QrCodeActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(one.show.live.R.layout.activity_qr_code);
        ButterKnife.bind(this);
        initBaseView();
    }

    private void initBaseView() {
        qrCodeTitle.setTitle(getString(one.show.live.R.string.top_up_code));
        qrCodeTitle.setLeftImage(one.show.live.R.drawable.back_white);
        qrCodeTitle.setRightText(getString(one.show.live.R.string.share), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast(getString(one.show.live.R.string.share));
            }
        });
        qrCodeTitle.setLayBac(one.show.live.R.color.color_333333);
        handler.sendEmptyMessage(1);


        qrCodeAddress.setText(POLogin.getInstance().getWalletAddress());
        qrCodeHead.setImage(POMember.getInstance().getAvatar());
        qrCodeName.setText(POMember.getInstance().getNickname());

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //执行逻辑
            qrCodeImage.setImageBitmap(QRCodeEncoder.syncEncodeQRCode(POLogin.getInstance().getWalletAddress(), DeviceUtils.dipToPX(one.show.live.wallet.ui.QrCodeActivity.this, 180)));
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    @OnClick(one.show.live.R.id.qr_code_copy)
    public void onViewClicked() {
        if(!TextUtils.isEmpty(POLogin.getInstance().getWalletAddress())){
            ClipboardManager cmb = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setText(POLogin.getInstance().getWalletAddress());
            ToastUtils.showToast(getString(one.show.live.R.string.copy_success));
        }
    }
}
