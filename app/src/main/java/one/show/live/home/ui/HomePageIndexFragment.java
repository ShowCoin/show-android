package one.show.live.home.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ScaleTransitionPagerTitleView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import one.show.live.R;
import one.show.live.common.ui.BaseFragment;
import one.show.live.common.util.DeviceUtils;

/**
 * Created by liuzehua on 2018/4/8.
 * 首页 fragment
 */

public class HomePageIndexFragment extends BaseFragment {


    public final static int HOT = 0x10 << 1;
    public final static int FOCUS = 0x10 << 2;
    public final static int LATEST = 0x10 << 3;




    @BindView(R.id.homepageindex_viewpage)
    ViewPager homepageindexViewpage;
    @BindView(R.id.homepageindex_indicator)
    MagicIndicator homepageindexIndicator;
    @BindView(R.id.homepageindex_lay)
    RelativeLayout homepageindexLay;
    @BindView(R.id.homepageindex_top)
    LinearLayout homepageindexTop;
    Unbinder unbinder;
    //顶部的标题
    private int[] titleIds = new int[]{R.string.focus,R.string.hot, R.string.latest};
    private int[] types = new int[]{FOCUS,HOT, LATEST};


    HotFragment hotFragment;
    FocusFragment focusFragment;
    FocusFragment latestFragment;


    public static HomePageIndexFragment newInstance(){
        HomePageIndexFragment homePageIndexFragment = new HomePageIndexFragment();
        return homePageIndexFragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_homepageindex;
    }


    @Override
    protected void initView() {
        super.initView();
        initMagicIndicator();

        homepageindexViewpage.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return types.length;
            }

            @Override
            public Fragment getItem(int position) {
                if (types[position] == HOT) {
                    return HotFragment.newInstance();
                } else if (types[position] == FOCUS){
                    return FocusFragment.newInstance(FocusFragment.FOCUS);
                }else{
                    return FocusFragment.newInstance(FocusFragment.LATEST);
                }

            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
                switch (position) {
                    case 0:

                        focusFragment = (FocusFragment) createdFragment;
                        break;
                    case 1:
                        hotFragment = (HotFragment) createdFragment;
                        break;
                    case 2:
                        latestFragment = (FocusFragment)createdFragment;
                        break;
                }
                return createdFragment;
            }


            @Override
            public CharSequence getPageTitle(int position) {
                return getString(titleIds[position]);
            }
        });

//        homepageindexViewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                positionNum = position;
////                if (position == 1) {
////                    if (TextUtils.isEmpty(address)) {
////
////                        if (TextUtils.isEmpty(POBaiduLocation.address)) {
////                            homepageindexCity.setVisibility(View.GONE);
////                            homepageindexFilter.setImageResource(R.drawable.ico_hometitle_address);
////                        } else {
////                            homepageindexFilter.setImageResource(R.drawable.ico_hometitle_next);
////                            homepageindexCity.setText(POBaiduLocation.address);
////                            homepageindexCity.setVisibility(View.VISIBLE);
////                        }
////                    } else {
////                        homepageindexFilter.setImageResource(R.drawable.ico_hometitle_next);
////                        homepageindexCity.setVisibility(View.VISIBLE);
////                        homepageindexCity.setText(address);
////                    }
////
////                } else {
////                    homepageindexCity.setVisibility(View.GONE);
////                    homepageindexFilter.setImageResource(R.drawable.ico_hometitle_search);
////                }
//                if (position == 0) {//切换箭头颜色
//                    homepageindexArrow.setImageResource(R.drawable.screening_arrow_g);
//                } else {
//                    homepageindexArrow.setImageResource(R.drawable.screening_arrow_b);
//                }
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });


        homepageindexViewpage.setOffscreenPageLimit(2);
        homepageindexViewpage.setCurrentItem(1);//设置显示热门
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titleIds.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ScaleTransitionPagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
                simplePagerTitleView.setText(titleIds[index]);
                simplePagerTitleView.setTextSize(16);
                simplePagerTitleView.setMinScale(30 * 1.0f / 32);
                simplePagerTitleView.setNormalColor(Color.parseColor("#B6B6B6"));//默认颜色
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(getContext(), R.color.color_ffffff));//选中颜色
                simplePagerTitleView.setWidth(DeviceUtils.dipToPX(getContext(), 60));
//                if(index == 0){
//                    simplePagerTitleView.setGravity(Gravity.RIGHT|Gravity.CENTER_VERTICAL);
//                }else if(index == 1){
//                    simplePagerTitleView.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
//                }

                int padding = UIUtil.dip2px(context, 5);
                simplePagerTitleView.setPadding(padding, 0, padding, 0);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        homepageindexViewpage.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {

                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                indicator.setLineWidth(UIUtil.dip2px(context, 20));
                indicator.setYOffset(UIUtil.dip2px(context, 0));
                indicator.setLineHeight(UIUtil.dip2px(context, 2));
                indicator.setColors(Color.parseColor("#ffffff"));
                return indicator;
//                return new ArrowPagerIndicator(context);
            }
        });
        homepageindexIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(homepageindexIndicator, homepageindexViewpage);
    }
}
