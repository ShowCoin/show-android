package one.show.live.live.gift.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.greenrobot.eventbus.EventBus;
import one.show.live.R;
import one.show.live.common.global.presenter.ActionPresenter;
import one.show.live.common.po.POCommonResp;
import one.show.live.common.po.POEventBus;
import one.show.live.common.po.POLaunch;
import one.show.live.po.POLive;
import one.show.live.common.po.POMember;
import one.show.live.common.util.ConvertToUtils;
import one.show.live.common.util.DeviceUtil;
import one.show.live.common.util.NetworkUtils;
import one.show.live.common.util.NumberUtil;
import one.show.live.common.util.StringUtils;
import one.show.live.common.util.UIUtils;
import one.show.live.common.view.ToastUtils;
import one.show.live.live.gift.db.DataBaseManager;
import one.show.live.live.gift.request.BuyGiftsRequest;
import one.show.live.live.gift.request.GetGiftsListRequest;
import one.show.live.live.model.GetBalanceRequest;
import one.show.live.live.po.POGift;
import one.show.live.live.po.POGiftList;
import one.show.live.live.util.LiveUiUtils;
import one.show.live.live.widget.GiftDoubleHitProgressBar;
import one.show.live.log.Logger;
import one.show.live.money.ui.MyCoinsActivity;
import one.show.live.po.GiftBean;
import one.show.live.po.POBalance;
import one.show.live.po.WalletBean;
import one.show.live.util.EventBusKey;
import one.show.live.util.NoDoubleClickListener;
import one.show.live.util.SystemUtils;


/**
 * 发送礼物弹窗
 */
public class SendGiftsView extends RelativeLayout implements View.OnClickListener {
    private ViewPager giftViewPager;
    private GiftAdapter giftAdapter;
    private int mPage, pageCount;
    public static final int GRIDVIEW_ITEM_COUNT = 10;//两行
    private List<GiftAdapter> mGridViewAdapters = new ArrayList<GiftAdapter>();
    private List<POGift> lists = new ArrayList<POGift>();
    private List<View> mAllViews = new ArrayList<View>();
    private GiftViewPagerAdapter myViewPagerAdapter;
    private POGift giftBean;
    private Handler mGiftHandler;

    private RelativeLayout mParentLay, mBottomLay;
    private LinearLayout mChargeLay;
    private TextView mGoldCoin;
    private TextView mCharge;
    private Button mSendGiftBtn;
    private long goldCoin = 0;
    private DataBaseManager dbManager;

    private POLive liveBean;

    private String payActivityAction = "one.show.live.payActivity";
    private Activity mContext;

    private int duration = 3000;
    private TextView timeTV;
    private RelativeLayout mDoubleHitLay;
    //    private GiftDoubleHitProgressBar mDoubleHitBtn;
    private ValueAnimator doubleHitAnimation;
    private SendGiftListener giftListener;

    private SendGiftsView giftsView;
    private OnVisibilityChangedListener visibilityChangedListener;

    private int width;
    private LinearLayout.LayoutParams params;
    private LinearLayout dotsLay;

    private boolean isGiftActivity;
    private int mHeight;

    ActionPresenter actionPresenter;

    public SendGiftsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public SendGiftsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SendGiftsView(Context context, POLive liveBean, SendGiftListener sendGiftListener) {
        super(context);
        this.liveBean = liveBean;
        this.giftListener = sendGiftListener;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.activity_gifts, this);
        mContext = (Activity) context;
        actionPresenter= new ActionPresenter();
        giftsView = this;
        findView();
        setDialogData();
        setListener();
    }

    private void findView() {
        mParentLay = (RelativeLayout) findViewById(R.id.gift_lay);
        giftViewPager = (ViewPager) findViewById(R.id.gift_viewpager);
        mChargeLay = (LinearLayout) findViewById(R.id.charge_lay);
        mBottomLay = (RelativeLayout) findViewById(R.id.gift_bottom_lay);
        mGoldCoin = (TextView) findViewById(R.id.goldCoin_value);
        mCharge = (TextView) findViewById(R.id.charge_index);
        mSendGiftBtn = (Button) findViewById(R.id.send_gift_btn);
        mDoubleHitLay = (RelativeLayout) findViewById(R.id.double_hit_lay);
        dotsLay = (LinearLayout) findViewById(R.id.dotsLay);
//        mDoubleHitBtn = (GiftDoubleHitProgressBar) findViewById(R.id.double_hit_btn);
        timeTV = (TextView) findViewById(R.id.gift_time_tv);

    }

    private void setListener() {
        mParentLay.setOnClickListener(this);
        mChargeLay.setOnClickListener(this);
        mSendGiftBtn.setOnClickListener(new NoDoubleClickListener() {
            @Override public void onNoDoubleClick(View view) {
                count = 0;
                if (checkGift()) {
                    buyGift();
                }
            }
        });
        mBottomLay.setOnClickListener(this);
        mDoubleHitLay.setOnClickListener(this);
        mCharge.setOnClickListener(this);
    }

    public void setDialogData() {
        initData();
        getData();
    }

    public interface OnVisibilityChangedListener {
        void onVisibilityChanged(int visibility);
    }

    public void setVisibilityChangedListener(OnVisibilityChangedListener
                                                     visibilityChangedListener) {
        this.visibilityChangedListener = visibilityChangedListener;
    }

    private void initData() {
        /**
         * 选中的礼物
         */
        mGiftHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                int position = msg.what;
                setGiftCheckedStatus(position);
                if (null != msg.obj) {//选中的礼物
                    giftBean = (POGift) msg.obj;
                    showIsHideDoubleHitLay(false);
                    mSendGiftBtn.setClickable(true);
//                    mSendGiftBtn.setBackgroundResource(R.drawable.shape_bg_send_gift);
                }
                return true;
            }
        });
        dbManager = new DataBaseManager(mContext);
//        mDoubleHitBtn.setMaxProgress(100);
        timeTV.setText("3");
//        mSendGiftBtn.setBackgroundResource(R.drawable.shape_bg_send_gift_unclickable);
        mSendGiftBtn.setClickable(false);
        width = DeviceUtil.getScreenSize(getContext()).widthPixels;

        params = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.height = ConvertToUtils.dipToPX(getContext(), 171);
        mHeight = params.height+ConvertToUtils.dipToPX(getContext(), 45);
        params.width = width;
        giftViewPager.setLayoutParams(params);

        giftViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {

//                daDian("live_gift_flip_event","");//礼物翻页的打点

                if (dotsLay.getVisibility() == View.VISIBLE) {
                    for (int i = 0; i < pageCount; i++) {
                        if (i == position) {
                            dots[i].setEnabled(true);
                        } else {
                            dots[i].setEnabled(false);
                        }
                    }
                }
            }
        });
    }

    public void getZhenzhu(){

        new GetBalanceRequest() {
            @Override
            public void onFinish(boolean isSuccess, String msg, POBalance data) {
                if (data != null) {
                    if (isSuccess) {
                        POMember.getInstance().updateBeike((int)data.getBeike());
                        refreshGoldCoin();
                    } else {
                        ToastUtils.showToast(msg);
                    }
                }
            }
        }.startRequest(null);
    }

    public void getData() {
        getZhenzhu();
        if (null != dbManager.query() && dbManager.query().size() > 0) {
            lists.clear();
            lists = dbManager.query();
            setData();
            return;
        }
        new GetGiftsListRequest() {
            @Override
            public void onFinish(boolean isSuccess, String msg, POGiftList data) {
                if (isSuccess) {
                    lists.clear();
                    lists = data.getGift_list();
                    setData();
                } else {
                    giftsView.setVisibility(View.GONE);
                    ToastUtils.showToast(msg);
                }

            }
        }.startRequest(null);
    }


    public void refreshGoldCoin(){
        goldCoin = POMember.getInstance().getBeikeNum();
        refreshGoldCoinText();
    }

    ImageView[] dots;


    private void initDots() {
        if (pageCount <= 1) {
            dotsLay.setVisibility(View.INVISIBLE);
        } else {
            dotsLay.setVisibility(View.VISIBLE);
            dots = new ImageView[pageCount];
        }
        int dp5 = ConvertToUtils.dipToPX(getContext(), 7);
        int dp6 = ConvertToUtils.dipToPX(getContext(), 6);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dp5, dp5);
        params.leftMargin = dp6;

        if (dotsLay.getVisibility() == View.VISIBLE) {
            for (int i = 0; i < pageCount; i++) {
                dots[i] = new ImageView(getContext());
                dots[i].setImageResource(R.drawable.dot_bg);
                dots[i].setScaleType(ImageView.ScaleType.FIT_CENTER);
                if (i == 0) {
                    dots[i].setEnabled(true);
                } else {
                    dots[i].setEnabled(false);
                }
                dotsLay.addView(dots[i], params);
            }
        }
    }

    private void setData() {
        pageCount = (lists.size() + GRIDVIEW_ITEM_COUNT - 1) / GRIDVIEW_ITEM_COUNT;
        initDots();
        mGridViewAdapters.clear();
        mAllViews.clear();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        for (int i = 0; i < pageCount; i++) {
            View mView = inflater.inflate(R.layout.gift_gridview, null);
            GridView mGridView = (GridView) mView
                    .findViewById(R.id.gift_gridview_view);
            giftAdapter = new GiftAdapter(mContext, i, mGiftHandler);
            mGridView.setAdapter(giftAdapter);
            giftAdapter.setData(lists);
            giftAdapter.notifyDataSetChanged();
            mGridViewAdapters.add(giftAdapter);
            mAllViews.add(mView);
        }

        myViewPagerAdapter = new GiftViewPagerAdapter();
        giftViewPager.setAdapter(myViewPagerAdapter);
        myViewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.charge_lay) {
            LiveUiUtils.jumpToPayActivity(getContext());
        } else if (i == R.id.send_gift_btn) {
            //count = 0;
            //if (checkGift()) {
            //    buyGift();
            //}
        } else if (i == R.id.gift_lay) {
            if(isGiftActivity){
                EventBus.getDefault().post(new POEventBus(EventBusKey.GIFT_ACTIVITY_FINISH, null));
            }else{
                this.setVisibility(View.GONE);
            }
        } else if (i == R.id.gift_bottom_lay) {//不要删,防止点击底部栏,giftView隐藏

        } else if (i == R.id.double_hit_lay) {
            if (checkGift()) {
                buyGift();
            }
        }else if(i == R.id.charge_index){//点击充值
            getContext().startActivity(MyCoinsActivity.getCallingIntent(getContext()));
        }
    }


    Animator animator;

    @Override
    public void setVisibility(final int visibility) {
        if(animator!=null&&animator.isRunning()) {
            clearAnimation();
            animator.cancel();
        }
        if (visibility == View.VISIBLE) {
            superVisiblity(visibility);
            animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, mHeight, 0);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (visibilityChangedListener != null) {
                        visibilityChangedListener.onVisibilityChanged(visibility);
                    }
                }
            });
        } else {
            animator = ObjectAnimator.ofFloat(this, View.TRANSLATION_Y, 0, mHeight);
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    superVisiblity(visibility);
                    if (visibilityChangedListener != null) {
                        visibilityChangedListener.onVisibilityChanged(visibility);
                    }
                }
            });
        }
        animator.start();
    }


    private void superVisiblity(int visibility){
        super.setVisibility(visibility);
    }

    /**
     * 送礼物判断
     */
    private boolean checkGift() {
        if (null == giftBean) {
//            UIToast.show(mContext, "请选择礼物");
            return false;
        }
        if (goldCoin >= giftBean.getPrice()) {
            goldCoin = goldCoin - giftBean.getPrice();
            refreshGoldCoinText();
        } else {
            LiveUiUtils.showBuyGoldDialog(getContext());
            return false;
        }
        if (null == liveBean && !isGiftActivity) {
//            UIToast.show(mContext, "未获取到主播信息");
            return false;
        }
        if(!isGiftActivity){
            if (giftBean.getType() != 1) {//非连击礼物
                showIsHideDoubleHitLay(false);
            } else {
                showIsHideDoubleHitLay(true);
                showDoubleHitProgress();
                count++;
            }
        }
        return true;
    }


    private void showIsHideDoubleHitLay(boolean isShow) {
        if (isShow) {
            mDoubleHitLay.setVisibility(View.VISIBLE);
            mSendGiftBtn.setVisibility(View.GONE);
        } else {
            mDoubleHitLay.setVisibility(View.GONE);
            mSendGiftBtn.setVisibility(View.VISIBLE);
        }
    }

    private int count;
    private String tid;

    public void setTargetId(String tid) {
        this.tid = tid;
    }

    /**
     * 买礼物
     */
    private void buyGift() {
//        if (giftBean.getAnimationtype() != 1){
//            setVisibility(GONE);
//        }
        Map<String, String> params = new HashMap<>();
        if(liveBean != null){
            params.put("roomId", liveBean.getRoom_id());
        }
        params.put("tid", tid);
        params.put("gid", String.valueOf(giftBean.getId()));
        params.put("number", "1");
        if(isGiftActivity){ //如果是送礼物的activity 即 在会话页面送礼物
            params.put("count", "1");
        }else{
            params.put("count", String.valueOf(count));
        }


        Logger.e("simon", "连送》》" + count);



        new BuyGiftsRequest() {
            @Override
            public void onFinish(boolean isSuccess, String msg, Object data) {
                if (isSuccess) {
                    giftListener.onSendGift(giftBean);
                    if (giftBean.getType() != 1) {
                        giftBean = null;
                        setGiftCheckedStatus(-1);
                    }
                } else {
                    ToastUtils.showToast(msg);
                    goldCoin = goldCoin + giftBean.getPrice();
                    refreshGoldCoinText();
                }
            }
        }.startRequest(params);

//        daDian("live_gift_send_event",String.valueOf(giftBean.getId()));//送礼按钮的点击事件
    }


    private void refreshGoldCoinText() {
        mGoldCoin.setText("余额: " + goldCoin);
    }

    /**
     * 修改礼物选中状态
     *
     * @param position
     */
    private void setGiftCheckedStatus(int position) {
        for (int i = 0; i < lists.size(); i++) {
            if (position == i) {
                lists.get(i).isCheck = true;
            } else {
                lists.get(i).isCheck = false;
            }
        }
        for (int j = 0; j < mGridViewAdapters.size(); j++) {
            mGridViewAdapters.get(j).notifyDataSetChanged();
        }
    }

    /**
     * 显示连击progressBtn
     */
    private void showDoubleHitProgress() {
        if (null != doubleHitAnimation) {
            doubleHitAnimation.end();
            doubleHitAnimation = null;
        }
        Logger.e("simon", "点击连送");
        doubleHitAnimation = ValueAnimator.ofInt(31, 0);
        doubleHitAnimation.setDuration(5000);
        doubleHitAnimation.setRepeatMode(ValueAnimator.RESTART);
        doubleHitAnimation.start();
        doubleHitAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
//                Logger.e("simon", "连送》》" + value);
//                mDoubleHitBtn.setProgress(100 - value);
                if (value == 1) {
                    showIsHideDoubleHitLay(false);
                }
                timeTV.setText((value - 1) + "s");
            }
        });
    }

    public void setGiftListener(SendGiftListener giftListener) {
        this.giftListener = giftListener;
    }

  /**
   * 设置当前view是否是activity的页面
   * @param isGiftActivity
   */
  public void setIsGiftActivity(boolean isGiftActivity){
        this.isGiftActivity = isGiftActivity;
    }

    /**
     * 充值成功,更新金币
     *
     * @param coin
     */
    public void setGoldCoin(String coin) {
        goldCoin = Long.parseLong(coin);
        refreshGoldCoinText();
    }


    /**
     * 设置使用的贝壳
     *
     * @param coin
     */
    public void setUseGoldCoin(int coin) {
        goldCoin = goldCoin - coin;
        refreshGoldCoinText();
    }

    public interface SendGiftListener {
        void onSendGift(POGift giftBean);
    }

    private class GiftViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mAllViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            giftViewPager.removeView((View) object);
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(mAllViews.get(position),
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                            .LayoutParams.MATCH_PARENT));
            return mAllViews.get(position);
        }
    }


    /**
     * 礼物页面的打点统计，翻页事件和送礼事件
     * gid 礼物id
     */
    public void daDian(String even,String gid){
        Map<String,String> maps = new HashMap<>();
        maps.put(POLaunch.M_event,even);
        maps.put(POLaunch.M_tn,String.valueOf(++POLaunch.actionNum));
        maps.put(POLaunch.M_pname,"live_play");
        maps.put(POLaunch.M_vid,liveBean.getVid());
        if(!gid.equals("")){
            maps.put(POLaunch.M_gid,gid);
        }
        actionPresenter.actionUp(maps);
    }

}
