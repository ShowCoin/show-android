package one.show.live.view.floating;

import android.content.Context;
import android.graphics.drawable.Drawable;

import one.show.live.common.R;


public class FloatsUtil {
    public static Drawable drawable[] = null;

    public FloatsUtil(Context context) {
        if (drawable == null) {
            drawable = new Drawable[4];
//            drawable[0] = context.getResources().getDrawable(R.drawable.icon_floats_1);
//            drawable[1] = context.getResources().getDrawable(R.drawable.icon_floats_2);
//            drawable[2] = context.getResources().getDrawable(R.drawable.icon_floats_3);
//            drawable[3] = context.getResources().getDrawable(R.drawable.icon_floats_4);
        }
    }

    public Drawable getFloats(int i) {
        if (i >= drawable.length || i < 0) {
            return null;
        } else {
            return drawable[i];
        }
    }
}
