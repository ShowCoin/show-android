package one.show.live.audio;

import android.net.Uri;

/**
 * Created by clarkM1ss1on on 2018/5/7
 */
public interface IVoiceRecorderInfoListener {

    /**
     * @param voiceFile the recording file
     */
    void onRecorderStart(Uri voiceFile);

    /**
     * @param voiceFile the recording file, maybe it needed to be removed
     */
    void onRecorderCanceled(Uri voiceFile);

    void onRecorderStop(Uri voiceFile, long duration);

    void onRecorderError();

}
