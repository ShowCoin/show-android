package one.show.live.live.play.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongContext;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.TextMessage;
import one.show.live.R;
import one.show.live.po.POEventBus;
import one.show.live.po.POLaunch;
import one.show.live.po.POLive;
import one.show.live.po.POMember;
import one.show.live.ui.BaseFragment;
import one.show.live.util.ConvertToUtils;
import one.show.live.util.DeviceUtils;
import one.show.live.util.StringUtils;
import one.show.live.util.WeakHandler;
import one.show.live.view.ToastUtils;
import one.show.live.conversation.ui.ConversationFragment;
import one.show.live.home.ui.ConversListFragment;
import one.show.live.live.adapter.ChatMsgAdapter;
import one.show.live.live.adapter.UserAdapter;
import one.show.live.live.gift.dialog.SendGiftsView;
import one.show.live.live.listener.ChatEventListener;
import one.show.live.live.listener.UserInfoListener;
import one.show.live.live.model.DanmaRequest;
import one.show.live.live.model.GetLiveUserListRequest;
import one.show.live.live.model.GoodLikeRequest;
import one.show.live.live.model.LiveStatusRequest;
import one.show.live.live.model.PlayStatisticsRequest;
import one.show.live.live.po.POAttendanchor;
import one.show.live.live.po.POGift;
import one.show.live.live.po.POIMAdmin;
import one.show.live.live.po.POIMContent;
import one.show.live.live.po.POIMDanmu;
import one.show.live.live.po.POIMEnd;
import one.show.live.live.po.POIMGift;
import one.show.live.live.po.POIMLevel;
import one.show.live.live.po.POIMLike;
import one.show.live.live.po.POIMMsg;
import one.show.live.live.po.POIMMsgList;
import one.show.live.live.po.POIMMsgObj;
import one.show.live.live.po.POIMMsgUserList;
import one.show.live.live.po.POIMPermission;
import one.show.live.live.po.POIMRank;
import one.show.live.live.po.POIMTalkMsg;
import one.show.live.live.po.POIMTalkMsgExtra;
import one.show.live.live.po.POIMUser;
import one.show.live.live.po.POIMUserList;
import one.show.live.live.po.POIMWelcome;
import one.show.live.live.po.POLiveStatus;
import one.show.live.live.util.IMClientUtils;
import one.show.live.live.util.LiveUiUtils;
import one.show.live.live.widget.AnimBatterContainer;
import one.show.live.live.widget.FlyHeartView;
import one.show.live.live.widget.InfoHeader;
import one.show.live.live.widget.UserInfoDialog;
import one.show.live.live.widget.danmu.DanmuBase.DanmakuActionManager;
import one.show.live.live.widget.danmu.DanmuBase.DanmakuChannel;
import one.show.live.log.Logger;
import one.show.live.personal.ui.AdministratorListActivity;
import one.show.live.personal.ui.OthersDetailsActivity;
import one.show.live.po.POConfig;
import one.show.live.po.eventbus.EBOJoinRoomCallBack;
import one.show.live.po.eventbus.EBORefreshBeike;
import one.show.live.share.ui.ShareDialog;
import one.show.live.util.EventBusKey;
import one.show.live.util.NoDoubleClickListener;

public class ChatFragment extends BaseFragment implements ChatEventListener, View.OnClickListener {

    /**
     * 主播界面
     */
    public static final int IMCLIENT_TYPE_RECORD = 0x01;
    /**
     * 观众界面
     */
    public static final int IMCLIENT_TYPE_PLAY = 0x01 << 1;

    private int type;
    @BindView(R.id.user_list_view)
    RecyclerView userListView;
    @BindView(R.id.message_list_view)
    RecyclerView msgListView;
    @BindView(R.id.menu_layout)
    View menuLayout;
    @BindView(R.id.send_msg_layout)
    View editLayout;
    @BindView(R.id.edit_chat)
    EditText chatEdit;
    @BindView(R.id.batter_anim_view)
    AnimBatterContainer batterAnim;
    @BindView(R.id.floating_heart_view)
    FlyHeartView heartView;
    @BindView(R.id.danmu)
    TextView danmu;
    @BindView(R.id.btn_send)
    TextView sendBtn;
    @BindView(R.id.headerLay)
    View headerLay;
    @BindView(R.id.live_headerview)
    InfoHeader liveHeaderView;
    @BindView(R.id.close_btn)
    ImageButton closeBtn;

    @BindView(R.id.user_money_lay)
    View userMoneyLay;
    @BindView(R.id.user_money)
    TextView userMoney;

    @BindView(R.id.live_record_menu)
    View liveRecordMenu;
    @BindView(R.id.live_record_flash)
    View liveRecordFlash;
    @BindView(R.id.live_record_smoothskin)
    View liveRecordSmoothSkin;
    @BindView(R.id.live_record_switchcamera)
    View liveRecordSWCamera;
    @BindView(R.id.live_record_share)
    View liveRecordShare;
    @BindView(R.id.live_record_message)
    View liveRecordMessage;
    @BindView(R.id.live_record_mic)
    View liveRecordMic;

    @BindView(R.id.live_record_chat)
    View liveRecordChat;

    @BindView(R.id.more_menu_layout)
    LinearLayout moreMenuLayout;
    @BindView(R.id.normal_menu_layout)
    LinearLayout normalMenuLayout;
    @BindView(R.id.record_menu)
    View recordMenu;


    @BindView(R.id.live_play_menu)
    View livePlayMenu;
    @BindView(R.id.live_play_chat)
    View livePlayChat;
    @BindView(R.id.live_play_clear)
    View livePlayClear;
    @BindView(R.id.live_play_share)
    View livePlayShare;
    @BindView(R.id.live_play_message)
    View livePlayMessage;

    @BindView(R.id.live_play_present)
    View livePlayPresent;
    @BindView(R.id.privateLetterLay)
    FrameLayout privateLetterLay;

    @BindView(R.id.conversLay)
    FrameLayout conversLay;
    @BindView(R.id.danA)
    DanmakuChannel danA;
    @BindView(R.id.danB)
    DanmakuChannel danB;

    @BindView(R.id.danC)
    DanmakuChannel danC;
    @BindView(R.id.red_tips)
    ImageView redTips;
    @BindView(R.id.message_layout)
    FrameLayout messageLayout;

    @BindView(R.id.record_message_layout)
    FrameLayout recordMessageLayout;

    @BindView(R.id.record_red_tips)
    ImageView recordRedTips;
    private boolean smoothSkinON;
    private ShareDialog mShareDialog;

    LiveShowActivity.OnRoomEventListener onRoomEventListener;
    private boolean isCamOn = true;

    public void setOnRoomEventListener(LiveShowActivity.OnRoomEventListener listener) {
        this.onRoomEventListener = listener;
    }

    public void setSmoothSkinSwithcer(boolean smoothSkinOn) {
        this.smoothSkinON = smoothSkinOn;
    }

    public void closeFlash() {
        // 闪光灯被关闭
        liveRecordFlash.setBackgroundResource(R.drawable.live_flash_off);
    }

    public void openFlash() {
        // 闪光灯被打开
        liveRecordFlash.setBackgroundResource(R.drawable.live_flash_on);
    }

    public void closeSmoothSkin() {
        // 关闭美颜
        liveRecordSmoothSkin.setBackgroundResource(R.drawable.smoothskin_off);
    }

    public void openSmoothSkin() {
        // 打开美颜
        liveRecordSmoothSkin.setBackgroundResource(R.drawable.smoothskin_on);
    }

    // 关闭mic
    public void closeMic() {
        liveRecordMic.setBackgroundResource(R.drawable.record_mic_off);
    }

    // 打开mic
    public void openMic() {
        liveRecordMic.setBackgroundResource(R.drawable.record_mic_on);
    }

    @OnClick(R.id.live_record_flash)
    protected void switchFlash() {
        onRoomEventListener.switchFlash();
    }

    @OnClick(R.id.live_record_mic)
    protected void switchMic() {
        onRoomEventListener.switchMic();
    }

    @OnClick(R.id.live_record_switchcamera)
    protected void switchCamera() {
        isCamOn = !isCamOn;
        onRoomEventListener.switchCamera(isCamOn);
    }

    @OnClick(R.id.live_record_smoothskin)
    protected void switchSmoothSkin() {
        onRoomEventListener.smoothSkin();
    }

    @OnClick({R.id.live_play_share, R.id.live_record_share})
    protected void showShareDialog() {
        if(liveBean.getVid()!=null){//不为空的话就是看播
            daDian("live_share_init_event","");//唤起分享页面的打点事件
        }
        mShareDialog = ShareDialog.newInstance(liveBean);
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.add(mShareDialog, null);
        ft.commitAllowingStateLoss();
    }

    @OnClick(R.id.close_btn)
    protected void clickClose() {
        if (liveBean.getMaster().getUid().equals(POMember.getInstance().getBeke_userid())) { //如果主播的id与当前用户id相同，则认为当前直播间为用户直播
            onRoomEventListener.exit(true);
        } else {
            onRoomEventListener.exit(false);
        }
    }

    @OnClick(R.id.record_menu)
    protected void onClick() {
        if (normalMenuLayout.getVisibility() == View.VISIBLE) {
            recordMenu.setBackgroundResource(R.drawable.close_more);
            normalMenuLayout.setVisibility(View.GONE);
            moreMenuLayout.setVisibility(View.VISIBLE);
        } else {
            recordMenu.setBackgroundResource(R.drawable.open_more);
            normalMenuLayout.setVisibility(View.VISIBLE);
            moreMenuLayout.setVisibility(View.GONE);
        }
    }


    private boolean isCleanMode;

    @OnClick(R.id.live_play_clear)
    protected void clickClear() {
        showOrHideView(msgListView, isCleanMode);
        showOrHideView(livePlayChat, isCleanMode);
        showOrHideView(livePlayShare, isCleanMode);
        //showOrHideView(livePlayMessage, isCleanMode);
        showOrHideView(livePlayPresent, isCleanMode);
        showOrHideView(userMoneyLay, isCleanMode);
        showOrHideView(userListView, isCleanMode);
        showOrHideView(liveHeaderView, isCleanMode);
        showOrHideView(batterAnim, isCleanMode);
        if (onRoomEventListener != null) {
            onRoomEventListener.showOrHideBigAnimation(isCleanMode);
        }
        showOrHideView(heartView, isCleanMode);
        showOrHideView(danA, isCleanMode);
        showOrHideView(danB, isCleanMode);
        showOrHideView(messageLayout, isCleanMode);
        showOrHideView(danC, isCleanMode);

        isCleanMode = !isCleanMode;
    }

    private void showOrHideView(View view, boolean isShow) {
        if (view != null) {
            view.setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    public void exit() {
        try {
            if (mUserDialog != null) {
                mUserDialog.mDismissDialog();
            }
            if (mShareDialog != null) {
                mShareDialog.dismissAllowingStateLoss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        IMClientUtils.quitChatRoom(liveBean.getRoom_id());
    }

    private POMember me;

    private DanmakuActionManager danmakuActionManager;

    private POLive liveBean;

    private UserAdapter userAdapter;
    private ChatMsgAdapter msgAdapter;

    private UserInfoListener userInfoListener;


    private UserInfoDialog mUserDialog;

    private long lastClickTime;

    private long startTime;

    private boolean isAllreadyLike;

    private WeakHandler mHandler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (null != msg) {
                checkUserInfo((String) msg.obj);
            }
            return true;
        }
    });

    public static ChatFragment getInstance(int type, POLive bean) {
        ChatFragment fragment = new ChatFragment();
        fragment.type = type;
        fragment.liveBean = bean;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RongContext.getInstance().getEventBus().register(this);
        //EventBus.builder().build();
    }

    @Override
    protected int getContentView() {
        return R.layout.im_fragment;
    }

    private boolean isLive() {
        if (isRecord()) return true;
        return liveBean.isLive();
    }

    private boolean isRecord() {
        return type == IMCLIENT_TYPE_RECORD;
    }

    @Override
    protected void initView() {
        if(liveBean==null)
            return;
        EventBus.getDefault().register(this);
        EBORefreshBeike.removeRefreshBeikePraiseEvent();
        me = POMember.getInstance();
        userAdapter = new UserAdapter(mHandler);
        userListView.setAdapter(userAdapter);
        userListView.setLayoutManager(
                new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

        msgAdapter = new ChatMsgAdapter(activity);
        msgAdapter.setOnItemClickListener(new ChatMsgAdapter.OnItemClickListener() {
            @Override
            public void onClickNickName(String uid) {
                checkUserInfo(uid);
            }

            @Override
            public void onLongClickNickName(String uid, String userName) {
                if (!uid.equals(me.getBeke_userid()))
                    replyUser(userName);
            }
        });
        msgListView.setNestedScrollingEnabled(false);
        msgListView.setAdapter(msgAdapter);
        msgListView.setLayoutManager(
                new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, true));

        danmakuActionManager = new DanmakuActionManager();
        danA.setDanAction(danmakuActionManager);
        danB.setDanAction(danmakuActionManager);
        danC.setDanAction(danmakuActionManager);
        danA.setOnClickListener(mDanmakuClickListener);
        danB.setOnClickListener(mDanmakuClickListener);
        danC.setOnClickListener(mDanmakuClickListener);
        danmakuActionManager.addChannel(danA);
        danmakuActionManager.addChannel(danB);
        danmakuActionManager.addChannel(danC);

        if (isRecord()) {
            initRecord();
        } else {
            initPlay();
        }

        if (isLive()) {
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (conversLay != null
                            && conversLay.getVisibility() == View.VISIBLE) {  //会话界面在会话列表界面上面，因此需要先判断会话界面
                        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        fragmentTransaction.remove(conversFragment);
                        fragmentTransaction.commitAllowingStateLoss();
                        conversLay.setVisibility(View.GONE);
                        return;
                    }

                    if (privateLetterLay != null && privateLetterLay.getVisibility() == View.VISIBLE) {
                        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        fragmentTransaction.remove(privateLetterFragement);
                        fragmentTransaction.commitAllowingStateLoss();
                        privateLetterLay.setVisibility(View.GONE);
                        return;
                    }

                    if (isRecord())
                        return;
                    if (!isAllreadyLike) {
                        Map<String, String> params = new HashMap<>();
                        params.put("vid", liveBean.getId());
                        params.put("roomId", liveBean.getRoom_id());
                        new GoodLikeRequest().startRequest(params);//点赞
                        daDian("live_like_btn_event","");//点赞的打点
                        isAllreadyLike = true;
                    } else {
                        addHeart(true);
                    }
                }
            });

            chatEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_SEND) {
                        long currentTime = Calendar.getInstance().getTimeInMillis();
                        if (currentTime - lastClickTime > NoDoubleClickListener.MIN_CLICK_DELAY_TIME) {
                            lastClickTime = currentTime;
                            sendMessage();
                        }

                    }
                    return true;
                }
            });

            initImClient();
            initUserList();
            if (!isRecord()) {
                getLiveStatus();
            }

        }

        sendBtn.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                sendMessage();
            }
        });

        initHeader();
        if (liveBean != null && liveBean.isLive()) {
            userMoneyLay.setVisibility(View.VISIBLE);
        } else {
            userMoneyLay.setVisibility(View.GONE);
        }

        if (smoothSkinON) {
            liveRecordSmoothSkin.setBackgroundResource(R.drawable.smoothskin_on);
        } else {
            liveRecordSmoothSkin.setBackgroundResource(R.drawable.smoothskin_off);
        }

        liveRecordMic.setBackgroundResource(R.drawable.record_mic_on);
    }


    /**
     * 异步获取直播状态
     */
    private void getLiveStatus() {
        if (liveBean != null && liveBean.getMaster() != null) {
            Map<String, String> params = new HashMap<>();
            params.put("uid", liveBean.getMaster().getUid());
            params.put("liveid", liveBean.getId());
            new LiveStatusRequest() {
                @Override
                public void onFinish(boolean isSuccess, String msg, POLiveStatus data) {
                    if (isSuccess && onRoomEventListener != null) {
                        if (data.isLiveClose()) {
                            if (type == IMCLIENT_TYPE_PLAY) {//如果是观众页面
                                POIMEnd poimEnd = new POIMEnd();
                                poimEnd.setLiked(data.getLiked());
                                poimEnd.setReason(data.getReason());
                                poimEnd.setReceive(data.getReceive());
                                poimEnd.setViewed(data.getViewed());
                                poimEnd.setUid(data.getUid());
                                onRoomEventListener.lookLiveEnd(poimEnd);
                            }
                            //主播界面不会调这个方法
//                            else {//如果是主播页面
//                                onRoomEventListener.liveEnd();
//                            }

                        } else if (data.isLiveSteamStop()) {
                            onRoomEventListener.streamInterrupt();
                        }
                    }
                }
            }.startRequest(params);
        }
    }

    private void addHeart(boolean isLocal) {
        if (heartView != null) {
            if (!isCleanMode) heartView.addHeart(isLocal);
        }
    }

    private DanmakuChannel.OnClickListener mDanmakuClickListener =
            new DanmakuChannel.OnClickListener() {
                @Override
                public void onClickHead(POIMDanmu entity) {
                    checkUserInfo(entity.getUid());
                }
            };

    private SendGiftsView sendGiftsView;

    @OnClick(R.id.danmu)
    public void selectDanma() {
        if (!danmu.isSelected()) {
            danmu.setSelected(true);
            chatEdit.setHint("1贝壳一条");
        } else {
            danmu.setSelected(false);
            chatEdit.setHint("说点什么吧");
        }
    }

    @OnClick(R.id.live_play_present)//点击礼物列表弹出的按钮
    public void showGift() {
        daDian("live_gift_init_event","");//礼物列表的事件打点
        hideViewsForGift(true);
        if (sendGiftsView != null) {
            sendGiftsView.setVisibility(View.VISIBLE);
            sendGiftsView.getZhenzhu();
            return;
        }
        sendGiftsView = new SendGiftsView(activity, liveBean, new SendGiftsView.SendGiftListener() {
            @Override
            public void onSendGift(POGift giftBean) {

            }
        });

        sendGiftsView.setVisibilityChangedListener(new SendGiftsView.OnVisibilityChangedListener() {
            @Override
            public void onVisibilityChanged(int visibility) {
                if (visibility == View.GONE) {
                    hideViewsForGift(false);
                }
            }
        });

        sendGiftsView.setTargetId(liveBean.getMaster().getUid());

        ((ViewGroup) rootView).addView(sendGiftsView);
        sendGiftsView.setVisibility(View.VISIBLE);
    }

    private void hideViewsForGift(boolean isHide) {
        hideChat(isHide);
    }

    private void initImClient() {
        IMClientUtils.checkRoomConect(liveBean.getRoom_id());
    }




    /**
     * 播放统计
     */
    private void playStatistics(int type) {
        Map<String, String> params = new HashMap<>();
        params.put("vid", liveBean.getId());
        params.put("live", type + "");//1直播，0回放
        params.put("roomId", liveBean.getRoom_id());
        new PlayStatisticsRequest().startRequest(params);
    }

    private void initUserList() {
        if (liveBean != null) {
            //首次进入获取用户列表
            new GetLiveUserListRequest() {
                @Override
                public void onFinish(boolean isSuccess, String msg, POIMUserList<POIMUser> data) {
                    if (isSuccess && data.getUserList() != null && data.getUserList().size() > 0) {
                        userAdapter.setData(data.getUserList());
                        userAdapter.notifyDataSetChanged();
                        liveHeaderView.setOnline(data.getTotal());
                    }
                }
            }.getData(liveBean.getRoom_id());
        }
    }

    /**
     * 当前收益
     */
    private long currentReceive = 0;

    private void initHeader() {

        int padding = ConvertToUtils.dipToPX(activity, 10);
        if (DeviceUtils.hasKitkat()) {
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();
            headerLay.setPadding(0, config.getPixelInsetTop(false) + padding, 0, 0);
        } else {
            headerLay.setPadding(0, padding, 0, 0);
        }

        if (liveBean != null) {
            if (liveBean.getMaster() != null) {
                liveHeaderView.setName(isLive() ? "直播Live" : "回放");
                liveHeaderView.setVid(liveBean.getVid());//设置房间的vid
                liveHeaderView.setMemberId(liveBean.getMaster().getUid());
                liveHeaderView.setAvatar(liveBean.getMaster().getAvatar(), true);
                liveHeaderView.setOnline(liveBean.getOnline_users());
                liveHeaderView.showFollowBtn(!liveBean.isFollow());
                liveHeaderView.setIsVip(liveBean.isVip());
                liveHeaderView.setOnInfoClickListener(new InfoHeader.OnInfoClickListener() {
                    @Override
                    public void clickHeader() {
                        checkUserInfo(liveBean.getMaster().getUid());
                    }
                });

            }
            if (isLive()) {
                setReceive(liveBean.getReceive());
                liveHeaderView.showOnlineNum();
            } else {
                liveHeaderView.hideOnlineNum();
            }
        }
    }

    private void setReceive(long receive) {
        currentReceive += receive;
        userMoney.setText(String.valueOf(currentReceive));
    }

    @OnClick(R.id.user_money_lay)
    protected void gotoRankList() {
        startActivity(OthersDetailsActivity.getCallingIntengData(activity, liveBean.getMaster().getUid(),
                OthersDetailsActivity.WATCH, 1));//最后面传一个1证明是直播间跳转过去的
    }

    private void initRecord() {
        liveRecordMenu.setVisibility(View.VISIBLE);
        livePlayMenu.setVisibility(View.GONE);
    }

    private void initPlay() {
        if (isLive()) {
            liveRecordMenu.setVisibility(View.GONE);
            livePlayMenu.setVisibility(View.VISIBLE);
            userListView.setVisibility(View.VISIBLE);
        } else {
            liveRecordMenu.setVisibility(View.GONE);
            livePlayMenu.setVisibility(View.GONE);
            userListView.setVisibility(View.GONE);
            heartView.setVisibility(View.GONE);
            batterAnim.setVisibility(View.GONE);
            if (onRoomEventListener != null) {
                onRoomEventListener.showOrHideBigAnimation(false);
            }
            msgListView.setVisibility(View.GONE);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onJoinRoomCallBack(EBOJoinRoomCallBack callBack){
        if(!isContextAlive() || callBack == null || StringUtils.isEmpty(callBack.roomId) || liveBean == null || StringUtils.isEmpty(liveBean.getRoom_id()))
            return;

        if(!callBack.roomId.equals(liveBean.getRoom_id()))
            return;

        if(callBack.isSuccess) {
            List<String> noticeList = POConfig.getInstance().getNoticeList();
            if (noticeList != null && noticeList.size() > 0) {
                for (String notice : noticeList) {
                    addMsg(POIMTalkMsg.buildSystemMsg(notice));
                }
            }
            playStatistics(1);
            Logger.e("t-tag", "RongIMClient--onSuccess");
        }else {
            //这里处理禁言或者踢出房间的处理
            Logger.i("t-tag", "RongIMClient--onError: " + callBack.errCode);
            switch (callBack.errCode) {
                case KICKED_FROM_CHATROOM://踢出房间
                    ToastUtils.showToast("你已被踢出房间");
                    if (onRoomEventListener != null) {
                        onRoomEventListener.exit(false);
                    }
                    break;
                case RC_DISCONN_KICK://用户自己互踢
                    ToastUtils.showToast("你的账号在别的地方已经登录");
                    if (onRoomEventListener != null) {
                        onRoomEventListener.exit(false);
                    }
                    break;
            }
        }
    }

    private String rankFirstUid = "";

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCustomMsgCallBack(POIMMsg customMsg) {//自定义消息回调
        if (customMsg == null || liveBean == null || customMsg.getType() == POIMMsg.MsgType.UNKNOW.type || !isLive()
                || !isContextAlive()) {
            return;
        }
        if (StringUtils.isEmpty(customMsg.getRoomId()) || !customMsg.getRoomId().equals(liveBean.getRoom_id())) {
            return;
        }

        if (customMsg.getType() == POIMMsg.MsgType.STARTLIVE.type) {
            if (onRoomEventListener != null) {
                onRoomEventListener.liveStart();
            }
        } else if (customMsg.getType() == POIMMsg.MsgType.ENDLIVE.type) {
            //获取到直播结束
            if (onRoomEventListener != null) {
                POIMEnd imEnd = ((POIMMsgObj<POIMEnd>) customMsg).getData();
                if (type == IMCLIENT_TYPE_PLAY) {//如果是观众结束页面
                    onRoomEventListener.lookLiveEnd(imEnd);
                } else {//如果是主播结束页面
                    onRoomEventListener.liveEnd(imEnd);
                }
            }

        } else if (customMsg.getType() == POIMMsg.MsgType.GIFT.type) {
            POIMGift gift = ((POIMMsgObj<POIMGift>) customMsg).getData();
            setReceive(gift.getReceive());
            if (gift.getGiftType() != 1) {
                if (onRoomEventListener != null) {
                    onRoomEventListener.addBigGift(gift);
                }
            } else {
                batterAnim.addGift(gift);
            }
            addMsg(POIMTalkMsg.buildSendGiftMsg(gift, POIMMsg.MsgType.GIFT));
        } else if (customMsg.getType() == POIMMsg.MsgType.LEVEL.type) {
            POIMLevel level = ((POIMMsgObj<POIMLevel>) customMsg).getData();
            if (level.getUid().equals(POMember.getInstance().getBeke_userid())) {
                POMember.getInstance().updateFanLevel(level.getFanLevel());
            }
            addMsg(POIMTalkMsg.buildRoomMsg(level.getUid(), level.getNickName(),
                    String.format("恭喜%s在本直播间升级为 %d级", level.getNickName(), level.getFanLevel()), 2, POIMMsg.MsgType.LEVEL));
        } else if (customMsg.getType() == POIMMsg.MsgType.DANMU.type) {
            POIMDanmu danmu = ((POIMMsgObj<POIMDanmu>) customMsg).getData();
            //FIXME 弹幕和礼物，带有头像icon的都需要添加点击事件
            if (!isCleanMode) { //清屏状态时，不显示飘屏动画，也不做保存
                danmakuActionManager.addDanmu(danmu);
            }
        } else if (customMsg.getType() == POIMMsg.MsgType.INTERRUPTLIVE.type) {
            if (onRoomEventListener != null) {
                onRoomEventListener.streamInterrupt();
            }
        } else if (customMsg.getType() == POIMMsg.MsgType.LIKE.type) {
            POIMLike like = ((POIMMsgObj<POIMLike>) customMsg).getData();
            addMsg(POIMTalkMsg.buildLikeMsg(like, POIMMsg.MsgType.LIKE));
            addHeart(false);
        } else if (customMsg.getType() == POIMMsg.MsgType.USERLIST.type) {
            POIMMsgUserList userListObj = (POIMMsgUserList<POIMUser>) customMsg;
            liveHeaderView.setOnline(userListObj.getTotal());
            List<POIMUser> userList = userListObj.getData();
            if (userList != null && userList.size() > 0) {
                userAdapter.setData(userList);
                userAdapter.notifyDataSetChanged();
            }
        } else if (customMsg.getType() == POIMMsg.MsgType.WELCOME.type) {
            POIMWelcome content = ((POIMMsgObj<POIMWelcome>) customMsg).getData();
            addMsg(POIMTalkMsg.buildRoomMsg(content.getUid(), content.getNickName(),
                    String.format("欢迎%s进入直播间", content.getNickName()), 2, POIMMsg.MsgType.WELCOME));
        } else if (customMsg.getType() == POIMMsg.MsgType.CONTENT.type) {
            POIMContent content = ((POIMMsgObj<POIMContent>) customMsg).getData();
            addMsg(POIMTalkMsg.buildRoomMsg(content.getContent()));
        } else if (customMsg.getType() == POIMMsg.MsgType.RANKLIST.type) {
            List<POIMRank> rankList = ((POIMMsgList<POIMRank>) customMsg).getData();
            if (rankList != null && rankList.size() > 0) {
                POIMRank rank = rankList.get(0);
                if (!rank.getUid().equals(rankFirstUid)) {
                    rankFirstUid = rank.getUid();
                    addMsg(POIMTalkMsg.buildRoomMsg(rank.getUid(), rank.getNickName(),
                            String.format("%s已冲到守护榜第一名", rank.getNickName()), 0, POIMMsg.MsgType.RANKLIST));
                }
            }
        } else if (customMsg.getType() == POIMMsg.MsgType.PERMISSION.type) {
            POIMPermission permission = ((POIMMsgObj<POIMPermission>) customMsg).getData();
            if (permission.getForbid_type() == 1) {
                if (permission.getUid().equals(me.getBeke_userid())) {
                    ToastUtils.showToast(String.format("你被%s踢出1小时", permission.getOpt_nickName()));
                    if (onRoomEventListener != null) {
                        onRoomEventListener.exit(false);
                    }
                } else {
                    addMsg(POIMTalkMsg.buildRoomMsg(permission.getUid(), permission.getNickName(),
                            String.format("%s被%s踢出1小时", permission.getNickName(), permission.getOpt_nickName()),
                            0, permission.getOpt_uid(), permission.getOpt_nickName(), 1, POIMMsg.MsgType.PERMISSION));
                }
            } else if (permission.getForbid_type() == 2) {
                addMsg(POIMTalkMsg.buildRoomMsg(permission.getUid(), permission.getNickName(),
                        String.format("%s被%s禁言5分钟", permission.getNickName(), permission.getOpt_nickName()), 0,
                        permission.getOpt_uid(), permission.getOpt_nickName(), 1, POIMMsg.MsgType.PERMISSION));
            }
        } else if (customMsg.getType() == POIMMsg.MsgType.ADMIN.type) {
            Logger.e("samuel", "管理权限");
            POIMAdmin admin = ((POIMMsgObj<POIMAdmin>) customMsg).getData();
            if (admin.getType() == 1) {
                if (admin.getUid().equals(me.getBeke_userid())) {
                    liveBean.setRoom_admin(1);
                }
                addMsg(POIMTalkMsg.buildRoomMsg(admin.getUid(), admin.getNickName(),
                        String.format("%s被主播提升为管理", admin.getNickName()), 0, POIMMsg.MsgType.ADMIN));
            } else {
                if (admin.getUid().equals(me.getBeke_userid())) {
                    liveBean.setRoom_admin(0);
                }
                liveBean.setRoom_admin(0);
                addMsg(POIMTalkMsg.buildRoomMsg(admin.getUid(), admin.getNickName(),
                        String.format("%s被主播撤销了管理", admin.getNickName()), 0, POIMMsg.MsgType.ADMIN));
            }
        } else if (customMsg.getType() == POIMMsg.MsgType.ATTENDANCHOR.type) { //关注主播
            POAttendanchor content = ((POIMMsgObj<POAttendanchor>) customMsg).getData();

            addMsg(POIMTalkMsg.buildRoomMsg(content.getUid(), content.getNickName(),
                    String.format("%s关注了主播，不错过下次直播", content.getNickName()), 0, POIMMsg.MsgType.ATTENDANCHOR));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTalkMessage(io.rong.imlib.model.Message message) {//聊天室聊天消息回调
        //保证消息的targetId和当前roomId一致
        if (message == null || liveBean == null || StringUtils.isEmpty(liveBean.getRoom_id()) || StringUtils.isEmpty(message.getTargetId()) || !message.getTargetId().equals(liveBean.getRoom_id()))
            return;

        try {
            TextMessage msg = (TextMessage) message.getContent();

            if (msg == null || StringUtils.isEmpty(msg.getExtra())) return;

            POIMTalkMsgExtra extra = new POIMTalkMsgExtra().fromJson(msg.getExtra());

            if (extra == null) {
                return;
            }
            addMsg(POIMTalkMsg.buildNormalMsg(msg.getContent(), extra));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addMsg(POIMTalkMsg msg) {
        Message message = Message.obtain();
        message.obj = msg;
        msgHandler.sendMessage(message);
    }

    Object lockObj = new Object();

    Handler msgHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(isContextAlive()) {
                POIMTalkMsg obj = (POIMTalkMsg)msg.obj;
                if(obj!=null&&msgAdapter!=null) {
                    msgAdapter.add(0, obj);
                    synchronized (lockObj) {
                        msgAdapter.notifyItemInserted(0);
                        if (msgListView.getScrollState() != RecyclerView.SCROLL_STATE_DRAGGING)
                            msgAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };



    public void setLiveBean(POLive mBean) {
        this.liveBean = mBean;
    }

    /**
     * 查看用户信息
     */
    private void checkUserInfo(String uid) {
        if (liveBean.isLive()) {  //如果是直播状态显示用户信息
            if (StringUtils.isNotEmpty(uid) && !uid.equals(me.getBeke_userid())) {
                hideIfConverLayShow();
                hideIfConverListLayShow();
                mUserDialog = new UserInfoDialog(activity,
                        type == IMCLIENT_TYPE_RECORD ? UserInfoDialog.anchor
                                : (liveBean.getIsAdmin() || me.getIsAdmin() == 1 ? UserInfoDialog.admin : UserInfoDialog.user));
                mUserDialog.setOnUserInfoEventListener(onUserInfoEventListener);
                mUserDialog.initData(uid, liveBean);
                mUserDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if(dialog!=null) {
                            dialog.dismiss();
                        }
                    }
                });
                mUserDialog.show();
            }
        } else { //录播装填，直接跳转个人页
            getActivity().startActivity(
                    OthersDetailsActivity.getCallingIntent(getActivity(), uid, OthersDetailsActivity.OTHER));
        }
    }

    private UserInfoDialog.OnUserInfoEventListener onUserInfoEventListener =
            new UserInfoDialog.OnUserInfoEventListener() {
                @Override
                public void onClickSendGift(POMember user) {
                    showGift();
                    sendGiftsView.setTargetId(user.getBeke_userid());
                }

                @Override
                public void onClickMessage(POMember user) {
                    showConversationFragment(user.getBeke_userid(), user.getBeke_nickname(),
                            Conversation.ConversationType.PRIVATE);
                }

                @Override
                public void onClickReply(POMember user) {
                    if (user == null) {
                        return;
                    }
                    replyUser(user.getBeke_nickname());
                }

                @Override
                public void gotoUserInfo(POMember user) {
                    startActivity(OthersDetailsActivity.getCallingIntengData(activity, user.getBeke_userid(),
                            OthersDetailsActivity.OTHER, 1));
                }

                @Override
                public void gotoAdminList() {
                    startActivity(AdministratorListActivity.getCallingIntent(activity, POMember.getInstance().getBeke_userid()
                            , AdministratorListActivity.ADMIN));
                }
            };

    private void replyUser(String userName) {
        chatEdit.setText(String.format("@%s ", userName));
        Editable etext = chatEdit.getText();
        Selection.setSelection(etext, etext.length());
        new WeakHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showKeyboard();
            }
        }, 200);

    }

    private ConversationFragment conversFragment;

    private void showConversationFragment(String mTargetId, String mTargetTitle,
                                          Conversation.ConversationType conversationType) {
        conversLay.setVisibility(View.VISIBLE);
        conversFragment = new ConversationFragment();
        conversFragment.isInChatRoom = true;
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName)
                .buildUpon()
                .appendPath("conversation")
                .appendPath(conversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId)
                .appendQueryParameter("title", mTargetTitle)
                .build();

        conversFragment.setUri(uri);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.conversLay, conversFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onBackPressed() {
        if (sendGiftsView != null && sendGiftsView.getVisibility() == View.VISIBLE) {
            sendGiftsView.setVisibility(View.GONE);
            return true;
        } else if (hideIfConverLayShow()) {  //会话界面在会话列表界面上面，因此on需要先判断会话界面
            return true;
        } else if (hideIfConverListLayShow()) {
            return true;
        }

        return false;
    }

    private boolean hideIfConverLayShow() {
        if (conversLay != null && conversLay.getVisibility() == View.VISIBLE) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.remove(conversFragment);
            fragmentTransaction.commitAllowingStateLoss();
            conversLay.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    private boolean hideIfConverListLayShow() {
        if ((privateLetterLay != null && privateLetterLay.getVisibility() == View.VISIBLE)) {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.remove(privateLetterFragement);
            fragmentTransaction.commitAllowingStateLoss();
            privateLetterLay.setVisibility(View.GONE);
            return true;
        }
        return false;
    }

    private ConversListFragment privateLetterFragement;

    @OnClick({R.id.live_record_message, R.id.live_play_message})
    protected void showPrivateLetter() {
        if(liveBean.getVid()!=null) {//不为空的话就是看播
            daDian("live_msg_init_event", "");//点击私信图标的打点事件
        }
        privateLetterLay.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        privateLetterFragement = ConversListFragment.newInstance(this, true);
        fragmentTransaction.add(R.id.privateLetterLay, privateLetterFragement, null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    /**
     * 设置用户信息监听
     *
     * @param userInfoListener 用户信息
     */
    public void setUserInfoListener(UserInfoListener userInfoListener) {
        this.userInfoListener = userInfoListener;
    }

    /**
     * 隐藏聊天的输入框
     *
     * @param isHide 是否隐藏：YES隐藏，NO显示
     */
    public void hideChatEdit(boolean isHide, int heightDifference) {
        if (isLive()) {
            if (editLayout != null) {
                editLayout.setVisibility(isHide ? View.GONE : View.VISIBLE);
                showOrHideHead(isHide);
                if (type == IMCLIENT_TYPE_RECORD) {
                    liveRecordMenu.setVisibility(isHide ? View.VISIBLE : View.INVISIBLE);
                } else {
                    livePlayMenu.setVisibility(isHide ? View.VISIBLE : View.GONE);
                }
            }
        }
    }

    private void showOrHideHead(boolean isShow) {
        //        AnimatorSet animator;
        if (isShow) {
            headerLay.setVisibility(View.VISIBLE);
            userMoneyLay.setVisibility(View.VISIBLE);

            //            ObjectAnimator animator1 = ObjectAnimator.ofFloat(headerLay, View.TRANSLATION_Y, -headerLay.getHeight(), 0);
            //            animator1.setDuration(200);
            //
            //            ObjectAnimator animator2 = ObjectAnimator.ofFloat(userMoneyLay, View.TRANSLATION_X, -userMoneyLay.getWidth(), 0);
            //            animator2.setDuration(500);
            //
            //            animator = new AnimatorSet();
            //            animator.playTogether(animator1,animator2);

        } else {
            //            ObjectAnimator animator1 = ObjectAnimator.ofFloat(headerLay, View.TRANSLATION_Y,  0,-headerLay.getHeight());
            //            animator1.setDuration(500);
            //
            //            ObjectAnimator animator2 = ObjectAnimator.ofFloat(userMoneyLay, View.TRANSLATION_X, 0, -userMoneyLay.getWidth());
            //            animator2.setDuration(1000);
            //
            //            animator = new AnimatorSet();
            //            animator.playTogether(animator1,animator2);
            //            animator.addListener(new AnimatorListenerAdapter() {
            //                @Override
            //                public void onAnimationEnd(Animator animation) {
            headerLay.setVisibility(View.GONE);
            userMoneyLay.setVisibility(View.GONE);
            //                }
            //            });
        }
        //        animator.start();
    }

    public void hideChat(boolean isHide) {
        if (msgListView != null) {
            msgListView.setVisibility(isHide ? View.INVISIBLE : View.VISIBLE);
        }

        if (menuLayout != null) {
            menuLayout.setVisibility(isHide ? View.INVISIBLE : View.VISIBLE);
        }
    }


    @OnClick({R.id.live_play_chat, R.id.live_record_chat})
    public void showKeyboard() {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        chatEdit.requestFocus();
    }


    private boolean isDanmuSending;

    /**
     * 发送消息
     */
    //@OnClick(R.id.btn_send) protected void sendMessage() {
    protected void sendMessage() {
        final String message = chatEdit.getText().toString().trim();

        if (StringUtils.isEmpty(message)) {
            ToastUtils.showToast("评论内容不能为空");
            return;
        }

        if (danmu.isSelected()) {
            if (message.length() > 20) {
                ToastUtils.showToast("弹幕不能超过20字哦");
                return;
            }
            //            else if(me.getBeikeNum()<1){
            //                LiveUiUtils.showBuyGoldDialog(getContext(), new View.OnClickListener() {
            //                    @Override
            //                    public void onClick(View v) {
            ////                jumpToPayActivity();
            //                    }
            //                });
            //            }
        } else {
            if (message.length() > 140) {
                ToastUtils.showToast("评论最长为140字哦");
                return;
            }
        }

        if (danmu.isSelected()) {
            if (isDanmuSending)
                return;
            isDanmuSending = true;
            Map<String, String> params = new HashMap<>();
            params.put("content", message);
            params.put("roomId", liveBean.getRoom_id());
            new DanmaRequest() {
                @Override
                public void onFinish(boolean isSuccess, String msg, Object data) {
                    isDanmuSending = false;
                    if (isSuccess) {
                        IMClientUtils.sendTextMessage(message, liveBean.getRoom_id(), me.getIsAdmin());
                        if (sendGiftsView != null) {
                            sendGiftsView.setUseGoldCoin(1);
                        }
                        chatEdit.setText("");
                    } else {
                        if (responseBean.getState() == 8001) {
                            LiveUiUtils.showBuyGoldDialog(getContext());
                        } else {
                            ToastUtils.showToast(StringUtils.isEmpty(msg) ? "发送失败" : msg);
                        }
                    }
                }
            }.startRequest(params);

        } else {
            IMClientUtils.sendTextMessage(message, liveBean.getRoom_id(), me.getIsAdmin());
            chatEdit.setText("");
            if(liveBean.getVid()!=null) {//不为空的话就是看播
                daDian("live_chat_btn_event", message);//发送聊天消息的打点统计
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onPraiseEventToRefreshBeike(EBORefreshBeike event) {
        try {
            EBORefreshBeike.removeRefreshBeikePraiseEvent();
            if (event == null || sendGiftsView == null)
                return;
            sendGiftsView.refreshGoldCoin();
        } catch (Exception e) {
            Logger.e(e.getMessage() + "");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(POEventBus bean) {
        switch (bean.getId()) {
            case EventBusKey.CLICK_CONVERSATION_BACK:
                if (conversLay != null
                        && conversLay.getVisibility() == View.VISIBLE) {  //会话界面在会话列表界面上面，因此需要先判断会话界面
                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    fragmentTransaction.remove(conversFragment);
                    fragmentTransaction.commitAllowingStateLoss();
                    conversLay.setVisibility(View.GONE);
                }
                break;
            case EventBusKey.RECEIVE_UNREAD_MESSAGE:
                String count = bean.getData();
                if (Integer.parseInt(count) > 0) {
                    if (isRecord()) {
                        recordRedTips.setVisibility(View.VISIBLE);
                    } else {
                        redTips.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (isRecord()) {
                        recordRedTips.setVisibility(View.GONE);
                    } else {
                        redTips.setVisibility(View.GONE);
                    }
                }
                break;
        }
    }

    @Override
    public void conversListItemClick(String targetID, String conversationTitle,
                                     Conversation.ConversationType conversationType) {
        showConversationFragment(targetID, conversationTitle, conversationType);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bigAnimation:
                if (conversLay != null
                        && conversLay.getVisibility() == View.VISIBLE) {  //会话界面在会话列表界面上面，因此需要先判断会话界面
                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    fragmentTransaction.remove(conversFragment);
                    fragmentTransaction.commitAllowingStateLoss();
                    conversLay.setVisibility(View.GONE);
                }
                if (privateLetterLay != null && privateLetterLay.getVisibility() == View.VISIBLE) {
                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    fragmentTransaction.remove(privateLetterFragement);
                    fragmentTransaction.commitAllowingStateLoss();
                    privateLetterLay.setVisibility(View.GONE);
                }
                break;
        }
    }

    //重写OnActivityResult方法，为了ImageInputProvider等融云provider可以回调到 OnActivityResult方法
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        int index = requestCode >> 12;
        if (index != 0) {
            --index;
            Fragment fragment = this.getOffsetFragment(index, this);
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private Fragment getOffsetFragment(int offset, Fragment fragment) {
        if (offset == 0) {
            return fragment;
        } else {
            Iterator i$ = this.getChildFragmentManager().getFragments().iterator();

            Fragment item;
            do {
                if (!i$.hasNext()) {
                    return null;
                }

                item = (Fragment) i$.next();
                --offset;
                if (offset == 0) {
                    return item;
                }
            } while (item.getChildFragmentManager().getFragments() == null
                    || item.getChildFragmentManager().getFragments().size() <= 0);

            return this.getOffsetFragment(offset, item);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (heartView != null) {
            heartView.stop();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (danmakuActionManager != null) {
            danmakuActionManager.release();
        }
        if (batterAnim != null) {
            batterAnim.release();
        }
        if (liveBean != null) {
            IMClientUtils.quitChatRoom(liveBean.getRoom_id());
        }
        msgHandler.removeCallbacksAndMessages(null);
        System.gc();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        RongContext.getInstance().getEventBus().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    /**
     * 看播页面的打点统计
     */
    public void daDian(String even,String content){
        Map<String,String> maps = new HashMap<>();
        maps.put(POLaunch.M_event,even);
        maps.put(POLaunch.M_tn,String.valueOf(++POLaunch.actionNum));
        maps.put(POLaunch.M_pname,"live_play");
        maps.put(POLaunch.M_vid,liveBean.getVid());
        if(!content.equals("")){
            maps.put(POLaunch.M_content,content);
        }
        actionPresenter.actionUp(maps);
    }
}
