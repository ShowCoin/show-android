package one.show.live.personal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.view.ToastUtils;
import one.show.live.login.presenter.PhoneRegisteredPresenter;
import one.show.live.login.view.LoginView;
import one.show.live.login.view.RegisteredOneView;
import one.show.live.po.eventbus.BindEventBean;
import one.show.live.po.eventbus.CaptitalBean;
import one.show.live.util.CheckOnDoubleClickUtils;
import one.show.live.util.StringUtil;
import one.show.live.util.WeakHandler;
import one.show.live.widget.TitleView;
public class MailBindingActivity extends BaseFragmentActivity implements LoginView, RegisteredOneView, WeakHandler.IHandler {


    int countdownInt = 120;
    boolean countdown = true;//倒计时判断
    PhoneRegisteredPresenter phoneRegisteredPresenter;
    boolean emailPassword_of;
    boolean emailCode_of;

    WeakHandler weakHandler = new WeakHandler(this);
    @BindView(R.id.email_title)
    TitleView emailTitle;
    @BindView(R.id.email)
    LinearLayout email;
    @BindView(R.id.email_password)
    EditText emailPassword;
    @BindView(R.id.email_code)
    EditText emailCode;
    @BindView(R.id.send_emailcode)
    TextView sendEmailcode;
    @BindView(R.id.btn_next)
    TextView btnNext;


    @Override
    public void handleMessage(Message msg) {
        if (isContextAlive()) {
            switch (msg.what) {
                case 1:
                    if (countdownInt == 0) {
                        countdown = false;//让倒计时不循环
                        sendEmailcode.setEnabled(true);
                        sendEmailcode.setText(getString(R.string.code_send));
                        break;
                    }
                    sendEmailcode.setText(String.format("%ds", countdownInt));
                    countdownInt = countdownInt - 1;
                    break;
            }
        }
    }


    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, MailBindingActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emaibind);
        ButterKnife.bind(this);
        initBaseView();
    }

    private void initBaseView() {
        initStatusBar(ContextCompat.getColor(this, R.color.color_0c0c0c), emailTitle);
        EventBus.getDefault().register(this);
        emailTitle.setTitle(getString(R.string.mail_bound))
                .setTextColor(ContextCompat.getColor(this, R.color.color_ffffff));
        emailTitle.setLeftImage(R.drawable.back_white);
        emailTitle.setLayBac(R.color.color_0c0c0c);
        phoneRegisteredPresenter = new PhoneRegisteredPresenter(this, this);
        emailPassword.addTextChangedListener(new MyTextWatcher(emailPassword, 2));
        emailCode.addTextChangedListener(new MyTextWatcher(emailCode, 3));


    }

    @OnClick({R.id.send_emailcode, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.send_emailcode:
                CheckOnDoubleClickUtils.onClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countdownInt = 120;//重置倒计时时间
                        sendEmailcode.setEnabled(false);
                        countdown = true;//让倒计时可以循环
                        phoneRegisteredPresenter.emailListener(emailPassword.getText().toString());
                    }
                });
                break;
            case R.id.btn_next:
                phoneRegisteredPresenter.bindingEmaiData(emailPassword.getText().toString(), emailCode.getText().toString());
                break;
        }
    }

    @Override
    public void loginFailed(int statusCode, String msg) {
        if (isContextAlive()) {
            ToastUtils.showCenterToast(this
                    , Toast.LENGTH_SHORT, msg);
        }
    }

    @Override
    public void loginSuccess() {
        if (isContextAlive()) {
            ToastUtils.showCenterToast(this
                    , Toast.LENGTH_SHORT, R.string.mail_success);
            finish();
            EventBus.getDefault().post(new BindEventBean(BindEventBean.EMAILBIND));
        }
    }

    @Override
    public Context getContext() {
        return null;
    }


    /**
     * 监听输入框的文字输入
     */
    class MyTextWatcher implements TextWatcher {

        EditText editText;//需要监听的文本框
        int type;

        public MyTextWatcher(EditText editText, int type) {
            this.editText = editText;
            this.type = type;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String content = editText.getText().toString();
            if (content.length() > 0) {//如果文本框输入了文字的话就设置为true
                if (type == 2) {
                    emailPassword_of = true;
                } else if (type == 3) {
                    if (content.length() < 0) {
                        emailCode_of = false;
                    } else {
                        emailCode_of = true;
                    }
                }

            } else {//反之设置为初始状态
                if (type == 2) {
                    emailPassword_of = false;
                } else if (type == 3) {
                    emailCode_of = false;
                }
            }
            //如果三个文本框都输入了文字的话，就让按钮可以点击
            if (emailPassword_of && emailCode_of) {
                btnNext.setEnabled(true);
            } else {
                btnNext.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }


    @Override
    public void codeSuccess() {
        if (isContextAlive()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (countdown) {

                        weakHandler.sendEmptyMessage(1);

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }).start();
        }
    }

    @Override
    public void codeFailure() {
        if (isContextAlive()) {
            sendEmailcode.setEnabled(true);
        }
    }
}
