package one.show.live.audio;

/**
 * Created by clarkM1ss1on on 2018/5/6
 */
public class VoiceRecorderConfiguration {
    private int audioSource;
    private int audioChannels;
    private int outputFormat;
    private int audioEncoder;
    private int maxDuration;
    private String fileSuffix;
    private int encodingRate;// = 7950;
    private int sampleRate;// = 8000;

    public VoiceRecorderConfiguration(int audioSource, int audioChannels, int outputFormat
            , int audioEncoder, int maxDuration, String fileSuffix, int encodingRate, int sampleRate) {
        this.audioSource = audioSource;
        this.audioChannels = audioChannels;
        this.outputFormat = outputFormat;
        this.audioEncoder = audioEncoder;
        this.maxDuration = maxDuration;
        this.fileSuffix = fileSuffix;
        this.encodingRate = encodingRate;
        this.sampleRate = sampleRate;
    }

    public int getAudioSource() {
        return audioSource;
    }

    public void setAudioSource(int audioSource) {
        this.audioSource = audioSource;
    }

    public int getAudioChannels() {
        return audioChannels;
    }

    public void setAudioChannels(int audioChannels) {
        this.audioChannels = audioChannels;
    }

    public int getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(int outputFormat) {
        this.outputFormat = outputFormat;
    }

    public int getAudioEncoder() {
        return audioEncoder;
    }

    public void setAudioEncoder(int audioEncoder) {
        this.audioEncoder = audioEncoder;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public String getFileSuffix() {
        return fileSuffix;
    }

    public void setFileSuffix(String fileSuffix) {
        this.fileSuffix = fileSuffix;
    }

    public int getEncodingRate() {
        return encodingRate;
    }

    public void setEncodingRate(int encodingRate) {
        this.encodingRate = encodingRate;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }
}
