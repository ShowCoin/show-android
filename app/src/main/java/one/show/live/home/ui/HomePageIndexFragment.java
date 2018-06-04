package one.show.live.home.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liulishuo.filedownloader.i.IFileDownloadIPCCallback;

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
import one.show.live.log.Logger;
import one.show.live.message.binder.ILiveInnerBinder;
import one.show.live.message.ui.ConversationListActivity;
import one.show.live.message.ui.LiveInnerConversationDialog;
import one.show.live.message.ui.LiveInnerConversationListDialog;
import one.show.live.po.POFocus;
import one.show.live.util.ConvertToUtils;

/**
 * Created by Nano on 2018/4/8.
 * 首页 fragment
 */

public class HomePageIndexFragment extends BaseFragment implements OnGetAnimatorWhenOpenLiveMenu, ILiveInnerBinder,LiveStatusChangedImpl,OnLockClickListener {


    public final static int HOT = 0x10 << 1;
    public final static int FOCUS = 0x10 << 2;
    public final static int LATEST = 0x10 << 3;


    @BindView(R.id.homepageindex_viewpage)
    ViewPager homepageindexViewpage;
    @BindView(R.id.homepageindex_indicator)
    MagicIndicator homepageindexIndicator;
    @BindView(R.id.homepageindex_lay)
    RelativeLayout homepageindexLay;
    Unbinder unbinder;

    @BindView(R.id.homepageindex_left)
    ImageView homepageindexLeft;
    @BindView(R.id.homepageindex_right)
    ImageView homepageindexRight;
    @BindView(R.id.homepageindex_right_text)
    TextView homepageindexRightText;


    private boolean isOpenLiveMenu;

    private boolean isLockClick;

    private float dp47;

    //顶部的标题
    private int[] titleIds = new int[]{R.string.focus, R.string.hot, R.string.latest};
    private int[] types = new int[]{FOCUS, HOT, LATEST};


    HotFragment hotFragment;
    FocusFragment focusFragment;
    FocusFragment latestFragment;


    public static HomePageIndexFragment newInstance() {
        HomePageIndexFragment homePageIndexFragment = new HomePageIndexFragment();
        return homePageIndexFragment;
    }


    @Override
    protected int getContentView() {
        return R.layout.fragment_homepageindex;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        dp47 = ConvertToUtils.dipToPX(getActivity(), 47 + 25);

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
                } else if (types[position] == FOCUS) {
                    return FocusFragment.newInstance(FocusFragment.FOCUS);
                } else {
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
                        latestFragment = (FocusFragment) createdFragment;
                        break;
                }
                return createdFragment;
            }


            @Override
            public CharSequence getPageTitle(int position) {
                return getString(titleIds[position]);
            }
        });

        homepageindexRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpenLiveMenu) {
                    showConversationList();
                } else {
                    final Intent intent = new Intent(getActivity(), ConversationListActivity.class);
                    startActivity(intent);
                }
            }
        });


        homepageindexViewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    resumeLive();
                    ((OnChangedHomePageSwipeListener) getActivity()).canSwipeChanged(true, true);
                } else {
                    pauseLive();
                    ((OnChangedHomePageSwipeListener) getActivity()).canSwipeChanged(true, false);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        homepageindexViewpage.setOffscreenPageLimit(2);
        homepageindexViewpage.setCurrentItem(1);//设置显示热门
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
                        Logger.e("s2s3","click isLockClick"+isLockClick+"isOpenLiveMenu"+isOpenLiveMenu);

                        if(!isLockClick&&!isOpenLiveMenu) {
                            homepageindexViewpage.setCurrentItem(index);
                        }
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

    public boolean getIsShowLiveList() {
        return homepageindexViewpage.getCurrentItem() == 1;
    }


    @Override
    public void showConversationList() {
        final LiveInnerConversationListDialog conversationList = new LiveInnerConversationListDialog();
        conversationList.attachToBinder(this);
        getFragmentManager().beginTransaction()
                .add(conversationList, LiveInnerConversationListDialog.TAG)
                .commit();
    }

    @Override
    public void moveToConversationView(String targetId) {
        final LiveInnerConversationDialog conversation = new LiveInnerConversationDialog();
        final Bundle bundle = LiveInnerConversationDialog.getCallingBundle(targetId);
        conversation.attachToBinder(this);
        conversation.setArguments(bundle);
        getFragmentManager().beginTransaction()
                .add(conversation, LiveInnerConversationDialog.TAG)
                .commit();
    }


    @Override
    public Animator getOpenLiveMenuAnim(boolean isOpenLiveMenu) {//这里执行的是上面热门 关注 最新的动画
        ObjectAnimator menuAnim;
        this.isOpenLiveMenu = isOpenLiveMenu;
        Animator anim = ((OnGetAnimatorWhenOpenLiveMenu) getParentFragment()).getOpenLiveMenuAnim(isOpenLiveMenu);
        if (isOpenLiveMenu) {
            menuAnim = ObjectAnimator.ofFloat(homepageindexIndicator, View.TRANSLATION_Y, 0f, -dp47);
            menuAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    if(animation.isRunning()) {
                        if (homepageindexIndicator.getTranslationY() != -dp47) {
                            homepageindexIndicator.setTranslationY(-dp47);
                        }
                    }
                }
            });
            menuAnim.setDuration(150);

        } else {
            menuAnim = ObjectAnimator.ofFloat(homepageindexIndicator, View.TRANSLATION_Y, -dp47, 0f);
            menuAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationCancel(Animator animation) {
                    if(animation.isRunning()) {
                        if (homepageindexIndicator.getTranslationY() != 0f) {
                            homepageindexIndicator.setTranslationY(0f);
                        }
                    }
                }
            });
            menuAnim.setDuration(150);
        }




        AnimatorSet set = new AnimatorSet();
        set.playTogether(anim, menuAnim);

        return set;
    }

    @Override
    public void pauseLive() {
        if(hotFragment!=null&&hotFragment.isAdded()){
            hotFragment.pauseLive();
        }
    }

    @Override
    public void resumeLive() {
        if(hotFragment!=null&&hotFragment.isAdded()){
            hotFragment.resumeLive();
        }
    }

    @Override
    public void onLockClick(boolean isLock) {
        isLockClick = isLock;
        if(getParentFragment() instanceof OnLockClickListener){
            ((OnLockClickListener) getParentFragment()).onLockClick(isLock);
        }
    }

}
