package one.show.live.media.ui;

import android.view.View;

import one.show.live.common.po.POLive;
import one.show.live.common.ui.BaseFragment;
import one.show.live.media.listener.PlayEventListener;


public abstract class PlayFragment extends BaseFragment {
    protected static final int RE_CONNECTION = 0x11;
    protected PlayEventListener eventListener;
    protected View.OnClickListener clickListener;
    protected String playURL;
    protected POLive liveBean;

    public void setEventListener(PlayEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void hideFeaturesLayout(boolean isHide){

    }

    public abstract void setRibbonHide(boolean hide);

    public void setPraiseListener(View.OnClickListener listener) {
        this.clickListener = listener;
    }

    /**
     * 网络出错
     */
    public void onNetWorkError() {
        //try {
        //  new UIAlert.Builder(getActivity().getApplicationContext())
        //      .setMessage("网络错误，请检测网络后重新连接")
        //      .setPositiveButton("确定", new DialogInterface.OnClickListener() {
        //        public void onClick(DialogInterface dialog, int whichButton) {
        //          getActivity().finish();
        //        }
        //      })
        //      .setCancelable(false)
        //      .show();
        //} catch (Exception ignored) {
        //
        //}
    }
}
