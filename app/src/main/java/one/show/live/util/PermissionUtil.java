package one.show.live.util;

import android.Manifest;
import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;

import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.IOException;

import rx.functions.Action1;
import one.show.live.view.ToastUtils;

/**
 * Created by seeu on 2018/1/5.
 * 拍照前获取权限
 */

public class PermissionUtil {

    public static boolean isAndroidVersonCode19(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return true;
        }

        return false;
    }

    public static boolean isAndroidVersonCode23(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return true;
        }

        return false;
    }

    public static boolean isAndroidVersonCode26(){
        if (Build.VERSION.SDK_INT >= 26) {
            return true;
        }

        return false;
    }

    public static void clickCameraRecord(Activity activity,final CallLisenter callLisenter) {
//        if (!isAndroidVersonCode19()){
//            ToastUtils.showToast("录制功能仅支持4.4系统以上机型");
//            return;
//        }
//        if (isAndroidVersonCode26()) {
//            ToastUtils.showToast("录制功能暂不支持8.0系统以上机型");
//            return;
//        }

        if (!isAndroidVersonCode23() && isAndroidVersonCode19()){
            boolean isGranted = getAudioPemission();
            if (isGranted){
                if(callLisenter!=null){
                    callLisenter.onCall();
                }
            } else {
                ToastUtils.showToast("请到设置中打开相机和录音权限");
            }
            return;
        }


        new RxPermissions(activity)
                .request(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean granted) {
                        if (granted) {
                            if(callLisenter!=null){
                                callLisenter.onCall();
                            }
                        } else {

                        }
                    }
                });
    }


    public static boolean getAudioPemission(){
        MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            // audioFile = File.createTempFile("recording", ".3gp", filePath);
            String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/audio.3gb";
            mediaRecorder.setOutputFile(filePath);
            mediaRecorder.prepare();
            mediaRecorder.release();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public interface CallLisenter{
        void onCall();
    }

}
