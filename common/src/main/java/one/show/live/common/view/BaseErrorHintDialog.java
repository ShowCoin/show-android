package one.show.live.common.view;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import one.show.live.common.util.DeviceUtils;
import one.show.live.common.common.R;


/**
 * Created by ydeng on 18/4/8.
 */
public class BaseErrorHintDialog extends Dialog implements View.OnClickListener {


    private Context mContext;

    TextView mTVContent;
    TextView mTVOk;
    TextView mTVCancel;
    TextView mTVTitle;
    ImageView mIVClose;


    onSuccessListener mSuccessListener;
    onCancelListener mCancelListener;

//    private String title;
//    private String message;

    public BaseErrorHintDialog(Context context) {
        this(context, R.layout.error_hint_dialog, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
    }

    private BaseErrorHintDialog(Context context, int layoutResID, int width, int height) {
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
        mTVContent = (TextView) findViewById(R.id.tv_content);
        mTVTitle = (TextView) findViewById(R.id.tv_title);
        mTVOk = (TextView) findViewById(R.id.tv_ok);
        mTVOk.setOnClickListener(this);
        mTVCancel = (TextView) findViewById(R.id.tv_cancel);
        mTVCancel.setOnClickListener(this);
        mIVClose = (ImageView) findViewById(R.id.iv_close);
        mIVClose.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.tv_ok) {
            dismiss();
            if (mSuccessListener != null) {
                mSuccessListener.onSuccess(this);
            }
        } else if (v.getId() == R.id.tv_cancel) {
            dismiss();
            if (mCancelListener != null) {
                mCancelListener.onCancel(this);
            }

        } else if (v.getId() == R.id.iv_close) {
            dismiss();
        }
    }


    public BaseErrorHintDialog setContent(String content) {
        if (TextUtils.isEmpty(content)) {
            mTVContent.setVisibility(View.GONE);
        } else {
            mTVContent.setVisibility(View.VISIBLE);
            mTVContent.setText(content);
        }
        return this;
    }

    public BaseErrorHintDialog setTitle(String title) {
        if (TextUtils.isEmpty(title)) {
            mTVTitle.setVisibility(View.GONE);
        } else {
            mTVTitle.setVisibility(View.VISIBLE);
            mTVTitle.setText(title);
        }
        return this;
    }

    public BaseErrorHintDialog setVisiCancle() {
            mTVCancel.setVisibility(View.GONE);
        return this;
    }

    @Override
    public void show() {
        super.show();
    }

    public BaseErrorHintDialog setSuccessButtonListener(onSuccessListener mSuccessListener) {
        this.mSuccessListener = mSuccessListener;
        return this;
    }

    public BaseErrorHintDialog setCancelButtonListener(onCancelListener mCancelListener) {
        mTVCancel.setVisibility(View.VISIBLE);
        this.mCancelListener = mCancelListener;
        return this;
    }


    public interface onSuccessListener {
        void onSuccess(Dialog dialog);
    }

    public interface onCancelListener {
        void onCancel(Dialog dialog);
    }
}
