package one.show.live.common.ui;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;


import com.umeng.analytics.MobclickAgent;

import butterknife.Unbinder;
import butterknife.ButterKnife;


/**
 * Fragment 基类
 */

public abstract class BaseFragment extends Fragment implements BaseView{

    protected View rootView;
    protected LayoutInflater mInflater;
    protected Activity activity;
    protected Unbinder unbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mInflater = inflater;
        hideSoftInput();
        if (rootView == null) {
            rootView = mInflater.inflate(getContentView(), container, false);
            unbinder = ButterKnife.bind(this, rootView);
            initView();
        }
        return rootView;
    }

    protected abstract int getContentView();

    protected void initView() {

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getClass().getName()); //统计页面，"MainScreen"为页面名称，可自定义
    }


//    @Override
//    public void onStart() {
//        if (rootView != null) {
//            rootView.setDrawingCacheEnabled(false);
//            rootView.setOnTouchListener(new OnTouchListener() {
//
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return true;
//                }
//            });
//        }
//        super.onStart();
//    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getClass().getName());
        hideSoftInput();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        if (unbinder != null){
            unbinder.unbind();
            unbinder=null;
        }

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            hideSoftInput();
        }
    }

    protected void hideSoftInput() {
        // hide soft input window after sending.
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            View focusView = activity.getCurrentFocus();
            if (focusView != null && imm != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    public boolean isContextAlive(){
        return activity!=null&&!activity.isFinishing()&&isAdded();
    }

    /**
     * 处理返回事件
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * activity回调当前的Fragment的back事件
     */
    public boolean onActCallBackPressed() {
        return false;
    }
}