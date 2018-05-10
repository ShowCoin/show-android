package one.show.live.audio;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.File;

import one.show.live.log.Logger;

/**
 * For playing voice msg
 * Created by clarkM1ss1on on 2018/5/8
 */
public class VoicePlayer implements MediaPlayer.OnPreparedListener {

    private final static String TAG = "VoicePlayer";
    private MediaPlayer mediaPlayer;
    private AudioManager audioManager;
    private IVoicePlayerInfoListener listener;
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private String path;


    /**
     * @param ctx
     * @param listener use {@link IVoicePlayerInfoListener} listen the changing of playing status, which to update ui
     */
    public void attach(Context ctx, final IVoicePlayerInfoListener listener) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnPreparedListener(this);
        audioManager = (AudioManager) ctx
                .getSystemService(Context.AUDIO_SERVICE);
        this.listener = listener;
        audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
            @Override
            public void onAudioFocusChange(int focusChange) {
                switch (focusChange) {
                    case AudioManager.AUDIOFOCUS_LOSS:
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                        stop();
                        break;
                }
            }
        };

    }

    /**
     * Start with request audio focus and will stop and reset if last voice msg playing.
     *
     * @param path
     */
    public void startWithRequestAudioFocus(final String path) {
        this.path = path;
        final int result = audioManager.requestAudioFocus(audioFocusChangeListener
                , AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            startWhenPrepared();
        } else {
            Logger.e(TAG, "request audio focus failed!");
        }
    }


    private void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        audioManager.abandonAudioFocus(audioFocusChangeListener);
        if (null != listener) {
            listener.onPlayerStop();
        }
    }

    private void startWhenPrepared() {

        if (mediaPlayer.isPlaying()) {
            stop();
        }
        if (!new File(path).exists()) {
            Logger.e(TAG, "voice file not exist!");
            return;
        }

        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
            stop();
            if (null != listener) {
                listener.onPlayerError();
            }
        }
    }

    /**
     * Release media player, that will stop the playing voice and invoke the method {@link IVoicePlayerInfoListener#onPlayerStop()}
     */
    public void release() {
        if (null == mediaPlayer) {
            return;
        }
        if (mediaPlayer.isPlaying()) {
            stop();
        }
        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

}
