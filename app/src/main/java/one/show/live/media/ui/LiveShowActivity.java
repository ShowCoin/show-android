package one.show.live.media.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import one.show.live.R;
import one.show.live.common.po.POLaunch;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.AnimUtil;
import one.show.live.log.Logger;
import one.show.live.media.listener.PlayEventListener;
import one.show.live.media.po.POIMEnd;
import one.show.live.media.po.POIMGift;
import one.show.live.media.po.POPublisherEnd;
import one.show.live.media.presenter.PublisherPresenter;
import one.show.live.media.view.PublisherView;
import one.show.live.po.POFocus;

public class LiveShowActivity extends BaseFragmentActivity implements PlayEventListener,PublisherView {
    private POFocus bean;
    private PlayFragment playFragment;
    private ChatFragment chatFragment;
    private WaitAnchorFragment waitFragment;
    private FrameLayout endLiveLayout;
    @BindView(R.id.cover_layout)
    RelativeLayout videoCover;
    @BindView(R.id.wait_image)
    ImageView waiting;
    @BindView(R.id.mainLay)
    View rootView;
    @BindView(R.id.bigAnimation)
    one.show.live.media.widget.BigAnimContainer bigAnimationLay;
    private PublisherPresenter publisherPresenter;

    private int mStatusBarHeight,mNavigationBarHeight;
    private boolean getVideoSize, bufferFull;
    PowerManager powerManager = null;
    PowerManager.WakeLock wakeLock = null;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case PlayEventListener.GET_VIDEOSIZE:
                    getVideoSize = true;
                    if (getVideoSize && bufferFull) {
                        AnimUtil.hideView(videoCover, true, 500);
                    }
                    break;
                case PlayEventListener.STATUS_START:
                    break;
                case PlayEventListener.STATUS_PLAY:
                    bufferFull = true;
                    if (getVideoSize && bufferFull) {
                        AnimUtil.hideView(videoCover, true, 500);
                    }
                    break;
            }
            return false;
        }
    });

    @Override
    public void dispatchStreamSuccess() {

    }

    @Override
    public void dispatchStreamFailed(String msg) {

    }

    @Override
    public void breakSuccess() {

    }

    @Override
    public void breakFailed(String msg) {

    }

    @Override
    public void publisherEndSuccess(POPublisherEnd data) {

    }

    @Override
    public void publisherEndFailed(String msg) {

    }

    private OnRoomEventListener onRoomEventListener = new OnRoomEventListener() {
        @Override
        public void exit(boolean isAsk) {
            if (isAsk) {
                //                new CustomDialogView.Builder(mActivity).setTitle("确定退出房间吗?").setLeftButton("确定", new DialogInterface.OnClickListener() {
                //                    @Override
                //                    public void onClick(DialogInterface dialog, int which) {
                //                        dialog.dismiss();
                exitRoom();
                //                    }
                //                }).setRightButton("取消").create().show();
            } else {
                if(POMember.getInstance().getIsAdmin()==1){//如果是巡管的话退出直播间需要弹出一个dialog
                new ActionSheetDialog(one.show.live.media.play.ui.LiveShowActivity.this)
                        .builder()
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(false)
                        .setCanceledColors(ActionSheetDialog.SheetItemColor.blue)
                        .addSheetItem("退出房间", ActionSheetDialog.SheetItemColor.blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        exitRoom();
                                    }
                                })
                        .addSheetItem("官方关闭直播", ActionSheetDialog.SheetItemColor.blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        closePublisher();
                                    }
                                }).show();
                }else{
                    exitRoom();
                }

            }
        }

        @Override
        public void streamInterrupt() {
            anchorGoAway();
        }

        @Override
        public void lookLiveEnd(POIMEnd imEnd) {
            if (videoCover != null) {
                videoCover.setVisibility(View.GONE);
            }
            endPlay(imEnd);
        }

        @Override
        public void liveEnd(POIMEnd endObj) {
            //readlyEndPlay();
        }

        @Override
        public void liveStart() {
            anchorGoBack();
        }

        @Override
        public void switchCamera(boolean isCamOn) {

        }

        @Override
        public void switchFlash() {

        }

        @Override
        public void smoothSkin(){

        }

        @Override
        public void switchMic() {}

        public void showOrHideBigAnimation(boolean isShow) {
            if (bigAnimationLay != null) {
                bigAnimationLay.setVisibility(isShow ? View.VISIBLE : View.GONE);
            }
        }

        @Override
        public void addBigGift(POIMGift gift) {
            if (bigAnimationLay != null) {
                bigAnimationLay.addGift(gift);
            }
        }
    };

    /**
     * 退出直播间
     */
    public void exitRoom() {
        EBORefreshList.sendRefreshListEvent();
        chatFragment.exit();
        daDian("live_exit_btn_event");//退出直播间打点
        finish();
    }
    private void initPresenter() {
        publisherPresenter = new PublisherPresenter(this);
    }
    /**
     * 关闭推流
     */
    private void closePublisher() {
        publisherPresenter.closePublisher(bean.getId());//作为巡管可以关闭主播的直播
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        initWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            new AndroidBug5497Workaround(this);
        } else {
            new AndroidBug5497Workaround1(this);
        }
        endLiveLayout = (FrameLayout) findViewById(R.id.end_live_frame);
        bean = getIntent().getParcelableExtra("bean");
        initView();
        initPresenter();

        //        infoView = (PlayInfoView) findViewById(R.id.info_layout);
        //        if (bean == null) {
        //            return false;
        //        }
        //        infoView.setCover(bean.getCover());

        daDian("live_play_success_event");//进入直播间打点
    }

    private void initWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintColor(Color.TRANSPARENT);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setNavigationBarTintColor(Color.TRANSPARENT);
            tintManager.setNavigationBarTintEnabled(false);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            mStatusBarHeight = config.getPixelInsetTop(false);
            mNavigationBarHeight = config.getPixelInsetBottom();
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.live_show_activity;
    }

    protected void initView() {
        //if (bean != null && StringUtils.isNotEmpty(bean.getCover())) {
        //  setCover(bean.getCover());
        //}
        AnimationDrawable animationDrawable = (AnimationDrawable) waiting.getDrawable();
        animationDrawable.start();

        chatFragment = ChatFragment.getInstance(ChatFragment.IMCLIENT_TYPE_PLAY, bean);
        chatFragment.setOnRoomEventListener(onRoomEventListener);
        playFragment = bean.isLive() ? getLiveFragment() : getVideoFragment();
        playFragment.setEventListener(this);

        //        if(bean.getIs_live() == Constants.STATUS_VIDEO){
        //            RelativeLayout.LayoutParams imlayoutManager = new RelativeLayout.LayoutParams(
        //                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        //            imlayoutManager.setMargins(0, 0, 0, ConvertToUtils.dipToPX(this, 50));
        //            findViewById(R.id.im_layout).setLayoutParams(imlayoutManager);
        //        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.im_layout, chatFragment);
        transaction.commitAllowingStateLoss();

        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
        transaction2.replace(R.id.player_layout, playFragment);
        transaction2.commitAllowingStateLoss();

        //        setInfoViewFollower();
        //
        //        if (bean.getStatus() > Constant.STATUS_VIDEO) {
        //            getReplayEvent(bean);
        //            infoView.setMaxOnline(bean.getViews());
        //            likeUtil = new LikeUtil();
        //        } else {
        //            infoView.setTagMsg("直播中");
        //        }
    }

    /**
     * 设置封面图
     */
    //public void setCover(String path) {
    //
    //  Postprocessor redMeshPostprocessor = new BasePostprocessor() {
    //    @Override public String getName() {
    //      return "Image Blur";
    //    }
    //
    //    @Override public void process(Bitmap bitmap) {
    //      ImageBlur.blurBitMap(bitmap, 10);
    //    }
    //  };
    //
    //  ImageRequest request = ImageRequestBuilder.newBuilderWithSource(FrescoUtils.getUri(path))
    //      .setPostprocessor(redMeshPostprocessor)
    //      .build();
    //
    //  PipelineDraweeController controller =
    //      (PipelineDraweeController) Fresco.newDraweeControllerBuilder()
    //          .setImageRequest(request)
    //          .setOldController(videoCover.getController())
    //          .build();
    //  videoCover.setController(controller);
    //}

    /**
     * 直播播放器
     */
    private PlayLive getLiveFragment() {
        //PlayLiveFragment liveFragment = PlayLiveFragment.getInstance(bean);
        PlayLive liveFragment = PlayLive.getInstance(bean);
        return liveFragment;
    }

    /**
     * 录播播放器
     */
    private PlayVideo getVideoFragment() {
        //PlayVideoFragment videoFragment = PlayVideoFragment.getInstance(bean);
        PlayVideo videoFragment = PlayVideo.getInstance(bean);
        return videoFragment;
    }

    @Override
    protected void onResume() {
//        SystemUiHider.getInstance(getWindow().getDecorView()).layoutFull();
        super.onResume();
        wakeLock.acquire();
    }


    @Subscribe( threadMode = ThreadMode.MAIN)
    public void closeRoom(EBOCloseRoom obj) {
        try {
            exitRoom();
        } catch (Exception e) {
            Logger.e(e.getMessage() + "");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
    //    public void onEventForFollow(FollowEventBean bean) {
    //        if (this.bean == null || bean == null) {
    //            return;
    //        }
    //        if (bean.getMember() == this.bean.getMemberid()) {
    //            this.bean.setIsfocus(bean.getFocus());
    //            setInfoViewFollower();
    //        }
    //    }
    //
    //    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    //    public void onEventMainThread(EventBusWalletBean event) {
    //        if (event.getId() == 0x203) {
    //            String coin = event.getCion();
    //            if (!"".equals(coin) && null != coin) {
    //                sendGiftsView.setGoldCoin(coin);
    //            }
    //        }
    //    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    /**
     * 主播离开
     */
    private void anchorGoAway() {
        if (videoCover != null) {
            videoCover.setVisibility(View.GONE);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_with_alpha, R.anim.exit_with_alpha);
        transaction.add(R.id.end_live_frame, waitFragment = new WaitAnchorFragment());
        transaction.commitAllowingStateLoss();
    }

    /**
     * 主播回来了
     */
    private void anchorGoBack() {
        if (waitFragment == null) {
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_with_alpha, R.anim.exit_with_alpha);
        transaction.remove(waitFragment);
        transaction.commitAllowingStateLoss();
        waitFragment = null;
        if (playFragment != null) {
            ((PlayLive) playFragment).startPlay();
        }
    }

    //    /**
    //     * 播放结束
    //     *
    //     * @param maxOnline 结束直播
    //     */
    //    private void endPlay(int maxOnline, long duration) {
    //        //AnimUtil.hideView(findViewById(R.id.report_btn), true, 300);
    //        endLiveLayout.removeAllViews();
    //        //        PlayEndFragment fragment = PlayEndFragment.getInstanceForLive(bean.getScid(), bean
    //        //                .getNickname(), maxOnline, duration, bean);
    //        //        fragment.setCover(bean.getCovers().getB());
    //        //        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    //        //        transaction.setCustomAnimations(R.anim.enter_with_alpha, R.anim.exit_with_alpha);
    //        //        transaction.remove(chatFragment);
    //        //        transaction.remove(playFragment);
    //        //        transaction.add(R.id.end_live_frame, fragment);
    //        //        transaction.commitAllowingStateLoss();
    //    }

    public class AndroidBug5497Workaround {

        private View mChildOfContent;
        private int usableHeightPrevious;
        private RelativeLayout.LayoutParams frameLayoutParams;
        private int offsetMargin = -1;

        private AndroidBug5497Workaround(final Activity activity) {
            FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
            mChildOfContent = activity.findViewById(R.id.im_layout);
            content.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        public void onGlobalLayout() {
                            possiblyResizeChildOfContent(activity);
                        }
                    });
            frameLayoutParams = (RelativeLayout.LayoutParams) mChildOfContent.getLayoutParams();
        }

        private void possiblyResizeChildOfContent(final Context context) {
            int usableHeightNow = computeUsableHeight();
            if (usableHeightNow != usableHeightPrevious) {
                int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
                //某些手机上计算可用空间等于屏幕的空间，这时候不需要减去状态栏的高度(三星盖世5)
                if (offsetMargin == -1) {
                    if(usableHeightNow >= usableHeightSansKeyboard) {
                        offsetMargin = 0;
                    }else{
                        offsetMargin = mStatusBarHeight;
                    }
                }
                Logger.e("samuel1", "usableHeightSansKeyboard>>" + usableHeightSansKeyboard + "|||usableHeightNow>>>" + usableHeightNow + "|||offsetMargin>>>>>"+offsetMargin+"|||mStatusBarHeight>>>" + mStatusBarHeight);
                frameLayoutParams.height = usableHeightSansKeyboard;
                int heightDifference = usableHeightSansKeyboard - usableHeightNow - offsetMargin;
                Logger.e("samuel1", "heightDifference1>>" + heightDifference);
                if (heightDifference > (usableHeightSansKeyboard / 4)) {
                    // keyboard probably just became visible

                    mChildOfContent.setPadding(0,0,0,0);

                    Logger.e("samuel1", "heightDifference2>>" + heightDifference);
                    frameLayoutParams.topMargin = -heightDifference;
                    if (chatFragment != null) {
                        chatFragment.hideChatEdit(false,heightDifference);
                    }
                }  else {
                    if(heightDifference == mNavigationBarHeight){
                        mChildOfContent.setPadding(0,0,0,mNavigationBarHeight);
                    }else {
                        mChildOfContent.setPadding(0, 0, 0, 0);
                    }
                    // keyboard probably just became hidden
                    frameLayoutParams.topMargin = 0;
                    if (chatFragment != null) {
                        chatFragment.hideChatEdit(true,heightDifference);
                    }
                }
                mChildOfContent.requestLayout();
                usableHeightPrevious = usableHeightNow;
            }
        }

        private int computeUsableHeight() {
            Rect r = new Rect();
            mChildOfContent.getWindowVisibleDisplayFrame(r);
            return (r.bottom - r.top);
        }
    }


    public class AndroidBug5497Workaround1 {

        private View mChildOfContent;
        private View mChildOfContent1;
        private View mChildOfContent2;

        private int usableHeightPrevious;
        private RelativeLayout.LayoutParams frameLayoutParams;
        private RelativeLayout.LayoutParams frameLayoutParams1;
        private RelativeLayout.LayoutParams frameLayoutParams2;

        private int usableHeightSansKeyboard;

        private AndroidBug5497Workaround1(final Activity activity) {
            FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
            mChildOfContent = activity.findViewById(R.id.im_layout);
            mChildOfContent1 = activity.findViewById(R.id.player_layout);
            mChildOfContent2 = activity.findViewById(R.id.end_live_frame);


            content.getViewTreeObserver()
                    .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        public void onGlobalLayout() {
                            possiblyResizeChildOfContent(activity);
                        }
                    });
            frameLayoutParams = (RelativeLayout.LayoutParams) mChildOfContent.getLayoutParams();
            frameLayoutParams1 = (RelativeLayout.LayoutParams) mChildOfContent1.getLayoutParams();
            frameLayoutParams2 = (RelativeLayout.LayoutParams) mChildOfContent2.getLayoutParams();

        }

        private void possiblyResizeChildOfContent(final Context context) {
            int usableHeightNow = computeUsableHeight();
            if (usableHeightNow != usableHeightPrevious) {
                if (usableHeightSansKeyboard == 0) {
                    usableHeightSansKeyboard = usableHeightNow;
                }

                Logger.e("samuel2", "usableHeightSansKeyboard>>" + usableHeightSansKeyboard + "|||usableHeightNow>>>" + usableHeightNow + "|||mStatusBarHeight>>>" + mStatusBarHeight);
                frameLayoutParams.height = usableHeightSansKeyboard;
                int heightDifference = usableHeightSansKeyboard - usableHeightNow;
                if (heightDifference > (usableHeightSansKeyboard / 4)) {
                    // keyboard probably just became visible


                    frameLayoutParams.topMargin = -(heightDifference);
                    frameLayoutParams1.bottomMargin = -(heightDifference);
                    frameLayoutParams2.topMargin = -(heightDifference);
                    if (chatFragment != null) {
                        chatFragment.hideChatEdit(false,heightDifference);
                    }
                }

                else {
                    // keyboard probably just became hidden
                    frameLayoutParams.topMargin = 0;
                    frameLayoutParams1.bottomMargin = 0;
                    frameLayoutParams2.topMargin = 0;
                    if (chatFragment != null) {
                        chatFragment.hideChatEdit(true,heightDifference);
                    }
                    frameLayoutParams.height = usableHeightSansKeyboard;
                    frameLayoutParams1.height = usableHeightSansKeyboard;
                    frameLayoutParams2.height = usableHeightSansKeyboard;
                }
                mChildOfContent.requestLayout();
                mChildOfContent1.requestLayout();
                mChildOfContent2.requestLayout();
                usableHeightPrevious = usableHeightNow;
            }
        }

        private int computeUsableHeight() {
            Rect r = new Rect();
            mChildOfContent.getWindowVisibleDisplayFrame(r);
            return (r.bottom - r.top);
        }
    }

    @Override
    public void onEvent(int what) {
//    if (what == PlayEventListener.STATUS_FINISH) {
////      endPlay(null);//这里先注释掉，目前以消息通知为准，不已播放结束为准
//    } else {
//      //startPaly(what);
//    }
        mHandler.sendEmptyMessage(what);
    }

    /**
     * 开始播放，隐藏封面图
     */
    private void startPaly(int what) {
        mHandler.sendEmptyMessageDelayed(what, 200);
    }

    /**
     * 看播时播放结束
     */
    private void endPlay(POIMEnd imEnd) {
        //nodemedia推流，主播端押后台后不会中断音频流，为了不出现用户端无画面却有声音的情况，需要暂停播放
        ((PlayLive) playFragment).stopPlay();
        chatFragment.exit();

        endLiveLayout.removeAllViews();
        LookEndFragment endFragment = LookEndFragment.getInstance(bean, imEnd);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_with_alpha, R.anim.exit_with_alpha);
        transaction.remove(chatFragment);
//        transaction.remove(playFragment);
        transaction.add(R.id.end_live_frame, endFragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 直播时播放结束
     */
//  private void readlyEndPlay() {
//    chatFragment.exit();
//    endLiveLayout.removeAllViews();
//    PlayEndFragment endFragment = PlayEndFragment.getInstance(bean);
//    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//    transaction.setCustomAnimations(R.anim.enter_with_alpha, R.anim.exit_with_alpha);
////    transaction.remove(chatFragment);//因为需要半透明的效果所以就把这个注释掉了
//    transaction.remove(playFragment);
//    transaction.add(R.id.end_live_frame, endFragment);
//    transaction.commitAllowingStateLoss();
//  }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (bigAnimationLay != null) {
            bigAnimationLay.release();
        }
        mHandler.removeCallbacksAndMessages(null);
        wakeLock.release();
    }

    @Override
    public void onBackPressed() {
        if (!chatFragment.onBackPressed()) {
            onRoomEventListener.exit(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 进入直播间和退出直播间的打点统计
     * @param event   事件名称
     */

    public void daDian(String event){
        Map<String,String> maps = new HashMap<>();
        maps.put(POLaunch.M_event,event);
        maps.put(POLaunch.M_tn,String.valueOf(++POLaunch.actionNum));
        maps.put(POLaunch.M_pname,"live_play");
        maps.put(POLaunch.M_vid,bean.getVid());
        actionPresenter.actionUp(maps);
    }

}
