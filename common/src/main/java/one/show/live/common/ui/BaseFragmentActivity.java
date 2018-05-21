package one.show.live.common.ui;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.bugtags.library.Bugtags;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import one.show.live.common.global.presenter.ActionPresenter;
import one.show.live.common.po.POEventBus;
import one.show.live.common.po.POLaunch;
import one.show.live.common.util.Constants;
import one.show.live.common.util.DeviceUtils;
import one.show.live.common.util.WifiMacAddress;
import one.show.live.common.view.BaseAlertDialog;
import one.show.live.common.view.BaseErrorHintDialog;
import one.show.live.common.view.BaseGuideDialog;
import one.show.live.common.view.CustomProgressDialog;

/**
 * Activity基类
 */
public abstract class BaseFragmentActivity extends CustomFragmentActivity implements BaseView {
    protected Activity mActivity;

    public ActionPresenter actionPresenter;

    private BaseAlertDialog dialog;
    private BaseGuideDialog guideDialog;
    private BaseErrorHintDialog errorHintDialog;

    private CustomProgressDialog progressDialog;
    protected WeakReference<BaseFragment> mCurFrag;

//    @Override
//    protected void attachBaseContext(Context newBase) {
//        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        setContentView(getContentView());
        super.onCreate(savedInstanceState);
        mActivity = this;
        ButterKnife.bind(this);
        actionPresenter = new ActionPresenter();
        initBaseView();
    }


    protected abstract int getContentView();


    protected void initBaseView() {

    }


    /**
     * 动态的设置状态栏  实现沉浸式状态栏
     *
     * @param view  占位置的view
     * @param color 颜色，
     */
    public void initState(View view, @ColorRes int color) {

        //当系统版本为4.4或者4.4以上时可以使用沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            //透明导航栏
//           getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            view.setVisibility(View.VISIBLE);
            //获取到状态栏的高度
            int statusHeight = DeviceUtils.getStatusBarHeight(BaseFragmentActivity.this);
            //动态的设置隐藏布局的高度
            ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) view.getLayoutParams();
            params.height = statusHeight;
            view.setLayoutParams(params);
            view.setBackgroundResource(color);
        }
    }


    public BaseAlertDialog getAlertDialog() {
        if (dialog == null) {
            dialog = new BaseAlertDialog(this);
        } else {
            dialog.dismiss();
        }
        dialog.clearOtherBtn();
        dialog.setMessage("");
        dialog.setTitle("");
        return dialog;
    }

    public BaseGuideDialog getGuideDialog(int mode) {
        if (guideDialog == null) {
            guideDialog = new BaseGuideDialog(this);
        } else {
            guideDialog.dismiss();
        }
        guideDialog.setMode(mode);
        return guideDialog;
    }

    public BaseErrorHintDialog getErrorHintDialog() {
        if (errorHintDialog == null) {
            errorHintDialog = new BaseErrorHintDialog(this);
        } else {
            errorHintDialog.setVisiCancle();
            errorHintDialog.dismiss();
        }
        return errorHintDialog;
    }

    public CustomProgressDialog getNewProgressDialog(String text) {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        progressDialog = new CustomProgressDialog.Builder(this).setMessage(text).create();
        return progressDialog;
    }

    public CustomProgressDialog getCurrentProgressDialog() {
        if (progressDialog != null) {
            return progressDialog;
        }
        return getNewProgressDialog("请稍等...");
    }


    @Override
    protected void onResume() {
        super.onResume();
        startApp();

        Bugtags.onResume(this);

    }

    public void startApp() {
        if (POLaunch.startTime == null && !TextUtils.isEmpty(POLaunch.url)) {
            Map<String, String> maps = new HashMap<>();
            actionPresenter.actionUp(maps, POLaunch.launch);//启动APP的打点统计
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            POLaunch.startTime = date;
        }
    }

    protected void onPause() {
        super.onPause();
        Bugtags.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (WifiMacAddress.isBackground(this)) {//判断当前在前台还是后台
            exitApp();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        //注：回调 3
        Bugtags.onDispatchTouchEvent(this, event);
        return super.dispatchTouchEvent(event);
    }


    @Override
    protected void onDestroy() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
        if (errorHintDialog != null) {
            errorHintDialog.dismiss();
            errorHintDialog = null;
        }
        if (guideDialog != null) {
            guideDialog.dismiss();
            guideDialog = null;
        }
        super.onDestroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(POEventBus event) {
        if (event.getId() == Constants.EVENT_BUS_INDEX_TABLE) {
            finish();
        }
    }

    /**
     * @param tag            frag tag 用于标记查找
     * @param modelFragment  class
     * @param layoutId       Identifier of the container whose fragment(s) are to be replaced.
     * @param data           给页面的数据
     * @param addToBackStack 是否加入回退栈
     * @return
     */
    public BaseFragment showFragment(String tag, Class<? extends BaseFragment> modelFragment, int layoutId, Bundle data, boolean addToBackStack) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        Fragment oldFrag = fm.findFragmentByTag(tag);
        if (oldFrag != null) {
            fm.beginTransaction().remove(oldFrag).commitAllowingStateLoss();
            if (addToBackStack) {
                fm.popBackStackImmediate();
            }
        }

        FragmentTransaction ft = fm.beginTransaction();
        BaseFragment newFrag = null;
        try {
            newFrag = modelFragment.newInstance();
        } catch (Exception e) {
            e.printStackTrace();

        }
        if (newFrag == null) {
            return null;
        }
        if (data != null) {
            newFrag.setArguments(data);
        }
        ft.replace(layoutId, newFrag, tag);

        if (addToBackStack) {
            ft.addToBackStack(null);
        }

        ft.commitAllowingStateLoss();
        mCurFrag = new WeakReference<BaseFragment>(newFrag);
        return newFrag;
    }

    @Override
    public void onBackPressed() {
        boolean notFollow = false;
        if (mCurFrag != null) {
            BaseFragment fragment = mCurFrag.get();
            if (fragment != null) {
                notFollow = fragment.onActCallBackPressed();
            }
        }
        if (!notFollow) {
            try {
                super.onBackPressed();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isContextAlive() {
        return mActivity != null && !mActivity.isFinishing();
    }

    public void exitApp() {//退出app  或者进入后台给服务器一个通知
        if (POLaunch.startTime != null) {
            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            long diff = 0;
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date d1 = df.parse(date);
                Date d2 = df.parse(POLaunch.startTime);
                diff = d1.getTime() - d2.getTime();//这样得到的差值是微秒级别
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Map<String, String> maps = new HashMap<>();
            maps.put(POLaunch.time, String.valueOf(diff / 1000));
            actionPresenter.actionUp(maps, POLaunch.exit);
            POLaunch.startTime = null;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus()) {
            /**
             * 点击空白位置 隐藏软键盘
             */
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    /**
     * AudioManager 内存泄露解决方法
     *
     * @param newBase
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(new ContextWrapper(newBase) {
            @Override
            public Object getSystemService(String name) {
                if (Context.AUDIO_SERVICE.equals(name))
                    return getApplicationContext().getSystemService(name);
                return super.getSystemService(name);
            }
        });
    }
}
