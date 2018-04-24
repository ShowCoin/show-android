package one.show.live.play.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

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
import one.show.live.R;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.DeviceUtils;

/**
 * Created by liuzehua on 2018/4/14.
 * 贡献榜的页面
 */

public class ContributionListActivity extends BaseFragmentActivity {



    public final static int CON = 0x10 << 1;
    public final static int INC = 0x10 << 2;



    @BindView(R.id.contribution_indicator)
    MagicIndicator contributionIndicator;
    @BindView(R.id.contribution_viewpage)
    ViewPager contributionViewpage;


    //顶部的标题
    private int[] titleIds = new int[]{R.string.contribution_to_the_list, R.string.list_incentives};
    private int[] types = new int[]{CON,INC};

    ContributionIndexFragment homePageIndexFragment;


    public static Intent getCallingIntent(Context context){
        Intent intent  = new Intent(context,ContributionListActivity.class);
        return intent;
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_contribution;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initBaseView() {
        super.initBaseView();
        initMagicIndicator();

        contributionViewpage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return types.length;
            }

            @Override
            public Fragment getItem(int position) {
                if (types[position] == CON) {
                    return ContributionIndexFragment.newInstance();
                } else{
                    return ContributionIndexFragment.newInstance();
                }

            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
                switch (position) {
                    case 0:

                        homePageIndexFragment = (ContributionIndexFragment) createdFragment;
                        break;
                    case 1:
                        homePageIndexFragment = (ContributionIndexFragment) createdFragment;
                        break;

                }
                return createdFragment;
            }


            @Override
            public CharSequence getPageTitle(int position) {
                return getString(titleIds[position]);
            }
        });

        contributionViewpage.setOffscreenPageLimit(2);
        contributionViewpage.setCurrentItem(0);//设置显示贡献榜

    }

    private void initMagicIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(this);
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
                simplePagerTitleView.setMinScale(16 * 1.0f / 18);
                simplePagerTitleView.setNormalColor(Color.parseColor("#B6B6B6"));//默认颜色
                simplePagerTitleView.setSelectedColor(ContextCompat.getColor(ContributionListActivity.this, R.color.color_ffffff));//选中颜色
                simplePagerTitleView.setWidth(DeviceUtils.dipToPX(ContributionListActivity.this, 60));

                int padding = UIUtil.dip2px(context, 5);
                simplePagerTitleView.setPadding(padding, 0, padding, 0);
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        contributionViewpage.setCurrentItem(index);
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
                return null;
//                return new ArrowPagerIndicator(context);
            }
        });
        contributionIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(contributionIndicator, contributionViewpage);
    }
}
