package one.show.live.common.util;

/**
 * Created by ydeng on 2017/12/12.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 状态栏view
 */
public class StatusBarView extends View {
    public StatusBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StatusBarView(Context context) {
        super(context);
    }

}