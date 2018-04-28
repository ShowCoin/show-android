package one.show.live.media.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.nodemedia.LivePlayer;
import cn.nodemedia.LivePlayerDelegate;
import one.show.live.R;
import one.show.live.common.po.POLive;
import one.show.live.common.util.StringUtils;
import one.show.live.common.util.WeakHandler;
import one.show.live.media.listener.PlayEventListener;
import one.show.live.log.Logger;
import one.show.live.util.TimeUtil;

/**
 * Created by Administrator on ..7/14 0014.
 */
public class PlayLive extends PlayFragment {

    @BindView(R.id.video_bekenum)
    TextView videoBekenum;
    @BindView(R.id.video_time)
    TextView videoTime;

    private POLive live;
    private String playStream;
    private int srcWidth, srcHeight;
    DisplayMetrics dm;
    @BindView(R.id.surfaceview)
    SurfaceView liveSurface;
    private boolean isStopPlay;

    public static one.show.live.media.play.ui.PlayLive getInstance(POLive bean) {
        one.show.live.media.play.ui.PlayLive playFragment = new one.show.live.media.play.ui.PlayLive();
        playFragment.live = bean;
        return playFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dm = getActivity().getResources().getDisplayMetrics();
        initMediaPlayer();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initMediaPlayer() {
        LivePlayer.init(getActivity());
        LivePlayer.setDelegate(new LivePlayerDelegate() {
            @Override
            public void onEventCallback(int event, String msg) {
                //handleMessage(event);
                Message message = new Message();
                Bundle b = new Bundle();
                b.putString("msg", msg);
                message.setData(b);
                message.what = event;
                handler.sendMessage(message);
            }
        });
        /**
         * 设置缓冲区时长，与flash编程时一样，可以设置2个值
         * 第一个bufferTime为从连接成功到开始播放的启动缓冲区长度，越小启动速度越快，最小100毫秒
         * 注意：声音因为没有关键帧，所以这个缓冲区足够马上就可以听到声音，但视频需要等待关键帧后才会开始显示画面。
         * 如果你的服务器支持GOP_cache可以开启来加快画面的出现
         */
        LivePlayer.setBufferTime(300);

        /**
         * maxBufferTime为最大缓冲区，当遇到网络抖动，较大的maxBufferTime更加平滑，但延迟也会跟着增加。
         * 这个值关乎延迟的大小。
         */
        LivePlayer.setMaxBufferTime(6000);
    }

    @Override
    protected void initView() {
        super.initView();
        if (live != null) {
            playStream = live.getStream_addr();
            Logger.e("流播放地址："+live.getStream_addr());
        }
        if (StringUtils.isNotEmpty(playStream)) {
            LivePlayer.setUIVIew(liveSurface);
            /**
             * 开始播放
             */
            LivePlayer.startPlay(playStream);
        } else {
            LivePlayer.setUIVIew(null);
        }
        videoBekenum.setText("贝壳号："+live.getMaster().getPid());
        videoTime.setText(TimeUtil.getDateToString(System.currentTimeMillis()));
    }

    @Override
    protected int getContentView() {
        return R.layout.play_live_layout;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void setRibbonHide(boolean hide) {

    }

    WeakHandler handler = new WeakHandler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1000:
                    eventListener.onEvent(PlayEventListener.STATUS_BUFFERING);
                    // Toast.LENGTH_SHORT).show();
                    break;
                case 1001:
                    // Toast.makeText(LivePlayerDemoActivity.this, "视频连接成功",
                    // Toast.LENGTH_SHORT).show();
                    eventListener.onEvent(PlayEventListener.STATUS_START);
                    break;
                case 1002:
                    // Toast.makeText(LivePlayerDemoActivity.this, "视频连接失败",
                    // Toast.LENGTH_SHORT).show();
                    //流地址不存在，或者本地网络无法和服务端通信，回调这里。5秒后重连， 可停止
                    //LivePlayer.stopPlay();
                    break;
                case 1003:
                    //handler.sendEmptyMessageDelayed(RE_CONNECTION, 15000);
                    //TODO
                    //                    listener.onStatusChange(PlayerStatusListener.STATUS_BUFFERING);
                    //Toast.makeText(LivePlayerDemoActivity.this, "视频开始重连",
                    //LivePlayer.stopPlay();	//自动重连总开关
                    break;
                case 1004:
                    // Toast.makeText(LivePlayerDemoActivity.this, "视频播放结束",
                    // Toast.LENGTH_SHORT).show();
                    //TODO
                    //                    listener.onStatusChange(PlayerStatusListener.STATUS_FINISH);
                    break;
                case 1005:
                    // Toast.makeText(LivePlayerDemoActivity.this, "网络异常,播放中断",
                    // Toast.LENGTH_SHORT).show();
                    //播放中途网络异常，回调这里。1秒后重连，如不需要，可停止
                    //LivePlayer.stopPlay();
                    break;
                case 1100:
                    //				System.out.println("NetStream.Buffer.Empty");
                case 1101:
                    //				System.out.println("NetStream.Buffer.Buffering");
                    //handler.sendEmptyMessageDelayed(RE_CONNECTION, 15000);
                    eventListener.onEvent(PlayEventListener.STATUS_BUFFERING);
                    break;
                case 1102:
                    //handler.removeMessages(RE_CONNECTION);
                    eventListener.onEvent(PlayEventListener.STATUS_PLAY);
                    //				System.out.println("NetStream.Buffer.Full");
                    break;
                case 1103:
                    //				System.out.println("Stream EOF");
                    //客服端明确收到服务端发送来的 StreamEOF 和 NetStream.Play.UnpublishNotify时回调这里
                    //收到本事件，说明：此流的发布者明确停止了发布，或者网络异常，被服务端明确关闭了流
                    //本sdk仍然会继续在1秒后重连，如不需要，可停止
                    //LivePlayer.stopPlay();
                    break;
                case 1104:
                    /**
                     * 得到 解码后得到的视频高宽值,可用于重绘surfaceview的大小比例 格式为:{width}x{height}
                     * 本例使用LinearLayout内嵌SurfaceView
                     * LinearLayout的大小为最大高宽,SurfaceView在内部等比缩放,画面不失真
                     * 等比缩放使用在不确定视频源高宽比例的场景,用上层LinearLayout限定了最大高宽
                     */
                    //String[] info = msg.getData().getString("msg").split("x");
                    //srcWidth = Integer.valueOf(info[0]);
                    //srcHeight = Integer.valueOf(info[1]);
                    //doVideoFix();
                    eventListener.onEvent(PlayEventListener.GET_VIDEOSIZE);
                    break;
                case RE_CONNECTION:
                    LivePlayer.stopPlay();
                    LivePlayer.startPlay(playURL);
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    public void onResume() {
        super.onResume();
//        startPlay();
    }

    /**
     * 直播中途主播离开后再次回来，要重新播放
     */
    public void reStartPlay() {
        LivePlayer.startPlay(playURL);
    }

    public void stopPlay() {
        LivePlayer.stopPlay();
        isStopPlay = true;
    }

    public void startPlay() {
        if (isStopPlay) {
            LivePlayer.setUIVIew(liveSurface);
            /**
             * 开始播放
             */
            LivePlayer.startPlay(playStream);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
//        stopPlay();
    }

    /**
     * 视频画面高宽等比缩放，此SDK——demo 取屏幕高宽做最大高宽缩放
     */
    private void doVideoFix() {
        float maxWidth = dm.widthPixels;
        float maxHeight = dm.heightPixels;
        float fixWidth;
        float fixHeight;
        if (srcWidth / srcHeight <= maxWidth / maxHeight) {
            fixWidth = srcWidth * maxHeight / srcHeight;
            fixHeight = maxHeight;
        } else {
            fixWidth = maxWidth;
            fixHeight = srcHeight * maxWidth / srcWidth;
        }
        ViewGroup.LayoutParams lp = liveSurface.getLayoutParams();
        lp.width = (int) fixWidth;
        lp.height = (int) fixHeight;

        liveSurface.setLayoutParams(lp);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new Thread() {
            @Override
            public void run() {
                LivePlayer.stopPlay();
                LivePlayer.setDelegate(null);
            }
        }.start();
    }
}
