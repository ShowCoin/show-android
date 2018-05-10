package one.show.live.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import java.io.File;

import one.show.live.log.Logger;

/**
 * For recording voice message or some simply audio recording works
 * Created by clarkM1ss1on on 2018/5/6
 */
public class VoiceRecorder {
    private final static String TAG = "VoiceRecorder";

    private MediaRecorder recorder;

    private boolean isReleased = false;

    private boolean isRecording = false;

    private Uri tempFileUri;

    private long startTime = 0;

    private AudioManager audioManager;

    private IVoiceRecorderInfoListener listener;

    private final static VoiceRecorderConfiguration DEFAULT = new VoiceRecorderConfiguration(
            MediaRecorder.AudioSource.MIC
            , 1
            , MediaRecorder.OutputFormat.AMR_NB
            , MediaRecorder.AudioEncoder.AMR_NB
            , 35 * 1000
            , ".voice"
            , 7950
            , 8000);

    private VoiceRecorderConfiguration config;

    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    cancel();
                    break;
            }

        }
    };

    /**
     * Attach with configuration and info listener, the configuration will be default if with null{@link #DEFAULT}
     * {@link VoiceRecorderConfiguration}
     *
     * @param ctx
     * @param config
     * @param listener
     */
    public void attach(Context ctx, @Nullable VoiceRecorderConfiguration config, IVoiceRecorderInfoListener listener) {
        if (null == config) {
            this.config = DEFAULT;
        }
        this.listener = listener;
        recorder = new MediaRecorder();
        audioManager = (AudioManager) ctx
                .getSystemService(Context.AUDIO_SERVICE);
    }

    private void configRecorder() {
        if (!isReleased) {
            recorder.reset();
            recorder.setAudioSource(config.getAudioSource());
            recorder.setAudioChannels(config.getAudioChannels());
            recorder.setOutputFormat(config.getOutputFormat());
            recorder.setAudioEncoder(config.getAudioEncoder());
            recorder.setMaxDuration(config.getMaxDuration());
            recorder.setAudioSamplingRate(config.getSampleRate());
            recorder.setAudioEncodingBitRate(config.getEncodingRate());
        } else {
            Logger.e(TAG, "the recorder was released!!!");
        }
    }

    /**
     * Start recording
     *
     * @param ctx
     */
    public void startWithAudioFocusRequest(Context ctx) {
        if (isRecording) {
            Logger.e(TAG, "recording is started");
            return;
        }
        int result = audioManager.requestAudioFocus(audioFocusChangeListener
                , AudioManager.STREAM_MUSIC
                , AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        if (AudioManager.AUDIOFOCUS_REQUEST_GRANTED != result) {
            Logger.e(TAG, "request audio focus failed !!");
            return;
        }
        configRecorder();
        tempFileUri = Uri.fromFile(new File(ctx.getCacheDir()
                , System.currentTimeMillis() + config.getFileSuffix()));
        if (null != listener) {
            listener.onRecorderStart(tempFileUri);
        }
        try {
            recorder.setOutputFile(tempFileUri.getPath());
            recorder.prepare();
            recorder.start();
            isRecording = true;
            startTime = SystemClock
                    .elapsedRealtime();
            Logger.e(TAG, "startWithAudioFocusRequest time :" + startTime);
        } catch (Exception e) {
            e.printStackTrace();
            isRecording = false;
            if (null != listener) {
                listener.onRecorderError();
            }
        }
    }

    private long stopRecord() {
        recorder.stop();
        isRecording = false;
        return SystemClock.elapsedRealtime() - startTime;
    }

    /**
     * Stop recording, when the recorder stopped, the audio file will generated under the path which in configuration
     */
    public void stop() {
        long duration = SystemClock
                .elapsedRealtime() - startTime;
        stopRecord();
        Logger.e(TAG, "current :" + SystemClock.elapsedRealtime());
        if (null != listener) {
            listener.onRecorderStop(tempFileUri, duration);
        }
    }

    public void cancel() {
        recorder.stop();
        if (null != listener) {
            listener.onRecorderCanceled(tempFileUri);
        }
        if (null != tempFileUri) {
            final File tempFile = new File(tempFileUri.getPath());
            tempFile.deleteOnExit();
        }
        //TODO delete temp file
    }

//    public void clearTemp() {
    //TODO delete temp file
//    }

    public void reset() {
        recorder.reset();
    }


    public void release() {
        if (!isReleased) {
            isReleased = true;
            recorder.release();
        }
    }
}
