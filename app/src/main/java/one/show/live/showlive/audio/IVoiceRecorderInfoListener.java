package one.show.live.audio;

import android.net.Uri;

/**
 * Created by clarkM1ss1on on 2018/5/7
 */
public interface IVoiceRecorderInfoListener {

    /**
     * @param voiceFile the recording file
     */
    void onStart(Uri voiceFile);

    /**
     * @param voiceFile the recording file, maybe it needed to be removed
     */
    void onCancel(Uri voiceFile);

    void onStop(Uri voiceFile, long duration);

    void onStartError();

}
