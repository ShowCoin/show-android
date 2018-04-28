package one.show.live.media.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.opengl.GLSurfaceView;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nodemedia.NodeCameraView;
import cn.nodemedia.NodePlayer;
import cn.nodemedia.NodePublisher;
import cn.nodemedia.NodePublisherDelegate;
import one.show.live.R;
import one.show.live.common.po.POLaunch;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BaseFragmentActivity;
import one.show.live.common.util.UserInfoCacheManager;
import one.show.live.common.view.CustomDialogView;
import one.show.live.log.Logger;
import one.show.live.media.listener.CountdownInterface;
import one.show.live.media.listener.ReadToLiveInterface;
import one.show.live.media.po.POIMEnd;
import one.show.live.media.po.POIMGift;
import one.show.live.media.po.POInitLive;
import one.show.live.media.po.POPublisherEnd;
import one.show.live.media.presenter.PublisherPresenter;
import one.show.live.media.view.PublisherView;
import one.show.live.po.POConfig;
import one.show.live.po.POFocus;
import one.show.live.po.POMaster;


/**
 * Created by Administrator on ..7/19 0019.
 */
public class PublisherActivity extends BaseFragmentActivity
        implements NodePublisherDelegate, PublisherView, CountdownInterface, ReadToLiveInterface {

    public static final String PUBLISHER_LIVEINFO_KEY = "publisher_liveinfo_key";

    @BindView(R.id.publisher_preview)
    NodeCameraView previewSurface;
    @BindView(R.id.countdown_fragment)
    FrameLayout countDownLayout;
    @BindView(R.id.end_live_frame)
    FrameLayout endLiveLayout;
    @BindView(R.id.readto_live_frame)
    FrameLayout readToLiveLayout;

    private boolean isStop, isClose, isStarting; //是否停止推流、是否关闭推流、是否开始推流
    private String publisherUrl, streamName;
    private long liveID, roomID;
    private POInitLive liveInfo;
    private POFocus live;
    private PublisherPresenter publisherPresenter;
    private ChatFragment chatFragment;
    private CountdownFragment countFragment;
    private ReadyToLiveFragment readyToLiveFragment;

    private NodePublisher np;

    @BindView(R.id.loading_layout)
    LinearLayout loadingLayout;
    @BindView(R.id.wait_image)
    ImageView loadingImage;
    private AnimationDrawable animationDrawable;

    private boolean isFlsOn = true;
    private boolean isSmoothSkinOn = true;
    private boolean isMicOn = true;
    private int mStatusBarHeight, mNavigationBarHeight;
    PowerManager powerManager = null;
    PowerManager.WakeLock wakeLock = null;
    private NetworkChangeReceiver networkChangeReceiver;

    private CustomDialogView closeDialog;

    private OnRoomEventListener publisherEvent =
            new OnRoomEventListener() {
                @Override
                public void exit(boolean isAsk) {
                    if (isAsk) {
                        closeDialog = new CustomDialogView.Builder(mActivity).setTitle("是否结束直播?")
                                .setLeftButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        closePublisher();
                                        dialog.dismiss();
                                        showLoading();
                                    }
                                })
                                .setRightButton("取消")
                                .create();
                        closeDialog.show();
                    }
                }

                @Override
                public void streamInterrupt() {

                }

                @Override
                public void liveStart() {

                }

                @Override
                public void lookLiveEnd(POIMEnd imEnd) {

                }

                @Override
                public void liveEnd(POIMEnd endObj) {
                    if (endObj.getReason() != 2) {
                        publisherEndSuccess(endObj);
                    }
                }

                @Override
                public void switchCamera(boolean isCamOn) {
                    np.switchCamera();
                    np.setFlashEnable(false);
                    isFlsOn = false;
                    if (chatFragment != null) {
                        chatFragment.closeFlash();
                    }
                }

                @Override
                public void switchFlash() {
                    int ret;
                    if (isFlsOn) {
                        ret = np.setFlashEnable(false);
                    } else {
                        ret = np.setFlashEnable(true);
                    }
                    if (ret == -1) {
                        // 无闪光灯,或处于前置摄像头,不支持闪光灯操作
                    } else if (ret == 0) {
                        // 闪光灯被关闭
                        chatFragment.closeFlash();
                        isFlsOn = false;
                    } else {
                        // 闪光灯被打开
                        chatFragment.openFlash();
                        isFlsOn = true;
                    }
                }

                @Override
                public void smoothSkin() {
                    if (isSmoothSkinOn) {
                        isSmoothSkinOn = false;
                        np.setBeautyLevel(0);
                        if (chatFragment != null) {
                            chatFragment.closeSmoothSkin();
                        }
                        if (readyToLiveFragment != null) {
                            readyToLiveFragment.closeSmoothSkin();
                        }

                    } else {
                        isSmoothSkinOn = true;
                        np.setBeautyLevel(5);
                        if (chatFragment != null) {
                            chatFragment.openSmoothSkin();
                        }
                        if (readyToLiveFragment != null) {
                            readyToLiveFragment.openSmoothSkin();
                        }
                    }
                }

                @Override
                public void switchMic() {
                    if (isStarting) {
                        isMicOn = !isMicOn;
                        np.setAudioEnable(isMicOn);
                        if (isMicOn) {
                            if (chatFragment != null) {
                                chatFragment.closeMic();
                            }
                        } else {
                            if (chatFragment != null) {
                                chatFragment.openMic();
                            }                        }
                    }

                }

                @Override
                public void showOrHideBigAnimation(boolean isShow) {

                }

                @Override
                public void addBigGift(POIMGift gift) {
                }
            };


    @Override
    public void onEventCallback(NodePublisher nodePublisher, int event, String msg) {
        Log.i("NodeMedia.NodePublisher", "EventCallback:" + event + " msg:" + msg);
        handler.sendEmptyMessage(event);
    }


    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 2000:
                    //ToastUtils.showLongToast(PublisherActivity.this, "正在发布视频");
                    break;
                case 2001:
                    //videoBtn.setBackgroundResource(R.drawable.ic_video_start);
                    //ToastUtils.showLongToast(PublisherActivity.this, "视频发布成功");
                    isStarting = true;
                    np.setAudioEnable(isMicOn);
                    publisherPresenter.dispatchVideoStream(streamName);
                    break;
                case 2002:
                    //ToastUtils.showLongToast(PublisherActivity.this, "视频发布失败");
                    break;
                case 2004:
                    //videoBtn.setBackgroundResource(R.drawable.ic_video_stop);
                    //ToastUtils.showLongToast(PublisherActivity.this, "视频发布结束");
                    isStarting = false;
                    break;
                case 2005:
                    //ToastUtils.showLongToast(PublisherActivity.this, "网络异常,发布中断");
                    breakPublisher();
                    break;
                case 2100:
                    //发布端网络阻塞，已缓冲了2秒的数据在队列中
                    //ToastUtils.showLongToast(PublisherActivity.this, "网络阻塞，发布卡顿");
                    break;
                case 2101:
                    //发布端网络恢复畅通
                    //ToastUtils.showLongToast(PublisherActivity.this, "网络恢复，发布流畅");
                    break;
                case 2102:
                    //ToastUtils.showLongToast(PublisherActivity.this, "截图保存成功");
                    break;
                case 2103:
                    //ToastUtils.showLongToast(PublisherActivity.this, "截图保存失败");
                    break;
                case 3100:
                    // mic off
                    //micBtn.setBackgroundResource(R.drawable.ic_mic_off);
                    //ToastUtils.showLongToast(PublisherActivity.this, "麦克风静音");
                    break;
                case 3101:
                    // mic on
                    //micBtn.setBackgroundResource(R.drawable.ic_mic_on);
                    //ToastUtils.showLongToast(PublisherActivity.this, "麦克风恢复");
                    break;
                case 3102:
                    // camera off
                    //camBtn.setBackgroundResource(R.drawable.ic_cam_off);
                    //ToastUtils.showLongToast(PublisherActivity.this, "摄像头传输关闭");
                    break;
                case 3103:
                    // camera on
                    //camBtn.setBackgroundResource(R.drawable.ic_cam_on);
                    //ToastUtils.showLongToast(PublisherActivity.this, "摄像头传输打开");
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    /**
     * 传参数的
     *
     * @param context
     * @param initLive
     * @return
     */
    public static Intent getCallingIntent(Context context, POInitLive initLive) {
        Intent intent = new Intent(context, PublisherActivity.class);
        intent.putExtra(PUBLISHER_LIVEINFO_KEY, initLive);
        return intent;
    }

    /**
     * 不传参数的
     *
     * @param context
     * @return
     */
    public static Intent getCallingIntent(Context context) {
        Intent intent = new Intent(context, PublisherActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");


        registReceiver();
        initWindow();
        new AndroidBug5497Workaround(this);
        ButterKnife.bind(this);
        initIntentData();
        initPresenter();
        initPublisher();
    }

    private void registReceiver() {
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, filter);
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
        return R.layout.activity_publisher;
    }

    private void initIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            liveInfo = (POInitLive) intent.getSerializableExtra(PUBLISHER_LIVEINFO_KEY);
            if (liveInfo != null) {
                publisherUrl = liveInfo.getRtmp();
                streamName = liveInfo.getStreamName();
                liveID = liveInfo.getLiveId();
                roomID = liveInfo.getRoomId();
                initView(liveInfo);
            } else {

                readyToLiveFragment = ReadyToLiveFragment.newInstance();
                readyToLiveFragment.setOnRoomEventListener(publisherEvent);
                FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
                transaction2.replace(R.id.readto_live_frame, readyToLiveFragment);
                transaction2.commitAllowingStateLoss();

            }

        }
    }

    private void initView(POInitLive poInitLive) {
        live = new POFocus();
        POMember me = POMember.getInstance();
        POMaster masterBean = new POMaster();
        masterBean.setAvatar(me.getAvatar());
        masterBean.setFan_level(me.getFanLevel());
        masterBean.setGender(me.getGender());
        masterBean.setMaster_level(me.getMasterLevel());
        masterBean.setNickname(me.getNickname());
        //masterBean.setPid(me.getU);
//        masterBean.setDescription(me.getDescription());
        masterBean.setUid(me.getUid());
        live.setMaster(masterBean);
        live.setIs_live(1);
        live.setReceive(me.getReceive());
        live.setFollow_state("1");
        live.setId(liveID);
        live.setIs_vip(me.getIs_vip());
        live.setRoom_admin(1);
        live.setRoom_id(roomID);
        live.setShare_addr(poInitLive.getShare_addr());
        addChartAndCount(live);
    }


    public void addChartAndCount(POFocus live) {
        countFragment = CountdownFragment.getCallingFragemnt();
        FragmentTransaction transaction2 = getSupportFragmentManager().beginTransaction();
        transaction2.replace(R.id.countdown_fragment, countFragment);
        transaction2.commitAllowingStateLoss();

        chatFragment = ChatFragment.getInstance(ChatFragment.IMCLIENT_TYPE_RECORD, live);
        chatFragment.setOnRoomEventListener(publisherEvent);
        chatFragment.setSmoothSkinSwithcer(isSmoothSkinOn);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.chat_fragment, chatFragment);
        transaction.commitAllowingStateLoss();
    }

    private void initPresenter() {
        publisherPresenter = new PublisherPresenter(this);
    }

    private void initPublisher() {

        np = new NodePublisher(this);
        np.setNodePublisherDelegate(this);
        np.setCameraPreview(previewSurface, NodePublisher.CAMERA_FRONT, true);
        np.setAudioParam(32 * 1000, NodePublisher.AUDIO_PROFILE_HEAAC);
        np.setVideoParam(NodePublisher.VIDEO_PPRESET_16X9_360, 24, 500 * 1000, NodePublisher.VIDEO_PROFILE_MAIN, false);
        np.setDenoiseEnable(true);
        np.setBeautyLevel(5);
        np.setOutputUrl(publisherUrl);


        /**
         * @brief rtmpdump 风格的connect参数
         * Append arbitrary AMF data to the Connect message. The type must be B for Boolean, N for number, S for string, O for object, or Z for null.
         * For Booleans the data must be either 0 or 1 for FALSE or TRUE, respectively. Likewise for Objects the data must be 0 or 1 to end or begin an object, respectively.
         * Data items in subobjects may be named, by prefixing the type with 'N' and specifying the name before the value, e.g. NB:myFlag:1.
         * This option may be used multiple times to construct arbitrary AMF sequences. E.g.
         */
        np.setConnArgs("S:info O:1 NS:uid:10012 NB:vip:1 NN:num:209.12 O:0");
        np.startPreview();

    }

    private void startPublisher() {
        int ret = np.start();
        Log.e("NP","start ret :" +ret);
    }


    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        restartPublisher();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
        handler.removeCallbacksAndMessages(null);
        wakeLock.release();
        np.stopPreview();
        np.stop();
        np.release();
    }

    @Override
    public void dispatchStreamSuccess() {
        Logger.e("lishizhong", "dispatchStreamSuccess");
        UserInfoCacheManager.getInstance().setValue("PUBLISHER_INFO", new Gson().toJson(liveInfo));
    }

    @Override
    public void dispatchStreamFailed(String msg) {
        Logger.e("lishizhong", "dispatchStreamFailed: " + msg);
    }

    @Override
    public void breakSuccess() {
        Logger.e("lishizhong", "breakSuccess");
    }

    @Override
    public void breakFailed(String msg) {
        Logger.e("lishizhong", "breakFailed: " + msg);
    }

    @Override
    public void publisherEndSuccess(POPublisherEnd data) {
        Logger.e("samuel", "publisherEndSuccess");
        publisherEndSuccess(data.convertToPOIMEnd());
        //finish();
    }


    public void publisherEndSuccess(POIMEnd data) {
        Logger.e("samuel", "publisherEndSuccess");
        hideLoading();
        closePublisherCount = 0;
        UserInfoCacheManager.getInstance().remove("PUBLISHER_INFO");
        isClose = true;
        np.stopPreview();
        np.stop();
        data.setLiveID(liveInfo.getLiveId());
        readlyEndPlay(data);
        //finish();
    }


    int closePublisherCount = 0;

    @Override
    public void publisherEndFailed(String msg) {
        Logger.e("samuel", "publisherEndFailed: " + msg);
        if (closePublisherCount < 2) {
            closePublisherCount++;
            closePublisher();
        } else {
            hideLoading();
            closeDialog.show();
        }
    }

    /**
     * 中断直播推流
     */
    private void breakPublisher() {
        if (isStarting&&np!=null) {
            isStop = true;
            publisherPresenter.breakVideoStream(streamName);
            np.stop();
        }

    }

    /**
     * 重新开始推流
     */
    private void restartPublisher() {
        if (isStop && !isClose && isStarting && np !=null) {
            publisherPresenter.dispatchVideoStream(streamName);
            np.start();
        }
    }

    /**
     * 关闭推流
     */
    private void closePublisher() {
        publisherPresenter.closePublisher(String.valueOf(liveID));
    }

    /**
     * 关闭倒计时
     */
    @Override
    public void countdownclose() {
        //ToastUtils.showToast("关闭倒计时啦");
        if (countDownLayout != null && countDownLayout.getVisibility() == View.VISIBLE) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(countFragment);
            fragmentTransaction.commitAllowingStateLoss();
            countDownLayout.setVisibility(View.GONE);
        }
        startPublisher();
    }

    /**
     * 直播时播放结束
     */
    private void readlyEndPlay(POIMEnd data) {
        chatFragment.exit();
        endLiveLayout.removeAllViews();
        PlayEndFragment endFragment = PlayEndFragment.getInstance(data, live);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_with_alpha, R.anim.exit_with_alpha);
//        transaction.remove(chatFragment);//因为需要半透明的效果所以就把这个注释掉了
        //transaction.remove(playFragment);
        transaction.add(R.id.end_live_frame, endFragment);
        transaction.commitAllowingStateLoss();
    }

    @Override
    public void onBackPressed() {
        if (chatFragment != null) {
            chatFragment.onBackPressed();
            return;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        breakPublisher();
    }


    /**
     * 点击开始直播
     */
    @Override
    public void readToLiveEnd(POInitLive initLive) {

        if (readToLiveLayout != null && readToLiveLayout.getVisibility() == View.VISIBLE) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(readyToLiveFragment);
            fragmentTransaction.commitAllowingStateLoss();
            readToLiveLayout.setVisibility(View.GONE);
        }

        if (initLive != null) {
            liveInfo = initLive;
            publisherUrl = initLive.getRtmp();
            streamName = initLive.getStreamName();
            liveID = initLive.getLiveId();
            roomID = initLive.getRoomId();
        }

        initView(initLive);
    }

    private void showLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
        animationDrawable = (AnimationDrawable) loadingImage.getDrawable();
        animationDrawable.start();
    }

    private void hideLoading() {
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
        loadingLayout.setVisibility(View.GONE);
    }


    public class AndroidBug5497Workaround {

        private View mChildOfContent;
        private int usableHeightPrevious;
        private RelativeLayout.LayoutParams frameLayoutParams;
        private int offsetMargin = -1;

        private AndroidBug5497Workaround(final Activity activity) {
            FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
            mChildOfContent = activity.findViewById(R.id.chat_fragment);
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
                    if (usableHeightNow >= usableHeightSansKeyboard) {
                        offsetMargin = 0;
                    } else {
                        offsetMargin = mStatusBarHeight;
                    }
                }
                Logger.e("samuel", "usableHeightSansKeyboard>>" + usableHeightSansKeyboard + "|||usableHeightNow>>>" + usableHeightNow + "|||offsetMargin>>>>>" + offsetMargin + "|||mStatusBarHeight>>>" + mStatusBarHeight);
                frameLayoutParams.height = usableHeightSansKeyboard;
                int heightDifference = usableHeightSansKeyboard - usableHeightNow - offsetMargin;
                Logger.e("samuel", "heightDifference1>>" + heightDifference);
                if (heightDifference > (usableHeightSansKeyboard / 4)) {
                    // keyboard probably just became visible

                    mChildOfContent.setPadding(0, 0, 0, 0);

                    Logger.e("samuel", "heightDifference2>>" + heightDifference);
                    frameLayoutParams.topMargin = -heightDifference;
                    if (chatFragment != null) {
                        chatFragment.hideChatEdit(false, heightDifference);
                    }
                } else {
                    if (heightDifference == mNavigationBarHeight) {
                        mChildOfContent.setPadding(0, 0, 0, mNavigationBarHeight);
                    } else {
                        mChildOfContent.setPadding(0, 0, 0, 0);
                    }
                    // keyboard probably just became hidden
                    frameLayoutParams.topMargin = 0;
                    if (chatFragment != null) {
                        chatFragment.hideChatEdit(true, heightDifference);
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

    /////////////监听网络状态变化的广播接收器

    private class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                ConnectivityManager mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {
                    restartPublisher();
                } else {
                    ////////网络断开

                }
            }

        }
    }

}
