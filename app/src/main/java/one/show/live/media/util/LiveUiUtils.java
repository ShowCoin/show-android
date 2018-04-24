package one.show.live.media.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import one.show.live.R;
import one.show.live.common.po.POMember;
import one.show.live.common.view.CustomDialogView;
import one.show.live.money.ui.MyCoinsActivity;

/**
 * Created by apple on 16/6/7.
 */
public class LiveUiUtils {

    public static void showBuyGoldDialog(final Context context) {
        CustomDialogView dialog = new CustomDialogView.Builder(context).setTitle(R.string.gold_not_enough_title)
                .setMessage(R.string.gold_not_enough_msg).setLeftButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setRightButton(R.string.gold_not_enough_go_buy_btn, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        jumpToPayActivity(context);
                        dialog.dismiss();

                    }
                }).create();
        dialog.show();
    }


    /**
     * 充值
     */
    public static void jumpToPayActivity(Context context) {
//        Intent intent = new Intent();
//        intent.setAction(payActivityAction);
//        mContext.startActivity(intent);
//        this.setVisibility(View.GONE);
          context.startActivity(MyCoinsActivity.getCallingIntent(context));
    }

}
