package one.show.live.home.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.po.POLogin;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BaseFragment;
import one.show.live.home.presenter.HomePresenter;
import one.show.live.home.view.HomeView;
import one.show.live.media.ui.PublisherActivity;
import one.show.live.personal.presenter.UserPresenter;
import one.show.live.personal.view.UserView;
import one.show.live.util.ConvertToUtils;
import one.show.live.wallet.ui.MeWalletFragment;

import static one.show.live.R2.id.home_one_lay;


/**
 * Created by Nano on 2018/4/2.
 */

public class HomeFragment extends BaseFragment implements OnGetAnimatorWhenOpenLiveMenu, LiveStatusChangedImpl,OnLockClickListener {

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

    @BindView(R.id.btn_to_publish)
    View btnToPublish;


    HomePageIndexFragment homeIndexFragment;
    MeWalletFragment focusFragment;
    MeWalletFragment messageFragment;
    MeFragment meFragment;

    TextView[] textViews;
    View[] views;

    private boolean mHasDoubleClicked = false;
    private static final long DOUBLE_PRESS_INTERVAL = 250;
    private long lastPressTime;

    private boolean isOpenLiveMenu;

    private boolean isLockClick;


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    protected int getContentView() {
        return R.layout.fragment_home;
    }


    protected void initView(Bundle savedInstanceState) {

        textViews = new TextView[]{homeOneText, homeTwoText, homeFourText, homeFiveText};
        views = new View[]{homeOneLine, homeTwoLine, homeFourLine, homeFiveLine};

        if (savedInstanceState != null) {
            currentId = savedInstanceState.getInt("currentId", R.id.home_one_lay);
            homeIndexFragment = (HomePageIndexFragment) getChildFragmentManager().findFragmentByTag("" + R.id.home_one_lay);
            focusFragment = (MeWalletFragment) getChildFragmentManager().findFragmentByTag("" + R.id.home_two_lay);
            messageFragment = (MeWalletFragment) getChildFragmentManager().findFragmentByTag("" + R.id.home_four_lay);
            meFragment = (MeFragment) getChildFragmentManager().findFragmentByTag("" + R.id.home_five_lay);
            updateTab(currentId);
        } else {
            onSingleClick(homeOneLay);
        }
    }

    /**
     * 按钮的点击事件
     *
     * @param v
     */
    @OnClick({R.id.home_one_lay, R.id.home_two_lay, R.id.btn_to_publish, R.id.home_four_lay, R.id.home_five_lay})
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
            textViews[i].setTextColor(ContextCompat.getColor(getActivity(), R.color.color_90ffffff));
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
        textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.color_ffffff));
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

                if(currentId!=id){//就在广场页面的时候，点击不处理事件
                    changePage(id, homeIndexFragment);
                    updateTab(id);
                }
                break;
            case R.id.home_two_lay:
                changePage(id, focusFragment);
                updateTab(id);
                break;
            case R.id.btn_to_publish:
                startActivity(PublisherActivity.getCallingIntent(getActivity()));
                break;
            case R.id.home_four_lay:
                changePage(id, messageFragment);
                updateTab(id);
                break;
            case R.id.home_five_lay:
                if(meFragment!=null){
                    meFragment.freshData();
                }
                if(currentId!=id){
                    changePage(id, meFragment);
                    updateTab(id);
                }
                break;
        }
    }

    private void changePage(int id, Fragment fragment) {

        if (id != home_one_lay) {
            pauseLive();
            ((OnChangedHomePageSwipeListener) getActivity()).canSwipeChanged(false, false);
        } else {
            resumeLive();
            if (homeIndexFragment == null) {
                ((OnChangedHomePageSwipeListener) getActivity()).canSwipeChanged(true, true);
            } else {
                if (homeIndexFragment.getIsShowLiveList()) {
                    ((OnChangedHomePageSwipeListener) getActivity()).canSwipeChanged(true, true);
                } else {
                    ((OnChangedHomePageSwipeListener) getActivity()).canSwipeChanged(true, false);
                }
            }
        }

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
        FragmentManager fragmentManager = getChildFragmentManager();
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
        FragmentManager fragmentManager = getChildFragmentManager();
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
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.home_frame, fragment, "" + id);
        fragmentTransaction.commitAllowingStateLoss();
    }

    private Fragment initFragment(int id) {
        Fragment fragment = null;
        switch (id) {
            case R.id.home_one_lay:
                fragment = homeIndexFragment = HomePageIndexFragment.newInstance();
                break;
            case R.id.home_two_lay:
                fragment = focusFragment = focusFragment.newInstance();
                break;
            case R.id.home_four_lay:
                fragment = messageFragment = messageFragment.newInstance();
                break;
            case R.id.home_five_lay:
                fragment = meFragment = meFragment.newInstance(POLogin.getInstance().getUid(), MeFragment.HOME);//加载我自己的个人中心页面
                break;
        }
        return fragment;
    }

    @Override
    public Animator getOpenLiveMenuAnim(boolean isOpenLiveMenu) {//这里执行的是广场等文字的动画

        final float dp45 = ConvertToUtils.dipToPX(getActivity(), 45);
        this.isOpenLiveMenu = isOpenLiveMenu;

        if (isOpenLiveMenu) {//隐藏

            PropertyValuesHolder publishBtnScale = PropertyValuesHolder.ofFloat(View.ROTATION_Y, 0, 90);
//            PropertyValuesHolder publishBtnAlpha = PropertyValuesHolder.ofFloat(View.ALPHA,  1, 0 );

            ObjectAnimator publishAnim = ObjectAnimator.ofPropertyValuesHolder(btnToPublish, publishBtnScale);
            publishAnim.setDuration(150);


            ObjectAnimator homeMenuAnim = ObjectAnimator.ofFloat(homeOneLay, View.TRANSLATION_Y, 0f, dp45);
            homeMenuAnim.setDuration(50);

            ObjectAnimator meMenuAnim = ObjectAnimator.ofFloat(homeFiveLay, View.TRANSLATION_Y, 0f, dp45);
            meMenuAnim.setDuration(50);


            meMenuAnim.setupEndValues();

            AnimatorSet anim = new AnimatorSet();
            anim.playTogether(publishAnim, homeMenuAnim, meMenuAnim);
            anim.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationCancel(Animator animation) {
                    if (animation.isRunning()) {
                        if (btnToPublish.getRotationY() != 90) {
                            btnToPublish.setRotationY(90);
                            homeOneLay.setTranslationY(dp45);
                            homeFiveLay.setTranslationY(dp45);
                        }
                    }
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    btnToPublish.setVisibility(View.GONE);//执行完成动画将按钮隐藏
                    homeOneLay.setVisibility(View.GONE);//执行完成动画将按钮隐藏
                    homeFiveLay.setVisibility(View.GONE);//执行完成动画将按钮隐藏
                }
            });

            return anim;

        } else {//显示
            PropertyValuesHolder publishBtnScale = PropertyValuesHolder.ofFloat(View.ROTATION_Y, 90, 0);
//            PropertyValuesHolder publishBtnAlpha = PropertyValuesHolder.ofFloat(View.ALPHA,  0, 1 );

            ObjectAnimator publishAnim = ObjectAnimator.ofPropertyValuesHolder(btnToPublish, publishBtnScale);
            publishAnim.setDuration(150);
            publishAnim.setStartDelay(150);


            ObjectAnimator homeMenuAnim = ObjectAnimator.ofFloat(homeOneLay, View.TRANSLATION_Y, dp45, 0f);
            homeMenuAnim.setDuration(150);
            homeMenuAnim.setStartDelay(150);

            ObjectAnimator meMenuAnim = ObjectAnimator.ofFloat(homeFiveLay, View.TRANSLATION_Y, dp45, 0f);
            meMenuAnim.setDuration(150);
            meMenuAnim.setStartDelay(150);

            AnimatorSet anim = new AnimatorSet();
            anim.playTogether(publishAnim, homeMenuAnim, meMenuAnim);

            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    if (animation.isRunning()) {
                        if (btnToPublish.getRotationY() != 0) {
                            btnToPublish.setRotationY(0);
                            homeOneLay.setTranslationY(0);
                            homeFiveLay.setTranslationY(0);
                        }
                    }
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    btnToPublish.setVisibility(View.VISIBLE);//执行动画前先把开播按钮显示
                    homeOneLay.setVisibility(View.VISIBLE);//执行动画前先把开播按钮显示
                    homeFiveLay.setVisibility(View.VISIBLE);//执行动画前先把开播按钮显示
                }
            });
            return anim;
        }

    }

    @Override
    public void onLockClick(boolean isLock) {
        isLockClick = isLock;
        if(getParentFragment() instanceof OnLockClickListener){
            ((OnLockClickListener) getParentFragment()).onLockClick(isLock);
        }
    }

    @Override
    public void pauseLive() {
        if (homeIndexFragment != null&&homeIndexFragment.getIsShowLiveList()) {
            homeIndexFragment.pauseLive();
        }
    }

    @Override
    public void resumeLive() {
        if (homeIndexFragment != null&&homeIndexFragment.getIsShowLiveList()) {
            homeIndexFragment.resumeLive();
        }
    }


}
