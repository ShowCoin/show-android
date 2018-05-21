package one.show.live.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import one.show.live.R;

public class DialogUtil {
    //自定义弹框
    public static AlertDialog showDialog(Context context, String title, String content, final View.OnClickListener onOKClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.show_dialog_view, null);
       TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvContent = view.findViewById(R.id.tv_content);
        Button tvCancel = view.findViewById(R.id.tv_left);
        Button tvOk = view.findViewById(R.id.tv_right);
        tvTitle.setText(title);
        tvContent.setText(content);
        final AlertDialog alertDialog = builder.setView(view).create();
        alertDialog.show();
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (onOKClickListener != null) {
                    onOKClickListener.onClick(v);
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }
    public static AlertDialog showNoMoneyDialog(Context context, String content, final Intent intent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.remind_dialog, null);

        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_left);
        TextView tvOk = (TextView) view.findViewById(R.id.tv_right);

        tvContent.setText(content);

        final AlertDialog alertDialog = builder.setView(view).create();
        alertDialog.show();

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(intent);
                alertDialog.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }


}
