package one.show.live.ui;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.Fragment;

public abstract class BasePresenter {
    protected Activity mContext;

    protected void initContext(Object targetView) {
        if (targetView instanceof Activity) {
            mContext = (Activity) targetView;
        } else if (targetView instanceof Fragment) {
            mContext = ((Fragment) targetView).getActivity();
        } else if(targetView instanceof Dialog){
            mContext = ((Dialog) targetView).getOwnerActivity();
        }else {
            throw new IllegalArgumentException("view只能是activity或fragment");
        }
    }

    protected boolean isContextAlive(){
        if(mContext != null) {
            if (mContext instanceof BaseView) {
                return ((BaseView)mContext).isContextAlive();
            }
        }
        return false;
    }


    public  void onDestory(){

    }
}
