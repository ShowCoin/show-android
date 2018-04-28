package one.show.live.play.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import one.show.live.R;
import one.show.live.play.util.VideoPlayerIJK;
import one.show.live.play.view.VideoPlayerListener;
import one.show.live.ui.BaseFragmentActivity;
import one.show.live.view.ToastUtils;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * Created by Nano on 2018/4/10.
 * 播放页面
 */

public class PlayActivity extends BaseFragmentActivity{
    @BindView(R.id.play_view)
    VideoPlayerIJK playView;


    public static Intent getCallingIntent(Context context){
        Intent intent = new Intent(context,PlayActivity.class);
        return intent;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_play;
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

        playView.setVideoPath("http://cn01.flv.wsxhy.com/live/WS_1523338445_23166022_9141.2211.flv?ABSTime=5acdc807&secret=93d394a30f5795084376b37e931ec194&rand=8470957&time=1523349127&guid=4906e84b225562fb8e39754983765f3b&platform=cdn.flv");
        playView.setListener(new VideoPlayerListener() {
            @Override
            public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int i) {

            }

            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                iMediaPlayer.release();//播放结束后，就自动释放player的资源。
            }

            @Override
            public boolean onError(IMediaPlayer iMediaPlayer, int i, int i1) {
                ToastUtils.showToast("无法继续播放该音频");
                return false;
            }

            @Override
            public boolean onInfo(IMediaPlayer iMediaPlayer, int i, int i1) {
                return false;
            }

            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {

                iMediaPlayer.start();
                ToastUtils.showToast("准备好了");
            }

            @Override
            public void onSeekComplete(IMediaPlayer iMediaPlayer) {

            }

            @Override
            public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int i, int i1, int i2, int i3) {
                //获取到视频的宽和高
            }
        });


    }

    @Override
    protected void onDestroy() {
        playView.release();
        super.onDestroy();
    }
}
