package one.show.live.home.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import one.show.live.R;
import one.show.live.common.po.POLogin;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BaseFragment;
import one.show.live.common.util.StatusBarUtil;
import one.show.live.log.Logger;
import one.show.live.personal.presenter.UserPresenter;
import one.show.live.personal.view.UserView;
import one.show.live.po.eventbus.FocusOnEventBean;
import one.show.live.widget.MeHeaderView;

/**
 * Created by Nano on 2018/4/10.
 * 我的个人中心页面
 */

public class MeFragment extends BaseFragment implements UserView {


    public final static int WORK = 0x10 << 1;
    public final static int LIKE = 0x10 << 2;

    public final static int HOME = 0x10 << 3;
    public final static int OTHER = 0x10 << 4;

    private final static String TAG = "MeFragment";

    @BindView(R.id.appBar)
    AppBarLayout appBarView;

    @BindView(R.id.me_top_header)
    MeHeaderView meTopHeader;
    @BindView(R.id.me_indicator)
    MagicIndicator meIndicator;
    @BindView(R.id.me_viewpage)
    ViewPager meViewpage;

    UserPresenter userPresenter;

    @BindView(R.id.toolbar)
    View toolbar;

    @BindView(R.id.titleTextView)
    TextView titleTextView;

    String imagePath;

    private final static float TOOLBAR_THRESHOLD_RADIOS = 0.7f;


    private POMember incomingPOmenber;//传进来的pomember
    private String incomingUid;//传进来的uid

    private int[] types = new int[]{WORK, LIKE};
    private int[] titles = new int[]{R.string.work, R.string.like};

    MeWorkFragment meWorkFragment;

    boolean isOneSelf;//用来判断是不是自己的个人中心页面
    boolean isHome;//用来判断是不是在首页显示的个人中心


    private POMember mPOmember;//用来存储pomember的，不管是传进来的还是请求来的

    public static MeFragment newInstance(POMember poMember, int showType) {
        MeFragment meFragment = new MeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("poMember", poMember);
        bundle.putInt("showType", showType);
        meFragment.setArguments(bundle);
        return meFragment;
    }

    public static MeFragment newInstance(String uid, int showType) {
        MeFragment meFragment = new MeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("uid", uid);
        bundle.putInt("showType", showType);
        meFragment.setArguments(bundle);
        return meFragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_me;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

//        ((BaseFragmentActivity) getContext()).initState(statusView, R.color.transparent);
        EventBus.getDefault().register(this);
        incomingPOmenber = (POMember) getArguments().getSerializable("poMember");
        incomingUid = getArguments().getString("uid");

        if (incomingPOmenber == null) {
            isOneSelf = POLogin.isCurrentUser(incomingUid);
        } else {
            isOneSelf = POLogin.isCurrentUser(incomingPOmenber.getUid());
            mPOmember = incomingPOmenber;
        }

        isHome = getArguments().getInt("showType") == HOME ? true : false;

        meTopHeader.setis(isOneSelf, isHome);

        userPresenter = new UserPresenter(this);

        initMagicIndicator2();

        meViewpage.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return types.length;
            }

            @Override
            public Fragment getItem(int position) {
                if (types[position] == WORK) {
                    return MeWorkFragment.newInstance(MeWorkFragment.WORK, incomingUid);
                } else {
                    return MeWorkFragment.newInstance(MeWorkFragment.LIKE, incomingUid);
                }
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
                switch (position) {
                    case 0:

                        meWorkFragment = (MeWorkFragment) createdFragment;
                        break;
                    case 1:
                        meWorkFragment = (MeWorkFragment) createdFragment;
                        break;
                }
                return createdFragment;
            }


            @Override
            public CharSequence getPageTitle(int position) {
                return getString(titles[position]);
            }
        });


        meViewpage.setOffscreenPageLimit(2);
        meViewpage.setCurrentItem(0);//设置显示作品

        appBarView.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                //设置toolbar透明度随动渐变
                int toolbarHeight = toolbar.
                        getHeight();
                int appBarHeight = appBarLayout
                        .getHeight();
                int range = appBarHeight - toolbarHeight;
                int offsetAbs = Math.abs(verticalOffset);
                if (range <= 0) {
                    toolbar.setAlpha(0);
                } else {
                    float alpha = (float) offsetAbs / range;
                    toolbar.setAlpha(alpha);
                    if (toolbar.getVisibility() != View.VISIBLE) {
                        toolbar.setVisibility(View.VISIBLE);
                    }
                }

                //处理浅色标题时状态栏文字看不清的问题
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (range > 0 && ((float) offsetAbs / range) > TOOLBAR_THRESHOLD_RADIOS) {
                        setLightStatusBar();
                    } else {
                        restoreNormalStatusBar();
                    }
                }
            }
        });

        //动态刷新Toolbar高度来适配状态栏和标题重叠问题
        int statusHeight = StatusBarUtil
                .getStatusBarHeight(getActivity());
        int titleHeight = getResources()
                .getDimensionPixelSize(R.dimen.me_fragment_translucent_title_height)
                + statusHeight;
        final CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar
                .getLayoutParams();
        lp.height = titleHeight;
        toolbar.setLayoutParams(lp);
        toolbar.setPadding(0, statusHeight, 0, 0);
    }

    private void setLightStatusBar() {
        getActivity().getWindow()
                .getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    private void restoreNormalStatusBar() {
        getActivity().getWindow()
                .getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
    }


    private void initMagicIndicator2() {
        meIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(getString(titles[index]));
                simplePagerTitleView.setNormalColor(Color.parseColor("#8e8e8e"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#333333"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        meViewpage.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setStartInterpolator(new AccelerateInterpolator());
//                indicator.setEndInterpolator(new DecelerateInterpolator(1.6f));
                indicator.setYOffset(UIUtil.dip2px(context, 0));
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setColors(Color.parseColor("#333333"));
                return indicator;
            }

            @Override
            public float getTitleWeight(Context context, int index) {
                return 1f;
            }
        });
        meIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(meIndicator, meViewpage);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (incomingPOmenber == null) {//没有传的话就去请求
            freshData();
        } else {//传的话直接显示就可以了
            if (imagePath != null && imagePath.equals(incomingPOmenber.getLarge_avatar())) {
                meTopHeader.setData(incomingPOmenber, false);
            } else {
                meTopHeader.setData(incomingPOmenber, true);
                imagePath = incomingPOmenber.getLarge_avatar();
            }
        }
    }

    public void resetParams(String uid) {
        incomingUid = uid;
    }


    /**
     * 刷新上部数据
     */
    public void freshData() {
        userPresenter.getData(incomingUid);
    }

    /**
     * 刷新列表
     */
    public void freshRecycle() {

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (getUserVisibleHint()) {
            meTopHeader.setBgheight(true);
        }
    }

    /**
     * 个人数据回调
     *
     * @param bool
     */
    @Override
    public void getUserSuccess(boolean bool, POMember data) {
        if (isContextAlive() && bool) {
            mPOmember = data;
            if (imagePath != null && imagePath.equals(data.getLarge_avatar())) {
                meTopHeader.setData(data, false);
            } else {
                meTopHeader.setData(data, true);
                imagePath = data.getLarge_avatar();
            }
            titleTextView.setText(data.getNickname());

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(FocusOnEventBean event) { //接收关注状态的
        if (event != null && event.isFocuson() && event.getUid().equals(mPOmember.getUid())) {
            if (event.getType() == FocusOnEventBean.FOCUS_ON) {//关注成功
                meTopHeader.setFocusStatus(true);
            } else if (event.getType() == FocusOnEventBean.UN_FOCUS_ON) {//取消关注成功
                meTopHeader.setFocusStatus(false);
            }
        }
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
