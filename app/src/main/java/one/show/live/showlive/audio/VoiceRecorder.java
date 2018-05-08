package one.show.live.audio;

import android.content.Context;
import android.media.AudioFocusRequest;
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

    /**
     * Attach with configuration and info listener, the configuration will be default if with null{@link #DEFAULT}
     * {@link VoiceRecorderConfiguration}
     *
     * @param config
     * @param listener
     */
    public void attach(@Nullable VoiceRecorderConfiguration config, IVoiceRecorderInfoListener listener) {
        if (null == config) {
            this.config = DEFAULT;
        }
        this.listener = listener;
        recorder = new MediaRecorder();
//        configRecorder();
    }

    private void configRecorder() {
        if (!isReleased) {
            recorder.setAudioSource(config.getAudioSource());
            recorder.setAudioChannels(config.getAudioChannels());
            recorder.setOutputFormat(config.getOutputFormat());
            recorder.setAudioEncoder(config.getAudioEncoder());
            recorder.setMaxDuration(config.getMaxDuration());
            recorder.setAudioSamplingRate(config.getSampleRate());
            recorder.setAudioEncodingBitRate(config.getEncodingRate());
        } else {
            //TODO throw exception or log error , alert the recorder was released!
        }
    }

    /**
     * Start recording
     *
     * @param ctx
     */
    public void start(Context ctx) {
        if (isRecording) {
            Logger.e(TAG, "recording is started");
            return;
        }
        recorder.reset();
        configRecorder();
        tempFileUri = Uri.fromFile(new File(ctx.getCacheDir()
                , System.currentTimeMillis() + config.getFileSuffix()));
        if (null != listener) {
            listener.onStart(tempFileUri);
        }
        try {
            recorder.setOutputFile(tempFileUri.getPath());
            recorder.prepare();
            recorder.start();
            isRecording = true;
            startTime = SystemClock
                    .elapsedRealtime();
            Logger.e(TAG, "start time :" + startTime);
        } catch (Exception e) {
            e.printStackTrace();
            isRecording = false;
            if (null != listener) {
                listener.onStartError();
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
            listener.onStop(tempFileUri, duration);
        }
    }

    public void cancel() {
        recorder.stop();
        if (null != listener) {
            listener.onCancel(tempFileUri);
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
