package one.show.live.media.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

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

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import one.show.live.R;
import one.show.live.common.ui.BaseFragment;

/**
 * Created by liuzehua on 2018/4/14.
 */

public class ContributionIndexFragment extends BaseFragment {


    public final static int DAY = 0x10 << 1;
    public final static int WEEK = 0x10 << 2;
    public final static int TOTAL = 0x10 << 3;

    private  String[] CHANNELS;
    private List<String> mDataList;

    private int[] types = new int[]{DAY,WEEK,TOTAL};


    @BindView(R.id.contributionindex_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.contributionindex_viewpage)
    ViewPager contributionindexViewpage;
    Unbinder unbinder;



    DayListFragment dayListFragment;

    public static ContributionIndexFragment newInstance() {
        ContributionIndexFragment contributionIndexFragment = new ContributionIndexFragment();
        return contributionIndexFragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_contributionindex;
    }

    @Override
    protected void initView() {
        super.initView();

        CHANNELS = new String[]{getString(R.string.list_of_day), getString(R.string.list_of_week), getString(R.string.the_total_list)};
        mDataList = Arrays.asList(CHANNELS);
        initMagicIndicator2();


        contributionindexViewpage.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                return types.length;
            }

            @Override
            public Fragment getItem(int position) {
                if (types[position] == DAY) {
                    return DayListFragment.newInstance();
                } else if (types[position] == WEEK){
                    return DayListFragment.newInstance();
                }else{
                    return DayListFragment.newInstance();
                }

            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
                switch (position) {
                    case 0:

                        dayListFragment = (DayListFragment) createdFragment;
                        break;
                    case 1:
                        dayListFragment = (DayListFragment) createdFragment;
                        break;
                    case 2:
                        dayListFragment = (DayListFragment) createdFragment;
                        break;
                }
                return createdFragment;
            }


            @Override
            public CharSequence getPageTitle(int position) {
                return mDataList.get(position);
            }
        });

        contributionindexViewpage.setOffscreenPageLimit(2);
        contributionindexViewpage.setCurrentItem(0);//设置显示日榜
    }

    private void initMagicIndicator2() {
        magicIndicator.setBackgroundColor(Color.WHITE);
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(mDataList.get(index));
                simplePagerTitleView.setNormalColor(Color.parseColor("#8e8e8e"));
                simplePagerTitleView.setSelectedColor(Color.parseColor("#333333"));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contributionindexViewpage.setCurrentItem(index);
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
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, contributionindexViewpage);
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
}
