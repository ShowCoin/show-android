package one.show.live.view;

import android.content.Context;
import android.content.DialogInterface;

public class UIAlert extends android.support.v7.app.AlertDialog {
    public UIAlert(Context context) {
        super(context);
    }

    protected UIAlert(Context context, int theme) {
        super(context, theme);
    }

    protected UIAlert(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
