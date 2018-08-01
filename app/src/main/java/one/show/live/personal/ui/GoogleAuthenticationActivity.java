package one.show.live.personal.ui;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import one.show.live.R;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.DeviceUtils;
import one.show.live.common.util.StringUtils;
import one.show.live.common.view.ToastUtils;
import one.show.live.po.PoGoogleAuthen;
import one.show.live.util.ToolUtil;
import one.show.live.widget.TitleView;

public class GoogleAuthenticationActivity extends BaseFragmentActivity {


    @BindView(R.id.google_title)
    TitleView googleTitle;
    @BindView(R.id.google_top)
    LinearLayout googleTop;
    @BindView(R.id.google_code_image)
    ImageView googleCodeImage;
    @BindView(R.id.google_copy)
    LinearLayout googleCopy;
    @BindView(R.id.tv_putforward)
    TextView tvPutforward;
    @BindView(R.id.google_certification)
    TextView googleCertification;
    @BindView(R.id.tv_google)
    TextView tvGoogle;
    PoGoogleAuthen poGoogleAuthen;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //执行逻辑
            if (!StringUtils.isEmpty(POMember.getInstance().getAuth())) {
                googleCodeImage.setImageBitmap(QRCodeEncoder.syncEncodeQRCode(POMember.getInstance().getAuth(), DeviceUtils.dipToPX(GoogleAuthenticationActivity.this, 230)
                        , ContextCompat.getColor(GoogleAuthenticationActivity.this, R.color.black), ContextCompat.getColor(GoogleAuthenticationActivity.this, R.color.color_f1f1f1), null));
            }
        }
    };

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, GoogleAuthenticationActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_identity);
        ButterKnife.bind(this);
        initBaseView();
    }

    private void initBaseView() {
        initStatusBar(ContextCompat.getColor(this, R.color.color_0c0c0c), googleTitle);
        EventBus.getDefault().register(this);
        googleTitle.setTitle(getString(R.string.google_identity_binding))
                .setTextColor(ContextCompat.getColor(this, R.color.color_ffffff));
        googleTitle.setLeftImage(R.drawable.back_white);
        googleTitle.setLayBac(R.color.color_0c0c0c);
        googleTitle.setRightText(getString(R.string.next_step), new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ToolUtil.isFastClick()) {
                    startActivity(GoogleCodeActivity.getCallingIntent(GoogleAuthenticationActivity.this));
                }
            }
        });
        SpannableString spannableString = new SpannableString(getString(R.string.putforward));
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#f7f7f7")), 21, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvPutforward.setText(spannableString);
        googleCertification.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线

        handler.sendEmptyMessage(1);
        showSecretkey();
    }

    private void showSecretkey() {
        if (!StringUtils.isEmpty(POMember.getInstance().getAuth()) && POMember.getInstance().getAuth().contains("secret")) {
            String[] all = POMember.getInstance().getAuth().split("secret=");
            Gson gson = new Gson();
            poGoogleAuthen = new PoGoogleAuthen();
            poGoogleAuthen.setOtpauth(all[0]);
            poGoogleAuthen.setSecret(all[1]);
            String json = gson.toJson(poGoogleAuthen);
            poGoogleAuthen = gson.fromJson(json, PoGoogleAuthen.class);
            tvGoogle.setText(poGoogleAuthen.getSecret());
            googleCopy.setVisibility(View.VISIBLE);
        } else {
            googleCopy.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick({R.id.google_copy, R.id.google_certification, R.id.tv_putforward})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.google_copy:
                ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText(POMember.getInstance().getAuth());
                ToastUtils.showToast(getString(R.string.copy_success));
                break;
            case R.id.tv_putforward:
                ToastUtils.showCenterToast(this
                        , Toast.LENGTH_SHORT, R.string.coming_soon);
                break;
            case R.id.google_certification:
                ToastUtils.showCenterToast(this
                        , Toast.LENGTH_SHORT, R.string.coming_soon);
                break;
        }
    }
}
