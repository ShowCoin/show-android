package one.show.live.common.view;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;

import one.show.live.common.ui.BaseApplication;
import one.show.live.common.common.R;

public class ToastUtils {

    public static void showToast(int resID) {
        showToast(BaseApplication.getContext(), Toast.LENGTH_SHORT, resID);
    }

    public static void showToast(String text) {
        showToast(BaseApplication.getContext(), Toast.LENGTH_SHORT, text, Gravity.TOP, false);
//        showToast(BaseApplication.getContext(), Toast.LENGTH_SHORT, text);
    }

    public static void showToast(String text, boolean isSystem) {
        showToast(BaseApplication.getContext(), Toast.LENGTH_SHORT, text, Gravity.TOP, isSystem);
//        showToast(BaseApplication.getContext(), Toast.LENGTH_SHORT, text);
    }

    public static void showToast(Context ctx, int resID) {
        showToast(ctx, Toast.LENGTH_SHORT, resID);
    }

    public static void showToast(Context ctx, String text) {
        showToast(ctx, Toast.LENGTH_SHORT, text, Gravity.TOP, false);
    }

    public static void showLongToast(Context ctx, int resID) {
        showToast(ctx, Toast.LENGTH_LONG, resID);
    }

    public static void showLongToast(int resID) {
        showToast(BaseApplication.getContext(), Toast.LENGTH_LONG, resID);
    }

    public static void showToast(Context ctx, int duration, int resID) {
        showToast(ctx, duration, ctx.getString(resID), Gravity.TOP, false);
    }


    /**
     * Toast一个图片
     */
    public static Toast showToastImage(Context ctx, int resID) {
        final Toast toast = Toast.makeText(ctx, "", Toast.LENGTH_SHORT);
        View mNextView = toast.getView();
        if (mNextView != null)
            mNextView.setBackgroundResource(resID);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return toast;
    }

    /**
     * Toast一个图片,拍摄界面使用,在预览画面居中.,需要传一个top
     */
    public static Toast showToastImage(Context ctx, int resID, int top) {
        final Toast toast = Toast.makeText(ctx, "", Toast.LENGTH_SHORT);
        View mNextView = toast.getView();
        if (mNextView != null)
            mNextView.setBackgroundResource(resID);
        toast.setGravity(Gravity.TOP, 0, top);
        toast.show();
        return toast;
    }

//
//    public static void showToast(final Context ctx, final int duration, final String text) {
//
//        if (mToast != null) {
//            mToast.cancel();
//        }
//
//        mToast = Toast.makeText(ctx, text, duration);
//
//        //		View view = RelativeLayout.inflate(ctx, R.layout.toast_layout, null);
//        //		TextView mNextView = (TextView) view.findViewById(R.id.toast_name);
//        //		toast.setView(view);
//        //		if (mNextView != null)
//        //			mNextView.setBackgroundResource(R.drawable.toast_tip_bg);
//        //		if (mNextView instanceof TextView)
//        //			((TextView) mNextView).setTextColor(ctx.getResources().getColor(R.color.white));
//        //		mNextView.setText(text);
//        mToast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.TOP, 0, 0);
//        mToast.show();
//    }


    /**
     * 4.0的新版toast
     *
     * @param ctx
     * @param duration
     * @param text
     * @param gravity
     */
    public static void showToast(final Context ctx, final int duration, final String text, int gravity, boolean isSystem) {

        if (mToast != null) {
            View view = mToast.getView();
            TextView textView = (TextView) view.findViewById(R.id.tv_toast_content);
//            mToast.cancel();
            textView.setText(text);
            mToast.setDuration(duration);
        } else {
            mToast = new Toast(ctx);
            View view = RelativeLayout.inflate(ctx, R.layout.view_toast, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_toast_content);
            textView.setText(text);
            mToast.setView(view);
            mToast.setDuration(duration);
            if (isSystem) {
                view.setBackgroundResource(R.drawable.shape_color_4088ff_to_64c2f8);
            } else {
                view.setBackgroundResource(R.color.color_85413e4f);
            }
            //		if (mNextView != null)
            //			mNextView.setBackgroundResource(R.drawable.toast_tip_bg);
            //		if (mNextView instanceof TextView)
            //			((TextView) mNextView).setTextColor(ctx.getResources().getColor(R.color.white));
            //		mNextView.setText(text);
            try {
                Field mTNField = mToast.getClass().getDeclaredField("mTN");
                mTNField.setAccessible(true);
                Object mTNObject = mTNField.get(mToast);
                Class tnClass = mTNObject.getClass();
                Field paramsField = tnClass.getDeclaredField("mParams");
                /**由于WindowManager.LayoutParams mParams的权限是private*/
                paramsField.setAccessible(true);
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) paramsField.get(mTNObject);
                layoutParams.windowAnimations = R.style.MyToast;
                layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            mToast.setGravity(gravity, 0, 0);
        }


//        mToast.setGravity(gravity, 0, DeviceUtils.getScreenHeight(ctx) / 4);

        mToast.show();
    }


    private static Toast mToast;


    /**
     * 在UI线程运行弹出
     */
    public static void showToastOnUiThread(final Activity ctx, final String text) {
        if (ctx != null) {
            ctx.runOnUiThread(new Runnable() {
                public void run() {
                    showToast(ctx, text);
                }
            });
        }
    }

    /**
     * new method
     *
     * @param act
     * @param msg
     */
    public static void showMessage(Context act, final String msg) {
        try {
            showMessage(BaseApplication.getContext(), msg, Toast.LENGTH_SHORT);
        } catch (Exception e) {

        }

    }

    /**
     * new method
     *
     * @param act
     * @param msg
     */
    public static void showMessage(Context act, final int msg) {
        try {
            showMessage(BaseApplication.getContext(), msg, Toast.LENGTH_SHORT);
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

    private static Toast showToast;

    public static void showMessage(final Context context, final String msg, final int len) {
        if (showToast != null) {
            showToast.cancel();
        }
        //							View mView = RelativeLayout.inflate(context, R.layout.toast_layout, null);
        //							((TextView) mView.findViewById(R.id.toast_name)).setText(msg);
        showToast = Toast.makeText(context, msg, len);
        //							showToast.setView(mView);
        showToast.show();
    }

    public static void showMessage(final Context context, final int msg, final int len) {
        if (showToast != null) {
            showToast.cancel();
        }
        //							View mView = RelativeLayout.inflate(context, R.layout.toast_layout, null);
        //							((TextView) mView.findViewById(R.id.toast_name)).setText(msg);
        showToast = Toast.makeText(context, msg, len);
        //							showToast.setView(mView);
        showToast.show();
    }

    public static void showNetError() {
        showToast("网络异常", true);
    }

}
