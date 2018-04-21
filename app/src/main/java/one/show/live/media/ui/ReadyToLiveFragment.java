package one.show.live.media.ui;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import one.show.live.R;
import one.show.live.common.po.POBaiduLocation;
import one.show.live.common.po.POEventBus;
import one.show.live.common.po.POLaunch;
import one.show.live.common.po.POMember;
import one.show.live.common.ui.BaseFragment;
import one.show.live.common.util.StringUtils;
import one.show.live.common.util.UIUtils;
import one.show.live.common.util.UserInfoCacheManager;
import one.show.live.common.view.ToastUtils;
import one.show.live.media.listener.ReadToLiveInterface;
import one.show.live.media.model.LiveStatusRequest;
import one.show.live.media.po.POInitLive;
import one.show.live.media.po.POLiveStatus;
import one.show.live.media.presenter.ReadyToLivePresenter;
import one.show.live.media.presenter.UpSharePresenter;
import one.show.live.media.view.ReadyToLiveView;
import one.show.live.util.EventBusKey;
import one.show.live.util.NoDoubleClickListener;
import one.show.live.util.ToolUtil;

/**
 * Created by apple on 16/5/23.
 * <p/>
 * 准备直播页面
 */
public class ReadyToLiveFragment extends BaseFragment
    implements ReadyToLiveView {

  ReadyToLivePresenter mReadyToLivePresenter;
  POInitLive plive;//点击开播按钮回调回来的数据设置为全局的
  OnRoomEventListener onRoomEventListener;
  private boolean  isCamOn = true;

  private UpSharePresenter upSharePresenter;

  @BindView(R.id.readylive_cha)
  ImageView readyliveCha;
  @BindView(R.id.readytolive_rotato)
  ImageView readytoliveRotato;
  @BindView(R.id.readytolive_title_context)
  EditText readytoliveTitleContext;
  @BindView(R.id.share_wxfriends)
  TextView shareWxfriends;
  @BindView(R.id.share_wx)
  TextView shareWx;
  @BindView(R.id.share_weibo)
  TextView shareWeibo;
  @BindView(R.id.readylive_center_lay)
  LinearLayout readyliveCenterLay;
  @BindView(R.id.readylive_button)
  TextView readyliveButton;
  @BindView(R.id.readytolive_meiyan)
  ImageView readytoliveMeiyan;
  @BindView(R.id.readytolive_positioning)
  TextView readytolivePositioning;


  @BindView(R.id.loading_layout) LinearLayout loadingLayout;
  @BindView(R.id.wait_image) ImageView loadingImage;
  private AnimationDrawable animationDrawable;
  private ReadToLiveInterface readToLiveInterface;

  private final static String QQ = "qq";
  private final static String WEIXIN = "weixin";
  private final static String SINA = "sina";
  private final static String PHONE = "phone";

  int shareType = 0;//分享那个平台的0是不分享1是朋友圈，2微信，3新浪，4QQ，5空间
  private POInitLive publisherInfo;
  public static ReadyToLiveFragment newInstance() {
    ReadyToLiveFragment fragment = new ReadyToLiveFragment();
    return fragment;
  }

  @Override protected int getContentView() {
    return R.layout.activity_readylive;
  }


  public void setOnRoomEventListener(OnRoomEventListener listener) {
    this.onRoomEventListener = listener;
  }

  @Override protected void initView() {
    super.initView();
    readToLiveInterface = (ReadToLiveInterface)activity;
    upSharePresenter = new UpSharePresenter();
    mReadyToLivePresenter = new ReadyToLivePresenter(this);
    EventBus.getDefault().register(this);
    readyliveButton.setOnClickListener(new NoDoubleClickListener() {
      @Override public void onNoDoubleClick(View view) {
        startLive();
      }
    });

    readytolivePositioning.setText(StringUtils.trim(POBaiduLocation.address));

    visi();//判断是不是有微信和QQ的
    current();
  }

  private boolean isRequestStartLive;

  private void startLive() {
    if(isRequestStartLive)
      return;
    isRequestStartLive = true;
    showLoading();
    checkPublisherStatus();
    //mReadyToLivePresenter.startLive(readytoliveTitleContext.getText().toString().trim(),
    //        readytolivePositioning.getText().toString().equals("") ? 0 : 1);
  }

  private void checkPublisherStatus() {
    publisherInfo = POInitLive.getInstance();
    if (publisherInfo != null && StringUtils.isNotEmpty(publisherInfo.getLiveId())) {
      Map<String, String> params = new HashMap<>();
      params.put("uid", POMember.getInstance().getUid());
      params.put("liveid", publisherInfo.getLiveId());
      new LiveStatusRequest() {
        @Override public void onFinish(boolean isSuccess, String msg, POLiveStatus data) {
          if (isSuccess && data != null) {
            if (data.getStatus() == 1) { //直播间状态为1，表示上次直播未结束
//              startActivity(PublisherActivity.getCallingIntent(getActivity(), publisherInfo));
              isRequestStartLive = false;
              readToLiveInterface.readToLiveEnd(publisherInfo);
              return;
            }else{
              UserInfoCacheManager.getInstance().remove("PUBLISHER_INFO");
            }
          }
            mReadyToLivePresenter.startLive(readytoliveTitleContext.getText().toString().trim(),
                readytolivePositioning.getText().toString().equals("") ? 0 : 1);

        }
      }.startRequest(params);
    } else {
      mReadyToLivePresenter.startLive(readytoliveTitleContext.getText().toString().trim(),
          readytolivePositioning.getText().toString().equals("") ? 0 : 1);
    }
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // TODO: inflate a fragment view
    View rootView = super.onCreateView(inflater, container, savedInstanceState);
    ButterKnife.bind(this, rootView);
    return rootView;
  }

  /**
   * 点击开播按钮的回调
   */
  @Override public void onReqStartLiveFinish(POInitLive live) {
    isRequestStartLive = false;
    hideLoading();
    this.plive = live;
    if (live != null) {
    if (shareType == 0) {//如果不需要分享的话就直接开播
        jumpLive(live);

    }else{//需要分享的话就先分享在开直播
//        evokeShare(live);
    }
    }
  }

  @Override public void onReqStartLiveFailed(String msg) {
    isRequestStartLive = false;
    if (isContextAlive()) {
      hideLoading();
      ToastUtils.showToast(msg);
    }
  }

  @Override public void checkPublisherSuccess(POLiveStatus liveStatus) {

  }

  @Override public void checkPublisherFailed() {

  }

  @OnClick({
      R.id.readytolive_rotato, R.id.readylive_cha,
      R.id.readytolive_positioning, R.id.share_wxfriends, R.id.share_wx, R.id.share_weibo,
      R.id.readytolive_meiyan
  }) public void onClick(View view) {
    switch (view.getId()) {
      case R.id.readytolive_rotato://前后摄像头切换
        //切换前后摄像头
        isCamOn = !isCamOn;
        onRoomEventListener.switchCamera(isCamOn);
        break;
      case R.id.readylive_cha:
        getActivity().finish();
        break;
      case R.id.readytolive_positioning:

        if (readytolivePositioning.getText().toString().equals("")) {
          readytolivePositioning.setText(StringUtils.trim(POBaiduLocation.address));
        } else {
          readytolivePositioning.setText("");
        }

        break;
      case R.id.readytolive_meiyan://美颜按钮
        onRoomEventListener.smoothSkin();
        break;
      case R.id.share_wxfriends:
        trueOfFalse(1);

        break;
      case R.id.share_wx:
        trueOfFalse(2);

        break;
      case R.id.share_weibo:
        trueOfFalse(3);
        break;
    }
  }

  public void trueOfFalse(int Type){
    if(Type==shareType){
      shareWxfriends.setSelected(false);
      shareWx.setSelected(false);
      shareWeibo.setSelected(false);
      shareType=0;
      return;
    }
    shareWxfriends.setSelected(Type==1);
    shareWx.setSelected(Type==2);
    shareWeibo.setSelected(Type==3);
    shareType  =Type;
  }


  //判断是不是有微信和QQ的
  public void visi(){
    if (!ToolUtil.packages("com.tencent.mm")) {
      shareWxfriends.setVisibility(View.GONE);
      shareWx.setVisibility(View.GONE);
    }
  }

  public void current() {

    if(StringUtils.isEmpty(POMember.getInstance().getLastLoginType())){
      trueOfFalse(1);
      return;
    }

    if(ToolUtil.packages("com.tencent.mm")&&!POMember.getInstance().getLastLoginType().equals(SINA)){
      trueOfFalse(1);//安装了微信,并且不是微博登陆的话就默认选中朋友圈
      return;
    }

    if (POMember.getInstance().getLastLoginType().equals(QQ)) {
//      accountNewLogin.setText("您正在使用QQ账号登录");
      trueOfFalse(4);
    }
    if (POMember.getInstance().getLastLoginType().equals(PHONE)) {
//      accountNewLogin.setText("您正在使用手机号登录");
      trueOfFalse(3);
    }
    if (POMember.getInstance().getLastLoginType().equals(SINA)) {
//      accountNewLogin.setText("您正在使用微博账号登录");
      trueOfFalse(3);
    }
    if (POMember.getInstance().getLastLoginType().equals(WEIXIN)) {
//      accountNewLogin.setText("您正在使用微信账号登录");
      trueOfFalse(1);//安装微信的话就是朋友圈
    }
  }

    /**
     * 判断分享的那一个平台
     */
//  public void evokeShare(POInitLive live){
//
//    daDian("launch_init_share_event",live.getLiveId());//点击开始直播按钮
//
//    if(shareType==1){
//      shareUtil.shareMoment(POMember.getInstance().getBeke_nickname(), POMember.getInstance().getProfileImg(), live.getShare_addr());
//    }
//    if(shareType==2){
//      shareUtil.shareWX(POMember.getInstance().getBeke_nickname(), POMember.getInstance().getProfileImg(), live.getShare_addr());
//    }
//    if(shareType==3){
//      shareUtil.shareWeiBo(POMember.getInstance().getBeke_nickname(), POMember.getInstance().getProfileImg(), live.getShare_addr());
//    }
//    if(shareType==4){
//      shareUtil.shareQQ(POMember.getInstance().getBeke_nickname(), POMember.getInstance().getProfileImg(), live.getShare_addr());
//    }
//    if(shareType==5){
//      shareUtil.shareQZone(POMember.getInstance().getBeke_nickname(), POMember.getInstance().getProfileImg(), live.getShare_addr());
//    }
//
//  }

    /**
     * 跳转到开播页
     */
    public void jumpLive(POInitLive live){
//        startActivity(PublisherActivity.getCallingIntent(getActivity(), live));
//        getActivity().finish();
      UIUtils.closeSoftInput(getActivity());
      readToLiveInterface.readToLiveEnd(live);
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

  @Override public void onDestroy() {
    super.onDestroy();
    EventBus.getDefault().unregister(this);
  }


//  /**
//   * 接收到分享的回调
//   */
//  @Subscribe(threadMode = ThreadMode.MAIN) public void onEventMainThread(POEventBus bean) {
//    switch (bean.getId()) {
//      case EventBusKey.EVENT_SHARE_BY_WEI_XIN:
//        jumpLive(plive);
//        upSharePresenter.upShare(POShareMode.WX, plive.getLiveId());
//        break;
//      case EventBusKey.EVENT_SHARE_BY_WEI_XIN_FAILED:
//        jumpLive(plive);
//        break;
//      case EventBusKey.EVENT_SHARE_BY_WEIBO_SUCCESS:
//        jumpLive(plive);
//        upSharePresenter.upShare(POShareMode.WB, plive.getLiveId());
//        break;
//      case EventBusKey.EVENT_SHARE_BY_WEIBO_FAILED:
//        jumpLive(plive);
//        break;
//    }
//
//  }

  public void closeSmoothSkin(){
    // 关闭美颜
    if(readytoliveMeiyan!=null){
      readytoliveMeiyan.setBackgroundResource(R.drawable.ready_brush_black);
    }

  }

  public void openSmoothSkin(){
    // 打开美颜
    if(readytoliveMeiyan!=null) {
      readytoliveMeiyan.setBackgroundResource(R.drawable.ready_brush_black);
    }
  }

}
