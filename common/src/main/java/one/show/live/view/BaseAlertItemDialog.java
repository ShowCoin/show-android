package one.show.live.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import one.show.live.common.R;


/**
 * Created by ydeng on 16/4/8.
 */
public class BaseAlertItemDialog extends Dialog implements View.OnClickListener {


    private Context mContext;

    TextView mTvTite;
    TextView mTvMessage;

    Button mBtnLeft;
    Button mBtnRight;


    onSuccessListener mSuccessListener;
    onCancelListener mCancelListener;

//    private String title;
//    private String message;

    public BaseAlertItemDialog(Context context) {
        this(context, R.layout.custom_item_dialog, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private BaseAlertItemDialog(Context context, int layoutResID, int width, int height) {
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
        mTvTite = (TextView) findViewById(R.id.dialog_title);
        mTvTite.setVisibility(View.GONE);
        mTvMessage = (TextView) findViewById(R.id.dialog_message);
        mTvMessage.setVisibility(View.GONE);
        mBtnLeft = (Button) findViewById(R.id.dialog_left_buton);
        mBtnLeft.setOnClickListener(this);
        mBtnRight = (Button) findViewById(R.id.dialog_right_buton);
        mBtnRight.setOnClickListener(this);
        findViewById(R.id.titleMsgLine).setVisibility(View.GONE);
    }




    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.dialog_left_buton) {
            if (mCancelListener != null) {
                mCancelListener.onCancel(this);
            }
        } else if (v.getId() == R.id.dialog_right_buton) {
            if (mSuccessListener != null) {
                mSuccessListener.onSuccess(this);
            }
        }
    }

    public BaseAlertItemDialog setTitle(String content) {
        if(TextUtils.isEmpty(content)){
            mTvTite.setVisibility(View.GONE);
        }else {
//        this.title = content;
            mTvTite.setVisibility(View.VISIBLE);
            mTvTite.setText(content);
        }
        return this;
    }

    public void setTitle(@StringRes int resId) {
//        this.title = mContext.getString(resId);
        mTvTite.setText(resId);
    }


    public BaseAlertItemDialog setMessage(String content) {
//        this.title = mContext.getString(resId);
        setMessageSize(13);
        if(TextUtils.isEmpty(content)){
            mTvMessage.setVisibility(View.GONE);
        }else{
            mTvMessage.setVisibility(View.VISIBLE);
            mTvMessage.setText(content);
        }
        return this;
    }


    public BaseAlertItemDialog setmRightButtonListener(String btnText, onSuccessListener mSuccessListener) {
        mBtnRight.setText(btnText);
        mBtnRight.setVisibility(View.VISIBLE);
        setmRightButtonListener(mSuccessListener);
        return this;
    }
    public BaseAlertItemDialog setmRightButtonListener(int color, String btnText, onSuccessListener mSuccessListener) {
        mBtnRight.setText(btnText);
        mBtnRight.setVisibility(View.VISIBLE);
        mBtnRight.setTextColor(ContextCompat.getColor(getContext(), color));
        setmRightButtonListener(mSuccessListener);
        return this;
    }

    public BaseAlertItemDialog setmRightButtonListener(@StringRes int resId, onSuccessListener mSuccessListener) {
        mBtnRight.setText(resId);
        mBtnRight.setVisibility(View.VISIBLE);
        setmRightButtonListener(mSuccessListener);
        return this;
    }

    @Override
    public void show() {
        super.show();
    }

    public BaseAlertItemDialog setmRightButtonListener(onSuccessListener mSuccessListener) {
        this.mSuccessListener = mSuccessListener;
        return this;
    }

    public BaseAlertItemDialog setmLeftButtonListener(String btnText, onCancelListener mCancelListener) {
        mBtnLeft.setText(btnText);
        setmLeftButtonListener(mCancelListener);
        return this;
    }


    public BaseAlertItemDialog setMessageSize(int textSize) {
        mTvMessage.setTextSize(textSize);
        return this;
    }

    public BaseAlertItemDialog setmLeftButtonListener(int color, String btnTex, onCancelListener mCancelListener) {
        mBtnLeft.setText(btnTex);
        mBtnLeft.setTextColor(ContextCompat.getColor(getContext(), color));
        setmLeftButtonListener(mCancelListener);
        return this;
    }

    public BaseAlertItemDialog setmLeftButtonListener(@StringRes int resId, onCancelListener mCancelListener) {
        mBtnLeft.setText(resId);
        setmLeftButtonListener(mCancelListener);
        return this;
    }

    public BaseAlertItemDialog setmLeftButtonListener(onCancelListener mCancelListener) {
        this.mCancelListener = mCancelListener;
        return this;
    }

    public BaseAlertItemDialog setRightButtonIsGone() {
        findViewById(R.id.button_line).setVisibility(View.GONE);
        mBtnRight.setVisibility(View.GONE);
        return this;
    }

    public interface onSuccessListener {
        void onSuccess(Dialog dialog);
    }

    public interface onCancelListener {
        void onCancel(Dialog dialog);
    }
}
