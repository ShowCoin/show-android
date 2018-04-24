package one.show.live.media.ui;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import one.show.live.R;
import one.show.live.common.po.POLive;
import one.show.live.media.listener.PlayEventListener;
import one.show.live.share.ui.ShareDialog;
import one.show.live.widget.media.IjkVideoView;
import one.show.live.widget.media.PlayController;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Administrator on 2016/6/6 0006.
 */
public class PlayVideoFragment extends PlayFragment implements View.OnClickListener{

  private static final int STATUS_BUFFERING_START = 0x2;
  private static final int STATUS_PLAY = 0x3;
  private POLive live;

  @BindView(R.id.video_view) IjkVideoView mVideoView;
  @BindView(R.id.play_controller) PlayController playController;

  private int pauseTime;
  private Handler handler = new Handler(new Handler.Callback() {
    @Override public boolean handleMessage(Message msg) {
      switch (msg.what) {
        case STATUS_BUFFERING_START:
          eventListener.onEvent(PlayEventListener.GET_VIDEOSIZE);
          break;
        case STATUS_PLAY:
          eventListener.onEvent(PlayEventListener.GET_VIDEOSIZE);
          eventListener.onEvent(PlayEventListener.STATUS_PLAY);
          break;
      }
      return true;
    }
  });

  public static one.show.live.media.play.ui.PlayVideoFragment getInstance(POLive bean) {
    one.show.live.media.play.ui.PlayVideoFragment fragment = new one.show.live.media.play.ui.PlayVideoFragment();
    fragment.live = bean;
    return fragment;
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // init player
    IjkMediaPlayer.loadLibrariesOnce(null);
    IjkMediaPlayer.native_profileBegin("libijkplayer.so");
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override protected int getContentView() {
    return R.layout.fragment_play_video;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    setListener();
  }

  @Override protected void initView() {
    String videoStream = live.getStream_addr();
    if (videoStream != null) {
      mVideoView.setVideoURI(Uri.parse(videoStream));
      mVideoView.setMediaController(playController);
    }
    playController.shareBtn.setOnClickListener(this);
  }

  protected void setListener() {
    rootView.setOnClickListener(clickListener);
    mVideoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
      @Override public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        switch (what) {
          case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
          case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
            handler.sendEmptyMessage(STATUS_PLAY);
            break;
          case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
            handler.sendEmptyMessage(STATUS_BUFFERING_START);
            break;
        }
        return true;
      }
    });

    mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
      @Override public void onCompletion(IMediaPlayer iMediaPlayer) {
        //handler.sendEmptyMessage(STATUS_BUFFERING_END);
        if (!iMediaPlayer.isPlaying()) {
          //handler.removeMessages(STATUS_BUFFERING_START);
          //handler.removeMessages(STATUS_BUFFERING_END);
          stopPlay();
        //  if (eventListener != null) {
        //  eventListener.onEvent(PlayEventListener.STATUS_FINISH);
        //}
        }
      }
    });

    mVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
      @Override public boolean onError(IMediaPlayer iMediaPlayer, int framework_err, int impl_err) {
        onNetWorkError();
        return true;
      }
    });
  }

  @Override public void setRibbonHide(boolean hide) {

  }

  @Override public void onClick(View v) {
    switch (v.getId()){
      case R.id.btn_share:
        showShareDialog();
        break;
    }
  }

  protected void showShareDialog() {
    ShareDialog dialog = ShareDialog.newInstance(live);
    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
    ft.add(dialog, null);
    ft.commitAllowingStateLoss();
  }

  @Override public void onResume() {
    super.onResume();
    //        if (liveBean.getStatus() > Constant.STATUS_VIDEO) {
    //mVideoView.resume();
    mVideoView.start();
    if (pauseTime > 0) {
      mVideoView.seekTo(pauseTime);
    }
    //mVideoView.seekTo(pauseTime);
    //        } else {
    //            if (TextUtils.isEmpty(liveBean.getRtmpurl())) {
    //                UIToast.show(context,"系统错误");
    //                return;
    //            }
    //            statusTV.setText("缓冲中...");
    //            mVideoView.release(true);
    //            mVideoView.stopPlayback();
    //            mVideoView.setVideoURI(Uri.parse(liveBean.getRtmpurl()));
    //        mVideoView.resume();
    //        mVideoView.start();
    //        }
  }

  @Override public void onPause() {
    super.onPause();
    pauseTime = mVideoView.getCurrentPosition();
    mVideoView.pause();
    if(playController != null){
      playController.playBtn.setImageResource(R.drawable.icon_player_pause);
    }
  }

  /**
   * 停止播放
   */
  public void stop() {
    mVideoView.stopPlayback();
    mVideoView.release(true);
    mVideoView.stopBackgroundPlay();
  }

  /**
   * 播放完成，停在当前播放位置，不释放播放器
   */
  public void stopPlay(){
    mVideoView.pause();
  }

  @Override public void onStop() {
    super.onStop();
    mVideoView.pause();
  }

  @Override public void onDestroyView() {
    handler.removeMessages(STATUS_BUFFERING_START);
    handler.removeMessages(STATUS_PLAY);
    if(playController != null && playController.handler != null){
      playController.handler.removeMessages(playController.REFRESH_PROGRESS);
    }
    mVideoView.stopPlayback();
    mVideoView.release(true);
    mVideoView.stopBackgroundPlay();
    IjkMediaPlayer.native_profileEnd();
    super.onDestroyView();
  }
}
