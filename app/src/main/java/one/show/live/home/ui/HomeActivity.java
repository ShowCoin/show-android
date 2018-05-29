package one.show.live.home.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;


import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import one.show.live.MeetApplication;
import one.show.live.R;
import one.show.live.common.po.POEventBus;
import one.show.live.common.po.POLogin;
import one.show.live.common.po.POMember;
import one.show.live.common.util.Constants;
import one.show.live.common.view.ToastUtils;
//import one.show.live.live.ui.ReadyLiveActivity;
import one.show.live.home.presenter.HomePresenter;
import one.show.live.home.view.HomeView;
import one.show.live.log.Logger;
import one.show.live.media.ui.PublisherFragment;
import one.show.live.personal.presenter.UserPresenter;
import one.show.live.personal.view.UserView;
import one.show.live.po.POFocus;
import one.show.live.po.eventbus.SeekBarEvent;
import one.show.live.util.SystemUtils;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.widget.FragmentPagerAdapter;
import one.show.live.widget.MyHomeViewPager;
import one.show.live.widget.ViewPager;

/**
 * Created by Nano on 2018/4/2.
 */

public class HomeActivity extends BaseFragmentActivity implements OnChangedHomePageSwipeListener, OnChangedLiveListener, OnOpenUserInfoListener, UserView, HomeView {


    @BindView(R.id.viewpager)
    MyHomeViewPager viewPager;

    PowerManager powerManager = null;
    PowerManager.WakeLock wakeLock = null;

    private boolean mNeedRefreshPersonList;

    private HomeFragment mHomeFragment;

    private PublisherFragment mPublisherFragment;

    private MeFragment mNewPersonalFragment;

    private int mPosition;

    UserPresenter userPresenter;

    private HomePresenter homePresenter;

    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_home);
        initTransparentWindow();
        powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        wakeLock.setReferenceCounted(false);
        userPresenter = new UserPresenter(this);
        userPresenter.getData(POLogin.getInstance().getUid());
        homePresenter = new HomePresenter(this);
        initBaseView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        homePresenter.connectIM(this);
    }

    private void initBaseView() {

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return PublisherFragment.newInstance(null);
                } else if (position == 1) {
                    return HomeFragment.newInstance();
                } else if (position == 2) {
                    return MeFragment.newInstance(POMember.getInstance(), MeFragment.OTHER);
                }
                return null;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
                switch (position) {
                    case 0:
                        mPublisherFragment = (PublisherFragment) createdFragment;
                        break;
                    case 1:
                        mHomeFragment = (HomeFragment) createdFragment;
                        break;
                    case 2:
                        mNewPersonalFragment = (MeFragment) createdFragment;
                        break;
                }
                return createdFragment;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Logger.e("sssss", "position:" + position + "||positionOffset:" + positionOffset + "||positionOffsetPixels:" + positionOffsetPixels);
                if (mPosition == 0) {
                    hideSoftInput();
                }

                if (mNeedRefreshPersonList) {
                    mNeedRefreshPersonList = false;
                    mNewPersonalFragment.freshRecycle();
                }
            }

            @Override
            public void onPageSelected(int position) {
                mPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setOffscreenPageLimit(3);
        viewPager.setCurrentItem(1);
        mPosition = 1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        wakeLock.release();
    }

    @Override
    public void onBackPressed() {
        exitHandler();
    }

    protected boolean isBack;

    private Handler exitHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isBack = false;
        }
    };

    private boolean mCanSwipe;
    private boolean mCanRightSwipe;

    float mLastMotionX;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (mCanSwipe) {
            if (!mCanRightSwipe && mPosition == 1) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mLastMotionX = ev.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (ev.getX() - mLastMotionX < 0) {
                            viewPager.setScrollable(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        viewPager.setScrollable(true);
                        break;
                }
            } else {
                viewPager.setScrollable(true);
            }
        } else {
            viewPager.setScrollable(false);
        }
        return super.dispatchTouchEvent(ev);
    }

    protected void exitHandler() {
        if (isBack) {
            exitApp();
            exitHandler.removeMessages(0);
            MeetApplication.isStart = false;
            finish();
            MeetApplication.finishApp();
        } else {
            isBack = true;
            ToastUtils.showMessage(this, R.string.app_exit);
            exitHandler.sendEmptyMessageDelayed(0, 2000);
        }
    }


    @Override
    public void onEventMainThread(POEventBus event) {
        if (event == null) {
            return;
        }
        if (event.getId() == Constants.RESPONSE_CODE_TOKEN_FAIL || event.getId() == Constants.RESPONSE_CODE_USER_SEALED) {
            SystemUtils.exitLogin(this);
            ToastUtils.showToast(event.getData());
        }
        super.onEventMainThread(event);
    }

    protected void hideSoftInput() {
        // hide soft input window after sending.
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            View focusView = getCurrentFocus();
            if (focusView != null && imm != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }

    @Override
    public void onChangedLive(POFocus liveBean) {
        if (mNewPersonalFragment != null && liveBean != null && liveBean.getMaster() != null) {
            mNewPersonalFragment.resetParams(liveBean.getMaster().getUid());
            mNewPersonalFragment.freshData();
            mNeedRefreshPersonList = true;
        } else {
//            mNewPersonalFragment.reset();//清空页面
        }
    }

    @Override
    public boolean onOpenUserInfo() {
        if (viewPager.getCurrentItem() == 0) {
            viewPager.setCurrentItem(1);
        }
        return true;
    }


    @Override
    public void canSwipeChanged(boolean canSwipe, boolean canRightSwipe) {
        mCanSwipe = canSwipe;
        mCanRightSwipe = canRightSwipe;
        viewPager.setCanSwipe(canSwipe, canRightSwipe);
    }

    @Override
    public void getUserSuccess(boolean bool, POMember data) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                EventBus.getDefault().post(new SeekBarEvent(keyCode, SeekBarEvent.SENKBAR));
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
