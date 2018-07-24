package one.show.live.streamer;

import android.graphics.SurfaceTexture;
import android.os.Environment;

import com.sve.engine.SVEEngine;

import java.io.File;

/**
 * Created by ClarkMission 2018/7/20
 */
public enum EngineDelegate {
    INSTANCE;

    public final static int DEFAULT_PREVIEW_WIDTH = 640;
    public final static int DEFAULT_PREVIEW_HEIGHT = 480;


    public final static int MAX_SKIN_SMOOTH = 100;
    public final static int MAX_WHITENING = 50;

    private SVEEngine engine;

    private int skinSmoothLevel; //max 100
    private int whiteningLevel; //max 50

    public void init() {
        engine = SVEEngine
                .getinstance();
        engine.init(Environment.getExternalStorageDirectory() + ShaderHelper.SHADER_PATH + ShaderHelper.SHADER_DIR_NAME + File.separator
                , DEFAULT_PREVIEW_HEIGHT, DEFAULT_PREVIEW_WIDTH, DEFAULT_PREVIEW_WIDTH, DEFAULT_PREVIEW_HEIGHT);
    }

    public EngineDelegate setImageBufferCallback(SVEEngine.onSVImageBufferCallBack callback) {
        engine.setCallBackImagBuffer(callback);
        return this;
    }

    public EngineDelegate pauseEngine() {
        engine.suspend();
        return this;
    }

    public EngineDelegate resumeEngine(SurfaceTexture surfaceTexture) {
        engine.resume();
        engine.setSurfaceTextureDraw(surfaceTexture);
        return this;
    }


    public EngineDelegate setSkinSmooth(int skinSmooth) {
        this.skinSmoothLevel = skinSmooth;
        applySkinSmooth();
        return this;
    }

    public EngineDelegate setWhitening(int whitening) {
        this.whiteningLevel = whitening;
        applyWhitening();
        return this;
    }

    private EngineDelegate applySkinSmooth() {
        engine.updateFilter(SVEEngine.FILTER_TYPE.SVI_EBEAUTY_FILTER, skinSmoothLevel);
        //TODO apply settings if engine prepared
        return this;
    }

    private EngineDelegate applyWhitening() {
        engine.updateFilter(SVEEngine.FILTER_TYPE.SVI_WHITENING_FILTER, whiteningLevel);
        //TODO apply settings if engine prepared
        return this;
    }

    public EngineDelegate startBeautyLevel(int level) {
        engine.openFaceBeauty(level);
        engine.updateFilter(SVEEngine.FILTER_TYPE.SVI_ACUTANCE_FILTER, 0);
        engine.updateFilter(SVEEngine.FILTER_TYPE.SVI_CONTRAST_FILTER, 20);
        return this;
    }

    public EngineDelegate closeBeauty() {
        engine.closeFaceBeauty();
        return this;
    }

    public EngineDelegate releaseEngine() {
        engine.destorySuface();
        engine.suspend();
        return this;
    }

    public EngineDelegate onCameraInput(byte[] data, int cameraWidth, int cameraHeight
            , int previewWidth, int previewHeight, int cameraDirection) {
        boolean isFront;
        int angle;
        if (cameraDirection == Streamer.CAMERA_FRONT) {
            isFront = true;
            angle = 90;
        } else {
            isFront = false;
            angle = 270;
        }
        engine.pushSteam(data, 0, cameraWidth, cameraHeight, isFront, angle);
        drawTexThroughEngine(previewWidth, previewHeight, cameraWidth, cameraHeight);
        return this;
    }


    private EngineDelegate drawTexThroughEngine(int previewWidth, int previewHeight
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
        return this;
    }

    public int getSkinSmoothLevel() {
        return skinSmoothLevel;
    }

    public int getWhiteningLevel() {
        return whiteningLevel;
    }
}
