package one.show.live.live.play.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import one.show.live.R;
import one.show.live.po.POLive;
import one.show.live.live.listener.PlayEventListener;
import one.show.live.util.TimeUtil;
import one.show.live.widget.media.IjkVideoView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayLiveFragment extends PlayFragment {

  private POLive live;
  private String videoStream;
  private static final int STATUS_BUFFERING_START = 0x2;
  private static final int STATUS_BUFFERING_END = 0x3;

  @BindView(R.id.video_view) IjkVideoView videoView;
  @BindView(R.id.video_bekenum)
  TextView videoBekenum;
  @BindView(R.id.video_time)
  TextView videoTime;

  private Handler handler = new Handler(new Handler.Callback() {
    @Override public boolean handleMessage(Message msg) {
      switch (msg.what) {
        case STATUS_BUFFERING_START:
          eventListener.onEvent(PlayEventListener.STATUS_BUFFERING);
          break;
        case STATUS_BUFFERING_END:
          eventListener.onEvent(PlayEventListener.STATUS_START);
          break;
      }
      return true;
    }
  });

  public static PlayLiveFragment getInstance(POLive bean) {
    PlayLiveFragment playFragment = new PlayLiveFragment();
    playFragment.live = bean;
    return playFragment;
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override protected int getContentView() {
    return R.layout.play_video_layout;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    // init player
    //IjkMediaPlayer.loadLibrariesOnce(null);
    //IjkMediaPlayer.native_profileBegin("libijkplayer.so");

    videoStream = live.getStream_addr();

    if (videoStream != null) {
      videoView.setVideoPath(videoStream);
      videoView.start();
    }
    setListener();
    videoBekenum.setText("贝壳号："+live.getMaster().getPid());
    videoTime.setText(TimeUtil.getDateToString(System.currentTimeMillis()));
  }

  @Override protected void initView() {
    super.initView();
  }

  private void setListener() {
    rootView.setOnClickListener(clickListener);
    videoView.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
      @Override public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        switch (what) {
          case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
          case IMediaPlayer.MEDIA_INFO_BUFFERING_END:
            handler.sendEmptyMessage(STATUS_BUFFERING_END);
            break;
          case IMediaPlayer.MEDIA_INFO_BUFFERING_START:
            handler.sendEmptyMessage(STATUS_BUFFERING_START);
            break;
        }
        return true;
      }
    });

    videoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
      @Override public void onCompletion(IMediaPlayer iMediaPlayer) {
        handler.sendEmptyMessage(STATUS_BUFFERING_END);
        if (!iMediaPlayer.isPlaying()) {
          handler.removeMessages(STATUS_BUFFERING_START);
          handler.removeMessages(STATUS_BUFFERING_END);
          stop();
          if (eventListener != null) {
            eventListener.onEvent(PlayEventListener.STATUS_FINISH);
          }
        }
      }
    });

    videoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
      @Override public boolean onError(IMediaPlayer iMediaPlayer, int framework_err, int impl_err) {
        onNetWorkError();
        return true;
      }
    });
  }

  @Override public void onResume() {
    super.onResume();
    //videoView.togglePlayer();
    //if (videoStream != null) {
    //  videoView.setVideoPath(videoStream);
    //  videoView.start();
    //}
  }

  /**
   * 停止播放
   */
  public void stop() {
    videoView.stopPlayback();
    videoView.release(true);
    videoView.stopBackgroundPlay();
  }

  @Override public void onStop() {
    super.onStop();
    //videoView.stopPlayback();
    //videoView.release(true);
    //videoView.stopBackgroundPlay();
    //IjkMediaPlayer.native_profileEnd();
  }

  @Override public void setRibbonHide(boolean hide) {

  }

  @Override  public void onDestroyView() {
    videoView.stopPlayback();
    videoView.release(true);
    videoView.stopBackgroundPlay();
    IjkMediaPlayer.native_profileEnd();
    super.onDestroyView();
  }
}
