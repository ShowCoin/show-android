package org.wwtx.uidebugtable.streamer;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.ksyun.media.streamer.capture.AudioCapture;
import com.ksyun.media.streamer.capture.AudioPlayerCapture;
import com.ksyun.media.streamer.capture.CameraCapture;
import com.ksyun.media.streamer.capture.ImageCapture;
import com.ksyun.media.streamer.capture.WaterMarkCapture;
import com.ksyun.media.streamer.encoder.AVCodecAudioEncoder;
import com.ksyun.media.streamer.encoder.AudioEncoderMgt;
import com.ksyun.media.streamer.encoder.Encoder;
import com.ksyun.media.streamer.encoder.MediaCodecAudioEncoder;
import com.ksyun.media.streamer.encoder.VideoEncoderMgt;
import com.ksyun.media.streamer.filter.audio.AudioAPMFilterMgt;
import com.ksyun.media.streamer.filter.audio.AudioFilterMgt;
import com.ksyun.media.streamer.filter.audio.AudioMixer;
import com.ksyun.media.streamer.filter.audio.AudioPreview;
import com.ksyun.media.streamer.filter.audio.AudioResampleFilter;
import com.ksyun.media.streamer.filter.imgtex.ImgTexFilterMgt;
import com.ksyun.media.streamer.filter.imgtex.ImgTexMixer;
import com.ksyun.media.streamer.filter.imgtex.ImgTexPreview;
import com.ksyun.media.streamer.filter.imgtex.ImgTexScaleFilter;
import com.ksyun.media.streamer.kit.KSYStreamer;
import com.ksyun.media.streamer.publisher.FilePublisher;
import com.ksyun.media.streamer.publisher.Publisher;
import com.ksyun.media.streamer.publisher.PublisherMgt;
import com.ksyun.media.streamer.publisher.RtmpPublisher;
import com.ksyun.media.streamer.util.gles.GLRender;

/**
 * Created by clarkM1ss1on on 2018/5/28
 */
public class StreamerExt extends KSYStreamer {


    private OnInfoListener mOnInfoListener;
    private OnErrorListener mOnErrorListener;
    private Handler mMainHandler;
    private final Object mReleaseObject = new Object();

    public StreamerExt(Context context) {
        super(context);
        if (null != context) {
            mMainHandler = new Handler(Looper.getMainLooper());
        }
    }

    @Override
    protected void initModules() {
        this.mGLRender = new GLRender();
        this.mWaterMarkCapture = new WaterMarkCapture(this.mGLRender);
        this.mImageCapture = new ImageCapture(this.mGLRender);
        this.mCameraCapture = new CameraCapture(this.mContext, this.mGLRender);
        this.mImgTexScaleFilter = new ImgTexScaleFilter(this.mGLRender);
        this.mImgTexFilterMgt = new ImgTexFilterMgt(this.mContext);
        this.mImgTexMixer = new ImgTexMixer(this.mGLRender);
        this.mImgTexMixer.setScalingMode(this.mIdxCamera, 2);
        this.mImgTexPreviewMixer = new ImgTexMixer(this.mGLRender);
        this.mImgTexPreviewMixer.setScalingMode(this.mIdxCamera, 2);
        this.mImgTexPreview = new ImgTexPreview();
        this.mCameraCapture.mImgTexSrcPin.connect(this.mImgTexScaleFilter.getSinkPin());
        //TODO construct pipeline connection adapt to fm engine
        this.mImgTexScaleFilter.getSrcPin().connect(this.mImgTexFilterMgt.getSinkPin());
        this.mImgTexFilterMgt.getSrcPin().connect(this.mImgTexMixer.getSinkPin(this.mIdxCamera));
        this.mWaterMarkCapture.mLogoTexSrcPin.connect(this.mImgTexMixer.getSinkPin(this.mIdxWmLogo));
        this.mWaterMarkCapture.mTimeTexSrcPin.connect(this.mImgTexMixer.getSinkPin(this.mIdxWmTime));
        this.mImgTexFilterMgt.getSrcPin().connect(this.mImgTexPreviewMixer.getSinkPin(this.mIdxCamera));
        this.mWaterMarkCapture.mLogoTexSrcPin.connect(this.mImgTexPreviewMixer.getSinkPin(this.mIdxWmLogo));
        this.mWaterMarkCapture.mTimeTexSrcPin.connect(this.mImgTexPreviewMixer.getSinkPin(this.mIdxWmTime));
        this.mImgTexPreviewMixer.getSrcPin().connect(this.mImgTexPreview.getSinkPin());
        this.mAudioPlayerCapture = new AudioPlayerCapture(this.mContext);
        this.mAudioCapture = new AudioCapture(this.mContext);
        this.mAudioCapture.setAudioCaptureType(this.mAudioCaptureType);
        this.mAudioFilterMgt = new AudioFilterMgt();
        this.mAudioPreview = new AudioPreview(this.mContext);
        this.mAudioResampleFilter = new AudioResampleFilter();
        this.mAudioMixer = new AudioMixer();
        this.mAudioAPMFilterMgt = new AudioAPMFilterMgt();
        this.mAudioCapture.getSrcPin().connect(this.mAudioFilterMgt.getSinkPin());
        this.mAudioFilterMgt.getSrcPin().connect(this.mAudioPreview.getSinkPin());
        this.mAudioPreview.getSrcPin().connect(this.mAudioResampleFilter.getSinkPin());
        this.mAudioResampleFilter.getSrcPin().connect(this.mAudioMixer.getSinkPin(this.mIdxAudioMic));
        if (this.mEnableAudioMix) {
            this.mAudioPlayerCapture.getSrcPin().connect(this.mAudioMixer.getSinkPin(this.mIdxAudioBgm));
        }

        this.mVideoEncoderMgt = new VideoEncoderMgt(this.mGLRender);
        this.mAudioEncoderMgt = new AudioEncoderMgt();
        this.mWaterMarkCapture.mLogoBufSrcPin.connect(this.mVideoEncoderMgt.getImgBufMixer().getSinkPin(this.mIdxWmLogo));
        this.mWaterMarkCapture.mTimeBufSrcPin.connect(this.mVideoEncoderMgt.getImgBufMixer().getSinkPin(this.mIdxWmTime));
        this.mImgTexMixer.getSrcPin().connect(this.mVideoEncoderMgt.getImgTexSinkPin());
        this.mCameraCapture.mImgBufSrcPin.connect(this.mVideoEncoderMgt.getImgBufSinkPin());
        this.mAudioMixer.getSrcPin().connect(this.mAudioEncoderMgt.getSinkPin());
        this.mRtmpPublisher = new RtmpPublisher();
        this.mFilePublisher = new FilePublisher();
        this.mFilePublisher.setAutoWork(true);
        this.mPublisherMgt = new PublisherMgt();
        this.mAudioEncoderMgt.getSrcPin().connect(this.mPublisherMgt.getAudioSink());
        this.mVideoEncoderMgt.getSrcPin().connect(this.mPublisherMgt.getVideoSink());
        this.mPublisherMgt.addPublisher(this.mRtmpPublisher);
        this.mGLRender.addListener(new GLRender.b() {
            public void a() {
                mImgTexPreview.setEGL10Context(mGLRender.getEGL10Context());
            }
        });
        this.mAudioCapture.setAudioCaptureListener(new AudioCapture.OnAudioCaptureListener() {
            public void onStatusChanged(int var1) {
            }

            public void onError(int var1) {
                Log.e("KSYStreamer", "AudioCapture error: " + var1);
                short var2;
                switch (var1) {
                    case -2005:
                    default:
                        var2 = -2005;
                        break;
                    case -2003:
                        var2 = -2003;
                }

                if (mOnErrorListener != null) {
                    mOnErrorListener.onError(var2, 0, 0);
                }

            }
        });
        this.mCameraCapture.setOnCameraCaptureListener(new CameraCapture.OnCameraCaptureListener() {
            public void onStarted() {
                Log.d("KSYStreamer", "CameraCapture ready");
                if (mOnInfoListener != null) {
                    mOnInfoListener.onInfo(1000, 0, 0);
                }

            }

            public void onFacingChanged(int var1) {
                mCameraFacing = var1;
                updateFrontMirror();
                if (mOnInfoListener != null) {
                    mOnInfoListener.onInfo(1002, var1, 0);
                }

            }

            public void onError(int var1) {
                Log.e("KSYStreamer", "CameraCapture error: " + var1);
                short var2;
                switch (var1) {
                    case -2007:
                        var2 = -2007;
                        break;
                    case -2006:
                        var2 = -2006;
                        break;
                    case -2005:
                    case -2004:
                    case -2003:
                    case -2001:
                    default:
                        var2 = -2001;
                        break;
                    case -2002:
                        var2 = -2002;
                }

                if (mOnErrorListener != null) {
                    mOnErrorListener.onError(var2, 0, 0);
                }

            }
        });
        Encoder.EncoderListener var1 = new Encoder.EncoderListener() {
            public void onError(Encoder var1, int var2) {
                if (var2 != 0) {
                    stopStream();
                }

                boolean var3 = true;
                if (var1 instanceof MediaCodecAudioEncoder || var1 instanceof AVCodecAudioEncoder) {
                    var3 = false;
                }

                int var4;
                switch (var2) {
                    case -1002:
                        var4 = var3 ? -1004 : -1008;
                        break;
                    case -1001:
                    default:
                        var4 = var3 ? -1003 : -1011;
                }

                if (mOnErrorListener != null) {
                    mOnErrorListener.onError(var4, 0, 0);
                }

            }
        };
        this.mVideoEncoderMgt.setEncoderListener(var1);
        this.mAudioEncoderMgt.setEncoderListener(var1);
        this.mRtmpPublisher.setPubListener(new Publisher.PubListener() {
            public void onInfo(int var1, long var2) {
                switch (var1) {
                    case 1:
                        if (!mAudioEncoderMgt.getEncoder().isEncoding()) {
                            mAudioEncoderMgt.getEncoder().start();
                        } else if (!mRtmpPublisher.isAudioExtraGot()) {
                            mRtmpPublisher.setAudioExtra(mAudioEncoderMgt.getEncoder().getExtra());
                        }

                        if (mOnInfoListener != null) {
                            mOnInfoListener.onInfo(0, 0, 0);
                        }
                        break;
                    case 2:
                        if (!mIsAudioOnly) {
                            if (!mVideoEncoderMgt.getEncoder().isEncoding()) {
                                mVideoEncoderMgt.start();
                            } else if (!mRtmpPublisher.isVideoExtraGot()) {
                                mRtmpPublisher.setVideoExtra(mVideoEncoderMgt.getEncoder().getExtra());
                                mVideoEncoderMgt.getEncoder().forceKeyFrame();
                            }
                        }
                        break;
                    case 100:
                        Log.i("KSYStreamer", "packet send slow, delayed " + var2 + "ms");
                        if (mOnInfoListener != null) {
                            mOnInfoListener.onInfo(3001, (int) var2, 0);
                        }
                        break;
                    case 101:
                        if (!mIsAudioOnly && mAutoAdjustVideoBitrate) {
                            var2 -= (long) mAudioBitrate;
                            var2 = Math.min(var2, (long) mMaxVideoBitrate);
                            Log.d("KSYStreamer", "Raise video bitrate to " + var2);
                            mVideoEncoderMgt.getEncoder().adjustBitrate((int) var2);
                            if (mOnInfoListener != null) {
                                mOnInfoListener.onInfo(3002, (int) var2, 0);
                            }
                        }
                        break;
                    case 102:
                        if (!mIsAudioOnly && mAutoAdjustVideoBitrate) {
                            var2 -= (long) mAudioBitrate;
                            var2 = Math.max(var2, (long) mMinVideoBitrate);
                            Log.d("KSYStreamer", "Drop video bitrate to " + var2);
                            mVideoEncoderMgt.getEncoder().adjustBitrate((int) var2);
                            if (mOnInfoListener != null) {
                                mOnInfoListener.onInfo(3003, (int) var2, 0);
                            }
                        }
                }

            }

            public void onError(int var1, long var2) {
                Log.e("KSYStreamer", "RtmpPub err=" + var1);
                if (var1 != 0) {
                    stopStream();
                }

                if (mOnErrorListener != null) {
                    int var4 = var1;
                    switch (var1) {
                        case -3020:
                            var4 = -1007;
                            break;
                        case -3012:
                            var4 = -1010;
                            break;
                        case -3011:
                            var4 = -1006;
                            break;
                        case -3010:
                            var4 = -1009;
                            break;
                        case -2004:
                            var4 = -2004;
                    }

                    mOnErrorListener.onError(var4, (int) var2, 0);

                    //TODO invoke autoRestart() method
                    autoRestart();
                }

            }
        });

        this.mFilePublisher.setPubListener(new Publisher.PubListener() {
            public void onInfo(int var1, long var2) {
                Log.d("KSYStreamer", "file publisher info:" + var1);
                switch (var1) {
                    case 1:
                        if (!mAudioEncoderMgt.getEncoder().isEncoding()) {
                            mAudioEncoderMgt.getEncoder().start();
                        } else if (!mFilePublisher.isAudioExtraGot()) {
                            mFilePublisher.setAudioExtra(mAudioEncoderMgt.getEncoder().getExtra());
                        }

                        if (mOnInfoListener != null) {
                            mOnInfoListener.onInfo(1, 0, 0);
                        }
                        break;
                    case 2:
                        if (!mIsAudioOnly) {
                            if (!mVideoEncoderMgt.getEncoder().isEncoding()) {
                                mVideoEncoderMgt.start();
                            } else if (!mFilePublisher.isVideoExtraGot()) {
                                mFilePublisher.setVideoExtra(mVideoEncoderMgt.getEncoder().getExtra());
                                mVideoEncoderMgt.getEncoder().forceKeyFrame();
                            }
                        }
                    case 3:
                    default:
                        break;
                    case 4:
                        mPublisherMgt.removePublisher(mFilePublisher);
                        mIsFileRecording = false;
                        if (mOnInfoListener != null) {
                            mOnInfoListener.onInfo(2, 0, 0);
                        }
                }

            }

            public void onError(int var1, long var2) {
                Log.e("KSYStreamer", "FilePublisher err=" + var1);
                if (var1 != 0) {
                    stopRecord();
                }

                if (mOnErrorListener != null) {
                    short var4;
                    switch (var1) {
                        case -4004:
                            var4 = -4004;
                            break;
                        case -4003:
                            var4 = -4003;
                            break;
                        case -4002:
                            var4 = -4002;
                            break;
                        case -4001:
                            var4 = -4001;
                            break;
                        default:
                            var4 = -4000;
                    }

                    mOnErrorListener.onError(var4, (int) var2, 0);
                }

            }
        });
        this.mGLRender.init(1, 1);
        if (this.mContext != null) {
            AudioManager var2 = (AudioManager) this.mContext.getSystemService(Context.AUDIO_SERVICE);
            this.mHeadSetPluged = var2.isWiredHeadsetOn();
            this.mBluetoothPluged = var2.isBluetoothA2dpOn();
        }


    }


    private void autoRestart() {
        if (this.mAutoRestart && this.mMainHandler != null) {
            this.stopStream();
            this.mMainHandler.postDelayed(new Runnable() {
                public void run() {
                    synchronized (mReleaseObject) {
                        if (mMainHandler != null) {
                            startStream();
                        }

                    }
                }
            }, (long) this.mAutoRestartInterval);
        }
    }


    @Override
    public void release() {
        if (null != mMainHandler) {
            this.mMainHandler.removeCallbacksAndMessages(null);
        }
        super.release();
    }

    @Override
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    @Override
    public void setOnInfoListener(OnInfoListener onInfoListener) {
        this.mOnInfoListener = onInfoListener;
    }
}
