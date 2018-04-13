package one.show.live.common.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import one.show.live.common.util.StringUtils;
import one.show.live.common.common.R;

public class CustomProgressDialog extends Dialog {


    private Context mContext;
    private boolean isShow;

    public static class Builder {

        private Context context;
        private String message;//信息

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }


        public CustomProgressDialog create() {
            final CustomProgressDialog dialog = new CustomProgressDialog(context, R.style.MyDialogStyle);
            dialog.mContext = context;

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View layout = inflater.inflate(R.layout.custom_progressdialog, null);

            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));


            if (message != null && StringUtils.isNotEmpty(message)) {
                ((TextView) layout.findViewById(R.id.dialog_message)).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.dialog_message)).setText(message);
            } else {
                ((TextView) layout.findViewById(R.id.dialog_message)).setVisibility(View.GONE);
            }
            dialog.setContentView(layout);
            return dialog;
        }

        }


    public CustomProgressDialog showCustomDialog() {
        if (isContextAlive()) {
            super.show();
            isShow = true;
        }else{
            isShow = false;
            mContext = null;
        }
        return this;
    }


    public void dismissCustomDialog() {
        if (isShow) {
//            AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
//
//                @Override
//                protected Void doInBackground(Void... params) {
//                    SystemClock.sleep(500);
//                    return null;
//                }
//
//                @Override
//                protected void onPostExecute(Void result) {
//                    super.onPostExecute(result);
//                    if (isContextAlive()){
//                        dismiss();
//                    }
//                    isShow = false;
//                    mContext = null;
//                }
//            };
//            task.execute();
            if (isContextAlive()){
                dismiss();
            }
        }
    }


    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomProgressDialog(Context context) {
        super(context);
    }


    private boolean isContextAlive(){
       return mContext!=null&&mContext instanceof Activity&&!((Activity) mContext).isFinishing();
    }

}
