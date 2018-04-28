package one.show.live.wallet.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import one.show.live.R;
import one.show.live.widget.TitleView;
import one.show.live.ui.BaseFragmentActivity;
import one.show.live.util.DeviceUtils;
import one.show.live.view.ToastUtils;

/**
 * Created by Nano on 2018/3/29.
 *
 *  二维码页面
 */

public class QrCodeActivity extends BaseFragmentActivity {

    ImageView imageView;

    TitleView qrCodeTitle;

    public static Intent getCallingIntent(Context context){
        Intent intent  = new Intent(context,QrCodeActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_qr_code;
    }

    @Override
    protected void initBaseView() {
        super.initBaseView();
        imageView = (ImageView) findViewById(R.id.qr_code_image);
        qrCodeTitle = (TitleView)findViewById(R.id.qr_code_title);
        qrCodeTitle.setTitle(getString(R.string.top_up_code));
        qrCodeTitle.setLeftImage(R.drawable.back_white);
        qrCodeTitle.setRightText(getString(R.string.share), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ToastUtils.showToast(getString(R.string.share));
            }
        });
        qrCodeTitle.setLayBac(R.color.color_333333);
        handler.sendEmptyMessage(1);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //执行逻辑
            imageView.setImageBitmap(QRCodeEncoder.syncEncodeQRCode("你好啊哈哈设备厂家爱不是差不出口哈不是考察时保持客户", DeviceUtils.dipToPX(QrCodeActivity.this,180)));
        }
    };

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
