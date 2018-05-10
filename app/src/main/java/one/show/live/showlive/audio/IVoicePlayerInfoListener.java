package one.show.live.audio;

import android.media.MediaPlayer;

/**
 * Created by clarkM1ss1on on 2018/5/8
 */
public interface IVoicePlayerInfoListener {

    public void onPlayerStop();

    public void onPlayerError();

    public void onPlayerCompletion(MediaPlayer mp);
}
