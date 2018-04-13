package one.show.live.common.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import one.show.live.common.util.DeviceUtils;
import one.show.live.common.common.R;


/**
 * Created by ydeng on 16/4/8.
 */
public class BaseAlertDialog extends Dialog implements View.OnClickListener {


    private Context mContext;

    TextView mTvTite;
    TextView mTvMessage;

    Button mBtnLeft;
    Button mBtnRight;

    LinearLayout other_btn;


    onSuccessListener mSuccessListener;
    onCancelListener mCancelListener;

    List<TextView> otherListeners = new ArrayList<>();
//    private String title;
//    private String message;

    public BaseAlertDialog(Context context) {
        this(context, R.layout.custom_dialog, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private BaseAlertDialog(Context context, int layoutResID, int width, int height) {
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
        other_btn = (LinearLayout) findViewById(R.id.other_btn);
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

    public void clearOtherBtn() {
        other_btn.removeAllViews();
        otherListeners.clear();
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

    public BaseAlertDialog setTitle(String content) {
        if (TextUtils.isEmpty(content)) {
            mTvTite.setVisibility(View.GONE);
        } else {
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


    public BaseAlertDialog setMessage(String content) {
//        this.title = mContext.getString(resId);
        setMessageSize(13);
        if (TextUtils.isEmpty(content)) {
            mTvMessage.setVisibility(View.GONE);
        } else {
            mTvMessage.setVisibility(View.VISIBLE);
            mTvMessage.setText(content);
        }
        return this;
    }


    public BaseAlertDialog setmRightButtonListener(String btnText, onSuccessListener mSuccessListener) {
        mBtnRight.setText(btnText);
        mBtnRight.setVisibility(View.VISIBLE);
        setmRightButtonListener(mSuccessListener);
        return this;
    }

    public BaseAlertDialog setmRightButtonListener(int color, String btnText, onSuccessListener mSuccessListener) {
        mBtnRight.setText(btnText);
        mBtnRight.setVisibility(View.VISIBLE);
        mBtnRight.setTextColor(ContextCompat.getColor(getContext(), color));
        setmRightButtonListener(mSuccessListener);
        return this;
    }

    public BaseAlertDialog setmRightButtonListener(@StringRes int resId, onSuccessListener mSuccessListener) {
        mBtnRight.setText(resId);
        mBtnRight.setVisibility(View.VISIBLE);
        setmRightButtonListener(mSuccessListener);
        return this;
    }

    @Override
    public void show() {
        super.show();
    }

    public BaseAlertDialog setmRightButtonListener(onSuccessListener mSuccessListener) {
        this.mSuccessListener = mSuccessListener;
        return this;
    }

    public BaseAlertDialog setmLeftButtonListener(String btnText, onCancelListener mCancelListener) {
        mBtnLeft.setText(btnText);
        setmLeftButtonListener(mCancelListener);
        return this;
    }


    public BaseAlertDialog setMessageSize(int textSize) {
        mTvMessage.setTextSize(textSize);
        return this;
    }

    public BaseAlertDialog setmLeftButtonListener(int color, String btnTex, onCancelListener mCancelListener) {
        mBtnLeft.setText(btnTex);
        mBtnLeft.setTextColor(ContextCompat.getColor(getContext(), color));
        setmLeftButtonListener(mCancelListener);
        return this;
    }

    public BaseAlertDialog setmLeftButtonListener(@StringRes int resId, onCancelListener mCancelListener) {
        mBtnLeft.setText(resId);
        setmLeftButtonListener(mCancelListener);
        return this;
    }

    public BaseAlertDialog addOtherBtnListener(String otherText, View.OnClickListener clickListener) {
        View view = new View(mContext);
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1));
        view.setBackgroundResource(R.color.color_50000000);
        other_btn.addView(view);
        TextView textView = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DeviceUtils.dipToPX(mContext, 44));
        textView.setText(otherText);
        textView.setTextSize(17);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(mContext.getResources().getColor(R.color.color_0076ff));
        textView.setBackgroundResource(R.color.transparentColor);
        textView.setLayoutParams(params);
        textView.setOnClickListener(clickListener);
        other_btn.addView(textView);
        return this;
    }

    public BaseAlertDialog setmLeftButtonListener(onCancelListener mCancelListener) {
        this.mCancelListener = mCancelListener;
        return this;
    }

    public BaseAlertDialog setRightButtonIsGone() {
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
