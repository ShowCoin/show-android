package one.show.live.util;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * Created by wanglushan on 2017/2/27.
 */

public class ContextUtils {

    public static boolean isContextAlive(Activity activity){

            return activity!=null&&!activity.isFinishing();

    }


    public static boolean isContextAlive(Dialog dialog){

        return dialog!=null&&dialog.isShowing();

    }


    public static boolean isContextAlive(Fragment fragment){
        return fragment!=null&&fragment.getActivity()!=null&&!fragment.getActivity().isFinishing()&&fragment.isAdded();
    }


    public static boolean isContextAlive(View view){
        return view!=null&&view.getContext()!=null&&!((Activity)view.getContext()).isFinishing();
    }

}
