package one.show.live.streamer;
//

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.ImageView;


import com.sve.engine.SVEEngine;

import java.io.File;
import java.nio.ByteBuffer;

import cn.nodemedia.NodePublisher;
import cn.nodemedia.NodePublisherDelegate;
import one.show.live.common.util.Constants;
import one.show.live.log.Logger;

/**
 * Created by clarkM1ss1on on 2018/6/5
 */
public class Streamer implements Camera.PreviewCallback, SVEEngine.onSVImageBufferCallBack {
    //
    public final static int CAMERA_FRONT = 0;
    public final static int CAMERA_BACK = 1;

    private final static String TAG = "Streamer";
    private final static String SCENE_NAME = "streamer";
    private final static String THREAD_NAME = "StreamerHandler";

    public final static int DEFAULT_PREVIEW_WIDTH = 640;
    public final static int DEFAULT_PREVIEW_HEIGHT = 480;

    public final static int BEAUTY_LEVEL_HIGH = 0;
    public final static int BEAUTY_LEVEL_MEDIUM = 1;
    public final static int BEAUTY_LEVEL_MINIMUM = 2;

    private NodePublisher publisher;

    private ShowCamera sveCamera;

    private boolean isEnginePrepared = false;
    private volatile boolean isStreaming = false;

    private boolean isPreviewStarted = false;

    private final static int BUFFER_RADIOS = 4;


    private PublisherConfiguration config;
    private ImageView debugPreview;
    private SVEEngine engine;

    private int cameraDir = CAMERA_FRONT;
    private int previewWidth, previewHeight;

    private HandlerThread pushThread;
    private Handler pushHandler;
    //    private Handler engineSettingHandler;
    private int beautyLevel = 0;//max 100
    private int whiteningLevel = 0;//max 50


    public Streamer(Context context) {
        publisher = new NodePublisher(context.getApplicationContext(), Constants.NODEMEDIA_SIGNKEY);
        config = PublisherConfiguration
                .getDefault();
        initPushThread();
        initEngine();
        initPublisher();

    }


    private void initPublisher() {
        publisher.setPublishType(NodePublisher.PUBLISH_TYPE_LIVE);
        publisher.setRawVideoParam(NodePublisher.NM_PIXEL_RGBA
                , config.getVideoWidth()
                , config.getVideoHeight()
                , config.getVideoResolution()
                , config.getVideoFps()
                , config.getVideoBitrate());
        publisher.setAudioParam(config.getAudioBitrate()
                , config.getAudioProfile()
                , config.getAudioSampleRate());
        publisher.setConnectWaitTimeout(-1);
        publisher.setPublishType(NodePublisher.PUBLISH_TYPE_LIVE);
    }


    private void initPushThread() {
        pushThread = new HandlerThread(THREAD_NAME);
        pushThread.start();
        pushHandler = new Handler(pushThread.getLooper());
//        engineSettingHandler = new Handler(Looper.getMainLooper());
    }

    private void initEngine() {

//        engineSettingHandler
//                .post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        engine.resume();
//                    }
//                });


    }

    public boolean isPreviewStarted() {
        return isPreviewStarted;
    }

    public void noticeRenderPrepared(final SurfaceTexture surfaceTexture, final int width, final int height) {

//        engineSettingHandler
//                .post(new Runnable() {
//                    @Override
//                    public void run() {
        previewWidth = width;
        previewHeight = height;
        isEnginePrepared = true;
        engine = SVEEngine.getinstance();
        engine.init(Environment.getExternalStorageDirectory() + ShaderHelper.SHADER_PATH + ShaderHelper.SHADER_DIR_NAME + File.separator
                , DEFAULT_PREVIEW_HEIGHT, DEFAULT_PREVIEW_WIDTH, DEFAULT_PREVIEW_WIDTH, DEFAULT_PREVIEW_HEIGHT);
        engine.setCallBackImagBuffer(Streamer.this);
        engine.resume();
        engine.setSurfaceTextureDraw(surfaceTexture);

        engine.openFaceBeauty(EnginePerformanceAdapter.getBeautyLevelByModel());
        engine.updateFilter(SVEEngine.FILTER_TYPE.SVI_ACUTANCE_FILTER, 0);
        engine.updateFilter(SVEEngine.FILTER_TYPE.SVI_CONTRAST_FILTER, 20);
        applyBeauty();
        applyWhitening();
//                    }
//                });

//        resumeEngine(surfaceTexture);
    }

    private void applyBeauty() {
        engine.updateFilter(SVEEngine.FILTER_TYPE.SVI_EBEAUTY_FILTER, beautyLevel);
    }

    private void applyWhitening() {
        engine.updateFilter(SVEEngine.FILTER_TYPE.SVI_WHITENING_FILTER, whiteningLevel);
    }


    public void updateWhitening(int val) {
        whiteningLevel = val;
        if (isEnginePrepared) {
            applyWhitening();
        }
    }

    public void updateBeauty(int val) {
        beautyLevel = val;
        if (isEnginePrepared) {
            applyBeauty();
        }
    }

    public int getBeautyLevel() {
        return beautyLevel;
    }

    public int getWhiteningLevel() {
        return whiteningLevel;
    }

    //
    public void setPushingParams(int fps, int width, int height, int minBitrate, int maxBitrate) {
        config.setVideoFps(fps);
        config.setVideoWidth(DEFAULT_PREVIEW_HEIGHT);
        config.setVideoHeight(DEFAULT_PREVIEW_WIDTH);
        config.setVideoMinBitrate(minBitrate);
        config.setVideoMaxBitrate(maxBitrate);
        updatePublisherSettings();
    }

    private void updatePublisherSettings() {
        publisher.setRawVideoParam(NodePublisher.NM_PIXEL_RGBA
                , config.getVideoWidth()
                , config.getVideoHeight()
                , config.getVideoResolution()
                , config.getVideoFps()
                , config.getVideoBitrate());
    }


    public void pauseEngine() {

//        engineSettingHandler
//                .post(new Runnable() {
//                    @Override
//                    public void run() {
        Logger.e("engineRelated", "pauseEngine");
//                        engine.destorySuface();
        engine.suspend();
//                    }
//                });
    }

    public void resumeEngine(final SurfaceTexture surfaceTexture) {

//        engineSettingHandler
//                .post(new Runnable() {
//                    @Override
//                    public void run() {
        Logger.e("engineRelated", "resumeEngine");
        engine.setCallBackImagBuffer(Streamer.this);
        engine.resume();
        engine.setSurfaceTextureDraw(surfaceTexture);

//                    }
//                });
    }

    public void startCamera(final Activity activity, final int camera) {
        Logger.e("engineRelated", "startCamera");
        if (isPreviewStarted) {
            return;
        }
        if (sveCamera == null) {
            try {
                cameraDir = camera;
                boolean isFront = cameraDir == CAMERA_FRONT;
                sveCamera = new ShowCamera(activity);
                sveCamera.setPreviewSize(DEFAULT_PREVIEW_WIDTH, DEFAULT_PREVIEW_HEIGHT);
                sveCamera.open(ImageFormat.NV21, isFront);
                sveCamera.start();
                isPreviewStarted = true;
                sveCamera.registerCameraPreviewListener(this);
            } catch (Exception e) {
                e.printStackTrace();
                Logger.e("engineRelated", "camera exception");
            }
        }
        Logger.e(TAG, "startCamera");
    }


    public void stopCamera() {
        Logger.e("engineRelated", "stopCamera");
        if (sveCamera != null) {
            sveCamera.unRegisterCameraPreviewListener();
            sveCamera.release();
            sveCamera = null;
            isPreviewStarted = false;
        }

        isEnginePrepared = false;

        Logger.e(TAG, "stopCamera");
    }

    public void noticeCameraChanged(final Activity activity, final int camera) {

        if (isPreviewStarted) {
            stopCamera();
            startCamera(activity, camera);
        }
    }


    public int getCurrentCamera() {
        return cameraDir;
    }


    public void setPublisherDelegate(NodePublisherDelegate delegate) {
        publisher.setNodePublisherDelegate(delegate);
    }

    public void startStreamer(String url) {
        isStreaming = true;
        publisher.setOutputUrl(url);
        publisher.start();
        Logger.e(TAG, "streaming start");
    }

    public void stopStreamer() {
        publisher.stop();
        isStreaming = false;
        Logger.e(TAG, "streaming stop");
    }


    public void release() {
        publisher.release();
        pushThread.quitSafely();
    }

    @Override
    public void onPreviewFrame(final byte[] data, Camera camera) {

        Camera.Parameters param = camera.getParameters();
        Camera.Size tSize = param.getPreviewSize();
        final int mCameraHeight = tSize.height;
        final int mCameraWidth = tSize.width;

        final boolean isFront = cameraDir == CAMERA_FRONT;
        if (engine != null) {
//            engine.resume();

            final int angle;
            if (cameraDir == CAMERA_FRONT) {
                angle = 90;
            } else {
                angle = 270;
            }

//            engineSettingHandler
//                    .post(new Runnable() {
//                        @Override
//                        public void run() {
            engine.pushSteam(data, 0, mCameraWidth, mCameraHeight, isFront, angle);
            drawTexThroughEngine(previewWidth, previewHeight, mCameraWidth, mCameraHeight);
//                        }
//                    });

        }
        camera.addCallbackBuffer(data);
    }

    @Override
    public void onSVBufferCallBack(ByteBuffer byteBuffer, int width, int height, int stride) {
        //TODO out put
        if (isStreaming) {
            int size = stride * height * 4;
            byte[] outData = new byte[size];
            byteBuffer.position(0);
            byteBuffer.get(outData);
            noticeToPushData(outData.clone(), size);
        }
    }


    public void setDebugPreviewImage(ImageView imgView) {
        this.debugPreview = imgView;
    }

    private void drawTexThroughEngine(int previewWidth, int previewHeight
            , int cameraWidth, int cameraHeight) {
        float previewAspect = (float) previewWidth / previewHeight;
        float cameraAspect = (float) cameraHeight / cameraWidth;
        if (engine != null) {
            if (previewAspect < cameraAspect) {
                engine.drawTex(previewHeight * cameraHeight / cameraWidth, previewHeight);
            } else {
                engine.drawTex(previewWidth, previewWidth * cameraWidth / cameraHeight);
            }
        }
    }

    private void noticeToPushData(final byte[] pushData, final int size) {

//        pushHandler.post(new Runnable() {
//            @Override
//            public void run() {
        publisher.pushRawvideo(pushData, size);
//            }
//        });

    }
}
