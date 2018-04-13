package one.show.live.common.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import one.show.live.common.common.R;


/**
 * Created by ydeng on 16/4/8.
 * 引导提示框
 */
public class BaseGuideDialog extends Dialog implements View.OnClickListener {

    public static final int MODE_LOCATION = 0x100;//位置引导
    public static final int MODE_PHOTO = 0x200;//照片引导
    public static final int MODE_INFO = 0x300;//完善信息引导
    public static final int MODE_PHONE = 0x400;//手机号引导
    public static final int MODE_MONEY = 0x500;//提现引导
    public static final int MODE_NOTIFY = 0x600;//通知引导


    private Context mContext;

    TextView mTvDesc1;//引导描述1
    TextView mTvDesc2;//引导描述2
    TextView mTvOk;//确认按钮
    TextView mTvTitle;//引导标题
    ImageView mIvCenter;//引导图片
    ImageView mIvClose;//关闭按钮


    onSuccessListener mSuccessListener;

    public BaseGuideDialog(Context context) {
        this(context, R.layout.guide_dialog, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private BaseGuideDialog(Context context, int layoutResID, int width, int height) {
        super(context, R.style.MyDialogStyle);
        setContentView(layoutResID);
        mContext = context;
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
//        if (resAnim != 0) {
//            window.setWindowAnimations(resAnim);
//        }
        window.setBackgroundDrawableResource(android.R.color.transparent);
        params.width = width;
        params.height = height;
//        params.gravity = G;
        window.setAttributes(params);
        setCanceledOnTouchOutside(true);
        setCancelable(true);
        initView();
    }

    private void initView() {
        mTvDesc1 = (TextView) findViewById(R.id.tv_desc_1);
        mTvDesc2 = (TextView) findViewById(R.id.tv_desc_2);
        mTvDesc2.setVisibility(View.GONE);
        mIvClose = (ImageView) findViewById(R.id.iv_close);
        mIvClose.setOnClickListener(this);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mTvOk = (TextView) findViewById(R.id.tv_ok);
        mTvOk.setOnClickListener(this);
        mIvCenter = (ImageView) findViewById(R.id.iv_center);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_ok) {
            if (mSuccessListener != null) {
                mSuccessListener.onSuccess(this);
            }
        }else if(v.getId() == R.id.iv_close){
            dismiss();
        }
    }

    public void setMode(int mode){
        mTvDesc2.setVisibility(View.GONE);
        switch (mode){
            case MODE_INFO:
                mIvCenter.setImageResource(R.drawable.icon_dialog_guide_info);
                mTvTitle.setText("完善资料");
                mTvDesc1.setText("大家都是有头有脸的人物\n没有资料没有信任度哦");
                mTvOk.setText("快速填写资料");
                break;
            case MODE_LOCATION:
                mIvCenter.setImageResource(R.drawable.icon_dialog_guide_location);
                mTvTitle.setText("获取位置失败");
                mTvDesc1.setText("您的地理位置获取失败\n可能会错过很多身边的朋友");
                mTvOk.setText("开启地理位置");
                break;
            case MODE_MONEY:
                mIvCenter.setImageResource(R.drawable.icon_dialog_guide_money);
                mTvTitle.setText("收益提现");
                //设置礼物名称
                String leftContent = "请前往微信公众号";
                String centerContent = "SEEU时刻";
                SpannableStringBuilder spStr = new SpannableStringBuilder();
                spStr.append(leftContent);
                spStr.append(centerContent);
                spStr.setSpan(new ForegroundColorSpan(Color.argb(255, 0xfc, 0x3f, 0x99)), leftContent.length(), leftContent.length() + centerContent.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mTvDesc1.setText(spStr);
                mTvDesc2.setVisibility(View.VISIBLE);
                mTvDesc2.setText("（微信号：SEEU516）");
                mTvOk.setText("前往微信提现");
                break;
            case MODE_NOTIFY:
                mIvCenter.setImageResource(R.drawable.icon_dialog_guide_notify);
                mTvTitle.setText("通知未开启");
                mTvDesc1.setText("当前未开启通知提醒\n可能会错过好友消息哦");
                mTvOk.setText("开启通知提示");
                break;
            case MODE_PHONE:
                mIvCenter.setImageResource(R.drawable.icon_dialog_guide_phone);
                mTvTitle.setText("填写手机号");
                mTvDesc1.setText("为了您的账号安全\n请尽快绑定手机号");
                mTvOk.setText("验证手机号");
                break;
            case MODE_PHOTO:
                mIvCenter.setImageResource(R.drawable.icon_dialog_guide_photo);
                mTvTitle.setText("头像未认证");
                mTvDesc1.setText("检测到您的头像未验证\n部分功能使用会受到影响哦");
                mTvOk.setText("去认证头像");
                break;
        }
    }


    public BaseGuideDialog setSuccessButtonListener(String btnText, onSuccessListener mSuccessListener) {
        mTvOk.setText(btnText);
        setSuccessButtonListener(mSuccessListener);
        return this;
    }


    public BaseGuideDialog setSuccessButtonListener(onSuccessListener mSuccessListener) {
        this.mSuccessListener = mSuccessListener;
        return this;
    }


    public interface onSuccessListener {
        void onSuccess(Dialog dialog);
    }
}
