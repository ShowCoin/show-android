package one.show.live.play.view;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by liuzehua on 2018/4/10.
 */

public abstract class VideoPlayerListener implements IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnCompletionListener, IMediaPlayer.OnPreparedListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnVideoSizeChangedListener, IMediaPlayer.OnErrorListener, IMediaPlayer.OnSeekCompleteListener {
}