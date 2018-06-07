package one.show.live.streamer;

import cn.nodemedia.NodePublisher;

/**
 * Created by clarkM1ss1on on 2018/6/4
 */
public class PublisherConfiguration {

    public final static int ACTION_CONNECTING = 2000;
    public final static int ACTION_CONNECTED = 2001;
    public final static int ACTION_STREAMING_FAILED = 2002;
    public final static int ACTION_STREAMING_ENDED = 2004;
    public final static int ACTION_NETWORK_ANOMALY = 2005;

    public PublisherConfiguration(int videoResolution, int videoFps
            , int videoBitrate, int videoProfile, boolean videoFrontMirror
            , int videoWidth, int videoHeight, int videoMinBitrate, int videoMaxBitrate
            , int keyframeInterval, int audioBitrate, int audioSampleRate, int audioProfile) {

        this.videoResolution = videoResolution;
        this.videoFps = videoFps;
        this.videoBitrate = videoBitrate;
        this.videoProfile = videoProfile;
        this.videoFrontMirror = videoFrontMirror;
        this.keyframeInterval = keyframeInterval;
        this.audioBitrate = audioBitrate;
        this.audioSampleRate = audioSampleRate;
        this.audioProfile = audioProfile;
        this.videoWidth = videoWidth;
        this.videoHeight = videoHeight;
        this.videoMinBitrate = videoMinBitrate;
        this.videoMaxBitrate = videoMaxBitrate;
    }

    private int videoResolution;
    private int videoFps;
    private int videoBitrate;
    private int videoProfile;
    private int videoWidth;
    private int videoHeight;
    private int videoMinBitrate;
    private int videoMaxBitrate;
    private boolean videoFrontMirror;
    private int keyframeInterval;
    private int audioBitrate;
    private int audioSampleRate;
    private int audioProfile;

    public int getVideoResolution() {
        return videoResolution;
    }

    public void setVideoResolution(int videoResolution) {
        this.videoResolution = videoResolution;
    }

    public int getVideoFps() {
        return videoFps;
    }

    public void setVideoFps(int videoFps) {
        this.videoFps = videoFps;
    }

    public int getVideoBitrate() {
        return videoBitrate;
    }

    public void setVideoBitrate(int videoBitrate) {
        this.videoBitrate = videoBitrate;
    }

    public int getVideoProfile() {
        return videoProfile;
    }

    public void setVideoProfile(int videoProfile) {
        this.videoProfile = videoProfile;
    }

    public boolean isVideoFrontMirror() {
        return videoFrontMirror;
    }

    public void setVideoFrontMirror(boolean videoFrontMirror) {
        this.videoFrontMirror = videoFrontMirror;
    }

    public int getKeyframeInterval() {
        return keyframeInterval;
    }

    public void setKeyframeInterval(int keyframeInterval) {
        this.keyframeInterval = keyframeInterval;
    }

    public int getAudioBitrate() {
        return audioBitrate;
    }

    public void setAudioBitrate(int audioBitrate) {
        this.audioBitrate = audioBitrate;
    }

    public int getAudioSampleRate() {
        return audioSampleRate;
    }

    public void setAudioSampleRate(int audioSampleRate) {
        this.audioSampleRate = audioSampleRate;
    }

    public int getAudioProfile() {
        return audioProfile;
    }

    public void setAudioProfile(int audioProfile) {
        this.audioProfile = audioProfile;
    }

    public int getVideoWidth() {
        return videoWidth;
    }

    public void setVideoWidth(int videoWidth) {
        this.videoWidth = videoWidth;
    }

    public int getVideoHeight() {
        return videoHeight;
    }

    public void setVideoHeight(int videoHeight) {
        this.videoHeight = videoHeight;
    }

    public int getVideoMinBitrate() {
        return videoMinBitrate;
    }

    public void setVideoMinBitrate(int videoMinBitrate) {
        this.videoMinBitrate = videoMinBitrate;
    }

    public int getVideoMaxBitrate() {
        return videoMaxBitrate;
    }

    public void setVideoMaxBitrate(int videoMaxBitrate) {
        this.videoMaxBitrate = videoMaxBitrate;
    }

    public static PublisherConfiguration getDefault() {
        return new PublisherConfiguration(NodePublisher.VIDEO_PPRESET_16X9_720
                , 30
                , 500000
                , NodePublisher.VIDEO_PROFILE_HIGH
                , false
                , 720
                , 1280
                , 800
                , 1000
                , 1
                , 32000
                , 44100
                , NodePublisher.AUDIO_PROFILE_HEAAC);
    }
}
