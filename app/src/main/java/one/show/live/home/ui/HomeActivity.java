package one.show.live.home.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


import butterknife.BindView;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.wallet.ui.MeFragment;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.view.ToastUtils;

/**
 * Created by liuzehua on 2018/4/2.
 */

public class HomeActivity extends BaseFragmentActivity {

    public int currentId;

    @BindView(R.id.home_one_lay)
    RelativeLayout homeOneLay;
    @BindView(R.id.home_two_lay)
    RelativeLayout homeTwoLay;
    @BindView(R.id.home_three_lay)
    RelativeLayout homeThreeLay;
    @BindView(R.id.home_four_lay)
    RelativeLayout homeFourLay;
    @BindView(R.id.home_five_lay)
    RelativeLayout homeFiveLay;


    @BindView(R.id.home_one_text)
    TextView homeOneText;
    @BindView(R.id.home_two_text)
    TextView homeTwoText;
    @BindView(R.id.home_four_text)
    TextView homeFourText;
    @BindView(R.id.home_five_text)
    TextView homeFiveText;

    @BindView(R.id.home_one_line)
    View homeOneLine;
    @BindView(R.id.home_two_line)
    View homeTwoLine;
    @BindView(R.id.home_four_line)
    View homeFourLine;
    @BindView(R.id.home_five_line)
    View homeFiveLine;


    HomePageIndexFragment homeFragment;
    MeFragment focusFragment;
    MeFragment messageFragment;
    MeFragment meFragment;

    TextView[] textViews;
    View[] views;

    boolean mHasDoubleClicked = false;
    private static final long DOUBLE_PRESS_INTERVAL = 250;
    private long lastPressTime;

    @Override
    protected int getContentView() {
        return R.layout.activity_home;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentId = savedInstanceState.getInt("currentId", R.id.home_one_lay);
            homeFragment = (HomePageIndexFragment) getSupportFragmentManager().findFragmentByTag("" + R.id.home_one_lay);
            focusFragment = (MeFragment) getSupportFragmentManager().findFragmentByTag("" + R.id.home_two_lay);
            messageFragment = (MeFragment) getSupportFragmentManager().findFragmentByTag("" + R.id.home_four_lay);
            meFragment = (MeFragment) getSupportFragmentManager().findFragmentByTag("" + R.id.home_five_lay);
            updateTab(currentId);
        } else {
            onSingleClick(homeOneLay);
        }

    }

    @Override
    protected void initBaseView() {
        super.initBaseView();
        textViews = new TextView[]{homeOneText, homeTwoText, homeFourText, homeFiveText};
        views = new View[]{homeOneLine, homeTwoLine, homeFourLine, homeFiveLine};
    }

    /**
     * 按钮的点击事件
     *
     * @param v
     */
    @OnClick({R.id.home_one_lay, R.id.home_two_lay, R.id.home_three_lay, R.id.home_four_lay, R.id.home_five_lay})
    public void clickTab(final View v) {

        doubleOrSingleClick(v);

    }

    private void doubleOrSingleClick(final View v) {
        long pressTime = System.currentTimeMillis();
        if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
            mHasDoubleClicked = true;
            v.setTag(100);
            onSingleClick(v);
        } else {
            mHasDoubleClicked = false;
            Handler myHandler = new Handler() {
                public void handleMessage(Message m) {
                    if (!mHasDoubleClicked) {
                        v.setTag(101);
                        onSingleClick(v);
                    }
                }
            };
            Message m = new Message();
            myHandler.sendMessageDelayed(m, DOUBLE_PRESS_INTERVAL);
        }
        lastPressTime = pressTime;
    }

    /**
     * 设置按钮的状态
     *
     * @param id
     */
    private void updateTab(int id) {

        for (int i = 0; i < textViews.length; i++) {
            textViews[i].setTextColor(ContextCompat.getColor(HomeActivity.this, R.color.color_90ffffff));
            textViews[i].setTextSize(12);
            TextPaint tp = textViews[i].getPaint();
            tp.setFakeBoldText(false);
            views[i].setBackgroundResource(R.color.transparent);
        }

        switch (id) {
            case R.id.home_one_lay:
                setButtonStyle(homeOneText, homeOneLine);
                break;
            case R.id.home_two_lay:
                setButtonStyle(homeTwoText, homeTwoLine);
                break;
            case R.id.home_four_lay:
                setButtonStyle(homeFourText, homeFourLine);
                break;
            case R.id.home_five_lay:
                setButtonStyle(homeFiveText, homeFiveLine);
                break;
        }
    }

    /**
     * 设置文字颜色大小等
     *
     * @param textView
     * @param view
     */
    private void setButtonStyle(TextView textView, View view) {
        textView.setTextColor(ContextCompat.getColor(HomeActivity.this, R.color.color_ffffff));
        textView.setTextSize(15);
        TextPaint tp5 = textView.getPaint();
        tp5.setFakeBoldText(true);
        view.setBackgroundResource(R.color.color_ffffff);
    }

    /**
     * 选中按钮的处理
     *
     * @param v
     */
    public void onSingleClick(View v) {
        final int id = v.getId();
        switch (id) {
            case R.id.home_one_lay:
                changePage(id, homeFragment);
                updateTab(id);
                break;
            case R.id.home_two_lay:
                changePage(id, focusFragment);
                updateTab(id);
                break;
            case R.id.home_three_lay:
                ToastUtils.showToast("拍摄");
                break;
            case R.id.home_four_lay:
                changePage(id, messageFragment);
                updateTab(id);
                break;
            case R.id.home_five_lay:
                changePage(id, meFragment);
                updateTab(id);
                break;
        }
    }

    private void changePage(int id, Fragment fragment) {
        hideFragment(currentId);

        if (fragment == null) {
            fragment = initFragment(id);
            addFragment(fragment, id);
        } else {
            showFragment(fragment);
        }
        currentId = id;
    }

    /**
     * 隐藏fragment
     *
     * @param id
     */
    private void hideFragment(int id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("" + id);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (fragment != null) {
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    /**
     * 显示fragment
     *
     * @param fragment
     */
    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.show(fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 添加fragment
     *
     * @param fragment
     * @param id
     */
    private void addFragment(Fragment fragment, long id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.home_frame, fragment, "" + id);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private Fragment initFragment(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.home_one_lay:
                fragment = homeFragment = HomePageIndexFragment.newInstance();
                break;
            case R.id.home_two_lay:
                fragment = focusFragment = meFragment.newInstance();
                break;
            case R.id.home_four_lay:
                fragment = messageFragment = meFragment.newInstance();
                break;
            case R.id.home_five_lay:
                fragment = meFragment = meFragment.newInstance();
                break;
        }
        return fragment;
    }

}
