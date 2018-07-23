package one.show.live.streamer;

import android.content.Context;
import android.os.Environment;

import one.show.live.log.Logger;

/**
 * Created by ClarkMission 2018/7/10
 */
public enum ShaderHelper {
    INSTANCE;

    public final static String SHADER_PATH = "/show/res/";

    public final static String SHADER_DIR_NAME = "sve.bundle";

    private final static String TAG = "ShaderHelper";

    public void copyShaderFiles(Context context, ShaderFileUtils.FileOperateCallback callback) {
        ShaderFileUtils
                .getInstance(context)
                .copyAssetsToSD(SHADER_DIR_NAME, SHADER_PATH + SHADER_DIR_NAME)
                .setFileOperateCallback(callback);
    }
}
