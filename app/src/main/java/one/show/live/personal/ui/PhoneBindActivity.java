package one.show.live.personal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.PhoneNumberCheck;
import one.show.live.common.util.StringUtils;
import one.show.live.common.view.ToastUtils;
import one.show.live.login.presenter.PhoneRegisteredPresenter;
import one.show.live.login.view.LoginView;
import one.show.live.login.view.RegisteredOneView;
import one.show.live.po.eventbus.BindEventBean;
import one.show.live.util.CheckOnDoubleClickUtils;
import one.show.live.util.StringUtil;
import one.show.live.util.WeakHandler;
import one.show.live.widget.TitleView;

public class PhoneBindActivity extends BaseFragmentActivity implements LoginView, RegisteredOneView, WeakHandler.IHandler {


    @BindView(R.id.phone_title)
    TitleView phoneTitle;
    @BindView(R.id.phonetitle)
    LinearLayout phonetitle;
    @BindView(R.id.phone_num)
    EditText phoneNum;
    @BindView(R.id.video_view)
    View videoView;
    @BindView(R.id.send_code)
    TextView sendCode;
    @BindView(R.id.message_authentication_code)
    EditText messageAuthenticationCode;
    @BindView(R.id.input_settinga_password)
    EditText inputSettingaPassword;
    @BindView(R.id.iv_shpassword)
    ImageView ivShpassword;
    @BindView(R.id.confirm_password)
    EditText confirmPassword;
    @BindView(R.id.btn_next)
    TextView btnNext;
    @BindView(R.id.iv_confirm)
    ImageView ivConfirm;
    boolean isShowPassword = false;
    boolean isConfiemPassword = false;
    int countdownInt = 120;
    boolean countdown = true;//倒计时判断
    PhoneRegisteredPresenter phoneRegisteredPresenter;
    boolean phoneBindPhone_of;//判断手机号码是否输入了
    boolean phoneBindVerification_of;//判断验证码是否输入了

    boolean phoneBindPassword_of;//判断密码是否输入了
    boolean phoneBindConfirmPassword_of;//判断两次密码是否输入了

    WeakHandler weakHandler = new WeakHandler(this);


    @Override
    public void handleMessage(Message msg) {
        if (isContextAlive()) {
            switch (msg.what) {
                case 1:
                    if (countdownInt == 0) {
                        countdown = false;//让倒计时不循环
                        sendCode.setEnabled(true);
                        sendCode.setText(getString(R.string.code_send));
                        break;
                    }
                    sendCode.setText(String.format("%ds", countdownInt));
                    countdownInt = countdownInt - 1;
                    break;
            }
        }
    }


    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, PhoneBindActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonebind);
        ButterKnife.bind(this);
        initBaseView();
    }

    private void initBaseView() {
        initStatusBar(ContextCompat.getColor(this, R.color.color_0c0c0c), phoneTitle);
        EventBus.getDefault().register(this);
        phoneTitle.setTitle(getString(R.string.phone_binding))
                .setTextColor(ContextCompat.getColor(this, R.color.color_ffffff));
        phoneTitle.setLeftImage(R.drawable.back_white);
        phoneTitle.setLayBac(R.color.color_0c0c0c);
        phoneRegisteredPresenter = new PhoneRegisteredPresenter(this, this);
        phoneNum.addTextChangedListener(new MyTextWatcher(phoneNum, 2));
        messageAuthenticationCode.addTextChangedListener(new MyTextWatcher(messageAuthenticationCode, 3));
        inputSettingaPassword.addTextChangedListener(new MyTextWatcher(inputSettingaPassword, 4));
        confirmPassword.addTextChangedListener(new MyTextWatcher(confirmPassword, 5));


    }

    @OnClick({R.id.iv_shpassword, R.id.iv_confirm, R.id.send_code, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_shpassword:
                if (isShowPassword) {
                    ivShpassword.setImageResource(R.drawable.hidepassword);
                    inputSettingaPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    int length = inputSettingaPassword.getText().toString().length();
                    inputSettingaPassword.setSelection(length);
                    isShowPassword = false;
                } else {
                    inputSettingaPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivShpassword.setImageResource(R.drawable.showpassword);
                    int length = inputSettingaPassword.getText().toString().length();
                    inputSettingaPassword.setSelection(length);
                    isShowPassword = true;
                }
                inputSettingaPassword.postInvalidate();
                break;
            case R.id.iv_confirm:
                if (isConfiemPassword) {
                    ivConfirm.setImageResource(R.drawable.hidepassword);
                    confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    int length = confirmPassword.getText().toString().length();
                    confirmPassword.setSelection(length);
                    isConfiemPassword = false;
                } else {
                    ivConfirm.setImageResource(R.drawable.showpassword);
                    confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                    int length = confirmPassword.getText().toString().length();
                    confirmPassword.setSelection(length);
                    isConfiemPassword = true;
                }
                confirmPassword.postInvalidate();
                break;
            case R.id.send_code:
                CheckOnDoubleClickUtils.onClick(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phone = phoneNum.getText().toString();
                        if (PhoneNumberCheck.PhoneOf(phone)) {
                            countdownInt = 120;//重置倒计时时间
                            sendCode.setEnabled(false);
                            countdown = true;//让倒计时可以循环
                            phoneRegisteredPresenter.codeListener(phone, 3);
                        } else {
                            ToastUtils.showToast(getString(R.string.correctphonenumber));
                        }
                    }
                });
                break;
            case R.id.btn_next:


                if (!StringUtil.equal(inputSettingaPassword.getText().toString(), confirmPassword.getText().toString())) {
                    ToastUtils.showCenterToast(this
                            , Toast.LENGTH_SHORT, R.string.inconsistency);
                    return;
                }
                if (StringUtils.isPhonePassWord(inputSettingaPassword.getText().toString())) {
                    phoneRegisteredPresenter.bindingData(phoneNum.getText().toString(), messageAuthenticationCode.getText().toString(), inputSettingaPassword.getText().toString());
                } else {
                    ToastUtils.showCenterToast(this
                            , Toast.LENGTH_SHORT, R.string.password_conformity);
                }
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
                    , Toast.LENGTH_SHORT, R.string.phonebindsuccess);
            finish();
            EventBus.getDefault().post(new BindEventBean(BindEventBean.PHONEBIND));
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
                    if (content.length() < 11) {
                        phoneBindPhone_of = false;
                    } else {
                        phoneBindPhone_of = true;
                    }
                } else if (type == 3) {
                    if (content.length() < 0) {
                        phoneBindVerification_of = false;
                    } else {
                        phoneBindVerification_of = true;
                    }
                } else if (type == 4) {
                    if (content.length() < 8) {
                        phoneBindPassword_of = false;
                    } else {
                        phoneBindPassword_of = true;
                    }
                } else if (type == 5) {
                    if (content.length() < 8) {
                        phoneBindConfirmPassword_of = false;
                    } else {
                        phoneBindConfirmPassword_of = true;
                    }
                }

            } else {//反之设置为初始状态
                if (type == 2) {
                    phoneBindPhone_of = false;
                } else if (type == 3) {
                    phoneBindVerification_of = false;
                } else if (type == 4) {
                    phoneBindPassword_of = false;
                } else if (type == 5) {
                    phoneBindConfirmPassword_of = false;
                }
            }
            //如果三个文本框都输入了文字的话，就让按钮可以点击
            if (phoneBindPhone_of && phoneBindVerification_of && phoneBindPassword_of && phoneBindConfirmPassword_of) {
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
            sendCode.setEnabled(true);
        }
    }
}
