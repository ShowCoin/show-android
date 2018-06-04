package one.show.live.personal.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.po.POEventBus;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.Constants;
import one.show.live.common.util.DeviceUtils;
import one.show.live.common.util.FrescoUtils;
import one.show.live.common.view.ToastUtils;
import one.show.live.personal.presenter.DataPresenter;
import one.show.live.personal.view.DataView;
import one.show.live.po.POFansList;
import one.show.live.po.eventbus.EUpdateAvatar;
import one.show.live.po.eventbus.FocusOnEventBean;
import one.show.live.util.StringUtil;
import one.show.live.util.SystemUtils;
import one.show.live.widget.ActionSheetDialog;
import one.show.live.widget.HeadImageView;
import one.show.live.widget.OneWheelDialog;
import one.show.live.widget.TimeDialog;
import one.show.live.widget.TitleView;
import one.show.live.widget.TwoWheelDialog;

/**
 * Created by Nano on 2018/4/19.
 * 编辑资料页面
 */

public class DataActivity extends BaseFragmentActivity implements DataView {


    @BindView(R.id.data_header)
    HeadImageView dataHeader;
    @BindView(R.id.data_header_lay)
    RelativeLayout dataHeaderLay;
    @BindView(R.id.data_name)
    EditText dataName;
    @BindView(R.id.data_name_lay)
    LinearLayout dataNameLay;
    @BindView(R.id.data_id)
    EditText dataId;
    @BindView(R.id.data_id_lay)
    LinearLayout dataIdLay;
    @BindView(R.id.data_gender)
    TextView dataGender;
    @BindView(R.id.data_gender_lay)
    LinearLayout dataGenderLay;
    @BindView(R.id.data_address)
    TextView dataAddress;
    @BindView(R.id.data_address_lay)
    LinearLayout dataAddressLay;
    @BindView(R.id.data_desc)
    TextView dataDesc;
    @BindView(R.id.data_desc_lay)
    LinearLayout dataDescLay;
    @BindView(R.id.data_code_lay)
    LinearLayout dataCodeLay;
    @BindView(R.id.data_title)
    TitleView dataTitle;

    int size40;
    @BindView(R.id.data_view)
    View dataView;
    @BindView(R.id.data_header_bg)
    SimpleDraweeView dataHeaderBg;

    DataPresenter dataPresenter;
    @BindView(R.id.data_birthday)
    TextView dataBirthday;
    @BindView(R.id.data_birthday_lay)
    LinearLayout dataBirthdayLay;
    @BindView(R.id.data_proportion)
    TextView dataProportion;
    @BindView(R.id.data_proportion_lay)
    LinearLayout dataProportionLay;
    @BindView(R.id.data_operating)
    TextView dataOperating;
    @BindView(R.id.data_operating_lay)
    LinearLayout dataOperatingLay;
    @BindView(R.id.data_name_cha)
    ImageView dataNameCha;
    @BindView(R.id.data_id_num)
    TextView dataIdNum;

    boolean nameHasFocus;


    private int gender;//存储修改之后的性别
    private String birthday;//存储修改之后的生日
    private String citys;//存储修改城市
    private int extract;//分成比例
    private int operation;//运营分成
    private String nickName;//昵称
    private String popId;//靓号

    String[] stringsCon = new String[]{"10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%"};
    List<String> listCon;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, DataActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        initBaseView();
    }

    private void initBaseView() {
        initStatusBar(ContextCompat.getColor(this,R.color.transparent),dataTitle);
        EventBus.getDefault().register(this);
        dataTitle.setTitle(getString(R.string.me_data))
                .setTextColor(ContextCompat.getColor(this, R.color.color_ffffff));
        dataTitle.setLeftImage(R.drawable.back_white);
        dataTitle.setLayBac(R.color.transparent);
        dataTitle.setRightText(getString(R.string.save), new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nickName = dataName.getText().toString();
                popId = dataId.getText().toString();

                if (nickName.length() <= 0) {
                    ToastUtils.showToast(getString(R.string.nickname_cannot_be_empty));
                } else if (popId.length() <= 0) {
                    ToastUtils.showToast(getString(R.string.id_cannot_be_empty));
                } else {
                    Map<String, String> map = new HashMap<>();

                    if (!nickName.equals(POMember.getInstance().getNickname()) && !popId.equals(POMember.getInstance().getPopularNo())) {
                        map.put("nickname", nickName);
                        map.put("popularNo", popId);
                        dataPresenter.uploadInfo(map, DataPresenter.POPID_NAME);
                        return;
                    }
                    if (!nickName.equals(POMember.getInstance().getNickname())) {
                        map.put("nickname", nickName);
                        dataPresenter.uploadInfo(map, DataPresenter.NAME);
                        return;
                    }
                    if (!popId.equals(POMember.getInstance().getPopularNo())) {
                        map.put("popularNo", popId);
                        dataPresenter.uploadInfo(map, DataPresenter.POPID);
                        return;
                    }
                    finish();

                }
            }
        });

        size40 = DeviceUtils.dipToPX(this, 40);

        dataPresenter = new DataPresenter(this);
        listCon = Arrays.asList(stringsCon);

        StringUtil.setEditTextInputSpace(dataName, 12);
    }


    /**
     * 添加数据
     *
     * @param poMember
     */
    private void setMyData(POMember poMember) {

        if (poMember == null) {
            return;
        }

        if (poMember.getGender() == 1) {//1男2女0未知
            dataGender.setText(getString(R.string.male));
        } else if (poMember.getGender() == 2) {
            dataGender.setText(getString(R.string.female));
        } else {
            dataGender.setText(getString(R.string.unknown));
        }
        dataName.setText(poMember.getNickname());
        nickName = poMember.getNickname();
        dataName.addTextChangedListener(new EditChangedListener(dataName, 1));
        dataName.setOnFocusChangeListener(new onFocusChange());

        dataAddress.setText(poMember.getCity());

        dataDesc.setText(poMember.getDescriptions());

        dataHeader.setImage(poMember.getAvatar(),"");
        dataHeader.setVisilevel();

        dataBirthday.setText(poMember.getBirthday());

        dataProportion.setText(poMember.getExtract() + "%");//分成比例
        dataOperating.setText(poMember.getOperation_extract() + "%");//运营分成

        dataId.setText(POMember.getInstance().getPopularNo());
        popId = POMember.getInstance().getPopularNo();
        dataIdNum.setText(String.format("%d/16", dataId.getText().toString().length()));
        dataId.addTextChangedListener(new EditChangedListener(dataId, 2));

        if (!TextUtils.isEmpty(poMember.getLarge_avatar())) {

            FrescoUtils.bindWithBlur(this, dataHeaderBg, poMember.getLarge_avatar(), 25);
        } else {
            FrescoUtils.bindWithBlur(this, dataHeaderBg, poMember.getAvatar(), 25);
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        setMyData(POMember.getInstance());
    }

    @OnClick({R.id.data_header_lay, R.id.data_id_lay, R.id.data_gender_lay, R.id.data_address_lay, R.id.data_name_cha, R.id.data_desc_lay,
            R.id.data_code_lay, R.id.data_birthday_lay, R.id.data_proportion_lay, R.id.data_operating_lay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.data_header_lay:
                startActivity(UpDataHeadActivity.getCallingIntent(this));
                break;
            case R.id.data_id_lay:
                break;
            case R.id.data_gender_lay://性别
                genderDialog();
                break;
            case R.id.data_birthday_lay://生日
                TimeDialog dialog = new TimeDialog(DataActivity.this);
                dialog.showTimeDialog(new TimeDialog.OnTextListening() {
                    @Override
                    public void onText(String date) {
                        birthday = date;
                        dataBirthday.setText(date);
                        if (POMember.getInstance() != null && !POMember.getInstance().getBirthday().equals(dataBirthday.getText().toString())) {
                            Map<String, String> map = new HashMap<>();
                            map.put("birthday", dataBirthday.getText().toString());
                            dataPresenter.uploadInfo(map, DataPresenter.BIRTHDAY);
                        }
                    }
                }, dataBirthday.getText().toString());

                break;
            case R.id.data_name_cha://清空昵称
                dataName.setText("");
                break;
            case R.id.data_address_lay:
                TwoWheelDialog twoWheelDialog = new TwoWheelDialog(this);
                twoWheelDialog.ShowCityDialog(new TwoWheelDialog.OnCityLisenter() {
                    @Override
                    public void onCity(String provinces, String city) {
                        dataAddress.setText(city);
                        citys = city;
                        Map<String, String> map = new HashMap<>();
                        map.put("city", city);
                        dataPresenter.uploadInfo(map, DataPresenter.CITY);
                    }
                });
                break;
            case R.id.data_desc_lay:
                startActivity(NickNameActivity.getCallingIntent(this, NickNameActivity.DESC_TYPE));
                break;
            case R.id.data_code_lay://我的二维码
                startActivity(MyCodeActivity.getCallingInteng(this));
                break;
            case R.id.data_proportion_lay://分成比例
                OneWheelDialog oneWheelDialog = new OneWheelDialog(this);
                oneWheelDialog.showOneDialog(new OneWheelDialog.OnTextListening() {
                    @Override
                    public void onText(String date) {

                        dataProportion.setText(date);
                        extract = Integer.parseInt(date.replace("%", ""));
                        Map<String, String> map = new HashMap<>();
                        map.put("extract", extract + "");
                        dataPresenter.uploadInfo(map, DataPresenter.EXTRACT);

                    }
                }, listCon, dataProportion.getText().toString());
                break;
            case R.id.data_operating_lay://运营分成
                OneWheelDialog oneWheelDialog1 = new OneWheelDialog(this);
                oneWheelDialog1.showOneDialog(new OneWheelDialog.OnTextListening() {
                    @Override
                    public void onText(String date) {
                        dataOperating.setText(date);
                        operation = Integer.parseInt(date.replace("%", ""));
                        Map<String, String> map = new HashMap<>();
                        map.put("operation_extract", operation + "");
                        dataPresenter.uploadInfo(map, DataPresenter.OPERATION);
                    }
                }, listCon, dataOperating.getText().toString());
                break;
        }
    }

    /**
     * 选择性别的弹窗
     */
    public void genderDialog() {
        new ActionSheetDialog(this).builder()
                .addSheetItem(DataActivity.this.getString(R.string.male), ActionSheetDialog.SheetItemColor.blue, new ActionSheetDialog.OnSheetItemClickListener() {
                    @Override
                    public void onClick(int which) {
                        Map<String, String> map = new HashMap<>();
                        map.put("gender", "1");
                        dataPresenter.uploadInfo(map, DataPresenter.GENDER);
                        dataGender.setText(DataActivity.this.getString(R.string.male));
                        gender = 1;

                    }
                }).addSheetItem(DataActivity.this.getString(R.string.female), ActionSheetDialog.SheetItemColor.Red, new ActionSheetDialog.OnSheetItemClickListener() {
            @Override
            public void onClick(int which) {
                Map<String, String> map = new HashMap<>();
                map.put("gender", "2");
                dataPresenter.uploadInfo(map, DataPresenter.GENDER);
                dataGender.setText(DataActivity.this.getString(R.string.female));
                gender = 2;
            }
        }).show();
    }

    /**
     * 信息修改完成回调
     *
     * @param isSuccess
     * @param type      这个参数是用来判断 修改的什么资料
     */
    @Override
    public void onFinishUploadInfo(boolean isSuccess, int type) {
        if (isContextAlive() && isSuccess) {

            switch (type) {
                case DataPresenter.GENDER:
                    POMember.fixGender(gender);//修改性别
                    break;
                case DataPresenter.BIRTHDAY:
                    POMember.fixBirthday(birthday);//修改性别
                    break;
                case DataPresenter.EXTRACT://分成比例
                    POMember.fixExtract(extract);//修改分成比例
                    break;
                case DataPresenter.OPERATION://
                    POMember.fixOperation(operation);//修改运营分成
                    break;
                case DataPresenter.CITY://
                    POMember.fixCity(citys);//修改城市
                    break;
                case DataPresenter.NAME://
                    POMember.fixNickName(nickName);//修改昵称
                    finish();
                    break;
                case DataPresenter.POPID://
                    POMember.fixPopularNo(popId);//修改靓号
                    finish();
                    break;
                case DataPresenter.POPID_NAME://
                    //昵称和靓号同时修改
                    POMember.fixNickName(nickName);//修改昵称
                    POMember.fixPopularNo(popId);//修改靓号
                    finish();
                    break;
            }

        }
    }

    /**
     * 监听edit
     */
    class EditChangedListener implements TextWatcher {
        EditText editText;
        int type;//1是昵称的2是ID的

        public EditChangedListener(EditText editText, int type) {
            this.editText = editText;
            this.type = type;
        }

        // 输入文本之前的状态
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        // 输入文字中的状态，count是一次性输入字符数
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        // 输入文字后的状态
        @Override
        public void afterTextChanged(Editable s) {

            int length = s.length();
            if (type == 1) {
                if (length > 0 && nameHasFocus) {
                    dataNameCha.setVisibility(View.VISIBLE);
                } else {
                    dataNameCha.setVisibility(View.GONE);
                }

            } else {
                dataIdNum.setText(String.format("%d/16", length));
            }
        }
    }

    /**
     * 监控editext的焦点
     */
    class onFocusChange implements View.OnFocusChangeListener {

        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (isContextAlive()) {
                nameHasFocus = hasFocus;
                if (hasFocus) {
                    // 获得焦点
                    if (dataName.getText().toString().length() > 0) {
                        dataNameCha.setVisibility(View.VISIBLE);
                    }
                } else {
                    // 失去焦点
                    dataNameCha.setVisibility(View.GONE);
                }
            }
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EUpdateAvatar event) { //接收上传完头像的通知
        if (event == null) {
            return;
        }
        if (!TextUtils.isEmpty(event.getLarge_avatar())) {

            FrescoUtils.bindWithBlur(this, dataHeaderBg, event.getLarge_avatar(), 25);
        } else {
            FrescoUtils.bindWithBlur(this, dataHeaderBg, event.getAvatar(), 25);
        }
        dataHeader.setImage(event.getAvatar(),"");


    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
